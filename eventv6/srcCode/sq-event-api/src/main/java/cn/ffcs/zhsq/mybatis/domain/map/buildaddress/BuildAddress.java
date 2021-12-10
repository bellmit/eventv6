package cn.ffcs.zhsq.mybatis.domain.map.buildaddress;



import java.io.Serializable;
import java.util.List;

/**
 * 2015-10-21 liushi add
 * 地址楼宇关联
 * @Table T_GIS_BUILD_ADDRESS
 * @author liushi
 */
public class BuildAddress implements Serializable{

	private static final long serialVersionUID = -7734223296801258647L;
	
	private Long addressId;//主键
	private Long buildingId;//网格id
	private String remark_;//说明
	private Long gridId;
	private String gridName;
	private String buildingName;//信息域code
	private String buildingAddress;//信息域code
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public Long getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	public String getRemark_() {
		return remark_;
	}
	public void setRemark_(String remark_) {
		this.remark_ = remark_;
	}
	public Long getGridId() {
		return gridId;
	}
	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}
	public String getGridName() {
		return gridName;
	}
	public void setGridName(String gridName) {
		this.gridName = gridName;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getBuildingAddress() {
		return buildingAddress;
	}
	public void setBuildingAddress(String buildingAddress) {
		this.buildingAddress = buildingAddress;
	}
	
}
