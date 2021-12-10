package cn.ffcs.zhsq.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * jackson工具类
 * @author zhangzhihua
 *
 */
public class JsonProcessUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(JsonProcessUtil.class);
	private static ObjectMapper objMapper;
	
	/**
	 * @Title: getMapperInstance
	 * @Description: 创建一个新的Mapper
	 */
	public static synchronized ObjectMapper getMapperInstance(boolean createNew) {
		if (createNew) {
			return new ObjectMapper();
		} 
		else if (objMapper == null) {
			objMapper = new ObjectMapper();
		}
		return objMapper;
	}
	
	/**
	 * @Title: toJsonByStream
	 * @Description: 将对象转化为Json
	 */
	public static String toJsonByStream(Object obj) {
		ObjectMapper mapper = getMapperInstance(false);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return toJsonByStream(mapper, dateFormat, obj);
	}
	
	/**
	 * @Title: toJsonByStream
	 * @Description: 将对象转化为Json，带时间格式
	 */
	public static String toJsonByStream(Object obj, DateFormat dateFormat) {
		ObjectMapper mapper = getMapperInstance(false);
		return toJsonByStream(mapper, dateFormat, obj);
	}
	
	/**
	 * @Title: toJsonByStream
	 * @Description: 将对象转化为Json
	 * 				   流方式处理比数据绑定方式要快
	 */
	public static String toJsonByStream(ObjectMapper mapper, DateFormat dateFormat, Object obj) {
		StringWriter sw = new StringWriter();
		JsonGenerator gen = null;
		try {
			gen = new JsonFactory().createJsonGenerator(sw);
			mapper.setDateFormat(dateFormat);
			mapper.writeValue(gen, obj);
			gen.close();
		} catch (IOException e) {
			logger.info("Json转换异常","toJsonByStream");
		} finally {
			try {
				if (gen != null && !gen.isClosed()) {
					gen.close();
				}				
			} catch (IOException e) {
				logger.info("Json转换异常","toJsonByStream");
			}
		}		
		return sw.toString();
	}
	
}
