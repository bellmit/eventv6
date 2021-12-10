package cn.ffcs.zhsq.mybatis.domain.map.menuconfigure;

import java.util.List;

public class TreeEntity {
	public String id;
	public String text;
	public String iconCls="";
	public String state="";
	public String attributes;
	public String orgCode;
	private boolean hasChildren;
	private boolean expanded;
	public List<TreeEntity> children;

	public boolean isHasChildren() {
		return hasChildren;
	}
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getAttributes() {
		return attributes;
	}
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<TreeEntity> getChildren() {
		return children;
	}
	public void setChildren(List<TreeEntity> children) {
		this.children = children;
	}
	
}
