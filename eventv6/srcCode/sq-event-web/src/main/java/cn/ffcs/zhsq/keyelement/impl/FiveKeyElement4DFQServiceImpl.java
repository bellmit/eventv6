package cn.ffcs.zhsq.keyelement.impl;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.keyelement.EventNodeCode;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 大丰区(DFQ) 事件人员选择
 * @author zhangls
 *
 */
@Service(value = "fiveKeyElement4DFQService")
public class FiveKeyElement4DFQServiceImpl extends FiveKeyElement4JYSServiceImpl {
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;

	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeIdStr, Map<String, Object> params)
			throws Exception {
		Map<String, Object> resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeIdStr, params);
		EventNodeCode eventNodeCode = null;
		
		if(CommonFunctions.isNotBlank(resultMap, "eventNodeCode")) {
			eventNodeCode = (EventNodeCode) resultMap.get("eventNodeCode");
		}
		
		if(eventNodeCode != null) {
			if(eventNodeCode.isComment()) {
				eventNodeCode.setClose(true);
				eventNodeCode.setComment(false);
			}

			if(StringUtils.isNotBlank(nodeCode) && nodeCode.startsWith("__") && eventNodeCode.isPerson()) {
				int nodeId = 0;
				Long instanceId = -1L,
					 eventId = -1L;


				if(CommonFunctions.isNotBlank(params, "nodeId")) {
					try {
						nodeId = Integer.valueOf(params.get("nodeId").toString());
					} catch(NumberFormatException e) {}
				}

				if(nodeId > 0) {
					if (CommonFunctions.isNotBlank(params,"instanceId")) {
						instanceId = Long.valueOf(params.get("instanceId").toString());
					} else if (CommonFunctions.isNotBlank(params,"eventId")){
						eventId = Long.valueOf(params.get("eventId").toString());
						instanceId = Long.valueOf(eventDisposalWorkflowService.capInstanceIdByEventId(eventId));
					}

					resultMap.putAll(eventDisposalWorkflowService.initFlowInfo(null, instanceId, userInfo, null));

				}
			}
			
			resultMap.put("eventNodeCode", eventNodeCode);
		}
		
		return resultMap;
	}
}
