package net.osandman.votingforrestaurants.aspect;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import net.osandman.votingforrestaurants.entity.HasId;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Aspect
@Slf4j
public class CacheLoggingAspect {
    private final CacheManager cacheManager;

    public CacheLoggingAspect(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Pointcut("within(net.osandman.votingforrestaurants.controller.DishController) || " +
            "within(net.osandman.votingforrestaurants.controller.RestaurantController)")
    public void controllerMethods() {
    }

    @AfterReturning("controllerMethods()")
    public void logCachedObjects() {
        List<String> cacheNames = cacheManager.getCacheNames().stream().toList();
        for (String cacheName : cacheNames) {
            log.info("Cache: " + cacheName);
            CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(cacheName);
            if (caffeineCache != null) {
                loggingCacheKeys(caffeineCache.getNativeCache());
            } else {
                log.warn("Can't get native cache");
            }
        }
    }

    private static void loggingCacheKeys(Cache<Object, Object> nativeCache) {
        log.info("Has keys: " + String.join(", ", nativeCache.asMap().values()
                .stream().map(key -> {
                    if (key instanceof List<?>) {
                        return String.join(", ", ((List<?>) key).stream()
                                .map(el -> String.valueOf(((HasId) el).id())).toList());
                    } else {
                        return String.valueOf(((HasId) key).id());
                    }
                }).toList()));
    }
}