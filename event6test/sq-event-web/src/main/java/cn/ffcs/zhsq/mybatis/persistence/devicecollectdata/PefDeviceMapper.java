package cn.ffcs.zhsq.mybatis.persistence.devicecollectdata;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.PefDevice;

/**
 * @Description: 井盖关联子设备模块dao接口
 * @Author: luth
 * @Date: 10-24 08:55:35
 * @Copyright: 2017 福富软件
 */
public interface PefDeviceMapper {
	
	/**
	 * 新增数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public long insert(PefDevice bo);
	
	/**
	 * 修改数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public long update(PefDevice bo);
	
	/**
	 * 删除数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public long delete(PefDevice bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 数据列表
	 */
	public List<PefDevice> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 业务id
	 * @return 业务对象
	 */
	public PefDevice searchById(Long id);

}