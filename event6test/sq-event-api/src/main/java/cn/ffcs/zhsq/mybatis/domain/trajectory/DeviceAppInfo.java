package cn.ffcs.zhsq.mybatis.domain.trajectory;

import java.io.Serializable;
import java.sql.Date;

public class DeviceAppInfo implements Serializable{

	private static final long serialVersionUID = -6301354346827482771L;
	
	private Long appDeviceId;
	
	private String deviceId;
	
	private String appCode;
	
	private String sourceIp;
	
	private String targetIp;
	
	private String commandType;
	
	private String dataType;
	
	private Date createTime;
	
	private byte[] note;
	
	private String status;

	public Long getAppDeviceId() {
		return appDeviceId;
	}

	public void setAppDeviceId(Long appDeviceId) {
		this.appDeviceId = appDeviceId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public String getTargetIp() {
		return targetIp;
	}

	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
	}

	public String getCommandType() {
		return commandType;
	}

	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public byte[] getNote() {
		return note;
	}

	public void setNote(byte[] note) {
		this.note = note;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

}
