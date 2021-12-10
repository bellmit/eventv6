package cn.ffcs.zhsq.mybatis.domain.szzg.education;

import java.io.Serializable;
import java.util.Date;

public class EducationBO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6863516635861854674L;
	private Long seqid;
	private String orgCode;
	private String orgName;
	private String type;
	private String typeName;
	private Long males;
	private Long females;
	private Long totalPeople;
	private Date createTime;
	private Date updateTime;
	private String yearStr;
	private String status;
	private String orgPath;

	public String getOrgPath() {
		return orgPath;
	}

	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getYearStr() {
		return yearStr;
	}

	public void setYearStr(String yearStr) {
		this.yearStr = yearStr;
	}

	public Long getSeqid() {
		return seqid;
	}
	public void setSeqid(Long seqid) {
		this.seqid = seqid;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Long getMales() {
		return males;
	}
	public void setMales(Long males) {
		this.males = males;
	}
	public Long getFemales() {
		return females;
	}
	public void setFemales(Long females) {
		this.females = females;
	}
	public Long getTotalPeople() {
		return totalPeople;
	}
	public void setTotalPeople(Long totalPeople) {
		this.totalPeople = totalPeople;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
