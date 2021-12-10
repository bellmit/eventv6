package cn.ffcs.zhsq.mybatis.persistence.event;

import java.util.List;
import java.util.Map;


public interface EventOverviewMapper {

	Map<String, Object> findEventCountA(Map<String, Object> params);
	
	List<Map<String, Object>> findEventCountB(Map<String, Object> params);
	
	List<Map<String, Object>> findEventCountC(Map<String, Object> params);
	
	List<Map<String, Object>> findEventCountD(Map<String, Object> params);
	
	List<Map<String, Object>> findEventCountForMyL(Map<String, Object> params);
	
}
