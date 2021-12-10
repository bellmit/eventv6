package cn.ffcs.zhsq.utils.message;

public interface IMsg {
	public ResultObj getResult();
	public ResultObj getResult(boolean isSuccess);
	public ResultObj getResult(boolean isSuccess, String tipMsg);
	
	public ResultObj getResult(Object data);
	public ResultObj getResult(boolean isSuccess, Object data);
	public ResultObj getResult(boolean isSuccess, String tipMsg, Object data);
	
	public ResultObj getResult(Exception e);
	
	public String getMsg(boolean isSuccess);
	
	public int getCode(boolean isSuccess);

}
