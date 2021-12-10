package cn.ffcs.zhsq.sweepBlackRemoveEvil.eventSBREvilGang.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.sweepBlackRemoveEvil.eventSBREvilGang.EventSBREvilGang;

/**
 * @Description: 扫黑除恶_黑恶团伙管理模块服务
 * @Author: LINZHU
 * @Date: 05-23 10:37:41
 * @Copyright: 2018 福富软件
 */
public interface IEventSBREvilGangService {

	/**
	 * 新增数据
	 * @param bo 扫黑除恶_黑恶团伙管理业务对象
	 * @return 扫黑除恶_黑恶团伙管理id
	 */
	public Long insert(EventSBREvilGang bo);

	/**
	 * 修改数据
	 * @param bo 扫黑除恶_黑恶团伙管理业务对象
	 * @return 是否修改成功
	 */
	public boolean update(EventSBREvilGang bo);

	/**
	 * 删除数据
	 * @param bo 扫黑除恶_黑恶团伙管理业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(EventSBREvilGang bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 扫黑除恶_黑恶团伙管理分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 扫黑除恶_黑恶团伙管理id
	 * @return 扫黑除恶_黑恶团伙管理业务对象
	 */
	public EventSBREvilGang searchById(Long id);
	
	/**
	 * 根据参数获取列表 
	 * @param params
	 * @return
	 */
	public List<EventSBREvilGang> getListByParams(Map<String, Object> params);
	

}