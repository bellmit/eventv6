package cn.ffcs.zhsq.eventExpand.service.impl;

import cn.ffcs.common.message.bo.ReceiverBO;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.persistence.event.EventDisposalMapper;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "eventDisposalExpandForWNServiceImpl")
public class EventDisposalExpandForWNServiceImpl extends EventDisposalExpandBaseServiceImpl {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private EventDisposalMapper eventDisposalMapper;

	//字典服务
	@Autowired
	private IBaseDictionaryService dictionaryService;

	//功能配置
	@Autowired
	private IFunConfigurationService configurationService;

	//事件-工作流服务
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;

	// 消息发送模块
	@Autowired
	private MessageOutService messageService;

	@Override
	public boolean updateEventDisposal(EventDisposal event, Map<String, Object> params,UserInfo userInfo) {

		// 事件紧急程度变动发送消息发送消息
		this.sendUrgencyMsg(event, userInfo);

		boolean result = super.updateEventDisposal(event,params,userInfo);

		return result;
	}

	// 事件等级消息发送
	private void sendUrgencyMsg(EventDisposal event, UserInfo userInfo) {
		try {
			String isSendUrgencyMsg = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE,
					ConstantValue.SEND_URGENCY_MSG, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(),
					IFunConfigurationService.CFG_ORG_TYPE_0);
			if (null != isSendUrgencyMsg && isSendUrgencyMsg.equals("1")) {

				EventDisposal oriEvent = eventDisposalMapper.findById(event.getEventId());

				// 事件原来的紧急程度
				String oriLevel = oriEvent.getUrgencyDegree();
				// 事件现在的紧急程度
				String curLevel = event.getUrgencyDegree();

				if (!oriLevel.equals(curLevel)) {// 如果时间紧急程度没有改变则不需要发送消息
					List<BaseDataDict> dataDictListByDictCode = dictionaryService
							.getDataDictTree(ConstantValue.URGENCY_DEGREE_PCODE, userInfo.getOrgCode());

					Map<String, String> codeMap = new HashMap<String, String>();
					for (BaseDataDict var : dataDictListByDictCode) {
						codeMap.put(var.getDictGeneralCode(), var.getDictName());
					}

					if (oriEvent.getCreatorId() != userInfo.getUserId()) {// 如果是修改自己的事件则不需要发送消息
						this.sendMessage(oriEvent, userInfo, codeMap, oriLevel, curLevel);
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void sendMessage(EventDisposal event, UserInfo userInfo, Map<String, String> codeMap, String oriLevel,
			String curLevel) {
		ReceiverBO receiver = new ReceiverBO();
		List<Long> nextUserIdList = new ArrayList<Long>();
		ProInstance pro = eventDisposalWorkflowService.capProInstanceByEventId(event.getEventId());

		String EVENT_MEG_MODULE_CODE = "02", // 事件消息所属模块编码
				EVENT_VIEW_LINK = "$EVENT_DOMAIN/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&eventId=", // 事件消息查看详情链接
				EVENT_MSG_ACTION = "系统发送了", // 消息发送操作
				viewLink = null,
				msgContent = "您上报的【@eventName】已被" + userInfo.getOrgName() + "的" + userInfo.getPartyName() + "调整，由"
						+ codeMap.get(oriLevel) + "等级变更为" + codeMap.get(curLevel) + "等级";// 消息内容

		if (event != null) {
			viewLink = EVENT_VIEW_LINK + event.getEventId() + "&instanceId=" + pro.getInstanceId();
			msgContent = msgContent.replaceAll("@eventName", event.getEventName());

			nextUserIdList.add(Long.valueOf(event.getCreatorId()));

			receiver.setUserIdList(nextUserIdList);

			messageService.sendCommonMessageA(null, null, EVENT_MEG_MODULE_CODE,
					event.getEventId(), viewLink, msgContent, EVENT_MSG_ACTION, receiver);
		}

	}

}
