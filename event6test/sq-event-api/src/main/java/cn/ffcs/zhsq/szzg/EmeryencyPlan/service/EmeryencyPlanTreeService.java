package cn.ffcs.zhsq.szzg.EmeryencyPlan.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.EmeryencyPlan.EmeryencyPlanTree;
import java.util.List;
import java.util.Map;

public interface EmeryencyPlanTreeService {

	public List<EmeryencyPlanTree> fingdTree(Map<String, Object> para);

	public EmeryencyPlanTree findTreeById(Map<String, Object> para);


	/**
	 * 分页查询
	 * @param params
	 * @param page
	 * @param rows
	 * @return
	 */
	public EUDGPagination findPageListByCriteria(Map<String, Object> params, int page, int rows);

	public List<EmeryencyPlanTree> fingdTreeByParent(Map<String, Object> para);
	
	public Boolean insert(EmeryencyPlanTree emeryencyPlanTree);
	
	public Boolean update(EmeryencyPlanTree emeryencyPlanTree);

	public Boolean delete(Long id);

}
