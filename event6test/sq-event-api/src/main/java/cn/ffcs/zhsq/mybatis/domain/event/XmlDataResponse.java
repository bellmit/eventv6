package cn.ffcs.zhsq.mybatis.domain.event;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="result")
@XmlAccessorType(XmlAccessType.NONE)
public class XmlDataResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1807720810713786329L;

	@XmlElement(name="code")
	private String code;

	@XmlElement(name="message")
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
