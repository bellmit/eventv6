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

@XmlRootElement(name="data")
public class XmlDataResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8001655140655789604L;
	private XmlEventResult event;
	
	private XmlEvaluate evaluate;
	
//	@XmlElementWrapper(name="tasks")
	private List<XmlTaskResult> tasks;
	
	private String orgCode;
	
	private String eventId;

	public void setTasks(List<XmlTaskResult> tasks) {
		this.tasks = tasks;
	}
	@XmlElementWrapper(name="tasks")
	@XmlElement(name="task")
	public List<XmlTaskResult> getTasks() {
		return tasks;
	}

	private XmlDataAuth auth;

	public XmlDataAuth getAuth() {
		return auth;
	}

	public void setAuth(XmlDataAuth auth) {
		this.auth = auth;
	}

	public XmlEventResult getEvent() {
		return event;
	}

	public void setEvent(XmlEventResult event) {
		this.event = event;
	}
//	@XmlElement(name="orgCode")
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public XmlEvaluate getEvaluate() {
		return evaluate;
	}
	public void setEvaluate(XmlEvaluate evaluate) {
		this.evaluate = evaluate;
	}

}
