package com.itzy.commonservice.kits;

import lombok.Getter;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA
 * Redis简易工具类
 *
 * @author MiaoWoo
 * Created on 2018/7/18 18:43.
 */
@Configuration
@ConditionalOnProperty(value = "spring.redis.host")
public class RedisKit {
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Getter
    private List list;
    @Getter
    private Set set;

    public RedisKit(List list, Set set) {
        this.list = list;
        this.set = set;
    }

    public Boolean has(String key) {
        return redisTemplate.hasKey(key);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    public Long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    public void del(String pattern) {
        val keys = redisTemplate.keys(pattern);
        if (Objects.isNull(keys)) {return;}
        redisTemplate.delete(keys);
    }

    @Configuration
    public static class List {
        @Resource
        private RedisTemplate<String, String> redisTemplate;

        public void add(String key, String value) {
            redisTemplate.opsForList().leftPush(key, value);
        }

        public void del(String key, String value) {
            redisTemplate.opsForList().remove(key, 1, value);
        }

        public boolean contains(String key) {
            val ranges = redisTemplate.opsForList().range(key, 0, -1);
            if (Objects.isNull(ranges)) {return false;}
            return ranges.contains(key);
        }
    }

    @Configuration
    public static class Set {
        @Resource
        private RedisTemplate<String, String> redisTemplate;

        public void add(String key, String... value) {
            redisTemplate.opsForSet().add(key, value);
        }

        public void del(String key, String... value) {
            redisTemplate.opsForSet().remove(key, (Object[]) value);
        }

        public boolean contains(String key) {
            val ranges = redisTemplate.opsForSet().members(key);
            if (Objects.isNull(ranges)) {return false;}
            return ranges.contains(key);
        }
    }
}
