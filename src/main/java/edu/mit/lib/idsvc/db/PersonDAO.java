/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.db;

import java.sql.Timestamp;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import edu.mit.lib.idsvc.api.Person;

/**
 * DAO for Person objects and friends
 * 
 * @author richardrodgers
 */

public interface PersonDAO {

    @SqlQuery("select * from person where id = :id")
    @Mapper(PersonMapper.class)
    Person findById(@Bind("id") int id);

    @SqlQuery("select person.* from person, pident where person.id = pident.person_id and pident.schema = :schema and pident.identifier = :ref")
    @Mapper(PersonMapper.class)
    Person findByRef(@Bind("schema") String schema, @Bind("ref") String ref);

    @SqlUpdate("insert into person (created) values (:created)")
    @GetGeneratedKeys
    int create(@Bind("created") Timestamp created);

    @SqlUpdate("update person set label = :label where id = :id")
    int update(@Bind("id") int id, @Bind("label") String label);

    @SqlUpdate("delete from person where id = :id")
    void remove(@Bind("id") int id);
}
