package cn.ffcs.zhsq.mybatis.domain.map.menuconfigure;




import java.util.ArrayList;
import java.util.List;

/**
 * 网格ZTree树节点
 * @author guohh
 *
 */
public class GdZTreeNode implements java.io.Serializable {
	private static final long serialVersionUID = 3715420974760475367L;
	
	private String id; //节点ID
	private String pId; //父节点ID
	private String name; //显示的名称（文字）
	private boolean open; //节点是否展开
	private boolean isParent; //是否是父节点（即是否有子节点）

	private String icon; //节点图标
	private String iconSkin; //节点图标
	private String iconOpen; //打开时显示的图标
	private String iconClose; //关闭时显示的图标

	private boolean checked; //checkbox radio是否选中
	private boolean halfCheck; //checkbox radio 是否半选中
	private boolean chkDisabled; //checkbox radio 是否可用
	private boolean nocheck; //checkbox radio 是否显示
	
	//--冗余字段
	private String layerType;  //层级类型
	private String orgCode; //组织编码（信息域）
	private Long orgId; //组织ID（信息域）
	private Long gridId;
	private String gridCode;
	private String gridPhoto; //网格图片
	private String targetUrl; // 对应的地址
	private String typeCode; //类型编码
	private String level; //层级
	private String gridLevel;
	private String professionCode;
	
	//事件 新组织树
	private boolean clickable;	//是否响应点击事件
	
	private List<GdZTreeNode> children;
	
	public String getIconSkin() {
		return iconSkin;
	}

	public void setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
	}

	public void addChildren(GdZTreeNode node) {
		if(children==null) children=new ArrayList<GdZTreeNode>();
		children.add(node);
	}

	public Long getGridId() {
		return gridId;
	}

	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}

	public String getGridCode() {
		return gridCode;
	}

	public void setGridCode(String gridCode) {
		this.gridCode = gridCode;
	}

	public GdZTreeNode() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPId() {
		return pId;
	}

	public void setPId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getOpen() {
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

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean getHalfCheck() {
		return halfCheck;
	}

	public void setHalfCheck(boolean halfCheck) {
		this.halfCheck = halfCheck;
	}

	public boolean getChkDisabled() {
		return chkDisabled;
	}

	public void setChkDisabled(boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}

	public boolean getNocheck() {
		return nocheck;
	}

	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof GdZTreeNode)) {
			return false;
		} else {
			GdZTreeNode treenode = (GdZTreeNode) obj;
			return id.equals(treenode.id);
		}
	}

	public int hashCode() {
		return id.hashCode();
	}

	public String getLayerType() {
		return layerType;
	}

	public void setLayerType(String layerType) {
		this.layerType = layerType;
	}

	public List<GdZTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<GdZTreeNode> children) {
		this.children = children;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getGridPhoto() {
		return gridPhoto;
	}

	public void setGridPhoto(String gridPhoto) {
		this.gridPhoto = gridPhoto;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getGridLevel() {
		return gridLevel;
	}

	public void setGridLevel(String gridLevel) {
		this.gridLevel = gridLevel;
	}

	public String getProfessionCode() {
		return professionCode;
	}

	public void setProfessionCode(String professionCode) {
		this.professionCode = professionCode;
	}

	public boolean isClickable() {
		return clickable;
	}

	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}
	
}