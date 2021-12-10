package cn.ffcs.zhsq.controller.service.impl;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.zhsq.base.export.ExpStruc;
import cn.ffcs.zhsq.base.export.IExporter;
import cn.ffcs.gmis.mybatis.domain.userscore.UserDetailScore;
import cn.ffcs.gmis.userscore.service.UserScoreService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.eventReportRecord.domain.EventReportRecordInfo;
import cn.ffcs.shequ.eventReportRecord.service.IEventReportRecordService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.dispute.service.IDisputeMediationService;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.mybatis.domain.CoordinateInverseQuery.CoordinateInverseQueryGridInfo;
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeMediation;
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeMediationRes;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.mybatis.persistence.dispute.DisputeMediationMapper;
import cn.ffcs.zhsq.mybatis.persistence.dispute.DisputeMediationResMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.data.DateUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.cookie.service.CacheService;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.CoordinateInverseQuery.service.ICoordinateInverseQueryService;

import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

@Service(value="disputeMediationServiceImpl")
@Transactional
public class DisputeMediationServiceImpl implements IDisputeMediationService,ServletContextAware, IExporter{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private IBaseDictionaryService dictionaryService;
	@Autowired
	private DisputeMediationMapper disputeMediationMapper;
	@Autowired
	private DisputeMediationResMapper disputeMediationResMapper;
	@Autowired
	private IEventReportRecordService eventReportRecordService;
    @Autowired
    private IResMarkerService resMarkerService;
	@Autowired
	private IEventDisposalDockingService eventDisposalDockingService;
	@Autowired
	private IFunConfigurationService configurationService;
	@Autowired
	private IEventDisposalService eventService;
	@Autowired
	private ICoordinateInverseQueryService coordinateInverseQueryService;
	@Autowired
	private IInvolvedPeopleService involvedPeopleService;
	@Autowired
	private IAttachmentService attachmentService;

	@Autowired
	private UserScoreService userScoreService;
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	@Autowired
	private UserManageOutService userManageService;
	
	private String DISPUTE_SCORE_TYPE = "D1";//矛盾纠纷个人积分 新增类型 上报
	private String DISPUTE_SCORE_TYPE_CLOSE = "D2";//矛盾纠纷个人积分 新增类型  结案

	/*@Autowired
	private DisputeService disputeService;*/
	@Autowired
	private CacheService cacheService;

	@Override
	public Long saveEventReport(DisputeMediation disputeMediation, UserInfo userInfo){
		//在模块事件上报记录关联表中添加记录
		EventReportRecordInfo eventReportRecordInfo = new EventReportRecordInfo();
		eventReportRecordInfo.setBizId(disputeMediation.getMediationId());
		eventReportRecordInfo.setProcStatus("0");//对应的事件未处理
		String bizType = "DISPUTE_MEDIATION_DF";
		eventReportRecordInfo.setEventId(disputeMediation.getEventId());
		//获取事件信息
		EventDisposal eventDisposal = eventService.findEventById(disputeMediation.getEventId(), userInfo.getOrgCode());
		if(eventDisposal != null){
			if(eventDisposal.getType()!=null){
				eventReportRecordInfo.setBizType(bizType);
			}
		}
		//上报的事件处理后，回调的服务名称
		String serviceName =  configurationService
				.turnCodeToValue(ConstantValue.EVENT_ARCH_CALLBACK_SERVICE, bizType, IFunConfigurationService.CFG_TYPE_JAVA_PROCESSOR, IFunConfigurationService.CFG_ORG_TYPE_0);
		eventReportRecordInfo.setServiceName(serviceName);
		
		if(disputeMediation.getDisputeStatus().equals("3")) {
			
			disputeMediation.setCreatorId(userInfo.getUserId());
			disputeMediation.setOrgId(userInfo.getOrgId());
			Long disputeMediationOrgId = disputeMediation.getOrgId(),
					createUserId = disputeMediation.getCreatorId();
					String orgCode = "";
					if(createUserId != null && createUserId > 0) {
						if(disputeMediationOrgId != null && disputeMediationOrgId > 0) {
							OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(disputeMediationOrgId);
							if(orgInfo != null) {
								orgCode = orgInfo.getOrgCode();  
							}
						}
						if(StringUtils.isNotBlank(orgCode)) {
							String userPartyName = "";
							UserBO userBO = userManageService.getUserInfoByUserId(createUserId);
							UserDetailScore userDetailScore = new UserDetailScore();
							
							if(userBO != null) {
								userPartyName = userBO.getPartyName();
							}
							userDetailScore.setUserId(createUserId);
							userDetailScore.setUserName(userPartyName);
							userDetailScore.setOrgCode(orgCode);
							userDetailScore.setCreateBy(createUserId);
							userDetailScore.setScoreType(DISPUTE_SCORE_TYPE_CLOSE);
							userDetailScore.setRemark(String.valueOf(disputeMediation.getMediationId()));
							userScoreService.insertUserDetailScore(userDetailScore);
						}
					}
		}
		
		return eventReportRecordService.insertEventReportRecordInfo(eventReportRecordInfo, userInfo.getOrgCode());
	}

	@Override
	public String getDisputeEventType(UserInfo userInfo, String type){
		String disputeEventType = "";
		String disputeEventTypeCfg = ConstantValue.DISPUTE_EVENT_TYPE;

		if(StringUtils.isNotBlank(type)){
			disputeEventType = type;
			//disputeEventType = "50" + type.substring(0, 2);//矛盾纠纷类型对应事件矛盾纠纷下类型
		}else{
			if(!StringUtils.isBlank(userInfo.getOrgCode())){
				disputeEventType =  configurationService.
						changeCodeToValue(ConstantValue.REPORT_EVENT_TYPE,disputeEventTypeCfg,IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode());
				if(StringUtils.isBlank(userInfo.getOrgCode())){
					disputeEventType =  configurationService.
							changeCodeToValue(ConstantValue.REPORT_EVENT_TYPE,disputeEventTypeCfg,IFunConfigurationService.CFG_TYPE_FACT_VAL);
				}
			}else{
				disputeEventType =  configurationService.
						changeCodeToValue(ConstantValue.REPORT_EVENT_TYPE,disputeEventTypeCfg,IFunConfigurationService.CFG_TYPE_FACT_VAL);
			}
		}
		return disputeEventType;
	}

	@Override
	public boolean updateEventAndClose(DisputeMediation disputeMediation, UserInfo userInfo){
		//update dispute
		disputeMediation.setDisputeStatus("3");//结案
		this.updateByPrimaryKeySelective(disputeMediation);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contactUser",userInfo.getPartyName());
		params.put("content",disputeMediation.getDisputeCondition());
		params.put("gridId",disputeMediation.getGridId());
		params.put("eventName",disputeMediation.getDisputeEventName());
		params.put("happenTimeStr",disputeMediation.getHappenTimeStr());
		params.put("occurred",disputeMediation.getHappenAddr());
		params.put("collectWay","02");//微信：05，APP：01，PC：02
		params.put("userInfo",userInfo);
		params.put("advice",disputeMediation.getMediationResult());//化解情况
		params.put("outerAttachmentIds",disputeMediation.getAttaIds());
		String disputeEventType= this.getDisputeEventType(userInfo, disputeMediation.getDisputeType2());//获取该组织的矛盾纠纷事件类型
		if(!StringUtils.isBlank(disputeEventType)){
			params.put("type",disputeEventType);
		}
		Map<String, Object> result = eventDisposalDockingService.saveEventDisposalAndClose(params);
		//保存中间表
//		disputeMediation.setMediationId(mediationId);
		disputeMediation.setEventId(Long.valueOf(result.get("eventId").toString()));
		this.saveEventReport(disputeMediation, userInfo);
		return result.get("result")!=null&&Boolean.valueOf(result.get("result").toString());
	}

