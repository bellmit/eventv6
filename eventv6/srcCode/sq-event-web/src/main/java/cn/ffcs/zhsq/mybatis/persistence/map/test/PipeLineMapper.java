package cn.ffcs.zhsq.mybatis.persistence.map.test;

import cn.ffcs.zhsq.mybatis.domain.map.test.PipeLine;

import java.util.List;
import java.util.Map;

public interface PipeLineMapper {
    /**
     * 获取管道信息
     * @param param
     * @return
     */
    List<PipeLine> findPipeLineList(Map<String, Object> param);
}
