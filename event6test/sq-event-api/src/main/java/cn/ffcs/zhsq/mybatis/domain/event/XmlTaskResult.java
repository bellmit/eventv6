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

@XmlRootElement(name="task")
public class XmlTaskResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2792995630634035038L;
	
	private List<XmlAttr> attrs = new ArrayList<XmlAttr>();
	
	@XmlElementWrapper(name="attrs")
	@XmlElement(name="attr")
	public List<XmlAttr> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<XmlAttr> attrs) {
		this.attrs = attrs;
	}
	
	private String nextOrgCode;
	
	private String nextUserId;
	
	private String oppoSideBusiCode;
	
	private String createTime;
	
	private String taskId;
	
	private String eventId;
	
	private String transactorId;
	
	private String transactorName;
	
	private String transactOrgID;
	
	private String transactOrgName;
	
	private String results;
	
//	private String createTime;
	
//	private String createUserID;
//	
//	private String createUserName;
	
	private String readTime;
	
	private String readUserID;
	
	private String readUserName;
	
	private String endTime;
	
	private String taskName;
	
	private String taskType;
	
	private String optType;
	
	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getTransactorId() {
		return transactorId;
	}

	public void setTransactorId(String transactorId) {
		this.transactorId = transactorId;
	}

	public String getTransactorName() {
		return transactorName;
	}

	public void setTransactorName(String transactorName) {
		this.transactorName = transactorName;
	}

	public String getTransactOrgID() {
		return transactOrgID;
	}

	public void setTransactOrgID(String transactOrgID) {
		this.transactOrgID = transactOrgID;
	}

	public String getTransactOrgName() {
		return transactOrgName;
	}

	public void setTransactOrgName(String transactOrgName) {
		this.transactOrgName = transactOrgName;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public String getReadTime() {
		return readTime;
	}

	public void setReadTime(String readTime) {
		this.readTime = readTime;
	}

	public String getReadUserID() {
		return readUserID;
	}

	public void setReadUserID(String readUserID) {
		this.readUserID = readUserID;
	}

	public String getReadUserName() {
		return readUserName;
	}

	public void setReadUserName(String readUserName) {
		this.readUserName = readUserName;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getOppoSideParentBizCode() {
		return oppoSideParentBizCode;
	}

	public void setOppoSideParentBizCode(String oppoSideParentBizCode) {
		this.oppoSideParentBizCode = oppoSideParentBizCode;
	}

	public String getOppoSideParentBizType() {
		return oppoSideParentBizType;
	}

	public void setOppoSideParentBizType(String oppoSideParentBizType) {
		this.oppoSideParentBizType = oppoSideParentBizType;
	}

	public String getOppoSideBizCode() {
		return oppoSideBizCode;
	}

	public void setOppoSideBizCode(String oppoSideBizCode) {
		this.oppoSideBizCode = oppoSideBizCode;
	}

	public String getOppoSideBizType() {
		return oppoSideBizType;
	}

	public void setOppoSideBizType(String oppoSideBizType) {
		this.oppoSideBizType = oppoSideBizType;
	}

	public String getSrcPlatform() {
		return srcPlatform;
	}

	public void setSrcPlatform(String srcPlatform) {
		this.srcPlatform = srcPlatform;
	}

	private String oppoSideParentBizCode;
	
	private String oppoSideParentBizType;
	
	private String oppoSideBizCode;
	
	private String oppoSideBizType;
	
	private String srcPlatform;
	
	public String getNextUserId() {
		return nextUserId;
	}

	public void setNextUserId(String nextUserId) {
		this.nextUserId = nextUserId;
	}

	public String getOppoSideBusiCode() {
		return oppoSideBusiCode;
	}

	public void setOppoSideBusiCode(String oppoSideBusiCode) {
		this.oppoSideBusiCode = oppoSideBusiCode;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserID() {
		return createUserID;
	}

	public void setCreateUserID(String createUserID) {
		this.createUserID = createUserID;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	private String createUserID;
	
	private String createUserName;

	public String getNextOrgCode() {
		return nextOrgCode;
	}

	public void setNextOrgCode(String nextOrgCode) {
		this.nextOrgCode = nextOrgCode;
	}
}
