package cn.ffcs.zhsq.intermediateData.eventWechat.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 江西省(JX) 微信事件审核处理类
 * @author zhangls
 *
 */
@Service("eventWechat4JXService")
@Transactional
public class EventWechat4JXServiceImpl extends EventWechatServiceImpl {
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	
	@Override
	public boolean updateEventVerifyById(Map<String, Object> eventVerify, UserInfo userInfo) throws Exception {
		boolean flag = super.updateEventVerifyById(eventVerify, userInfo);
		
		if(flag && CommonFunctions.isNotBlank(eventVerify, "eventId")) {
			Long eventVerifyId = Long.valueOf(eventVerify.get("eventVerifyId").toString());
			String STATUS_REPORTED = "02",//已上报
				   eventVerifyStatus = null;
			
			Map<String, Object> eventVerifyMap = this.findEventVerifyById(eventVerifyId, null);
			
			if(CommonFunctions.isNotBlank(eventVerifyMap, "status")) {
				eventVerifyStatus = eventVerifyMap.get("status").toString();
			}
			
			//记录为已上报时，才调整事件的所属网格
			if(STATUS_REPORTED.equals(eventVerifyStatus)) {
				String eventInfoOrgCode = null;
				MixedGridInfo gridInfo = null;
				
				if(CommonFunctions.isNotBlank(eventVerifyMap, "eventInfoOrgCode")) {
					eventInfoOrgCode = eventVerifyMap.get("eventInfoOrgCode").toString();
					
					gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(eventInfoOrgCode);
				}
				
				//事件所属地域对应的默认所属网格有效时，才进行事件的所属网格更新操作
				if(gridInfo != null) {
					Long eventId = Long.valueOf(eventVerify.get("eventId").toString());
					EventDisposal event = new EventDisposal();
					
					event.setEventId(eventId);
					event.setGridId(gridInfo.getGridId());
					event.setGridCode(gridInfo.getInfoOrgCode());
					
					eventDisposalService.updateEventDisposalById(event);
				}
			}
		}
		
		return flag;
	}
	
	@Override
	public Map<String, Object> fetchParam4Event(Map<String, Object> eventVerify) {
		Map<String, Object> resultMap = super.fetchParam4Event(eventVerify);
		
		resultMap.put("isReport", false);
		resultMap.put("isShowCloseBtn", true);
		
		return resultMap;
	}
}
