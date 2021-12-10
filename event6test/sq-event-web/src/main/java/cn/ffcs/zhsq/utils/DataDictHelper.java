package cn.ffcs.zhsq.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.EasyUITreeNode;
import cn.ffcs.uam.service.IBaseDictionaryService;

public class DataDictHelper {
	
	private static Logger logger = LoggerFactory.getLogger(DataDictHelper.class);
	
	private static IBaseDictionaryService dictionaryService = null;

	private static IBaseDictionaryService getDictService() {
		if (dictionaryService == null) {
			dictionaryService = (IBaseDictionaryService) SpringContextUtil.getBean("baseDictionaryService");
		}
		return dictionaryService;
	}

	public static void setDictValueForField(Object obj, String fromFieldName,
			String toFieldName, String dictPCode, String orgCode) {
		try {
			int flag = 0;
			Field m_soField = null;
			Field m_toField = null;
			for (Field field : obj.getClass().getDeclaredFields()) {
				if (field.getName().equals(fromFieldName)) {
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
					logger.error("未找到该对象有【" + fromFieldName + "】或【" + toFieldName + "】属性！");
				}
				throw new Exception("未找到该对象有【" + fromFieldName + "】或【" + toFieldName + "】属性！");
			}
			m_soField.setAccessible(true);
			String value = String.valueOf(m_soField.get(obj));
			setDictValueEx(obj, value, m_toField, dictPCode, orgCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将对象属性进行字典翻译
	 * @param obj			字典翻译涉及对象
	 * @param fromFieldName	待翻译的属性(字典业务编码)
	 * @param toFieldName	翻译结果存放属性(字典名称)
	 * @param dataDict		字典记录列表
	 */
	public static void setDictValueForField(Object obj, String fromFieldName,
			String toFieldName, List<BaseDataDict> dataDict) {
		try {
			int flag = 0;
			Field m_soField = null;
			Field m_toField = null;
			for (Field field : obj.getClass().getDeclaredFields()) {
				if (field.getName().equals(fromFieldName)) {
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
					logger.error("未找到该对象有【" + fromFieldName + "】或【" + toFieldName + "】属性！");
				}
				throw new Exception("未找到该对象有【" + fromFieldName + "】或【" + toFieldName + "】属性！");
			}
			m_soField.setAccessible(true);
			String value = String.valueOf(m_soField.get(obj));
			setDictValueEx(obj, value, m_toField, dataDict);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Map<String, Object>中字典翻译
	 * @param map			字典转换涉及的对象
	 * @param fromKeyName	需要转换的key值
	 * @param toKeyName		转换后的字典名称存放的key值
	 * @param dataDictList	用于转换的字典
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
	
	public static void setDictValueForValue(Object obj, String fromValue,
			String toFieldName, String dictPCode, String orgCode) {
		try {
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
			setDictValueEx(obj, fromValue, m_toField, dictPCode, orgCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void setDictValueEx(Object obj, String soVal,
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
	
	public static List<Map<String, Object>> getDataDictTree(String dictPcode, String orgCode) {
		List<Map<String, Object>> treeList = new ArrayList<Map<String,Object>>();
		EasyUITreeNode node = getDictService().getDataDictAllTree(dictPcode, orgCode);
		List<EasyUITreeNode> nodeList = node.getChildren();
		if (nodeList != null && nodeList.size() > 0) {
			getDataDictTreeRecursive(treeList, node.getId(), nodeList);
		}
		return treeList;
	}
	
	private static void getDataDictTreeRecursive(List<Map<String, Object>> treeList, String pId, List<EasyUITreeNode> nodeList) {
		for (EasyUITreeNode node : nodeList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", node.getId());
			map.put("pid", pId);
			map.put("name", node.getText());
			map.put("value", node.getAttributes());
			List<EasyUITreeNode> nodes = node.getChildren();
			if (nodes != null && nodes.size() > 0) {
				map.put("nocheck", true);
				treeList.add(map);
				getDataDictTreeRecursive(treeList, node.getId(), nodes);
			} else {
				treeList.add(map);
			}
		}
	}
	
	/**
	 * 获取多层级字典的全路径信息
	 * @param dictGeneralCode	起始字典业务编码
	 * @param dictList				进行字典翻译的字典列表
	 * @param dictCodeRoot		字典列表的根节点字典编码
	 * @return
	 * 		dictFullPath		字典名称
	 * 		dictName			字典名称全路径
	 * 		dictCode			字典编码
	 * 		dictCodeTop     	顶层字典编码
	 */
	public static Map<String, Object> capMultilevelDictInfo(String dictGeneralCode, String dictCodeRoot, List<BaseDataDict> dictList) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(StringUtils.isNotBlank(dictGeneralCode) && StringUtils.isNotBlank(dictCodeRoot) && dictList != null) {
			StringBuffer dictFullPath = new StringBuffer("");
			String bigType = dictGeneralCode, bigTypeName = "", bigDictCode = null, 
					  dictCodeTop = null;//顶层大类字典编码
			BaseDataDict dictInfo = null;
			
			do {
				bigTypeName = "";
				
				for(BaseDataDict dataDict : dictList) {
					if((StringUtils.isNotBlank(bigDictCode) && !dictCodeRoot.equals(bigDictCode) && bigDictCode.equals(dataDict.getDictCode()))
						||
						(StringUtils.isNotBlank(bigType) && bigType.equals(dataDict.getDictGeneralCode()))) {
						bigTypeName = dataDict.getDictName();
						bigDictCode = dataDict.getDictPcode();
						
						if(dictCodeRoot.equals(bigDictCode)) {
							dictCodeTop = dataDict.getDictCode();
						}
						
						if(dictGeneralCode.equals(bigType)) {
							dictInfo = dataDict;
						}
						
						bigType = null;
						break;
					}
				}
				
				if(StringUtils.isNotBlank(bigTypeName)) {
					dictFullPath.insert(0, bigTypeName).insert(0, "-");
				}
			} while(StringUtils.isNotBlank(bigTypeName));
			
			if(dictFullPath.length() > 0) {
				resultMap.put("dictFullPath", dictFullPath.substring(1));
				resultMap.put("dictCodeTop", dictCodeTop);
				
				if(dictInfo != null) {
					resultMap.put("dictCode", dictInfo.getDictCode());
					resultMap.put("dictName", dictInfo.getDictName());
				}
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 获取多层级字典的全路径信息
	 * @param session
	 * @param dictGeneralCode	起始字典业务编码
	 * @param dictList				进行字典翻译的字典列表
	 * @param dictCodeRoot		字典列表的根节点字典编码
	 * @return
	 * 		dictFullPath		字典名称
	 * 		dictName			字典名称全路径
	 * 		dictCode			字典编码
	 * 		dictCodeTop     	顶层字典编码
	 */
	public static Map<String, Object> capMultilevelDictInfo(String dictGeneralCode, String dictCodeRoot, String userOrgCode) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(StringUtils.isNotBlank(dictGeneralCode) && StringUtils.isNotBlank(dictCodeRoot)) {
			List<BaseDataDict> eventTypeDictList = null;
			Map<String, Object> dictMap = new HashMap<String, Object>();
			
			dictMap.put("orgCode", userOrgCode);
			dictMap.put("dictPcode", dictCodeRoot);
			
			eventTypeDictList = getDictService().findDataDictListByCodes(dictMap);
			
			resultMap = capMultilevelDictInfo(dictGeneralCode, dictCodeRoot, eventTypeDictList);
		}
		
		return resultMap;
	}
	
}
