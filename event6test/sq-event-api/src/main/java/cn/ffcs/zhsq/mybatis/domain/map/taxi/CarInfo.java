package cn.ffcs.zhsq.mybatis.domain.map.taxi;

import java.io.Serializable;

/**
 * 出租车信息
 * @author zkongbai
 *
 */
public class CarInfo implements Serializable {
	private static final long serialVersionUID = 2257027674884905913L;
	
	private Long buslineId;				//车队id
	private String buslineName;			//车队名称
	private String carBxno;				//波箱系列号
	private String carDjrq;				//登记日期
	private String carDjzh;				//登记证号
	private String carDph;				//底盘号
	private String carFdjh;				//发动机号
	private String carFzrq;				//发证日期
	private String carGrsj;				//购入时间
	private String carGzzh;				//购置证号
	private String carHslx;				//核算类型
	private Long carId;					//车辆id
	private String carKthm;				//空调号码
	private String carNo;				//车牌号
	private String carQyrq;				//启用日期
	private Double carSize;				//座位数/吨位
	private String carYyzh;				//营运证号
	private String carZbh;				//自编号
	private Long cardjId;				//车辆等级
	private String cardjMemo;			//营运类型
	private Long cartypeId;				//终端类型id
	private String cartypeName;			//终端类型
	private String devId;					//设备id
	private String oilTypeName;			//油箱类型名称
	private Long ownerUserId;			//创建者用户id
	private String ownerUserName;		//创建者
	private Long resId;					//图片id（未用到）
	private Long zpdjId;				//装配等级
	private String zpdjName;			//公司名称
	private Long carKindId;				//1:英伦 2：byd
	private String carKindName;			//车辆类型名称
	private Long taxiType;				//1:电召专用车  2：普通出租车
	private String isgravitysensor;		//是否安装重力感应传感器   1：已安装
	private Long carModelsId;			//车辆模型id
	private String carModelsName;		//车辆模型名称
	private Long ktDevId;				//科泰设备id(用于与车牌号组合为鉴权码)
	private String lineCodes;			//城际快车线路代码,如”123;315;46”
	private String lineNames;			//城际快车线路名称,如”asj;ajal”
	
