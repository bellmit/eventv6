package cn.ffcs.zhsq.sweepBlackRemoveEvil.eventSBREvilGang.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.sweepBlackRemoveEvil.eventSBREvilGang.EventSBREvilGang;
import cn.ffcs.zhsq.mybatis.persistence.sweepBlackRemoveEvil.eventSBREvilGang.EventSBREvilGangMapper;
import cn.ffcs.zhsq.sweepBlackRemoveEvil.eventSBREvilGang.service.IEventSBREvilGangService;

/**
 * @Description: 扫黑除恶_黑恶团伙管理模块服务实现
 * @Author: LINZHU
 * @Date: 05-23 10:37:41
 * @Copyright: 2018 福富软件
 */
@Service("eventSBREvilGangServiceImpl")
@Transactional
public class EventSBREvilGangServiceImpl implements IEventSBREvilGangService {

	@Autowired
	private EventSBREvilGangMapper eventSBREvilGangMapper; //注入扫黑除恶_黑恶团伙管理模块dao

	/**
	 * 新增数据
	 * @param bo 扫黑除恶_黑恶团伙管理业务对象
	 * @return 扫黑除恶_黑恶团伙管理id
	 */
	@Override
	public Long insert(EventSBREvilGang bo) {
		eventSBREvilGangMapper.insert(bo);
		return bo.getGangId();
	}

	/**
	 * 修改数据
	 * @param bo 扫黑除恶_黑恶团伙管理业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(EventSBREvilGang bo) {
		long result = eventSBREvilGangMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 扫黑除恶_黑恶团伙管理业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(EventSBREvilGang bo) {
		long result = eventSBREvilGangMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 扫黑除恶_黑恶团伙管理分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<EventSBREvilGang> list = eventSBREvilGangMapper.searchList(params, rowBounds);
		long count = eventSBREvilGangMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 扫黑除恶_黑恶团伙管理id
	 * @return 扫黑除恶_黑恶团伙管理业务对象
	 */
	@Override
	public EventSBREvilGang searchById(Long id) {
		EventSBREvilGang bo = eventSBREvilGangMapper.searchById(id);
		return bo;
	}

	@Override
	public List<EventSBREvilGang> getListByParams(
			Map<String, Object> params) {
		return eventSBREvilGangMapper.getListByParams(params);
	}



}