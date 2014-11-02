/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.db;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import edu.mit.lib.idsvc.api.Work;

/**
 * DAO for Work objects
 * 
 * @author richardrodgers
 */

public interface WorkDAO {

    @SqlQuery("select * from work where id = :id")
    @Mapper(WorkMapper.class)
    Work findById(@Bind("id") int id);

    @SqlQuery("select * from work where schema = :schema and identifier = :ident")
    @Mapper(WorkMapper.class)
    Work findByRef(@Bind("schema") String schema, @Bind("ident") String ident);

    @SqlUpdate("insert into work (schema, identifier) values (:schema, :ident)")
    @GetGeneratedKeys
    int create(@Bind("schema") String schema, @Bind("ident") String ident);

    @SqlUpdate("delete from work where id = :id")
    void remove(@Bind("id") int id);
}
