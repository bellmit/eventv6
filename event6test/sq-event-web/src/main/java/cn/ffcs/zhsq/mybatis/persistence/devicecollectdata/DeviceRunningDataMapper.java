package cn.ffcs.zhsq.mybatis.persistence.devicecollectdata;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceRunningData;

/**
 * @Description: 设备运行数据表模块dao接口
 * @Author: husp
 * @Date: 11-28 08:49:19
 * @Copyright: 2017 福富软件
 */
public interface DeviceRunningDataMapper extends MyBatisBaseMapper<DeviceRunningData>{
	
	/**
	 * 新增数据
	 * @param bo 设备运行数据表业务对象
	 * @return 设备运行数据表id
	 */
	public int insert(DeviceRunningData bo);
	
	/**
	 * 修改数据
	 * @param bo 设备运行数据表业务对象
	 * @return 修改的记录数
	 */
	public int update(DeviceRunningData bo);
	
	/**
	 * 删除数据
	 * @param bo 设备运行数据表业务对象
	 * @return 删除的记录数
	 */
	public long delete(DeviceRunningData bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备运行数据表数据列表
	 */
	public List<DeviceRunningData> searchList(Map<String, Object> params, RowBounds rowBounds);
	/**
	 * 查询数据（不分页）
	 * @param params 查询参数
	 * @return 设备运行数据表数据列表
	 */
	public List<DeviceRunningData> findByDeviceServiceId(String deviceServiceId);
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 设备运行数据表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 设备运行数据表id
	 * @return 设备运行数据表业务对象
	 */
	public DeviceRunningData searchById(Long id);

}