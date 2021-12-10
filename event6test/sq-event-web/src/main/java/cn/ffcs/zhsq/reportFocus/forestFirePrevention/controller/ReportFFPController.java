package cn.ffcs.zhsq.reportFocus.forestFirePrevention.controller;

import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.ffcs.zhsq.reportFocus.forestFirePrevention.service.impl.FFPDataSourceEnum;
import cn.ffcs.zhsq.reportFocus.forestFirePrevention.service.impl.FocusReportNode358Enum;
import cn.ffcs.zhsq.reportFocus.reportFeedback.IReportFeedbackService;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.controller.ReportTwoVioPreController;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * @Description: 森林防灭火
 * @ClassName:   ReportFFPController   
 * @author:      张联松(zhangls)
 * @date:        2021年3月31日 上午10:16:15
 */
@Controller
@RequestMapping("/zhsq/reportFFP")
public class ReportFFPController extends ReportTwoVioPreController {
	/**
	 * 跳转森林防灭火列表
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
			@RequestParam(value = "reportType", required=false, defaultValue="9") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/forestFirePrevention/list_ffp.ftl";
	}
	
	/**
	 * 跳转森林防灭火阅办列表
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
			@RequestParam(value = "reportType", required=false, defaultValue="9") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		this.toList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/forestFirePrevention/list_ffp_msgReading.ftl";
	}
	
	/**
	 * 跳转森林防灭火新增/编辑页面
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
			@RequestParam(value = "reportType", required=false, defaultValue="9") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toAdd(session, reportUUID, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/forestFirePrevention/add_ffp.ftl";
	}
	
	/**
	 * 跳转森林防灭火详情页面
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
			@RequestParam(value = "reportType", required=false, defaultValue="9") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		String forwardUrl = "/zzgl/reportFocus/forestFirePrevention/detail_ffp.ftl";
		
		super.toDetail(session, reportUUID, listType, reportType, params, map);
		
		map.addAttribute("formType", FocusReportNode358Enum.FORM_TYPE.toString());
		
		if(CommonFunctions.isNotBlank(map,"isEditable") && Boolean.valueOf(String.valueOf(map.get("isEditable")))){
			forwardUrl = "/zzgl/reportFocus/forestFirePrevention/detail_ffp_editable.ftl";
		}
		
		return forwardUrl;
	}
	
	@Override
	protected boolean isFrom12345(Object dataSource) {
		return FFPDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource);
	}

	@Override
	protected String getBizType() {
		return IReportFeedbackService.BizType.FOREST_FIRE_PREVENTION.getCode();
	}
}