	@Override
	public Long saveEventAndClose(DisputeMediation disputeMediation, UserInfo userInfo, Map<String, Object> otherParams){
		//System.out.println("开始："+cn.ffcs.shequ.utils.date.DateUtils.getNow());
		//save dispute
		Long disputeMediationOrgId = disputeMediation.getOrgId();
		
		if(disputeMediationOrgId == null || disputeMediationOrgId < 0 ) {
			disputeMediation.setOrgId(userInfo.getOrgId());
		}
		disputeMediation.setSource("02");//
		disputeMediation.setCreatorId(userInfo.getUserId());
		disputeMediation.setDisputeStatus("3");//

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contactUser",userInfo.getPartyName());
		params.put("content",disputeMediation.getDisputeCondition());
		params.put("gridId",disputeMediation.getGridId());
		params.put("eventName",disputeMediation.getDisputeEventName());
		params.put("happenTimeStr",disputeMediation.getHappenTimeStr());
		params.put("occurred",disputeMediation.getHappenAddr());
		params.put("collectWay","01");//微信：05，APP：01
		params.put("userInfo",userInfo);
		params.put("advice","矛盾纠纷上报事件！");
		params.put("outerAttachmentIds",disputeMediation.getAttaIds());
		String disputeEventType= this.getDisputeEventType(userInfo, disputeMediation.getDisputeType2());//获取该组织的矛盾纠纷事件类型
		if(!StringUtils.isBlank(disputeEventType)){
			params.put("type",disputeEventType);
		}
		System.out.println("开始保存事件："+cn.ffcs.shequ.wap.util.DateUtils.getNow());
		Map<String, Object> result = eventDisposalDockingService.saveEventDisposalAndClose(params);
		System.out.println("==============================================================================");
		System.out.println(result);
		System.out.println("==============================================================================");
		System.out.println("结束保存事件："+cn.ffcs.shequ.wap.util.DateUtils.getNow());
		//保存中间表
		disputeMediation.setEventId(Long.valueOf(result.get("eventId").toString()));

		disputeMediation = this.saveDispute(disputeMediation, new HashMap<String, Object>());

		System.out.println("开始保存中间表："+cn.ffcs.shequ.wap.util.DateUtils.getNow());
		this.saveEventReport(disputeMediation, userInfo);
		System.out.println("结束保存事件："+cn.ffcs.shequ.wap.util.DateUtils.getNow());

		return disputeMediation.getMediationId();
	}

	public boolean reportEvent(DisputeMediation disputeMediation, UserInfo userInfo, Map<String, Object> otherParams){
		//report event
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contactUser",userInfo.getPartyName());
		params.put("content",disputeMediation.getDisputeCondition());
		params.put("gridId",disputeMediation.getGridId());
		params.put("eventName",disputeMediation.getDisputeEventName());
		params.put("happenTimeStr",disputeMediation.getHappenTimeStr());
		params.put("occurred",disputeMediation.getHappenAddr());
		params.put("collectWay","01");//微信：05，APP：01
		params.put("userInfo",userInfo);
		params.put("advice","矛盾纠纷上报事件！");
		params.put("outerAttachmentIds",disputeMediation.getAttaIds());
		String disputeEventType= this.getDisputeEventType(userInfo, disputeMediation.getDisputeType2());//获取该组织的矛盾纠纷事件类型
		if(!StringUtils.isBlank(disputeEventType)){
			params.put("type",disputeEventType);
		}
		Map<String, Object> result = eventDisposalDockingService.saveEventDisposalAndReport(params);
		//保存中间表
		disputeMediation.setMediationId(disputeMediation.getMediationId());
		disputeMediation.setEventId(Long.valueOf(result.get("eventId").toString()));
		this.saveEventReport(disputeMediation, userInfo);
		return result.get("result")!=null&&Boolean.valueOf(result.get("result").toString());
	}

	@Override
	public boolean updateAndReport(DisputeMediation disputeMediation, UserInfo userInfo, Map<String, Object> otherParams){
		//update dispute
		disputeMediation.setDisputeStatus("2");//上报
		this.updateByPrimaryKeySelective(disputeMediation);
		return this.reportEvent(disputeMediation,userInfo,otherParams);
	}

	@Override
	public Long saveEventAndReport(DisputeMediation disputeMediation, UserInfo userInfo, Map<String, Object> otherParams){
		//save dispute
		Long disputeMediationOrgId = disputeMediation.getOrgId();
		
		if(disputeMediationOrgId == null || disputeMediationOrgId < 0 ) {
			disputeMediation.setOrgId(userInfo.getOrgId());
		}
		disputeMediation.setSource("02");//来源app
		disputeMediation.setCreatorId(userInfo.getUserId());
		disputeMediation.setDisputeStatus("2");//上报

		disputeMediation = this.saveDispute(disputeMediation, new HashMap<String, Object>());


		boolean reportEvent = this.reportEvent(disputeMediation, userInfo, otherParams);
		System.out.println("===============================================");
		System.out.println("===============================================");
		System.out.println("====================="+reportEvent+"==========================");
		System.out.println("===============================================");
		System.out.println("===============================================");
		System.out.println("===============================================");
		System.out.println("===============================================");
		System.out.println("===============================================");
		System.out.println("====================="+disputeMediation+"==========================");
		System.out.println("===============================================");
		System.out.println("===============================================");
		System.out.println("===============================================");
		System.out.println("======================="+disputeMediation.getMediationId()+"========================");

		return reportEvent ? disputeMediation.getMediationId() : -1L;
	}

	@Override
	public boolean saveEventAndReport(DisputeMediation disputeMediation, UserInfo userInfo){
		//save dispute
		disputeMediation.setCreatorId(userInfo.getUserId());
		disputeMediation = this.saveDispute(disputeMediation, new HashMap<String, Object>());
		return this.reportEvent(disputeMediation,userInfo,new HashMap<String, Object>());
	}

	@Override
	public boolean evaluateEvent(Long mediationId, Map<String, Object> params){
		//System.out.println("mediationId---"+mediationId);
		//System.out.println("params.get(\"evaContent\")---"+params.get("evaContent"));
		if(mediationId==null) return false;
		EventReportRecordInfo eventReportRecordInfo = new EventReportRecordInfo();
		eventReportRecordInfo.setBizId(mediationId);
		eventReportRecordInfo.setBizType("DISPUTE_MEDIATION_DF");
		EventReportRecordInfo eventReportRecordInfoByEventId = eventReportRecordService.findEventReportRecordInfoByEventId(eventReportRecordInfo);
		DisputeMediation disputeMediation = this.selectByPrimaryKey(mediationId);
		if(eventReportRecordInfoByEventId!=null){
			Long eventId = eventReportRecordInfoByEventId.getEventId();
			params.put("eventId",eventId);
			params.put("eventDisposalWorkflowService","eventDisposalWorkflow4DisputeServiceImpl");
			//System.out.println("params---"+params);
			//01评价不满意回退
			if(params.get("evaLevel")!=null&&params.get("evaLevel").equals("01")){
				boolean rejectWorkFlow = eventDisposalDockingService.rejectWorkFlow(params, new UserInfo());
				disputeMediation.setEvaDate(new Date());
				disputeMediation.setEvaOpn(params.get("evaContent").toString());
				disputeMediation.setDisputeStatus("2");
				this.updateByPrimaryKeySelective(disputeMediation);
				return rejectWorkFlow;
			}else{//满意归档
				Map<String, Object> stringObjectMap = eventDisposalDockingService.subWorkflow(params);
				if(stringObjectMap!=null){
					disputeMediation.setEvaDate(new Date());
					disputeMediation.setEvaOpn(params.get("evaContent").toString());
					disputeMediation.setDisputeStatus("6");
					this.updateByPrimaryKeySelective(disputeMediation);
					return stringObjectMap.get("result")!=null&&Boolean.valueOf(stringObjectMap.get("result").toString());
				}
			}
		}
		return false;
	}

	@Override
	public int deleteById(Long id, Long userId){
		int result = disputeMediationMapper.deleteByPrimaryKeyForLogic(id, userId);
		return result;
	}

	@Override
	public int deleteByIds(List<Long> ids, Long userId){
		int record = 0;
		for(Long id : ids){
			int result = disputeMediationMapper.deleteByPrimaryKeyForLogic(id, userId);
			if(result > 0){
				record ++;
			}
		}
		return record;
	}

	@Override
	public int deleteAllByGridId(Map<String, Object> params) {
		int result = disputeMediationMapper.deleteAllByGridId(params);
		return result;
	}

