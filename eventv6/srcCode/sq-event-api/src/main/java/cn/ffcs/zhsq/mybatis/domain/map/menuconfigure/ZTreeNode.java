package cn.ffcs.zhsq.mybatis.domain.map.menuconfigure;

import java.util.ArrayList;
import java.util.List;

/**
 * 网格ZTree树节点
 * 
 * @author zhongshm
 * 
 */
public class ZTreeNode implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4338090817313365008L;

	private Long id;
	private String name;
	private Long pId;
	private Long layCfgId;
	private String elementsCollectionStr;
	private String callBack;
	private String orgCode;
	
	private String icon; //节点图标
	private String iconOpen; //打开时显示的图标
	private String iconClose; //关闭时显示的图标
	
	private boolean open; //节点是否展开
	private boolean isParent; //是否是父节点（即是否有子节点）
	
	private boolean checked; //checkbox radio是否选中
	private boolean halfCheck; //checkbox radio 是否半选中
	private boolean chkDisabled; //checkbox radio 是否可用
	private boolean nocheck; //checkbox radio 是否显示
	
	private List<ZTreeNode> children;
	
	public void addChildren(ZTreeNode node) {
		if (children == null)
			children = new ArrayList<ZTreeNode>();
		children.add(node);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getpId() {
		return pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}

	public Long getLayCfgId() {
		return layCfgId;
	}

	public void setLayCfgId(Long layCfgId) {
		this.layCfgId = layCfgId;
	}

	public String getElementsCollectionStr() {
		return elementsCollectionStr;
	}

	public void setElementsCollectionStr(String elementsCollectionStr) {
		this.elementsCollectionStr = elementsCollectionStr;
	}

	public String getCallBack() {
		return callBack;
	}

	public void setCallBack(String callBack) {
		this.callBack = callBack;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconOpen() {
		return iconOpen;
	}

	public void setIconOpen(String iconOpen) {
		this.iconOpen = iconOpen;
	}

	public String getIconClose() {
		return iconClose;
	}

	public void setIconClose(String iconClose) {
		this.iconClose = iconClose;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isHalfCheck() {
		return halfCheck;
	}

	public void setHalfCheck(boolean halfCheck) {
		this.halfCheck = halfCheck;
	}

	public boolean isChkDisabled() {
		return chkDisabled;
	}

	public void setChkDisabled(boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}

	public boolean isNocheck() {
		return nocheck;
	}

	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}

	public List<ZTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<ZTreeNode> children) {
		this.children = children;
	}

}