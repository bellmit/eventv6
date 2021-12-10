package cn.ffcs.zhsq.mybatis.domain.handlerCfg;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EventHandlerCfgVO implements Serializable {

	private static final long serialVersionUID = 2160655623377092276L;

	private Long wfcId;
	private String regionCode;
	private Long wfCfgId;
	private String bizType;
	private String status;
	
	private List<Long> wfpcIds;
	private List<String> regionCodes;
	private List<String> taskCodes;
	private List<String> eventCodes;
	
	private List<String> transactorIds;
	private List<String> transactorTypes;
	
	private Long createUserId;
	private Date createTime;
	private Long updateUserId;
	private Date updateTime;
	
	public Long getWfcId() {
		return wfcId;
	}
	public void setWfcId(Long wfcId) {
		this.wfcId = wfcId;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public Long getWfCfgId() {
		return wfCfgId;
	}
	public void setWfCfgId(Long wfCfgId) {
		this.wfCfgId = wfCfgId;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Long> getWfpcIds() {
		return wfpcIds;
	}
	public void setWfpcIds(List<Long> wfpcIds) {
		this.wfpcIds = wfpcIds;
	}
	public List<String> getRegionCodes() {
		return regionCodes;
	}
	public void setRegionCodes(List<String> regionCodes) {
		this.regionCodes = regionCodes;
	}
	public List<String> getTaskCodes() {
		return taskCodes;
	}
	public void setTaskCodes(List<String> taskCodes) {
		this.taskCodes = taskCodes;
	}
	public List<String> getEventCodes() {
		return eventCodes;
	}
	public void setEventCodes(List<String> eventCodes) {
		this.eventCodes = eventCodes;
	}
	public List<String> getTransactorIds() {
		return transactorIds;
	}
	public void setTransactorIds(List<String> transactorIds) {
		this.transactorIds = transactorIds;
	}
	public List<String> getTransactorTypes() {
		return transactorTypes;
	}
	public void setTransactorTypes(List<String> transactorTypes) {
		this.transactorTypes = transactorTypes;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
