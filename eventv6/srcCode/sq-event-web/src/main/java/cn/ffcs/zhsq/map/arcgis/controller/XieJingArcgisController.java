package cn.ffcs.zhsq.map.arcgis.controller;

import cn.ffcs.geo.mybatis.domain.geoDivision.GeoDivision;
import cn.ffcs.geo.standardAddress.service.IStandardAddressService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.geo.mybatis.domain.geoStdAddress.GeoStdAddress;
import cn.ffcs.geo.mybatis.domain.xieJingAddress.XieJingAddress;
import cn.ffcs.geo.xieJingAddress.service.IXieJingAddressService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfGrid;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang.StringUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 协警地址地图标注
 *
 * @Author sulch
 * @Date 2018-04-23 17:48
 */
@Controller
@RequestMapping(value="/zhsq/map/xiejingController")
public class XieJingArcgisController extends ZZBaseController {
    @Autowired
    private IArcgisInfoService arcgisInfoService;
    @Autowired
    private IXieJingAddressService xieJingAddressService;
    @Autowired
    private IStandardAddressService standardAddressService;


    @RequestMapping(value="/index")
    public String outPlatCrossDomain(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
             @RequestParam(value="x", required=false) Double x
            ,@RequestParam(value="y", required=false) Double y
            ,@RequestParam(value="gridId", required=false) Long gridId
            ,@RequestParam(value="mapt", required=false) Integer mapt
            ,@RequestParam(value="callBackUrl", required=false) String callBackUrl
            ,@RequestParam(value="mapType", required=false) String mapType
            ,@RequestParam(value="isEdit", required=false) String isEdit
            ,@RequestParam(value="infoOrgCode", required=false) String infoOrgCode
            ,@RequestParam(value="targetDownDivId", required=false) String targetDownDivId
            ,@RequestParam(value="nearbyAddressShow", required=false) String nearbyAddressShow
            ,@RequestParam(value="address", required=false) String address
            ,@RequestParam(value="showMap", required=false) String showMap
            ,@RequestParam(value="wgGisType", required=false) String wgGisType//两违TYPE
            ,@RequestParam(value="wgGisId", required=false) Long wgGisId//两违ID
            ) throws Exception{
        String forward = "/map/arcgis/arcgis_xiejing/list_xiejing.ftl";

        if(StringUtils.isNotBlank(infoOrgCode) && infoOrgCode.startsWith(ConstantValue.NANAN_FUNC_ORG_CODE)){//南安个性化地址选择器
            if (StringUtils.isNotBlank(showMap) && "1".equals(showMap)) {
                forward = "/map/arcgis/arcgis_xiejing/list_xiejing_nanAn.ftl";
            }
        }

        if(mapt == null) {
            mapt = 5;
        }
        if(isEdit == null || "".equals(isEdit)) {
            isEdit = "true";
        }
        if(StringUtils.isBlank(mapType) || "undefined".equals(mapType)){
            mapType = "2";
        }
        if("3".equals(mapType)) {
            mapt = 30;
        }else if("2".equals(mapType)) {
            mapt = 5;
        }
        Integer mapCenterLevel = 5;
        if(x==null || y==null || x==0.0 || y==0.0){
            if(gridId == null){
                if(StringUtils.isNotBlank(infoOrgCode)){
                    List<MixedGridInfo> gridInfos = mixedGridInfoService.getMixedGridMappingListByOrgCode(infoOrgCode);
                    if(gridInfos != null && gridInfos.size()>0){
                        gridId = gridInfos.get(0).getGridId();
                    }
                }else {
                    MixedGridInfo gridInfo = getMixedGridInfo(session, request);
                    //获取当前登录人员所在网格信息
                    if (gridInfo == null)
                        throw new Exception("未找到当前网格信息！！");
                    gridId = gridInfo.getGridId();
                }
            }
            //获取最接近改网格的且有数据的父级网格轮廓数据
            ArcgisInfoOfGrid arcgisInfoOfGrid = this.arcgisInfoService.getArcgisDataOfLastParentgrid(gridId, mapt);
            if(arcgisInfoOfGrid != null){
                x = arcgisInfoOfGrid.getX();
                y = arcgisInfoOfGrid.getY();
                mapCenterLevel = arcgisInfoOfGrid.getMapCenterLevel();
            }

            if(StringUtils.isNotBlank(address) && !address.equals("标注地理位置")){
                Map<String, Object> params = new HashMap<>();
                params.put("address", address.trim());
                params.put("number", 1);
                List<XieJingAddress> list = xieJingAddressService.findXiejingAddress(params);
                if(list != null && list.size() >0){
                    if(list.get(0).getX() >0 && list.get(0).getY() >0){
                        Float x1 = list.get(0).getX();
                        x = x1.doubleValue();

                        Float y1 = list.get(0).getY();
                        y = y1.doubleValue();
                        //获取地址的所属网格
                        if(StringUtils.isNotBlank(list.get(0).getRegionCode())){
                            List<MixedGridInfo> gridInfos = mixedGridInfoService.getMixedGridMappingListByOrgCode(list.get(0).getRegionCode());
                            if(gridInfos != null && gridInfos.size()>0){
                                gridId = gridInfos.get(0).getGridId();
                            }
                        }
                    }
                }
            }

        } else {
            if(gridId == null){
                if(StringUtils.isNotBlank(infoOrgCode)){
                    List<MixedGridInfo> gridInfos = mixedGridInfoService.getMixedGridMappingListByOrgCode(infoOrgCode);
                    if(gridInfos != null && gridInfos.size()>0){
                        gridId = gridInfos.get(0).getGridId();
                    }
                }else {
                    MixedGridInfo gridInfo = getMixedGridInfo(session, request);
                    //获取当前登录人员所在网格信息
                    if (gridInfo == null) throw new Exception("未找到当前网格信息！！");
                    gridId = gridInfo.getGridId();
                }
            }

            if(gridId != null){
                List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGrid(gridId ,mapt);
                if(list != null && list.size() == 1){
                    mapCenterLevel = list.get(0).getMapCenterLevel();
                }else{
                    //获取最接近改网格的且有数据的父级网格轮廓数据
                    ArcgisInfoOfGrid arcgisInfoOfGrid = this.arcgisInfoService.getArcgisDataOfLastParentgrid(gridId, mapt);
                    if(arcgisInfoOfGrid != null){
                        mapCenterLevel = arcgisInfoOfGrid.getMapCenterLevel();
                    }
                }
            }

        }
        if(x==null) {
            x = 0.0;
        }
        if(y == null) {
            y = 0.0;
        }
        if(mapCenterLevel == null){
            mapCenterLevel = 5;
        }
        if(StringUtils.isNotBlank(nearbyAddressShow)){
            map.put("nearbyAddressShow", nearbyAddressShow);
        }else{
            map.put("nearbyAddressShow", "true");
        }
        map.put("wgGisType", wgGisType);
        map.put("wgGisId", wgGisId);
        map.put("targetDownDivId", targetDownDivId);
        map.put("x", x);
        map.put("y", y);
        map.put("mapt", mapt);
        map.put("gridId", gridId);
        map.put("isEdit", isEdit);
        map.put("mapType", mapType);
        map.put("infoOrgCode", infoOrgCode);
        map.put("address", address);
        map.put("callBackUrl", callBackUrl);
        map.put("showMap", showMap);
        return forward;
    }

