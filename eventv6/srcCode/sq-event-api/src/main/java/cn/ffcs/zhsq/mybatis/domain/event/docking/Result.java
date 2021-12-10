package cn.ffcs.zhsq.mybatis.domain.event.docking;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

public class Result implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8500418336633839399L;
	
	private String innerIncidentCode;

	public String getInnerIncidentCode() {
		return innerIncidentCode;
	}

	public void setInnerIncidentCode(String innerIncidentCode) {
		this.innerIncidentCode = innerIncidentCode;
	}
}
