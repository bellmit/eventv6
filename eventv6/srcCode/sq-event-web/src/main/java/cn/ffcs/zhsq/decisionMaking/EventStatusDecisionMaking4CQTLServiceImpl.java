package cn.ffcs.zhsq.decisionMaking;

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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:重庆铜梁事件状态决策类
 * @Author: youwj
 * @Date: 2021/1/7 15:41
 */
@Service(value = "eventStatusDecisionMaking4CQTLService")
public class EventStatusDecisionMaking4CQTLServiceImpl extends EventStatusDecisionMakingServiceImpl {
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private UserScoreService userScoreService;

	@Autowired
	private MessageOutService messageService;

	private static final String EVENT_MEG_MODULE_CODE = "02";	//事件消息所属模块编码

	private static final String START_NODE_CODE = "start";		//事件流程启动
	private static final String COLLECT_NODE_CODE = "task1";	//事件采集处理环节
	
	private static final String GRID_ADMIN_HANDLE_NODE_CODE = "task2";	//网格员处理
	private static final String COMMUNITY_UNIT_HANDLE_NODE_CODE = "task3";	//村社区处理
	private static final String TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE = "task4";	//乡镇街道处理
	private static final String DISTRICT_UNIT_HANDLE_NODE_CODE = "task5";	//区指挥中心处理
	private static final String DISTRICT_DEPT_HANDLE_NODE_CODE = "task6";	//群工系统处理(区职能部门)
	
	private static final String CLOSE_NODE_CODE = "task8";	//结案
	private static final String EVALUATE_NODE_CODE = "task9";	//评价
	private static final String INSPECTION_NODE_CODE = "task7";	//督导组抽查
	private static final String NETWORK_SECURITY_NODE_CODE = "task10";//网安平台处理
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
		
		if(INSPECTION_NODE_CODE.equals(nextNodeCode)) {//事件归档(重庆铜梁区指的是事件下一环节是督导组抽查)时，新增个人积分信息
			this.addUserDetailScore(eventId,userId,orgId);
			//事件归档时，将所有消息中心的消息抹除
			batchReadMessage(eventId);
		}
		
