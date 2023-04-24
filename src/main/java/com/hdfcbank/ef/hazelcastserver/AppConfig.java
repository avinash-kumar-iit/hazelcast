package com.hdfcbank.ef.hazelcastserver;

import com.google.gson.GsonBuilder;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 *
 */
@Configuration
public class AppConfig {

    /**
     *
     * @return
     */
    @Bean
    HazelcastInstance hazelCastInstance(){
//        Config cfg = new Config();
//        var manConfig=cfg.getManagementCenterConfig();
//        manConfig.setScriptingEnabled(true).addTrustedInterface("http://localhost:8080/mancenter");
        return Hazelcast.newHazelcastInstance();
    }

    @Bean
    @ConfigurationProperties(prefix = "app.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource){
        return new NamedParameterJdbcTemplate(dataSource);

    }

    @Bean
    public GsonBuilder jsonBuilder(){
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        return builder;
    }
}
