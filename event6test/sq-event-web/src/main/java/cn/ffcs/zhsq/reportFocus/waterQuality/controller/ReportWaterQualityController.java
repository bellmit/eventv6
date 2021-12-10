package cn.ffcs.zhsq.reportFocus.waterQuality.controller;

import java.util.Map;
import javax.servlet.http.HttpSession;

import cn.ffcs.zhsq.reportFocus.reportFeedback.IReportFeedbackService;
import cn.ffcs.zhsq.reportFocus.waterQuality.service.impl.FocusReportNode35401Enum;
import cn.ffcs.zhsq.reportFocus.waterQuality.service.impl.WaterQualityDataSourceEnum;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.controller.ReportTwoVioPreController;

/**
 * @Description: 流域水质(Water Quality)
 * @ClassName:   ReportWaterQualityController   
 * @author:      张联松(zhangls)
 * @date:        2020年11月19日 下午3:38:20
 */
@Controller
@RequestMapping("/zhsq/reportWQ")
public class ReportWaterQualityController extends ReportTwoVioPreController {

	/**
	 * 跳转流域水质列表
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
			@RequestParam(value = "reportType", required=false, defaultValue="5") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/waterQuality/list_waterQuality.ftl";
	}
	
	/**
	 * 跳转流域水质阅办列表
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
			@RequestParam(value = "reportType", required=false, defaultValue="5") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		this.toList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/waterQuality/list_waterQuality_msgReading.ftl";
	}
	
	/**
	 * 跳转流域水质新增/编辑页面
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
			@RequestParam(value = "reportType", required=false, defaultValue="5") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toAdd(session, reportUUID, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/waterQuality/add_waterQuality.ftl";
	}
	
	/**
	 * 跳转流域水质详情页面
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
			@RequestParam(value = "reportType", required=false, defaultValue="5") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toDetail(session, reportUUID, listType, reportType, params, map);
		
		map.addAttribute("formType", FocusReportNode35401Enum.FORM_TYPE.toString());
		if(CommonFunctions.isNotBlank(map,"isEditable") && Boolean.valueOf(String.valueOf(map.get("isEditable")))){
			return "/zzgl/reportFocus/waterQuality/detail_waterQuality_editable.ftl";
		}
		return "/zzgl/reportFocus/waterQuality/detail_waterQuality.ftl";
	}

	@Override
	protected boolean isFrom12345(Object dataSource) {
		return WaterQualityDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource);
	}

	@Override
	protected String getBizType() {
		return IReportFeedbackService.BizType.BASIN_WATER_QUALITY.getCode();
	}
}
