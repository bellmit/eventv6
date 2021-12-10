package cn.ffcs.zhsq.szzg.hospital.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.szzg.hospital.HospitalBO;
import cn.ffcs.system.publicUtil.EUDGPagination;


public interface HospitalService {
	
	/**
	 * 查询医院标注
	 * @param params
	 * @return
	 */
	public List<HospitalBO> findHospitalMark(Map<String, Object> params);
	
	/**
	 * 分页查询
	 * @param params
	 * @param page
	 * @param rows
	 * @return
	 */
	public EUDGPagination findPageListByCriteria(Map<String, Object> params, Integer page, Integer rows);
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public HospitalBO findHospitalById(Long id);
	
	/**
	 * 更新
	 * @param hospital
	 * @return
	 */
	public Boolean save(HospitalBO hospital);
	
	/**
	 * 批量删除
	 * @param seqid
	 * @return
	 */
	public Boolean delete(Long seqid);
	
	/**
	 * 新增
	 * @param hospital
	 * @return
	 */
	public Boolean insert(HospitalBO hospital);
	
	/**
	 * 获取标注
	 * @param params
	 * @return
	 */
	public List<HospitalBO> findHospitalPoint(Map<String, Object> params);
	
	/**
	 * 获取柱状图数据
	 * @return
	 */
	public List<Map<String, Object>> getHospitalCharts(Map<String, Object> params);

}
