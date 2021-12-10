package cn.ffcs.zhsq.peopleLivelihoodInfo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserInfoOutService;
import cn.ffcs.workflow.common.Constants;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.NodeTransition;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.mybatis.persistence.peopleLivelihoodInfo.PeopleLivelihoodInfoMapper;
import cn.ffcs.zhsq.peopleLivelihoodInfo.service.IPeopleLivelihoodInfoService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * @Description: 民生信息模块服务实现
 * @Author: youwj
 * @Date: 09-01 15:32:03
 * @Copyright: 2020 福富软件
 */
@Service("peopleLivelihoodInfoServiceImpl")
@Transactional
public class PeopleLivelihoodInfoServiceImpl implements IPeopleLivelihoodInfoService {
	
	
	@Autowired
	private PeopleLivelihoodInfoMapper peopleLivelihoodInfoMapper;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IWorkflow4BaseService workflow4BaseService;
	
    @Autowired
   	private IBaseWorkflowService baseWorkflowService;
    
    @Autowired
    private UserInfoOutService userInfoService;
    
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;
	
	@Resource(name="eventStatusDecisionMaking4PeopleLivelihoodService")  
	private IWorkflowDecisionMakingService<Boolean> statusDecisionMaking;//状态回调
	
	@Resource(name = "workflowDecisionMaking4PeopleLivelihoodService")
   	private IWorkflowDecisionMakingService<String> workflowDecisionMakingService;//决策跳转类
	
	@Resource(name = "fiveKeyElementForEventService")
	private IFiveKeyElementService fiveKeyElementService;//五要素选人
	
	
	private final String INFO_WORKFLOW_NAME = "西藏昌都民生信息流程";//流程图名称
	private final String INFO_WFTYPE_ID = "people_livelihood";//流程类型
	private final String START_NODE_NAME = "start";//流程开始节点名称
    private static final String POSITION_NAME = "民生信息员";	
	
	//驳回节点编码
	protected static final String REJECT_NODE_CODE = "reject";	//虚拟的驳回环节节点，流程图中不存在
	//撤回节点编码
	private static final String RECALL_NODE_CODE = "recall";	//虚拟的撤回环节节点，流程图中不存在
	
	private static final String OPERATE_TYPE_REJECT = "2";//驳回操作
	private static final String OPERATE_TYPE_RECALL = "5";//撤回操作
	private static final String DESCISION_NODE_TYPE = "2";//决策节点环节类型
	
	public final static Map<String, String> taskMap = new HashMap<String, String>() {
	    {
	        put("3", "task5");//县区级
	        put("4", "task3");//乡镇街道
	        put("5", "task2");//社区
	    }
	};
	
	
	

