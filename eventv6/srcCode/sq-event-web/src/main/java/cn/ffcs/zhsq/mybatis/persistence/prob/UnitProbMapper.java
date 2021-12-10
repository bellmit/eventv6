package cn.ffcs.zhsq.mybatis.persistence.prob;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;


public interface UnitProbMapper extends MyBatisBaseMapper<Map<String,Object>>{

	List<Map<String,Object>> findPageList4Unit(Map<String, Object> params,RowBounds rowBounds);
	
	List<Map<String,Object>> findPageList4Unit(Map<String, Object> params);

	int findCount4Unit(Map<String, Object> params);

	Map<String, Object> searchById(int id);

}
