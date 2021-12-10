package cn.ffcs.zhsq.security;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 权限控制标签
 * 
 * @author zengy 2014-05-05
 * */
public abstract class RightTag extends TagSupport {

	/**
	 * 标识 (把该用户在这个页面所有的权限都存在pageContext容器或别的缓存里统一的标识符)
	 * */ 
	protected static final String RIGTHTVERSIONUID = "RIGHT_VID";

	/**
	 * 父权限编号
	 * */
	protected String parentCode="8800";
	/**
	 * 权限编号
	 * */
	protected String rightCode = "8888";
	
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public void setRightCode(String rightCode) {
		this.rightCode = rightCode;
	}

	/**
	 * 处理是否显示标签
	 * */
	public int doStartTag() throws JspException {
		if (hasRight()) {
			return 1;
		}
		//return SKIP_BODY; // 跳过body,body部分不会显示
		return ConstantValue.ENABLE_BTN_AUTHORITY ? SKIP_BODY : 1; // 开发模式
	}

	/**
	 * 判断用户是否拥有该权限（具体判断权限的方法可能有所差异，所以这边用抽象）
	 * */
	 public abstract boolean hasRight();

}
