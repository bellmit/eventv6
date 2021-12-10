package cn.ffcs.zhsq.devicecollectdata.service;

import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceGpsData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: 设备采集GPS数据模块bo对象
 * @Author: huangjianming
 * @Date: 2021/3/9 15:39
 * @Copyright: 2021 福富软件
 */
public interface DeviceGpsDataService {

	/**
	 * 新增数据
	 * @param bo 设备采集GPS数据业务对象
	 * @return 设备采集数据id
	 */
	public String insert(DeviceGpsData bo);

	/**
	 * 新增或修改最新的gps数据
	 * @param bo 设备采集GPS数据业务对象
	 * @return 设备采集数据id
	 */
	public String saveOrUpdateLastestGpsData(DeviceGpsData bo);

	/**
	 * 根据条件查询gps日表数据
	 * @param params 查询参数
	 * @return gps日表数据列表
	 */
	List<Map<String, Object>> searchGpsDataList(Map<String, Object> params);

	/**
	 * 根据监控点编号查询gps最新数据
	 * @param resno 执法仪监控点编号
	 * @return gps最新数据
	 */
	Map<String, Object> selectOneLastestGpsData(String resno);

	/**
	 * 根据resno和mapt获取执法仪的最新定位信息
	 * @param resno
	 * @param mapt
	 * @return
	 */
	List<Map<String, Object>> fetchEnForceRecorderLatestLocateDataListByResno(String resno,String mapt);

	/**
	 * 获取多个执法仪最新定位数据
	 * @param params
	 * 			resIds
	 * 		     mapt
	 * @return
	 */
	List<Map<String, Object>> fetchMoreEnForceRecorderLatestLocateDataList(Map<String, Object> params);
}