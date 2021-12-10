package cn.ffcs.zhsq.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.ffcs.zhsq.utils.ConstantValue;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateModelException;

/**
 * @author zengy 2014-5-5
 * 
 * */
public class FreeMarkerRightTag extends RightTag {

	/**
	 * 获取该用户在这个页面的所有权限。
	 * */
	private Map<String, String> getRightsFromDB() {
		Map<String, String> temp = new HashMap<String, String>();
		Map<String, String> json = (Map<String, String>) pageContext.getSession()
				.getAttribute(ConstantValue.SYSTEM_PRIVILEGE_MAP);
		Set<Map.Entry<String, String>> set = json.entrySet();
		for (Map.Entry<String, String> entry : set) {
			if (entry.getValue().equals(super.parentCode)) {
				// json.remove(entry.getKey());
				temp.put(entry.getKey(), entry.getValue());
			}
		}
		return temp;
	}
	
	/**
	 * @throws TemplateModelException 
	 * 从pageContext容器里读取权限，如果没有就去数据库获取。
	 * 
	 * @return 返回用户在该页面能访问到的所有权限
	 * @throws
	 * */
	private Map<String, String> getRights() throws TemplateModelException {
		Object obj = pageContext.getAttribute(RIGTHTVERSIONUID);
		if (obj == null) {
			// 获取权限
			Map<String, String> rights = getRightsFromDB();
			if (rights == null)
				rights = new HashMap<String, String>();
			pageContext.setAttribute(RIGTHTVERSIONUID, rights);
		}
		SimpleHash hash = (SimpleHash) pageContext.getAttribute(RIGTHTVERSIONUID);
		return hash.toMap();
	}
	
	/**
	 * 判断用户是否拥有该权限
	 * */
	@Override
	public boolean hasRight() {
		/*try {
			Map<String, String> rights = getRights();
			if (rights.containsKey("all")) {
				return true;
			} else {
				return rights.containsKey(super.rightCode);
			}
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}*/
		return true;
	}

}
