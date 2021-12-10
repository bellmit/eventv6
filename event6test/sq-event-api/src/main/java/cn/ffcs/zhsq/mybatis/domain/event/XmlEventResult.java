package cn.ffcs.zhsq.mybatis.domain.event;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.text.ParseException;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import cn.ffcs.zhsq.mybatis.domain.crowd.VisitRecord;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;


@XmlRootElement(name="event")
public class XmlEventResult implements Serializable{
	
/**
	 * 
	 */
	private static final long serialVersionUID = -3117727021748205972L;
	//	@XmlAttribute(name="gridCode")
	private String gridCode;
//	@XmlAttribute(name="eventName")
	private String eventName;
	
	private String happenTimeStr;
	
	private String occurred;
	
	private String content;
	
	private String expectDepartMent;
	
	private String handleDate;
	
	private Boolean isSendSms;
	
	private String oppoSideBusiCode;
	
	private String bizPlatform;
	
	private Long taskId;
	
	private String eventType;
		
	private String urgency;
	
	private String influence;
	
	private String creatorID;
	
	private String creatorName;
	
	private String contactUserName;
	
	private String status;
	
	private String closeOrgName;
	
	private String contactTel;
	private String evlTime;//评价时间
	private String evlName;//评价人员
	private String satisfaction;//满意度
	private String collectWay;//采集渠道
	private String source;
	private String longitude;//经度
	private String latitude;//纬度
	private String targetOrgCode;//派发组织
	private String orgCode;//所属组织
	private String formId;//第三方ID
	private String remarks;//备注描述
	private Boolean isMatter;//是否为入格事项事件
	private String serialNumber;//入格事项序号
	private String isShowPublic;//是否公开
	private String interval;//申请延期天数
	private String intervalUnit;//延期天数单位 1：工作日，2：自然日
	private String userType;//上报人角色类型 1：网格员，2：市民
	private String createTimeStr;//申请时间
	private String timeAppCheckCreateTimeStr;//审核开始时间
	private String timeAppCheckUpdateTimeStr;//审核结束时间
	private String reason;//延期理由
	private Boolean isBefore;//是否是预上报事件  true 
	private String involvedNum;//涉及人数
	
	private String isUseOrgCodeToVerify;//是否直接使用orgCode当作审核记录的网格 1：使用
	private String useXyFindGridByLevel;//使用经纬度和指定网格层级获取网格
	
	
	 public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getIntervalUnit() {
		return intervalUnit;
	}

