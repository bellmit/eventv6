package cn.ffcs.zhsq.mybatis.domain.historyInfo;

import java.io.Serializable;

/**
 * 车辆识别
 * @author zhangzhenhai
 * @date 2017-10-23 上午9:44:41
 */
public class CarBistinguishHis implements Serializable {
	
	private String car_number;//车牌号
	private String in_out_status;//进出状态 0:进，1：出
	private String recordtime;//记录时间:yyyy-MM-dd HH:mm:ss
	public String getCar_number() {
		return car_number;
	}
	public void setCar_number(String car_number) {
		this.car_number = car_number;
	}
	public String getIn_out_status() {
		return in_out_status;
	}
	public void setIn_out_status(String in_out_status) {
		this.in_out_status = in_out_status;
	}
	public String getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
	}
	
	
}
