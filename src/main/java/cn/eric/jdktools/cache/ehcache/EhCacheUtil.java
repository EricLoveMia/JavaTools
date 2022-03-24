package cn.eric.jdktools.cache.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description: ehcache工具
 * @author: 钱旭
 * @date: 2022-02-22 14:06
 **/
public class EhCacheUtil {

    /**
     * @Description: 获取缓存
     * @param cacheName 缓存名字
     * @return Cache
     * @Autor: Jason
     */
    private static Cache getCache(String cacheName) {
        CacheManager cacheManager = CacheManager.getInstance();
        if (null == cacheManager) {
            return null;
        }
        Cache cache = cacheManager.getCache(cacheName);
        if (null == cache) {
            return null;
        }
        return cache;
    }

    /**
     * @Description:    新增缓存记录
     * @param cacheName 缓存名字
     * @param key       缓存key
     * @Autor: Jason
     */
    public static void put(String cacheName, String key, Object value) {
        Cache cache = getCache(cacheName);
        if (null != cache) {
            Element element = new Element(key, value);
            cache.put(element);
        }
    }

    /**
     * @Description:    删除缓存记录
     * @param cacheName 缓存名字
     * @param key       缓存key
     * @return boolean  是否成功删除
     * @Autor: Jason
     */
    public static boolean remove(String cacheName, String key) {
        Cache cache = getCache(cacheName);
        if (null == cache) {
            return false;
        }
        return cache.remove(key);
    }

    /**
     * @Description: 删除全部缓存记录
     * @param cacheName 缓存名字
     * @Autor: Jason
     */
    public static void removeAll(String cacheName) {
        Cache cache = getCache(cacheName);
        if (null != cache) {
            //logOnRemoveAllIfPinnedCache();
            cache.removeAll();
        }
    }

    /**
     * @Description: 获取缓存记录
     * @param cacheName  缓存名字
     * @param key        缓存key
     * @return Object    缓存记录数据Object
     * @Autor: Jason
     */
    public static Object get(String cacheName, String key) {
        Cache cache = getCache(cacheName);
        if (null == cache) {
            return null;
        }
        Element cacheElement = cache.get(key);
        if (null == cacheElement) {
            return null;
        }
        return cacheElement.getObjectValue();
    }


}

