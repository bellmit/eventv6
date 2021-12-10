package cn.ffcs.zhsq.mybatis.domain.eventCase;

import java.io.Serializable;
import java.util.Date;

/**
 * 案件 江西省罗坊镇
 * @author zhangls
 *
 */
public class EventCase implements Serializable{
	
	private static final long serialVersionUID = 1L;

	//案件字段T_EVENT_CASE
	private Long caseId;//案件ID  CASE_ID
	
	private String code;//案件编码  CODE_
	
	private String caseName;//案件标题 CASE_NAME
	
	private String type;//案件类型  TYPE_，字典编码：A001093199
	
	private String typeName;//案件类型名称 TYPE_NAME
	
	private String eventClass;//案件分类
	
	private String content;//案件描述  CONTENT_
	
	private String occurred;//案发详址  OCCURRED
	
	private Date happenTime;//案发时间 HAPPEN_TIME

	private Date endTime;//结案时间 END_TIME
	
	private String happenTimeStr;//案发时间文本
	
	private String tel;//联系电话  TELEPHONE
	
	private String contactUser;  //(联系人姓名、当事人姓名) CONTACT_USER
	
	private Long gridId;//网格ID
	
	private String gridName;//网格名称

	private String infoOrgCode;//信息域
	
	private String gridPath;//网格名称全路径
	
	private String source;//信息来源  SOURCE 目击(01)、举报(02)，字典编码：A001093222
	
	private String sourceName;//信息来源名称 SOURCE_NAME 
	
	private String collectWay;//采集渠道 COLLECT_WAY，字典编码：A001093096  01表示案件由手机端采集，02表示案件由PC端采集
	
	private String collectWayName;//采集渠道名称
	
	private String involvedNum;//涉及人数 INVOLVED_NUM，字典编码：A001093270
	
	private String involvedNumName;//涉及人数名称
	
	private String involvedPersonName;//涉及人员姓名 INVOLVED_PERSION
	
	private String influenceDegree;//影响范围  INFLUENCE_DEGREE，字典编码：A001093094
	
	private String influenceDegreeName;//影响范围名称 INFLUENCE_DEGREE_NAME
	
	private String urgencyDegree;//紧急程度 URGENCY_DEGREE，字典编码：A001093271
	
	private String urgencyDegreeName;//紧急程度名称 URGENCY_DEGREE_NAME
	
	private String status;//案件状态:草稿(99)、已上报(01),已删除(06)，字典编码：A001093095
	
	private String statusName;//状态文本  STATUS_NAME
	
	private Long creatorId;//创建人  CREATOR_ID
	
	private Date createTime;//创建时间  CREATE_TIME
	
	private String createTimeStr;//创建时间文本
	
	private Long updaterId;//更新人员 UPDATER_ID
	
	private Date updateTime;//更新时间  UPDATE_TIME

	public Long getCaseId() {
		return caseId;
	}

	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
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

	public String getEventClass() {
		return eventClass;
	}

	public void setEventClass(String eventClass) {
		this.eventClass = eventClass;
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

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getContactUser() {
		return contactUser;
	}

	public void setContactUser(String contactUser) {
		this.contactUser = contactUser;
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

	public String getInfoOrgCode() {
		return infoOrgCode;
	}

	public void setInfoOrgCode(String infoOrgCode) {
		this.infoOrgCode = infoOrgCode;
	}

	public String getGridPath() {
		return gridPath;
	}

	public void setGridPath(String gridPath) {
		this.gridPath = gridPath;
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

	public String getCollectWay() {
		return collectWay;
	}

	public void setCollectWay(String collectWay) {
		this.collectWay = collectWay;
	}

	public String getCollectWayName() {
		return collectWayName;
	}

	public void setCollectWayName(String collectWayName) {
		this.collectWayName = collectWayName;
	}

	public String getInvolvedNum() {
		return involvedNum;
	}

	public void setInvolvedNum(String involvedNum) {
		this.involvedNum = involvedNum;
	}

	public String getInvolvedNumName() {
		return involvedNumName;
	}

	public void setInvolvedNumName(String involvedNumName) {
		this.involvedNumName = involvedNumName;
	}

	public String getInvolvedPersonName() {
		return involvedPersonName;
	}

	public void setInvolvedPersonName(String involvedPersonName) {
		this.involvedPersonName = involvedPersonName;
	}

	public String getInfluenceDegree() {
		return influenceDegree;
	}

	public void setInfluenceDegree(String influenceDegree) {
		this.influenceDegree = influenceDegree;
	}

	public String getInfluenceDegreeName() {
		return influenceDegreeName;
	}

	public void setInfluenceDegreeName(String influenceDegreeName) {
		this.influenceDegreeName = influenceDegreeName;
	}

	public String getUrgencyDegree() {
		return urgencyDegree;
	}

	public void setUrgencyDegree(String urgencyDegree) {
		this.urgencyDegree = urgencyDegree;
	}

	public String getUrgencyDegreeName() {
		return urgencyDegreeName;
	}

	public void setUrgencyDegreeName(String urgencyDegreeName) {
		this.urgencyDegreeName = urgencyDegreeName;
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

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public Long getUpdaterId() {
		return updaterId;
	}

	public void setUpdaterId(Long updaterId) {
		this.updaterId = updaterId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
