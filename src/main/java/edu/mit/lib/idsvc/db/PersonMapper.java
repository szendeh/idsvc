/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import edu.mit.lib.idsvc.api.Person;

/**
 * DAO for Person objects
 * 
 * @author richardrodgers
 */

public class PersonMapper implements ResultSetMapper<Person>  {

    @Override
    public Person map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Person(r.getInt("id"));
    }
}