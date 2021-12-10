package cn.ffcs.zhsq.mybatis.persistence.dataExchange;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchange;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;

public interface DataExchangeMapper extends MyBatisBaseMapper<DataExchange>{
	public int deleteByPrimaryKey(Integer interId);

	public int insert(DataExchange record);

	public int insertSelective(DataExchange record);

	public DataExchange selectByPrimaryKey(Integer interId);

	public int updateByPrimaryKeySelective(DataExchange record);

	public int updateByPrimaryKey(DataExchange record);
    
	public List<DataExchangeStatus> findByDataExchang(Map<String, Object> params);
}