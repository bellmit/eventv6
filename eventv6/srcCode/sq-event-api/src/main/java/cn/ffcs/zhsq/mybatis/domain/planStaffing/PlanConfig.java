package cn.ffcs.zhsq.mybatis.domain.planStaffing;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 应急预案配置模块bo对象
 * @Author: LINZHU
 * @Date: 04-23 15:47:50
 * @Copyright: 2021 福富软件
 */
public class PlanConfig implements Serializable {

	private static final long serialVersionUID = -9160009977389494734L;
	private String planConfigId; //预案配置主键
	private String planId; //预案主键
	private String planLevel; //预案等级
	private String planLevelName; //预案等级名称
	private Long creator; //创建人
	private Date createTime; //创建时间
	private Long updator; //更新人
	private Date updateTime; //更新时间
	private String isValid; //记录状态（0-无效，1-有效）


	public String getPlanConfigId() {
		return planConfigId;
	}
	public void setPlanConfigId(String planConfigId) {
		this.planConfigId = planConfigId;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getPlanLevel() {
		return planLevel;
	}
	public void setPlanLevel(String planLevel) {
		this.planLevel = planLevel;
	}
	public Long getCreator() {
		return creator;
	}
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getUpdator() {
		return updator;
	}
	public void setUpdator(Long updator) {
		this.updator = updator;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String getPlanLevelName() {
		return planLevelName;
	}
	public void setPlanLevelName(String planLevelName) {
		this.planLevelName = planLevelName;
	}


}