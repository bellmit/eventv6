package cn.ffcs.zhsq.bus.demo;
import java.io.Serializable;
import java.util.Date;

/**
 * 校车gps信息
 * @author Administrator
 *
 */
public class SchoolBusGpsBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7806896379467766060L;

	private String deviceNumber;//设备编号
	private String mobile;//手机号
	private Date gpsTime;//GPS定位时间
	private Date updatedTime;//汇报时间
	private String positionValid;//定位标识:0-定位,1-无定位
	private String longitude;//经度
	private String latitude;//纬度
	private String altitude;//海拔
	private String speed;//速度
	private String direction;//行驶方向原始值
	private String directionText;//行驶方向转换值
	private String status;//车辆状态原始值
	private String statusText;//车辆状态转换值
	private String mileage;//总里程
	private boolean online;//车辆在线离线
	
	
	public String getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public Date getGpsTime() {
		return gpsTime;
	}
	public void setGpsTime(Date gpsTime) {
		this.gpsTime = gpsTime;
	}
	public String getPositionValid() {
		return positionValid;
	}
	public void setPositionValid(String positionValid) {
		this.positionValid = positionValid;
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
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getDirectionText() {
		return directionText;
	}
	public void setDirectionText(String directionText) {
		this.directionText = directionText;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	
	
}
