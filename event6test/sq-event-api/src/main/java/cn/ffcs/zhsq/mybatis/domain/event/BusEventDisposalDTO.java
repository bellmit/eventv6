package cn.ffcs.zhsq.mybatis.domain.event;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import cn.ffcs.shequ.mybatis.domain.zzgl.event.TaskInfo;
import cn.ffcs.zhsq.mybatis.domain.crowd.VisitRecord;


public class BusEventDisposalDTO implements Serializable {
	
	private static final long serialVersionUID = 8675457448066236887L;
	private Integer eventId;// 事件标识
	private String code;// 事件编号
	private String type;// 事件分类
	private String typeName;// 事件分类名称
	private String reporter;// 事件反馈人员
	private String content;// 事件描述
	private String occurred;// 发生地
	private String happenTime;// 发生时间
	private String dimensions = "0.000000";// 经度
	private String longitude = "0.000000";// 纬度
	private String buildingId;// 楼宇ID
	private String buildingName;// 楼宇名称
	private String gridCode;// 网格编号
	private String gridName;// 网格名称
	private String source;// 信息来源（目击、举报）
	private String sourceName;// 信息来源（目击、举报）名称
	private Integer involvedNum;// 涉及的人数
	private String urgencyDegree; // 紧急程度
	private String results;// 处理结果
	private Integer updater;// 修改人ID
	private String updaterName;// 修改人姓名
	private Date updated;// 修改时间
	private String updatedToStr;// 修改时间
	private Integer register;// 登记人
	private String registerName;//登记人姓名
	private String registerPhone;//登记人电话
	private Date registerTime;// 登记时间
	private String registerTimeToStr;// 登记时间
	private String isDel;// 标志
	private String   closed;	// 结案时间
	private int closer;	// 结案人员
	private String closeName;	// 结案人员
	private String firstImg;			//处理前图片
	private String lastImg;				//处理后图片
	private String handleDate;			//处理时间
	private String reporteTelephone;	//反馈人员电话号码
	private transient String spEventTypeName;			//事件类型大类中文名称
	private String evaluateFlag;//评价标识
	private transient String involvedNumName;//涉及人数中文名称：1:单人; 2:双人; 9:多人
	private String collectWay;
	private String oppoSideBusiCode;	//交互系统事件唯一性标识
	private String status;
	private Long gridId;

	/*******************重点人员管理*****************/
	private VisitRecord visitRecord = new VisitRecord();
	/*******************出租户管理表***************/
	private EventRent eventRent = new EventRent();
	/*******************消防安全表***************/
	private EventSurvey eventSurvey = new EventSurvey();
	
	private Set<TaskInfo> eventDisposalTaskOMs = new HashSet<TaskInfo>();
	
	public Set<TaskInfo> getEventDisposalTaskOMs() {
		return eventDisposalTaskOMs;
	}
	public void setEventDisposalTaskOMs(Set<TaskInfo> eventDisposalTaskOMs) {
		this.eventDisposalTaskOMs = eventDisposalTaskOMs;
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getReporter() {
		return reporter;
	}
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOccurred() {
		return occurred;
	}
	public void setOccurred(String occurred) {
		this.occurred = occurred;
	}
	public String getHappenTime() {
		return happenTime;
	}
	public void setHappenTime(String happenTime) {
		this.happenTime = happenTime;
	}
	public String getDimensions() {
		return dimensions;
	}
	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public Integer getInvolvedNum() {
		return involvedNum;
	}
	public void setInvolvedNum(Integer involvedNum) {
		this.involvedNum = involvedNum;
	}
	public String getUrgencyDegree() {
		return urgencyDegree;
	}
	public void setUrgencyDegree(String urgencyDegree) {
		this.urgencyDegree = urgencyDegree;
	}
	public String getResults() {
		return results;
	}
	public void setResults(String results) {
		this.results = results;
	}
	public Integer getUpdater() {
		return updater;
	}
	public void setUpdater(Integer updater) {
		this.updater = updater;
	}
	public String getUpdaterName() {
		return updaterName;
	}
	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public String getUpdatedToStr() {
		return updatedToStr;
	}
	public void setUpdatedToStr(String updatedToStr) {
		this.updatedToStr = updatedToStr;
	}
	public Integer getRegister() {
		return register;
	}
	public void setRegister(Integer register) {
		this.register = register;
	}
	public String getRegisterName() {
		return registerName;
	}
	public void setRegisterName(String registerName) {
		this.registerName = registerName;
	}
	public String getRegisterPhone() {
		return registerPhone;
	}
	public void setRegisterPhone(String registerPhone) {
		this.registerPhone = registerPhone;
	}
	public Date getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	public String getRegisterTimeToStr() {
		return registerTimeToStr;
	}
	public void setRegisterTimeToStr(String registerTimeToStr) {
		this.registerTimeToStr = registerTimeToStr;
	}
	public String getIsDel() {
		return isDel;
	}
	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}
	public String getClosed() {
		return closed;
	}
	public void setClosed(String closed) {
		this.closed = closed;
	}
	public int getCloser() {
		return closer;
	}
	public void setCloser(int closer) {
		this.closer = closer;
	}
	public String getCloseName() {
		return closeName;
	}
	public void setCloseName(String closeName) {
		this.closeName = closeName;
	}
	public String getFirstImg() {
		return firstImg;
	}
	public void setFirstImg(String firstImg) {
		this.firstImg = firstImg;
	}
	public String getLastImg() {
		return lastImg;
	}
	public void setLastImg(String lastImg) {
		this.lastImg = lastImg;
	}
	public String getHandleDate() {
		return handleDate;
	}
	public void setHandleDate(String handleDate) {
		this.handleDate = handleDate;
	}
	public String getReporteTelephone() {
		return reporteTelephone;
	}
	public void setReporteTelephone(String reporteTelephone) {
		this.reporteTelephone = reporteTelephone;
	}
	public String getSpEventTypeName() {
		return spEventTypeName;
	}
	public void setSpEventTypeName(String spEventTypeName) {
		this.spEventTypeName = spEventTypeName;
	}
	public String getEvaluateFlag() {
		return evaluateFlag;
	}
	public void setEvaluateFlag(String evaluateFlag) {
		this.evaluateFlag = evaluateFlag;
	}
	public String getInvolvedNumName() {
		return involvedNumName;
	}
	public void setInvolvedNumName(String involvedNumName) {
		this.involvedNumName = involvedNumName;
	}
	public String getCollectWay() {
		return collectWay;
	}
	public void setCollectWay(String collectWay) {
		this.collectWay = collectWay;
	}
	public String getOppoSideBusiCode() {
		return oppoSideBusiCode;
	}
	public void setOppoSideBusiCode(String oppoSideBusiCode) {
		this.oppoSideBusiCode = oppoSideBusiCode;
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
	public VisitRecord getVisitRecord() {
		return visitRecord;
	}
	public void setVisitRecord(VisitRecord visitRecord) {
		this.visitRecord = visitRecord;
	}
	public EventRent getEventRent() {
		return eventRent;
	}
	public void setEventRent(EventRent eventRent) {
		this.eventRent = eventRent;
	}
	public EventSurvey getEventSurvey() {
		return eventSurvey;
	}
	public void setEventSurvey(EventSurvey eventSurvey) {
		this.eventSurvey = eventSurvey;
	}
	
}
