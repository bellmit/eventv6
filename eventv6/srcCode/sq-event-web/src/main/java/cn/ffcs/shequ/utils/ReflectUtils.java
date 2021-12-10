package cn.ffcs.shequ.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射工具类
 * @author zkongbai
 *
 */
public class ReflectUtils {
	/**
	 * 获取某个类的所有字段名称
	 * @param clazz
	 * @return
	 */
	public static List<String> getAllFieldName(Class<?> clazz){
		Field[] fields = clazz.getDeclaredFields();//获取所有字段,包含private
		List<String> ret = new ArrayList<String>();
		String fieldName = null;
		for(Field field : fields){
			fieldName = field.getName();
			if("serialVersionUID".equals(fieldName))
				continue;
			ret.add(fieldName);
		}
		return ret;
	}
	
	/**
	 * 将驼峰格式的字段转换为数据库格式字段形式 [eg:carNo-> car_no]
	 * @param fieldName
	 * @param upper 是否需要全部转换为大写字母, true:转换结果为大写
	 * @return
	 */
	public static String convert2DbField(String fieldName,boolean upper){
		char[] charArr = fieldName.toCharArray();
		String ret = "";
		int charCode = -1;
		for(char ch : charArr){
			charCode = (int)ch;
			if(charCode >=65 && charCode < 97){//大写字母
				ret += "_" + Character.toLowerCase(ch);
			}else{
				ret += ch;
			}
		}
		if(upper)
			ret = ret.toUpperCase();
		return ret;
	}
	
}
