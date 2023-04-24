package com.hdfcbank.ef.hazelcastserver.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.hdfc.ef.cache.entities.DerivationEntity;
import com.hdfc.ef.cache.entities.DerivationResult;
import com.hdfc.ef.cache.entities.DerivationRule;


@Component
public class DerivationUtils {

	private static final Logger log = LoggerFactory.getLogger(DerivationUtils.class);

	private ObjectMapper mapper;

	private ObjectMapper dbMapper;

	public DerivationUtils() {
		this.mapper =  new ObjectMapper();
		this.dbMapper = new ObjectMapper();
		this.dbMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		this.dbMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.dbMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	public ObjectMapper getDbMapper() {
		return dbMapper;
	}

	public DerivationRule getDerivationRule(Map<String, Object> ruleRowMap) {

		DerivationRule e = dbMapToPojo(ruleRowMap, DerivationRule.class);

		if (ruleRowMap.get("PARAMETER") != null) {
			List<DerivationResult> results = new ArrayList<>();
			results.add(getDerivationResult(ruleRowMap));
			e.setDerivationResult(results);
		}

		e.setCompiledExpression(getCompiledExpression(e.getExpression()));

		return e;
	}

	public DerivationResult getDerivationResult(Map<String, Object> ruleRowMap) {
		DerivationResult dr = dbMapToPojo(ruleRowMap, DerivationResult.class);
		if (hasErrorParam(dr)) {
			dr.setErrorParamPresent(true);

			String code = dr.getValue().substring(0, dr.getValue().indexOf(':'));
			dr.setErrorCode(code);

			String msg = dr.getValue().substring(dr.getValue().indexOf(':') + 1);
			dr.setErrorMsg(msg);
			
			try {
				Serializable s = getCompiledExpression(code);
				dr.setCompiledErrorCode(s);
			} catch (Exception e) {
				// pass
			}
			try {
				Serializable s = getCompiledExpression(msg);
				dr.setCompiledResult(s);
			} catch (Exception e) {
				// pass
			}

		} else {
			String resultExpression = getDerivationResultExpression(dr);
			dr.setCompiledResult(getCompiledExpression(resultExpression));
		}
		return dr;
	}

	public boolean hasErrorParam(DerivationResult result) {
		return !StringUtils.isEmpty(result.getParameter())
				&& result.getParameter().equals("error");
	}

	public String getDerivationResultExpression(DerivationResult dr) {
		return new StringBuilder(dr.getParameter()).append("=").append(dr.getValue()).append(";").toString();
	}

	public String getDerivationRuleId(Map<String, Object> derivationRuleRowMap) {
		return (String) derivationRuleRowMap.get("RULE_ID");
	}

	public <T> Map<String, Object> pojoToMap(T obj) {

		return mapper.convertValue(obj, new TypeReference<Map<String, Object>>() {
		});

	}

	public Map<String, Object> jsonToMap(String jsonStr) throws IOException {

		return mapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {
		});

	}

	public <T> T dbMapToPojo(Map<String, Object> map, Class<T> className) {
		return dbMapper.convertValue(map, className);
	}

	public <T> T mapToPojo(Map<String, Object> map, Class<T> className, ObjectMapper mapper) {
		return mapper.convertValue(map, className);
	}

	public DerivationRule getCompiledDerivationRule(Collection<DerivationEntity> derivationEntities) {
		if (CollectionUtils.isEmpty(derivationEntities)) {
			return null;
		}
		DerivationRule derivationRule = getDerivationRule(derivationEntities.stream().findFirst().orElse(null));
		List<DerivationResult> result = derivationEntities.parallelStream().map(this::getDerivationResult)
				.collect(Collectors.toList());
		derivationRule.setDerivationResult(result);
		derivationRule.setCompiledExpression(getCompiledExpression(derivationRule.getExpression()));
		return derivationRule;
	}

	public void mapRow(Map<String, Object> ruleRow, Map<Object, DerivationRule> derivationMap) {

		String ruleId = getDerivationRuleId(ruleRow);

		if (derivationMap.containsKey(ruleId)) {
			DerivationRule derivationRule = derivationMap.get(ruleId);
			List<DerivationResult> existingResults = derivationRule.getDerivationResult();
			existingResults.add(getDerivationResult(ruleRow));
			derivationRule.setDerivationResult(existingResults);
			derivationMap.put(ruleId, derivationRule);
		} else {
			DerivationRule e = getDerivationRule(ruleRow);
			derivationMap.put(e.getRuleId(), e);
		}
	}

	public void mapRow(DerivationEntity derivationEntity, Map<Object, DerivationRule> derivationMap) {

		String ruleId = derivationEntity.getRuleId();

		if (derivationMap.containsKey(ruleId)) {
			DerivationRule derivationRule = derivationMap.get(ruleId);
			List<DerivationResult> existingResults = derivationRule.getDerivationResult();
			existingResults.add(getDerivationResult(derivationEntity));
			derivationRule.setDerivationResult(existingResults);
			derivationMap.put(ruleId, derivationRule);
		} else {
			DerivationRule derivationRule = getDerivationRule(derivationEntity);
			List<DerivationResult> result = new ArrayList<>();
			result.add(getDerivationResult(derivationEntity));
			derivationRule.setDerivationResult(result);
			derivationRule.setCompiledExpression(getCompiledExpression(derivationRule.getExpression()));
			derivationMap.put(derivationRule.getRuleId(), derivationRule);
		}
	}

	public DerivationRule getDerivationRule(DerivationEntity derivationEntity) {
		DerivationRule dr = new DerivationRule();
		BeanUtils.copyProperties(derivationEntity, dr);
		dr.setRuleGroup(derivationEntity.getRuleGroup().intValue()); //Remove the unboxing from "Integer"
		dr.setStrict(derivationEntity.getStrict().intValue());
		dr.setRulePriority(derivationEntity.getRulePriority().intValue());//Remove the unboxing from "Integer"
		return dr;
	}

	public DerivationResult getDerivationResult(DerivationEntity derivationEntity) {
		DerivationResult dr = new DerivationResult();
		BeanUtils.copyProperties(derivationEntity, dr);
		dr.setRuleGroup(derivationEntity.getRuleGroup().intValue());//Remove the unboxing from "Integer"
		return dr;
	}

	public Serializable getCompiledExpression(String expression) {
		log.trace("Compiling mvel expression : {}", expression);
		ParserContext ctx = getParserContext(null);
		return MVEL.compileExpression(expression, ctx);
	}

	public ParserContext getParserContext(List<String> imports) {

		ParserContext ctx = new ParserContext();
		ctx.addImport(MVEL.class);
		ctx.addImport(java.util.regex.Pattern.class);
		ctx.addImport(java.time.LocalDate.class);
		ctx.addImport(java.time.format.DateTimeFormatter.class);
		if (!CollectionUtils.isEmpty(imports)) {
			imports.stream().forEach(ctx::addPackageImport);
		}

		return ctx;
	}

}
