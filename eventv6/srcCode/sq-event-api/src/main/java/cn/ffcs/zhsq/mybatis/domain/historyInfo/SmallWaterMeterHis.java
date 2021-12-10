package cn.ffcs.zhsq.mybatis.domain.historyInfo;

import java.io.Serializable;

/**
 * 小水表历史信息实体
 * @author zhangzhh
 * @date 2017-10-19 下午8:05:50
 */
public class SmallWaterMeterHis  implements Serializable {
	
	private static final long serialVersionUID = -8199817027773953326L;
	private String water_degree;//水表度数
	private String signal_intensity;//信号强度
	private String recordtime;//记录时间:yyyy-MM-dd HH:mm:ss
	
	
	public String getWater_degree() {
		return water_degree;
	}
	public void setWater_degree(String water_degree) {
		this.water_degree = water_degree;
	}
	public String getSignal_intensity() {
		return signal_intensity;
	}
	public void setSignal_intensity(String signal_intensity) {
		this.signal_intensity = signal_intensity;
	}
	public String getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
