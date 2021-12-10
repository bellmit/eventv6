package cn.ffcs.zhsq.mybatis.domain.relatedEvents;

import java.io.Serializable;
import java.util.Date;

import cn.ffcs.shequ.bo.BaseEntity;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
/**
 * 涉及业务案(事)件 T_ZZ_RELATED_EVENTS
 * @author 
 *
 */
public class RelatedEvents  extends BaseEntity  implements Serializable{

	private static final long serialVersionUID = 5468598533246926519L;
	private Long reId;					//RE_ID 主键
	private String hashId;				//RE_ID 加密主键
	private Long bizId;					//BIZ_ID 业务ID 1：护线护路；2：校园安全
	private String bizType;				//BIZ_TYPE 业务类型 1:护线护路；2：校园安全；3：重特大案（事）件；4：命案防控；
	private String bizName;				//BIZ_ID对应名称
	private String reNo;				//RE_NO 案(事)件编号
	private String reName;				//RE_NAME 案(事)件名称
	private Date occuDate;				//OCCU_DATE 发生日期
	private String occuDateStr;			//发生日期文本格式
	private String nature;				//NATURE_ 案件性质
	private String natureName;			//案件性质名称
	private String situation;			//SITUATION 案(事)件情况
	private String prisonersDocType;	//PRISONERS_DOC_TYPE 主犯(嫌疑人)证件类型
	private String prisonersDocTypeName;//主犯(嫌疑人)证件类型名称
	private String prisonersDocNo;		//PRISONERS_DOC_NO 主犯(嫌疑人)证件号码
	private String prisonersDocNoHS;		//PRISONERS_DOC_NO 主犯(嫌疑人)证件号码
	private String prisonersName;		//PRISONERS_NAME 主犯(嫌疑人)姓名
	private String isDetection;			//IS_DETECTION 是否破案
	private String isDetectionName;		//是否破案名称
	private Long crimeNum;				//CRIME_NUM 作案人数
	private Long ecapeNum;				//ECAPE_NUM 在逃人数
	private Long arrestedNum;			//ARRESTED_NUM 抓捕人数
	private String detectedOverview;	//DETECTED_OVERVIEW 案件侦破情况
	private String status;				//STATUS 状态
	private String statusName;			//状态名称
	private Date createdTime;			//CREATED_TIME 创建时间
	private String createdTimeStr;		//创建时间文本
	private Long createUserId;			//CREATED_USER_ID 创建人
	private Date updateTime;			//UPDATE_TIME 更新时间
	private String updateTimeStr;		//更新时间文本
	private Long updateUserId;			//UPDATED_USER_ID 更新人
	private String occuAddr;			//OCCU_ADDR 案发地点
	private String eventType;			//EVENT_TYPE 案件类型
	private String eventTypeCN;
	private String eventLevel;			//EVENT_LEVEL 案件分级
	private String eventLevelCN;
	private String gridCode;			//GRID_CODE 网格编码
	private String gridName;
	private String gridPath;
	private Date spyEndDate;			//SPY_END_DATE 侦查终结日期
	private String spyEndDateStr;		//侦查终结日期文本
	//--标注
    private ResMarker resMarker;
   
    
    
