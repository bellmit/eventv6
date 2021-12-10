package cn.ffcs.zhsq.reportFocus.statistics.controller;

import cn.ffcs.system.publicFilter.CommonController;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.statistics.IReportFocusStatisticsService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.JXLExcelUtil;
import cn.ffcs.zhsq.utils.domain.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: zhangtc
 * @Date: 2021/3/30 17:23
 */
@Controller
@RequestMapping("/zhsq/statistics")
public class StatisticsController extends ZZBaseController {
    @Autowired
    private IReportFocusStatisticsService reportFocusStatisticsService;

    /**
     * 数据详情首页
     * */
    @RequestMapping(value = "/toReportForStatistics")
    public String toIndex(HttpSession session,
                          @RequestParam Map<String,Object> params,
                          ModelMap map){
        Map<String,Object> defaultGridInfo =  this.getDefaultGridInfo(session);
        String beginTime = null,
                endTime = null;
                //forwardPath = "/zzgl/reportFocus/statistics/view_report_statistics.ftl"

        map.addAllAttributes(params);
        //默认地域orgId
        map.addAttribute("startInfoOrgId", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_ID));
        map.addAttribute("gridId", defaultGridInfo.get(KEY_START_GRID_ID));
        //默认地域编码
        map.addAttribute("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));

        map.addAttribute("PLATFORM_DOMAIN_ROOT", App.TOP.getDomain(session));
        map.addAttribute("RESIDENT_IMPEXP_DOMAIN", App.IMPEXP.getDomain(session));

        //默认时间
        beginTime = DateUtils.getToday();
        endTime = DateUtils.getToday();

        map.addAttribute("beginTime",beginTime);
        map.addAttribute("endTime",endTime);

        return "/zzgl/reportFocus/statistics/view_report_statistics.ftl";
    }