	@Override
	public Map<String, Object> saveOrUpdate(Map<String, Object> livelihoodInfo,UserInfo userInfo) {
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("operation", "");
		result.put("operationResult", false);
		
		if(CommonFunctions.isBlank(livelihoodInfo, "updator")) {
			livelihoodInfo.put("updator", userInfo.getUserId());
		}
		
		try {
			if(CommonFunctions.isNotBlank(livelihoodInfo, "infoId")) {//存在主键Id执行更新操作
				
				Boolean update = this.update(livelihoodInfo);
				result.put("operation", "update");
				result.put("operationResult", livelihoodInfo.get("infoId").toString());
				
			}else {//不存在主键执行新增操作
				if(CommonFunctions.isBlank(livelihoodInfo, "creator")) {
					livelihoodInfo.put("creator", userInfo.getUserId());
				}
				String insert = this.insert(livelihoodInfo);
				result.put("operation", "insert");
				result.put("operationResult", insert);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}


	@Override
	public String insert(Map<String, Object> livelihoodInfo) {
		peopleLivelihoodInfoMapper.insert(livelihoodInfo);
		Map<String, Object> searchBySeqId = peopleLivelihoodInfoMapper.searchBySeqId(Long.valueOf(livelihoodInfo.get("infoSeqId").toString()));
		return searchBySeqId.get("infoId").toString();
	}

	@Override
	public Boolean update(Map<String, Object> livelihoodInfo) {
		Long update = peopleLivelihoodInfoMapper.update(livelihoodInfo);
		return update!=null?update>0:false;
	}
	

	@Override
	public Boolean updateBySeqId(Map<String, Object> livelihoodInfo) {
		Long update = peopleLivelihoodInfoMapper.updateBySeqId(livelihoodInfo);
		return update!=null?update>0:false;
	}

	@Override
	public Boolean delete(Map<String, Object> livelihoodInfo) {
		Long delete = peopleLivelihoodInfoMapper.delete(livelihoodInfo);
		return delete!=null?delete>0:false;
	}

	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) throws Exception{
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 10 : rows;
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<Map<String, Object>> list =new ArrayList<Map<String,Object>>();
		this.formatDataIn(params);
		Long count = this.countList(params);
		
		if(count>0) {
			String listType="";
			if(CommonFunctions.isNotBlank(params, "listType")) {
				listType=params.get("listType").toString();
			}
			
			switch (listType) {
				case "1"://草稿采集列表
					if(CommonFunctions.isBlank(params, "orderByField")) {
						params.put("orderByField", " T.STATUS DESC ");
					}
					list=peopleLivelihoodInfoMapper.searchDraftList(params, rowBounds);
					break;
		
				case "2"://待办列表
					list=peopleLivelihoodInfoMapper.searchTodoList(params, rowBounds);
					break;
					
				case "3"://经办列表
					list=peopleLivelihoodInfoMapper.searchDoneList(params, rowBounds);
					break;
					
				case "4"://辖区列表
					list=peopleLivelihoodInfoMapper.searchAllList(params, rowBounds);
					break;
					
				default:
					break;
			}
		}
		
		this.formatDataOut(list, params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
		
	}

	@Override
	public List<Map<String, Object>> searchListByParams(Map<String, Object> params) {
		List<Map<String, Object>> list =new ArrayList<Map<String,Object>>();
		
		try {
			this.formatDataIn(params);
			list = peopleLivelihoodInfoMapper.searchListByParams(params);
			this.formatDataOut(list, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public Long countList(Map<String, Object> params) throws Exception {
		Long result=0L;
		this.formatDataIn(params);
		String listType="";
		if(CommonFunctions.isNotBlank(params, "listType")) {
			listType=params.get("listType").toString();
		}
		
		switch (listType) {
			case "1"://草稿采集列表
				result=peopleLivelihoodInfoMapper.countDraftList(params);
				break;
			case "2"://待办列表
				result=peopleLivelihoodInfoMapper.countTodoList(params);
				break;
			case "3"://经办列表
				result=peopleLivelihoodInfoMapper.countDoneList(params);
				break;
			case "4"://辖区列表
				result=peopleLivelihoodInfoMapper.countAllList(params);
				break;
	
			default:
				break;
		}
		
		return result;
	}

	@Override
	public Map<String, Object> searchById(String id,String orgCode) {
		Map<String, Object> searchById=null;
		
		try {
			searchById = peopleLivelihoodInfoMapper.searchById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(searchById!=null&&searchById.size()>0) {
			List<Map<String,Object>> formatList=new ArrayList<Map<String,Object>>();
			formatList.add(searchById);
			
			Map<String,Object> formatParams=new HashMap<String,Object>();
			formatParams.put("orgCode", orgCode);
			
			this.formatDataOut(formatList, formatParams);
			
		}
		return searchById;
	}
	
	
	@Override
	public Map<String, Object> searchBySeqId(Long id, String orgCode) {
		Map<String, Object> searchById=null;
		
		try {
			searchById = peopleLivelihoodInfoMapper.searchBySeqId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(searchById!=null&&searchById.size()>0) {
			List<Map<String,Object>> formatList=new ArrayList<Map<String,Object>>();
			formatList.add(searchById);
			
			Map<String,Object> formatParams=new HashMap<String,Object>();
			formatParams.put("orgCode", orgCode);
			
			this.formatDataOut(formatList, formatParams);
			
		}
		return searchById;
	}

	
	public void formatDataIn(Map<String,Object> params) throws ZhsqEventException {
		if(params !=null) {
			
			if(CommonFunctions.isBlank(params, "userId")) {
				throw new ZhsqEventException("缺少查询的[userId]参数");
			}
			
			if(CommonFunctions.isBlank(params, "orgId")) {
				throw new ZhsqEventException("缺少查询的[orgId]参数");
			}
			
			//获取当前用户层级
			Long orgId = Long.valueOf(params.get("orgId").toString());
			OrgSocialInfoBO findByOrgId = orgSocialInfoOutService.findByOrgId(orgId);
			String chiefLevel = findByOrgId.getChiefLevel();
			
			if(CommonFunctions.isNotBlank(taskMap, chiefLevel)) {
				params.put("limitTask", taskMap.get(chiefLevel).toString());
			}
			
			
			if(CommonFunctions.isNotBlank(params, "infoId")) {
				String infoId = params.get("infoId").toString();
				if(infoId.contains(",")) {
					params.put("infoIdArray", infoId.split(","));
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "infoSeqId")) {
				String infoSeqId = params.get("infoSeqId").toString();
				if(infoSeqId.contains(",")) {
					params.put("infoSeqIdArray", infoSeqId.split(","));
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "infoType")) {
				String infoType = params.get("infoType").toString();
				if(infoType.contains(",")) {
					params.put("infoTypeArray", infoType.split(","));
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "infoTrendsType")) {
				String infoTrendsType = params.get("infoTrendsType").toString();
				if(infoTrendsType.contains(",")) {
					params.put("infoTrendsTypeArray", infoTrendsType.split(","));
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "urgenceDegree")) {
				String urgenceDegree = params.get("urgenceDegree").toString();
				if(urgenceDegree.contains(",")) {
					params.put("urgenceDegreeArray", urgenceDegree.split(","));
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "status")) {
				String status = params.get("status").toString();
				if(status.contains(",")) {
					params.put("statusArray", status.split(","));
				}
			}
			
		}
	}
	
	public void formatDataOut(List<Map<String,Object>> list,Map<String,Object> params) {
		
		if(list != null && list.size() > 0) {
			String userOrgCode = null;
			List<BaseDataDict> infoTypeDictList = null,//民生信息类型字典
							   infoTrendsTypeDictList = null,//民生动态类型字典
							   urgenceDegreeDictList = null,//紧急程度紧急程度字典
							   statusDictList = null,//信息状态字典
							   subStatusDictList = null;//信息子状态字典
			
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			} else if(CommonFunctions.isNotBlank(params, "orgCode")) {
				userOrgCode = params.get("orgCode").toString();
			}
			
			
			infoTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.INFO_TYPE_PCODE, userOrgCode);
			infoTrendsTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.INFO_TRENDS_TYPE_PCODE, userOrgCode);
			urgenceDegreeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.URGENCE_DEGREE_PCODE, userOrgCode);
			statusDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.INFO_STATUS_PCODE, userOrgCode);
			subStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.INFO_SUB_STATUS, userOrgCode);
			
			
			
			for(Map<String, Object> eventMap : list) {
				
				
				try {
					// 民生信息类型字典
					DataDictHelper.setDictValueForField(eventMap, "infoType", "infoTypeName", infoTypeDictList);
					
					// 民生动态类型字典
					DataDictHelper.setDictValueForField(eventMap, "infoTrendsType", "infoTrendsTypeName", infoTrendsTypeDictList);
					
					// 民生动态类型字典
					DataDictHelper.setDictValueForField(eventMap, "urgenceDegree", "urgenceDegreeName", urgenceDegreeDictList);
					
					// 信息状态字典
					DataDictHelper.setDictValueForField(eventMap, "status", "statusName", statusDictList);
					
					// 信息子状态字典
					DataDictHelper.setDictValueForField(eventMap, "subStatus", "subStatusName", subStatusDictList);
					
					
					if(CommonFunctions.isNotBlank(eventMap, "happenTime")) {
						eventMap.put("happenTimeStr", DateUtils.formatDate((Date)eventMap.get("happenTime"), DateUtils.PATTERN_24TIME));
					}else {
						eventMap.put("happenTimeStr", "");
					}
					if(CommonFunctions.isNotBlank(eventMap, "createTime")) {
						eventMap.put("createTimeStr", DateUtils.formatDate((Date)eventMap.get("createTime"), DateUtils.PATTERN_24TIME));
					}
					if(CommonFunctions.isNotBlank(eventMap, "updateTime")) {
						eventMap.put("updateTimeStr", DateUtils.formatDate((Date)eventMap.get("updateTime"), DateUtils.PATTERN_24TIME));
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
	}

	
	/**工作流相关部分start*/

	@Override
	public Long startWorkflow(Long infoId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		Long result = -1L;

		if (isUserAbleToStart(userInfo)) {
			Boolean flag = workflow4BaseService.startWorkflow4Base(infoId, INFO_WORKFLOW_NAME, INFO_WFTYPE_ID, userInfo,
					extraParam);

			if (flag) {
				//启动成功后设置采集用户信息以及信息采集时间至主表
				try {
					Map<String,Object> updateParams=new HashMap<String,Object>();
					updateParams.put("infoSeqId", infoId);
					updateParams.put("userName", userInfo.getPartyName());
					updateParams.put("happenTime", new Date());
					peopleLivelihoodInfoMapper.updateBySeqId(updateParams);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				result = this.capInstanceId(infoId, userInfo);
				alterInfoStatus(infoId, START_NODE_NAME, START_NODE_NAME, userInfo);// 为了防止提交失败而导致状态仍保留在草稿状态
			}
		}
		//进行决策跳转
		List<Node> nextNodeList = null;
		extraParam.put("curOrgId", userInfo.getOrgId());
		extraParam.put("curUserId", userInfo.getUserId());
        
        nextNodeList = baseWorkflowService.findNextTaskNodes(INFO_WORKFLOW_NAME, INFO_WFTYPE_ID, infoId, userInfo, extraParam);
        if(nextNodeList != null) {
			
			if(nextNodeList.size() == 1) {
				Node nextNode = nextNodeList.get(0);
				Node nextTaskNode = nextNodeList.get(0);
				
				String nextNodeCode = "";
				
				extraParam.put("curNodeCode", nextNode.getNodeName());
				nextNodeCode = workflowDecisionMakingService.makeDecision(extraParam);
				
				if(StringUtils.isNotBlank(nextNodeCode)) {
					nextTaskNode = baseWorkflowService.capNodeInfo(INFO_WORKFLOW_NAME, INFO_WFTYPE_ID, infoId, nextNodeCode, userInfo);
					//决策节点重新获取，下一节点为决策节点时，决策后的节点名称
					extraParam.put("decisionTaskName", nextTaskNode.getNodeName());
					
					nextNodeList = baseWorkflowService.findNextTaskNodes(INFO_WORKFLOW_NAME, INFO_WFTYPE_ID, infoId, userInfo, extraParam);
					nextNode = nextNodeList.get(0);
					nextTaskNode = nextNodeList.get(0);
					
					if(nextTaskNode != null) {
						NodeTransition nodeTransition = baseWorkflowService.capNodeTransition(nextNode.getNodeId(), nextTaskNode.getNodeId());
						if(nodeTransition != null) {
							nextTaskNode.setTransitionCode(nextNode.getFromCode() + nodeTransition.getType() + nodeTransition.getLevel() + nextTaskNode.getToCode());
						}
					}
					Map<String,Object> subParams=new HashMap<String,Object>();
					subParams.put("curNodeName", "task1");//设置当前环节为事件采集
					Boolean subWorkflow = this.subWorkflow(infoId, nextTaskNode.getNodeName(), null, userInfo, subParams);
					if(subWorkflow) {
						
						if(CommonFunctions.isNotBlank(extraParam, "isClose")&&"1".equals(extraParam.get("isClose").toString())) {
							
							Map<String,Object> handleDataMap = findCurTaskData(infoId, userInfo);
							
							String curNodeName = "";
							
							if(CommonFunctions.isNotBlank(handleDataMap,"NODE_NAME")){
								curNodeName = String.valueOf(handleDataMap.get("NODE_NAME"));
							}
							extraParam.put("curNodeName", curNodeName);
							
							this.subWorkflow(infoId,"end1",null,userInfo,extraParam);
						}
					}
				}
				
			}
        }

		
		return result;
	}
	
	
	
	@Override
	public Boolean subWorkflow(Long infoId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		
		boolean flag = workflow4BaseService.subWorkflow4Base(infoId, INFO_WORKFLOW_NAME, INFO_WFTYPE_ID, nextNodeName, nextUserInfoList, userInfo, extraParam);
		
		if(flag) {
			String curNodeName = null;
			
			if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
				curNodeName = extraParam.get("curNodeName").toString();
			}
			
			this.alterInfoStatus(infoId, curNodeName, nextNodeName, userInfo);
		}
		
		return flag;
	}
	
	
	
	@Override
	public Boolean rejectWorkflow4Clue(Long infoId, String rejectToNodeName, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		
		Boolean result = false;
		
		List<Map<String, Object>> taskMapList = baseWorkflowService.capHandledTaskInfoMap(Long.valueOf(extraParam.get("instanceId").toString()), Constants.SQL_ORDER_DESC, null);
	    String preTaskName="";//回退1个环节
	    String preTaskName_2="";//回退两个环节
	    Integer flag=0;
	    if(taskMapList!=null&&taskMapList.size()>0) {
	    	for (Map<String, Object> taskmap : taskMapList) {
	    		String operateType="";
	    		if(CommonFunctions.isNotBlank(taskmap, "OPERATE_TYPE")) {
	    			operateType=taskmap.get("OPERATE_TYPE").toString();
	    		}
				if((!OPERATE_TYPE_REJECT.equals(operateType)) && ( !OPERATE_TYPE_RECALL.equals(operateType))) {
					if(flag>0) {
						flag+=-1;
					}else {
						
						if(StringUtils.isBlank(preTaskName)) {
							preTaskName=taskmap.get("TASK_CODE").toString();
							continue;	
						}
						if(StringUtils.isBlank(preTaskName_2)) {
							preTaskName_2=taskmap.get("TASK_CODE").toString();
							continue;	
						}
					}
				}else {
					flag+=1;
				}
			}
	    }
		
		if(infoId != null && infoId > 0) {
			result = workflow4BaseService.rejectWorkflow4Base(infoId, INFO_WORKFLOW_NAME, INFO_WFTYPE_ID, preTaskName, userInfo, extraParam);
			
			if(result) {
				alterInfoStatus(infoId, preTaskName_2, preTaskName,userInfo);
			}
		}
		
		return result;
	}


	

	
	
	
	/**
	 * 判断用户是否可以启动流程
	 * @param userInfo
	 * @throws Exception
	 */
	@Override
	public Boolean isUserAbleToStart(UserInfo userInfo) throws Exception {
		boolean flag = false;
		
		if(userInfo != null) {
			
			//判断用户在该组织下是否具有管理员职位
			Boolean isPosition=userInfoService.cheakUserForParas(userInfo.getUserId(), userInfo.getOrgId(), POSITION_NAME) > 0;
			if(!isPosition) {
				throw new Exception("该用户不能提交流程！提交流程的用户必须具有"+POSITION_NAME+"的职位！");
			}
			Long orgId = userInfo.getOrgId();
			
			if(orgId != null && orgId > 0) {
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
				if(orgInfo != null) {
					String chiefLevel = orgInfo.getChiefLevel(),
						   orgType = orgInfo.getOrgType(),
						   msgWrong = null;
					//目前只有村社区，街道、县区、市级职能部门可以采集事件
					Map<String,Object> isUserAbleToStartMap=new HashMap<String,Object>();
					//key值为：组织层级_组织类型
					isUserAbleToStartMap.put("5_1", true);
					isUserAbleToStartMap.put("4_0", true);
					isUserAbleToStartMap.put("3_0", true);
					isUserAbleToStartMap.put("2_0", true);
					
					if(CommonFunctions.isBlank(isUserAbleToStartMap, chiefLevel+"_"+orgType)) {
						msgWrong="能启动流程的用户必须为村社区级别用户，或者街道、县区、市级职能部门。";
					}
					
					if(StringUtils.isNotBlank(msgWrong)) {
						throw new Exception("该用户不能提交流程！" + msgWrong);
					} else {
						flag = true;
					}
				}
			}
		}
		
		return flag;
	}
	
	/**
	 * 更新线索状态
	 * @param clueId		线索id
	 * @param curNodeName	当前环节名称
	 * @param nextNodeName	下一环节名称
	 * @param userInfo		用户信息
	 * @return
	 * @throws Exception
	 */
	@Override
	public Boolean alterInfoStatus(Long infoId, String curNodeName, String nextNodeName, UserInfo userInfo) throws Exception {
		boolean result = false;
		Map<String, Object> statusMap = new HashMap<String, Object>();
		
		if(infoId != null && infoId > 0) {
			statusMap.put("infoId", infoId);
		}
		
		if(StringUtils.isNotBlank(curNodeName)) {
			statusMap.put("curNodeName", curNodeName);
		}
		
		if(StringUtils.isNotBlank(nextNodeName)) {
			statusMap.put("nextNodeName", nextNodeName);
		}
		
		if(userInfo != null) {
			statusMap.put("updator", userInfo.getUserId());
		}
		
		if(!statusMap.isEmpty()) {
			result = statusDecisionMaking.makeDecision(statusMap);
		}
		
		return result;
	}


	@Override
	public Long capInstanceId(Long infoId, UserInfo userInfo) throws Exception {
		Long instanceId = -1L;
		
		if(infoId != null && infoId > 0) {
			Map<String, Object> resultMap = workflow4BaseService.capInstance4Base(infoId, INFO_WORKFLOW_NAME, INFO_WFTYPE_ID, userInfo);
			
			if(CommonFunctions.isNotBlank(resultMap, "instanceId")) {
				instanceId = Long.valueOf(resultMap.get("instanceId").toString());
			}
		}
		
		return instanceId;
	}


	@Override
	public Map<String, Object> findCurTaskData(Long infoId, UserInfo userInfo) throws Exception {
		Map<String, Object> curTaskData = null;
		
		if(infoId != null && infoId > 0) {
			curTaskData = workflow4BaseService.findCurTaskData4Base(infoId, INFO_WORKFLOW_NAME, INFO_WFTYPE_ID, userInfo);
		}
		
		return curTaskData;
	}


	@Override
	public List<Map<String, Object>> findParticipantsByTaskId(Long taskId) {
		
		List<Map<String, Object>> participantMapList = null;
		
		if(taskId != null && taskId > 0){
			participantMapList = workflow4BaseService.findParticipantsByTaskId(taskId);
		}
		
		return participantMapList;
	}


	@Override
	public Boolean isCurTaskPaticipant(Long infoId,List<Map<String, Object>> participantMapList, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		
		return workflow4BaseService.isCurTaskPaticipant4Base(infoId, INFO_WORKFLOW_NAME, INFO_WFTYPE_ID, userInfo, extraParam);
		
	}


	@Override
	public List<Map<String, Object>> findOperateByNodeId(Integer nodeId) {
		
		List<Map<String, Object>> operateMapList = null;
		
		if(nodeId != null && nodeId > 0) {
			operateMapList = workflow4BaseService.findOperateByNodeId(nodeId);
		}
		
		return operateMapList;
	}


	@Override
	public List<Map<String, Object>> findNextTaskNodes(Long infoId, UserInfo userInfo, Map<String, Object> params)
			throws Exception {
		List<Node> nextNodeList = null;
		List<Map<String, Object>> nextNodeMapList = null;
		
		if(infoId == null || infoId < 0) {
			throw new IllegalArgumentException("民生信息不完整！");
		}
		
		if(params == null || params.isEmpty()) {
			nextNodeMapList = this.findNextTaskNodes(infoId, userInfo);
		} else {
			
			nextNodeList = baseWorkflowService.findNextTaskNodes(INFO_WORKFLOW_NAME, INFO_WFTYPE_ID, infoId, userInfo, params);
			
			if(nextNodeList != null) {
				nextNodeMapList = new ArrayList<Map<String, Object>>();
				Map<String, Object> nextNodeMap = null;
				
				if(nextNodeList.size() == 1) {
					Node nextNode = nextNodeList.get(0);
					Node nextTaskNode = nextNodeList.get(0);
					
					if(DESCISION_NODE_TYPE.equals(nextNode.getNodeType())) {//下一节点为决策节点
						String nextNodeCode = "";
						
						params.put("curNodeCode", nextNode.getNodeName());
						nextNodeCode = workflowDecisionMakingService.makeDecision(params);
						
						if(StringUtils.isNotBlank(nextNodeCode)) {
							nextTaskNode = baseWorkflowService.capNodeInfo(INFO_WORKFLOW_NAME, INFO_WFTYPE_ID, infoId, nextNodeCode, userInfo);
							//决策节点重新获取，下一节点为决策节点时，决策后的节点名称
							params.put("decisionTaskName", nextTaskNode.getNodeName());
							
							nextNodeList = baseWorkflowService.findNextTaskNodes(INFO_WORKFLOW_NAME, INFO_WFTYPE_ID, infoId, userInfo, params);
							nextNode = nextNodeList.get(0);
							nextTaskNode = nextNodeList.get(0);
							
							if(nextTaskNode != null) {
								NodeTransition nodeTransition = baseWorkflowService.capNodeTransition(nextNode.getNodeId(), nextTaskNode.getNodeId());
								if(nodeTransition != null) {
									nextTaskNode.setTransitionCode(nextNode.getFromCode() + nodeTransition.getType() + nodeTransition.getLevel() + nextTaskNode.getToCode());
								}
							}
						}
						nextNodeList.clear();
						
						nextNodeList.add(nextTaskNode);
					}
					
				}
				
				for(Node nextNode : nextNodeList) {
					nextNodeMap = new HashMap<String, Object>();
					
					nextNodeMap.put("nodeId", nextNode.getNodeId());
					nextNodeMap.put("nodeName", nextNode.getNodeName());
					nextNodeMap.put("nodeNameZH", nextNode.getNodeNameZH());
					nextNodeMap.put("nodeType", nextNode.getNodeType());
					nextNodeMap.put("transitionCode", nextNode.getTransitionCode());
					nextNodeMap.put("dynamicSelect", nextNode.getDynamicSelect());
					
					nextNodeMapList.add(nextNodeMap);
				}
			}
		}
		
		return nextNodeMapList;
	}


	@Override
	public List<Map<String, Object>> findNextTaskNodes(Long clueId, UserInfo userInfo) throws Exception {
		List<Map<String, Object>> nextNodeMapList = null;
		Map<String, Object> param = new HashMap<String, Object>();
		Long orgId = -1L, userId = -1L;
		
		if(clueId == null || clueId < 0) {
			throw new IllegalArgumentException("民生信息不完整！");
		}
		if(userInfo != null) {
			orgId = userInfo.getOrgId();
			userId = userInfo.getUserId();
		}
		
		param.put("curOrgId", orgId);
		param.put("curUserId", userId);
		
		nextNodeMapList = this.findNextTaskNodes(clueId, userInfo, param);
		
		return nextNodeMapList;
	}

	@Override
	public Map<String, Object> getNodeInfo(UserInfo userInfo, String curnodeName, String nodeName, String nodeCode,
			String nodeId, Map<String, Object> params) throws Exception {
		return fiveKeyElementService.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
	}


	@Override
	public Map<String, Object> capWorkflowRelativeData(String infoId, Integer listType, UserInfo userInfo)
			throws Exception {
		Map<String, Object> curTaskData = null,
				resultData = new HashMap<String, Object>();

		Long infoSeqId=-1L;
		
		Map<String, Object> searchById = this.searchById(infoId, userInfo.getOrgCode());
		if(CommonFunctions.isNotBlank(searchById, "infoSeqId")) {
			infoSeqId=Long.valueOf(searchById.get("infoSeqId").toString());
		}
		
		
		Long taskId = -1L;
		
		Long instanceId = this.capInstanceId(infoSeqId, userInfo);
		
		if(instanceId > 0) {
			curTaskData = this.findCurTaskData(infoSeqId, userInfo);
			
			if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
				taskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
			}
			
			List<Map<String, Object>> participantMapList = this.findParticipantsByTaskId(taskId);
			if(participantMapList != null) {
				StringBuffer taskPersonStr = new StringBuffer(";");
				
				for(Map<String, Object> participant : participantMapList){
					Object orgNameObj = participant.get("ORG_NAME");
					
					if(CommonFunctions.isNotBlank(participant, "USER_NAME")){
						taskPersonStr.append(participant.get("USER_NAME"));
						if(orgNameObj != null){
							taskPersonStr.append("(").append(orgNameObj).append(");");
						}
					}else if(orgNameObj != null){
						taskPersonStr.append(orgNameObj).append(";");
					}
				}
				
				resultData.put("taskPersonStr", taskPersonStr.substring(1));
				
				if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
					resultData.put("curNodeName", curTaskData.get("NODE_NAME"));
				}
				
				if(listType == 2) {//待办
					resultData.put("isCurHandler", this.isCurTaskPaticipant(infoSeqId,participantMapList, userInfo, null));
					
					if(CommonFunctions.isNotBlank(curTaskData, "NODE_ID")) {
						Integer nodeId = Integer.valueOf(curTaskData.get("NODE_ID").toString());
						resultData.put("operateList", this.findOperateByNodeId(nodeId));
					}
					
					resultData.put("nextTaskNodes", this.findNextTaskNodes(infoSeqId, userInfo, null));
				}
			}
			
			if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
				resultData.put("curTaskName", curTaskData.get("WF_ACTIVITY_NAME_"));
			}
			
			resultData.put("formId", infoSeqId);
			resultData.put("formType", INFO_WFTYPE_ID);
			resultData.put("instanceId", instanceId);
			
			//判断是否可以显示流程办理详情
			//目前的规则只有挂载在市级单位的用户并且在辖区列表才能看到这个信息
			Boolean showFlowDetail=false;
			OrgSocialInfoBO findByOrgId = orgSocialInfoOutService.findByOrgId(userInfo.getOrgId());
			String chiefLevel = findByOrgId.getChiefLevel();
			String orgType = findByOrgId.getOrgType();
			if(ConstantValue.MUNICIPAL_ORG_LEVEL.equals(chiefLevel)
					&&orgType.equals(String.valueOf(ConstantValue.ORG_TYPE_UNIT))
					&&listType==4) {//辖区列表
				showFlowDetail=true;
			}
			resultData.put("showFlowDetail", showFlowDetail);
		
		}		
		return resultData;
	}

	/**工作流相关部分end*/
}