package cn.ffcs.zhsq.map.buildingBind.service.impl;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.zhsq.map.buildingBind.service.IBuildingBindService;
import cn.ffcs.zhsq.mybatis.domain.map.buildingBind.BuildingBindInfo;
import cn.ffcs.zhsq.mybatis.domain.map.buildingBind.CarTrajectoryInfo;
import cn.ffcs.zhsq.mybatis.persistence.map.buildingBind.BuildingBindMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 楼宇绑定接口服务实现类
 *
 * @Author sulch
 * @Date 2017-10-18 10:07
 */
@Service(value="buildingBindServiceImpl")
public class BuildingBindServiceImpl implements IBuildingBindService {

    @Autowired
    private BuildingBindMapper buildingBindMapper;
    @Autowired
    private IMixedGridInfoService mixedGridInfoService;

    @Override
    public EUDGPagination findBuildingBindInfoPagination(int pageNo, int pageSize, Map<String, Object> params) {
        pageNo = pageNo < 1 ? 1 : pageNo;
        pageSize = pageSize < 1 ? 20 : pageSize;
        RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
        if (params.get("gridId") != null) {
            Long gridId = Long.parseLong(params.get("gridId").toString());
            MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, true);
            params.put("gridCode", gridInfo.getGridCode());
            params.put("infoOrgCode", gridInfo.getInfoOrgCode());
            params.put("gridId", null);
        }
        int count = 0;
        List<BuildingBindInfo> list =null;
        count= buildingBindMapper.findCountByCriteria(params);
        list = buildingBindMapper.findPageListByCriteria(params, rowBounds);
        EUDGPagination eudgPagination = new EUDGPagination(count, list);
        return eudgPagination;
    }

    @Override
    public BuildingBindInfo findBuildingBindInfoByParams(Map<String, Object> params) {
        List<BuildingBindInfo> buildingBindInfos = new ArrayList<BuildingBindInfo>();
        BuildingBindInfo buildingBindInfo = new BuildingBindInfo();
        buildingBindInfos = buildingBindMapper.findBuildingBindInfoByParams(params);
        if(buildingBindInfos != null && buildingBindInfos.size() > 0){
            buildingBindInfo = buildingBindInfos.get(0);
        }
        return buildingBindInfo;
    }

    @Override
    public Boolean saveBuildingBindInfo(BuildingBindInfo buildingBindInfo) {
        List<BuildingBindInfo> buildingBindInfos = buildingBindMapper.getBuildingBindInfoForIsExist(buildingBindInfo.getBuildingId(), buildingBindInfo.getMapType());
        int row = 0;
        boolean flag = false;
        if(buildingBindInfos.size()>0){
            row = this.buildingBindMapper.updateBuildingBindInfo(buildingBindInfo);
        }else{
            row = this.buildingBindMapper.insertBuildingBindInfo(buildingBindInfo);
        }
        flag = row==1?true:false;
        return flag;
    }

    @Override
    public Map<String, Object> findCarTrajectoryInfoByParams(Map<String, Object> params) {
        String carIdStr = (String) params.get("carIds");
        Map<String, Object> result = new HashMap<String, Object>();
        String[] carIdArr = null;
        if(StringUtils.isNotBlank(carIdStr)){
            carIdArr = carIdStr.split(",");
            if(carIdArr != null && carIdArr.length > 0){
                for (int i=0;i<carIdArr.length;i++){
                    params.put("carId", carIdArr[i]);
                    List<CarTrajectoryInfo> list = buildingBindMapper.findCarTrajectoryInfoByParams(params);
                    if(list != null && list.size() > 0){
                        result.put(carIdArr[i], list);
                    }
                }
            }
        }
        return result;
    }


}
