package cn.ffcs.zhsq.mybatis.domain.relatedEvents;

public class RelatedEventsForSearch extends RelatedEvents {
	private static final long serialVersionUID = 4911916366341048664L;
	private Long gridId;				//网格ID
	private String occuDateStartStr;	//发生起始时间
	private String occuDateEndStr;		//发生结束时间
	private String spyEndDateStartStr;	//侦查终结开始时间
	private String spyEndDateEndStr;	//侦查终结结束时间
	
	public Long getGridId() {
		return gridId;
	}
	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}
	public String getOccuDateStartStr() {
		return occuDateStartStr;
	}
	public void setOccuDateStartStr(String occuDateStartStr) {
		this.occuDateStartStr = occuDateStartStr;
	}
	public String getOccuDateEndStr() {
		return occuDateEndStr;
	}
	public void setOccuDateEndStr(String occuDateEndStr) {
		this.occuDateEndStr = occuDateEndStr;
	}
	public String getSpyEndDateStartStr() {
		return spyEndDateStartStr;
	}
	public void setSpyEndDateStartStr(String spyEndDateStartStr) {
		this.spyEndDateStartStr = spyEndDateStartStr;
	}
	public String getSpyEndDateEndStr() {
		return spyEndDateEndStr;
	}
	public void setSpyEndDateEndStr(String spyEndDateEndStr) {
		this.spyEndDateEndStr = spyEndDateEndStr;
	}
}
