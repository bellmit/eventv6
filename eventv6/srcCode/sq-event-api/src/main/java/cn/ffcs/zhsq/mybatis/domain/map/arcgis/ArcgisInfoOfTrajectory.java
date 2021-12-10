package cn.ffcs.zhsq.mybatis.domain.map.arcgis;

import java.io.Serializable;

/**
 * 2014-05-16 liushi add
 * 
 * @author liushi
 *
 */
public class ArcgisInfoOfTrajectory extends ArcgisInfo {

	private static final long serialVersionUID = 2759762667292525991L;
	
	private String locateTime;
	private Long gridId;
	private String isInGrid;

	public Long getGridId() {
		return gridId;
	}

	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}

	public String getLocateTime() {
		return locateTime;
	}

	public void setLocateTime(String locateTime) {
		this.locateTime = locateTime;
	}

	public String getIsInGrid() {
		return isInGrid;
	}

	public void setIsInGrid(String isInGrid) {
		this.isInGrid = isInGrid;
	}
}
