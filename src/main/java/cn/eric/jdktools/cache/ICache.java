package cn.eric.jdktools.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description: 缓存公共接口
 * @author: 钱旭
 * @date: 2022-02-22 11:41
 **/
public interface ICache<K, V> {

    /**
     * 通过key获取缓存中的value，若不存在直接返回null
     */
    V getIfPresent(K key);

    /**
     * 添加缓存，若key存在，就覆盖旧值
     */
    void put(K key, V value);

    /**
     * 删除该key关联的缓存
     */
    void invalidate(K key);
}
