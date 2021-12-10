package cn.ffcs.zhsq.mybatis.domain.requestion;

import java.io.Serializable;


/**
 * @Description: 企业联动单位模块bo对象
 * @Author: caiby
 * @Date: 03-12 10:28:45
 * @Copyright: 2018 福富软件
 */
public class CorpLink implements Serializable {

	private Long cluId; //主键
	private Long linkageUnitId; //联动单位ID
	private String linkMan; //联络员
	private String linkManTel; //联络员联系方式
	private String leaderName; //分管领导
	private String leaderTel; //分管领导联系方式


	public Long getCluId() {
		return cluId;
	}
	public void setCluId(Long cluId) {
		this.cluId = cluId;
	}
	public Long getLinkageUnitId() {
		return linkageUnitId;
	}
	public void setLinkageUnitId(Long linkageUnitId) {
		this.linkageUnitId = linkageUnitId;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getLinkManTel() {
		return linkManTel;
	}
	public void setLinkManTel(String linkManTel) {
		this.linkManTel = linkManTel;
	}
	public String getLeaderName() {
		return leaderName;
	}
	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
	public String getLeaderTel() {
		return leaderTel;
	}
	public void setLeaderTel(String leaderTel) {
		this.leaderTel = leaderTel;
	}


}