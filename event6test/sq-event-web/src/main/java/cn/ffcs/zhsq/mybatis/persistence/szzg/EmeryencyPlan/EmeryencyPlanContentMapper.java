package cn.ffcs.zhsq.mybatis.persistence.szzg.EmeryencyPlan;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.szzg.EmeryencyPlan.EmeryencyPlanContent;

import java.util.List;
import java.util.Map;



public interface EmeryencyPlanContentMapper  extends MyBatisBaseMapper<EmeryencyPlanContent> {
	
	public List<EmeryencyPlanContent> findById(Map<String,Object> param);

	public List<EmeryencyPlanContent> findByTreeId(Map<String,Object> param);

	public List<EmeryencyPlanContent> findByParams(Map<String,Object> param);
}
