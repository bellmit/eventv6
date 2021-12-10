package cn.ffcs.zhsq.mybatis.domain.callInPerson;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 主叫人员管理模块bo对象
 * @Author: linwd
 * @Date: 04-12 14:55:55
 * @Copyright: 2018 福富软件
 */
public class CallInPerson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4492416264084211066L;
	private Long cpId; //主键ID
	private String cpName; //姓名
	private String cpMobile; //联系电话
	private String cpSex; //性别：01为男  02为女
	private Long creater; //创建人
	private Date createTime; //创建时间
	private Long updater; //修改人
	private Date updateTime; //修改时间
	private String status; //状态：0无效 1删除


	public Long getCpId() {
		return cpId;
	}
	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}
	public String getCpName() {
		return cpName;
	}
	public void setCpName(String cpName) {
		this.cpName = cpName;
	}
	public String getCpMobile() {
		return cpMobile;
	}
	public void setCpMobile(String cpMobile) {
		this.cpMobile = cpMobile;
	}
	public String getCpSex() {
		return cpSex;
	}
	public void setCpSex(String cpSex) {
		this.cpSex = cpSex;
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