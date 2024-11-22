package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.cache;

import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.config.GuavaConfig;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.exception.CacheServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GuavaCacheServiceImpl implements CacheService {
    private final GuavaConfig guavaConfig;

    @Autowired
    public GuavaCacheServiceImpl(GuavaConfig guavaConfig) {
        this.guavaConfig = guavaConfig;
    }

    @Override
    public void set(String key, String value, long expireInMs) throws CacheServiceException {

    }

    @Override
    public <K, V> void setAll(Map<K, V> kvs, long expireInMs) throws CacheServiceException {

    }

    @Override
    public boolean setNX(String key, String value, long expireInMs) throws CacheServiceException {
        return false;
    }

    @Override
    public long append(String key, String value) throws CacheServiceException {
        return 0;
    }

    @Override
    public long ttl(String key) throws CacheServiceException {
        return 0;
    }

    @Override
    public String get(String key) throws CacheServiceException {
        return "";
    }

    @Override
    public boolean del(String key) throws CacheServiceException {
        return false;
    }

    @Override
    public void clear() throws CacheServiceException {

    }

    @Override
    public long memSize() throws CacheServiceException {
        return 0;
    }
}
