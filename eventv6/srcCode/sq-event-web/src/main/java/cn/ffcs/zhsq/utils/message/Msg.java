package cn.ffcs.zhsq.utils.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;

public enum Msg implements IMsg {

	BLANK("BLANK", ""), 
	ADD("ADD", "添加"), 
	EDIT("EDIT", "更新"), 
	DELETE("DELETE", "删除"), 
    INVALID("INVALID", "作废"), 
	QUERY("QUERY", "查询"), 
	OPERATE("OPERATE", "操作"), 
	SUCCESS("SUCCESS", "成功"), 
	FAILURE("FAILURE", "失败"), 
	SYMBOL("SYMBOL", "！"), 
	SYM_EER_MSG("SYM_EER_MSG", "系统出错，请联系管理员！");

	private String msg;
	private String type;

	private Msg(String type, String msg) {
		this.type = type;
		this.msg = msg;
	}

	@Override
	public String toString() {
		return this.msg;
	}

	@Override
	public ResultObj getResult(boolean isSuccess, String tipMsg) {
		return this.getResult(isSuccess, tipMsg, null);
	}

	@Override
	public ResultObj getResult(boolean isSuccess) {
		return this.getResult(isSuccess, null);
	}

	@Override
	public int getCode(boolean isSuccess) {
		return isSuccess ? 0 : 1;
	}

	@Override
	public String getMsg(boolean isSuccess) {
		return this.msg + (isSuccess ? Msg.SUCCESS : Msg.FAILURE) + Msg.SYMBOL;
	}

	@Override
	public ResultObj getResult(boolean isSuccess, Object data) {
		return this.getResult(isSuccess, null, data);
	}

	@Override
	public ResultObj getResult(boolean isSuccess, String tipMsg, Object data) {
		String _tipMsg = this.msg + (isSuccess ? Msg.SUCCESS : Msg.FAILURE);
		if (isSuccess) {
			if (StringUtils.isBlank(tipMsg)) {
				_tipMsg += Msg.SYMBOL;
			} else {
				_tipMsg += "：" + tipMsg;
			}
		} else {
			if (StringUtils.isBlank(tipMsg)) {
				_tipMsg += "：" + Msg.SYM_EER_MSG;
			} else {
				_tipMsg += "：" + tipMsg;
			}
		}
		return new ResultObj(this.type, isSuccess, _tipMsg, data);
	}

	@Override
	public ResultObj getResult() {
		return this.getResult(true);
	}

	@Override
	public ResultObj getResult(Object data) {
		return this.getResult(true, data);
	}

	@Override
	public ResultObj getResult(Exception e) {
		ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
		e.printStackTrace(new PrintWriter(buf, true));
		String tipMsg = Msg.SYM_EER_MSG.toString()
				+ "<br/><span style='cursor: pointer; color: red;' onclick='alert($(this).next().html());return false;'>查看错误信息</span><div style='display:none;'>"
				+ buf.toString() + "</div>";
		try {
			buf.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return this.getResult(false, tipMsg, null);
	}
}
