package cn.ffcs.zhsq.mybatis.persistence.map.buildingBind;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.map.buildingBind.BuildingBindInfo;
import cn.ffcs.zhsq.mybatis.domain.map.buildingBind.CarTrajectoryInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 楼宇绑定
 *
 * @Author sulch
 * @Date 2017-10-18 10:12
 */
public interface BuildingBindMapper extends MyBatisBaseMapper<BuildingBindInfo> {

    /**
     * 获取楼宇列表总数
     * @param param 参数
     * @return
     */
    public int findCountByCriteria(Map<String, Object> param);

    /**
     * 获取楼宇列表
     * @param param 参数
     * @param bounds 分页信息
     * @return
     */
    public List<BuildingBindInfo> findPageListByCriteria(Map<String, Object> param, RowBounds bounds);

    /**
     * 查询楼宇的绑定信息
     * @param buildingId
     * @param mapType
     * @return
     */
    public List<BuildingBindInfo> getBuildingBindInfoForIsExist(@Param(value="buildingId")Long buildingId, @Param(value="mapType")String mapType);

    /**
     * 查询楼宇的绑定信息
     * @param param
     * @return
     */
    public List<BuildingBindInfo> findBuildingBindInfoByParams(Map<String, Object> param);

    /**
     * 更新楼宇绑定消息
     * @param buildingBindInfo
     * @return
     */
    public int updateBuildingBindInfo(BuildingBindInfo buildingBindInfo);

    /**
     * 新增楼宇绑定信息
     * @param buildingBindInfo
     * @return
     */
    public int insertBuildingBindInfo(BuildingBindInfo buildingBindInfo);

    /**
     * 根据参数获取渣土车轨迹信息
     * @param param
     * @return
     */
    public List<CarTrajectoryInfo> findCarTrajectoryInfoByParams(Map<String, Object> param);
}
