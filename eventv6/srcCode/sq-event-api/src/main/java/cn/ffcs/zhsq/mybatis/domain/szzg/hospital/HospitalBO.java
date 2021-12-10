package cn.ffcs.zhsq.mybatis.domain.szzg.hospital;

import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;

import java.io.Serializable;
import java.util.Date;

public class HospitalBO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8322128607920142603L;
	private Long seqId;
	private Long hospitalId;
	private String hospitalName;
	private String orgCode;
	private String orgName;
	private String address;
	private String x;					//x坐标
	private String y;					//y坐标
	private ResMarker resMarker;

	public ResMarker getResMarker() {
		return resMarker;
	}

	public void setResMarker(ResMarker resMarker) {
		this.resMarker = resMarker;
	}

	private String longitude;			//经度
	private String latitude;			//纬度
	private String tel;					//联系电话
	private String url;					//官网
	private String mail;				//邮箱
	private String gobus;				//乘车路线
	private String hospitalLevel;		//医院等级
	private String hospitalNature;		//医院性质
	private String hospitalNatureName;
	private String hospitalMType;		//医保类型
	private String hospitalComment;		//医院评分
	private String imgPath;				
	private Date createTime;
	private String createTimeStr;
	private Date updateTime;
	private String status;
	private String type;
	private String typeName;
	private String contacts;
	private String legalRepresentative;
	private Long employeeNum;
	private String isUebmiFixedUnit;
	private String isNcmsFixedUnit;
	private String area;
	private String constructionArea;
	private Long hospitalBeds;
	private Long specialistNum;
	private String hospitalSummary;
	private String postcode;
	private String bloodBankStock;
	private String orgPath;

	private Long createUserId;
	private Long updateUserId;
	private Long hospitalNumber;
	private Long totalPoPu;

	public String getOrgPath() {
		return orgPath;
	}

	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getHospitalNumber() {
		return hospitalNumber;
	}

	public void setHospitalNumber(Long hospitalNumber) {
		this.hospitalNumber = hospitalNumber;
	}

	public Long getTotalPoPu() {
		return totalPoPu;
	}

	public void setTotalPoPu(Long totalPoPu) {
		this.totalPoPu = totalPoPu;
	}

	public Long getSeqId() {
		return seqId;
	}
	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}
	public Long getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getGobus() {
		return gobus;
	}
	public void setGobus(String gobus) {
		this.gobus = gobus;
	}
	public String getHospitalLevel() {
		return hospitalLevel;
	}
	public void setHospitalLevel(String hospitalLevel) {
		this.hospitalLevel = hospitalLevel;
	}
	public String getHospitalNature() {
		return hospitalNature;
	}
	public void setHospitalNature(String hospitalNature) {
		this.hospitalNature = hospitalNature;
	}
	public String getHospitalMType() {
		return hospitalMType;
	}
	public void setHospitalMType(String hospitalMType) {
		this.hospitalMType = hospitalMType;
	}
	public String getHospitalComment() {
		return hospitalComment;
	}
	public void setHospitalComment(String hospitalComment) {
		this.hospitalComment = hospitalComment;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getLegalRepresentative() {
		return legalRepresentative;
	}
	public void setLegalRepresentative(String legalRepresentative) {
		this.legalRepresentative = legalRepresentative;
	}
	public Long getEmployeeNum() {
		return employeeNum;
	}
	public void setEmployeeNum(Long employeeNum) {
		this.employeeNum = employeeNum;
	}
	public String getIsUebmiFixedUnit() {
		return isUebmiFixedUnit;
	}
	public void setIsUebmiFixedUnit(String isUebmiFixedUnit) {
		this.isUebmiFixedUnit = isUebmiFixedUnit;
	}
	public String getIsNcmsFixedUnit() {
		return isNcmsFixedUnit;
	}
	public void setIsNcmsFixedUnit(String isNcmsFixedUnit) {
		this.isNcmsFixedUnit = isNcmsFixedUnit;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getConstructionArea() {
		return constructionArea;
	}
	public void setConstructionArea(String constructionArea) {
		this.constructionArea = constructionArea;
	}
	public Long getHospitalBeds() {
		return hospitalBeds;
	}
	public void setHospitalBeds(Long hospitalBeds) {
		this.hospitalBeds = hospitalBeds;
	}
	public Long getSpecialistNum() {
		return specialistNum;
	}
	public void setSpecialistNum(Long specialistNum) {
		this.specialistNum = specialistNum;
	}
	public String getHospitalSummary() {
		return hospitalSummary;
	}
	public void setHospitalSummary(String hospitalSummary) {
		this.hospitalSummary = hospitalSummary;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getBloodBankStock() {
		return bloodBankStock;
	}
	public void setBloodBankStock(String bloodBankStock) {
		this.bloodBankStock = bloodBankStock;
	}
	public String getHospitalNatureName() {
		return hospitalNatureName;
	}
	public void setHospitalNatureName(String hospitalNatureName) {
		this.hospitalNatureName = hospitalNatureName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
}
