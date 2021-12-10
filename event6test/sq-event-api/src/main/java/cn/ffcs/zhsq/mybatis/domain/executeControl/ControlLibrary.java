package cn.ffcs.zhsq.mybatis.domain.executeControl;

import cn.ffcs.zhsq.mybatis.domain.pages.Pages;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 布控库管理模块bo对象
 * @Author: dtj
 * @Date: 07-17 09:21:54
 * @table: 表信息描述 T_CONTROL_LIBRARY 布控库管理  布控库管理  序列SEQ_T_CONTROL_LIBRARY
 * @Copyright: 2020 福富软件
 */
public class ControlLibrary extends Pages implements Serializable {

  private static final long serialVersionUID = 1L;

	private Long id; //主键ID 	VARCHAR(24)
	private String controlLibraryId; //布控库ID 	VARCHAR(24)
	private String userIds; //管理人员ids 	VARCHAR(500)
	private String name; //名称 	VARCHAR(50)
	private String description; //描述 	VARCHAR(200)
	private String libType; //布控库类型 	NUMERIC
	private String isValid; //是否有效 	CHAR(1)
	private Long creator; //创建人 	NUMBER(9)
	private Date createTime; //创建时间 	DATE
	private Long updator; //更新人 	NUMBER(9)
	private Date updateTime; //更新时间 	DATE
	private Long gridId; //所属网格 	NUMBER(24)
	private String gridCode; //网格编码 	VARCHAR(24)
	private String gridName; //网格名称 	VARCHAR(24)
	private String gridPath; //网格全路径 	VARCHAR(50)
	private String userName;//管理员名称 	VARCHAR(24)
	private List<ControlLibrary> infos;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<ControlLibrary> getInfos() {
		return infos;
	}

	public void setInfos(List<ControlLibrary> infos) {
		this.infos = infos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getControlLibraryId() {  //布控库ID
		return controlLibraryId;
	}
	public void setControlLibraryId(String controlLibraryId) { //布控库ID
		this.controlLibraryId = controlLibraryId;
	}
	public String getUserIds() {  //管理人员ids
		return userIds;
	}
	public void setUserIds(String userIds) { //管理人员ids
		this.userIds = userIds;
	}
	public String getName() {  //名称
		return name;
	}
	public void setName(String name) { //名称
		this.name = name;
	}
	public String getDescription() {  //描述
		return description;
	}
	public void setDescription(String description) { //描述
		this.description = description;
	}
	public String getLibType() {  //布控库类型
		return libType;
	}
	public void setLibType(String libType) { //布控库类型
		this.libType = libType;
	}
	public String getIsValid() {  //是否有效
		return isValid;
	}
	public void setIsValid(String isValid) { //是否有效
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
	public Long getUpdator() {  //更新人
		return updator;
	}
	public void setUpdator(Long updator) { //更新人
		this.updator = updator;
	}
	public Date getUpdateTime() {  //更新时间
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) { //更新时间
		this.updateTime = updateTime;
	}
	public Long getGridId() {  //所属网格
		return gridId;
	}
	public void setGridId(Long gridId) { //所属网格
		this.gridId = gridId;
	}
	public String getGridCode() {  //网格编码
		return gridCode;
	}
	public void setGridCode(String gridCode) { //网格编码
		this.gridCode = gridCode;
	}
	public String getGridName() {  //网格名称
		return gridName;
	}
	public void setGridName(String gridName) { //网格名称
		this.gridName = gridName;
	}
	public String getGridPath() {  //网格全路径
		return gridPath;
	}
	public void setGridPath(String gridPath) { //网格全路径
		this.gridPath = gridPath;
	}


}