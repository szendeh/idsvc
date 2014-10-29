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

import edu.mit.lib.idsvc.api.Name;
import edu.mit.lib.idsvc.api.NameGraph;
import edu.mit.lib.idsvc.db.NameDAO;

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

    public NameResource(NameDAO nameDao) {
        this.nameDao = nameDao;
    }

    @GET @Path("{nameId}")
    public NameGraph getNameById(@PathParam("nameId") int nameId) {
        // return graph if name exists
        Name name = nameDao.findById(nameId);
        if (name != null) {
            // look up associated persons and works
            return new NameGraph(name, nameDao.personsWithName(name.getId()), nameDao.worksWithName(name.getId()));
        }
        throw new WebApplicationException(Status.NOT_FOUND);
    }

    @GET @Path("literal")
    public NameGraph getNameByRef(@QueryParam("ref") String ref) {
        // return graph if name exists
        Name name = nameDao.findByName(ref);
        if (name != null) {
            // look up associated persons and works
            return new NameGraph(name, nameDao.personsWithName(name.getId()), nameDao.worksWithName(name.getId()));
        }
        throw new WebApplicationException(Status.NOT_FOUND);
    }
}
