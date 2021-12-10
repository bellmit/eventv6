package cn.ffcs.zhsq.mybatis.domain.ypms.mscase;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 延平民生派发单位处理过程表模块bo对象
 * @Author: zhangzhenhai
 * @Date: 05-03 14:34:50
 * @Copyright: 2018 福富软件
 */
public class DeptHandle implements Serializable {

	/**
	 * @author zhangzhenhai
	 * @date 2018-5-3 下午2:37:18
	 */
	private static final long serialVersionUID = -4009555722683359370L;
	private Long rdhId; //主键ID
	private Long relacaseId; //案件ID
	private Long rdId; //派发单位ID
	private String rdName; //派发单位名称
	private String rdOrgCode; //派发单位orgCode
	private String handleProcess; //处理过程
	private String handleResult; //处理结果
	private String currStatus; //当前状态:01正常；02回退
	private String createName; //创建人姓名
	private Date createTime; //创建时间
	private Long createId; //创建人ID
	private Date updateTime; //更新时间
	private Long updateId; //更新人ID
	private String delFlag; //0：无效；1：有效

	private String createTimeStr; //创建时间
	private String updateTimeStr; //更新时间

	public Long getRdhId() {
		return rdhId;
	}
	public void setRdhId(Long rdhId) {
		this.rdhId = rdhId;
	}
	public Long getRelacaseId() {
		return relacaseId;
	}
	public void setRelacaseId(Long relacaseId) {
		this.relacaseId = relacaseId;
	}
	public Long getRdId() {
		return rdId;
	}
	public void setRdId(Long rdId) {
		this.rdId = rdId;
	}
	public String getRdName() {
		return rdName;
	}
	public void setRdName(String rdName) {
		this.rdName = rdName;
	}
	public String getHandleProcess() {
		return handleProcess;
	}
	public void setHandleProcess(String handleProcess) {
		this.handleProcess = handleProcess;
	}
	public String getHandleResult() {
		return handleResult;
	}
	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getCreateId() {
		return createId;
	}
	public void setCreateId(Long createId) {
		this.createId = createId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getUpdateId() {
		return updateId;
	}
	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getUpdateTimeStr() {
		return updateTimeStr;
	}
	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}
	public String getRdOrgCode() {
		return rdOrgCode;
	}
	public void setRdOrgCode(String rdOrgCode) {
		this.rdOrgCode = rdOrgCode;
	}
	public String getCurrStatus() {
		return currStatus;
	}
	public void setCurrStatus(String currStatus) {
		this.currStatus = currStatus;
	}


}