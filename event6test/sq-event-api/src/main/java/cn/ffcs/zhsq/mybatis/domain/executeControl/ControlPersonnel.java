package cn.ffcs.zhsq.mybatis.domain.executeControl;


import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.zhsq.mybatis.domain.pages.Pages;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 布控库对象模块bo对象
 * @Author: dtj
 * @Date: 07-17 09:21:58
 * @table: 表信息描述 T_CONTROL_PERSONNEL 布控库对象  布控库对象  序列SEQ_T_CONTROL_PERSONNEL
 * @Copyright: 2020 福富软件
 */
public class ControlPersonnel extends Pages implements Serializable {

  private static final long serialVersionUID = 1L;

	private Long id; //主键ID 	VARCHAR(24)
	private String controlObjectId; //布控对象ID 	VARCHAR(24)
	private String myControlLibraryId; //自己的布控库ID 	VARCHAR(24)
	private String controlLibraryId; //接口中布控库ID 	VARCHAR(24)
	private String name; //姓名 	VARCHAR(50)
	private String gender; //性别 	CHAR(1)
	private String mobile; //手机号 	VARCHAR(11)
	private Date birthday; //出生日期 	DATE
	private String nationality; //民族 	VARCHAR(12)
	private String nationalityCN; //民族 	VARCHAR(12)
	private String identityCardNumber; //身份证号 	VARCHAR(18)
	private String description; //描述 	VARCHAR(200)
	private String isValid; //是否有效 	CHAR(1)
	private Long creator; //创建人 	NUMBER(9)
	private Date createTime; //创建时间 	DATE
	private Long updator; //更新人 	NUMBER(9)
	private Date updateTime; //更新时间 	DATE
	private Long gridId; //所属网格 	NUMBER(24)
	private String gridCode; //网格编码 	VARCHAR(24)
	private String gridName; //网格名称 	VARCHAR(24)
	private String gridPath; //网格全路径 	VARCHAR(50)
	private Integer similarity;//相似度
	private String libType;//布控库类型
	private String imageUrl;//图片URL
	private Long[] attachmentId;//附件ID集合
	private List<Attachment> attList = null;//附件列表
	private String controlTaskId;//布控任务ID

	public String getControlTaskId() {
		return controlTaskId;
	}

	public void setControlTaskId(String controlTaskId) {
		this.controlTaskId = controlTaskId;
	}

	public Long[] getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(Long[] attachmentId) {
		this.attachmentId = attachmentId;
	}
	public List<Attachment> getAttList() {
		return attList;
	}
	public void setAttList(List<Attachment> attList) {
		this.attList = attList;
	}
	public String getLibType() {
		return libType;
	}
	public void setLibType(String libType) {
		this.libType = libType;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Integer getSimilarity() {
		return similarity;
	}
	public void setSimilarity(Integer similarity) {
		this.similarity = similarity;
	}
	public String getNationalityCN() {
		return nationalityCN;
	}
	public void setNationalityCN(String nationalityCN) {
		this.nationalityCN = nationalityCN;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getControlObjectId() {  //布控对象ID
		return controlObjectId;
	}
	public void setControlObjectId(String controlObjectId) { //布控对象ID
		this.controlObjectId = controlObjectId;
	}
	public String getMyControlLibraryId() {  //自己的布控库ID
		return myControlLibraryId;
	}
	public void setMyControlLibraryId(String myControlLibraryId) { //自己的布控库ID
		this.myControlLibraryId = myControlLibraryId;
	}
	public String getControlLibraryId() {  //接口中布控库ID
		return controlLibraryId;
	}
	public void setControlLibraryId(String controlLibraryId) { //接口中布控库ID
		this.controlLibraryId = controlLibraryId;
	}
	public String getName() {  //姓名
		return name;
	}
	public void setName(String name) { //姓名
		this.name = name;
	}
	public String getGender() {  //性别
		return gender;
	}
	public void setGender(String gender) { //性别
		this.gender = gender;
	}
	public String getMobile() {  //手机号
		return mobile;
	}
	public void setMobile(String mobile) { //手机号
		this.mobile = mobile;
	}
	public Date getBirthday() {  //出生日期
		return birthday;
	}
	public void setBirthday(Date birthday) { //出生日期
		this.birthday = birthday;
	}
	public String getNationality() {  //民族
		return nationality;
	}
	public void setNationality(String nationality) { //民族
		this.nationality = nationality;
	}
	public String getIdentityCardNumber() {  //身份证号
		return identityCardNumber;
	}
	public void setIdentityCardNumber(String identityCardNumber) { //身份证号
		this.identityCardNumber = identityCardNumber;
	}
	public String getDescription() {  //描述
		return description;
	}
	public void setDescription(String description) { //描述
		this.description = description;
	}
	public String getIsValid() {  //是否有效
		return isValid;
	}
	public void setIsValid(String isValid) { //是否有效
		this.isValid = isValid;
	}
	public Long getCreator() {  //创建人
		return creator;
	}
	public void setCreator(Long creator) { //创建人
		this.creator = creator;
	}
	public Date getCreateTime() {  //创建时间
		return createTime;
	}
	public void setCreateTime(Date createTime) { //创建时间
		this.createTime = createTime;
	}
	public Long getUpdator() {  //更新人
		return updator;
	}
	public void setUpdator(Long updator) { //更新人
		this.updator = updator;
	}
	public Date getUpdateTime() {  //更新时间
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) { //更新时间
		this.updateTime = updateTime;
	}
	public Long getGridId() {  //所属网格
		return gridId;
	}
	public void setGridId(Long gridId) { //所属网格
		this.gridId = gridId;
	}
	public String getGridCode() {  //网格编码
		return gridCode;
	}
	public void setGridCode(String gridCode) { //网格编码
		this.gridCode = gridCode;
	}
	public String getGridName() {  //网格名称
		return gridName;
	}
	public void setGridName(String gridName) { //网格名称
		this.gridName = gridName;
	}
	public String getGridPath() {  //网格全路径
		return gridPath;
	}
	public void setGridPath(String gridPath) { //网格全路径
		this.gridPath = gridPath;
	}


}