package cn.ffcs.zhsq.map.buildingBind.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.buildingBind.BuildingBindInfo;
import java.util.Map;

/**
 * 楼宇绑定服务
 *
 * @Author sulch
 * @Date 2017-10-18 10:05
 */
public interface IBuildingBindService {
    /**
     * 分页查找楼房信息
     * @param pageNo
     * @param pageSize
     * @param params
     * @return
     */
    public EUDGPagination findBuildingBindInfoPagination(int pageNo, int pageSize, Map<String, Object> params);

    /**
     * 根据条件查找楼房信息
     * @param params
     * @return
     */
    public BuildingBindInfo findBuildingBindInfoByParams(Map<String, Object> params);

    /**
     * 保存楼宇绑定信息
     * @param buildingBindInfo
     * @return
     */
    public Boolean saveBuildingBindInfo(BuildingBindInfo buildingBindInfo);

    /**
     * 获取渣土车轨迹信息
     * @param params
     * @return
     */
    public Map<String, Object> findCarTrajectoryInfoByParams(Map<String, Object> params);
}
