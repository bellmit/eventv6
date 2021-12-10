package cn.ffcs.zhsq.event.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.event.service.IPatrolService;
import cn.ffcs.zhsq.mybatis.domain.event.Patrol;
import cn.ffcs.zhsq.mybatis.persistence.event.EventDisposalMapper;
import cn.ffcs.zhsq.mybatis.persistence.event.PatrolMapper;
import cn.ffcs.zhsq.utils.DateUtils;

@Service(value = "patrolServiceImpl")
public class PatrolServiceImpl implements IPatrolService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private EventDisposalMapper eventDisposalMapper;
	
	@Autowired
	private PatrolMapper patrolMapper;
	
	@Override
	public int deleteByIdForLogic(Patrol patrol){
		return patrolMapper.deleteByIdForLogic(patrol);
	}
	
	@Override
	public int updateByPrimaryKeySelective(Patrol patrol){
		return patrolMapper.updateByPrimaryKeySelective(patrol);
	}
	
	@Override
	public Patrol findById(Long patrolId){
		Patrol Patrol = patrolMapper.selectByPrimaryKey(patrolId);
		return Patrol;
	}

	@Override
	public String getPatrolCode(){
		String patrolCode = DateUtils.formatDate(new Date(), "yyyyMMdd");
		String patrolSequence = patrolMapper.getPotarlSequence()+"";
		switch (patrolSequence.length()) {
		case 1:
			patrolCode = patrolCode + "_" + "0000" + patrolSequence;
			break;
		case 2:
			patrolCode = patrolCode + "_" + "000" + patrolSequence;
			break;
		case 3:
			patrolCode = patrolCode + "_" + "00" + patrolSequence;
			break;
		case 4:
			patrolCode = patrolCode + "_" + "0" + patrolSequence;
			break;
		case 5:
			patrolCode = patrolCode + "_" + patrolSequence;
			break;
		default:
			break;
		}
		return patrolCode;
	}

	@Override
	public EUDGPagination findPatrolPagination(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo<1?1:pageNo;
		pageSize = pageSize<1?10:pageSize;
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		int count = patrolMapper.findCountByCriteria(params);
		List<Patrol> list = patrolMapper.findPageListByCriteria(params, rowBounds);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}
	
	@Override
	public int insertPatrol(Patrol patrol){
		return patrolMapper.insert(patrol);
	}
	

	@Override
	public int updatePatrol(Patrol patrol){
		return patrolMapper.updateByPrimaryKeySelective(patrol);
	}
}
