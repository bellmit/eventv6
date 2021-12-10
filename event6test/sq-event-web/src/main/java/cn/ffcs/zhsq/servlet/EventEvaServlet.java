package cn.ffcs.zhsq.servlet;

import cn.ffcs.shequ.zzgl.service.event.IEvaResultService;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExpand.service.IEventDisposal4ExpandService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 事件自动评价 归档
 * 可自动评价事件状态为03
 * 自动评价等级为满意，评价意见为满意
 * 
 * 功能配置编码：SMS_TASK_ATTRIBUTE
 * 触发条件：
 * 以下配置需要项目重启后生效
 * evaDefaultOrgCode	自动评价归档轮询默认组织编码，多个值使用英文逗号连接
 * evaPollingPeriod		自动评价归档轮询周期，单位为小时
 * 
 * 以下配置可即时生效
 * isEvaValid			是否启用自动评价归档轮询，true为启用
 * evaPaginationLimit	自动评价归档每页处理记录数
 * evaTaskName			获取可自动评价事件需要经过的指定环节名称，多个值使用英文逗号分隔
 * evaEventStatus		获取可进行自动评价的事件状态，默认为03，多个值使用英文逗号分隔
 * 
 * @author zhangls
 *
 */
@Component("eventEvaServlet") 
public class EventEvaServlet implements
		ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEvaResultService evaResultService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	//事件扩展信息接口
	@Autowired
	private IEventDisposal4ExpandService eventDisposalExpandService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String DEFAULT_PERIOD = "24",//默认设置时间间隔，单位为小时
				   		  DEFAULT_PAGINATION_LIMIT = "100";//默认设置的每页记录数
	private volatile boolean isRunning = true;//用于判断当次轮询是否完成
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() == null) {
			String defaultOrgCodeStr = funConfigurationService.changeCodeToValue(ConstantValue.SMS_TASK_ATTRIBUTE, ConstantValue.EVA_DEFAULT_ORG_CODE, IFunConfigurationService.CFG_TYPE_FACT_VAL);
			
			if(StringUtils.isNotBlank(defaultOrgCodeStr)) {
				String[] defaultOrgCodeArray = defaultOrgCodeStr.split(",");
				String infoOrgCode = "";
				OrgEntityInfoBO orgEntityInfo = null;
				OrgSocialInfoBO orgInfo = null;
				
				for(String defaultOrgCode : defaultOrgCodeArray) {
					if(StringUtils.isNotBlank(defaultOrgCode) && StringUtils.isNotBlank(defaultOrgCode.trim())) {
						defaultOrgCode = defaultOrgCode.trim();
						
						orgInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode(defaultOrgCode);
						
						if(orgInfo != null) {
							orgEntityInfo = orgSocialInfoService.selectOrgEntityInfoByOrgCode(defaultOrgCode);
							if(orgEntityInfo != null) {
								infoOrgCode = orgEntityInfo.getOrgCode();
							}
							
							threadSetting4Eva(orgInfo, infoOrgCode);
						} else {
							logger.error("组织编码[" + defaultOrgCode + "] 没有有效的组织信息！");
						}
					}
				}
			}
		}
	}
	
	/**
	 * 自动归档评价轮询设置
	 * @param orgInfo				组织信息
	 * @param defaultInfoOrgCode	默认地域编码
	 */
	private void threadSetting4Eva(OrgSocialInfoBO orgInfo, String defaultInfoOrgCode) {
		final String defaultOrgCode = orgInfo.getOrgCode();
		String isValid = funConfigurationService.changeCodeToValue(ConstantValue.SMS_TASK_ATTRIBUTE, ConstantValue.IS_EVA_VALID, IFunConfigurationService.CFG_TYPE_FACT_VAL, defaultOrgCode);
		
		boolean isStart = StringUtils.isNotBlank(isValid) && Boolean.valueOf(isValid);
		
		try {
			if(isStart) {
				Long periodL = 0L;
				Double period = 0d;
				//获取系统默认设置的时间间隔，没有则使用默认配置
				String eventDockingPeriod = funConfigurationService.changeCodeToValue(ConstantValue.SMS_TASK_ATTRIBUTE, ConstantValue.EVA_POLLING_PERIOD, IFunConfigurationService.CFG_TYPE_FACT_VAL, defaultOrgCode);
				final String infoOrgCode = defaultInfoOrgCode;
				Timer timer = new Timer(); 
				
				if(StringUtils.isBlank(eventDockingPeriod)) {
					eventDockingPeriod = DEFAULT_PERIOD;
				}
				
				try {
					period = Double.valueOf(eventDockingPeriod);
					if(period < 0) {
						period = Double.valueOf(DEFAULT_PERIOD);
					}
				} catch(Exception e) {
					period = Double.valueOf(DEFAULT_PERIOD);
					e.printStackTrace();
				}
				
				periodL = Math.round(period * 60 * 60 * 1000);//转换为毫秒
				
			    //timer.schedule(task, firstTime, period); 
				// firstTime为Date类型,period为long   
				// 从firstTime时刻开始，每隔period毫秒执行一次。 
			    timer.schedule(new TimerTask() {  
			        public void run() {
			        	//轮询会快于global.properties的读取
			        	if (isRunning && StringUtils.isNotBlank(ConstantValue.WORKFLOW_DB)) {
			        		String paginationLimitStr = funConfigurationService.changeCodeToValue(ConstantValue.SMS_TASK_ATTRIBUTE, ConstantValue.EVA_PAGINATION_LIMIT, IFunConfigurationService.CFG_TYPE_FACT_VAL, defaultOrgCode),
			        			   evaTaskName = funConfigurationService.changeCodeToValue(ConstantValue.SMS_TASK_ATTRIBUTE, ConstantValue.EVA_TASK_NAME, IFunConfigurationService.CFG_TYPE_FACT_VAL, defaultOrgCode),
			        			   evaEventStatus = funConfigurationService.changeCodeToValue(ConstantValue.SMS_TASK_ATTRIBUTE, ConstantValue.EVA_EVENT_STATUS, IFunConfigurationService.CFG_TYPE_FACT_VAL, defaultOrgCode);
			        		Integer paginationLimit = 0;
			        		isRunning = false;
			        		
			        		if(StringUtils.isBlank(paginationLimitStr)) {
								paginationLimitStr = DEFAULT_PAGINATION_LIMIT;
							}
			        		
			        		try {
								paginationLimit = Integer.valueOf(paginationLimitStr);
								if(paginationLimit < 0) {
									paginationLimit = Integer.valueOf(DEFAULT_PAGINATION_LIMIT);
								}
							} catch(Exception e) {
								paginationLimit = Integer.valueOf(DEFAULT_PAGINATION_LIMIT);
								e.printStackTrace();
							}
			        		
			        		try {
			        			findEvent4Eva(infoOrgCode, paginationLimit, evaTaskName, evaEventStatus);
							} catch (Exception e) {
								e.printStackTrace();
							}
			        		
			        		isRunning = true;
			        	}
			        }  
			    }, new Date(), periodL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取需要自动评价归档的事件
	 * @param infoOrgCode		地域编码
	 * @param paginationLimit	每页记录数
	 * @param evaTaskName		环节名称
	 * @param evaEventStatus	事件状态
	 */
	private void findEvent4Eva(String infoOrgCode, int paginationLimit, String evaTaskName, String evaEventStatus) {
		StringBuffer sql = new StringBuffer("");
		List<Map<String, Object>> resultMapList = null;
		List<Object> queryParam = new ArrayList<Object>();
		
		sql.append(" SELECT T1.EVENT_ID, T3.INSTANCE_ID, T5.TASK_ID, T5.USER_ID, T5.ORG_ID, T5.USER_TYPE ")
		   .append(" FROM T_EVENT T1 ")
		   .append(" INNER JOIN T_DC_GRID T2 ")
		   .append("   ON T1.GRID_ID = T2.GRID_ID ")
		   .append(" INNER JOIN ").append(ConstantValue.WORKFLOW_DB).append("WF_PROCESS_INSTANCE T3 ")
		   .append("   ON T1.EVENT_ID = T3.FORM_ID ")
		   .append(" INNER JOIN ").append(ConstantValue.WORKFLOW_DB).append("JBPM4_TASK T4 ")
		   .append("   ON T3.INSTANCE_ID = T4.PROCINST_ ")
		   .append(" INNER JOIN ").append(ConstantValue.WORKFLOW_DB).append("WF_TASK_PARTICIPATION T5 ")
		   .append("   ON T4.DBID_ = T5.TASK_ID ")
		   .append(" INNER JOIN WORKFLOW.WF_TASK T6 ")
		   .append("   ON T3.INSTANCE_ID = T6.INSTANCE_ID ")
		   .append(" WHERE T3.FORM_TYPE_ID = 300 AND T3.STATUS = '1' AND T2.STATUS = '001' ");
		
		if(StringUtils.isNotBlank(evaEventStatus)) {
			if(evaEventStatus.contains(",")) {
				String[] evaEventStatusArray = evaEventStatus.replace(" ", "").split(",");
				StringBuffer paramBuffer = new StringBuffer("");
				sql.append(" AND T1.STATUS IN (");
				
				for(String eventStatus : evaEventStatusArray) {
					paramBuffer.append(",").append("?");
					queryParam.add(eventStatus);
				}
				
				sql.append(paramBuffer.substring(1)).append(") ");
			} else {
				sql.append(" AND T1.STATUS = ? ");
				queryParam.add(evaEventStatus);
			}
		} else {
			sql.append(" AND T1.STATUS = '03' ");
		}
		
		if(StringUtils.isNotBlank(evaTaskName) && evaTaskName.contains(",")) {
			String[] evaTaskNameArray = evaTaskName.replace(" ", "").split(",");
			StringBuffer paramBuffer = new StringBuffer("");
			sql.append(" AND T6.TASK_NAME IN (");
			
			for(String taskName : evaTaskNameArray) {
				paramBuffer.append(",").append("?");
				queryParam.add(taskName);
			}
			
			sql.append(paramBuffer.substring(1)).append(") ");
		} else {
			sql.append(" AND T6.TASK_NAME = ? ");
			queryParam.add(evaTaskName);
		}
		
		if(StringUtils.isNotBlank(infoOrgCode)) {
			sql.append(" AND T2.INFO_ORG_CODE LIKE ? || '%' ");
			queryParam.add(infoOrgCode);
		}
		
		sql.append(" AND ROWNUM > 0 AND ROWNUM <= ? ")
		   .append(" ORDER BY T1.FIN_TIME DESC ");
		queryParam.add(paginationLimit);
		
		resultMapList = jdbcTemplate.queryForList(sql.toString(), queryParam.toArray());
		
		if(resultMapList != null) {
			Map<Object, Map<String, Object>> eventResultMap = new HashMap<Object, Map<String, Object>>();
			
			for(Map<String, Object> eventMap : resultMapList) {
				if(CommonFunctions.isNotBlank(eventMap, "EVENT_ID")) {
					eventResultMap.put(eventMap.get("EVENT_ID"), eventMap);
				}
			}
			
			//有多人办理时，只需要保留一条就可以了
			if(!eventResultMap.isEmpty()) {
				resultMapList = new ArrayList<Map<String, Object>>();
				
				for(Object mapKey : eventResultMap.keySet()) {
					resultMapList.add(eventResultMap.get(mapKey));
				}
			}
			
			for(Map<String, Object> eventMap : resultMapList) {
				try {
					autoEva4Event(eventMap);
				} catch (Exception e) {//防止单条失败，导致循环终止
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 自动评价并归档
	 * @param eventMap
	 * 			EVENT_ID	事件id
	 * 			INSTANCE_ID	流程实例id
	 * 			TASK_ID		任务id
	 * 			USER_ID		USER_TYPE为3时；当前办理用户id；USER_TYPE为1时：当前办理组织id；
	 * 			ORG_ID		USER_TYPE为3时：当前办理组织id；
	 * 			USER_TYPE	当前办理用户类型 1 组织；3 用户；
	 * @throws Exception
	 */
	private void autoEva4Event(Map<String, Object> eventMap) throws Exception {
		Long eventId = -1L, evaUserId = -1L, evaOrgId = -1L;
		String DEFAULT_EVA_LEVEL = "02", DEFAULT_EVA_CONTENT = "满意",
			   userType = null,
			   evaUserName = null, evaOrgName = null;
		UserInfo userInfo = new UserInfo();
		StringBuffer msgWrong = new StringBuffer("");
		
		if(eventMap != null && !eventMap.isEmpty()) {
			Long handerUserId = -1L, handerOrgId = -1L;
			
			if(CommonFunctions.isNotBlank(eventMap, "EVENT_ID")) {
				eventId = Long.valueOf(eventMap.get("EVENT_ID").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "USER_ID")) {
				handerUserId = Long.valueOf(eventMap.get("USER_ID").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "ORG_ID")) {
				handerOrgId = Long.valueOf(eventMap.get("ORG_ID").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "USER_TYPE")) {
				userType = eventMap.get("USER_TYPE").toString();
			}
			
			if(IEventDisposalWorkflowService.ACTOR_TYPE_USER.equals(userType)) {
				evaUserId = handerUserId;
				evaOrgId = handerOrgId;
				
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(evaOrgId);
				if(orgInfo != null) {
					evaOrgName = orgInfo.getOrgName();
					userInfo.setOrgCode(orgInfo.getOrgCode());
				} else {
					msgWrong.append("组织id[").append(evaOrgId).append("]没有对应有效的组织信息！");
				}
				UserBO userBO = userManageService.getUserInfoByUserId(evaUserId);
				if(userBO != null) {
					evaUserName = userBO.getPartyName();
				} else {
					msgWrong.append("用户id[").append(evaUserId).append("]没有对应有效的用户信息！");
				}
			} else if(IEventDisposalWorkflowService.ACTOR_TYPE_ORG.equals(userType)) {
				evaOrgId = handerUserId;
				
				List<UserBO> userList = userManageService.getUserListByUserExampleParamsOut(null, null, evaOrgId);
				if(userList != null && userList.size() > 0) {
					UserBO user = userList.get(0);
					evaUserId = user.getUserId();
					evaUserName = user.getPartyName();
					evaOrgName = user.getOrgName();
					userInfo.setOrgCode(user.getOrgCode());
				} else {
					msgWrong.append("组织id[").append(evaOrgId).append("]没有找到有效的用户信息！");
				}
			}
		}
		
		if(msgWrong.length() == 0 && eventId > 0 && StringUtils.isNotBlank(evaUserName) && StringUtils.isNotBlank(evaOrgName)) {
			Long instanceId = -1L, currentTaskId = -1L;
			
			if(CommonFunctions.isNotBlank(eventMap, "INSTANCE_ID")) {
				instanceId = Long.valueOf(eventMap.get("INSTANCE_ID").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "TASK_ID")) {
				currentTaskId =  Long.valueOf(eventMap.get("TASK_ID").toString());
			}
			
			if(instanceId > 0 && currentTaskId > 0) {
				Map<String, Object> extraParam = new HashMap<String, Object>();
				boolean archiveFlag = false;
				
				userInfo.setUserId(evaUserId);
				userInfo.setOrgId(evaOrgId);
				userInfo.setOrgName(evaOrgName);
				userInfo.setPartyName(evaUserName);
				
				extraParam.put("instanceId", instanceId);
				extraParam.put("taskId", currentTaskId);

				//构造评价信息，评价等级为：满意
				//归档成功后，才增添评价信息，否则会因为评价环节有多人时，会有多条评价意见
				extraParam.put("isEvaluate",true);
				extraParam.put("eventId",eventId);
				extraParam.put("evaLevel",DEFAULT_EVA_LEVEL);
				extraParam.put("evaContent",DEFAULT_EVA_CONTENT);

				archiveFlag = eventDisposalWorkflowService.archiveAndEndWorkflowForEvent(eventId, userInfo, DEFAULT_EVA_CONTENT, extraParam);
				
				if(!archiveFlag) {
					msgWrong.append("事件id[").append(eventId).append("]归档失败！");
				}
			}
		}
		
		if(msgWrong.length() > 0) {
			throw new Exception(msgWrong.toString());
		}
	}

}
