package cn.ffcs.zhsq.event.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.common.EUDGPagination;
import cn.ffcs.common.FileUtils;
import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.shequ.base.service.IUserService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.JaxbBinder;
import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.shequ.utils.http.MyX509TrustManager;
import cn.ffcs.shequ.utils.rules.IdentityCardRule2;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.holidayinfo.IHolidayInfoService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.CoordinateInverseQuery.service.ICoordinateInverseQueryService;
import cn.ffcs.zhsq.api.enums.BizPlatfromEnum;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.intermediateData.eventVerify.service.IEventVerifyBaseService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.CoordinateInverseQuery.CoordinateInverseQueryGridInfo;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.TaskInfoData;
import cn.ffcs.zhsq.mybatis.domain.event.TaskResult;
import cn.ffcs.zhsq.mybatis.domain.event.UserInfoData;
import cn.ffcs.zhsq.mybatis.domain.event.UserInfoResult;
import cn.ffcs.zhsq.mybatis.domain.event.XmlAttr;
import cn.ffcs.zhsq.mybatis.domain.event.XmlDataResult;
import cn.ffcs.zhsq.mybatis.domain.event.XmlPeople;
import cn.ffcs.zhsq.mybatis.domain.event.XmlTaskResult;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.Base64;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.sql.TIMESTAMP;

/**
 * 事件对接接口服务提供
 */
@Service(value = "eventDisposalServImpl")
public class EventDisposalServImpl {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private IUserService userService;
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private UserManageOutService userManageService;
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private IEventDisposalDockingService eventDisposalDockingService;
	@Autowired
	private IDataExchangeStatusService dataExchangeStatusService;
	@Autowired
	private IAttachmentService attachmentService;
	@Autowired
	private ICoordinateInverseQueryService coordinateInverseQueryService;
	@Autowired
	private IEventExtendService eventExtendService;
	@Autowired
	private IEventVerifyBaseService eventVerifyBaseService;
	@Autowired
	private IHolidayInfoService holidayInfoService;
	@Autowired
	private ITimeApplicationService timeApplicationService;
	@Autowired
	private ITimeApplicationCheckService timeApplicationCheckService;
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;

