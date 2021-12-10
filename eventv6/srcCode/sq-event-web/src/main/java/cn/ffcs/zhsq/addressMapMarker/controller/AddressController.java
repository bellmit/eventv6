package cn.ffcs.zhsq.addressMapMarker.controller;

import cn.ffcs.geo.mybatis.domain.xieJingAddress.XieJingAddress;
import cn.ffcs.geo.xieJingAddress.service.IXieJingAddressService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfGrid;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 标准地址控制器
 *
 * @Author sulch
 * @Date 2018-08-01 9:04
 */
@Controller
@RequestMapping(value = "/zhsq/addressController")
public class AddressController extends ZZBaseController {
    @Autowired
    private IFunConfigurationService funConfigurationService;//功能配置
    @Autowired
    private IXieJingAddressService xieJingAddressService;//协警地址服务
    @Autowired
    private IMixedGridInfoService mixedGridInfoService;
    @Autowired
    private IArcgisInfoService arcgisInfoService;

    /**
     * 地址
     * @param session
     * @param isMarker
     * @param map
     * @return
     */
    @RequestMapping(value = "/index")
    public String index(HttpSession session,
                        @RequestParam(value = "isMarker", required = false) String isMarker,
                        @RequestParam(value = "addressId", required = false) Long addressId, ModelMap map) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
        String defaultOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE)
                .toString();

        map.addAttribute("defaultInfoOrgCode", defaultOrgCode);
        map.addAttribute("startGridName",
                defaultGridInfo.get(KEY_START_GRID_NAME));
        map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
        if(StringUtils.isNotBlank(isMarker)){
            map.addAttribute("isMarker", isMarker);
        }else{
            map.addAttribute("isMarker", "-1");
        }
        if(addressId != null){
            map.addAttribute("addressId", addressId);
        }
        return "/zzgl/addressMapMarker/list_drowAddress.ftl";
    }

    @ResponseBody
    @RequestMapping(value = "/listData", method = RequestMethod.POST)
    public cn.ffcs.common.EUDGPagination listData(
            HttpSession session,
            @RequestParam(value = "addressName", required = false) String addressName,
            @RequestParam(value = "regionCode", required = false) String regionCode,
            @RequestParam(value = "addressId", required = false) Long addressId,
            @RequestParam(value = "isMarker", required = false) String isMarker,
            @RequestParam(value = "mapt", required = false) String mapt,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "rows") int rows) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("addressName", addressName);
        if(StringUtils.isNotBlank(regionCode)){
            paramsMap.put("regionCode", regionCode);
        }else{
            paramsMap.put("regionCode", userInfo.getOrgCode());
        }
        if(addressId != null){
            paramsMap.put("addressId", addressId);
        }

        paramsMap.put("isMarker", isMarker);
        paramsMap.put("mapt", mapt);
        cn.ffcs.common.EUDGPagination eudPagination = new cn.ffcs.common.EUDGPagination();
        eudPagination = xieJingAddressService.findXIEJINGAddressPagination(page, rows, paramsMap);
        return eudPagination;
    }

    /**
     * 2014-05-28 liushi add
     * 转到arcgis楼宇轮廓编辑的panel
     * @param session
     * @param request
     * @param map
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/toDrawAddressPanel")
    public String toDrawAddressPanel(HttpSession session, HttpServletRequest request, ModelMap map
            ,@RequestParam(value="addressId", required = false) Long addressId,
            @RequestParam(value="regionCode", required = false) String regionCode) throws Exception{
        String forward = "/zzgl/addressMapMarker/draw_address_panel.ftl";

        Map<String, Object> defaultOrgInfo = this.getDefaultOrgInfo(session);
        String BUILD_FACTOR_STATION = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "BUILD_FACTOR_STATION",
                IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
        String BUILD_FACTOR_SERVICE = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "BUILD_FACTOR_SERVICE",
                IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
        String BUILD_FACTOR_URL = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "BUILD_FACTOR_URL",
                IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
        map.addAttribute("FACTOR_STATION", BUILD_FACTOR_STATION);
        map.addAttribute("FACTOR_SERVICE", BUILD_FACTOR_SERVICE);
        map.addAttribute("FACTOR_URL", BUILD_FACTOR_URL);


        String addressName = "";
        Long parentGridId = null;
        Integer gridLevel = 0;
        XieJingAddress xieJingAddress = new XieJingAddress();
        if(addressId != null){
            xieJingAddress = xieJingAddressService.findXiejingAddrssById(addressId);
        }
        if(xieJingAddress != null){
            if(StringUtils.isNotBlank(xieJingAddress.getRegionCode())){
                regionCode = xieJingAddress.getRegionCode();
            }
            if(StringUtils.isNotBlank(xieJingAddress.getAddressName())){
                addressName = xieJingAddress.getAddressName();
            }
        }
        if(StringUtils.isNotBlank(regionCode)){
            List<MixedGridInfo> list = mixedGridInfoService.getMixedGridMappingListByOrgCode(regionCode);
            if(list != null && list.size()>0){
                parentGridId = list.get(0).getParentGridId();
                map.addAttribute("gridId", list.get(0).getGridId()+"");
                gridLevel = list.get(0).getGridLevel();
                List<ArcgisInfoOfGrid> resultList = this.arcgisInfoService.getArcgisDrawDataOfGrids(list.get(0).getGridId(), 5);
                if(resultList != null && resultList.size() >0){
                    map.addAttribute("mapCenterLevel", resultList.get(0).getMapCenterLevel());
                    map.addAttribute("gridX", resultList.get(0).getX());
                    map.addAttribute("gridY", resultList.get(0).getY());
                }
            }
        }
        map.addAttribute("projectPath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());
        map.addAttribute("parentGridId", parentGridId + "");

        if(addressId != null){
            map.addAttribute("addressId", addressId.toString());
        }else{
            map.addAttribute("addressId", "");
        }
        map.addAttribute("addressName", addressName);
        return forward;
    }

    /**
     * 2018-08-02 sulch add
     * 转载到地图地址标注编辑的加载页面
     * @param session
     * @param request
     * @param map
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/toMapOfDrawAddress")
    public String toMapOfDrawAddress(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map) throws Exception{
        String forward = "/zzgl/addressMapMarker/arcgis_draw_address.ftl";
        MixedGridInfo gridInfo = getMixedGridInfo(session,request);
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        //获取当前登录人员所在网格信息
        if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
        map.put("gridId", gridInfo.getGridId());
        map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
        String OUTLINE_FONT_SETTINGS = this.funConfigurationService.turnCodeToValue("OUTLINE_FONT_SETTINGS", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
        String ARCGIS_DOCK_MODE = this.funConfigurationService.turnCodeToValue("ARCGIS_DOCK_MODE", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
        if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
            OUTLINE_FONT_SETTINGS = "14";
        }

        String OUTLINE_FONT_SETTINGS_BUILD = this.funConfigurationService.turnCodeToValue("OUTLINE_FONT_SETTINGS_BUILD", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
        if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
            OUTLINE_FONT_SETTINGS = "14";
        }
        if(ARCGIS_DOCK_MODE == null || "".equals(ARCGIS_DOCK_MODE)) {
            ARCGIS_DOCK_MODE = "0";
        }
        map.put("OUTLINE_FONT_SETTINGS", OUTLINE_FONT_SETTINGS);
        map.put("OUTLINE_FONT_SETTINGS_BUILD", OUTLINE_FONT_SETTINGS_BUILD);
        map.put("ARCGIS_DOCK_MODE", ARCGIS_DOCK_MODE);
        response.setHeader("Cache-Control","no-store");
        response.setHeader("Pragrma","no-cache");
        response.setDateHeader("Expires",0);
        return forward;
    }

    /**
     * 地址标注信息保存
     * @param session
     * @param request
     * @param addressId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/getAddressById")
    public Map<String, Object> getAddressById(HttpSession session, HttpServletRequest request, ModelMap map
            ,@RequestParam(value="addressId") Long addressId) throws Exception{
        XieJingAddress xieJingAddress = new XieJingAddress();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> result = new HashMap<String,Object>();
        if(addressId != null) {
            params.put("addressId", addressId);
        }
        List<XieJingAddress> list = xieJingAddressService.findXiejingAddress(params);
        if(list != null && list.size()>0){
            xieJingAddress = list.get(0);
            //获取所属网格的地图数据（父级或者子级）
            if(xieJingAddress != null && StringUtils.isNotBlank(xieJingAddress.getRegionCode())){
                List<MixedGridInfo> gridInfoList = mixedGridInfoService.getMixedGridMappingListByOrgCode(xieJingAddress.getRegionCode());
                if(gridInfoList != null && gridInfoList.size()>0){
                    List<ArcgisInfoOfGrid> resultList = this.arcgisInfoService.getArcgisDrawDataOfGrids(gridInfoList.get(0).getGridId(), 5);
                    List<ArcgisInfoOfGrid> arcgisInfoOfGrids = new ArrayList<ArcgisInfoOfGrid>();
                    for(ArcgisInfoOfGrid arcgisInfoOfGrid:resultList) {
                        if(gridInfoList.get(0).getGridId().equals(arcgisInfoOfGrid.getWid()) ) {
                            arcgisInfoOfGrids.add(arcgisInfoOfGrid);
                        }
                    }
                    if((arcgisInfoOfGrids == null || arcgisInfoOfGrids.size()<=0) && resultList.size()>0) {
                        arcgisInfoOfGrids.add(resultList.get(0));
                    }

                    if(arcgisInfoOfGrids != null && arcgisInfoOfGrids.size() >0){
                        result.put("mapCenterLevel", arcgisInfoOfGrids.get(0).getMapCenterLevel());
                        result.put("gridX", arcgisInfoOfGrids.get(0).getX());
                        result.put("gridY", arcgisInfoOfGrids.get(0).getY());
                        result.put("gridId", arcgisInfoOfGrids.get(0).getWid());
                    }
                }

            }
        }
        result.put("xieJingAddress", xieJingAddress);
        return result;
    }

    /**
     * 地址标注信息保存
     * @param session
     * @param request
     * @param map
     * @param addressId
     * @param x
     * @param y
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/saveAddressMapData")
    public Map<String, Object> saveAddressMapData(HttpSession session, HttpServletRequest request, ModelMap map
            ,@RequestParam(value="addressId") Long addressId
            ,@RequestParam(value="x") double x
            ,@RequestParam(value="y") double y) throws Exception{
        boolean flag = false;
        XieJingAddress xieJingAddress = new XieJingAddress();
        xieJingAddress.setAddressId(addressId);
        xieJingAddress.setX(Float.parseFloat(x + ""));
        xieJingAddress.setY(Float.parseFloat(y + ""));
        flag = xieJingAddressService.saveOrUpdate(xieJingAddress);
        Map<String, Object> result = new HashMap<String,Object>();
        result.put("flag", flag);
        return result;
    }

    @ResponseBody
    @RequestMapping(value="/deleteAddressMapData")
    public Map<String, Object> deleteAddressMapData(HttpSession session, HttpServletRequest request, ModelMap map
            ,@RequestParam(value="addressId") Long addressId) throws Exception{

        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        String userName = "";
        if(userInfo != null && StringUtils.isNotBlank(userInfo.getUserName())){
            userName = userInfo.getUserName();
        }
        int rows = xieJingAddressService.deleteAddressMapData(userName, addressId);
        Map<String, Object> result = new HashMap<String,Object>();
        result.put("flag", rows>0?true:false);
        return result;
    }
}
