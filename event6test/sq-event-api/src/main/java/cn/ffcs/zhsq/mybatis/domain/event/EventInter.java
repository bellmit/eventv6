package cn.ffcs.zhsq.mybatis.domain.event;

import java.io.Serializable;
import java.util.Date;

public class EventInter implements Serializable {
	
	private static final long serialVersionUID = -5981431609183333605L;
	
	//事件中间表字段T_SJ_EVENT_INTER
	private Long interId;				//中间表ID  INTER_ID
	private String oppoSideBusiCode;	//关联对象ID OPPO_SIDE_BUSI_CODE
	private String oppoSideBusiType;	//关联对象类型 OPPO_SIDE_BUSI_TYPE
	private String ownSideEventCode;	//关联事件ID OWN_SIDE_EVENT_CODE
	private String ownSideEventStatus;	//关联事件状态 OWN_SIDE_EVENT_STATUS
	private String interactiveStatus;	//交互状态 INTERACTIVE_STATUS 00：已确认；01：已反馈。
	private String recStatus;			//反馈状态 0：未反馈；1：已反馈。
	private String eventSrc;			//事件来源
	private Long creator;				//创建人ID
	private Date created;				//创建时间
	private Long updater;				//修改人ID
	private Date updated;				//修改时间
	private String status;				//记录状态 01：有效；06：删除。
	
	public Long getInterId() {
		return interId;
	}
	public void setInterId(Long interId) {
		this.interId = interId;
	}
	public String getOppoSideBusiCode() {
		return oppoSideBusiCode;
	}
	public void setOppoSideBusiCode(String oppoSideBusiCode) {
		this.oppoSideBusiCode = oppoSideBusiCode;
	}
	public String getOppoSideBusiType() {
		return oppoSideBusiType;
	}
	public void setOppoSideBusiType(String oppoSideBusiType) {
		this.oppoSideBusiType = oppoSideBusiType;
	}
	public String getOwnSideEventCode() {
		return ownSideEventCode;
	}
	public void setOwnSideEventCode(String ownSideEventCode) {
		this.ownSideEventCode = ownSideEventCode;
	}
	public String getOwnSideEventStatus() {
		return ownSideEventStatus;
	}
	public void setOwnSideEventStatus(String ownSideEventStatus) {
		this.ownSideEventStatus = ownSideEventStatus;
	}
	public String getInteractiveStatus() {
		return interactiveStatus;
	}
	public void setInteractiveStatus(String interactiveStatus) {
		this.interactiveStatus = interactiveStatus;
	}
	public String getRecStatus() {
		return recStatus;
	}
	public void setRecStatus(String recStatus) {
		this.recStatus = recStatus;
	}
	public String getEventSrc() {
		return eventSrc;
	}
	public void setEventSrc(String eventSrc) {
		this.eventSrc = eventSrc;
	}
	public Long getCreator() {
		return creator;
	}
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Long getUpdater() {
		return updater;
	}
	public void setUpdater(Long updater) {
		this.updater = updater;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
