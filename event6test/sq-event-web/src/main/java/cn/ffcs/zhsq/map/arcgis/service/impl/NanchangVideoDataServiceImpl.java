package cn.ffcs.zhsq.map.arcgis.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.mybatis.domain.zzgl.globalEyes.MonitorBO;
import cn.ffcs.zhsq.map.arcgis.service.INanchangVideoDataService;
import cn.ffcs.zhsq.mybatis.persistence.map.arcgis.NanchangVideoDataMapper;

@Service(value = "nanchangVideoDataService")
public class NanchangVideoDataServiceImpl implements INanchangVideoDataService {
	
	@Autowired
	private NanchangVideoDataMapper nanchangVideoDataMapper;

	public EUDGPagination listCams( Integer pageNo, Integer pageSize,
			Map<String, Object> params) {
		
		EUDGPagination pagination = new EUDGPagination();
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		Integer count =nanchangVideoDataMapper.countCamlistByParams(params);
		if(count>0) {
			List<MonitorBO> list =nanchangVideoDataMapper.findCamlistByParams(params,rowBounds);
			 pagination = new EUDGPagination(count, list);
		}
		
		return pagination;
	}

	@Override
	public List<Map<String, Object>> findCamPointlistByParams(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return nanchangVideoDataMapper.findCamPointlistByParams(params);
	}


	
}
