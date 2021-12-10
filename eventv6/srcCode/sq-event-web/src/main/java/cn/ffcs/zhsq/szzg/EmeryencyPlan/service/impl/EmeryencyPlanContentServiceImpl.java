package cn.ffcs.zhsq.szzg.EmeryencyPlan.service.impl;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.EmeryencyPlan.EmeryencyPlanContent;
import cn.ffcs.zhsq.mybatis.persistence.szzg.EmeryencyPlan.EmeryencyPlanContentMapper;
import cn.ffcs.zhsq.szzg.EmeryencyPlan.service.EmeryencyPlanContentService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service(value = "emeryencyPlanContentService")
public class EmeryencyPlanContentServiceImpl implements EmeryencyPlanContentService {

	@Autowired
    EmeryencyPlanContentMapper mapper;

	@Override
	public List<EmeryencyPlanContent> findById(
			Map<String, Object> param) {
		return mapper.findById(param);
	}

	@Override
	public List<EmeryencyPlanContent> findByTreeId(Map<String, Object> param) {
		return mapper.findByTreeId(param);
	}

	@Override
	public List<EmeryencyPlanContent> findByParams(Map<String, Object> param)
	{
		return mapper.findByParams(param);
	}

	@Override
	public EUDGPagination findPageListByCriteria(Map<String, Object> params,
                                                 int page, int rows) {
		RowBounds bounds = new RowBounds((page - 1) * rows, rows);
		int count =mapper.findCountByCriteria(params);
		List<EmeryencyPlanContent> list = mapper.findPageListByCriteria(params, bounds);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}

	@Override
	public Boolean insert(EmeryencyPlanContent e) {
		int count = mapper.insert(e);
		return  count>0;
	}
	
	@Override
	public Boolean update(EmeryencyPlanContent e) {
		int count =mapper.update(e);
		return count>0;
	}

	@Override
	public Boolean delete(Long id) {
		int count =mapper.delete(id);
		return count>0;
	}



}
