package cn.ffcs.zhsq.mybatis.domain.triadRelatedCases;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 庭审记录表模块bo对象
 * @Author: chenshikai
 * @Date: 09-08 17:46:14
 * @Copyright: 2021 福富软件
 */
public class TrialRecord implements Serializable {

	private String trialUuid; //庭审记录ID
	private String undCaseUuid; //涉黑案件逻辑主键
	private Integer trialLevel; //1为一审,2为2审
	private Date judgmentTime; //判决时间
	private String judicialUnit; //审判单位
	private String trialStatus; //01生效,02上诉（抗诉）
	private String orgCode; //组织编码
	private Long creator; //创建人
	private Date createTime; //创建时间
	private Long updator; //修改人
	private Date updateTime; //修改时间
	private String isValid; //有效状态(1有效0无效)
	private String remark; //备注


	public String getTrialUuid() {
		return trialUuid;
	}

	public void setTrialUuid(String trialUuid) {
		this.trialUuid = trialUuid;
	}

	public String getUndCaseUuid() {
		return undCaseUuid;
	}
	public void setUndCaseUuid(String undCaseUuid) {
		this.undCaseUuid = undCaseUuid;
	}
	public Integer getTrialLevel() {
		return trialLevel;
	}
	public void setTrialLevel(Integer trialLevel) {
		this.trialLevel = trialLevel;
	}
	public Date getJudgmentTime() {
		return judgmentTime;
	}
	public void setJudgmentTime(Date judgmentTime) {
		this.judgmentTime = judgmentTime;
	}
	public String getJudicialUnit() {
		return judicialUnit;
	}
	public void setJudicialUnit(String judicialUnit) {
		this.judicialUnit = judicialUnit;
	}
	public String getTrialStatus() {
		return trialStatus;
	}
	public void setTrialStatus(String trialStatus) {
		this.trialStatus = trialStatus;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public Long getCreator() {
		return creator;
	}
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getUpdator() {
		return updator;
	}
	public void setUpdator(Long updator) {
		this.updator = updator;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}


}