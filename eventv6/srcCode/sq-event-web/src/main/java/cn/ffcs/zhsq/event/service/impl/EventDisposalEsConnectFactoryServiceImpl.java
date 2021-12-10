package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.zhsq.event.service.IEventDisposalEsConnectFactoryService;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Description:事件同步Es连接类
 * @Author: ztc
 * @Date: 2018/9/18 8:58
 */
@Service("eventDisposalEsConnectFactoryServiceImpl")
public class EventDisposalEsConnectFactoryServiceImpl implements IEventDisposalEsConnectFactoryService,InitializingBean, DisposableBean {

    @Value("${es.host.ip}")
    private String hostName;
    @Value("${es.host.port}")
    private int hostPort;

    private static final String SCHEMA = "http";
    private static final int CONNECT_TIME_OUT = 1000;
    private static final int SOCKET_TIME_OUT = 3000;
    private static final int CONNECTION_REQUEST_TIME_OUT = 500;

    private static final int MAX_CONNECT_NUM = 200;
    private static final int MAX_CONNECT_PER_ROUTE = 200;

    private static boolean uniqueConnectTimeConfig = false;
    private static boolean uniqueConnectNumConfig = true;

    private RestClientBuilder builder;
    private RestHighLevelClient restHighLevelClient;


    public void init(){
        builder = RestClient.builder(new HttpHost(hostName,hostPort,SCHEMA));

        if (uniqueConnectTimeConfig) {
            setConnectTimeOutConfig();
        }

        if (uniqueConnectNumConfig) {
            setMutiConnectConfig();
        }

        restHighLevelClient = new RestHighLevelClient(builder);
    }

    /**
     *异步httpclient连接延时配置
     * */
    public void setConnectTimeOutConfig(){

        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(CONNECT_TIME_OUT);
            requestConfigBuilder.setSocketTimeout(SOCKET_TIME_OUT);
            requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT);
            return requestConfigBuilder;
        });
    }

    /**
     * 异步httpclient连接数配置
     * */
    public void setMutiConnectConfig(){

        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(MAX_CONNECT_NUM);
            httpClientBuilder.setMaxConnPerRoute(MAX_CONNECT_PER_ROUTE);
            return httpClientBuilder;
        });

    }

    @Override
    public RestHighLevelClient getHighLevelClient() {
        if (restHighLevelClient == null) {
            init();
        }
        return restHighLevelClient;
    }

    public void close(){
        if (restHighLevelClient != null) {
            try {
                restHighLevelClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
