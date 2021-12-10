package cn.ffcs.zhsq.mybatis.domain.crowd;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 综治.重点人员走访安排表
 * @Table T_ZZ_VISIT_ARRANGE
 * @author zhangyy
 *
 */
public class VisitArrange implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1263408599428606599L;
	private Long vamId;//标识
	private Long objId;//对象标识
	private String objType;//对象分类
	private Integer cycle;//执行周期
	private Date lastTime;//上次走访时间
	private Date nextTime;//下次走访时间
	private String status;//状态
	private Date statusTime;//状态时间
	
	//-- 冗余字段
    private String nextTimeStr; //-- 下次走访时间
    
	public Long getVamId() {
		return vamId;
	}
	public void setVamId(Long vamId) {
		this.vamId = vamId;
	}
	public Long getObjId() {
		return objId;
	}
	public void setObjId(Long objId) {
		this.objId = objId;
	}
	public String getObjType() {
		return objType;
	}
	public void setObjType(String objType) {
		this.objType = objType;
	}
	public Integer getCycle() {
		return cycle;
	}
	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	public Date getNextTime() {
		return nextTime;
	}
	public void setNextTime(Date nextTime) {
		//this.nextTime = nextTime;
		if(nextTime==null) this.nextTime = null;
		else {
			this.nextTime = new java.util.Date(nextTime.getTime());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			this.nextTimeStr = sdf.format(nextTime);
		}
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNextTimeStr() {
		return nextTimeStr;
	}
	public void setNextTimeStr(String nextTimeStr) {
		this.nextTimeStr = nextTimeStr;
	}
	public Date getStatusTime() {
		return statusTime;
	}
	public void setStatusTime(Date statusTime) {
		this.statusTime = statusTime;
	}
}
