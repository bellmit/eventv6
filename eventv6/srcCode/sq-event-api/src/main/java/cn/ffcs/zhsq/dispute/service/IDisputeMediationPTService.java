package cn.ffcs.zhsq.dispute.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeMediationPT;

public interface IDisputeMediationPTService {

	List<DisputeMediationPT> findDisputeMediationPT(Map<String, Object> param);

	DisputeMediationPT getWholeInfo(Map<String, Object> param);
}
