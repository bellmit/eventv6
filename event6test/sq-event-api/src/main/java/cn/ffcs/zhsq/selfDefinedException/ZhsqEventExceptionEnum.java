package cn.ffcs.zhsq.selfDefinedException;

public enum ZhsqEventExceptionEnum {
	
	OVERREPORTING_EXCEPTION("10001","上报失败：每人每月的交叉巡防事件上报数不能超过10条！"),
	PATROLTYPE_EXCEPTION("10002","未查询到事件的巡访类型！"),
	NOT_GRID_LEVEL_REGION_EXCEPTION("10003", "所属区域请精确到二级网格！");//地域非网格层级业务异常
	
	private String code;
	private String message;
	
	private ZhsqEventExceptionEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ZhsqEventException getZhsqEventException() {
		return new ZhsqEventException(this.code, this.message);
	}

}
