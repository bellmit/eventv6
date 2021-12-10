package cn.ffcs.zhsq.intermediateData.eventVerify.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.intermediateData.eventWechat.service.impl.EventWechat4JXServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 非微信、非第三方对接 事件审核基础类
 * @author zhangls
 *
 */
@Service("eventVerifyService")
@Transactional
public class EventVerifyServiceImpl extends EventWechat4JXServiceImpl {

	@Override
	public boolean updateEventVerifyById(Map<String, Object> eventVerify, UserInfo userInfo) throws Exception {
		boolean flag = false;
		
		//不回调手机端接口
		if(CommonFunctions.isNotBlank(eventVerify, "operateType")) {
			eventVerify.remove("operateType");
		}
		
		flag = super.updateEventVerifyById(eventVerify, userInfo);

		return flag;
	}
	
	@Override
	public Map<String, Object> fetchParam4Event(Map<String, Object> eventVerify) {
		Map<String, Object> resultMap = super.fetchParam4Event(eventVerify);
		
		//不是以第三方方式进行对接的，故不生成第三方关联记录(REPORT_EVENT_RECORD)
		resultMap.remove("moduleCode");
		
		return resultMap;
	}

}