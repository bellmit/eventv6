package cn.ffcs.zhsq.mybatis.domain.map.arcgis;


import java.io.Serializable;
import java.util.List;

/**
 * 2014-08-06 liushi add
 * 地图配置信息
 * @Table Map_Config_Gis
 * @author liushi
 */
public class ArcgisConfigInfo implements Serializable{

	private static final long serialVersionUID = -1088639662639553047L;
	
	private Integer arcgisConfigInfoId;//ID
	private String gridCode;//网格code
	private Integer mapType;//地图切换类型，原先的mapt
	private String mapTypeName;//地图切换类型名称
	private Integer mapRows;//切片高，像素高
	private Integer mapCols;//切片宽，像素宽
	private Double mapOrgiginX;//切图原点x
	private Double mapOrgiginY;//切图原点y
	private Integer wkid;//空间参考系
	private Double mapCenterX;//默认中心点
	private Double mapCenterY;//默认中心点
	private Integer zoom;//默认显示级别
	private Integer minZoom;//最小显示级别
	private Integer orderIndex;//排序
	private Integer mapTypeCode;//地图类型编码（与现有的名称对应）
	private String coordinateServiceName;//坐标转换服务名称
	private String mapKey;//地图密钥：互联网的天地图需要加这个值
	private String remark;// 地图描述
	private String engineName;// 引擎类型
	private List<ArcgisServiceInfo> arcgisServiceInfos;//arcgis服务列表
	private List<ArcgisScalenInfo> arcgisScalenInfos;//arcgis标尺列表

	public String getCoordinateServiceName() {
		return coordinateServiceName;
	}
	public void setCoordinateServiceName(String coordinateServiceName) {
		this.coordinateServiceName = coordinateServiceName;
	}
	public Double getMapCenterX() {
		return mapCenterX;
	}
	public void setMapCenterX(Double mapCenterX) {
		this.mapCenterX = mapCenterX;
	}
	public Double getMapCenterY() {
		return mapCenterY;
	}
	public void setMapCenterY(Double mapCenterY) {
		this.mapCenterY = mapCenterY;
	}
	public Integer getZoom() {
		return zoom;
	}
	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}
	public Integer getOrderIndex() {
		return orderIndex;
	}
	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}
	public Integer getMapTypeCode() {
		return mapTypeCode;
	}
	public void setMapTypeCode(Integer mapTypeCode) {
		this.mapTypeCode = mapTypeCode;
	}
	public List<ArcgisServiceInfo> getArcgisServiceInfos() {
		return arcgisServiceInfos;
	}
	public void setArcgisServiceInfos(List<ArcgisServiceInfo> arcgisServiceInfos) {
		this.arcgisServiceInfos = arcgisServiceInfos;
	}
	public List<ArcgisScalenInfo> getArcgisScalenInfos() {
		return arcgisScalenInfos;
	}
	public void setArcgisScalenInfos(List<ArcgisScalenInfo> arcgisScalenInfos) {
		this.arcgisScalenInfos = arcgisScalenInfos;
	}
	public Integer getArcgisConfigInfoId() {
		return arcgisConfigInfoId;
	}
	public void setArcgisConfigInfoId(Integer arcgisConfigInfoId) {
		this.arcgisConfigInfoId = arcgisConfigInfoId;
	}
	public String getGridCode() {
		return gridCode;
	}
	public void setGridCode(String gridCode) {
		this.gridCode = gridCode;
	}
	public Integer getMapType() {
		return mapType;
	}
	public void setMapType(Integer mapType) {
		this.mapType = mapType;
	}
	public String getMapTypeName() {
		return mapTypeName;
	}
	public void setMapTypeName(String mapTypeName) {
		this.mapTypeName = mapTypeName;
	}
	public Integer getMapRows() {
		return mapRows;
	}
	public void setMapRows(Integer mapRows) {
		this.mapRows = mapRows;
	}
	public Integer getMapCols() {
		return mapCols;
	}
	public void setMapCols(Integer mapCols) {
		this.mapCols = mapCols;
	}
	public Double getMapOrgiginX() {
		return mapOrgiginX;
	}
	public void setMapOrgiginX(Double mapOrgiginX) {
		this.mapOrgiginX = mapOrgiginX;
	}
	public Double getMapOrgiginY() {
		return mapOrgiginY;
	}
	public void setMapOrgiginY(Double mapOrgiginY) {
		this.mapOrgiginY = mapOrgiginY;
	}
	public Integer getWkid() {
		return wkid;
	}
	public void setWkid(Integer wkid) {
		this.wkid = wkid;
	}

	public Integer getMinZoom() {
		return minZoom;
	}

	public void setMinZoom(Integer minZoom) {
		this.minZoom = minZoom;
	}

	public String getMapKey() {
		return mapKey;
	}

	public void setMapKey(String mapKey) {
		this.mapKey = mapKey;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getEngineName() {
		return engineName;
	}
	public void setEngineName(String engineName) {
		this.engineName = engineName;
	}
	
}
