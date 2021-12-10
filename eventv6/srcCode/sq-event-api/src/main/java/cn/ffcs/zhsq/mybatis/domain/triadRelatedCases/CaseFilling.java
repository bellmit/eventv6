package cn.ffcs.zhsq.mybatis.domain.triadRelatedCases;

import cn.ffcs.file.mybatis.domain.attachment.AttachmentByUUID;
import cn.ffcs.zhsq.mybatis.domain.common.LayuiPage;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 涉黑案件填报表模块bo对象
 * @Author: chenshikai
 * @Date: 09-08 17:46:05
 * @Copyright: 2021 福富软件
 */
public class CaseFilling  extends LayuiPage implements  Serializable {

	private static final long serialVersionUID = 3961754709110630093L;

	private Long undCaseId; //涉黑案件ID
	private String undCaseUuid; //涉黑案件逻辑主键
	private String gridCode; //案件所属区域CODE
	private String gridCodeStr;//案件所属区域名称
	private String involvedNature; //01黑社会性质组织案件、02恶势力犯罪集团案件、03恶势力犯罪团伙案件
	private String caseName; //案件名称
	private Date filingTime; //立案时间
	private String isFinishde; //案件是否办结(1:是;0否)
	private String isDisposed; //财产是否处置到位(1:是;0否)
	private String investigationUnit; //侦查单位
	private Double freezeAssets; //查封扣押冻结财产数
	private Date referralProsecutionTime; //移送起诉时间
	private String publicProsecutionUnit; //公诉单位
	private Date complaintTime; //诉出时间
	private Integer prosecutionsNumber; //起诉人数
	private Integer judgementsNumber; //判决人数
	private Integer executionsNumber; //死刑人数
	private Integer stayExecutionNumber; //死缓人数
	private Integer indefiniteNumber; //无期人数
	private Integer otherSentencesNumber; //其他重刑人数
	private String fillStatus; //填报状态(00:草稿;01:提交)
	private String orgCode; //组织编码
	private Long creator; //创建人
	private Date createTime; //创建时间
	private Long updator; //修改人
	private Date updateTime; //修改时间
	private String isValid; //有效状态(1有效0无效)
	private String remark; //备注
	private String isSupervise;//是否督办
	private List<TrialRecord> trialRecords;//庭审记录信息
	private List<AttachmentByUUID> attList;//附件对象集合


	public Long getUndCaseId() {
		return undCaseId;
	}
	public void setUndCaseId(Long undCaseId) {
		this.undCaseId = undCaseId;
	}
	public String getUndCaseUuid() {
		return undCaseUuid;
	}
	public void setUndCaseUuid(String undCaseUuid) {
		this.undCaseUuid = undCaseUuid;
	}
	public String getGridCode() {
		return gridCode;
	}
	public void setGridCode(String gridCode) {
		this.gridCode = gridCode;
	}
	public String getInvolvedNature() {
		return involvedNature;
	}
	public void setInvolvedNature(String involvedNature) {
		this.involvedNature = involvedNature;
	}
	public String getCaseName() {
		return caseName;
	}
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
	public Date getFilingTime() {
		return filingTime;
	}
	public void setFilingTime(Date filingTime) {
		this.filingTime = filingTime;
	}
	public String getIsFinishde() {
		return isFinishde;
	}
	public void setIsFinishde(String isFinishde) {
		this.isFinishde = isFinishde;
	}
	public String getIsDisposed() {
		return isDisposed;
	}
	public void setIsDisposed(String isDisposed) {
		this.isDisposed = isDisposed;
	}
	public String getInvestigationUnit() {
		return investigationUnit;
	}
	public void setInvestigationUnit(String investigationUnit) {
		this.investigationUnit = investigationUnit;
	}
	public Double getFreezeAssets() {
		return freezeAssets;
	}
	public void setFreezeAssets(Double freezeAssets) {
		this.freezeAssets = freezeAssets;
	}
	public Date getReferralProsecutionTime() {
		return referralProsecutionTime;
	}
	public void setReferralProsecutionTime(Date referralProsecutionTime) {
		this.referralProsecutionTime = referralProsecutionTime;
	}
	public String getPublicProsecutionUnit() {
		return publicProsecutionUnit;
	}
	public void setPublicProsecutionUnit(String publicProsecutionUnit) {
		this.publicProsecutionUnit = publicProsecutionUnit;
	}
	public Date getComplaintTime() {
		return complaintTime;
	}
	public void setComplaintTime(Date complaintTime) {
		this.complaintTime = complaintTime;
	}
	public Integer getProsecutionsNumber() {
		return prosecutionsNumber;
	}
	public void setProsecutionsNumber(Integer prosecutionsNumber) {
		this.prosecutionsNumber = prosecutionsNumber;
	}
	public Integer getJudgementsNumber() {
		return judgementsNumber;
	}
	public void setJudgementsNumber(Integer judgementsNumber) {
		this.judgementsNumber = judgementsNumber;
	}
	public Integer getExecutionsNumber() {
		return executionsNumber;
	}
	public void setExecutionsNumber(Integer executionsNumber) {
		this.executionsNumber = executionsNumber;
	}
	public Integer getStayExecutionNumber() {
		return stayExecutionNumber;
	}
	public void setStayExecutionNumber(Integer stayExecutionNumber) {
		this.stayExecutionNumber = stayExecutionNumber;
	}
	public Integer getIndefiniteNumber() {
		return indefiniteNumber;
	}
	public void setIndefiniteNumber(Integer indefiniteNumber) {
		this.indefiniteNumber = indefiniteNumber;
	}
	public Integer getOtherSentencesNumber() {
		return otherSentencesNumber;
	}
	public void setOtherSentencesNumber(Integer otherSentencesNumber) {
		this.otherSentencesNumber = otherSentencesNumber;
	}
	public String getFillStatus() {
		return fillStatus;
	}
	public void setFillStatus(String fillStatus) {
		this.fillStatus = fillStatus;
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

	public List<TrialRecord> getTrialRecords() {
		return trialRecords;
	}

	public void setTrialRecords(List<TrialRecord> trialRecords) {
		this.trialRecords = trialRecords;
	}

	public List<AttachmentByUUID> getAttList() {
		return attList;
	}

	public void setAttList(List<AttachmentByUUID> attList) {
		this.attList = attList;
	}

	public String getGridCodeStr() {
		return gridCodeStr;
	}

	public void setGridCodeStr(String gridCodeStr) {
		this.gridCodeStr = gridCodeStr;
	}

	public String getIsSupervise() {
		return isSupervise;
	}

	public void setIsSupervise(String isSupervise) {
		this.isSupervise = isSupervise;
	}
}