package cn.ffcs.zhsq.reportFocus.ruralHousing.controller;

import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import cn.ffcs.zhsq.reportFocus.ruralHousing.service.impl.FocusReportNode357Enum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.controller.ReportTwoVioPreController;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * @Description: 农村建房，一告知四到场
 * @ClassName:   ReportRuralHousingController   
 * @author:      张联松(zhangls)
 * @date:        2021年3月31日 上午10:16:37
 */
@Controller
@RequestMapping("/zhsq/reportRuralHousing")
public class ReportRuralHousingController extends ReportTwoVioPreController {
	/**
	 * 跳转农村建房列表
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
			@RequestParam(value = "reportType", required=false, defaultValue="8") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/ruralHousing/list_ruralHousing.ftl";
	}
	
	/**
	 * 跳转农村建房阅办列表
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
			@RequestParam(value = "reportType", required=false, defaultValue="8") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		this.toList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/ruralHousing/list_ruralHousing_msgReading.ftl";
	}
	
	/**
	 * 跳转农村建房新增/编辑页面
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
			@RequestParam(value = "reportType", required=false, defaultValue="8") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toAdd(session, reportUUID, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/ruralHousing/add_ruralHousing.ftl";
	}
	
	/**
	 * 跳转农村建房详情页面
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
			@RequestParam(value = "reportType", required=false, defaultValue="8") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		String forwardUrl = "/zzgl/reportFocus/ruralHousing/detail_ruralHousing.ftl";
		
		super.toDetail(session, reportUUID, listType, reportType, params, map);
		
		map.addAttribute("formType", FocusReportNode357Enum.FORM_TYPE.toString());
		
		if(CommonFunctions.isNotBlank(map,"isEditable") && Boolean.valueOf(String.valueOf(map.get("isEditable")))){
			forwardUrl = "/zzgl/reportFocus/ruralHousing/detail_ruralHousing_editable.ftl";
		}
		
		return forwardUrl;
	}
	
	@Override
	protected boolean isFrom12345(Object dataSource) {
		return false;
	}

	@Override
	protected String getBizType() {
		return "";
	}
}
