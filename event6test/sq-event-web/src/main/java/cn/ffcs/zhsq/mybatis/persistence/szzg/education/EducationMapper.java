package cn.ffcs.zhsq.mybatis.persistence.szzg.education;


import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.szzg.education.EducationBO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;



public interface EducationMapper extends MyBatisBaseMapper<EducationBO> {

	public List<EducationBO> getEducationByParams(Map<String, Object> params);

	public int delete(Long seqid);

	public List<Map<String, Object>> getEducationCharts(Map<String, Object> params);

	/**
	 * 报表
	 * @param type 类型
	 * @param orgCode 区域
	 * @param yearStr 年份
	 */
	public List<Map<String,Object>> findChartByEducation(Map<String,Object> param);

	public int findCountByCriteriaPriSchool(@Param("params")Map<String, Object> params);

	public List<Map<String, Object>> findPageListByCriteriaPriSchool(@Param("params")Map<String, Object> params, RowBounds bounds);

	public Map<String, Object> findPriEduById(Long seqid);

	public int updatePriEdu(@Param("params")Map<String, Object> params);

	public int deletePriEdu(Long seqid);

	public int checkPriEdu(@Param("params")Map<String, Object> map);
	
	public int insertPriEdu(@Param("params")Map<String, Object> map);


}
