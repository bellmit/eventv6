package cn.ffcs.zhsq.reportFocus.enterpriseHiddenDanger.controller;

import java.util.Map;
import javax.servlet.http.HttpSession;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.reportFocus.enterpriseHiddenDanger.service.impl.EHDDataSourceEnum;
import cn.ffcs.zhsq.reportFocus.enterpriseHiddenDanger.service.impl.FocusReportNode351Enum;
import cn.ffcs.zhsq.reportFocus.reportFeedback.IReportFeedbackService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.controller.ReportTwoVioPreController;

/**
 * @Description: 企业安全隐患(Enterprise Hidden Danger)
 * @ClassName:   ReportEHDController   
 * @author:      张联松(zhangls)
 * @date:        2020年11月6日 下午3:20:49
 */
@Controller
@RequestMapping("/zhsq/reportEHD")
public class ReportEHDController extends ReportTwoVioPreController {
	
	/**
	 * 跳转企业安全隐患列表
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
			@RequestParam(value = "reportType", required=false, defaultValue="3") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		super.toList(session, listType, reportType, params, map);
		map.put("userOrgCode",userInfo.getOrgCode());
		return "/zzgl/reportFocus/enterpriseHiddenDanger/list_enterpriseHiddenDanger.ftl";
	}
	
	/**
	 * 跳转企业安全隐患阅办列表
	 * @param session
	 * @param listType		列表类型
	 * @param reportType	上报类型
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toMsgReadingList")
	public String toMsgReadingList(HttpSession session, 
			@RequestParam(value = "listType") Integer listType,
			@RequestParam(value = "reportType", required=false, defaultValue="3") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		this.toList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/enterpriseHiddenDanger/list_enterpriseHiddenDanger_msgReading.ftl";
	}
	
	/**
	 * 跳转企业安全隐患新增/编辑页面
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
			@RequestParam(value = "reportType", required=false, defaultValue="3") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toAdd(session, reportUUID, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/enterpriseHiddenDanger/add_enterpriseHiddenDanger.ftl";
	}
	
	/**
	 * 跳转企业安全隐患详情页面
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
			@RequestParam(value = "reportType", required=false, defaultValue="3") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toDetail(session, reportUUID, listType, reportType, params, map);
		
		map.addAttribute("formType", FocusReportNode351Enum.FORM_TYPE.toString());
		if(CommonFunctions.isNotBlank(map,"isEditable") && Boolean.valueOf(String.valueOf(map.get("isEditable")))){
			return "/zzgl/reportFocus/enterpriseHiddenDanger/detail_enterpriseHiddenDanger_editable.ftl";
		}
		return "/zzgl/reportFocus/enterpriseHiddenDanger/detail_enterpriseHiddenDanger.ftl";
	}

	@Override
	protected boolean isFrom12345(Object dataSource) {
		return EHDDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource);
	}

	@Override
	protected String getBizType() {
		return IReportFeedbackService.BizType.ENTERPRISE_HIDDEN_DANGER.getCode();
	}
}
