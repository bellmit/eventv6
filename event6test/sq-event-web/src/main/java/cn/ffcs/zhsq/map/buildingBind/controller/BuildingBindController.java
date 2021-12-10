package cn.ffcs.zhsq.map.buildingBind.controller;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.buildingBind.service.IBuildingBindService;
import cn.ffcs.zhsq.mybatis.domain.map.buildingBind.BuildingBindInfo;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 地图测试
 *
 * @Author sulch
 * @Date 2017-10-18 8:58
 */
@Controller
@RequestMapping(value="/zhsq/map/buildBindController")
public class BuildingBindController extends ZZBaseController {
    @Autowired
    private IBuildingBindService buildingBindService;

    /**
     * 3d地图首页
     * @param session
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value="/index")
    public String index(HttpSession session, HttpServletRequest request, ModelMap map){

        map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
        map.addAttribute("UI_DOMAIN", App.UI.getDomain(session));
        return "/map/buildingBind/index.ftl";
    }

    /**
     * 楼宇绑定首页
     * @param session
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value="/index_3d_binding")
    public String index_3d_binding(HttpSession session, HttpServletRequest request, ModelMap map){

        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
        String defaultOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE)
                .toString();
        map.addAttribute("defaultInfoOrgCode", defaultOrgCode);
        map.addAttribute("startGridName",defaultGridInfo.get(KEY_START_GRID_NAME));
        map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));

        return "/map/buildingBind/index_3d_binding.ftl";
    }

    /**
     * 获取楼宇数据
     * @param session
     * @param buildingName
     * @param infoOrgCode
     * @param gridId
     * @param isBind
     * @param isMarker
     * @param mapt
     * @param page
     * @param rows
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/buildingListData3d", method = RequestMethod.POST)
    public EUDGPagination buildingListData3d(
            HttpSession session,
            @RequestParam(value = "buildingName", required = false) String buildingName,
            @RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
            @RequestParam(value = "gridId", required = false) Long gridId,
            @RequestParam(value = "isBind", required = false) String isBind,
            @RequestParam(value = "isMarker", required = false) String isMarker,
            @RequestParam(value = "mapt", required = false) String mapt,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "rows") int rows) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("buildingName", buildingName);
        paramsMap.put("gridId", gridId);
        paramsMap.put("infoOrgCode", infoOrgCode);
        paramsMap.put("isBind", isBind);
        paramsMap.put("orgCode", userInfo.getOrgCode());
        paramsMap.put("orderBy", "letter2");// letter2 = buildingName asc, letter2= buildingName desc
        EUDGPagination eudPagination = new EUDGPagination();
        eudPagination = buildingBindService.findBuildingBindInfoPagination(page, rows, paramsMap);
        return eudPagination;
    }

    /**
     * 绑定楼宇地图页面
     * @param session
     * @param request
     * @param map
     * @param buildingId
     * @param gridId
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/toBindMap")
    public String toBindMap(HttpSession session, HttpServletRequest request, ModelMap map
            ,@RequestParam(value="buildingId", required = false) Long buildingId,
        @RequestParam(value="gridId", required = false) Long gridId) throws Exception{
        map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
        map.addAttribute("UI_DOMAIN", App.UI.getDomain(session));
        String forward = "/map/buildingBind/bind_map.ftl";
        return forward;
    }

    /**
     * 保存绑定信息
     * @param session
     * @param request
     * @param map
     * @param buildingId
     * @param mapType
     * @param oid
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/saveBuildingBindInfo")
    public Map<String, Object> saveBuildingBindInfo(HttpSession session, HttpServletRequest request, ModelMap map
            ,@RequestParam(value="buildingId") Long buildingId
            ,@RequestParam(value="mapType") String mapType
            ,@RequestParam(value="oid") Long oid) throws Exception{

        boolean saveFlag = false;
        BuildingBindInfo buildingBindInfo = new BuildingBindInfo();
        buildingBindInfo.setBuildingId(buildingId);
        buildingBindInfo.setOid(oid);
        buildingBindInfo.setMapType(mapType);
        saveFlag = buildingBindService.saveBuildingBindInfo(buildingBindInfo);
        Map<String, Object> result = new HashMap<String,Object>();
        result.put("flag", saveFlag);
        return result;
    }

    /**
     * 展示绑定的楼宇信息
     * @param session
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value="/show_building_bind_index")
    public String show_building_bind_index(HttpSession session, HttpServletRequest request, ModelMap map){

        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
        String defaultOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE)
                .toString();
        map.addAttribute("defaultInfoOrgCode", defaultOrgCode);
        map.addAttribute("startGridName", defaultGridInfo.get(KEY_START_GRID_NAME));
        map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
        map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
        map.addAttribute("UI_DOMAIN", App.UI.getDomain(session));
        return "/map/buildingBind/show_building_bind_index.ftl";
    }

    /**
     * 获取楼宇信息
     * @param session
     * @param request
     * @param map
     * @param mapType
     * @param oid
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/getBuildingBindInfo")
    public Map<String, Object> getBuildingBindInfo(HttpSession session, HttpServletRequest request, ModelMap map
            ,@RequestParam(value="mapType") String mapType
            ,@RequestParam(value="oid") Long oid) throws Exception{
        Map<String, Object> params = new HashMap<String, Object>();
        boolean saveFlag = false;
        if(oid != null){
            params.put("oid", oid);
        }
        if(StringUtils.isNotBlank(mapType)){
            params.put("mapType", mapType);
        }
        BuildingBindInfo buildingInfo = buildingBindService.findBuildingBindInfoByParams(params);
        Map<String, Object> result = new HashMap<String,Object>();
        result.put("buildingInfo", buildingInfo);
        return result;
    }

    /**
     * 显示多条轨迹信息，定位信息读取数据库
     * @param session
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value="/show_car_trajectory_index")
    public String show_car_trajectory_index(HttpSession session, HttpServletRequest request, ModelMap map){
        map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
        map.addAttribute("UI_DOMAIN", App.UI.getDomain(session));
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.HOUR, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String startTime = sdf.format(ca.getTime());
        String endTime = sdf.format(new Date());
        map.addAttribute("startTime", startTime);
        map.addAttribute("endTime", endTime);
        return "/map/buildingBind/show_car_trajectory_index.ftl";
    }

    /**
     * 读取轨迹信息
     * @param session
     * @param request
     * @param map
     * @param mapType
     * @param carIds
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/getTrajectoryDatasByCarIds")
    public Map<String, Object> getTrajectoryDatasByCarIds(HttpSession session, HttpServletRequest request, ModelMap map
            ,@RequestParam(value="mapType") String mapType
            ,@RequestParam(value="carIds") String carIds
            ,@RequestParam(value="startTime") String startTime
            ,@RequestParam(value="endTime") String endTime) throws Exception{
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();

        boolean saveFlag = false;
        if(StringUtils.isNotBlank(carIds)){
            params.put("carIds", carIds);
        }
        if(StringUtils.isNotBlank(mapType)){
            params.put("mapType", mapType);
        }
        if(StringUtils.isNotBlank(startTime)){
            params.put("startTime", startTime);
        }
        if(StringUtils.isNotBlank(endTime)){
            params.put("endTime", endTime);
        }
        resultMap = buildingBindService.findCarTrajectoryInfoByParams(params);
//        Map<String, Object> result = new HashMap<String,Object>();
//        result.put("resultMap", resultMap);
        return resultMap;
    }

    /**
     * 显示多条轨迹信息，定位信息是写死的
     * @param session
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value="/show_multitermCar_trajectory_index")
    public String show_multitermCar_trajectory_index(HttpSession session, HttpServletRequest request, ModelMap map){
        map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
        map.addAttribute("UI_DOMAIN", App.UI.getDomain(session));
        return "/map/buildingBind/show_multitermCar_trajectory_index.ftl";
    }

    /**
     * 显示一条轨迹新，定位信息是写死的
     * @param session
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value="/show_oneCar_trajectory_index")
    public String show_oneCar_trajectory_index(HttpSession session, HttpServletRequest request, ModelMap map){
        map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
        map.addAttribute("UI_DOMAIN", App.UI.getDomain(session));
        return "/map/buildingBind/show_oneCar_trajectory_index.ftl";
    }
}
