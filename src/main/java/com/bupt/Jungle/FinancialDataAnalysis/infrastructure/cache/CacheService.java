package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.cache;

import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.exception.CacheServiceException;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public interface CacheService {
    void set(String key, String value, long timeout, TimeUnit timeUnit) throws CacheServiceException;

    void set(String key, String value) throws CacheServiceException;

    <K, V> void setAll(Map<K, V> kvs, long timeout, TimeUnit timeUnit) throws CacheServiceException;

    <K, V> void setAll(Map<K, V> kvs) throws CacheServiceException;

    boolean setNX(String key, String value, long timeout, TimeUnit timeUnit) throws CacheServiceException;

    boolean setNX(String key, String value) throws CacheServiceException;

    Integer append(String key, String value) throws CacheServiceException;

    Long ttl(String key, TimeUnit timeUnit) throws CacheServiceException;

    default Boolean persist(String key) throws CacheServiceException {
        String value = get(key);
        if (Objects.isNull(value)) {
            return false;
        }
        set(key, value);
        return true;
    }

    String get(String key) throws CacheServiceException;

    default boolean exists(String key) throws CacheServiceException {
        return get(key) != null;
    }

    boolean del(String key) throws CacheServiceException;

    void clear(String prefix) throws CacheServiceException;

    long memSize(String prefix) throws CacheServiceException;

    default boolean expire(String key, long timeout, TimeUnit timeUnit) throws CacheServiceException {
        String value = get(key);
        if (!del(key)) {
            return false;
        }
        set(key, value , timeout, timeUnit);
        return true;
    }
}
