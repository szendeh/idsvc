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
import edu.mit.lib.idsvc.api.Person;
import edu.mit.lib.idsvc.api.Work;
import edu.mit.lib.idsvc.api.WorkGraph;
import edu.mit.lib.idsvc.db.ClaimDAO;
import edu.mit.lib.idsvc.db.PersonDAO;
import edu.mit.lib.idsvc.db.WorkDAO;

/**
 * Resource class for works - primary type of which is DSpace Items 
 * 
 * @author richardrodgers
 */

@Path("/work")
@Produces(MediaType.APPLICATION_JSON)
public class WorkResource {

    private final WorkDAO workDao;
    private final PersonDAO personDao;
    private final ClaimDAO claimDao;

    public WorkResource(WorkDAO workDao, PersonDAO personDao, ClaimDAO claimDao) {
        this.workDao = workDao;
        this.personDao = personDao;
        this.claimDao = claimDao;
    }

    @GET @Path("{workId}")
    public WorkGraph getWorkById(@PathParam("workId") int workId) {
        // return graph if work exists
        Work work = workDao.findById(workId);
        if (work != null) {
            // look up associated identifiers and names
            return new WorkGraph(work, authorsOf(work.getId()), claimDao.namesIn(work.getId()));
        }
        throw new WebApplicationException(Status.NOT_FOUND);
    }

    @GET @Path("ref/{schema}")
    public WorkGraph getWorkByRef(@PathParam("schema") String schema, @QueryParam("ref") String ref) {
        // return graph if work exists
        Work work = workDao.findByRef(schema, ref);
        if (work != null) {
            // look up associated identifiers and names
            return new WorkGraph(work, authorsOf(work.getId()), claimDao.namesIn(work.getId()));
        }
        throw new WebApplicationException(Status.NOT_FOUND);
    }

    private List<Person> authorsOf(int workId) {
        List<Person> personList = new ArrayList<>();
        for (Identifier pid: claimDao.authorsOf(workId)) {
            personList.add(personDao.findById(pid.getPersonId()));
        }
        return personList;
    }
}
