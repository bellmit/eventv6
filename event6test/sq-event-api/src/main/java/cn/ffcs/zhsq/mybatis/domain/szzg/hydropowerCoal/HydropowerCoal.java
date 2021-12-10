package cn.ffcs.zhsq.mybatis.domain.szzg.hydropowerCoal;

import java.io.Serializable;
import java.util.Date;

/**
 * 居民水电煤
 * @author linzhu
 *
 */
public class HydropowerCoal implements Serializable {

	private Long hydropowerCoalId; //主键 居民水电煤id
	private String syear; //年份
	private String orgCode; //组织编码
	private Long usage; //使用量
	private Double yearBasis; //同比
	private Double linkRelRatio; //环比
	private Double yearBasisInc; //同比增长
	private Double linkRelRatioInc; //环比增长
	private String type; //类型（水、电、煤）
	private Date createTime; //创建时间
	private Long createUserId; //创建人id
	private Date updateTime; //更新时间
	private Long updateUserId; //更新人id
	private String status; //状态：1正常0删除
	
	
	private String orgName;//组织名称


	public Long getHydropowerCoalId() {
		return hydropowerCoalId;
	}
	public void setHydropowerCoalId(Long hydropowerCoalId) {
		this.hydropowerCoalId = hydropowerCoalId;
	}
	public String getSyear() {
		return syear;
	}
	public void setSyear(String syear) {
		this.syear = syear;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public Long getUsage() {
		return usage;
	}
	public void setUsage(Long usage) {
		this.usage = usage;
	}
	public Double getYearBasis() {
		return yearBasis;
	}
	public void setYearBasis(Double yearBasis) {
		this.yearBasis = yearBasis;
	}
	public Double getLinkRelRatio() {
		return linkRelRatio;
	}
	public void setLinkRelRatio(Double linkRelRatio) {
		this.linkRelRatio = linkRelRatio;
	}
	public Double getYearBasisInc() {
		return yearBasisInc;
	}
	public void setYearBasisInc(Double yearBasisInc) {
		this.yearBasisInc = yearBasisInc;
	}
	public Double getLinkRelRatioInc() {
		return linkRelRatioInc;
	}
	public void setLinkRelRatioInc(Double linkRelRatioInc) {
		this.linkRelRatioInc = linkRelRatioInc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}


}
