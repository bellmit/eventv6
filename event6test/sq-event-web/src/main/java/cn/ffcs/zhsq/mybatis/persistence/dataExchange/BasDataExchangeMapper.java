package cn.ffcs.zhsq.mybatis.persistence.dataExchange;

import cn.ffcs.zhsq.mybatis.domain.dataExchange.BasDataExchange;

import java.util.Map;

public interface BasDataExchangeMapper {
    int deleteByPrimaryKey(Integer exchangeId);

    int insert(BasDataExchange record);

    int insertSelective(BasDataExchange record);

    BasDataExchange selectByPrimaryKey(Integer exchangeId);

    int updateByPrimaryKeySelective(BasDataExchange record);

    int updateByPrimaryKey(BasDataExchange record);

    BasDataExchange findBasDataExchange(Map<String, Object> params);

}