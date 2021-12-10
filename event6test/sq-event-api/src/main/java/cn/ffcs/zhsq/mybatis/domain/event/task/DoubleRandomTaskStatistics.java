package cn.ffcs.zhsq.mybatis.domain.event.task;

import java.io.Serializable;

/**
 * @Description: 双随机任务模块统计(按照乡镇统计)
 * @Author: zkongbai
 * @Date: 11-07 10:15:16
 * @Copyright: 2017 福富软件
 */
public class DoubleRandomTaskStatistics implements Serializable {
	private static final long serialVersionUID = -1573112723602109456L;
	
	private String regionCode;
	private String regionName;
	private String hgl;//合格率
	private String jsl;//及时率
	private String startTime;//查询开始时间 yyyy-MM-dd HH:mm:ss
	private String endTime;
	
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getHgl() {
		return hgl;
	}
	public void setHgl(String hgl) {
		this.hgl = hgl;
	}
	public String getJsl() {
		return jsl;
	}
	public void setJsl(String jsl) {
		this.jsl = jsl;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


}