package cn.ffcs.zhsq.intermediateData.eventVerify.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * @Description: 晋江市事件审核实现跳转中心
 * @ClassName:   EventVerify4JinJiangShiServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年7月31日 上午10:59:56
 */
@Service("eventVerify4JinJiangShiService")
public class EventVerify4JinJiangShiServiceImpl extends EventVerify4WechatServiceImpl {
	@Override
	public Map<String, Object> fetchParam4Event(Map<String, Object> eventVerify) {
		Map<String, Object> resultMap = super.fetchParam4Event(eventVerify);
		
		resultMap.put("isReport", false);
		
		return resultMap;
	}
}
