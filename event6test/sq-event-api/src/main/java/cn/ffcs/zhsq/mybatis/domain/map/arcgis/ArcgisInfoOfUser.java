package cn.ffcs.zhsq.mybatis.domain.map.arcgis;

import java.io.Serializable;
import java.util.Date;

/**
 * 2014-05-16 liushi add
 * 
 * @author liushi
 *
 */
public class ArcgisInfoOfUser implements Serializable {

	private static final long serialVersionUID = 2759762667292525991L;
	private Long userId;//用户id
	private String userName;//用户名
	private String realName;//姓名
	private String mobilePhone;//电话
	private Long orgId;//组织id
	private String orgCode;//组织code
	private Double x;//经度
	private Double y;//纬度
	private Date locateTime;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Date getLocateTime() {
		return locateTime;
	}

	public void setLocateTime(Date locateTime) {
		this.locateTime = locateTime;
	}
}