	//
	private void saveAtts(DisputeMediation disputeMediation){
		 attachmentService.deleteByBizId(disputeMediation.getMediationId(), ConstantValue.DISPUTE_ATTACHMENT_TYPE);
		//保存附件
		if("01".equals(disputeMediation.getSource())){
			//保存相对路径
			String[] imgFilePaths = disputeMediation.getImgFilePath();
			String[] soundFilePaths = disputeMediation.getSoundFilePath();
			String imgAtts = "";
			String soundAtts = "";
			if(imgFilePaths!=null&&imgFilePaths.length>0){
				for(String imgFilePath : imgFilePaths){
					Attachment att = new Attachment();
					att.setBizId(disputeMediation.getMediationId());
					att.setAttachmentType(ConstantValue.DISPUTE_ATTACHMENT_TYPE);
					att.setFilePath(imgFilePath);
					att.setFileName(getFileName(imgFilePath));
					att.setEventSeq("1");
					Long attachmentId = attachmentService.saveAttachment(att);
					imgAtts = imgAtts + "," + attachmentId;
				}
				imgAtts = imgAtts.substring(1,imgAtts.length());
			}
			if(soundFilePaths!=null&&soundFilePaths.length>0){
				for(String soundFilePath : soundFilePaths){
					Attachment att = new Attachment();
					att.setBizId(disputeMediation.getMediationId());
					att.setAttachmentType(ConstantValue.DISPUTE_ATTACHMENT_TYPE);
					att.setFilePath(soundFilePath);
					att.setFileName(getFileName(soundFilePath));
					att.setEventSeq("1");
					Long attachmentId = attachmentService.saveAttachment(att);
					soundAtts = soundAtts + "," + attachmentId;
				}
				soundAtts = soundAtts.substring(1,soundAtts.length());
			}
			if(!"".equals(imgAtts) && !"".equals(soundAtts)){
				disputeMediation.setAttaIds(imgAtts + "," + soundAtts);
			}else{
				disputeMediation.setAttaIds(imgAtts + soundAtts);
			}
		}else if("02".equals(disputeMediation.getSource())){
			String[] imgFilePaths = disputeMediation.getImgFilePath();
			String[] soundFilePaths = disputeMediation.getSoundFilePath();
			String imgAtts = "";
			String soundAtts = "";
			if(imgFilePaths!=null&&imgFilePaths.length>0){
				for(String imgFilePath : imgFilePaths){
					Attachment att = new Attachment();
					att.setBizId(disputeMediation.getMediationId());
					att.setAttachmentType(ConstantValue.DISPUTE_ATTACHMENT_TYPE);
					att.setFilePath(imgFilePath);
					att.setFileName(getFileName(imgFilePath));
					att.setEventSeq("1");
					Long attachmentId = attachmentService.saveAttachment(att);
					imgAtts = imgAtts + "," + attachmentId;
				}
				imgAtts = imgAtts.substring(1,imgAtts.length());
			}
			if(soundFilePaths!=null&&soundFilePaths.length>0){
				for(String soundFilePath : soundFilePaths){
					Attachment att = new Attachment();
					att.setBizId(disputeMediation.getMediationId());
					att.setAttachmentType(ConstantValue.DISPUTE_ATTACHMENT_TYPE);
					att.setFilePath(soundFilePath);
					att.setFileName(getFileName(soundFilePath));
					att.setEventSeq("1");
					Long attachmentId = attachmentService.saveAttachment(att);
					soundAtts = soundAtts + "," + attachmentId;
				}
				soundAtts = soundAtts.substring(1,soundAtts.length());
			}
			if(!"".equals(imgAtts) && !"".equals(soundAtts)){
				disputeMediation.setAttaIds(imgAtts + "," + soundAtts);
			}else{
				disputeMediation.setAttaIds(imgAtts + soundAtts);
			}
		}
	}

	@Override
	public int updateByPrimaryKeySelective(DisputeMediation record){
		//添加修改
        if(!StringUtils.isEmpty(record.getMapType())){
            ResMarker resMarker = new ResMarker();
            resMarker.setCatalog("02");
            resMarker.setMarkerType(ConstantValue.DISPUTE_MEDIATION);
            resMarker.setMapType(record.getMapType());
            resMarker.setX(record.getX());
            resMarker.setY(record.getY());
            resMarker.setResourcesId(record.getMediationId());
            resMarkerService.saveOrUpdateResMarker(resMarker);
        }
		int result = disputeMediationMapper.updateByPrimaryKeySelective(record);
		disputeMediationMapper.selectByPrimaryKey(record.getMediationId());
		DisputeMediationRes disputeMediationRes = new DisputeMediationRes();
		disputeMediationRes.setMediationResId(record.getMediationResId());
		disputeMediationRes.setMediator(record.getMediator());
		disputeMediationRes.setMediationResult(record.getMediationResult());
		disputeMediationRes.setIsSuccess(record.getIsSuccess());
		disputeMediationRes.setMediationId(record.getMediationId());
		disputeMediationRes.setMediationOrgName(record.getMediationOrgName());
		disputeMediationRes.setMediationTel(record.getMediationTel());
		disputeMediationRes.setHjCertNumber(record.getHjCertNumber());
		//int res = 
		disputeMediationResMapper.updateByPrimaryKeySelective(disputeMediationRes);
//		saveAtts(record);//保存附件
		DisputeMediation bo = disputeMediationMapper.selectByPrimaryKey(record.getMediationId());
		Long disputeMediationOrgId = bo.getOrgId(),
		createUserId = bo.getCreatorId();
		String orgCode = "";
		
		if("3".equals(bo.getDisputeStatus())) {
			System.out.println("333333333333333333::::::::::::"+bo.getDisputeStatus());
			if(createUserId != null && createUserId > 0) {
				if(disputeMediationOrgId != null && disputeMediationOrgId > 0) {
					OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(disputeMediationOrgId);
					if(orgInfo != null) {
						orgCode = orgInfo.getOrgCode();  
					}
				}
				if(StringUtils.isNotBlank(orgCode)) {
					String userPartyName = "";
					UserBO userBO = userManageService.getUserInfoByUserId(createUserId);
					UserDetailScore userDetailScore = new UserDetailScore();
					
					if(userBO != null) {
						userPartyName = userBO.getPartyName();
					}
					System.out.println("222222222222222222222222222");
					userDetailScore.setUserId(createUserId);
					userDetailScore.setUserName(userPartyName);
					userDetailScore.setOrgCode(orgCode);
					userDetailScore.setCreateBy(createUserId);
					
					System.out.println("333333333333333333"+bo.getDisputeStatus());
					userDetailScore.setScoreType(DISPUTE_SCORE_TYPE_CLOSE);
					userDetailScore.setRemark(String.valueOf(bo.getMediationId()));
					
					userScoreService.insertUserDetailScore(userDetailScore);
				}
			}
		}
		return result;
	}

	@Transactional
	@Override
	public Boolean eventReject(Long mediationId, Long disputeId){
		Boolean r = true;
		/*Dispute dispute = new Dispute();
		dispute.setDisputeId(disputeId);
		dispute.setDisputeStatus("5");//1：未受理，2：上报，3：结案，4：已受理，5：驳回，6：已评价
		if(!disputeService.update(dispute)){
			logger.error("更新微信状态失败！");
			return false;
		}*/
		DisputeMediation disputeMediation = new DisputeMediation();
		disputeMediation.setMediationId(mediationId);
		disputeMediation.setDisputeStatus(ConstantValue.DISPUTESTATUS_BOHUI);
		if(this.updateByPrimaryKeySelective(disputeMediation) < 1){
			logger.error("更新矛盾纠纷失败！");
			return false;
		}
		return r;
	}

