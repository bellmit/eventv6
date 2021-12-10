package cn.ffcs.zhsq.mybatis.domain.handlerCfg;

import java.io.Serializable;

/**
 * 流程环节配置
 * T_BIZ_WF_PROC_CFG
 * 
 * @author zhangls
 *
 */
public class EventHandlerWfProcCfgPO implements Serializable {

	private static final long serialVersionUID = -4160698100498120720L;
	
	private Long wfpcId;		//主键，序列为：SEQ_WFPC_ID
	private String regionCode;	//地域编码
	private String regionName;	//地域名称
	private String taskCode;	//环节编码/环节名称
	private String taskCodeName;//环节中文名称
	private Long wfcId;			//工作流业务配置主键id
	private String eventCodes;	//事件类型编码，多个值以英文逗号相连
	private String eventCodeNames;	//事件类型名称，多个值以英文逗号相连
	
	public Long getWfpcId() {
		return wfpcId;
	}
	public void setWfpcId(Long wfpcId) {
		this.wfpcId = wfpcId;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getTaskCode() {
		return taskCode;
	}
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	public String getTaskCodeName() {
		return taskCodeName;
	}
	public void setTaskCodeName(String taskCodeName) {
		this.taskCodeName = taskCodeName;
	}
	public Long getWfcId() {
		return wfcId;
	}
	public void setWfcId(Long wfcId) {
		this.wfcId = wfcId;
	}
	public String getEventCodes() {
		return eventCodes;
	}
	public void setEventCodes(String eventCodes) {
		this.eventCodes = eventCodes;
	}
	public String getEventCodeNames() {
		return eventCodeNames;
	}
	public void setEventCodeNames(String eventCodeNames) {
		this.eventCodeNames = eventCodeNames;
	}
	
}
