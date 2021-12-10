package cn.ffcs.shequ.utils;

import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.SpringContextUtil;
import cn.ffcs.shequ.commons.util.StringUtils;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataDictHelper {

	private static Logger logger = LoggerFactory.getLogger(DataDictHelper.class);
	
	private static IBaseDictionaryService getDictService() {
//		IBaseDictionaryService dictionaryService = SpringContextUtil.getApplicationContext().getBean(cn.ffcs.uam.service.IBaseDictionaryService.class);
		 IBaseDictionaryService dictionaryService = (IBaseDictionaryService) SpringContextUtil.getBean("baseDictionaryService");
		return dictionaryService;
	}

	public static void setDictValueForField(Object obj, String soFieldName,
			String toFieldName, String dictPCode, String orgCode)
			throws Exception {
		int flag = 0;
		Field m_soField = null;
		Field m_toField = null;
		for (Field field : obj.getClass().getDeclaredFields()) {
			if (field.getName().equals(soFieldName)) {
				m_soField = field;
				flag++;
			} else if (field.getName().equals(toFieldName)) {
				m_toField = field;
				flag++;
			}
			if (flag == 2) break;
		}
		if (m_soField == null || m_toField == null) {
			if(logger.isErrorEnabled()) {
				logger.error("未找到该对象有【" + soFieldName + "】或【" + toFieldName + "】属性！");
			}
			throw new Exception("未找到该对象有【" + soFieldName + "】或【" + toFieldName + "】属性！");
		}
		m_soField.setAccessible(true);
		String value = String.valueOf(m_soField.get(obj));
		setDictValueEx(obj, value, m_toField, dictPCode, orgCode);
	}
	
	public static void setDictValueForValue(Object obj, String soValue,
			String toFieldName, String dictPCode, String orgCode)
			throws Exception {
		Field m_toField = null;
		for (Field field : obj.getClass().getDeclaredFields()) {
			if (field.getName().equals(toFieldName)) {
				m_toField = field;
				break;
			}
		}
		if (m_toField == null) {
			if(logger.isErrorEnabled()) {
				logger.error("未找到该对象有【" + toFieldName + "】属性！");
			}
			throw new Exception("未找到该对象有【" + toFieldName + "】属性！");
		}
		setDictValueEx(obj, soValue, m_toField, dictPCode, orgCode);
	}
	
	public static void setDictValueEx(Object obj, String soVal,
			Field toField, String dictPCode, String orgCode)
			throws Exception {
		toField.setAccessible(true);
		List<BaseDataDict> dataDict = getDictService().getDataDictListOfSinglestage(dictPCode, orgCode);
		for (BaseDataDict baseDataDict : dataDict) {
			if (baseDataDict.getDictGeneralCode().equals(soVal)) {
				toField.set(obj, baseDataDict.getDictName());
				break;
			}
		}
	}
	
	public static List<Map<String, Object>> getDataDictList(String dictPcode, String orgCode) {
		List<Map<String, Object>> treeList = new ArrayList<Map<String,Object>>();
		List<BaseDataDict> dicts = getDictService().getDataDictListOfSinglestage(dictPcode, orgCode);
		if (dicts != null && dicts.size() > 0) {
			for (BaseDataDict dict : dicts) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", dict.getDictId());
				map.put("pid", dict.getDictPid());
				map.put("name", dict.getDictName());
				map.put("value", dict.getDictGeneralCode());
				map.put("dictCode", dict.getDictCode());
				map.put("dictPcode", dict.getDictPcode());
				treeList.add(map);
			}
		}
		return treeList;
	}
	
	public static List<Map<String, Object>> getDataDictTree(String dictPcode, String orgCode) {
		List<Map<String, Object>> treeList = new ArrayList<Map<String,Object>>();
		List<BaseDataDict> dicts = getDictService().getDataDictListOfSinglestage(dictPcode, orgCode);
		getDataDictTreeEx(dicts, treeList, orgCode);
		return treeList;
	}
	
	private static void getDataDictTreeEx(List<BaseDataDict> dicts, List<Map<String, Object>> treeList, String orgCode) {
		for (BaseDataDict dict : dicts) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", dict.getDictId());
			map.put("pid", dict.getDictPid());
			map.put("name", dict.getDictName());
			map.put("value", dict.getDictGeneralCode());
			map.put("dictCode", dict.getDictCode());
			map.put("dictPcode", dict.getDictPcode());
			List<BaseDataDict> subDicts = getDictService().getDataDictListOfSinglestage(dict.getDictCode(), orgCode);
			if (subDicts != null && subDicts.size() > 0) {
				map.put("nocheck", true);
				treeList.add(map);
				getDataDictTreeEx(subDicts, treeList, orgCode);
			} else {
				treeList.add(map);
			}
		}
	}
	
	
	/**
	 * Map<String, Object>中字典转换
	 * @param map
	 * @param fromKeyName	需要转换的key值
	 * @param toKeyName		转换后的字典名称存放的key值
	 * @param dataDictList	用户转换的字典
	 */
	public static void setDictValueForField(Map<String, Object> map, String fromKeyName,
			String toKeyName, List<BaseDataDict> dataDictList) {
		if(dataDictList != null && CommonFunctions.isNotBlank(map, fromKeyName)) {
			String valueStr = map.get(fromKeyName).toString();
			if(StringUtils.isNotBlank(valueStr)) {
				String[] valueArray = valueStr.split(",");
				StringBuffer dictNameBuffer = new StringBuffer("");
				
				for(String value : valueArray) {
					for(BaseDataDict dataDict : dataDictList) {
						if(value.equals(dataDict.getDictGeneralCode())) {
							dictNameBuffer.append(",").append(dataDict.getDictName());
							break;
						}
					}
				}
				
				if(dictNameBuffer.length() > 0) {
					map.put(toKeyName, dictNameBuffer.substring(1));
				}
			}
		}
	}
	
	
	private static void setDictValueEx(Object obj, String soVal,
			Field toField, List<BaseDataDict> dataDict)
			throws Exception {
		toField.setAccessible(true);
		if(dataDict != null && StringUtils.isNotBlank(soVal)) {
			String[] soValArray = soVal.split(",");
			StringBuffer dictName = new StringBuffer("");
			
			for(String value : soValArray) {
				for (BaseDataDict baseDataDict : dataDict) {
					if (baseDataDict.getDictGeneralCode().equals(value)) {
						dictName.append(",").append(baseDataDict.getDictName());
						break;
					}
				}
			}
			
			if(dictName.length() > 0) {
				toField.set(obj, dictName.substring(1));
			}
		}
	}
	
	
	
}
