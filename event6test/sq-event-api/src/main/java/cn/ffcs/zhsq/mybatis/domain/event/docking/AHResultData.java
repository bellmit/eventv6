package cn.ffcs.zhsq.mybatis.domain.event.docking;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="result")
@XmlAccessorType(XmlAccessType.NONE)
public class AHResultData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7283945706749848498L;

	@XmlElement(name = "code")
	private String code;

	@XmlElement(name = "message")
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
	
	
	
//	@XmlAttribute(name = "incidentName")

}
