package cn.ffcs.zhsq.mybatis.domain.administrationPenalty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 张天慈 on 2017/12/5.
 */
public class AdministrationPenalty implements Serializable {
	private Long penaltyId; //处罚信息ID
	private String registrationId; //工商注册号
	private String enterpriseName; //企业名称
	private String enterpriseAddress; //企业地址
	private String decisionInstrument; //决定文书号
	private String penaltyCause; //处罚事由
	private String penaltyBases; //处罚依据
	private String penaltyConclusion; //处罚结论
	private Date	penaltyDate; //处罚日期
	private String penaltyDateStr; //
	private Long syear; //统计年
	private Long smonth;//统计月
	private Long   creator;//创建人
	private Date 	createdTime;//创建时间
	private String	createdTimeStr;//
	private String creatorName;//创建人姓名
	private Long	updater; //修改人
	private Date	updatedTime;//修改时间
	private String updatedTimeStr;//
	private String updaterName;//修改人姓名
	private String regionCode;//区域编码
	private String status;//状态值：0无效，有效

	private Long gridId;//网格ID
	private String gridCode;//网格编码
	private String gridName;//网格名称
	private String gridPath;//

	public AdministrationPenalty() {
	}

	public AdministrationPenalty(Long penaltyId, String registrationId, String enterpriseName, String enterpriseAddress, String decisionInstrument, String penaltyCause, String penaltyBases, String penaltyConclusion, Date penaltyDate, String penaltyDateStr, Long syear, Long smonth, Long creator, Date createdTime, String createdTimeStr, String creatorName, Long updater, Date updatedTime, String updatedTimeStr, String updaterName, String regionCode, String status, Long gridId, String gridCode, String gridName, String gridPath) {
		this.penaltyId = penaltyId;
		this.registrationId = registrationId;
		this.enterpriseName = enterpriseName;
		this.enterpriseAddress = enterpriseAddress;
		this.decisionInstrument = decisionInstrument;
		this.penaltyCause = penaltyCause;
		this.penaltyBases = penaltyBases;
		this.penaltyConclusion = penaltyConclusion;
		this.penaltyDate = penaltyDate;
		this.penaltyDateStr = penaltyDateStr;
		this.syear = syear;
		this.smonth = smonth;
		this.creator = creator;
		this.createdTime = createdTime;
		this.createdTimeStr = createdTimeStr;
		this.creatorName = creatorName;
		this.updater = updater;
		this.updatedTime = updatedTime;
		this.updatedTimeStr = updatedTimeStr;
		this.updaterName = updaterName;
		this.regionCode = regionCode;
		this.status = status;
		this.gridId = gridId;
		this.gridCode = gridCode;
		this.gridName = gridName;
		this.gridPath = gridPath;
	}

	public Long getPenaltyId() {
		return penaltyId;
	}

	public void setPenaltyId(Long penaltyId) {
		this.penaltyId = penaltyId;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getEnterpriseAddress() {
		return enterpriseAddress;
	}

	public void setEnterpriseAddress(String enterpriseAddress) {
		this.enterpriseAddress = enterpriseAddress;
	}

	public String getDecisionInstrument() {
		return decisionInstrument;
	}

	public void setDecisionInstrument(String decisionInstrument) {
		this.decisionInstrument = decisionInstrument;
	}

	public String getPenaltyCause() {
		return penaltyCause;
	}

	public void setPenaltyCause(String penaltyCause) {
		this.penaltyCause = penaltyCause;
	}

	public String getPenaltyBases() {
		return penaltyBases;
	}

	public void setPenaltyBases(String penaltyBases) {
		this.penaltyBases = penaltyBases;
	}

	public String getPenaltyConclusion() {
		return penaltyConclusion;
	}

	public void setPenaltyConclusion(String penaltyConclusion) {
		this.penaltyConclusion = penaltyConclusion;
	}

	public Date getPenaltyDate() {
		return penaltyDate;
	}

	public void setPenaltyDate(Date penaltyDate) {
		this.penaltyDate = penaltyDate;
	}

	public String getPenaltyDateStr() {
		return penaltyDateStr;
	}

	public void setPenaltyDateStr(String penaltyDateStr) {
		this.penaltyDateStr = penaltyDateStr;
	}

	public Long getSyear() {
		return syear;
	}

	public void setSyear(Long syear) {
		this.syear = syear;
	}

	public Long getSmonth() {
		return smonth;
	}

	public void setSmonth(Long smonth) {
		this.smonth = smonth;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getCreatedTimeStr() {
		return createdTimeStr;
	}

	public void setCreatedTimeStr(String createdTimeStr) {
		this.createdTimeStr = createdTimeStr;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Long getUpdater() {
		return updater;
	}

	public void setUpdater(Long updater) {
		this.updater = updater;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getUpdatedTimeStr() {
		return updatedTimeStr;
	}

	public void setUpdatedTimeStr(String updatedTimeStr) {
		this.updatedTimeStr = updatedTimeStr;
	}

	public String getUpdaterName() {
		return updaterName;
	}

	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getGridId() {
		return gridId;
	}

	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}

	public String getGridCode() {
		return gridCode;
	}

	public void setGridCode(String gridCode) {
		this.gridCode = gridCode;
	}

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public String getGridPath() {
		return gridPath;
	}

	public void setGridPath(String gridPath) {
		this.gridPath = gridPath;
	}

	@Override
	public String toString() {
		return "AdministrationPenalty{" +
				"penaltyId=" + penaltyId +
				", registrationId='" + registrationId + '\'' +
				", enterpriseName='" + enterpriseName + '\'' +
				", enterpriseAddress='" + enterpriseAddress + '\'' +
				", decisionInstrument='" + decisionInstrument + '\'' +
				", penaltyCause='" + penaltyCause + '\'' +
				", penaltyBases='" + penaltyBases + '\'' +
				", penaltyConclusion='" + penaltyConclusion + '\'' +
				", penaltyDate=" + penaltyDate +
				", penaltyDateStr='" + penaltyDateStr + '\'' +
				", syear=" + syear +
				", smonth=" + smonth +
				", creator=" + creator +
				", createdTime=" + createdTime +
				", createdTimeStr='" + createdTimeStr + '\'' +
				", creatorName='" + creatorName + '\'' +
				", updater=" + updater +
				", updatedTime=" + updatedTime +
				", updatedTimeStr='" + updatedTimeStr + '\'' +
				", updaterName='" + updaterName + '\'' +
				", regionCode='" + regionCode + '\'' +
				", status='" + status + '\'' +
				", gridId=" + gridId +
				", gridCode='" + gridCode + '\'' +
				", gridName='" + gridName + '\'' +
				", gridPath='" + gridPath + '\'' +
				'}';
	}
}
