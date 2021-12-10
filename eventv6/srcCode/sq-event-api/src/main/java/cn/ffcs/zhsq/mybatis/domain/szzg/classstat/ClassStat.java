package cn.ffcs.zhsq.mybatis.domain.szzg.classstat;

import java.io.Serializable;


/**
 * @Description: 户籍人口对象
 * @Author: linzhu
 * @Date: 08-10 17:20:03
 * @Copyright: 2017 福富软件
 */
public class ClassStat implements Serializable {
	private static final long serialVersionUID = 1505682109595894672L;
	private Long orgId;
	private String orgCode;
	private String orgName;
	private String type; //A-党建 B-低保 C-退体人员 D-失业人员 E-残疾人 F-老年人 G-服兵役 H-幼儿 I-妇女 J-非党建 K-刑满释放人员 L-重性精神病症记录 M-危险品从业信息 N-社区矫正信息 O-邪教人员 P- 吸毒人员 Q-上访人员R-常住人口 S-流动人口 T-其他人口 U-临时常住人口 V-临时居住人口 W-台胞 X-外籍人口 Y-挂户人口 Z-传销人员 HJ-户籍人口 OS-境外 LB-留守AH全球眼数 WA 企业数 WB 网格数 WC 二级网格数 WD 门店数 WE 楼宇数 Wf 出租屋数JJK-晋江*刑满释放人员 JJL-晋江*重性精神病症记录 JJM-晋江*危险品从业信息 JJN-晋江*社区矫正信息 JJO-晋江*邪教人员 JJP-晋江*吸毒人员 JJQ-晋江*上访人员JJR-晋江*常住人口 JJS-晋江*流动人口 JJT-晋江*户籍人口 JJZ-晋江*传销人员GSR-甘肃*常住 GSS-甘肃*流动 GST-甘肃*户籍 GSU-甘肃*流入 GSV-甘肃*流出
	private Long total;
	private Long maleTotal;
	private Long femaleTotal;
	private Long ageM10;
	private Long ageM20;
	private Long ageM30;
	private Long ageM40;
	private Long ageM50;
	private Long ageM60;
	private Long ageM70;
	private Long ageM80;
	private Long ageM90;
	private Long ageM100;
	private String orgPcode;
	private Long ageF10;
	private Long ageF20;
	private Long ageF30;
	private Long ageF40;
	private Long ageF50;
	private Long ageF60;
	private Long ageF70;
	private Long ageF80;
	private Long ageF90;
	private Long ageF100;


	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Long getMaleTotal() {
		return maleTotal;
	}
	public void setMaleTotal(Long maleTotal) {
		this.maleTotal = maleTotal;
	}
	public Long getFemaleTotal() {
		return femaleTotal;
	}
	public void setFemaleTotal(Long femaleTotal) {
		this.femaleTotal = femaleTotal;
	}
	public Long getAgeM10() {
		return ageM10;
	}
	public void setAgeM10(Long ageM10) {
		this.ageM10 = ageM10;
	}
	public Long getAgeM20() {
		return ageM20;
	}
	public void setAgeM20(Long ageM20) {
		this.ageM20 = ageM20;
	}
	public Long getAgeM30() {
		return ageM30;
	}
	public void setAgeM30(Long ageM30) {
		this.ageM30 = ageM30;
	}
	public Long getAgeM40() {
		return ageM40;
	}
	public void setAgeM40(Long ageM40) {
		this.ageM40 = ageM40;
	}
	public Long getAgeM50() {
		return ageM50;
	}
	public void setAgeM50(Long ageM50) {
		this.ageM50 = ageM50;
	}
	public Long getAgeM60() {
		return ageM60;
	}
	public void setAgeM60(Long ageM60) {
		this.ageM60 = ageM60;
	}
	public Long getAgeM70() {
		return ageM70;
	}
	public void setAgeM70(Long ageM70) {
		this.ageM70 = ageM70;
	}
	public Long getAgeM80() {
		return ageM80;
	}
	public void setAgeM80(Long ageM80) {
		this.ageM80 = ageM80;
	}
	public Long getAgeM90() {
		return ageM90;
	}
	public void setAgeM90(Long ageM90) {
		this.ageM90 = ageM90;
	}
	public Long getAgeM100() {
		return ageM100;
	}
	public void setAgeM100(Long ageM100) {
		this.ageM100 = ageM100;
	}
	public String getOrgPcode() {
		return orgPcode;
	}
	public void setOrgPcode(String orgPcode) {
		this.orgPcode = orgPcode;
	}
	public Long getAgeF10() {
		return ageF10;
	}
	public void setAgeF10(Long ageF10) {
		this.ageF10 = ageF10;
	}
	public Long getAgeF20() {
		return ageF20;
	}
	public void setAgeF20(Long ageF20) {
		this.ageF20 = ageF20;
	}
	public Long getAgeF30() {
		return ageF30;
	}
	public void setAgeF30(Long ageF30) {
		this.ageF30 = ageF30;
	}
	public Long getAgeF40() {
		return ageF40;
	}
	public void setAgeF40(Long ageF40) {
		this.ageF40 = ageF40;
	}
	public Long getAgeF50() {
		return ageF50;
	}
	public void setAgeF50(Long ageF50) {
		this.ageF50 = ageF50;
	}
	public Long getAgeF60() {
		return ageF60;
	}
	public void setAgeF60(Long ageF60) {
		this.ageF60 = ageF60;
	}
	public Long getAgeF70() {
		return ageF70;
	}
	public void setAgeF70(Long ageF70) {
		this.ageF70 = ageF70;
	}
	public Long getAgeF80() {
		return ageF80;
	}
	public void setAgeF80(Long ageF80) {
		this.ageF80 = ageF80;
	}
	public Long getAgeF90() {
		return ageF90;
	}
	public void setAgeF90(Long ageF90) {
		this.ageF90 = ageF90;
	}
	public Long getAgeF100() {
		return ageF100;
	}
	public void setAgeF100(Long ageF100) {
		this.ageF100 = ageF100;
	}


}