	public String getHashId() {
		return hashId;
	}
	public void setHashId(String hashId) {
		this.hashId = hashId;
	}
	public String getPrisonersDocNoHS() {
		return prisonersDocNoHS;
	}
	public void setPrisonersDocNoHS(String prisonersDocNoHS) {
		this.prisonersDocNoHS = prisonersDocNoHS;
	}
	public ResMarker getResMarker() {
		return resMarker;
	}
	public void setResMarker(ResMarker resMarker) {
		this.resMarker = resMarker;
	}
	public Long getReId() {
		return reId;
	}
	public void setReId(Long reId) {
		this.reId = reId;
	}
	public Long getBizId() {
		return bizId;
	}
	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getBizName() {
		return bizName;
	}
	public void setBizName(String bizName) {
		this.bizName = bizName;
	}
	public String getReNo() {
		return reNo;
	}
	public void setReNo(String reNo) {
		this.reNo = reNo;
	}
	public String getReName() {
		return reName;
	}
	public void setReName(String reName) {
		this.reName = reName;
	}
	public Date getOccuDate() {
		return occuDate;
	}
	public void setOccuDate(Date occuDate) {
		this.occuDate = occuDate;
	}
	public String getOccuDateStr() {
		return occuDateStr;
	}
	public void setOccuDateStr(String occuDateStr) {
		this.occuDateStr = occuDateStr;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public String getNatureName() {
		return natureName;
	}
	public void setNatureName(String natureName) {
		this.natureName = natureName;
	}
	public String getSituation() {
		return situation;
	}
	public void setSituation(String situation) {
		this.situation = situation;
	}
	public String getPrisonersDocType() {
		return prisonersDocType;
	}
	public void setPrisonersDocType(String prisonersDocType) {
		this.prisonersDocType = prisonersDocType;
	}
	public String getPrisonersDocTypeName() {
		return prisonersDocTypeName;
	}
	public void setPrisonersDocTypeName(String prisonersDocTypeName) {
		this.prisonersDocTypeName = prisonersDocTypeName;
	}
	public String getPrisonersDocNo() {
		return prisonersDocNo;
	}
	public void setPrisonersDocNo(String prisonersDocNo) {
		this.prisonersDocNo = prisonersDocNo;
	}
	public String getPrisonersName() {
		return prisonersName;
	}
	public void setPrisonersName(String prisonersName) {
		this.prisonersName = prisonersName;
	}
	public String getIsDetection() {
		return isDetection;
	}
	public void setIsDetection(String isDetection) {
		this.isDetection = isDetection;
	}
	public String getIsDetectionName() {
		return isDetectionName;
	}
	public void setIsDetectionName(String isDetectionName) {
		this.isDetectionName = isDetectionName;
	}
	public Long getCrimeNum() {
		return crimeNum;
	}
	public void setCrimeNum(Long crimeNum) {
		this.crimeNum = crimeNum;
	}
	public Long getEcapeNum() {
		return ecapeNum;
	}
	public void setEcapeNum(Long ecapeNum) {
		this.ecapeNum = ecapeNum;
	}
	public Long getArrestedNum() {
		return arrestedNum;
	}
	public void setArrestedNum(Long arrestedNum) {
		this.arrestedNum = arrestedNum;
	}
	public String getDetectedOverview() {
		return detectedOverview;
	}
	public void setDetectedOverview(String detectedOverview) {
		this.detectedOverview = detectedOverview;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
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
	public String getUpdateTimeStr() {
		return updateTimeStr;
	}
	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}
	public Long getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getOccuAddr() {
		return occuAddr;
	}
	public void setOccuAddr(String occuAddr) {
		this.occuAddr = occuAddr;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventLevel() {
		return eventLevel;
	}
	public void setEventLevel(String eventLevel) {
		this.eventLevel = eventLevel;
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
	public String getEventTypeCN() {
		return eventTypeCN;
	}
	public void setEventTypeCN(String eventTypeCN) {
		this.eventTypeCN = eventTypeCN;
	}
	public String getEventLevelCN() {
		return eventLevelCN;
	}
	public void setEventLevelCN(String eventLevelCN) {
		this.eventLevelCN = eventLevelCN;
	}
	public Date getSpyEndDate() {
		return spyEndDate;
	}
	public void setSpyEndDate(Date spyEndDate) {
		this.spyEndDate = spyEndDate;
	}
	public String getSpyEndDateStr() {
		return spyEndDateStr;
	}
	public void setSpyEndDateStr(String spyEndDateStr) {
		this.spyEndDateStr = spyEndDateStr;
	}

	public String getGridPath() {
		return gridPath;
	}

	public void setGridPath(String gridPath) {
		this.gridPath = gridPath;
	}
}
