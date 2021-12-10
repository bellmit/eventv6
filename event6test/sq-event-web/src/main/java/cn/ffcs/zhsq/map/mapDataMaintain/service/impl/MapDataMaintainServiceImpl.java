package cn.ffcs.zhsq.map.mapDataMaintain.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.common.Pagination;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.GisGridInfo;
import cn.ffcs.shequ.zzgl.service.firegrid.IFireResourceService;
import cn.ffcs.shequ.zzgl.service.globalEyes.MonitorService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResInfoService;
import cn.ffcs.zhsq.map.mapDataMaintain.service.IMapDataMaintainService;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;
import cn.ffcs.zhsq.mybatis.persistence.map.mapDataMaintain.MapDataMaintainMapper;
import cn.ffcs.zhsq.mybatis.persistence.map.menuconfigure.MenuConfigMapper;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 地图数据维护接口实现类
 *
 * @Author sulch
 * @Date 2018-05-29 16:31
 */
@Service(value = "mapDataMaintainServiceImpl")
public class MapDataMaintainServiceImpl implements IMapDataMaintainService {
    @Autowired
    private MapDataMaintainMapper mapDataMaintainMapper;
    @Autowired
    private IResInfoService resInfoService;
    @Autowired
    private MonitorService monitorService;
    @Autowired
    private MenuConfigMapper menuConfigMapper;
    @Autowired
    private IFireResourceService fireResourceService;

    @Override
    public EUDGPagination findGridInfoPagination(int pageNo, int pageSize, Map<String, Object> params) {
        pageNo = pageNo<1?1:pageNo;
        pageSize = pageSize<1?10:pageSize;
        RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
        if(params.get("mapt") == null) {
            params.put("mapt", 5);
        }
        int count = mapDataMaintainMapper.findCountByCriteria(params);
        List<GisGridInfo> list = mapDataMaintainMapper.findPageListByCriteria(params, rowBounds);
        String gridModel = (String)params.get("gridModel");
        gridModel = gridModel==null ? IMixedGridInfoService.GRID_MODEL_XINGZHENG : gridModel;
        //-- 添加附属信息
        //addExtraInfo(gridModel,list, true);
        EUDGPagination eudgPagination = new EUDGPagination(count, list);
        return eudgPagination;
    }

    @Override
    public EUDGPagination findResInfoPagination(int pageNo, int pageSize, Map<String, Object> params) {
        pageNo = pageNo<1?1:pageNo;
        pageSize = pageSize<1?10:pageSize;
        RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
        long count = 0;
        List<Map<String, Object>> list = new ArrayList<>();
        String infoOrgCode = "";
        infoOrgCode = (String)params.get("infoOrgCode");
        EUDGPagination eudgPagination = null;
        cn.ffcs.common.EUDGPagination pageObj = null;
        if(params.get("resTypeCode") != null) {
            String resTypeCode = (String)params.get("resTypeCode");
            if(StringUtils.isNotBlank(resTypeCode)){
                if(resTypeCode.equals(ConstantValue.GLOBAL_EYE_TYPE_CODE)){//全球眼
                    Pagination pagination = monitorService.listGlobalEyeConfig(infoOrgCode, pageNo, pageSize, null, null, null, null, null, "003");
                    if(pagination != null && pagination.getList() != null){
                        list = formateListdata(pagination.getList(), resTypeCode);
                        count = pagination.getTotalCount();
                    }

                }else if (resTypeCode.equals(ConstantValue.HIRE_HYDRANT_TYPE_CODE)){//消防栓
                    if(params.get("resName") != null){
                        params.put("name", params.get("resName"));
                    }
                    cn.ffcs.common.EUDGPagination pagination = fireResourceService.findByPagination(pageNo, pageSize, params);
                    if(pagination.getRows() != null){
                        list = formateListdata(pagination.getRows(), resTypeCode);
                    }
                    count = pagination.getTotal();
                }else{
                    pageObj = resInfoService.findResInfoPagination(pageNo, pageSize, params);
                    if(pageObj != null){
                        if(pageObj.getRows() != null){
                            list = formateListdata(pageObj.getRows(), resTypeCode);
                            list = (List<Map<String, Object>>) pageObj.getRows();
                        }
                        count = pageObj.getTotal();
                    }
                }
            }
            if(list != null){
                formateListdata(list, resTypeCode);
            }
        }
        eudgPagination = new EUDGPagination(count, list);
        return eudgPagination;
    }

