package cn.ffcs.zhsq.szzg.aqi.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.mybatis.domain.szzg.aqi.ZgAQI;
import cn.ffcs.zhsq.mybatis.persistence.szzg.aqi.ZgAQIMapper;
import cn.ffcs.zhsq.szzg.aqi.service.IZgAQIService;
@Service(value="zgAQIServiceImpl")
public class ZgAQIServiceImpl implements IZgAQIService{
	@Autowired
	private ZgAQIMapper zgAQIMapper;
	@Override
	public boolean insert(ZgAQI zgAQI) {
		return zgAQIMapper.insert(zgAQI)>0;
	}

	@Override
	public boolean findCountByStatioId(Map<String, Object> params) {
		return zgAQIMapper.findCountByStatioId(params)>0;
	}

	@Override
	public List<ZgAQI> findList(Map<String, Object> params) {
		return zgAQIMapper.findList(params);
	}

	@Override
	public List<Map<String, Object>> getHour(String statioId) {
		return zgAQIMapper.getHour(statioId);
	}

	@Override
	public List<Map<String, Object>> getDay(String statioId) {
		return zgAQIMapper.getDay(statioId);
	}

	@Override
	public List<Map<String, Object>> getStationListByCity(String statioId) {
		return zgAQIMapper.getStationListByCity(statioId);
	}

}
