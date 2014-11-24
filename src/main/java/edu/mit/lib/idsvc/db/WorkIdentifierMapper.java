/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import edu.mit.lib.idsvc.api.WorkIdentifier;

/**
 * Mapper for WorkIdentifier objects
 * 
 * @author richardrodgers, szendeh
 */

public class WorkIdentifierMapper implements ResultSetMapper<WorkIdentifier>  {

    @Override
    public WorkIdentifier map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new WorkIdentifier(r.getInt("id"), r.getInt("work_id"), r.getString("schema"), r.getString("identifier"));
    }
}