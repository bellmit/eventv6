package cn.ffcs.zhsq.mybatis.domain.szzg.aqi;

import java.io.Serializable;
import java.util.Date;
/**
 * 空气质量
 * @author linzhu
 *
 */
public class ZgAQI implements Serializable {
	private static final long serialVersionUID = 6636277309459770021L;
	private long seqid;
	private String stationName;//监测点名称
	private String statioId;//监测点ID
	private Date monitorTime;//监测时间
	private String monitorType;//监测类型：1按小时2按天3首要站点
	private String hour;//监测小时数00-23
	private String aqi;//空气质量指数
	private String state;//空气质量状况值  1 优 2 良 3 轻度污染 4中度污染 5重度污染 6严重污染
	private String stateName;//空气质量状况值名称
	private String airClass;
	private String className;
	private String so2;
	private String so224;
	private String no2;
	private String no224;
	private String pm10;
	private String pm1024;
	private String pm25;
	private String pm2524;
	private String co;
	private String co24;
	private String o3;
	private String o38;
	private String mainFomite;//首要污染物
	private String picPath;
	private String class24;
	private String state24;
	private String aqi24;
	private String effectInfo;
	private String adviseInfo;
	private String x;
	private String y;
	private String longitude;
	private String dimensions;
	private Date opdate;

	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Date getMonitorTime() {
		return monitorTime;
	}
	public void setMonitorTime(Date monitorTime) {
		this.monitorTime = monitorTime;
	}
	public String getMonitorType() {
		return monitorType;
	}
	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getAqi() {
		return aqi;
	}
	public void setAqi(String aqi) {
		this.aqi = aqi;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getAirClass() {
		return airClass;
	}
	public void setAirClass(String airClass) {
		this.airClass = airClass;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSo2() {
		return so2;
	}
	public void setSo2(String so2) {
		this.so2 = so2;
	}
	public String getSo224() {
		return so224;
	}
	public void setSo224(String so224) {
		this.so224 = so224;
	}
	public String getNo2() {
		return no2;
	}
	public void setNo2(String no2) {
		this.no2 = no2;
	}
	public String getNo224() {
		return no224;
	}
	public void setNo224(String no224) {
		this.no224 = no224;
	}
	public String getPm10() {
		return pm10;
	}
	public void setPm10(String pm10) {
		this.pm10 = pm10;
	}
	public String getPm1024() {
		return pm1024;
	}
	public void setPm1024(String pm1024) {
		this.pm1024 = pm1024;
	}
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	public String getPm2524() {
		return pm2524;
	}
	public void setPm2524(String pm2524) {
		this.pm2524 = pm2524;
	}
	public String getCo() {
		return co;
	}
	public void setCo(String co) {
		this.co = co;
	}
	public String getCo24() {
		return co24;
	}
	public void setCo24(String co24) {
		this.co24 = co24;
	}
	public String getO3() {
		return o3;
	}
	public void setO3(String o3) {
		this.o3 = o3;
	}
	public String getO38() {
		return o38;
	}
	public void setO38(String o38) {
		this.o38 = o38;
	}
	public String getMainFomite() {
		return mainFomite;
	}
	public void setMainFomite(String mainFomite) {
		this.mainFomite = mainFomite;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getClass24() {
		return class24;
	}
	public void setClass24(String class24) {
		this.class24 = class24;
	}
	public String getState24() {
		return state24;
	}
	public void setState24(String state24) {
		this.state24 = state24;
	}
	public String getAqi24() {
		return aqi24;
	}
	public void setAqi24(String aqi24) {
		this.aqi24 = aqi24;
	}
	public String getEffectInfo() {
		return effectInfo;
	}
	public void setEffectInfo(String effectInfo) {
		this.effectInfo = effectInfo;
	}
	public String getAdviseInfo() {
		return adviseInfo;
	}
	public void setAdviseInfo(String adviseInfo) {
		this.adviseInfo = adviseInfo;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getDimensions() {
		return dimensions;
	}
	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}
	public Date getOpdate() {
		return opdate;
	}
	public void setOpdate(Date opdate) {
		this.opdate = opdate;
	}
	public long getSeqid() {
		return seqid;
	}
	public void setSeqid(long seqid) {
		this.seqid = seqid;
	}
	public String getStatioId() {
		return statioId;
	}
	public void setStatioId(String statioId) {
		this.statioId = statioId;
	}
	

}
