package cn.ffcs.zhsq.mybatis.domain.historyInfo;

import java.io.Serializable;

/**
 * 井盖历史信息实体
 * @author zhangzhenhai
 * @date 2017-10-23 上午9:05:21
 */
public class ManholeCoverHis implements Serializable {

	private String type;//类型 0：井下，1：路面积水，2：浊度，3：流速
	private String acquisition_value;//采集值
	private String recordtime;//记录时间:yyyy-MM-dd HH:mm:ss
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAcquisition_value() {
		return acquisition_value;
	}
	public void setAcquisition_value(String acquisition_value) {
		this.acquisition_value = acquisition_value;
	}
	public String getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
	}
	
	
}
