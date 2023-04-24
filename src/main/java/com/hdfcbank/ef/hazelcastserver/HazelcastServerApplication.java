package com.hdfcbank.ef.hazelcastserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,JdbcTemplateAutoConfiguration.class, GsonAutoConfiguration.class})
public class HazelcastServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(HazelcastServerApplication.class, args);
	}

}
