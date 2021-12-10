package cn.ffcs.zhsq.mybatis.domain.devicealarminfo;

import java.io.Serializable;

/**\
 * 烟感告警信息实体
 * @author zhangzhenhai
 * @date 2017-10-23 上午9:02:41
 */
public class SmokeAlarm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7670604694937962707L;

	private String alarm_type;//告警类型  1：上线，2：失联，3：烟雾告警，4：火警，5：电量正常，6：电量低，7：拆除，8：自检故障，9：烟雾告警解除，10：火警解除
	private String recordtime;//记录时间:yyyy-MM-dd HH:mm:ss
	public String getAlarm_type() {
		return alarm_type;
	}
	public void setAlarm_type(String alarm_type) {
		this.alarm_type = alarm_type;
	}
	public String getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
	}
	
	
}
