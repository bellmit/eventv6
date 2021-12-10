package cn.ffcs.zhsq.event.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 厦门(Amoy)环保(EnvPro)
 * @author zhangls
 *
 */
@Service(value = "eventDisposalWorkflow4AmoyEnvProService")
public class EventDisposalWorkflow4AmoyEnvProServiceImpl extends
		EventDisposalWorkflowForEventServiceImpl {
	//可移除的环节编码  评价，父类中已经移除 流程开始、结案
	private final String[] REMOVE_NODE_CODE = {"PJ"};
	
	@Override
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId, int sqlOrder) {
		List<Map<String, Object>> removeTasks = new ArrayList<Map<String, Object>>(),
								  workflowTaskList = super.queryProInstanceDetail(instanceId, sqlOrder);
		
		//构建可移除的环节
		for(Map<String, Object> mapTemp : workflowTaskList) {
			if(!mapTemp.containsKey("IS_CURRENT_TASK") && CommonFunctions.isNotBlank(mapTemp, "NODE_CODE")) {//当前环节不可过滤
				
				for(String nodeCode : REMOVE_NODE_CODE) {
					if(nodeCode.equals(mapTemp.get("NODE_CODE"))) {
						removeTasks.add(mapTemp);
					}
				}
			}
		}
		
		//分两步移除，是为了避免抛出java.util.ConcurrentModificationException异常
		workflowTaskList.removeAll(removeTasks);
		
		return workflowTaskList;
	}
}
