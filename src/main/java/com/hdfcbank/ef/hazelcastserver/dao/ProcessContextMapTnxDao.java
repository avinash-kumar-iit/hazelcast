package com.hdfcbank.ef.hazelcastserver.dao;


import com.google.gson.GsonBuilder;
import com.hdfc.ef.cache.entities.interceptor.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 *
 */
@Slf4j
@Repository
public class ProcessContextMapTnxDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private GsonBuilder gsonBuilder;

    private static final String GET_TXN_BY_JOURNEY_KEY_SQL="SELECT *FROM PROCESS_CONTEXT_TRANSACTIONS WHERE JOURNEY_KEY=:aJourneyKey and ACTIVITY=:anActivity";

    private static final String INSERT_TXN_SQL="INSERT INTO efdev01.PROCESS_CONTEXT_TRANSACTIONS(ACTIVITY,JOURNEY_KEY,JOURNEY_ID,events) VALUES(:anActivity,:journeyKey,:journeyId,:events)";

//    private static final String UPDATE_TXN_STATUS_SQL="UPDATE efdev01.PROCESS_CONTEXT_TRANSACTIONS SET STATUS=:status,ISTIMEOUT=:timeout WHERE JOURNEY_KEY=:journeyKey and ACTIVITY=:anActivity";

    private static final String UPDATE_TXN_EVENT_SQL="UPDATE efdev01.PROCESS_CONTEXT_TRANSACTIONS SET EVENTS=:events WHERE JOURNEY_KEY=:journeyKey and ACTIVITY=:anActivity";

    private static final String DELETE_TXN_SQL="DELETE from efdev01.PROCESS_CONTEXT_TRANSACTIONS WHERE JOURNEY_KEY=:aJourneyKey and ACTIVITY=:anActivity";

    private static final String AN_ACTIVITY = "anActivity"; 
    private static final String EVENTS = "events"; 
    private static final String A_JOURNEYKEY = "aJourneyKey"; 
    /**
     *
     * @param tnx
     */
    public void save(Transaction tnx){
        log.info("Persisting transaction.");
        String query=INSERT_TXN_SQL;
        Map<String, Object> params = new HashMap<String, Object>();
        if("NEW".equals(tnx.getType())){
            query=INSERT_TXN_SQL;
        }
        else if("DIRTY".equals(tnx.getType())){
            query=UPDATE_TXN_EVENT_SQL;
        }
        try {
            params.put(AN_ACTIVITY, tnx.getActivity());
            params.put("journeyKey", tnx.getJourneyKey());
            params.put("journeyId", tnx.getJourneyId());
            params.put(EVENTS,tnx.getJsonEvents());

            jdbcTemplate.execute(query, params, new PreparedStatementCallback() {
                @Override
                public Object doInPreparedStatement(PreparedStatement ps)
                        throws SQLException, DataAccessException {
                    return ps.executeUpdate();
                }
            });
        } catch (Exception e) {
            log.error("Exception caught at " + this.getClass().getName() + " : " + e.getMessage());
            throw e;
        }
    }

    /**
     *
     * @param journeyKey
     * @param activity
     * @return
     */
    public Transaction load(String journeyKey,String activity){
        Map<String,Object> params=new HashMap<String,Object>();
        params.put(A_JOURNEYKEY,journeyKey);
        params.put(AN_ACTIVITY,activity);
        try{
            return jdbcTemplate.queryForObject(GET_TXN_BY_JOURNEY_KEY_SQL, params, new RowMapper<Transaction>() {
                @Override
                public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {

                    return Transaction.builder()
                            .journeyId(rs.getString("journey_id"))
                            .journeyKey(rs.getString("journey_key"))
                            .activity(rs.getString("activity"))
                            .jsonEvents(rs.getString(EVENTS))
                            .build();
                }
            });
        }catch (EmptyResultDataAccessException e) {
            log.info("No Records found for -journey key-{} - activity: {}",journeyKey,activity);
            return null;
        }
    }

    /**
     *
     * @param journeyKey
     * @param activity
     */
    public void delete(String journeyKey,String activity){
        Map<String,Object> params=new HashMap<String,Object>();
        params.put(A_JOURNEYKEY,journeyKey);
        params.put(AN_ACTIVITY,activity);

        jdbcTemplate.execute(DELETE_TXN_SQL, params, new PreparedStatementCallback() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.executeUpdate();
            }
        });
    }

    public Map<String,Transaction> loadJourneyActivities(String journeyKey){

        Map<String,Object> params=new HashMap<String,Object>();
        params.put(A_JOURNEYKEY,journeyKey);
        var results=jdbcTemplate.query(GET_TXN_BY_JOURNEY_KEY_SQL, params, new RowMapper<Transaction>() {
            @Override
            public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {

                return Transaction.builder()
                        .journeyId(rs.getString("journey_id"))
                        .journeyKey(rs.getString("journey_key"))
                        .activity(rs.getString("activity"))
                        .jsonEvents(rs.getString(EVENTS))
                        .build();
            }
        });
        if(results!=null){
           return results.stream().collect(Collectors.toMap(Transaction::getActivity,v->v));
        }
        return null;
    }

    /**
     *
     * @param tnx
     * @return
     */
    private  String toJson(Transaction tnx){
       var gson=gsonBuilder.create();
       var map=gson.fromJson(tnx.getJsonEvents(),Map.class);
       var events=(List<String>)map.get(EVENTS);
       events.addAll(tnx.getEvents());
       map.put(EVENTS,events);
       return gson.toJson(map);
    }

    private List<String> fromJson(String events){
        var gson=gsonBuilder.create();
        var map=gson.fromJson(events,Map.class);
        return (List<String>)map.get(EVENTS);
    }
}
