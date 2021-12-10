package cn.ffcs.zhsq.szzg.school.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.school.SchoolBo;
import cn.ffcs.zhsq.mybatis.persistence.szzg.school.SchoolMapper;
import cn.ffcs.zhsq.szzg.school.service.SchoolService;


@Service(value="schoolService")
public class SchoolServiceImpl implements SchoolService {

	@Autowired
	private SchoolMapper schoolMapper;


	@Override
	public List<SchoolBo> findSchoolMark(Map<String, Object> params) {
		return schoolMapper.findSchoolMark(params);
	}

	@Override
	public EUDGPagination findPageListByCriteria(Map<String, Object> params,
												 Integer page, Integer rows) {
		RowBounds bounds = new RowBounds((page - 1) * rows, rows);
		List<SchoolBo> list = null;
		int count = schoolMapper.findCountByCriteria(params);
		if(count > 0) {
			list = schoolMapper.findPageListByCriteria(params, bounds);
		}else {
			list = new ArrayList<>();
		}
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
	public SchoolBo findSchoolById(Long id) {
		SchoolBo schoolBo = schoolMapper.findById(id);
//		this.formatterData(hospital);
		return schoolBo;
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
	public Boolean save(SchoolBo schoolBo) {
		int count = schoolMapper.isExit(schoolBo);
		if(count > 0) {
			return false;
		}
		count = schoolMapper.update(schoolBo);
		return count > 0;
	}

	@Override
	public Boolean delete(Long seqid) {
		int count = schoolMapper.del(seqid);
		return count > 0;
	}

	@Override
	public Boolean insert(SchoolBo schoolBo) {
		int count = schoolMapper.isExit(schoolBo);
		if(count > 0) {
			return false;
		}
		count = schoolMapper.insert(schoolBo);
		return count > 0;
	}

	@Override
	public List<SchoolBo> findSchoolPoint(Map<String, Object> params) {

		return schoolMapper.findSchoolPoint(params);
	}

	@Override
	public Map<String, Object> getSchoolCharts(Map<String, Object> params) {

		Map<String,Object> map=new HashMap<String,Object>();
		map.put("school", schoolMapper.getSchoolChartsSchool(params));
		map.put("malesFemales", schoolMapper.getSchoolChartsMalesFemales(params));
		map.put("count", schoolMapper.findSchoolCount1(params));
		map.put("count2",schoolMapper.findSchoolCount2(params));
		return map;
	}



	@Override
	public List<SchoolBo> findTreeTable(Map<String, Object> param)
	{
		return schoolMapper.findTreeTable(param);
	}

}
