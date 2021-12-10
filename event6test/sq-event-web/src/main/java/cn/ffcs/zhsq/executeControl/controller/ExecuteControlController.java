package cn.ffcs.zhsq.executeControl.controller;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.executeControl.service.IControlLibraryService;
import cn.ffcs.zhsq.executeControl.service.IControlPersonnelService;
import cn.ffcs.zhsq.executeControl.service.IMonitorTaskService;
import cn.ffcs.zhsq.mybatis.domain.executeControl.ControlLibrary;
import cn.ffcs.zhsq.mybatis.domain.executeControl.MonitorTask;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @Description: 布控库管理模块控制器
 * @Author: dtj
 * @Date: 07-16 20:37:41
 * @Copyright: 2020 福富软件
 */

@Controller("executeControlController")
@RequestMapping("/zhsq/event/executeControl")
public class ExecuteControlController extends ZZBaseController {

    @Autowired
    private IControlLibraryService controlLibraryService; //注入布控库管理模块服务

    @Autowired
    private IControlPersonnelService controlPersonnelService; //注入布控库对象模块服务

    @Autowired
    private IMonitorTaskService monitorTaskService; //注入布控任务管理模块服务

    /**
     * 列表页面
     * @param request
     * @param session
     * @param map
     * @return
     */
    @RequestMapping("/index")
    public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
        Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
        map.addAttribute("gridId", defaultGridInfo.get(KEY_START_GRID_ID));
        map.addAttribute("regionCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
        UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //替换成本地常量
        map.put("userInfo", userInfo);
        map.put("sysDomain", App.SYSTEM.getDomain(session));
        map.put("uiDomain", App.UI.getDomain(session));
        return "/zzgl/executeControl/executeControl.ftl";
    }

    /**
     * 列表数据
     * @param request
     * @param session
     * @param map
     * @param bo
     * @param page
     * @param rows
     * @return
     */
    @ResponseBody
    @RequestMapping("/listData")
    public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,
                           ControlLibrary bo) {
        Map<String, Object> params = new HashMap<String, Object>();
        String gridCode = bo.getGridCode();
        if(gridCode==null || gridCode.toString().equals("")){
            Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(request.getSession());
            params.put("gridCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
        }else{
            params.put("gridCode",bo.getGridCode());
        }
        params.put("libType",bo.getLibType());
        params.put("name",bo.getName());
        EUDGPagination pagination = null;
        try {
            String token = controlLibraryService.getToken();
            pagination = controlLibraryService.searchList(bo, params,token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pagination;
    }

    /**
     * 获取数据
     * @param request
     * @param session
     * @param map
     * @param bo
     * @return
     */
    @ResponseBody
    @RequestMapping("/getTitle")
    public Object getTitle(HttpServletRequest request, HttpSession session, ModelMap map, ControlLibrary bo) {
        List<ControlLibrary> list = null;
        try {
            list = controlLibraryService.getTitle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 详情页面
     * @param request
     * @param session
     * @param map
     * @param id
     * @return
     */
    @RequestMapping("/detail")
    public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
                       Long id) {
        ControlLibrary bo = null;
        try {
            bo = controlLibraryService.searchById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.addAttribute("bo", bo);
        return "/zzgl/executeControl/detail_executeControl.ftl";
    }

    /**
     * 表单页面
     * @param request
     * @param session
     * @param map
     * @param id
     * @param myControlLibraryId
     * @return
     */
    @RequestMapping("/form")
    public Object form(HttpServletRequest request, HttpSession session, ModelMap map,Long id,
                       @RequestParam(value = "myControlLibraryId", required=false) Long myControlLibraryId) {
        UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //替换成本地常量
        ControlLibrary bo = new ControlLibrary();
        try {
            String token = controlLibraryService.getToken();
            if (id != null ) {
                //修改
                bo = controlLibraryService.searchById(id);
                map.put("bo", bo);
            }else {
                MonitorTask monitorTask = monitorTaskService.searchById(myControlLibraryId, token);
                map.put("monitorTask",monitorTask);
                //新增
                bo.setUserIds(String.valueOf(userInfo.getUserId()));
                map.put("bo", bo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("LIB_TYPE", ConstantValue.LIB_TYPE);
        return "/zzgl/executeControl/form_executeControl.ftl";
    }

    /**
     * 保存数据
     * @param request
     * @param session
     * @param map
     * @param bo
     * @return
     */
    @ResponseBody
    @RequestMapping("/save")
    public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
                       ControlLibrary bo) {
        UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //替换成本地常量
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            String token = controlLibraryService.getToken();
            String result = "fail";
            ArrayList<ControlLibrary> list = new ArrayList<ControlLibrary>();
            if (bo.getInfos() != null && bo.getInfos().size() > 0) {
                for (ControlLibrary info : bo.getInfos()) {
                    if (info.getId() == null ) { //新增
                        info.setCreator(userInfo.getUserId());  //设置创建人
                        info.setUserIds(String.valueOf(userInfo.getUserId()));
                        list.add(info);
                    } else { //修改
                        info.setUpdator(userInfo.getUserId());  //设置更新人
                        info.setUpdateTime(new Date()); //设置更新时间
                        boolean updateResult = controlLibraryService.update(info, token);
                        if (updateResult) {
                            result = "success";
                        }
                        resultMap.put("tipMsg","edit");
                    }
                }
                if (list != null && list.size() > 0){
                    Long count = controlLibraryService.batchInsert(list);
                    if (count > 0) {
                        result = "success";
                        resultMap.put("tipMsg","edit");
                    }
                }
            }
            //批量删除
            String delIds = request.getParameter("delIds");
            if (delIds != null && !"".equals(delIds)){
                String[] ids = delIds.split(",");
                Long count = controlLibraryService.batchDelete(ids);
                if (count > 0){
                    result = "success";
                }
            }
            resultMap.put("result", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

}
