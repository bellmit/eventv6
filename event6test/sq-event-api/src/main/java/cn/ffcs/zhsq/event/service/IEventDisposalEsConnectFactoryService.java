package cn.ffcs.zhsq.event.service;

import org.elasticsearch.client.RestHighLevelClient;

/**
 * @Description:事件同步Es连接类
 * @Author: ztc
 * @Date: 2018/9/18 8:56
 */
public interface IEventDisposalEsConnectFactoryService {
    public RestHighLevelClient getHighLevelClient();
}
