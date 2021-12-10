package cn.ffcs.zhsq.decisionMaking;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.gmis.mybatis.domain.workcircle.WorkCircle;
import cn.ffcs.gmis.workcircle.service.WorkCircleService;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.handlerCfg.service.IEventHandlerCfgService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 大联动(JointCooperation) 工作流节点跳转决策类
 * 必填参数
 * 		decisionService	指定实现类 workflowDecisionMaking4JointCooperationService
 * 		curUserId		当前用户
 * 		createUserId	当前事件的采集人员
 * 		createOrgId		当前事件的采集人员组织
 * 		curOrgId		当前用户所属组织
 * 		curNodeCode		当前环节节点编码
 * 		instanceId		流程实例id
 * 非必填参数
 * 		toClose			是否结案归档 1：结案归档；0 未结案归档；默认为0
 * @author zhangls
 *
 */
@Service(value = "workflowDecisionMaking4JointCooperationService")
public class WorkflowDecisionMaking4JointCooperationServiceImpl extends
		ApplicationObjectSupport implements IWorkflowDecisionMakingService<Map<String, Object>> {

	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventHandlerCfgService eventHandlerCfgService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private WorkCircleService workCircleService;//工作圈接口
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	//决策1
	private static final String DECISION_MAKING_NODE_CODE = "decision1";			//决策1环节节点编码
	private static final String COLLECT_NODE_CODE = "task1";						//事件采集
	private static final String COMMUNITY_NODE_CODE_COLLECT = "task2";				//社区处理
	private static final String STREET_CENTER_NODE_CODE_COLLECT = "task3";			//街道指挥中心处理
	private static final String STREET_DEPARTMENT_NODE_CODE_COLLECT = "task4";		//街道处置单位处理
	private static final String DISTRICT_CENTER_NODE_CODE_COLLECT = "task5";		//县区指挥中心处理
	private static final String DISTRICT_DEPARTMENT_NODE_CODE_COLLECT = "task6";	//区处置单位处理
	
	//决策2
	private static final String DECISION_MAKING_NODE_CODE_2 = "decision2";			//决策2环节节点编码
	private static final String STREET_CENTER_NODE_CODE = "task7";					//街道指挥中心处理
	private static final String DISTRICT_CENTER_NODE_CODE = "task8";				//县区指挥中心处理
	
	//决策3
	private static final String DECISION_MAKING_NODE_CODE_3 = "decision3";			//决策3环节节点编码
	private static final String COMMUNITY_NODE_CODE_SEND = "task9";					//社区处理
	private static final String STREET_DEPARTMENT_NODE_CODE_SEND = "task10";		//街道处置单位处理
	private static final String DISTRICT_DEPARTMENT_NODE_CODE_SEND = "task11";		//区处置单位处理
	private static final String EVALUATE_NODE_CODE = "task12";						//核查评价
	
	private static final String END_NODE_CODE = "end1";								//归档环节
	
	private static final String WORK_CIRCLE_WCTYPE_EVENT = "1";//工作圈类型 事件
	private static final String WORK_CIRCLE_OPTYPE_REPORT = "1";//工作圈操作类型 上报
	private static final String WORK_CIRCLE_OPTYPE_ARCHIVE = "4";//工作圈类型 上报并结案
	
	@Override
	public Map<String, Object> makeDecision(Map<String, Object> params) throws Exception {
		Long curUserId = -1L,		//当前用户
			 createUserId = -1L,	//当前事件的采集人员
			 createOrgId = -1L,		//当前事件的采集人员组织
			 curOrgId = -1L,		//当前用户所属组织
			 instanceId = -1L;		//流程实例id
		UserBO curUserBO = null;	//当前用户信息
		OrgSocialInfoBO curOrgInfo = null;	//当前组织信息
		String curNodeCode = "",	//当前环节节点编码
			   nextNodeCode = "",	//下一环节节点编码
			   curUserName = "",	//当前用户姓名
			   curOrgCode = "",		//当前组织编码
			   curOrgName = "",		//当前组织名称
			   chiefLevl = "",		//当前组织层级
			   orgType = "",		//当前组织类型
			   toClose = "0";		//是否结案归档
		ProInstance proInstance = null;
		
		Map<String, Object> resultMap = new HashMap<String, Object>(),
							nodeHandlerMap = null;//决策后的环节办理人员
		
		if(CommonFunctions.isNotBlank(params, "curUserId")) {
			try {
				curUserId = Long.valueOf(params.get("curUserId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[curUserId]："+params.get("curUserId")+" 不能转换为Long型！");
			}
			
			if(curUserId > 0) {
				curUserBO = userManageService.getUserInfoByUserId(curUserId);
				if(curUserBO != null) {
					curUserName = curUserBO.getPartyName();
				}
			}
		} else {
			throw new IllegalArgumentException("缺失参数[curUserId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curOrgId")) {
			try {
				curOrgId = Long.valueOf(params.get("curOrgId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[curOrgId]："+params.get("curOrgId")+" 不能转换为Long型！");
			}
			
			if(curOrgId > 0) {
				curOrgInfo = orgSocialInfoService.findByOrgId(curOrgId);
				if(curOrgInfo != null) {
					curOrgCode = curOrgInfo.getOrgCode();
					curOrgName = curOrgInfo.getOrgName();
				}
			}
		} else {
			throw new IllegalArgumentException("缺失参数[curOrgId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "createUserId")) {
			try {
				createUserId = Long.valueOf(params.get("createUserId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[createUserId]："+params.get("createUserId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[createUserId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "createOrgId")) {
			try {
				createOrgId = Long.valueOf(params.get("createOrgId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[createOrgId]："+params.get("createOrgId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[createOrgId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			String instanceIdStr = params.get("instanceId").toString();
			try {
				instanceId = Long.valueOf(instanceIdStr);
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[instanceId]："+params.get("instanceId")+" 不能转换为Long型！");
			}
			
			if(instanceId != null && instanceId > 0) {
				proInstance = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
			}
		} else {
			throw new IllegalArgumentException("缺失参数[instanceId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "toClose")) {
			toClose = params.get("toClose").toString();
		}
		
		if(curOrgInfo != null) {
			chiefLevl = curOrgInfo.getChiefLevel();
			orgType = curOrgInfo.getOrgType();
		}
		
		if(DECISION_MAKING_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)) {//决策1
			if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(chiefLevl)) {
				nextNodeCode = COMMUNITY_NODE_CODE_COLLECT;
			} else if(ConstantValue.STREET_ORG_LEVEL.equals(chiefLevl)) {
				if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(orgType)) {
					nextNodeCode = STREET_CENTER_NODE_CODE_COLLECT;
				} else if(String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgType)) {
					nextNodeCode = STREET_DEPARTMENT_NODE_CODE_COLLECT;
				}
			} else if(ConstantValue.DISTRICT_ORG_LEVEL.equals(chiefLevl)) {
				if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(orgType)) {
					nextNodeCode = DISTRICT_CENTER_NODE_CODE;
				} else if(String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgType)) {
					nextNodeCode = DISTRICT_DEPARTMENT_NODE_CODE_COLLECT;
				}
			}
		} else if(DECISION_MAKING_NODE_CODE_2.equals(curNodeCode) || COMMUNITY_NODE_CODE_COLLECT.equals(curNodeCode) 
				|| STREET_CENTER_NODE_CODE_COLLECT.equals(curNodeCode) || DISTRICT_CENTER_NODE_CODE_COLLECT.equals(curNodeCode)
				|| STREET_DEPARTMENT_NODE_CODE_COLLECT.equals(curNodeCode) || DISTRICT_DEPARTMENT_NODE_CODE_COLLECT.equals(curNodeCode)) {//决策2
			//该区域该事件类型是否有对应的办理人员配置
			if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose)) {//归档
				nextNodeCode = END_NODE_CODE;
			} else {
				if(DISTRICT_CENTER_NODE_CODE_COLLECT.equals(curNodeCode)) {
					nextNodeCode = STREET_CENTER_NODE_CODE;
				} else if(!DECISION_MAKING_NODE_CODE_2.equals(curNodeCode)) {//非决策节点时，进行人员获取
					Long eventId = -1L;
					String decisionResultTaskName = null;
					
					if(proInstance != null) {
						eventId = proInstance.getFormId();
						
						if(eventId > 0) {
							EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
							if(event != null) {
								String bizPlatform = event.getBizPlatform();
								
								if(StringUtils.isNotBlank(bizPlatform)) {
									decisionResultTaskName = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.DECISION_RESULT_TASK_NAME+"_"+proInstance.getProName()+"_"+bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, curOrgCode);
								}
							}
						}
					}
					
					//优先使用配置的环节
					if(StringUtils.isNotBlank(decisionResultTaskName)) {
						nextNodeCode = decisionResultTaskName;
						nodeHandlerMap = this.capNodeHandler(proInstance, curNodeCode, nextNodeCode, curUserBO, curOrgInfo);
					}
					
					if(nodeHandlerMap == null || nodeHandlerMap.isEmpty()) {
						if(COMMUNITY_NODE_CODE_COLLECT.equals(curNodeCode) || STREET_DEPARTMENT_NODE_CODE_COLLECT.equals(curNodeCode)) {
							nextNodeCode = STREET_CENTER_NODE_CODE;
							nodeHandlerMap = this.capNodeHandler(proInstance, curNodeCode, nextNodeCode, curUserBO, curOrgInfo);
						}
					}
					
					if(nodeHandlerMap == null || nodeHandlerMap.isEmpty()) {
						nextNodeCode = DISTRICT_CENTER_NODE_CODE;
						nodeHandlerMap = this.capNodeHandler(proInstance, curNodeCode, nextNodeCode, curUserBO, curOrgInfo);
					}
					
					if(nodeHandlerMap != null && !nodeHandlerMap.isEmpty()) {
						resultMap.putAll(nodeHandlerMap);
					} else {
						throw new Exception("下一环节没有可办理人员！");
					}
				}
			}
		} else if(DECISION_MAKING_NODE_CODE_3.equals(curNodeCode) || COMMUNITY_NODE_CODE_SEND.equals(curNodeCode)
				|| STREET_DEPARTMENT_NODE_CODE_SEND.equals(curNodeCode) || DISTRICT_DEPARTMENT_NODE_CODE_SEND.equals(curNodeCode)
				|| STREET_CENTER_NODE_CODE.equals(curNodeCode) || DISTRICT_CENTER_NODE_CODE.equals(curNodeCode)) {//决策3
			if(curUserId.equals(createUserId) && curOrgId.equals(createOrgId)) {//相同组织的同一个人采集并结案时，直接归档
				nextNodeCode = END_NODE_CODE;
			} else {//否则需要进行评价操作
				nextNodeCode = EVALUATE_NODE_CODE;
				
				nodeHandlerMap = this.capNodeHandler(proInstance, curNodeCode, nextNodeCode, curUserBO, curOrgInfo);
			}
			
			if(nodeHandlerMap != null && !nodeHandlerMap.isEmpty()) {
				resultMap.putAll(nodeHandlerMap);
			}
		}
		
		if(StringUtils.isBlank(nextNodeCode)) {
			throw new Exception("没有可用的下一环节！");
		}
		
		resultMap.put("nextNodeCode", nextNodeCode);
		
		if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose) || STREET_CENTER_NODE_CODE.equals(nextNodeCode) || DISTRICT_CENTER_NODE_CODE.equals(nextNodeCode)) {
			WorkCircle workCircle = new WorkCircle();
			workCircle.setWcType(WORK_CIRCLE_WCTYPE_EVENT);
			workCircle.setOpUserId(curUserId);
			workCircle.setOpUserName(curUserName);
			workCircle.setOpDeptCode(curOrgCode);
			workCircle.setOpDeptName(curOrgName);
			
			if(instanceId != null && instanceId > 0) {
				Long eventId = -1L;
				
				if(proInstance != null) {
					eventId = proInstance.getFormId();
				}
				
				if(eventId > 0) {
					EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
					if(event != null) {
						workCircle.setEventId(eventId);
						workCircle.setEventTitle(event.getEventName());
						workCircle.setEventTime(event.getHappenTime());
						workCircle.setEventAddr(event.getOccurred());
					}
				}
			}
			
			if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose)) {//归档
				//添加 上报并结案 工作圈记录
				workCircle.setOpType(WORK_CIRCLE_OPTYPE_ARCHIVE);//上报并结案
				workCircle.setNextUserId(curUserId.toString());
				workCircle.setNextUserName(curUserName);
				workCircle.setNextDeptCode(curOrgCode);
				workCircle.setNextDeptName(curOrgName);
				
			} else if(STREET_CENTER_NODE_CODE.equals(nextNodeCode) || DISTRICT_CENTER_NODE_CODE.equals(nextNodeCode)) {//添加 上报 工作圈记录
				workCircle.setOpType(WORK_CIRCLE_OPTYPE_REPORT);//上报
				
				if(CommonFunctions.isNotBlank(resultMap, "transactorIds")) {//下一环节办理人员id
					workCircle.setNextUserId(resultMap.get("transactorIds").toString());
				}
				if(CommonFunctions.isNotBlank(resultMap, "transactorOrgIds")) {//下一环节办理人员组织id
					String[] transactorOrgIdArray = resultMap.get("transactorOrgIds").toString().split(",");
					Long orgId = -1L;
					OrgSocialInfoBO orgInfo = null;
					StringBuffer orgCodeBuffer = new StringBuffer(""),
							     orgNameBuffer = new StringBuffer("");
					
					for(String transactorOrgId : transactorOrgIdArray) {
						if(StringUtils.isNotBlank(transactorOrgId)) {
							try {
								orgId = Long.valueOf(transactorOrgId);
							} catch(NumberFormatException e) {
								orgId = -1L;
								e.printStackTrace();
							}
							
							if(orgId > 0) {
								orgInfo = orgSocialInfoService.findByOrgId(orgId);
								if(orgInfo != null) {
									orgCodeBuffer.append(",").append(orgInfo.getOrgCode());
									orgNameBuffer.append(",").append(orgInfo.getOrgName());
								}
							}
						}
					}
					
					if(orgCodeBuffer.length() > 0) {
						workCircle.setNextDeptCode(orgCodeBuffer.substring(1));
						workCircle.setNextDeptName(orgNameBuffer.substring(1));
					}
				}
			}
			
			try {
				workCircleService.insert(workCircle);
			} catch(Exception e) {//防止工作圈异常导致决策失败
				e.printStackTrace();
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 获取环节办理人员信息
	 * @param instanceId	流程实例id
	 * @param curNodeCode	当前环节
	 * @param nextNodeCode	下一环节
	 * @param curUserBO		当前办理人员
	 * @param curOrgInfo	当前办理人员组织
	 * @return
	 * 		transactorIds		办理人员id，以英文逗号相连
	 * 		transactorOrgIds	办理人员组织id，以英文逗号相连
	 * @throws Exception
	 */
	private Map<String, Object> capNodeHandler(ProInstance proInstance, String curNodeCode, String nextNodeCode, UserBO curUserBO, OrgSocialInfoBO curOrgInfo) throws Exception {
		Node curNode = null,
			 nextNode = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long instanceId = -1L;
		
		if(proInstance != null) {
			instanceId = proInstance.getInstanceId();
			
			curNode = eventDisposalWorkflowService.findNodeById(instanceId, curNodeCode);
			nextNode = eventDisposalWorkflowService.findNodeById(instanceId, nextNodeCode);
		}
		
		if(curNode != null && nextNode != null) {
			String nodeCodeFrom = curNode.getNodeCode(),
				   nodeCodeTo = nextNode.getNodeCode(),
				   operateType = "",
				   nodeCode = "";
			int fromLevel = 0, toLevel = 0;
			
			operateType = capOperateType(curNodeCode, nextNodeCode);
			
			if(StringUtils.isNotBlank(nodeCodeFrom) && nodeCodeFrom.length() > 1) {
				String fromLevelStr = nodeCodeFrom.substring(1, 2);
				
				try {
					fromLevel = Integer.valueOf(fromLevelStr);
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			if(StringUtils.isNotBlank(nodeCodeTo) && nodeCodeTo.length() > 1) {
				String toLevelStr = nodeCodeTo.substring(1, 2);
				
				try {
					toLevel = Integer.valueOf(toLevelStr);
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			//人工构建nodeCode
			nodeCode = nodeCodeFrom + operateType + (Math.abs(fromLevel - toLevel)) + nodeCodeTo;
			//人工构建当前登入用户信息
			UserInfo userInfo = new UserInfo();
			if(curUserBO != null) {
				userInfo.setUserId(curUserBO.getUserId());
				userInfo.setPartyName(curUserBO.getPartyName());
			}
			if(curOrgInfo != null) {
				userInfo.setOrgId(curOrgInfo.getOrgId());
				userInfo.setOrgCode(curOrgInfo.getOrgCode());
				userInfo.setOrgName(curOrgInfo.getOrgName());
			}
			//人工模拟环节选中效果
			Map<String, Object> extraParam = new HashMap<String, Object>();
			if(proInstance != null) {
				extraParam.put("eventId", proInstance.getFormId());
			}
			
			Map<String, Object> nodeResultMap = fiveKeyElementService.getNodeInfoForEvent(userInfo, curNode.getNodeName(), nextNode.getNodeName(), nodeCode, nextNode.getNodeId().toString(), extraParam);
			
			if(CommonFunctions.isNotBlank(nodeResultMap, "userIds")) {
				resultMap.put("transactorIds", nodeResultMap.get("userIds"));
			}
			if(CommonFunctions.isNotBlank(nodeResultMap, "orgIds")) {
				resultMap.put("transactorOrgIds", nodeResultMap.get("orgIds"));
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 获取操作类型
	 * @param curNodeCode	当前环节名称
	 * @param nextNodeCode	下一环节名称
	 * @return
	 */
	private String capOperateType(String curNodeCode, String nextNodeCode) {
		Map<String, String> operateTypeMap = new HashMap<String, String>();
		String operateType = INodeCodeHandler.OPERATE_REPORT;//默认上报
		
		//上报
		operateTypeMap.put(STREET_DEPARTMENT_NODE_CODE_COLLECT + "-" + DISTRICT_CENTER_NODE_CODE , INodeCodeHandler.OPERATE_REPORT);
		operateTypeMap.put(DISTRICT_DEPARTMENT_NODE_CODE_COLLECT + "-" + DISTRICT_CENTER_NODE_CODE , INodeCodeHandler.OPERATE_REPORT);
		
		//分流
		operateTypeMap.put(STREET_DEPARTMENT_NODE_CODE_COLLECT, INodeCodeHandler.OPERATE_FLOW);
		operateTypeMap.put(DISTRICT_CENTER_NODE_CODE, INodeCodeHandler.OPERATE_FLOW);
		
		if(operateTypeMap.containsKey(curNodeCode + "-" + nextNodeCode)) {
			operateType = operateTypeMap.get(curNodeCode + "-" + nextNodeCode);
		} else if(operateTypeMap.containsKey(curNodeCode)) {
			operateType = operateTypeMap.get(curNodeCode);
		}
		
		return operateType;
	}
}
