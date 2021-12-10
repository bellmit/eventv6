package cn.ffcs.zhsq.mybatis.domain.dispute;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.ffcs.shequ.bo.BaseEntity;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import org.springframework.format.annotation.DateTimeFormat;

public class DisputeMediation extends BaseEntity implements Serializable {
    /**
	 * @文件描述: TODO
	 * @内容摘要: 
	 * @完成日期: Sep 11, 2014
	 * 修改日期			修改人
	 * Sep 11, 2014		zhongshm
	 */
	private static final long serialVersionUID = 1107170358333432896L;

	private Long mediationId;

    private String mediationCode;

    private Date acceptedDate;

    private String acceptedDateStr;

    private Date mediationDate;

    private String mediationDateStr;

    private String disputeType1;

    private String disputeTypeStr;

    private String disputeType2;
    
    private String bigType; //类别大类  导出用

	private String smallType; //类别小类   导出用

    private String disputeCondition;

    private Long orgId;

    private Integer updateId;

    private Date updateTime;

    private String status;

    private BigDecimal longitude;

    private BigDecimal latitude;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String addressColumn;

    private String petitionId;
    
    private String disputeStatus;
    
    private String disputeStatusStr;
    
    private Long gridId;
    
    private String gridName;
    
    private Double involvedAmount;//涉及金额
    
    private Long eventId;

    private  String scopeInfluenece;//影响范围

	private  String eventNature;//事件性质

    
    private String disputeEventName;//事件名称
    
    private Date happenTime;//发生时间
    
    private String happenTimeStr;//发生时间文本
    
    private String happenAddr;//发生现场
    
    private String disputeScale;//事件规模
    
    private String disputeScaleStr;//
    
    private Integer involveNum;//涉及人数
    
    private String mediationType;//调处方式

	private String mediationTypeStr;//调处方式文本
    
    private Date evaDate;//考评日期
    
    private String evaDateStr;//考评日期文本
    
    private String evaOpn;//考评意见
    
    //--add by zhongshm 2016年2月22日11:09:55
    private String involvedOrgName;//涉及单位
    
    private Date mediationDeadline;//调节时限
    
    private String mediationDeadlineStr;//
    
    private String handleDateFlag;
    
    private String involveType;//涉事类型
    
    //-------------------------------------------------扩展字段
    private String mediationResult;//调解结果
    
    private String gridCode;
    
    private String mediator;//调解人
    
    private String isSuccess;//是否成功
    
    private Long mediationResId;//
    
    private String mediationOrgName;
    
    private String mediationTel;
    
    private String hjCertNumber;//化解人身份证

	private String x;//X纬度
	private String y;//Y经度
    private String mapType;

	private Long creatorId;

	private Long disputeId;//第三方ID
	private String involvedName;//当事人名称
	private String[] imgFilePath;//图片附件路径
	private String[] soundFilePath;//录音附件路径
	private String fileType;//附件类型
	private String source;//来源
	private String accepter;//受理人

	private String attaIds;

	private String gridPath;

	//计算一键筛选字段
	private  Integer count;

	//--add by zhangtianci 2018年5月8日 11:09:55
	private String invalidReason;
	
	//--add by weitaili 2018年10月12日16:15:38
	private List<Attachment> attList = null;//附件列表
	
	private List<InvolvedPeople> involvedPeople = null;//主要当事人信息
	
	private String workOrderDetail;//工作记载_详细情况
	private String riskCode;//风险类型代码
	private String riskGrade;//风险等级代码
	private String measureDetail;//措施手段_详细情况
	
	//2019.08.21 新增字段
	private String caseAssessment;//案情评估
	private String intenseDegree;//激烈程度
	private String disputeLevel;//纠纷等级
	private String warningLevel;//预警等级
	private String disputeType3;//矛盾纠纷类型
	private String happenAddrCode;//标准地址库代码

	//2020-12-02 江西矛盾纠纷数据对接 新增字段
	/**
	 * 数据来源
	 */
	private String dataSource;
	/**
	 * 是否设为突出事件
	 */
	private String isOutStand;
	/**
	 * 涉湖类型
	 */
	private String relatedLakeType;
	/**
	 * 事件状态
	 */
	private String eventStatus;
	/**
	 * 督办部门
	 */
	private String supervisionDepartment;
	/**
	 * 包案领导信息
	 */
	private List<MediationCase> leaders;
	/**
	 * 化解责任人信息
	 */
	private List<MediationCase> persons;
	/**
	 * 流程信息
	 */
	private List<DisputeFlowInfo> flowInfo;
	
	public List<InvolvedPeople> getInvolvedPeople() {
		return involvedPeople;
	}

	public void setInvolvedPeople(List<InvolvedPeople> involvedPeople) {
		this.involvedPeople = involvedPeople;
	}

