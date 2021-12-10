package cn.ffcs.zhsq.mybatis.domain.ypms.mscase;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 延平民生案件操作员信息表模块bo对象
 * @Author: zhangzhenhai
 * @Date: 04-19 11:32:00
 * @Copyright: 2018 福富软件
 */
public class CaseHandler implements Serializable {

	/**
	 * @author zhangzhenhai
	 * @date 2018-4-19 上午11:34:10
	 */
	private static final long serialVersionUID = -752814057407107285L;
	private Long chId; //主键ID
	private Long handlerId; //操作员ID
	private String handlerName; //操作员姓名
	private Long handlerOrgId; //操作员单位ID
	private String handlerOrgCode; //操作员单位Code
	private String handlerTel; //操作员联系方式
	private Date handleTime; //操作时间（创建时间）
	private String handleContent; //操作意见
	private String handleType; //01：登记派发；02：委办局反馈处理；03：委办局驳回；04：中心代处理；05：中心回退；06：中心再次派发；07：中心回访；08：归档；09：归档前退回（退回到中心回访环节）
	private Long relaCaseId; //关联案件ID
	private String delFlag; //0：无效；1：有效

	private String handleTimeStr; //操作时间Str
	private String handlerOrgCN; //操作员单位名称

	public Long getChId() {
		return chId;
	}
	public void setChId(Long chId) {
		this.chId = chId;
	}
	public Long getHandlerId() {
		return handlerId;
	}
	public void setHandlerId(Long handlerId) {
		this.handlerId = handlerId;
	}
	public String getHandlerName() {
		return handlerName;
	}
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
	public Long getHandlerOrgId() {
		return handlerOrgId;
	}
	public void setHandlerOrgId(Long handlerOrgId) {
		this.handlerOrgId = handlerOrgId;
	}
	public String getHandlerOrgCode() {
		return handlerOrgCode;
	}
	public void setHandlerOrgCode(String handlerOrgCode) {
		this.handlerOrgCode = handlerOrgCode;
	}
	public String getHandlerTel() {
		return handlerTel;
	}
	public void setHandlerTel(String handlerTel) {
		this.handlerTel = handlerTel;
	}
	public Date getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	public String getHandleContent() {
		return handleContent;
	}
	public void setHandleContent(String handleContent) {
		this.handleContent = handleContent;
	}
	public String getHandleType() {
		return handleType;
	}
	public void setHandleType(String handleType) {
		this.handleType = handleType;
	}
	public Long getRelaCaseId() {
		return relaCaseId;
	}
	public void setRelaCaseId(Long relaCaseId) {
		this.relaCaseId = relaCaseId;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getHandleTimeStr() {
		return handleTimeStr;
	}
	public void setHandleTimeStr(String handleTimeStr) {
		this.handleTimeStr = handleTimeStr;
	}
	public String getHandlerOrgCN() {
		return handlerOrgCN;
	}
	public void setHandlerOrgCN(String handlerOrgCN) {
		this.handlerOrgCN = handlerOrgCN;
	}


}