package cn.ffcs.zhsq.mybatis.domain.eventPush;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 事件推送模块bo对象
 * @Author: os.wuzhj
 * @Date: 06-10 09:13:18
 * @table: 表信息描述 T_EVENT_PUSH 事件推送    序列SEQ_T_EVENT_PUSH
 * @Copyright: 2019 福富软件
 */
public class EventPush implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id; //主键ID 	NUMBER(9)
	private Long eventId; //事件ID 	NUMBER(9)
	private Long creator; //创建人 	NUMBER(9)
	private String createName; //创建人姓名 	VARCHAR2(50)
	private Date createTime; //创建时间 	DATE
	private Long updator; //更新人 	NUMBER(9)
	private Date updateTime; //更新时间 	DATE
	private String isValid; //是否有效 	CHAR(1)
	private String remark; //备注 	VARCHAR2(500)
	
	
	private String eventName; //事件标题
	private String type;//事件分类
	private String typeCN;//事件分类CN
	private String gridPath;//所属网格
	private String status;//当前状态
	private String statusCN;//当前状态CN
	private String createTimeStr;//采集时间
	private String bizPlatform;
	private Long instanceId;
	private Long workflowId;
	private Date happenTime; //事发时间
	private Date handleTime; //办结期限
	private String superViseMark; //事件督办标记 1为督办状态
	private String content;//事件内容
	private String handleDateFlag;
	
	public Long getId() {  //主键ID
		return id;
	}
	public void setId(Long id) { //主键ID
		this.id = id;
	}
	public Long getEventId() {  //事件ID
		return eventId;
	}
	public void setEventId(Long eventId) { //事件ID
		this.eventId = eventId;
	}
	public Long getCreator() {  //创建人
		return creator;
	}
	public void setCreator(Long creator) { //创建人
		this.creator = creator;
	}
	public Date getCreateTime() {  //创建时间
		return createTime;
	}
	public void setCreateTime(Date createTime) { //创建时间
		this.createTime = createTime;
	}
	public Long getUpdator() {  //更新人
		return updator;
	}
	public void setUpdator(Long updator) { //更新人
		this.updator = updator;
	}
	public Date getUpdateTime() {  //更新时间
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) { //更新时间
		this.updateTime = updateTime;
	}
	public String getIsValid() {  //是否有效
		return isValid;
	}
	public void setIsValid(String isValid) { //是否有效
		this.isValid = isValid;
	}
	public String getRemark() {  //备注
		return remark;
	}
	public void setRemark(String remark) { //备注
		this.remark = remark;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getGridPath() {
		return gridPath;
	}
	public void setGridPath(String gridPath) {
		this.gridPath = gridPath;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeCN() {
		return typeCN;
	}
	public void setTypeCN(String typeCN) {
		this.typeCN = typeCN;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusCN() {
		return statusCN;
	}
	public void setStatusCN(String statusCN) {
		this.statusCN = statusCN;
	}
	public String getBizPlatform() {
		return bizPlatform;
	}
	public void setBizPlatform(String bizPlatform) {
		this.bizPlatform = bizPlatform;
	}
	public Long getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}
	public Long getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}
	public Date getHappenTime() {
		return happenTime;
	}
	public void setHappenTime(Date happenTime) {
		this.happenTime = happenTime;
	}
	public Date getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getSuperViseMark() {
		return superViseMark;
	}
	public void setSuperViseMark(String superViseMark) {
		this.superViseMark = superViseMark;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getHandleDateFlag() {
		return handleDateFlag;
	}
	public void setHandleDateFlag(String handleDateFlag) {
		this.handleDateFlag = handleDateFlag;
	}
}