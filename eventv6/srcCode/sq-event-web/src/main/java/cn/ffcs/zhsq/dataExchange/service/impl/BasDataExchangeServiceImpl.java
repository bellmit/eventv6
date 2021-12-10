package cn.ffcs.zhsq.dataExchange.service.impl;

import cn.ffcs.zhsq.dataExchange.service.IBasDataExchangeService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.BasDataExchange;
import cn.ffcs.zhsq.mybatis.persistence.dataExchange.BasDataExchangeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service(value="basDataExchangeServiceImpl")
public class BasDataExchangeServiceImpl implements IBasDataExchangeService {

	@Autowired
	private BasDataExchangeMapper basDataExchangeMapper;

	@Override
	public int saveBasDataExchange(BasDataExchange basDataExchange) {
		int record = basDataExchangeMapper.insert(basDataExchange);
		return record;
	}

	@Override
	public BasDataExchange findBasDataExchange(Map<String, Object> params){
		return basDataExchangeMapper.findBasDataExchange(params);
	}

	@Override
	public int updateBasDataExchange(BasDataExchange basDataExchange){
		return basDataExchangeMapper.updateByPrimaryKeySelective(basDataExchange);
	}
}
