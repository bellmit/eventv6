package cn.ffcs.zhsq.devicecollectdata.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceCollectData;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.BidWaterMeterHis;

/**
 * @Description: 设备采集数据模块服务
 * @Author: luth
 * @Date: 10-13 09:04:18
 * @Copyright: 2017 福富软件
 */
public interface DeviceCollectDataService {

	/**
	 * 新增数据
	 * @param bo 设备采集数据业务对象
	 * @return 设备采集数据id
	 */
	public Long insert(DeviceCollectData bo);

	/**
	 * 修改数据
	 * @param bo 设备采集数据业务对象
	 * @return 是否修改成功
	 */
	public boolean update(DeviceCollectData bo);

	/**
	 * 保存或修改数据，存在修改，不存在保存
	 * @param runDataList 设备采集数据指标项编码和值集合
	 * @param bo 设备采集数据业务对象
	 * @param resultMap
	 * @return 是否修改成功
	 */
	public boolean saveOrUpdate(List<Map<String,Object>> runDataList,DeviceCollectData bo,Map<String, Object> resultMap);
	
	/**
	 * 删除数据
	 * @param bo 设备采集数据业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(DeviceCollectData bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 设备采集数据分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 设备采集数据id
	 * @return 设备采集数据业务对象
	 */
	public DeviceCollectData searchById(Long id);
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	public List<DeviceCollectData> findList( Map<String, Object> params);
	/**
	 * 空气质量
	 * @param regionCode
	 * @param bizType
	 * @return
	 */
	public Map<String, Object> getEnvMonitoringHisMap(String regionCode,String bizType);
	
	/**
	 * 充电桩
	 * @param regionCode
	 * @param bizType
	 * @return
	 */
	public Map<String, Object>  getChargingPileList(String regionCode,String bizType);

	/**
	 * 获取地磁设备统计数据
	 * @return
	 */
	public Map<String,Object> findDcData();
	
	/**
	 * 井盖子设备的所有信息
	 * @return
	 */
	public List<Map<String, Object>>  findManholeCoverData();


	
	/**
	 * 获取大表数据
	 * @param params
	 * @return
	 */
	public List<BidWaterMeterHis> findBidWaterList(Map<String, Object> params);
	
	
	public List<Map<String, Object>> manholeCoverCount();
	
	public List<Map<String, Object>> findAccessControlList(Map<String, Object> params);  

}