package cn.ffcs.zhsq.mybatis.domain.executeControl;

import java.io.Serializable;

/**
 * @Description: 布控库管理模块bo对象  用于json传递数据使用
 * @Author: dtj
 * @Date: 07-17 09:21:54
 * @table: 表信息描述 T_CONTROL_LIBRARY 布控库管理  布控库管理  序列SEQ_T_CONTROL_LIBRARY
 * @Copyright: 2020 福富软件
 */
public class ControlLibraryTwo  implements Serializable {

  private static final long serialVersionUID = 1L;


	private String[] userIds; //管理人员ids 	VARCHAR(500)
	private String name; //名称 	VARCHAR(50)
	private String description; //描述 	VARCHAR(200)
	private String libType; //布控库类型 	NUMERIC

	public String[] getUserIds() {
		return userIds;
	}

	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLibType() {
		return libType;
	}

	public void setLibType(String libType) {
		this.libType = libType;
	}
}