package cn.ffcs.zhsq.intermediateData.eventVerify.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 延平区事件审核
 * @ClassName:   EventVerify4YPQServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年5月31日 上午11:06:25
 */
@Service("eventVerify4YPQService")
@Transactional
public class EventVerify4YPQServiceImpl extends EventVerify4WechatServiceImpl {
	@Override
	public Map<String, Object> fetchParam4Event(Map<String, Object> eventVerify) {
		Map<String, Object> resultMap = super.fetchParam4Event(eventVerify);
		
		resultMap.put("isReport", false);
		
		return resultMap;
	}
}
