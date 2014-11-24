/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.db;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import edu.mit.lib.idsvc.api.WorkIdentifier;

/**
 * DAO for WorkIdentifier objects
 * 
 * @author richardrodgers, szendeh
 */

public interface WorkIdentifierDAO {

    @SqlQuery("select * from wident where id = :id")
    @Mapper(WorkIdentifierMapper.class)
    WorkIdentifier findById(@Bind("id") int id);

    @SqlQuery("select * from wident where schema = :schema and identifier = :identifier")
    @Mapper(WorkIdentifierMapper.class)
    WorkIdentifier findByIdentifier(@Bind("schema") String schema, @Bind("identifier") String identifier);

    @SqlQuery("select * from wident where work_id = :work_id")
    @Mapper(WorkIdentifierMapper.class)
    List<WorkIdentifier> identifiersFor(@Bind("work_id") int work_id);

    @SqlUpdate("delete from wident where work_id = :work_id")
    void removeIdentifiersOf(@Bind("work_id") int work_id);

    @SqlUpdate("insert into wident (work_id, schema, identifier) values (:work_id, :schema, :identifier)")
    @GetGeneratedKeys
    int create(@Bind("work_id") int work_id, @Bind("schema") String schema, @Bind("identifier") String identifier);

}
