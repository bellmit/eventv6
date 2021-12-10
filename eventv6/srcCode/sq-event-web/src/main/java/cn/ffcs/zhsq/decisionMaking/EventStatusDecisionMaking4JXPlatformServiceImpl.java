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
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description:江西省平台事件状态决策类
 * @Author: youwj
 * @Date: 2019/7/12 15:41
 */
@Service(value = "eventStatusDecisionMaking4JXPlatformService")
public class EventStatusDecisionMaking4JXPlatformServiceImpl extends EventStatusDecisionMakingServiceImpl {
	
	private static final String START_NODE_CODE = "start";		//事件流程启动
	private static final String COLLECT_NODE_CODE = "task1";	//事件采集处理环节
	
	
	protected static final String GRID_ADMIN_ACCEPT_NODE_CODE = "task2";	//网格员受理
	protected static final String GRID_ADMIN_HANDLE_NODE_CODE = "task3";	//网格员办理
	
	protected static final String COMMUNITY_UNIT_ACCEPT_NODE_CODE = "task4";	//村社区综治中心受理
	protected static final String COMMUNITY_UNIT_HANDLE_NODE_CODE = "task5";	//村社区综治中心办理
	
	protected static final String TOWNDISPOSAl_UNIT_ACCEPT_NODE_CODE = "task6";	//乡镇街道综治中心受理
	protected static final String TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE = "task7";	//乡镇街道综治中心办理
	
	protected static final String TOWNDISPOSAl_DEPT_ACCEPT_NODE_CODE = "task8";	//乡镇街道职能部门受理
	private static final String TOWNDISPOSAl_DEPT_HANDLE_NODE_CODE = "task9";	//乡镇街道职能部门办理
	
	protected static final String DISTRICT_UNIT_ACCEPT_NODE_CODE = "task10";	//县区综治中心受理
	private static final String DISTRICT_UNIT_HANDLE_NODE_CODE = "task11";	//县区综治中心办理
	
	protected static final String DISTRICT_DEPT_ACCEPT_NODE_CODE = "task12";	//县区职能部门受理
	private static final String DISTRICT_DEPT_HANDLE_NODE_CODE = "task13";	//县区职能部门办理
	
	private static final String CITY_UNIT_ACCEPT_NODE_CODE = "task14";	//市综治中心受理
	private static final String CITY_UNIT_HANDLE_NODE_CODE = "task15";	//市综治中心办理
	
	private static final String CITY_DEPT_ACCEPT_NODE_CODE = "task16";	//市职能部门受理
	private static final String CITY_DEPT_HANDLE_NODE_CODE = "task17";	//市职能部门办理
	
	private static final String PROVINCE_UNIT_ACCEPT_NODE_CODE = "task18";	//省级综治中心受理
	private static final String PROVINCE_UNIT_HANDLE_NODE_CODE = "task19";	//省级综治中心办理
	
	private static final String PROVINCE_DEPT_ACCEPT_NODE_CODE = "task20";	//省级职能部门受理
	private static final String PROVINCE_DEPT_HANDLE_NODE_CODE = "task21";	//省级职能部门办理

    private static final String END_NODE_CODE = "end1";			//归档环节

    private static final String subStatus_09 = "09";		//受理中
    
    @SuppressWarnings("serial")
	public final static Map<String, String> NodeMapping = new HashMap<String, String>() {
	    {
	        put("task2", "task3");
	        put("task4", "task5");
	        put("task6", "task7");
	        put("task8", "task9");
	        put("task10", "task11");
	        put("task12", "task13");
	        put("task14", "task15");
	        put("task16", "task17");
	        put("task18", "task19");
	        put("task20", "task21");
	    }
	};
    
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
			this.addUserDetailScore(eventId,userId,orgId);
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
		Long createUserId = -1L, createOrgId = -1L, closeUserId = -1L, closeOrgId = -1L;
		String scoreType = null, createUserName = null;
		String SCORE_TYPE_1 = "B1", SCORE_TYPE_2 = "B2";
		
