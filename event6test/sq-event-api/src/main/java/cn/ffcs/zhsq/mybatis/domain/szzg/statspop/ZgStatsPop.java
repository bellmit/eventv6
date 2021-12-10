package cn.ffcs.zhsq.mybatis.domain.szzg.statspop;
// default package

import java.util.Date;

/**
 * 人口统计
 * 
 * ZgStatsPop entity. @author MyEclipse Persistence Tools
 */

public class ZgStatsPop implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = -3383757508600571457L;
	private Long seqId;//主键：seq_stats_pop.nextval
	private Integer syear;
	private Integer smonth;
	private String orgCode;
	private String orgName;
	private String sex;//性别
	private String agegroup;
	private String stype;
	private String stypeStr;
	private Integer snum;
	private Long createId;
	private Long updateId;
	private Date createTime;
	private Date updateTime;
	// Constructors

	/** default constructor */
	public ZgStatsPop() {
	}

	public String getStypeStr() {
		return stypeStr;
	}

	public void setStypeStr(String stypeStr) {
		this.stypeStr = stypeStr;
	}

	public Long getSeqId() {
		return this.seqId;
	}

	public void setSeqId(Long seqid) {
		this.seqId = seqid;
	}

	public Integer getSyear() {
		return this.syear;
	}

	public void setSyear(Integer syear) {
		this.syear = syear;
	}

	public Integer getSmonth() {
		return this.smonth;
	}

	public void setSmonth(Integer smonth) {
		this.smonth = smonth;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAgegroup() {
		return this.agegroup;
	}

	public void setAgegroup(String agegroup) {
		this.agegroup = agegroup;
	}

	public String getStype() {
		return this.stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	public Integer getSnum() {
		return this.snum;
	}

	public void setSnum(Integer snum) {
		this.snum = snum;
	}



	public Long getCreateId() {
		return createId;
	}



	public void setCreateId(Long createId) {
		this.createId = createId;
	}



	public Long getUpdateId() {
		return updateId;
	}



	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
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

	

}