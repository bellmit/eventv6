package cn.ffcs.zhsq.handlerCfg.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.workflow.om.Workflow;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.handlerCfg.service.IEventHandlerWfCfgService;
import cn.ffcs.zhsq.mybatis.domain.handlerCfg.EventHandlerWfCfgPO;
import cn.ffcs.zhsq.mybatis.persistence.handlerCfg.EventHandlerWfCfgMapper;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;

/**
 * 业务配置接口
 * T_BIZ_WF_CFG
 * 
 * @author zhangls
 *
 */
@Service(value="eventHandlerWfCfgService")
public class EventHandlerWfCfgServiceImpl implements IEventHandlerWfCfgService {
	
	@Autowired
	private EventHandlerWfCfgMapper eventHandlerWfCfgMapper;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Override
	public Long saveHandlerWfCfg(EventHandlerWfCfgPO eventHandlerWfCfgPO) throws Exception {
		Long wfcId = -1L;
		
		if(eventHandlerWfCfgPO != null) {
			int result = 0;
			
			recordDefault(eventHandlerWfCfgPO);
			
			if(isRecordValid(eventHandlerWfCfgPO) && isRecordNotDuplicated(eventHandlerWfCfgPO)) {
				result = eventHandlerWfCfgMapper.insert(eventHandlerWfCfgPO);
			}
			
			if(result > 0) {
				wfcId = eventHandlerWfCfgPO.getWfcId();
				formatDataOut(eventHandlerWfCfgPO);
			}
		}
		
		return wfcId;
	}

	@Override
	public boolean updateHandlerWfCfgById(
			EventHandlerWfCfgPO eventHandlerWfCfgPO) throws Exception {
		boolean flag = false;
		
		if(eventHandlerWfCfgPO != null) {
			Long wfcId = eventHandlerWfCfgPO.getWfcId();
			
			if(wfcId != null && wfcId > 0) {
				int result = 0;
				
				if(isRecordNotDuplicated(eventHandlerWfCfgPO)) {
					result = eventHandlerWfCfgMapper.update(eventHandlerWfCfgPO);
				}
				
				flag = result > 0;
			}
		}
		
		return flag;
	}

	@Override
	public boolean delHandlerWfCfgById(Long wfcId, Long delUserId) {
		boolean flag = false;
		
		if(wfcId != null && wfcId > 0) {
			flag = eventHandlerWfCfgMapper.delete(wfcId, delUserId) > 0;
		}
		
		return flag;
	}

	@Override
	public EventHandlerWfCfgPO findHandlerWfCfgById(Long wfcId) {
		EventHandlerWfCfgPO eventHandlerWfCfgPO = null;
		
		if(wfcId != null && wfcId > 0) {
			eventHandlerWfCfgPO = eventHandlerWfCfgMapper.findById(wfcId);
			formatDataOut(eventHandlerWfCfgPO);
		}
		
		return eventHandlerWfCfgPO;
	}
	
	@Override
	public EUDGPagination findHandlerWfCfgPagination(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		
		int count = eventHandlerWfCfgMapper.findCountByCriteria(params);
		List<EventHandlerWfCfgPO> wfCfgList = eventHandlerWfCfgMapper.findPageListByCriteria(params, rowBounds);
		
		formatDataOut(wfCfgList);
		
		EUDGPagination wfCfgPagination = new EUDGPagination(count, wfCfgList);
		
		return wfCfgPagination;
	}
	
