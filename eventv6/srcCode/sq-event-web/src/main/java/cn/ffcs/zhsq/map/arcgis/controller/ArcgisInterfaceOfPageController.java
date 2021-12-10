package cn.ffcs.zhsq.map.arcgis.controller;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.GridAdmin;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IGridAdminService;
import cn.ffcs.shequ.zzgl.service.holidayinfo.IHolidayInfoService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.menuconfigure.service.IMenuConfigService;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;
import com.alibaba.fastjson.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * arcgis地图页面接口
 *
 * @Author sulch
 * @Date 2018-01-08 14:23
 */
@Controller
@RequestMapping(value = "/zhsq/map/arcgis/arcgisInterfaceOfPageController")
public class ArcgisInterfaceOfPageController extends ZZBaseController {

    @Autowired
    private IFunConfigurationService funConfigurationService;
    @Autowired
    private IMenuConfigService menuConfigService;
    @Autowired
    private IGridAdminService gridAdminService;

    /**
     * 根据网格管理员id获取网格管理员轨迹页面接口
     * @param session
     * @param request
     * @param response
     * @param map
     * @param gridAdminId       网格管理员id
     * @param startTime         轨迹开始时间（默认为一天前的当前时间）
     * @param endTime           轨迹结束时间（默认为当前时间）
     * @param showDetailFlag    是否显示网格管理员信息窗口，true为显示；false为不显示
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/toShowGridAdminTrajectory")
    public String toShowGridAdminTrajectory(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map
            , @RequestParam(value="gridAdminId", required=false) Long gridAdminId
            , @RequestParam(value = "queryDay", required=false) String queryDay
            , @RequestParam(value = "startTime", required=false) String startTime
            , @RequestParam(value = "endTime", required=false) String endTime
            , @RequestParam(value = "showDetailFlag", required=false) String showDetailFlag) throws Exception{
        String forward = "/map/arcgis/arcgis_interface_page/gridAdmin_trajectory__index.ftl";
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        MixedGridInfo gridInfo = getMixedGridInfo(session,request);
        //获取当前登录人员所在网格信息
        if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
        Long gridId = null;
        if(gridAdminId != null){
            GridAdmin gridAdmin = gridAdminService.findGridAdminById(gridAdminId);
            if(gridAdmin != null && gridAdmin.getGridId() != null){
                gridId = gridAdmin.getGridId();
            }
            if(gridAdmin != null && StringUtils.isNotBlank(gridAdmin.getInfoOrgCode())){
                if(gridAdmin.getInfoOrgCode().startsWith(ConstantValue.JIANGYIN_FUNC_ORG_CODE)){
                    map.addAttribute("jsJiangYinFlag","true");
                }
            }
        }else {
            if(gridInfo != null){
                gridId = gridInfo.getGridId();
            }
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		if (StringUtils.isBlank(queryDay)) {
			queryDay = dateFormat.format(calendar.getTime()).substring(0, 10);
		}
		if (StringUtils.isBlank(startTime)) {// 为空默认7点
			startTime = "07:00";
		}
		if (StringUtils.isBlank(endTime)) {// 为空默认21点
			endTime = "21:00";
		}
		map.addAttribute("queryDay", queryDay);
        map.addAttribute("startTime", startTime);
        map.addAttribute("endTime", endTime);
        map.addAttribute("currentTime", dateFormat.format(new Date()));
        
        if(StringUtils.isBlank(showDetailFlag)){
            showDetailFlag = "true";//默认弹出网格员的信息框
        }
        map.addAttribute("showDetailFlag", showDetailFlag);
        map.addAttribute("gridAdminId", gridAdminId);
        map.addAttribute("gridInfo", gridInfo);
        map.put("gridId", gridId);
        map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
        map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
        response.setHeader("Cache-Control","no-store");
        response.setHeader("Pragrma","no-cache");
        response.setDateHeader("Expires",0);
        return forward;
    }
}
