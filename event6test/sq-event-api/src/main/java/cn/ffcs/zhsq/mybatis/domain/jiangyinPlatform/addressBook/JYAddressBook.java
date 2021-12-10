package cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.addressBook;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 通讯录表模块bo对象
 * @Author: 林树轩
 * @Date: 03-06 17:20:57
 * @table: 表信息描述 T_JY_ADDRESS_BOOK 通讯录表  江阴大屏通讯录表  序列SEQ_T_JY_ADDRESS_BOOK
 * @Copyright: 2020 福富软件
 */
public class JYAddressBook implements Serializable {

  private static final long serialVersionUID = 1L;

	private String uuid; //主键标识 	VARCHAR(32)
	private String name; //姓名 	VARCHAR(32)
	private String tel; //电话 	VARCHAR(20)
	private String unit; //单位 	VARCHAR(200)
	private String isValid; //记录状态 	CHAR(1)
	private Long creator; //创建人 	NUMBER(16)
	private Date createTime; //创建时间 	DATE
	private Long updator; //修改人 	NUMBER(16)
	private Date updateTime; //修改时间 	DATE
	private String remark; //备注 	VARCHAR(200)


	public String getUuid() {  //主键标识
		return uuid;
	}
	public void setUuid(String uuid) { //主键标识
		this.uuid = uuid;
	}
	public String getName() {  //姓名
		return name;
	}
	public void setName(String name) { //姓名
		this.name = name;
	}
	public String getTel() {  //电话
		return tel;
	}
	public void setTel(String tel) { //电话
		this.tel = tel;
	}
	public String getUnit() {  //单位
		return unit;
	}
	public void setUnit(String unit) { //单位
		this.unit = unit;
	}
	public String getIsValid() {  //记录状态
		return isValid;
	}
	public void setIsValid(String isValid) { //记录状态
		this.isValid = isValid;
	}
	public Long getCreator() {  //创建人
		return creator;
	}
	public void setCreator(Long creator) { //创建人
		this.creator = creator;
	}
	public Date getCreateTime() {  //创建时间
		return createTime;
	}
	public void setCreateTime(Date createTime) { //创建时间
		this.createTime = createTime;
	}
	public Long getUpdator() {  //修改人
		return updator;
	}
	public void setUpdator(Long updator) { //修改人
		this.updator = updator;
	}
	public Date getUpdateTime() {  //修改时间
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) { //修改时间
		this.updateTime = updateTime;
	}
	public String getRemark() {  //备注
		return remark;
	}
	public void setRemark(String remark) { //备注
		this.remark = remark;
	}


}