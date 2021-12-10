package cn.ffcs.zhsq.utils.filter;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 简单工厂模式
 * 
 * @author Administrator
 * 
 */
public class StringFilterFactory {
	private static final Log log = LogFactory.getLog(StringFilterFactory.class);
	private static final StringFilterFactory stringFilterFactory = new StringFilterFactory();
	private Map<String, String> filterMap = new HashMap<String, String>();

	private StringFilterFactory() {
		filterMap = ConstantValue.STRING_FILTER_MAP;
	}

	public static StringFilterFactory getInstance() {
		return stringFilterFactory;
	}

	// 通过一个以逗号分割的过滤器引用名串，获取过滤器
	public StringFilter getStringFilterChain(String chain) {
		if (chain == null || chain.length() == 0) {
			return new EmptyFilter();
		}

		if ("all".equalsIgnoreCase(chain)) {
			return getAllStringFilterChain();
		}

		String[] filters = chain.split("\\,");
		return getStringFilterChain(filters);
	}

	public StringFilter getAllStringFilterChain() {
		String[] filters = (String[]) filterMap.values().toArray();
		return getStringFilterChain(filters);
	}

	public StringFilter getStringFilterChain(String[] filters) {
		if (filters == null || filters.length == 0) {
			return new EmptyFilter();
		}

		StringFilter[] stringFilters = new StringFilter[filters.length];
		for (int i = filters.length - 1; i >= 0; i--) {
			stringFilters[i] = getStringFilter(filters[i]);
			if (i != filters.length - 1) {
				stringFilters[i].setNextStringFilter(stringFilters[i + 1]);
			} else {
				stringFilters[i].setNextStringFilter(null);
			}
		}
		return stringFilters[0];
	}

	public StringFilter getStringFilter(String key) {
		if (key != null) {
			key = key.trim();
			if(!filterMap.containsKey(key)) return new EmptyFilter();
			try {
				// 通过类名反射得到过滤器的实例
				Class clazz = Class.forName(filterMap.get(key));
				StringFilter stringFilter = (StringFilter) clazz.newInstance();
				return stringFilter;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				log.warn(e);
			} catch (InstantiationException e) {
				e.printStackTrace();
				log.warn(e);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				log.warn(e);
			}
		}
		return new EmptyFilter();
	}
}
