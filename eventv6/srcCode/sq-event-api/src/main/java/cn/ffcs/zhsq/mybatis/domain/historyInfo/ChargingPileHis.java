package cn.ffcs.zhsq.mybatis.domain.historyInfo;

import java.io.Serializable;


/**
 * 充电桩历史信息实体
 * @author zhangzhenhai
 * @date 2017-10-23 上午9:03:21
 */
public class ChargingPileHis implements Serializable {
	
	private static final long serialVersionUID = 886545637012511353L;
	private String host_status;//主机状态 0：在线，1：离线
	private String free_port;//空闲端口
	private String use_port;//在用端口
	private String fault_port;//故障端口
	private String total_port;//总端口
	private String recordtime;//记录时间:yyyy-MM-dd HH:mm:ss
	public String getHost_status() {
		return host_status;
	}
	public void setHost_status(String host_status) {
		this.host_status = host_status;
	}
	public String getFree_port() {
		return free_port;
	}
	public void setFree_port(String free_port) {
		this.free_port = free_port;
	}
	public String getUse_port() {
		return use_port;
	}
	public void setUse_port(String use_port) {
		this.use_port = use_port;
	}
	public String getFault_port() {
		return fault_port;
	}
	public void setFault_port(String fault_port) {
		this.fault_port = fault_port;
	}
	public String getTotal_port() {
		return total_port;
	}
	public void setTotal_port(String total_port) {
		this.total_port = total_port;
	}
	public String getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
	}
	
	
}
