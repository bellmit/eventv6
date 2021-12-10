package cn.ffcs.zhsq.mybatis.domain.event;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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


@XmlRootElement(name="evaluate")
public class XmlEvaluate implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1831833998893881821L;

	private String eventId;
	
	private String evalContent;
	
	private String evalLevel;
	
	private String evalDate;
	
	private String userName;
	
	private String userId;
	
	private String userCode;
	
	private String orgName;
	
	private String orgId;
	
	private String orgCode;
	
	private String bizPlatform;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEvalContent() {
		return evalContent;
	}

	public void setEvalContent(String evalContent) {
		this.evalContent = evalContent;
	}

	public String getEvalLevel() {
		return evalLevel;
	}

	public void setEvalLevel(String evalLevel) {
		this.evalLevel = evalLevel;
	}

	public String getEvalDate() {
		return evalDate;
	}

	public void setEvalDate(String evalDate) {
		this.evalDate = evalDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getBizPlatform() {
		return bizPlatform;
	}

	public void setBizPlatform(String bizPlatform) {
		this.bizPlatform = bizPlatform;
	}
	
	
}
