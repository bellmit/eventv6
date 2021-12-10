package cn.ffcs.zhsq.decisionMaking;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.mybatis.domain.sweepBlackRemoveEvil.eventSBREClue.EventSBREClue;
import cn.ffcs.zhsq.sweepBlackRemoveEvil.eventSBREClue.service.IEventSBREClueService;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 扫黑除恶(Sweep Black Remove Evil)线索管理(Clue) 状态变更决策类
 * 必填参数
 * 		clueId			线索id
 * 		nextNodeName	下一环节名称
 * 非必填参数
 * 		updaterId		更新操作用户id
 * @author zhangls
 *
 */
@Service(value = "eventStatusDecisionMaking4SBREClueService")
public class EventStatusDecisionMaking4SBREClueServiceImpl extends
		ApplicationObjectSupport implements
		IWorkflowDecisionMakingService<Boolean> {
	@Autowired
	private IEventSBREClueService eventSBREClueService;
	
	private final String STATUS_END = "04";		//结束
	
	@Override
	public Boolean makeDecision(Map<String, Object> params) throws Exception {
		String nextNodeName = null;
		Long clueId = -1L, updaterId = -1L;
		StringBuffer msgWrong = new StringBuffer("");
		Boolean result = false;
		
		if(CommonFunctions.isNotBlank(params, "clueId")) {
			clueId = Long.valueOf(params.get("clueId").toString());
		} else {
			msgWrong.append("缺少参数[clueId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "nextNodeName")) {
			nextNodeName = params.get("nextNodeName").toString();
		} else {
			msgWrong.append("缺少参数[nextNodeName]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "updaterId")) {
			try {
				updaterId = Long.valueOf(params.get("updaterId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		if(msgWrong.length() > 0) {
			throw new Exception(msgWrong.toString());
		} else {
			String clueStatus = null;
			
			clueStatus = capCaseStatus(nextNodeName);
			
			//更新线索状态
			if(StringUtils.isNotBlank(clueStatus) && clueId > 0) {
				EventSBREClue eventSBREClue = new EventSBREClue();
				eventSBREClue.setClueId(clueId);
				eventSBREClue.setClueStatus(clueStatus);
				eventSBREClue.setUpdaterId(updaterId);
				
				if(STATUS_END.equals(clueStatus)) {//归档时，设置办结时间
					eventSBREClue.setEndDate(new Date());
				}
				
				eventSBREClueService.updateClue(eventSBREClue, null, null);
			}
		}
		
		return result;
	}
	
	/**
	 * 获取变更后的状态
	 * @param curNodeName	当前节点名称
	 * @param nextNodeName	下一节点名称
	 * @return
	 */
	private String capCaseStatus(String nextNodeName) {
		String status = null;
		Map<String, String> statusMap = initStatusMap();
		
		status = statusMap.get(nextNodeName);
		
		return status;
	}
	
	/**
	 * 初始化状态
	 * @return
	 */
	private Map<String, String> initStatusMap() {
		String START_NODE_NAME = "start",//流程开始
			   COLLECT_NODE = "task1",	//线索采集
			   TRANSFER_NODE = "task2",	//线索扭转
			   CHECK_NODE = "task3",	//线索核查
			   FEEDBACK_NODE = "task4",	//线索反馈
			   EVA_NODE = "task5",		//评价确认
			   END_NODE = "end1",		//归档

			   STATUS_REPORTED = "00",	//已上报
			   STATUS_TRANSFER = "01",	//流转中
			   STATUS_CHECK = "02",		//待核查
			   STATUS_FEEDBACK = "03",	//已核查待处置
			   STATUS_EVA = "07";		//待评价
		
		Map<String, String> statusMap = new HashMap<String, String>();

		statusMap.put(START_NODE_NAME, STATUS_REPORTED);
		statusMap.put(COLLECT_NODE, STATUS_REPORTED);
		statusMap.put(TRANSFER_NODE, STATUS_TRANSFER);
		statusMap.put(CHECK_NODE, STATUS_CHECK);
		statusMap.put(FEEDBACK_NODE, STATUS_FEEDBACK);
		statusMap.put(EVA_NODE, STATUS_EVA);
		statusMap.put(END_NODE, STATUS_END);
		
		return statusMap;
	}
	
}
