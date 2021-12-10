package cn.ffcs.zhsq.timeApplication.controller;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationReportForDelEventService;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:事件删除列表数据详情页面控制器
 * @Author: zhangtc
 * @Date: 2018/12/4 15:21
 */
@Controller
@RequestMapping("/zhsq/timeApplicationReportFordelEvent")
public class TimeApplicationReportForDelEventController extends ZZBaseController {

    @Autowired
    private ITimeApplicationReportForDelEventService eventDetailService;

    /**
    * 数据详情首页
    * */
    @RequestMapping(value = "/toReportForDelEvent")
    public String toIndex(HttpSession session, 
    		@RequestParam(value = "reportType", required = false, defaultValue = "1") Integer reportType,
    		ModelMap map){
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String,Object> defaultGridInfo =  this.getDefaultGridInfo(session);
        String beginTime = null,
        	   endTime = null,
        	   forwardPath = "/zzgl/timeApplication/delEventApplication/view_report_delEvent.ftl";

        //默认地域orgId
        map.addAttribute("startInfoOrgId", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_ID));
        map.addAttribute("gridId", defaultGridInfo.get(KEY_START_GRID_ID));
        //默认地域编码
        map.addAttribute("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));

        map.addAttribute("PLATFORM_DOMAIN_ROOT", App.TOP.getDomain(session));
        map.addAttribute("RESIDENT_IMPEXP_DOMAIN", App.IMPEXP.getDomain(session));
        map.addAttribute("RESIDENT_IMPEXP_DOMAIN", App.IMPEXP.getDomain(session));

        //默认时间
        Calendar dayStart = Calendar.getInstance();
        beginTime = DateUtils.formatDate(dayStart,DateUtils.PATTERN_DATE);
        endTime = DateUtils.formatDate(dayStart,DateUtils.PATTERN_DATE);

        map.addAttribute("beginTime",beginTime);
        map.addAttribute("endTime",endTime);
        map.addAttribute("reportType", reportType);

        switch(reportType) {
	        case 2: {//督查
	        	forwardPath = "/zzgl/timeApplication/inspectApplication/view_report_delEvent.ftl";
	        	break;
	        }
        }
        
        return forwardPath;
    }

    /**
     * 数据列表
     * */
    @ResponseBody
    @RequestMapping(value = "/listData")
    public List<Map<String,Object>> listData(HttpSession session, @RequestParam Map<String,Object> params){
        List<Map<String,Object>> dataList = null;

        //orgCode
        //gridId
        //查询时间：start-end
        //获取数据列表
        try {
            dataList = eventDetailService.findListDataOfDelEvent(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 导出数据
     * */
    @RequestMapping(value = "/exportData")
    public void exportData(HttpSession session,HttpServletResponse response,
                           HttpServletRequest request, 
                           @RequestParam(value = "reportType", required = false, defaultValue = "1") Integer reportType,
                           @RequestParam Map<String,Object> params){
        List<Map<String,Object>> dataList = null;
        String[] titles = null;
        String[] columns =null;
        Integer[] columnWidths = null;
        String excelName="事件绩效考评表";

        try {
            dataList = eventDetailService.findListDataOfDelEvent(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        titles=new String[] { "网格名称","总删除量","总上报量", "删除率","占比率" };
        columns = new String[] { "GRID_NAME","TOTAL_NUM_DELTED","TOTAL_NUM_REPORTED", "PERCENT_DELETED","PERCENT_TAKING"};
        columnWidths = new Integer[] { 80, 30, 30,30, 30};
        
        switch(reportType) {
	        case 2: {//督查
	        	titles = new String[] { "网格名称","总督查量","总上报量", "督查率","占比率" };
	        	break;
	        }
        }
        
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
