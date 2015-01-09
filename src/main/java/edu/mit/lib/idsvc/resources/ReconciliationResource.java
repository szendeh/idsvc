package edu.mit.lib.idsvc.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import edu.mit.lib.idsvc.api.Identifier;
import edu.mit.lib.idsvc.api.Name;
import edu.mit.lib.idsvc.db.ClaimDAO;
import edu.mit.lib.idsvc.db.NameDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebServiceException;

import java.util.*;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements OpenRefine Reconciliation API for personal names.
 *
 * @author richardrodgers
 */
@Path("/reconcile")
@Produces({"application/javascript"})
public class ReconciliationResource {
    Logger logger = LoggerFactory.getLogger(ReconciliationResource.class);

    private final NameDAO nameDao;
    private final ClaimDAO claimDao;
    private final ObjectMapper queryMapper;

    public ReconciliationResource(NameDAO nameDao, ClaimDAO claimDao) {
        this.nameDao = nameDao;
        this.claimDao = claimDao;
        this.queryMapper = new ObjectMapper();
    }

    // reconciles personal names
    @GET @Path("name")
    public Response reconcileName(@QueryParam("callback") String callback, @QueryParam("queries") String jsonParam) {
        logger.debug("SOLH TESTING");
        if (jsonParam == null) {
            ReconMetadata metaData = new ReconMetadata();
            JSONPObject jsonpMetaData = new JSONPObject(callback, metaData);
            return Response.ok().entity(jsonpMetaData).build();
        }
        try {
            Map<String, ReconQuery> querySet = queryMapper.readValue(jsonParam, new TypeReference<Map<String, ReconQuery>>() {} );
            // calculate a result for each query and map to query name in result set
            Map<String, ReconResults> resultSet = new HashMap<>();
            for (String qName : querySet.keySet()) {
                ReconQuery query = querySet.get(qName);
                List<ReconResult> results = new ArrayList<>();
                Name name = nameDao.findByName(query.getQuery());
                if (name != null) {
                    // score the matches: score is just number of authored works
                    for (Identifier authId : claimDao.authorsNamed(name.getId())) {
                        results.add(new ReconResult(true, authId.getIdentifier(), name.getName(), (double)claimDao.numClaimsBy(authId.getId())));
                    }
                } else {
                    // not sure what API protocol is for non-matches is - just echo query
                    results.add(new ReconResult(false, "Unknown", query.getQuery(), (double)0));
                }
                resultSet.put(qName, new ReconResults(results));
            }

            JSONPObject jsonpResultSet = new JSONPObject(callback, resultSet);

            return Response.ok().entity(jsonpResultSet).build();
        } catch (Exception e) {
            //System.out.println("Ouch: " + e.getMessage());
            //e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    // @POST @Path("name")
    // public Response reconcileName(String postData) {
    //     logger.debug("SOLH2");
    //     logger.debug(postData);
    //     return Response.ok().entity(postData).build();
    // }

    @POST @Path("name")
    @Consumes("application/x-www-form-urlencoded")
    public Response reconcileName(String postData) {
        try {
            logger.debug("reconcileName");
            logger.debug(postData);
            String postDataDecoded = URLDecoder.decode(postData.substring(8), "UTF-8");
            logger.debug(postDataDecoded);

            Map<String, ReconQuery> querySet = queryMapper.readValue(postDataDecoded, new TypeReference<Map<String, ReconQuery>>() {} );
            // calculate a result for each query and map to query name in result set
            Map<String, ReconResults> resultSet = new HashMap<>();
            for (String qName : querySet.keySet()) {
                ReconQuery query = querySet.get(qName);
                List<ReconResult> results = new ArrayList<>();
                Name name = nameDao.findByName(query.getQuery());
                
                if (name != null) {
                    // score the matches: score is just number of authored works
                    for (Identifier authId : claimDao.authorsNamed(name.getId())) {
                        results.add(new ReconResult(true, authId.getIdentifier(), name.getName(), (double)claimDao.numClaimsBy(authId.getId())));
                    }
                }
                else {
                    // not sure what API protocol is for non-matches is - just echo query
                    results.add(new ReconResult(false, "Unknown", query.getQuery(), (double)0));
                }
                
                resultSet.put(qName, new ReconResults(results));
            }

            logger.debug("SOLHresultSet");
            logger.debug(queryMapper.writeValueAsString(resultSet));

            return Response.ok().entity(resultSet).build();
        }
        catch (Exception e) {
            System.out.println("Ouch: " + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    static class ReconQuery {

        private String query;
        private int limit;
        private String type;
        private String type_strict;

        public ReconQuery() {}

        @JsonProperty
        public String getQuery() {
            return query;
        }

        @JsonProperty
        public void setQuery(String query) {
            this.query = query;
        }

        @JsonProperty
        public int getLimit() {
            return limit;
        }

        @JsonProperty
        public void setLimit(int limit) {
            this.limit = limit;
        }

        @JsonProperty
        public String getType() {
            return type;
        }

        @JsonProperty
        public void setType(String type) {
            this.type = type;
        }

        @JsonProperty
        public String getType_strict() {
            return type_strict;
        }

        @JsonProperty
        public void setType_strict(String type_strict) {
            this.type_strict = type_strict;
        }
    }

    static class ReconResults {

        private List<ReconResult> result;

        public ReconResults() {}

        public ReconResults(List<ReconResult> result) {
            this.result = result;
        }

        @JsonProperty
        public List<ReconResult> getResult() {
            return result;
        }
    }

    static class ReconResult {

        private boolean match = true;
        private String id;
        private String name;
        private double score;


        public ReconResult(boolean match, String id, String name, double score) {
            this.match = match;
            this.id = id;
            this.name = name;
            this.score = score;
        }

        @JsonProperty
        public boolean getMatch() {
            return match;
        }

        @JsonProperty
        public String getId() {
            return id;
        }

        @JsonProperty
        public String getName() {
            return name;
        }

        @JsonProperty
        public double getScore() {
            return score;
        }
    }

    static class ReconMetadata {

        private final String name = "MIT Identity Service";
        private final String identifierSpace = "http://idsvc.lib.mit.edu/ns/Idsvc";
        private final String schemaSpace = "http://idsvc.lib.mit.edu/ns/MITID";
        private ArrayList defaultTypes = new ArrayList();

        public ReconMetadata() {
            HashMap<String, String> defaultName = new HashMap<String, String>();
            defaultName.put("id","/reconcile/name");
            defaultName.put("name","name");
            defaultTypes.add(defaultName);
        }

        @JsonProperty
        public String getName() {
            return name;
        }

        @JsonProperty
        public String getIdentifierSpace() {
            return identifierSpace;
        }

        @JsonProperty
        public String getSchemaSpace() {
            return schemaSpace;
        }

        @JsonProperty
        public ArrayList getDefaultTypes() {
            return defaultTypes;
        }
    }
}
