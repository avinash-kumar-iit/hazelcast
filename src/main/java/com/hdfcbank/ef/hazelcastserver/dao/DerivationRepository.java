package com.hdfcbank.ef.hazelcastserver.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hdfc.ef.cache.entities.DerivationRule;
import com.hdfc.ef.cache.entities.DerivationRuleList;
import com.hdfcbank.ef.hazelcastserver.service.DerivationUtils;

import lombok.extern.slf4j.Slf4j;




@Slf4j
@Repository
public class DerivationRepository {

    private static final Logger log = LoggerFactory.getLogger(DerivationRepository.class);

    @Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
    
    @Autowired
    private DerivationUtils utils;

    
    private String derivationQuery="SELECT d.STRICT,d.SERVICE,d.CHANNEL,d.SUB_PRODUCT_CODE,d.RULE_ID,dr.RESULT_ID,d.JOURNEY_NAME,d.RULE_PURPOSE,d.RULE_GROUP, d.RULE_PRIORITY, d.RULE_ACTIVATE ,d.EXPRESSION ,dr.PARAMETER,dr.VALUE FROM DERIVATION_RULES d join DERIVATION_RESULTS dr on (d.RULE_GROUP=dr.RULE_GROUP AND d.RULE_PURPOSE=dr.RULE_PURPOSE AND d.SERVICE=dr.SERVICE AND d.JOURNEY_NAME=dr.JOURNEY_NAME AND d.CHANNEL=dr.CHANNEL AND d.SUB_PRODUCT_CODE=dr.SUB_PRODUCT_CODE) where d.RULE_ACTIVATE ='Y' AND d.SERVICE = :serviceCode AND d.version = '0.0.1' ORDER BY d.RULE_PRIORITY, d.RULE_PURPOSE,  d.RULE_GROUP";
    
    public DerivationRuleList fetchRules(String serviceName,
            String version) {
    	Map<String, Object> params = new HashMap<>();
    	params.put("serviceCode",serviceName);
    	params.put("version",version);
        DerivationRuleList drList = new DerivationRuleList();        
        drList.setRules(fetch(params, derivationQuery));
        return drList;
    }

 
 
    private List<DerivationRule> fetch(Map<String, Object> params, String query)
           {
        log.debug("Fetching derivation rules from db...");
        log.trace(query);
        Map<Object, DerivationRule> derivationMap = new LinkedHashMap<>();

        try {
            List<Map<String, Object>> rulesList = jdbcTemplate.queryForList(query, params);

            for (Map<String, Object> r : rulesList) {
                utils.mapRow(r, derivationMap);
            }

        } catch (Exception e) {
            log.error("Error occured while fetching Derivation rules from DB :{} " + e.getMessage());
            return new ArrayList<>();
        }

        List<DerivationRule> rules = new ArrayList<>();
        rules.addAll(derivationMap.values());
        log.info("Found {} rules in db.", rules.size());
        return rules;
    }
    
   

  
	

}
