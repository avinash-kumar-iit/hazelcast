package com.hdfcbank.ef.hazelcastserver.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hdfc.ef.cache.entities.Activity;
import com.hdfc.ef.cache.entities.ChannelJourneyActivities;

@Repository
public class StepLoadRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String JOURNEY_STEPS_SQL="select js.activity_name,js.sequence_no,cj.journey_id from  journey_steps js inner join channel_journey cj on js.journey_id=cj.id \n" +
            "where cj.journey_id=:journeyId order by js.sequence_no;";

    /**
     *
     * @param channel
     * @param journeyId
     * @return
     */
    public ChannelJourneyActivities loadChannelActivities(String channel, String journeyId){

        Map<String,Object> params=new HashMap<String,Object>();
        params.put("journeyId",journeyId);
        
        var results=jdbcTemplate.query(JOURNEY_STEPS_SQL, params, new RowMapper<Activity>() {
            @Override
            public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
                Activity activity=new Activity();
                activity.setActivityName(rs.getString("activity_name"));
                activity.setSequenceId(rs.getInt("sequence_no"));
                activity.setJourneyId(rs.getString("journey_id"));
                return activity;
            }
        });

        if(results!=null){
            ChannelJourneyActivities journeyActivities=new ChannelJourneyActivities();
            journeyActivities.setChannelId(channel);
            journeyActivities.setJourneyId(journeyId);
            journeyActivities.setActivities(results);
            return journeyActivities;
        }
        return null;

    }

}
