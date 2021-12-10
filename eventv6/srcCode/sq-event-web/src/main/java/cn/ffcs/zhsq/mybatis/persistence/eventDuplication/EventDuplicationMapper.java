package cn.ffcs.zhsq.mybatis.persistence.eventDuplication;

import java.util.List;
import java.util.Map;

/**
 * 重复事件数据操作，对应表T_EVENT_DUPLICATION
 * @author zhangls
 *
 */
public interface EventDuplicationMapper {
	/**
	 * 新增/更新重复事件记录
	 * @param eventDuplication		重复事件信息
	 * 						duplicationId		主键，类型为Long
	 * 						leadEventId			主事件id，类型为Long
	 * 						duplicateEventId	重复事件id，类型为Long
	 * @return
	 */
	public int insertOrUpdate(List<Map<String, Object>> eventDuplication);
	
	/**
	 * 获取重复事件记录数量
	 * @param params	查询参数
	 * 						leadEventId	主事件id
	 * @return
	 */
	public int findDuplicatedEventCount(Map<String, Object> params);
	
	/**
	 * 获取重复事件记录信息
	 * @param params	查询参数
	 * 						leadEventId	主事件id
	 * @return
	 */
	public List<Map<String, Object>> findDuplicatedEventList(Map<String, Object> params);
	
	/**
	 * 删除重复事件记录
	 * @param eventDuplication		重复事件信息
	 * 						duplicationId		主键，类型为Long
	 * 						leadEventId			主事件id，类型为Long
	 * 						duplicateEventId	重复事件id，类型为Long
	 * @return
	 */
	public int delete(Map<String, Object> eventDuplication);
}
