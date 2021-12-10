package cn.ffcs.zhsq.devicecollectdata.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceRunningData;
import cn.ffcs.system.publicUtil.EUDGPagination;

/**
 * @Description: 设备运行数据表模块服务
 * @Author: husp
 * @Date: 11-28 08:49:19
 * @Copyright: 2017 福富软件
 */
public interface DeviceRunningDataService {

	/**
	 * 新增数据
	 * @param bo 设备运行数据表业务对象
	 * @return 设备运行数据表id
	 */
	public Long insert(DeviceRunningData bo);

	/**
	 * 修改数据
	 * @param bo 设备运行数据表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(DeviceRunningData bo);

	/**
	 * 删除数据
	 * @param bo 设备运行数据表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(DeviceRunningData bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 设备运行数据表分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 设备运行数据表id
	 * @return 设备运行数据表业务对象
	 */
	public DeviceRunningData searchById(Long id);
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 设备运行数据表分页数据对象
	 */
	public List<DeviceRunningData> findList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 查询数据
	 * @param params 查询参数
	 * @return 设备运行数据表数据对象
	 */
	public List<DeviceRunningData> findByDeviceServiceId(String deviceServiceId);

}