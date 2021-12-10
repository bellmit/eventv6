package cn.ffcs.zhsq.mybatis.persistence.devicecollectdata;

import cn.ffcs.gis.mybatis.domain.base.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceCollectData;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceGpsData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * @Description: 设备采集GPS数据模块dao接口
 * @Author: huangjm
 * @Date: 2021/3/9 15:39
 * @Copyright: 2021 福富软件
 */
public interface DeviceGpsDataMapper extends MyBatisBaseMapper<DeviceGpsData>{

    /**
     * 更新最新GPS信息
     * @param bo
     * @return
     */
    int updateLastestGpsData(DeviceGpsData bo);

    /**
     * 新增GPS日表数据
     * @param bo
     * @return
     */
    int insertLastestGpsData(DeviceGpsData bo);

    /**
     * 根据条件查询gps日表数据
     * @param params 查询参数
     * @return gps日表数据列表
     */
    List<Map<String, Object>> searchGpsDataList(Map<String, Object> params);

    /**
     * 根据监控点编号查询gps最新数据
     * @param resno 执法仪监控点编号
     * @return gps最新一条数据
     */
    Map<String, Object> selectOneLastestGpsData(String resno);

    /**
     * 根据resno和mapt获取执法仪的最新定位信息
     * @param resno
     * @param mapt
     * @return
     */
    List<Map<String, Object>> fetchEnForceRecorderLatestLocateDataListByResno(@Param(value="resno")String resno, @Param(value="mapt")String mapt);

    /**
     * 根据resIds和mapt获取多个执法仪的最新定位信息
     * @param params
     *       resIds
     *       mapt
     * @return
     */
    List<Map<String, Object>> fetchMoreEnForceRecorderLatestLocateDataList(Map<String, Object> params);
}