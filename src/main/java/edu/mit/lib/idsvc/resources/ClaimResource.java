/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.resources;

import java.sql.Timestamp;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import edu.mit.lib.idsvc.api.Claim;
import edu.mit.lib.idsvc.api.Identifier;
import edu.mit.lib.idsvc.api.Name;
import edu.mit.lib.idsvc.api.ResolvedClaim;
import edu.mit.lib.idsvc.api.Work;
import edu.mit.lib.idsvc.api.WorkIdentifier;
import edu.mit.lib.idsvc.db.ClaimDAO;
import edu.mit.lib.idsvc.db.IdentifierDAO;
import edu.mit.lib.idsvc.db.NameDAO;
import edu.mit.lib.idsvc.db.PersonDAO;
import edu.mit.lib.idsvc.db.WorkDAO;
import edu.mit.lib.idsvc.db.WorkIdentifierDAO;

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
    private final ClaimDAO claimDao;
    private final PersonDAO personDao;
    private final IdentifierDAO identifierDao;
    private final NameDAO nameDao;
    private final WorkDAO workDao;
    private final WorkIdentifierDAO workIdentifierDao;

    public ClaimResource(ClaimDAO claimDao, PersonDAO personDao, IdentifierDAO identifierDao, NameDAO nameDao, WorkDAO workDao, WorkIdentifierDAO workIdentifierDao) {
        this.claimDao = claimDao;
        this.personDao = personDao;
        this.identifierDao = identifierDao;
        this.nameDao = nameDao;
        this.workDao = workDao;
        this.workIdentifierDao = workIdentifierDao;
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response make(Claim claim) {
        // For now, only identifier we are accepting is MIT ID
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Identifier identifier = identifierDao.findByIdentifier("mitid", claim.getIdentifier());
        if (identifier == null) {
            // we have no record of this - create a new person and link it to this identifier
            int personId = personDao.create(timestamp);
            int identId = identifierDao.create(personId, "mitid", claim.getIdentifier());
            identifier = identifierDao.findById(identId);
        }
        // only WorkIdentifier we accept is cnri for now
        WorkIdentifier workIdentifier = workIdentifierDao.findByIdentifier("cnri", claim.getWorkIdentifier());
        if (workIdentifier == null) {
            int workId = workDao.create(timestamp);
            int workIdentifierId = workIdentifierDao.create(workId, "cnri", claim.getWorkIdentifier());
            workIdentifier = workIdentifierDao.findById(workIdentifierId);
        }
        // OK - determine whether a claim already has been make
        ResolvedClaim rc = claimDao.findByRefs(identifier.getId(), workIdentifier.getId());
        if (rc == null) {
            // create name if new, then record the claim
            Name name = nameDao.findByName(claim.getName());
            if (name == null) {
                int nameId = nameDao.create(claim.getName());
                name = nameDao.findById(nameId);
            }
            claimDao.create(timestamp, claim.getSource(), identifier.getId(), workIdentifier.getId(), name.getId());
            
            return Response.status(Status.CREATED).header("Access-Control-Allow-Origin", "*").build();
        }
        else {
            return Response.status(Status.NO_CONTENT).header("Access-Control-Allow-Origin", "*").build();
        }
    }

    @DELETE
    public Response retract(@PathParam("personId") String personId, @QueryParam("wid") String wid) {
        // For now, only identifier we recognize is MIT ID
        Identifier identifier = identifierDao.findByIdentifier("mitid", personId);
        if (identifier != null) {
            // likewise, default work id to CNRI handle
            WorkIdentifier workIdentifier = workIdentifierDao.findByIdentifier("cnri", wid);
            if (workIdentifier != null) {
                ResolvedClaim claim = claimDao.findByRefs(identifier.getId(), workIdentifier.getId());
                if (claim != null) {
                    claimDao.remove(claim.getId());

                    // now determine whether person, work or name is bereft of claims, if so remove them

                    // person
                    int numClaimsBy = 0;
                    for (Identifier pid : identifierDao.identifiersFor(identifier.getPersonId())) {
                        numClaimsBy += claimDao.numClaimsBy(pid.getId());
                    }
                    if (numClaimsBy == 0) {
                        identifierDao.removeIdentifiersOf(identifier.getPersonId());
                        personDao.remove(identifier.getPersonId());
                    }

                    // work
                    int numClaimsOn = 0;
                    for (WorkIdentifier workIdentifierTmp : workIdentifierDao.identifiersFor(workIdentifier.getWorkId())) {
                        numClaimsOn += claimDao.numClaimsOn(workIdentifierTmp.getId());
                    }
                    if (numClaimsOn == 0) {
                        workIdentifierDao.removeIdentifiersOf(workIdentifier.getWorkId());
                        workDao.remove(workIdentifier.getWorkId());
                    }

                    // name
                    if (claimDao.numClaimsNaming(claim.getPnameId()) == 0) {
                        nameDao.remove(claim.getPnameId());
                    }

                    return Response.ok("{}").header("Access-Control-Allow-Origin", "*").build();
                }
            }
        }
        throw new WebApplicationException(Status.NOT_FOUND);
    }

    @GET
    public Response get(@PathParam("personId") String personId, @QueryParam("wid") String workIdentifier) {
        ResolvedClaim claim = claimDao.findByPersonIdAndWorkIdentifier(personId, workIdentifier);
        if (claim != null) {
            return Response.ok(claim).header("Access-Control-Allow-Origin", "*").build();
        }
        
        throw new WebApplicationException(Response.status(Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").build());
        // return Response.status(Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").build();
    }

    @OPTIONS
    public Response option(@PathParam("personId") String personId) {
        return Response.ok().header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept").header("Access-Control-Allow-Methods", "PUT, DELETE, OPTIONS").build();
    }
}
