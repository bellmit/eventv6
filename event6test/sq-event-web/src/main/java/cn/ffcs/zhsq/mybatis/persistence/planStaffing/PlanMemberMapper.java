package cn.ffcs.zhsq.mybatis.persistence.planStaffing;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.planStaffing.PlanMember;

/**
 * @Description: 应急预案人员模块dao接口
 * @Author: LINZHU
 * @Date: 04-28 16:17:29
 * @Copyright: 2021 福富软件
 */
public interface PlanMemberMapper {
	
	/**
	 * 新增数据
	 * @param bo 应急预案人员业务对象
	 * @return 应急预案人员id
	 */
	public long insert(PlanMember bo);
	
	/**
	 * 修改数据
	 * @param bo 应急预案人员业务对象
	 * @return 修改的记录数
	 */
	public long update(PlanMember bo);
	
	/**
	 * 删除数据
	 * @param bo 应急预案人员业务对象
	 * @return 删除的记录数
	 */
	public long delete(PlanMember bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 应急预案人员数据列表
	 */
	public List<PlanMember> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 应急预案人员数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 应急预案人员id
	 * @return 应急预案人员业务对象
	 */
	public PlanMember searchById(String id);
	
	/**
	 * 批量删除 
	 * 根据bizId删除不存在list中的数据
	 * @param params 
	 * @return
	 */
	public long batchDelete( Map<String, Object> params);
	
	/**
	 * 批量保存
	 * @param 
	 * @return
	 */
	public long batchInsert(@Param("list")List<PlanMember> vList);
	/**
	 * 批量保存
	 * @param vList
	 * @return
	 */
	public long batchUpdate(@Param("list")List<PlanMember> vList);
	/**
	 *  根据参数获取人员列表  
	 * @param params 
	 * planType 预案类型    planLevel 预案级别    等参数查询人员列表   regionCodeAll 区域代码全匹配  regionCode 区域代码模糊匹配
	 * @return
	 */
	public List<PlanMember> findPlanMemberListParams(Map<String, Object> params);

}