		if(eventId != null && eventId > 0) {
			ProInstance pro = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
			if(pro != null) {
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
    protected String updateEventStatus(Long eventId, Long userId, Long orgId, String chiefLevel, String curNodeCode, String nextNodeCode, Date handleDate, Map<String, Object> extraParam) throws Exception {
		String eventStatus = "";
		String eventSubStatus = "";
		String currentNodeCode="";
		eventStatus = super.updateEventStatus(eventId, userId, orgId, chiefLevel, curNodeCode, nextNodeCode, handleDate, extraParam);
		
		try {
			if(StringUtils.isBlank(currentNodeCode)) {
				ProInstance pro = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
				
				if(pro != null) {
					currentNodeCode = pro.getCurNode();
				}
			}
			
			if(CommonFunctions.isNotBlank(NodeMapping, currentNodeCode)) {
				eventSubStatus=subStatus_09;
			}else {
				eventSubStatus=ConstantValue.HANDLING_STATUS;
			}
			
			if("end1".equals(nextNodeCode)) {
				eventSubStatus="";
			}
			
			EventDisposal event =new EventDisposal();
			event.setEventId(eventId);
			event.setSubStatus(eventSubStatus);
			event.setUpdatorId(userId);
			
			eventDisposalService.updateEventDisposalById(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return eventStatus;
	}
    
    protected Map<String, Object> initStatusMap(String chiefLevel) {
        Map<String, Object> statusMap = new HashMap<String,Object>();
        
        statusMap.put(START_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);		//事件启动流程
		statusMap.put(COLLECT_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);		//事件采集
        
        //相关流转状态
		statusMap.put(GRID_ADMIN_HANDLE_NODE_CODE + "-" + COMMUNITY_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(GRID_ADMIN_HANDLE_NODE_CODE + "-" + TOWNDISPOSAl_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(GRID_ADMIN_HANDLE_NODE_CODE + "-" + TOWNDISPOSAl_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(GRID_ADMIN_HANDLE_NODE_CODE + "-" + DISTRICT_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(GRID_ADMIN_HANDLE_NODE_CODE + "-" + DISTRICT_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
       
        statusMap.put(COMMUNITY_UNIT_HANDLE_NODE_CODE + "-" + GRID_ADMIN_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(COMMUNITY_UNIT_HANDLE_NODE_CODE + "-" + TOWNDISPOSAl_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(COMMUNITY_UNIT_HANDLE_NODE_CODE + "-" + TOWNDISPOSAl_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(COMMUNITY_UNIT_HANDLE_NODE_CODE + "-" + DISTRICT_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(COMMUNITY_UNIT_HANDLE_NODE_CODE + "-" + DISTRICT_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        
        statusMap.put(TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE + "-" + GRID_ADMIN_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE + "-" + COMMUNITY_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE + "-" + TOWNDISPOSAl_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE + "-" + DISTRICT_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE + "-" + DISTRICT_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        
        statusMap.put(TOWNDISPOSAl_DEPT_HANDLE_NODE_CODE + "-" + GRID_ADMIN_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(TOWNDISPOSAl_DEPT_HANDLE_NODE_CODE + "-" + COMMUNITY_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(TOWNDISPOSAl_DEPT_HANDLE_NODE_CODE + "-" + TOWNDISPOSAl_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(TOWNDISPOSAl_DEPT_HANDLE_NODE_CODE + "-" + DISTRICT_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(TOWNDISPOSAl_DEPT_HANDLE_NODE_CODE + "-" + DISTRICT_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        
        statusMap.put(DISTRICT_UNIT_HANDLE_NODE_CODE + "-" + GRID_ADMIN_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(DISTRICT_UNIT_HANDLE_NODE_CODE + "-" + COMMUNITY_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(DISTRICT_UNIT_HANDLE_NODE_CODE + "-" + TOWNDISPOSAl_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(DISTRICT_UNIT_HANDLE_NODE_CODE + "-" + TOWNDISPOSAl_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(DISTRICT_UNIT_HANDLE_NODE_CODE + "-" + DISTRICT_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(DISTRICT_UNIT_HANDLE_NODE_CODE + "-" + CITY_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(DISTRICT_UNIT_HANDLE_NODE_CODE + "-" + CITY_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        
        statusMap.put(DISTRICT_DEPT_HANDLE_NODE_CODE + "-" + GRID_ADMIN_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(DISTRICT_DEPT_HANDLE_NODE_CODE + "-" + COMMUNITY_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(DISTRICT_DEPT_HANDLE_NODE_CODE + "-" + TOWNDISPOSAl_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(DISTRICT_DEPT_HANDLE_NODE_CODE + "-" + TOWNDISPOSAl_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(DISTRICT_DEPT_HANDLE_NODE_CODE + "-" + DISTRICT_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(DISTRICT_DEPT_HANDLE_NODE_CODE + "-" + CITY_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(DISTRICT_DEPT_HANDLE_NODE_CODE + "-" + CITY_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        
        
        statusMap.put(CITY_UNIT_HANDLE_NODE_CODE + "-" + DISTRICT_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(CITY_UNIT_HANDLE_NODE_CODE + "-" + DISTRICT_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(CITY_UNIT_HANDLE_NODE_CODE + "-" + CITY_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(CITY_UNIT_HANDLE_NODE_CODE + "-" + PROVINCE_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(CITY_UNIT_HANDLE_NODE_CODE + "-" + PROVINCE_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);

        
        statusMap.put(CITY_DEPT_HANDLE_NODE_CODE + "-" + DISTRICT_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(CITY_DEPT_HANDLE_NODE_CODE + "-" + DISTRICT_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(CITY_DEPT_HANDLE_NODE_CODE + "-" + CITY_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(CITY_DEPT_HANDLE_NODE_CODE + "-" + PROVINCE_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(CITY_DEPT_HANDLE_NODE_CODE + "-" + PROVINCE_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        
        
        statusMap.put(PROVINCE_UNIT_HANDLE_NODE_CODE + "-" + PROVINCE_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(PROVINCE_UNIT_HANDLE_NODE_CODE + "-" + CITY_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(PROVINCE_UNIT_HANDLE_NODE_CODE + "-" + CITY_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        
        
        statusMap.put(PROVINCE_DEPT_HANDLE_NODE_CODE + "-" + CITY_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(PROVINCE_DEPT_HANDLE_NODE_CODE + "-" + CITY_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(PROVINCE_DEPT_HANDLE_NODE_CODE + "-" + PROVINCE_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        
        statusMap.put(END_NODE_CODE, ConstantValue.EVENT_STATUS_END);
        
        return statusMap;
    }
   
}
