package cn.ffcs.zhsq.mybatis.persistence.dispute;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeMediationPT;

public interface DisputeMediationPTMapper extends MyBatisBaseMapper<DisputeMediationPT> {
    int insert(DisputeMediationPT record);

    int insertSelective(DisputeMediationPT record);
    
    List<DisputeMediationPT> findDisputeMediationPT(Map<String, Object> param);
}