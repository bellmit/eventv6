package cn.ffcs.zhsq.szzg.EmeryencyPlan.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.EmeryencyPlan.EmeryencyPlanContent;

import java.util.List;
import java.util.Map;

public interface EmeryencyPlanContentService {

	public List<EmeryencyPlanContent> findById(Map<String, Object> param);

	public List<EmeryencyPlanContent> findByParams(Map<String, Object> param);

	/**
	 * 分页查询
	 * @param params
	 * @param page
	 * @param rows
	 * @return
	 */
	public EUDGPagination findPageListByCriteria(Map<String, Object> params, int page, int rows);

	public List<EmeryencyPlanContent> findByTreeId(Map<String, Object> param);

	public Boolean insert(EmeryencyPlanContent e);

	public Boolean update(EmeryencyPlanContent e);

	public Boolean delete(Long id);
}
