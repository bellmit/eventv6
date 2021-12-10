package cn.ffcs.zhsq.mybatis.persistence.eventExtend;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 事件扩展属性
 * @author zhangls
 *
 */
public interface EventExtendMapper extends MyBatisBaseMapper<Map<String, Object>> {
	/**
	 * 依据id删除扩展属性信息
	 * @param extendId	主键id
	 * @param delUserId	删除用户id
	 * @return
	 */
	public int delete(@Param(value="extendId") Long extendId, @Param(value="delUserId") Long delUserId);
	
	/**
	 * 依据事件id获取扩展属性
	 * @param eventId	事件id
	 * @return
	 */
	public Map<String, Object> findByEventId(Long eventId);
	
	/**
	 * 获取辖区内事件数量
	 * @param param
	 * @return
	 */
	public int findCount4EventAll(Map<String, Object> param);
	
	/**
	 * 分页获取辖区内事件记录
	 * @param param
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findPageList4EventAll(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 获取辖区内事件数量(必有扩展信息)
	 * @param param
	 * @return
	 */
	public int findCount4EventExtendAll(Map<String, Object> param);
	
	/**
	 * 分页获取辖区内事件记录(必有扩展信息)
	 * @param param
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findPageList4EventExtendAll(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 获取待办事件数量
	 * @param param
	 * 			curUserId	当前办理人员id，String类型
	 * 			curOrgId	当前办理组织id，String类型
	 * @return
	 */
	public int findCount4EventTodo(Map<String, Object> param);
	
	/**
	 * 分页获取待办事件记录
	 * @param param
	 * 			curUserId	当前办理人员id，String类型
	 * 			curOrgId	当前办理组织id，String类型
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findPageList4EventTodo(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 依据事件id获取祖先数量
	 * @param params
	 * 			eventId	事件id
	 * @return
	 */
	public int findAncestorCountByEventId(Map<String, Object> params);
	
	/**
	 * 依据事件id获取祖先列表
	 * @param params
	 * 			eventId	事件id
	 * @return
	 */
	public List<Map<String, Object>> findAncestorListByEventId(Map<String, Object> params);

	/**
	 * 依据事件id获取发生过驳回操作的环节列表
	 * @param params
	 * 			eventId	事件id
	 * @return
	 */
	public List<Map<String, Object>> findRejectTaskByEventId(Map<String, Object> params);

}
