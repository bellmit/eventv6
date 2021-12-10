package cn.ffcs.zhsq.mybatis.domain.dispute;

import java.io.Serializable;
import java.util.Date;

public class DisputeMediationRes implements Serializable {
    /**
	 * @文件描述: TODO
	 * @内容摘要: 
	 * @完成日期: Sep 11, 2014
	 * 修改日期			修改人
	 * Sep 11, 2014		zhongshm
	 */
	private static final long serialVersionUID = -4941565274505131413L;

	private Long mediationResId;

    private Long mediationId;

    private String mediationResult;

    private String mediator;

    private Integer updateId;

    private Date updateTime;

    private Short status;

    private String isSuccess;

    private String agreeType;
    
    private String mediationOrgName;
    
    private String mediationTel;
    
    private String hjCertNumber;//化解人身份证号码

    public Long getMediationResId() {
        return mediationResId;
    }

    public void setMediationResId(Long mediationResId) {
        this.mediationResId = mediationResId;
    }

    public Long getMediationId() {
        return mediationId;
    }

    public void setMediationId(Long mediationId) {
        this.mediationId = mediationId;
    }

    public String getMediationResult() {
        return mediationResult;
    }

    public void setMediationResult(String mediationResult) {
        this.mediationResult = mediationResult == null ? null : mediationResult.trim();
    }

    public String getMediator() {
        return mediator;
    }

    public void setMediator(String mediator) {
        this.mediator = mediator == null ? null : mediator.trim();
    }

    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess == null ? null : isSuccess.trim();
    }

    public String getAgreeType() {
        return agreeType;
    }

    public void setAgreeType(String agreeType) {
        this.agreeType = agreeType == null ? null : agreeType.trim();
    }

	public String getMediationTel() {
		return mediationTel;
	}

	public void setMediationTel(String mediationTel) {
		this.mediationTel = mediationTel;
	}

	public String getMediationOrgName() {
		return mediationOrgName;
	}

	public void setMediationOrgName(String mediationOrgName) {
		this.mediationOrgName = mediationOrgName;
	}

	public String getHjCertNumber() {
		return hjCertNumber;
	}

	public void setHjCertNumber(String hjCertNumber) {
		this.hjCertNumber = hjCertNumber;
	}
	
	
}