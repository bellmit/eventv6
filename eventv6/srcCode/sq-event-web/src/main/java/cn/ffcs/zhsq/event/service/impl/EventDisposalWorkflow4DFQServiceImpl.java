package cn.ffcs.zhsq.event.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.decisionMaking.EventStatusDecisionMaking4DFQServiceImpl;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExpand.service.IEventDisposal4ExpandService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 江苏省盐城市 大丰区(DFQ) 工作流使用接口实现（新图）
 * @author zhangls
 *
 */
@Service(value = "eventDisposalWorkflow4DFQService")
public class EventDisposalWorkflow4DFQServiceImpl extends EventDisposalWorkflow4YCHSHServiceImpl {
	@Autowired
	private IEventDisposalService eventDisposalService;

	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;

	@Autowired
	private IBaseWorkflowService baseWorkflowService;

	@Autowired
	private EventStatusDecisionMaking4DFQServiceImpl eventStatusDecisionMaking4DFQService;
	
	@Autowired
	private EventDisposalWorkflowForEventServiceImpl eventDisposalWorkflowForEventService;
	
	

	//事件扩展信息接口
	@Autowired
	private IEventDisposal4ExpandService eventDisposalExpandService;

	private static final String EVENT_MEG_MODULE_CODE = "02";//事件消息所属模块编码

	private static final String CLOSE_NODE_CODE = "task8";//结案环节编码
	private static final String _12345_NODE_CODE = "task7";//12345平台处理环节编码
	private static final String DISTRICT_NODE_CODE = "task16";//12345平台处理过后的县(区)处理环节编码
	private static final String CAIJI_NODE_CODE = "task1";//采集环节节点编码
	private static final String END_NODE_CODE = "end1";//归档环节节点编码
	private static final String REJECT_CODE = "2";//驳回操作编码

	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String instanceId = null;

