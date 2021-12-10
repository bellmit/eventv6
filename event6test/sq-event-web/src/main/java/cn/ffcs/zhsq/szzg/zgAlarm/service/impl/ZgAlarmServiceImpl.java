package cn.ffcs.zhsq.szzg.zgAlarm.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.zgAlarm.ZgAlarm;
import cn.ffcs.zhsq.mybatis.persistence.szzg.zgAlarm.ZgAlarmMapper;
import cn.ffcs.zhsq.szzg.zgAlarm.service.IZgAlarmService;

/**
 * @Description: 告警信息模块服务实现
 * @Author: linzhu
 * @Date: 08-07 15:03:54
 * @Copyright: 2017 福富软件
 */
@Service("zgAlarmServiceImpl")
@Transactional
public class ZgAlarmServiceImpl implements IZgAlarmService {

	@Autowired
	private ZgAlarmMapper zgAlarmMapper; //注入告警信息模块dao

	/**
	 * 新增数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	@Override
	public boolean insert(ZgAlarm bo) {
		return zgAlarmMapper.insert(bo)>0;
	}

	/**
	 * 修改数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	@Override
	public boolean update(ZgAlarm bo) {
		int result = zgAlarmMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	@Override
	public boolean delete(ZgAlarm bo) {
		int result = zgAlarmMapper.delete(bo);
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
		List<ZgAlarm> list = zgAlarmMapper.searchList(params, rowBounds);
		int count = zgAlarmMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 业务id
	 * @return 业务对象
	 */
	@Override
	public ZgAlarm searchById(Long id) {
		ZgAlarm bo = zgAlarmMapper.searchById(id);
		return bo;
	}

	@Override
	public List<ZgAlarm> findZgAlarmList() {
		return zgAlarmMapper.findZgAlarmList();
	}

	@Override
	public int findZgAlarmCount() {
		return zgAlarmMapper.findZgAlarmCount();
	}

}