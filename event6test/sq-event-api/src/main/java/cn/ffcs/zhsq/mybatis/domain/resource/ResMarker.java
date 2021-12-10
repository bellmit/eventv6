package cn.ffcs.zhsq.mybatis.domain.resource;

import java.io.Serializable;

/**
 * 资源标注信息
 * @Table T_ZY_RES_MARKER
 * @SEQUENCE seq_resmarker_id
 * @author guohh
 *
 */
public class ResMarker implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 589598690875751793L;
	
	private Long markerId;// 标注ID
	private String catalog;//01组织库  02资源库 
	private String markerType;//0101党组织   01020501全球眼  0103 事件
	private Long resourcesId;//
	private String mapType;//01二维、02三维
	private String x;//经度
	private String y;//纬度
	private String hs;//点位信息
	private String colordesc; //颜色
	private String levnum; //显示级别

	public Long getMarkerId() {
		return markerId;
	}

	public void setMarkerId(Long markerId) {
		this.markerId = markerId;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog == null ? null : catalog.trim();
	}

	public String getMarkerType() {
		return markerType;
	}

	public void setMarkerType(String markerType) {
		this.markerType = markerType == null ? null : markerType.trim();
	}

	public Long getResourcesId() {
		return resourcesId;
	}

	public void setResourcesId(Long resourcesId) {
		this.resourcesId = resourcesId;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType == null ? null : mapType.trim();
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x == null ? null : x.trim();
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y == null ? null : y.trim();
	}

	public String getHs() {
		return hs;
	}

	public void setHs(String hs) {
		this.hs = hs;
	}

	public String getColordesc() {
		return colordesc;
	}

	public void setColordesc(String colordesc) {
		this.colordesc = colordesc;
	}

	public String getLevnum() {
		return levnum;
	}

	public void setLevnum(String levnum) {
		this.levnum = levnum;
	}
}