package cn.ffcs.zhsq.intermediateData.clue.service.impl;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.zhsq.CoordinateInverseQuery.service.ICoordinateInverseQueryService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.intermediateData.clue.service.IEventClueService;
import cn.ffcs.zhsq.intermediateData.eventVerify.service.IEventVerifyBaseService;
import cn.ffcs.zhsq.mybatis.domain.CoordinateInverseQuery.CoordinateInverseQueryGridInfo;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("eventClueService")
@Transactional
public class EventClueServiceImpl implements IEventClueService {

	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private IEventDisposalService eventDisposalService;
	//@Autowired
	//private IEventDisposalWorkflowService eventDisposalWorkflowService;

	@Autowired
	private IEventVerifyBaseService eventVerifyBaseService;

	@Autowired
	private ICoordinateInverseQueryService coordinateInverseQueryService;

	@Autowired
	private IAttachmentService attachmentService;

	//private String url = "http://218.85.66.69:9080/pazz/service/clueFeedback.jsp";

	@Override
//	@Transactional(rollbackFor=Exception.class, propagation= Propagation.REQUIRED)
	public Long report(Map<String, Object> params, UserInfo userInfo) throws Exception {
		if(params.get("eventVerifyId") == null || params.get("eventType") == null) return -1L;
		Long eventVerifyId = (Long)params.get("eventVerifyId");
		String eventType = (String)params.get("eventType");
		Map<String, Object> verifyById = eventVerifyBaseService.findEventVerifyById(eventVerifyId, new HashMap<String, Object>());
		String eventName = "线索事件";
		String occurred = verifyById.get("occurred").toString();
		String content = verifyById.get("content").toString();
		Date happenTime = (Date)verifyById.get("happenTime");
		String contactUser = verifyById.get("contactUser").toString();
		String tel = verifyById.get("tel").toString();
		String x = verifyById.get("latitude").toString();
		String y = verifyById.get("longitude").toString();


		Map<String, Object> coordinateInverseParams = new HashMap<String, Object>();
		params.put("x", x);
		params.put("y", y);

		//Long gridId = null;
		String infoOrgCode = verifyById.get("infoOrgCode").toString();

		List<MixedGridInfo> mixedGridMappingListByOrgCode = mixedGridInfoService.getMixedGridMappingListByOrgCode(infoOrgCode);
		Long gridId1 = mixedGridMappingListByOrgCode.get(0).getGridId();

		List<CoordinateInverseQueryGridInfo> gridInfoList = coordinateInverseQueryService.findGridInfo(coordinateInverseParams);
		if(gridInfoList != null && gridInfoList.size() > 0) {
			CoordinateInverseQueryGridInfo queryGridInfo = gridInfoList.get(0);
			//gridId = queryGridInfo.getGridId();
			infoOrgCode = queryGridInfo.getInfoOrgCode();
//			eventVerify.put("infoOrgCode", queryGridInfo.getInfoOrgCode());
		}

		//event
		EventDisposal eventDisposal = new EventDisposal();
		eventDisposal.setGridId(gridId1);
		eventDisposal.setGridCode(infoOrgCode);
		eventDisposal.setEventName(eventName);
		eventDisposal.setType(eventType);
		eventDisposal.setHappenTimeStr(DateUtils.formatDate(happenTime, DateUtils.PATTERN_24TIME));
		eventDisposal.setOccurred(occurred);
		eventDisposal.setContent(content);
//		eventDisposal.setHandleDateStr(eventResult.getEvent().getHandleDate());
//		eventDisposal.setBizPlatform(eventResult.getEvent().getBizPlatform());
//		eventDisposal.setUrgencyDegree(eventResult.getEvent().getUrgency());
//		eventDisposal.setInfluenceDegree(eventResult.getEvent().getInfluence());
//		eventDisposal.setSource(eventResult.getEvent().getSource());
		eventDisposal.setContactUser(contactUser);
		eventDisposal.setTel(tel);
//		eventDisposal.setCreateTimeStr(eventResult.getEvent().getRegisterTimeStr());
		eventDisposal.setStatus(ConstantValue.EVENT_STATUS_DRAFT);
		eventDisposal.setBizPlatform("050");


		List<Attachment> attachmentList = attachmentService.findByBizId(eventVerifyId, ConstantValue.EVENT_CLUE_ATTACHMENT_TYPE);
		if(attachmentList!=null && attachmentList.size()>0){
			String attachmentIds = "";
			for(Attachment attachment : attachmentList){
				attachmentIds = attachmentIds + "," + attachment.getAttachmentId();
			}
			attachmentIds = attachmentIds.substring(1, attachmentIds.length());
			eventDisposal.setAttachmentIds(attachmentIds);
		}
		eventDisposal.setCreatorId(userInfo.getUserId());
		eventDisposal.setCreatorName(userInfo.getUserName());


		//保存事件
		Long eventId = eventDisposalService.saveEventDisposalReturnId(eventDisposal, userInfo);
		if(null == eventId || eventId.equals(-1L))
			throw new Exception("事件新增失败！");

		//获取workflowId
//		Long workflowId = eventDisposalWorkflowService.capWorkflowId(null, eventId, userInfo, null);
//		String instanceId = "";
//		if(workflowId!=null && workflowId>0){
//			Map<String, Object> extraParam = new HashMap<String, Object>();
//			extraParam.put("advice", "");
//			instanceId = eventDisposalWorkflowService.startFlowByWorkFlow(eventId, workflowId, ConstantValue.WORKFLOW_IS_NO_CLOSE, userInfo, extraParam);
//		}
//		if(StringUtils.isBlank(instanceId))
//			throw new Exception("事件启动失败！");
		//修改中间表状态
		Map<String, Object> verifyParms = new HashMap<String, Object>();
		verifyParms.put("eventVerifyId",eventVerifyId);
		verifyParms.put("status","02");//已上报状态
		verifyParms.put("infoOrgCode",infoOrgCode);
		verifyParms.put("eventId",eventId);
		eventVerifyBaseService.updateEventVerifyById(verifyParms,userInfo);
		//通知第三方
		JSONObject jsonParams = new JSONObject();
		jsonParams.put("appkey", "47122356D623E632E434CECC3EB5933B");
		jsonParams.put("code", "350600200000000049030");
		jsonParams.put("handleUser", userInfo.getPartyName());
		jsonParams.put("handleDepar", userInfo.getOrgName());
		jsonParams.put("happenTimeStr", DateUtils.getNow());
		jsonParams.put("happenResultStr", "事件受理");
		jsonParams.put("result", "1");//0驳回，1处理中，2处理完成
//		String s = HttpUtil.doPost(url, "jsondata="+jsonParams);
//		System.out.println(s);

		return eventId;
	}

