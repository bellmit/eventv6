package cn.ffcs.zhsq.szzg.education.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.mybatis.domain.szzg.education.EducationBO;


public interface IEducationService {


	/**
	 * 报表
	 * @param type 类型
	 * @param orgCode 区域
	 * @param yearStr 年份
	 */
	public List<Map<String, Object>> findChartByEducation(Map<String, Object> param);

	/**
	 * 条件查询
	 * @param params
	 * @return
	 */
	public List<EducationBO> getEducationByParams(Map<String, Object> params);
	
	/**
	 * 分页查询
	 * @param params
	 * @param page
	 * @param rows
	 * @return
	 */
	public EUDGPagination findPageListByCriteria(Map<String, Object> params, int page, int rows);
	
	/**
	 * 根据id查询
	 * @param seqid
	 * @return
	 */
	public EducationBO findById(Long seqid);
	
	/**
	 * 新增
	 * @param education
	 * @return
	 */
	public Boolean save(EducationBO education);
	
	/**
	 * 更新
	 * @param education
	 * @return
	 */
	public Boolean update(EducationBO education);
	
	/**
	 * 单条数据删除
	 * @return
	 */
	public Boolean delete(Long seqid);


	/**
	 * 获取图表数据
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getEducationCharts(Map<String, Object> params);

	/**
	 * 分页查询(小学就读)
	 * @param params
	 * @return
	 */
	public EUDGPagination findPageListByCriteriaPriSchool(Map<String, Object> params, int page, int rows);

	/**
	 * 根据id查询(小学就读)
	 * @param params
	 * @return
	 */
	public Map<String, Object> findPriEduById(Long seqid);

	/**
	 * 根据条件更新(小学就读)
	 * @param params
	 * @return
	 */
	public boolean updatePriEdu(Map<String, Object> params);

	/**
	 * 根据id删除(小学就读)
	 * @param params
	 * @return
	 */
	public boolean deletePriEdu(Long seqid);
	
	/**
	 * 新增(小学就读)
	 * @param params
	 * @return
	 */
	public Long savePriEdu(List<Map<String, Object>> list,UserInfo userInfo);



}
