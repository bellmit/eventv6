package cn.ffcs.zhsq.devicecollectdata.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceCollectAlert;

/**
 * @Description: 设备采集告警数据模块服务
 * @Author: luth
 * @Date: 10-13 09:04:54
 * @Copyright: 2017 福富软件
 */
public interface DeviceCollectAlertService {

	/**
	 * 新增数据
	 * @param bo 设备采集告警数据业务对象
	 * @return 设备采集告警数据id
	 */
	public Long insert(DeviceCollectAlert bo);

	/**
	 * 修改数据
	 * @param bo 设备采集告警数据业务对象
	 * @return 是否修改成功
	 */
	public boolean update(DeviceCollectAlert bo);

	/**
	 * 删除数据
	 * @param bo 设备采集告警数据业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(DeviceCollectAlert bo);

	/**
	 * 保存或修改数据，存在修改，不存在保存
	 * @param runDataList 设备采集数据指标项编码和值集合
	 * @param bo 设备采集数据业务对象
	 * @param resultMap
	 * @return 是否修改成功
	 */
	public boolean saveOrUpdate(List<Map<String,Object>> runDataList,DeviceCollectAlert bo,Map<String, Object> resultMap);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 设备采集告警数据分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 设备采集告警数据id
	 * @return 设备采集告警数据业务对象
	 */
	public DeviceCollectAlert searchById(Long id);
	
	
	public List<DeviceCollectAlert> findList( Map<String, Object> params);
	
	/**
	 * 根据设备ID获取告警数
	 * @param deviceId
	 * @return
	 */
	public Integer getCountByDeviceId(Long deviceId) ; 
	public List<DeviceCollectAlert> findListByDeviceId(Long deviceId);
	
	public Long countList(Map<String, Object> params);

}