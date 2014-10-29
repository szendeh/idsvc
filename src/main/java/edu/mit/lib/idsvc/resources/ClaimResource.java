/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.resources;

import java.sql.Timestamp;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import edu.mit.lib.idsvc.api.Claim;
import edu.mit.lib.idsvc.api.Identifier;
import edu.mit.lib.idsvc.api.Name;
import edu.mit.lib.idsvc.api.Person;
import edu.mit.lib.idsvc.api.Work;
import edu.mit.lib.idsvc.db.IdentifierDAO;
import edu.mit.lib.idsvc.db.NameDAO;
import edu.mit.lib.idsvc.db.PersonDAO;
import edu.mit.lib.idsvc.db.WorkDAO;

/**
 * Resource class for claims - which are the atomic transactional units
 * of the service. There are only 2 operations defined for claims:
 * 'make' - modeled as an Http 'put' so that it can be idempotent.
 * Making a claim will result in the creation of persons, works, etc
 * 'retract' -  modeled as an Http 'delete' for similar reasons.
 * Retracting a claim may result in deletion of persons, works, etc
 * 
 * @author richardrodgers
 */

@Path("/claim/{personId}")
@Produces(MediaType.APPLICATION_JSON)
public class ClaimResource {

    private final PersonDAO personDao;
    private final IdentifierDAO identifierDao;
    private final NameDAO nameDao;
    private final WorkDAO workDao;

    public ClaimResource(PersonDAO personDao, IdentifierDAO identifierDao, NameDAO nameDao, WorkDAO workDao) {
        this.personDao = personDao;
        this.identifierDao = identifierDao;
        this.nameDao = nameDao;
        this.workDao = workDao;
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response make(Claim claim) {
        // update the model with the claim
        // For now, only identifier we are accepting is MIT ID
        boolean knownPerson = false;
        Person person = null;
        Identifier identifier = identifierDao.findByIdentifier("mitid", claim.getIdentifier());
        if (identifier == null) {
            // we have no record of this - create a new person and link it to this identifier
            int personId = personDao.create(new Timestamp(System.currentTimeMillis()));
            person = personDao.findById(personId);
            identifierDao.create(personId, "mitid", claim.getIdentifier());
        } else {
           person = personDao.findById(identifier.getPersonId());
           knownPerson = true;
        }
        // next up, the work - create if new to model, then link to person if needed
        boolean knownWork = true;
        Work work = workDao.findByRef("cnri", claim.getWork());
        if (work == null) {
            // create a new work
            int workId = workDao.create("cnri", claim.getWork());
            work = workDao.findById(workId);
            knownWork = false;
        }
        // RLR - this is an inefficient test - should be rewritten
        List<Work> linkWorks = personDao.linksToWork(work.getId(), person.getId());
        if (linkWorks.size() == 0) {
            personDao.linkWork(work.getId(), person.getId());
        } else if (knownPerson && knownWork) {
            // model already contains both the person and the work & they are linked
            // return immediately since claim has already been been made
            return Response.ok().build();
        }
        // finally, the name as it appeared in the work. Essentially same treatment, but doubly linked
        Name name = nameDao.findByName(claim.getName());
        if (name == null) {
            // create a new Name
            int nameId = nameDao.create(claim.getName());
            name = nameDao.findById(nameId);
        }
        List<Name> linkNames = personDao.linksToName(name.getId(), person.getId());
        if (linkNames.size() == 0) {
            personDao.linkName(name.getId(), person.getId());
        }

        List<Name> nameLinkWorks = workDao.linksToName(name.getId(), work.getId());
        if (nameLinkWorks.size() == 0) {
            workDao.linkName(name.getId(), work.getId());
        }
        return Response.ok().build();
    }

    @DELETE
    public Response retract(@PathParam("personId") String personId, @QueryParam("wid") String wid) {
        // For now, only identifier we recognize is MIT ID
        Identifier identifier = identifierDao.findByIdentifier("mitid", personId);
        if (identifier != null) {
            Work work = workDao.findByRef("cnri", wid);
            if (work != null) {
                Person person = personDao.findById(identifier.getPersonId());
                // Is this this person linked to the work?
                 List<Work> linkedWorks = personDao.linksToWork(work.getId(), person.getId());
                 if (linkedWorks.size() != 0) {
                     // yes - so proceed to do the retraction
                     personDao.unlinkWork(work.getId(), person.getId());
                     // same for names - both on the person and works side
                     Name name = nameDao.appearsAsInWork(person.getId(), work.getId());
                     List<Name> personLinkedNames = personDao.linksToName(name.getId(), person.getId());
                     if (personLinkedNames.size() != 0) {
                         personDao.unlinkName(name.getId(), person.getId());
                     }
                     List<Name> workLinkedNames = workDao.linksToName(name.getId(), work.getId());
                     if (workLinkedNames.size() != 0) {
                         workDao.unlinkName(name.getId(), work.getId());
                     }
                     // now consider whether person is bereft of works, if so delete her, her identifiers, etc
                     List<Work> allWorks = personDao.worksBy(person.getId());
                     if (allWorks.size() == 0) {
                         identifierDao.removeIdentifiersOf(person.getId());
                         personDao.remove(person.getId());
                     }
                     // also consider whether work now has no authors, if so delete it
                     List<Person> authors = workDao.authorsOf(work.getId());
                     if (authors.size() == 0) {
                         workDao.remove(work.getId());
                     }
                     // finally consider whether name now refers to no-one, if so delete it
                     List<Person> referents = nameDao.personsWithName(name.getId());
                     if (referents.size() == 0) {
                         nameDao.remove(name.getId());
                     }
                 }
                return Response.ok().build();
            }
        }
        throw new WebApplicationException(Status.NOT_FOUND);
    }
}
