/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import edu.mit.lib.idsvc.api.Identifier;
import edu.mit.lib.idsvc.api.Name;
import edu.mit.lib.idsvc.api.Person;
import edu.mit.lib.idsvc.api.PersonGraph;
import edu.mit.lib.idsvc.api.Work;
import edu.mit.lib.idsvc.db.ClaimDAO;
import edu.mit.lib.idsvc.db.IdentifierDAO;
import edu.mit.lib.idsvc.db.PersonDAO;

/**
 * Resource class for persons - which represent unique identities. Persons
 * cannot be directly created, removed, modified - they are entailed by claim operations.
 * 
 * @author richardrodgers
 */

@Path("/person")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    private final PersonDAO personDao;
    private final IdentifierDAO identifierDao;
    private final ClaimDAO claimDao;

    public PersonResource(PersonDAO personDao, IdentifierDAO identifierDao, ClaimDAO claimDao) {
        this.personDao = personDao;
        this.identifierDao = identifierDao;
        this.claimDao = claimDao;
    }

    @GET @Path("{personId}")
    public PersonGraph getPersonById(@PathParam("personId") int personId) {
        // return graph if name exists
        Person person = personDao.findById(personId);
        if (person != null) {
            // look up associated identifiers, names and works
            return new PersonGraph(person, claimedNames(personId),
                                   identifierDao.identifiersFor(personId), claimedWorks(personId));
        }
        throw new WebApplicationException(Status.NOT_FOUND);
    }

    @GET @Path("ref/{schema}")
    public PersonGraph getPersonByRef(@PathParam("schema") String schema, @QueryParam("ref") String ref) {
        // return graph if ref exists
        Person person = personDao.findByRef(schema, ref);
        if (person != null) {
            // look up associated identifiers, names and works
            return new PersonGraph(person, claimedNames(person.getId()),
                                   identifierDao.identifiersFor(person.getId()), claimedWorks(person.getId()));
        }
        throw new WebApplicationException(Status.NOT_FOUND);
    }

    @PUT @Path("{personId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePerson(@PathParam("personId") int personId, Person person) {
        Person theperson = personDao.findById(personId);
        if (theperson != null) {
            personDao.update(personId, person.getLabel());
            return Response.ok().build();
        }
        throw new WebApplicationException(Status.NOT_FOUND);
    }

    private List<Name> claimedNames(int personId) {
        List<Name> nameList = new ArrayList<>();
        for (Identifier pid : identifierDao.identifiersFor(personId)) {
             nameList.addAll(claimDao.namesFor(pid.getId()));
        }
        return nameList;
    }

    private List<Work> claimedWorks(int personId) {
        List<Work> workList = new ArrayList<>();
        for (Identifier pid : identifierDao.identifiersFor(personId)) {
             workList.addAll(claimDao.worksBy(pid.getId()));
        }
        return workList;
    }
}
