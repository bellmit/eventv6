package cn.ffcs.zhsq.mybatis.domain.event;

import java.io.Serializable;
import java.util.Date;

public class EventSurvey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9179151478992042519L;
	
	private Long srId;		//ID
	private String code;		//排查记录编号
	private String surveyCode;		//整改通知书编号
	private Long plaId;		//受查场所ID
	private String plaName;			//受查场所名称
	private String checkType;		//检查类型
	private String checkTypeName;	//检查内容(名称)
	private String checkContent;		//检查内容
	private String checker;		//检查负责人（姓名）
	private Date checkTime;		//检查时间
	private String checkTimeStr;
	private Date createDate;	//记录时间
	private Long createUser;	//记录人（ID）
	private String isSurvey;		//是否存在隐患
	private String isRefit;				//是否整改
	private String refitScheme;//整改措施
	private String refitLimit;//整改期限
	private String refitResult;//整改结果
	private String ponderance;	//严重性
	private String level;		//关注度
	private String status;		//状态
	private Date updateDate;		//修改时间
	private Long updateUser;		//修改人
	private String isDel;			//是否删除
	
	private String buildingId;// 楼宇ID
	private String buildingName;// 楼宇名称
	private String buildingAddre;// 楼宇地址
	private String plaAdd;// 场所地址
	private String plaType;// 场所类别
	private String plaTypeLabel;
	private Long eventId;		//事件ID
	private String plaFacilities;// 消防设施情况
	private String isElderlyPregnancy;  //是否有老弱病残孕
	private String elderlyPregnancyNum;  //老弱病残孕人数
	private String isElderlyPregnancyName;
	private String isRefitName;
	
	private String surveyDanger;			//消防隐患情况
	
	private String visitDetails;
	private Long gridId;
	private String gridName;
	
	private String gridCode;
	
	public Long getSrId() {
		return srId;
	}
	public void setSrId(Long srId) {
		this.srId = srId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSurveyCode() {
		return surveyCode;
	}
	public void setSurveyCode(String surveyCode) {
		this.surveyCode = surveyCode;
	}
	public Long getPlaId() {
		return plaId;
	}
	public void setPlaId(Long plaId) {
		this.plaId = plaId;
	}
	public String getPlaName() {
		return plaName;
	}
	public void setPlaName(String plaName) {
		this.plaName = plaName;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getCheckTypeName() {
		return checkTypeName;
	}
	public void setCheckTypeName(String checkTypeName) {
		this.checkTypeName = checkTypeName;
	}
	public String getCheckContent() {
		return checkContent;
	}
	public void setCheckContent(String checkContent) {
		this.checkContent = checkContent;
	}
	public String getChecker() {
		return checker;
	}
	public void setChecker(String checker) {
		this.checker = checker;
	}
	public Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
	public String getIsSurvey() {
		return isSurvey;
	}
	public void setIsSurvey(String isSurvey) {
		this.isSurvey = isSurvey;
	}
	public String getIsRefit() {
		return isRefit;
	}
	public void setIsRefit(String isRefit) {
		this.isRefit = isRefit;
	}
	public String getRefitScheme() {
		return refitScheme;
	}
	public void setRefitScheme(String refitScheme) {
		this.refitScheme = refitScheme;
	}
	public String getRefitLimit() {
		return refitLimit;
	}
	public void setRefitLimit(String refitLimit) {
		this.refitLimit = refitLimit;
	}
	public String getPlaTypeLabel() {
		return plaTypeLabel;
	}
	public void setPlaTypeLabel(String plaTypeLabel) {
		this.plaTypeLabel = plaTypeLabel;
	}
	public String getRefitResult() {
		return refitResult;
	}
	public void setRefitResult(String refitResult) {
		this.refitResult = refitResult;
	}
	public String getPonderance() {
		return ponderance;
	}
	public void setPonderance(String ponderance) {
		this.ponderance = ponderance;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Long getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}
	public String getIsDel() {
		return isDel;
	}
	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getBuildingAddre() {
		return buildingAddre;
	}
	public void setBuildingAddre(String buildingAddre) {
		this.buildingAddre = buildingAddre;
	}
	public String getPlaAdd() {
		return plaAdd;
	}
	public void setPlaAdd(String plaAdd) {
		this.plaAdd = plaAdd;
	}
	public String getPlaType() {
		return plaType;
	}
	public void setPlaType(String plaType) {
		this.plaType = plaType;
	}
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public String getPlaFacilities() {
		return plaFacilities;
	}
	public void setPlaFacilities(String plaFacilities) {
		this.plaFacilities = plaFacilities;
	}
	public String getIsElderlyPregnancy() {
		return isElderlyPregnancy;
	}
	public void setIsElderlyPregnancy(String isElderlyPregnancy) {
		this.isElderlyPregnancy = isElderlyPregnancy;
	}
	public String getElderlyPregnancyNum() {
		return elderlyPregnancyNum;
	}
	public void setElderlyPregnancyNum(String elderlyPregnancyNum) {
		this.elderlyPregnancyNum = elderlyPregnancyNum;
	}
	public String getIsElderlyPregnancyName() {
		return isElderlyPregnancyName;
	}
	public void setIsElderlyPregnancyName(String isElderlyPregnancyName) {
		this.isElderlyPregnancyName = isElderlyPregnancyName;
	}
	public String getIsRefitName() {
		return isRefitName;
	}
	public void setIsRefitName(String isRefitName) {
		this.isRefitName = isRefitName;
	}
	public String getSurveyDanger() {
		return surveyDanger;
	}
	public void setSurveyDanger(String surveyDanger) {
		this.surveyDanger = surveyDanger;
	}
	public String getVisitDetails() {
		return visitDetails;
	}
	public void setVisitDetails(String visitDetails) {
		this.visitDetails = visitDetails;
	}
	public Long getGridId() {
		return gridId;
	}
	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}
	public String getGridName() {
		return gridName;
	}
	public void setGridName(String gridName) {
		this.gridName = gridName;
	}
	public String getGridCode() {
		return gridCode;
	}
	public void setGridCode(String gridCode) {
		this.gridCode = gridCode;
	}
	public String getCheckTimeStr() {
		return checkTimeStr;
	}
	public void setCheckTimeStr(String checkTimeStr) {
		this.checkTimeStr = checkTimeStr;
	}
	
}
