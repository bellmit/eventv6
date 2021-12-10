package cn.ffcs.zhsq.mybatis.domain.event.docking;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="doc")
@XmlAccessorType(XmlAccessType.NONE)
public class SJLCResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1999742561721459959L;

	@XmlElement(name = "result")
	private String result;

	@XmlElement(name = "message")
	private String message;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
	
//	@XmlAttribute(name = "incidentName")

}
