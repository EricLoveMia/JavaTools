package cn.eric.jdktools.cache.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description: guava-cache工具类
 * @author: 钱旭
 * @date: 2022-02-22 13:59
 **/
public class GuavaCache {

    final static Cache<Object, Object> manualCache = CacheBuilder.newBuilder()
            //设置cache的初始大小为10，要合理设置该值
            .initialCapacity(10)
            //设置并发数为5，即同一时间最多只能有5个线程往cache执行写入操作
            .concurrencyLevel(5)
            //设置cache中的数据在写入之后的存活时间为10秒
            .expireAfterWrite(10, TimeUnit.SECONDS)
            //构建cache实例
            .build();

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
