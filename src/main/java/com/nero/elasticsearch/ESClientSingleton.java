package com.nero.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 单例模式
 * 为elasticsearch提供客户client
 * ...
 * <p>
 * author: nero
 * date: 2017/6/15
 * time: 15:56
 */
@Component
public class ESClientSingleton {

    private static Logger logger = LoggerFactory.getLogger(ESClientSingleton.class);

    /**
     * 配置节点列表
     */
    @Value("${info.elasticsearch.properties.urls}")
    public String urls;

    /**
     * 集群名称
     */
    @Value("${info.elasticsearch.properties.cluster}")
    public String clusterName;

    /**
     * 节点端口号
     */
    @Value("${info.elasticsearch.properties.port}")
    public int port;

    /**
     * 解析配置文件中节点地址列表
     */
    private static List<String> staticUrlList;

    /**
     * 解析集群名称
     */
    private static String staticClusterName;

    /**
     * 解析节点端口号
     */
    private static int staticPort;

    private static TransportClient client;

    @PostConstruct
    public void init() {
        // 解析配置文件中节点地址`
        if(!Strings.isNullOrEmpty(urls)){
            staticUrlList = new ArrayList<String>();
            String[] urlsArgs = urls.split(",");
            Collections.addAll(staticUrlList, urlsArgs);
        }
        staticClusterName = clusterName;
        staticPort = port;
    }

    public static TransportClient get() {

        if (client == null) {
            synchronized (ESClientSingleton.class) {
                if (client == null) {
                    Settings settings = Settings.builder().put("cluster.name", staticClusterName).build();
                    client = new PreBuiltTransportClient(settings);
                    for (String url : staticUrlList) {
                        try {
                            InetAddress inetAddress = InetAddress.getByName(url);
                            client.addTransportAddress(new InetSocketTransportAddress(inetAddress, staticPort));
                        } catch (UnknownHostException e) {
                            logger.error("elasticsearch客户端构造失败!",e);
                        }
                    }
                }
                return client;
            }
        }
        return client;
    }
}
