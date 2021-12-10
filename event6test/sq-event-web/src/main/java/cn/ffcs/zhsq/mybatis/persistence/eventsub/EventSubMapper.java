package cn.ffcs.zhsq.mybatis.persistence.eventsub;

import cn.ffcs.zhsq.mybatis.domain.eventsub.EventSub;
import org.apache.ibatis.session.RowBounds;
import java.util.List;
import java.util.Map;


/**
 * @Description: event_na模块dao接口
 * @Author: chenshikai
 * @Date: 07-03 14:26:31
 * @Copyright: 2020 福富软件
 */
public interface EventSubMapper {
	
	/**
	 * 新增数据
	 * @param bo event_na业务对象
	 * @return event_naid
	 */
	public long insert(EventSub bo);
	
	/**
	 * 修改数据
	 * @param bo event_na业务对象
	 * @return 修改的记录数
	 */
	public long update(EventSub bo);
	
	/**
	 * 删除数据
	 * @param param bo event_na业务对象
	 * @return 删除的记录数
	 */
	Boolean delete(Map<String, Object> param);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return event_na数据列表
	 */
	public List<EventSub> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return event_na数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id event_naid
	 * @return event_na业务对象
	 */
	public EventSub searchById(Long id);


	/**
	 * 批量插入 csk    南安7类对接事件
	 *
	 * */
	public boolean insertList(List<Map<String, Object>> list);

	/**
	 * 统计 南安7类对接事件 数量总数
	 *
	 * */
	public List<Map<String,Object>> findStatusNum(Map<String, Object> params);


	/**
	 * 全删表内容,根据PID进行删除
	 *
	 * */
	public boolean delTypeRela(List<Map<String, Object>> list);

}