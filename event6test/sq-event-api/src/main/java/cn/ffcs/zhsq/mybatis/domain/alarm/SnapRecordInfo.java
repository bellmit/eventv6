package cn.ffcs.zhsq.mybatis.domain.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SnapRecordInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6246204085105954819L;
	private String stSnapTime;
	private String strIP;
	private String strName;
	private String equName;
	
	private String snapTime;
	private String deviceNote;
	private String name;
	private String deviceAddr;
	
	private FaceRecordInfo[] faceRecordInfos;
	
	public String getSnapTime() {
		return snapTime;
	}
	public void setSnapTime(String snapTime) {
		this.snapTime = snapTime;
	}
	public String getDeviceNote() {
		return deviceNote;
	}
	public void setDeviceNote(String deviceNote) {
		this.deviceNote = deviceNote;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeviceAddr() {
		return deviceAddr;
	}
	public void setDeviceAddr(String deviceAddr) {
		this.deviceAddr = deviceAddr;
	}
	public String getStrIP() {
		return strIP;
	}
	public void setStrIP(String strIP) {
		this.strIP = strIP;
	}
	public FaceRecordInfo[] getFaceRecordInfos() {
		return faceRecordInfos;
	}
	public void setFaceRecordInfos(FaceRecordInfo[] faceRecordInfos) {
		this.faceRecordInfos = faceRecordInfos;
	}
	public String getStSnapTime() {
		return stSnapTime;
	}
	public void setStSnapTime(String stSnapTime) {
		this.stSnapTime = stSnapTime;
	}
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public String getEquName() {
		return equName;
	}
	public void setEquName(String equName) {
		this.equName = equName;
	}
	
}
