package cn.ffcs.zhsq.mybatis.domain.map.arcgis;

import java.io.Serializable;

/**
 * 2014-05-16 liushi add
 * 
 * @author liushi
 *
 */
public class ArcgisInfoOfGrid extends ArcgisInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8529726825878587874L;
	private String infoOrgCode;
	private String gridCode;//网格code
	private String gridName;//网格名称
	private String gridPath;//网格路径
	private Integer mapCenterLevel;//当前网格层级
	
	

	public String getInfoOrgCode() {
		return infoOrgCode;
	}
	public void setInfoOrgCode(String infoOrgCode) {
		this.infoOrgCode = infoOrgCode;
	}
	public Integer getMapCenterLevel() {
		return mapCenterLevel;
	}
	public void setMapCenterLevel(Integer mapCenterLevel) {
		this.mapCenterLevel = mapCenterLevel;
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

	
}