	public List<Attachment> getAttList() {
		return attList;
	}

	public void setAttList(List<Attachment> attList) {
		this.attList = attList;
	}

	public String getInvalidReason() {
		return invalidReason;
	}

	public void setInvalidReason(String invalidReason) {
		this.invalidReason = invalidReason;
	}

	public String getBigType() {
		return bigType;
	}

	public void setBigType(String bigType) {
		this.bigType = bigType;
	}

	public String getSmallType() {
		return smallType;
	}

	public void setSmallType(String smallType) {
		this.smallType = smallType;
	}
	
    public Long getMediationResId() {
		return mediationResId;
	}

	public void setMediationResId(Long mediationResId) {
		this.mediationResId = mediationResId;
	}

	public String getMediationResult() {
		return mediationResult;
	}

	public void setMediationResult(String mediationResult) {
		this.mediationResult = mediationResult;
	}

	public String getMediator() {
		return mediator;
	}

	public void setMediator(String mediator) {
		this.mediator = mediator;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Long getMediationId() {
        return mediationId;
    }

    public void setMediationId(Long mediationId) {
        this.mediationId = mediationId;
    }

    public String getMediationCode() {
        return mediationCode;
    }

    public void setMediationCode(String mediationCode) {
        this.mediationCode = mediationCode == null ? null : mediationCode.trim();
    }

    public Date getAcceptedDate() {
        return acceptedDate;
    }

    public void setAcceptedDate(Date acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public Date getMediationDate() {
        return mediationDate;
    }

    public void setMediationDate(Date mediationDate) {
        this.mediationDate = mediationDate;
    }

    public String getDisputeType1() {
        return disputeType1;
    }

    public void setDisputeType1(String disputeType1) {
        this.disputeType1 = disputeType1;
    }

    public String getDisputeCondition() {
        return disputeCondition;
    }

    public void setDisputeCondition(String disputeCondition) {
        this.disputeCondition = disputeCondition == null ? null : disputeCondition.trim();
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAddressColumn() {
        return addressColumn;
    }

    public void setAddressColumn(String addressColumn) {
        this.addressColumn = addressColumn == null ? null : addressColumn.trim();
    }

    public String getPetitionId() {
        return petitionId;
    }

    public void setPetitionId(String petitionId) {
        this.petitionId = petitionId == null ? null : petitionId.trim();
    }

	public String getDisputeStatus() {
		return disputeStatus;
	}

	public void setDisputeStatus(String disputeStatus) {
		this.disputeStatus = disputeStatus;
	}

	public Long getGridId() {
		return gridId;
	}

	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}

	public Double getInvolvedAmount() {
		return involvedAmount;
	}

	public void setInvolvedAmount(Double involvedAmount) {
		this.involvedAmount = involvedAmount;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getDisputeEventName() {
		return disputeEventName;
	}

	public void setDisputeEventName(String disputeEventName) {
		this.disputeEventName = disputeEventName;
	}

	public Date getHappenTime() {
		return happenTime;
	}

	public void setHappenTime(Date happenTime) {
		this.happenTime = happenTime;
	}

	public String getHappenTimeStr() {
		return happenTimeStr;
	}

	public void setHappenTimeStr(String happenTimeStr) {
		this.happenTimeStr = happenTimeStr;
	}

	public String getHappenAddr() {
		return happenAddr;
	}

	public void setHappenAddr(String happenAddr) {
		this.happenAddr = happenAddr;
	}

	public String getDisputeScale() {
		return disputeScale;
	}

	public void setDisputeScale(String disputeScale) {
		this.disputeScale = disputeScale;
	}

	public Integer getInvolveNum() {
		return involveNum;
	}

	public void setInvolveNum(Integer involveNum) {
		this.involveNum = involveNum;
	}

	public String getMediationType() {
		return mediationType;
	}

	public void setMediationType(String mediationType) {
		this.mediationType = mediationType;
	}

	public Date getEvaDate() {
		return evaDate;
	}

	public void setEvaDate(Date evaDate) {
		this.evaDate = evaDate;
	}

	public String getEvaDateStr() {
		return evaDateStr;
	}

	public void setEvaDateStr(String evaDateStr) {
		this.evaDateStr = evaDateStr;
	}

	public String getEvaOpn() {
		return evaOpn;
	}

	public void setEvaOpn(String evaOpn) {
		this.evaOpn = evaOpn;
	}

	public String getAcceptedDateStr() {
		return acceptedDateStr;
	}

	public void setAcceptedDateStr(String acceptedDateStr) {
		this.acceptedDateStr = acceptedDateStr;
	}

	public String getMediationDateStr() {
		return mediationDateStr;
	}

	public void setMediationDateStr(String mediationDateStr) {
		this.mediationDateStr = mediationDateStr;
	}

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public String getDisputeStatusStr() {
		return disputeStatusStr;
	}

	public void setDisputeStatusStr(String disputeStatusStr) {
		this.disputeStatusStr = disputeStatusStr;
	}

	public String getGridCode() {
		return gridCode;
	}

	public void setGridCode(String gridCode) {
		this.gridCode = gridCode;
	}

	public String getInvolvedOrgName() {
		return involvedOrgName;
	}

	public void setInvolvedOrgName(String involvedOrgName) {
		this.involvedOrgName = involvedOrgName;
	}

	public Date getMediationDeadline() {
		return mediationDeadline;
	}

	public void setMediationDeadline(Date mediationDeadline) {
		this.mediationDeadline = mediationDeadline;
	}

	public String getMediationDeadlineStr() {
		return mediationDeadlineStr;
	}

	public void setMediationDeadlineStr(String mediationDeadlineStr) {
		this.mediationDeadlineStr = mediationDeadlineStr;
	}

	public String getMediationOrgName() {
		return mediationOrgName;
	}

	public void setMediationOrgName(String mediationOrgName) {
		this.mediationOrgName = mediationOrgName;
	}

	public String getMediationTel() {
		return mediationTel;
	}

	public void setMediationTel(String mediationTel) {
		this.mediationTel = mediationTel;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getHandleDateFlag() {
		return handleDateFlag;
	}

	public void setHandleDateFlag(String handleDateFlag) {
		this.handleDateFlag = handleDateFlag;
	}

	public String getDisputeScaleStr() {
		return disputeScaleStr;
	}

	public void setDisputeScaleStr(String disputeScaleStr) {
		this.disputeScaleStr = disputeScaleStr;
	}

	public String getDisputeTypeStr() {
		return disputeTypeStr;
	}

	public void setDisputeTypeStr(String disputeTypeStr) {
		this.disputeTypeStr = disputeTypeStr;
	}

	public String getDisputeType2() {
		return disputeType2;
	}

	public void setDisputeType2(String disputeType2) {
		this.disputeType2 = disputeType2;
	}

	public String getInvolveType() {
		return involveType;
	}

	public void setInvolveType(String involveType) {
		this.involveType = involveType;
	}


	public String getInvolvedName() {
		return involvedName;
	}

	public void setInvolvedName(String involvedName) {
		this.involvedName = involvedName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String[] getSoundFilePath() {
		return soundFilePath;
	}

	public void setSoundFilePath(String[] soundFilePath) {
		this.soundFilePath = soundFilePath;
	}

	public String[] getImgFilePath() {
		return imgFilePath;
	}

	public void setImgFilePath(String[] imgFilePath) {
		this.imgFilePath = imgFilePath;
	}

	public Long getDisputeId() {
		return disputeId;
	}

	public void setDisputeId(Long disputeId) {
		this.disputeId = disputeId;
	}

	public String getAccepter() {
		return accepter;
	}

	public void setAccepter(String accepter) {
		this.accepter = accepter;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public String getMediationTypeStr() {
		return mediationTypeStr;
	}

	public void setMediationTypeStr(String mediationTypeStr) {
		this.mediationTypeStr = mediationTypeStr;
	}

	public String getAttaIds() {
		return attaIds;
	}

	public void setAttaIds(String attaIds) {
		this.attaIds = attaIds;
	}

	public String getGridPath() {
		return gridPath;
	}

	public void setGridPath(String gridPath) {
		this.gridPath = gridPath;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getScopeInfluenece() {
		return scopeInfluenece;
	}

	public void setScopeInfluenece(String scopeInfluenece) {
		this.scopeInfluenece = scopeInfluenece;
	}

	public String getEventNature() {
		return eventNature;
	}

	public void setEventNature(String eventNature) {
		this.eventNature = eventNature;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getWorkOrderDetail() {
		return workOrderDetail;
	}

	public void setWorkOrderDetail(String workOrderDetail) {
		this.workOrderDetail = workOrderDetail;
	}

	public String getRiskCode() {
		return riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getRiskGrade() {
		return riskGrade;
	}

	public void setRiskGrade(String riskGrade) {
		this.riskGrade = riskGrade;
	}

	public String getMeasureDetail() {
		return measureDetail;
	}

	public void setMeasureDetail(String measureDetail) {
		this.measureDetail = measureDetail;
	}

	public String getHjCertNumber() {
		return hjCertNumber;
	}

	public void setHjCertNumber(String hjCertNumber) {
		this.hjCertNumber = hjCertNumber;
	}

	public String getCaseAssessment() {
		return caseAssessment;
	}

	public void setCaseAssessment(String caseAssessment) {
		this.caseAssessment = caseAssessment;
	}

	public String getIntenseDegree() {
		return intenseDegree;
	}

	public void setIntenseDegree(String intenseDegree) {
		this.intenseDegree = intenseDegree;
	}

	public String getDisputeLevel() {
		return disputeLevel;
	}

	public void setDisputeLevel(String disputeLevel) {
		this.disputeLevel = disputeLevel;
	}

	public String getWarningLevel() {
		return warningLevel;
	}

	public void setWarningLevel(String warningLevel) {
		this.warningLevel = warningLevel;
	}

	public String getDisputeType3() {
		return disputeType3;
	}

	public void setDisputeType3(String disputeType3) {
		this.disputeType3 = disputeType3;
	}

	public String getHappenAddrCode() {
		return happenAddrCode;
	}

	public void setHappenAddrCode(String happenAddrCode) {
		this.happenAddrCode = happenAddrCode;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getIsOutStand() {
		return isOutStand;
	}

	public void setIsOutStand(String isOutStand) {
		this.isOutStand = isOutStand;
	}

	public String getRelatedLakeType() {
		return relatedLakeType;
	}

	public void setRelatedLakeType(String relatedLakeType) {
		this.relatedLakeType = relatedLakeType;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getSupervisionDepartment() {
		return supervisionDepartment;
	}

	public void setSupervisionDepartment(String supervisionDepartment) {
		this.supervisionDepartment = supervisionDepartment;
	}

	public List<MediationCase> getLeaders() {
		return leaders;
	}

	public void setLeaders(List<MediationCase> leaders) {
		this.leaders = leaders;
	}

	public List<MediationCase> getPersons() {
		return persons;
	}

	public void setPersons(List<MediationCase> persons) {
		this.persons = persons;
	}

	public List<DisputeFlowInfo> getFlowInfo() {
		return flowInfo;
	}

	public void setFlowInfo(List<DisputeFlowInfo> flowInfo) {
		this.flowInfo = flowInfo;
	}

	@Override
	public String toString() {
		return "DisputeMediation{" +
				"mediationId=" + mediationId +
				", mediationCode='" + mediationCode + '\'' +
				", acceptedDate=" + acceptedDate +
				", mediationDate=" + mediationDate +
				", disputeType2='" + disputeType2 + '\'' +
				", bigType='" + bigType + '\'' +
				", disputeCondition='" + disputeCondition + '\'' +
				", orgId=" + orgId +
				", status='" + status + '\'' +
				", createTime=" + createTime +
				", disputeStatus='" + disputeStatus + '\'' +
				", gridId=" + gridId +
				", gridName='" + gridName + '\'' +
				", scopeInfluenece='" + scopeInfluenece + '\'' +
				", eventNature='" + eventNature + '\'' +
				", disputeEventName='" + disputeEventName + '\'' +
				", happenTime=" + happenTime +
				", happenAddr='" + happenAddr + '\'' +
				", disputeScale='" + disputeScale + '\'' +
				", involveNum=" + involveNum +
				", mediationType='" + mediationType + '\'' +
				", evaDate=" + evaDate +
				", involvedOrgName='" + involvedOrgName + '\'' +
				", mediationDeadline=" + mediationDeadline +
				", handleDateFlag='" + handleDateFlag + '\'' +
				", involveType='" + involveType + '\'' +
				", mediationResult='" + mediationResult + '\'' +
				", gridCode='" + gridCode + '\'' +
				", mediator='" + mediator + '\'' +
				", isSuccess='" + isSuccess + '\'' +
				", mediationResId=" + mediationResId +
				", mediationOrgName='" + mediationOrgName + '\'' +
				", mediationTel='" + mediationTel + '\'' +
				", hjCertNumber='" + hjCertNumber + '\'' +
				", creatorId=" + creatorId +
				", disputeId=" + disputeId +
				", involvedName='" + involvedName + '\'' +
				", accepter='" + accepter + '\'' +
				", involvedPeople=" + involvedPeople +
				", workOrderDetail='" + workOrderDetail + '\'' +
				", riskCode='" + riskCode + '\'' +
				", riskGrade='" + riskGrade + '\'' +
				", measureDetail='" + measureDetail + '\'' +
				", caseAssessment='" + caseAssessment + '\'' +
				", intenseDegree='" + intenseDegree + '\'' +
				", disputeLevel='" + disputeLevel + '\'' +
				", warningLevel='" + warningLevel + '\'' +
				", disputeType3='" + disputeType3 + '\'' +
				", happenAddrCode='" + happenAddrCode + '\'' +
				", dataSource='" + dataSource + '\'' +
				", isOutStand='" + isOutStand + '\'' +
				", relatedLakeType='" + relatedLakeType + '\'' +
				", eventStatus='" + eventStatus + '\'' +
				", supervisionDepartment='" + supervisionDepartment + '\'' +
				", leaders=" + leaders +
				", persons=" + persons +
				'}';
	}
}