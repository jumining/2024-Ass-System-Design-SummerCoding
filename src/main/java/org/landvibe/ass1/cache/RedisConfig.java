package org.landvibe.ass1.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {
    // 1. 캐시 매니저 : 캐시 관리와 관련된 기능 제공
    // 2. 레디스 템플릿 : 레디스에 직접적인 데이터 작업 수행

    @Bean
    // 레디스를 캐시 저장소로 사용하는데 필요한 설정 제공, 캐시 유효 기간, 직렬화 방법 등 정의
    // + RedisCacheManager : CacheManager 구현체 (->레디스를 캐시 저장소로 사용하는 Spring Cache의 CacheManager 설정)
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig() // 기본 캐시 구성 생성
                .entryTtl(Duration.ofHours(1)) // 캐시 유효 기간 1시간으로 설정
                .disableCachingNullValues() // null 값을 캐시에 저장하지 않도록 설정, 캐시의 무결성
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
                // 캐시 키와 값의 직렬화 방식을 설정 (키:문자열, 값:JSON)

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration) // 모든 캐시의 기본 구성 설정, 위에서 설정한 cacheConfiguration 사용
                .build();
    }

    @Bean
    // 레디스와의 데이터 상호작용을 위한 템플릿 설정, 직렬화 및 역직렬화 방법 정의, 레디스에 데이터를 읽고 쓰는데 필요한 설정 제공
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory); // 레디스 디비와의 연결 설정
        template.setKeySerializer(new StringRedisSerializer()); // 키를 문자열로 직렬화 (문자열 키를 레디스에 적합한 형식으로 직렬화)
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // 값을 JSON형식으로 직렬화
        return template;
    }
}
