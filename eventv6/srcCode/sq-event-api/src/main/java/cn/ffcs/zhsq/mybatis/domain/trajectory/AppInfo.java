package cn.ffcs.zhsq.mybatis.domain.trajectory;

import java.io.Serializable;

public class AppInfo implements Serializable{

	private static final long serialVersionUID = -7408579874834147319L;

	private Long appId;
	
	private String appCode;
	
	private String appName;
	
	private byte[] appNote;
	
	private String status;

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public byte[] getAppNote() {
		return appNote;
	}

	public void setAppNote(byte[] appNote) {
		this.appNote = appNote;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
