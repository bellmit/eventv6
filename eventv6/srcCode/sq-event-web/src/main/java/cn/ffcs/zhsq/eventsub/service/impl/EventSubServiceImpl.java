package cn.ffcs.zhsq.eventsub.service.impl;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.eventsub.service.EventSubService;
import cn.ffcs.zhsq.mybatis.domain.eventsub.EventSub;
import cn.ffcs.zhsq.mybatis.persistence.eventsub.EventSubMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.ffcs.system.publicUtil.EUDGPagination;


/**
 * @Description: event_na模块服务实现
 * @Author: chenshikai
 * @Date: 07-03 14:26:31
 * @Copyright: 2020 福富软件
 */
@Service("eventSubServiceImpl")
@Transactional
public class EventSubServiceImpl implements EventSubService {

	@Autowired
	private EventSubMapper eventSubMapper; //注入event_na模块dao

	/**
	 * 新增数据
	 * @param bo event_na业务对象
	 * @return event_naid
	 */
	@Override
	public Long insert(EventSub bo) {
		eventSubMapper.insert(bo);
		return bo.getTypeId();
	}

	/**
	 * 修改数据
	 * @param bo event_na业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(EventSub bo) {
		long result = eventSubMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo event_na业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(Map<String,Object> param) {
		Boolean result = eventSubMapper.delete(param);
		return result ;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return event_na分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<EventSub> list = eventSubMapper.searchList(params, rowBounds);
		long count = eventSubMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id event_naid
	 * @return event_na业务对象
	 */
	@Override
	public EventSub searchById(Long id) {
		EventSub bo = eventSubMapper.searchById(id);
		return bo;
	}

}