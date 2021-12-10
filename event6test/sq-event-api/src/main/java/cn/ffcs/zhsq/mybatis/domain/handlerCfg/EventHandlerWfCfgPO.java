package cn.ffcs.zhsq.mybatis.domain.handlerCfg;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作流业务配置
 * T_BIZ_WF_CFG
 * 
 * @author zhangls
 *
 */
public class EventHandlerWfCfgPO implements Serializable {

	private static final long serialVersionUID = 6990766053421190258L;
	
	private Long wfcId;			//主键 序列为：SEQ_WFC_ID
	private String regionCode;	//地域编码
	private String regionName;	//地域名称
	private Long wfCfgId;		//流程图id
	private String wfCfgName;	//流程图名称
	private String bizType;		//业务类型 00 工作流事件类型办理人员配置
	private String bizTypeName;	//业务类型名称
	private String status;		//状态 0 无效；1 有效
	private Long createUserId;	//创建人员id
	private Date createTime;	//创建日期
	private Long updateUserId;	//更新人员id
	private Date updateTime;	//更新日期
	
	/**
	 * 业务类型
	 * @author zhangls
	 *
	 */
	public enum BIZ_TYPE {
		WORKFLOW("工作流", "00");	//工作流
		
		private String name;
		private String code;
		
		private BIZ_TYPE(String name, String code) {
			this.name = name;
			this.code = code;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getCode() {
			return this.code;
		}
		
		@Override
		public String toString() {
			return this.code;
		}
	}
	
	/**
	 * 状态
	 * @author zhangls
	 *
	 */
	public enum STATUS {
		VALID("1"),		//有效状态
		INVALID("0");	//无效状态
		
		private String code;
		
		private STATUS(String code) {
			this.code = code;
		}
		
		@Override
		public String toString() {
			return this.code;
		}
	}
	
	public Long getWfcId() {
		return wfcId;
	}
	public void setWfcId(Long wfcId) {
		this.wfcId = wfcId;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public Long getWfCfgId() {
		return wfCfgId;
	}
	public void setWfCfgId(Long wfCfgId) {
		this.wfCfgId = wfCfgId;
	}
	public String getWfCfgName() {
		return wfCfgName;
	}
	public void setWfCfgName(String wfCfgName) {
		this.wfCfgName = wfCfgName;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getBizTypeName() {
		return bizTypeName;
	}
	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
