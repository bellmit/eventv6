package cn.ffcs.zhsq.mybatis.domain.event;

import java.io.Serializable;
/**
 * 涉及线路事件 T_SJ_EVENT_INCIDENT_INVOLVED
 * @author 
 *
 */
public class IncidentInvolved implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5468598533246926519L;
	//INVOLVED_ID,EVENT_ID,LOT_ID,IS_DETECTION,FUGITIVE_AMOUNT,ARRESTED_AMOUNT,DETECTED_DESC,STATUS
	private Long involvedId;//主键
	private Long eventId;//事件ID
	private Long lotId;//地段ID
	private String isDetection;//是否破案
	private String isDetectionName;
	public String getIsDetectionName() {
		return isDetectionName;
	}
	public void setIsDetectionName(String isDetectionName) {
		this.isDetectionName = isDetectionName;
	}
	private Long fugitiveAmount;//在逃人数
	private Long arrestedAmount;//抓捕人数
	private String detectedDesc;//案件侦破情况
	private String status;//状态
	public Long getInvolvedId() {
		return involvedId;
	}
	public void setInvolvedId(Long involvedId) {
		this.involvedId = involvedId;
	}
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public Long getLotId() {
		return lotId;
	}
	public void setLotId(Long lotId) {
		this.lotId = lotId;
	}
	public String getIsDetection() {
		return isDetection;
	}
	public void setIsDetection(String isDetection) {
		this.isDetection = isDetection;
	}
	public Long getFugitiveAmount() {
		return fugitiveAmount;
	}
	public void setFugitiveAmount(Long fugitiveAmount) {
		this.fugitiveAmount = fugitiveAmount;
	}
	public Long getArrestedAmount() {
		return arrestedAmount;
	}
	public void setArrestedAmount(Long arrestedAmount) {
		this.arrestedAmount = arrestedAmount;
	}
	public String getDetectedDesc() {
		return detectedDesc;
	}
	public void setDetectedDesc(String detectedDesc) {
		this.detectedDesc = detectedDesc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
