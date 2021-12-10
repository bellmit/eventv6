package cn.ffcs.zhsq.mybatis.domain.map.taxi;

import java.io.Serializable;

import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfo;

/**
 * 出租车运行历史轨迹数据(考虑暂不入库)
 * @author zkongbai
 *
 */
public class CarHisTrack extends ArcgisInfo implements Serializable{
	private static final long serialVersionUID = -2312466568648669178L;
	
	private String stime;				//服务器时间，时间格式
	private String gtime;				//GPS时间，时间格式
	private Long islocat;				//是否定位：0-已定位 ,1-未定位
	private Long latitude;				//纬度 X 100 0000
	private Long longtitude;			//经度 X 100 0000
	private Long hight;					//高度
	private Long speed;					//速度
	private Long direction;				//方向
	private Long isreplace;				//是否补传，预留，数据暂时不用
	private Long sumdis;				//里程，预留，数据暂时不用
	private Long carstatus;				//车辆状态字，详见部标808定位数据
	private Long alarmstatus;			//车辆报警状态，详见部标808定位数据
	private Long runningstatus;			//出租运营状态，云平台协议中位置基本信息中的出租运营状态
	private Long gravitySensorStatus;	//重力感应状态，云平台协议中位置基本信息中的出租运营状态(0xE7)
	
	private String locateTime;
	
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getGtime() {
		return gtime;
	}
	public void setGtime(String gtime) {
		this.gtime = gtime;
	}
	public Long getIslocat() {
		return islocat;
	}
	public void setIslocat(Long islocat) {
		this.islocat = islocat;
	}
	public Long getLatitude() {
		return latitude;
	}
	public void setLatitude(Long latitude) {
		this.latitude = latitude;
	}
	public Long getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(Long longtitude) {
		this.longtitude = longtitude;
	}
	public Long getHight() {
		return hight;
	}
	public void setHight(Long hight) {
		this.hight = hight;
	}
	public Long getSpeed() {
		return speed;
	}
	public void setSpeed(Long speed) {
		this.speed = speed;
	}
	public Long getDirection() {
		return direction;
	}
	public void setDirection(Long direction) {
		this.direction = direction;
	}
	public Long getIsreplace() {
		return isreplace;
	}
	public void setIsreplace(Long isreplace) {
		this.isreplace = isreplace;
	}
	public Long getSumdis() {
		return sumdis;
	}
	public void setSumdis(Long sumdis) {
		this.sumdis = sumdis;
	}
	public Long getCarstatus() {
		return carstatus;
	}
	public void setCarstatus(Long carstatus) {
		this.carstatus = carstatus;
	}
	public Long getAlarmstatus() {
		return alarmstatus;
	}
	public void setAlarmstatus(Long alarmstatus) {
		this.alarmstatus = alarmstatus;
	}
	public Long getRunningstatus() {
		return runningstatus;
	}
	public void setRunningstatus(Long runningstatus) {
		this.runningstatus = runningstatus;
	}
	public Long getGravitySensorStatus() {
		return gravitySensorStatus;
	}
	public void setGravitySensorStatus(Long gravitySensorStatus) {
		this.gravitySensorStatus = gravitySensorStatus;
	}
	
	public String getLocateTime() {
		return locateTime;
	}
	public void setLocateTime(String locateTime) {
		this.locateTime = locateTime;
	}
	@Override
	public String toString() {
		return "CarHisTrack [stime=" + stime + ", gtime=" + gtime
				+ ", islocat=" + islocat + ", latitude=" + latitude
				+ ", longtitude=" + longtitude + ", hight=" + hight
				+ ", speed=" + speed + ", direction=" + direction
				+ ", isreplace=" + isreplace + ", sumdis=" + sumdis
				+ ", carstatus=" + carstatus + ", alarmstatus=" + alarmstatus
				+ ", runningstatus=" + runningstatus + ", gravitySensorStatus="
				+ gravitySensorStatus + "]";
	}

}
