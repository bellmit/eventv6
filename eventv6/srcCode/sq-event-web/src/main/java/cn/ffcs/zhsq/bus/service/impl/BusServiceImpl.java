package cn.ffcs.zhsq.bus.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.bus.service.BusService;
import cn.ffcs.zhsq.mybatis.domain.map.bus.Busline;
import cn.ffcs.zhsq.mybatis.domain.map.bus.CarBusline;
import cn.ffcs.zhsq.mybatis.domain.map.bus.CarLastGps;
import cn.ffcs.zhsq.mybatis.domain.map.bus.Station;
import cn.ffcs.zhsq.mybatis.domain.map.bus.StationBusline;
import cn.ffcs.zhsq.mybatis.persistence.bus.BusMapper;

@Service("busService")
public class BusServiceImpl implements BusService{

	@Autowired
	private BusMapper busMapper;
	
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
	
	@Override
	public Long batchMergeBusline(List<Busline> list) {
		busMapper.deleteAllBusline();
		Map<String,Integer> res = getLenAndDiv(list.size());
		int len = res.get("len");
		int div = res.get("div");
		for(int i=0;i<len;i++){
			busMapper.batchMergeBusline(list.subList(i*100, (i != len-1)?((i*100+100)):(i*100+div)));
//			if(i != len-1)
//				busMapper.batchMergeBusline(list.subList(i*100, (i+1)*100));
//			else
//				busMapper.batchMergeBusline(list.subList(i*100, i*100+div));
		}
		return 1L;
	}

	@Override
	public Long batchMergeCarBusline(List<CarBusline> list) {
		busMapper.deleteAllCarBusline();
		Map<String,Integer> res = getLenAndDiv(list.size());
		int len = res.get("len");
		int div = res.get("div");
		for(int i=0;i<len;i++){
			busMapper.batchMergeCarBusline(list.subList(i*100, (i != len-1)?((i*100+100)):(i*100+div)));
		}
		return 1L;
	}

	@Override
	public Long batchMergeCarLastGps(List<CarLastGps> list) {
		busMapper.deleteAllCarLastGps();
		Map<String,Integer> res = getLenAndDiv(list.size());
		int len = res.get("len");
		int div = res.get("div");
		for(int i=0;i<len;i++){
			busMapper.batchMergeCarLastGps(list.subList(i*100, (i != len-1)?((i*100+100)):(i*100+div)));
		}
		return 1L;
	}

	@Override
	public Long batchMergeStation(List<Station> list) {
		busMapper.deleteAllStation();
		Map<String,Integer> res = getLenAndDiv(list.size());
		int len = res.get("len");
		int div = res.get("div");
		for(int i=0;i<len;i++){
			busMapper.batchMergeStation(list.subList(i*100, (i != len-1)?((i*100+100)):(i*100+div)));
		}
		return 1L;
	}

	@Override
	public Long batchMergeStationBusline(List<StationBusline> list) {
		busMapper.deleteAllStationBusline();
		Map<String,Integer> res = getLenAndDiv(list.size());
		int len = res.get("len");
		int div = res.get("div");
		for(int i=0;i<len;i++){
			busMapper.batchMergeStationBusline(list.subList(i*100, (i != len-1)?((i*100+100)):(i*100+div)));
		}
		return 1L;
	}

	@Override
	public List<Busline> searchBuslineList(Map<String, Object> params) {
		return busMapper.searchBuslineList(params);
	}
	
	@Override
	public EUDGPagination findBuslinePage(int pageNo, int pageSize,Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 20 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		long count = busMapper.countBuslineList(params);
		List<Busline> list = busMapper.searchBuslineList(params, rowBounds);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	@Override
	public Busline searchBuslineById(Long id) {
		return busMapper.searchBuslineById(id);
	}

	@Override
	public List<CarBusline> searchCarBuslineList(Map<String, Object> params) {
		return busMapper.searchCarBuslineList(params);
	}

	@Override
	public CarBusline searchCarBuslineById(Long id) {
		return null;
	}

	@Override
	public List<CarLastGps> searchCarLastGpsList(Map<String, Object> params,
			RowBounds rowBounds) {
		return null;
	}

	@Override
	public CarLastGps searchCarLastGpsById(Long id) {
		return null;
	}

	@Override
	public List<Station> searchStationList(Map<String, Object> params,
			RowBounds rowBounds) {
		return null;
	}

	@Override
	public List<Station> searchStationListByLineInfo(Map<String, Object> params){
		return busMapper.searchStationListByLineInfo(params);
	}
	
	@Override
	public Station searchStationById(Long id) {
		return null;
	}

	@Override
	public List<StationBusline> searchStationBuslineList(
			Map<String, Object> params, RowBounds rowBounds) {
		return null;
	}

	@Override
	public StationBusline searchStationBuslineById(Long id) {
		return null;
	}

}
