package cn.ffcs.zhsq.mybatis.domain.historyInfo;

import java.io.Serializable;
/**
 * 摄像头
 * @author zhangzhenhai
 * @date 2017-10-23 上午9:23:47
 */
public class CameraHis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8344535754478827543L;
	private String user;//用户
	private String user_type;//人员类型 0：临时 ，1：固定
	private String house_floor;//户主楼层
	private String in_out_status;//进出状态 0:进，1：出
	private String user_image_url;//最新人员图片地址
	private String recordtime;//记录时间:yyyy-MM-dd HH:mm:ss
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getHouse_floor() {
		return house_floor;
	}
	public void setHouse_floor(String house_floor) {
		this.house_floor = house_floor;
	}
	public String getIn_out_status() {
		return in_out_status;
	}
	public void setIn_out_status(String in_out_status) {
		this.in_out_status = in_out_status;
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
	
	
}
