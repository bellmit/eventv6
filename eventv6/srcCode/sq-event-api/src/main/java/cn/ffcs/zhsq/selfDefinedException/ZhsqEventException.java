package cn.ffcs.zhsq.selfDefinedException;

/**
 * 自定义异常
 * @author zhangls
 *
 */
public class ZhsqEventException extends Exception{
	
	private static final long serialVersionUID = -3604370991437510570L;
	
	private String code;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ZhsqEventException () {
		super();
	}
	
	public ZhsqEventException (String msg) {
		super(msg);
	}
	
	public ZhsqEventException (String code,String msg) {
		super(msg);
		this.setCode(code);
	}
	
	public ZhsqEventException (Throwable cause) {
		super(cause);
	}
	
	public ZhsqEventException (String msg, Throwable cause) {
		super(msg, cause);
	}
}
