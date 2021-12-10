package cn.ffcs.zhsq.reportFocus.houseHiddenDanger.controller;

import cn.ffcs.zhsq.reportFocus.houseHiddenDanger.service.impl.FocusReportNode35301Enum;
import cn.ffcs.zhsq.reportFocus.houseHiddenDanger.service.impl.HHDDataSourceEnum;
import cn.ffcs.zhsq.reportFocus.reportFeedback.IReportFeedbackService;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.controller.ReportTwoVioPreController;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Description: 房屋安全隐患事件（House Hidden Danger）
 * @ClassName:   ReportHHDController   
 * @author:      张联松(zhangls)
 * @date:        2020年11月20日 下午3:21:05
 */
@Controller
@RequestMapping("/zhsq/reportHHD")
public class ReportHHDController extends ReportTwoVioPreController {
	
	/**
	 * 跳转房屋安全隐患列表
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
			@RequestParam(value = "reportType", required=false, defaultValue="2") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/houseHiddenDanger/list_hhd.ftl";
	}
	
	/**
	 * 跳转房屋安全隐患阅办列表页面
	 * @param session
	 * @param listType
	 * @param reportType
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toMsgReadingList")
	public String toMsgReadingList(HttpSession session, 
			@RequestParam(value = "listType") Integer listType,
			@RequestParam(value = "reportType", required=false, defaultValue="2") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toMsgReadingList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/houseHiddenDanger/list_hhd_msgReading.ftl";
	}
	
	/**
	 * 跳转房屋安全隐患新增/编辑页面
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
			@RequestParam(value = "reportType", required=false, defaultValue="2") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {

		params.put("isCapResMarkerInfo",true);

		super.toAdd(session, reportUUID, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/houseHiddenDanger/add_hhd.ftl";
	}
	
	/**
	 * 跳转房屋安全隐患详情页面
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
			@RequestParam(value = "reportType", required=false, defaultValue="2") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		params.put("isCapResMarkerInfo",true);

		super.toDetail(session, reportUUID, listType, reportType, params, map);

		map.addAttribute("formType", FocusReportNode35301Enum.FORM_TYPE.toString());
		if(CommonFunctions.isNotBlank(map,"isEditable") && Boolean.valueOf(String.valueOf(map.get("isEditable")))){
			return "/zzgl/reportFocus/houseHiddenDanger/detail_hhd_editable.ftl";
		}
		return "/zzgl/reportFocus/houseHiddenDanger/detail_hhd.ftl";
	}

	@Override
	protected boolean isFrom12345(Object dataSource) {
		return HHDDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource);
	}

	@Override
	protected String getBizType() {
		return IReportFeedbackService.BizType.HOUSE_HIDDEN_DANGER.getCode();
	}
}
