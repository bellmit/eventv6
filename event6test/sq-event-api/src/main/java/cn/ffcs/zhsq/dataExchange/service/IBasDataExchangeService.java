package cn.ffcs.zhsq.dataExchange.service;

import cn.ffcs.zhsq.mybatis.domain.dataExchange.BasDataExchange;
import java.util.Map;

public interface IBasDataExchangeService {


    /**
     * 新增
     * @param basDataExchange
     * @return
     */
    public int saveBasDataExchange(BasDataExchange basDataExchange);

    public BasDataExchange findBasDataExchange(Map<String, Object> params);

    public int updateBasDataExchange(BasDataExchange basDataExchange);
}
