package cn.ffcs.zhsq.mybatis.domain.executeControl;

import java.io.Serializable;

/**
 * @Description: 布控任务管理模块bo对象
 * @Author: dtj
 * @Date: 07-22 17:50:39
 * @table: 表信息描述 T_MONITOR_TASK 布控任务管理  布控任务管理  序列SEQ_T_MONITOR_TASK
 * @Copyright: 2020 福富软件
 */
public class MonitorTaskThree implements Serializable {

  private static final long serialVersionUID = 1L;

	private String id;
	private String name; //布控任务名称 	VARCHAR(50)
	private String description; //布控任务描述 	VARCHAR(200)
	private String validTime; //布控任务有效期开始时间 	VARCHAR(50)
	private String invalidTime; //布控任务有效期结束时间 	VARCHAR(50)
	private String captureStartTime; //每天的抓拍开始时间 	VARCHAR(50)
	private String captureEndTime; //每天的抓拍结束时间 	VARCHAR(50)
	private String[] libIds; //布控库ids 	VARCHAR(500)
	private String[] deviceIds; //布控设备ids 	VARCHAR(500)
	private String[] acceptAlarmUserIds; //报警消息接收者 	VARCHAR(500)
	private Integer alarmThreshold; //布控报警阈值 	NUMBER(3)
	private String alarmMode; //报警推送方式 	VARCHAR(24)
	private String repeatMode; //布控任务多重方式 	VARCHAR(24)
	private String expireType; //报警存储时间 	VARCHAR(24)

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}

	public String getInvalidTime() {
		return invalidTime;
	}

	public void setInvalidTime(String invalidTime) {
		this.invalidTime = invalidTime;
	}

	public String getCaptureStartTime() {
		return captureStartTime;
	}

	public void setCaptureStartTime(String captureStartTime) {
		this.captureStartTime = captureStartTime;
	}

	public String getCaptureEndTime() {
		return captureEndTime;
	}

	public void setCaptureEndTime(String captureEndTime) {
		this.captureEndTime = captureEndTime;
	}

	public String[] getLibIds() {
		return libIds;
	}

	public void setLibIds(String[] libIds) {
		this.libIds = libIds;
	}

	public String[] getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String[] deviceIds) {
		this.deviceIds = deviceIds;
	}

	public String[] getAcceptAlarmUserIds() {
		return acceptAlarmUserIds;
	}

	public void setAcceptAlarmUserIds(String[] acceptAlarmUserIds) {
		this.acceptAlarmUserIds = acceptAlarmUserIds;
	}

	public Integer getAlarmThreshold() {
		return alarmThreshold;
	}

	public void setAlarmThreshold(Integer alarmThreshold) {
		this.alarmThreshold = alarmThreshold;
	}

	public String getAlarmMode() {
		return alarmMode;
	}

	public void setAlarmMode(String alarmMode) {
		this.alarmMode = alarmMode;
	}

	public String getRepeatMode() {
		return repeatMode;
	}

	public void setRepeatMode(String repeatMode) {
		this.repeatMode = repeatMode;
	}

	public String getExpireType() {
		return expireType;
	}

	public void setExpireType(String expireType) {
		this.expireType = expireType;
	}
}