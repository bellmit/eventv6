package cn.ffcs.zhsq.mybatis.domain.planStaffing;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 应急预案管理模块
 * @Author: LINZHU
 * @Date: 04-22 16:31:15
 * @Copyright: 2021 福富软件
 */
public class EmergencyPlan implements Serializable {
	private static final long serialVersionUID = 1L;
	private String planId; //预案主键
	private String planName; //预案名称
	private String planType; //预案类型
	private String planTypeName; //预案类型名称
	private String planContent; //预案内容
	private String remark; //备注
	private Long creator; //创建人
	private Date createTime; //创建时间
	private Long updator; //更新人
	private Date updateTime; //更新时间
	private String isValid; //记录状态（0-无效，1-有效）
	private String regionCode; //地域编码
	private String regionName; //地域名称

	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getPlanType() {
		return planType;
	}
	public void setPlanType(String planType) {
		this.planType = planType;
	}
	public String getPlanContent() {
		return planContent;
	}
	public void setPlanContent(String planContent) {
		this.planContent = planContent;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getPlanTypeName() {
		return planTypeName;
	}
	public void setPlanTypeName(String planTypeName) {
		this.planTypeName = planTypeName;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}


}