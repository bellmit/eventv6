package cn.ffcs.zhsq.mybatis.domain.map.bus;

/**
 * 车辆末次位置信息
 * @author zkongbai
 *
 */
public class CarLastGps implements java.io.Serializable{
	private static final long serialVersionUID = -9145578454805402598L;
	
	private Long buslineId;		//线路ID
	private Long carId;			//车辆ID
	private String carNo;		//车牌号
	private String gpsTime;		//GPS时间,格式：YYYY-MM-DD HH24:MI:SS
	private Long lng;			//经纬,乘以10的六次方
	private Long lat;			//纬度,乘以10的六次方
	private Long hight;		//高度,乘以10的2次方(wrong word)
	private Long speed;			//高度,乘以10的4次方
	private Long direct;		//高度,乘以10的2次方
	
	public Long getBuslineId() {
		return buslineId;
	}
	public void setBuslineId(Long buslineId) {
		this.buslineId = buslineId;
	}
	public Long getCarId() {
		return carId;
	}
	public void setCarId(Long carId) {
		this.carId = carId;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public String getGpsTime() {
		return gpsTime;
	}
	public void setGpsTime(String gpsTime) {
		this.gpsTime = gpsTime;
	}
	public Long getLng() {
		return lng;
	}
	public void setLng(Long lng) {
		this.lng = lng;
	}
	public Long getLat() {
		return lat;
	}
	public void setLat(Long lat) {
		this.lat = lat;
	}
	public Long getHeight() {
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
	public Long getDirect() {
		return direct;
	}
	public void setDirect(Long direct) {
		this.direct = direct;
	}
}
