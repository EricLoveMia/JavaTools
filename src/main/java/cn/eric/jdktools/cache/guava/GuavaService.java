package cn.eric.jdktools.cache.guava;

import cn.eric.jdktools.cache.ICache;
import cn.eric.jdktools.cache.caffeine.CaffeineCache;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description: caffeine缓存服务实现类
 * @author: 钱旭
 * @date: 2022-02-22 14:11
 **/
public class GuavaService<K,V> implements ICache<K,V> {

    @Override
    public V getIfPresent(K key) {
        return (V) GuavaCache.getById(key);
    }

    @Override
    public void put(K key, V value) {
        GuavaCache.addById(key,value);
    }

    @Override
    public void invalidate(K key) {
        GuavaCache.invalidate(key);
    }
}
