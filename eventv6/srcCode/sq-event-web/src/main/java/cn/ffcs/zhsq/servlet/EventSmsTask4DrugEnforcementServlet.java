package cn.ffcs.zhsq.servlet;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.drugEnforcementEvent.service.IDrugEnforcementEventService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 禁毒事件轮询
 * @author zhangls
 *
 */
@Component("eventSmsTask4DrugEnforcementServlet") 
public class EventSmsTask4DrugEnforcementServlet implements
		ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IDrugEnforcementEventService drugEnforcementEventService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String DEFAULT_PERIOD = "1800000";//默认设置时间间隔
	private volatile boolean isRunning = true;//用于判断当次轮询是否完成
	private volatile AtomicBoolean isInit = new AtomicBoolean(false);//用于控制onApplicationEvent内部方法只执行一次
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(!isInit.compareAndSet(false, true)) {
			String defaultOrgCodeStr = funConfigurationService.changeCodeToValue(ConstantValue.SMS_TASK_ATTRIBUTE, ConstantValue.DRUG_ENFORCEMENT_EVENT_DEFAULT_ORG_CODE, IFunConfigurationService.CFG_TYPE_FACT_VAL);
			
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
							
							threadSetting(orgInfo, infoOrgCode);
						} else {
							logger.error("组织编码[" + defaultOrgCode + "] 没有有效的组织信息！");
						}
					}
				}
			}
		}
	}
	
	/**
	 * 线程设置
	 * @param orgInfo				默认组织信息
	 * @param defaultInfoOrgCode	默认组织编码对应的地域编码
	 */
	private void threadSetting(OrgSocialInfoBO orgInfo, String defaultInfoOrgCode) {
		String defaultOrgCode = orgInfo.getOrgCode(),
			   isValid = funConfigurationService.changeCodeToValue(ConstantValue.SMS_TASK_ATTRIBUTE, ConstantValue.IS_DRUG_ENFORCEMENT_EVENT_VALID, IFunConfigurationService.CFG_TYPE_FACT_VAL, defaultOrgCode);
		
		boolean isStart = StringUtils.isNotBlank(isValid) && Boolean.valueOf(isValid);
		
		try {
			if(isStart) {
				Long period = 0L;
				//获取系统默认设置的时间间隔，没有则使用默认配置
				String eventDockingPeriod = funConfigurationService.changeCodeToValue(ConstantValue.SMS_TASK_ATTRIBUTE, ConstantValue.DRUG_ENFORCEMENT_EVENT_POLLING_PERIOD, IFunConfigurationService.CFG_TYPE_FACT_VAL, defaultOrgCode);
				final OrgSocialInfoBO defaultOrgInfo = orgInfo;
				final String infoOrgCode = defaultInfoOrgCode;
				
				Timer timer = new Timer(); 
				
				if(StringUtils.isBlank(eventDockingPeriod)) {
					eventDockingPeriod = DEFAULT_PERIOD;
				}
				
				try {
					period = Long.valueOf(eventDockingPeriod);
					if(period < 0) {
						period = Long.valueOf(DEFAULT_PERIOD);
					}
				} catch(Exception e) {
					period = Long.valueOf(DEFAULT_PERIOD);
					e.printStackTrace();
				}
				
			    //timer.schedule(task, firstTime, period); 
				// firstTime为Date类型,period为long   
				// 从firstTime时刻开始，每隔period毫秒执行一次。 
			    timer.schedule(new TimerTask() {  
			        public void run() {
			        	if (isRunning) {
			        		isRunning = false;
			        		
			        		try {
								handleDrugEnforcementEventTask(defaultOrgInfo, infoOrgCode);
							} catch (Exception e) {
								e.printStackTrace();
							}
			        		
			        		isRunning = true;
			        	}
			        }  
			    }, new Date(), period);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 禁毒事件短信发送处理
	 * @param orgInfo		组织信息
	 * @param infoOrgCode	地域编码
	 * @throws Exception
	 */
	private void handleDrugEnforcementEventTask(OrgSocialInfoBO orgInfo, String infoOrgCode) throws Exception {
		StringBuffer sql = new StringBuffer("");
		String orgCode = orgInfo.getOrgCode(),
			   MSG_SEND_MID_BIZ_TYPE = "DRUG_ENFORCEMENT_EVENT_TASK";//消息中间表业务类型
		
		sql.append(" SELECT T1.* ")
		   .append(" FROM V_DRUG_ENFORCE_TASK_OVER_TIME T1 ")
		   .append(" WHERE T1.INFO_ORG_CODE LIKE '").append(infoOrgCode).append("%' ")
		   .append(" AND NOT EXISTS ")
		   .append("	(SELECT 1 ")
		   .append("	 FROM T_BAS_MSG_SEND_MID MID ")
		   .append("	 WHERE MID.BIZ_ID = T1.TASK_ID ")
		   .append("		AND MID.BIZ_TYPE = '").append(MSG_SEND_MID_BIZ_TYPE).append("' ")
		   .append("		AND MID.SEND_TYPE = '").append(ConstantValue.DRUG_ENFORCEMENT_EVENT_TASK_OVER_TIME_NOTE).append("' ")
		   .append("	) ");
		
		List<Map<String, Object>> resultMapList = jdbcTemplate.queryForList(sql.toString());
		
		if(resultMapList != null && resultMapList.size() > 0) {
			UserInfo userInfo = new UserInfo();
			Map<String, Object> extraParam = new HashMap<String, Object>(),
								smsParam = new HashMap<String, Object>();
			Long drugEnforcementId = null, curTaskId = null, daySpan = null;
			String smsContent = "",
				   isMsgSendMidSubValidStr = null;
			
			userInfo.setOrgCode(orgCode);
			userInfo.setOrgId(orgInfo.getOrgId());
			userInfo.setOrgName(orgInfo.getOrgName());
			
			for(Map<String, Object> resultMap : resultMapList) {
				extraParam.clear();
				smsParam.clear();
				
				if(CommonFunctions.isNotBlank(resultMap, "DRUG_ENFORCEMENT_ID")) {
					drugEnforcementId = Long.valueOf(resultMap.get("DRUG_ENFORCEMENT_ID").toString());
				}
				if(CommonFunctions.isNotBlank(resultMap, "TASK_ID")) {
					curTaskId = Long.valueOf(resultMap.get("TASK_ID").toString());
				}
				if(CommonFunctions.isNotBlank(resultMap, "DAY_SPAN")) {
					daySpan = Long.valueOf(resultMap.get("DAY_SPAN").toString());
					
					if(daySpan > 0) {
						extraParam.put("daySpan", daySpan);
					}
				}
				
				extraParam.put("contentType", ConstantValue.DRUG_ENFORCEMENT_EVENT_TASK_OVER_TIME_NOTE);
				
				//获取短信内容
				smsContent = drugEnforcementEventService.capSMSContent(drugEnforcementId, null, null, userInfo, extraParam);
				
				smsParam.put("smsContent", smsContent);
				smsParam.put("isAutoSendSms", true);//自动发送短信
				smsParam.put("isCheckDuplication", true);//进行重复性验证
				smsParam.put("formId", drugEnforcementId);
				smsParam.put("msgSendMidBizId", curTaskId);
				smsParam.put("msgSendMidBizType", MSG_SEND_MID_BIZ_TYPE);
				smsParam.put("msgSendMidSendType", ConstantValue.DRUG_ENFORCEMENT_EVENT_TASK_OVER_TIME_NOTE);
				
				isMsgSendMidSubValidStr = funConfigurationService.changeCodeToValue(ConstantValue.SMS_TASK_ATTRIBUTE, ConstantValue.IS_SEND_DRUG_ENFOCEMENT_EVENT_TASK_OVER_TIME_SMS, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
				if(StringUtils.isNotBlank(isMsgSendMidSubValidStr)) {
					smsParam.put("isMsgSendMidSubValid", Boolean.valueOf(isMsgSendMidSubValidStr));//是否发送短信
				}
				
				//发送短信
				drugEnforcementEventService.sendSMS(userInfo, smsParam);
				
			}
		}
	}

}
