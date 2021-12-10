package cn.ffcs.zhsq.mybatis.persistence.szzg.hospital;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.szzg.hospital.HospitalBO;

import java.util.List;
import java.util.Map;



public interface HospitalMapper extends MyBatisBaseMapper<HospitalBO> {
	
	public List<HospitalBO> findHospitalMark(Map<String, Object> params);

	public int del(Long seqid);
	
	public List<HospitalBO> findHospitalPoint(Map<String, Object> params);
	
	public List<Map<String, Object>> getHospitalCharts(Map<String, Object> params);

}
