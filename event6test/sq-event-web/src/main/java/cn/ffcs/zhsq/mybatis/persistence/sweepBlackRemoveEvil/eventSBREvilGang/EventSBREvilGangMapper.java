package cn.ffcs.zhsq.mybatis.persistence.sweepBlackRemoveEvil.eventSBREvilGang;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.sweepBlackRemoveEvil.eventSBREvilGang.EventSBREvilGang;

/**
 * @Description: 扫黑除恶_黑恶团伙管理模块dao接口
 * @Author: LINZHU
 * @Date: 05-23 10:37:41
 * @Copyright: 2018 福富软件
 */
public interface EventSBREvilGangMapper {
	
	/**
	 * 新增数据
	 * @param bo 扫黑除恶_黑恶团伙管理业务对象
	 * @return 扫黑除恶_黑恶团伙管理id
	 */
	public long insert(EventSBREvilGang bo);
	
	/**
	 * 修改数据
	 * @param bo 扫黑除恶_黑恶团伙管理业务对象
	 * @return 修改的记录数
	 */
	public long update(EventSBREvilGang bo);
	
	/**
	 * 删除数据
	 * @param bo 扫黑除恶_黑恶团伙管理业务对象
	 * @return 删除的记录数
	 */
	public long delete(EventSBREvilGang bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 扫黑除恶_黑恶团伙管理数据列表
	 */
	public List<EventSBREvilGang> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 扫黑除恶_黑恶团伙管理数据总数
	 */
	public long countList(Map<String, Object> params);
	
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