    /**
     * 数据列表
     * */
    @ResponseBody
    @RequestMapping(value = "/listData")
    public List<Map<String,Object>> listData(HttpSession session, @RequestParam Map<String,Object> params){
        List<Map<String,Object>> dataList = null;
        boolean flag = true;
        if (CommonFunctions.isNotBlank(params,"infoOrgCode")) {
            flag = super.checkDataPermission(session, CommonController.TYPE_GRID, params.get("infoOrgCode").toString());
        }

        //orgCode
        //查询时间：start-end
        //获取数据列表
        try {
            if(flag){
                dataList = reportFocusStatisticsService.findListData(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 疫情防控环节超期数据详情首页
     * */
    @RequestMapping(value = "/toEpcOverdueStatistics")
    public String toEpcOverdueStatistics(HttpSession session,
                          @RequestParam Map<String,Object> params,
                          ModelMap map){
        Map<String,Object> defaultGridInfo =  this.getDefaultGridInfo(session),
                            firstLastDay = new HashMap<>();
        String beginTime = null,
                endTime = null,
                reportType = ReportTypeEnum.epidemicPreControl.getReportTypeIndex();

        map.addAllAttributes(params);
        //默认地域orgId
        map.addAttribute("startInfoOrgId", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_ID));
        map.addAttribute("gridId", defaultGridInfo.get(KEY_START_GRID_ID));
        //默认地域编码
        map.addAttribute("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));

        map.addAttribute("PLATFORM_DOMAIN_ROOT", App.TOP.getDomain(session));
        map.addAttribute("RESIDENT_IMPEXP_DOMAIN", App.IMPEXP.getDomain(session));

        //默认时间
        beginTime = DateUtils.getMonthFirstDay();
        endTime = DateUtils.getToday();
        firstLastDay = DateUtils.getFirstday_Lastday_Month(new Date(),false);

        if(CommonFunctions.isNotBlank(firstLastDay,"last")){
            endTime = String.valueOf(firstLastDay.get("last"));
        }

        map.addAttribute("beginTime",beginTime);
        map.addAttribute("endTime",endTime);

        if(CommonFunctions.isNotBlank(params,"reportType")){
            reportType = String.valueOf(params.get("reportType"));
        }
        map.addAttribute("reportType",reportType);

        return "/zzgl/reportFocus/statistics/view_epcOverdue_statistics.ftl";
    }

    /**
     * 疫情防控环节超期数据列表
     * */
    @ResponseBody
    @RequestMapping(value = "/listEpcOverdueData")
    public EUDGPagination listEpcOverdueData(HttpSession session,
                                           @RequestParam(value = "page") int page,
                                           @RequestParam(value = "rows") int rows,
                                           @RequestParam Map<String,Object> params){
        EUDGPagination epcOverduePagination = null;
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        List<Map<String,Object>> dataList = null;

        params = params == null ? new HashMap<String, Object>() : params;
        boolean flag = true;

        if (CommonFunctions.isNotBlank(params,"infoOrgCode")) {
            flag = super.checkDataPermission(session, CommonController.TYPE_GRID, params.get("infoOrgCode").toString());
        }

        //orgCode
        //查询时间：start-end
        //获取数据分页
        try {
            if(flag){
                epcOverduePagination = reportFocusStatisticsService.findPagination4EpcOverdue(page, rows, params);
            }else{
                epcOverduePagination = new EUDGPagination(0, new ArrayList<Map<String, Object>>());
            }
        } catch (Exception e) {
            epcOverduePagination = new EUDGPagination();
            e.printStackTrace();
        }
        return epcOverduePagination;
    }

    /**
     * 导出数据
     * */
    @RequestMapping(value = "/exportEpcOverdueData")
    public void exportEpcOverdueData(HttpSession session, HttpServletResponse response,
                           HttpServletRequest request,
                           @RequestParam Map<String,Object> params){
        List<Map<String,Object>> dataList = null;
        String[] titles = null;
        String[] columns =null;
        Integer[] columnWidths = null;
        String excelName="超时环节统计报表",
                reportType = ReportTypeEnum.epidemicPreControl.getReportTypeIndex(),
                modelName = ReportTypeEnum.epidemicPreControl.getReportTypeName();

        if(CommonFunctions.isNotBlank(params,"reportType")){
            reportType = String.valueOf(params.get("reportType"));
        }

        switch (reportType) {
            case "1":
                //两违防治
                modelName = "两违防治";
                break;
            case "2":
                //房屋安全隐患
                modelName = "房屋安全隐患";
                break;
            case "3":
                //企业安全隐患
                modelName = "企业安全隐患";
                break;
            case "4":
                //疫情防控
                modelName = "疫情防控";
                break;
            case "5":
                //流域水质
                modelName = "流域水质";
                break;
            case "6":
                //三会一课
                modelName = "三会一课";
                break;
            case "7":
                //扶贫走访
                modelName = "扶贫走访";
                break;
            case "8":
                //农村建房
                modelName = "农村建房";
                break;
            case "9":
                //森林防灭火
                modelName = "森林防灭火";
                break;
            case "10":
                //营商问题
                modelName = "营商问题";
                break;
            case "11":
                //致贫返贫监测
                modelName = "致贫返贫监测";
                break;
            case "12":
                //信访人员稳控
                modelName = "信访人员稳控";
                break;
            case "13":
                //烈士纪念设施
                modelName = "烈士纪念设施";
                break;
            case "14":
                //环境卫生问题处置
                modelName = "环境卫生问题处置";
                break;
            case "15":
                //三合一整治
                modelName = "三合一整治";
        }

        excelName= modelName + "超时环节统计报表";

        try {
            dataList = reportFocusStatisticsService.findListDataOfEpcOverdue(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        titles=new String[] { "报告编号","所属镇","所属村","所属网格", "超时环节","超时人员","超时时长(小时)" };
        columns = new String[] { "reportCode","streetName","communityName", "gridName","taskName","transactor","interTime"};
        columnWidths = new Integer[] { 30, 30, 30,30, 50, 60, 30};

        JXLExcelUtil t= new JXLExcelUtil<Map<String, Object>>();
        Map map = new HashMap();
        map.put("list", dataList);
        map.put("columns", columns);
        map.put("titles", titles);
        map.put("widths", columnWidths);
        map.put("name", excelName);
        t.buildExcelDocument(map, request, response);
    }
}
