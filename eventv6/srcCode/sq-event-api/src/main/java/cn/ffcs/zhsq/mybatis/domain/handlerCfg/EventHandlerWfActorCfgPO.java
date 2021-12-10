package cn.ffcs.zhsq.mybatis.domain.handlerCfg;

import java.io.Serializable;

/**
 * 环节区域办理人配置
 * T_BIZ_REGION_ACTOR_CFG
 * 
 * @author zhangls
 *
 */
public class EventHandlerWfActorCfgPO implements Serializable {

	private static final long serialVersionUID = -4121792228563060525L;
	
	private Long racId;				//主键，序列为：SEQ_RAC_ID
	private Long transactorId;		//办理人员id
	private String transactorName;	//办理人员名称
	private Long transactorOrgId;	//办理人员组织id
	private String transactorOrgName;//办理人员组织名称
	private String transactorType;	//办理人员类型 0 用户；1 组织；2 角色
	private Long wfpcId;			//流程环节配置主键id
	
	public enum TRANSACTOR_TYPE {
		PERSON("0"),		//用户
		ORGANIZATION("1"),	//组织
		ROLE("2");			//角色
		
		private String code;
		
		private TRANSACTOR_TYPE(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return this.code;
		}
		
		@Override
		public String toString() {
			return this.code;
		}
	}
	
	public Long getRacId() {
		return racId;
	}
	public void setRacId(Long racId) {
		this.racId = racId;
	}
	public Long getTransactorId() {
		return transactorId;
	}
	public void setTransactorId(Long transactorId) {
		this.transactorId = transactorId;
	}
	public String getTransactorName() {
		return transactorName;
	}
	public void setTransactorName(String transactorName) {
		this.transactorName = transactorName;
	}
	public String getTransactorType() {
		return transactorType;
	}
	public void setTransactorType(String transactorType) {
		this.transactorType = transactorType;
	}
	public Long getWfpcId() {
		return wfpcId;
	}
	public void setWfpcId(Long wfpcId) {
		this.wfpcId = wfpcId;
	}
	public Long getTransactorOrgId() {
		return transactorOrgId;
	}
	public void setTransactorOrgId(Long transactorOrgId) {
		this.transactorOrgId = transactorOrgId;
	}
	public String getTransactorOrgName() {
		return transactorOrgName;
	}
	public void setTransactorOrgName(String transactorOrgName) {
		this.transactorOrgName = transactorOrgName;
	}
	
}
