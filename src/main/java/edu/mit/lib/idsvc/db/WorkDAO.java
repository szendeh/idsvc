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

import edu.mit.lib.idsvc.api.Name;
import edu.mit.lib.idsvc.api.Person;
import edu.mit.lib.idsvc.api.Work;

/**
 * DAO for Work objects and friends
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

    @SqlQuery("select person.* from personworks, person where person.id = personworks.person_id and personworks.work_id = :wid")
    @Mapper(PersonMapper.class)
    List<Person> authorsOf(@Bind("wid") int wid);

    @SqlQuery("select pname.* from workpnames, pname where pname.id = workpnames.pname_id and workpnames.work_id = :wid")
    @Mapper(NameMapper.class)
    List<Name> namesIn(@Bind("wid") int wid);

    @SqlQuery("select pname.* from workpnames, pname where pname.id = workpnames.pname_id and workpnames.pname_id = :nid and workpnames.work_id = :wid")
    @Mapper(NameMapper.class)
    List<Name> linksToName(@Bind("nid") int nid, @Bind("wid") int wid);

    @SqlUpdate("insert into workpnames (pname_id, work_id) values (:nid, :wid)")
    void linkName(@Bind("nid") int nid, @Bind("wid") int wid);

    @SqlUpdate("delete from workpnames where pname_id = :nid and work_id = :wid")
    void unlinkName(@Bind("nid") int nid, @Bind("wid") int wid);

}