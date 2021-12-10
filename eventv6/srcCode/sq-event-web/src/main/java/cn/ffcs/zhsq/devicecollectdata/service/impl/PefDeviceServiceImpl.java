package cn.ffcs.zhsq.devicecollectdata.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.devicecollectdata.service.PefDeviceService;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.PefDevice;
import cn.ffcs.zhsq.mybatis.persistence.devicecollectdata.PefDeviceMapper;

/**
 * @Description: 井盖关联子设备模块服务实现
 * @Author: luth
 * @Date: 10-24 08:55:35
 * @Copyright: 2017 福富软件
 */
@Service("pefDeviceServiceImpl")
@Transactional
public class PefDeviceServiceImpl implements PefDeviceService {

	@Autowired
	private PefDeviceMapper pefDeviceMapper; //注入井盖关联子设备模块dao

	/**
	 * 新增数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	@Override
	public Long insert(PefDevice bo) {
		pefDeviceMapper.insert(bo);
		return bo.getRefDevId();
	}

	/**
	 * 修改数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	@Override
	public boolean update(PefDevice bo) {
		long result = pefDeviceMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	@Override
	public boolean delete(PefDevice bo) {
		long result = pefDeviceMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 数据列表
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<PefDevice> list = pefDeviceMapper.searchList(params, rowBounds);
		long count = pefDeviceMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 业务id
	 * @return 业务对象
	 */
	@Override
	public PefDevice searchById(Long id) {
		PefDevice bo = pefDeviceMapper.searchById(id);
		return bo;
	}

}