package cn.ffcs.zhsq.event.service.impl;

import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * @Description: 江西省吉安市吉州区码上办(Ji ZHou Qu Code) 工作流使用接口实现
 * @ClassName:   EventDisposalWorkflow4JiZHouQuCodeServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年11月5日 下午3:47:02
 */
@Service(value = "eventDisposalWorkflow4JiZHouQuCodeService")
public class EventDisposalWorkflow4JiZHouQuCodeServiceImpl extends EventDisposalWorkflowForJXPlatformServiceImpl {
	private static final String COMMUNITY_UNIT_ACCEPT_NODE_CODE = "task4";	//村社区综治中心受理
	
	/**
	 * 节点映射关系构建
	 * @return
	 */
	protected Map<String, String> init4NodeMappingConstruct() {
		Map<String, String> nodeMapping = super.init4NodeMappingConstruct();

		nodeMapping.remove(COMMUNITY_UNIT_ACCEPT_NODE_CODE);
		
		return nodeMapping;
	}
	
	/**
	 * 构造节点人员选择类型
	 * @return
	 * 		1 选择人员；
	 * 		2 选择组织；
	 */
	protected Map<String, String> init4TaskUserConstruct() {
		Map<String, String> initMap = super.init4TaskUserConstruct();
		
		initMap.put(COMMUNITY_UNIT_ACCEPT_NODE_CODE, "1");
		
		return initMap;
	}
}
