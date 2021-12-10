package cn.ffcs.zhsq.mybatis.domain.warningScheme;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 预警方案模块：方案判定匹配
 * @Author: youwj
 * @Date: 05-28 15:32:01
 * @Copyright: 2019 福富软件
 */
public class SchemeMatch implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long schemeId;//方案主键
	private String schemeName;//方案名称
	private String bizType;//业务模块,默认'01'
	private String status;//业务状态,默认'1'
	private String isValid;//记录状态,默认'1'
	private Long creator;//创建人Id
	private Date createTime;//创建时间,默认当前时间
	private Long updator;//修改人
	private Date updateTime;//修改时间
	private String remark;//备注说明
	public Long getSchemeId() {
		return schemeId;
	}
	public void setSchemeId(Long schemeId) {
		this.schemeId = schemeId;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
