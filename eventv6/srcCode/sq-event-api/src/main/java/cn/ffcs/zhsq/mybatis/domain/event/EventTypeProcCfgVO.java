package cn.ffcs.zhsq.mybatis.domain.event;

import java.io.Serializable;
import java.util.List;

public class EventTypeProcCfgVO implements Serializable {

	private static final long serialVersionUID = -5891165338507261264L;
	
	private String regionCode;
	private String type;

	private List<Long> etpcIds;
	private List<String> collectEventSpecs;
	private List<String> timeLimitTypes;
	private List<Integer> timeLimitVals;
	private List<String> eventCloseSpecs;
	
	private String regionName;
	private String typeName;

	public List<Long> getEtpcIds() {
		return etpcIds;
	}

	public void setEtpcIds(List<Long> etpcIds) {
		this.etpcIds = etpcIds;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getCollectEventSpecs() {
		return collectEventSpecs;
	}

	public void setCollectEventSpecs(List<String> collectEventSpecs) {
		this.collectEventSpecs = collectEventSpecs;
	}

	public List<String> getTimeLimitTypes() {
		return timeLimitTypes;
	}

	public void setTimeLimitTypes(List<String> timeLimitTypes) {
		this.timeLimitTypes = timeLimitTypes;
	}

	public List<Integer> getTimeLimitVals() {
		return timeLimitVals;
	}

	public void setTimeLimitVals(List<Integer> timeLimitVals) {
		this.timeLimitVals = timeLimitVals;
	}

	public List<String> getEventCloseSpecs() {
		return eventCloseSpecs;
	}

	public void setEventCloseSpecs(List<String> eventCloseSpecs) {
		this.eventCloseSpecs = eventCloseSpecs;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
