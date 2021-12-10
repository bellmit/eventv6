package cn.ffcs.zhsq.mybatis.domain.wellKnownTrademark;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 张天慈 on 2017/12/15.
 */
public class Trademark implements Serializable {

	private Long trademarkId; //商标ID
	private String unitName; //企业名称
	private String brand; //商标内容
	private Date thatTimeDate; //认定年份
	private String thatTimeStr; //认定年份
	private String scope; //核定商品范围
	private Long creatorId; // 创建人ID
	private Date creatTimeDate; // 创建时间
	private String creatTimeStr; // 创建时间
	private String creatorName; // 创建人姓名
	private Long updaterId; // 修改人ID
	private Date updateTimeDate; // 修改时间
	private String updateTimeStr; // 修改时间
	private String updaterName; // 修改人姓名
	private String unitAddress; // 单位地址
	private String longitude; // 单位定位信息(经度)x
	private String latitude; // 单位定位信息(纬度)y
	private String regionCode; //区域编码
	private String trademarkImg; //商标图样
	private String status; //状态,删除-0，可用-1

	private Long gridId; //所属网格
	private String gridCode; //网格编码
	private String gridName; //网格名称
	private String gridPath; //网格名称

	public Trademark() {
	}

	public Trademark(Long trademarkId, String unitName, String brand, Date thatTimeDate, String thatTimeStr, String scope, Long creatorId, Date creatTimeDate, String creatTimeStr, String creatorName, Long updaterId, Date updateTimeDate, String updateTimeStr, String updaterName, String unitAddress, String longitude, String latitude, String regionCode, String trademarkImg, String status, Long gridId, String gridCode, String gridName, String gridPath) {
		this.trademarkId = trademarkId;
		this.unitName = unitName;
		this.brand = brand;
		this.thatTimeDate = thatTimeDate;
		this.thatTimeStr = thatTimeStr;
		this.scope = scope;
		this.creatorId = creatorId;
		this.creatTimeDate = creatTimeDate;
		this.creatTimeStr = creatTimeStr;
		this.creatorName = creatorName;
		this.updaterId = updaterId;
		this.updateTimeDate = updateTimeDate;
		this.updateTimeStr = updateTimeStr;
		this.updaterName = updaterName;
		this.unitAddress = unitAddress;
		this.longitude = longitude;
		this.latitude = latitude;
		this.regionCode = regionCode;
		this.trademarkImg = trademarkImg;
		this.status = status;
		this.gridId = gridId;
		this.gridCode = gridCode;
		this.gridName = gridName;
		this.gridPath = gridPath;
	}

	public Long getTrademarkId() {
		return trademarkId;
	}

	public void setTrademarkId(Long trademarkId) {
		this.trademarkId = trademarkId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Date getThatTimeDate() {
		return thatTimeDate;
	}

	public void setThatTimeDate(Date thatTimeDate) {
		this.thatTimeDate = thatTimeDate;
	}

	public String getThatTimeStr() {
		return thatTimeStr;
	}

	public void setThatTimeStr(String thatTimeStr) {
		this.thatTimeStr = thatTimeStr;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public Long getGridId() {
		return gridId;
	}

	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}

	public String getGridCode() {
		return gridCode;
	}

	public void setGridCode(String gridCode) {
		this.gridCode = gridCode;
	}

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public String getGridPath() {
		return gridPath;
	}

	public void setGridPath(String gridPath) {
		this.gridPath = gridPath;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreatTimeDate() {
		return creatTimeDate;
	}

	public void setCreatTimeDate(Date creatTimeDate) {
		this.creatTimeDate = creatTimeDate;
	}

	public String getCreatTimeStr() {
		return creatTimeStr;
	}

	public void setCreatTimeStr(String creatTimeStr) {
		this.creatTimeStr = creatTimeStr;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Long getUpdaterId() {
		return updaterId;
	}

	public void setUpdaterId(Long updaterId) {
		this.updaterId = updaterId;
	}

	public Date getUpdateTimeDate() {
		return updateTimeDate;
	}

	public void setUpdateTimeDate(Date updateTimeDate) {
		this.updateTimeDate = updateTimeDate;
	}

	public String getUpdateTimeStr() {
		return updateTimeStr;
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}

	public String getUpdaterName() {
		return updaterName;
	}

	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}

	public String getUnitAddress() {
		return unitAddress;
	}

	public void setUnitAddress(String unitAddress) {
		this.unitAddress = unitAddress;
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

	public String getTrademarkImg() {
		return trademarkImg;
	}

	public void setTrademarkImg(String trademarkImg) {
		this.trademarkImg = trademarkImg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Trademark{" +
				"trademarkId=" + trademarkId +
				", unitName='" + unitName + '\'' +
				", brand='" + brand + '\'' +
				", thatTimeDate=" + thatTimeDate +
				", thatTimeStr='" + thatTimeStr + '\'' +
				", scope='" + scope + '\'' +
				", creatorId=" + creatorId +
				", creatTimeDate=" + creatTimeDate +
				", creatTimeStr='" + creatTimeStr + '\'' +
				", creatorName='" + creatorName + '\'' +
				", updaterId=" + updaterId +
				", updateTimeDate=" + updateTimeDate +
				", updateTimeStr='" + updateTimeStr + '\'' +
				", updaterName='" + updaterName + '\'' +
				", unitAddress='" + unitAddress + '\'' +
				", longitude='" + longitude + '\'' +
				", latitude='" + latitude + '\'' +
				", regionCode='" + regionCode + '\'' +
				", trademarkImg='" + trademarkImg + '\'' +
				", status='" + status + '\'' +
				", gridId=" + gridId +
				", gridCode='" + gridCode + '\'' +
				", gridName='" + gridName + '\'' +
				", gridPath='" + gridPath + '\'' +
				'}';
	}
}
