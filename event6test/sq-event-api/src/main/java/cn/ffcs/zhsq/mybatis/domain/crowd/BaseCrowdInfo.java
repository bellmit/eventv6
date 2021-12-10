/**
 * 
 */
package cn.ffcs.zhsq.mybatis.domain.crowd;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
//import cn.ffcs.zhsq.utils.JsonDateSerializer;

/**
 * 重点人员信息基类
 * @author guohh
 *
 */
public class BaseCrowdInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8897844318544669556L;

	//--人员基本信息
    private Long ciRsId; //居民ID
    private String name; //姓名
    private String gender; //性别
    //@JsonSerialize(using = JsonDateSerializer.class)
    private Date birthday; //出生日期
    private String residence; //户籍地
    private String familyAddress; //家庭地址
    private String phone; //联系电话，固定电话
    private String type; //人口类型
    private String education; //文化程度
    private String identityCard; //身份证号
    private String residentMobile; //移动电话
    private String marriage; //婚姻状况
    private String houseReside;//人户状态
    private String holderRelation; //与户主的关系
    private Integer age;//年龄
    private String residenceAddr;//I_RESIDENCE_ADDR 现居住地址

    private String familySn; //家庭编号 I_FAMILY_SN
    private String residentBirthplace; //籍贯 RESIDENT_BIRTHPLACE
    private String residentPolitics; //政治面貌 RESIDENT_POLITICS
    private String ethnic; //民族 I_ETHNIC
    //private String marriage; //婚姻状况 I_MARRIAGE
    private String career; //职业 I_CAREER
    private String organization; // 工作单位 I_ORGANIZATION
    private String houseSource; // 住房性质 I_HOUSE_SOURCE
    private String ophoneNum; // 办公电话 OPHONE_NUM
    private String residentNationality; // 国籍 RESIDENT_NATIONALITY
    private String remark; // 备注 REMARK

	//--公用扩展信息
    private String status; //状态
    private Long updateUser; // 更新人员
    private Date updateDate; //更新时间
    private Long createUser; //创建人员
    private Date createDate; //创建时间
    
    private Long gridId; //--所属网格ID

    //--冗余信息
    private String updateUserPartyName; // 更新人员，参与人名字
    private String createUserPartyName; // 创建人员，参与人名字
    private String gridName; // 所属网格名称
    private String marriageLabel; //婚姻状况
    private String typeLabel; //人口类型

    /*******重点人员走访安排表*******/
    VisitArrange visitArrange = new VisitArrange();
    
	public VisitArrange getVisitArrange() {
		return visitArrange;
	}
	public void setVisitArrange(VisitArrange visitArrange) {
		this.visitArrange = visitArrange;
	}
	
    public Long getCiRsId() {
        return ciRsId;
    }

    public void setCiRsId(Long ciRsId) {
        this.ciRsId = ciRsId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public String getResidence() {
		return residence;
	}
	
	public void setResidence(String residence) {
		this.residence = residence;
	}
	
	public String getFamilyAddress() {
		return familyAddress;
	}

	public void setFamilyAddress(String familyAddress) {
		this.familyAddress = familyAddress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUpdateUserPartyName() {
		return updateUserPartyName;
	}

	public void setUpdateUserPartyName(String updateUserPartyName) {
		this.updateUserPartyName = updateUserPartyName;
	}

	public String getCreateUserPartyName() {
		return createUserPartyName;
	}

	public void setCreateUserPartyName(String createUserPartyName) {
		this.createUserPartyName = createUserPartyName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getResidentMobile() {
		return residentMobile;
	}

	public void setResidentMobile(String residentMobile) {
		this.residentMobile = residentMobile;
	}

	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}

	public Long getGridId() {
		return gridId;
	}

	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public String getHouseReside() {
		return houseReside;
	}

	public void setHouseReside(String houseReside) {
		this.houseReside = houseReside;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getHolderRelation() {
		return holderRelation;
	}

	public void setHolderRelation(String holderRelation) {
		this.holderRelation = holderRelation;
	}
	public String getResidenceAddr() {
		return residenceAddr;
	}
	public void setResidenceAddr(String residenceAddr) {
		this.residenceAddr = residenceAddr;
	}
	public String getMarriageLabel() {
		return marriageLabel;
	}
	public void setMarriageLabel(String marriageLabel) {
		this.marriageLabel = marriageLabel;
	}
	public String getTypeLabel() {
		return typeLabel;
	}
	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}
	public String getFamilySn() {
		return familySn;
	}
	public void setFamilySn(String familySn) {
		this.familySn = familySn;
	}
	public String getResidentBirthplace() {
		return residentBirthplace;
	}
	public void setResidentBirthplace(String residentBirthplace) {
		this.residentBirthplace = residentBirthplace;
	}
	public String getResidentPolitics() {
		return residentPolitics;
	}
	public void setResidentPolitics(String residentPolitics) {
		this.residentPolitics = residentPolitics;
	}
	public String getEthnic() {
		return ethnic;
	}
	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
	}
	public String getCareer() {
		return career;
	}
	public void setCareer(String career) {
		this.career = career;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getHouseSource() {
		return houseSource;
	}
	public void setHouseSource(String houseSource) {
		this.houseSource = houseSource;
	}
	public String getOphoneNum() {
		return ophoneNum;
	}
	public void setOphoneNum(String ophoneNum) {
		this.ophoneNum = ophoneNum;
	}
	public String getResidentNationality() {
		return residentNationality;
	}
	public void setResidentNationality(String residentNationality) {
		this.residentNationality = residentNationality;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
