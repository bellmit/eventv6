package cn.ffcs.zhsq.decisionMaking;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.gmis.mybatis.domain.userscore.UserDetailScore;
import cn.ffcs.gmis.userscore.service.UserScoreService;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description:西藏昌都事件状态决策类
 * @Author: youwj
 * @Date: 2021/1/7 15:41
 */
@Service(value = "eventStatusDecisionMaking4XZCDService")
public class EventStatusDecisionMaking4XZCDServiceImpl extends EventStatusDecisionMakingServiceImpl {
	
	@Autowired
	private MessageOutService messageService;
	
	private static final String EVENT_MEG_MODULE_CODE = "02";	//事件消息所属模块编码

	private static final String END_NODE_CODE = "end1";			//归档环节
    
    @Override
	public String makeDecision(Map<String, Object> params) throws Exception {
    	Long eventId = -1L,			//事件id
   			 userId = -1L,			//事件操作人员Id
   			 orgId = -1L;			//事件操作人员所属组织
   		
   		String eventStatus = "",	//事件状态
   			   curNodeCode = "",	//当前环节节点编码
   			   nextNodeCode = "";	//下一环节节点编码
   		
   		Date handleDate = null;		//环节办理时限
   		
   		if(CommonFunctions.isNotBlank(params, "eventId")) {
   			try{
   				eventId = Long.valueOf(params.get("eventId").toString());
   			} catch(NumberFormatException e) {
   				throw new NumberFormatException("参数[eventId]："+params.get("eventId")+" 不能转换为Long型！");
   			}
   		} else {
   			throw new IllegalArgumentException("缺失参数[eventId]，请检查！");
   		}
   		
   		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
   			curNodeCode = params.get("curNodeCode").toString();
   		} else {
   			throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
   		}
   		
   		if(CommonFunctions.isNotBlank(params, "nextNodeCode")) {
   			nextNodeCode = params.get("nextNodeCode").toString();
   		} else {
   			throw new IllegalArgumentException("缺失参数[nextNodeCode]，请检查！");
   		}
   		
   		if(CommonFunctions.isNotBlank(params, "userId")) {
   			try{
   				userId = Long.valueOf(params.get("userId").toString());
   			} catch(NumberFormatException e) {
   				throw new NumberFormatException("参数[userId]："+params.get("userId")+" 不能转换为Long型！");
   			}
   		} else {
   			throw new IllegalArgumentException("缺失参数[userId]，请检查！");
   		}
   		
   		if(CommonFunctions.isNotBlank(params, "orgId")) {
   			try{
   				orgId = Long.valueOf(params.get("orgId").toString());
   			} catch(NumberFormatException e) {
   				throw new NumberFormatException("参数[orgId]："+params.get("orgId")+" 不能转换为Long型！");
   			}
   		} else {
   			throw new IllegalArgumentException("缺失参数[orgId]，请检查！");
   		}
   		
   		if(CommonFunctions.isNotBlank(params, "handleDate")) {
   			handleDate = (Date)params.get("handleDate");
   		}
   		
   		eventStatus = this.updateEventStatus(eventId, userId, orgId, "", curNodeCode, nextNodeCode, handleDate, params);
   		
   		if(END_NODE_CODE.equals(nextNodeCode)) {//事件归档时，新增个人积分信息
   			addUserDetailScore(eventId);
   			//事件归档时，西藏昌都将所有消息中心的消息抹除
   			batchReadMessage(eventId);
   		}
		
		return eventStatus;
	}
	
    /**
     * 批量读取事件相关消息
     * @param eventId	事件id
     */
    private void batchReadMessage(Long eventId) {
    	if(eventId != null && eventId > 0) {
	    	Map<String, Object> params = new HashMap<String, Object>();
	    	
	    	params.put("bizId", eventId);
	    	params.put("moduleCode", EVENT_MEG_MODULE_CODE);
	    	
	    	messageService.batchReadMessage(params);
    	}
    }

}
