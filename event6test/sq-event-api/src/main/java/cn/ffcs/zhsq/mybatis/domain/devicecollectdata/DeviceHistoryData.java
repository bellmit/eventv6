package cn.ffcs.zhsq.mybatis.domain.devicecollectdata;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 设备历史记录模块bo对象
 * @Author: husp
 * @Date: 12-7  
 * @Copyright: 2017 福富软件
 */
public class DeviceHistoryData implements Serializable {
	private static final long serialVersionUID = 1L;
	//共用
	private String deviceServiceId; //点位编号
	private String deviceId; //设备编号
	private String deviceName; //设备名称
	private String longitude; //经度
	private String latitude; //纬度
	private Date colleTime; //采集时间
	private String remark; //备注
	private String isValid; //0 无效 1 有效
	private Long createBy; //创建人
	private Date createTime; //创建时间
	
	//水质监测历史数据
	private Long dwhId;//编号
	private Double ph; //PH值
	private Double temp; //水温（C）
	private Double dorate; //溶解氧（mg/L）
	private Double elec; //电导率（us/cm）
	private Double turb; //浊度（NTU）
	private String dataTime;//采集时间

	//空气质量监测历史数据
	private Long dahId; //编号
	private String pmTwoFive; //PM2.5(μg/m3)
	private String pmTen; //PM10(μg/m3)
	private String aqi; //AQI
	private String dayAqiStatus; //空气质量状况
	private String primaryPollutant; //首要污染物
	private String monitorTime;//监测日期
	
	//河流监测历史数据
	private Long drhId; //编号
	private Double _do; //DO(mg/L)
	private Double codmn; //高锰酸盐指数(mg/L)
	private Double codcr; //化学需氧量(mg/L)
	private Double bod5; //BOD5(mg/L)
	private Double nhn; //氨氮(mg/L)
	private Double tp; //总磷(mg/L)
	private String yearMonth;//监测日期
	
	//垃圾桶
	private Long dthId; //编号
	private String batterystatus;
	private String capacity;
	private String compress;
	private Long compress70count;
	private Long compress90count;
	private String open;
	private String primarybattery;
	private String scroll;
	private String voice;
	private String alarmReason;				//报警原因
	private String id;	//报警ID
	private String pointX;				//设备维度
	private String pointY;				//设备经度
	private String startTime;	
	
	//井盖
	private Long dchId; //编号
	private Double electricity;    //电池电量
	private Double inclinationAngle;	//倾斜角度
	private Double redidualVoltage;	//剩余电压
	private Double signalIntensity;	//信号强度"  
	
