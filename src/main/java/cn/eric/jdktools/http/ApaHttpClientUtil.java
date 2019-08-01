package cn.eric.jdktools.http;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author BinCain
 * @version V1.0
 * @Description: 使用apache工具类远程调用接口
 * @date 2017/11/14 0014 14:19
 */
public class ApaHttpClientUtil {

    /**
     * APACHE 处理日志工具类
     */
    private static Logger logger = Logger.getLogger(ApaHttpClientUtil.class);

    /**
     * @return void
     * @method postBodyString
     * @author BinCain
     * @date 2017/11/14 0014 13:38
     * @Description: apache httpClient apply request(POST)
     */
    public static String postBodyString(String reqUrl, String reqStr) throws Exception {

        // https 协议添加信任
        Protocol protocol = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", protocol);

        // 返回报文
        String resultStr;
        HttpClient httpClient = new HttpClient();
        logger.info(reqUrl + " 调用开始========");
        // todo 测试结束上生产时 需要根据实际业务需求进行设置
        // 等待连接时间
//        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
        // 读取响应时间
//        httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
        PostMethod postMethod = new PostMethod(reqUrl);
        postMethod.setRequestEntity(new StringRequestEntity(reqStr, "application/json", "UTF-8"));
        postMethod.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        int statusCode = httpClient.executeMethod(postMethod);
        // 注意：经测试 如果是这里报错500,基本上是参数不对或者请求URL错误
        if (statusCode != HttpStatus.SC_OK)
            logger.info(postMethod.getStatusLine());
        // 获取返回体信息
        // 第一种方式(有长度限制)
        //resultStr = postMethod.getResponseBodyAsString();
        // 第二种方式(返回流转成string,无长度限制)
        resultStr = convertStreamToString(postMethod.getResponseBodyAsStream());

        if (postMethod != null) {
            postMethod.releaseConnection();
        }
        logger.info(reqUrl + " 调用成功====end====");
        return resultStr;
    }

    /**
     * trans InputStream to string
     */
    public static String convertStreamToString(InputStream is) {
         /*
          * To convert the InputStream to String we use the BufferedReader.readLine()
          * method. We iterate until the BufferedReader return null which means
          * there's no more data to read. Each line will appended to a StringBuilder
          * and returned as String.
          */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            logger.info(e.toString());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                logger.info(e.toString());
            }
        }
        return sb.toString();
    }
}