	/*private void reportByEventId(Long eventId, UserInfo userInfo) throws Exception {
		//获取workflowId
		Long workflowId = eventDisposalWorkflowService.capWorkflowId(null, eventId, userInfo, null);
		String instanceId = "";
		if(workflowId!=null && workflowId>0){
			Map<String, Object> extraParam = new HashMap<String, Object>();
			extraParam.put("advice", "");
			instanceId = eventDisposalWorkflowService.startFlowByWorkFlow(eventId, workflowId, ConstantValue.WORKFLOW_IS_NO_CLOSE, userInfo, extraParam);
		}
		if(StringUtils.isBlank(instanceId))
			throw new Exception("事件启动失败！");
	}*/

	@Override
//	@Transactional(rollbackFor=Exception.class, propagation= Propagation.REQUIRED)
	public Long report(Long id, String eventType, UserInfo userInfo) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eventVerifyId",id);
		params.put("eventType",eventType);
		return this.report(params, userInfo);
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation= Propagation.REQUIRED)
	public Boolean reject(Long id, UserInfo userInfo) throws Exception{
		//修改中间表状态
		Map<String, Object> verifyParms = new HashMap<String, Object>();
		verifyParms.put("eventVerifyId",id);
		verifyParms.put("status","03");//已上报状态
		boolean updateEventVerifyById = eventVerifyBaseService.updateEventVerifyById(verifyParms, userInfo);
		//通知第三方

		JSONObject params = new JSONObject();
		params.put("appkey", "47122356D623E632E434CECC3EB5933B");
		params.put("code", "350600200000000049030");
		params.put("handleUser", userInfo.getPartyName());
		params.put("handleDepar", userInfo.getOrgName());
		params.put("happenTimeStr", DateUtils.getNow());
		params.put("happenResultStr", "驳回");
		params.put("result", "0");//0驳回，1处理中，2处理完成
		System.out.println(params.toString());
//		String s = HttpUtil.doPost(url, "jsondata="+params);
//		System.out.println(s);
		return updateEventVerifyById;
	}

	public void save(Map<String, Object> params){

	}


}