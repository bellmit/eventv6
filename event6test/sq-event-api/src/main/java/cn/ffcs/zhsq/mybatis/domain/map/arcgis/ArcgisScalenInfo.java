package cn.ffcs.zhsq.mybatis.domain.map.arcgis;


import java.io.Serializable;

/**
 * 2014-05-16 liushi add
 * 地图加载的信息实体
 * @Table Map_Config_Gis
 * @author liushi
 */
public class ArcgisScalenInfo implements Serializable{

	private static final long serialVersionUID = 8033156781834113181L;
	
	private Integer arcgisScaleInfoId;//地图标尺ID
	private Integer arcgisConfigInfoId;//地图配置id
	private Integer scaleLevel;//级别
	private Double scaleResolution;//分辨率
	private Double scaleScale;//标尺
	public Integer getArcgisScaleInfoId() {
		return arcgisScaleInfoId;
	}
	public void setArcgisScaleInfoId(Integer arcgisScaleInfoId) {
		this.arcgisScaleInfoId = arcgisScaleInfoId;
	}
	public Integer getArcgisConfigInfoId() {
		return arcgisConfigInfoId;
	}
	public void setArcgisConfigInfoId(Integer arcgisConfigInfoId) {
		this.arcgisConfigInfoId = arcgisConfigInfoId;
	}
	public Integer getScaleLevel() {
		return scaleLevel;
	}
	public void setScaleLevel(Integer scaleLevel) {
		this.scaleLevel = scaleLevel;
	}
	public Double getScaleResolution() {
		return scaleResolution;
	}
	public void setScaleResolution(Double scaleResolution) {
		this.scaleResolution = scaleResolution;
	}
	public Double getScaleScale() {
		return scaleScale;
	}
	public void setScaleScale(Double scaleScale) {
		this.scaleScale = scaleScale;
	}
	
	
	
}
