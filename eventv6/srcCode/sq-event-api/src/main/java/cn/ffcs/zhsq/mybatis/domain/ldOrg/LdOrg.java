package cn.ffcs.zhsq.mybatis.domain.ldOrg;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.ffcs.zhsq.mybatis.domain.addressBook.AddressBook;

/**
 * @Description: 联动单位管理表模块bo对象
 * @Author: linwd
 * @Date: 04-13 15:21:53
 * @Copyright: 2018 福富软件
 */
public class LdOrg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long ldId; //主键ID
	private String ldType; //单位类型:0；联动队伍、1：专业化队伍
	private String ldName; //名称
	private String orgCode; //组织编码
	private Long orgId; //组织ID
	
	//联动单位
	private Long leaderId; 
	private String leader; //分管领导
	private String leaderDuty; //分管领导职务
	private String leaderMobile; //分管领导联系方式
	private Long contacterId; 
	private String contacter; //联络人
	private String contacterDuty; //联络人职务
	private String contacterMobile; //联络人联系方式
	
	//专业化队伍
	private Long lxrId; 
	private String lxr; //联络人
	private String lxrMobile; //联络人联系方式
	private String ldItem; //服务项目 : 00开锁装所 01家电维修 02电脑维修 03汽车服务 04灭四害
	private String ldPrice; //原价
	private String ldDisCount; //折后价
	private String ldServer; //材料及服务费
	
	private String ldBz; //备注
	private Long creater; //创建人
	private Date createTime; //创建时间
	private Long updater; //修改人
	private Date updateTime; //修改时间
	private String status; //状态：1有效 0删除

	private List<AddressBook> addressBookList;//单位联系人list

	public Long getLdId() {
		return ldId;
	}
	public void setLdId(Long ldId) {
		this.ldId = ldId;
	}
	public String getLdType() {
		return ldType;
	}
	public void setLdType(String ldType) {
		this.ldType = ldType;
	}
	public String getLdName() {
		return ldName;
	}
	public void setLdName(String ldName) {
		this.ldName = ldName;
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
	public String getLdBz() {
		return ldBz;
	}
	public void setLdBz(String ldBz) {
		this.ldBz = ldBz;
	}
	public String getLdItem() {
		return ldItem;
	}
	public void setLdItem(String ldItem) {
		this.ldItem = ldItem;
	}
	public String getLdPrice() {
		return ldPrice;
	}
	public void setLdPrice(String ldPrice) {
		this.ldPrice = ldPrice;
	}
	public String getLdDisCount() {
		return ldDisCount;
	}
	public void setLdDisCount(String ldDisCount) {
		this.ldDisCount = ldDisCount;
	}
	public String getLdServer() {
		return ldServer;
	}
	public void setLdServer(String ldServer) {
		this.ldServer = ldServer;
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
	public List<AddressBook> getAddressBookList() {
		return addressBookList;
	}
	public void setAddressBookList(List<AddressBook> addressBookList) {
		this.addressBookList = addressBookList;
	}
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	public String getLeaderDuty() {
		return leaderDuty;
	}
	public void setLeaderDuty(String leaderDuty) {
		this.leaderDuty = leaderDuty;
	}
	public String getLeaderMobile() {
		return leaderMobile;
	}
	public void setLeaderMobile(String leaderMobile) {
		this.leaderMobile = leaderMobile;
	}
	public String getContacter() {
		return contacter;
	}
	public void setContacter(String contacter) {
		this.contacter = contacter;
	}
	public String getContacterDuty() {
		return contacterDuty;
	}
	public void setContacterDuty(String contacterDuty) {
		this.contacterDuty = contacterDuty;
	}
	public String getContacterMobile() {
		return contacterMobile;
	}
	public void setContacterMobile(String contacterMobile) {
		this.contacterMobile = contacterMobile;
	}
	public String getLxr() {
		return lxr;
	}
	public void setLxr(String lxr) {
		this.lxr = lxr;
	}
	public String getLxrMobile() {
		return lxrMobile;
	}
	public void setLxrMobile(String lxrMobile) {
		this.lxrMobile = lxrMobile;
	}
	public Long getLeaderId() {
		return leaderId;
	}
	public void setLeaderId(Long leaderId) {
		this.leaderId = leaderId;
	}
	public Long getContacterId() {
		return contacterId;
	}
	public void setContacterId(Long contacterId) {
		this.contacterId = contacterId;
	}
	public Long getLxrId() {
		return lxrId;
	}
	public void setLxrId(Long lxrId) {
		this.lxrId = lxrId;
	}


}