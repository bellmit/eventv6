package cn.ffcs.zhsq.szzg.greenManager.service.impl;

import java.util.List;
import java.util.Map;

import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.zhsq.mybatis.domain.szzg.greenManager.StatsGreenBO;
import cn.ffcs.zhsq.mybatis.persistence.szzg.greenManager.GreenIndicatorsMapper;
import cn.ffcs.zhsq.szzg.greenManager.service.GreenIndicatorsService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;


@Service(value="greenIndicatorsService")
public class GreenIndicatorsServiceImpl implements GreenIndicatorsService {
	
	@Autowired
	private GreenIndicatorsMapper greenIndicatorsMapper;
	@Autowired
	private IResMarkerService iResMarkerService;



	@Override
	public EUDGPagination findPageListByCriteria(Map<String, Object> params,
			int page, int rows) {
		RowBounds bounds = new RowBounds((page - 1) * rows, rows);
		int count  = greenIndicatorsMapper.findCountByCriteria(params);
		List<StatsGreenBO> list = greenIndicatorsMapper.findPageListByCriteria(params, bounds);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}

	@Override
	public StatsGreenBO findGreenIndicatorsById(Long seqid) {
		
		return greenIndicatorsMapper.findById(seqid);
	}

	@Override
	public Boolean update(StatsGreenBO statsGreen) {
		int count=greenIndicatorsMapper.update(statsGreen);
		return count>0;
	}

	@Override
	public Boolean del(Long seqid) {
		int count =greenIndicatorsMapper.delete(seqid);
		return count >0 ;
	}

	@Override
	public List<StatsGreenBO> findStatsGreen() {
		
		return greenIndicatorsMapper.findStatsGreen();
	}

	@Override
	public List<StatsGreenBO> findStatsGreenByParams(Map<String, Object> params) {
		
		return greenIndicatorsMapper.findStatsGreenByParams(params);
	}

	@Override
	public Boolean insert(StatsGreenBO statsGreen) {
		int count=greenIndicatorsMapper.insert(statsGreen);
		return  count>0;
	}

	@Override
	public Map<String,Object> findChartDataByQs(Map<String, Object> params) {
		params.put("qs_1",greenIndicatorsMapper.findCharDataByQs(params));
		params.put("qs_2",greenIndicatorsMapper.findCharDataByQs_2(params));
		return  params;
	}

}
