package cn.ffcs.zhsq.mybatis.domain.trajectory;

import java.io.Serializable;

public class DeviceInfo implements Serializable{

	private static final long serialVersionUID = 3069774853892184750L;
	
	private Long deviceId;
	
	private String deviceName;
	
	private String deviceSn;
	
	private String deviceManufacture;
	
	private byte[] note;
	
	private String ownerRsName;
	
	private Long ownerRsId;
	
	private String infoOrgCode;
	
	private String Status;

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getDeviceManufacture() {
		return deviceManufacture;
	}

	public void setDeviceManufacture(String deviceManufacture) {
		this.deviceManufacture = deviceManufacture;
	}

	public byte[] getNote() {
		return note;
	}

	public void setNote(byte[] note) {
		this.note = note;
	}

	public String getOwnerRsName() {
		return ownerRsName;
	}

	public void setOwnerRsName(String ownerRsName) {
		this.ownerRsName = ownerRsName;
	}

	public Long getOwnerRsId() {
		return ownerRsId;
	}

	public void setOwnerRsId(Long ownerRsId) {
		this.ownerRsId = ownerRsId;
	}

	public String getInfoOrgCode() {
		return infoOrgCode;
	}

	public void setInfoOrgCode(String infoOrgCode) {
		this.infoOrgCode = infoOrgCode;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

}
