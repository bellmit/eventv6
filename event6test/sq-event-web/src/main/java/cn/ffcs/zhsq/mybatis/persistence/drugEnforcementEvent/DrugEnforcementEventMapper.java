package cn.ffcs.zhsq.mybatis.persistence.drugEnforcementEvent;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.drugEnforcementEvent.DrugEnforcementEvent;

/**
 * 禁毒事件
 * @author zhangls
 *
 */
public interface DrugEnforcementEventMapper extends MyBatisBaseMapper<DrugEnforcementEvent> {
	/**
	 * 依据主键id获取禁毒事件记录
	 * @param params
	 * 			drugEnforcementId	禁毒事件id
	 * 			statusArray			禁毒事件状态
	 * @return
	 */
	public DrugEnforcementEvent findById(Map<String, Object> params);
	
	/**
	 * 依据主键删除记录
	 * @param drugEnforcementId	主键
	 * @param userId			删除操作用户
	 * @return
	 */
	public int delete(@Param(value="id") Long drugEnforcementId, @Param(value="delUserId") Long userId);
	
	/**
	 * 上报禁毒事件
	 * @param drugEnforcementEvent
	 * @return
	 */
	public int reportDrugEnforcementEvent(DrugEnforcementEvent drugEnforcementEvent);
	
	/**
	 * 获取待办记录统计数量
	 * @param params
	 * 			curUserId		当前人员id
	 * 			curOrgId		当前人员组织id
	 * 			workflowName	流程图名称
	 * 			wfTypeId		流程类型
	 * @return
	 */
	public int findTodoCountByCriteria(Map<String, Object> params);

	/**
	 * 分页获取待办记录列表
	 * @param params
	 * 			curUserId		当前人员id
	 * 			curOrgId		当前人员组织id
	 * 			workflowName	流程图名称
	 * 			wfTypeId		流程类型
	 * @param bounds
	 * @return
	 */
	public List<DrugEnforcementEvent> findTodoListByCriteria(Map<String, Object> params, RowBounds bounds);
	
	/**
	 * 获取经办记录统计数量
	 * @param params
	 * 			handledUserId	经办人员id
	 * 			handledOrgId	经办人员组织id
	 * 			workflowName	流程图名称
	 * 			wfTypeId		流程类型
	 * @return
	 */
	public int findHandledCountByCriteria(Map<String, Object> params);

	/**
	 * 分页获取经办记录列表
	 * @param params
	 * 			handledUserId	经办人员id
	 * 			handledOrgId	经办人员组织id
	 * 			workflowName	流程图名称
	 * 			wfTypeId		流程类型
	 * @param bounds
	 * @return
	 */
	public List<DrugEnforcementEvent> findHandledListByCriteria(Map<String, Object> params, RowBounds bounds);
}
