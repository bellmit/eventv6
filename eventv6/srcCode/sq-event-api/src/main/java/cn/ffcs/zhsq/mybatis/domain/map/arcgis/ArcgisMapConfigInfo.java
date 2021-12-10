package cn.ffcs.zhsq.mybatis.domain.map.arcgis;


import java.io.Serializable;

/**
 * 2014-05-16 liushi add
 * 地图加载的信息实体
 * @Table Map_Config_Gis
 * @author liushi
 */
public class ArcgisMapConfigInfo implements Serializable{

	private static final long serialVersionUID = -8856869822503442399L;
	
	private Long id;
	private String orgCode;
	private String theme;//主题用于控制各个首页差异的一般不用
	private Integer mapStartType;//map_start_type;//默认显示地图类型
	private String mapFile;//map_file;//二维地图文件路径
	private String mapFile3D;//map_file_3D;//三维地图文件路径
	private Double mapCenterX;//map_center_x;//二维地图中心点位x
	private Double mapCenterY;//map_center_y;//二维地图中心点位y
	private Integer mapCenterLevel;//map_center_level;//二维地图默认显示级别
	private Double mapCenterX3D;//map_center_x_3D;//三维地图中心点位x
	private Double mapCenterY3D;//map_center_y_3D;//三维地图中心点位y
	private Integer mapCenterLevel3D;//map_center_level_3D;//三维地图默认显示级别
	private Integer mapType;//map_type;//二维地图类型
	private Integer mapType3D;//map_type_3D;//三维地图类型
	private Integer mapMinl;//map_minl;//二维地图最小缩放
	private Integer mapMaxl;//map_maxl;//二维地图最大缩放
	private Integer mapMinl3D;//map_minl_3D;//三维地图最小缩放
	private Integer mapMaxl3D;//map_maxl_3D;//三维地图最大缩放
	private Double positionOffsetX;//position_offset_x;//二维地图定位偏移量
	private Double positionOffsetY;//position_offset_y;//二维地图定位偏移量
	private Double positionOffsetX3D;//position_offset_x_3D;//三维地图定位偏移量
	private Double positionOffsetY3D;//position_offset_y_3D;//三维地图定位偏移量
	private String engineType;//引擎类型 gis、arcgis
	private String contrastColor;//地图对比颜色（跟地图底色有鲜明对比的颜色）
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public Integer getMapStartType() {
		return mapStartType;
	}
	public void setMapStartType(Integer mapStartType) {
		this.mapStartType = mapStartType;
	}
	public String getMapFile() {
		return mapFile;
	}
	public void setMapFile(String mapFile) {
		this.mapFile = mapFile;
	}
	public String getMapFile3D() {
		return mapFile3D;
	}
	public void setMapFile3D(String mapFile3D) {
		this.mapFile3D = mapFile3D;
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
	public Integer getMapCenterLevel() {
		return mapCenterLevel;
	}
	public void setMapCenterLevel(Integer mapCenterLevel) {
		this.mapCenterLevel = mapCenterLevel;
	}
	public Double getMapCenterX3D() {
		return mapCenterX3D;
	}
	public void setMapCenterX3D(Double mapCenterX3D) {
		this.mapCenterX3D = mapCenterX3D;
	}
	public Double getMapCenterY3D() {
		return mapCenterY3D;
	}
	public void setMapCenterY3D(Double mapCenterY3D) {
		this.mapCenterY3D = mapCenterY3D;
	}
	public Integer getMapCenterLevel3D() {
		return mapCenterLevel3D;
	}
	public void setMapCenterLevel3D(Integer mapCenterLevel3D) {
		this.mapCenterLevel3D = mapCenterLevel3D;
	}
	public Integer getMapType() {
		return mapType;
	}
	public void setMapType(Integer mapType) {
		this.mapType = mapType;
	}
	public Integer getMapType3D() {
		return mapType3D;
	}
	public void setMapType3D(Integer mapType3D) {
		this.mapType3D = mapType3D;
	}
	public Integer getMapMinl() {
		return mapMinl;
	}
	public void setMapMinl(Integer mapMinl) {
		this.mapMinl = mapMinl;
	}
	public Integer getMapMaxl() {
		return mapMaxl;
	}
	public void setMapMaxl(Integer mapMaxl) {
		this.mapMaxl = mapMaxl;
	}
	public Integer getMapMinl3D() {
		return mapMinl3D;
	}
	public void setMapMinl3D(Integer mapMinl3D) {
		this.mapMinl3D = mapMinl3D;
	}
	public Integer getMapMaxl3D() {
		return mapMaxl3D;
	}
	public void setMapMaxl3D(Integer mapMaxl3D) {
		this.mapMaxl3D = mapMaxl3D;
	}
	public Double getPositionOffsetX() {
		return positionOffsetX;
	}
	public void setPositionOffsetX(Double positionOffsetX) {
		this.positionOffsetX = positionOffsetX;
	}
	public Double getPositionOffsetY() {
		return positionOffsetY;
	}
	public void setPositionOffsetY(Double positionOffsetY) {
		this.positionOffsetY = positionOffsetY;
	}
	public Double getPositionOffsetX3D() {
		return positionOffsetX3D;
	}
	public void setPositionOffsetX3D(Double positionOffsetX3D) {
		this.positionOffsetX3D = positionOffsetX3D;
	}
	public Double getPositionOffsetY3D() {
		return positionOffsetY3D;
	}
	public void setPositionOffsetY3D(Double positionOffsetY3D) {
		this.positionOffsetY3D = positionOffsetY3D;
	}
	public String getEngineType() {
		return engineType;
	}
	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}
	public String getContrastColor() {
		return contrastColor;
	}
	public void setContrastColor(String contrastColor) {
		this.contrastColor = contrastColor;
	}
	
	

	
}
