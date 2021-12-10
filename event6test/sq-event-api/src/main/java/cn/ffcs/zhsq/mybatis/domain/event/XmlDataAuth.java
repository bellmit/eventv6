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

@XmlRootElement(name="auth")
public class XmlDataAuth implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6924862969627298731L;

	private String username;
	
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
