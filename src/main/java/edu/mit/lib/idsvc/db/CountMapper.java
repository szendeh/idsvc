/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * Mapper for SQL Count queries
 * 
 * @author richardrodgers
 */

public class CountMapper implements ResultSetMapper<Integer>  {

    @Override
    public Integer map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return r.getInt("count");
    }
}