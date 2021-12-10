package cn.ffcs.zhsq.mybatis.domain.dispute;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 矛盾纠纷调解人表模块bo对象
 * @Author: wangh
 * @Date: 12-02 14:26:34
 * @table: 表信息描述 T_ZZ_DISPUTE_MEDIATION_CASE 矛盾纠纷调解人表  矛盾纠纷调解人表  序列SEQ_ZZ_DISPUTE_MEDIATION_CASE
 * @Copyright: 2020 福富软件
 */
public class MediationCase implements Serializable {

  private static final long serialVersionUID = 1L;

	private Long mediationCaseId; //MEDIATION_CASE_ID 	NUMBER(20)
	private Long mediationId; //编号 	NUMBER(20)
	private String mediationOrgName; //调解组织 	VARCHAR2(100)
	private String mediator; //调解人 	VARCHAR2(20)
	private String mediationPost; //调解人职务 	VARCHAR2(50)
	private String mediatorTel; //调解人联系方式 	VARCHAR2(50)
	private String mediatorType; //调解人类型 	CHAR(2)
	private Integer status; //状态 	NUMBER(3)
	private Date updateTime; //录入时间 	DATE


	public Long getMediationCaseId() {  //MEDIATION_CASE_ID
		return mediationCaseId;
	}
	public void setMediationCaseId(Long mediationCaseId) { //MEDIATION_CASE_ID
		this.mediationCaseId = mediationCaseId;
	}
	public Long getMediationId() {  //编号
		return mediationId;
	}
	public void setMediationId(Long mediationId) { //编号
		this.mediationId = mediationId;
	}
	public String getMediationOrgName() {  //调解组织
		return mediationOrgName;
	}
	public void setMediationOrgName(String mediationOrgName) { //调解组织
		this.mediationOrgName = mediationOrgName;
	}
	public String getMediator() {  //调解人
		return mediator;
	}
	public void setMediator(String mediator) { //调解人
		this.mediator = mediator;
	}
	public String getMediationPost() {  //调解人职务
		return mediationPost;
	}
	public void setMediationPost(String mediationPost) { //调解人职务
		this.mediationPost = mediationPost;
	}
	public String getMediatorTel() {  //调解人联系方式
		return mediatorTel;
	}
	public void setMediatorTel(String mediatorTel) { //调解人联系方式
		this.mediatorTel = mediatorTel;
	}
	public String getMediatorType() {  //调解人类型
		return mediatorType;
	}
	public void setMediatorType(String mediatorType) { //调解人类型
		this.mediatorType = mediatorType;
	}
	public Integer getStatus() {  //状态
		return status;
	}
	public void setStatus(Integer status) { //状态
		this.status = status;
	}
	public Date getUpdateTime() {  //录入时间
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) { //录入时间
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "MediationCase{" +
				"mediationCaseId=" + mediationCaseId +
				", mediationId=" + mediationId +
				", mediationOrgName='" + mediationOrgName + '\'' +
				", mediator='" + mediator + '\'' +
				", mediationPost='" + mediationPost + '\'' +
				", mediatorTel='" + mediatorTel + '\'' +
				", mediatorType='" + mediatorType + '\'' +
				", status=" + status +
				", updateTime=" + updateTime +
				'}';
	}
}