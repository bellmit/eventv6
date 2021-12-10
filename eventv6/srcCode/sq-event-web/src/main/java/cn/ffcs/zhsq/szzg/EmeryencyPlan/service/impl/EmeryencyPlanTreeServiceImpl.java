package cn.ffcs.zhsq.szzg.EmeryencyPlan.service.impl;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.EmeryencyPlan.EmeryencyPlanTree;
import cn.ffcs.zhsq.mybatis.persistence.szzg.EmeryencyPlan.EmeryencyPlanTreeMapper;
import cn.ffcs.zhsq.szzg.EmeryencyPlan.service.EmeryencyPlanTreeService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service(value = "emeryencyPlanTreeService")
public class EmeryencyPlanTreeServiceImpl implements EmeryencyPlanTreeService {

	@Autowired
    EmeryencyPlanTreeMapper mapper;
	
	@Override
	public List<EmeryencyPlanTree> fingdTree(Map<String, Object> para) {
		List<EmeryencyPlanTree> list = mapper.fingdTree(para);
		return list;// mapper.fingdTree(para);
	}

	@Override
	public EmeryencyPlanTree findTreeById(Map<String, Object> para) {
		EmeryencyPlanTree emeryencyPlanTree = mapper.findTreeById(para);
		return emeryencyPlanTree;// mapper.fingdTree(para);
	}

	@Override
	public EUDGPagination findPageListByCriteria(Map<String, Object> params,
                                                 int page, int rows) {
		RowBounds bounds = new RowBounds((page - 1) * rows, rows);
		int count =mapper.findCountByCriteria(params);
		List<EmeryencyPlanTree> list = mapper.findPageListByCriteria(params, bounds);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}

	@Override
	public List<EmeryencyPlanTree> fingdTreeByParent(Map<String, Object> para) {
		return mapper.fingdTreeByParent(para);
	}
	@Override
	public Boolean insert(EmeryencyPlanTree e) {
		int count =mapper.insert(e);
		return count>0;
	}
	@Override
	public Boolean update(EmeryencyPlanTree emeryencyPlanTree) {
		int count =mapper.update(emeryencyPlanTree);
		return count>0;

	}

	@Override
	public Boolean delete(Long id) {
		int count =mapper.delete(id);
		return count>0;

	}

}
