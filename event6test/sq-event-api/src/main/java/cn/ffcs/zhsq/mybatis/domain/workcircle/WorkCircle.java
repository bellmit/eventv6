package cn.ffcs.zhsq.mybatis.domain.workcircle;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;

/**
 * @Description: 工作圈表模块bo对象
 * @Author: zhengyi
 * @Date: 2019-09-09
 * @Copyright: 2019 福富软件
 */
public class WorkCircle implements Serializable {

	private Long wcId; // 工作圈编号
	private String wcType; // 工作圈类型：1 事件 2 后期扩展
	private String opType; // 操作类型 1 上报 2 受理 3 结案 4 上报并结案
	private String opTypeName; // 操作类型 1 上报 2 受理 3 结案 4 上报并结案
	private Date opTime; // 操作时间
	private String opTimeStr; // 操作事件(str)
	private Long opUserId; // 操作人员编号
	private String opUserName; // 操作人员名称
	private Long opDeptId; // 操作部门ID
	private String opDeptCode; // 操作部门
	private String opDeptName; // 操作部门名称
	private String nextUserId; // 下一办理人
	private String nextUserName; // 下一办理人名称
	private String nextDeptCode; // 下一办理部门
	private String nextDeptName; // 下一办理部门名称
	private Long eventId; // 事件编号
	private String eventTitle; // 事件主题
	private Date eventTime; // 事发时间
	private String eventTimeStr; // 事发时间(str)
	private String eventAddr; // 事发地址
	private String eventContent; // 事发地址
	private String remark; // 备注
	private String isValid; // 0 无效 1 有效
	private Long createBy; // 创建人
	private Date createTime; // 创建时间
	private Long updateBy; // 修改人
	private Date updateTime; // 修改时间

	private Integer reportDept;// 上报
	private Integer acceptanceDept;// 受理
	private Integer handleDept;// 办结
	private Integer timeoutDept;// 延期

	private Integer acceptanceMonth;// 按月受理
	private Integer acceptanceYear;// 按年受理
	private Integer handleMonth;// 按月办结
	private Integer handleYear;// 按年办结

	private String opTimeYear;
	private String opTimeMonth;

	private String workflowName;

	private List<Attachment> listAtta = new ArrayList<Attachment>();

	public Long getWcId() {
		return wcId;
	}

	public void setWcId(Long wcId) {
		this.wcId = wcId;
	}

	public String getWcType() {
		return wcType;
	}

	public void setWcType(String wcType) {
		this.wcType = wcType;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public Long getOpUserId() {
		return opUserId;
	}

	public void setOpUserId(Long opUserId) {
		this.opUserId = opUserId;
	}

	public String getOpUserName() {
		return opUserName;
	}

	public void setOpUserName(String opUserName) {
		this.opUserName = opUserName;
	}

	public String getOpDeptCode() {
		return opDeptCode;
	}

	public void setOpDeptCode(String opDeptCode) {
		this.opDeptCode = opDeptCode;
	}

	public String getOpDeptName() {
		return opDeptName;
	}

	public void setOpDeptName(String opDeptName) {
		this.opDeptName = opDeptName;
	}

	public String getNextUserId() {
		return nextUserId;
	}

	public void setNextUserId(String nextUserId) {
		this.nextUserId = nextUserId;
	}

	public String getNextUserName() {
		return nextUserName;
	}

	public void setNextUserName(String nextUserName) {
		this.nextUserName = nextUserName;
	}

	public String getNextDeptCode() {
		return nextDeptCode;
	}

	public void setNextDeptCode(String nextDeptCode) {
		this.nextDeptCode = nextDeptCode;
	}

	public String getNextDeptName() {
		return nextDeptName;
	}

	public void setNextDeptName(String nextDeptName) {
		this.nextDeptName = nextDeptName;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public String getEventAddr() {
		return eventAddr;
	}

	public void setEventAddr(String eventAddr) {
		this.eventAddr = eventAddr;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getEventTimeStr() {
		return eventTimeStr;
	}

	public void setEventTimeStr(String eventTimeStr) {
		this.eventTimeStr = eventTimeStr;
	}

	public Integer getReportDept() {
		return reportDept;
	}

	public void setReportDept(Integer reportDept) {
		this.reportDept = reportDept;
	}

	public Integer getAcceptanceDept() {
		return acceptanceDept;
	}

	public void setAcceptanceDept(Integer acceptanceDept) {
		this.acceptanceDept = acceptanceDept;
	}

	public Integer getHandleDept() {
		return handleDept;
	}

	public void setHandleDept(Integer handleDept) {
		this.handleDept = handleDept;
	}

	public Integer getTimeoutDept() {
		return timeoutDept;
	}

	public void setTimeoutDept(Integer timeoutDept) {
		this.timeoutDept = timeoutDept;
	}

	public Integer getAcceptanceYear() {
		return acceptanceYear;
	}

	public void setAcceptanceYear(Integer acceptanceYear) {
		this.acceptanceYear = acceptanceYear;
	}

	public Integer getHandleYear() {
		return handleYear;
	}

	public void setHandleYear(Integer handleYear) {
		this.handleYear = handleYear;
	}

	public Integer getAcceptanceMonth() {
		return acceptanceMonth;
	}

	public void setAcceptanceMonth(Integer acceptanceMonth) {
		this.acceptanceMonth = acceptanceMonth;
	}

	public Integer getHandleMonth() {
		return handleMonth;
	}

	public void setHandleMonth(Integer handleMonth) {
		this.handleMonth = handleMonth;
	}

	public String getOpTimeStr() {
		if (opTimeStr == null || opTimeStr.trim().length() == 0) {
			if (opTime != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				opTimeStr = sdf.format(opTime);
			}
		}
		return opTimeStr;
	}

	public void setOpTimeStr(String opTimeStr) {
		this.opTimeStr = opTimeStr;
	}

	public String getOpTimeYear() {
		return opTimeYear;
	}

	public void setOpTimeYear(String opTimeYear) {
		this.opTimeYear = opTimeYear;
	}

	public String getOpTimeMonth() {
		return opTimeMonth;
	}

	public void setOpTimeMonth(String opTimeMonth) {
		this.opTimeMonth = opTimeMonth;
	}

	public Long getOpDeptId() {
		return opDeptId;
	}

	public void setOpDeptId(Long opDeptId) {
		this.opDeptId = opDeptId;
	}

	public List<Attachment> getListAtta() {
		return listAtta;
	}

	public void setListAtta(List<Attachment> listAtta) {
		this.listAtta = listAtta;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getEventContent() {
		return eventContent;
	}

	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}

	public String getOpTypeName() {
		return opTypeName;
	}

	public void setOpTypeName(String opTypeName) {
		this.opTypeName = opTypeName;
	}

}