	@Transactional
	@Override
	public Boolean eventReport(Long mediationId, Long disputeId, String mediationDeadlineStr, UserInfo userInfo){
		Boolean r = true;
		DisputeMediation dm = this.selectByPrimaryKey(mediationId);
		//更新矛盾纠纷
		dm.setMediationDeadlineStr(mediationDeadlineStr);
		dm.setAcceptedDate(new Date());
		dm.setCreatorId(userInfo.getUserId());
		dm.setAccepter(userInfo.getPartyName());
		if(this.updateByPrimaryKeySelective(dm) < 1){
			logger.error("更新矛盾纠纷失败！");
			return false;
		}
		//上报事件
		EventDisposal event = new EventDisposal();
		//获取该组织的矛盾纠纷事件类型
		String disputeEventType= this.getDisputeEventType(userInfo, dm.getDisputeType2());
		if(!StringUtils.isBlank(disputeEventType)){
			event.setType(disputeEventType);
			//event.setType(ConstantValue.DISPUTE_TYPE);
		}
		event.setContent(dm.getDisputeCondition());
		event.setGridId(dm.getGridId());
		event.setEventName(dm.getDisputeEventName());
		event.setHappenTimeStr(dm.getHappenTimeStr());
		event.setOccurred(dm.getHappenAddr());
		//微信：05，APP：01
		if(StringUtils.isNotBlank(dm.getSource())){
			if(dm.getSource().equals("01"))
				event.setCollectWay("05");
			else if(dm.getSource().equals("02")){
				event.setCollectWay("01");
			}
		}
		Map<String, Object> result = eventDisposalDockingService.saveEventDisposalAndReport(event, userInfo, "矛盾纠纷上报事件！");
		//System.out.println(result);
		if(null!=result.get("result")&&result.get("result").toString().equals("true")){//上报成功
			dm.setEventId((Long)result.get("eventId"));
			DisputeMediation disputeMediation = new DisputeMediation();
			disputeMediation.setMediationId(mediationId);
			disputeMediation.setDisputeStatus(ConstantValue.DISPUTESTATUS_REPORT);
			this.updateByPrimaryKeySelective(disputeMediation);
			//保存中间表
			this.saveEventReport(dm, userInfo);
		}else{
			logger.error("事件上报失败！");
			return false;
		}
		//更新微信状态
		/*Dispute dispute = new Dispute();
		dispute.setDisputeId(disputeId);
		dispute.setDisputeStatus("2");//1：未受理，2：上报，3：结案，4：已受理，5：驳回，6：已评价
		if(!disputeService.update(dispute)){
			logger.error("更新微信状态失败！");
			return false;
		}*/
		return r;
	}

	
	@Override
	public DisputeMediation selectByPrimaryKey(Long mediationId){

		DisputeMediation disputeMediation = disputeMediationMapper.selectByPrimaryKey(mediationId);
		if(disputeMediation != null && disputeMediation.getMediationId() != null){
			if(StringUtils.isNotBlank(disputeMediation.getDisputeType3())) {
				disputeMediation.setDisputeType3(disputeMediation.getDisputeType3().trim());
			}
			DisputeMediationRes disputeMediationRes = disputeMediationResMapper.selectByMediationId(mediationId);
			if(disputeMediationRes != null){
				disputeMediation.setMediationResId(disputeMediationRes.getMediationResId());
				disputeMediation.setIsSuccess(disputeMediationRes.getIsSuccess());
				disputeMediation.setMediationResult(disputeMediationRes.getMediationResult());
				disputeMediation.setMediator(disputeMediationRes.getMediator());
				disputeMediation.setMediationTel(disputeMediationRes.getMediationTel());
				disputeMediation.setMediationOrgName(disputeMediationRes.getMediationOrgName());
				disputeMediation.setHjCertNumber(disputeMediationRes.getHjCertNumber());
			}
			EventReportRecordInfo eventReportRecordInfo = new EventReportRecordInfo();
			eventReportRecordInfo.setEventId(disputeMediation.getEventId());
			eventReportRecordInfo.setBizId(mediationId);
			eventReportRecordInfo.setBizType("DISPUTE_MEDIATION_DF");
			if(disputeMediation.getEventId() != null){
				EventReportRecordInfo eventReportRecordInfoByEvent = eventReportRecordService.findEventReportRecordInfoByEventId(eventReportRecordInfo);
				if(eventReportRecordInfoByEvent!=null){
					disputeMediation.setEventId(eventReportRecordInfoByEvent.getEventId());
				}
			}
			if(StringUtils.isNotBlank(disputeMediation.getMediationType())){//
				String mediationTypeStr = dictionaryService.changeCodeToName(ConstantValue.MEDIATION_TYPE_CODE, disputeMediation.getMediationType(), null, true);
				if(StringUtils.isBlank(mediationTypeStr)){
					mediationTypeStr = dictionaryService.changeCodeToName("B417", disputeMediation.getMediationType(), null, true);
				}
				disputeMediation.setMediationTypeStr(mediationTypeStr);
			}
		}
		return disputeMediation;
	}
	