    @RequestMapping(value = "/xiejingMap")
    public String xiejingMap(HttpSession session, HttpServletRequest request,HttpServletResponse response,
             @RequestParam(value="x", required=false) Double x
            ,@RequestParam(value="y", required=false) Double y
            ,@RequestParam(value="gridId", required=false) Long gridId
            ,@RequestParam(value="mapt", required=false) Integer mapt
            ,@RequestParam(value="callBackUrl", required=false) String callBackUrl
            ,@RequestParam(value="mapType", required=false) String mapType
            ,@RequestParam(value="isEdit", required=false) String isEdit
            ,@RequestParam(value="infoOrgCode", required=false) String infoOrgCode
            ,@RequestParam(value="showMap", required=false) String showMap
            ,@RequestParam(value="targetDownDivId", required=false) String targetDownDivId
            ,@RequestParam(value="wgGisType", required=false) String wgGisType//两违TYPE
            ,@RequestParam(value="wgGisId", required=false) Long wgGisId//两违ID
            , ModelMap map) throws Exception {
        String forward = "/map/arcgis/arcgis_xiejing/outplat_domain_XIEJING.ftl";
        if(StringUtils.isNotBlank(infoOrgCode) && infoOrgCode.startsWith(ConstantValue.NANAN_FUNC_ORG_CODE)){//南安个性化地址选择器
            if (StringUtils.isNotBlank(showMap) && "1".equals(showMap)) {
                forward = "/map/arcgis/arcgis_xiejing/outplat_domain_XIEJING_nanAn.ftl";
            }
        }
        if(mapt == null) {
            mapt = 5;
        }
        if(isEdit == null || "".equals(isEdit)) {
            isEdit = "true";
        }
        if(StringUtils.isBlank(mapType) || "undefined".equals(mapType)){
            mapType = "2";
        }
        if("3".equals(mapType)) {
            mapt = 30;
        }else if("2".equals(mapType)) {
            mapt = 5;
        }
        Integer mapCenterLevel = 5;
        if(x==null || y==null || x<=0 || x<=0){
            if(gridId == null){
                if(StringUtils.isNotBlank(infoOrgCode)){
                    List<MixedGridInfo> gridInfos = mixedGridInfoService.getMixedGridMappingListByOrgCode(infoOrgCode);
                    if(gridInfos != null && gridInfos.size()>0){
                        gridId = gridInfos.get(0).getGridId();
                    }
                }else {
                    MixedGridInfo gridInfo = getMixedGridInfo(session, request);
                    //获取当前登录人员所在网格信息
                    if (gridInfo == null)
                        throw new Exception("未找到当前网格信息！！");
                    gridId = gridInfo.getGridId();
                }
            }
            //获取最接近改网格的且有数据的父级网格轮廓数据
            ArcgisInfoOfGrid arcgisInfoOfGrid = this.arcgisInfoService.getArcgisDataOfLastParentgrid(gridId, mapt);
            if(arcgisInfoOfGrid != null){
                x = arcgisInfoOfGrid.getX();
                y = arcgisInfoOfGrid.getY();
                mapCenterLevel = arcgisInfoOfGrid.getMapCenterLevel();
            }

        } else {
            if(gridId == null){
                if(StringUtils.isNotBlank(infoOrgCode)){
                    List<MixedGridInfo> gridInfos = mixedGridInfoService.getMixedGridMappingListByOrgCode(infoOrgCode);
                    if(gridInfos != null && gridInfos.size()>0){
                        gridId = gridInfos.get(0).getGridId();
                    }
                }else {
                    MixedGridInfo gridInfo = getMixedGridInfo(session, request);
                    //获取当前登录人员所在网格信息
                    if (gridInfo == null) throw new Exception("未找到当前网格信息！！");
                    gridId = gridInfo.getGridId();
                }
            }

            if(gridId != null){
                List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGrid(gridId ,mapt);
                if(list != null && list.size() == 1){
                    mapCenterLevel = list.get(0).getMapCenterLevel();
                }else{
                    //获取最接近改网格的且有数据的父级网格轮廓数据
                    ArcgisInfoOfGrid arcgisInfoOfGrid = this.arcgisInfoService.getArcgisDataOfLastParentgrid(gridId, mapt);
                    if(arcgisInfoOfGrid != null){
                        mapCenterLevel = arcgisInfoOfGrid.getMapCenterLevel();
                    }
                }
            }

        }
        if(x==null) {
            x = 0.0;
        }
        if(y == null) {
            y = 0.0;
        }
        if(mapCenterLevel == null){
            mapCenterLevel = 5;
        }
        if(callBackUrl == null){
            callBackUrl = "";
        }
        MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, true);
        map.addAttribute("gridInfo", gridInfo);

