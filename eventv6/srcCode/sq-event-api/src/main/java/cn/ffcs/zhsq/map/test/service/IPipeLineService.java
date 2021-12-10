package cn.ffcs.zhsq.map.test.service;

import cn.ffcs.zhsq.mybatis.domain.map.test.PipeLine;

import java.util.List;
import java.util.Map;

public interface IPipeLineService {
    public List<PipeLine> findPipeLineList(Map<String, Object> params);
}