	//农业
	private Long dshId; //编号
	private String alertlevel;//报警等级
	private String gathertime;//采集时间
	private String name;//传感器名称
	private String newestvalue;//传感器最新值
	private String type;//传感器类型名称
	private String unit;//传感器单位
	
	
	public Long getDshId() {
		return dshId;
	}
	public void setDshId(Long dshId) {
		this.dshId = dshId;
	}
	public String getAlertlevel() {
		return alertlevel;
	}
	public void setAlertlevel(String alertlevel) {
		this.alertlevel = alertlevel;
	}
	public String getGathertime() {
		return gathertime;
	}
	public void setGathertime(String gathertime) {
		this.gathertime = gathertime;
	}
	public String getNewestvalue() {
		return newestvalue;
	}
	public void setNewestvalue(String newestvalue) {
		this.newestvalue = newestvalue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Long getDthId() {
		return dthId;
	}
	public void setDthId(Long dthId) {
		this.dthId = dthId;
	}
	public Long getDchId() {
		return dchId;
	}
	public void setDchId(Long dchId) {
		this.dchId = dchId;
	}
	public Double getElectricity() {
		return electricity;
	}
	public void setElectricity(Double electricity) {
		this.electricity = electricity;
	}
	public Double getInclinationAngle() {
		return inclinationAngle;
	}
	public void setInclinationAngle(Double inclinationAngle) {
		this.inclinationAngle = inclinationAngle;
	}
	public Double getRedidualVoltage() {
		return redidualVoltage;
	}
	public void setRedidualVoltage(Double redidualVoltage) {
		this.redidualVoltage = redidualVoltage;
	}
	public Double getSignalIntensity() {
		return signalIntensity;
	}
	public void setSignalIntensity(Double signalIntensity) {
		this.signalIntensity = signalIntensity;
	}
	public String getDataTime() {
		return dataTime;
	}
	public String getBatterystatus() {
		return batterystatus;
	}
	public void setBatterystatus(String batterystatus) {
		this.batterystatus = batterystatus;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public String getCompress() {
		return compress;
	}
	public void setCompress(String compress) {
		this.compress = compress;
	}
	public Long getCompress70count() {
		return compress70count;
	}
	public void setCompress70count(Long compress70count) {
		this.compress70count = compress70count;
	}
	public Long getCompress90count() {
		return compress90count;
	}
	public void setCompress90count(Long compress90count) {
		this.compress90count = compress90count;
	}
	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}
	public String getPrimarybattery() {
		return primarybattery;
	}
	public void setPrimarybattery(String primarybattery) {
		this.primarybattery = primarybattery;
	}
	public String getScroll() {
		return scroll;
	}
	public void setScroll(String scroll) {
		this.scroll = scroll;
	}
	public String getVoice() {
		return voice;
	}
	public void setVoice(String voice) {
		this.voice = voice;
	}
	public String getAlarmReason() {
		return alarmReason;
	}
	public void setAlarmReason(String alarmReason) {
		this.alarmReason = alarmReason;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPointX() {
		return pointX;
	}
	public void setPointX(String pointX) {
		this.pointX = pointX;
	}
	public String getPointY() {
		return pointY;
	}
	public void setPointY(String pointY) {
		this.pointY = pointY;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}
	public String getMonitorTime() {
		return monitorTime;
	}
	public void setMonitorTime(String monitorTime) {
		this.monitorTime = monitorTime;
	}
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public Double get_do() {
		return _do;
	}
	public void set_do(Double _do) {
		this._do = _do;
	}
	public Double getCodmn() {
		return codmn;
	}
	public void setCodmn(Double codmn) {
		this.codmn = codmn;
	}
	public Double getCodcr() {
		return codcr;
	}
	public void setCodcr(Double codcr) {
		this.codcr = codcr;
	}
	public Double getBod5() {
		return bod5;
	}
	public void setBod5(Double bod5) {
		this.bod5 = bod5;
	}
	public Double getNhn() {
		return nhn;
	}
	public void setNhn(Double nhn) {
		this.nhn = nhn;
	}
	public Double getTp() {
		return tp;
	}
	public void setTp(Double tp) {
		this.tp = tp;
	}
	public Long getDrhId() {
		return drhId;
	}
	public void setDrhId(Long drhId) {
		this.drhId = drhId;
	}

	public Long getDahId() {
		return dahId;
	}
	public void setDahId(Long dahId) {
		this.dahId = dahId;
	}
	public String getPmTen() {
		return pmTen;
	}
	public void setPmTen(String pmTen) {
		this.pmTen = pmTen;
	}
	public String getAqi() {
		return aqi;
	}
	public void setAqi(String aqi) {
		this.aqi = aqi;
	}
	
	public String getPmTwoFive() {
		return pmTwoFive;
	}
	public void setPmTwoFive(String pmTwoFive) {
		this.pmTwoFive = pmTwoFive;
	}
	public String getDayAqiStatus() {
		return dayAqiStatus;
	}
	public void setDayAqiStatus(String dayAqiStatus) {
		this.dayAqiStatus = dayAqiStatus;
	}
	public String getPrimaryPollutant() {
		return primaryPollutant;
	}
	public void setPrimaryPollutant(String primaryPollutant) {
		this.primaryPollutant = primaryPollutant;
	}
	public Long getDwhId() {
		return dwhId;
	}
	public void setDwhId(Long dwhId) {
		this.dwhId = dwhId;
	}
	public String getDeviceServiceId() {
		return deviceServiceId;
	}
	public void setDeviceServiceId(String deviceServiceId) {
		this.deviceServiceId = deviceServiceId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public Date getColleTime() {
		return colleTime;
	}
	public void setColleTime(Date colleTime) {
		this.colleTime = colleTime;
	}
	public Double getPh() {
		return ph;
	}
	public void setPh(Double ph) {
		this.ph = ph;
	}
	public Double getTemp() {
		return temp;
	}
	public void setTemp(Double temp) {
		this.temp = temp;
	}
	public Double getDorate() {
		return dorate;
	}
	public void setDorate(Double dorate) {
		this.dorate = dorate;
	}
	public Double getElec() {
		return elec;
	}
	public void setElec(Double elec) {
		this.elec = elec;
	}
	public Double getTurb() {
		return turb;
	}
	public void setTurb(Double turb) {
		this.turb = turb;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}	
}