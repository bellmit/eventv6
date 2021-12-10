package cn.ffcs.zhsq.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.Jedis;

/**
 * @文件描述: TODO:事件编号工具
 * @版权所有: Copyright(C)2013
 * @内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明
 * @完成日期: Apr 10, 2013
 * 修改日期			修改人		修改原因	版本号
 * 2013-5-15		YangCQ　　	新建		6231
 */
public class EventNoUtil {
	private static Logger logger = LoggerFactory.getLogger(EventNoUtil.class);

	@Autowired
	private static JedisConnectionFactory jedisConnectionFactory;

	private static JdbcTemplate jdbcTemplate = null;
	
	private static final String EVENT_CODE_FORMAT = "000000";
	
	private static JdbcTemplate getJdbc() {
		if (jdbcTemplate == null) {
			jdbcTemplate = (JdbcTemplate) SpringContextUtil.getBean("jdbcTemplateOracle");
		}
		return jdbcTemplate;
	}
	
	private static Connection getConnection() throws SQLException {
		return getJdbc().getDataSource().getConnection();
	}
	
	/**
	 * @date：Apr 10, 2013
	 * @Description：获取事件编号
	 * @param conn
	 * @param seqName
	 * @param eventType
	 * @return
	 * @throws Exception: 返回结果描述
	 * @return String: 返回值类型
	 * @throws
	 */
	public static String getEventNO(Connection conn, String seqName,
			String eventType) throws Exception {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String sql = "";
		String retVal = "";
		boolean CONN_FLAG = false;
		try {
			if (conn == null || conn.isClosed()) {
				CONN_FLAG = true;
				conn = EventNoUtil.getConnection();
			}
			sql = "SELECT TO_CHAR(SYSDATE,'YYYYMMDD') || '_" + eventType
					+ "_' || LPAD(" + seqName
					+ ".NEXTVAL, 6, '0') AS EVENT_NO FROM DUAL";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			if (rs.next()) {
				retVal = rs.getString("EVENT_NO");
			} else {
				retVal = "";
			}
		} catch (Exception e) {
			logger.error("获取事件编号出错，seqName:{}，eventType：{}",seqName,eventType,e);
		} finally {
			if (rs != null) rs.close();
			if (pstm != null) pstm.close();
			if (CONN_FLAG) {
				if (conn != null) conn.close();
			}
		}
		return retVal;
	}
	
	/**
	 * @date：DEC 20, 2019
	 * @Description：从数据库中获取事件编号
	 * @param eventType
	 * @return
	 * @throws Exception: 返回结果描述
	 * @return String: 返回值类型
	 * @throws
	 */
	public static String getEventNoByRedis(Connection conn, String seqName,
			String eventType) throws Exception {
		String result="";
		try {
			jedisConnectionFactory = (JedisConnectionFactory) SpringContextUtil.getBean("jedisConnectionFactory");
			Jedis jedis = jedisConnectionFactory.getShardInfo().createResource();
			Long curEventCode = getCurEventCode(jedis,ConstantValue.EVENT_CODE,String.valueOf(ConstantValue.JEDIS_DB_EVENT));
			result=DateUtils.formatDate(new Date(), "yyyyMMdd")
					+"_"+eventType+"_"+Lpad(curEventCode);
		} catch (Exception e) {
			logger.error("redis获取事件编号出错,seqName:{},eventType:{},redis server:{}:{}",seqName,eventType,
					jedisConnectionFactory.getHostName(),jedisConnectionFactory.getPort(),e);
			return getEventNO(conn,seqName,eventType);
		}
		return result;
		
	}

	private static Long getCurEventCode(Jedis jedis,String key,String field){
		Long cur = 0L;
		if(jedis.exists(key)){
			cur = jedis.hincrBy(key, field, 1L);//将当前值加1
		}else{//如果当前的key不存在或者过期了
			if(logger.isInfoEnabled()){
				logger.info("首次生成事件编号，当前的key:{},field:{}不存在",key,field);
			}
			jedis.hset(key, field, "0");//创建一个新的key，初始值设为0
			jedis.expire(key, DateUtils.getSecondsNextEarlyMorning());//设定这个key的存在时限（直到第二天凌晨0点）
			cur = jedis.hincrBy(key, field, 1L);//将当前值加1
		}
		
		if(jedis.ttl(key)==-1){//恰巧在执行incr的时候key值消失，导致key重新被赋值为0再执行加1操作,此时是没有设置有效时间的
			jedis.hset(key, field, "0");//创建一个新的key，初始值设为0
			jedis.expire(key, DateUtils.getSecondsNextEarlyMorning());//设定这个key的存在时限（直到第二天凌晨0点）
			cur = jedis.hincrBy(key, field, 1L);//将当前值加1
		}
        return cur;		
	}

	private static String Lpad(Long eventCodeLong){
	    DecimalFormat df = new DecimalFormat(EVENT_CODE_FORMAT);
	    return df.format(eventCodeLong);
	}
}
