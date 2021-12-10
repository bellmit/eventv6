package cn.ffcs.zhsq.mybatis.domain.map.bus;

/**
 * 车辆信息(车辆和线路的映射关系)
 * @author zkongbai
 *
 */
public class CarBusline implements java.io.Serializable{
	private static final long serialVersionUID = -8545492175476032257L;
	
	private Long buslineId;		//线路ID
	private Long carId;			//车辆ID
	private String carNo;		//车牌号
	private String devId;		//设备ID
	
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
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
}
