/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import edu.mit.lib.idsvc.api.Work;

/**
 * ResultSet mapper for Work objects
 * 
 * @author richardrodgers
 */

public class WorkMapper implements ResultSetMapper<Work>  {

    @Override
    public Work map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Work(r.getInt("id"), r.getString("schema"), r.getString("identifier"));
    }
}