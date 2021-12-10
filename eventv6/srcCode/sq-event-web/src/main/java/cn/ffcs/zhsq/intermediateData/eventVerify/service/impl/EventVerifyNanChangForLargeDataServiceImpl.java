package cn.ffcs.zhsq.intermediateData.eventVerify.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.intermediateData.eventWechat.service.impl.EventWechat4JXServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 非微信、非第三方对接 事件审核基础类
 * @author youwj
 *
 */
@Service("eventVerifyNanChangForLargeDataService")
@Transactional
public class EventVerifyNanChangForLargeDataServiceImpl extends EventVerifyServiceImpl {

	@Override
	public Map<String, Object> fetchParam4Event(Map<String, Object> eventVerify) {
		Map<String, Object> resultMap = null;
		
		resultMap=super.fetchParam4Event(eventVerify);
		
		resultMap.put("isShowInvalid", true);	
		
		return resultMap;
	}


}