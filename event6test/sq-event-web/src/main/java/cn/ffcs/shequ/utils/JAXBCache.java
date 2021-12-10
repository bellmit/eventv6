package cn.ffcs.shequ.utils;


import java.util.concurrent.ConcurrentHashMap;

//import javax.xml.bind.ContextFinder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * JAXBContext 缓存类，主要解决在jdk8内存泄漏问题；
 * 涉及调用类：jaxbUtil.java和JaxbBinder.java
 * 
 * @author wuqf
 * @create 2018-1-6上午9:50:51
 */
public final class JAXBCache {
	//单例
	private static final JAXBCache instance = new JAXBCache();
	
	//JAXBContext cache池，避免每次都创建新的对接，jdk8会造成内存泄漏
	private final ConcurrentHashMap<Class<?>,JAXBContext> contextCache = new ConcurrentHashMap<Class<?>,JAXBContext>();
	
	/**
	 * 默认构造方法
	 */
	private JAXBCache(){		
	}
	
	/**
	 * 使用此类一定要使用单例
	 * @return
	 */
	public static JAXBCache getInstance(){
		return instance;
	}
	
	/*
	 * 从缓存中取JAXBContext
	 */
	JAXBContext getJAXBContext(Class<?> clazz) throws JAXBException {		
		JAXBContext context = contextCache.get(clazz);
		if(context ==null){
			context = JAXBContext.newInstance(clazz);
			contextCache.put(clazz, context);//每次存在则覆盖
			contextCache.putIfAbsent(clazz, context);//如果传入key对应的value已经存在，就返回存在的value，不进行替换。如果不存在，就添加key和value，返回null
		}
		return context;
	}
	
	/**
	 * 实现参数泛型支持
	 */
//	JAXBContext getJAXBContext(Class<?>... clazzs) throws JAXBException {
//		for(Class<?> clazz:clazzs){
//			if (clazz == null)
//				throw new IllegalArgumentException();
//		}
//		Class[] classesToBeBound = clazzs;
//		return ContextFinder.find(classesToBeBound, Collections.emptyMap());
//	}
}
