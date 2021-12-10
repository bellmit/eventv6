package cn.ffcs.zhsq.mybatis.domain.szzg.greenManager;

import java.io.Serializable;
import java.util.Date;

public class GreenStdBO implements Serializable{
	private static final long serialVersionUID = -7655057454308896456L;
	private Long seqid;
	private String name="";
	private String type="";
	private String typeName;
	private String content="";
	private String stdval="";
	private String actval="";
	private String syear;
	private Date opdate;
	private String status;
	private Date updateTime;
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getSeqid() {
		return seqid;
	}
	public void setSeqid(Long seqid) {
		this.seqid = seqid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStdval() {
		return stdval;
	}
	public void setStdval(String stdval) {
		this.stdval = stdval;
	}
	public String getActval() {
		return actval;
	}
	public void setActval(String actval) {
		this.actval = actval;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getSyear() {
		return syear;
	}

	public void setSyear(String syear) {
		this.syear = syear;
	}

	public Date getOpdate() {
		return opdate;
	}
	public void setOpdate(Date opdate) {
		this.opdate = opdate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
