/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import edu.mit.lib.idsvc.api.Person;
import edu.mit.lib.idsvc.api.PersonGraph;
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

    public PersonResource(PersonDAO personDao) {
        this.personDao = personDao;
    }

    @GET @Path("{personId}")
    public PersonGraph getPersonById(@PathParam("personId") int personId) {
        // return graph if name exists
        Person person = personDao.findById(personId);
        if (person != null) {
            // look up associated identifiers, names and works
            return new PersonGraph(person, personDao.namesFor(personId),
                                   personDao.identifiersFor(personId), personDao.worksBy(personId));
        }
        throw new WebApplicationException(Status.NOT_FOUND);
    }

    @GET @Path("ref/{schema}")
    public PersonGraph getPersonByRef(@PathParam("schema") String schema, @QueryParam("ref") String ref) {
        // return graph if ref exists
        Person person = personDao.findByRef(schema, ref);
        if (person != null) {
            // look up associated identifiers, names and works
            return new PersonGraph(person, personDao.namesFor(person.getId()),
                                   personDao.identifiersFor(person.getId()), personDao.worksBy(person.getId()));
        }
        throw new WebApplicationException(Status.NOT_FOUND);
    }
}
