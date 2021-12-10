package cn.ffcs.zhsq.szzg.hospital.service.impl;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.szzg.hospital.HospitalBO;
import cn.ffcs.zhsq.mybatis.persistence.szzg.hospital.HospitalMapper;
import cn.ffcs.zhsq.szzg.hospital.service.HospitalService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;


@Service(value="hospitalService")
public class HospitalServiceImpl implements HospitalService {
	
	@Autowired
	private HospitalMapper hospitalMapper;
	


	@Override
	public List<HospitalBO> findHospitalMark(Map<String, Object> params) {
		return hospitalMapper.findHospitalMark(params);
	}

	@Override
	public EUDGPagination findPageListByCriteria(Map<String, Object> params,
			Integer page, Integer rows) {
		RowBounds bounds = new RowBounds((page - 1) * rows, rows);
		int count = hospitalMapper.findCountByCriteria(params);
		List<HospitalBO> list = hospitalMapper.findPageListByCriteria(params, bounds);
//		this.formatterList(list);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}
	
//	private void formatterList(List<HospitalBO> list){
//		if(list.size() <= 0) return;
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("codetype", "HOSPITAL_TYPE");
//		List<DictionaryBO> types = szzgDictionaryMapper.findDicAllType(params);
//		params.put("codetype", "HOSPITAL_NATURE_TYPE");
//		List<DictionaryBO> nature = szzgDictionaryMapper.findDicAllType(params);
//		for(HospitalBO h : list){
//			if(!StringUtil.isEmpty(h.getType())){
//				for(DictionaryBO t : types){
//					if(h.getType().equals(t.getItemvalue())){
//						h.setTypeName(t.getItemname());
//						break;
//					}
//				}
//			}
//
//			if(!StringUtil.isEmpty(h.getHospitalNature())){
//				for(DictionaryBO n : nature){
//					if(h.getHospitalNature().equals(n.getItemvalue())){
//						h.setHospitalNatureName(n.getItemname());
//						break;
//					}
//				}
//			}
//		}
//	}

	@Override
	public HospitalBO findHospitalById(Long id) {
		HospitalBO hospital = hospitalMapper.findById(id);
//		this.formatterData(hospital);
		return hospital;
	}
	
//	private void formatterData(HospitalBO hospital){
//		Map<String, Object> params = new HashMap<String, Object>();
//		if(hospital.getType()!=null){
//			params.put("codetype", "HOSPITAL_TYPE");
//			params.put("itemvalue", hospital.getType());
//			List<DictionaryBO> types = szzgDictionaryMapper.findDicAllType(params);
//			if(types.size()>0){
//				hospital.setTypeName(types.get(0).getItemname());
//			}
//		}
//
//		if(!StringUtil.isEmpty(hospital.getHospitalNature())){
//			params.put("codetype", "HOSPITAL_NATURE_TYPE");
//			params.put("itemvalue", hospital.getHospitalNature());
//			List<DictionaryBO> types = szzgDictionaryMapper.findDicAllType(params);
//			if(types.size()>0){
//				hospital.setHospitalNatureName(types.get(0).getItemname());
//			}
//		}
//	}

	@Override
	public Boolean save(HospitalBO hospital) {
		int count= hospitalMapper.update(hospital);
		return count>0;
	}

	@Override
	public Boolean delete(Long seqid) {
		int count =hospitalMapper.del(seqid);
		return count>0;
	}

	@Override
	public Boolean insert(HospitalBO hospital) {
		int count=hospitalMapper.insert(hospital);
		return count>0;
	}

	@Override
	public List<HospitalBO> findHospitalPoint(Map<String, Object> params) {
		
		return hospitalMapper.findHospitalPoint(params);
	}

	@Override
	public List<Map<String, Object>> getHospitalCharts(Map<String, Object> params) {
		
		return hospitalMapper.getHospitalCharts(params);
	}

}
