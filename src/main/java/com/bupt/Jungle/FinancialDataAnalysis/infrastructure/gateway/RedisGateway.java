package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.gateway;

import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.cache.CacheService;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.exception.CacheServiceException;
import com.bupt.Jungle.FinancialDataAnalysis.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class RedisGateway implements CacheService {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisGateway(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void set(String key, String value, long timeout, TimeUnit timeUnit) throws CacheServiceException {
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    @Override
    public void set(String key, String value) throws CacheServiceException {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public <K, V> void setAll(Map<K, V> kvs, long timeout, TimeUnit timeUnit) throws CacheServiceException {
        for (Map.Entry<K, V> entry : kvs.entrySet()) {
            stringRedisTemplate.opsForValue().set(entry.getKey().toString(), GsonUtil.beanToJson(entry.getValue()), timeout, timeUnit);
        }
    }

    @Override
    public <K, V> void setAll(Map<K, V> kvs) throws CacheServiceException {
        for (Map.Entry<K, V> entry : kvs.entrySet()) {
            stringRedisTemplate.opsForValue().set(entry.getKey().toString(), GsonUtil.beanToJson(entry.getValue()));
        }
    }

    @Override
    public boolean setNX(String key, String value, long timeout, TimeUnit timeUnit) throws CacheServiceException {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit));
    }

    @Override
    public boolean setNX(String key, String value) throws CacheServiceException {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, value));
    }

    @Override
    public Integer append(String key, String value) throws CacheServiceException {
        return stringRedisTemplate.opsForValue().append(key, value);
    }

    @Override
    public Long ttl(String key, TimeUnit timeUnit) throws CacheServiceException {
        return stringRedisTemplate.opsForValue().getOperations().getExpire(key, timeUnit);
    }

    @Override
    public Boolean persist(String key) throws CacheServiceException {
        return stringRedisTemplate.persist(key);
    }

    @Override
    public String get(String key) throws CacheServiceException {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean del(String key) throws CacheServiceException {
        return Boolean.TRUE.equals(stringRedisTemplate.delete(key));
    }

    @Override
    public void clear(String prefix) throws CacheServiceException {
        String clearLuaScript = """
                        local keys = redis.call('keys', KEYS[1])
                        for i, key in ipairs(keys) do
                            redis.call('del', key)
                        end
                """;
        stringRedisTemplate.execute(new DefaultRedisScript<>(clearLuaScript, Long.class), Collections.singletonList(prefix + "*"));
    }

    @Override
    public long memSize(String prefix) throws CacheServiceException {
        long totalSize = 0;
        totalSize += Objects.requireNonNull(stringRedisTemplate.execute(new DefaultRedisScript<>("return #redis.call('keys', KEYS[1])", Long.class), Collections.singletonList(prefix + ":*")));
        totalSize += Objects.requireNonNull(stringRedisTemplate.execute(new DefaultRedisScript<>("return #redis.call('keys', KEYS[1])", Long.class), Collections.singletonList(prefix + ":[list]*")));
        totalSize += Objects.requireNonNull(stringRedisTemplate.execute(new DefaultRedisScript<>("return #redis.call('keys', KEYS[1])", Long.class), Collections.singletonList(prefix + ":[set]*")));
        totalSize += Objects.requireNonNull(stringRedisTemplate.execute(new DefaultRedisScript<>("return #redis.call('keys', KEYS[1])", Long.class), Collections.singletonList(prefix + ":[zset]*")));
        totalSize += Objects.requireNonNull(stringRedisTemplate.execute(new DefaultRedisScript<>("return #redis.call('keys', KEYS[1])", Long.class), Collections.singletonList(prefix + ":[hash]*")));
        return totalSize;
    }

    @Override
    public boolean expire(String key, long timeout, TimeUnit timeUnit) throws CacheServiceException {
        return Boolean.TRUE.equals(stringRedisTemplate.expire(key, timeout, timeUnit));
    }
}
