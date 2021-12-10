package cn.ffcs.zhsq.reportFocus.twoViolationPre.controller;

import cn.ffcs.gis.mybatis.domain.base.ResMarker;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.reportFocus.controller.ReportFocusController;
import cn.ffcs.zhsq.reportFocus.reportFeedback.IReportFeedbackService;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.FocusReportNode350Enum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.TwoVioPreDataSourceEnum;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.data.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 两违防治
 * @ClassName:   ReportTwoVioPreController   
 * @author:      张联松(zhangls)
 * @date:        2020年9月18日 下午4:34:55
 */
@Controller
@RequestMapping("/zhsq/reportTwoVioPre")
public class ReportTwoVioPreController extends ReportFocusController {

	@Autowired
	IReportFeedbackService reportFeedbackService;
	
	@Autowired
	private IAttachmentService attachmentService;
	
	/**
	 * 跳转两违防治列表
	 * @param session
	 * @param listType		列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 我发起的
	 * 			5 辖区所有
	 * @param reportType	上报类型
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public String toList(HttpSession session, 
			@RequestParam(value = "listType") Integer listType,
			@RequestParam(value = "reportType", required=false, defaultValue="1") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.put("userOrgCode",userInfo.getOrgCode());
		map.addAllAttributes(params);
		map.addAttribute("listType", listType);
		map.addAttribute("reportType", reportType);
		
		return "/zzgl/reportFocus/twoVioPre/list_twoVioPre.ftl";
	}
	
	/**
	 * 跳转我的阅办列表
	 * @param session
	 * @param listType		列表类型
	 * @param reportType	入格事件类型
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toMsgReadingList")
	public String toMsgReadingList(HttpSession session, 
			@RequestParam(value = "listType") Integer listType,
			@RequestParam(value = "reportType", required=false, defaultValue="1") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		this.toList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/twoVioPre/list_twoVioPre_msgReading.ftl";
	}
	
	/**
	 * 跳转两违防治新增/编辑页面
	 * @param session
	 * @param reportUUID	上报UUID
	 * @param reportType	上报类型
	 * @param params		额外参数
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd(HttpSession session, 
			@RequestParam(value = "reportUUID", required=false) String reportUUID,
			@RequestParam(value = "listType") Integer listType,
			@RequestParam(value = "reportType", required=false, defaultValue="1") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> reportFocusMap = null,
							defaultGridInfo = this.getDefaultGridInfo(session);
		List<OrgEntityInfoBO> orgEntityInfoList = null;
		String startDivisionCode = "-1";
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(StringUtils.isNotBlank(reportUUID)) {
			if(CommonFunctions.isBlank(params, "reportType")) {
				params.put("reportType", reportType);
			}

			//进行所属区域中文名称查询
			params.put("isCapRegionName",true);
			//获取定位信息
			params.put("isCapResMarkerInfo", true);

			reportFocusMap = reportIntegrationService.findReportFocusByUUID(reportUUID, userInfo, params);
		}
		
		if(reportFocusMap == null) {
			reportFocusMap = new HashMap<String, Object>();
			reportFocusMap.put("reporterName", userInfo.getPartyName());
			reportFocusMap.put("reporterTel", userInfo.getVerifyMobile());
			reportFocusMap.put("reportTimeStr", DateUtils.getNow());
		}
		
		//获取当前登录账号下所管理的所有网格
		orgEntityInfoList = userInfo.getInfoOrgList();

		if (null != orgEntityInfoList && orgEntityInfoList.size() > 0) {
			StringBuffer orgCodeSb = new StringBuffer("");
			String infoOrgCode = null;
			
			for (OrgEntityInfoBO orgEntityInfo : orgEntityInfoList) {
				infoOrgCode = orgEntityInfo.getOrgCode();
				
				if(StringUtils.isNotBlank(infoOrgCode)) {
					orgCodeSb.append(",").append(infoOrgCode);
				}
			}
			
			if(orgCodeSb.length() > 0) {
				startDivisionCode = orgCodeSb.substring(1);//标准地址库控件起始网格
			}
		}
		
		toAddOutParam(params);
		
		reportFocusMap.putAll(params);
		map.addAttribute("defaultGridIdStr", defaultGridInfo.get(KEY_START_GRID_ID).toString());
		map.addAttribute("startDivisionCode", startDivisionCode);//标准地址库控件起始网格
		map.addAttribute("maxReportTimeStr", DateUtils.getToday());
		map.addAttribute("reportFocus", reportFocusMap);
		map.addAttribute("reportType", reportType);
		
		return "/zzgl/reportFocus/twoVioPre/add_twoVioPre.ftl";
	}
	
	/**
	 * 跳转两违防治详情页面
	 * @param session
	 * @param reportUUID	上报UUID
	 * @param reportType	上报类型
	 * @param params		额外参数
	 * 			instanceId	流程实例id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toDetail")
	public String toDetail(HttpSession session, 
			@RequestParam(value = "reportUUID") String reportUUID,
			@RequestParam(value = "listType") Integer listType,
			@RequestParam(value = "reportType", required=false, defaultValue="1") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> reportFocusMap = null;
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		params = params == null ? new HashMap<String, Object>() : params;

		//是否进行所属区域中文名称的查询，由于可编辑环节判断在基本信息获取之后，所以详情默认获取
		params.put("isCapRegionName",true);
		reportFocusMap = reportIntegrationService.findReportFocusByUUID(reportUUID, userInfo, params);
		
		if(reportFocusMap != null && !reportFocusMap.isEmpty()) {
			params.put("isCapTaskList", false);
			
			if(listType == 2 || listType == 8 || listType == 9 || listType == 10 ) {
				if(CommonFunctions.isBlank(params, "reportFocusMap")) {
					params.put("reportFocusMap", reportFocusMap);
				}
			} else {
				params.put("isAble2Handle", false);
			}
			
			try {
				map.addAllAttributes(reportIntegrationService.initWorkflow4Handle(null, userInfo, params));
			} catch (Exception e) {
				map.addAttribute("msgWrong", e.getMessage());
				e.printStackTrace();
			}
		} else {
			reportFocusMap = new HashMap<String, Object>();
			map.addAttribute("msgWrong", "没有找到有效的记录信息！");
		}

		map.addAttribute("defaultGridIdStr", defaultGridInfo.get(KEY_START_GRID_ID).toString());
		map.addAttribute("reportFocus", reportFocusMap);
		map.addAllAttributes(params);
		map.addAttribute("reportType", reportType);
		map.addAttribute("formType", FocusReportNode350Enum.FORM_TYPE.toString());
		map.addAttribute("listType", listType);
        if(CommonFunctions.isNotBlank(reportFocusMap,"dataSource") && this.isFrom12345(reportFocusMap.get("dataSource"))){
            Long feedbackCount = reportFeedbackService.findReportFeedbackCount(reportUUID, this.getBizType(),null);
            if (feedbackCount!=null && feedbackCount>0) {
                map.addAttribute("bizType",this.getBizType());
                map.addAttribute("feedbackCount",feedbackCount);
            }
        }

		if(CommonFunctions.isNotBlank(map,"isEditable") && Boolean.valueOf(String.valueOf(map.get("isEditable")))){
			return "/zzgl/reportFocus/twoVioPre/detail_twoVioPre_editable.ftl";
		}
		
		return "/zzgl/reportFocus/twoVioPre/detail_twoVioPre.ftl";
	}

    protected boolean isFrom12345(Object dataSource) {
        return TwoVioPreDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource);
    }

    protected String getBizType(){
		return IReportFeedbackService.BizType.TWO_VIO_PRE_EVENT.getCode();
	}
    
    /**
     * 审核模块跳转调用设置参数
     * @param eventMap
     * @return 
     */
	private void toAddOutParam(Map<String, Object> eventMap) {
		
		
		if (CommonFunctions.isNotBlank(eventMap, "isEventVerify")) {

			// 构造定位resMarker
			if (CommonFunctions.isNotBlank(eventMap, "longitude") && CommonFunctions.isNotBlank(eventMap, "latitude")) {
				ResMarker resMarker = new ResMarker();
				resMarker.setX(eventMap.get("longitude").toString());
				resMarker.setY(eventMap.get("latitude").toString());
				resMarker.setMapType("5");
				eventMap.put("resMarker", resMarker);
			}
			
			//构造附件attachmentIds
			if (CommonFunctions.isNotBlank(eventMap, "outerAttachmentIds")) {// 附件id，以英文逗号分隔，没有进行判重处理

				String outerAttachmentIds = eventMap.get("outerAttachmentIds").toString();
				String[] outerAttachmentIdArray = outerAttachmentIds.split(",");
				Long attachmentId = -1L, attachmentIdAfter = -1L;
				Attachment attachment = null;

				StringBuffer attachmentIds = new StringBuffer("");
				String eventSeq = "";
				String attachmentType = "ZHSQ_EVENT";
				if (CommonFunctions.isNotBlank(eventMap, "attachmentType")) {
					attachmentType = eventMap.get("attachmentType").toString();
				}

				for (String outerAttachmentId : outerAttachmentIdArray) {
					attachmentId = -1L;

					if (StringUtils.isNotBlank(outerAttachmentId)) {
						try {
							attachmentId = Long.valueOf(outerAttachmentId);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}

					if (attachmentId > 0) {
						attachment = attachmentService.findById(attachmentId);

						if (attachment != null) {
							eventSeq = attachment.getEventSeq();

							if (StringUtils.isBlank(eventSeq)) {
								attachment.setEventSeq("1");
							}
							attachment.setAttachmentId(null);
							attachment.setAttachmentType(attachmentType);
							attachmentIdAfter = attachmentService.saveAttachment(attachment);

							if (attachmentIdAfter > 0) {
								attachmentIds.append(",").append(attachmentIdAfter);
							}
						}
					}
				}
				
				if (attachmentIds.length() > 0) {
					eventMap.put("attachmentIds", attachmentIds.substring(1));
				}
			}
		}
	}
    
	
}
