package cn.ffcs.zhsq.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 具体权限处理类
 * @author zengy 2014-05-07
 * */
public class WebRightHandler implements IRightHandler {
	
	/**
	 * 获取该用户在这个页面的所有权限。
	 * */
	private Map<String, String> getRightsFromDB(String parentCode) {
		Map<String, String> temp = new HashMap<String, String>();
		// 获得session对象
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		Map<String, String> json = (Map<String, String>) session
				.getAttribute(ConstantValue.SYSTEM_PRIVILEGE_ACTION);
		Set<Map.Entry<String, String>> set = json.entrySet();
		for (Map.Entry<String, String> entry : set) {
			if (entry.getValue().equals(parentCode)) {
				json.remove(entry.getKey());
				temp.put(entry.getKey(), entry.getValue());
			}
		}
		return temp;
	}

	/**
	 * 权限判断
	 * */
	@Override
	public void hasRight(String parentCode, String rightCode) throws Exception {
		Map<String, String> rights = getRightsFromDB(parentCode);
		if (!rights.containsKey(rightCode)) {
			throw new Exception("没有权限");
		}
	}

}
