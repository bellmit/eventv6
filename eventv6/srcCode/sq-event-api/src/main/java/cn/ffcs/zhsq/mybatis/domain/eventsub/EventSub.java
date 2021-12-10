package cn.ffcs.zhsq.mybatis.domain.eventsub;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: event_na模块bo对象
 * @Author: chenshikai
 * @Date: 07-03 14:26:31
 * @Copyright: 2020 福富软件
 */
public class EventSub implements Serializable {

	private Long typeId; //id编号  自增类型
	private Long bizId; //业务id编号
	private String tableName; //关联主表名
	private String typeName; //关联字段名
	private String typeVal; //类别值


	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public Long getBizId() {
		return bizId;
	}
	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeVal() {
		return typeVal;
	}
	public void setTypeVal(String typeVal) {
		this.typeVal = typeVal;
	}



}