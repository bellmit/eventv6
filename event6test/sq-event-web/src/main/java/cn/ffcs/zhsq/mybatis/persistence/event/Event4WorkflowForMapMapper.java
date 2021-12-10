package cn.ffcs.zhsq.mybatis.persistence.event;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

/**
 * 事件与工作流关联相关接口
 * @author zhangls
 *
 */
public interface Event4WorkflowForMapMapper {
	
	
	/**
	 * 待办记录统计
	 * @param param
	 * @return
	 */
	public int findCount4Todo(Map<String, Object> param);
	

	/**
	 * 待办列表记录
	 * @param param 参数
	 * @param bounds 分页信息
	 * @return
	 */
	public List<Map<String, Object>> findPageList4Todo(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 待办列表记录
	 * @param param 参数
	 * @return
	 */
	public List<Map<String, Object>> findPageList4Todo(Map<String, Object> param);
	
	
	
	/**
	 * 辖区所有列表统计
	 * @param param
	 * @return
	 */
	public int findCount4Jurisdiction(Map<String, Object> param);
	
	

	/**
	 * 辖区所有列表记录
	 * @param param 参数
	 * @param bounds 分页信息
	 * @return
	 */
	public List<Map<String, Object>> findPageList4Jurisdiction(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 辖区所有列表记录
	 * @param param 参数
	 * @return
	 */
	public List<Map<String, Object>> findPageList4Jurisdiction(Map<String, Object> param);
	
	/**
	 * 辖区所有列表记录数量(精简)
	 * @param param 参数
	 * @return
	 */
	public Long getEventCountSimple(Map<String, Object> params);
	
	/**
	 * 辖区所有列表记录（精简）
	 * @param param 参数
	 * @return
	 */
	public List<Map<String,Object>> getEventListSimple(Map<String, Object> params, RowBounds bounds);
	
	
	/**
	 * 辖区所有列表记录（精简）
	 * @param param 参数
	 * @return
	 */
	public Map<String,Object> getEventDetail(Long eventId);
	
	
	
	
	/**
	 * 辖区所有相似事件总数
	 * @param param 参数
	 * @return
	 */
	public Long getSimilarEventCount(Map<String, Object> params);
	
	/**
	 * 辖区所有相似事件列表
	 * @param param 参数
	 * @return
	 */
	public List<Map<String,Object>> getSimilarEventList(Map<String, Object> params, RowBounds bounds);
	
	
	
	
}
