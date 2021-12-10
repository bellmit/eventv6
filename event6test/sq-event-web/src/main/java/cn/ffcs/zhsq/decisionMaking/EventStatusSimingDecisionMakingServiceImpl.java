package cn.ffcs.zhsq.decisionMaking;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 思明区事件状态变更
 * 必填参数
 * 		eventId		事件id
 * 		curNodeCode	当前节点编码
 * 		nextNodeCode	下一节点编码
 * 		userId		办理人员id
 * 		orgId		办理人员所属组织
 * 		chiefLevel	办理人员所属组织层级         有流程实例时，使用采集人员所属组织的层级，否则使用传入的参数chiefLevel
 * 非必填参数
 * 		handleDate	环节处理时限，办结时为结案时间
 * 
 * @author zhangls
 *
 */
@Service(value = "eventStatusSimingDecisionMakingService")
public class EventStatusSimingDecisionMakingServiceImpl extends 
			EventStatusDecisionMakingServiceImpl {

	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	private static final String START_NODE_CODE = "start";							//事件流程启动
	//社区级流程图
	private static final String C_TOP_GRID_ADMIN_NODE_CODE = "task3";				//总网格长处理环节编码
	private static final String C_PARTY_NODE_CODE = "task4";						//街道党政办处理环节编码
	private static final String C_DISTRICT_HANDLE_NODE_CODE = "task10";				//区指挥中心办理环节编码
	private static final String C_STREET_CONFIRM_NODE_CODE = "task6";				//街道党政办确认环节编码
	private static final String C_DISTRICT_CONFIRM_NODE_CODE = "task12";			//区指挥中心确认环节编码
	private static final String C_COMMUNITY_EVA_NODE_CODE = "task7,task9,task13";	//社区评价环节编码
	private static final String C_STREET_PARTY_EVA_NODE_CODE = "task8";				//街道党政办评价环节编码
	private static final String C_DISTRICT_EVA_NODE_CODE = "task14";				//区指挥中心评价环节编码
	private static final String C_END_NODE_CODE = "end1,end2,end3";					//办结环节编码
	
	//街道级流程图
	private static final String S_TOP_GRID_ADMIN_NODE_CODE = "task4";				//总网格长处理环节编码
	private static final String S_PARTY_NODE_CODE = "task1";						//街道党政办处理环节编码
	private static final String S_DISTRICT_HANDLE_NODE_CODE = "task10";				//区指挥中心办理环节编码
	private static final String S_TOP_GRID_ADMIN_CONFIRM_NODE_CODE = "task18";		//总网格长确认环节编码
	private static final String S_STREET_CONFIRM_NODE_CODE = "task6";				//街道党政办确认环节编码
	private static final String S_DISTRICT_CONFIRM_NODE_CODE = "task12";			//区指挥中心确认环节编码
	private static final String S_STREET_PARTY_EVA_NODE_CODE = "task9,task14,task17";				//街道党政办评价环节编码
	private static final String S_STREET_DEPARTMENT_EVA_NODE_CODE = "task3,task8,task13,task16";	//街道部门评价环节编码
	private static final String S_DISTRICT_EVA_NODE_CODE = "task15";				//区指挥中心评价环节编码
	private static final String S_END_NODE_CODE = "end1,end2,end3,end4";			//办结环节编码
	
	//区级流程图
	private static final String D_TOP_GRID_ADMIN_NODE_CODE = "";				//总网格长处理环节编码
	private static final String D_PARTY_NODE_CODE = "";						//街道党政办处理环节编码
	private static final String D_DISTRICT_HANDLE_NODE_CODE = "";				//区指挥中心办理环节编码
	private static final String D_TOP_GRID_ADMIN_CONFIRM_NODE_CODE = "";			//总网格长确认环节编码
	private static final String D_STREET_CONFIRM_NODE_CODE = "";				//街道党政办确认环节编码
	private static final String D_DISTRICT_CONFIRM_NODE_CODE = "";			//区指挥中心确认环节编码
	private static final String D_COMMUNITY_EVA_NODE_CODE = "";	//社区评价环节编码
	private static final String D_STREET_PARTY_EVA_NODE_CODE = "";				//街道党政办评价环节编码
	private static final String D_STREET_DEPARTMENT_EVA_NODE_CODE = "";				//街道部门评价环节编码
	private static final String D_DISTRICT_EVA_NODE_CODE = "";				//区指挥中心评价环节编码
	private static final String D_END_NODE_CODE = "";					//办结环节编码
	
	private static final String handleStatus = "00";	//事件状态——待处理
	private static final String handlingStatus = "01";	//事件状态——处理中
	private static final String confirmStatus = "02";	//事件状态——待确认
	private static final String evaluateStatus = "03";	//事件状态——待评价
	private static final String endStatus = "04";		//事件状态——已完成
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		Long eventId = -1L,			//事件id
			 userId = -1L,			//事件操作人员Id
			 orgId = -1L;			//事件操作人员所属组织
		
		String eventStatus = "",	//事件状态
			   curNodeCode = "",	//当前环节节点编码
			   nextNodeCode = "",	//下一环节节点编码
			   chiefLevel = "";		//组织层级，用于标识当前使用的流程图是哪个层级的
		
		ProInstance pro = null;		//流程实例id
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
		
		if(eventId > 0) {//优先使用流程实例中的采集人员所属组织
			pro = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
			if(pro != null) {
				Long createOrgId = pro.getOrgId();
				OrgSocialInfoBO orgInfo = null;
				if(createOrgId != null && createOrgId > 0) {
					try {
						orgInfo = orgSocialInfoOutService.findByOrgId(createOrgId);
					} catch(NumberFormatException e) {
						throw new NumberFormatException("事件流程实例中，采集人员组织编码："+createOrgId+" 不能转换为Long型！");
					}
					
					if(orgInfo != null) {
						chiefLevel = orgInfo.getChiefLevel();
					} else {
						throw new Exception("事件流程实例中，采集人员组织编码："+createOrgId+" 没有对应的组织信息！");
					}
				}
			}
		}
		
		if(StringUtils.isBlank(chiefLevel)) {
			if(CommonFunctions.isNotBlank(params, "chiefLevel")) {
				chiefLevel = params.get("chiefLevel").toString();
			} else {
				throw new IllegalArgumentException("缺失参数[chiefLevel]，请检查！");
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "handleDate")) {
			handleDate = (Date)params.get("handleDate");
		}
		
		eventStatus = this.updateEventStatus(eventId, userId, orgId, chiefLevel, curNodeCode, nextNodeCode, handleDate, params);
		
		return eventStatus;
	}

	/**
	 * 依据组织层级初始化事件状态Map
	 * @param chiefLevel
	 * @return
	 */
	protected Map<String, Object> initStatusMap(String chiefLevel) {
		Map<String, Object> statusMap = new HashMap<String, Object>();
		String gridLevel = "6";			//网格组织层级
		String communityLevel = "5";	//社区组织层级
		String SteetLevel = "4";		//街道组织层级
		String districtLevel = "3";		//区组织层级
		
		statusMap.put(START_NODE_CODE, handleStatus);		//事件启动流程
		if(gridLevel.equals(chiefLevel) || communityLevel.equals(chiefLevel)) {//网格级、社区级组织流程图
			statusMap.put("task3-task4", handlingStatus);	//总网格长办理——>街道党政办办理
			statusMap.put("task4-task3", handleStatus);		//街道党政办办理——>总网格长办理
			statusMap.put("task6", confirmStatus);			//街道党政办确认
			statusMap.put("task6-task5", handlingStatus);	//街道党政办确认——>街道部门办理 驳回操作
			statusMap.put("task12", confirmStatus);			//区指挥中心确认
			statusMap.put("task12-task11", handlingStatus);	//区指挥中心确认——>区部门办理  驳回操作
			statusMap.put("task7", evaluateStatus);			//社区评价
			statusMap.put("task9", evaluateStatus);			//社区评价
			statusMap.put("task13", evaluateStatus);		//社区评价
			statusMap.put("task8", evaluateStatus);			//街道党政办评价
			statusMap.put("task14", evaluateStatus);		//区指挥中心评价
			statusMap.put("end1", endStatus);				//办结
			statusMap.put("end2", endStatus);				//办结
			statusMap.put("end3", endStatus);				//办结
		} else if(SteetLevel.equals(chiefLevel)) {//街道级组织流程图
			statusMap.put("task4-task1", handleStatus);		//总网格长办理——>街道党政办办理
			statusMap.put("task1-task4", handlingStatus);	//街道党政办办理——>总网格长办理
			statusMap.put("task1-task7", handlingStatus);	//街道党政办办理——>街道部门办理
			statusMap.put("task7-task1", handleStatus);		//街道部门办理——>街道党政办办理
			statusMap.put("task1-task10", handlingStatus);	//街道党政办办理——>区指挥中心办理
			statusMap.put("task10-task1", handleStatus);	//区指挥中心办理——>街道党政办办理
			statusMap.put("task18", confirmStatus);			//总网格长确认
			statusMap.put("task18-task5", handlingStatus);	//总网格长确认——>网格员办理 驳回操作
			statusMap.put("task6", confirmStatus);			//街道党政办确认
			statusMap.put("task6-task7", handlingStatus);	//街道党政办确认——>街道部门办理  驳回操作
			statusMap.put("task12", confirmStatus);			//区指挥中心确认
			statusMap.put("task12-task11", handlingStatus);	//区指挥中心确认——>区部门处理 驳回操作
			statusMap.put("task9", evaluateStatus);			//街道党政办评价
			statusMap.put("task17", evaluateStatus);		//街道党政办评价
			statusMap.put("task14", evaluateStatus);		//街道党政办评价
			statusMap.put("task3", evaluateStatus);			//街道部门评价
			statusMap.put("task8", evaluateStatus);			//街道部门评价
			statusMap.put("task13", evaluateStatus);		//街道部门评价
			statusMap.put("task16", evaluateStatus);		//街道部门评价
			statusMap.put("task15", evaluateStatus);		//区指挥中心评价
			statusMap.put("end1", endStatus);				//办结
			statusMap.put("end2", endStatus);				//办结
			statusMap.put("end3", endStatus);				//办结
			statusMap.put("end4", endStatus);				//办结
		} else if(districtLevel.equals(chiefLevel)) {//区级组织流程图
			statusMap.put("task2-task4", handlingStatus);	//区指挥中心办理——>街道党政办办理
			statusMap.put("task4-task2", handleStatus);		//街道党政办办理——>区指挥中心办理
			statusMap.put("task2-task14", handlingStatus);	//区指挥中心办理——>区部门办理
			statusMap.put("task14-task2", handleStatus);	//区部门办理——>区指挥中心办理
			statusMap.put("task7", confirmStatus);			//总网格员确认
			statusMap.put("task7-task6", handlingStatus);	//总网格员确认——>网格员办理 驳回操作
			statusMap.put("task9", confirmStatus);			//街道党政办确认
			statusMap.put("task9-task8", handlingStatus);	//街道党政办确认——>街道部门办理 驳回操作
			statusMap.put("task10", confirmStatus);			//区指挥中心确认
			statusMap.put("task10-task4", handlingStatus);	//区指挥中心确认——>街道党政办办理 驳回操作
			statusMap.put("task15", confirmStatus);			//区指挥中心确认
			statusMap.put("task15-task14", handlingStatus);	//区指挥中心确认——>区部门办理 驳回操作
			statusMap.put("task3", evaluateStatus);			//区部门评价
			statusMap.put("task11", evaluateStatus);		//区部门评价
			statusMap.put("task16", evaluateStatus);		//区部门评价
			statusMap.put("task12", evaluateStatus);		//区指挥中心评价
			statusMap.put("task17", evaluateStatus);		//区指挥中心评价
			statusMap.put("task13", evaluateStatus);		//街道党政办评价
			statusMap.put("end1", endStatus);				//办结
			statusMap.put("end2", endStatus);				//办结
			statusMap.put("end3", endStatus);				//办结
			statusMap.put("end4", endStatus);				//办结
		}
		
		return statusMap;
	}
}
