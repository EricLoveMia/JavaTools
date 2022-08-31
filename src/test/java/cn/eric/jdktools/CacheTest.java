package cn.eric.jdktools;

import cn.eric.jdktools.cache.ICache;
import cn.eric.jdktools.cache.caffeine.CaffeineService;
import cn.eric.jdktools.cache.guava.GuavaService;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description: 缓存测试
 * @author: 钱旭
 * @date: 2022-02-22 14:10
 **/
public class CacheTest {

    public static void main(String[] args) {
        ICache<String,String> cache = new CaffeineService<>();

        cache.put("test","test");
        String test = cache.getIfPresent("test");
        System.out.println(test);


        ICache<String,String> cacheG = new GuavaService<>();

        cacheG.put("test","test");
        String testG = cacheG.getIfPresent("test");
        System.out.println(testG);
    }
}
