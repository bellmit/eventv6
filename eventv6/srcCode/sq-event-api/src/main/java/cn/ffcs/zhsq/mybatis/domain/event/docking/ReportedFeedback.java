package cn.ffcs.zhsq.mybatis.domain.event.docking;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ReportedFeedback",namespace="http://www.dscomm.com.cn/ns/soc/dailyIncidentService")
@XmlAccessorType(XmlAccessType.NONE)
public class ReportedFeedback  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6607885584263096047L;

	@XmlAttribute(name = "commandCode")
	private String commandCode;

	@XmlAttribute(name = "feedbackSource")
	private int feedbackSource;

	@XmlAttribute(name = "feedbackType")
	private String feedbackType;

	@XmlAttribute(name = "content")
	private String content;

	@XmlAttribute(name = "feedbackPerson")
	private String feedbackPerson;

	@XmlAttribute(name = "editPerson")
	private String editPerson;

	@XmlAttribute(name = "feedbackDate")
	private String feedbackDate;

	@XmlAttribute(name = "feedbackSourceCode")
	private String feedbackSourceCode;

	@XmlAttribute(name = "feedbackedIncidentType")
	private String feedbackedIncidentType;

	public String getCommandCode() {
		return commandCode;
	}

	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}

	public int getFeedbackSource() {
		return feedbackSource;
	}

	public void setFeedbackSource(int feedbackSource) {
		this.feedbackSource = feedbackSource;
	}

	public String getFeedbackType() {
		return feedbackType;
	}

	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFeedbackPerson() {
		return feedbackPerson;
	}

	public void setFeedbackPerson(String feedbackPerson) {
		this.feedbackPerson = feedbackPerson;
	}

	public String getEditPerson() {
		return editPerson;
	}

	public void setEditPerson(String editPerson) {
		this.editPerson = editPerson;
	}

	public String getFeedbackDate() {
		return feedbackDate;
	}

	public void setFeedbackDate(String feedbackDate) {
		this.feedbackDate = feedbackDate;
	}

	public String getFeedbackSourceCode() {
		return feedbackSourceCode;
	}

	public void setFeedbackSourceCode(String feedbackSourceCode) {
		this.feedbackSourceCode = feedbackSourceCode;
	}

	public String getFeedbackedIncidentType() {
		return feedbackedIncidentType;
	}

	public void setFeedbackedIncidentType(String feedbackedIncidentType) {
		this.feedbackedIncidentType = feedbackedIncidentType;
	}
}
