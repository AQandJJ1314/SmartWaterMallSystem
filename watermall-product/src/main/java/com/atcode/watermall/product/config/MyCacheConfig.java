package com.atcode.watermall.product.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * 和配置文件绑定的配置类  CacheProperties.class 原来是：
 *  @ConfigurationProperties(prefix = "spring.cache")
 *  public class CacheProperties { ...}
 *  使用以下注解让他能注册到spring的容器中，可以被使用
 *  @EnableConfigurationProperties(CacheProperties.class)
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)//拿到Redis在配置文件的配置
public class MyCacheConfig {
//    @Bean
//    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
//        System.out.println("Custom RedisCacheManager created......");
//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(redisCacheConfiguration(null))
//                .build();
//    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        System.out.println("redisCacheConfiguration method invoked......");
        //获取到配置文件中的配置信息
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();

        org.springframework.data.redis.cache.RedisCacheConfiguration config = org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig();
        //指定缓存序列化方式为json
        config = config.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        //设置配置文件中的各项配置，如过期时间
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }

        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        //验证过期时间
//        System.out.println("==============TTL from config: " + redisProperties.getTimeToLive()+"==================");
//        System.out.println("==============KeyPrefix from config: " + redisProperties.getKeyPrefix()+"==================");

        return config;
    }
}
