package cn.ffcs.zhsq.devicecollectdata.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.devicecollectdata.service.DeviceRunningDataService;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceRunningData;
import cn.ffcs.zhsq.mybatis.persistence.devicecollectdata.DeviceRunningDataMapper;

/**
 * @Description: 设备运行数据表模块服务实现
 * @Author: husp
 * @Date: 11-28 08:49:19
 * @Copyright: 2017 福富软件
 */
@Service("deviceRunningDataServiceImpl")
@Transactional
public class DeviceRunningDataServiceImpl implements DeviceRunningDataService {

	@Autowired
	private DeviceRunningDataMapper deviceRunningDataMapper; //注入设备运行数据表模块dao

	/**
	 * 新增数据
	 * @param bo 设备运行数据表业务对象
	 * @return 设备运行数据表id
	 */
	@Override
	public Long insert(DeviceRunningData bo) {
		deviceRunningDataMapper.insert(bo);
		return bo.getRunId();
	}

	/**
	 * 修改数据
	 * @param bo 设备运行数据表业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(DeviceRunningData bo) {
		long result = deviceRunningDataMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 设备运行数据表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(DeviceRunningData bo) {
		long result = deviceRunningDataMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 设备运行数据表分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<DeviceRunningData> list = deviceRunningDataMapper.searchList(params, rowBounds);
		long count = deviceRunningDataMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 设备运行数据表分页数据对象
	 */
	@Override
	public List<DeviceRunningData> findList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<DeviceRunningData> list = deviceRunningDataMapper.searchList(params, rowBounds);
		return list;
	}
	/**
	 * 查询数据
	 * @param params 查询参数
	 * @return 设备运行数据表分页数据对象
	 */
	@Override
	public List<DeviceRunningData> findByDeviceServiceId(String deviceServiceId) {
		List<DeviceRunningData> list = deviceRunningDataMapper.findByDeviceServiceId(deviceServiceId);
		return list;
	}
	/**
	 * 根据业务id查询数据
	 * @param id 设备运行数据表id
	 * @return 设备运行数据表业务对象
	 */
	@Override
	public DeviceRunningData searchById(Long id) {
		DeviceRunningData bo = deviceRunningDataMapper.searchById(id);
		return bo;
	}

}