package cn.ffcs.zhsq.map.labelLocation.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.GisMapConfigInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.map.labelLocation.service.ILabelLocationService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfGrid;
import cn.ffcs.zhsq.mybatis.domain.map.labelLocation.BuildingLLInfo;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

import com.alibaba.fastjson.JSONArray;

/**
 * 地理位置标注
 *
 * @Author sulch
 * @Date 2016-11-03 16:44
 */
@Controller
@RequestMapping(value = "/zhsq/map/labelLocation/labelLocationController")
public class LabelLocationController extends ZZBaseController {
    @Autowired
    private IFunConfigurationService configurationService;
    @Autowired
    private IFunConfigurationService funConfigurationService;
    @Autowired
    private IArcgisInfoService arcgisInfoService;
    @Autowired
    private IResMarkerService resMarkerService;
    @Autowired
    private ILabelLocationService labelLocationService;

    // 地图标注使用的地图维度（二维、三维）
    private final String MAP_TYPE_CODE = "MAP_TYPE_CODE";
    // 新地图
    private final String NEW_MAP_ENGINE = "005";
    // 旧地图
    private final String OLD_MAP_ENGINE = "004";
    // 二维
    private final String TWO_DIMENSION = "2";
    // 三维
    private final String THREE_DIMENSION = "3";
    // 新地图二维mapt
    private final String TWO_DIMENSION_MAPT_OF_NEWMAP = "5";
    // 新地图三维mapt
    private final String THREE_DIMENSION_MAPT_OF_NEWMAP = "30";
    // 旧地图二维mapt
    private final String TWO_DIMENSION_MAPT_OF_OLDMAP = "2";
    // 旧地图三维mapt
    private final String THREE_DIMENSION_MAPT_OF_OLDMAP = "20";
    //部件地图标注模块code
    private final String COMPONENTS_MODULE_CODE = "020130";

    /**
     * <p>Description:进入首页</p>
     * @param session
     * @param map
     * @return
     * @throws Exception
     * add sulch
     * 2015年10月27日 下午8:34:07
     */
    @RequestMapping(value = "/index")
    public String index(HttpSession session, ModelMap map, HttpServletRequest request) throws Exception {
        map.addAttribute("ZHSQ_EVENT", App.EVENT.getDomain(session));
        map.addAttribute("ZZGRID", App.ZZGRID.getDomain(session));
        map.addAttribute("SYSTEM", App.SYSTEM.getDomain(session));
        return "/zzgl/labelLocation/index.ftl";
    }

