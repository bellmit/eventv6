package cn.ffcs.zhsq.mybatis.domain.map.arcgis;


/**
 * 2019-07-11 sunzhn add
 * 
 * @author sunzhn
 *
 */
public class ArcgisInfoOfPoint extends ArcgisInfo {
	
	private static final long serialVersionUID = 1L;
	
	private String pointNo;
	private String pointAddress;
	private String pointName;
	private Long gridId;
	private String pointLevel;
	private String infoOrgCode;
	private String pointManager;
	private String pmTel;
	private String checkType;
	private String pointType;
	private String pointTypeStr;
	private String pointLevelStr;
	private String gridPath;//所属县区街道名称

	public String getGridPath() { return gridPath; }

	public void setGridPath(String gridPath) { this.gridPath = gridPath; }

	public String getPointLevel() {
		return pointLevel;
	}

	public void setPointLevel(String pointLevel) {
		this.pointLevel = pointLevel;
	}

	public String getInfoOrgCode() {
		return infoOrgCode;
	}

	public void setInfoOrgCode(String infoOrgCode) {
		this.infoOrgCode = infoOrgCode;
	}

	public String getPointManager() {
		return pointManager;
	}

	public void setPointManager(String pointManager) {
		this.pointManager = pointManager;
	}

	public String getPmTel() {
		return pmTel;
	}

	public void setPmTel(String pmTel) {
		this.pmTel = pmTel;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public Long getGridId() {
		return gridId;
	}

	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getPointType() {
		return pointType;
	}

	public void setPointType(String pointType) {
		this.pointType = pointType;
	}

	public String getPointTypeStr() {
		return pointTypeStr;
	}

	public void setPointTypeStr(String pointTypeStr) {
		this.pointTypeStr = pointTypeStr;
	}

	public String getPointNo() {
		return pointNo;
	}

	public void setPointNo(String pointNo) {
		this.pointNo = pointNo;
	}

	public String getPointAddress() {
		return pointAddress;
	}

	public void setPointAddress(String pointAddress) {
		this.pointAddress = pointAddress;
	}

	public String getPointLevelStr() {
		return pointLevelStr;
	}

	public void setPointLevelStr(String pointLevelStr) {
		this.pointLevelStr = pointLevelStr;
	}
	
	
}
