package cn.ffcs.zhsq.mybatis.persistence.prob;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;

public interface PersonProbMapper extends MyBatisBaseMapper<Map<String,Object>>{

	int findCount4Person(Map<String, Object> params);

	List<Map<String,Object>> findPageList4Person(Map<String, Object> params,
			RowBounds rowBounds);
	/**
	 * 未分页的
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> findPageList4Person(Map<String, Object> params);

	Map<String, Object> searchById(int probId);
}
