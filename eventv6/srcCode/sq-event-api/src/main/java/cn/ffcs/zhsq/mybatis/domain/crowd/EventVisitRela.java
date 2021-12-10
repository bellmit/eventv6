package cn.ffcs.zhsq.mybatis.domain.crowd;

import java.io.Serializable;

/**
 * 事件-重点人员关联表
 * @author zhangyy
 *
 */
public class EventVisitRela implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -408164943294491873L;

	private Long visitId;
	
	private Long eventId;
	
	private Long associationId;
	
	public Long getVisitId() {
		return visitId;
	}
	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public Long getAssociationId() {
		return associationId;
	}
	public void setAssociationId(Long associationId) {
		this.associationId = associationId;
	}
}
