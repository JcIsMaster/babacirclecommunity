package com.example.babacirclecommunity.common.utils;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;


import javax.annotation.Resource;
import java.time.Duration;

/**
 * @author JC
 * @date 2021/6/1 19:33
 */
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (o, method, objects) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(o.getClass().getName()).append(".");
            sb.append(method.getName()).append(".");
            for (Object obj : objects) {
                sb.append(obj.toString());
            }
            System.out.println("keyGenerator=" + sb.toString());
            return sb.toString();
        };
    }

    @Bean
    @Override
    public CacheResolver cacheResolver() {
        return new SimpleCacheResolver(cacheManager());
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        // 用于捕获从Cache中进行CRUD时的异常的回调处理器。
        return new SimpleCacheErrorHandler();
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        // --------------2.x的配置方式--------------
        // 方式一：直接create
        //RedisCacheManager redisCacheManager = RedisCacheManager.create(redisConnectionFactory());
        // 方式二：builder方式（推荐）

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(1)) //Duration.ZERO表示永不过期（此值一般建议必须设置）
                //.disableKeyPrefix() // 禁用key的前缀
                .disableCachingNullValues() //禁止缓存null值
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

                //=== 前缀我个人觉得是非常重要的，建议约定：注解缓存一个统一前缀、RedisTemplate直接操作的缓存一个统一前缀===
                //.prefixKeysWith("baidu:") // 底层其实调用的还是computePrefixWith() 方法，只是它的前缀是固定的（默认前缀是cacheName，此方法是把它固定住，一般不建议使用固定的）
                //.computePrefixWith(CacheKeyPrefix.simple()); // 使用内置的实现
                //.computePrefixWith(cacheName -> "caching:" + cacheName) // 自己实现，建议这么使用(cacheName也保留下来了)
                ;

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory)
                // .disableCreateOnMissingCache() // 关闭动态创建Cache
                //.initialCacheNames() // 初始化时候就放进去的cacheNames（若关闭了动态创建，这个就是必须的）
                .cacheDefaults(configuration) // 默认配置（强烈建议配置上）。  比如动态创建出来的都会走此默认配置
                //.withInitialCacheConfigurations() // 个性化配置  可以提供一个Map，针对每个Cache都进行个性化的配置（否则是默认配置）
                //.transactionAware() // 支持事务
                .build();
        return redisCacheManager;
    }



    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
