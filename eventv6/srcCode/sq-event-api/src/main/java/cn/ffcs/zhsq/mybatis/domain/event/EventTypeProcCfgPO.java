package cn.ffcs.zhsq.mybatis.domain.event;

import java.io.Serializable;

public class EventTypeProcCfgPO implements Serializable {

	private static final long serialVersionUID = -5891165338507261264L;
	private Long etpcId;
	private String regionCode;
	private String type;

	private String collectEventSpec;
	private String timeLimitType;
	private Integer timeLimitVal;
	private String eventCloseSpec;
	
	private String regionName;

	public Long getEtpcId() {
		return etpcId;
	}

	public void setEtpcId(Long etpcId) {
		this.etpcId = etpcId;
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

	public String getCollectEventSpec() {
		return collectEventSpec;
	}

	public void setCollectEventSpec(String collectEventSpec) {
		this.collectEventSpec = collectEventSpec;
	}

	public String getTimeLimitType() {
		return timeLimitType;
	}

	public void setTimeLimitType(String timeLimitType) {
		this.timeLimitType = timeLimitType;
	}

	public Integer getTimeLimitVal() {
		return timeLimitVal;
	}

	public void setTimeLimitVal(Integer timeLimitVal) {
		this.timeLimitVal = timeLimitVal;
	}

	public String getEventCloseSpec() {
		return eventCloseSpec;
	}

	public void setEventCloseSpec(String eventCloseSpec) {
		this.eventCloseSpec = eventCloseSpec;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

}
