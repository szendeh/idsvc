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

import edu.mit.lib.idsvc.api.Name;

/**
 * DAO for Name objects
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

    @SqlUpdate("insert into pname (pname) values (:pn)")
    @GetGeneratedKeys
    int create(@Bind("pn") String pn);

    @SqlUpdate("delete from pname where id = :id")
    void remove(@Bind("id") int id);
}