    private List<Map<String, Object>> formateListdata(List<?> list, String resTypeCode){
        List<Map<String, Object>> mapList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            Map<String, Object> map = new HashMap<>();
            try {
                map = BeanUtils.describe(list.get(i));
            } catch (Exception e) {
                System.out.println("transMap2Bean2 Error " + e);
            }
            if(StringUtils.isNotBlank(resTypeCode)){
                if(resTypeCode.equals(ConstantValue.GLOBAL_EYE_TYPE_CODE)){
                    if(map.get("platformName") != null){
                        map.put("resName", map.get("platformName"));
                    }
                    if(map.get("monitorId") != null){
                        map.put("resId", map.get("monitorId"));
                    }
                }else if(resTypeCode.equals(ConstantValue.HIRE_HYDRANT_TYPE_CODE)){
                    if(map.get("name") != null){
                        map.put("resName", map.get("name"));
                    }
                    if(map.get("id") != null){
                        map.put("resId", map.get("id"));
                    }
                }
            }
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public GisDataCfg getGisDataCfgByCode(String menuCode, String orgCode) {
        String layerName = "";
        if(StringUtils.isNotBlank(menuCode)){
            if(menuCode.equals(ConstantValue.COMFORT_STATION_TYPE_CODE)){//公共厕所
                menuCode = "toilet";
                layerName = "urbanPartsLayer";
            }else if(menuCode.equals(ConstantValue.STREET_LAMP_TYPE_CODE)){//路灯
                menuCode = "streetlight";
                layerName = "urbanPartsLayer";
            }else if(menuCode.equals(ConstantValue.HIRE_HYDRANT_TYPE_CODE)){//消防栓
                menuCode = "newFireHydrant";
                layerName = "urbanPartsLayer";
            }else if(menuCode.equals(ConstantValue.BUS_STATION_TYPE_CODE)){//公交车站
                menuCode = "worldGjz";
                layerName = "urbanPartsLayer";
            }else if(menuCode.equals(ConstantValue.GLOBAL_EYE_TYPE_CODE)){//全球眼
                menuCode = "globalEyes";
                layerName = "globalEyesLayer";
            }else if(menuCode.equals("020116")){//全球眼（晋江）
                menuCode = "globalEyesJJ";
                layerName = "urbanPartsLayer";
            }
        }
        GisDataCfg gisDataCfg = this.menuConfigMapper.getGisDataCfgByCode(menuCode);
        if(gisDataCfg == null){

        }
        if (gisDataCfg != null) {
            String elementsCollectionStr = "";
            elementsCollectionStr += "gdcId_,_" + String.valueOf(gisDataCfg.getGdcId()) + ",_,";
            elementsCollectionStr += "orgCode_,_" + gisDataCfg.getOrgCode() + ",_,";
            elementsCollectionStr += "smallIco_,_" + gisDataCfg.getSmallIco() + ",_,";
            elementsCollectionStr += "treeIcon_,_" + gisDataCfg.getTreeIcon() + ",_,";
            elementsCollectionStr += "menuCode_,_" + gisDataCfg.getMenuCode() + ",_,";
            elementsCollectionStr += "menuName_,_" + gisDataCfg.getMenuName() + ",_,";
            elementsCollectionStr += "smallIcoSelected_,_" + gisDataCfg.getSmallIcoSelected() + ",_,";
            elementsCollectionStr += "menuListUrl_,_" + gisDataCfg.getMenuListUrl() + ",_,";
            elementsCollectionStr += "menuSummaryUrl_,_" + gisDataCfg.getMenuSummaryUrl() + ",_,";
            elementsCollectionStr += "menuLayerName_,_" + (layerName.isEmpty()?gisDataCfg.getMenuLayerName():layerName) + ",_,";
            elementsCollectionStr += "menuDetailUrl_,_" + gisDataCfg.getMenuDetailUrl() + ",_,";
            elementsCollectionStr += "menuDetailWidth_,_" + gisDataCfg.getMenuDetailWidth() + ",_,";
            elementsCollectionStr += "menuDetailHeight_,_" + gisDataCfg.getMenuDetailHeight() + ",_,";
            elementsCollectionStr += "menuSummaryWidth_,_" + gisDataCfg.getMenuSummaryWidth() + ",_,";
            elementsCollectionStr += "menuSummaryHeight_,_" + gisDataCfg.getMenuSummaryHeight() + ",_,";
            elementsCollectionStr += "remark_,_" + gisDataCfg.getRemark() + ",_,";
            elementsCollectionStr += "callBack_,_" + gisDataCfg.getCallBack() + ",_,";
            gisDataCfg.setElementsCollectionStr(elementsCollectionStr);
            String callBackStr = gisDataCfg.getCallBack() + "('" + elementsCollectionStr + "')";
            gisDataCfg.setCallBack(callBackStr);
            return gisDataCfg;
        }
        return null;
    }
}
