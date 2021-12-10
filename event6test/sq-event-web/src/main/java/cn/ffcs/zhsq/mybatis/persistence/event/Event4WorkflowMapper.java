package cn.ffcs.zhsq.mybatis.persistence.event;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

/**
 * 事件与工作流关联相关接口
 * @author zhangls
 *
 */
public interface Event4WorkflowMapper {
	/**
	 * 草稿记录统计
	 * @param param
	 * 			curUserId	办理人id
	 * 			curOrgId	办理人所属组织id
	 * @return
	 */
	public int findCount4Draft(Map<String, Object> param);

	/**
	 * 草稿列表记录
	 * @param param 参数
	 * 			curUserId	办理人id
	 * 			curOrgId	办理人所属组织id
	 * @param bounds 分页信息
	 * @return
	 */
	public List<Map<String, Object>> findPageList4Draft(Map<String, Object> param, RowBounds bounds);
	/**
	 * 待办记录统计
	 * @param param
	 * 			curUserId	办理人id
	 * 			curOrgId	办理人所属组织id
	 * @return
	 */
	public int findCount4Todo(Map<String, Object> param);

	/**
	 * 待办列表记录
	 * @param param 参数
	 * 			curUserId	办理人id
	 * 			curOrgId	办理人所属组织id
	 * @param bounds 分页信息
	 * @return
	 */
	public List<Map<String, Object>> findPageList4Todo(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 经办记录统计
	 * @param param
	 * 			handledUserId	经办理人id
	 * 			handledOrgId	经办理人所属组织id
	 * @return
	 */
	public int findCount4Handled(Map<String, Object> param);

	/**
	 * 经办列表记录
	 * @param param 参数
	 * 			handledUserId	经办理人id
	 * 			handledOrgId	经办理人所属组织id
	 * @param bounds 分页信息
	 * @return
	 */
	public List<Map<String, Object>> findPageList4Handled(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 辖区所有(办理人员查询)记录统计
	 * @param param
	 * 			eventOrgUserId	待办/经办办理人员id
	 * 			eventOrgOrgId	待办/经办办理组织id
	 * @return
	 */
	public int findCount4EventOrg(Map<String, Object> param);

	/**
	 * 辖区所有(办理人员查询)列表记录
	 * @param param 参数
	 * 			eventOrgUserId	待办/经办办理人员id
	 * 			eventOrgOrgId	待办/经办办理组织id
	 * @param bounds 分页信息
	 * @return
	 */
	public List<Map<String, Object>> findPageList4EventOrg(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 归档列表统计
	 * @param param
	 * @return
	 */
	public int findCount4Archive(Map<String, Object> param);

	/**
	 * 归档列表记录
	 * @param param 参数
	 * @param bounds 分页信息
	 * @return
	 */
	public List<Map<String, Object>> findPageList4Archive(Map<String, Object> param, RowBounds bounds);
	
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
	 * 我发起的列表统计
	 * @param param
	 * @return
	 */
	public int findCount4Initiator(Map<String, Object> param);

	/**
	 * 我发起的列表记录
	 * @param param 参数
	 * 			initiatorId		事件发起用户id
	 * 			initiatorOrgId	事件发起组织id
	 * @param bounds 分页信息
	 * @return
	 */
	public List<Map<String, Object>> findPageList4Initiator(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 辖区所有关注列表统计
	 * @param param
	 * 			attentionUserId		事件关注用户id
	 * @return
	 */
	public int findCount4Attention(Map<String, Object> param);

	/**
	 * 辖区所有关注列表记录
	 * @param param 参数
	 * 			attentionUserId		事件关注用户id
	 * @param bounds 分页信息
	 * @return
	 */
	public List<Map<String, Object>> findPageList4Attention(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 辖区所有需要督办列表统计
	 * @param param
	 * @return
	 */
	public int findCount4Supervise(Map<String, Object> param);

	/**
	 * 辖区所有需要督办列表记录
	 * @param param 参数
	 * @param bounds 分页信息
	 * @return
	 */
	public List<Map<String, Object>> findPageList4Supervise(Map<String, Object> param, RowBounds bounds);
	
}
