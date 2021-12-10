package cn.ffcs.zhsq.keyelement.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 大联动(JointCooperation) 五要素实现类
 * @author zhangls
 *
 */
@Service(value = "fiveKeyElement4JointCooperationService")
public class FiveKeyElement4JointCooperationServiceImpl extends
		FiveKeyElement4GLGridServiceImpl {
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	private static final String COMMUNITY_NODE_CODE_COLLECT = "task2";				//社区处理
	private static final String STREET_CENTER_NODE_CODE_COLLECT = "task3";			//街道指挥中心处理
	private static final String STREET_DEPARTMENT_NODE_CODE_COLLECT = "task4";		//街道处置单位处理
	private static final String DISTRICT_CENTER_NODE_CODE_COLLECT = "task5";		//县区指挥中心处理
	private static final String DISTRICT_DEPARTMENT_NODE_CODE_COLLECT = "task6";	//区处置单位处理
	
	private static final String STREET_CENTER_NODE_CODE = "task7";					//街道指挥中心处理
	private static final String DISTRICT_CENTER_NODE_CODE = "task8";				//县区指挥中心处理
	
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if((COMMUNITY_NODE_CODE_COLLECT.equals(curnodeName) 
				|| STREET_DEPARTMENT_NODE_CODE_COLLECT.equals(curnodeName) || STREET_CENTER_NODE_CODE_COLLECT.equals(curnodeName) 
				|| DISTRICT_CENTER_NODE_CODE_COLLECT.equals(curnodeName) || DISTRICT_DEPARTMENT_NODE_CODE_COLLECT.equals(curnodeName)) && 
		(STREET_CENTER_NODE_CODE.equals(nodeName) || DISTRICT_CENTER_NODE_CODE.equals(nodeName))) {
			nodeCode = alterNodeCode(curnodeName, nodeName, params);
		}
		
		resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		
		return resultMap;
	}
	
	/**
	 * 变更环节编码
	 * @param curNodeName	流程实例
	 * @param nextNodeName	当前环节
	 * @param params
	 * 			eventId		事件id
	 * @return
	 * @throws Exception
	 */
	private String alterNodeCode(String curNodeName, String nextNodeName, Map<String, Object> params) throws Exception {
		Long instanceId = -1L;
		Node curNode = null,
			 nextNode = null;
		String nodeCode = "";
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			instanceId = Long.valueOf(params.get("instanceId").toString());
		} else if(CommonFunctions.isNotBlank(params, "eventId")) {
			Long eventId = Long.valueOf(params.get("eventId").toString());
			
			if(eventId != null && eventId > 0) {
				instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
			}
		}
		
		if(instanceId > 0) { 
			curNode = eventDisposalWorkflowService.findNodeById(instanceId, curNodeName);
			nextNode = eventDisposalWorkflowService.findNodeById(instanceId, nextNodeName);
			
			if(curNode != null && nextNode != null) {
				String nodeCodeFrom = curNode.getNodeCode(),
					   nodeCodeTo = nextNode.getNodeCode(),
					   operateType = "";
				int fromLevel = 0, toLevel = 0;
				
				operateType = capOperateType(curNodeName, nextNodeName);
				
				if(StringUtils.isNotBlank(nodeCodeFrom) && nodeCodeFrom.length() > 1) {
					String fromLevelStr = nodeCodeFrom.substring(1, 2);
					
					try {
						fromLevel = Integer.valueOf(fromLevelStr);
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
				}
				
				if(StringUtils.isNotBlank(nodeCodeTo) && nodeCodeTo.length() > 1) {
					String toLevelStr = nodeCodeTo.substring(1, 2);
					
					try {
						toLevel = Integer.valueOf(toLevelStr);
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
				}
				
				//人工构建nodeCode
				nodeCode = nodeCodeFrom + operateType + (Math.abs(fromLevel - toLevel)) + nodeCodeTo;
			}
		}
		
		return nodeCode;
	}
	
	/**
	 * 获取操作类型
	 * @param curNodeCode	当前环节名称
	 * @param nextNodeCode	下一环节名称
	 * @return
	 */
	private String capOperateType(String curNodeCode, String nextNodeCode) {
		Map<String, String> operateTypeMap = new HashMap<String, String>();
		String operateType = INodeCodeHandler.OPERATE_REPORT;//默认上报
		
		//上报
		operateTypeMap.put(STREET_DEPARTMENT_NODE_CODE_COLLECT + "-" + DISTRICT_CENTER_NODE_CODE , INodeCodeHandler.OPERATE_REPORT);
		
		//分流
		operateTypeMap.put(STREET_DEPARTMENT_NODE_CODE_COLLECT, INodeCodeHandler.OPERATE_FLOW);
		operateTypeMap.put(DISTRICT_CENTER_NODE_CODE, INodeCodeHandler.OPERATE_FLOW);
		
		if(operateTypeMap.containsKey(curNodeCode + "-" + nextNodeCode)) {
			operateType = operateTypeMap.get(curNodeCode + "-" + nextNodeCode);
		} else if(operateTypeMap.containsKey(curNodeCode)) {
			operateType = operateTypeMap.get(curNodeCode);
		}
		
		return operateType;
	}
}
