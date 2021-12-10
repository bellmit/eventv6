package cn.ffcs.shequ.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.util.JSONUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ffcs.json.utils.JsonDateValueProcessor;

/**
 * Json-lib的封装类，实现json字符串与json对象之间的转换
 * 
 * @author lsx
 * 
 */
public class JsonHelper {
	protected static final Log log = LogFactory.getLog(JsonHelper.class);

	/**
	 * 处理number及其子类的null转换 具体的使用例子: List<Order> orderList = new
	 * ArrayList<Order>(); orderList.add(new Order()); JSONObject json = new
	 * JSONObject(); json.elementOpt("order",
	 * orderList,JsonUtil.NUMBER_NULL_JSONCONF);
	 * 通过以上的代码,order对象中的Integer属性如果有null的，就会自动转成json的null，不会默认为0
	 */
	public final static JsonConfig NUMBER_NULL_JSONCONF = createNumberNullValueJsonConfig();
	// 处理number的null与日期类
	public final static JsonConfig DEFAULT_AND_DATTE_JSONCONF = createDefaultAndDateJsonConfig();

	private JsonHelper() {
	}

	/**
	 * 从一个JSON 对象字符格式中得到一个java对象，形如： {"id" : idValue, "name" : nameValue,
	 * "aBean" : {"aBeanId" : aBeanIdValue, ...}}
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static Object getObject(String jsonString, Class clazz) {
		JSONObject jsonObject = null;
		try {
			setDataFormat2JAVA();
			jsonObject = JSONObject.fromObject(jsonString);
		} catch (Exception e) {
			log.error("转换object异常:", e);
		}
		return JSONObject.toBean(jsonObject, clazz);
	}

	/**
	 * 从一个JSON 对象字符格式中得到一个java对象，其中beansList是一类的集合，形如： {"id" : idValue, "name" :
	 * nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...}, beansList:[{}, {},
	 * ...]}
	 * 
	 * @param jsonString
	 * @param clazz
	 * @param map
	 *            集合属性的类型 (key : 集合属性名, value : 集合属性类型class) eg: ("beansList" :
	 *            Bean.class)
	 * @return
	 */
	public static Object getObject(String jsonString, Class clazz, Map map) {
		JSONObject jsonObject = null;
		try {
			setDataFormat2JAVA();
			jsonObject = JSONObject.fromObject(jsonString);
		} catch (Exception e) {
			log.error("json string convert to obj error:", e);
			return null;
		}
		return JSONObject.toBean(jsonObject, clazz, map);
	}

