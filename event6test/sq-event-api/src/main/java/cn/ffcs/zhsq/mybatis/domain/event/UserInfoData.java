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
public class UserInfoData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6365767242891798178L;

	private Long userId;

	private String userName;
	
	private Long orgId;
	
	private String orgCode;
	
	private String orgName;
	
	private String tel;

	@XmlElement(name="userId")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@XmlElement(name="userName")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@XmlElement(name="orgId")
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	@XmlElement(name="orgCode")
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	@XmlElement(name="orgName")
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}
