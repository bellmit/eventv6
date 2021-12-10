package cn.ffcs.zhsq.mybatis.domain.addressBook;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 通讯录表模块bo对象
 * @Author: linwd
 * @Date: 04-13 15:24:43
 * @Copyright: 2018 福富软件
 */
public class AddressBook implements Serializable {

	private Long abId; //主键ID
	private Long ldId; //联动单位ID
	private String abName; //姓名
	private String abMobile; //联系方式
	private String abRole; //角色：0分管领导 1联络员 2联系人 
	private String abDuty; //职务
	private String orgCode; //组织编码
	private Long orgId; //组织ID
	private Long creater; //创建人
	private Date createTime; //创建时间
	private Long updater; //修改人
	private Date updateTime; //修改时间
	private String status; //状态
	private String ldName; //部门名称

	public String getLdName() {
		return ldName;
	}
	public void setLdName(String ldName) {
		this.ldName = ldName;
	}
	public Long getAbId() {
		return abId;
	}
	public void setAbId(Long abId) {
		this.abId = abId;
	}
	public Long getLdId() {
		return ldId;
	}
	public void setLdId(Long ldId) {
		this.ldId = ldId;
	}
	public String getAbName() {
		return abName;
	}
	public void setAbName(String abName) {
		this.abName = abName;
	}
	public String getAbMobile() {
		return abMobile;
	}
	public void setAbMobile(String abMobile) {
		this.abMobile = abMobile;
	}
	public String getAbRole() {
		return abRole;
	}
	public void setAbRole(String abRole) {
		this.abRole = abRole;
	}
	public String getAbDuty() {
		return abDuty;
	}
	public void setAbDuty(String abDuty) {
		this.abDuty = abDuty;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getCreater() {
		return creater;
	}
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getUpdater() {
		return updater;
	}
	public void setUpdater(Long updater) {
		this.updater = updater;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}


}