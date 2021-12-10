package cn.ffcs.zhsq.mybatis.domain.historyInfo;

import java.io.Serializable;

/**
 * 门禁
 * @author zhangzhenhai
 * @date 2017-10-23 上午9:38:24
 */
public class AccessControlHis implements Serializable {

	private String open_door_mode;//开门方式	
	private String user_id;//用户ID 卡号,密码,微信二维码
	private String user_name;//用户姓名
	private String user_image_url;//用户图片地址
	private String in_out_status;//0：进去 1：出去 2：异常
	private String recordtime;//记录时间:yyyy-MM-dd HH:mm:ss
	public String getOpen_door_mode() {
		return open_door_mode;
	}
	public void setOpen_door_mode(String open_door_mode) {
		this.open_door_mode = open_door_mode;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_image_url() {
		return user_image_url;
	}
	public void setUser_image_url(String user_image_url) {
		this.user_image_url = user_image_url;
	}
	public String getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getIn_out_status() {
		return in_out_status;
	}
	public void setIn_out_status(String in_out_status) {
		this.in_out_status = in_out_status;
	}
	
	
}
