package cn.ffcs.zhsq.szzg.education.service.impl;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.szzg.education.EducationBO;
import cn.ffcs.zhsq.mybatis.persistence.szzg.education.EducationMapper;
import cn.ffcs.zhsq.szzg.education.service.IEducationService;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;


@Service(value="educationService")
public class EducationServiceImpl implements IEducationService {
	
	@Autowired
	private EducationMapper educationMapper;

	@Override
	public List<EducationBO> getEducationByParams(Map<String, Object> params) {

		return educationMapper.getEducationByParams(params);
	}

	/**
	 * 报表
	 * @param type 类型
	 * @param orgCode 区域
	 * @param yearStr 年份
	 */@Override
	public List<Map<String,Object>> findChartByEducation(Map<String,Object> param){
		return educationMapper.findChartByEducation(param);
	}

	@Override
	public EUDGPagination findPageListByCriteria(Map<String, Object> params,
			int page, int rows) {
		RowBounds bounds = new RowBounds((page - 1) * rows, rows);
		int count = educationMapper.findCountByCriteria(params);
		List<EducationBO> list = educationMapper.findPageListByCriteria(params, bounds);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}

	@Override
	public Boolean save(EducationBO education) {
		int count =educationMapper.insert(education);
		return count>0;
	}

	@Override
	public Boolean update(EducationBO education) {
		int count =educationMapper.update(education);
		return count>0;
	}

	@Override
	public Boolean delete(Long seqid) {

		int count = educationMapper.delete(seqid);

		return count > 0;
	}



	@Override
	public EducationBO findById(Long seqid) {
		
		return educationMapper.findById(seqid);
	}


	@Override
	public List<Map<String, Object>> getEducationCharts(
			Map<String, Object> params) {

		return educationMapper.getEducationCharts(params);
	}
	
	@Override
	public EUDGPagination findPageListByCriteriaPriSchool(Map<String, Object> params,
			int page, int rows) {
		RowBounds bounds = new RowBounds((page - 1) * rows, rows);
		int count = educationMapper.findCountByCriteriaPriSchool(params);
		List<Map<String, Object>> list = educationMapper.findPageListByCriteriaPriSchool(params, bounds);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}

	@Override
	public Map<String, Object> findPriEduById(Long seqid) {
		return educationMapper.findPriEduById(seqid);
	}

	@Override
	public boolean updatePriEdu(Map<String, Object> params) {
		int count =educationMapper.updatePriEdu(params);
		return count>0;
	}

	@Override
	public boolean deletePriEdu(Long seqid) {
		int count = educationMapper.deletePriEdu(seqid);

		return count > 0;
	}

	@Override
	public Long savePriEdu(List<Map<String, Object>> list,UserInfo userInfo) {
		Long result=0L;
		for (Map<String, Object> map : list) {
			int checkPriEdu = educationMapper.checkPriEdu(map);
			if(checkPriEdu==0){
				map.put("createId", userInfo.getUserId());
				educationMapper.insertPriEdu(map);
				result+=1;
			}
		}
		return result;
	}

}