		return eventStatus;
	}
	
	
	/**
	 * 添加个人积分
	 * 业务信息必须存在：
	 * 		userId，userName，orgCode，createBy，
	 * 		scoreType 
	 * 			B1 事件处置 1分；事件采集人员与结案人员是相同组织、相同用户id的人员
	 * 			B2 事件处置 2分；事件采集人员与结案人员不是相同组织、相同用户id的人员
	 * 可选信息：dsTime（不存在值时默认当前时间），createTime（不存在值时默认当前时间）
	 * 不填写信息：dsId，updateBy，updateTime
	 */
	protected void addUserDetailScore(Long eventId,Long endUserId,Long endOrgId) {
		UserDetailScore userDetailScore = new UserDetailScore();
		Long createUserId = -1L, createOrgId = -1L, closeUserId = -1L, closeOrgId = -1L, instanceId = -1L;
		String scoreType = null, createUserName = null;
		String SCORE_TYPE_1 = "B1", SCORE_TYPE_2 = "B2";
		
		if(eventId != null && eventId > 0) {
			ProInstance pro = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
			if(pro != null) {
				instanceId = pro.getInstanceId();
				createUserId = pro.getUserId();
				createUserName = pro.getUserName();
				createOrgId = pro.getOrgId();
			}
		}
		
		if(StringUtils.isBlank(createUserName)) {
			UserBO userBO = userManageService.getUserInfoByUserId(createUserId);
			if(userBO != null && userBO.getUserId() != null && userBO.getUserId() > 0) {
				createUserName = userBO.getPartyName();
			} else {
				createUserId = -1L;
			}
		}
		
		if(createOrgId != null && createOrgId > 0) {
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(createOrgId);
			if(orgInfo != null && orgInfo.getOrgId() != null && orgInfo.getOrgId() > 0) {
				userDetailScore.setOrgCode(orgInfo.getOrgCode());
			} else {
				createOrgId = -1L;
			}
		}
		
		if(endUserId != null && endUserId > 0) {
			closeUserId = endUserId;
		}
		
		if(endOrgId != null && endOrgId > 0) {
			closeOrgId = endOrgId;
		}
		
		if(createUserId != null && createUserId > 0 && createOrgId != null && createOrgId > 0 &&
		   closeUserId != null && closeUserId > 0 && closeOrgId != null && closeOrgId > 0) {
			if(closeUserId.equals(createUserId) && closeOrgId.equals(createOrgId)) {
				scoreType = SCORE_TYPE_1;
			} else {
				scoreType = SCORE_TYPE_2;
			}
			
			userDetailScore.setUserId(createUserId);
			userDetailScore.setUserName(createUserName);
			userDetailScore.setCreateBy(createUserId);
			userDetailScore.setScoreType(scoreType);
			userDetailScore.setDsTime(new Date());//当前时间
			userDetailScore.setRemark(String.valueOf(eventId));
			
			//获取事件详情
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			if(event!=null) {
				userDetailScore.setSourceRemark(event.getEventName()+"("+event.getHappenTimeStr()+")");//事件为：事件名称+上报时间（年月日时分秒）。
			}
			
			userScoreService.insertUserDetailScore(userDetailScore);
		}
	}
	

    @Override
    public Map<String, Object> initStatusMap(String chiefLevel) {
        Map<String, Object> statusMap = new HashMap<String,Object>();
        
        statusMap.put(START_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);		//事件启动流程
		statusMap.put(COLLECT_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);		//事件采集
        
        //相关流转状态
        statusMap.put(GRID_ADMIN_HANDLE_NODE_CODE + "-" + COMMUNITY_UNIT_HANDLE_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        
        statusMap.put(COMMUNITY_UNIT_HANDLE_NODE_CODE + "-" + GRID_ADMIN_HANDLE_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(COMMUNITY_UNIT_HANDLE_NODE_CODE + "-" + TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        
        
        statusMap.put(TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE + "-" + GRID_ADMIN_HANDLE_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE + "-" + COMMUNITY_UNIT_HANDLE_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE + "-" + DISTRICT_UNIT_HANDLE_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        
        
        statusMap.put(DISTRICT_UNIT_HANDLE_NODE_CODE + "-" + GRID_ADMIN_HANDLE_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(DISTRICT_UNIT_HANDLE_NODE_CODE + "-" + COMMUNITY_UNIT_HANDLE_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(DISTRICT_UNIT_HANDLE_NODE_CODE + "-" + TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(DISTRICT_UNIT_HANDLE_NODE_CODE + "-" + DISTRICT_DEPT_HANDLE_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(DISTRICT_UNIT_HANDLE_NODE_CODE + "-" + NETWORK_SECURITY_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        
        statusMap.put(DISTRICT_DEPT_HANDLE_NODE_CODE + "-" + DISTRICT_UNIT_HANDLE_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        
        statusMap.put(NETWORK_SECURITY_NODE_CODE + "-" + DISTRICT_UNIT_HANDLE_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        
        statusMap.put(CLOSE_NODE_CODE, ConstantValue.EVENT_STATUS_ARCHIVE);		//事件结案
		statusMap.put(EVALUATE_NODE_CODE, ConstantValue.EVENT_STATUS_ARCHIVE);	//事件评价 用于采集并结案
        statusMap.put(INSPECTION_NODE_CODE, ConstantValue.EVENT_STATUS_END);//事件归档(重庆铜梁区指的是事件下一环节是督导组抽查)
        statusMap.put(END_NODE_CODE, ConstantValue.EVENT_STATUS_END);
        
        return statusMap;
    }
    
    
    /**
	 * 获取对应的变更事件状态
	 * @param statusMap
	 * @param curNodeCode
	 * @param nextNodeCode
	 * @return
	 */
    @Override
	public String capEventStatus(Map<String, Object> statusMap, String curNodeCode, String nextNodeCode) {
		String eventStatus = "";
		String mapKey = curNodeCode + "-" + nextNodeCode;
		
		if((START_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)) && CommonFunctions.isNotBlank(statusMap, curNodeCode)) {
			eventStatus = statusMap.get(curNodeCode).toString();
		} else if(CommonFunctions.isNotBlank(statusMap, mapKey)) {
			eventStatus = statusMap.get(mapKey).toString();
		} else if(CommonFunctions.isNotBlank(statusMap, nextNodeCode)) {
			eventStatus = statusMap.get(nextNodeCode).toString();
		}
		
		return eventStatus;
	}
    
    
    /**
   	 * 更新事件状态，并新增中间记录
   	 * @param eventId
   	 * @param userId
   	 * @param orgId
   	 * @param chiefLevel
   	 * @param curNodeCode
   	 * @param nextNodeCode
   	 * @param handleDate
   	 * @return
   	 * @throws Exception
   	 */
    @Override
   	public String updateEventStatus(Long eventId, Long userId, Long orgId, String chiefLevel, String curNodeCode, String nextNodeCode, Date handleDate, Map<String, Object> extraParam) throws Exception {
   		return super.updateEventStatus(eventId, userId, orgId, chiefLevel, curNodeCode, nextNodeCode, handleDate, extraParam);
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
