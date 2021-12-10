package cn.ffcs.zhsq.map.taxi.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.map.taxi.service.ITaxiService;
import cn.ffcs.zhsq.mybatis.domain.map.taxi.CarInfo;
import cn.ffcs.zhsq.mybatis.persistence.taxi.TaxiMapper;

@Service("iTaxiService")
public class TaxiServiceImpl implements ITaxiService {

	@Autowired
	private TaxiMapper taxiMapper;
	
	@Override
	public Long batchMergeCarInfo(List<CarInfo> list) {
		return taxiMapper.batchMergeCarInfo(list);
	}

	@Override
	public EUDGPagination findTaxiPage(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 20 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		long count = taxiMapper.countCarInfoList(params);
		List<CarInfo> list = taxiMapper.searchCarInfoList(params, rowBounds);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	@Override
	public CarInfo searchCarInfoById(Long id) {
		return taxiMapper.searchCarInfoById(id);
	}

}
