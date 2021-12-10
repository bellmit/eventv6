package cn.ffcs.zhsq.decisionMaking;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.mybatis.domain.sweepBlackRemoveEvil.eventSBREClue.EventSBREClue;
import cn.ffcs.zhsq.peopleLivelihoodInfo.service.IPeopleLivelihoodInfoService;
import cn.ffcs.zhsq.sweepBlackRemoveEvil.eventSBREClue.service.IEventSBREClueService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 民生信息 状态变更决策类（昌都）
 * 必填参数
 * 		infoId			线索id
 * 		curNodeName		当前环节名称
 * 		nextNodeName	下一环节名称
 * 非必填参数
 * 		updaterId		更新操作用户id
 * @author youwj
 *
 */
@Service(value = "eventStatusDecisionMaking4PeopleLivelihoodService")
public class EventStatusDecisionMaking4PeopleLivelihoodServiceImpl extends
		ApplicationObjectSupport implements
		IWorkflowDecisionMakingService<Boolean> {
	
	@Autowired
	private IPeopleLivelihoodInfoService peopleLivelihoodInfoService;
	
	private final String STATUS_DRAFT = "99";		//草稿
	private final String STATUS_FLOW = "00";		//待流转
	private final String STATUS_REPORT = "01";		//已上报
	private final String STATUS_END = "04";			//结束
	
	
	private static final String START_NODE_CODE = "start";		//流程启动
	private static final String COLLECT_NODE_CODE = "task1";	//采集处理环节
	
	private static final String COMMUNITY_UNIT_HANDLE_NODE_CODE = "task2";	//村社区处理
	private static final String TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE = "task3";	//乡镇街道处理
	private static final String TOWNDISPOSAl_DEPT_HANDLE_NODE_CODE = "task4";	//街道职能部门办理
	private static final String DISTRICT_UNIT_HANDLE_NODE_CODE = "task5";	//县区办理
	private static final String DISTRICT_DEPT_HANDLE_NODE_CODE = "task6";	//县区职能部门办理
	private static final String CITY_UNIT_HANDLE_NODE_CODE = "task7";	//市级办理
	private static final String CITY_DEPT_HANDLE_NODE_CODE = "task8";	//市职能部门办理

    private static final String END_NODE_CODE = "end1";			//归档环节
	
	@Override
	public Boolean makeDecision(Map<String, Object> params) throws Exception {
		String nextNodeName = null;
		String curNodeName = null;
		Long infoId = -1L, updaterId = -1L;
		StringBuffer msgWrong = new StringBuffer("");
		Boolean result = false;
		
		if(CommonFunctions.isNotBlank(params, "infoId")) {
			infoId = Long.valueOf(params.get("infoId").toString());
		} else {
			msgWrong.append("缺少参数[infoId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "nextNodeName")) {
			nextNodeName = params.get("nextNodeName").toString();
		} else {
			msgWrong.append("缺少参数[nextNodeName]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curNodeName")) {
			curNodeName = params.get("curNodeName").toString();
		} else {
			msgWrong.append("缺少参数[curNodeName]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "updator")) {
			try {
				updaterId = Long.valueOf(params.get("updator").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		if(msgWrong.length() > 0) {
			throw new Exception(msgWrong.toString());
		} else {
			String infoStatus = null;
			
			infoStatus = capCaseStatus(curNodeName,nextNodeName);
			
			//更新信息状态
			if(StringUtils.isNotBlank(infoStatus) && infoId > 0) {
				
				Map<String,Object> livelihoodInfo=new HashMap<String,Object>();
				livelihoodInfo.put("status", infoStatus);
				livelihoodInfo.put("infoSeqId", infoId);
				livelihoodInfo.put("updator", updaterId);
				
				peopleLivelihoodInfoService.updateBySeqId(livelihoodInfo);
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
	private String capCaseStatus(String curNodeCode,String nextNodeCode) {
		String eventStatus = "";
		String mapKey = curNodeCode + "_" + nextNodeCode;
		Map<String,String> statusMap=initStatusMap();
		if((START_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)) && CommonFunctions.isNotBlank(statusMap, curNodeCode)) {
			eventStatus = statusMap.get(curNodeCode).toString();
		} else if(CommonFunctions.isNotBlank(statusMap, mapKey)) {
			eventStatus = statusMap.get(mapKey).toString();
		} else if(CommonFunctions.isNotBlank(statusMap, nextNodeCode)) {
			eventStatus = statusMap.get(nextNodeCode).toString();
		}
		
		return eventStatus;
	}
	
	/**
	 * 初始化状态
	 * @return
	 */
	private Map<String, String> initStatusMap() {
		 Map<String, String> statusMap = new HashMap<String,String>();
	        
        statusMap.put(START_NODE_CODE, STATUS_FLOW);		//事件启动流程
		statusMap.put(COLLECT_NODE_CODE, STATUS_FLOW);		//事件采集
        
        //相关上报状态
        
        statusMap.put(COMMUNITY_UNIT_HANDLE_NODE_CODE + "_" + TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE, STATUS_REPORT);
        statusMap.put(TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE + "_" + DISTRICT_UNIT_HANDLE_NODE_CODE, STATUS_REPORT);
        statusMap.put(DISTRICT_UNIT_HANDLE_NODE_CODE + "_" + CITY_UNIT_HANDLE_NODE_CODE, STATUS_REPORT);
        
        statusMap.put(COMMUNITY_UNIT_HANDLE_NODE_CODE + "_" + CITY_UNIT_HANDLE_NODE_CODE, STATUS_REPORT);
        
        statusMap.put(TOWNDISPOSAl_DEPT_HANDLE_NODE_CODE + "_" + TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE, STATUS_REPORT);
        statusMap.put(DISTRICT_DEPT_HANDLE_NODE_CODE + "_" + DISTRICT_UNIT_HANDLE_NODE_CODE, STATUS_REPORT);
        statusMap.put(CITY_DEPT_HANDLE_NODE_CODE + "_" + CITY_UNIT_HANDLE_NODE_CODE, STATUS_REPORT);
        
        statusMap.put(END_NODE_CODE, ConstantValue.EVENT_STATUS_END);
        
        return statusMap;
	}
	
}
