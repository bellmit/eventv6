package cn.ffcs.zhsq.reportFocus.poorSupportVisit.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.ffcs.zhsq.reportFocus.poorSupportVisit.service.impl.FocusReportNode356Enum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.controller.ReportTwoVioPreController;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * @Description: 扶贫走访（两不愁三保障一饮水）（Poor Support Visit)
 * @ClassName:   ReportPSVController   
 * @author:      张联松(zhangls)
 * @date:        2020年12月4日 上午9:15:05
 */
@Controller
@RequestMapping("/zhsq/reportPSV")
public class ReportPSVController extends ReportTwoVioPreController {
	/**
	 * 跳转扶贫走访列表
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
			@RequestParam(value = "reportType", required=false, defaultValue="7") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/poorSupportVisit/list_psv.ftl";
	}
	
	/**
	 * 跳转扶贫走访阅办列表页面
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
			@RequestParam(value = "reportType", required=false, defaultValue="7") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toMsgReadingList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/poorSupportVisit/list_psv_msgReading.ftl";
	}
	
	/**
	 * 跳转扶贫走访新增/编辑页面
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
			@RequestParam(value = "reportType", required=false, defaultValue="7") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toAdd(session, reportUUID, listType, reportType, params, map);
		map.addAttribute("defaultRegionCode", this.getDefaultInfoOrgCode(session));
		
		return "/zzgl/reportFocus/poorSupportVisit/add_psv.ftl";
	}
	
	/**
	 * 跳转扶贫走访详情页面
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
			@RequestParam(value = "reportType", required=false, defaultValue="7") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toDetail(session, reportUUID, listType, reportType, params, map);

		map.addAttribute("formType", FocusReportNode356Enum.FORM_TYPE.toString());
		
		if(CommonFunctions.isNotBlank(map,"isEditable") && Boolean.valueOf(String.valueOf(map.get("isEditable")))) {
			map.addAttribute("defaultRegionCode", this.getDefaultInfoOrgCode(session));
			
			return "/zzgl/reportFocus/poorSupportVisit/detail_psv_editable.ftl";
		}
		
		return "/zzgl/reportFocus/poorSupportVisit/detail_psv.ftl";
	}
	
	protected boolean isFrom12345(Object dataSource) {
        return false;
    }

    protected String getBizType(){
		return "";
	}
}
