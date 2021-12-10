package cn.ffcs.zhsq.szzg.greenManager.service.impl;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.szzg.greenManager.GreenBeltBO;
import cn.ffcs.zhsq.mybatis.persistence.szzg.greenManager.GreenBeltMapper;
import cn.ffcs.zhsq.szzg.greenManager.service.GreenBeltService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;


@Service(value = "greenBeltService")
public class GreenBeltServiceImpl implements GreenBeltService {
	@Autowired
	private GreenBeltMapper greenMapper;




	@Override
	public List<GreenBeltBO> findGreenByParams(Map<String, Object> params) {
		
		return greenMapper.findGreenByParams(params);
	}


	@Override
	public EUDGPagination findPageListByCriteria(Map<String, Object> params,
			int page, int rows) {
		RowBounds bounds = new RowBounds((page - 1) * rows, rows);
		int count  = greenMapper.findCountByCriteria(params);
		List<GreenBeltBO> list = greenMapper.findPageListByCriteria(params, bounds);
	//	this.formatterData(list);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}
	
//	private void formatterData(List<GreenBeltBO> list){
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("codetype", "greenType");
//		List<DictionaryBO> gType = szzgTreeService.findDicAllType(map);
//		map.put("codetype", "stdType");
//		List<DictionaryBO> stdType = szzgTreeService.findDicAllType(map);
//
//		for(GreenBeltBO greenBelt:list){
//			if(!StringUtil.isEmpty(greenBelt.getgType())){
//				for(DictionaryBO dic:gType){
//					if(greenBelt.getgType().equals(dic.getItemvalue())){
//						greenBelt.setgTypeName(dic.getItemname());
//						break;
//					}
//				}
//			}
//
//			if(!StringUtil.isEmpty(greenBelt.getStd())){
//				for(DictionaryBO dic: stdType){
//					if(greenBelt.getStd().equals(dic.getItemvalue())){
//						greenBelt.setStdName(dic.getItemname());
//						break;
//					}
//				}
//			}
//		}
//	}

	@Override
	public GreenBeltBO findGreenBeltById(Long seqid) {
		return greenMapper.findById(seqid);
	}

	@Override
	public Boolean update(GreenBeltBO greenBelt) {
		int count=greenMapper.update(greenBelt);
		return count>0;
	}

	@Override
	public Boolean del(Long seqid) {
		int count=greenMapper.del(seqid);
		return  count>0;
	}

	@Override
	public Boolean insert(GreenBeltBO greenBelt) {
		int count=greenMapper.insert(greenBelt);

		return  count>0;
	}

}
