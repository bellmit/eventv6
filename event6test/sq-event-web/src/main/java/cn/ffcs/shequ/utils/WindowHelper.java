package cn.ffcs.shequ.utils;

import java.util.List;
import java.util.Map;

public class WindowHelper {

	public static String getPmStr(Map<String, Object> params, String key) {
		Object obj = params.get(key);
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}
	
	public static String getPmStr(Object obj, String defaultValue) {
		if (obj == null) {
			return defaultValue;
		} else {
			return String.valueOf(obj);
		}
	}
	
	public static String getPmStr(Object obj) {
		return WindowHelper.getPmStr(obj, "");
	}
	
	public static String decode(String val, String... trans) throws Exception {
		if (trans != null && trans.length > 1) {
			boolean isComplex = trans.length % 2 == 0;// 是否为复数
			for (int i = 0; i < (isComplex ? trans.length : trans.length - 1); i+=2) {
				if (val == null) {// 判断为null的情况
					if (val == trans[i]) {
						return trans[i + 1];
					}
				} else if (val.equals(trans[i])) {
					return trans[i + 1];
				}
			}
			return isComplex ? val : trans[trans.length - 1];
		}
		throw new Exception("please don't use this function!");
	}
	
	public static String decode(String val, List<Map<String, Object>> dc) throws Exception {
		return WindowHelper.decode(val, WindowHelper.listToArray(dc));
	}
	
	public static String[] listToArray(List<Map<String, Object>> dc) throws Exception {
		if (dc != null && dc.size() > 0) {
			String[] trans = new String[dc.size() * 2 + 1];
			for (int i = 0; i < dc.size(); i++) {
				Map<String, Object> map = dc.get(i);
				trans[i * 2]  = String.valueOf(map.get("COLUMN_VALUE"));
				trans[i * 2 + 1]  = String.valueOf(map.get("COLUMN_VALUE_REMARK"));
			}
			return trans;
		}
		throw new Exception("please don't use this function!");
	}
	
	public static String addQuotes(List<String> arg0) {
		if (arg0.size() > 0) {
			return arg0.toString().replaceAll(" ", "").replace("[", "'").replace("]", "'").replaceAll(",", "','");
		}
		return "";
	}
	
	public static String addQuotes(String arg0) {
		if (arg0 != null && arg0.trim().length() > 0) {
			return "'" + arg0.trim().replaceAll(",", "','") + "'";
		}
		return "";
	}
	
	public static void main(String[] args) throws Exception {
		String[] arg = { "1", "2", "3", "4", "5", "6", "7" };
		for (String val : arg) {
			System.out.println(WindowHelper.decode(val, "1", "一", "2", "二",
					"3", "三", "4", "四", "5", "五", "无"));
		}
	}
}
