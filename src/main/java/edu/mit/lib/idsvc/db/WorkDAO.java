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

    @SqlQuery("select work.* from work, wident where work.id = wident.work_id and wident.schema = :schema and wident.identifier = :ref")
    @Mapper(WorkMapper.class)
    Work findByRef(@Bind("schema") String schema, @Bind("ref") String ref);

    @SqlUpdate("insert into work (created) values (:created)")
    @GetGeneratedKeys
    int create(@Bind("created") Timestamp created);

    @SqlUpdate("delete from work where id = :id")
    void remove(@Bind("id") int id);
}
