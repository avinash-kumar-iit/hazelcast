package com.hdfcbank.ef.hazelcastserver.loaders;

import com.hazelcast.map.MapLoader;
import com.hdfcbank.ef.hazelcastserver.dao.StepLoadRepository;
import com.hdfcbank.ef.hazelcastserver.service.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;

/**
 *
 * @param <K>
 * @param <V>
 */

@Slf4j
public class JourneyStepsLoader<K,V> implements MapLoader<K,V> {

    @Override
    public V load(K k) {
        log.info("load: key-{}",k);
        String key=(String)k;
        var tokens=key.split("_");
        StepLoadRepository repository= ApplicationContextUtil.getBean(StepLoadRepository.class);
        return (V)repository.loadChannelActivities(tokens[0],tokens[1]);
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
