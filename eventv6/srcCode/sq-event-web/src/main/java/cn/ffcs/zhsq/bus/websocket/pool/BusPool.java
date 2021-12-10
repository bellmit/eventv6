package cn.ffcs.zhsq.bus.websocket.pool;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BusPool {

	private static Logger logger = LoggerFactory.getLogger(BusPool.class);
	private static volatile Map<String, Session> BUS_SESSION_POOL = new ConcurrentHashMap<String, Session>();
	
	public static void add(Session session){
		if(logger.isDebugEnabled())
			logger.debug("加入session:{}",session.getId());
		BUS_SESSION_POOL.put(session.getId(), session);
	}
	
	public static void remove(Session session){
		if(logger.isDebugEnabled())
			logger.debug("移除session:{}",session.getId());
		BUS_SESSION_POOL.remove(session.getId());
	}
	
	/**
	 * 给所有注册用户发送消息
	 * @param msg
	 */
	public static void send2All(String msg){
		Session session = null;
		for(Map.Entry<String, Session> entry : BUS_SESSION_POOL.entrySet()){
			session = entry.getValue();
			if(!session.isOpen()){
				if(logger.isDebugEnabled())
					logger.debug("session失效:{}",session.getId());
				remove(session);
			}else{
				try {
					session.getBasicRemote().sendText(msg);
				} catch (IOException e) {
					logger.error("webSocket发送数据失败:{}",e.getMessage());
				}
			}
		}
	}
}
