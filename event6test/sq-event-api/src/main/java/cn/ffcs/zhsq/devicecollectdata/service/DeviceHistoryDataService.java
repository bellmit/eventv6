package cn.ffcs.zhsq.devicecollectdata.service;

import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceHistoryData;
/**
 * @Description: 设备采集数据模块接口
 * @Author: husp
 * @Date: 10-13 09:04:19
 * @Copyright: 2017 福富软件
 */
public interface DeviceHistoryDataService {
	/**
	 * 水质监测历史列表（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备运行数据表数据列表
	 */
	public EUDGPagination searchWaterHistoryList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 水质监测历史列表（总数）
	 * @param params 查询参数
	 * @return 设备运行数据表数据总数
	 */
	public long countWaterHistory(Map<String, Object> params);
	
	/**
	 * 水质监测历史详情
	 * @param id 设备运行数据表sid
	 * @return 设备运行数据表业务对象
	 */
	public Map<String, Object> getWaterHistoryById(Long sid);
	/**
	 * 新增水质监测历史详情 
	 * @param bo 设备运行数据表业务对象
	 * @return 设备运行数据表id
	 */
	public Long insertWaterHistory(DeviceHistoryData bo);
	
	
	/**
	 * 空气质量监测历史详情列表（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备运行数据表数据列表
	 */
	public EUDGPagination searchAirHistoryList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 空气质量监测历史详情列表（总数）
	 * @param params 查询参数
	 * @return 设备运行数据表数据总数
	 */
	public long countAirHistory(Map<String, Object> params);
	
	/**
	 * 空气质量监测历史详情
	 * @param id 设备运行数据表sid
	 * @return 设备运行数据表业务对象
	 */
	public Map<String, Object> getAirHistoryById(Long sid);
	/**
	 * 新增空气质量监测历史详情
	 * @param bo 设备运行数据表业务对象
	 * @return 设备运行数据表id
	 */
	public Long insertAirHistory(DeviceHistoryData bo);
	
	
	/**
	 * 河流监测历史详情列表（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备运行数据表数据列表
	 */
	public EUDGPagination searchRiverHistoryList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 河流监测历史详情列表（总数）
	 * @param params 查询参数
	 * @return 设备运行数据表数据总数
	 */
	public long countRiverHistory(Map<String, Object> params);
	
	/**
	 * 河流监测历史详情
	 * @param id 设备运行数据表sid
	 * @return 设备运行数据表业务对象
	 */
	public Map<String, Object> getRiverHistoryById(Long sid);
	/**
	 * 新增河流监测历史详情
	 * @param bo 设备运行数据表业务对象
	 * @return 设备运行数据表id
	 */
	public Long insertRiverHistory(DeviceHistoryData bo);
	
	/**
	 * 垃圾桶监测数据列表（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备运行数据表数据列表
	 */
	public EUDGPagination searchTrashHistoryList(int page, int rows, Map<String, Object> params);
	/**
	 * 垃圾桶监测数据列表（总数）
	 * @param params 查询参数
	 * @return 设备运行数据表数据总数
	 */
	public long countTrashHistory(Map<String, Object> params);
	
	/**
	 * 垃圾桶监测数据详情
	 * @param id 设备运行数据表sid
	 * @return 设备运行数据表业务对象
	 */
	public Map<String, Object> getTrashHistoryById(Long sid);
	/**
	 * 新增垃圾桶监测数据
	 * @param bo 设备运行数据表业务对象
	 * @return 设备运行数据表id
	 */
	public Long insertTrashHistory(DeviceHistoryData bo);
	
	/**
	 * 井盖监测历史数据列表（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备运行数据表数据列表
	 */
	public EUDGPagination searchCoverHistoryList(int page, int rows, Map<String, Object> params);
	/**
	 * 井盖监测历史数据列表（总数）
	 * @param params 查询参数
	 * @return 设备运行数据表数据总数
	 */
	public long countCoverHistory(Map<String, Object> params);
	
	/**
	 * 井盖监测历史数据详情
	 * @param id 设备运行数据表sid
	 * @return 设备运行数据表业务对象
	 */
	public Map<String, Object> getCoverHistoryById(Long sid);
	/**
	 * 新增井盖监测历史数据
	 * @param bo 设备运行数据表业务对象
	 * @return 设备运行数据表id
	 */
	public Long insertCoverHistory(DeviceHistoryData bo);
	
	/**
	 * 农业传感器监测历史记录列表（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备运行数据表数据列表
	 */
	public EUDGPagination searchSensorHistoryList(int page, int rows, Map<String, Object> params);
	/**
	 * 农业传感器监测历史记录列表（总数）
	 * @param params 查询参数
	 * @return 设备运行数据表数据总数
	 */
	public long countSensorHistory(Map<String, Object> params);
	
	/**
	 * 农业传感器监测历史记录详情
	 * @param id 设备运行数据表sid
	 * @return 设备运行数据表业务对象
	 */
	public Map<String, Object> getSensorHistoryById(Long sid);
	/**
	 * 新增农业传感器监测历史记录数据
	 * @param bo 设备运行数据表业务对象
	 * @return 设备运行数据表id
	 */
	public Long insertSensorHistory(DeviceHistoryData bo);
}
