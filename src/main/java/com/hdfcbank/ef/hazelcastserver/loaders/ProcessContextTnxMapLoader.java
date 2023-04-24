package com.hdfcbank.ef.hazelcastserver.loaders;

import com.hazelcast.map.MapStore;
import com.hdfc.ef.cache.entities.interceptor.Transaction;
import com.hdfcbank.ef.hazelcastserver.dao.ProcessContextMapTnxDao;
import com.hdfcbank.ef.hazelcastserver.service.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @param <K>
 * @param <V>
 */
@Slf4j
public class ProcessContextTnxMapLoader<K, V> implements  MapStore<K, V> {


    /**
     *
     * @param key
     * @param transaction
     */
    @Override
    public void store(K key, V transaction) {

        log.info("store->key-{} , transaction-{}",key,transaction);
        ProcessContextMapTnxDao repository= ApplicationContextUtil.getBean(ProcessContextMapTnxDao.class);
        repository.save((Transaction) transaction);
    }

    @Override
    public void storeAll(Map<K, V> map) {
            //do something or remove it
    }

    /**
     *
     * @param key
     */
    @Override
    public void delete(K key) {
        log.info("delete key-{}",key);
        String tnxKey=(String)key;
        var tokens=tnxKey.split("_");
        ProcessContextMapTnxDao repository= ApplicationContextUtil.getBean(ProcessContextMapTnxDao.class);
        repository.delete(tokens[0],tokens[1]);
    }

    @Override
    public void deleteAll(Collection<K> collection) {
    	 //do something or remove it
    }

    /**
     *
     * @param k
     * @return
     */
    @Override
    public V load(K k) {
        log.info("load key-{}",k);
        String key=(String)k;
        var tokens=key.split("_");
        ProcessContextMapTnxDao repository= ApplicationContextUtil.getBean(ProcessContextMapTnxDao.class);
        return (V)repository.load(tokens[0],tokens[1]);
    }

    @Override
    public Map<K, V> loadAll(Collection<K> collection) {
        List<String> list=new ArrayList(collection);
        //should always have one entry i.e. journeyKey
        var journeyKey=list.get(0);
        ProcessContextMapTnxDao repository= ApplicationContextUtil.getBean(ProcessContextMapTnxDao.class);
        return (Map<K,V>)repository.loadJourneyActivities(journeyKey);
    }

    @Override
    public Iterable<K> loadAllKeys() {
        return null;
    }

}
