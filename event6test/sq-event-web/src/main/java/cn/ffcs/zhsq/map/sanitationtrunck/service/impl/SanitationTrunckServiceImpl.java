package cn.ffcs.zhsq.map.sanitationtrunck.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.map.sanitationtrunck.SanitationTrunckService;
import cn.ffcs.zhsq.mybatis.domain.map.sanitationtrunck.SanitationTrunck;
import cn.ffcs.zhsq.mybatis.persistence.sanitationtrunck.SanitationTrunckMapper;

@Transactional
@Service("sanitationTrunckService")
public class SanitationTrunckServiceImpl implements SanitationTrunckService {

	@Autowired
	private SanitationTrunckMapper sanitationTrunckMapper;
	
	@Override
	public void insert(SanitationTrunck bo) {
		sanitationTrunckMapper.insert(bo);
	}

	@Override
	public void batchSave(List<SanitationTrunck> list) {
		sanitationTrunckMapper.batchSave(list);
	}

	@Override
	public boolean update(SanitationTrunck bo) {
		return sanitationTrunckMapper.update(bo) > 0;
	}

	@Override
	public boolean delete(SanitationTrunck bo) {
		return sanitationTrunckMapper.delete(bo) > 0;
	}

	@Override
	public EUDGPagination searchList(int page, int rows,
			Map<String, Object> params) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		long count = sanitationTrunckMapper.countList(params);
		List<SanitationTrunck> list = sanitationTrunckMapper.searchList(params, rowBounds);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	@Override
	public SanitationTrunck searchById(String id) {
		return sanitationTrunckMapper.searchById(id);
	}

	@Transactional
	@Override
	public void sysSanitatinTrunckInfo(List<SanitationTrunck> list) {
		sanitationTrunckMapper.delAll();
		
		Map<String,Integer> res = getLenAndDiv(list.size());
		int len = res.get("len");
		int div = res.get("div");
		
		for(int i=0;i<len;i++){
			sanitationTrunckMapper.batchSave(list.subList(i*100, (i != len-1)?((i*100+100)):(i*100+div)));
		}
	}
	
	private Map<String,Integer> getLenAndDiv(int size){
		Map<String,Integer> res = new HashMap<String,Integer>();
		int len = size/100;
		int div = size%100;//余数
		if(len == 0 && div == 0)
			div = size;
		if(div != 0){
			len = len+1;
		}
		res.put("len", len);
		res.put("div", div);
		return res;
	}

}
