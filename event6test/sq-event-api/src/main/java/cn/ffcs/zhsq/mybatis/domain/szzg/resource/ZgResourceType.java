package cn.ffcs.zhsq.mybatis.domain.szzg.resource;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: zg_resource_type模块bo对象
 * @Author: huangwenbin
 * @Date: 09-16 10:01:50
 * @Copyright: 2017 福富软件
 */
public class ZgResourceType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -442850961725911095L;
	private Long resTypeId; //主键.SEQ_ZG_RESOURCE_TYPE
	private String resTypeName; //资源名称
	private Long parentTypeId;
	private String parentTypeName;
	private String typeCode; //资源类型
	private Long menuId; //专题图层资源id
	private String menuName; //专题图层资源id
	private String icon; //图片名称
	private String status; //状态,1:有效,0:无效
	private Long createUser;
	private Date createDate;
	private Long updateUser;
	private Date updateDate;


	public ZgResourceType() {
		super();
	}
	
	
	public String getParentTypeName() {
		return parentTypeName;
	}


	public void setParentTypeName(String parentTypeName) {
		this.parentTypeName = parentTypeName;
	}


	public String getMenuName() {
		return menuName;
	}


	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}


	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}


	public Long getMenuId() {
		return menuId;
	}



	public Long getResTypeId() {
		return resTypeId;
	}
	public void setResTypeId(Long resTypeId) {
		this.resTypeId = resTypeId;
	}
	public String getResTypeName() {
		return resTypeName;
	}
	public void setResTypeName(String resTypeName) {
		this.resTypeName = resTypeName;
	}
	public Long getParentTypeId() {
		return parentTypeId;
	}
	public void setParentTypeId(Long parentTypeId) {
		this.parentTypeId = parentTypeId;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	

	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


}