package com.hdfcbank.ef.hazelcastserver.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdfc.ef.cache.entities.MessageTransformationMappingEntity;
import com.hdfc.ef.cache.entities.MessageTransformationParamEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class MessageParamRepository {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private static final String  MTP_DBQUERY = "Select * from message_transformation_param where service_id=:aServiceId and version=:aVersion;";

	private static final String  MTM_DBQUERY = "Select * from message_transformation_mapping where service_id=:aServiceId and version=:aVersion";

	public MessageTransformationParamEntity getMessageTransformationParam(String serviceId, String version)
			throws Exception {

		MessageTransformationParamEntity messagetransformationParamEntity = null;

		try {
			Map<String, Object> inputParam = new HashMap<String, Object>();
			inputParam.put("aServiceId", serviceId);
			inputParam.put("aVersion", version);

			List<Map<String, Object>> messageParams = jdbcTemplate.queryForList(MTP_DBQUERY, inputParam);
			if (!CollectionUtils.isEmpty(messageParams)) {
				messagetransformationParamEntity = dbMapToPojo(messageParams.get(0),
						MessageTransformationParamEntity.class);
			}
			return messagetransformationParamEntity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<MessageTransformationMappingEntity> getMessageTransformationMapping(String serviceId, String version)
			throws Exception {

		List<MessageTransformationMappingEntity> messagetransaformationMappingEntities = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("aServiceId", serviceId);
		params.put("aVersion", version);
		List<Map<String, Object>> mappings = jdbcTemplate.queryForList(MTM_DBQUERY, params);
		mappings.forEach(m -> {
			messagetransaformationMappingEntities.add(dbMapToPojo(m, MessageTransformationMappingEntity.class));
		});
		log.debug("Total records stored in local cache for {} is {}", serviceId + version,
				messagetransaformationMappingEntities.size());
		return (List<MessageTransformationMappingEntity>) messagetransaformationMappingEntities;

	}

	public <T> T dbMapToPojo(Map<String, Object> map, Class<T> className) {
		ObjectMapper dbMapper = new ObjectMapper();
		return dbMapper.convertValue(map, className);
	}

}
