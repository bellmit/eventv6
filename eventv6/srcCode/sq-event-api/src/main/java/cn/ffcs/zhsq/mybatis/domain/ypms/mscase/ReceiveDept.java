package cn.ffcs.zhsq.mybatis.domain.ypms.mscase;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.ffcs.zhsq.mybatis.domain.addressBook.AddressBook;

/**
 * @Description: 延平民生派发单位表模块bo对象
 * @Author: zhangzhenhai
 * @Date: 04-25 11:05:00
 * @Copyright: 2018 福富软件
 */
public class ReceiveDept implements Serializable {

	/**
	 * @author zhangzhenhai
	 * @date 2018-4-25 上午11:05:50
	 */
	private static final long serialVersionUID = -7260904491121409166L;
	private Long rdId; //主键ID
	private Long relaCaseId; //关联案件ID
	private Long ldorgId; //联动单位表主键ID
	private String handleStatus; //01：待办；02：反馈；03：驳回；04：中心代处理
	private String msgSendPepo; //短信发送的人：01：分管领导；02：联络员；12：分管领导+联络员
	private Date createTime; //创建时间
	private Long createId; //创建人ID
	private String delFlag; //0：无效；1：有效

	//冗余字段
	private String ldType; //单位类型:0；联动队伍、1：专业化队伍
	private String ldName; //名称
	private String orgCode; //组织编码
	private Long orgId; //组织ID
	private List<AddressBook> addressBookList;//单位联系人list(分管联动，联络员，业务热线)

	public Long getRdId() {
		return rdId;
	}
	public void setRdId(Long rdId) {
		this.rdId = rdId;
	}
	public Long getRelaCaseId() {
		return relaCaseId;
	}
	public void setRelaCaseId(Long relaCaseId) {
		this.relaCaseId = relaCaseId;
	}
	public Long getLdorgId() {
		return ldorgId;
	}
	public void setLdorgId(Long ldorgId) {
		this.ldorgId = ldorgId;
	}
	public String getHandleStatus() {
		return handleStatus;
	}
	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
	}
	public String getMsgSendPepo() {
		return msgSendPepo;
	}
	public void setMsgSendPepo(String msgSendPepo) {
		this.msgSendPepo = msgSendPepo;
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
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getLdName() {
		return ldName;
	}
	public void setLdName(String ldName) {
		this.ldName = ldName;
	}
	public List<AddressBook> getAddressBookList() {
		return addressBookList;
	}
	public void setAddressBookList(List<AddressBook> addressBookList) {
		this.addressBookList = addressBookList;
	}
	public String getLdType() {
		return ldType;
	}
	public void setLdType(String ldType) {
		this.ldType = ldType;
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


}