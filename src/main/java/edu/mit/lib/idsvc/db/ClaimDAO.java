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
import edu.mit.lib.idsvc.api.ResolvedClaim;
import edu.mit.lib.idsvc.api.Work;

/**
 * DAO for resolved Claims
 * 
 * @author richardrodgers
 */

public interface ClaimDAO {

    @SqlQuery("select * from claim where id = :id")
    @Mapper(ClaimMapper.class)
    ResolvedClaim findById(@Bind("id") int id);

    @SqlQuery("select * from claim where pident_id = :pid and work_id = :wid")
    @Mapper(ClaimMapper.class)
    ResolvedClaim findByRefs(@Bind("pid") int pid, @Bind("wid") int wid);

    @SqlUpdate("insert into claim (created, source, pident_id, work_id, pname_id) values (:created, :source, :pid, :wid, :nid)")
    @GetGeneratedKeys
    int create(@Bind("created") Timestamp created, @Bind("source") String source, @Bind("pid") int pid, @Bind("wid") int wid, @Bind("nid") int nid);

    @SqlUpdate("delete from claim where id = :id")
    void remove(@Bind("id") int id);

    @SqlQuery("select work.* from work, claim where work.id = claim.work_id and claim.pident_id = :pid")
    @Mapper(WorkMapper.class)
    List<Work> worksBy(@Bind("pid") int pid);

    @SqlQuery("select pident.* from pident, claim where pident.id = claim.pident_id and claim.work_id = :wid")
    @Mapper(IdentifierMapper.class)
    List<Identifier> authorsOf(@Bind("wid") int wid); 

    @SqlQuery("select pident.* from pident, claim where pident.id = claim.pident_id and claim.pname_id = :nid")
    @Mapper(IdentifierMapper.class)
    List<Identifier> authorsNamed(@Bind("nid") int nid);    

    @SqlQuery("select pname.* from pname, claim where pname.id = claim.pname_id and claim.work_id = :wid")
    @Mapper(NameMapper.class)
    List<Name> namesIn(@Bind("wid") int wid); 

    @SqlQuery("select work.* from work, claim where work.id = claim.work_id and claim.pname_id = :nid")
    @Mapper(WorkMapper.class)
    List<Work> worksWithName(@Bind("nid") int nid);

    @SqlQuery("select pname.* from pname, claim where pname.id = claim.pname_id and claim.pident_id = :pid")
    @Mapper(NameMapper.class)
    List<Name> namesFor(@Bind("pid") int pid);

    @SqlQuery("select count(pident_id) as count from claim where pident_id = :pid")
    @Mapper(CountMapper.class)
    Integer numClaimsBy(@Bind("pid") int pid);

    @SqlQuery("select count(work_id) as count from claim where work_id = :wid")
    @Mapper(CountMapper.class)
    Integer numClaimsOn(@Bind("wid") int wid);

    @SqlQuery("select count(pname_id) as count from claim where pname_id = :nid")
    @Mapper(CountMapper.class)
    Integer numClaimsNaming(@Bind("nid") int nid);
}
