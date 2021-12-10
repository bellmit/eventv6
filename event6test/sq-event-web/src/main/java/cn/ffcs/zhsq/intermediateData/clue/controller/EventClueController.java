package cn.ffcs.zhsq.intermediateData.clue.controller;

import cn.ffcs.module.event.bo.Event;
import cn.ffcs.module.event.service.EventService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.eventReportRecord.domain.EventReportRecordInfo;
import cn.ffcs.shequ.eventReportRecord.service.IEventReportRecordService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.intermediateData.eventVerify.service.IEventVerifyBaseService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/zhsq/clue")
public class EventClueController extends ZZBaseController {

	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	
	@Autowired
	private IAttachmentService attachmentService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IEventReportRecordService eventReportRecordService;
	
	@Autowired
	private EventService wehatEventService;
	
	@Autowired
	private IEventVerifyBaseService eventVerifyBaseService;
	
	private static final String EVENT_WECHAT_SERVICE = "eventWechatService";
	
	/**
	 * 跳转微信事件列表
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping("/toList")
	public String toList(HttpSession session, 
			ModelMap map) {
		map.addAttribute("infoOrgCode", this.getDefaultInfoOrgCode(session));
		map.addAttribute("EVENT_VERIFY_STATUS_PCODE", ConstantValue.EVENT_VERIFY_STATUS_PCODE);
		
		return "/zzgl/intermediateData/eventWechat/list_eventWechat.ftl";
	}

	/**
	 * 跳转微信事件编辑页面
	 * @param session
	 * @param eventWechatId
	 * @param map
	 * @return
	 */
	@RequestMapping("/toEdit")
	public String toEdit(HttpSession session, 
			@RequestParam(value = "eventVerifyId") Long eventVerifyId,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> eventVerifyMap = new HashMap<String, Object>(),
							eventVerify = null;
		String infoOrgCode = null;
		
		eventVerifyMap.put("userOrgCode", userInfo.getOrgCode());
		
		eventVerify = eventVerifyBaseService.findEventVerifyById(eventVerifyId, eventVerifyMap);
		
		if(eventVerify == null) {
			eventVerify = new HashMap<String, Object>();
			
			infoOrgCode = this.getDefaultInfoOrgCode(session);
		} else if(CommonFunctions.isNotBlank(eventVerify, "infoOrgCode")) {
			infoOrgCode = eventVerify.get("infoOrgCode").toString();
		}
		
		if(StringUtils.isNotBlank(infoOrgCode)) {
			MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(infoOrgCode);
			if(gridInfo != null) {
				map.addAttribute("startGridId", gridInfo.getParentGridId());
				map.addAttribute("gridName", gridInfo.getGridName());
				map.addAttribute("gridId", gridInfo.getGridId());
			}
		}
		map.addAttribute("eventWechat", eventVerify);
		map.addAttribute("EVENT_WECHAT_ATTACHMENT_TYPE", ConstantValue.EVENT_WECHAT_ATTACHMENT_TYPE);
		map.addAttribute("imgDownPath", App.IMG.getDomain(session));//用于附件上传组件中的缩略图展示
		map.addAttribute("moduleCode", ConstantValue.EVENT_WECHAT_MODULE_CODE);
		map.addAttribute("markerOperation", ConstantValue.TITLE_MARKER);//标注操作
		
		return "/zzgl/intermediateData/eventWechat/add_eventWechat.ftl";
	}
	
	/**
	 * 跳转微信事件详情页面
	 * @param session
	 * @param eventWechatId
	 * @param map
	 * @return
	 */
	@RequestMapping("/toDetail")
	public String toDetail(HttpSession session, 
			@RequestParam(value = "eventVerifyId") Long eventVerifyId,
			@RequestParam(value = "isClosable", required=false, defaultValue="true") boolean isClosable,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> eventVerifyMap = new HashMap<String, Object>(),
							eventWechat = null;
		String infoOrgCode = null;
		
		eventVerifyMap.put("userOrgCode", userInfo.getOrgCode());

		eventWechat = eventVerifyBaseService.findEventVerifyById(eventVerifyId, eventVerifyMap);
		
		if(CommonFunctions.isNotBlank(eventWechat, "infoOrgCode")) {
			infoOrgCode = eventWechat.get("infoOrgCode").toString();
		}
		
		if(StringUtils.isNotBlank(infoOrgCode)) {
			MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(infoOrgCode);
			if(gridInfo != null) {
				map.addAttribute("gridId", gridInfo.getGridId());
			}
		}
		
		//获取相关联的事件信息
		EventReportRecordInfo eventReportRecordInfo = new EventReportRecordInfo();
		eventReportRecordInfo.setBizId(eventVerifyId);
		eventReportRecordInfo.setBizType(ConstantValue.EVENT_WECHAT_MODULE_CODE);
		
		eventReportRecordInfo = eventReportRecordService.findEventReportRecordInfoByEventId(eventReportRecordInfo);
		if(eventReportRecordInfo != null) {
			map.addAttribute("eventUrl", App.EVENT.getDomain(session) + "/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&eventId=" + eventReportRecordInfo.getEventId());
		}
		map.addAttribute("isClosable", isClosable);
		
		map.addAttribute("eventWechat", eventWechat);
		map.addAttribute("EVENT_WECHAT_ATTACHMENT_TYPE", ConstantValue.EVENT_WECHAT_ATTACHMENT_TYPE);
		map.addAttribute("imgDownPath", App.IMG.getDomain(session));//用于附件上传组件中的缩略图展示
		map.addAttribute("moduleCode", ConstantValue.EVENT_WECHAT_MODULE_CODE);
		map.addAttribute("markerOperation", ConstantValue.TITLE_MARKER);//标注操作
		
		
		return "/zzgl/intermediateData/eventWechat/detail_eventWechat.ftl";
	}
	
