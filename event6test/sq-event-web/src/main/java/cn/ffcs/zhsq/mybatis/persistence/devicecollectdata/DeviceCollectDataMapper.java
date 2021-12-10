package cn.ffcs.zhsq.mybatis.persistence.devicecollectdata;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceCollectData;

/**
 * @Description: 设备采集数据模块dao接口
 * @Author: luth
 * @Date: 10-13 09:04:18
 * @Copyright: 2017 福富软件
 */
public interface DeviceCollectDataMapper extends MyBatisBaseMapper<DeviceCollectData>{
	
	/**
	 * 新增数据
	 * @param bo 设备采集数据业务对象
	 * @return 设备采集数据id
	 */
	public int insert(DeviceCollectData bo);
	
	/**
	 * 修改数据
	 * @param bo 设备采集数据业务对象
	 * @return 修改的记录数
	 */
	public int update(DeviceCollectData bo);
	
	/**
	 * 删除数据
	 * @param bo 设备采集数据业务对象
	 * @return 删除的记录数
	 */
	public long delete(DeviceCollectData bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备采集数据数据列表
	 */
	public List<DeviceCollectData> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 未分页
	 * @param params
	 * @return
	 */
	public List<DeviceCollectData> searchList(Map<String, Object> params);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 设备采集数据数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 设备采集数据id
	 * @return 设备采集数据业务对象
	 */
	public DeviceCollectData searchById(Long id);
	
	/**
	 * 获取地磁的统计数据
	 * @return
	 */
	public Map<String,Object> findDcData();
	
	/**
	 * 获取井盖子设备的所以信息
	 * @return
	 */
	public  List<Map<String,Object>>  findManholeCoverData();

}