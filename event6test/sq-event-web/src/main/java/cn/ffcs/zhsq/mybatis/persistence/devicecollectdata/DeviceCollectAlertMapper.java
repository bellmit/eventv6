package cn.ffcs.zhsq.mybatis.persistence.devicecollectdata;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceCollectAlert;

/**
 * @Description: 设备采集告警数据模块dao接口
 * @Author: luth
 * @Date: 10-13 09:04:54
 * @Copyright: 2017 福富软件
 */
public interface DeviceCollectAlertMapper extends MyBatisBaseMapper<DeviceCollectAlert>{
	
	/**
	 * 新增数据
	 * @param bo 设备采集告警数据业务对象
	 * @return 设备采集告警数据id
	 */
	public int insert(DeviceCollectAlert bo);
	
	/**
	 * 修改数据
	 * @param bo 设备采集告警数据业务对象
	 * @return 修改的记录数
	 */
	public int update(DeviceCollectAlert bo);
	
	/**
	 * 删除数据
	 * @param bo 设备采集告警数据业务对象
	 * @return 删除的记录数
	 */
	public long delete(DeviceCollectAlert bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备采集告警数据数据列表
	 */
	public List<DeviceCollectAlert> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	
	/**
	 * 查询数据
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备采集告警数据数据列表
	 */
	public List<DeviceCollectAlert> searchList(Map<String, Object> params);
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 设备采集告警数据数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 设备采集告警数据id
	 * @return 设备采集告警数据业务对象
	 */
	public DeviceCollectAlert searchById(Long id);
	/**
	 * 根据设备ID查询告警数
	 * @param deviceId
	 * @return
	 */
	public Integer getCountByDeviceId(@Param(value="deviceId")Long deviceId) ;
	
	public List<DeviceCollectAlert>  findListByDeviceId(@Param(value="deviceId")Long deviceId) ;

}