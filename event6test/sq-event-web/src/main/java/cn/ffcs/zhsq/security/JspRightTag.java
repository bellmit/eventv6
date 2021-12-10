package cn.ffcs.zhsq.security;

import java.util.ArrayList;
import java.util.List;

import freemarker.template.SimpleSequence;
import freemarker.template.TemplateModelException;

/**
 * @author zengy 2014-5-5
 * 
 * */
public class JspRightTag extends RightTag {

	/**
	 * 获取该用户在这个页面的所有权限。
	 * */
	private List<String> getRightsFromDB() {
		//user = (user)pageContext.getSession().getAttribute("user");
		//这里可以获得 user.id ,parentCode
		// todo：这是目前至是测试
		List<String> test = new ArrayList<String>();
		test.add("8881");
		test.add("8882");
		test.add("8883");
		test.add("8884");
		return test;
	}
	
	/**
	 * 从pageContext容器里读取权限，如果没有就去数据库获取。
	 * 
	 * @return 返回用户在该页面能访问到的所有权限
	 * @throws
	 * */
	private List<String> getRights() {
		Object obj = pageContext.getAttribute(RIGTHTVERSIONUID);
		if (obj == null) {
			// 获取权限
			List<String> rights = getRightsFromDB();
			if (rights == null)
				rights = new ArrayList<String>();
			pageContext.setAttribute(RIGTHTVERSIONUID, rights);
		}
		
		return (List<String>) pageContext.getAttribute(RIGTHTVERSIONUID);
	}
	
	/**
	 * 判断用户是否拥有该权限
	 * */
	@Override
	public boolean hasRight() {
		List<String> rights = getRights();
		for (String r : rights) {
			if (r.equals(rightCode))
				return true;
		}
		return false;
	}
}
