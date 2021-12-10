package cn.ffcs.zhsq.reportFocus.environmentHealTreatment.controller;

import cn.ffcs.zhsq.reportFocus.environmentHealTreatment.service.impl.EHTDataSourceEnum;
import cn.ffcs.zhsq.reportFocus.environmentHealTreatment.service.impl.FocusReportNode361Enum;
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
 * @Description:
 * @Author: zhangtc
 * @Date: 2021/7/27 14:39
 */
@Controller
@RequestMapping("/zhsq/reportEHT")
public class ReportEHTController extends ReportTwoVioPreController {

    /**
     * 跳转列表
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
                         @RequestParam(value = "reportType", required=false, defaultValue="14") String reportType,
                         @RequestParam Map<String, Object> params,
                         ModelMap map) {
        super.toList(session, listType, reportType, params, map);

        return "/zzgl/reportFocus/environmentHealTreatment/list_eht.ftl";
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
                                   @RequestParam(value = "reportType", required=false, defaultValue="14") String reportType,
                                   @RequestParam Map<String, Object> params,
                                   ModelMap map) {
        this.toList(session, listType, reportType, params, map);

        return "/zzgl/reportFocus/environmentHealTreatment/list_eht_msgReading.ftl";
    }

    /**
     * 跳转新增/编辑页面
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
                        @RequestParam(value = "reportType", required=false, defaultValue="14") String reportType,
                        @RequestParam Map<String, Object> params,
                        ModelMap map) {

        super.toAdd(session, reportUUID, listType, reportType, params, map);

        String dataSource = null;
        Map<String,Object> reportFocus = null;
        if(CommonFunctions.isNotBlank(params,"dataSource")){
            dataSource = String.valueOf(params.get("dataSource"));
            if(EHTDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)){
                map.put("dataSourceName","网格员巡查发现");
            }else{
                map.put("dataSourceName","非网格员巡查发现");
            }
        }else if(null != map.get("reportFocus")){
            reportFocus = (Map<String, Object>) map.get("reportFocus");
            if(CommonFunctions.isNotBlank(reportFocus,"dataSourceName")){
                map.put("dataSourceName",reportFocus.get("dataSourceName"));
            }
        }

        return "/zzgl/reportFocus/environmentHealTreatment/add_eht.ftl";
    }

    /**
     * 跳转详情页面
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
                           @RequestParam(value = "reportType", required=false, defaultValue="14") String reportType,
                           @RequestParam Map<String, Object> params,
                           ModelMap map) {
        super.toDetail(session, reportUUID, listType, reportType, params, map);

        map.addAttribute("formType", FocusReportNode361Enum.FORM_TYPE.toString());

        if(CommonFunctions.isNotBlank(map,"isEditable") && Boolean.valueOf(String.valueOf(map.get("isEditable")))) {

            return "/zzgl/reportFocus/environmentHealTreatment/detail_eht_editable.ftl";
        }

        return "/zzgl/reportFocus/environmentHealTreatment/detail_eht.ftl";
    }

    @Override
    protected boolean isFrom12345(Object dataSource) {
        return EHTDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource);
    }

    @Override
    protected String getBizType() {
        return IReportFeedbackService.BizType.ENVIRONMENT_HEAL_TREATMENT.getCode();
    }
}
