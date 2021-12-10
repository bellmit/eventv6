package cn.ffcs.zhsq.handlerCfg.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.Workflow;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.handlerCfg.service.IEventHandlerWfActorCfgService;
import cn.ffcs.zhsq.handlerCfg.service.IEventHandlerWfCfgService;
import cn.ffcs.zhsq.handlerCfg.service.IEventHandlerWfProcCfgService;
import cn.ffcs.zhsq.mybatis.domain.handlerCfg.EventHandlerWfCfgPO;
import cn.ffcs.zhsq.mybatis.domain.handlerCfg.EventHandlerWfProcCfgPO;
import cn.ffcs.zhsq.mybatis.persistence.handlerCfg.EventHandlerWfProcCfgMapper;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 流程环节配置
 * T_BIZ_WF_PROC_CFG
 * 
 * @author zhangls
 *
 */
@Service(value="eventHandlerWfProcCfgService")
public class EventHandlerWfProcCfgServiceImpl implements
		IEventHandlerWfProcCfgService {

	@Autowired
	private EventHandlerWfProcCfgMapper eventHandlerWfProcCfgMapper;
	
	@Autowired
	private IEventHandlerWfCfgService eventHandlerWfCfgService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventHandlerWfActorCfgService eventHandlerWfActorCfgService;
	
	@Autowired
	private IBaseDictionaryService dictionaryService;
	
	@Override
	public Long saveHandlerWfProcCfg(
			EventHandlerWfProcCfgPO eventHandlerWfProcCfgPO) throws Exception {
		Long wfpcId = -1L;
		
		if(eventHandlerWfProcCfgPO != null) {
			int result = 0;
			
			if(isRecordValid(eventHandlerWfProcCfgPO) && isRecordNotDuplicated(eventHandlerWfProcCfgPO)) {
				Map<String, Object> params = new HashMap<String, Object>();
				
				params.put("regionCode", eventHandlerWfProcCfgPO.getRegionCode());
				params.put("taskCode", eventHandlerWfProcCfgPO.getTaskCode());
				params.put("wfcId", eventHandlerWfProcCfgPO.getWfcId());
				
				List<EventHandlerWfProcCfgPO> wfProcCfgList = null;//this.findHandlerWfProcCfgList(params);
				
				if(wfProcCfgList != null && wfProcCfgList.size() > 0) {//用于合并事件类别
					EventHandlerWfProcCfgPO wfProcCfg = wfProcCfgList.get(0);
					String eventCodes = wfProcCfg.getEventCodes(),
						   eventCodesBak = eventHandlerWfProcCfgPO.getEventCodes();
					
					if(StringUtils.isNotBlank(eventCodesBak)) {
						eventCodes += "," + eventCodesBak;
						
						String[] eventCodeArray = eventCodes.split(",");
						Set<String> eventCodeSet = new HashSet<String>();
						StringBuffer eventCodeBuffer = new StringBuffer("");
						
						for(String eventCode : eventCodeArray) {
							if(StringUtils.isNotBlank(eventCode)) {
								eventCodeSet.add(eventCode);
							}
						}
						
						if(!eventCodeSet.isEmpty()) {
							for(String key : eventCodeSet) {
								eventCodeBuffer.append(",").append(key);
							}
						}
						
						if(eventCodeBuffer.length() > 0) {
							eventCodes = eventCodeBuffer.substring(1);
						}
					}
					
					eventHandlerWfProcCfgPO.setWfpcId(wfProcCfg.getWfpcId());
					eventHandlerWfProcCfgPO.setEventCodes(eventCodes);
					
					result = eventHandlerWfProcCfgMapper.update(eventHandlerWfProcCfgPO);
				} else {
					result = eventHandlerWfProcCfgMapper.insert(eventHandlerWfProcCfgPO);
				}
			}
			
			if(result > 0) {
				wfpcId = eventHandlerWfProcCfgPO.getWfpcId();
			}
		}
		
		return wfpcId;
	}

	@Override
	public boolean updateHandlerWfProcCfgById(
			EventHandlerWfProcCfgPO eventHandlerWfProcCfgPO) throws Exception {
		boolean flag = false;
		
		if(eventHandlerWfProcCfgPO != null) {
			Long wfpcId = eventHandlerWfProcCfgPO.getWfpcId();
			
			if(wfpcId != null && wfpcId > 0) {
				int result = 0;
				
				if(isRecordNotDuplicated(eventHandlerWfProcCfgPO)) {
					result = eventHandlerWfProcCfgMapper.update(eventHandlerWfProcCfgPO);
				}
				
				flag = result > 0;
			}
		}
		
		return flag;
	}

	@Override
	public boolean delHandlerWfProcCfgById(Long wfpcId) {
		boolean flag = false;
		
		if(wfpcId != null && wfpcId > 0) {
			flag = eventHandlerWfProcCfgMapper.delete(wfpcId) > 0;
			
			if(flag) {//环节配置删除成功时，同步删除相关的环节人员配置信息
				eventHandlerWfActorCfgService.delHandlerWfActorCfgByWfpcId(wfpcId);
			}
		}
		
		return flag;
	}
	
	@Override
	public int delHandlerWfProcCfgByWfcId(Long wfcId) {
		int result = 0;
		
		if(wfcId != null && wfcId > 0) {
			result = eventHandlerWfActorCfgService.delHandlerWfActorCfgByWfcId(wfcId);
			
			if(result > 0) {
				result = eventHandlerWfProcCfgMapper.deleteByWfcId(wfcId);
			}
		}
		
		return result;
	}

	@Override
	public EventHandlerWfProcCfgPO findHandlerWfProcCfgById(Long wfpcId) {
		EventHandlerWfProcCfgPO wfProcCfg = null;
		
		if(wfpcId != null && wfpcId > 0) {
			wfProcCfg = eventHandlerWfProcCfgMapper.findById(wfpcId);
			formatDataOut(wfProcCfg);
		}
		
		return wfProcCfg;
	}
	
	@Override
	public EUDGPagination findHandlerWfProcCfgPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		
		formatDataIn(params);
		
		int count = eventHandlerWfProcCfgMapper.findCountByCriteria(params);
		List<EventHandlerWfProcCfgPO> wfProcCfgList = eventHandlerWfProcCfgMapper.findPageListByCriteria(params, rowBounds);
		
		formatDataOut(wfProcCfgList);
		
		EUDGPagination wfProcCfgPagination = new EUDGPagination(count, wfProcCfgList);
		
		return wfProcCfgPagination;
	}
	
	@Override
	public List<EventHandlerWfProcCfgPO> findHandlerWfProcCfgList(Map<String, Object> params) {
		List<EventHandlerWfProcCfgPO> wfProcCfgList = null;
		
		formatDataIn(params);
		
		wfProcCfgList = eventHandlerWfProcCfgMapper.findPageListByCriteria(params);
		
		formatDataOut(wfProcCfgList);
		
		return wfProcCfgList;
	}
	
	/**
	 * 判断属性是否有效
	 * @param eventHandlerWfProcCfgPO
	 * @return 属性均有效，返回true；有缺少属性，或者属性无效，返回false
	 * @throws Exception
	 */
	private boolean isRecordValid(EventHandlerWfProcCfgPO eventHandlerWfProcCfgPO) throws Exception {
		StringBuffer msgWrong = new StringBuffer("");
		boolean flag = false;
		
		if(eventHandlerWfProcCfgPO != null) {
			String taskCode = eventHandlerWfProcCfgPO.getTaskCode(),
				   eventCodes = eventHandlerWfProcCfgPO.getEventCodes(),
				   regionCode = eventHandlerWfProcCfgPO.getRegionCode();
			Long wfcId = eventHandlerWfProcCfgPO.getWfcId();
			
			if(StringUtils.isBlank(taskCode)) {
				msgWrong.append("缺少参数[taskCode]！");
			}
			if(StringUtils.isBlank(eventCodes)) {
				msgWrong.append("缺少参数[eventCodes]！");
			}
			if(StringUtils.isBlank(regionCode)) {
				msgWrong.append("缺少参数[regionCode]！");
			}
			if(wfcId == null || wfcId < 0) {
				msgWrong.append("缺少参数[wfcId]！");
			}
		}
		
		if(msgWrong.length() > 0) {
			throw new IllegalArgumentException(msgWrong.toString());
		} else {
			flag = true;
		}
		
		return flag;
	}
	
	/**
	 * 判断记录是否没有重复
	 * @param eventHandlerWfProcCfgPO
	 * @return 记录重复，返回false；否则返回true
	 * @throws ZhsqEventException 
	 */
	private boolean isRecordNotDuplicated(EventHandlerWfProcCfgPO eventHandlerWfProcCfgPO) throws ZhsqEventException {
		boolean isNotDuplicated = true;
		
		if(eventHandlerWfProcCfgPO != null) {
			String regionCode = eventHandlerWfProcCfgPO.getRegionCode(),
				   eventCodes = eventHandlerWfProcCfgPO.getEventCodes(),
				   taskCode = eventHandlerWfProcCfgPO.getTaskCode();
			Long wfcId = eventHandlerWfProcCfgPO.getWfcId(),
				 wfpcId = eventHandlerWfProcCfgPO.getWfpcId();
			Map<String, Object> params = new HashMap<String, Object>();
			StringBuffer exceptionMsg = new StringBuffer("");
			
			if(StringUtils.isNotBlank(regionCode)) {
				params.put("regionCode", regionCode);
				params.put("searchType", "eq");
				exceptionMsg.append("[regionCode]为").append(regionCode).append(";");
			}
			if(StringUtils.isNotBlank(taskCode)) {
				params.put("taskCode", taskCode);
				exceptionMsg.append("[taskCode]为").append(taskCode).append(";");
			}
			if(wfcId != null && wfcId > 0) {
				params.put("wfcId", wfcId);
				exceptionMsg.append("[wfcId]为").append(wfcId).append(";");
			}
			if(wfpcId != null && wfpcId > 0) {//更新操作时，使用
				params.put("wfpcId", wfpcId);
				exceptionMsg.append("[wfpcId]为").append(wfpcId).append(";");
			}
			
			if(StringUtils.isNotBlank(eventCodes)) {
				String[] eventCodeArray = eventCodes.split(",");
				String eventCode = "";
				
				for(int index = 0, len = eventCodeArray.length; index < len; index++) {
					eventCode = eventCodeArray[index];
					
					if(StringUtils.isNotBlank(eventCode)) {
						params.put("eventCode", eventCode);
						
						isNotDuplicated = eventHandlerWfProcCfgMapper.findCount4Duplicate(params) == 0;
						
						if(!isNotDuplicated) {
							break;
						}
					}
				}
				
				if(StringUtils.isNotBlank(eventCode)) {
					exceptionMsg.append("[eventCode]为").append(eventCode).append(";");
				}
			}
			
			if(!isNotDuplicated) {
				throw new ZhsqEventException("如下属性组合的记录已存在：" + exceptionMsg);
			}
		}
		
		return isNotDuplicated;
	}
	
	/**
	 * 格式化输入参数
	 * @param params
	 * 			eventCodes	事件类别字符串，多个值以英文逗号分隔
	 */
	private void formatDataIn(Map<String, Object> params) {
		if(CommonFunctions.isNotBlank(params, "eventCodes")) {
			String eventCodes = params.get("eventCodes").toString();
			
			eventCodes = "," + eventCodes.replaceAll(",", ",%,") + ",";
			
			params.put("eventCodes", eventCodes);
		}
	}
	
	/**
	 * 格式化输出数据
	 * @param procCfgList
	 */
	private void formatDataOut(List<EventHandlerWfProcCfgPO> procCfgList) {
		if(procCfgList != null && procCfgList.size() > 0) {
			for(EventHandlerWfProcCfgPO procCfg : procCfgList) {
				formatDataOut(procCfg);
			}
		}
	}
	
	/**
	 * 格式化输出数据
	 * @param procCfg
	 */
	private void formatDataOut(EventHandlerWfProcCfgPO procCfg) {
		if(procCfg != null) {
			String regionCode = procCfg.getRegionCode(),
				   taskCode = procCfg.getTaskCode(),
				   eventCodes = procCfg.getEventCodes();
			Long wfcId = procCfg.getWfcId();
			
			if(StringUtils.isNotBlank(regionCode)) {
				OrgEntityInfoBO orgEntityInfo = orgEntityInfoOutService.selectOrgEntityInfoByOrgCode(regionCode);
				if(orgEntityInfo != null) {
					procCfg.setRegionName(orgEntityInfo.getOrgName());
				}
			}
			
			if(wfcId != null && wfcId > 0) {
				EventHandlerWfCfgPO wfCfg = eventHandlerWfCfgService.findHandlerWfCfgById(wfcId);
				if(wfCfg != null) {
					String bizType = wfCfg.getBizType();
					
					if(EventHandlerWfCfgPO.BIZ_TYPE.WORKFLOW.getCode().equals(bizType)) {
						Workflow workflow = eventDisposalWorkflowService.queryWorkflowById(wfCfg.getWfCfgId());
						
						if(workflow != null) {
							Node node = new Node();
							
							node.setWorkFlowId(workflow.getWorkFlowId());
							node.setDeploymentId(workflow.getDeploymentId());
							node.setNodeName(taskCode);
							
							List<Node> nodeList = eventDisposalWorkflowService.queryNodes(node);
							if(nodeList != null && nodeList.size() > 0) {
								procCfg.setTaskCodeName(nodeList.get(0).getNodeNameZH());
							}
						}
					}
					
				}
			}
			
			if(StringUtils.isNotBlank(eventCodes)) {
				String[] eventCodeArray = eventCodes.split(",");
				StringBuffer eventCodeNames = new StringBuffer("");
				
				for(String eventCode : eventCodeArray) {
					if(StringUtils.isNotBlank(eventCode)) {
						StringBuffer eventClass = new StringBuffer("");
						String bigType = "", bigTypeName = "", bigTypeDictCode = ConstantValue.BIG_TYPE_PCODE;
						Map<String, Object> dictMap = new HashMap<String, Object>();
						BaseDataDict dataDict = null;
						
						for(int index = 2, len = eventCode.length(); index <= len;) {//每两位为一个级别
							bigType = eventCode.substring(0, index);
							
							dictMap.put("dictGeneralCode", bigType);
							dictMap.put("dictPcode", bigTypeDictCode);
							
							dataDict = dictionaryService.findByCodes(dictMap);
							
							if(dataDict != null && dataDict.getDictId() != null) {
								bigTypeName = dataDict.getDictName();
								
								if(StringUtils.isNotBlank(bigTypeName)) {
									eventClass.append("-").append(bigTypeName);
								}
								
								bigTypeDictCode = dataDict.getDictCode();
							} else {//只要有一级不能转换，则清除所有已转换的
								bigTypeName = "";
								eventClass = new StringBuffer("");
								break;
							}
							
							index += 2;
						}
						
						if(eventClass.length() > 0) {
							eventCodeNames.append(",").append(eventClass.substring(1));
						}
					}
				}
				
				if(eventCodeNames.length() > 0) {
					procCfg.setEventCodeNames(eventCodeNames.substring(1));
				}
			}
		}
	}

}
