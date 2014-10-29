/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.db;

//import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import edu.mit.lib.idsvc.api.Identifier;

/**
 * DAO for Identifier objects
 * 
 * @author richardrodgers
 */

public interface IdentifierDAO {

    @SqlQuery("select * from pident where id = :id")
    @Mapper(IdentifierMapper.class)
    Identifier findById(@Bind("id") int id);

    @SqlQuery("select * from pident where schema = :schema and identifier = :ident")
    @Mapper(IdentifierMapper.class)
    Identifier findByIdentifier(@Bind("schema") String schema, @Bind("ident") String ident);

    @SqlQuery("select * from pident where person_id = :pid")
    @Mapper(IdentifierMapper.class)
    Identifier findByPersonId(@Bind("pid") int pid);

    @SqlUpdate("delete from pident where person_id = :pid")
    void removeIdentifiersOf(@Bind("pid") int pid);

    @SqlUpdate("insert into pident (person_id, schema, identifier) values (:pid, :schema, :ident)")
    void create(@Bind("pid") int pid, @Bind("schema") String schema, @Bind("ident") String ident);

}