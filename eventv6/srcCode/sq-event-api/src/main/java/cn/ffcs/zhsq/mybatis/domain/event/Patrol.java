package cn.ffcs.zhsq.mybatis.domain.event;

import java.io.Serializable;
import java.util.Date;

public class Patrol implements Serializable{
    /**
	 * @文件描述: TODO
	 * @内容摘要: 
	 * @完成日期: Sep 28, 2014
	 * 修改日期			修改人
	 * Sep 28, 2014		zhongshm
	 */
	private static final long serialVersionUID = -1648492999760877853L;

	private Long patrolId;

    private String code;

    private String name;

    private String type;

    private String content;

    private String occurred;

    private Date startPatrolTime;

    private Date endPatrolTime;

    private String involvedNum;

    private String principal;

    private String principalTel;

    private String handleResult;

    private Long gridId;

    private String gridCode;

    private String status;

    private Date created;

    private Long creator;

    private String creatorName;

    private Date updated;

    private Long updator;

    private String updatorName;
    
    //扩展字段
    private String gridName;

    private String startPatrolTimeStr;

    private String endPatrolTimeStr;
    
    private String keyWord;
    
    private String bizType;

    public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public Long getPatrolId() {
        return patrolId;
    }

    public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public void setPatrolId(Long patrolId) {
        this.patrolId = patrolId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getOccurred() {
        return occurred;
    }

    public void setOccurred(String occurred) {
        this.occurred = occurred == null ? null : occurred.trim();
    }

    public Date getStartPatrolTime() {
        return startPatrolTime;
    }

    public void setStartPatrolTime(Date startPatrolTime) {
        this.startPatrolTime = startPatrolTime;
    }

    public Date getEndPatrolTime() {
        return endPatrolTime;
    }

    public void setEndPatrolTime(Date endPatrolTime) {
        this.endPatrolTime = endPatrolTime;
    }

    public String getInvolvedNum() {
        return involvedNum;
    }

    public void setInvolvedNum(String involvedNum) {
        this.involvedNum = involvedNum == null ? null : involvedNum.trim();
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal == null ? null : principal.trim();
    }

    public String getPrincipalTel() {
        return principalTel;
    }

    public void setPrincipalTel(String principalTel) {
        this.principalTel = principalTel == null ? null : principalTel.trim();
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult == null ? null : handleResult.trim();
    }

    public Long getGridId() {
        return gridId;
    }

    public void setGridId(Long gridId) {
        this.gridId = gridId;
    }

    public String getGridCode() {
        return gridCode;
    }

    public void setGridCode(String gridCode) {
        this.gridCode = gridCode == null ? null : gridCode.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName == null ? null : creatorName.trim();
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Long getUpdator() {
        return updator;
    }

    public void setUpdator(Long updator) {
        this.updator = updator;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName == null ? null : updatorName.trim();
    }

	public String getStartPatrolTimeStr() {
		return startPatrolTimeStr;
	}

	public void setStartPatrolTimeStr(String startPatrolTimeStr) {
		this.startPatrolTimeStr = startPatrolTimeStr;
	}

	public String getEndPatrolTimeStr() {
		return endPatrolTimeStr;
	}

	public void setEndPatrolTimeStr(String endPatrolTimeStr) {
		this.endPatrolTimeStr = endPatrolTimeStr;
	}
}