    /**
     * 获取当前登陆的组织的网格信息
     * @param session
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getDefaultGridInfoAndStartGrid")
    public String getDefaultGridInfoAndStartGrid(HttpSession session,
                                                 HttpServletRequest request) {
        Map<String, Object> gridInfos = new HashMap<String, Object>();
        MixedGridInfo mixedGridInfo = this.getMixedGridInfo(session,request);
        if(mixedGridInfo != null){
            gridInfos.put("defaultGridId", mixedGridInfo.getGridId());
            gridInfos.put("defaultGridName", mixedGridInfo.getGridName());
            gridInfos.put("defaultInfoOrgCode", mixedGridInfo.getInfoOrgCode());
        }

        String startInfoOrgCode = "";
        if(mixedGridInfo != null && StringUtils.isNotBlank(mixedGridInfo.getInfoOrgCode())){
            startInfoOrgCode =  configurationService.
                    changeCodeToValue("SELECT_REGION_SCOPE", "COMMON", IFunConfigurationService.CFG_TYPE_FACT_VAL, mixedGridInfo.getInfoOrgCode());
        }

        List<MixedGridInfo> mixedGridInfos = mixedGridInfoService.getMixedGridMappingListByOrgCode(startInfoOrgCode);//获取晋江的网格信息
        if(mixedGridInfos != null && mixedGridInfos.size()>0){
            gridInfos.put("startGridId", mixedGridInfos.get(0).getGridId());
        }

        String jsoncallback = request.getParameter("jsoncallback");
        jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(gridInfos) + ")";
        return jsoncallback;
    }

    /**
     * 根据搜索条件获取部件列表（包含部件的定位信息）
     * @param session
     * @param request
     * @param page
     * @param rows
     * @param searchStr
     * @param gridId
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/addressListData")
    public String addressListData(HttpSession session,
                                  HttpServletRequest request,
                                  @RequestParam(value="page", required=false) int page,
                                  @RequestParam(value="rows", required=false) int rows,
                                  @RequestParam(value="searchStr", required=false) String searchStr,
                                  @RequestParam(value="mapt", required=false) String mapt,
                                  @RequestParam(value="gridId", required=false) Long gridId) {
        Map<String, Object> params = new HashMap<String, Object>();
        if(page<=0){
            page=1;
        }
        String jsoncallback = request.getParameter("jsoncallback");
        EUDGPagination pagination = new EUDGPagination();
        if(StringUtils.isNotBlank(searchStr)){
            try {
                searchStr = URLDecoder.decode(searchStr, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.put("searchStr", searchStr);
        }
        if(gridId != null){
            params.put("gridId", gridId);
        }

        if(StringUtils.isNotBlank(mapt)){
            params.put("mapt", mapt);
        }
        params.put("markerType", COMPONENTS_MODULE_CODE);
        params.put("findType","components");
        pagination = labelLocationService.findLabelLocationPagination(page, rows, params);
        jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(pagination) + ")";
        //返回部件地理位置分页信息的json串
        return jsoncallback;
    }

    /**
     * 根据搜索条件获取楼宇列表(包含楼宇的定位信息)
     * @param session
     * @param request
     * @param page
     * @param rows
     * @param searchStr
     * @param gridId
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/buildingListData")
    public String buildingListData(HttpSession session,
                                   HttpServletRequest request,
                                   @RequestParam(value="page", required=false) int page,
                                   @RequestParam(value="rows", required=false) int rows,
                                   @RequestParam(value="searchStr", required=false) String searchStr,
                                   @RequestParam(value="mapt", required=false) String mapt,
                                   @RequestParam(value="gridId", required=false) Long gridId) {
        Map<String, Object> params = new HashMap<String, Object>();
        if(page<=0){
            page=1;
        }
        String jsoncallback = request.getParameter("jsoncallback");
        EUDGPagination pagination = new EUDGPagination();
        if(StringUtils.isNotBlank(searchStr)){
            try {
                searchStr = URLDecoder.decode(searchStr, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.put("searchStr", searchStr);

        }
        if(gridId != null){
            params.put("gridId", gridId);
        }
        if(StringUtils.isNotBlank(mapt)){
            params.put("mapt", mapt);
        }
        params.put("findType","building");
        pagination = labelLocationService.findLabelLocationPagination(page, rows, params);
        jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(pagination) + ")";
        //返回楼宇分页信息的json串
        return jsoncallback;
    }

    /**
     * 获取地图引擎配置信息
     * @param session
     * @param map
     * @param res
     * @param modularCode
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/getMapEngineInfo")
    public Map<String, Object> getMapEngineInfo(HttpSession session, ModelMap map, HttpServletResponse res
            ,@RequestParam(value="modularCode", required=false) String modularCode) throws Exception{
        Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
        MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(Long.valueOf(defaultGridInfo.get(KEY_START_GRID_ID).toString()), false);
        String mapTypeResult = "";
        if(modularCode!=null && !"".equals(modularCode)) {
            mapTypeResult = changeMapType(modularCode, session);
        }
        String mapEngineType = "";
        String markerType = "";
        if(gridInfo != null && StringUtils.isNotBlank(gridInfo.getMapType())){
            mapEngineType = gridInfo.getMapType();
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("mapEngineType", mapEngineType);
        result.put("mapType", mapTypeResult);
        result.put("ZHSQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
        result.put("ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
        result.put("markerType",markerType);
        return result;
    }

    /**
     * 加载地图页面
     * @param session
     * @param request
     * @param response
     * @param map
     * @param x
     * @param y
     * @param gridId
     * @param mapt
     * @param mapType
     * @param isEdit
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/outPlatCrossDomain")
    public String outPlatCrossDomain(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map
            ,@RequestParam(value="x", required=false) Double x
            ,@RequestParam(value="y", required=false) Double y
            ,@RequestParam(value="gridId", required=false) Long gridId
            ,@RequestParam(value="mapt", required=false) Integer mapt
            ,@RequestParam(value="mapType", required=false) String mapType
            ,@RequestParam(value="_callBackUrl", required=false) String callBackUrl
            ,@RequestParam(value="targetDownDivId", required=false) String targetDownDivId
            ,@RequestParam(value="isEdit", required=false) String isEdit) throws Exception{
        String forward = "/map/labelLocation/outplat_domain.ftl";
        if(mapt == null) {
            mapt = 5;
        }
        if(isEdit == null || "".equals(isEdit)) {
            isEdit = "true";
        }
        if("3".equals(mapType)) {
            mapt = 30;
        }else if("2".equals(mapType)) {
            mapt = 5;
        }
        if(x==null || y==null){
            if(gridId == null){
                MixedGridInfo gridInfo = getMixedGridInfo(session,request);
                //获取当前登录人员所在网格信息
                if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
                gridId = gridInfo.getGridId();
            }
            List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGrid(gridId ,mapt);
            if(list.size() == 1){
                x = list.get(0).getX();
                y = list.get(0).getY();
            }

        } else {
            if(gridId == null){
                MixedGridInfo gridInfo = getMixedGridInfo(session,request);
                //获取当前登录人员所在网格信息
                if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
                gridId = gridInfo.getGridId();
            }
        }
        if(x==null) {
            x = 0.0;
        }
        if(y == null) {
            y = 0.0;
        }
        MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, true);
        map.addAttribute("gridInfo", gridInfo);
        //-- 区域点位坐标转换成json数据
        String locationListJson = JSONArray.toJSONString(gridInfo.getLocationList());
        map.addAttribute("locationListJson", locationListJson);

        map.put("x", x);
        map.put("y", y);
        map.put("mapt", mapt);
        map.put("gridId", gridId);

        map.put("isEdit", isEdit);
        map.put("mapType", mapType);
        map.put("callBackUrl", callBackUrl);
        map.put("targetDownDivId", targetDownDivId);
        map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
        map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragrma", "no-cache");
        response.setDateHeader("Expires", 0);
        return forward;
    }

    /**
     * 地图标注回填坐标和地图类型
     * @param session
     * @param map
     * @param x
     * @param y
     * @param mapt
     * @return
     */
    @RequestMapping(value = "/toLabelLocationCrossDomain")
    public String toArcgisCrossDomain(HttpSession session, ModelMap map,
                                      @RequestParam(value = "x") String x,
                                      @RequestParam(value="y") String y,
                                      @RequestParam(value="mapt") Long mapt,
                                      @RequestParam(value="targetDownDivId") String targetDownDivId
                                      ) {
        map.addAttribute("x",x);
        map.addAttribute("y",y);
        map.addAttribute("mapt",mapt);
        map.addAttribute("targetDownDivId",targetDownDivId);
        return "/map/labelLocation/labelLocation_cross_domain.ftl";
    }

