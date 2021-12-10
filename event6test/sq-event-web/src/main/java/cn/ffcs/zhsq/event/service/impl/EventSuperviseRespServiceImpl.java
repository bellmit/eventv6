package cn.ffcs.zhsq.event.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.ffcs.common.message.bo.ReceiverBO;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.event.service.IEventSuperviseRespService;
import cn.ffcs.zhsq.mybatis.persistence.event.EventSuperviseRespMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;

public class EventSuperviseRespServiceImpl implements IEventSuperviseRespService{

	// 消息发送模块
	@Autowired
	private MessageOutService messageService;
	
	@Autowired
	private EventSuperviseRespMapper eventSuperviseRespMapper;

	
	@Override
	public boolean saveOrUpdateSuperviseRespMsg(Map<String, Object> params) {
		boolean result=true;
		Long remindId=0L;
		Long userId=0L;
		Long msgId=0L;
		String replyMsg="";
		
		if(CommonFunctions.isNotBlank(params, "ownDataId")){
			remindId=(Long) params.get("ownDataId");
		}else{
			return false;
		}
		
		//获取被督办人Id
		if(CommonFunctions.isNotBlank(params, "msgSupervisedId")){
			userId=(Long) params.get("msgSupervisedId");
		}else{
			return false;
		}
		
		//获取消息列表id
		Map<String, Object> findMsgInfo = this.findMsgInfo(remindId,userId);
		
		if(CommonFunctions.isNotBlank(findMsgInfo, "MSGID")){
			msgId=(Long) findMsgInfo.get("MSGID");
		}
		//获取消息回复内容
		if(CommonFunctions.isNotBlank(params, "msgRespContent")){
			replyMsg=(String) params.get("msgRespContent");
		}else{
			return false;
		}
		if(result){
			result = messageService.replyMessage(msgId, userId, replyMsg);
		}
		
		return result;
	}

	@Override
	public Map<String, Object> findMsgInfo(Long remindId,Long supervisedId) {
		
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("ownDataId", remindId);
		params.put("supervisedId", supervisedId);
		
		return eventSuperviseRespMapper.findMsgInfo(params);
	}

}
