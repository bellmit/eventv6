package cn.ffcs.zhsq.mybatis.persistence.szzg.EmeryencyPlan;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.szzg.EmeryencyPlan.EmeryencyPlanTree;

import java.util.List;
import java.util.Map;



public interface EmeryencyPlanTreeMapper  extends MyBatisBaseMapper<EmeryencyPlanTree> {
	
	public List<EmeryencyPlanTree> fingdTree(Map<String,Object> para);

	public EmeryencyPlanTree findTreeById(Map<String,Object> para);
	
	public List<EmeryencyPlanTree> fingdTreeByParent(Map<String,Object> para);

	public int insert(EmeryencyPlanTree emeryencyPlanTree);

	public int update(EmeryencyPlanTree emeryencyPlanTree);

}