	private String BIZ_PLATFORM = "007";
	private String DEFAULT_EVENT_TYPE_TRIGGER = ConstantValue.DEFAULT_EVENT_TYPE_TRIGGER+"_"+BIZ_PLATFORM;
	private String DEFAULT_RECEIVE_USER_TRIGGER = ConstantValue.DEFAULT_RECEIVE_USER_TRIGGER+"_"+BIZ_PLATFORM;
	private String DEFAULT_RECEIVE_ORG_TRIGGER = ConstantValue.DEFAULT_RECEIVE_ORG_TRIGGER+"_"+BIZ_PLATFORM;
	private String FAIL_RESULT = "-1";
	private String SUCCESS_CODE = "1";
	private String RESULT_CODE = "1";
	private String MESSAGE_INFO = "接口正在调试中...";
	private String DEFAULT_URGENCY = "01";//紧急程度，默认为01-一般
	private String DEFAULT_INFLUENCE = "01";//影响范围，默认为01-小
	private String DEfAULT_COLLECTWAY ="02";//默认为02-PC
	private String DEFAULT_EVENT_SOURCE = "01";//事件来源，目击
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public String evaluateEvent(String xmlData){
		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);
		if(checkEvaluate(eventResult)){
			String eventId = eventResult.getEvaluate().getEventId();

			DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
			dataExchangeStatus.setDestPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
			dataExchangeStatus.setSrcPlatform(eventResult.getEvaluate().getBizPlatform());
			dataExchangeStatus.setOppoSideBizCode("0");
			dataExchangeStatus.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
			dataExchangeStatus.setOwnSideBizCode(eventId);
			dataExchangeStatus.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
			dataExchangeStatus.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
			dataExchangeStatus.setStatus(IDataExchangeStatusService.STATUS_VALIDATE);
			dataExchangeStatus.setXmlData(xmlData);
			dataExchangeStatusService.saveDataExchangeStatus(dataExchangeStatus);
		}
//		JaxbBinder jaxbBinderResult = new JaxbBinder(XmlDataResult.class);
//		String xmlResult =jaxbBinderResult.toXml(eventResult, "UTF-8");
//		System.out.println(xmlResult);
		return convertToXmlResult(RESULT_CODE, MESSAGE_INFO);
	}

	public String userInfo(String xmlData){
		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);

		if(checkUserInfo(eventResult)){
//    		List<cn.ffcs.uam.bo.UserInfo> userInfos = userManageService.findUserByOrgCode(eventResult.getOrgCode());
			List<cn.ffcs.uam.bo.UserInfo> userInfo = userManageService.findAllUserByParam(eventResult.getOrgCode());
			if(userInfo.size() < 1) return convertToXmlResult("0", "组织编码不存在！");
			List<UserInfoData> userInfoDatas = new ArrayList<UserInfoData>();
			for(cn.ffcs.uam.bo.UserInfo user : userInfo){
				UserInfoData userInfoData = new UserInfoData();
				userInfoData.setUserId(user.getUserId());
				userInfoData.setUserName(user.getPartyName());
				userInfoData.setOrgId(user.getOrgId());
				userInfoData.setOrgName(user.getOrgName());
				userInfoData.setOrgCode(user.getOrgCode());
				userInfoData.setTel(user.getVerifyMobile()!=null?String.valueOf(user.getVerifyMobile()):"");
				userInfoDatas.add(userInfoData);
			}
			UserInfoResult userInfoResult = new UserInfoResult();
			userInfoResult.setUserInfoData(userInfoDatas);
			JaxbBinder jaxbBinderResult = new JaxbBinder(UserInfoResult.class);
			String xmlResult =jaxbBinderResult.toXml(userInfoResult, "UTF-8");
			return xmlResult;
		}
		return convertToXmlResult("0", "错误访问！");
	}

	public String syncTask(String xmlData){
		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);
		if(checkSyncTask(eventResult)){
			String instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(Long.valueOf(eventResult.getEventId()));
			if(StringUtils.isBlank(instanceId)) return convertToXmlResult("0", "流程错误！");;
			List<Map<String, Object>> queryTaskList = eventDisposalWorkflowService.queryProInstanceDetail(Long.valueOf(instanceId), IEventDisposalWorkflowService.SQL_ORDER_DESC);
			List<TaskInfoData> taskDatas = new ArrayList<TaskInfoData>();
			for(Map<String, Object> queryTask : queryTaskList){
				TaskInfoData taskInfoData = new TaskInfoData();
				if(queryTask.get("ORG_NAME") != null){
					taskInfoData.setOrgName(queryTask.get("ORG_NAME").toString());
					taskInfoData.setRemarks(queryTask.get("REMARKS")!=null?queryTask.get("REMARKS").toString():"");
					taskInfoData.setTaskName(queryTask.get("TASK_NAME").toString());
					taskInfoData.setTaskTime(queryTask.get("END_TIME").toString());
					taskInfoData.setTransactorName(queryTask.get("TRANSACTOR_NAME").toString());
					taskDatas.add(taskInfoData);
				}
			}
			TaskResult taskResult = new TaskResult();
			taskResult.setTaskInfoData(taskDatas);

			JaxbBinder jaxbBinderResult = new JaxbBinder(TaskResult.class);
			String xmlResult =jaxbBinderResult.toXml(taskResult, "UTF-8");
			return xmlResult;
		}
		return convertToXmlResult("0", "错误访问！");
	}

	
	/**
	 * 根据经纬度获取网格编码
	 * @param x
	 * @param y
	 * @param level
	 * @return
	 */
	private String getGridCode(String x, String y,String level){
		Map<String, Object> gridParams = new HashMap<>();
		gridParams.put("x", x);
		gridParams.put("y", y);
		if (StringUtils.isNotBlank(level)) {
			gridParams.put("gridLevel", level);
		}
		List<CoordinateInverseQueryGridInfo> queryGridInfos = coordinateInverseQueryService
				.findGridInfo(gridParams);
		if (null != queryGridInfos && queryGridInfos.size() > 0) {
			return queryGridInfos.get(0).getInfoOrgCode();
		} 
		return "";
	}
	
	/**
	 * 申请延期接口
	 * @param xmlData
	 * @return
	 */
	public String saveDelayEvt(String xmlData){
		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);

		//验证有效性
		String checkResult = saveDelayCheckEvtData(eventResult);
		if (StringUtils.isNotBlank(checkResult)) {
			return convertToXmlResult("0", checkResult);
		}
		
		String eventId = eventResult.getEvent().getEventId();
		String userName = eventResult.getEvent().getCreatorName();
		String orgCode = eventResult.getEvent().getOrgCode();
		String closeOrgID = eventResult.getEvent().getCloseOrgID();
		String status =  eventResult.getEvent().getStatus();
		
		EventDisposal eventDisposal = eventDisposalService.findEventByIdSimple(Long.valueOf(eventId));
		if (null != eventDisposal) {
			if ("04".equals(eventDisposal.getStatus())) {
				return convertToXmlResult("0", "【" + eventId + "】事件已归档！");
			}
			if ("06".equals(eventDisposal.getStatus())) {
				return convertToXmlResult("0", "【" + eventId + "】事件已被删除！");
			}
			if ("99".equals(eventDisposal.getStatus())) {
				return convertToXmlResult("0", "【" + eventId + "】事件未启动流程！");
			}
		} else {
			return convertToXmlResult("0", "eventId：" + eventId + "未找到！");
		}
		
			UserInfo userInfo = new UserInfo();
			// 根据指定账号发起延期申请 （userName orgCode两个字段要么都有值，要么就都为空）
			if (StringUtils.isNotBlank(userName)) {
				UserBO userBO = new UserBO();
				// 查找用户信息
				try {
					 userBO = userManageService.getUserInfoByUserName(userName);
				} catch (Exception e) {
					e.printStackTrace();
					return convertToXmlResult("0", "根据用户名查找用户异常！");
				}
				if (null != userBO && userBO.getUserId() != null ) {
					userInfo.setUserId(userBO.getUserId());
				}else {
					return convertToXmlResult("0", userName+"未找到用户！");
				}
				OrgSocialInfoBO selectOrgSocialInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode(orgCode);
				if (null != selectOrgSocialInfo) {
					userInfo.setOrgCode(orgCode);
					userInfo.setOrgId(selectOrgSocialInfo.getOrgId());
					
				}else {
					return convertToXmlResult("0", orgCode+"未找到对应组织！");
				}
				
			// 使用事件当前办理人
			} else {
				Long instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(Long.valueOf(eventId));
				// 获取事件当前办理人信息
				Map<String, Object> par = eventDisposalWorkflowService.curNodeData(Long.valueOf(instanceId));
				
				//当前办理人是用户
				if (CommonFunctions.isNotBlank(par, "WF_USERID")) {
					if (CommonFunctions.isNotBlank(par, "WF_ORGID")) {
						String userOrgId = par.get("WF_ORGID").toString().split(",")[0];
						String userCurId = par.get("WF_USERID").toString().split(",")[0];
						userInfo.setOrgId(Long.valueOf(userOrgId));
						userInfo.setUserId(Long.valueOf(userCurId));
						OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoService.findByOrgId(Long.valueOf(userOrgId));
						if (null != orgSocialInfoBO) {
							userInfo.setOrgCode(orgSocialInfoBO.getOrgCode());
						}
					} else {
						return convertToXmlResult("0", "获取当前办理人信息失败！");
					}
				//当前办理人是组织	
				}else {
					if (CommonFunctions.isNotBlank(par, "WF_ORGID")) {
						//需根据组织Id 取组织下的一个人
						String userOrgId = par.get("WF_ORGID").toString().split(",")[0];
						OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoService.findByOrgId(Long.valueOf(userOrgId));
						if (null != orgSocialInfoBO) {
							userInfo.setOrgId(Long.valueOf(userOrgId));
							userInfo.setOrgCode(orgSocialInfoBO.getOrgCode());
							
							List<UserBO> userListByUser = userManageService.getUserListByUserExampleParamsOut(
									null, null, Long.valueOf(userOrgId));
							
							if(userListByUser!=null && userListByUser.size()>0){
								if (null != userListByUser.get(0).getUserId()) {
									userInfo.setUserId(userListByUser.get(0).getUserId());
								}
					    	}else {
								 return convertToXmlResult("0", "组织编码【"+orgCode+"】用户id为空！");
							}
						}
					}else {
						return convertToXmlResult("0", "获取当前办理人信息失败！");
					}
				}
			}
			
		try {

			Map<String, Object> timeApplication = new HashMap<String, Object>();

			Map<String, Object> initTimeMap = timeApplicationService.initTimeApplication4Event(Long.valueOf(eventId),
					ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue(), userInfo, timeApplication);
			
			//设置延期的审核人员所属组织（该组织下的所有人均可审核），未设置则优先使用接口默认组织
			if (StringUtils.isNotBlank(closeOrgID)) {
				initTimeMap.put("auditorOrgId", closeOrgID);
			}
			timeApplication.putAll(initTimeMap);
			// 待办延时申请
			timeApplication.put("applicationType", ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue());
			timeApplication.put("businessId", eventId);
			timeApplication.put("businessKeyId", eventId);
			timeApplication.put("interval", eventResult.getEvent().getInterval());// 申请延期天数
			// 延期天数单位1：工作日2：自然日， 默认1
			if (StringUtils.isNotBlank(eventResult.getEvent().getIntervalUnit())) {
				timeApplication.put("intervalUnit", eventResult.getEvent().getIntervalUnit());
			}else {
				timeApplication.put("intervalUnit", "1");// 延期天数单位1：工作日2：自然日， 默认1
			}
			timeApplication.put("reason", eventResult.getEvent().getReason());// 延期理由
			if (StringUtils.isNotBlank(status)) {//延期申请审核状态1:通过2：不通过，直接发起并审核
				timeApplication.put("timeAppCheckStatus", status);
			}
			if (StringUtils.isNotBlank(eventResult.getEvent().getAdvice())) {//审核意见
				timeApplication.put("checkAdvice", eventResult.getEvent().getAdvice());
			}
			if (StringUtils.isNotBlank(eventResult.getEvent().getCreateTimeStr())) {
				timeApplication.put("createTimeStr", eventResult.getEvent().getCreateTimeStr());
			}
			if (StringUtils.isNotBlank(eventResult.getEvent().getTimeAppCheckCreateTimeStr())) {
				timeApplication.put("timeAppCheckCreateTimeStr", eventResult.getEvent().getTimeAppCheckCreateTimeStr());
			}
			if (StringUtils.isNotBlank(eventResult.getEvent().getTimeAppCheckUpdateTimeStr())) {
				timeApplication.put("timeAppCheckUpdateTimeStr", eventResult.getEvent().getTimeAppCheckUpdateTimeStr());
			}

			if (timeApplicationService.saveTimeApplication(timeApplication) < 0) {
				return convertToXmlResult("0", "发起延期申请失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return convertToXmlResult("0", e.getMessage());
		}
		
		return convertToXmlResult("1", eventId);
	}
	
	/**
	 * 申请延期接口数据校验
	 * @param eventResult
	 * @return
	 */
	public String saveDelayCheckEvtData(XmlDataResult eventResult){
		if(null == eventResult.getAuth())
			return convertToXmlResult("0","认证信息[auth]无效");
		if(StringUtils.isBlank(eventResult.getAuth().getUsername()))
			return convertToXmlResult("0","用户名[username]无效");
		if(StringUtils.isBlank(eventResult.getAuth().getPassword()))
			return convertToXmlResult("0","密码[password]无效");
		if(!checkUserPwd(eventResult.getAuth().getUsername(), eventResult.getAuth().getPassword()))
			return convertToXmlResult("0","用户[username][password]验证失败");
		if(null == eventResult.getEvent())
			return convertToXmlResult("0","事件信息[event]无效");
		if(StringUtils.isBlank(eventResult.getEvent().getEventId()))
			return convertToXmlResult("0", "事件ID[eventId]无效");
		if(StringUtils.isBlank(eventResult.getEvent().getReason()))
			return convertToXmlResult("0","延期理由[reason]无效");
		
		if (StringUtils.isNotBlank(eventResult.getEvent().getOrgCode())) {
			if(StringUtils.isBlank(eventResult.getEvent().getCreatorName()))
				return convertToXmlResult("0","使用平台指定账号发起延期失败！发起人用户名[creatorName]无效");
		}
		if (StringUtils.isNotBlank(eventResult.getEvent().getCreatorName())) {
			if(StringUtils.isBlank(eventResult.getEvent().getOrgCode()))
			return convertToXmlResult("0","使用平台指定账号发起延期失败！组织编码[orgCode]无效");
		}
		
		if(StringUtils.isBlank(eventResult.getEvent().getInterval()))
			return convertToXmlResult("0","申请延期天数[interval]无效");
		if (!StringUtils.isNumeric(eventResult.getEvent().getInterval())) {
			return convertToXmlResult("0","申请延期天数[interval]不是整型");
		}
		if(StringUtils.isBlank(eventResult.getEvent().getBizPlatform()))
			return convertToXmlResult("0", "业务平台[bizPlatform]无效");
		if (StringUtils.isNotBlank(eventResult.getEvent().getCloseOrgID())) {
			OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoService.findByOrgId(
					Long.valueOf(eventResult.getEvent().getCloseOrgID()));
			if (StringUtils.isBlank(orgSocialInfoBO.getOrgCode())) {
				return convertToXmlResult("0",eventResult.getEvent().getCloseOrgID()+"无效组织Id！");
			}
		}
		if(StringUtils.isNotBlank(eventResult.getEvent().getStatus())) {
			if(!(eventResult.getEvent().getStatus().equals("1") || eventResult.getEvent().getStatus().equals("2")))
				return convertToXmlResult("0", "审核状态识别失败！ 1：通过 2：不通过");
		}
		return "";
	}
	
	/**
	 * 申请延期审核接口
	 * @param xmlData
	 * @return
	 */
	public String delayCheckEvt(String xmlData){
		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);

		//验证有效性
		String checkResult = delayCheckEvtData(eventResult);
		if(StringUtils.isNotBlank(checkResult)){
			return convertToXmlResult("0", checkResult);
		}
		String orgCode = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN,
				"EVENT_DELAY_ORGCODE",IFunConfigurationService.CFG_TYPE_FACT_VAL);
		
		String delayBizPlatform = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN,
				"EVENT_DELAY_BIZPLATFORM",IFunConfigurationService.CFG_TYPE_FACT_VAL);
		
		String eventId = eventResult.getEvent().getEventId();
		String advice = eventResult.getEvent().getAdvice();
		String state = eventResult.getEvent().getStatus();//1：通过 2：不通过
		
		Map<String, Object> checkMap = new HashMap<String, Object>();
		checkMap.put("applicationType", ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue());//时限申请类型
		checkMap.put("businessId", eventId);
		checkMap.put("businessKeyId", eventId);
		checkMap.put("timeAppCheckStatus", ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue());//审核状态
		checkMap.put("isTransferUser", true);//是否转换人员姓名，true为转换；默认为false   
		checkMap.put("isTransferOrg", true);//是否转换组织名称，true为转换；默认为false
		checkMap.put("userOrgCode", orgCode);
		//根据事件id获取延期信息（接口目前只会返回一条数据）
		List<Map<String, Object>> applicationList = timeApplicationService.findTimeApplicationList(checkMap);
		String checkId = "";
		String applicationId = "";
		String updaterId = "";
		String updaterName = "";
		String updaterOrgId = "";
		String updaterOrgName = "";
		String intervalName = "";
		
		if (null != applicationList && applicationList.size()>0) {
			
			checkId = applicationList.get(0).get("checkId").toString();
			applicationId = applicationList.get(0).get("applicationId").toString();
			
			if (CommonFunctions.isNotBlank(applicationList.get(0), "auditorId")) {
				updaterId = applicationList.get(0).get("auditorId").toString();
				updaterName = applicationList.get(0).get("auditorName").toString();
				updaterOrgId = applicationList.get(0).get("auditorOrgId").toString();
				updaterOrgName = applicationList.get(0).get("auditorOrgName").toString();
			}
			intervalName = applicationList.get(0).get("intervalName").toString();//4(工作日)
			
			checkMap = new HashMap<String, Object>();
			checkMap.put("checkId", checkId);// 审核id
			checkMap.put("updaterId", updaterId);// 审核人
			checkMap.put("updaterName", updaterName);// 审核人名称
			checkMap.put("updaterOrgId", updaterOrgId);// 审核人所在组织
			checkMap.put("updaterOrgName", updaterOrgName);// 审核人所在组织名称
			checkMap.put("checkAdvice", advice);// 审核意见
			checkMap.put("applicationId", applicationId);// 申请id
			checkMap.put("timeAppCheckStatus", state);// 审核状态
			checkMap.put("userOrgCode", orgCode);
			 
			 try {
				//更新延期审核：通过或不通过
				if (!timeApplicationCheckService.updateTimeAppCheckById(checkMap)) {
					return convertToXmlResult("0", "审核事件延期失败！");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else {
			return convertToXmlResult("0", "根据事件Id获取延期信息为空！");
		}
		
		//将延期请求成功的记录删除，若再次申请可再次请求延期
		DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
		dataExchangeStatus.setOwnSideBizCode(eventId);
		dataExchangeStatus.setOppoSideBizType("8");
		dataExchangeStatus.setSrcPlatform("000");
		dataExchangeStatus.setDestPlatform(delayBizPlatform);
		dataExchangeStatus.setStatus("1");
		List<DataExchangeStatus> dataExchange = dataExchangeStatusService.findDataExchange(dataExchangeStatus);
		if (null != dataExchange && dataExchange.size()>0) {
			dataExchangeStatusService.deleteDataExchangeStatusById(dataExchange.get(0).getInterId());
		}
		return convertToXmlResult("1", eventId);
	}

	/**
	 * 申请延期审核接口数据校验
	 * @param eventResult
	 * @return
	 */
	public String delayCheckEvtData(XmlDataResult eventResult){
		if(null == eventResult.getAuth())
			return convertToXmlResult("0","认证信息[auth]无效");
		if(StringUtils.isBlank(eventResult.getAuth().getUsername()))
			return convertToXmlResult("0","用户名[username]无效");
		if(StringUtils.isBlank(eventResult.getAuth().getPassword()))
			return convertToXmlResult("0","密码[password]无效");
		if(!checkUserPwd(eventResult.getAuth().getUsername(), eventResult.getAuth().getPassword()))
			return convertToXmlResult("0","用户[username][password]验证失败");
		if(null == eventResult.getEvent())
			return convertToXmlResult("0","事件信息[event]无效");
		if(StringUtils.isBlank(eventResult.getEvent().getAdvice()))
			return convertToXmlResult("0","审核意见[advice]无效");
		if(StringUtils.isBlank(eventResult.getEvent().getEventId()))
			return convertToXmlResult("0", "事件ID[eventId]无效");
		if(StringUtils.isBlank(eventResult.getEvent().getStatus()))
			return convertToXmlResult("0", "审核状态[status]无效");
		if(!(eventResult.getEvent().getStatus().equals("1") || eventResult.getEvent().getStatus().equals("2")))
			return convertToXmlResult("0", "审核状态识别失败！ 1：通过 2：不通过");
		if(StringUtils.isBlank(eventResult.getEvent().getBizPlatform()))
			return convertToXmlResult("0", "业务平台[bizPlatform]无效");
	
		return "";
	}
	
	/**
	 * 评价
	 * @param xmlData
	 * @return
	 */
	public String evlEvt(String xmlData){
		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);
		//验证有效性
		String checkResult = checkRejectEvtData(eventResult);
		if(StringUtils.isNotBlank(checkResult)){
			return checkResult;
		}
	
		String eventId = eventResult.getEvent().getEventId();
		String advice = eventResult.getEvent().getAdvice();
		
		Map<String, Object> eventMap = new HashMap<String, Object>();
      	eventMap.put("evaLevel","02");
    	eventMap.put("evaContent","满意");
		eventMap.put("eventId",eventId);
		eventMap.put("advice",advice);
		eventMap.put("isEvaluate", true);
		eventDisposalDockingService.subWorkflow(eventMap);

		return convertToXmlResult("1", "");
	}

	/**
	 * 驳回
	 * @param xmlData
	 * @return
	 */
	public String rejectEvt(String xmlData){
		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);

		//验证有效性
		String checkResult = checkRejectEvtData(eventResult);
		if(StringUtils.isNotBlank(checkResult)){
			return convertToXmlResult("0", checkResult);
		}

		//驳回处理
		String advice = eventResult.getEvent().getAdvice();
		String eventId = eventResult.getEvent().getEventId();
		String bizPlatform = eventResult.getEvent().getBizPlatform();

		Long instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(Long.valueOf(eventId));
		Long taskId = eventDisposalWorkflowService.curNodeTaskId(instanceId);

		//保存附件
		this.saveAttrs(eventResult.getEvent().getAttrs(), Long.valueOf(eventId), ConstantValue.EVENT_SEQ_2);

		boolean result = false;
		try {
			result = eventDisposalWorkflowService.rejectWorkFlow(String.valueOf(taskId), String.valueOf(instanceId), advice);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(!result){
			return convertToXmlResult("0", "操作失败！");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bizPlatform", bizPlatform);
		params.put("own", String.valueOf(eventId));
		params.put("xmlData", xmlData);
		//保存事件对应关系
		this.saveEventDataExchangeStatus(params);
		return convertToXmlResult("1", eventId);
	}

	
	/**
	 * 事件流程查询接口
	 * @param xmlData
	 * @return
	 */
	public String eventFlow(String xmlData){
		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);

		//验证有效性
		String checkResult = checkEvtData(eventResult);
		if(StringUtils.isNotBlank(checkResult)){
			return convertToXmlResult("0", checkResult);
		}
		String eventId  = eventResult.getEvent().getEventId();
		EventDisposal eventDisposal = eventDisposalService.findEventByIdSimple(Long.valueOf(eventId));
		
		if (null != eventDisposal) {
			if ("06".equals(eventDisposal.getStatus())) {
				return convertToXmlResult("0", "【" + eventId + "】事件已被删除！");
			}
			if ("99".equals(eventDisposal.getStatus())) {
				return convertToXmlResult("0", "【" + eventId + "】事件未启动流程！");
			}
		} else {
			return convertToXmlResult("0", "eventId：" + eventId + "未找到！");
		}

		// 根据事件Id查找实例Id获取历史环节
		Long instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(Long.valueOf(eventId));
		if (instanceId <= 0) {
			return convertToXmlResult("0", "eventId：" + eventId + "，未找到有效事件信息！");
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setOrgCode("");
		JSONObject data = null;
		JSONArray dataList = null;

		String remark = "";
		List<Map<String, Object>> hisFlow = eventDisposalWorkflowService.queryProInstanceDetail(instanceId,
				IEventDisposalWorkflowService.SQL_ORDER_ASC, userInfo);

		if (null != hisFlow && hisFlow.size() > 0) {
			
			dataList = new JSONArray();
			String orgId="";
			String transactorId="";
			OrgSocialInfoBO orgSocialInfoBO = null;
			UserBO userBO = null;
			
			for (Map<String, Object> flow : hisFlow) {
				
				data = new JSONObject();
				
				//与上一流程比较ORG_ID、TRANSACTOR_ID，若不同才调用接口获取信息（赣州90%事件为网格员自办自结，流程中的组织和人员相同）
				if(!orgId.equals(flow.get("ORG_ID").toString())) {
					orgId = flow.get("ORG_ID").toString();
					// 查询流程办理人所属组织
					orgSocialInfoBO = orgSocialInfoService.findByOrgId(Long.valueOf(orgId));
				}
				if(!transactorId.equals(flow.get("TRANSACTOR_ID").toString())) {
					transactorId = flow.get("TRANSACTOR_ID").toString();
					// 查询流程办理人
					userBO = userManageService.getUserInfoByUserId(Long.valueOf(transactorId));
				}
				
				remark = (String) flow.get("REMARKS");// 办理意见（为空是 null字符串）
				if (StringUtils.isBlank(remark)) {
					remark = "";
				}
				data.put("orgCode", orgSocialInfoBO.getOrgCode());
				data.put("registValue", userBO.getRegisValue());
				data.put("advice", remark);
				data.put("userName", (String) flow.get("TRANSACTOR_NAME"));//处理人名称
				data.put("orgName", (String) flow.get("ORG_NAME"));// 办理组织名称
				data.put("dealTime", (String) flow.get("START_TIME"));// 处理开始时间
				data.put("finishTime", (String) flow.get("END_TIME"));// 处理结束时间
				data.put("taskName", flow.get("TASK_NAME").toString());
				dataList.add(data);
			}

			if (null != dataList && dataList.size() > 0) {
				return convertToXmlResult("1", dataList.toString());
			}

		} else {
			return convertToXmlResult("0", "eventId：" + eventId + "，未找到相关流程信息！");
		}
		return convertToXmlResult("0", eventId);
	}
	

	/**
	 * 结案
	 * @param xmlData
	 * @return
	 */
	public String closeEvt(String xmlData) {
		        //gt lt转换
				xmlData = StringEscapeUtils.unescapeXml(xmlData);
				xmlData = xmlData.replace("&","&amp;");
				JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
				XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);
				//参数有效性判断
				String checkCloseEvtData = checkCloseEvtData(eventResult);
				if(StringUtils.isNotBlank(checkCloseEvtData)){
					return convertToXmlResult("0", checkCloseEvtData);
				}
				
				String advice = eventResult.getEvent().getAdvice();
				String bizPlatform = eventResult.getEvent().getBizPlatform();
				String eventId  = eventResult.getEvent().getEventId();
				
				//事件状态判断
				EventDisposal eventDisposal = eventDisposalService.findEventByIdSimple(Long.valueOf(eventId));
			    if(null != eventDisposal ){
			    	if (ConstantValue.EVENT_STATUS_END.equals(eventDisposal.getStatus())) {
			    		return convertToXmlResult("0", "【"+eventId+"】事件已结案归档！");//04
			    	}
			    	if (ConstantValue.STATUS_DEL_AND_RETURN_BACK.equals(eventDisposal.getStatus())) {
			    		return convertToXmlResult("0", "【"+eventId+"】事件已被删除！");//06
			    	}
			    	if (ConstantValue.EVENT_STATUS_DRAFT.equals(eventDisposal.getStatus())) {
			    		return convertToXmlResult("0", "【"+eventId+"】事件未启动流程！");//99
			    	}
				}else{
					return convertToXmlResult("0", "【"+eventId+"】事件未找到！");
				}
			    
				String instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(Long.valueOf(eventId));
				if(StringUtils.isBlank(instanceId)){
					return convertToXmlResult("0", "【"+eventId+"】事件未找到！");
				}
				
				//当前办理人
				Long nowOrgId = -1L;
				Long nowUserId = -1L;
				String orgCode = "";
				try{
					Map<String, Object> stringObjectMap = eventDisposalWorkflowService.curNodeData(Long.valueOf(instanceId));
					if(stringObjectMap != null){
						if(stringObjectMap.get("WF_ORGID")!=null){
							String[] ss = new String[100];
							ss = stringObjectMap.get("WF_ORGID").toString().split(",");
							nowOrgId = Long.valueOf(ss[0]);
							OrgSocialInfoBO bo	= orgSocialInfoService.findByOrgId(nowOrgId);
							  if (null != bo) {
								orgCode = bo.getOrgCode();
							  }else {
								  return convertToXmlResult("0", "获取当前环节错误！");
							  }
						}
						if(stringObjectMap.get("WF_USERID")!=null){
							String[] ss = new String[100];
							ss = stringObjectMap.get("WF_USERID").toString().split(",");
							nowUserId = Long.valueOf(ss[0]);
						}
					}
				} catch (Exception e1) {					
					e1.printStackTrace();
				}
				
				//设置当前办理人
				UserInfo userInfo = new UserInfo();
				userInfo.setOrgId(nowOrgId);
				userInfo.setUserId(nowUserId);
				userInfo.setOrgCode(orgCode);
				
				Map<String, Object> initResultMap = eventDisposalWorkflowService.initFlowInfo("", Long.valueOf(instanceId), userInfo, null);
				String taskId = "";
				if (CommonFunctions.isNotBlank(initResultMap, "taskId")) {
					taskId = initResultMap.get("taskId").toString();
				}else {
					return convertToXmlResult("0", "未找到事件ID【"+eventId+"】实例ID【"+instanceId+"】对应的当前任务！") ;
				}

				List<Node> taskNodes = (List<Node>) initResultMap.get("taskNodes");
			
				String nodeName = "";
				if(taskNodes!=null && taskNodes.size()>0) {//下一环节中，有结案环节时，才能结案
					boolean isCloseAble = false;
					for(Node taskNode : taskNodes){
						if(taskNode!=null){
							if("结案".equals(taskNode.getNodeNameZH())
									||"核查评价".equals(taskNode.getNodeNameZH())){
								nodeName = taskNode.getNodeName();
								isCloseAble = true;
								break;
							}
						}
					}
					if(isCloseAble){//有结案环节
						
						if(nowOrgId.equals(-1L) || nowUserId.equals(-1L)){
							return convertToXmlResult("0", "当前环节错误！");
						}
						//设置当前办理人员
						userInfo.setPartyName(eventResult.getEvent().getCloserName());
						ProInstance proInstance = (ProInstance) initResultMap.get("proInstance");
						Long nextUserId = proInstance.getUserId(),
								 nextOrgId = proInstance.getOrgId();
						logger.info(userInfo.getUserId() + "+-+" + userInfo.getOrgId());
						logger.info("--instanceId="+Long.valueOf(instanceId)+"taskId="+taskId+
								"nodeName-"+nodeName+" nextUserId="+nextUserId+"-nextOrgId="+nextOrgId);
						Map<String, Object> params= new HashMap<String, Object>();
						params.put("eventId",eventId);
						params.put("formId",eventId);
						params.put("instanceId", Long.valueOf(instanceId));
			
						boolean result = false;
						try {
							result = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(Long.valueOf(instanceId),taskId, nodeName, "", "", advice, userInfo, "", params);	
						} catch (Exception e1) {					
							e1.printStackTrace();
						}

						String attrsStatus = ConstantValue.EVENT_SEQ_3;
						if(result){
							if(StringUtils.isNotBlank(eventId)){//将办理意见更新至事件result
								EventDisposal eventTmp = new EventDisposal();
								eventTmp.setEventId(Long.valueOf(eventId));
								eventTmp.setResult(advice);
								eventDisposalService.updateEventDisposalById(eventTmp);
								//江阴街镇12345对接，无论街镇12345是驳回还是结案，图片均为处理中
								if (("038").equals(eventResult.getEvent().getBizPlatform())) {
									attrsStatus = ConstantValue.EVENT_SEQ_2;
								}
								this.saveAttrs(eventResult.getEvent().getAttrs(), Long.parseLong(eventId),attrsStatus);
							}
						}else{
							return convertToXmlResult("0", "事件办理错误！");
						}
					}else{
						return convertToXmlResult("0", "没有结案环节！");
					}
				}
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("bizPlatform", bizPlatform);
				params.put("own", String.valueOf(eventId));
				params.put("xmlData", xmlData);
				params.put("ownSideBizStatus", "1");
				//保存事件对应关系
				this.saveEventDataExchangeStatus(params);
				
				//完整度计算
				Map<String, Object> paramss = new HashMap<String, Object>();
				paramss.put("eventId", eventResult.getEvent().getEventId());
				Long score=dataExchangeStatusService.countScore(paramss);	
				//更新完整度
				Map<String, Object> eventExtend = new HashMap<>();
				eventExtend.put("eventId",eventResult.getEvent().getEventId());
				eventExtend.put("integrityScore", score);
				if(null!=score){
					try {
						eventExtendService.saveEventExtend(eventExtend);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return convertToXmlResult("1", eventId);
	}

	/**
	 * 启动结案
	 * @param xmlData
	 * @return
	 */
	public String startCloseEvt(String xmlData){
		return saveEvt(xmlData, ConstantValue.WORKFLOW_IS_TO_COLSE);
	}

	/**
	 * 启动
	 * @param xmlData
	 * @return
	 */
	public String startEvt(String xmlData){
		return saveEvt(xmlData, ConstantValue.WORKFLOW_IS_NO_CLOSE);
	}

	

	private String saveEvt(String xmlData, String workflowIsNoClose){
		xmlData = xmlData.replace("&","&amp;");
		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);

		String checkResult = checkStartEvtData(eventResult);
		if(StringUtils.isNotBlank(checkResult)){
			return checkResult;
		}


		UserInfo userInfo = new UserInfo();

		EventDisposal eventDisposal = new EventDisposal();
		String eventGridCode = "";
		String gridCode = eventResult.getEvent().getGridCode();
		String bizPlatform = eventResult.getEvent().getBizPlatform();
		String targetOrgCode = eventResult.getEvent().getTargetOrgCode();
		String orgCode = eventResult.getEvent().getOrgCode();
		String advice = eventResult.getEvent().getAdvice();
		String chiefLevel = "";
		String orgType = "";
        String gridName = "";

        //唯一判断
      	String oppoSideBusiCode = eventResult.getEvent().getOppoSideBusiCode();
      	if(StringUtils.isNotBlank(oppoSideBusiCode)){
      			Map<String, Object> dataExParams = new HashMap<String, Object>();
      			dataExParams.put("oppoSideBizCode", oppoSideBusiCode);
      			dataExParams.put("srcPlatform",eventResult.getEvent().getBizPlatform());
      			dataExParams.put("oppoSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
      			EUDGPagination exchangePagination = dataExchangeStatusService.findDataExchangePagination(1, 1, dataExParams);
      			if(exchangePagination.getTotal() > 0){
      				return convertToXmlResult("0", "第三方事件ID[oppoSideBusiCode]["+oppoSideBusiCode+"]已存在！");
      			}
      	}
		if(StringUtils.isNotBlank(gridCode)){
			if(bizPlatform.equals("030")){//领悟综合执法
				if("320281115".equals(orgCode)){
					gridCode = "320281401";//夏港街道
				}else if("320281116".equals(orgCode)){
					gridCode = "320281403";//利港街道
				}
			}
			if(bizPlatform.equals("020") || bizPlatform.equals("029") || bizPlatform.equals("027")){//020思明、翔安 029集美,027海沧
				String cfg_gridCode = funConfigurationService.changeCodeToValue("MEIYA_EVENT_GRID_CODE", gridCode, IFunConfigurationService.CFG_TYPE_FACT_VAL);
				if(StringUtils.isNotBlank(cfg_gridCode)){
					eventGridCode = cfg_gridCode;
				}
			}else{
				MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(gridCode);
				if(gridInfo != null) {
					eventGridCode = gridCode;
				   gridName = gridInfo.getGridName();
				}else {
					return convertToXmlResult("0", "地域编码【"+gridCode+"】不存在！");
			   }
			}
		}

		if(StringUtils.isBlank(orgCode)){
			orgCode = eventGridCode;
		}
		if(StringUtils.isNotBlank(orgCode)){
			if(bizPlatform.equals("030")){//领悟综合执法
				if("3202810cg".equals(orgCode)){
					orgCode = "320281001A03";//城管局
				}

				if(orgCode.equals("320281116")){//利港
					orgCode = "320281116A01";
				}else if(orgCode.equals("320281115")){//夏港
					orgCode = "320281115A01";
				}else{
					OrgEntityInfoBO orgEntityInfoBO = this.orgEntityInfoOutService.selectOrgEntityInfoByOrgCode(orgCode);
					if(orgEntityInfoBO!=null){
						List<OrgSocialInfoBO> orgSocialInfoBOs = this.orgSocialInfoService.findByLocationId(orgEntityInfoBO.getOrgId());
						for(OrgSocialInfoBO orgSocialInfo : orgSocialInfoBOs){
							if(orgSocialInfo.getOrgType().equals("0")){//部门
								orgCode = orgSocialInfo.getOrgCode();break;
							}
						}
					}
				}

			}
			
			OrgSocialInfoBO socialInfoBO = this.orgSocialInfoService.selectOrgSocialInfoByOrgCode(orgCode);
			if(socialInfoBO != null){
				chiefLevel = socialInfoBO.getChiefLevel();
				orgType = socialInfoBO.getOrgType();
				userInfo.setOrgId(socialInfoBO.getOrgId());
				userInfo.setOrgCode(orgCode);
				List<cn.ffcs.uam.bo.UserInfo> userByOrgCode = this.userManageService.findUserByOrgCode(orgCode);
				if(userByOrgCode!=null && userByOrgCode.size()>0){
					//晋江对接个性化：微信事件上报，设置事件采集人为对方传参
					if (bizPlatform.equals(BizPlatfromEnum.JJ_WECHAT.getValue())) {
						eventDisposal.setCreatorId(Long.valueOf(eventResult.getEvent().getCreatorID()));
						userInfo.setUserId(Long.valueOf(eventResult.getEvent().getCreatorID()));
						userInfo.setPartyName(eventResult.getEvent().getContactUserName());
					}else {
						eventDisposal.setCreatorId(userByOrgCode.get(0).getUserId());
						userInfo.setUserId(userByOrgCode.get(0).getUserId());
						userInfo.setPartyName(userByOrgCode.get(0).getPartyName());
					}
					userInfo.setOrgName(userByOrgCode.get(0).getOrgName());
				}else{
					return convertToXmlResult("0", "组织编码【"+orgCode+"】用户未配置！");
				}
			}else{
				return convertToXmlResult("0", "组织编码【"+orgCode+"】不存在！");
			}
		}
		
		eventDisposal.setGridCode(eventGridCode);
		eventDisposal.setEventName(eventResult.getEvent().getEventName());
		String eventType = eventResult.getEvent().getEventType();
		//万宁12345对接事件类型映射
		if ("035".equals(bizPlatform)) {
			Map<String, Object> dictData = new HashMap<String, Object>();
			   List<BaseDataDict> eventTypeList = baseDictionaryService.getDataDictListOfSinglestage(
					   "B915007", null);
		     	// dict.getDictName() 对方字典 ,dict.getDictGeneralCode()我方字典
		     	for (BaseDataDict dict : eventTypeList) {
		     			dictData.put(dict.getDictName(),dict.getDictGeneralCode());
		     	}
			if (CommonFunctions.isNotBlank(dictData, eventType)) {
				eventType = dictData.get(eventType).toString();
			}
		}
		eventDisposal.setType(eventType);
		eventDisposal.setHappenTimeStr(eventResult.getEvent().getHappenTimeStr());
		eventDisposal.setOccurred(eventResult.getEvent().getOccurred());
		eventDisposal.setContent(eventResult.getEvent().getContent());
		eventDisposal.setHandleDateStr(eventResult.getEvent().getHandleDate());
		eventDisposal.setBizPlatform(bizPlatform);
		if ( null != eventResult.getEvent().getPeoples() && //涉及人员(如：001，wxq，身份证；001，lr，身份证)
				eventResult.getEvent().getPeoples().size()>0) {
			String peopleList = "";
			for(XmlPeople people:eventResult.getEvent().getPeoples()) {
				peopleList += people.getCardType()+"，"+people.getName()+"，"+people.getIdCard()+"；";
			}
			if (StringUtils.isNotBlank(peopleList)) {
				peopleList = peopleList.substring(0,peopleList.length()-1);
				eventDisposal.setEventInvolvedPeople(peopleList);
			}
		}
		//紧急程度
        String urgency = eventResult.getEvent().getUrgency();
        if(StringUtils.isBlank(urgency)){//如果为空，默认为01-一般
            urgency = DEFAULT_URGENCY;
        }
        eventDisposal.setUrgencyDegree(urgency);
        //影响范围
        String influence = eventResult.getEvent().getInfluence();
        if(StringUtils.isBlank(influence)){
            influence = DEFAULT_INFLUENCE;
        }
        eventDisposal.setInfluenceDegree(influence);
        //采集渠道
        String collectWay = eventResult.getEvent().getCollectWay();
        if(StringUtils.isBlank(collectWay)){//默认值为02-PC
            collectWay = DEfAULT_COLLECTWAY;
        }
        eventDisposal.setCollectWay(collectWay);
        //事件来源
        String source = eventResult.getEvent().getSource();
        if(StringUtils.isBlank(source)){//默认为01-目击
            source = DEFAULT_EVENT_SOURCE;
        }
        
        //设置额外参数
        //巡防类型
        Map<String,Object> paramsSave=new HashMap<String,Object>();
        if(eventResult.getEvent().getBizPlatform().equals("3601002")) {//海康平台对接
        	paramsSave.put("patrolType", "0");
        }
        
        eventDisposal.setSource(source);
        eventDisposal.setInvolvedNum("00");//涉及人员-无
		eventDisposal.setContactUser(eventResult.getEvent().getContactUserName());
		eventDisposal.setTel(eventResult.getEvent().getContactTel());
		eventDisposal.setCreateTimeStr(eventResult.getEvent().getRegisterTimeStr());
		eventDisposal.setStatus(ConstantValue.EVENT_STATUS_DRAFT);//默认事件状态为草稿

		eventDisposal.getResMarker().setMapType("5");//默认二维地图类型

		eventDisposal.getResMarker().setY(eventResult.getEvent().getLatitude());
		eventDisposal.getResMarker().setX(eventResult.getEvent().getLongitude());
		if(StringUtils.isBlank(eventDisposal.getEventName())){
			if (!checkBizPlatformByType(bizPlatform, "1")) {
				eventDisposal.setEventName(eventDisposal.getOccurred()+"事件");
			}
		}
		if(StringUtils.isBlank(eventDisposal.getCreateTimeStr())){
			if(StringUtils.isNotBlank(eventDisposal.getHappenTimeStr())){
				eventDisposal.setCreateTimeStr(eventDisposal.getHappenTimeStr());
			}
		}
		if(workflowIsNoClose.equals(ConstantValue.WORKFLOW_IS_TO_COLSE)){
			eventDisposal.setFinTimeStr(eventDisposal.getCreateTimeStr());
		}

		Long eventId = null;
		String instanceId = "";
		String eventId1 = eventResult.getEvent().getEventId();
		if(StringUtils.isNotBlank(eventId1)){
			instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(Long.valueOf(eventId1));
			if(StringUtils.isBlank(instanceId)){
				return convertToXmlResult("0", "事件ID[eventId]无效！");
			}
		}else{
			//保存事件
			eventId = eventDisposalService.saveEventDisposalReturnId(eventDisposal,paramsSave, userInfo);
			if(null == eventId || eventId.equals(-1L)) return convertToXmlResult("0", "事件保存失败！");
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("bizPlatform", eventResult.getEvent().getBizPlatform());
			params.put("oppo", eventResult.getEvent().getOppoSideBusiCode());
			if (eventId==null) {
				params.put("own", eventId1);
			}else {
				params.put("own", String.valueOf(eventId));
			}
			params.put("xmlData", xmlData);
			params.put("ownSideBizStatus", "0");
			//保存事件对应关系
			this.saveEventDataExchangeStatus(params);

			//完整度
			int score = this.getScore(eventDisposal, eventResult);
			Map<String, Object> eventExtend = new HashMap<>();
			eventExtend.put("eventId", eventId);
			eventExtend.put("integrityScore", score);
			try {
				eventExtendService.saveEventExtend(eventExtend);
			} catch (Exception e) {
				e.printStackTrace();
			}

			//获取workflowId
			Long workflowId = eventDisposalWorkflowService.capWorkflowId(null, eventId, userInfo, null);

			//启动事件
			if(workflowId!=null && workflowId>0){
				Map<String, Object> extraParam = new HashMap<String, Object>();
				extraParam.put("advice", eventResult.getEvent().getAdvice());
				try {
					instanceId = eventDisposalWorkflowService.startFlowByWorkFlow(eventId, workflowId, workflowIsNoClose, userInfo, extraParam);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(StringUtils.isBlank(instanceId)) return convertToXmlResult("0", "事件启动失败！");
			
			//盐都拉取12345对接个性化，使用对方传参采集时间，修改事件主表create,handle-time，历史任务表中流程的采集时间及办结时间
			if (checkTimeBybizPlatform(bizPlatform)) {
				
				Map<String, Object> pas = null;
				//对方传参事件采集时间
				Date endDate = DateUtils.formatDate(eventResult.getEvent().getRegisterTimeStr(),DateUtils.PATTERN_24TIME);
				//-1分钟
	            String startDate = DateUtils.formatDate(new Date(endDate.getTime() - 60000), "yyyy-MM-dd HH:mm:ss");
	            //处理时限+7天
	            Date  handleDate = holidayInfoService.getWorkDateByAfterWorkDay(endDate, 7);
//	            String transactOrgName="盐都区";//默认077对应的值
				if(("3507002").equals(bizPlatform)) {
//					transactOrgName="武夷山市";
					handleDate=DateUtils.formatDate(eventResult.getEvent().getHandleDate(),DateUtils.PATTERN_24TIME);//处理时限
				}
	            //查找启动流程后的任务
				List<Map<String, Object>> hisflow = eventDisposalWorkflowService.queryProInstanceDetail(Long.valueOf(instanceId));
				for(Map<String, Object> flow:hisflow) {
					pas = new HashMap<String, Object>();
					pas.put("eventId", eventId);
					pas.put("taskName", flow.get("TASK_CODE"));//start task1
					pas.put("transactOrgName", gridName);
					pas.put("taskId", flow.get("TASK_ID").toString());
					pas.put("taskCreateTime",startDate);
					pas.put("taskEndTime",eventResult.getEvent().getRegisterTimeStr());
					Long record = eventDisposalWorkflowService.saveOrUpdateTask(pas);
					if (record<1) {
						return convertToXmlResult("0", "【事件上报】设置任务采集时间失败！");
					}
				}
				EventDisposal event = new EventDisposal();
				event.setEventId(eventId);
				event.setCreateTime(endDate);
				event.setHandleDate(handleDate);
				if (eventDisposalService.updateEventDisposalById(event)<1) {
					return convertToXmlResult("0", "【事件上报】设置事件采集时间失败！");
				}
			}
			
		}
		//保存附件
		this.saveAttrs(eventResult.getEvent().getAttrs(),eventId,  ConstantValue.EVENT_SEQ_1);

		//派发事件
		if (StringUtils.isNotBlank(targetOrgCode)) {
			if (StringUtils.isNotBlank(eventId1)) {
				eventId = Long.valueOf(eventId1);
			}
			String sendEvtResult = sendEvt(bizPlatform, orgCode, targetOrgCode, Long.valueOf(instanceId), advice,
					String.valueOf(eventId));
			if (StringUtils.isNotBlank(sendEvtResult)) {
				return sendEvtResult;
			}
		}


		//保存任务对应关系
		List<Map<String, Object>> taskList = eventDisposalWorkflowService.queryProInstanceDetail(Long.valueOf(instanceId), IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
		for(Map<String, Object> map : taskList){
			if(map.containsKey("REMARKS")){
				String taskId_owe = map.get("TASK_ID").toString();
				this.saveTaskDataExchangeStatus(eventResult.getEvent().getBizPlatform(),eventResult.getEvent().getOppoSideBusiCode(),taskId_owe, xmlData);
			}
		}
		
		return convertToXmlResult("1", eventId == null ? eventId1 : String.valueOf(eventId));
	}
	
	
	
	/**
	 * 派发接口数据校验
	 * @param eventResult
	 * @return
	 */
	public String checkSendEvtData(XmlDataResult eventResult){
		if(null == eventResult.getAuth())
			return convertToXmlResult("0","认证信息[auth]无效");
		if(StringUtils.isBlank(eventResult.getAuth().getUsername()))
			return convertToXmlResult("0","用户名[username]无效");
		if(StringUtils.isBlank(eventResult.getAuth().getPassword()))
			return convertToXmlResult("0","密码[password]无效");
		if(!checkUserPwd(eventResult.getAuth().getUsername(), eventResult.getAuth().getPassword()))
			return convertToXmlResult("0","用户[username][password]验证失败");
		if(null == eventResult.getEvent())
			return convertToXmlResult("0","事件信息[event]无效");
		if(StringUtils.isBlank(eventResult.getEvent().getEventId()))
			return convertToXmlResult("0", "事件ID[eventId]无效");
		String bizPaltform = eventResult.getEvent().getBizPlatform();		
		if(StringUtils.isBlank(bizPaltform))
			return convertToXmlResult("0", "业务平台[bizPlatform]无效");
		if(StringUtils.isBlank(eventResult.getEvent().getAdvice()))
			return convertToXmlResult("0","处理意见[advice]无效");
		if(StringUtils.isBlank(eventResult.getEvent().getTargetOrgCode()))
			return convertToXmlResult("0","派发组织编码[targetOrgCode]无效");
		//校验附件名称是否为文件名称
		return "";
	}
	
	/**
	 * 事件派发接口
	 * @param xmlData
	 * @return
	 * @throws ParseException
	 */
	public String sendEvt(String xmlData){
		
		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);
		//验证有效性
		String checkResult = checkSendEvtData(eventResult);
		if(StringUtils.isNotBlank(checkResult)){
			return convertToXmlResult("0",checkResult);
		}
		Long eventId = Long.valueOf(eventResult.getEvent().getEventId());
		//事件状态判断
		EventDisposal eventDisposal = eventDisposalService.findEventByIdSimple(eventId);
		if (null != eventDisposal) {
			if (ConstantValue.EVENT_STATUS_ARCHIVE.equals(eventDisposal.getStatus())) {
				return convertToXmlResult("0", "【" + eventId + "】事件已结案！");// 03
			}
			if (ConstantValue.EVENT_STATUS_END.equals(eventDisposal.getStatus())) {
				return convertToXmlResult("0", "【" + eventId + "】事件已归档！");// 04
			}
			if (ConstantValue.STATUS_DEL_AND_RETURN_BACK.equals(eventDisposal.getStatus())) {
				return convertToXmlResult("0", "【" + eventId + "】事件已被删除！");// 06
			}
			if (ConstantValue.EVENT_STATUS_DRAFT.equals(eventDisposal.getStatus())) {
				return convertToXmlResult("0", "【" + eventId + "】事件未启动流程！");// 99
			}
		} else {
			return convertToXmlResult("0", "【" + eventId + "】事件未找到！");
		}
		
		String advice = eventResult.getEvent().getAdvice();
		String bizPlatform = eventResult.getEvent().getBizPlatform();
		String targetOrgCode = eventResult.getEvent().getTargetOrgCode();
		String instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(eventId);
		
		// 获取事件当前办理环节信息
		Map<String, Object> par = eventDisposalWorkflowService.curNodeData(Long.valueOf(instanceId));
		if (CommonFunctions.isNotBlank(par, "WF_ORGID")) {
			String orgId = par.get("WF_ORGID").toString().split(",")[0];
			OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoService.findByOrgId(Long.valueOf(orgId));
			
			String result = sendEvt(bizPlatform, orgSocialInfoBO.getOrgCode(), targetOrgCode, Long.valueOf(instanceId), advice, String.valueOf(eventId));
			if (StringUtils.isNotBlank(result)) {
				return result;
			}
			
			//派发成功，删除原先上报给对方成功的记录并保存附件
			DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
        	dataExchangeStatus.setOppoSideBizType("2");
        	dataExchangeStatus.setOwnSideBizType("2");
        	dataExchangeStatus.setOwnSideBizCode(String.valueOf(eventId));
        	dataExchangeStatus.setDestPlatform(bizPlatform);
			List<DataExchangeStatus> dataExchange = dataExchangeStatusService.findDataExchangeList(dataExchangeStatus);
			if (null != dataExchange && dataExchange.size()>0) {
				for(DataExchangeStatus data:dataExchange) {
					data.setOppoSideBizType("4");
					dataExchangeStatusService.updateDataExchangeStatusById(data);
				}
			}
			
			//保存附件
			this.saveAttrs(eventResult.getEvent().getAttrs(),eventId,ConstantValue.EVENT_SEQ_3);
			
		}else {
			return convertToXmlResult("0", "获取事件当前环节信息失败!");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bizPlatform", bizPlatform);
		params.put("own", String.valueOf(eventId));
		params.put("xmlData", xmlData);
		params.put("ownSideBizStatus", "1");
		//保存事件对应关系
		this.saveEventDataExchangeStatus(params);
		
		return convertToXmlResult("1", "派发成功!");
	}
	
	    /**
	     * 事件派发接口
	     * @param bizPlatform  来源平台
	     * @param orgCode  事件当前环节组织编码
	     * @param targetOrgCode  需要派发到组织编码
	     * @param instanceId  事件实例Id
	     * @param advice  意见
	     * @return
	     */
		private String sendEvt(String bizPlatform,String orgCode,String targetOrgCode,
					Long instanceId,String advice,String eventId) {
			
			UserInfo userInfo = new UserInfo();
			
			if(bizPlatform.equals("030")){//领悟综合执法
				if("3202810cg".equals(orgCode)){
					orgCode = "320281001A03";//城管局
				}
				OrgEntityInfoBO orgEntityInfoBO = orgEntityInfoOutService.selectOrgEntityInfoByOrgCode(targetOrgCode);
				if(orgEntityInfoBO!=null){
					List<OrgSocialInfoBO> orgSocialInfoBOs = orgSocialInfoService.findByLocationId(
							orgEntityInfoBO.getOrgId());
					for(OrgSocialInfoBO orgSocialInfo : orgSocialInfoBOs){
						if(orgSocialInfo.getOrgType().equals("1")){//组织
							targetOrgCode = orgSocialInfo.getOrgCode();
							break;
						}
					}
				}
			}
			
			// 事件进行派发
			String chiefLevel = "";
			String orgType = "";
			OrgSocialInfoBO socialInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode(orgCode);
			if (socialInfo != null) {
				chiefLevel = socialInfo.getChiefLevel();
				orgType = socialInfo.getOrgType();
			}

			OrgSocialInfoBO socialInfoBO = this.orgSocialInfoService.selectOrgSocialInfoByOrgCode(targetOrgCode);
			if(socialInfoBO!=null){
				String nextChiefLevel = socialInfoBO.getChiefLevel();
				
				String curNodeName = "";
				// 获取事件当前办理环节信息
				Map<String, Object> par = eventDisposalWorkflowService.curNodeData(Long.valueOf(instanceId));
				if (CommonFunctions.isNotBlank(par, "WF_ORGID") && CommonFunctions.isNotBlank(par, "WF_USERID")) {
					String userOrgId = par.get("WF_ORGID").toString().split(",")[0];
					String userCurId = par.get("WF_USERID").toString().split(",")[0];
					String orgName = par.get("WF_ORGNAME").toString().split(",")[0];
					String partyName = par.get("WF_USERNAME").toString().split(",")[0];
					curNodeName = par.get("NODE_NAME").toString();
					
					userInfo.setUserId(Long.valueOf(userCurId));
					userInfo.setOrgId(Long.valueOf(userOrgId));
					userInfo.setOrgCode(orgCode);
					userInfo.setPartyName(partyName);
					userInfo.setOrgName(orgName);
					
				}

				Long taskId = eventDisposalWorkflowService.curNodeTaskId(Long.valueOf(instanceId));

				String nextNodeName = "";//下一环节名称
				if ("018".equals(bizPlatform)) {
					nextNodeName = "task5";
				}else if ("038".equals(bizPlatform)) {
					nextNodeName = "task4";
				}

				if (StringUtils.isBlank(nextNodeName)) {

					int i = Integer.valueOf(nextChiefLevel) - Integer.valueOf(chiefLevel);
					String taskType = "";
					String taskLevel = String.valueOf(Math.abs(i));
					if (i > 0) {
						taskType = INodeCodeHandler.OPERATE_SEND;
					} else if (i < 0) {
						taskType = INodeCodeHandler.OPERATE_REPORT;
					} else if (i == 0 && !(socialInfoBO.getOrgType().equals("1") && orgType.equals("1"))) {// 同级 分流
						taskType = INodeCodeHandler.OPERATE_FLOW;
					} else {
						return convertToXmlResult("0", "【" + orgCode + "】【" + socialInfoBO.getOrgCode() + "】事件派发组织错误！");
					}

					Map<String, Object> initResultMap = eventDisposalWorkflowService
							.initFlowInfo(String.valueOf(taskId), Long.valueOf(instanceId), userInfo, null);

					if (CommonFunctions.isNotBlank(initResultMap, "taskNodes")) {
						List<Node> taskNodes = (List<Node>) initResultMap.get("taskNodes");
						if (taskNodes != null && taskNodes.size() > 0) {
							for (Node taskNode : taskNodes) {
								if (taskType.equals(taskNode.getType()) && taskLevel.equals(taskNode.getLevel())) {
									nextNodeName = taskNode.getNodeName();
									break;
								}
							}
						}
					  }
				    }

					boolean result = false;
					if (StringUtils.isBlank(nextNodeName)) {
						return convertToXmlResult("0", "下一可办理环节为空！事件由【"+orgCode+"】派发至【"+socialInfoBO.getOrgCode()+"】错误！");
					}

			try {
				
				String nodeCode = "";
				String nodeId = "";
				String userIds = "";
				String orgIds = "";
				String workflowName = "";
				
				ProInstance findProByInstanceId = baseWorkflowService.findProByInstanceId(instanceId);
				if (findProByInstanceId!=null) {
					//获取工作流流程图名称
					workflowName = findProByInstanceId.getProName();
				}
				Map<String, Object> nextNodeMap = new HashMap<String, Object>();
				nextNodeMap.put("curTaskName", curNodeName);
				List<Node> nextTaskNodes = baseWorkflowService.findNextTaskNodes(workflowName,
						ConstantValue.EVENT_WORKFLOW_WFTYPEID, Long.valueOf(eventId), userInfo, nextNodeMap);
				
				if (null != nextTaskNodes && nextTaskNodes.size()>0) {
					 for (Node node:nextTaskNodes) {
						if (node.getNodeName().equals(nextNodeName)) {
							nodeCode = node.getTransitionCode();
							nodeId = String.valueOf(node.getNodeId());
							break;
						}
						 
					 }
				}else {
					return convertToXmlResult("0", "获取下一环节信息失败！");
				}
				
				Map<String, Object> extraParam = new HashMap<String, Object>();
				extraParam.put("eventId", eventId);
				extraParam.put("instanceId", instanceId);
				extraParam.put("formId",eventId);
				extraParam.put("nodeId",nodeId);

				List<UserInfo> userInfoList = fiveKeyElementService.getUserInfoList(socialInfoBO.getOrgId(),nodeCode, null, extraParam);
				if (userInfoList != null && userInfoList.size() > 0) {
					for (UserInfo user : userInfoList) {
						userIds = userIds + "," + user.getUserId();
						orgIds = orgIds + "," + socialInfoBO.getOrgId();
					}
				} else {
					return convertToXmlResult("0", "获取下一环节人员信息失败！");
				}
				userIds = userIds.substring(1, userIds.length());
				orgIds = orgIds.substring(1, orgIds.length());

				result = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(Long.valueOf(instanceId),
						String.valueOf(taskId), nextNodeName, userIds, orgIds, advice, userInfo, "",
						new HashMap<String, Object>());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!result) {
				return convertToXmlResult("0", "事件派发失败！");
			}
				
			}else{
				return convertToXmlResult("0", "事件派发组织不存在！");
			}
			return "";
		}
	
	
	   //根据对接平台判断是否要使用对方传参时间
	  private boolean checkTimeBybizPlatform(String bizPlatform){
		//对接平台标识可查看对接枚举类-BizPlatfromEnum	
		String envir[] = {"3507002","077","035","320908001"};
		for (String s:envir) {
			if (bizPlatform.equals(s)) {
				return true;
			}
		}
			return false;
		}
		
		private String checkStartEvtCheckData(XmlDataResult eventResult){	
		if(null == eventResult.getAuth())
			return "认证信息[auth]无效";
		if(StringUtils.isBlank(eventResult.getAuth().getUsername()))
			return "用户名[username]无效";
		if(StringUtils.isBlank(eventResult.getAuth().getPassword()))
			return "密码[password]无效";
		if(!checkUserPwd(eventResult.getAuth().getUsername(), eventResult.getAuth().getPassword()))
			return "用户[username][password]验证失败";
		if(null == eventResult.getEvent())
			return "事件信息[event]无效";	
		if(StringUtils.isBlank(eventResult.getEvent().getOrgCode()))
			return "组织编码[orgCode]无效";	
		if(StringUtils.isBlank(eventResult.getEvent().getEventName()))
			return "事件标题[eventName]无效";
		if(StringUtils.isBlank(eventResult.getEvent().getHappenTimeStr()))
			return "发生时间[happenTimeStr]无效";
		if(StringUtils.isBlank(eventResult.getEvent().getOccurred()))
			return "事件发生地址[occurred]无效";
		if(StringUtils.isBlank(eventResult.getEvent().getContent()))
			return "事件内容[content]无效";
		if(StringUtils.isBlank(eventResult.getEvent().getOppoSideBusiCode()))
			return "第三方事件标识[oppoSideBusiCode]无效";
		
		String bizPlatform = eventResult.getEvent().getBizPlatform();
		if(StringUtils.isBlank(bizPlatform))
			return convertToXmlResult("0","来源平台[bizPlatform]无效");
		
		if(StringUtils.isBlank(eventResult.getEvent().getCreatorName())) {
			if (checkParamIsRequired(bizPlatform, "3")) { //3:校验联系人跟电话是否必填
				return convertToXmlResult("0","联系人[creatorName]无效");
			}
		}
		if (StringUtils.isBlank(eventResult.getEvent().getContactTel())) {
			if (checkParamIsRequired(bizPlatform, "3")) {
				return convertToXmlResult("0", "联系电话[contactTel]无效");
			}
		}
		if(StringUtils.isBlank(eventResult.getEvent().getLongitude())){
            if (checkParamIsRequired(bizPlatform,"2")) {//2：校验经纬度是非必填
          	  return convertToXmlResult("0","经纬度无效");
			}
		}
		
		//校验附件名称是否为文件名称
		if (null != eventResult.getEvent().getAttrs() && eventResult.getEvent().getAttrs().size()>0) {
			String result = checkAttrName(eventResult.getEvent().getAttrs());
	           if (StringUtils.isNotBlank(result)) {
	    	return result;
		    }
	    }
		if(StringUtils.isNotBlank(eventResult.getEvent().getHappenTimeStr())){
			String happenTimeStr = eventResult.getEvent().getHappenTimeStr();
			Date date = DateUtils.formatDate(happenTimeStr, DateUtils.PATTERN_24TIME);
			if(date==null)
				return "发生时间[happenTimeStr]格式不正确";
		}
		//去除特殊字符
		String eventName=this.StringFilter(eventResult.getEvent().getEventName());
		String occurred=this.StringFilter(eventResult.getEvent().getOccurred());
		String content=this.StringFilter(eventResult.getEvent().getContent());
		//验证长度
		if (!CommonFunctions.isLengthValidate(occurred, 256)) {
			return convertToXmlResult("0","事件发生地址[occurred]超出长度");
		}
		if (!CommonFunctions.isLengthValidate(eventName, 64)) {
        	return convertToXmlResult("0","事件标题[eventName]超出长度");
		}
		if (!CommonFunctions.isLengthValidate(content, 3500)) {
			return convertToXmlResult("0","事件描述[content]超出长度");
		}
		//南平随手拍政企直通车
		if ("204004".equals(eventResult.getEvent().getBizPlatform())) {
			if (StringUtils.isNotBlank(eventResult.getEvent().getExpectDepartMent())) {
				// 去除特殊字符
				String expect = this.StringFilter(eventResult.getEvent().getExpectDepartMent());
				// 验证期望部门长度
				if (!CommonFunctions.isLengthValidate(expect, 40)) {
					return convertToXmlResult("0", "期望处理部门[expectDepartMent]超出长度");
				}
				// 去除特殊字符
				String cotenctAndexpect = this.StringFilter(content + expect);
				if (!CommonFunctions.isLengthValidate(cotenctAndexpect, 4000)) {
					return convertToXmlResult("0", "事件内容【期望处理部门】超出长度");
				}
			}
		}
		
		//组织编码判断是否存在
		String orgCode=eventResult.getEvent().getOrgCode();
		OrgSocialInfoBO socialInfoBO = this.orgSocialInfoService.selectOrgSocialInfoByOrgCode(orgCode);
		if(socialInfoBO == null||socialInfoBO.getOrgId()==null){
			return  "组织编码【"+orgCode+"】不存在！";
		}	
		
		return "";
	}
		
	/**
	 * 判断是否校验上报唯一性
	 * @param bizPlatform
	 * @return
	 */
	private boolean checkOnlyReport(String bizPlatform) {
		String envir[] = {"3601025,3601026"};
		for (String s:envir) {
			if (bizPlatform.equals(s)) {
				return false;
			}
		}
		return true;
	}
		
	
	/**
	 * 微信、手机App上报事件到网格平台，先要经过审核才能转换为事件，首先要存入审核表T_EVENT_VERIFY.
	 * 目前通过App好报有的南平随手拍
	 * @param xmlData
	 * @return
	 */
	public String startEvtCheck(String xmlData){
		//将xml转化为XmlDataResult对象
				xmlData = xmlData.replace("&","&amp;");
				JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
				XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);
			
				//数据完整性校验
				String checkResult = checkStartEvtCheckData(eventResult);
				if(StringUtils.isNotBlank(checkResult)){
					return checkResult;
				}		
				//参数封装
				Map<String, Object> eventVerify=new HashMap<>();
				//是否为驳回事件
				String attEvtType = "";
				String oppoSideBusiCode = eventResult.getEvent().getOppoSideBusiCode();
				//orgCode必填项：用于查找对应的附件类型
				String userOrgCode =  eventResult.getEvent().getOrgCode();
				String bizPlatform = eventResult.getEvent().getBizPlatform();
				String x = eventResult.getEvent().getLongitude();
				String y = eventResult.getEvent().getLatitude();
				
				Map<String, Object> dataExParams = new HashMap<String, Object>();
				dataExParams.put("oppoSideBizCode", oppoSideBusiCode);
				dataExParams.put("srcPlatform", bizPlatform);
				Map<String, Object> param = new HashMap<String,Object>();
				param.put("userOrgCode", userOrgCode);
				Map<String, Object> attEventType = eventVerifyBaseService.fetchParam4Event(param);
				if (CommonFunctions.isNotBlank(attEventType, "attachmentType")) {
					attEvtType = attEventType.get("attachmentType").toString();
				}
				
				if (checkOnlyReport(bizPlatform)) {
					try {
						EUDGPagination exchangePagination = dataExchangeStatusService.findDataExchangePagination(1, 1, dataExParams);
						if(exchangePagination.getTotal() > 0){//表示之前有上报过且被驳回过的事件，对方可再次上报
							Long eventVerifyId=dataExchangeStatusService.findEventIdByOppCode(1,1,dataExParams);
							if(eventVerifyId==-1L){
								return convertToXmlResult("0","事件保存失败！(事件ID不存在)");
							}else{
								Map<String, Object> eventVerifyBy=eventVerifyBaseService.findEventVerifyById(eventVerifyId, null);
								if(eventVerifyBy!=null && "03".equals(//只有是驳回状态才可以修改
										eventVerifyBy.get("status").toString())){
									logger.info("驳回eventVerifyId=-"+eventVerifyId);
									eventVerify.put("isForce2Update",true);//强制更新
									eventVerify.put("eventVerifyId",eventVerifyId);
									eventVerify.put("status","01");//状态驳回更新为待审核
									eventVerify.put("remark","");//将原来备注清空				
									//先删除原来的附件
									int row=attachmentService.deleteByBizId(eventVerifyId, attEvtType);
									if(row<=0){
										return convertToXmlResult("0","修改失败(修改附件失败!)");
									}
								}else{
									return convertToXmlResult("0","修改失败(该事件状态不是驳回!)");
								}
							}
						}	
					}catch (Exception e) {					
						e.printStackTrace();
						return convertToXmlResult("0","事件保存失败！");
					}
				}
				
				Map<String, Object> eventJsonMap=new HashMap<>();
				
				if ("204003".equals(bizPlatform)) {
					eventJsonMap.put("source","013");//信息来源，默认为“随手拍”
				}else if ("202002".equals(bizPlatform)) {
					eventJsonMap.put("source","47");//信息来源，默认为“平安志愿者”
				}else if("202001".equals(bizPlatform)){//南昌app对接
					eventJsonMap.put("eventBizPlatform","202001");
					eventJsonMap.put("source", "15"); //默认为南昌社会治理app
				}else if ("3507001".equals(bizPlatform)) {
					eventJsonMap.put("source", "47"); //信息来源，默认为金山银山
				}else if ("011".equals(bizPlatform)) {
					eventJsonMap.put("source", "47"); //晋江公众微信
				}else if ("350701005".equals(bizPlatform)) {//延平城管二期
			    	eventJsonMap.put("source", "47");//AI视频监控
				}else if ("5403001".equals(bizPlatform)) {//西藏昌都对接雪亮工程
			    	eventJsonMap.put("source", "07");//雪亮工程
				}	
				eventJsonMap.put("isShow2Public",eventResult.getEvent().getIsShowPublic());//是否公开 1：是 0:否
				eventVerify.put("userOrgCode", userOrgCode);
				eventVerify.put("bizId", eventResult.getEvent().getOppoSideBusiCode());
				eventVerify.put("bizPlatform", bizPlatform);
				eventVerify.put("eventName", this.StringFilter(eventResult.getEvent().getEventName()));
				eventVerify.put("occurred", this.StringFilter(eventResult.getEvent().getOccurred()));
				//南平随手拍政企直通车
				if ("204004".equals(eventResult.getEvent().getBizPlatform())&&
						StringUtils.isNotBlank(eventResult.getEvent().getExpectDepartMent())){
					
					String content = this.StringFilter(eventResult.getEvent().getContent());
					String expect = this.StringFilter(eventResult.getEvent().getExpectDepartMent());
					eventVerify.put("content", content+"【期望处理部门】"+expect);
					
				}else {
					eventVerify.put("content", this.StringFilter(eventResult.getEvent().getContent()));
				}
				eventVerify.put("happenTimeStr", eventResult.getEvent().getHappenTimeStr());
				
				if (checkParamIsRequired(bizPlatform, "3")) {
					eventVerify.put("contactUser", this.StringFilter(eventResult.getEvent().getCreatorName()));
				}
				
				if(eventResult.getEvent().getContactTel()!=null&&!eventResult.getEvent().getContactTel().equals("0")) {
					eventVerify.put("tel", eventResult.getEvent().getContactTel());
				}
				eventVerify.put("longitude", eventResult.getEvent().getLongitude());
				eventVerify.put("latitude", eventResult.getEvent().getLatitude());	
				eventVerify.put("dataJsonMap",eventJsonMap);
				
				
				//isUseOrgCodeToVerify=1，直接使用orgCode当作审核记录的网格
				if ("1".equals(eventResult.getEvent().getIsUseOrgCodeToVerify())) {
					eventVerify.put("infoOrgCode", userOrgCode);
				//使用经纬度定位到指定网格层级(晋江公众微信指定到街道层级，orgCode传默认值：350582)
				}else if (StringUtils.isNotBlank(eventResult.getEvent().getUseXyFindGridByLevel())) {
					Map<String, Object> gridParams = new HashMap<>();
					gridParams.put("x", eventResult.getEvent().getLongitude());
					gridParams.put("y", eventResult.getEvent().getLatitude());
					gridParams.put("gridLevel", eventResult.getEvent().getUseXyFindGridByLevel());
					List<CoordinateInverseQueryGridInfo> queryGridInfos = coordinateInverseQueryService
							.findGridInfo(gridParams);
					//查找到则使用
					if (null != queryGridInfos && queryGridInfos.size() > 0) {
						eventVerify.put("infoOrgCode", queryGridInfos.get(0).getInfoOrgCode());
					//未找到则默认使用userOrgCode
					}else {
						eventVerify.put("infoOrgCode", userOrgCode);
					} 
				}
				//两者都没有，saveEventVerify默认使用传参经纬度查找网格，若查找不到照样入库但是审核列表不会显示！
				
				
				if ("350701005".equals(bizPlatform)) {
					//延平城管二期AI视频监控对接：经纬度不为空默认匹配到社区，没找到社区级找街道级，再没有默认到延平区；经纬度为空默认到延平区
					if (StringUtils.isNotBlank(x) && StringUtils.isNotBlank(y)) {
						String gridCode = getGridCode(x, y, "5");
						if (StringUtils.isNotBlank(gridCode)) {
							eventVerify.put("infoOrgCode", gridCode);
						} else {
							String code = getGridCode(x, y, "4");
							if (StringUtils.isNotBlank(code)) {
								eventVerify.put("infoOrgCode", code);
							} else {
								eventVerify.put("infoOrgCode", userOrgCode);
							}
						}
					}else {
						eventVerify.put("infoOrgCode", userOrgCode);
					}
				}
				
				
				Long eventVerifyId=null;		
				try {		
					//保存到审核列表
					eventVerifyId=eventVerifyBaseService.saveEventVerify(eventVerify);
					if((eventVerifyId==-1L) || (eventVerifyId==null)){
						return convertToXmlResult("0","事件保存审核列表失败！");
					}	
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("bizPlatform", eventResult.getEvent().getBizPlatform());
					params.put("oppo", eventResult.getEvent().getOppoSideBusiCode());
					params.put("own", String.valueOf(eventVerifyId));
					params.put("xmlData", xmlData);
					//保存事件对应关系，存储到中间表过程
					this.saveEventDataExchangeStatus(params);
					//保存附件
 					String innerAttachmentIds=this.saveAttrsReturnId(eventResult.getEvent().getAttrs(),
							eventVerifyId,attEvtType);	
		            if(StringUtils.isNotBlank(innerAttachmentIds)){//附件不为空更新审核列表
		            	eventVerify.put("isForce2Update",true);//强制更新
						eventVerify.put("eventVerifyId",eventVerifyId);
						eventVerifyBaseService.saveEventVerify(eventVerify);
		            }
					
		           //华为AI、实地AI对接个性化：判断该任务id 48小时内 是否有审核记录 上报成事件了，
		           //如果上报成事件的还没结案，还在处理中，则不审核上报成事件，否则就把收到的这笔审核记录自动上报事件到城管
		            if ("3601025".equals(bizPlatform)||"3601026".equals(bizPlatform)) {
		            	//开关
		            	String isOpen = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "NC_HUAWEI_FLOW_CG",
		        				IFunConfigurationService.CFG_TYPE_FACT_VAL);
		            	if (StringUtils.isNotBlank(isOpen) && "1".equals(isOpen)) {
		            		Map<String, Object> userMap = new HashMap<String, Object>();
			            	userMap.put("bizId", oppoSideBusiCode);
			            	userMap.put("bizPlatform", bizPlatform);
			            	userMap.put("createTimeStart", df.format(cn.ffcs.zhsq.utils.
			            			DateUtils.addInterval(new Date(), -2, "00")));
			            	//查询是否有正在办理的事件
			            	if (dataExchangeStatusService.findCountEvent4NcHuaWei(userMap)==0) {//没有正在处理的事件
			            		userMap = new HashMap<String, Object>();
			            		userMap.put("userOrgCode", "3601");
				            	Map<String, Object> findEventVerifyById = eventVerifyBaseService.findEventVerifyById(
				            			eventVerifyId, userMap);
				            	//自动审核上报事件流转至城管平台
					            autoVerifyEvent(findEventVerifyById, eventVerifyId, "");
							}
						}
					}
		            
				} catch (Exception e) {			
					e.printStackTrace();
					return convertToXmlResult("0","事件保存失败！");
				}
				
				return convertToXmlResult("1", String.valueOf(eventVerifyId));
	}
	
	/**
	 * 特殊字符过滤.
	 * @param str
	 * @return
	 */
	private String StringFilter(String str) throws PatternSyntaxException{		
		String regEx="[`~@#$%^&*()+=|{}''\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’\r\n|\r|\n|\n\r]"; 	
		Pattern p = Pattern.compile(regEx); 
		Matcher m = p.matcher(str);
    	return m.replaceAll("").trim();
	}
	
	    //保存事件对应附件接口
		public String saveEventAttrs(String xmlData){
			//将xml转化为XmlDataResult对象
			xmlData = xmlData.replace("&","&amp;");
			JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
			XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);

			//数据完整性校验
			String checkResult = checkSaveAttrsData(eventResult);
			if(StringUtils.isNotBlank(checkResult)){
				return checkResult;
			}
			//事件审核传附件需传入orgCode
			String eventId = eventResult.getEvent().getEventId();
			String orgCode = eventResult.getEvent().getOrgCode();
			String bizPlatform = eventResult.getEvent().getBizPlatform();
			if (StringUtils.isBlank(orgCode)) {
				//事件保存附件(默认为处理前，根据attrBiz为准)
				this.saveAttrs(eventResult.getEvent().getAttrs(),Long.valueOf(eventId),
						ConstantValue.EVENT_SEQ_1);
			}else {
				//事件审核保存附件(处理前)
				String attEvtType = "";
				Map<String, Object> param = new HashMap<String,Object>();
				param.put("userOrgCode", orgCode);
				Map<String, Object> attEventType = eventVerifyBaseService.fetchParam4Event(param);
				if (CommonFunctions.isNotBlank(attEventType, "attachmentType")) {
					attEvtType = attEventType.get("attachmentType").toString();
				}
				this.saveAttrsReturnId(eventResult.getEvent().getAttrs(),Long.valueOf(eventId),attEvtType);
			}
			
			if (bizPlatform.equals("3601025")) {//南昌大数据平台对接华为AI分析预警
				newReport(eventId);
			}
			
			return convertToXmlResult("1",eventId);
		}
		
		
	private void newReport(String eventId) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userOrgCode", "3601");
		params.put("eventId", eventId);
		params.put("taskName", "task8");
		//获取事件结案时间
		Map<String, Object> closeEvt = dataExchangeStatusService.getCloseEvt(params);
		String endTime = "";
		try {
			endTime = df.format(((TIMESTAMP)closeEvt.get("END_TIME")).dateValue());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//获取审核记录中，保存对方的任务号
		List<Map<String, Object>> findEventVerifyByParam = eventVerifyBaseService.findEventVerifyByParam(params);

		if (null != findEventVerifyByParam && findEventVerifyByParam.size() > 0) {
			String bizId = findEventVerifyByParam.get(0).get("bizId").toString();
			//根据任务号、结案时间，查找审核列表中是否有同类型预警
			params.put("endTime", endTime);
			params.put("bizId", bizId);
			 List<Map<String, Object>> findLikeEventVerify = dataExchangeStatusService.findLikeEventVerify(params);
			 
			//有同类型预警，将原来结案待评事件进行归档，意见为：“未处置完成，新事件编号为：……”
			if (null != findLikeEventVerify && findLikeEventVerify.size()>0) {
				
				Map<String,Object> verifyMap = findLikeEventVerify.get(0);
				Long eventVerifyId = Long.valueOf(verifyMap.get("eventVerifyId").toString());
				
				//有同类型预警，审核记录中有记录，判断是否已上报成事件了，若还是未审核，则自动上报事件并传给数字城管
				if (CommonFunctions.isBlank(verifyMap, "eventId") && 
						verifyMap.get("status").toString().equals("01")) {
					
					autoVerifyEvent(verifyMap, eventVerifyId, eventId);
					
			    //已审核，直接获取事件编号，将原来结案待评事件进行归档，意见为：“经过二次自动核查，未处置完成，新事件编号为：”
				}else {
					//将原来结案待评事件进行归档
					closeEvt(eventId,verifyMap.get("eventId").toString());
				}
				
			}else {
				//没有同类型预警，将原来结案待评事件进行归档，意见为：“经过二次自动核查，已完成处置”
				closeEvt(eventId,"");
			}
		}
	}
	
	/**
	 * 南昌创文明平台华为AI对接：
	 * 审核记录自动上报成事件并流转到数字城管环节
	 * 
	 */
	private void autoVerifyEvent(Map<String, Object> verifyMap,Long eventVerifyId,String eventId) {
		//eventId不为空，华为二次分析调用
		
		//获取默认的事件类型
        String eventTypeDefault= funConfigurationService.changeCodeToValue(
				ConstantValue.EVENT_VERIFY_ATTRIBUTE, "FLOW_EVENT_TYPE", IFunConfigurationService.CFG_TYPE_FACT_VAL,
				"3601");
		//获取默认的流程人员
		String flowUserId= funConfigurationService.changeCodeToValue(
				ConstantValue.EVENT_VERIFY_ATTRIBUTE, "FLOW_USER_ID_CG", IFunConfigurationService.CFG_TYPE_FACT_VAL,
				"3601");
		//获取默认的流程人员组织
		String flowOrgId= funConfigurationService.changeCodeToValue(
				ConstantValue.EVENT_VERIFY_ATTRIBUTE, "FLOW_ORG_ID_CG", IFunConfigurationService.CFG_TYPE_FACT_VAL,
				"3601");
		
		//首先根据审核表的信息保存成事件
		EventDisposal event = new EventDisposal();
		event.setGridCode(verifyMap.get("infoOrgCode").toString());//所属网格
		event.setEventName(verifyMap.get("eventName").toString());//事件标题
		event.setType(eventTypeDefault);//市容环境-背街小巷
		event.setHappenTimeStr(df.format((Date)verifyMap.get("happenTime")));//事发时间
		event.setOccurred(verifyMap.get("occurred").toString());//事发地址
		event.setContent(verifyMap.get("content").toString());//事件内容
		event.setBizPlatform(verifyMap.get("bizPlatform").toString());
		event.setContactUser(verifyMap.get("contactUser").toString());//联系人
		event.setTel(verifyMap.get("tel").toString());//联系人电话
		event.setStatus(ConstantValue.EVENT_STATUS_DRAFT);//默认事件状态为草稿
		event.getResMarker().setMapType("5");//默认二维地图类型
		event.setUrgencyDegree(DEFAULT_URGENCY);//紧急程度
		event.setInfluenceDegree(DEFAULT_INFLUENCE);//影响范围 
		event.setCollectWay(DEfAULT_COLLECTWAY);//采集渠道
		event.setSource(DEFAULT_EVENT_SOURCE);//事件来源
		event.setInvolvedNum("00");//涉及人员
		
		Map<String,Object> expandParam=new HashMap<String,Object>();
		expandParam.put("patrolType", "0");	//巡防类型
		expandParam.put("pointSelection", 0L);//获取不到责任点位，设置为其他
		event.getResMarker().setX(verifyMap.get("longitude").toString());
		event.getResMarker().setY(verifyMap.get("latitude").toString());
		
		UserInfo userAdmin = null;
		List<UserInfo> findUserByOrgCode = userManageService.findUserByOrgCode("3601");
        if(findUserByOrgCode.size() > 0){
            for(UserInfo u:findUserByOrgCode){
                if("南昌市管理员".equals(u.getPartyName())){
                    userAdmin = u;
                    break;
                }
            }
        } 
		//上报事件
        Long eventIdNew = eventDisposalService.saveEventDisposalReturnId(event, expandParam, userAdmin);
        if(eventIdNew > 0) {//事件保存成功
			
        	if (StringUtils.isNotBlank(eventId)) {//华为二次分析调用
        		//将原来结案待评事件进行归档
    			closeEvt(eventId,String.valueOf(eventIdNew));
			}
        	
			//保存附件
			List<Attachment> attList = attachmentService.findByBizId(eventVerifyId, "ZHSQ_EVENT_WECHAT", "1");
			for (Attachment attachment : attList) {
				attachment.setAttachmentId(null);
				attachment.setBizId(eventIdNew);
				attachment.setAttachmentType("ZHSQ_EVENT");
				 attachmentService.saveAttachment(attachment);
			}
				
				//事件流转
				Map<String,Object> extraParma=new HashMap<String,Object>();
				extraParma.put("advice", "AI识别事件自动流转提交");
				//获取工作流流程Id
				Long capWorkflowId = eventDisposalWorkflowService.capWorkflowId(null, eventIdNew, userAdmin, new HashMap<String,Object>());
				if(capWorkflowId!=null&&capWorkflowId>0) {
					
					try {
						String instanceId = eventDisposalWorkflowService.startFlowByWorkFlow(eventIdNew, capWorkflowId, "0", userAdmin, extraParma);
						
						if(StringUtils.isNotBlank(instanceId)&&Long.valueOf(instanceId)>0) {
							
							//提交事件至数字城管环节
							//获取当前任务Id
							Long curNodeTaskId = eventDisposalWorkflowService.curNodeTaskId(Long.valueOf(instanceId));
							//设置办理意见
							String advice="AI识别事件流转到数字城管";
							boolean result = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(Long.valueOf(instanceId), 
									String.valueOf(curNodeTaskId), "task14", flowUserId, flowOrgId, advice, userAdmin, null, new HashMap<String,Object>());
							if(result) {
								//修改审核表信息(设置事件Id,改为已上报)
								Map<String,Object> updateVerifyParams=new HashMap<String,Object>();
								updateVerifyParams.put("eventId", eventIdNew);
								updateVerifyParams.put("status", "02");
								updateVerifyParams.put("eventVerifyId", eventVerifyId);
								updateVerifyParams.put("remark", "AI识别事件流转到数字城管");
								eventVerifyBaseService.updateEventVerifyById(updateVerifyParams, userAdmin);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		  }
	}
	
	private void closeEvt(String eventId,String newEvtId) {
		
		EventDisposal event = null;
		String advice = "经过二次自动核查，已完成处置";
		if (StringUtils.isNotBlank(newEvtId)) {//找到同类型预警
			 event = eventDisposalService.findEventByIdSimple(
					Long.valueOf(newEvtId));
			 advice = "经过二次自动核查，未处置完成，新事件编号为：" + event.getCode();
		}
		Map<String, Object> eventMap = new HashMap<String, Object>();
      	eventMap.put("evaLevel","02");
    	eventMap.put("evaContent","满意");
		eventMap.put("eventId",eventId);
		eventMap.put("advice",advice);
		eventMap.put("isEvaluate", true);
		eventDisposalDockingService.subWorkflow(eventMap);
	}
		
	private String saveAttrsReturnId(List<XmlAttr> attrs, Long eventId,String attEvtType){

		String innerAttachmentIds="";
		Long id=-1L;		
		if(attrs!=null &&attrs.size()>0){
			for(XmlAttr attr : attrs){
				id=-1L;				
				logger.info("attr.getAttrURL()---"+attr.getAttrURL());
				if(StringUtils.isNotBlank(attr.getAttrURL())){
					int j = 0;//附件若获取失败，默认取3次
					while (j < 3) {
						try {
							URL url = new URL(attr.getAttrURL());
							
							//通过url获取输入流
							InputStream inputStream =fileURL2InputStream(url);
							
							//获取自己数组
							byte[] getData = readInputStream(inputStream);
							String path = fileUploadService.uploadSingleFile(attr.getAttrName(), getData,
									ConstantValue.RESOURSE_DOMAIN_KEY, "eventPhoto");
							Attachment at = new Attachment();
							at.setBizId(eventId);
							at.setAttachmentType(attEvtType);
							at.setFilePath(path);
							at.setFileName(attr.getAttrName());
							at.setEventSeq(ConstantValue.EVENT_SEQ_1);
						
							id = attachmentService.saveAttachment(at);
							if(id!=-1){
								if(innerAttachmentIds!=""){
									innerAttachmentIds+=",";
								}
								innerAttachmentIds+=id;
							}
							break;
						}catch (Exception e) {
							j++;
							try {
								Thread.sleep(1000);
								//附件重试下载3次，都失败将消息发送至消息队列中处理,并记录到中间表中
								if (j==3) {
									Map<String, Object> pam = new HashMap<String,Object>();
									pam.put("xmlData", "附件接收失败事件Id:" + eventId + "||附件名称：" + attr.getAttrName()
											+ "||附件Url:" + attr.getAttrURL()+"||异常信息："+e.getMessage());
									pam.put("own", eventId);
									pam.put("oppo","321");
									this.saveEventDataExchangeStatus(pam);
								}
							} catch (Exception e1) {
								e1.getMessage();
							}
						}
					}
				}
				
				if(id==-1) {
					
					if ((StringUtils.isNotBlank(attr.getBase64())) || (StringUtils.isNotBlank(attr.getAttrBASE64()))) {
						String base64 = "";
						if (StringUtils.isNotBlank(attr.getBase64()))
							base64 = attr.getBase64();
						if (StringUtils.isNotBlank(attr.getAttrBASE64())) {
							base64 = attr.getAttrBASE64();
						}

						Attachment attachment = new Attachment();
						attachment.setBizId(eventId);
						if (StringUtils.isNotBlank(attr.getAttrName()))
							attachment.setFileName(attr.getAttrName());
						else {
							attachment.setFileName("temp.jpg");
						}
						attachment.setAttachmentType(attEvtType);
						attachment.setEventSeq(ConstantValue.EVENT_SEQ_1);
						try {
							String imgUrl = this.fileUploadService.uploadSingleFile(attr.getAttrName(),
									Base64.decode(base64), "ZHSQ_EVENT", "perfile");
							attachment.setFilePath(imgUrl);
							Long r = this.attachmentService.saveAttachment(attachment);
							if (r.longValue() != -1L) {
								if (innerAttachmentIds != "") {
									innerAttachmentIds = innerAttachmentIds + ",";
								}
								innerAttachmentIds = innerAttachmentIds + r;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					
				}
				
			}
		}
		return innerAttachmentIds;
	}
	
	private String checkSaveAttrsData(XmlDataResult eventResult){	
		if(null == eventResult.getAuth())
		    return convertToXmlResult("0", "认证信息[auth]无效");
		if(StringUtils.isBlank(eventResult.getAuth().getUsername()))
		    return convertToXmlResult("0", "用户名[username]无效");
		if(StringUtils.isBlank(eventResult.getAuth().getPassword()))
		    return convertToXmlResult("0", "密码[password]无效");
		if(!checkUserPwd(eventResult.getAuth().getUsername(), eventResult.getAuth().getPassword()))
		    return convertToXmlResult("0", "用户[username][password]验证失败");
		if(null == eventResult.getEvent())
		    return convertToXmlResult("0", "事件信息[event]无效");
		String orgCode = eventResult.getEvent().getOrgCode();
		if (StringUtils.isNotBlank(orgCode)) {
			if (!StringUtils.isNumeric(orgCode)) {
				return convertToXmlResult("0", "组织编码[orgCode]不是整型");
			}
			OrgSocialInfoBO socialInfoBO = this.orgSocialInfoService.selectOrgSocialInfoByOrgCode(orgCode);
			if(socialInfoBO == null||socialInfoBO.getOrgId()==null){
				return  convertToXmlResult("0","组织编码【"+orgCode+"】不存在！");
			}	
		}
		if (StringUtils.isBlank(eventResult.getEvent().getEventId())) 
			return convertToXmlResult("0", "事件Id[eventId]无效");
		if (StringUtils.isBlank(eventResult.getEvent().getBizPlatform())) 
			return convertToXmlResult("0", "来源平台[bizPlatform]无效");
		if (null == eventResult.getEvent().getAttrs()) 
			return convertToXmlResult("0", "附件信息[attrs]无效");		
		//校验附件名称是否为文件名称  如：图片1.jpg
		if (null != eventResult.getEvent().getAttrs() && eventResult.getEvent().getAttrs().size()>0) {
			 String result = checkAttrName(eventResult.getEvent().getAttrs());
		  if (StringUtils.isNotBlank(result)) {
			return result;
		  }
		}
		
		return "";
	}

      private void saveAttrs(List<XmlAttr> attrs, Long eventId, String eventSeq){
		
    	Long attachmentId=-1L;  
    	
		if(attrs!=null &&attrs.size()>0){
			for(XmlAttr attr : attrs){
				
				attachmentId=-1L;
				
				logger.info("attr.getAttrURL()---"+attr.getAttrURL());
				if(StringUtils.isNotBlank(attr.getAttrURL())){
					int  i = 0;
					while(i<3) {//若下载异常，默认重试3次
						try {
							URL url = new URL(attr.getAttrURL());
							
							//通过url获取输入流
							InputStream inputStream =fileURL2InputStream(url);

							//获取自己数组
							byte[] getData = readInputStream(inputStream);
							String path = fileUploadService.uploadSingleFile(attr.getAttrName(), getData, ConstantValue.RESOURSE_DOMAIN_KEY, "eventPhoto");
							Attachment at = new Attachment();
							at.setBizId(eventId);
							at.setAttachmentType("ZHSQ_EVENT");
							at.setFilePath(path);
							at.setFileName(attr.getAttrName());
							if(StringUtils.isNotBlank(attr.getAttrBiz())){//设置图片类型 0：处理前  1：处理后
								if("1".equals(attr.getAttrBiz())){
									at.setEventSeq("3");
								}else {
									at.setEventSeq(eventSeq);
								}
							}else{//默认处理前
								at.setEventSeq(eventSeq);
							}
							attachmentId = attachmentService.saveAttachment(at);
							if (attachmentId == -1) {//保存附件失败
								throw new Exception("saveAttachment异常！");
							}
						    break;
						}catch (Exception e) {
							i++;
							try {
								Thread.sleep(1000);
								if (i==3) {//失败重试3次完记录到中间表
									Map<String, Object> pam = new HashMap<String,Object>();
									pam.put("xmlData", "附件下载失败事件Id:" + eventId + "||附件名称：" + attr.getAttrName()
											+ "||附件Url:" + attr.getAttrURL()+"||异常信息："+e.getMessage());
									pam.put("own", eventId);
									pam.put("oppo","123");
									this.saveEventDataExchangeStatus(pam);
								}
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
				
				if(attachmentId==-1) {
					
					if(StringUtils.isNotBlank(attr.getBase64()) || StringUtils.isNotBlank(attr.getAttrBASE64())){
						String base64 = "";
						if(StringUtils.isNotBlank(attr.getBase64()))
							base64 = attr.getBase64();
						if(StringUtils.isNotBlank(attr.getAttrBASE64()))
							base64 = attr.getAttrBASE64();

//						attachmentService.deleteByBizId(eventId, ConstantValue.EVENT_ATTACHMENT_TYPE);
						Attachment attachment = new Attachment();
						attachment.setBizId(eventId);
						if(StringUtils.isNotBlank(attr.getAttrName())){
							attachment.setFileName(attr.getAttrName());
						}else{
							attachment.setFileName("temp.jpg");
						}
						attachment.setAttachmentType(ConstantValue.EVENT_ATTACHMENT_TYPE);
						try {
							String imgUrl = fileUploadService.uploadSingleFile(attr.getAttrName(), Base64.decode(base64), "ZHSQ_EVENT", "perfile");
							attachment.setFilePath(imgUrl);
							if(StringUtils.isNotBlank(attr.getAttrBiz())){//设置图片类型 0：处理前  1：处理后
								if("1".equals(attr.getAttrBiz())){
									attachment.setEventSeq("3");
								}else {
									attachment.setEventSeq(eventSeq);
								}
							}else{
								attachment.setEventSeq(eventSeq);
							}
							attachmentService.saveAttachment(attachment);
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				}
				
				
			}
		}
	}


	/**
	 * 保存过程中间记录
	 * @param bizPlatform
	 * @param oppo
	 * @param own
	 * @param xmlData
	 * @return
	 */
	private Boolean saveTaskDataExchangeStatus(String bizPlatform, String oppo, String own, String xmlData){
		DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
		dataExchangeStatus.setDestPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		dataExchangeStatus.setSrcPlatform(bizPlatform);
		dataExchangeStatus.setOppoSideBizCode(oppo);
		dataExchangeStatus.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
		dataExchangeStatus.setOwnSideBizCode(own);
		dataExchangeStatus.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
		dataExchangeStatus.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
		dataExchangeStatus.setStatus(IDataExchangeStatusService.STATUS_VALIDATE);
		dataExchangeStatus.setXmlData(xmlData);
		return dataExchangeStatusService.saveDataExchangeStatus(dataExchangeStatus) > 0;
	}

/**
	 * 保存事件中间记录
	 * @param bizPlatform
	 * @param oppo 第三方业务主键
	 * @param own 我方事件业务主键
	 * @param xmlData
	 * @return
	 */
	private Boolean saveEventDataExchangeStatus(Map<String, Object> params){
		
		String xmlData = "";
		String bizPlatform = "";
		String oppo = "";
		String own = "";
		String ownSideBizStatus = "";
		if (params.get("xmlData")!=null) {
			xmlData = params.get("xmlData").toString();
		}
		if (params.get("bizPlatform")!=null) {
			bizPlatform = params.get("bizPlatform").toString();
		}
		if (params.get("oppo")!=null) {
			oppo = params.get("oppo").toString();
		}
		if (params.get("own")!=null) {
			own = params.get("own").toString();
		}
		if (params.get("ownSideBizStatus")!=null) {
			ownSideBizStatus = params.get("ownSideBizStatus").toString();
		}
		
		if(StringUtils.isNotBlank(xmlData)){
				xmlData=this.subRangeString(xmlData.replace(" ", ""), "<base64>","</base64>");//去除base64
				xmlData=this.subRangeString(xmlData.replace(" ", ""), "<attrBASE64>","</attrBASE64>");//去除attrBASE64
				xmlData=this.subRangeString(xmlData.replace(" ", ""), "<attrBase64>","</attrBase64>");//去除attrBase64
		}
		//为了防止base64未过滤，导致字段长度过大
		if (xmlData.length()>1500) {
				xmlData = xmlData.substring(0,1500);
		}
		DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
		dataExchangeStatus.setDestPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		dataExchangeStatus.setSrcPlatform(bizPlatform);
		dataExchangeStatus.setOppoSideBizCode(oppo);
		dataExchangeStatus.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		dataExchangeStatus.setOwnSideBizCode(own);
		dataExchangeStatus.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		dataExchangeStatus.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_UNEXCHANGE);
		dataExchangeStatus.setStatus(IDataExchangeStatusService.STATUS_VALIDATE);
		dataExchangeStatus.setOwnSideBizStatus(ownSideBizStatus);
		dataExchangeStatus.setXmlData(xmlData);

		return dataExchangeStatusService.saveDataExchangeStatus(dataExchangeStatus) > 0;
	}
	
	/**
	 * 将字符串去除制定str1到str2中的字符串
	 * @param body str1  str2
	 * @return
	 */
	private String subRangeString(String body,String str1,String str2) {
        while (true) {
            int index1 = body.indexOf(str1);
            if (index1 != -1) {
                int index2 = body.indexOf(str2, index1);
                if (index2 != -1) {
                    String str3 = body.substring(0, index1) + body.substring(index2 +    str2.length(), body.length());       
                    body = str3;
                }else {
                    return body;
                }
            }else {
                return body;
            }
        }
    }

	/**
	 * 下派
	 * @param xmlData
	 * @return
	 * @throws AxisFault
	 */
	public String shuntEvent(String xmlData) throws AxisFault{
		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);
		if(checkShuntEventData(eventResult)){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("gridCode", eventResult.getEvent().getGridCode());
			params.put("eventName", eventResult.getEvent().getEventName());
			params.put("happenTimeStr", eventResult.getEvent().getHappenTimeStr());
			params.put("occurred", eventResult.getEvent().getOccurred());
			params.put("content", eventResult.getEvent().getContent());
			params.put("handleDate", eventResult.getEvent().getHandleDate());
			params.put("isSendSms", eventResult.getEvent().getIsSendSms());
			params.put("oppoSideBizCode", eventResult.getEvent().getOppoSideBusiCode());
			params.put("bizPlatform", eventResult.getEvent().getBizPlatform());
			params.put("urgencyDegree", eventResult.getEvent().getUrgency());
			params.put("influenceDegree", eventResult.getEvent().getInfluence());
			params.put("creatorID", eventResult.getEvent().getCreatorID());
			params.put("creatorName", eventResult.getEvent().getCreatorName());
			params.put("contactUser", eventResult.getEvent().getContactUserName());
			params.put("source", eventResult.getEvent().getSource());
			params.put("tel", eventResult.getEvent().getContactTel());
			params.put("registerTimeStr", eventResult.getEvent().getRegisterTimeStr());
			params.put("advice", eventResult.getEvent().getAdvice());
			params.put("pics", eventResult.getEvent().getPics());
			params.put("attrs", eventResult.getEvent().getAttrs());
			
			
			String x = eventResult.getEvent().getX();
			String y = eventResult.getEvent().getY();
			JSONObject resMarker = new JSONObject();
			resMarker.put("x",x);
			resMarker.put("y",y);
			params.put("resMarker",resMarker);

			String nextOrgCodes = "";
			String nextNodeName = "";
			String parentGridCode = "";
			String isSearchXy4GridCode = "";
			long gridAdminUserId = 0;
			String bizPlatform = params.get("bizPlatform").toString();
			
			String eventType = formatEventType(eventResult.getEvent().getEventType(), bizPlatform);
			params.put("type", eventType);
			
			if(bizPlatform.equals("015")){
				String gridCode = getGridCode(x, y, "");
				if (StringUtils.isNotBlank(gridCode)) {
					params.put("gridCode", gridCode);
				}
				
				nextOrgCodes = gridCode.substring(0, gridCode.length() - 3);
			//盐都燃气云平台对接
			}else if(bizPlatform.equals("320903001")) {
				
				Map<String, Object> gridParams = new HashMap<>();
				gridParams.put("x", x);
				gridParams.put("y", y);
				gridParams.put("gridLevel", "6");
				List<CoordinateInverseQueryGridInfo> gridInfos = coordinateInverseQueryService
						.findGridInfo(gridParams);
				// 根据经纬度自动到对应网格
				if (null != gridInfos && gridInfos.size() > 0) {
					isSearchXy4GridCode = gridInfos.get(0).getInfoOrgCode();
					//若是专属网格，网格下没有挂用户，需要使用父级
					OrgSocialInfoBO socialInfoBO = this.orgSocialInfoService.
							selectOrgSocialInfoByOrgCode(isSearchXy4GridCode);
					List<UserBO> userList = this.userManageService.
								getUserListByUserExampleParamsOut(null, null, socialInfoBO.getOrgId());
					if (userList == null || userList.size() == 0) {
						gridParams = new HashMap<>();
						gridParams.put("x", x);
						gridParams.put("y", y);
						gridParams.put("gridLevel", "5");
					    gridInfos = coordinateInverseQueryService.findGridInfo(gridParams);
						if (null != gridInfos && gridInfos.size() > 0) {
							isSearchXy4GridCode = gridInfos.get(0).getInfoOrgCode();
						} else {
							return convertToXmlResult("0", "经纬度【" + x + "】【" + y + "】未找到网格！");
						}
					}
					
				}else {
					// 若未找到则直接使用经纬度能查找出来的网格
					gridParams = new HashMap<>();
					gridParams.put("x", x);
					gridParams.put("y", y);
					gridInfos = coordinateInverseQueryService.findGridInfo(gridParams);
					if (null != gridInfos && gridInfos.size() > 0) {
						isSearchXy4GridCode = gridInfos.get(0).getInfoOrgCode();
					} else {
						return convertToXmlResult("0", "经纬度【" + x + "】【" + y + "】未找到网格！");
					}
				}
				//使用网格编码查找对应父级的编码（村社区级别采集事件）
				MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(isSearchXy4GridCode);
				MixedGridInfo parentGridInfo = mixedGridInfoService.findMixedGridInfoById(
						gridInfo.getParentGridId(), false);
				parentGridCode = parentGridInfo.getGridCode();
				params.put("gridCode", parentGridCode);
				params.put("orgCode", parentGridCode);
				if (StringUtils.isNotBlank(eventResult.getEvent().getEventName())) {
					params.put("eventName", "[燃气云平台]"+eventResult.getEvent().getEventName());
				}
				
			}else{
				String codes = "";
				for(XmlTaskResult xmlTaskResult : eventResult.getEvent().getTasks()){
					String nextOrgCode = xmlTaskResult.getNextOrgCode();
					nextNodeName = xmlTaskResult.getTaskName();
					String orgCode = baseDictionaryService.changeCodeToName(ConstantValue.EVENT_DOCKING_ORG, nextOrgCode, null);
					if(StringUtils.isBlank(orgCode)){
						return convertToXmlResult("0", "组织不存在！");
					}
					if(StringUtils.isNotBlank(orgCode)){
						codes = codes + "," + orgCode;
					}else{
						codes = codes + "," + nextOrgCode;
					}
				}
				nextOrgCodes = codes.substring(1, codes.length());
			}
			params.put("nextNodeName", nextNodeName);
			params.put("nextOrgCode", nextOrgCodes);

			//004便民服务、011群众微信上报对接
			if(bizPlatform.equals("004") || bizPlatform.equals("011")){
				params.put("gridCode", "350582");
				params.put("orgCode", "350582");
				params.put("nextNodeName", "乡镇(街道)处理");
				params.put("oppoSideBizCode", eventResult.getEvent().getEventId());
				if (bizPlatform.equals("004")) {
					params.put("source", "20");//默认信息来源：市便民服务中心
					params.put("collectWay", "06");//默认采集渠道：市便民服务中心
				}
				//判断事件内容是否超过50个字符
				String content = eventResult.getEvent().getContent();
				if (!CommonFunctions.isLengthValidate(content, 50)) {
					params.put("eventName", content.substring(0, 25));
				}else {
					params.put("eventName", content);
				}
			}
			
			if(bizPlatform.equals("040")){
				String gridCode = funConfigurationService.changeCodeToValue("MEIYA_EVENT_GRID_CODE", params.get("gridCode").toString(), IFunConfigurationService.CFG_TYPE_FACT_VAL);
				params.put("gridCode", gridCode);
			}
			if(bizPlatform.equals("008")){
				params.put("userId", "30013429");
			}
			Map<String, Object> result = eventDisposalDockingService.saveEventDisposalAndShunt(params);
			Boolean isJJ = false;
			for(String orgCode : params.get("nextOrgCode").toString().split(",")){
				if(orgCode.equals("350582")){
					isJJ = true;
				}
			}
			if(bizPlatform.equals("008") && !isJJ){
				Long instanceId = result.get("instanceId") == null ? null : Long.valueOf(result.get("instanceId").toString());
				Long taskId = eventDisposalWorkflowService.curNodeTaskId(Long.valueOf(instanceId));
				String nextNode = "便民服务中心处理";//便民指挥中心处理
				String advice = "派发便民指挥中心处理";

				UserInfo userInfo = userService.getUserExtraInfoByUserId(30013429L, "350582114");
				Boolean reportResult = false;
				try {
					reportResult = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(instanceId,String.valueOf(taskId), nextNode, "", "", advice, userInfo, "", null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(!reportResult){
					return convertToXmlResult("0", result.get("eventId").toString());
				}
			}
			
			if(params.get("bizPlatform").equals("011")){
				if(CommonFunctions.isNotBlank(params, "pics")){
					String[] pics = params.get("pics").toString().split(",");
					String configUri = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.WX_WS_URI, IFunConfigurationService.CFG_TYPE_FACT_VAL);
					if(StringUtils.isNotBlank(configUri)){//configUri + pic
						List<Attachment> attachments = new ArrayList<Attachment>();
						for(String pic : pics){
							try {
							// http://192.168.151.133:11800/upload/weixin/202007/HxvgIWoXZga7qkXKzh1INutSVbywybGlqVy8ctjpv1cZQ471zMue7N3hZe-oCMCW.jpg	
							String picPath = configUri + pic;
							URL url = new URL(picPath);
							//通过url获取输入流
							InputStream inputStream = fileURL2InputStream(url);
							byte[] getData = readInputStream(inputStream);
							String fileName = String.valueOf(System.currentTimeMillis());
							String suffix = pic.substring(pic.lastIndexOf("."), pic.length());
							String imgUrl = fileUploadService.uploadSingleFile(fileName+suffix, getData, ConstantValue.RESOURSE_DOMAIN_KEY, "eventPhoto");
								Attachment attachment = new Attachment();
								attachment.setBizId(Long.valueOf(result.get("eventId").toString()));
								attachment.setAttachmentType(ConstantValue.EVENT_ATTACHMENT_TYPE);
								attachment.setEventSeq("1");
								attachment.setFilePath(imgUrl);
								attachments.add(attachment);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						attachmentService.saveAttachment(attachments);
					}
				}
			}
			
			if(bizPlatform.equals("015")) if (CommonFunctions.isNotBlank(params, "attrs")) {
				List<XmlAttr> attrs = (List<XmlAttr>) params.get("attrs");
				if (attrs != null && attrs.size() > 0) {
					List<Attachment> attachments = new ArrayList<Attachment>();
					for(int i=0;i<attrs.size();i++){
						XmlAttr attr = attrs.get(i);
						if(StringUtils.isNotBlank(attr.getAttrBASE64())){
							Attachment attachment = new Attachment();
							attachment.setBizId(Long.valueOf(result.get("eventId").toString()));
							attachment.setAttachmentType(ConstantValue.EVENT_ATTACHMENT_TYPE);
							attachment.setEventSeq("1");
							String imgUrl = fileUploadService.uploadSingleFile(i+".png", Base64.decode(attr.getAttrBASE64()), "ZHSQ_EVENT", "perfile");
							attachment.setFilePath(imgUrl);
							attachments.add(attachment);
						}
					}
					attachmentService.saveAttachment(attachments);
				}
			}
			
			if (bizPlatform.equals("011")) {// 晋江群众微信
				// 根据经纬度，自动到对应乡镇街道，未找到则默认到晋江市
				Map<String, Object> gridParams = new HashMap<>();
				gridParams.put("x", x);
				gridParams.put("y", y);
				gridParams.put("gridLevel", "4");
				List<CoordinateInverseQueryGridInfo> gridInfos = coordinateInverseQueryService
						.findGridInfo(gridParams);
				if (null != gridInfos && gridInfos.size() > 0) {
					isSearchXy4GridCode = gridInfos.get(0).getInfoOrgCode();
				}
			}
			
			if((Boolean)result.get("result") || (result.get("eventId")!=null && result.get("instanceId")!=null)){
				//需要派发的组织编码
				String gridCode = eventResult.getEvent().getGridCode();
				String creatGridCode = "350582";
				
				//晋江便民服务对接、群众微信对接，默认由晋江市层级采集事件，自动下派到对应乡镇；盐都燃气云平台对接默认由村社区采集下派到网格
				if(bizPlatform.equals("004") || StringUtils.isNotBlank(isSearchXy4GridCode)){
					if (StringUtils.isNotBlank(isSearchXy4GridCode)) {
						gridCode = isSearchXy4GridCode;
					}
					String chiefLevel = "";
					String orgType = "";
					if (bizPlatform.equals("320903001")) {
						creatGridCode = parentGridCode;
					}
					OrgSocialInfoBO socialInfo = this.orgSocialInfoService.selectOrgSocialInfoByOrgCode(creatGridCode);
					if(socialInfo != null){
						chiefLevel = socialInfo.getChiefLevel();
						orgType = socialInfo.getOrgType();
					}
					String instanceId = result.get("instanceId").toString();
					
					//下派组织
					OrgSocialInfoBO socialInfoBO = this.orgSocialInfoService.selectOrgSocialInfoByOrgCode(gridCode);
					if(socialInfoBO!=null){
						
						EventDisposal event = new EventDisposal(); 
						event.setEventId(Long.valueOf(result.get("eventId").toString()));
						
						String nextChiefLevel = socialInfoBO.getChiefLevel();
						if (!"4".equals(nextChiefLevel) && bizPlatform.equals("004")) {//对方传的gridCode不是街镇层级 将其删除
							event.setStatus(ConstantValue.STATUS_DEL_AND_RETURN_BACK);
							eventDisposalService.updateEventDisposalById(event);
						}else {//对方传的gridCode为正常街镇编码 更新事件的gridId和gridCode
							MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(gridCode);
							event.setGridId(gridInfo.getGridId());
							event.setGridCode(gridCode);
							eventDisposalService.updateEventDisposalById(event);
						}
						String orgId = "";
						List<UserBO> userList = this.userManageService.
								getUserListByUserExampleParamsOut(null, null, socialInfoBO.getOrgId());
						String staffIds = "";
						if(userList != null && userList.size()>0){
							//盐都燃气云平台上报成功 需要返回网格员的姓名和手机号
							gridAdminUserId = userList.get(0).getUserId();
							for(UserBO userBo : userList){
								staffIds = staffIds + "," + userBo.getUserId();
								orgId = orgId + "," + socialInfoBO.getOrgId();
							}
						}
						if(StringUtils.isNotBlank(staffIds)){
							staffIds = staffIds.substring(1, staffIds.length());
							orgId = orgId.substring(1, orgId.length());
							Long taskId = eventDisposalWorkflowService.curNodeTaskId(Long.valueOf(instanceId));

							int i = Integer.valueOf(nextChiefLevel) - Integer.valueOf(chiefLevel);

							String nextName = "";
							String taskType = "";
							String taskLevel = String.valueOf(Math.abs(i));
							if(i > 0){
								taskType = INodeCodeHandler.OPERATE_SEND;
							}else if(i < 0){
								taskType = INodeCodeHandler.OPERATE_REPORT;
							}else if(i == 0 && !(socialInfoBO.getOrgType().equals("1") && orgType.equals("1"))){//同级 分流
								taskType = INodeCodeHandler.OPERATE_FLOW;
							}
							// 设置当前办理人进行派发事，获取事件当前办理环节信息
							UserInfo userInfo = new UserInfo();
							Map<String, Object> par = eventDisposalWorkflowService.curNodeData(Long.valueOf(instanceId));
							if (CommonFunctions.isNotBlank(par, "WF_ORGID")&& CommonFunctions.isNotBlank(par, "WF_USERID")) {
								String userOrgId = par.get("WF_ORGID").toString().split(",")[0];
								String userCurId = par.get("WF_USERID").toString().split(",")[0];
								userInfo.setUserId(Long.valueOf(userCurId));
								userInfo.setPartyName(par.get("WF_USERNAME").toString().split(",")[0]);
								userInfo.setOrgId(Long.valueOf(userOrgId));
							}
							
							Map<String, Object> initResultMap = eventDisposalWorkflowService.initFlowInfo(String.valueOf(taskId), Long.valueOf(instanceId), userInfo, null);

							if(CommonFunctions.isNotBlank(initResultMap, "taskNodes")) {
								List<Node> taskNodes = (List<Node>) initResultMap.get("taskNodes");
								if(taskNodes != null && taskNodes.size() > 0) {
									for(Node taskNode : taskNodes) {
										if(taskType.equals(taskNode.getType()) && taskLevel.equals(taskNode.getLevel())) {
											nextName = taskNode.getNodeName();
											break;
										}
									}
								}
							}
							try {
								 eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(Long.valueOf(instanceId),String.valueOf(taskId), nextName, staffIds, orgId, "", userInfo);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				params = new HashMap<String, Object>();
				List<XmlTaskResult> tasks = eventResult.getEvent().getTasks();
				if(tasks!=null&&tasks.size() > 0){
					XmlTaskResult task = tasks.get(0);
					params.put("taskId", task.getTaskId());
					params.put("eventId", result.get("eventId"));
					params.put("taskName", task.getTaskName());
					params.put("transactorId", task.getTransactorId());
					params.put("transactOrgId", task.getTransactOrgID());
					params.put("transactOrgName", task.getTransactOrgName());
					params.put("transactorName", task.getTransactorName());
					params.put("taskAdvice", task.getResults());
					params.put("taskCreateTime", task.getCreateTime());
					params.put("taskCreateUserId", task.getCreateUserID());
					params.put("taskCreateUserName", task.getCreateUserName());
					params.put("taskReadTime", task.getReadTime());
					params.put("taskReadUserId", task.getReadUserID());
					params.put("taskReadUserName", task.getReadUserName());
					params.put("taskEndTime", task.getEndTime());
					params.put("taskType", task.getTaskType());
					params.put("optType", task.getOptType());
					params.put("oppoSideParentBizCode", task.getOppoSideParentBizCode());
					params.put("oppoSideParentBizType", task.getOppoSideParentBizType());
					params.put("oppoSideBizCode", task.getOppoSideBizCode());
					params.put("oppoSideBizType", task.getOppoSideBizType());
					params.put("srcPlatform", task.getSrcPlatform());
					eventDisposalWorkflowService.saveOrUpdateTask(params);
				}

				//
				DataExchangeStatus deStatus = new DataExchangeStatus();
				deStatus.setDestPlatform(eventResult.getEvent().getBizPlatform());
				deStatus.setOppoSideBizCode(eventResult.getEvent().getOppoSideBusiCode());
				deStatus.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
				dataExchangeStatusService.isEventTaskFeedback(Long.parseLong(result.get("eventId").toString()), deStatus);
				RESULT_CODE = "1";
				MESSAGE_INFO = "" + result.get("eventId");
				//任务插入中间表（任务）
				DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
				dataExchangeStatus.setDestPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
				dataExchangeStatus.setSrcPlatform(eventResult.getEvent().getBizPlatform());
				dataExchangeStatus.setOppoSideBizCode(eventResult.getEvent().getOppoSideBusiCode());
				dataExchangeStatus.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
				dataExchangeStatus.setOwnSideBizCode(result.get("eventId").toString());
				dataExchangeStatus.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
				dataExchangeStatus.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_UNEXCHANGE);
				dataExchangeStatus.setStatus(IDataExchangeStatusService.STATUS_VALIDATE);
				dataExchangeStatus.setXmlData(xmlData);
				dataExchangeStatusService.saveDataExchangeStatus(dataExchangeStatus);
				//任务插入中间表（接收）
				dataExchangeStatus.setDestPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
				dataExchangeStatus.setSrcPlatform(eventResult.getEvent().getBizPlatform());
				dataExchangeStatus.setOppoSideBizCode(eventResult.getEvent().getOppoSideBusiCode());
				dataExchangeStatus.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_RECEIVE_NEW);
				dataExchangeStatus.setOwnSideBizCode(result.get("eventId").toString());
				dataExchangeStatus.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
				dataExchangeStatus.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_UNEXCHANGE);
				dataExchangeStatus.setStatus(IDataExchangeStatusService.STATUS_VALIDATE);
				dataExchangeStatusService.saveDataExchangeStatus(dataExchangeStatus);
				
				//盐都燃气云平台上报成功 需要返回网格员的姓名和手机号
				if (gridAdminUserId != 0 && bizPlatform.equals("320903001")) {
					StringBuffer message = new StringBuffer();
					UserBO userInfo = userManageService.getUserInfoByUserId(gridAdminUserId);
					message.append("<eventId>");
					message.append(result.get("eventId"));
					message.append("</eventId>");
					if (null != userInfo) {
						message.append("<userName>");
						message.append(userInfo.getPartyName());
						message.append("</userName>");
						message.append("<tel>");
						message.append(userInfo.getVerifyMobile());
						message.append("</tel>");
					}
					MESSAGE_INFO = message.toString();
				}
				
			}else{
				RESULT_CODE = "0";
				MESSAGE_INFO = "任务添加失败！" + result.get("msg") + result.get("eventId") + result.get("instanceId");
			}
		}
		return convertToXmlResult(RESULT_CODE, MESSAGE_INFO);
	}

	/**
	 * 从输入流中获取字节数组
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static  byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}


	/**
	 * 结案
	 * @param xmlData
	 * @return
	 * @throws AxisFault
	 */
	public String closeEvent(String xmlData) throws AxisFault {
		
		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);

		//参数有效性判断
		String checkCloseEvtData = checkCloseEvtData(eventResult);
		if(StringUtils.isNotBlank(checkCloseEvtData)){
			return convertToXmlResult("0", checkCloseEvtData);
		}

		String advice = eventResult.getEvent().getAdvice();
		String eventId  = eventResult.getEvent().getEventId();
		String userName = eventResult.getEvent().getCloserName();
		String orgName = eventResult.getEvent().getCloseOrgName();
		
		//事件状态判断
		EventDisposal eventDisposal = eventDisposalService.findEventByIdSimple(Long.valueOf(eventId));
	    if(null != eventDisposal ){
	    	if (ConstantValue.EVENT_STATUS_END.equals(eventDisposal.getStatus())) {
	    		return convertToXmlResult("0", "【"+eventId+"】事件已结案归档！");//04
	    	}
	    	if (ConstantValue.STATUS_DEL_AND_RETURN_BACK.equals(eventDisposal.getStatus())) {
	    		return convertToXmlResult("0", "【"+eventId+"】事件已被删除！");//06
	    	}
	    	if (ConstantValue.EVENT_STATUS_DRAFT.equals(eventDisposal.getStatus())) {
	    		return convertToXmlResult("0", "【"+eventId+"】事件未启动流程！");//99
	    	}
		}else{
			return convertToXmlResult("0", "【"+eventId+"】事件未找到！");
		}
	    
		String instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(Long.valueOf(eventId));
		if(StringUtils.isBlank(instanceId)){
			return convertToXmlResult("0", "【"+eventId+"】事件未找到！");
		}
		
		//当前办理人
		Long nowOrgId = -1L;
		Long nowUserId = -1L;
		String orgCode = "";
		try{
			Map<String, Object> stringObjectMap = eventDisposalWorkflowService.curNodeData(Long.valueOf(instanceId));
			if(stringObjectMap != null){
				if(stringObjectMap.get("WF_ORGID")!=null){
					String[] ss = new String[100];
					ss = stringObjectMap.get("WF_ORGID").toString().split(",");
					nowOrgId = Long.valueOf(ss[0]);
					OrgSocialInfoBO bo	= orgSocialInfoService.findByOrgId(nowOrgId);
					  if (null != bo) {
						orgCode = bo.getOrgCode();
					  }else {
						  return convertToXmlResult("0", "获取当前环节错误！");
					  }
				}
				if(stringObjectMap.get("WF_USERID")!=null){
					String[] ss = new String[100];
					ss = stringObjectMap.get("WF_USERID").toString().split(",");
					nowUserId = Long.valueOf(ss[0]);
				}
			}
		} catch (Exception e1) {					
			e1.printStackTrace();
		}
		
		//设置当前办理人
		UserInfo userInfo = new UserInfo();
		userInfo.setOrgId(nowOrgId);
		userInfo.setUserId(nowUserId);
		userInfo.setOrgCode(orgCode);
		
		Map<String, Object> initResultMap = eventDisposalWorkflowService.initFlowInfo("", Long.valueOf(instanceId), userInfo, null);
		String taskId = "";
		if (CommonFunctions.isNotBlank(initResultMap, "taskId")) {
			taskId = initResultMap.get("taskId").toString();
		}else {
			return convertToXmlResult("0", "未找到事件ID【"+eventId+"】实例ID【"+instanceId+"】对应的当前任务！") ;
		}

		List<Node> taskNodes = (List<Node>) initResultMap.get("taskNodes");
	
		String nodeName = "";
		if(taskNodes!=null && taskNodes.size()>0) {//下一环节中，有结案环节时，才能结案
			boolean isCloseAble = false;
			for(Node taskNode : taskNodes){
				if(taskNode!=null){
					if("结案".equals(taskNode.getNodeNameZH())
							||"核查评价".equals(taskNode.getNodeNameZH())){
						nodeName = taskNode.getNodeName();
						isCloseAble = true;
						break;
					}
				}
			}
			if(isCloseAble){//有结案环节
				
				if(nowOrgId.equals(-1L) || nowUserId.equals(-1L)){
					return convertToXmlResult("0", "当前环节错误！");
				}
				
				Map<String, Object> params= new HashMap<String, Object>();
				params.put("eventId",eventId);
				params.put("formId",eventId);
				params.put("instanceId", Long.valueOf(instanceId));
	
				boolean result = false;
				try {
					result = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(Long.valueOf(instanceId),taskId, nodeName, "", "", advice, userInfo, "", params);	
				} catch (Exception e1) {					
					e1.printStackTrace();
				}
				if(result){//更改结案环节（市级处理）的结案人姓名、组织名称为对方传参
					 //查找事件流程
					List<Map<String, Object>> hisflow = eventDisposalWorkflowService.queryProInstanceDetail(Long.valueOf(instanceId),
							eventDisposalWorkflowService.SQL_ORDER_ASC);
					
					if (null != hisflow && hisflow.size()>0) {
						Map<String,Object>  closeFlow = new HashMap<String,Object>();
						//根据结案任务名称找到 需要更改的任务id
						for(Map<String, Object> his:hisflow) {
							if ("task5".equals((String)his.get("TASK_CODE"))) {
								 closeFlow = his;
								break;
							}
						}
						if (null != closeFlow && closeFlow.size()>0) {
							Map<String, Object> param = new HashMap<String, Object>();
							param.put("taskId", closeFlow.get("TASK_ID").toString());
							param.put("eventId", eventId);
							param.put("taskName", closeFlow.get("TASK_CODE"));
							param.put("transactorName", userName);//办理人名称
							param.put("transactOrgName", orgName);//办理组织名称
							param.put("taskAdvice", advice);//办理意见
							eventDisposalWorkflowService.saveOrUpdateTask(param);
						}
					 }
				  }else {
					return convertToXmlResult("0", "事件办理错误！");
				}
			}else{
				return convertToXmlResult("0", "没有结案环节！");
			}
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bizPlatform", "004");
		params.put("own", String.valueOf(eventId));
		params.put("xmlData", xmlData);
		params.put("ownSideBizStatus", "1");
		//保存事件对应关系
		this.saveEventDataExchangeStatus(params);
		
		return convertToXmlResult("1", eventId);
		
	}

	/**
	 * 事件同步
	 * @return
	 */
	public String snycEvt(String xmlData) throws AxisFault {
		xmlData = xmlData.replace("&","&amp;");
		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);

		String checkResult = checkStartEvtData(eventResult);
		if(StringUtils.isNotBlank(checkResult)){
			
			return checkResult;
		}

		String bizPlatform = eventResult.getEvent().getBizPlatform();
		
		//type:4 调用snycEvt接口 tasks非必填
		if (!checkBizPlatformByType(bizPlatform, "4")) {
			String checkTaskData = checkTaskData(eventResult);
			if(StringUtils.isNotBlank(checkResult)){
				return checkTaskData;
			}
		}
		
		String startCloseEvt = this.startCloseEvt(xmlData);//事件采集并结案
		
		String advice = eventResult.getEvent().getAdvice();

		if(StringUtils.isNotBlank(startCloseEvt)){
			String code = startCloseEvt.substring(startCloseEvt.indexOf("<code>") + 6, startCloseEvt.indexOf("</code>"));
			if(StringUtils.isNotBlank(code) && "1".equals(code)){
				if(startCloseEvt.indexOf("<message>") > 0){
					String eventId = startCloseEvt.substring(startCloseEvt.indexOf("<message>") + 9, startCloseEvt.indexOf("</message>"));
					this.flowTask(eventResult,eventId);//节点信息同步
					
					//type:5 调用snycEvt接口 不生成评价信息
					if (!checkBizPlatformByType(bizPlatform, "5")) {
						Map<String, Object> eventMap = new HashMap<String, Object>();
						eventMap.put("evaLevel", "02");
						eventMap.put("evaContent", "满意");
						eventMap.put("eventId", eventId);
						eventMap.put("advice", advice);
						eventMap.put("isEvaluate", true);
						eventDisposalDockingService.subWorkflow(eventMap);
					}
				}
			}
		}
		return startCloseEvt;
	}

	private String flowTask(XmlDataResult xmlDataResult, String eventId){
		String result = "";
		if(null!=xmlDataResult && null!=xmlDataResult.getEvent().getTasks()){
			for(XmlTaskResult task : xmlDataResult.getEvent().getTasks()){
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("taskId", task.getTaskId());
				params.put("eventId", eventId);
				params.put("taskName", task.getTaskName());
				params.put("transactorId", task.getTransactorId());
				params.put("transactOrgId", task.getTransactOrgID());
				params.put("transactOrgName", task.getTransactOrgName());
				params.put("transactorName", task.getTransactorName());
				params.put("taskAdvice", task.getResults());
				params.put("taskCreateTime", task.getCreateTime());
				params.put("taskCreateUserId", task.getCreateUserID());
				params.put("taskCreateUserName", task.getCreateUserName());
				params.put("taskReadTime", task.getCreateTime());
				params.put("taskReadUserId", task.getReadUserID());
				params.put("taskReadUserName", task.getReadUserName());
				params.put("taskEndTime", task.getCreateTime());
				params.put("taskType", task.getTaskType());
				params.put("optType", task.getOptType());
				params.put("oppoSideParentBizCode", task.getOppoSideParentBizCode());
				params.put("oppoSideParentBizType", task.getOppoSideParentBizType());
				params.put("oppoSideBizCode", task.getOppoSideBizCode());
				params.put("oppoSideBizType", task.getOppoSideBizType());
				params.put("srcPlatform", task.getSrcPlatform());
				Long record = eventDisposalWorkflowService.saveOrUpdateTask(params);
				result = result + "," + record;
			}
		}
		if(result.length() > 0){
			result = result.substring(1, result.length());
		}
		return result;
	}

	/**
	 *
	 * 事件任务过程同步
	 * @param xmlData
	 * @return
	 * @throws AxisFault
	 */
	public String flowTask(String xmlData) throws AxisFault {

		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		XmlDataResult eventResult =jaxbBinder.fromXml(xmlData);

		String checkResult = checkFlowTaskData(eventResult);
		if(StringUtils.isNotBlank(checkResult)){
			return checkResult;
		}
			StringBuffer resultData = new StringBuffer();
			for(XmlTaskResult task : eventResult.getTasks()){
				logger.debug("task.getSrcPlatform()->"+task.getSrcPlatform());
				if(task.getSrcPlatform().equals("004")){
					return handlingTask(xmlData);
				}
				String oppoSideBizCode = task.getOppoSideBizCode().toString();
				String bizPlatForm = task.getSrcPlatform();
				DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
				dataExchangeStatus.setOppoSideBizCode(oppoSideBizCode);
				dataExchangeStatus.setSrcPlatform(bizPlatForm);
				List<DataExchangeStatus>  dataExchangeStatuss = dataExchangeStatusService.findDataExchange(dataExchangeStatus);
				if(dataExchangeStatuss.size() > 0){
					RESULT_CODE = "0";
					MESSAGE_INFO = "任务已存在！";
					continue;
				}

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("taskId", task.getTaskId());
				params.put("eventId", task.getEventId());
				params.put("taskName", task.getTaskName());
				params.put("transactorId", task.getTransactorId());
				params.put("transactOrgId", task.getTransactOrgID());
				params.put("transactOrgName", task.getTransactOrgName());
				params.put("transactorName", task.getTransactorName());
				params.put("taskAdvice", task.getResults());
				params.put("taskCreateTime", task.getCreateTime());
				params.put("taskCreateUserId", task.getCreateUserID());
				params.put("taskCreateUserName", task.getCreateUserName());
				params.put("taskReadTime", task.getCreateTime());
				params.put("taskReadUserId", task.getReadUserID());
				params.put("taskReadUserName", task.getReadUserName());
				params.put("taskEndTime", task.getCreateTime());
				params.put("taskType", task.getTaskType());
				params.put("optType", task.getOptType());
				params.put("oppoSideParentBizCode", task.getOppoSideParentBizCode());
				params.put("oppoSideParentBizType", task.getOppoSideParentBizType());
				params.put("oppoSideBizCode", task.getOppoSideBizCode());
				params.put("oppoSideBizType", task.getOppoSideBizType());
				params.put("srcPlatform", task.getSrcPlatform());
				if(task.getSrcPlatform().equals("009")){//安海
					String instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(Long.valueOf(task.getEventId()));
					if(StringUtils.isBlank(instanceId)){
						RESULT_CODE = "0";
						MESSAGE_INFO = "任务添加失败！流程不存在！";
						return convertToXmlResult(RESULT_CODE, MESSAGE_INFO);
					}
					Long taskId = eventDisposalWorkflowService.curNodeTaskId(Long.valueOf(instanceId));
					if(null==taskId || taskId < 1){
						RESULT_CODE = "0";
						MESSAGE_INFO = "任务添加失败！任务不存在！";
						return convertToXmlResult(RESULT_CODE, MESSAGE_INFO);
					}
					String nextNode = "村(社区)处理";

					OrgSocialInfoBO orgSocialInfo = orgSocialInfoService.findByOrgId(Long.valueOf(task.getTransactOrgID()));
					if(null == orgSocialInfo){
						RESULT_CODE = "0";
						MESSAGE_INFO = "任务添加失败！组织不存在！";
						return convertToXmlResult(RESULT_CODE, MESSAGE_INFO);
					}
					UserInfo userInfo = userService.getUserExtraInfoByUserId(30012745L, "350582101");
					if(null == userInfo){
						RESULT_CODE = "0";
						MESSAGE_INFO = "任务添加失败！用户不存在！";
						return convertToXmlResult(RESULT_CODE, MESSAGE_INFO);
					}
					String userId = "";
					String org = "";
					if(task.getTransactorId().equals("") || task.getTransactorId() == null){
						List<UserInfo> users = userService.getSubUsersByOrgId(Long.valueOf(task.getTransactOrgID()));
						String u = "";
						String o = "";
						for(UserInfo user : users){
							u = u + "," + user.getUserId();
							o = o + "," + orgSocialInfo.getOrgId();
						}
						if(!"".equals(u)){
							u = u.substring(1,u.length());
							o = o.substring(1,o.length());
						}
						userId = u;
						org = o;
					}else{
						userId = task.getTransactorId();
						org = task.getTransactOrgID();
					}
					Boolean result = false;
					try {
						result = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(Long.valueOf(instanceId),String.valueOf(taskId),
								nextNode, userId, org, task.getResults(), userInfo, "", null);
					} catch (Exception e) {
						e.printStackTrace();
					}

					if(!result){
						RESULT_CODE = "0";
						MESSAGE_INFO = "任务添加失败！";
						break;
					}else{
						Long resultTaskId = eventDisposalWorkflowService.curNodeTaskId(Long.valueOf(instanceId));
						RESULT_CODE = "1";
						MESSAGE_INFO = "" + resultTaskId;
						DataExchangeStatus dataExchange = new DataExchangeStatus();
						dataExchange.setSrcPlatform("000");
						dataExchange.setDestPlatform(task.getSrcPlatform());
						dataExchange.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
						dataExchange.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
						dataExchange.setOwnSideBizCode(String.valueOf(resultTaskId));
						dataExchange.setOppoSideBizCode(task.getOppoSideBizCode());
						dataExchange.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
						if(StringUtils.isNotBlank(xmlData)){
							xmlData=this.subRangeString(xmlData.replace(" ", ""), "<base64>","</base64>");//去除base64
							xmlData=this.subRangeString(xmlData.replace(" ", ""), "<attrBASE64>","</attrBASE64>");//去除attrBASE64
							xmlData=this.subRangeString(xmlData.replace(" ", ""), "<attrBase64>","</attrBase64>");//去除attrBase64
				        	}
					   //为了防止base64未过滤，导致字段长度过大
					   if (!CommonFunctions.isLengthValidate(xmlData, 1500)) {
						   xmlData = xmlData.substring(0,1500);
					   }
					   dataExchange.setXmlData(xmlData);
					   dataExchangeStatusService.saveDataExchangeStatus(dataExchange);
					}
				}else{
					Long record = eventDisposalWorkflowService.saveOrUpdateTask(params);
					if(record<1){
						RESULT_CODE = "0";
						MESSAGE_INFO = "任务添加失败！";
						continue;
					}else{
						RESULT_CODE = "1";
						MESSAGE_INFO = "" + record;
						resultData.append(record + "," + task.getOppoSideBizCode() + "|");
						//
						DataExchangeStatus dataExchange = new DataExchangeStatus();
						dataExchange.setSrcPlatform(task.getSrcPlatform());
						dataExchange.setDestPlatform("000");
						dataExchange.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
						dataExchange.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
						dataExchange.setOwnSideBizCode(String.valueOf(record));
						dataExchange.setOppoSideBizCode(task.getOppoSideBizCode());
						dataExchange.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
						if(StringUtils.isNotBlank(xmlData)){
							xmlData=this.subRangeString(xmlData.replace(" ", ""), "<base64>","</base64>");//去除base64
							xmlData=this.subRangeString(xmlData.replace(" ", ""), "<attrBASE64>","</attrBASE64>");//去除attrBASE64
							xmlData=this.subRangeString(xmlData.replace(" ", ""), "<attrBase64>","</attrBase64>");//去除attrBase64
				        	}
						//为了防止base64未过滤，导致字段长度过大
						if (!CommonFunctions.isLengthValidate(xmlData, 1500)) {
							xmlData = xmlData.substring(0, 1500);
						}
						dataExchange.setXmlData(xmlData);
						dataExchangeStatusService.saveDataExchangeStatus(dataExchange);
						//保存附件
						this.saveAttrs(task.getAttrs(),Long.valueOf(task.getEventId()),ConstantValue.EVENT_SEQ_1);
					}
				}
			}
			if(eventResult.getTasks().size() > 1){
				MESSAGE_INFO = resultData.substring(0, resultData.length() - 1);
			}
		
		return convertToXmlResult(RESULT_CODE, MESSAGE_INFO);
	}


	/**
	 *  处理中
	 * @param xmlData
	 * @return
	 */
	public String handlingTask(String xmlData){
		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		XmlDataResult xmlDataResult =jaxbBinder.fromXml(xmlData);
	
		String checkResult = checkFlowTaskData(xmlDataResult);
		if(StringUtils.isNotBlank(checkResult)){
			return checkResult;
		}
			StringBuffer resultData = new StringBuffer();
			for(XmlTaskResult task : xmlDataResult.getTasks()){
				String oppoSideBizCode = task.getOppoSideBizCode().toString();
//				if(checkExchangeStatus(oppoSideBizCode)) continue;

				String ownSideBizCode = "";
				if(task.getSrcPlatform().equals("004")){//迪艾斯ID转换
					DataExchangeStatus dataExchangeStatusS = new DataExchangeStatus();
					dataExchangeStatusS.setOppoSideBizCode(oppoSideBizCode);
//					dataExchangeStatusS.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
					dataExchangeStatusS.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
					List<DataExchangeStatus> dataExchanges = dataExchangeStatusService.findDataExchange(dataExchangeStatusS);

					if(dataExchanges.size() < 1){
						ownSideBizCode = task.getEventId();
					}else{
						ownSideBizCode = dataExchanges.get(0).getOwnSideBizCode();
					}
				}else{
					ownSideBizCode = task.getEventId();
				}

				//保存中间表
				DataExchangeStatus dataExchangeStatusBef = new DataExchangeStatus();
				dataExchangeStatusBef.setDestPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
				dataExchangeStatusBef.setSrcPlatform(task.getSrcPlatform());
				dataExchangeStatusBef.setOppoSideBizCode(oppoSideBizCode);
				dataExchangeStatusBef.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
				dataExchangeStatusBef.setOwnSideBizCode(ownSideBizCode);
				dataExchangeStatusBef.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
				dataExchangeStatusBef.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
				dataExchangeStatusBef.setStatus(IDataExchangeStatusService.STATUS_VALIDATE);
				dataExchangeStatusBef.setXmlData(xmlData);
				dataExchangeStatusService.saveDataExchangeStatus(dataExchangeStatusBef);
				//处理中
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("taskId", task.getTaskId());
				params.put("eventId", ownSideBizCode);
				params.put("taskName", task.getTaskName());
				params.put("transactorId", task.getTransactorId());
				params.put("transactOrgId", task.getTransactOrgID());
				params.put("transactOrgName", task.getTransactOrgName());
				params.put("transactorName", task.getCreateUserName());
				params.put("taskAdvice", task.getResults());
				params.put("taskCreateTime", task.getCreateTime());
				params.put("taskCreateUserId", task.getCreateUserID());
				params.put("taskCreateUserName", task.getCreateUserName());
				params.put("taskReadTime", task.getCreateTime());
				params.put("taskReadUserId", task.getReadUserID());
				params.put("taskReadUserName", task.getReadUserName());
				params.put("taskEndTime", task.getCreateTime());
				params.put("taskType", task.getTaskType());
				params.put("optType", task.getOptType());
				params.put("oppoSideParentBizCode", task.getOppoSideParentBizCode());
				params.put("oppoSideParentBizType", task.getOppoSideParentBizType());
				params.put("oppoSideBizCode", task.getOppoSideBizCode());
				params.put("oppoSideBizType", task.getOppoSideBizType());
				params.put("srcPlatform", task.getSrcPlatform());

				if(StringUtils.isBlank(task.getTransactorId())&&StringUtils.isBlank(task.getTransactOrgID())){
					params.put("type", ConstantValue.HANDLING_TASK_TYPE);
					Long instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(Long.valueOf(ownSideBizCode));
					Long taskId = eventDisposalWorkflowService.curNodeTaskId(instanceId);
					params.put("instanceId", instanceId);
					params.put("taskId", taskId);
				}
				Long record = eventDisposalWorkflowService.saveOrUpdateTask(params);
				if(record<1){
					RESULT_CODE = "0";
					MESSAGE_INFO = "failed!";
					continue;
				}else{
					RESULT_CODE = "1";
					MESSAGE_INFO = "" + record;
					resultData.append(record + "," + task.getOppoSideBizCode() + "|");
					//
					DataExchangeStatus dataExchange = new DataExchangeStatus();
					dataExchange.setSrcPlatform(task.getSrcPlatform());
					dataExchange.setDestPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
					dataExchange.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
					dataExchange.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
					dataExchange.setOwnSideBizCode(String.valueOf(record));
					dataExchange.setOppoSideBizCode(task.getOppoSideBizCode());
					dataExchange.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
					dataExchange.setXmlData(xmlData);
					dataExchangeStatusService.saveDataExchangeStatus(dataExchange);
				}
				if(xmlDataResult.getTasks().size() > 1){
					MESSAGE_INFO = resultData.substring(0, resultData.length() - 1);
				}
			}
		
		return convertToXmlResult(RESULT_CODE, MESSAGE_INFO);
	}


	private byte[] fileToBytes(String filePath) throws FileNotFoundException,
			IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				filePath));
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		byte[] temp = new byte[1024];
		int size = 0;
		while ((size = in.read(temp)) != -1) {
			out.write(temp, 0, size);
		}
		in.close();
		byte[] pictureByte = out.toByteArray();
		return pictureByte;
	}

	private Boolean isCurrentUser(String taskId){
		//获取当前办理人信息
		List<Map<String, Object>> taskPerson = eventDisposalWorkflowService.queryMyTaskParticipation(taskId);
		StringBuffer taskPersonStr = null;
		if(taskPerson!=null && taskPerson.size()>0){
			taskPersonStr = new StringBuffer("");
			boolean isCurrentUser = false;//判断当前登录人员是否在为当前办理人

			for(Map<String, Object> mapTemp : taskPerson){
				Object userTypeObj = mapTemp.get("USER_TYPE");//1表示USER_ID为组织ID；3表示USER_ID为用户ID
				Object orgNameObj = mapTemp.get("ORG_NAME");

				if(CommonFunctions.isNotBlank(mapTemp, "USER_NAME")){
					taskPersonStr.append(mapTemp.get("USER_NAME"));
					if(orgNameObj != null){
						taskPersonStr.append("(").append(orgNameObj).append(");");
					}
				}else if(orgNameObj != null){
					taskPersonStr.append(orgNameObj).append(";");
				}
				if(CommonFunctions.isNotBlank(mapTemp, "USER_ID") && !isCurrentUser){
					Long userId = Long.valueOf(mapTemp.get("USER_ID").toString());

				}
			}

			if(!isCurrentUser){//当前登录人员未在当前办理人中时，不可办理该事件
				return true;
			}
			return false;
		}
		return false;
	}
	
	
	private boolean checkParamIsRequired(String bizPlatform,String type) {
		//对接平台标识可查看枚举类：BizPlatfromEnum
		if (StringUtils.isNotBlank(bizPlatform)) {
			if ("1".equals(type)) {
				String checkOrgCode[] = {""};
				for (String a:checkOrgCode) {
					if (bizPlatform.equals(a)) {
							return false;//orgCode非必填
						}
					}
			}
			if ("2".equals(type)) {
				String checkLongitude[] = {"350701005"};
				for (String b:checkLongitude) {
					if (bizPlatform.equals(b)) {
							return false;//经纬度非必填
						}
					}
			}
			if ("3".equals(type)) {
				String checkTel[] = {"078","5403001"};
				for (String b:checkTel) {
					if (bizPlatform.equals(b)) {
							return false;//联系人跟电话非必填
						}
					}
			}
		}
		return true;//参数必填
	}

	private Boolean checkUserInfo(XmlDataResult eventResult){
		Boolean result = false;
		if(null == eventResult.getAuth()) {MESSAGE_INFO = "顾客信息[auth]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getAuth().getUsername())) {MESSAGE_INFO = "顾客名称[username]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getAuth().getPassword())) {MESSAGE_INFO = "顾客密码[password]无效";RESULT_CODE="0"; return result;}
		if(!checkUserPwd(eventResult.getAuth().getUsername(), eventResult.getAuth().getPassword())){MESSAGE_INFO = "顾客[username][password]验证失败";RESULT_CODE="0"; return result;}
		if(null == eventResult.getOrgCode()) {MESSAGE_INFO = "组织编码[orgCode]无效";RESULT_CODE="0"; return result;}
		return true;
	}

	private Boolean checkSyncTask(XmlDataResult eventResult){
		Boolean result = false;
		if(null == eventResult.getAuth()) {MESSAGE_INFO = "顾客信息[auth]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getAuth().getUsername())) {MESSAGE_INFO = "顾客名称[username]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getAuth().getPassword())) {MESSAGE_INFO = "顾客密码[password]无效";RESULT_CODE="0"; return result;}
		if(!checkUserPwd(eventResult.getAuth().getUsername(), eventResult.getAuth().getPassword())){MESSAGE_INFO = "顾客[username][password]验证失败";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getEventId())) {MESSAGE_INFO = "组织编码[eventId]无效";RESULT_CODE="0"; return result;}
		return true;
	}

	private Boolean checkEvaluate(XmlDataResult eventResult){
		Boolean result = false;
		if(null == eventResult.getAuth()) {MESSAGE_INFO = "顾客信息[auth]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getAuth().getUsername())) {MESSAGE_INFO = "顾客名称[username]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getAuth().getPassword())) {MESSAGE_INFO = "顾客密码[password]无效";RESULT_CODE="0"; return result;}
		if(!checkUserPwd(eventResult.getAuth().getUsername(), eventResult.getAuth().getPassword())){MESSAGE_INFO = "顾客[username][password]验证失败";RESULT_CODE="0"; return result;}
		if(null == eventResult.getEvaluate()) {MESSAGE_INFO = "评价信息[Evaluate]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getEvaluate().getEventId())) {MESSAGE_INFO = "事件标识[eventId]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getEvaluate().getEvalContent())) {MESSAGE_INFO = "评价内容[evalContent]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getEvaluate().getEvalLevel())) {MESSAGE_INFO = "评价等级[evalLevel]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getEvaluate().getEvalDate())) {MESSAGE_INFO = "评价时间[evalDate]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getEvaluate().getUserName())) {MESSAGE_INFO = "用户姓名[userName]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getEvaluate().getOrgName())) {MESSAGE_INFO = "组织名称[orgName]无效";RESULT_CODE="0"; return result;}
		return true;
	}

	private String checkStartEvtData(XmlDataResult eventResult){
		String bizPlatform = eventResult.getEvent().getBizPlatform();
		if(null == eventResult.getAuth())
		    return convertToXmlResult("0", "认证信息[auth]无效");
		if(StringUtils.isBlank(eventResult.getAuth().getUsername()))
		    return convertToXmlResult("0", "用户名[username]无效");
		if(StringUtils.isBlank(eventResult.getAuth().getPassword()))
		    return convertToXmlResult("0", "密码[password]无效");
		if(!checkUserPwd(eventResult.getAuth().getUsername(), eventResult.getAuth().getPassword()))
		    return convertToXmlResult("0", "用户[username][password]验证失败");
		if(null == eventResult.getEvent())
		    return convertToXmlResult("0", "事件信息[event]无效");
		if(StringUtils.isBlank(bizPlatform))
		    return convertToXmlResult("0", "来源平台[bizPlatform]无效");
		if(StringUtils.isNotBlank(eventResult.getEvent().getContactTel())) {
			if(!StringUtils.isNumeric(eventResult.getEvent().getContactTel()))
				return convertToXmlResult("0","联系人电话[contactTel]不是整型");
		}
        if(StringUtils.isNotBlank(eventResult.getEvent().getEventName())) {
        	String eventName=this.StringFilter(eventResult.getEvent().getEventName());
            if (!CommonFunctions.isLengthValidate(eventName, 100)) {
            	return convertToXmlResult("0","事件标题[eventName]超出长度");
			}
        }
        if(!checkBizPlatformByType(bizPlatform, "2")) {//根据来源平台过滤，是否校验事件类型
			 if(StringUtils.isBlank(eventResult.getEvent().getEventType()))
					return convertToXmlResult("0","事件类型[eventType]无效");
		}
        if(checkBizPlatformByType(bizPlatform, "3")) {//根据来源平台过滤，是否校验采集人Id
        	if(StringUtils.isBlank(eventResult.getEvent().getCreatorID()))
			    return convertToXmlResult("0","事件采集人Id[creatorID]无效");
		}
		if(StringUtils.isBlank(eventResult.getEvent().getGridCode()))
			return convertToXmlResult("0","网格编码[gridCode]无效");
		if(StringUtils.isBlank(eventResult.getEvent().getHappenTimeStr()))
			return convertToXmlResult("0","发生时间[happenTimeStr]无效");
		if(StringUtils.isBlank(eventResult.getEvent().getOccurred()))
			return convertToXmlResult("0","事件发生地址[occurred]无效");
		if(StringUtils.isBlank(eventResult.getEvent().getContent()))
			return convertToXmlResult("0","事件内容[content]无效");
		if(StringUtils.isBlank(eventResult.getEvent().getOppoSideBusiCode()))
			return convertToXmlResult("0","第三方事件标识[oppoSideBusiCode]无效");
		
		if(StringUtils.isBlank(eventResult.getEvent().getRegisterTimeStr()))
			return convertToXmlResult("0","采集时间[registerTimeStr]无效");
		if(StringUtils.isBlank(eventResult.getEvent().getAdvice()))
			return convertToXmlResult("0","处理意见[advice]无效");
		// 校验附件名称是否为文件名称
		if (null != eventResult.getEvent().getAttrs() && eventResult.getEvent().getAttrs().size() > 0) {
			String result = checkAttrName(eventResult.getEvent().getAttrs());
			if (StringUtils.isNotBlank(result)) {
				return result;
			}
		}
		// 校验涉及人员参数
		if (null != eventResult.getEvent().getPeoples() && eventResult.getEvent().getPeoples().size() > 0) {
			String result = checkInvolvedPeople(eventResult.getEvent().getPeoples());
			if (StringUtils.isNotBlank(result)) {
				return result;
			}
		}
		if(StringUtils.isNotBlank(eventResult.getEvent().getRegisterTimeStr())){
			String registerTimeStr = eventResult.getEvent().getRegisterTimeStr();
			Date date = DateUtils.formatDate(registerTimeStr, DateUtils.PATTERN_24TIME);
			if(date==null)
				return convertToXmlResult("0","采集时间[registerTimeStr]格式不正确");
		}
		if(StringUtils.isNotBlank(eventResult.getEvent().getHappenTimeStr())){
			String happenTimeStr = eventResult.getEvent().getHappenTimeStr();
			Date date = DateUtils.formatDate(happenTimeStr, DateUtils.PATTERN_24TIME);
			if(date==null)
				return convertToXmlResult("0","事件发生时间[happenTimeStr]格式不正确");
		}
		
		//去除特殊字符
		String occurred=this.StringFilter(eventResult.getEvent().getOccurred());
		String content=this.StringFilter(eventResult.getEvent().getContent());
		//验证长度
		if (!CommonFunctions.isLengthValidate(occurred, 256)) {
			return convertToXmlResult("0","事件发生地址[occurred]超出长度");
		}
		if (!CommonFunctions.isLengthValidate(content, 4000)) {
			return convertToXmlResult("0","事件描述[content]超出长度");
		}
		return "";
	}

	//根据来源平台、过滤类型，接口个性化
	private boolean checkBizPlatformByType(String bizPlatform,String type) {
			//对接平台标识可查看枚举类：BizPlatfromEnum
//			if ("1".equals(type)) {//type:1 过滤事件标题
//				String envir[] = {"350582001"};
//				for (String s:envir) {
//					if (bizPlatform.equals(s)) {
//						return true;
//					}
//				}
//			}
			if ("2".equals(type)) {//type:2 过滤事件类型
				String envir[] = {"350582001"};
				for (String s:envir) {
					if (bizPlatform.equals(s)) {
						return true;
					}
				}
			}else if ("3".equals(type)) {//type:3 过滤采集人Id
				String envir[] = {"350582001"};
				for (String s:envir) {
					if (bizPlatform.equals(s)) {
						return true;
					}
				}
			}else if ("4".equals(type)) {//type:4 调用snycEvt接口 tasks非必填
				String envir[] = {"3607001"};
				for (String s:envir) {
					if (bizPlatform.equals(s)) {
						return true;
					}
				}
			}else if ("5".equals(type)) {//type:5 调用snycEvt接口 不生成评价信息
				String envir[] = {"3607001"};
				for (String s:envir) {
					if (bizPlatform.equals(s)) {
						return true;
					}
				}
			}
			
			return false;
		}
		
	public String checkAttrName(List<XmlAttr> attrs) {
		for (XmlAttr attr:attrs) {
			if (StringUtils.isNotBlank(attr.getAttrName())) {
				String getFileLength = FileUtils.getFileExtension(attr.getAttrName());
				if (StringUtils.isNotBlank(getFileLength) && getFileLength.length()>1) {
					continue;//表示文件名称如：图片1.jpg
				}else {
					return convertToXmlResult("0","附件名["+attr.getAttrName()+"]无效，应如：图片1.jpg");
				}
			}else {
				return convertToXmlResult("0","附件名["+attr.getAttrName()+"]为空");
			}
		}
		return "";
	}
	
	/**
	 * 校验涉及人员参数
	 * @author wuxq
	 */
	public String checkInvolvedPeople(List<XmlPeople> peoples) {
		List<String> idCardList = new ArrayList<String>();
		for (XmlPeople people:peoples) {
			if (StringUtils.isBlank(people.getCardType())) {
				return convertToXmlResult("0","证件类型[cardType]为空！");
			}else {//证件类型(D030001)，001：居民身份证；002：台胞证；003：护照；009：港澳通行证；
				if (!StringUtils.isNumeric(people.getCardType())) {
					return convertToXmlResult("0","证件类型[cardType]不是整型！");
				}
			}
			if (StringUtils.isBlank(people.getIdCard())) {
				return convertToXmlResult("0","证件号码[idCard]为空！");
			}else {
				if (!IdentityCardRule2.isLegalPattern(people.getIdCard())) {
					return convertToXmlResult("0","证件号码["+people.getIdCard()+"]不合法！");
				}
			}
			if (StringUtils.isBlank(people.getName())) {
				return convertToXmlResult("0","姓名[name]为空！");
			}
			if (idCardList.contains(people.getIdCard())) {
				return convertToXmlResult("0","证件号码["+people.getIdCard()+"]重复！");
			}else {
				idCardList.add(people.getIdCard());
			}
		}
		return "";
	}
	
	public String checkRejectEvtData(XmlDataResult eventResult){
		if (StringUtils.isNotBlank(checkEvtData(eventResult))) {
			return checkEvtData(eventResult);
		}
		if(StringUtils.isBlank(eventResult.getEvent().getAdvice()))
			return "处理意见[advice]无效";
		
		return "";
	}
	
	public String checkEvtData(XmlDataResult eventResult){
		if(null == eventResult.getAuth())
			return "顾客信息[auth]无效";
		if(StringUtils.isBlank(eventResult.getAuth().getUsername()))
			return "顾客名称[username]无效";
		if(StringUtils.isBlank(eventResult.getAuth().getPassword()))
			return "顾客密码[password]无效";
		if(null == eventResult.getEvent())
			return "事件信息[event]无效";
		if(StringUtils.isBlank(eventResult.getEvent().getEventId()))
			return "事件ID[eventId]无效";
		return "";
	}
	

	private Boolean checkShuntEventData(XmlDataResult eventResult){
		Boolean result = false;
		if(null == eventResult.getAuth()) {MESSAGE_INFO = "顾客信息[auth]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getAuth().getUsername())) {MESSAGE_INFO = "顾客名称[username]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getAuth().getPassword())) {MESSAGE_INFO = "顾客密码[password]无效";RESULT_CODE="0"; return result;}
		if(!checkUserPwd(eventResult.getAuth().getUsername(), eventResult.getAuth().getPassword())){MESSAGE_INFO = "顾客[username][password]验证失败";RESULT_CODE="0"; return result;}
		if(null == eventResult.getEvent()) {MESSAGE_INFO = "事件信息[event]无效";RESULT_CODE="0"; return result;}
//		if(StringUtils.isBlank(eventResult.getEvent().getEventName())) {MESSAGE_INFO = "事件标题[eventName]无效"; return result;}
//		if(StringUtils.isBlank(eventResult.getEvent().getGridCode())) {MESSAGE_INFO = "网格编码[gridCode]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getEvent().getHappenTimeStr())) {MESSAGE_INFO = "网格编码[happenTimeStr]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getEvent().getOccurred())) {MESSAGE_INFO = "网格编码[happenTimeStr]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getEvent().getContent())) {MESSAGE_INFO = "网格编码[happenTimeStr]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getEvent().getOppoSideBusiCode())) {MESSAGE_INFO = "网格编码[happenTimeStr]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getEvent().getBizPlatform())) {MESSAGE_INFO = "网格编码[happenTimeStr]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getEvent().getEventType())) {MESSAGE_INFO = "事件类型[eventType]无效";RESULT_CODE="0"; return result;}
//		if(StringUtils.isBlank(eventResult.getEvent().getUrgency())) {MESSAGE_INFO = "紧急程度[urgency]无效";RESULT_CODE="0"; return result;}
//		if(StringUtils.isBlank(eventResult.getEvent().getInfluence())) {MESSAGE_INFO = "影响范围[influence]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getEvent().getRegisterTimeStr())) {MESSAGE_INFO = "采集时间[registerTimeStr]无效";RESULT_CODE="0"; return result;}
		if(StringUtils.isBlank(eventResult.getEvent().getAdvice())) {MESSAGE_INFO = "处理意见[advice]无效";RESULT_CODE="0"; return result;}
//		if(null == eventResult.getEvent().getTasks()) {MESSAGE_INFO = "流程信息[tasks]无效";RESULT_CODE="0"; return result;}
//		if(eventResult.getEvent().getTasks().size() < 1) {MESSAGE_INFO = "流程信息[task]无效";RESULT_CODE="0"; return result;}
//		if(StringUtils.isBlank(eventResult.getEvent().getTasks().get(0).getNextOrgCode())) {MESSAGE_INFO = "下一环节办理人员所属组织编码[nextOrgCode]无效";RESULT_CODE="0"; return result;}
//		if(StringUtils.isBlank(eventResult.getEvent().getTasks().get(0).getOppoSideBusiCode())) {MESSAGE_INFO = "第三方任务标识[oppoSideBusiCode]无效";RESULT_CODE="0"; return result;}
//		if(StringUtils.isBlank(eventResult.getEvent().getTasks().get(0).getTaskName())) {MESSAGE_INFO = "任务名称[taskName]无效";RESULT_CODE="0"; return result;}
//		if(StringUtils.isBlank(eventResult.getEvent().getTasks().get(0).getOptType())) {MESSAGE_INFO = "操作类型[optType]无效";RESULT_CODE="0"; return result;}
//		if(StringUtils.isBlank(eventResult.getEvent().getTasks().get(0).getOppoSideBizType())) {MESSAGE_INFO = "第三方业务类型[oppoSideBizCode]无效";RESULT_CODE="0"; return result;}
//		if(StringUtils.isBlank(eventResult.getEvent().getTasks().get(0).getSrcPlatform())) {MESSAGE_INFO = "来源平台[srcPlatform]无效";RESULT_CODE="0"; return result;}

		return true;
	}

	private String checkCloseEvtData(XmlDataResult eventResult){
		if(null == eventResult.getAuth())
			return "顾客信息[auth]无效";
		if(StringUtils.isBlank(eventResult.getAuth().getUsername()))
			return "顾客名称[username]无效";
		if(StringUtils.isBlank(eventResult.getAuth().getPassword()))
			return "顾客密码[password]无效";
		if(!checkUserPwd(eventResult.getAuth().getUsername(), eventResult.getAuth().getPassword()))
			return "顾客[username][password]验证失败";
		if(null == eventResult.getEvent())
			return "事件信息[event]无效";
		if(StringUtils.isBlank(eventResult.getEvent().getEventId()))
			return "事件ID[eventId]无效";
		if(StringUtils.isBlank(eventResult.getEvent().getCloseDate()))
			return "事件结案时间[closeDate]无效";
		if(StringUtils.isBlank(eventResult.getEvent().getAdvice()))
			return "事件处理意见[advice]无效";
		if(StringUtils.isBlank(eventResult.getEvent().getCloseOrgName()))
			return "结案人所属组织名称[closeOrgName]无效";
		if (!eventResult.getAuth().getUsername().equals("jjbmfwzx")) {
			if(StringUtils.isBlank(eventResult.getEvent().getBizPlatform()))
				return "来源平台[bizPlatform]无效";
		}
	
		//校验附件名称是否为文件名称
		if (null != eventResult.getEvent().getAttrs() && eventResult.getEvent().getAttrs().size()>0) {
			String result = checkAttrName(eventResult.getEvent().getAttrs());
		   if (StringUtils.isNotBlank(result)) {
			 return result;
		     }
		}
		return "";
	}


	private String checkTaskData(XmlDataResult eventResult){
		if(null == eventResult.getAuth())
			return "顾客信息[auth]无效";
		if(StringUtils.isBlank(eventResult.getAuth().getUsername()))
			return "顾客名称[username]无效";
		if(StringUtils.isBlank(eventResult.getAuth().getPassword()))
			return "顾客密码[password]无效";
		if(!checkUserPwd(eventResult.getAuth().getUsername(), eventResult.getAuth().getPassword()))
			return "顾客[username][password]验证失败";
		if(null == eventResult.getTasks())
			return "事件信息[tasks]无效";
		if(eventResult.getTasks().size() < 1)
			return "事件信息[task]无效";
//		if(StringUtils.isBlank(eventResult.getTasks().get(0).getEventId()))
//			return "我方事件ID[eventId]无效";
		if(StringUtils.isBlank(eventResult.getTasks().get(0).getTransactOrgName()))
			return "办理组织名称[transactOrgName]无效";
		if(StringUtils.isBlank(eventResult.getTasks().get(0).getResults()))
			return "处理结果[results]无效";
		if(StringUtils.isBlank(eventResult.getTasks().get(0).getTransactorName()))
			return "任务创建用户姓名[transactorName]无效";
		if(StringUtils.isBlank(eventResult.getTasks().get(0).getTaskName()))
			return "任务名称[taskName]无效";
		if(StringUtils.isBlank(eventResult.getTasks().get(0).getOptType()))
			return "操作类型[optType]无效";
		if(StringUtils.isBlank(eventResult.getTasks().get(0).getOppoSideBizCode()))
			return "对方业务编码[oppoSideBizCode]无效";
		if(StringUtils.isBlank(eventResult.getTasks().get(0).getOppoSideBizType()))
			return "对方业务类型[oppoSideBizType]无效";
		if(StringUtils.isBlank(eventResult.getTasks().get(0).getSrcPlatform()))
			return "来源平台[srcPlatform]无效";
		return "";
	}

	private String checkFlowTaskData(XmlDataResult eventResult){
		
	       //2019-05-23 为了实现调用接口返回code和message信息   wuxq
//			Boolean result = false;
			if(null == eventResult.getAuth()) 
				return convertToXmlResult("0", "认证信息[auth]无效");
			if(StringUtils.isBlank(eventResult.getAuth().getUsername())) 
				return convertToXmlResult("0","用户名[username]无效");
			if(StringUtils.isBlank(eventResult.getAuth().getPassword()))
				return convertToXmlResult("0","密码[password]无效");
			if(!checkUserPwd(eventResult.getAuth().getUsername(), eventResult.getAuth().getPassword()))
				return convertToXmlResult("0","用户[username][password]验证失败");
			if(StringUtils.isBlank(eventResult.getTasks().get(0).getSrcPlatform())) 
				return convertToXmlResult("0","来源平台[srcPlatform]无效");
			if(StringUtils.isBlank(eventResult.getTasks().get(0).getEventId())) 
				return convertToXmlResult("0","我方事件ID[eventId]无效");
			if(null == eventResult.getTasks())
				return convertToXmlResult("0","流程信息[tasks]无效");
			if(eventResult.getTasks().size() < 1) 
				return convertToXmlResult("0","流程信息[task]无效");
			if(StringUtils.isBlank(eventResult.getTasks().get(0).getTransactOrgName())) 
				return convertToXmlResult("0","办理组织名称[transactOrgName]无效");
			if(StringUtils.isBlank(eventResult.getTasks().get(0).getResults()))
				return convertToXmlResult("0","处理结果[results]无效");
			if(StringUtils.isBlank(eventResult.getTasks().get(0).getCreateTime())) 
				return convertToXmlResult("0","任务创建时间[createTime]无效");
			if(StringUtils.isBlank(eventResult.getTasks().get(0).getCreateUserName()))
				return convertToXmlResult("0","任务创建用户姓名[createUserName]无效");
			if(StringUtils.isBlank(eventResult.getTasks().get(0).getTaskName())) 
				return convertToXmlResult("0","任务名称[taskName]无效");
//			if(StringUtils.isBlank(eventResult.getTasks().get(0).getTaskType())) {MESSAGE_INFO = "任务类型[taskType]无效"; return result;}
			if(StringUtils.isBlank(eventResult.getTasks().get(0).getOptType()))
				return convertToXmlResult("0","操作类型[optType]无效");
//			if(StringUtils.isBlank(eventResult.getTasks().get(0).getOppoSideParentBizCode())) {MESSAGE_INFO = "对方父级业务编码[oppoSideParentBizCode]无效"; return result;}
//			if(StringUtils.isBlank(eventResult.getTasks().get(0).getOppoSideParentBizType())) {MESSAGE_INFO = "对方父级业务类型[oppoSideParentBizType]无效"; return result;}
			if(StringUtils.isBlank(eventResult.getTasks().get(0).getOppoSideBizCode())) 
				return convertToXmlResult("0","对方业务编码[oppoSideBizCode]无效");
			if(StringUtils.isBlank(eventResult.getTasks().get(0).getOppoSideBizType())) 
				return convertToXmlResult("0","对方业务类型[oppoSideBizType]无效");
			String transactorId = eventResult.getTasks().get(0).getTransactorId();
			if (StringUtils.isNotBlank(transactorId)) {
				if (!StringUtils.isNumeric(transactorId)) {
					return convertToXmlResult("0","办理人Id[transactorId]必须是整型");
				}
			}
			String transactOrgID = eventResult.getTasks().get(0).getTransactOrgID();
			if (StringUtils.isNotBlank(transactOrgID)) {
				if (!StringUtils.isNumeric(transactOrgID)) {
					return convertToXmlResult("0","办理组织编码[transactOrgID]必须是整型");
				}
			}
			//校验附件名称是否为文件名称
			if (null != eventResult.getTasks().get(0).getAttrs() && eventResult.getTasks().get(0).getAttrs().size()>0) {
				String result = checkAttrName(eventResult.getTasks().get(0).getAttrs());
		           if (StringUtils.isNotBlank(result)) {
		    	return result;
			    }
		    }
			return "";
		}

		
	private Boolean checkUserPwd(String usernameInter, String passwordInter){
//		String username = "jjbmfwzx";
//		String password = "086FBB45F6F0D56D03E811E73D54E8E8";
//		String defaulUsername = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, usernameInter, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		String defaulPassword = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, usernameInter, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(usernameInter) && StringUtils.isNotBlank(passwordInter) && StringUtils.isNotBlank(defaulPassword)){
			if(passwordInter.equals(defaulPassword)){
				return true;
			}
		}
		return false;
	}

	private String convertToXmlResult(String code, String message){
		StringBuffer result = new StringBuffer();
		result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		result.append("<result>");
		result.append("<code>");
		result.append(code);
		result.append("</code>");
		result.append("<message>");
		result.append(message);
		result.append("</message>");
		result.append("</result>");
		return result.toString();
	}

	
	private String formatEventType(String type, String bizPlatform){
		if(bizPlatform.equals("008")){
//			String eventType = baseDictionaryService.changeCodeToName(ConstantValue.EVENT_DOCKING_TYPE, type, null);
			String eventType = null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("dictPcode", ConstantValue.EVENT_DOCKING_TYPE);
			List<BaseDataDict> baseDataDicts = baseDictionaryService.getDataDictListOfSinglestage(params);
			for(BaseDataDict baseDataDict : baseDataDicts){
				if(baseDataDict.getDictName().equals(type)){
					eventType = baseDataDict.getDictGeneralCode();
					break;
				}
			}
			System.out.println("eventType:"+eventType);
			return StringUtils.isNotBlank(eventType) ? eventType : type;
		}
		return type;
	}



	private int getScore(EventDisposal eventDisposal, XmlDataResult eventResult){
		int score = 100;
		if(StringUtils.isBlank(eventDisposal.getEventName())){
			score = score - 10;
		}else if(StringUtils.isBlank(eventDisposal.getHappenTimeStr())){
			score = score - 10;
		}else if(StringUtils.isBlank(eventDisposal.getGridCode())){
			score = score - 10;
		}else if(StringUtils.isBlank(eventDisposal.getOccurred())){
			score = score - 8;
		}else if(StringUtils.isBlank(eventDisposal.getContent())){
			score = score - 10;
		}else if(StringUtils.isBlank(eventDisposal.getType())){
			score = score - 8;
		}else if(StringUtils.isBlank(eventDisposal.getContactUser())){
			score = score - 10;
		}else if(StringUtils.isBlank(eventDisposal.getResult())){
			score = score - 10;
		}else if(StringUtils.isBlank(eventDisposal.getResMarker().getX())){
			score = score - 6;
		}else if(StringUtils.isBlank(eventDisposal.getResMarker().getY())){
			score = score - 6;
		}else if(null == eventResult.getEvent().getAttrs() ||
				eventResult.getEvent().getAttrs().size() < 1){
			score = score - 12;
		}
		return score;
	}
	
	
	//通过url获取输入流
	private InputStream fileURL2InputStream(URL url) throws Exception {

		InputStream inputStream = null;

		if (null != url) {

			if (url.toURI().getScheme().equals("https")) {

				TrustManager[] tm = { new MyX509TrustManager() };
				SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
				sslContext.init(null, tm, new java.security.SecureRandom());
				// 从上述SSLContext对象中得到SSLSocketFactory对象
				SSLSocketFactory ssf = sslContext.getSocketFactory();

				HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
				httpUrlConn.setSSLSocketFactory(ssf);

				// 防止屏蔽程序抓取而返回403错误
				httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

				inputStream = httpUrlConn.getInputStream();
			} else {

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setConnectTimeout(3 * 5000);
				// 防止屏蔽程序抓取而返回403错误
				conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

				inputStream = conn.getInputStream();
			}

		}
		return inputStream;
	}
	
	
}
