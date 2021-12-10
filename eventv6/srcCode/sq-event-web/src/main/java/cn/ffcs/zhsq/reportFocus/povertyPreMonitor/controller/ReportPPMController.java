package cn.ffcs.zhsq.reportFocus.povertyPreMonitor.controller;

import cn.ffcs.zhsq.reportFocus.povertyPreMonitor.service.impl.FocusReportNode360Enum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.controller.ReportTwoVioPreController;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Description: 致贫返贫监测
 * @Author: zhangtc
 * @Date: 2021/6/1 17:25
 */
@Controller
@RequestMapping("/zhsq/reportPPM")
public class ReportPPMController extends ReportTwoVioPreController {

//    @Autowired
//    private IBaseDictionaryService baseDictionaryService;

    /**
     * 跳转监测列表
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
    @Override
    @RequestMapping(value = "/toList")
    public String toList(HttpSession session,
                         @RequestParam(value = "listType") Integer listType,
                         @RequestParam(value = "reportType", required=false, defaultValue="11") String reportType,
                         @RequestParam Map<String, Object> params,
                         ModelMap map) {
        super.toList(session, listType, reportType, params, map);

        return "/zzgl/reportFocus/povertyPreMonitor/list_ppm.ftl";
    }

    /**
     * 跳转阅办列表
     * @param session
     * @param listType		列表类型
     * @param reportType	上报类型
     * @param params
     * @param map
     * @return
     */
    @Override
    @RequestMapping(value = "/toMsgReadingList")
    public String toMsgReadingList(HttpSession session,
                                   @RequestParam(value = "listType") Integer listType,
                                   @RequestParam(value = "reportType", required=false, defaultValue="11") String reportType,
                                   @RequestParam Map<String, Object> params,
                                   ModelMap map) {
        this.toList(session, listType, reportType, params, map);

        return "/zzgl/reportFocus/povertyPreMonitor/list_ppm_msgReading.ftl";
    }

    /**
     * 跳转疫情防控新增/编辑页面
     * @param session
     * @param reportUUID	上报UUID
     * @param reportType	上报类型
     * @param params		额外参数
     * @param map
     * @return
     */
    @Override
    @RequestMapping(value = "/toAdd")
    public String toAdd(HttpSession session,
                        @RequestParam(value = "reportUUID", required=false) String reportUUID,
                        @RequestParam(value = "listType") Integer listType,
                        @RequestParam(value = "reportType", required=false, defaultValue="11") String reportType,
                        @RequestParam Map<String, Object> params,
                        ModelMap map) {

        super.toAdd(session, reportUUID, listType, reportType, params, map);

        return "/zzgl/reportFocus/povertyPreMonitor/add_ppm.ftl";
    }

    /**
     * 跳转疫情防控详情页面
     * @param session
     * @param reportUUID	上报UUID
     * @param reportType	上报类型
     * @param params		额外参数
     * 			instanceId	流程实例id
     * @param map
     * @return
     */
    @Override
    @RequestMapping(value = "/toDetail")
    public String toDetail(HttpSession session,
                           @RequestParam(value = "reportUUID") String reportUUID,
                           @RequestParam(value = "listType") Integer listType,
                           @RequestParam(value = "reportType", required=false, defaultValue="11") String reportType,
                           @RequestParam Map<String, Object> params,
                           ModelMap map) {
        super.toDetail(session, reportUUID, listType, reportType, params, map);

        map.addAttribute("formType", FocusReportNode360Enum.FORM_TYPE.toString());

        if(CommonFunctions.isNotBlank(map,"isEditable") && Boolean.valueOf(String.valueOf(map.get("isEditable")))) {

//            Map<String, Object> reportFocusMap = null;
//
//            if(CommonFunctions.isNotBlank(map, "reportFocus")) {
//                reportFocusMap = (Map<String, Object>) map.get("reportFocus");
//            }
            return "/zzgl/reportFocus/povertyPreMonitor/detail_ppm_editable.ftl";
        }

        return "/zzgl/reportFocus/povertyPreMonitor/detail_ppm.ftl";
    }
}