	/**
	 * 从一个JSON数组得到一个java对象数组，形如： [{"id" : idValue, "name" : nameValue}, {"id" :
	 * idValue, "name" : nameValue}, ...]
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static Object[] getObjectArray(String jsonString, Class clazz) {
		setDataFormat2JAVA();
		JSONArray array = JSONArray.fromObject(jsonString);
		Object[] obj = new Object[array.size()];
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			obj[i] = JSONObject.toBean(jsonObject, clazz);
		}
		return obj;
	}

	/**
	 * 从一个JSON数组得到一个java对象数组，形如： [{"id" : idValue, "name" : nameValue}, {"id" :
	 * idValue, "name" : nameValue}, ...]
	 * 
	 * @param object
	 * @param clazz
	 * @param map
	 * @return
	 */
	public static Object[] getObjectArray(String jsonString, Class clazz,
			Map map) {
		setDataFormat2JAVA();
		JSONArray array = JSONArray.fromObject(jsonString);
		Object[] obj = new Object[array.size()];
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			obj[i] = JSONObject.toBean(jsonObject, clazz, map);
		}
		return obj;
	}

	/**
	 * 从一个JSON数组得到一个java对象集合
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static List getObjectList(String jsonString, Class clazz) {
		setDataFormat2JAVA();
		JSONArray array = JSONArray.fromObject(jsonString);
		List list = new ArrayList();
		for (Iterator iter = array.iterator(); iter.hasNext();) {
			JSONObject jsonObject = (JSONObject) iter.next();
			list.add(JSONObject.toBean(jsonObject, clazz));
		}
		return list;
	}

	/**
	 * 从一个JSON数组得到一个java对象集合，其中对象中包含有集合属性
	 * 
	 * @param object
	 * @param clazz
	 * @param map
	 *            集合属性的类型
	 * @return
	 */
	public static List getObjectList(String jsonString, Class clazz, Map map) {
		setDataFormat2JAVA();
		JSONArray array = JSONArray.fromObject(jsonString);
		List list = new ArrayList();
		for (Iterator iter = array.iterator(); iter.hasNext();) {
			JSONObject jsonObject = (JSONObject) iter.next();
			list.add(JSONObject.toBean(jsonObject, clazz, map));
		}
		return list;
	}

	/**
	 * 从json HASH表达式中获取一个map，该map支持嵌套功能 形如：{"id" : "johncon", "name" : "小强"}
	 * 
	 * @param object
	 * @return
	 */
	public static Map getMap(String jsonString) {
		setDataFormat2JAVA();
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Map map = new HashMap();
		for (Iterator iter = jsonObject.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			map.put(key, jsonObject.get(key));
		}
		return map;
	}

	/**
	 * 从json数组中得到相应java数组 json形如：["123", "456"]
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Object[] getObjectArray(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return jsonArray.toArray();
	}

	private static void setDataFormat2JAVA() {
		// 设定日期转换格式
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd",
						"yyyy-MM-dd HH:mm:ss" }));
	}

	/**
	 * 把对象转换为json字符串 日期类型为默认的: YYYY-MM-DD HH:MM:SS
	 * 
	 * @param obj
	 * @return
	 */
	public static String getJsonString(Object obj) {
		if (obj == null)
			return "{}";
		return getJsonString(obj, DEFAULT_AND_DATTE_JSONCONF);
	}

	/**
	 * 取json字符串
	 * 
	 * @param obj
	 * @param cfg
	 * @return
	 */
	public static String getJsonString(Object obj, JsonConfig cfg) {
		if (obj != null) {

			if (isArray(obj)) {
				JSONArray jsonArray = JSONArray.fromObject(obj, cfg);
				return jsonArray.toString();
			} else {

				JSONObject jsonObject = JSONObject.fromObject(obj, cfg);
				return jsonObject.toString();
			}
		}
		return "{}";
	}

	/**
	 * 对象是否是数组
	 * 
	 * @param obj
	 * @return
	 */
	private static boolean isArray(Object obj) {
		return obj instanceof Collection || obj.getClass().isArray();
	}

	/**
	 * 处理number及其子类的null转换
	 * 
	 * @return
	 */
	public static JsonConfig createNumberNullValueJsonConfig() {
		JsonConfig conf = new JsonConfig();
		registerDefaultNullValueProcessor(conf);
		return conf;

	}

	/**
	 * 处理全部
	 * 
	 * @return
	 */
	public static JsonConfig createDefaultAndDateJsonConfig() {
		JsonConfig conf = new JsonConfig();
		registerDefaultNullValueProcessor(conf);
		registerDateValueProcessor(conf);
		return conf;
	}

	/**
	 * 处理日期类转换
	 * 
	 * @param conf
	 */
	public static void registerDateValueProcessor(JsonConfig conf) {
		conf.registerJsonValueProcessor(java.util.Date.class,
				new JsonDateValueProcessor());
	}

	/**
	 * 注册json的处理类
	 * 
	 * @param conf
	 */
	public static void registerDefaultNullValueProcessor(JsonConfig conf) {
		conf.registerDefaultValueProcessor(Number.class,
				new DefaultNullValueProcessor());
		conf.registerDefaultValueProcessor(AtomicInteger.class,
				new DefaultNullValueProcessor());
		conf.registerDefaultValueProcessor(BigDecimal.class,
				new DefaultNullValueProcessor());
		conf.registerDefaultValueProcessor(BigInteger.class,
				new DefaultNullValueProcessor());
		conf.registerDefaultValueProcessor(Byte.class,
				new DefaultNullValueProcessor());
		conf.registerDefaultValueProcessor(Double.class,
				new DefaultNullValueProcessor());
		conf.registerDefaultValueProcessor(Float.class,
				new DefaultNullValueProcessor());
		conf.registerDefaultValueProcessor(Integer.class,
				new DefaultNullValueProcessor());
		conf.registerDefaultValueProcessor(Long.class,
				new DefaultNullValueProcessor());
		conf.registerDefaultValueProcessor(Short.class,
				new DefaultNullValueProcessor());
	}

	/**
	 * null空值特殊处理类
	 * 
	 * @author zhaoxin
	 * 
	 */
	public static class DefaultNullValueProcessor implements
			DefaultValueProcessor {
		public Object getDefaultValue(Class type) {
			return JSONNull.getInstance();
		}
	}
}