		//采集并结案时，发送消息
		if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose)) {

			instanceId = super.startFlowByWorkFlow(eventId, workFlowId, ConstantValue.WORKFLOW_IS_NO_CLOSE, userInfo, extraParam);

			//对采集并结案的事件进行归档
			if (baseWorkflowService.endWorkflow4Base(Long.valueOf(instanceId),userInfo,extraParam)){
				Map<String,Object> params = new HashMap<>();
				params.put("decisionService","eventStatusDecisionMaking4DFQService");
				params.put("eventId",eventId);
				params.put("curNodeCode",END_NODE_CODE);
				params.put("nextNodeCode",END_NODE_CODE);
				params.put("userId",userInfo.getUserId());
				params.put("orgId",userInfo.getOrgId());
				//更新事件状态为结束
				eventStatusDecisionMaking4DFQService.makeDecision(params);

				//采集并结案的事件默认添加评价信息，评价等级为：满意
				String evaLevel = "02";
				String evaContent = "满意";
				try {
					eventDisposalExpandService.saveOrUpdateEventEvaluate(userInfo,eventId,evaLevel,evaContent,params);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else {
			instanceId = super.startFlowByWorkFlow(eventId, workFlowId, toClose, userInfo, extraParam);
		}

		return instanceId;
	}

	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{

		/*System.out.println("开始结案： nextNodeName:" + nextNodeName + " ,nextStaffId:" + nextStaffId + " ,curOrgIds:" + " ,userOrgCode" + userInfo.getOrgCode() +" ,userOrgId" + userInfo.getOrgId() + " ,userOrgName" + userInfo.getOrgName());*/

		//当下一环节为结案环节时，置空下一办理人员信息，否则和12345平台对接时会报错
		if(CLOSE_NODE_CODE.equals(nextNodeName)){
			nextStaffId = "";
		}

		boolean flag = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);

		if(flag) {
			Long eventId = -1L;

			if(CommonFunctions.isNotBlank(extraParam, "formId")) {
				try {
					eventId = Long.valueOf(extraParam.get("formId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}

			if(eventId < 0) {
				ProInstance pro = this.capProInstanceByInstanceId(instanceId);
				if(pro != null) {
					eventId = pro.getFormId();
				}
			}

			//当前环节为结案环节时，将事件扭转至进行结案操作环节的上一个环节，
			// 在扭转成功后的环节办理时需要判断历史环节是否包含结案环节，人为构造后面办理环节信息
			if (CLOSE_NODE_CODE.equals(nextNodeName)) {
				//最近办理的任务信息
				Map<String,Object> taskMap = null,//实施结案操作的环节任务信息
						preTaskMap = null,//结案后需要回到的节点的任务信息-->用来获取办理人员信息和组织信息
						curNodeMap = super.curNodeData(instanceId);
				String preNodeName = null,//结案后需要回到的节点的节点名称
						preOrgId = null,
						preStaffId = null,
						curTaskId = null;
				List<Map<String,Object>> instanceDetailList = super.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC,userInfo);
				List<String> preTaskIdList = new ArrayList<>();
				//去除历史环节中所有的驳回环节信息
				for (Map<String,Object> map:instanceDetailList) {
					//获取驳回环节的 PRE_TASK_ID，去除所有 PRE_TASK_ID 为驳回环节的PRE_TASK_ID环节
					if (CommonFunctions.isNotBlank(map,"TASK_CODE")
							&& REJECT_CODE.equals(String.valueOf(map.get("OPERATE_TYPE")))) {
						preTaskIdList.add(String.valueOf(map.get("TASK_ID")));
					}
				}

				//1.去除历史环节中所有的驳回环节信息；2.去除12345平台的部门处理环节（对接时插入的假的环节信息，待完成）
				if (preTaskIdList.size() > 0){
					for (String preTaskId:preTaskIdList) {
						Iterator iterator = instanceDetailList.iterator();
						while(iterator.hasNext()){
							Map<String,Object> map = (Map<String, Object>) iterator.next();
							if (preTaskId.equals(String.valueOf(map.get("PRE_TASK_ID"))) || preTaskId.equals(String.valueOf(map.get("TASK_ID")))
									|| "12345平台".equals(String.valueOf(map.get("TASK_NAME")))){
								iterator.remove();
							}
						}
					}
				} else {//不存在驳回环节的，单独去除12345平台的部门处理环节
					Iterator iterator = instanceDetailList.iterator();
					while(iterator.hasNext()){
						Map<String,Object> map = (Map<String, Object>) iterator.next();
						if ("12345平台".equals(String.valueOf(map.get("TASK_NAME")))){
							iterator.remove();
						}
					}

				}


				//根据taskId查找上一个处理环节，过滤驳回操作环节信息
				if (null != instanceDetailList && instanceDetailList.size() > 0) {

					List<Map<String,Object>> taskInfoBeforeEnd = new ArrayList<>();
					//构造结案前环节节点信息
					Collections.reverse(instanceDetailList);
					for (Map<String,Object> map:instanceDetailList) {
						if (!CLOSE_NODE_CODE.equals(map.get("TASK_CODE"))) {
							taskInfoBeforeEnd.add(map);
						} else {
							Collections.reverse(taskInfoBeforeEnd);
							break;
						}
					}

					if (taskInfoBeforeEnd.size() >= 2) {
						preTaskMap = taskInfoBeforeEnd.get(1);

						//需要返回到的环节的环节名称、处理人员transactor、组织信息orgId
						if (CommonFunctions.isNotBlank(preTaskMap,"TASK_CODE")) {
							preNodeName = String.valueOf(preTaskMap.get("TASK_CODE"));
						}
					}

				}

				//根据当前环节信息，构造提交工作流时的参数
				if (CommonFunctions.isNotBlank(curNodeMap,"WF_DBID_")) {
					curTaskId = String.valueOf(curNodeMap.get("WF_DBID_"));
				}
				if (CommonFunctions.isNotBlank(preTaskMap,"ORG_ID")) {
					preOrgId = String.valueOf(preTaskMap.get("ORG_ID"));
				}
				if (CommonFunctions.isNotBlank(preTaskMap,"TRANSACTOR_ID")) {
					preStaffId = String.valueOf(preTaskMap.get("TRANSACTOR_ID"));
				}

				/*System.out.println("结案后开始推事件到节点：需要返回到的环节的环节名称 preNodeName:" + preNodeName + " ,nextStaffId:" + nextStaffId + " ,curOrgIds:" +
						" ,userOrgCode" + userInfo.getOrgCode() +" ,userOrgId" + userInfo.getOrgId() + " ,userOrgName" + userInfo.getOrgName() +"preStaffId" +preStaffId
						+ "preOrgId" + preOrgId);*/

				if (StringUtils.isNotBlank(preNodeName)) {//taskId：结案环节taskId，
					flag = super.subWorkFlowForEndingAndEvaluate(instanceId, curTaskId, preNodeName, preStaffId, preOrgId, advice, userInfo, smsContent, extraParam);
				}
			}

		}

		return flag;
	}

	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId, UserInfo userInfo, Map<String, Object> params) {

		Map<String,Object> resultMap = super.initFlowInfo(taskId, instanceId, userInfo, params);

		//获取历史环节（降序排列，根据当前环节信息获取需要返回到的节点信息），降序时当前环节会包含进来
		List<Map<String,Object>> taskMapList = super.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC,userInfo);
		Map<String,Object> curHandleMap = new HashMap<>();
		//处理事件环节展示信息，如果事件已经结案过了，下一办理环节仅展示当前环节之前的节点
		//是否已经结案过了，默认没有结案过(历史环节当中没有，要重新获取一遍事件，用事件的状态判断)
		Boolean endTaskExist = false,
				isSelfCollectAndHandle = false;
		Long eventId = -1L,
				creatorId = -1L,
				creatorOrgId = -1L;//事件采集人员所属的组织
		String END_STATUS = "03",
				EVENT_STATUS = "";
		List<Node> taskNodes = new ArrayList<>();
		List<String> preTaskIdList = new ArrayList<>();

		//去除历史环节中所有的驳回环节信息
		for (Map<String,Object> taskMap:taskMapList) {
			//获取驳回环节的 TASK_ID，去除所有 PRE_TASK_ID 或 TASK_ID 为驳回环节的 TASK_ID 的环节
			if (CommonFunctions.isNotBlank(taskMap,"TASK_CODE") /*&& CLOSE_NODE_CODE.equals(String.valueOf(taskMap.get("TASK_CODE")))*/
					&& REJECT_CODE.equals(String.valueOf(taskMap.get("OPERATE_TYPE")))) {
				preTaskIdList.add(String.valueOf(taskMap.get("TASK_ID")));
			}
		}

		//1.去除历史环节中所有的驳回环节信息；2.去除12345平台的部门处理环节（对接时插入的假的环节信息，待完成）
		if (preTaskIdList.size() > 0) {//存在驳回环节的
			for (String preTaskId:preTaskIdList) {
				Iterator iterator = taskMapList.iterator();
				while(iterator.hasNext()){
					Map<String,Object> map = (Map<String, Object>) iterator.next();
					if (preTaskId.equals(String.valueOf(map.get("PRE_TASK_ID"))) || preTaskId.equals(String.valueOf(map.get("TASK_ID")))
							|| "12345平台".equals(String.valueOf(map.get("TASK_NAME")))){
						/*System.out.println("去除的环节名称:" + String.valueOf(map.get("TASK_NAME")));*/
						iterator.remove();
					}
				}
			}
		} else {//不存在驳回环节的，单独去除12345平台的部门处理环节
			Iterator iterator = taskMapList.iterator();
			while(iterator.hasNext()){
				Map<String,Object> map = (Map<String, Object>) iterator.next();
				if ("12345平台".equals(String.valueOf(map.get("TASK_NAME")))){
					/*System.out.println("去除的环节名称:" + String.valueOf(map.get("TASK_NAME")));*/
					iterator.remove();
				}
			}

		}

		//获取流程实例-->根据流程实例获取事件实体
		if (instanceId > 0) {
			ProInstance pro = this.capProInstanceByInstanceId(instanceId);
			if(pro != null) {
				eventId = pro.getFormId();
				creatorOrgId = Long.valueOf(pro.getOrgId());
				creatorId = pro.getUserId();
			}
		}

		if (eventId > 0) {
			EventDisposal eventDisposal = eventDisposalService.findEventByIdSimple(eventId);


			if (null!= eventDisposal && null != eventDisposal.getStatus()) {
				EVENT_STATUS = eventDisposal.getStatus();
			}
			//事件已经归档
			if (END_STATUS.equals(EVENT_STATUS)) {
				endTaskExist = true;
			}
			//判断采集人和当前办理人员是不是同一组织同一人员
			if ( creatorId.equals(userInfo.getUserId()) && creatorOrgId.equals(userInfo.getOrgId()) ){
				isSelfCollectAndHandle = true;
			}
		}

		if (endTaskExist) {//事件已经结过案，构造返回的节点信息
			//获取历史环节（降序排列，根据当前环节信息获取需要返回到的节点信息），降序时当前环节会包含进来
			List<Map<String,Object>> taskInfoBeforeEnd = new ArrayList<>(),
					taskInfoAfterEnd = new ArrayList<>();
			Map<String,Object>	     curTaskMap = null,
					backToTaskMap = null;
			Integer nodeId = -1;
			Node node = null;

			//构造结案前环节节点信息
			Collections.reverse(taskMapList);
			for (Map<String,Object> map:taskMapList) {
				if (!CLOSE_NODE_CODE.equals(map.get("TASK_CODE"))) {
					taskInfoBeforeEnd.add(map);
				} else {
					Collections.reverse(taskInfoBeforeEnd);
					break;
				}
			}

			Collections.reverse(taskMapList);
			//构造结案后环节节点信息
			for (Map<String,Object> map:taskMapList) {
				if (!CLOSE_NODE_CODE.equals(map.get("TASK_CODE"))) {
					taskInfoAfterEnd.add(map);
				} else {
					break;
				}
			}

			//结案过后需要回到的环节信息（回到的节点不是自己采集的）
			if (taskInfoAfterEnd.size() + 1 < taskInfoBeforeEnd.size()){
				backToTaskMap = taskInfoBeforeEnd.get(taskInfoAfterEnd.size() + 1);
			} else if (taskInfoAfterEnd.size() + 1 == taskInfoBeforeEnd.size()){
				//自己采集并且当前处理环节是自己处理
				backToTaskMap = taskInfoBeforeEnd.get(taskInfoAfterEnd.size());
			}


			//如果回到的处理节点是采集节点，则直接跳到归档节点
			if (CommonFunctions.isNotBlank(backToTaskMap,"TASK_CODE")
					&& CAIJI_NODE_CODE.equals(String.valueOf(backToTaskMap.get("TASK_CODE")))) {
				//获取归档节点
				if (CommonFunctions.isNotBlank(resultMap,"taskNodes")) {
					taskNodes = (List<Node>) resultMap.get("taskNodes");
					//去除非归档节点
					for (Node no:taskNodes) {
						if(END_NODE_CODE.equals(no.getNodeName())){
							node = no;
							break;
						}
					}
					//清空节点信息
					taskNodes.clear();
				}
			} else if (CommonFunctions.isNotBlank(backToTaskMap,"NODE_ID")
					&& !CAIJI_NODE_CODE.equals(String.valueOf(backToTaskMap.get("TASK_CODE")))) {//构造页面下一环节和办理人信息
				//下一环节 NODE_ID
				nodeId = Integer.valueOf(backToTaskMap.get("NODE_ID").toString());

				if (nodeId > 0) {
					node = eventDisposalWorkflowService.findNodeById(nodeId);
					if (null != node) {
						node.setTransitionCode("__P0" + node.getNodeCode());
					}
				} else {
					throw new IllegalArgumentException("获取参数【NODE_ID】出错，请检查！");
				}
			}

			//构造下一办理环节
			taskNodes.add(node);
			resultMap.put("taskNodes",taskNodes);
			//构造办理人信息
			resultMap.put("userIds", backToTaskMap.get("TRANSACTOR_ID"));
			resultMap.put("userNames", backToTaskMap.get("TRANSACTOR_NAME"));
			resultMap.put("orgIds", backToTaskMap.get("ORG_ID"));

			//去除驳回按钮
			List<OperateBean> operateLists = (List<OperateBean>) resultMap.get("operateLists");
			String REJECT_BTN_EVENT = "reject";//驳回按钮相应事件名称

			if(operateLists != null) {
				for(OperateBean operate : operateLists) {
					if(REJECT_BTN_EVENT.equals(operate.getOperateEvent())) {
						operateLists.remove(operate);
						break;
					}
				}
			}

			return  resultMap;
		} else {
			/*System.out.println("事件没有结过案！");*/
			//自己采集提交过后去除结案环节，只有归档（采集人和当前环节处理人员是同一个人）
			if (taskMapList.size() <= 3 && isSelfCollectAndHandle) {//去除结案环节
				/*System.out.println("只有归档节点！");*/
				if (CommonFunctions.isNotBlank(resultMap,"taskNodes")) {
					taskNodes = (List<Node>) resultMap.get("taskNodes");
					//去除结案节点
					Iterator nodeIterator = taskNodes.iterator();

					while (nodeIterator.hasNext()) {
						Node endNode = (Node) nodeIterator.next();
						if (CLOSE_NODE_CODE.equals(endNode.getNodeName())) {
							nodeIterator.remove();
						}
					}
				}
			} else if (CommonFunctions.isNotBlank(resultMap,"taskNodes")) {//事件没有结过案，并且采集人和当前环节处理人员不是同一个人，屏蔽归档节点
				taskNodes = (List<Node>) resultMap.get("taskNodes");
				//去除归档节点
				Iterator nodeIterator = taskNodes.iterator();

				while (nodeIterator.hasNext()) {
					Node endNode = (Node) nodeIterator.next();
					if (END_NODE_CODE.equals(endNode.getNodeName())) {
						nodeIterator.remove();
					}
				}
			}
			resultMap.put("taskNodes",taskNodes);
		}

		return resultMap;
	}
	
	@Override
	public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {
		int result = -1;
		StringBuffer msgWrong = new StringBuffer("");
		String orgType="";
		String orgLevel="";
		
		if(orgSocialInfo != null) {
			orgLevel = orgSocialInfo.getChiefLevel();
			orgType = orgSocialInfo.getOrgType();
			
			if(StringUtils.isNotBlank(orgLevel)) {
				if(ConstantValue.DISTRICT_ORG_LEVEL.compareTo(orgLevel) > 0) {
					msgWrong.append("组织层级不可超过县区级，请先检验！");
				}
			} else {
				msgWrong.append("当前组织层级不存在，请先检验！");
			}
			
			if(!(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(orgType)
					||String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgType))) {
				msgWrong.append("当前用户的组织类型有误：既不是单位，也不是部门。请先修正！");
			}
			
		} else {
			msgWrong.append("当前组织不存在，请先检验！");
		}
		
		if(msgWrong.length() > 0) {
			throw new ZhsqEventException(msgWrong.toString());
		} else {
			if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(orgType)
					&&(orgLevel.equals(ConstantValue.COMMUNITY_ORG_LEVEL)||orgLevel.equals(ConstantValue.GRID_ORG_LEVEL))) {
				result = eventDisposalWorkflowForEventService.checkUserToStartWorkflow(orgSocialInfo, userId);
			}else {
				result = 1;
			}
		}
		
		return result;
	}
	

}
