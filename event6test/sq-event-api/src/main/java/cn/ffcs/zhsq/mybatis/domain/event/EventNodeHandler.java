package cn.ffcs.zhsq.mybatis.domain.event;

import java.io.Serializable;

public class EventNodeHandler implements Serializable {

	private static final long serialVersionUID = 6797961519761873167L;
	private Long userId;
	private Long orgId;

	public EventNodeHandler(Long userId, Long orgId) {
		this.userId = userId;
		this.orgId = orgId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}
