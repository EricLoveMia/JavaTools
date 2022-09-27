package cn.eric.jdktools.loopcall;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description:
 * @author: eric
 * @date: 2022-08-31 14:07
 **/
@Slf4j
public class LoopWorker {

    private CloseableHttpClient httpClient;
    private ScheduledExecutorService executorService;

    public LoopWorker(String url, String dataId) {
        this.executorService = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("client.worker.executor-%d");
            thread.setDaemon(true);
            return thread;
        });
        // ① httpClient 客户端超时时间要大于长轮询约定的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(40000).build();
        this.httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();

        executorService.execute(new LongPollingRunnable(url, dataId));
    }

    class LongPollingRunnable implements Runnable {

        private final String url;
        private final String dataId;

        public LongPollingRunnable(String url, String dataId) {
            this.url = url;
            this.dataId = dataId;
        }

        @SneakyThrows
        @Override
        public void run() {
            String endpoint = url + "?dataId=" + dataId;
            log.info("endpoint: {}", endpoint);
            HttpGet request = new HttpGet(endpoint);
            CloseableHttpResponse response = httpClient.execute(request);
            switch (response.getStatusLine().getStatusCode()) {
                case 200: {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity()
                            .getContent()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                    response.close();
                    String configInfo = result.toString();
                    log.info("dataId: [{}] changed, receive configInfo: {}", dataId, configInfo);
                    break;
                }
                // ② 304 响应码标记配置未变更
                case 304: {
                    log.info("longPolling dataId: [{}] once finished, configInfo is unchanged, longPolling again", dataId);
                    break;
                }
                default: {
                    throw new RuntimeException("unExcepted HTTP status code");
                }
            }
            executorService.execute(this);
        }
    }

    public static void main(String[] args) throws IOException {

        new LoopWorker("http://127.0.0.1:8080/listener", "user");
        System.in.read();
    }

}
