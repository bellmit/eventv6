package cn.ffcs.zhsq.utils.message;

import java.io.Serializable;

public class ResultObj implements Serializable {
	
	private static final long serialVersionUID = 6159559600632892290L;
	private boolean success;
	private String tipMsg;
	private Object data;
	private String type;

	public ResultObj(String tipMsg, Object data) {
		this.success = true;
		this.tipMsg = tipMsg;
		this.data = data;
	}
	
	public ResultObj(String type, String tipMsg, Object data) {
		this.type = type;
		this.success = true;
		this.tipMsg = tipMsg;
		this.data = data;
	}
	
	public ResultObj(String type, boolean success, String tipMsg, Object data) {
		this.type = type;
		this.success = success;
		this.tipMsg = tipMsg;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getTipMsg() {
		return tipMsg;
	}

	public void setTipMsg(String tipMsg) {
		this.tipMsg = tipMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}