package cn.ffcs.zhsq.devicecollectdata.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.devicecollectdata.service.DeviceHistoryDataService;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceHistoryData;
import cn.ffcs.zhsq.mybatis.persistence.devicecollectdata.DeviceHistoryDataMapper;

/**
 * @Description: 物联网历史记录模块服务实现
 * @Author: husp
 * @Date: 11-28 08:49:19
 * @Copyright: 2017 福富软件
 */
@Service("deviceHistoryDataServiceImpl")
@Transactional
public class DeviceHistoryDataServiceImpl implements DeviceHistoryDataService{
	@Autowired
	private DeviceHistoryDataMapper deviceHistoryDataMapper; 
	/**
	 * 水质监测历史列表（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备运行数据表数据列表
	 */
	public EUDGPagination searchWaterHistoryList(int page, int rows, Map<String, Object> params){
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<Map<String, Object>> list = deviceHistoryDataMapper.searchWaterHistoryList(params, rowBounds);
		long count = deviceHistoryDataMapper.countWaterHistory(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 水质监测历史列表（总数）
	 * @param params 查询参数
	 * @return 设备运行数据表数据总数
	 */
	public long countWaterHistory(Map<String, Object> params){
		return deviceHistoryDataMapper.countWaterHistory(params);
	}
	
	/**
	 * 水质监测历史详情
	 * @param id 设备运行数据表sid
	 * @return 设备运行数据表业务对象
	 */
	public Map<String, Object> getWaterHistoryById(Long sid){
		return deviceHistoryDataMapper.getWaterHistoryById(sid);
	}
	/**
	 * 新增水质监测历史详情 
	 * @param bo 设备运行数据表业务对象
	 * @return 设备运行数据表id
	 */
	public Long insertWaterHistory(DeviceHistoryData bo){
		deviceHistoryDataMapper.insertWaterHistory(bo);
		return bo.getDwhId();
	}
	
	
	/**
	 * 空气质量监测历史详情列表（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备运行数据表数据列表
	 */
	public EUDGPagination searchAirHistoryList(int page, int rows, Map<String, Object> params){
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<Map<String, Object>> list = deviceHistoryDataMapper.searchAirHistoryList(params, rowBounds);
		long count = deviceHistoryDataMapper.countAirHistory(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 空气质量监测历史详情列表（总数）
	 * @param params 查询参数
	 * @return 设备运行数据表数据总数
	 */
	public long countAirHistory(Map<String, Object> params){
		return deviceHistoryDataMapper.countAirHistory(params);
	}
	
	/**
	 * 空气质量监测历史详情
	 * @param id 设备运行数据表sid
	 * @return 设备运行数据表业务对象
	 */
	public Map<String, Object> getAirHistoryById(Long sid){
		return deviceHistoryDataMapper.getAirHistoryById(sid);
	}
	/**
	 * 新增空气质量监测历史详情
	 * @param bo 设备运行数据表业务对象
	 * @return 设备运行数据表id
	 */
	public Long insertAirHistory(DeviceHistoryData bo){
		deviceHistoryDataMapper.insertAirHistory(bo);
		return bo.getDahId();
	}
	
	
	/**
	 * 河流监测历史详情列表（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备运行数据表数据列表
	 */
	public EUDGPagination searchRiverHistoryList(int page, int rows, Map<String, Object> params){
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<Map<String, Object>> list = deviceHistoryDataMapper.searchRiverHistoryList(params, rowBounds);
		long count = deviceHistoryDataMapper.countRiverHistory(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 河流监测历史详情列表（总数）
	 * @param params 查询参数
	 * @return 设备运行数据表数据总数
	 */
	public long countRiverHistory(Map<String, Object> params){
		return deviceHistoryDataMapper.countRiverHistory(params);
	}
	
	/**
	 * 河流监测历史详情
	 * @param id 设备运行数据表sid
	 * @return 设备运行数据表业务对象
	 */
	public Map<String, Object> getRiverHistoryById(Long sid){
		return deviceHistoryDataMapper.getRiverHistoryById(sid);
	}
	/**
	 * 新增河流监测历史详情
	 * @param bo 设备运行数据表业务对象
	 * @return 设备运行数据表id
	 */
	public Long insertRiverHistory(DeviceHistoryData bo){
		deviceHistoryDataMapper.insertRiverHistory(bo);
		return bo.getDrhId();
	}
	
	/**
	 * 垃圾桶监测数据列表（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备运行数据表数据列表
	 */
	public EUDGPagination searchTrashHistoryList(int page, int rows, Map<String, Object> params){
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<Map<String, Object>> list = deviceHistoryDataMapper.searchTrashHistoryList(params, rowBounds);
		long count = deviceHistoryDataMapper.countTrashHistory(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	/**
	 * 垃圾桶监测数据列表（总数）
	 * @param params 查询参数
	 * @return 设备运行数据表数据总数
	 */
	public long countTrashHistory(Map<String, Object> params){
		return deviceHistoryDataMapper.countTrashHistory(params);
	}
	
	/**
	 * 垃圾桶监测数据详情
	 * @param id 设备运行数据表sid
	 * @return 设备运行数据表业务对象
	 */
	public Map<String, Object> getTrashHistoryById(Long sid){
		return deviceHistoryDataMapper.getTrashHistoryById(sid);
	}
	/**
	 * 新增垃圾桶监测数据
	 * @param bo 设备运行数据表业务对象
	 * @return 设备运行数据表id
	 */
	public Long insertTrashHistory(DeviceHistoryData bo){
		deviceHistoryDataMapper.insertTrashHistory(bo);
		return bo.getDthId();
	}
	
	/**
	 * 井盖监测历史数据列表（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备运行数据表数据列表
	 */
	public EUDGPagination searchCoverHistoryList(int page, int rows, Map<String, Object> params){
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<Map<String, Object>> list = deviceHistoryDataMapper.searchCoverHistoryList(params, rowBounds);
		long count = deviceHistoryDataMapper.countCoverHistory(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	/**
	 * 井盖监测历史数据列表（总数）
	 * @param params 查询参数
	 * @return 设备运行数据表数据总数
	 */
	public long countCoverHistory(Map<String, Object> params){
		return deviceHistoryDataMapper.countCoverHistory(params);
	}
	
	/**
	 * 井盖监测历史数据详情
	 * @param id 设备运行数据表sid
	 * @return 设备运行数据表业务对象
	 */
	public Map<String, Object> getCoverHistoryById(Long sid){
		return deviceHistoryDataMapper.getCoverHistoryById(sid);
	}
	/**
	 * 新增井盖监测历史数据
	 * @param bo 设备运行数据表业务对象
	 * @return 设备运行数据表id
	 */
	public Long insertCoverHistory(DeviceHistoryData bo){
		deviceHistoryDataMapper.insertCoverHistory(bo);
		return bo.getDchId();
	}
	/**
	 * 农业传感器监测历史记录列表（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备运行数据表数据列表
	 */
	public EUDGPagination searchSensorHistoryList(int page, int rows, Map<String, Object> params){
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<Map<String, Object>> list = deviceHistoryDataMapper.searchSensorHistoryList(params, rowBounds);
		long count = deviceHistoryDataMapper.countSensorHistory(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	/**
	 * 农业传感器监测历史记录列表（总数）
	 * @param params 查询参数
	 * @return 设备运行数据表数据总数
	 */
	public long countSensorHistory(Map<String, Object> params){
		return deviceHistoryDataMapper.countSensorHistory(params);
	}
	
	/**
	 * 农业传感器监测历史记录详情
	 * @param id 设备运行数据表sid
	 * @return 设备运行数据表业务对象
	 */
	public Map<String, Object> getSensorHistoryById(Long sid){
		return deviceHistoryDataMapper.getSensorHistoryById(sid);
	}
	/**
	 * 新增农业传感器监测历史记录数据
	 * @param bo 设备运行数据表业务对象
	 * @return 设备运行数据表id
	 */
	public Long insertSensorHistory(DeviceHistoryData bo){
		deviceHistoryDataMapper.insertSensorHistory(bo);
		return bo.getDchId();
	}
}
