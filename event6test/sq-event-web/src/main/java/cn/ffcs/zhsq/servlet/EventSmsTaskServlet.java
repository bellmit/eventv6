package cn.ffcs.zhsq.servlet;

import java.text.ParseException;
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
import org.springframework.stereotype.Component;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.wap.util.DateUtils;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.smsTask.SmsTask;
import cn.ffcs.zhsq.smsTask.service.ISmsTaskService;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 事件将到期、已过期短信发送
 * 目前未找到使用场景，有出现时，在对该功能进行优化 20190802
 * @author zhangls
 *
 */
@Component("EventSmsTaskServlet") 
public class EventSmsTaskServlet implements ApplicationListener<ContextRefreshedEvent> {
	//spring中已经内置的几种事件：
	//ContextClosedEvent、ContextRefreshedEvent、ontextStartedEvent、ContextStoppedEvent、RequestHandleEvent 
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String defaultPeriod = "1800000";//默认设置时间间隔
	private volatile AtomicBoolean isStart = new AtomicBoolean(false);//用于控制onApplicationEvent内部方法只执行一次
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IFunConfigurationService configurationService;
	
	@Autowired
	private ISmsTaskService smsTaskService;
	
	/** 
	* 当一个ApplicationContext被初始化或刷新触发 
	*/ 
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			if(!isStart.compareAndSet(false, true)){
				String isValid = configurationService.turnCodeToValue(ConstantValue.IS_SEND_WILL_EXPIRE_SMS, "", IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
				
				if(StringUtils.isNotBlank(isValid) && Boolean.valueOf(isValid)) {
					Long period = 0L;
					//获取系统默认设置的时间间隔，没有则使用默认配置
					String overduePeriod = configurationService.changeCodeToValue(ConstantValue.WILL_EXPIRE_SMS_PERIOD, "", IFunConfigurationService.CFG_TYPE_FACT_VAL);
					Timer timer = new Timer(); 
					
					if(StringUtils.isBlank(overduePeriod)){
						overduePeriod = defaultPeriod;
					}
					
					try{
						period = Long.valueOf(overduePeriod);
						if(period < 0){
							period = Long.valueOf(defaultPeriod);
						}
					}catch(Exception e){
						period = Long.valueOf(defaultPeriod);
						e.printStackTrace();
					}
					
				    //timer.schedule(task, firstTime, period); 
					// firstTime为Date类型,period为long   
					// 从firstTime时刻开始，每隔period毫秒执行一次。 
				    timer.schedule(new TimerTask() {  
				        public void run() {  
				        	eventSmsTask();
				        }  
				    }, new Date(), period);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void eventSmsTask(){//向将到期事件发送短信提醒
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map> eventSmsList = smsTaskService.findListByView(param);
		
		for(int index = 0, length = eventSmsList.size(); index < length; index++){
			EventDisposal event = new EventDisposal();
			Object eventIdObj = eventSmsList.get(index).get("EVENT_ID");			//事件ID
			Object contentObj = eventSmsList.get(index).get("CONTENT_");			//事件内容
			Object eventNameObj = eventSmsList.get(index).get("EVENT_NAME");		//事件标题
			Object urgencyDegreeObj = eventSmsList.get(index).get("URGENCY_DEGREE");//事件紧急程度
			Object handleDateObj = eventSmsList.get(index).get("HANDLE_DATE");		//事件处理时限
			Object userIdObj = eventSmsList.get(index).get("USER_ID");				//当前办理人ID
			Object phoneNumObj = eventSmsList.get(index).get("VERIFY_MOBILE");		//当前办理人手机号码
			Object orgCodeObj = eventSmsList.get(index).get("ORG_CODE");			//当前办理人所在组织机构
			
			Long eventId = null;
			String eventContent = "";
			String eventName = "";
			String urgencyDegree = null;
			String handleDate = "";
			Long userId = 0L;
			String mobile = "";
			String orgCode = null;
			
			if(eventIdObj != null){
				eventId = Long.valueOf(eventIdObj.toString());
			}
			
			if(contentObj != null){
				eventContent = contentObj.toString();
				event.setContent(eventContent);
			}
			
			if(eventNameObj != null){
				eventName = eventNameObj.toString();
				event.setEventName(eventName);
			}
			
			if(urgencyDegreeObj != null){
				urgencyDegree = urgencyDegreeObj.toString();
			}
			
			if(handleDateObj != null){
				handleDate = handleDateObj.toString();
				try {
					Date handleDateD = DateUtils.convertStringToDate(handleDate, DateUtils.PATTERN_24TIME);
					handleDate = DateUtils.convertDateToString(handleDateD);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				event.setHandleDateStr(handleDate);
			}
			
			if(userIdObj != null){
				userId = Long.valueOf(userIdObj.toString());
			}
			
			if(phoneNumObj != null){
				mobile = phoneNumObj.toString();
			}
			
			if(orgCodeObj != null){
				orgCode = orgCodeObj.toString();
			}
			
			String timeInterval = configurationService.changeCodeToValue(ConstantValue.WILL_EXPIRE_TIME_INTERVAL, urgencyDegree, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
			if(StringUtils.isNotBlank(timeInterval)){
				event.setHandleDateInterval(Integer.valueOf(timeInterval));
			}
			//构建userInfo
			UserInfo userInfo = new UserInfo();
			userInfo.setOrgCode(orgCode);
			userInfo.setUserId(userId);
			
			//获取发送短信内容
			String toBeOverdueNote = eventDisposalWorkflowService.capSmsContent(null, ConstantValue.WILL_EXPIRE_NOTE, null, event, userInfo);
					
			if(StringUtils.isNotBlank(mobile)){//向有效的手机号码发送短信
				//发送短信后，向短信发送记录表中新增一条记录
				SmsTask smsTask = new SmsTask();
				smsTask.setBizId(eventId);
				smsTask.setBizType(ISmsTaskService.BIZ_TYPE_EVENT);
				smsTask.setPhoneNum(mobile);
				smsTask.setTaskType(ISmsTaskService.TASK_TYPE_0);
				smsTask.setTaskStatus(ISmsTaskService.TASK_STATUS_FINISH);
				
				logger.info("准备=============>发送短信: "+toBeOverdueNote+" 给 "+mobile);   
				
				//判断是否有同类型已完成的记录 不存在则发送短信,同时该任务可操作才发送短信
				if(!smsTaskService.isSmsTaskExist(smsTask) && smsTaskService.isSmsTaskAble(smsTask)){
					logger.info("开始==========>发送短信: "+toBeOverdueNote+" 给 "+mobile);   
					boolean flag = eventDisposalWorkflowService.sendSmsSyn("", mobile, toBeOverdueNote, userInfo);
					logger.info("结束==========> 短信结果："+ flag +" 发送短信: "+toBeOverdueNote+" 给 "+mobile);   
					if(flag){//短信发送成功
						smsTask.setSuccessCount(1);
					}else{//短信发送失败
						smsTask.setTaskStatus(ISmsTaskService.TASK_STATUS_UNFINISH);
						smsTask.setFailCount(1);
					}
					//新增短信发送记录
					smsTaskService.insertSmsTask(smsTask);
				}
			}
		}
	}
}