	public Long getBuslineId() {
		return buslineId;
	}
	public void setBuslineId(Long buslineId) {
		this.buslineId = buslineId;
	}
	public String getBuslineName() {
		return buslineName;
	}
	public void setBuslineName(String buslineName) {
		this.buslineName = buslineName;
	}
	public String getCarBxno() {
		return carBxno;
	}
	public void setCarBxno(String carBxno) {
		this.carBxno = carBxno;
	}
	public String getCarDjrq() {
		return carDjrq;
	}
	public void setCarDjrq(String carDjrq) {
		this.carDjrq = carDjrq;
	}
	public String getCarDjzh() {
		return carDjzh;
	}
	public void setCarDjzh(String carDjzh) {
		this.carDjzh = carDjzh;
	}
	public String getCarDph() {
		return carDph;
	}
	public void setCarDph(String carDph) {
		this.carDph = carDph;
	}
	public String getCarFdjh() {
		return carFdjh;
	}
	public void setCarFdjh(String carFdjh) {
		this.carFdjh = carFdjh;
	}
	public String getCarFzrq() {
		return carFzrq;
	}
	public void setCarFzrq(String carFzrq) {
		this.carFzrq = carFzrq;
	}
	public String getCarGrsj() {
		return carGrsj;
	}
	public void setCarGrsj(String carGrsj) {
		this.carGrsj = carGrsj;
	}
	public String getCarGzzh() {
		return carGzzh;
	}
	public void setCarGzzh(String carGzzh) {
		this.carGzzh = carGzzh;
	}
	public String getCarHslx() {
		return carHslx;
	}
	public void setCarHslx(String carHslx) {
		this.carHslx = carHslx;
	}
	public Long getCarId() {
		return carId;
	}
	public void setCarId(Long carId) {
		this.carId = carId;
	}
	public String getCarKthm() {
		return carKthm;
	}
	public void setCarKthm(String carKthm) {
		this.carKthm = carKthm;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public String getCarQyrq() {
		return carQyrq;
	}
	public void setCarQyrq(String carQyrq) {
		this.carQyrq = carQyrq;
	}
	public Double getCarSize() {
		return carSize;
	}
	public void setCarSize(Double carSize) {
		this.carSize = carSize;
	}
	public String getCarYyzh() {
		return carYyzh;
	}
	public void setCarYyzh(String carYyzh) {
		this.carYyzh = carYyzh;
	}
	public String getCarZbh() {
		return carZbh;
	}
	public void setCarZbh(String carZbh) {
		this.carZbh = carZbh;
	}
	public Long getCardjId() {
		return cardjId;
	}
	public void setCardjId(Long cardjId) {
		this.cardjId = cardjId;
	}
	public String getCardjMemo() {
		return cardjMemo;
	}
	public void setCardjMemo(String cardjMemo) {
		this.cardjMemo = cardjMemo;
	}
	public Long getCartypeId() {
		return cartypeId;
	}
	public void setCartypeId(Long cartypeId) {
		this.cartypeId = cartypeId;
	}
	public String getCartypeName() {
		return cartypeName;
	}
	public void setCartypeName(String cartypeName) {
		this.cartypeName = cartypeName;
	}
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	public String getOilTypeName() {
		return oilTypeName;
	}
	public void setOilTypeName(String oilTypeName) {
		this.oilTypeName = oilTypeName;
	}
	public Long getOwnerUserId() {
		return ownerUserId;
	}
	public void setOwnerUserId(Long ownerUserId) {
		this.ownerUserId = ownerUserId;
	}
	public String getOwnerUserName() {
		return ownerUserName;
	}
	public void setOwnerUserName(String ownerUserName) {
		this.ownerUserName = ownerUserName;
	}
	public Long getResId() {
		return resId;
	}
	public void setResId(Long resId) {
		this.resId = resId;
	}
	public Long getZpdjId() {
		return zpdjId;
	}
	public void setZpdjId(Long zpdjId) {
		this.zpdjId = zpdjId;
	}
	public String getZpdjName() {
		return zpdjName;
	}
	public void setZpdjName(String zpdjName) {
		this.zpdjName = zpdjName;
	}
	public Long getCarKindId() {
		return carKindId;
	}
	public void setCarKindId(Long carKindId) {
		this.carKindId = carKindId;
	}
	public String getCarKindName() {
		return carKindName;
	}
	public void setCarKindName(String carKindName) {
		this.carKindName = carKindName;
	}
	public Long getTaxiType() {
		return taxiType;
	}
	public void setTaxiType(Long taxiType) {
		this.taxiType = taxiType;
	}
	public String getIsgravitysensor() {
		return isgravitysensor;
	}
	public void setIsgravitysensor(String isgravitysensor) {
		this.isgravitysensor = isgravitysensor;
	}
	public Long getCarModelsId() {
		return carModelsId;
	}
	public void setCarModelsId(Long carModelsId) {
		this.carModelsId = carModelsId;
	}
	public String getCarModelsName() {
		return carModelsName;
	}
	public void setCarModelsName(String carModelsName) {
		this.carModelsName = carModelsName;
	}
	public Long getKtDevId() {
		return ktDevId;
	}
	public void setKtDevId(Long ktDevId) {
		this.ktDevId = ktDevId;
	}
	public String getLineCodes() {
		return lineCodes;
	}
	public void setLineCodes(String lineCodes) {
		this.lineCodes = lineCodes;
	}
	public String getLineNames() {
		return lineNames;
	}
	public void setLineNames(String lineNames) {
		this.lineNames = lineNames;
	}
	@Override
	public String toString() {
		return "CarInfo [buslineId=" + buslineId + ", buslineName="
				+ buslineName + ", carBxno=" + carBxno + ", carDjrq=" + carDjrq
				+ ", carDjzh=" + carDjzh + ", carDph=" + carDph + ", carFdjh="
				+ carFdjh + ", carFzrq=" + carFzrq + ", carGrsj=" + carGrsj
				+ ", carGzzh=" + carGzzh + ", carHslx=" + carHslx + ", carId="
				+ carId + ", carKthm=" + carKthm + ", carNo=" + carNo
				+ ", carQyrq=" + carQyrq + ", carSize=" + carSize
				+ ", carYyzh=" + carYyzh + ", carZbh=" + carZbh + ", cardjId="
				+ cardjId + ", cardjMemo=" + cardjMemo + ", cartypeId="
				+ cartypeId + ", cartypeName=" + cartypeName + ", devId="
				+ devId + ", oilTypeName=" + oilTypeName + ", ownerUserId="
				+ ownerUserId + ", ownerUserName=" + ownerUserName + ", resId="
				+ resId + ", zpdjName=" + zpdjName + ", carKindId=" + carKindId
				+ ", carKindName=" + carKindName + ", taxiType=" + taxiType
				+ ", isgravitysensor=" + isgravitysensor + ", carModelsId="
				+ carModelsId + ", carModelsName=" + carModelsName
				+ ", ktDevId=" + ktDevId + ", lineCodes=" + lineCodes
				+ ", lineNames=" + lineNames + "]";
	}
	
}
