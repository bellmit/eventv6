package cn.ffcs.zhsq.map.test.service.impl;

import cn.ffcs.zhsq.map.test.service.IPipeLineService;
import cn.ffcs.zhsq.mybatis.domain.map.test.PipeLine;
import cn.ffcs.zhsq.mybatis.persistence.map.test.PipeLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service("pipeLineServiceImpl")
public class PipeLineServiceImpl implements IPipeLineService {
    @Autowired
    private PipeLineMapper pipeLineMapper;

    @Override
    public List<PipeLine> findPipeLineList(Map<String, Object> params) {
        List<PipeLine> pipeLines = pipeLineMapper.findPipeLineList(params);
        return pipeLines;
    }
}
