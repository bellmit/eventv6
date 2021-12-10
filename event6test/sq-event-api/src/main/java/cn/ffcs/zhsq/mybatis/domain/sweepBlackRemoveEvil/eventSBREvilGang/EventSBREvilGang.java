package cn.ffcs.zhsq.mybatis.domain.sweepBlackRemoveEvil.eventSBREvilGang;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 扫黑除恶_黑恶团伙管理模块bo对象
 * @Author: LINZHU
 * @Date: 05-23 10:37:41
 * @Copyright: 2018 福富软件
 */
public class EventSBREvilGang implements Serializable {

	private static final long serialVersionUID = 2092311505638207097L;
	private Long gangId; //团伙编号，序列为：SEQ_GANG_ID
	private String gangName; //团伙名称
	private String infoOrgCode; //所属区域
	private String activityZone; //主要活动地带
	private String situation; //团伙涉黑涉恶情况
	private String hitStatus; //打击状态，1 已扫除；2 扫除中
	private String gangRemark; //备注
	private String gangStatus; //状态，1 有效；0 无效
	private Date createDate; //登记时间
	private Long creatorId; //登记人员
	private Date updateDate; //更新时间
	private Long updaterId; //更新人员
	
	private String infoOrgName;//区域名称
	private String gridPath;//网格全称
	
	
	public String getInfoOrgName() {
		return infoOrgName;
	}
	public void setInfoOrgName(String infoOrgName) {
		this.infoOrgName = infoOrgName;
	}

	public Long getGangId() {
		return gangId;
	}
	public void setGangId(Long gangId) {
		this.gangId = gangId;
	}
	public String getGangName() {
		return gangName;
	}
	public void setGangName(String gangName) {
		this.gangName = gangName;
	}
	public String getInfoOrgCode() {
		return infoOrgCode;
	}
	public void setInfoOrgCode(String infoOrgCode) {
		this.infoOrgCode = infoOrgCode;
	}
	public String getActivityZone() {
		return activityZone;
	}
	public void setActivityZone(String activityZone) {
		this.activityZone = activityZone;
	}
	public String getSituation() {
		return situation;
	}
	public void setSituation(String situation) {
		this.situation = situation;
	}
	public String getHitStatus() {
		return hitStatus;
	}
	public void setHitStatus(String hitStatus) {
		this.hitStatus = hitStatus;
	}
	public String getGangRemark() {
		return gangRemark;
	}
	public void setGangRemark(String gangRemark) {
		this.gangRemark = gangRemark;
	}
	public String getGangStatus() {
		return gangStatus;
	}
	public void setGangStatus(String gangStatus) {
		this.gangStatus = gangStatus;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Long getUpdaterId() {
		return updaterId;
	}
	public void setUpdaterId(Long updaterId) {
		this.updaterId = updaterId;
	}
	public String getGridPath() {
		return gridPath;
	}
	public void setGridPath(String gridPath) {
		this.gridPath = gridPath;
	}


}