package cn.eric.jdktools.http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by liaohanhan
 * @classname UrlUtil
 * @description url解析工具
 * @date 2019/3/26 5:09 PM
 */
public class UrlUtil {

    public static class UrlEntity {
        /**
         * 基础url
         */
        public String baseUrl;
        /**
         * url参数
         */
        public Map<String, String> params;
    }

    /**
     * 解析url
     *
     * @param url
     * @return
     */
    public static UrlEntity parse(String url) {
        UrlEntity entity = new UrlEntity();
        if (url == null) {
            return entity;
        }
        url = url.trim();
        if (url.equals("")) {
            return entity;
        }
        String[] urlParts = url.split("\\?");
        entity.baseUrl = urlParts[0];
        //没有参数
        if (urlParts.length == 1) {
            return entity;
        }
        //有参数
        String[] params = urlParts[1].split("&");
        entity.params = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=");
            entity.params.put(keyValue[0], keyValue[1]);
        }

        return entity;
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        UrlEntity entity = parse(null);
        System.out.println(entity.baseUrl + "\n" + entity.params);
        entity = parse("http://www.123.com");
        System.out.println(entity.baseUrl + "\n" + entity.params);
        entity = parse("http://www.123.com?id=1");
        System.out.println(entity.baseUrl + "\n" + entity.params);
        entity = parse("https://mcar.tpi.cntaiping.com/mall/dispatch/order/payCallback?orderNo=5fc8c0d9-d0de-44cd-992e-3cc24df4807a&status=P_E&userCode=15266551100&clientName=H024003");
        System.out.println(entity.baseUrl + "\n" + entity.params);
    }

}
