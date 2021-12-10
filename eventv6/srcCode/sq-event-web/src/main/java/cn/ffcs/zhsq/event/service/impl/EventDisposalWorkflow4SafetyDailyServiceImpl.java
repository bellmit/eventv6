package cn.ffcs.zhsq.event.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 甘肃 平安日报 工作流使用接口实现 
 * @author zhangls
 *
 */
@Service(value = "eventDisposalWorkflow4SafetyDailyService")
public class EventDisposalWorkflow4SafetyDailyServiceImpl extends
		EventDisposalWorkflowForEventNewServiceImpl {
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	private static final String CLOSE_NODE_CODE = "task8";		//结案环节节点编码
	private static final String END_NODE_CODE = "end1";			//归档环节
	
	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String instanceId = "";
		Map<String, Object> userMap = new HashMap<String, Object>();
		
		if(StringUtils.isBlank(toClose)) {
			toClose = ConstantValue.WORKFLOW_IS_NO_CLOSE;
		}
		if(workFlowId == null || workFlowId < 0) {
			workFlowId = this.capWorkflowId(null, eventId, userInfo, null);
		}
		
		if(extraParam != null) {
			userMap.putAll(extraParam);
		}
		
		instanceId = super.startFlowByWorkFlow(eventId, workFlowId, toClose, userInfo, extraParam);
		
		if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose)) {//采集并归档
			Long instanceIdL = -1L;
			
			try {
				instanceIdL = Long.valueOf(instanceId);
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
			
			this.endWorkflow(instanceIdL, eventId, userInfo, extraParam);
		}
		
		
		return instanceId;
	}
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{
		boolean result = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		
		if(result && CLOSE_NODE_CODE.equals(nextNodeName)) {
			Map<String, Object> endParam = new HashMap<String, Object>();
			endParam.put("advice", advice);
			
			this.endWorkflow(instanceId, null, userInfo, endParam);
		}
		
		return result;
	}
	
	/**
	 * 将事件归档，并手动添加归档回调
	 * @param instanceId	流程实例id
	 * @param eventId		事件id
	 * @param userInfo		用户信息
	 * @param extraParam	额外参数
	 * 			advice		办理意见
	 */
	private void endWorkflow(Long instanceId, Long eventId, UserInfo userInfo, Map<String, Object> extraParam) {
		if(instanceId != null && instanceId > 0) {
			if(eventId == null) {
				ProInstance pro = this.capProInstanceByInstanceId(instanceId);
				
				if(pro != null) {
					eventId = pro.getFormId();
				}
			}
			
			try {
				boolean result = baseWorkflowService.endWorkflow4Base(instanceId, userInfo, extraParam);
				
				if(result) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
					
					LinkedHashMap<String, Date> duedate = new LinkedHashMap<String, Date>();
					duedate.put(END_NODE_CODE + "_" + END_NODE_CODE, new Date());
					
					resultMap.put("duedate", duedate);
					
					this.updateEventStatus(eventId, userInfo.getUserId(), orgInfo, resultMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}