        //-- 区域点位坐标转换成json数据
        if(gridInfo != null && gridInfo.getLocationList() != null){
            String locationListJson = JSONArray.toJSONString(gridInfo.getLocationList());
            map.addAttribute("locationListJson", locationListJson);
        }else{
            map.addAttribute("locationListJson", null);
        }
        if(StringUtils.isNotBlank(targetDownDivId)){
            map.put("targetDownDivId", targetDownDivId);
        }
        map.put("wgGisType", wgGisType);
        map.put("wgGisId", wgGisId);
        map.put("x", x);
        map.put("y", y);
        map.put("mapCenterLevel", mapCenterLevel);
        map.put("mapt", mapt);
        map.put("gridId", gridId);

        map.put("isEdit", isEdit);
        map.put("mapType", mapType);
        map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
        map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
        map.put("callBackUrl", callBackUrl);
        response.setHeader("Cache-Control","no-store");
        response.setHeader("Pragrma","no-cache");
        response.setDateHeader("Expires",0);
        return forward;
    }

    @RequestMapping(value="/outPlatCrossDomainCallBack")
    public String outPlatCrossDomainCallBack(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
                                             @RequestParam(value="x", required=false) Double x
            ,@RequestParam(value="y", required=false) Double y
            ,@RequestParam(value="gridId", required=false) Long gridId
            ,@RequestParam(value="mapt", required=false) Integer mapt) throws Exception{
        String forward = "/map/arcgis/arcgis_draw/gis_cross_domain_back.ftl";
        map.put("gridId", gridId);
        map.put("x", String.valueOf(x));
        map.put("y", String.valueOf(y));
        map.put("mapt", mapt);
        return forward;
    }

    /**
     * 获取晋江协警地址
     * @param session
     * @param request
     * @param response
     * @param map
     * @param x
     * @param y
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/getAuxiliaryPoliceAddrsByXY")
    public EUDGPagination getAuxiliaryPoliceAddrsByXY(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map
            , @RequestParam(value="x", required=false) String x
            , @RequestParam(value="y", required=false) String y
            , @RequestParam(value="infoOrgCode", required=false) String infoOrgCode
            , @RequestParam(value="addressName", required=false) String addressName) throws Exception{
        List<GeoStdAddress> list = new ArrayList<GeoStdAddress>();
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            if(StringUtils.isNotBlank(x)){
                params.put("x",x);
            }
            if(StringUtils.isNotBlank(y)){
                params.put("y",y);
            }
            if(StringUtils.isNotBlank(addressName)){
                params.put("addressName",addressName);
            }
            if(StringUtils.isNotBlank(infoOrgCode)){
                params.put("regionCode",infoOrgCode);
            }
            list = xieJingAddressService.findNearbyListByParam(params);//AuxiliaryPolice.getAddrs(x, y);
        }catch (Exception e){
            list = new ArrayList<GeoStdAddress>();
        }

        String address = "";
        EUDGPagination eudgPagination = new EUDGPagination(0, null);;
        if(list != null && list.size()>0){
            eudgPagination = new EUDGPagination(list.size(), list);
        }else{
//			list.add("协警地址1");
//			list.add("协警地址11");
//			list.add("协警地址12");
//			list.add("协警地址13协警地址13协警地址13协警地址13协警地址13");
//			list.add("协警地址14");
//			list.add("协警地址15");
//			list.add("协警地址16");
//			list.add("协警地址17");
//			list.add("协警地址18");
//			list.add("协警地址19");
//			list.add("协警地址20");
//			list.add("协警地址21");
//			list.add("协警地址22");
//			list.add("协警地址23");
//			list.add("协警地址24");
//			list.add("协警地址25");
//			list.add("协警地址26");
//			list.add("协警地址15");
//			list.add("协警地址16");
//			list.add("协警地址17");
//			list.add("协警地址18");
//			list.add("协警地址19");
//			list.add("协警地址20");
//			list.add("协警地址21");
//			list.add("协警地址22");
//			list.add("协警地址23");
//			list.add("协警地址24");
//			list.add("协警地址25");
//			list.add("协警地址26");
        }
        eudgPagination = new EUDGPagination(list.size(), list);
        return eudgPagination;
    }

    @ResponseBody
    @RequestMapping("/getOnlineStandardAddr")
    public cn.ffcs.common.EUDGPagination getOnlineStandardAddr(HttpServletRequest request,
                                                               @RequestParam(value = "pageNo") int pageNo,
                                                               @RequestParam(value = "pageSize") int pageSize) {
        String infoOrgCode = request.getParameter("regionCode");
        Map<String, Object> params = super.getParameterMap(request);
        params.put("status", GeoDivision.USE_STATUS);// 默认取启用 的数据
        params.put("searchType", "XIEJING");
        params.put("isNanAn", "isNanAn");
        cn.ffcs.common.EUDGPagination pagination = new cn.ffcs.common.EUDGPagination();
        try {
            pagination = standardAddressService.findStandardAddressPagination(pageNo, pageSize, params,
                    infoOrgCode);
        }catch (Exception e){
            e.printStackTrace();
        }
        return pagination;
    }
}