	/**
	 * 保存微信事件记录
	 * @param session
	 * @param eventWechat
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveEventWechat")
	public ResultObj saveEventWechat(HttpSession session, 
			@RequestParam Map<String, Object> eventVerify,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Long eventVerifyId = null;
		boolean result = false;
		Msg msg = Msg.EDIT;
		ResultObj resultObj = null;
		
		if(CommonFunctions.isNotBlank(eventVerify, "eventVerifyId")) {
			try {
				eventVerifyId = Long.valueOf(eventVerify.get("eventVerifyId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isBlank(eventVerify, "eventVerifyServiceName")) {
			eventVerify.put("eventVerifyServiceName", EVENT_WECHAT_SERVICE);
		}
		
		try {
			if(eventVerifyId != null && eventVerifyId > 0) { //修改
				result = eventVerifyBaseService.updateEventVerifyById(eventVerify, userInfo);
			} else { //新增
				result = eventVerifyBaseService.saveEventVerify(eventVerify) > 0;
				msg = Msg.ADD;
			}
			
			if(result && CommonFunctions.isNotBlank(eventVerify, "operateType")) {
				int operateType = Integer.valueOf(eventVerify.get("operateType").toString());
				String status = null;
				
				switch(operateType) {
					case 0: {//上报
						status = Event.HANDLE_STATUS_DEALING;
						break;
					}
					case 1: {//驳回
						status = Event.HANDLE_STATUS_BACK;
						break;
					}
				}
				
				if(StringUtils.isNotBlank(status) ) {
					Map<String, Object> eventWechatMap = eventVerifyBaseService.findEventVerifyById(eventVerifyId, null);
					String remark = "";
					Long bizId = -1L;
					
					if(CommonFunctions.isNotBlank(eventWechatMap, "bizId")) {
						try {
							bizId = Long.valueOf(eventWechatMap.get("bizId").toString());
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
					}
					
					if(bizId != null && bizId > 0) {
						if(CommonFunctions.isNotBlank(eventWechatMap, "remark")) {
							remark = eventWechatMap.get("remark").toString();
						}
						
						Event wechatEvent = new Event();
						wechatEvent.setEventId(bizId);
						wechatEvent.setHandleSatus(status);
						wechatEvent.setHandleCont(remark);
						
						wehatEventService.update(wechatEvent);
					}
				}
			}
			
			resultObj = msg.getResult(result);
		} catch(Exception e) {
			e.printStackTrace();
			resultObj = msg.getResult(e);
		}
		
		return resultObj;
	}

	/**
	 * 删除微信事件记录
	 * @param session
	 * @param eventWechatId
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delEventWechat")
	public ResultObj delEventWechat(HttpSession session, 
			@RequestParam(value = "eventVerifyId") Long eventVerifyId,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> eventVerify = new HashMap<String, Object>();
		
		boolean delResult = eventVerifyBaseService.deleteEventVerifyById(eventVerifyId, userInfo.getUserId(), eventVerify);
		
		return Msg.DELETE.getResult(delResult);
	}
	
	/**
	 * 加载微信事件记录
	 * @param session
	 * @param page		页码
	 * @param rows		每页记录
	 * @param params
	 * 			infoOrgCode	所属地域
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public EUDGPagination listData(HttpSession session, 
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", this.getDefaultInfoOrgCode(session));
		}
		if(CommonFunctions.isBlank(params, "eventVerifyServiceName")) {
			params.put("eventVerifyServiceName", EVENT_WECHAT_SERVICE);
		}
		params.put("userOrgCode", userInfo.getOrgCode());
		
		EUDGPagination pagination = eventVerifyBaseService.findEventVerifyPagination(page, rows, params);
		
		return pagination;
	}
	
	/**
	 * 获取事件上报所需参数
	 * @param session
	 * @param params
	 * 			eventWechatId	微信事件id
	 * @param map
	 * @return
	 * 		outerAttachmentIds	附件id，以英文逗号连接
	 */
	@ResponseBody
	@RequestMapping("/fetchParam4Report")
	public Map<String, Object> fetchParam4Report(HttpSession session, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long eventVerifyId = -1L;
		String outerAttachmentIds = "";
		
		if(CommonFunctions.isNotBlank(params, "eventVerifyId")) {
			try {
				eventVerifyId = Long.valueOf(params.get("eventVerifyId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		if(eventVerifyId != null && eventVerifyId > 0) {
			List<Attachment> attahmentList = attachmentService.findByBizId(eventVerifyId, ConstantValue.EVENT_WECHAT_ATTACHMENT_TYPE);
			if(attahmentList != null && attahmentList.size() > 0) {
				StringBuffer attachmentId = new StringBuffer("");
				
				for(Attachment attachment : attahmentList) {
					attachmentId.append(",").append(attachment.getAttachmentId());
				}
				
				outerAttachmentIds = attachmentId.substring(1);
			}
		}
		
		resultMap.put("outerAttachmentIds", outerAttachmentIds);
		
		return resultMap;
	}
}