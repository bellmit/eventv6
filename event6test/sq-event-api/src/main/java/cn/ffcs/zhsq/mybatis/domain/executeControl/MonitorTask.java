package cn.ffcs.zhsq.mybatis.domain.executeControl;

import cn.ffcs.zhsq.mybatis.domain.pages.Pages;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 布控任务管理模块bo对象
 * @Author: dtj
 * @Date: 07-22 17:50:39
 * @table: 表信息描述 T_MONITOR_TASK 布控任务管理  布控任务管理  序列SEQ_T_MONITOR_TASK
 * @Copyright: 2020 福富软件
 */
public class MonitorTask extends Pages implements Serializable {

  private static final long serialVersionUID = 1L;

	private Long id; //主键ID 	VARCHAR(32)
	private String taskType; //布控任务类型 	VARCHAR(24)
	private String taskTypeCN;//布控任务类型
	private String name; //布控任务名称 	VARCHAR(50)
	private String description; //布控任务描述 	VARCHAR(200)
	private String validTime; //布控任务有效期开始时间 	VARCHAR(50)
	private String invalidTime; //布控任务有效期结束时间 	VARCHAR(50)
	private String captureStartTime; //每天的抓拍开始时间 	VARCHAR(50)
	private String captureEndTime; //每天的抓拍结束时间 	VARCHAR(50)
	private String libIds; //布控库ids 	VARCHAR(500)
	private String deviceIds; //布控设备ids 	VARCHAR(500)
	private String deviceNames;//布控设备名称 VARCHAR(500)
	private String acceptAlarmUserIds; //报警消息接收者 	VARCHAR(500)
	private Integer alarmThreshold; //布控报警阈值 	NUMBER(3)
	private String alarmMode; //报警推送方式 	VARCHAR(24)
	private String repeatMode; //布控任务多重方式 	VARCHAR(24)
	private String expireType; //报警存储时间 	VARCHAR(24)
	private String isValid; //是否有效 	CHAR(1)
	private Long creator; //创建人 	NUMBER(9)
	private Date createTime; //创建时间 	DATE
	private Long updator; //更新人 	NUMBER(9)
	private Date updateTime; //更新时间 	DATE
	private Long gridId; //所属网格 	NUMBER(24)
	private String gridCode; //网格编码 	VARCHAR(24)
	private String gridName; //网格名称 	VARCHAR(24)
	private String gridPath; //网格全路径 	VARCHAR(50)
	private String controlTaskId;//布控任务ID
	private Integer queryType;//查询任务方式:默认为1,查询全部
	private Integer ignoreStatus;//是否忽略报警消息,0表示取消忽略;1代表忽略
	private Integer taskStatus;//布控任务状态(0:暂停,1:运行中,2:未运行,3:已过期,4:以删除)
	private String taskStatusCN;//布控任务状态(0:暂停,1:运行中,2:未运行,3:已过期,4:以删除)
	private String scope;//是否全选

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getTaskTypeCN() {
		return taskTypeCN;
	}

	public void setTaskTypeCN(String taskTypeCN) {
		this.taskTypeCN = taskTypeCN;
	}

	public String getTaskStatusCN() {
		return taskStatusCN;
	}

	public void setTaskStatusCN(String taskStatusCN) {
		this.taskStatusCN = taskStatusCN;
	}

	public Integer getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}
	public Integer getQueryType() {
		return queryType;
	}
	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}
	public Integer getIgnoreStatus() {
		return ignoreStatus;
	}
	public void setIgnoreStatus(Integer ignoreStatus) {
		this.ignoreStatus = ignoreStatus;
	}
	public String getDeviceNames() {
		return deviceNames;
	}
	public void setDeviceNames(String deviceNames) {
		this.deviceNames = deviceNames;
	}
	public String getControlTaskId() {
		return controlTaskId;
	}
	public void setControlTaskId(String controlTaskId) {
		this.controlTaskId = controlTaskId;
	}
	public Long getId() {  //主键ID
		return id;
	}
	public void setId(Long id) { //主键ID
		this.id = id;
	}
	public String getTaskType() {  //布控布类型
		return taskType;
	}
	public void setTaskType(String taskType) { //布控布类型
		this.taskType = taskType;
	}
	public String getName() {  //布控布名称
		return name;
	}
	public void setName(String name) { //布控布名称
		this.name = name;
	}
	public String getDescription() {  //布控布描述
		return description;
	}
	public void setDescription(String description) { //布控布描述
		this.description = description;
	}
	public String getValidTime() {  //布控任务有效期开始时间
		return validTime;
	}
	public void setValidTime(String validTime) { //布控任务有效期开始时间
		this.validTime = validTime;
	}
	public String getInvalidTime() {  //布控任务有效期结束时间
		return invalidTime;
	}
	public void setInvalidTime(String invalidTime) { //布控任务有效期结束时间
		this.invalidTime = invalidTime;
	}
	public String getCaptureStartTime() {  //每天的抓拍开始时间
		return captureStartTime;
	}
	public void setCaptureStartTime(String captureStartTime) { //每天的抓拍开始时间
		this.captureStartTime = captureStartTime;
	}
	public String getCaptureEndTime() {  //每天的抓拍结束时间
		return captureEndTime;
	}
	public void setCaptureEndTime(String captureEndTime) { //每天的抓拍结束时间
		this.captureEndTime = captureEndTime;
	}
	public String getLibIds() {  //布控库ids
		return libIds;
	}
	public void setLibIds(String libIds) { //布控库ids
		this.libIds = libIds;
	}
	public String getDeviceIds() {
		return deviceIds;
	}
	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}
	public String getAcceptAlarmUserIds() {  //报警消息接收者
		return acceptAlarmUserIds;
	}
	public void setAcceptAlarmUserIds(String acceptAlarmUserIds) { //报警消息接收者
		this.acceptAlarmUserIds = acceptAlarmUserIds;
	}
	public Integer getAlarmThreshold() {  //布控报警阈值
		return alarmThreshold;
	}
	public void setAlarmThreshold(Integer alarmThreshold) { //布控报警阈值
		this.alarmThreshold = alarmThreshold;
	}
	public String getAlarmMode() {  //报警推送方式
		return alarmMode;
	}
	public void setAlarmMode(String alarmMode) { //报警推送方式
		this.alarmMode = alarmMode;
	}
	public String getRepeatMode() {  //布控任务多重方式
		return repeatMode;
	}
	public void setRepeatMode(String repeatMode) { //布控任务多重方式
		this.repeatMode = repeatMode;
	}
	public String getExpireType() {  //报警存储时间
		return expireType;
	}
	public void setExpireType(String expireType) { //报警存储时间
		this.expireType = expireType;
	}
	public String getIsValid() {  //是否有效
		return isValid;
	}
	public void setIsValid(String isValid) { //是否有效
		this.isValid = isValid;
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
	public Long getGridId() {  //所属网格
		return gridId;
	}
	public void setGridId(Long gridId) { //所属网格
		this.gridId = gridId;
	}
	public String getGridCode() {  //网格编码
		return gridCode;
	}
	public void setGridCode(String gridCode) { //网格编码
		this.gridCode = gridCode;
	}
	public String getGridName() {  //网格名称
		return gridName;
	}
	public void setGridName(String gridName) { //网格名称
		this.gridName = gridName;
	}
	public String getGridPath() {  //网格全路径
		return gridPath;
	}
	public void setGridPath(String gridPath) { //网格全路径
		this.gridPath = gridPath;
	}


}