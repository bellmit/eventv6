package cn.ffcs.zhsq.mybatis.domain.map.buildingBind;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;

import java.io.Serializable;

/**
 * 地图楼房信息表
 * 
 * @table T_DC_AREA_BUILDING_INFO
 * @author sulch
 * 
 */
public class BuildingBindInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long buildingBindId;//主键
	private Long buildingId;// 楼房标识
	private String buildingName;// 楼房名称
	private String buildingAddress;// 楼房地址
	private Long oid;//地图上楼宇的id
	private String mapType;//地图类型

	public Long getBuildingBindId() {
		return buildingBindId;
	}

	public void setBuildingBindId(Long buildingBindId) {
		this.buildingBindId = buildingBindId;
	}

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
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

	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}
}
