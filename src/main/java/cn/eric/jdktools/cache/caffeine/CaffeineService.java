package cn.eric.jdktools.cache.caffeine;

import cn.eric.jdktools.cache.ICache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description: caffeine缓存服务实现类
 * @author: 钱旭
 * @date: 2022-02-22 14:11
 **/
public class CaffeineService<K,V> implements ICache<K,V> {

    @Override
    public V getIfPresent(K key) {
        return (V) CaffeineCache.getById(key);
    }

    @Override
    public void put(K key, V value) {
        CaffeineCache.addById(key,value);
    }

    @Override
    public void invalidate(K key) {
        CaffeineCache.invalidate(key);
    }
}
