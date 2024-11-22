package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.cache;

import com.bupt.Jungle.FinancialDataAnalysis.common.constant.CacheServiceConstant;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.exception.CacheServiceException;

import java.util.Map;
import java.util.Objects;

public interface CacheService {
    void set(String key, String value, long expireInMs) throws CacheServiceException;

    default void set(String key, String value) throws CacheServiceException {
        set(key, value, CacheServiceConstant.NEVER_EXPIRE);
    }

    <K, V> void setAll(Map<K, V> kvs, long expireInMs) throws CacheServiceException;

    default <K, V> void setAll(Map<K, V> kvs) throws CacheServiceException {
        setAll(kvs, CacheServiceConstant.NEVER_EXPIRE);
    }

    boolean setNX(String key, String value, long expireInMs) throws CacheServiceException;

    default boolean setNX(String key, String value) throws CacheServiceException {
        return setNX(key, value, CacheServiceConstant.NEVER_EXPIRE);
    }

    long append(String key, String value) throws CacheServiceException;

    long ttl(String key) throws CacheServiceException;

    default boolean persist(String key) throws CacheServiceException {
        String value = get(key);
        if (Objects.isNull(value)) {
            return false;
        }
        set(key, value, CacheServiceConstant.NEVER_EXPIRE);
        return true;
    }

    String get(String key) throws CacheServiceException;

    default boolean exists(String key) throws CacheServiceException {
        return get(key) != null;
    }

    boolean del(String key) throws CacheServiceException;

    void clear() throws CacheServiceException;

    long memSize() throws CacheServiceException;
}
