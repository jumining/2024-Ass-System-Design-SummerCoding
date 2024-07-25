package org.landvibe.ass1.cache.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.landvibe.ass1.cache.annotation.CacheInLandvibe;
import org.landvibe.ass1.cache.annotation.CacheOutLandvibe;
import org.landvibe.ass1.cache.annotation.CachingLandvibe;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class CacheLandvibeAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    public CacheLandvibeAspect(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Around("@annotation(org.landvibe.ass1.cache.annotation.CachingLandvibe)")
    public Object caching(ProceedingJoinPoint joinPoint) throws Throwable {
        CachingLandvibe cachingLandvibe = getAnnotation(joinPoint, CachingLandvibe.class); // 현재 메서드에 적용된 애노테이션 가져옴

        String cacheKey = createCacheKey(cachingLandvibe.tableName(), cachingLandvibe.key());
        Object cachedData = redisTemplate.opsForValue().get(cacheKey);

        if (cachedData != null) {
            return cachedData;
        } else { // 캐시에 없을 경우 DB에서 데이터 조회
            Object result = joinPoint.proceed(); // 원래 메서드 실행(비즈니스 로직 실행) 데이터 조회한 결과 저장
            cacheData(cacheKey, result, cachingLandvibe.ttl()); // 캐시에 저장
            return result;
        }
    }

    @Before("@annotation(org.landvibe.ass1.cache.annotation.CacheOutLandvibe)")
    public void cacheout(JoinPoint joinPoint) {
        CacheOutLandvibe cacheoutLandvibe = getAnnotation(joinPoint, CacheOutLandvibe.class);

        redisTemplate.delete(createCacheKey(cacheoutLandvibe.tableName(), cacheoutLandvibe.key()));
    }

    @Around("@annotation(org.landvibe.ass1.cache.annotation.CacheInLandvibe)")
    public Object cacheinLandvibe(ProceedingJoinPoint joinPoint) throws Throwable {
        CacheInLandvibe cacheinLandvibe = getAnnotation(joinPoint, CacheInLandvibe.class);

        Object result = joinPoint.proceed(); // 디비에 먼저 저장(비즈니스 로직)

        cacheData(createCacheKey(cacheinLandvibe.tableName(), cacheinLandvibe.key()),
                result,
                cacheinLandvibe.ttl());

        return result;
    }

    private <T extends Annotation> T getAnnotation(JoinPoint joinPoint, Class<T> annotationClass) {
        return getMethod(joinPoint).getAnnotation(annotationClass);
    }

    private Method getMethod(JoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod();
    }

    private String createCacheKey(String tableName, String key) {
        return tableName + ":" + key;
    }

    private void cacheData(String key, Object data, long ttl) {
        redisTemplate.opsForValue().set(key, data, ttl, TimeUnit.SECONDS);
    }
}
