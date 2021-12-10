package cn.ffcs.zhsq.planStaffing;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.planStaffing.PlanMember;

/**
 * @Description: 应急预案人员模块服务
 * @Author: LINZHU
 * @Date: 04-28 16:17:29
 * @Copyright: 2021 福富软件
 */
public interface IPlanMemberService {

	/**
	 * 新增数据
	 * @param bo 应急预案人员业务对象
	 * @return 应急预案人员id
	 */
	public String insert(PlanMember bo);

	/**
	 * 修改数据
	 * @param bo 应急预案人员业务对象
	 * @return 是否修改成功
	 */
	public boolean update(PlanMember bo);

	/**
	 * 删除数据
	 * @param bo 应急预案人员业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(PlanMember bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 应急预案人员分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 应急预案人员id
	 * @return 应急预案人员业务对象
	 */
	public PlanMember searchById(String id,String userOrgCode);
	

	
	 /** 根据bizId批量删除不存在list的数据
	 * @param params
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public long batchDelete( Map<String, Object> params);
	
	/**
	 * 批量保存
	 * @param vList
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public long batchInsert(List<PlanMember> vList);
	/**
	 * 批量保存
	 * @param vList
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public long batchUpdate(List<PlanMember> vList);

	/**
	 *  根据参数获取人员列表  
	 * @param params 
	 * planType 预案类型    planLevel 预案级别    等参数查询人员列表   regionCodeAll 区域代码全匹配  regionCode 区域代码模糊匹配  
	 * isCN为true时会转字典名称
	 * @return
	 */
	public List<PlanMember> findPlanMemberListByParams(Map<String, Object> params);
}