package cn.ffcs.zhsq.mybatis.domain.map.arcgis;


/**
 * 2014-06-10 zhanh add
 * 
 * @author zhanh
 *
 */
public class ArcgisInfoOfBuild extends ArcgisInfo {
	
	private static final long serialVersionUID = 1L;
	
	
	private String buildingName;
	private Long gridId;
	private Long foreignAidId;
	
	public Long getForeignAidId() {
		return foreignAidId;
	}

	public void setForeignAidId(Long foreignAidId) {
		this.foreignAidId = foreignAidId;
	}

	public Long getGridId() {
		return gridId;
	}

	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	
}
