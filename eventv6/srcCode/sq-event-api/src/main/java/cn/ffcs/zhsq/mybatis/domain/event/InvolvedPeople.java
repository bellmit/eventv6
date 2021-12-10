package cn.ffcs.zhsq.mybatis.domain.event;

import cn.ffcs.shequ.bo.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * T_ZZ_INVOLVED_PEOPLE 涉及人员表
 * @author zhangls
 *
 */
public class InvolvedPeople extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7944953126989297450L;

	private Long ipId;			//主键
	private String hashId;		//加密主键
	private String bizType;		//业务类型，01：T_SJ_EVENT_DISPOSAL事件；02：T_EVENT事件；03：命案防控-嫌疑人；04：命案防控-受害人；12甘肃矛盾纠纷(新)
	private Long bizId;			//业务id
	private String hashBizId;	//加密业务id
	private String name;		//姓名
	private String cardType;	//证件类型(D030001)，001：居民身份证；002：台胞证；003：护照；009：港澳通行证；
	private String cardTypeName;//证件类型名称
	private String idCard;		//证件号码
	private String idCardHS;		//证件号码
	private String homeAddr;	//家庭地址
	private String homeAddrHS;	//家庭地址
	private String tel;			//联系电话
	private String telHS;			//联系电话
	private String peopleProp;	//人员归属，1：主要责任人；0：次要责任人
	private String peopleType;	//人员类别
	private String peopleTypeName;	//人员类别名称
	private String remark;		//备注
	private String sex;			//性别(B153)
	private String sexName;		//性别名称
	private Date birthday;		//出生日期
	private String birthdayStr;	//出生日期文本
	private Integer age;			//年龄
	private String picUrl;		//相片url
	private String politics;	//政治面貌(B118)
	private String politicsName;//政治面貌名称
	private String nationality;	//国籍(B113)
	private String nationalityName;	//国籍名称
	private String nation;			//民族(B162)
	private String nationName;		//民族名称
	private String birthPlace;		//籍贯
	private String birthPlaceName;	//籍贯中文名称
	private String religion;		//宗教信仰(B168)
	private String religionName;	//宗教信仰名称
	private String usedName;		//曾用名
	private String edu;				//学历(B064)
	private String eduName;			//学历名称
	private String marriage;		//婚姻状况(B151)
	private String marriageName;	//婚姻状况名称
	private String professionType;	//职业类别(B265)
	private String professionTypeName;	//职业类别名称
	private String profession;			//职业
	private String reOrgCode;			//户籍地编码
	private String reOrgCodeName;		//户籍地中文名称
	private String registAddr;			//户籍地详址
	private String registAddrHS;			//户籍地详址
	private String liOrgCode;			//现住地编码
	private String liOrgCodeName;		//现住地中文名称
	private String residenceAddr;		//现住地详址
	private String residenceAddrHS;		//现住地详址
	private String homePhone;			//家庭电话
	private String workUnit;			//工作单位(服务处所)
	private String isMentalDisease;		//是否为严重精神障碍患者 1：是；0：否
	private String mentalDisease;		//是否为严重精神障碍患者中文名称
	private String isMinors;			//是否未成年人 1：是；0：否
	private String minors;				//是否未成年人中文名称
	private String isTeenager;			//是否青少年 1：是；0：否
	private String teenager;			//是否青少年中文名称
	private Long ciRsId;				//关联人口基本信息 
	
	
	private String isSkeletonStaff;		//是否骨干人员 1：是；0：否
	private String skeletonStaff;		//是否骨干人员中文
	
	private Long createUserId;          //CREATED_USER_ID 创建人
	
	//9+x不需要ciRsId，换成partyId
	private Long partyId;				//关联人口基本信息
	
	private String involvedPeopleType;		//当事人类型代码
	private String involvedPeopleTypeStr;		//当事人类型代码
	
	
	
	public String getHashId() {
		return hashId;
	}
	public void setHashId(String hashId) {
		this.hashId = hashId;
	}
	public String getHashBizId() {
		return hashBizId;
	}
	public void setHashBizId(String hashBizId) {
		this.hashBizId = hashBizId;
	}
	public String getRegistAddrHS() {
		return registAddrHS;
	}
	public void setRegistAddrHS(String registAddrHS) {
		this.registAddrHS = registAddrHS;
	}
	public String getResidenceAddrHS() {
		return residenceAddrHS;
	}
	public void setResidenceAddrHS(String residenceAddrHS) {
		this.residenceAddrHS = residenceAddrHS;
	}
	public String getIdCardHS() {
		return idCardHS;
	}
	public void setIdCardHS(String idCardHS) {
		this.idCardHS = idCardHS;
	}
	public String getHomeAddrHS() {
		return homeAddrHS;
	}
	public void setHomeAddrHS(String homeAddrHS) {
		this.homeAddrHS = homeAddrHS;
	}
	public String getTelHS() {
		return telHS;
	}
	public void setTelHS(String telHS) {
		this.telHS = telHS;
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
	private Long updateUserId;			//UPDATED_USER_ID 更新人
	private Date startUpdateTime;			//UPDATE_TIME 更新开始时间
	private Date endUpdateTime;			//CREATED_TIME 更新结束时间
	public enum BIZ_TYPE {				//业务类型
		EVENT_DISPOSAL("01"),			//T_SJ_EVENT_DISPOSAL
		ZHSQ_EVENT("02"),				//T_EVENT
		HOMICIDE_SUSPECT("03"),			//命案防控-嫌疑人
		HOMICIDE_VITICM("04"),			//命案防控-受害人
		CONSTRUCTION("05"),				//施工环境保障
		DISPUTE_MEDIATION("06"),		//矛盾纠纷
		EVENT_CASE("07"),				//案件 T_EVENT_CASE
		ACCOUNT_ENFORCE("08"),			//新疆执纪问责 T_PROBLEM
		SBRE_CLUE_INFORMANT("09"),		//扫黑除恶(Sweep Black Remove Evil)举报人员 T_EVENT_SBRE_CLUE
		SBRE_CLUE_REPORTED("10"),		//扫黑除恶(Sweep Black Remove Evil)被举报人员 T_EVENT_SBRE_CLUE
		SBRE_CLUE_GANG_MEM("11"),		//扫黑除恶(Sweep Black Remove Evil)黑恶团伙成员 T_EVENT_EVIL_GANG
		CARE_ROADS("12"),				//涉及线路案(事)件 T_ZZ_RELATED_EVENTS
		SCHOOL_RELATED_EVENTS("13"),	//涉及师生案(事)件 T_ZZ_RELATED_EVENTS
		DISPUTE_MEDIATION_PEOPLE_IN_CHARGE("14");	//矛盾纠纷让化解责任人

		private String bizType;
		
		private BIZ_TYPE(String bizType) {
			this.bizType = bizType;
		}
		
		public String getBizType() {
			return this.bizType;
		}
		
		@Override
		public String toString() {
			return this.bizType;
		}
	}
	
	public Long getIpId() {
		return ipId;
	}
	public void setIpId(Long ipId) {
		this.ipId = ipId;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public Long getBizId() {
		return bizId;
	}
	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardTypeName() {
		return cardTypeName;
	}
	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getHomeAddr() {
		return homeAddr;
	}
	public void setHomeAddr(String homeAddr) {
		this.homeAddr = homeAddr;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getPeopleProp() {
		return peopleProp;
	}
	public void setPeopleProp(String peopleProp) {
		this.peopleProp = peopleProp;
	}
	public String getPeopleType() {
		return peopleType;
	}
	public void setPeopleType(String peopleType) {
		this.peopleType = peopleType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSexName() {
		return sexName;
	}
	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getBirthdayStr() {
		return birthdayStr;
	}
	public void setBirthdayStr(String birthdayStr) {
		this.birthdayStr = birthdayStr;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getPolitics() {
		return politics;
	}
	public void setPolitics(String politics) {
		this.politics = politics;
	}
	public String getPoliticsName() {
		return politicsName;
	}
	public void setPoliticsName(String politicsName) {
		this.politicsName = politicsName;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getNationalityName() {
		return nationalityName;
	}
	public void setNationalityName(String nationalityName) {
		this.nationalityName = nationalityName;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getNationName() {
		return nationName;
	}
	public void setNationName(String nationName) {
		this.nationName = nationName;
	}
	public String getBirthPlace() {
		return birthPlace;
	}
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}
	public String getBirthPlaceName() {
		return birthPlaceName;
	}
	public void setBirthPlaceName(String birthPlaceName) {
		this.birthPlaceName = birthPlaceName;
	}
	public String getReligion() {
		return religion;
	}
	public void setReligion(String religion) {
		this.religion = religion;
	}
	public String getReligionName() {
		return religionName;
	}
	public void setReligionName(String religionName) {
		this.religionName = religionName;
	}
	public String getUsedName() {
		return usedName;
	}
	public void setUsedName(String usedName) {
		this.usedName = usedName;
	}
	public String getEdu() {
		return edu;
	}
	public void setEdu(String edu) {
		this.edu = edu;
	}
	public String getEduName() {
		return eduName;
	}
	public void setEduName(String eduName) {
		this.eduName = eduName;
	}
	public String getMarriage() {
		return marriage;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public String getMarriageName() {
		return marriageName;
	}
	public void setMarriageName(String marriageName) {
		this.marriageName = marriageName;
	}
	public String getProfessionType() {
		return professionType;
	}
	public void setProfessionType(String professionType) {
		this.professionType = professionType;
	}
	public String getProfessionTypeName() {
		return professionTypeName;
	}
	public void setProfessionTypeName(String professionTypeName) {
		this.professionTypeName = professionTypeName;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getReOrgCode() {
		return reOrgCode;
	}
	public void setReOrgCode(String reOrgCode) {
		this.reOrgCode = reOrgCode;
	}
	public String getReOrgCodeName() {
		return reOrgCodeName;
	}
	public void setReOrgCodeName(String reOrgCodeName) {
		this.reOrgCodeName = reOrgCodeName;
	}
	public String getRegistAddr() {
		return registAddr;
	}
	public void setRegistAddr(String registAddr) {
		this.registAddr = registAddr;
	}
	public String getLiOrgCode() {
		return liOrgCode;
	}
	public void setLiOrgCode(String liOrgCode) {
		this.liOrgCode = liOrgCode;
	}
	public String getLiOrgCodeName() {
		return liOrgCodeName;
	}
	public void setLiOrgCodeName(String liOrgCodeName) {
		this.liOrgCodeName = liOrgCodeName;
	}
	public String getResidenceAddr() {
		return residenceAddr;
	}
	public void setResidenceAddr(String residenceAddr) {
		this.residenceAddr = residenceAddr;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getWorkUnit() {
		return workUnit;
	}
	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}
	public String getIsMentalDisease() {
		return isMentalDisease;
	}
	public void setIsMentalDisease(String isMentalDisease) {
		this.isMentalDisease = isMentalDisease;
	}
	public String getMentalDisease() {
		return mentalDisease;
	}
	public void setMentalDisease(String mentalDisease) {
		this.mentalDisease = mentalDisease;
	}
	public String getIsMinors() {
		return isMinors;
	}
	public void setIsMinors(String isMinors) {
		this.isMinors = isMinors;
	}
	public String getMinors() {
		return minors;
	}
	public void setMinors(String minors) {
		this.minors = minors;
	}
	public String getIsTeenager() {
		return isTeenager;
	}
	public void setIsTeenager(String isTeenager) {
		this.isTeenager = isTeenager;
	}
	public String getTeenager() {
		return teenager;
	}
	public void setTeenager(String teenager) {
		this.teenager = teenager;
	}
	public String getIsSkeletonStaff() {
		return isSkeletonStaff;
	}
	public void setIsSkeletonStaff(String isSkeletonStaff) {
		this.isSkeletonStaff = isSkeletonStaff;
	}
	public String getSkeletonStaff() {
		return skeletonStaff;
	}
	public void setSkeletonStaff(String skeletonStaff) {
		this.skeletonStaff = skeletonStaff;
	}
	public Long getCiRsId() {
		return ciRsId;
	}
	public void setCiRsId(Long ciRsId) {
		this.ciRsId = ciRsId;
	}
	public String getPeopleTypeName() {
		return peopleTypeName;
	}
	public void setPeopleTypeName(String peopleTypeName) {
		this.peopleTypeName = peopleTypeName;
	}
	public Long getPartyId() {
		return partyId;
	}
	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}
	public Date getStartUpdateTime() {
		return startUpdateTime;
	}
	public void setStartUpdateTime(Date startUpdateTime) {
		this.startUpdateTime = startUpdateTime;
	}
	public Date getEndUpdateTime() {
		return endUpdateTime;
	}
	public void setEndUpdateTime(Date endUpdateTime) {
		this.endUpdateTime = endUpdateTime;
	}
	public String getInvolvedPeopleType() {
		return involvedPeopleType;
	}
	public void setInvolvedPeopleType(String involvedPeopleType) {
		this.involvedPeopleType = involvedPeopleType;
	}
	public String getInvolvedPeopleTypeStr() {
		return involvedPeopleTypeStr;
	}
	public void setInvolvedPeopleTypeStr(String involvedPeopleTypeStr) {
		this.involvedPeopleTypeStr = involvedPeopleTypeStr;
	}

	
}
