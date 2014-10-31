/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.db;

import java.sql.Timestamp;
import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import edu.mit.lib.idsvc.api.Identifier;
import edu.mit.lib.idsvc.api.Name;
import edu.mit.lib.idsvc.api.Person;
import edu.mit.lib.idsvc.api.Work;

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

    @SqlQuery("select pname.* from personpnames, pname where pname.id = personpnames.pname_id and personpnames.person_id = :pid")
    @Mapper(NameMapper.class)
    List<Name> namesFor(@Bind("pid") int pid);

    @SqlQuery("select * from pident where person_id = :pid")
    @Mapper(IdentifierMapper.class)
    List<Identifier> identifiersFor(@Bind("pid") int pid);

    @SqlQuery("select work.* from personworks, work where work.id = personworks.work_id and personworks.person_id = :pid")
    @Mapper(WorkMapper.class)
    List<Work> worksBy(@Bind("pid") int pid);

    @SqlQuery("select work.* from personworks, work where work.id = personworks.work_id and personworks.work_id = :wid and personworks.person_id = :pid")
    @Mapper(WorkMapper.class)
    List<Work> linksToWork(@Bind("wid") int wid, @Bind("pid") int pid);

    @SqlUpdate("insert into personworks (work_id, person_id) values (:wid, :pid)")
    void linkWork(@Bind("wid") int wid, @Bind("pid") int pid);

    @SqlUpdate("delete from personworks where work_id = :wid and person_id = :pid")
    void unlinkWork(@Bind("wid") int wid, @Bind("pid") int pid);

    @SqlQuery("select pname.* from personpnames, pname where pname.id = personpnames.pname_id and personpnames.pname_id = :nid and personpnames.person_id = :pid")
    @Mapper(NameMapper.class)
    List<Name> linksToName(@Bind("nid") int nid, @Bind("pid") int pid);

    @SqlUpdate("insert into personpnames (pname_id, person_id) values (:nid, :pid)")
    void linkName(@Bind("nid") int nid, @Bind("pid") int pid);

    @SqlUpdate("delete from personpnames where pname_id = :nid and person_id = :pid")
    void unlinkName(@Bind("nid") int nid, @Bind("pid") int pid);
}