    /**
     * 获取该条业务id对应的定位信息
     * @param session
     * @param map
     * @param res
     * @param moduleTypeCode
     * @param bizId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/getMapLocationInfo")
    public Map<String, Object> getMapLocationInfo(HttpSession session, ModelMap map, HttpServletResponse res
            ,@RequestParam(value="moduleTypeCode", required=false) String moduleTypeCode
            ,@RequestParam(value="labelLocationType", required=false) String labelLocationType
            ,@RequestParam(value="bizId", required=false) Long bizId
            ,@RequestParam(value="resId", required=false) Long resId) throws Exception{
        ResMarker resMarker = new ResMarker();
        String mapTypeResult = "";
        mapTypeResult = changeMapType(moduleTypeCode, session);
        Map<String, Object> result = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(mapTypeResult)){
            result.put("mapt", mapTypeResult);
        }else{
            result.put("mapt", "2");
        }

        if(bizId != null && "map".equals(labelLocationType)) {
            resMarker = resMarkerService.findResMarkerByResId(moduleTypeCode, bizId, mapTypeResult);
            if (resMarker != null) {
                if (StringUtils.isNotBlank(resMarker.getX())) {
                    result.put("x", resMarker.getX());
                }
                if (StringUtils.isNotBlank(resMarker.getY())) {
                    result.put("y", resMarker.getY());
                }
                if (StringUtils.isNotBlank(resMarker.getMapType())) {
                    result.put("mapt", resMarker.getMapType());
                }
            }
        }else if("components".equals(labelLocationType)){//获取部件标注信息
            if(resId != null){
                resMarker = resMarkerService.findResMarkerByResId(COMPONENTS_MODULE_CODE, resId, mapTypeResult);
                if (resMarker != null) {
                    if (StringUtils.isNotBlank(resMarker.getX())) {
                        result.put("x", resMarker.getX());
                    }
                    if (StringUtils.isNotBlank(resMarker.getY())) {
                        result.put("y", resMarker.getY());
                    }
                    if (StringUtils.isNotBlank(resMarker.getMapType())) {
                        result.put("mapt", resMarker.getMapType());
                    }
                }
            }
        }else if("building".equals(labelLocationType)){//获取楼宇标注信息
            BuildingLLInfo buildingLLInfo = new BuildingLLInfo();
            Map<String, Object> params = new HashMap<String, Object>();
            if(resId != null){
                params.put("buildingId", resId);
            }
            if(StringUtils.isNotBlank(mapTypeResult)){
                params.put("mapt", mapTypeResult);
            }
            buildingLLInfo = labelLocationService.findBuildingLLInfo(params);
            if(buildingLLInfo != null){
                if(StringUtils.isNotBlank(buildingLLInfo.getX())){
                    result.put("x", buildingLLInfo.getX());
                }
                if (StringUtils.isNotBlank(buildingLLInfo.getY())) {
                    result.put("y", buildingLLInfo.getY());
                }
                if (buildingLLInfo.getMapt() != null) {
                    result.put("mapt", buildingLLInfo.getMapt());
                }
            }
        }
        return result;
    }

    /**
     * 转换地图类型
     * @param moduleTypeCode
     * @param session
     * @return
     */
    private String changeMapType(String moduleTypeCode, HttpSession session){
        Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
        MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(
                Long.valueOf(defaultGridInfo.get(KEY_START_GRID_ID).toString()), false);
        String mapEngineType = gridInfo.getMapType();

        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        String mapTypeResult = "";

        if(moduleTypeCode!=null && !"".equals(moduleTypeCode)) {
            mapTypeResult = this.funConfigurationService.turnCodeToValue(this.MAP_TYPE_CODE, moduleTypeCode,
                    IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(),
                    IFunConfigurationService.CFG_ORG_TYPE_0);
            // 如果是新地图引擎
            if (mapEngineType.equals(this.NEW_MAP_ENGINE)) {
                // 有配置值
                if (StringUtils.isNotBlank(mapTypeResult)) {
                    if (mapTypeResult.equals(this.TWO_DIMENSION)) { // 二维
                        mapTypeResult = this.TWO_DIMENSION_MAPT_OF_NEWMAP; // 二维mapt为5
                    } else if (mapTypeResult.equals(this.THREE_DIMENSION)) { // 三维
                        mapTypeResult = this.THREE_DIMENSION_MAPT_OF_NEWMAP; // 三维mapt为30
                    }
                }
            } else if (mapEngineType.equals(this.OLD_MAP_ENGINE)) { // 旧地图引擎
                // 有配置值
                if (StringUtils.isNotBlank(mapTypeResult)) {
                    if (mapTypeResult.equals(this.TWO_DIMENSION)) { // 二维
                        mapTypeResult = this.TWO_DIMENSION_MAPT_OF_OLDMAP;// 二维mapt为2
                    } else if (mapTypeResult.equals(this.THREE_DIMENSION)) {// 三维
                        mapTypeResult = this.THREE_DIMENSION_MAPT_OF_OLDMAP;// 三维mapt为20
                    }
                }
            } else {
                mapTypeResult = "5";
            }
            if(StringUtils.isNotBlank(mapTypeResult) && ("4".equals(mapTypeResult) ||  "20".equals(mapTypeResult))){
                GisMapConfigInfo mapConfig = gisInfoService.findGisMapConfigInfoByOrgCode(gridInfo.getInfoOrgCode());
                mapTypeResult = mapConfig.getMap_type_3D().toString();
            }
        }
        return mapTypeResult;
    }

}
