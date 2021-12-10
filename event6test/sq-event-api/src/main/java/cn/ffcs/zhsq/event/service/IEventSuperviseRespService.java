package cn.ffcs.zhsq.event.service;

import java.util.Map;

/**
 * @Description:督办消息回复
 * @Author: youwj
 */
public interface IEventSuperviseRespService{


    /**
     * 保存督办信息的回复消息
     * @param 
     *  Long msgId 消息Id;
		Long userId 用户Id(被发送人的Id);
		String replyMsg 消息回复消息;
     * @return
     */
    boolean saveOrUpdateSuperviseRespMsg(Map<String,Object> params);
    
    
    /**
     * 根据督办信息Id与被督办人Id查找消息IdmsgId
     * @param 
     *  remindId 督办信息Id
     *  supervisedId 被督办人Id
     * @return
     */
    public Map<String,Object> findMsgInfo(Long remindId,Long supervisedId);


}
