package cn.ffcs.zhsq.eventExpand.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.shequ.eventReportRecord.domain.EventReportRecordInfo;
import cn.ffcs.shequ.eventReportRecord.service.IEventReportRecordService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.dispute.service.IDisputeMediationService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
//西藏昌都个性化事件拓展处理类
@Service(value = "eventDisposalExpandForXZCDServiceImpl")
public class EventDisposalExpandForXZCDServiceImpl extends EventDisposalExpandBaseServiceImpl {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IEventReportRecordService eventReportRecordService;
	
	@Resource(name="disputeMediationServiceImpl")
	private IDisputeMediationService disputeMediationService;
	
	private final static String bizType = "DISPUTE_MEDIATION_DF";//矛盾纠纷的中间业务关联标识

	@Override
    public Map<String, Object> delEventById(List<Long> eventIdList, UserInfo userInfo, Map<String, Object> params) {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	resultMap=super.delEventById(eventIdList, userInfo, params);
    	//昌都删除事件的时候需要关联删除相关的业务表信息
    	
    	try {
			//删除业务事件关联信息
			EventReportRecordInfo eventReportRecordInfo = new EventReportRecordInfo();
			for (int i = 0; i < eventIdList.size(); i++) {
				eventReportRecordInfo.setEventId(eventIdList.get(i));
				EventReportRecordInfo eventReportRecordInfoByEventId = null;
				eventReportRecordInfoByEventId = eventReportRecordService.findEventReportRecordInfoByEventId(eventReportRecordInfo);

				if(eventReportRecordInfoByEventId!=null&&eventReportRecordInfoByEventId.getBizId()!=null){
					
					eventReportRecordInfoByEventId.setProcStatus("2");//删除
					eventReportRecordService.editEventReportRecordInfo(eventReportRecordInfoByEventId);
					
					switch (eventReportRecordInfoByEventId.getBizType()) {
						case bizType://匹配到矛盾纠纷，则调用矛盾纠纷的删除接口
							//删除矛盾纠纷
							List<Long> bizIdList = new ArrayList<Long>();
							bizIdList.add(eventReportRecordInfoByEventId.getBizId());
							disputeMediationService.deleteByIds(bizIdList, userInfo.getUserId());
							break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

    	return resultMap;
    }

}