	/**
	 * 判断属性是否有效
	 * @param eventHandlerWfCfgPO
	 * @return 属性均有效，返回true；有缺少属性，或者属性无效，返回false
	 * @throws Exception
	 */
	private boolean isRecordValid(EventHandlerWfCfgPO eventHandlerWfCfgPO) throws Exception {
		StringBuffer msgWrong = new StringBuffer("");
		boolean flag = false;
		
		if(eventHandlerWfCfgPO != null) {
			String bizType = eventHandlerWfCfgPO.getBizType(),
				   regionCode = eventHandlerWfCfgPO.getRegionCode();
			Long wfCfgId = eventHandlerWfCfgPO.getWfCfgId();
			
			if(StringUtils.isBlank(bizType)) {
				msgWrong.append("缺少参数[bizType]！");			
			}
			if(wfCfgId == null || wfCfgId < 0) {
				msgWrong.append("缺少参数[wfCfgId]！");
			}
			if(StringUtils.isBlank(regionCode)) {
				msgWrong.append("缺少参数[regionCode]！");
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
	 * 设置属性默认值
	 * @param eventHandlerWfCfgPO
	 */
	private void recordDefault(EventHandlerWfCfgPO eventHandlerWfCfgPO) {
		if(eventHandlerWfCfgPO != null) {
			String status = eventHandlerWfCfgPO.getStatus();
			
			if(StringUtils.isBlank(status)) {
				eventHandlerWfCfgPO.setStatus(EventHandlerWfCfgPO.STATUS.VALID.toString());
			}
		}
	}
	
	/**
	 * 判断记录是否没有重复
	 * @param eventHandlerWfCfgPO
	 * @return 记录重复，返回false；否则返回true
	 * @throws ZhsqEventException 
	 */
	private boolean isRecordNotDuplicated(EventHandlerWfCfgPO eventHandlerWfCfgPO) throws ZhsqEventException {
		boolean isNotDuplicated = true;
		
		if(eventHandlerWfCfgPO != null) {
			String bizType = eventHandlerWfCfgPO.getBizType(),
				   regionCode = eventHandlerWfCfgPO.getRegionCode(),
				   searchType = "eq";
			Long wfCfgId = eventHandlerWfCfgPO.getWfCfgId(),
				 wfcId = eventHandlerWfCfgPO.getWfcId();
			
			StringBuffer exceptionMsg = new StringBuffer("");
			
			Map<String, Object> params = new HashMap<String, Object>();
			
			if(StringUtils.isNotBlank(regionCode)) {
				params.put("regionCode", regionCode);
				params.put("searchType", searchType);
				exceptionMsg.append("[regionCode]为").append(regionCode).append(";");
			}
			if(StringUtils.isNotBlank(bizType)) {
				params.put("bizType", bizType);
				exceptionMsg.append("[bizType]为").append(bizType).append(";");
			}
			if(wfCfgId != null && wfCfgId > 0) {
				params.put("wfCfgId", wfCfgId);
				exceptionMsg.append("[wfCfgId]为").append(wfCfgId).append(";");
			}
			if(wfcId != null && wfcId > 0) {
				params.put("wfcId", wfcId);
			}
			
			isNotDuplicated = eventHandlerWfCfgMapper.findCount4Duplicate(params) == 0;
			
			if(!isNotDuplicated) {
				throw new ZhsqEventException("如下属性组合的记录已存在：" + exceptionMsg);
			}
		}
		
		return isNotDuplicated;
	}
	
	/**
	 * 格式化输出记录
	 * @param eventHandlerWfCfgPOList
	 */
	private void formatDataOut(List<EventHandlerWfCfgPO> wfCfgList) {
		if(wfCfgList != null && wfCfgList.size() > 0) {
			for(EventHandlerWfCfgPO eventHandlerWfCfgPO : wfCfgList) {
				formatDataOut(eventHandlerWfCfgPO);
			}
		}
	}
	
	/**
	 * 格式化输出记录
	 * @param eventHandlerWfCfgPO
	 */
	private void formatDataOut(EventHandlerWfCfgPO eventHandlerWfCfgPO) {
		if(eventHandlerWfCfgPO != null) {
			String regionCode = eventHandlerWfCfgPO.getRegionCode(),
				   bizType = eventHandlerWfCfgPO.getBizType();
			Long wfCfgId = eventHandlerWfCfgPO.getWfCfgId();
			
			if(StringUtils.isNotBlank(regionCode)) {
				OrgEntityInfoBO orgEntityInfo = orgEntityInfoOutService.selectOrgEntityInfoByOrgCode(regionCode);
				if(orgEntityInfo != null) {
					eventHandlerWfCfgPO.setRegionName(orgEntityInfo.getOrgName());
				}
			}
			
			if(StringUtils.isNotBlank(bizType)) {
				EventHandlerWfCfgPO.BIZ_TYPE[] bizTypes = EventHandlerWfCfgPO.BIZ_TYPE.values();
				for(EventHandlerWfCfgPO.BIZ_TYPE bizTypeEnum : bizTypes) {
					if(bizTypeEnum.getCode().equals(bizType)) {
						eventHandlerWfCfgPO.setBizTypeName(bizTypeEnum.getName());
						break;
					}
				}
			}
			
			if(wfCfgId != null && wfCfgId > 0) {
				if(EventHandlerWfCfgPO.BIZ_TYPE.WORKFLOW.toString().equals(bizType)) {
					Workflow workflow = eventDisposalWorkflowService.queryWorkflowById(wfCfgId);
					if(workflow != null) {
						String wfCfgName = workflow.getFlowName(),
							   respectOrgName = workflow.getRespectOrgName();
						
						if(StringUtils.isNotBlank(respectOrgName)) {
							wfCfgName += "-" + respectOrgName;
						}
						
						eventHandlerWfCfgPO.setWfCfgName(wfCfgName);
					}
				}
			}
		}
	}

}
