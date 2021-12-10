package cn.ffcs.zhsq.servlet;

import java.util.ArrayList;
import java.util.Date;
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

import cn.ffcs.common.message.bo.ReceiverBO;
import cn.ffcs.shequ.zzgl.service.holidayinfo.IHolidayInfoService;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * 南昌(NCH) 大联动事件消息发送
 * @author zhangls
 *
 */
@Component("eventMsg4NCHJointCooperationServlet") 
public class EventMsg4NCHJointCooperationServlet implements
		ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private MessageOutService messageService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IHolidayInfoService holidayInfoService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String DEFAULT_PERIOD = "1";//默认设置时间间隔，单位为天
	private volatile boolean isRunning = true;//用于判断当次轮询是否完成
	private volatile AtomicBoolean isInit = new AtomicBoolean(false);//用于控制onApplicationEvent内部方法只执行一次
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(!isInit.compareAndSet(false, true)) {
			String defaultOrgCodeStr = funConfigurationService.changeCodeToValue(ConstantValue.SMS_TASK_ATTRIBUTE, ConstantValue.NCHJC_MSG_DEFAULT_ORG_CODE, IFunConfigurationService.CFG_TYPE_FACT_VAL);
			
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
			   isValid = funConfigurationService.changeCodeToValue(ConstantValue.SMS_TASK_ATTRIBUTE, ConstantValue.IS_SEND_MSG_NCHJC_VALID, IFunConfigurationService.CFG_TYPE_FACT_VAL, defaultOrgCode);
		
		boolean isStart = StringUtils.isNotBlank(isValid) && Boolean.valueOf(isValid);
		
		try {
			if(isStart) {
				Long period = 0L;
				//获取系统默认设置的时间间隔，没有则使用默认配置
				String eventDockingPeriod = funConfigurationService.changeCodeToValue(ConstantValue.SMS_TASK_ATTRIBUTE, ConstantValue.NCHJC_MSG_POLLING_PERIOD, IFunConfigurationService.CFG_TYPE_FACT_VAL, defaultOrgCode);
				final String infoOrgCode = defaultInfoOrgCode;
				Date firstDate = DateUtils.convertStringToDate(DateUtils.getToday() + " 16:30:00", DateUtils.PATTERN_24TIME);
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
				
				period = period * 24 * 60 * 60 * 1000;//转换为毫秒
				
			    //timer.schedule(task, firstTime, period); 
				// firstTime为Date类型,period为long   
				// 从firstTime时刻开始，每隔period毫秒执行一次。 
			    timer.schedule(new TimerTask() {  
			        public void run() {
			        	if (isRunning) {
			        		isRunning = false;
			        		
			        		try {
								findEvent4MsgSend(2, infoOrgCode);
								findEvent4MsgSend(3, infoOrgCode);
							} catch (Exception e) {
								e.printStackTrace();
							}
			        		
			        		isRunning = true;
			        	}
			        }  
			    }, firstDate, period);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取需要辖区内需要发送消息的事件
	 * @param handleDateFlag	时限状态 2 将到期；3 已过期
	 * @param infoOrgCode		地域编码
	 */
	private void findEvent4MsgSend(int handleDateFlag, String infoOrgCode) {
		StringBuffer sql = new StringBuffer("");
		List<Map<String, Object>> resultMapList = null;
		
		sql.append(" SELECT T1.EVENT_ID, T3.INSTANCE_ID, T1.CODE_, T5.TASK_ID, T5.USER_ID, T5.ORG_ID, T5.USER_TYPE ")
		   .append(" FROM T_EVENT T1 ")
		   .append(" INNER JOIN T_DC_GRID T2 ")
		   .append("   ON T1.GRID_ID = T2.GRID_ID ")
		   .append(" INNER JOIN WORKFLOW.WF_PROCESS_INSTANCE T3 ")
		   .append("   ON T1.EVENT_ID = T3.FORM_ID ")
		   .append(" LEFT JOIN WORKFLOW.JBPM4_TASK T4 ")
		   .append("   ON T3.INSTANCE_ID = T4.PROCINST_ ")
		   .append(" LEFT JOIN WORKFLOW.WF_TASK_PARTICIPATION T5 ")
		   .append("   ON T4.DBID_ = T5.TASK_ID ")
		   .append(" WHERE T3.FORM_TYPE_ID = 300 AND T3.STATUS = '1' AND T2.STATUS = '001' AND T1.STATUS IN ('00', '01', '02', '03') ")
		   .append(" AND T5.TASK_ID IS NOT NULL ")
		   .append(" AND NOT EXISTS ( ")
		   .append("  SELECT 1 ")
		   .append("  FROM T_BAS_MSG T6 ")
		   .append("  WHERE T6.VIEW_LINK LIKE '%handleDateFlag=").append(handleDateFlag).append("&currentTaskId=' || T5.TASK_ID || '&eventId=' || T1.EVENT_ID || '%' ")
		   .append("  AND T6.MODULE_CODE = '02' ")
		   .append("  AND T6.IS_DEL = '0' ")
		   .append(" ) ");
		
		if(StringUtils.isNotBlank(infoOrgCode)) {
			sql.append(" AND T2.INFO_ORG_CODE LIKE '%").append(infoOrgCode).append("%' ");
		}
		switch(handleDateFlag) {
			case 2: {
				String handleDateEnd = DateUtils.formatDate(holidayInfoService.getWorkDateByAfterWorkDay(new Date(), 1), DateUtils.PATTERN_24TIME);
				
				sql.append(" AND T1.HANDLE_DATE < TO_DATE('").append(handleDateEnd).append("', 'YYYY-MM-DD HH24:MI:SS')");
				
				break;
			}
			case 3: {
				sql.append(" AND T1.HANDLE_DATE_FLAG = '3' ");
				
				break;
			}
		}
		
		resultMapList = jdbcTemplate.queryForList(sql.toString());
		if(resultMapList != null) {
			for(Map<String, Object> eventMap : resultMapList) {
				sendMsg4Event(eventMap, handleDateFlag);
			}
		}
	}
	
	/**
	 * 发送事件消息
	 * @param eventMap			事件信息
	 * 			EVENT_ID		事件id
	 * 			INSTANCE_ID		实例id
	 * 			CODE_			事件编码
	 * 			USER_ID			当前办理人员id
	 * 			ORG_ID			当前办理组织id
	 * 			USER_TYPE		办理人员类型 3 用户；1 组织；		
	 * @param handleDateFlag	时限状态 2 将到期；3 已过期
	 */
	private void sendMsg4Event(Map<String, Object> eventMap, int handleDateFlag) {
		Long eventId = -1L, instanceId = -1L, currentTaskId = -1L, receiverId = -1L;
		String eventCode = null, userType = null;
		
		if(eventMap != null && !eventMap.isEmpty()) {
			if(CommonFunctions.isNotBlank(eventMap, "EVENT_ID")) {
				eventId = Long.valueOf(eventMap.get("EVENT_ID").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "INSTANCE_ID")) {
				instanceId = Long.valueOf(eventMap.get("INSTANCE_ID").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "TASK_ID")) {
				currentTaskId =  Long.valueOf(eventMap.get("TASK_ID").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "CODE_")) {
				eventCode = eventMap.get("CODE_").toString();
			}
			if(CommonFunctions.isNotBlank(eventMap, "USER_ID")) {
				receiverId = Long.valueOf(eventMap.get("USER_ID").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "USER_TYPE")) {
				userType = eventMap.get("USER_TYPE").toString();
			}
		}
		
		if(eventId > 0 && instanceId > 0 && receiverId > 0) {
			List<Long> nextUserIdList = new ArrayList<Long>();
			ReceiverBO receiver = new ReceiverBO();
			String EVENT_MEG_MODULE_CODE = "02",//事件消息所属模块编码
					   EVENT_VIEW_LINK = "$EVENT_DOMAIN/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=todo",//事件消息查看详情链接
					   EVENT_MSG_ACTION = "系统发送了";//消息发送操作
			StringBuffer viewLink = new StringBuffer(""),
						 msgContent = new StringBuffer("您的待办事件");//消息内容
			
			if(StringUtils.isNotBlank(eventCode)) {
				msgContent.append("，事件编号为：").append(eventCode);
			}
			
			switch(handleDateFlag) {
				case 2: {
					msgContent.append("，即将到期");
					break;
				}
				case 3: {
					msgContent.append("，已经超期");
					break;
				}
			}
			msgContent.append("，请尽快处理");
			
			nextUserIdList.add(receiverId);
			if(IEventDisposalWorkflowService.ACTOR_TYPE_USER.equals(userType)) {
				receiver.setUserIdList(nextUserIdList);
			} else if(IEventDisposalWorkflowService.ACTOR_TYPE_ORG.equals(userType)) {
				receiver.setOrgIdList(nextUserIdList);
			}
			
			viewLink.append(EVENT_VIEW_LINK)
					.append("&handleDateFlag=").append(handleDateFlag)
					.append("&currentTaskId=").append(currentTaskId)
					.append("&eventId=").append(eventId)
					.append("&instanceId=").append(instanceId);
			
			messageService.sendCommonMessage(null, null, EVENT_MEG_MODULE_CODE, viewLink.toString(), msgContent.toString(), EVENT_MSG_ACTION, receiver);
		}
	}

}