	@Override
	public EUDGPagination findDisputePagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo<1?1:pageNo;
		pageSize = pageSize<1?10:pageSize;
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		Object nanchang= params.get("nanchang");
		int count=0;
		List<DisputeMediation> list=null;
		if(nanchang!=null){//南昌矛盾纠纷一键筛选个性化
			count = disputeMediationMapper.findCountByCriteriaNC(params);
			list = disputeMediationMapper.findPageListByCriteriaNC(params, rowBounds);
		}else{
			list = disputeMediationMapper.findPageListByCriteria(params, rowBounds);
			count = disputeMediationMapper.findCountByCriteria(params);
		}
		list = formatData(list, params.get("infoOrgCode").toString());
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}
	
	public int countDisputes(Map<String, Object> params) {
		return disputeMediationMapper.findCountByCriteria(params);
	}

	@Override
	public List<DisputeMediation> findDisputeList(Map<String, Object> params){
		List<DisputeMediation> list = disputeMediationMapper.findList(params);
		list = formatData(list, params.get("infoOrgCode").toString());
		return list;
	}

	@Override
	public Map<String, Object> findMaxDisputeType(Map<String, Object> params){
		return disputeMediationMapper.findMaxDisputeType(params);
	}
	
	private List<DisputeMediation> formatData(List<DisputeMediation> list, String infoOrgCode){
		String disputeTypeDict = "A001093199056";
		if(infoOrgCode.startsWith("36")){
			disputeTypeDict = "A001093199009";
		}
		//System.out.println(cn.ffcs.shequ.utils.date.DateUtils.getNow());
		List<BaseDataDict> disputeType= dictionaryService.getDataDictListOfSinglestage("B037", infoOrgCode);
		List<BaseDataDict> disputeType2= dictionaryService.getDataDictListOfSinglestage(disputeTypeDict, infoOrgCode);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("dictPcode",disputeTypeDict);
		//List<BaseDataDict> disputeTypeSub2 = this.dictionaryService.getDataDictListByPids(params);

		List<BaseDataDict> disputeScaleDC= dictionaryService.getDataDictListOfSinglestage(ConstantValue.DISPUTE_SCALE_CODE, infoOrgCode);
		for(DisputeMediation disputeMediation : list){
			DisputeMediationRes disputeMediationRes = disputeMediationResMapper.selectByMediationId(disputeMediation.getMediationId());
			if (disputeMediationRes != null) {
				disputeMediation.setMediationResult(disputeMediationRes.getMediationResult());
			}
			//formatDate(disputeMediation);
			if(disputeMediation.getDisputeStatus() != null){
				if(disputeMediation.getDisputeStatus().equals(ConstantValue.DISPUTESTATUS_ACCEPT)){
					disputeMediation.setDisputeStatusStr("草稿");//1
				}else if(disputeMediation.getDisputeStatus().equals(ConstantValue.DISPUTESTATUS_REPORT)){
					disputeMediation.setDisputeStatusStr("已上报");//2
				}else if(disputeMediation.getDisputeStatus().equals(ConstantValue.DISPUTESTATUS_CLOSE)){
					disputeMediation.setDisputeStatusStr("结案");//3
				}
			}
			if(StringUtils.isNotBlank(disputeMediation.getDisputeScale())){//事件规模
		        for(BaseDataDict baseDataDict : disputeScaleDC){
		        	if(disputeMediation.getDisputeScale().equals(baseDataDict.getDictGeneralCode())){
		        		disputeMediation.setDisputeScaleStr(baseDataDict.getDictName());
		        	}
		        }
			}
			if(null != disputeMediation.getDisputeType2()){//事件类型
		        for(BaseDataDict baseDataDict : disputeType){
		        	if(String.valueOf(disputeMediation.getDisputeType2()).equals(baseDataDict.getDictGeneralCode())){
		        		disputeMediation.setDisputeTypeStr(baseDataDict.getDictName());
		        	}
		        }
			}
			if(null != disputeMediation.getDisputeType2() && StringUtils.isBlank(disputeMediation.getDisputeTypeStr())){
				String disputeTypeName1 = "";
				//String disputeTypeName2 = "";
				String disputeType1 ="";
				if(StringUtils.isNotBlank(disputeMediation.getDisputeType2()) && 
						(disputeMediation.getDisputeType2().length()>=2)){					
//						disputeType1 =disputeMediation.getDisputeType2().substring(0, 2);
						disputeType1 =disputeMediation.getDisputeType2();
					
				}
				for(BaseDataDict baseDataDict : disputeType2){
					if(disputeType1.equals(baseDataDict.getDictGeneralCode())){
						disputeTypeName1 = baseDataDict.getDictName();
						disputeMediation.setBigType(disputeTypeName1);
						String changeCodeToName = dictionaryService.changeCodeToName(baseDataDict.getDictCode(), disputeMediation.getDisputeType2(), infoOrgCode);
						disputeMediation.setSmallType(changeCodeToName);
						//disputeMediation.setDisputeTypeStr(disputeTypeName1 + "-" + changeCodeToName);
						disputeMediation.setDisputeTypeStr("矛盾纠纷标准-"+disputeTypeName1);
					}
				}
//				for(BaseDataDict baseDataDict : disputeTypeSub2){
//					if(String.valueOf(disputeMediation.getDisputeType2()).equals(baseDataDict.getDictGeneralCode())){
//						disputeTypeName2 = baseDataDict.getDictName();
//					}
//				}
//				disputeMediation.setDisputeTypeStr(disputeTypeName1 + "-" + disputeTypeName2);
//				System.out.println(disputeTypeName1 + "" + disputeTypeName2);
			}
			if(null != disputeMediation.getMediationType()){//
				String mediationTypeStr = dictionaryService.changeCodeToName(ConstantValue.MEDIATION_TYPE_CODE, disputeMediation.getMediationType(), null, true);
				disputeMediation.setMediationTypeStr(mediationTypeStr);
			}
			if(null != disputeMediation.getGridPath()) {
				Map<String, String> buildScopeSettingMap = cacheService.getBuildScopeSettingMap();
				if(disputeMediation!=null && StringUtils.isNotBlank(buildScopeSettingMap.get("firstValue"))) {
					String orgEntityPath = disputeMediation.getGridPath();   //业务对象全路径
					if(StringUtils.isNotBlank(orgEntityPath)&&!orgEntityPath.equals(buildScopeSettingMap.get("firstValue"))) {
						orgEntityPath = orgEntityPath.replaceAll(buildScopeSettingMap.get("firstValue"), "");
						disputeMediation.setGridPath(orgEntityPath);
					}
					if(StringUtils.isNotBlank(orgEntityPath)&&orgEntityPath.equals(buildScopeSettingMap.get("firstValue"))) {
						orgEntityPath = orgEntityPath.replaceAll(buildScopeSettingMap.get("firstValue"),buildScopeSettingMap.get("secondValue"));
						disputeMediation.setGridPath(orgEntityPath);
					}			
				}
			}
		}
		//System.out.println(cn.ffcs.shequ.utils.date.DateUtils.getNow());
		return list;
	}
	
	private DisputeMediation formatDate(DisputeMediation disputeMediation){
		//默认状态
//		disputeMediation.setStatus("1");
		//默认编码
//		disputeMediation.setMediationCode(this.getMediationCode());

		if(!StringUtils.isBlank(disputeMediation.getHappenTimeStr())){
			disputeMediation.setHappenTime(DateUtils.formatDate(disputeMediation.getHappenTimeStr(), DateUtils.PATTERN_24TIME, null));
		}
		if(!StringUtils.isBlank(disputeMediation.getAcceptedDateStr())){
			disputeMediation.setAcceptedDate(DateUtils.formatDate(disputeMediation.getAcceptedDateStr(), DateUtils.PATTERN_DATE, null));
		}
		if(!StringUtils.isBlank(disputeMediation.getEvaDateStr())){
			disputeMediation.setEvaDate(DateUtils.formatDate(disputeMediation.getEvaDateStr(), DateUtils.PATTERN_DATE, null));
		}
		if(!StringUtils.isBlank(disputeMediation.getMediationDateStr())){
			disputeMediation.setMediationDate(DateUtils.formatDate(disputeMediation.getMediationDateStr(), DateUtils.PATTERN_DATE, null));
		}
		if(!StringUtils.isBlank(disputeMediation.getMediationDeadlineStr())){
			disputeMediation.setMediationDeadline(DateUtils.formatDate(disputeMediation.getMediationDeadlineStr(), DateUtils.PATTERN_DATE, null));
		}
		
		if(disputeMediation.getHappenTime() != null){
			disputeMediation.setHappenTimeStr(DateUtils.formatDate(disputeMediation.getHappenTime(), DateUtils.PATTERN_24TIME));
		}
		if(disputeMediation.getAcceptedDate() != null){
			disputeMediation.setAcceptedDateStr(DateUtils.formatDate(disputeMediation.getAcceptedDate(), DateUtils.PATTERN_DATE));
		}
		return disputeMediation;
	}

	@Override
	public boolean updateWeiXinDispute(Long disputeId, String disputeStatus){
		/*Dispute dispute = new Dispute();
		dispute.setDisputeId(disputeId);
		dispute.setDisputeStatus(disputeStatus);//1：未受理，2：上报，3：结案，4：已受理，5：驳回，6：已评价*/
		//return disputeService.update(dispute);
		return true;
	}

	/**
	 * 根据经纬度获取网格信息
	 * @param x
	 * @param y
     * @return
     */
	private CoordinateInverseQueryGridInfo getGridInfo(String x, String y){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("y",x);
		params.put("x",y);
		List<CoordinateInverseQueryGridInfo> gridInfos = coordinateInverseQueryService.findGridInfo(params);
		String infoOrgCode = "";
		CoordinateInverseQueryGridInfo r = null;
		if(gridInfos!=null&&gridInfos.size()>0){
			for(CoordinateInverseQueryGridInfo c : gridInfos){
				String code = c.getInfoOrgCode();
				//Long gId = 
						c.getGridId();
				if(StringUtils.isBlank(infoOrgCode)||infoOrgCode.length()<code.length()){
					infoOrgCode = code;
					r = c;
				}
			}
		}
		return r;
	}

	private void saveFiles(Long id, String[] filePaths){
		StringBuffer attachmentIds = new StringBuffer("");
		byte[] buffer = null;
		String fileName = "wechat-attachment-d8f1ae7a92c744d3a2f48f6ed0c1863a.jpg";
		String filePath = "http://img.sq.aishequ.org/wechat/attachment/2017/02/21/wechat-attachment-d8f1ae7a92c744d3a2f48f6ed0c1863a.jpg";
		String path = "http://img.sq.aishequ.org/";
		int index = 0;

		for(String attachemntObjStr : filePaths) {
			index ++;
			fileName = String.valueOf(index) + ".jpg";
			InputStream netFileInputStream = null;
			try {
				URL url = new URL(path + attachemntObjStr);
				URLConnection urlConn = url.openConnection();
				netFileInputStream = urlConn.getInputStream();

				if(netFileInputStream != null) {
					String picUrl = "";
					int len = 0;

					//将获取到的附件转换为字节数组
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					while((len = netFileInputStream.read(buffer)) != -1) {
						bos.write(buffer, 0, len);
					}
					bos.close();

					byte[] bytes = bos.toByteArray();

					if(bytes.length > 0) {//上传附件
						picUrl = fileUploadService.uploadSingleFile(fileName, bytes, "zzgrid", "attachment");
					}

					if(StringUtils.isNotBlank(picUrl)) {
						Attachment att = new Attachment();

						Image imgOri = javax.imageio.ImageIO.read(url.openStream());
						if(imgOri != null) {//获取图片的高度、宽度，不是图片的附件，imgOri为null
							Integer imgWidthOri = imgOri.getWidth(null);
							Integer imgHeightOri = imgOri.getHeight(null);
							att.setImgWidth(imgWidthOri + "");
							att.setImgHeight(imgHeightOri + "");
						}

//						if(DEFAULT_FILE_NAME.equals(fileName) && picUrl.lastIndexOf("/") >= 0) {//如果使用的是默认附件名称，则最后保留的附件名称为上传成功后自动生成的文件名称
//							fileName = picUrl.substring(picUrl.lastIndexOf("/") + 1);
//						}

						att.setBizId(id);
						att.setAttachmentType(ConstantValue.DISPUTE_ATTACHMENT_TYPE);
						att.setFilePath(picUrl);
						att.setFileName(fileName);
						att.setFileSize(String.valueOf(bytes.length));
//						att.setEventSeq(eventSeq);
//						att.setCreatorId(event.getCreatorId());
//						att.setCreatorName(event.getCreatorName());
						//将附件保存入库
						Long attachmentId = attachmentService.saveAttachment(att);

						if (attachmentId > 0) {
							att.setAttachmentId(attachmentId);
							attachmentIds.append(",").append(attachmentId);
						}
					}
				}
			} catch (IOException e) {
//				logger.error("读取：" + filePath + " 失败！");
				e.printStackTrace();
			} finally {
				try {
					if (netFileInputStream != null) {
						netFileInputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

//		if(attachmentIds.length() > 0) {
//			event.setAttachmentIds(attachmentIds.substring(1));
//		}
	}

	@Autowired
	private FileUploadService fileUploadService;

	private void getFiles(Long id){
		String path = "/opt/newapp/tomcat6-event-all-8550/webapps/zhsq_event/images/12345.png";
		File file = new File(path);
		String picUrl = "";
		String fileName = "12345.png";
		byte[] buffer = new byte[1024];
		try {
			FileInputStream inputStream = new FileInputStream(file);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len = 0;
			while((len = inputStream.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			bos.close();
			byte[] bytes = bos.toByteArray();
			if(bytes.length > 0) {//上传附件
				picUrl = fileUploadService.uploadSingleFile(fileName, bytes, "zzgrid", "attachment");
			}

			Attachment att = new Attachment();
			att.setBizId(id);
			att.setAttachmentType(ConstantValue.DISPUTE_ATTACHMENT_TYPE);
			att.setFilePath(picUrl);
			att.setFileName(fileName);
			att.setFileSize(String.valueOf(bytes.length));
			att.setEventSeq("1");
			//将附件保存入库
			Long attachmentId = attachmentService.saveAttachment(att);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getFileName(String imgFilePath){
		return imgFilePath.substring(imgFilePath.lastIndexOf("/") + 1);
	}

	private DisputeMediation saveDispute(DisputeMediation disputeMediation, Map<String, Object> otherParams){
		Long aLong = this.saveDisputeMediation(disputeMediation, otherParams);
		disputeMediation.setMediationId(aLong);
		return disputeMediation;
	}

	@Override
	public Long saveDisputeMediation(DisputeMediation disputeMediation, Map<String, Object> otherParams){
		//经纬度网格转换
		if(disputeMediation.getGridId() != null){
			CoordinateInverseQueryGridInfo gridInfo = getGridInfo(disputeMediation.getX(), disputeMediation.getY());
			if(gridInfo!=null)
				disputeMediation.setGridId(gridInfo.getGridId());
		}
		//disputeMediation.setGridId("208914");
		//保存矛盾纠纷信息
		Long record = this.insertDisputeMediation(disputeMediation);
		//保存附件
		if("01".equals(disputeMediation.getSource())){
			//保存相对路径
			String[] imgFilePaths = disputeMediation.getImgFilePath();
			String[] soundFilePaths = disputeMediation.getSoundFilePath();
			String imgAtts = "";
			String soundAtts = "";
			if(imgFilePaths!=null&&imgFilePaths.length>0){
				for(String imgFilePath : imgFilePaths){
					Attachment att = new Attachment();
					att.setBizId(disputeMediation.getMediationId());
					att.setAttachmentType(ConstantValue.DISPUTE_ATTACHMENT_TYPE);
					att.setFilePath(imgFilePath);
					att.setFileName(getFileName(imgFilePath));
					att.setEventSeq("1");
					Long attachmentId = attachmentService.saveAttachment(att);
					imgAtts = imgAtts + "," + attachmentId;
				}
				imgAtts = imgAtts.substring(1,imgAtts.length());
			}
			if(soundFilePaths!=null&&soundFilePaths.length>0){
				for(String soundFilePath : soundFilePaths){
					Attachment att = new Attachment();
					att.setBizId(disputeMediation.getMediationId());
					att.setAttachmentType(ConstantValue.DISPUTE_ATTACHMENT_TYPE);
					att.setFilePath(soundFilePath);
					att.setFileName(getFileName(soundFilePath));
					att.setEventSeq("1");
					Long attachmentId = attachmentService.saveAttachment(att);
					soundAtts = soundAtts + "," + attachmentId;
				}
				soundAtts = soundAtts.substring(1,soundAtts.length());
			}
			if(!"".equals(imgAtts) && !"".equals(soundAtts)){
				disputeMediation.setAttaIds(imgAtts + "," + soundAtts);
			}else{
				disputeMediation.setAttaIds(imgAtts + soundAtts);
			}
		}else if("02".equals(disputeMediation.getSource())){
			String[] imgFilePaths = disputeMediation.getImgFilePath();
			String[] soundFilePaths = disputeMediation.getSoundFilePath();
			String imgAtts = "";
			String soundAtts = "";
			if(imgFilePaths!=null&&imgFilePaths.length>0){
				for(String imgFilePath : imgFilePaths){
					Attachment att = new Attachment();
					att.setBizId(disputeMediation.getMediationId());
					att.setAttachmentType(ConstantValue.DISPUTE_ATTACHMENT_TYPE);
					att.setFilePath(imgFilePath);
					att.setFileName(getFileName(imgFilePath));
					att.setEventSeq("1");
					Long attachmentId = attachmentService.saveAttachment(att);
					imgAtts = imgAtts + "," + attachmentId;
				}
				imgAtts = imgAtts.substring(1,imgAtts.length());
			}
			if(soundFilePaths!=null&&soundFilePaths.length>0){
				for(String soundFilePath : soundFilePaths){
					Attachment att = new Attachment();
					att.setBizId(disputeMediation.getMediationId());
					att.setAttachmentType(ConstantValue.DISPUTE_ATTACHMENT_TYPE);
					att.setFilePath(soundFilePath);
					att.setFileName(getFileName(soundFilePath));
					att.setEventSeq("1");
					Long attachmentId = attachmentService.saveAttachment(att);
					soundAtts = soundAtts + "," + attachmentId;
				}
				soundAtts = soundAtts.substring(1,soundAtts.length());
			}
			if(!"".equals(imgAtts) && !"".equals(soundAtts)){
				disputeMediation.setAttaIds(imgAtts + "," + soundAtts);
			}else{
				disputeMediation.setAttaIds(imgAtts + soundAtts);
			}
		}
		//保存当事人
		String involvedNames = disputeMediation.getInvolvedName();
		if(StringUtils.isNotBlank(involvedNames)){
			String[] invNames = involvedNames.split(",");//
			for(String involvedName : invNames){
				InvolvedPeople involvedPeople = new InvolvedPeople();
				involvedPeople.setBizType(InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
				involvedPeople.setName(involvedName);
				involvedPeople.setBizId(record);
				try {
					involvedPeopleService.insertInvolvedPeople(involvedPeople, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return record;
	}
	
	@Transactional
	@Override
	public Long insertDisputeMediation(DisputeMediation disputeMediation) {

		//默认状态
		disputeMediation.setStatus("1");
		//默认编码
		disputeMediation.setMediationCode(this.getMediationCode());
//		formatDate(disputeMediation);
		
		int result = disputeMediationMapper.insert(disputeMediation);

		//添加修改
        if(!StringUtils.isEmpty(disputeMediation.getMapType())){
            ResMarker resMarker = new ResMarker();
            resMarker.setCatalog("02");
            resMarker.setMarkerType(ConstantValue.DISPUTE_MEDIATION);
            resMarker.setMapType(disputeMediation.getMapType());
            resMarker.setX(disputeMediation.getX());
            resMarker.setY(disputeMediation.getY());
            resMarker.setResourcesId(disputeMediation.getMediationId());
            resMarkerService.saveOrUpdateResMarker(resMarker);
        }
		
		DisputeMediationRes disputeMediationRes = new DisputeMediationRes();
		disputeMediationRes.setMediator(disputeMediation.getMediator());
		disputeMediationRes.setMediationResult(disputeMediation.getMediationResult());
		disputeMediationRes.setIsSuccess(disputeMediation.getIsSuccess());
//		if("on".equals(disputeMediation.getIsSuccess())){
//			disputeMediationRes.setIsSuccess("1");
//		}else{
//			disputeMediationRes.setIsSuccess("0");
//		}
		disputeMediationRes.setMediationId(disputeMediation.getMediationId());
		disputeMediationRes.setStatus((short) 1);
		disputeMediationRes.setMediationTel(disputeMediation.getMediationTel());
		disputeMediationRes.setMediationOrgName(disputeMediation.getMediationOrgName());
		disputeMediationRes.setHjCertNumber(disputeMediation.getHjCertNumber());
		
		//int record =
		disputeMediationResMapper.insert(disputeMediationRes);
//		getFiles(disputeMediation.getMediationId());
		
	/*	if(result > 0) {//新增成功，增添个人积分
			Long disputeMediationOrgId = disputeMediation.getOrgId(),
				 createUserId = disputeMediation.getCreatorId();
			String orgCode = "";
			
			if(createUserId != null && createUserId > 0) {
				if(disputeMediationOrgId != null && disputeMediationOrgId > 0) {
					OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(disputeMediationOrgId);
					if(orgInfo != null) {
						orgCode = orgInfo.getOrgCode();
					}
				}
				
				if(StringUtils.isNotBlank(orgCode)) {
					String userPartyName = "";
					UserBO userBO = userManageService.getUserInfoByUserId(createUserId);
					UserDetailScore userDetailScore = new UserDetailScore();
					
					if(userBO != null) {
						userPartyName = userBO.getPartyName();
					}
					
					userDetailScore.setUserId(createUserId);
					userDetailScore.setUserName(userPartyName);
					userDetailScore.setOrgCode(orgCode);
					userDetailScore.setCreateBy(createUserId);
					userDetailScore.setScoreType(DISPUTE_SCORE_TYPE);
					userDetailScore.setRemark(String.valueOf(disputeMediationRes.getMediationId()));
					
					userScoreService.insertUserDetailScore(userDetailScore);
				}
			}
		}*/
		
		return disputeMediation.getMediationId();
	}
	
	@Override
	public String getMediationCode(){
		String mediationCode = DateUtils.formatDate(new Date(), "yyyyMMdd");
		String mediationSequence = disputeMediationMapper.getMediationSequence()+"";
		//if(mediationSequence.length())
		switch (mediationSequence.length()) {
		case 1:
			mediationCode = mediationCode + "_" + "0000" + mediationSequence;
			break;
		case 2:
			mediationCode = mediationCode + "_" + "000" + mediationSequence;
			break;
		case 3:
			mediationCode = mediationCode + "_" + "00" + mediationSequence;
			break;
		case 4:
			mediationCode = mediationCode + "_" + "0" + mediationSequence;
			break;
		case 5:
			mediationCode = mediationCode + "_" + mediationSequence;
			break;
		default:
			break;
		}
		return mediationCode;
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

	@Override
	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
		this.servletContext = servletContext;
	}

	private ServletContext servletContext;

	@Override
	public EUDGPagination findDisputeAndZeroPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo<1?1:pageNo;
		pageSize = pageSize<1?10:pageSize;
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		int count = disputeMediationMapper.findCountAndZero(params);
		if(params.get("infoOrgName")!=null){
			params.put("infoOrgNameLength", params.get("infoOrgName").toString().length());
		}
		//System.out.println(params.toString());
		List<DisputeMediation> list = disputeMediationMapper.findPageListAndZero(params, rowBounds);
		list = formatData(list, params.get("infoOrgCode").toString());
		return new EUDGPagination(count, list);
	}

	@Override
	public Long insertDisputeMediationZero(DisputeMediation disputeMediation) {
		disputeMediation.setDisputeCondition(disputeMediation.getGridName()+"今日无矛盾纠纷发生,一切正常");
		disputeMediationMapper.insertZero(disputeMediation);
		
		DisputeMediationRes disputeMediationRes = new DisputeMediationRes(); 
		disputeMediationRes.setMediationId(disputeMediation.getMediationId());
		disputeMediationRes.setStatus((short) 1);
		disputeMediationResMapper.insert(disputeMediationRes);
		//新增成功，增添个人积分
		Long disputeMediationOrgId = disputeMediation.getOrgId(),
			 createUserId = disputeMediation.getCreatorId();
		String orgCode = "";
		
		/*if(createUserId != null && createUserId > 0) {
			if(disputeMediationOrgId != null && disputeMediationOrgId > 0) {
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(disputeMediationOrgId);
				if(orgInfo != null) {
					orgCode = orgInfo.getOrgCode();
				}
			}
			
			if(StringUtils.isNotBlank(orgCode)) {
				String userPartyName = "";
				UserBO userBO = userManageService.getUserInfoByUserId(createUserId);
				UserDetailScore userDetailScore = new UserDetailScore();
				
				if(userBO != null) {
					userPartyName = userBO.getPartyName();
				}
				
				userDetailScore.setUserId(createUserId);
				userDetailScore.setUserName(userPartyName);
				userDetailScore.setOrgCode(orgCode);
				userDetailScore.setCreateBy(createUserId);
				userDetailScore.setScoreType(DISPUTE_SCORE_TYPE);
				userDetailScore.setRemark(String.valueOf(disputeMediationRes.getMediationId()));
				
				userScoreService.insertUserDetailScore(userDetailScore);
				
			}
		}*/
		return disputeMediation.getMediationId();
	}

	@Override
	public Integer findZeroIdByGridId(String gridId) {
		return disputeMediationMapper.findZeroIdByGridId(gridId);
	}

	/**
	 * 根据网格id删除零报告
	 */
	@Override


	public int deleteZero(String gridId,Long userId) {
		UserDetailScore userScore = new UserDetailScore(); 
		Integer zeroId = disputeMediationMapper.findZeroIdByGridId(gridId);
		if(zeroId!=null){
			//矛盾纠纷主键ID
			userScore.setRemark(zeroId.toString());
			//积分获得人userID
			userScore.setUserId(userId);
			//积分类型
			userScore.setScoreType(DISPUTE_SCORE_TYPE);
			userScoreService.deleteUserDetailScore(userScore);
			return disputeMediationMapper.deleteZero(gridId);
			
		}return 0;

	}
	/**
	 * 查询当天是否存在矛盾纠纷
	 * @param gridId
	 * @return
	 */@Override
	public int findCountByGridId(String gridId){
		return disputeMediationMapper.findCountByGridId(gridId);
	}
	 
	 @Override
		public ExpStruc getExpStruc(String ctlType, UserInfo userInfo, Map<String, Object> pmMap) {
			RowBounds rowBounds = new RowBounds(0, Integer.MAX_VALUE);
			Map<String, Object> params = new HashMap<String, Object>();
			String time = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			params.put("handleDateFlag", pmMap.get("handleDateFlag"));
			params.put("isSuccess", pmMap.get("isSuccess"));
			params.put("mediationDeadlineStart", pmMap.get("mediationDeadlineStart"));
			params.put("mediationDeadlineEnd", pmMap.get("mediationDeadlineEnd"));
			
			String keyWord = "";
			if (pmMap.get("keyWord") != null) {
				keyWord = pmMap.get("keyWord").toString();				
			}
			if(!StringUtils.isBlank(keyWord)){
				int a = keyWord.indexOf("%");
				if(a>=0){
					String astr = keyWord.replaceAll("%", "/%");
					System.out.println(astr);
					keyWord = astr;
				}
				int b = keyWord.indexOf("_");
				if(b>=0){
					String bstr = keyWord.replaceAll("_", "/_");
					System.out.println(bstr);
					keyWord = bstr;
				}
			}
			params.put("keyWord", keyWord);
			params.put("startHappenTime", pmMap.get("startHappenTime"));
			
			String endHappenTime = "";
			if (pmMap.get("endHappenTime") != null) {
				endHappenTime = pmMap.get("endHappenTime").toString();				
			}
			if(endHappenTime!=null && !"".equals(endHappenTime)){
				Calendar c = Calendar.getInstance();  
				Date date = null;
				try {
					date = sdf.parse(endHappenTime);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				c.setTime(date);
				int day = c.get(Calendar.DATE); 
				c.set(Calendar.DATE, day + 1);
				String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
				params.put("endHappenTime", dayAfter);
			}
			
			params.put("startCreateTime",  pmMap.get("startCreateTime"));
			params.put("endCreateTime",  pmMap.get("endCreateTime"));
			params.put("involveType",  pmMap.get("itype"));
			params.put("infoOrgCode",  pmMap.get("InfoOrgCode"));
			params.put("gridId",  pmMap.get("gridId"));
			
			String nanchang = "";
			if (pmMap.get("nanchang") != null) {
				nanchang = pmMap.get("nanchang").toString();
			}
			if (StringUtils.isNotBlank(nanchang)) {
				params.put("nanchang", nanchang);
			}
			String mediationType = "";
			if (pmMap.get("mediationType") != null) {
				mediationType = pmMap.get("mediationType").toString();				
			}
			if (StringUtils.isNotBlank(mediationType)) {
				params.put("mediationType", mediationType);
			}
			String disputeType = "";
			if (pmMap.get("disputeType") != null) {
				disputeType = pmMap.get("disputeType").toString();
			}
			if (StringUtils.isNotBlank(disputeType)) {
				params.put("disputeType", disputeType);
			}
			String noDisputeStatus = "";
			if (pmMap.get("noDisputeStatus") != null) {
				noDisputeStatus = pmMap.get("noDisputeStatus").toString();
			}
			if (StringUtils.isNotBlank(noDisputeStatus)) {
				params.put("noDisputeStatus", noDisputeStatus);
			}
			String search = ""; 
			if (pmMap.get("search") != null) {
				search = pmMap.get("search").toString();
			}
			if (StringUtils.isNotBlank(search)) {
				params.put("search", search);
			}
			//矛盾纠纷上报结案列表 导出排除草稿事件。
			if(CommonFunctions.isNotBlank(pmMap, "disputeStatuss")){
				String disputeStatuss = pmMap.get("disputeStatuss").toString();
				if (StringUtils.isNotBlank(disputeStatuss)) {
					params.put("disputeStatuss", disputeStatuss.split(","));
				}
			}
			
			ExpStruc expStruc = new ExpStruc(IExporter.EXP_LIST, "矛盾纠纷");
			//获取数据/
			List<DisputeMediation> bo=null;
			if(pmMap.get("nanchang") != null){//南昌矛盾纠纷一键筛选个性化
				bo = disputeMediationMapper.findPageListByCriteriaNC(params, rowBounds);
			}else{
				bo = disputeMediationMapper.findPageListByCriteria(params, rowBounds);
			}
			String infoOrgCode = pmMap.get("InfoOrgCode").toString();
			
			bo = formatData(bo, infoOrgCode);
			//数据处理
			List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>(bo.size());
			
			Double var = 0.00;
			for (int i = 0; i < bo.size(); i++) {
				DisputeMediation bean = bo.get(i);
				
				
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("gridName", 	"南昌市");
				item.put("gridPath", bean.getGridPath());
				item.put("happenAddr", bean.getHappenAddr());
				item.put("happenTimeStr", bean.getHappenTimeStr());
				item.put("disputeType1", bean.getBigType());
				item.put("disputeType2", bean.getSmallType());
				item.put("gdMoney", "");
				item.put("mediationTypeStr", bean.getMediationTypeStr());
				item.put("disputeScaleStr", bean.getDisputeScaleStr());
				item.put("mediationDeadlineStr", bean.getMediationDeadlineStr());
				item.put("mediationDateStr", bean.getMediationDateStr());
				item.put("disputeCondition", bean.getDisputeCondition());
				item.put("mediationResult", bean.getMediationResult());

				datas.add(item);
			}
			
			//excel字段设置
			List<String[]> keys = new ArrayList<String[]>();
			keys.add(new String[]{"所属市", "gridName"});
			keys.add(new String[]{"发生地区", "gridPath"});
			keys.add(new String[]{"发生详细地区", "happenAddr"});
			keys.add(new String[]{"发生时间", "happenTimeStr"});
			keys.add(new String[]{"类别大类", "disputeType1"});
			keys.add(new String[]{"类别子类", "disputeType2"});
			keys.add(new String[]{"涉湖分类", "createTime"});
			keys.add(new String[]{"化解方式", "mediationTypeStr"});
			keys.add(new String[]{"事件规模", "disputeScaleStr"});
			keys.add(new String[]{"化解时限", "mediationDeadlineStr"});
			keys.add(new String[]{"化解（具体时间）", "mediationDateStr"});
			keys.add(new String[]{"事件简介", "disputeCondition"});
			keys.add(new String[]{"化解情况", "mediationResult"});
			expStruc.setKeys(keys);
			expStruc.setVals(datas);
			return expStruc;
	 	
		}



	/**
	 * 根据矛盾纠纷ID 查询对应的化解信息
	 * @param id
	 * @return
	 */
	@Override
	public DisputeMediation searchResInfoById(Long id) {
		DisputeMediation disputeMediation = disputeMediationMapper.selectByPrimaryKey(id);
		DisputeMediationRes disputeMediationRes = disputeMediationResMapper.selectByMediationId(id);
		if(disputeMediationRes != null){
			disputeMediation.setMediationResId(disputeMediationRes.getMediationResId());
			disputeMediation.setIsSuccess(disputeMediationRes.getIsSuccess());
			disputeMediation.setMediationResult(disputeMediationRes.getMediationResult());
			disputeMediation.setMediator(disputeMediationRes.getMediator());
			disputeMediation.setMediationTel(disputeMediationRes.getMediationTel());
			disputeMediation.setMediationOrgName(disputeMediationRes.getMediationOrgName());
			disputeMediation.setHjCertNumber(disputeMediationRes.getHjCertNumber());
		}
		if(null != disputeMediation.getMediationType()){//
			String mediationTypeStr = dictionaryService.changeCodeToName("B417", disputeMediation.getMediationType(), null, true);
			disputeMediation.setMediationTypeStr(mediationTypeStr);
		}
		return disputeMediation;
	}

	/**
	 * 根据矛盾纠纷主表ID判断 化解信息是新增还是编辑
	 * @param disputeMediation
	 * @return
	 */
	@Override
	public boolean insertOrUpdateResovle(DisputeMediation disputeMediation) {
		DisputeMediationRes disputeMediationRes = disputeMediationResMapper.selectByMediationId(disputeMediation.getMediationId());
		int update = 0, result = 0;
		if(disputeMediationRes != null){
			update = disputeMediationMapper.updateByPrimaryKeySelective(disputeMediation);
			disputeMediationRes.setMediationResId(disputeMediationRes.getMediationResId());
			disputeMediationRes.setMediator(disputeMediation.getMediator());
			disputeMediationRes.setMediationResult(disputeMediation.getMediationResult());
			disputeMediationRes.setIsSuccess(disputeMediation.getIsSuccess());
			disputeMediationRes.setMediationId(disputeMediation.getMediationId());
			disputeMediationRes.setMediationTel(disputeMediation.getMediationTel());
			disputeMediationRes.setMediationOrgName(disputeMediation.getMediationOrgName());
			disputeMediationRes.setHjCertNumber(disputeMediation.getHjCertNumber());
			result = disputeMediationResMapper.updateByPrimaryKeySelective(disputeMediationRes);
		}else{
			update = disputeMediationMapper.updateByPrimaryKeySelective(disputeMediation);
			disputeMediationRes.setMediator(disputeMediation.getMediator());
			disputeMediationRes.setMediationResult(disputeMediation.getMediationResult());
			disputeMediationRes.setIsSuccess(disputeMediation.getIsSuccess());
			disputeMediationRes.setMediationId(disputeMediation.getMediationId());
			disputeMediationRes.setMediationTel(disputeMediation.getMediationTel());
			disputeMediationRes.setMediationOrgName(disputeMediation.getMediationOrgName());
			disputeMediationRes.setHjCertNumber(disputeMediation.getHjCertNumber());
			result = disputeMediationResMapper.insert(disputeMediationRes);
		}
		return result > 0 && update > 0;
	}

	@Override
	public List<Map<String, Object>> getGridDisputeNum(Map<String, Object> params) {
		return disputeMediationMapper.getGridDisputeNum(params);
	}

	@Override
	public List<Map<String, Object>> getGridDispiteTop(Map<String, Object> params) {
		List<Map<String, Object>> gridDispiteTop = disputeMediationMapper.getGridDispiteTop(params);
		for (Map<String, Object> obj : gridDispiteTop) {
			params.put("userId",obj.get("CREATOR_ID"));
			Map<String, Object> gridDisputeTopGridName = disputeMediationMapper.getGridDisputeTopGridName(params);
			obj.put("partyName",gridDisputeTopGridName.get("PARTY_NAME"));
			obj.put("gridName",gridDisputeTopGridName.get("GRID_NAME"));
		}
		return gridDispiteTop;
	}
}
