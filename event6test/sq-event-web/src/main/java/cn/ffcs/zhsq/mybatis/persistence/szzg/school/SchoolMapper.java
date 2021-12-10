package cn.ffcs.zhsq.mybatis.persistence.szzg.school;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;

import cn.ffcs.zhsq.mybatis.domain.szzg.school.SchoolBo;

import java.util.List;
import java.util.Map;


public interface SchoolMapper extends MyBatisBaseMapper<SchoolBo> {
	
	public List<SchoolBo> findSchoolMark(Map<String, Object> params);

	public int del(Long seqid);
	
	public List<SchoolBo> findSchoolPoint(Map<String, Object> params);
	
	public List<Map<String, Object>> getSchoolChartsSchool(Map<String, Object> params);

	public List<Map<String, Object>> getSchoolChartsMalesFemales(Map<String, Object> params);

	public List<Map<String, Object>> findSchoolCount1(Map<String, Object> params);

	public List<Map<String, Object>> findSchoolCount2(Map<String, Object> params);

	public int isExit(SchoolBo schoolBo);

	/**
	 * 查询树形
	 * @param param
	 * @return
	 */
	public List<SchoolBo> findTreeTable(Map<String,Object> param);

}
