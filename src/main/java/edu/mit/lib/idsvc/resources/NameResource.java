/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import edu.mit.lib.idsvc.api.Identifier;
import edu.mit.lib.idsvc.api.Name;
import edu.mit.lib.idsvc.api.NameGraph;
import edu.mit.lib.idsvc.api.Person;
import edu.mit.lib.idsvc.api.Work;
import edu.mit.lib.idsvc.api.WorkIdentifier;
import edu.mit.lib.idsvc.db.ClaimDAO;
import edu.mit.lib.idsvc.db.NameDAO;
import edu.mit.lib.idsvc.db.PersonDAO;
import edu.mit.lib.idsvc.db.WorkDAO;

/**
 * Resource class for personal names. An Http GET
 * will return a subgraph of the model with persons
 * and works associated with the name.
 * 
 * @author richardrodgers
 */

@Path("/name")
@Produces(MediaType.APPLICATION_JSON)
public class NameResource {

    private final NameDAO nameDao;
    private final PersonDAO personDao;
    private final ClaimDAO claimDao;
    private final WorkDAO workDao;

    public NameResource(NameDAO nameDao, PersonDAO personDao, ClaimDAO claimDao, WorkDAO workDao) {
        this.nameDao = nameDao;
        this.personDao = personDao;
        this.claimDao = claimDao;
        this.workDao = workDao;
    }

    @GET @Path("{nameId}")
    public NameGraph getNameById(@PathParam("nameId") int nameId) {
        // return graph if name exists
        Name name = nameDao.findById(nameId);
        if (name != null) {
            // look up associated persons and works
            return new NameGraph(name, personsNamed(name.getId()), worksWithName(name.getId()));
        }
        throw new WebApplicationException(Status.NOT_FOUND);
    }

    @GET @Path("literal")
    public NameGraph getNameByRef(@QueryParam("ref") String ref) {
        // return graph if name exists
        Name name = nameDao.findByName(ref);
        if (name != null) {
            // look up associated persons and works
            return new NameGraph(name, personsNamed(name.getId()), worksWithName(name.getId()));
        }
        throw new WebApplicationException(Status.NOT_FOUND);
    }

    private List<Person> personsNamed(int nameId) {
        List<Person> personList = new ArrayList<>();
        for (Identifier pid: claimDao.authorsNamed(nameId)) {
            personList.add(personDao.findById(pid.getPersonId()));
        }
        return personList;
    }

    private List<Work> worksWithName(int nameId) {
        List<Work> workList = new ArrayList<>();
        for (WorkIdentifier workIdentifier: claimDao.worksWithName(nameId)) {
            workList.add(workDao.findById(workIdentifier.getWorkId()));
        }
        return workList;
    }
}
