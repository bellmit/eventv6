package cn.ffcs.zhsq.mybatis.domain.faithfulEnterprise;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 张天慈 on 2017/12/20.
 */
public class FaithfulEnterprise implements Serializable {

	private Long enterpriseId;
	//工商注册号
	private String registrationId;
	//企业名称
	private String enterpriseName;
	//	企业地址
	private String enterpriseAddress;
	//认定时间
	private Date evaluationTime;
	private String evaluationTimeStr;
	//认定级别
	private String evaluationLevel;
	//创建人ID
	private Long creator;
	//创建人姓名
	private String creatorName;
	//创建时间
	private Date createdTime;
	private String createdTimeStr;
	//修改人ID
	private Long updater;
	//修改人姓名
	private String updaterName;
	//修改时间
	private Date updatedTime;
	private String updatedTimeStr;
	//企业定位信息(经度)
	private String longitude;
	//企业定位信息(纬度)
	private String latitude;
	//企业区域编码
	private String regionCode;
	//企业状态信息：0-移除，1-可用
	private String status;

	//所属网格
	private Long gridId;
	//网格编码
	private String gridCode;
	//网格名称
	private String gridName;
	//网格名称
	private String gridPath;

	public FaithfulEnterprise() {
	}

	public FaithfulEnterprise(Long enterpriseId, String registrationId, String enterpriseName, String enterpriseAddress, Date evaluationTime, String evaluationTimeStr, String evaluationLevel, Long creator, String creatorName, Date createdTime, String createdTimeStr, Long updater, String updaterName, Date updatedTime, String updatedTimeStr, String longitude, String latitude, String regionCode, String status, Long gridId, String gridCode, String gridName, String gridPath) {
		this.enterpriseId = enterpriseId;
		this.registrationId = registrationId;
		this.enterpriseName = enterpriseName;
		this.enterpriseAddress = enterpriseAddress;
		this.evaluationTime = evaluationTime;
		this.evaluationTimeStr = evaluationTimeStr;
		this.evaluationLevel = evaluationLevel;
		this.creator = creator;
		this.creatorName = creatorName;
		this.createdTime = createdTime;
		this.createdTimeStr = createdTimeStr;
		this.updater = updater;
		this.updaterName = updaterName;
		this.updatedTime = updatedTime;
		this.updatedTimeStr = updatedTimeStr;
		this.longitude = longitude;
		this.latitude = latitude;
		this.regionCode = regionCode;
		this.status = status;
		this.gridId = gridId;
		this.gridCode = gridCode;
		this.gridName = gridName;
		this.gridPath = gridPath;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
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

	public Date getEvaluationTime() {
		return evaluationTime;
	}

	public void setEvaluationTime(Date evaluationTime) {
		this.evaluationTime = evaluationTime;
	}

	public String getEvaluationTimeStr() {
		return evaluationTimeStr;
	}

	public void setEvaluationTimeStr(String evaluationTimeStr) {
		this.evaluationTimeStr = evaluationTimeStr;
	}

	public String getEvaluationLevel() {
		return evaluationLevel;
	}

	public void setEvaluationLevel(String evaluationLevel) {
		this.evaluationLevel = evaluationLevel;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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

	public Long getUpdater() {
		return updater;
	}

	public void setUpdater(Long updater) {
		this.updater = updater;
	}

	public String getUpdaterName() {
		return updaterName;
	}

	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
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

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
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
		return "FaithfulEnterprise{" +
				"enterpriseId=" + enterpriseId +
				", registrationId='" + registrationId + '\'' +
				", enterpriseName='" + enterpriseName + '\'' +
				", enterpriseAddress='" + enterpriseAddress + '\'' +
				", evaluationTime=" + evaluationTime +
				", evaluationTimeStr='" + evaluationTimeStr + '\'' +
				", evaluationLevel='" + evaluationLevel + '\'' +
				", creator=" + creator +
				", creatorName='" + creatorName + '\'' +
				", createdTime=" + createdTime +
				", createdTimeStr='" + createdTimeStr + '\'' +
				", updater=" + updater +
				", updaterName='" + updaterName + '\'' +
				", updatedTime=" + updatedTime +
				", updatedTimeStr='" + updatedTimeStr + '\'' +
				", longitude='" + longitude + '\'' +
				", latitude='" + latitude + '\'' +
				", regionCode='" + regionCode + '\'' +
				", status='" + status + '\'' +
				", gridId=" + gridId +
				", gridCode='" + gridCode + '\'' +
				", gridName='" + gridName + '\'' +
				", gridPath='" + gridPath + '\'' +
				'}';
	}
}
