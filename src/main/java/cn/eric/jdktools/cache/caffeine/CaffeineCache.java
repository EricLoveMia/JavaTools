package cn.eric.jdktools.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description: caffeine工具
 * @author: 钱旭
 * @date: 2022-02-22 13:56
 **/
public class CaffeineCache {

    static final Cache<Object, Object> manualCache;

    static {
        manualCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .build();
    }
    /**
     * 获取缓存
     * @param key
     * @return
     */
    public static Object getById(Object key){
        return manualCache.getIfPresent(key);
    }

    /**
     * 添加缓存
     * @param key
     * @param value
     */
    public static void addById(Object key, Object value){
        manualCache.put(key, value);
    }

    /**
     * 删除缓存
     * @param key
     */
    public static void invalidate(Object key){
        manualCache.invalidate(key);
    }

}

