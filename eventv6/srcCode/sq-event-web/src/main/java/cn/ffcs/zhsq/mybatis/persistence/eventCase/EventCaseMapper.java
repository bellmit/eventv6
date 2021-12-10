package cn.ffcs.zhsq.mybatis.persistence.eventCase;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;

/**
 * 江西省罗坊镇案件
 * @author zhangls
 *
 */
public interface EventCaseMapper extends MyBatisBaseMapper<Map<String, Object>> {
	/**
	 * 依据案件id删除案件记录
	 * @param caseId	案件id
	 * @param delUserId	删除操作用户id
	 * @return
	 */
	public int delete(@Param(value="caseId") Long caseId, @Param(value="delUserId") Long delUserId);
	
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
	 * 			handledUserId	经办人员id
	 * 			handledOrgId	经办人员所属组织id
	 * @return
	 */
	public int findCount4Handled(Map<String, Object> param);

	/**
	 * 经办列表记录
	 * @param param 参数
	 * 			handledUserId	经办人员id
	 * 			handledOrgId	经办人员所属组织id
	 * @param bounds 分页信息
	 * @return
	 */
	public List<Map<String, Object>> findPageList4Handled(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 我发起的记录统计
	 * @param param
	 * 			initiatorId		我发起的人员id
	 * 			initiatorOrgId	我发起的所属组织id
	 * @return
	 */
	public int findCount4Initiator(Map<String, Object> param);

	/**
	 * 我发起的列表记录
	 * @param param 参数
	 * 			initiatorId		我发起的人员id
	 * 			initiatorOrgId	我发起的所属组织id
	 * @param bounds 分页信息
	 * @return
	 */
	public List<Map<String, Object>> findPageList4Initiator(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 辖区所有记录统计
	 * @param param
	 * 			infoOrgCode	地域编码
	 * @return
	 */
	public int findCount4Jurisdiction(Map<String, Object> param);

	/**
	 * 辖区所有列表记录
	 * @param param 参数
	 * 			infoOrgCode	地域编码
	 * @param bounds 分页信息
	 * @return
	 */
	public List<Map<String, Object>> findPageList4Jurisdiction(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 依据案件id获取督办信息
	 * @param caseId
	 * @return
	 */
	public List<Map<String, Object>> findRemindListByCaseId(Long caseId);
}
