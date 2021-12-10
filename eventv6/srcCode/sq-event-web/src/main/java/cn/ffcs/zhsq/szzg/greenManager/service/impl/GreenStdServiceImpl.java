package cn.ffcs.zhsq.szzg.greenManager.service.impl;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.szzg.greenManager.GreenStdBO;
import cn.ffcs.zhsq.mybatis.persistence.szzg.greenManager.GreenStdMapper;
import cn.ffcs.zhsq.szzg.greenManager.service.GreenStdService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;


@Service(value="greenStdService")
public class GreenStdServiceImpl implements GreenStdService {
	
	@Autowired
	private GreenStdMapper greenStdMapper;
	
//	@Autowired
//	private SzzgTreeService szzgTreeService;

	@Override
	public EUDGPagination findPageListByCriteria(Map<String, Object> params,
			int page, int rows) {
		RowBounds bounds = new RowBounds((page - 1) * rows, rows);
		int count = greenStdMapper.findCountByCriteria(params);
		List<GreenStdBO> list = greenStdMapper.findPageListByCriteria(params, bounds);
	//	this.formatter(list);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}
	
//	private void formatter(List<GreenStdBO> list){
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("codetype", "GREENSTDTYPE");
//		List<DictionaryBO> dic = szzgTreeService.findDicAllType(params);
//		for(GreenStdBO greenStd:list){
//			if(!StringUtil.isEmpty(greenStd.getType())){
//				for(DictionaryBO d:dic){
//					if(greenStd.getType().equals(d.getItemvalue())){
//						greenStd.setTypeName(d.getItemname());
//						break;
//					}
//				}
//			}
//		}
//	}

	@Override
	public GreenStdBO findGreenStdById(Long seqid) {
		
		return greenStdMapper.findById(seqid);
	}

	@Override
	public Boolean update(GreenStdBO greenStd) {
		int count= greenStdMapper.update(greenStd);
		return count>0;
	}

	@Override
	public Boolean del(Long seqid) {
		int count= greenStdMapper.del(seqid);
		return count>0;
	}

	@Override
	public Boolean insert(GreenStdBO greenStd) {
		int count=greenStdMapper.insert(greenStd);
		return count>0;
	}

	@Override
	public List<GreenStdBO> findGreenStdByParams(Map<String, Object> params) {
		return greenStdMapper.findGreenStdByParams(params);
	}

}
