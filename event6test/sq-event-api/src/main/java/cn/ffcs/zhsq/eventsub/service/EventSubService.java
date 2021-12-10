package cn.ffcs.zhsq.eventsub.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.eventsub.EventSub;

import java.util.Map;


/**
 * @Description: event_na模块服务
 * @Author: chenshikai
 * @Date: 07-03 14:26:31
 * @Copyright: 2020 福富软件
 */
public interface EventSubService {

	/**
	 * 新增数据
	 * @param bo event_na业务对象
	 * @return event_naid
	 */
	public Long insert(EventSub bo);

	/**
	 * 修改数据
	 * @param bo event_na业务对象
	 * @return 是否修改成功
	 */
	public boolean update(EventSub bo);

	/**
	 * 删除数据
	 * @param bo event_na业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(Map<String,Object> param);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return event_na分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id event_naid
	 * @return event_na业务对象
	 */
	public EventSub searchById(Long id);

}