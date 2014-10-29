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
 * DAO for Name objects and friends
 * 
 * @author richardrodgers
 */

public interface NameDAO {

    @SqlQuery("select * from pname where id = :id")
    @Mapper(NameMapper.class)
    Name findById(@Bind("id") int id);

    @SqlQuery("select * from pname where pname = :pn")
    @Mapper(NameMapper.class)
    Name findByName(@Bind("pn") String pn);

    @SqlQuery("select pname.* from workpnames, personpnames, pname where pname.id = workpnames.pname_id and personpnames.pname_id = pname.id and workpnames.work_id = :wid and personpnames.person_id = :pid")
    @Mapper(NameMapper.class)
    Name appearsAsInWork(@Bind("pid") int pid, @Bind("wid") int wid);

    @SqlUpdate("insert into pname (pname) values (:pname)")
    @GetGeneratedKeys
    int create(@Bind("pname") String pname);

    @SqlUpdate("delete from pname where id = :id")
    void remove(@Bind("id") int id);

    @SqlQuery("select work.* from workpnames, work where work.id = workpnames.work_id and workpnames.pname_id = :nid")
    @Mapper(WorkMapper.class)
    List<Work> worksWithName(@Bind("nid") int nid);

    @SqlQuery("select person.* from personpnames, person where person.id = personpnames.person_id and personpnames.pname_id = :nid")
    @Mapper(PersonMapper.class)
    List<Person> personsWithName(@Bind("nid") int nid);
}