	public void setIntervalUnit(String intervalUnit) {
		this.intervalUnit = intervalUnit;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getTimeAppCheckCreateTimeStr() {
		return timeAppCheckCreateTimeStr;
	}

	public void setTimeAppCheckCreateTimeStr(String timeAppCheckCreateTimeStr) {
		this.timeAppCheckCreateTimeStr = timeAppCheckCreateTimeStr;
	}

	public String getTimeAppCheckUpdateTimeStr() {
		return timeAppCheckUpdateTimeStr;
	}

	public void setTimeAppCheckUpdateTimeStr(String timeAppCheckUpdateTimeStr) {
		this.timeAppCheckUpdateTimeStr = timeAppCheckUpdateTimeStr;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Boolean getIsBefore() {
		return isBefore;
	}

	public void setIsBefore(Boolean isBefore) {
		this.isBefore = isBefore;
	}

	public String getInvolvedNum() {
		return involvedNum;
	}

	public void setInvolvedNum(String involvedNum) {
		this.involvedNum = involvedNum;
	}

	public List<XmlTaskResult> getXmlTaskResult() {
		return xmlTaskResult;
	}

	public void setXmlTaskResult(List<XmlTaskResult> xmlTaskResult) {
		this.xmlTaskResult = xmlTaskResult;
	}

	public String getIsUseOrgCodeToVerify() {
		return isUseOrgCodeToVerify;
	}

	public void setIsUseOrgCodeToVerify(String isUseOrgCodeToVerify) {
		this.isUseOrgCodeToVerify = isUseOrgCodeToVerify;
	}

	public String getUseXyFindGridByLevel() {
		return useXyFindGridByLevel;
	}

	public void setUseXyFindGridByLevel(String useXyFindGridByLevel) {
		this.useXyFindGridByLevel = useXyFindGridByLevel;
	}

	public String getIsShowPublic() {
		return isShowPublic;
	}

	public void setIsShowPublic(String isShowPublic) {
		this.isShowPublic = isShowPublic;
	}

	public String getEvlName() {
			return evlName;
	}

	public void setEvlName(String evlName) {
			this.evlName = evlName;
	}
	public String getEvlTime() {
		return evlTime;
	}

	public void setEvlTime(String evlTime) {
		this.evlTime = evlTime;
	}
	public String getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(String satisfaction) {
		this.satisfaction = satisfaction;
	}
	
	
	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Boolean getIsMatter() {
		return isMatter;
	}

	public void setIsMatter(Boolean isMatter) {
		this.isMatter = isMatter;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	//	@XmlElementWrapper(name="attrs")
//	@XmlElement(name="attr")
	private List<XmlAttr> attrs = new ArrayList<XmlAttr>();
	
	private List<XmlPeople> peoples = new ArrayList<XmlPeople>();
	
	@XmlElementWrapper(name="peoples")
	@XmlElement(name="people")
	public List<XmlPeople> getPeoples() {
		return peoples;
	}

	public void setPeoples(List<XmlPeople> peoples) {
		this.peoples = peoples;
	}
	
	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	//
	private String closerID;
	
	private String closerName;
	
	private String closeDate;
	
	private String closeOrgID;
	
	private String eventId;
	
	private String pics;

	private String x;

	private String y;
	
	public String getCloserID() {
		return closerID;
	}

	public void setCloserID(String closerID) {
		this.closerID = closerID;
	}

	public String getCloserName() {
		return closerName;
	}

	public void setCloserName(String closerName) {
		this.closerName = closerName;
	}
	public String getCollectWay() {
		return collectWay;
	}


	public void setCollectWay(String collectWay) {
		this.collectWay = collectWay;
	}
	public String getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}

	public String getCloseOrgID() {
		return closeOrgID;
	}

	public void setCloseOrgID(String closeOrgID) {
		this.closeOrgID = closeOrgID;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	private List<XmlTaskResult> xmlTaskResult;

	public String getHappenTimeStr() {
		return happenTimeStr;
	}

	public void setHappenTimeStr(String happenTimeStr) {
		this.happenTimeStr = happenTimeStr;
	}

	public String getOccurred() {
		return occurred;
	}

	public void setOccurred(String occurred) {
		this.occurred = occurred;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHandleDate() {
		return handleDate;
	}

	public void setHandleDate(String handleDate) {
		this.handleDate = handleDate;
	}

	public Boolean getIsSendSms() {
		return isSendSms;
	}

	public void setIsSendSms(Boolean isSendSms) {
		this.isSendSms = isSendSms;
	}

	public String getOppoSideBusiCode() {
		return oppoSideBusiCode;
	}

	public void setOppoSideBusiCode(String oppoSideBusiCode) {
		this.oppoSideBusiCode = oppoSideBusiCode;
	}

	public String getBizPlatform() {
		return bizPlatform;
	}

	public void setBizPlatform(String bizPlatform) {
		this.bizPlatform = bizPlatform;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public String getInfluence() {
		return influence;
	}

	public void setInfluence(String influence) {
		this.influence = influence;
	}

	public String getCreatorID() {
		return creatorID;
	}

	public void setCreatorID(String creatorID) {
		this.creatorID = creatorID;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getContactUserName() {
		return contactUserName;
	}

	public void setContactUserName(String contactUserName) {
		this.contactUserName = contactUserName;
	}

	public String getRegisterTimeStr() {
		return registerTimeStr;
	}

	public void setRegisterTimeStr(String registerTimeStr) {
		this.registerTimeStr = registerTimeStr;
	}

	public String getAdvice() {
		return advice;
	}

	public void setAdvice(String advice) {
		this.advice = advice;
	}

	private String registerTimeStr;
	
	private String advice;
	
	

//	@XmlTransient
	public String getGridCode() {
		return gridCode;
	}

	public void setGridCode(String gridCode) {
		this.gridCode = gridCode;
	}
//	@XmlTransient
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getCloseOrgName() {
		return closeOrgName;
	}

	public void setCloseOrgName(String closeOrgName) {
		this.closeOrgName = closeOrgName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlElementWrapper(name="tasks")
	@XmlElement(name="task")
	public List<XmlTaskResult> getTasks() {
		return xmlTaskResult;
	}

	public void setTasks(List<XmlTaskResult> xmlTaskResult) {
		this.xmlTaskResult = xmlTaskResult;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPics() {
		return pics;
	}

	public void setPics(String pics) {
		this.pics = pics;
	}
	@XmlElementWrapper(name="attrs")
	@XmlElement(name="attr")
	public List<XmlAttr> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<XmlAttr> attrs) {
		this.attrs = attrs;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getTargetOrgCode() {
		return targetOrgCode;
	}

	public void setTargetOrgCode(String targetOrgCode) {
		this.targetOrgCode = targetOrgCode;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getExpectDepartMent() {
		return expectDepartMent;
	}

	public void setExpectDepartMent(String expectDepartMent) {
		this.expectDepartMent = expectDepartMent;
	}
}
