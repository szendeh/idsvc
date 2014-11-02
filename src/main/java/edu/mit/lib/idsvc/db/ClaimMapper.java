/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import edu.mit.lib.idsvc.api.ResolvedClaim;

/**
 * Mapper for Claim objects
 * 
 * @author richardrodgers
 */

public class ClaimMapper implements ResultSetMapper<ResolvedClaim>  {

    @Override
    public ResolvedClaim map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new ResolvedClaim(r.getInt("id"), r.getString("source"), r.getInt("pident_id"), r.getInt("work_id"), r.getInt("pname_id"));
    }
}