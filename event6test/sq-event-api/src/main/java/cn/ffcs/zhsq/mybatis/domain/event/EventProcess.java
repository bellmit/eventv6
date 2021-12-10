package cn.ffcs.zhsq.mybatis.domain.event;

import java.io.Serializable;
import java.util.Date;

public class EventProcess implements Serializable{
    /**
	 * @文件描述: TODO
	 * @内容摘要: 
	 * @完成日期: Sep 28, 2014
	 * 修改日期			修改人
	 * Sep 28, 2014		zhongshm
	 */
	private static final long serialVersionUID = -535250486494258331L;

	private Integer eventProcessId;

    private Long eventId;

    private String status;

    private Date createTime;

    private Date handleDate;
    
    private Long orgId;
    
    private Long userId;

    public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getEventProcessId() {
        return eventProcessId;
    }

    public void setEventProcessId(Integer eventProcessId) {
        this.eventProcessId = eventProcessId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getHandleDate() {
        return handleDate;
    }

    public void setHandleDate(Date handleDate) {
        this.handleDate = handleDate;
    }
}