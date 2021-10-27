package cn.eric.jdktools.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

//import org.springframework.core.io.Resource;

/**
 * RestTemplateUtil 工具类
 *
 * @author YCKJ3213
 */
@Component
public class RestTemplateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateUtil.class);

    @Resource
    private RestTemplate restTemplate;

    /**
     * 封装请求头
     *
     * @param header
     * @return
     */
    private static HttpHeaders getHttpHeaders(JSONObject header) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Content-Encoding", "UTF-8");
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        if (header != null) {
            for (String key : header.keySet()) {
                httpHeaders.add(key, header.get(key).toString());
            }
        }
        return httpHeaders;
    }

    /**
     * get请求
     *
     * @param url
     * @param header
     * @param data
     * @return
     */
    public <T> T get(String url, JSONObject header, String data, Class<T> type) {
        return get(url, header, data, type, true);
    }

    public <T> T get(String url, JSONObject header, String data, Class<T> type, boolean printErrorLog) {
        try {
            ResponseEntity<JSONObject> response = restTemplate
                    .exchange(url, HttpMethod.GET, new HttpEntity(data, getHttpHeaders(header)), JSONObject.class);
            JSONObject result = response.getBody();
            result.put("status", response.getStatusCodeValue());
            LOGGER.debug("REST-GET结果, result->{}", result);
            return JSON.parseObject(result.toJSONString(), type);
        } catch (Exception e) {
            if (printErrorLog) {
                LOGGER.warn("get请求失败,请求地址为{},请求参数为{}", url, data);
            }
        }
        return null;
    }

    /**
     * get 请求
     *
     * @param url
     * @param type
     * @param <T>
     * @return
     */
    public <T> T get(String url, Class<T> type) {
        try {
            ResponseEntity<T> response = restTemplate.getForEntity(url, type);
            T result = response.getBody();
            LOGGER.debug("REST-GET结果, result->{}", result);
            return result;
        } catch (Exception e) {
            LOGGER.warn("get请求失败,请求地址为{}", url, e);
        }
        return null;
    }

    public String get(String url) {
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            LOGGER.warn("get请求失败,请求地址为{}", url, e);
            return null;
        }
    }

    public InputStream getForStream(String url) {
        try {
            ResponseEntity<org.springframework.core.io.Resource> entity = restTemplate.getForEntity(url, org.springframework.core.io.Resource.class);
            return entity.getBody().getInputStream();
        } catch (RestClientException | IOException e) {
            LOGGER.warn("get请求失败,请求地址为{}", url, e);
            return null;
        }
    }

    /**
     * post请求
     *
     * @param url
     * @param header
     * @param data
     * @return
     */
    public <T> T post(String url, JSONObject header, String data, Class<T> type) {

        try {
            ResponseEntity<JSONObject> response = restTemplate
                    .exchange(url, HttpMethod.POST, new HttpEntity(data, getHttpHeaders(header)), JSONObject.class);
            JSONObject result = response.getBody();
            result.put("status", response.getStatusCodeValue());
            LOGGER.debug("REST-POST结果, result->{}", result);
            return JSON.parseObject(result.toJSONString(), type);
        } catch (Exception e) {
            LOGGER.warn("post请求失败,请求地址为{},请求参数为{}", url, data, e);
        }
        return null;
    }

    /**
     * put请求
     *
     * @param url
     * @param header
     * @param data
     * @return
     */
    public <T> T put(String url, JSONObject header, String data, Class<T> type) {

        try {
            ResponseEntity<JSONObject> response = restTemplate
                    .exchange(url, HttpMethod.PUT, new HttpEntity(data, getHttpHeaders(header)), JSONObject.class);
            JSONObject result = response.getBody();
            result.put("status", response.getStatusCodeValue());
            LOGGER.debug("REST-POST结果, result->{}", result);
            return JSON.parseObject(result.toJSONString(), type);
        } catch (Exception e) {
            LOGGER.warn("post请求失败,请求地址为{},请求参数为{}", url, data, e);
        }
        return null;
    }
}
