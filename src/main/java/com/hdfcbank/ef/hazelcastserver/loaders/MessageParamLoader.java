package com.hdfcbank.ef.hazelcastserver.loaders;

import java.util.Collection;
import java.util.Map;

import com.hazelcast.map.MapLoader;
import com.hdfcbank.ef.hazelcastserver.dao.MessageParamRepository;
import com.hdfcbank.ef.hazelcastserver.service.ApplicationContextUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageParamLoader<K,V> implements MapLoader<K,V> {

	@Override
    public V load(K k) {
        log.info("load: key-{}",k);
        String key=(String)k;
        var tokens=key.split("_");
        MessageParamRepository repository= ApplicationContextUtil.getBean(MessageParamRepository.class);
        try {
			return (V)repository.getMessageTransformationParam(tokens[0],tokens[1]);
		} catch (Exception e) {
			log.error("Error while fetching Data from DB {}",e.getMessage());
			 return null;
		}
     }

    @Override
    public Map<K, V> loadAll(Collection<K> collection) {
        return null;
    }

    @Override
    public Iterable<K> loadAllKeys() {
        return null;
    }
}
