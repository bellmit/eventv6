package cn.ffcs.zhsq.mybatis.domain.xiangshui;

import java.io.Serializable;

public class LogStatistics implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orgName;//组织全称
	private Long orgCode;//组织编码
	private Long logCount;//日志数量
	private Long personCount;//网格员数量
	public LogStatistics() {
		
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Long getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(Long orgCode) {
		this.orgCode = orgCode;
	}
	public Long getLogCount() {
		return logCount;
	}
	public void setLogCount(Long logCount) {
		this.logCount = logCount;
	}
	public Long getPersonCount() {
		return personCount;
	}
	public void setPersonCount(Long personCount) {
		this.personCount = personCount;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LogStatistics [orgName=");
		builder.append(orgName);
		builder.append(", orgCode=");
		builder.append(orgCode);
		builder.append(", logCount=");
		builder.append(logCount);
		builder.append(", personCount=");
		builder.append(personCount);
		builder.append("]");
		return builder.toString();
	}
	
}
