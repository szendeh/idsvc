/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import edu.mit.lib.idsvc.api.Name;

/**
 * Mapper for Name objects
 * 
 * @author richardrodgers
 */

public class NameMapper implements ResultSetMapper<Name>  {

    @Override
    public Name map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Name(r.getInt("id"), r.getString("pname"));
    }
}