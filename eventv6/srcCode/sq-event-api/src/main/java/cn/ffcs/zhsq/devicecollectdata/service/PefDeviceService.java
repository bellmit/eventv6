package cn.ffcs.zhsq.devicecollectdata.service;

import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.PefDevice;

/**
 * @Description: 井盖关联子设备模块服务
 * @Author: luth
 * @Date: 10-24 08:55:35
 * @Copyright: 2017 福富软件
 */
public interface PefDeviceService {

	/**
	 * 新增数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public Long insert(PefDevice bo);

	/**
	 * 修改数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public boolean update(PefDevice bo);

	/**
	 * 删除数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public boolean delete(PefDevice bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 数据列表
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 业务id
	 * @return 业务对象
	 */
	public PefDevice searchById(Long id);

}