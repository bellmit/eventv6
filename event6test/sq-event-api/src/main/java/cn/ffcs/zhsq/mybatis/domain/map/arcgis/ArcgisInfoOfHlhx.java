package cn.ffcs.zhsq.mybatis.domain.map.arcgis;

/**
 * 
 * @author huangmw
 * 
 */
public class ArcgisInfoOfHlhx extends ArcgisInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1128334898879892432L;

	private String lotId;
	private String lotName;
	private String gridName;// 网格名称

	public String getLotId() {
		return lotId;
	}

	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	public String getLotName() {
		return lotName;
	}

	public void setLotName(String lotName) {
		this.lotName = lotName;
	}

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

}
