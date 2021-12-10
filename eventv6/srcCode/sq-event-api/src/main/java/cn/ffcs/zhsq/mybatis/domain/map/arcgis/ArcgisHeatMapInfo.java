package cn.ffcs.zhsq.mybatis.domain.map.arcgis;

import java.io.Serializable;

public class ArcgisHeatMapInfo implements Serializable {

	private static final long serialVersionUID = -7889369036502850508L;
	private Double x;// 中心点x
	private Double y;// 中心点y
	private Long count;

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
