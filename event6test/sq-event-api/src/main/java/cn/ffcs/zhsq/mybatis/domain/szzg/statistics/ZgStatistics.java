package cn.ffcs.zhsq.mybatis.domain.szzg.statistics;
// default package

import java.util.Date;

/**
 * 经济运行
 * huangwenbin
 */

public class ZgStatistics implements java.io.Serializable {

	private static final long serialVersionUID = -3383757508600571457L;
	private Long seqId;//主键：SEQ_STATISTICS_ID.nextval
	private Integer syear;
	private Integer smonth;
	private String dictCode;//字典编码
	private String stype;//指标类型
	private String s1;
	private String s2;
	private String s3;
	private String s4;
	private Long createId;
	private Long updateId;
	private Date createTime;
	private Date updateTime;

	private Long dictId;
	private Long dictPid;
	private String dictName;
	private String dictRemark;
	
	public ZgStatistics() {
	}

	public Long getSeqId() {
		return seqId;
	}

	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}

	public Integer getSyear() {
		return syear;
	}

	public void setSyear(Integer syear) {
		this.syear = syear;
	}

	public Integer getSmonth() {
		return smonth;
	}

	public void setSmonth(Integer smonth) {
		this.smonth = smonth;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	public String getS1() {
		return s1;
	}

	public void setS1(String s1) {
		this.s1 = s1;
	}

	public String getS2() {
		return s2;
	}

	public void setS2(String s2) {
		this.s2 = s2;
	}

	public String getS3() {
		return s3;
	}

	public void setS3(String s3) {
		this.s3 = s3;
	}

	public String getS4() {
		return s4;
	}

	public void setS4(String s4) {
		this.s4 = s4;
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
	
	public String getDictRemark() {
		return dictRemark;
	}

	public void setDictRemark(String dictRemark) {
		this.dictRemark = dictRemark;
	}

	public Long getDictId() {
		return dictId;
	}

	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}

	public Long getDictPid() {
		return dictPid;
	}

	public void setDictPid(Long dictPid) {
		this.dictPid = dictPid;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}
	
	
}