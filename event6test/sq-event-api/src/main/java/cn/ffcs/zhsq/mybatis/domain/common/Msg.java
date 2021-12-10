package cn.ffcs.zhsq.mybatis.domain.common;

/**
 * @author zhanzhenhai
 * @create 2019-01-29 10:11
 * 描述：结果消息类
 */
public class Msg {

	private String code;
	private String msg;
	private Object data;
	 
	public String getCode() {
	    return code;
	}
	   
	public void setCode(String code) {
	    this.code = code;
	}
	 
	public String getMsg() {
	    return msg;
	}
	 
	public void setMsg(String msg) {
	    this.msg = msg;
	}
	 
	 
	public static Msg success(){
	    Msg msg = new Msg();
	    msg.setCode("01");
	    msg.setMsg("成功");
	    return msg;
	}
	 
	public static Msg error(){
	    Msg msg = new Msg();
	    msg.setCode("02");
	    msg.setMsg("失败");
	    return msg;
	}
	 
	public static Msg success(String msgInfo){
		Msg msg = new Msg();
		msg.setCode("01");
		msg.setMsg(msgInfo);
		return msg;
	}
	
	public static Msg error(String msgInfo){
		Msg msg = new Msg();
		msg.setCode("02");
		msg.setMsg(msgInfo);
		return msg;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	 
}
