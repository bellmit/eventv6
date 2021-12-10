package cn.ffcs.zhsq.mybatis.persistence.jointDefence;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 联防长
 * @author zhongshm
 *
 */
public interface JointDefenceMapper extends MyBatisBaseMapper<Map<String, Object>> {

	public int findCount(Map<String, Object> param);

	public List<Map<String, Object>> findPageList(Map<String, Object> param, RowBounds bounds);

	public List<Map<String, Object>> findPageList(Map<String, Object> param);

	public int findEventCount(Map<String, Object> param);

	public List<Map<String, Object>> findEventPageList(Map<String, Object> param, RowBounds bounds);
	
	
	public List<Map<String, Object>> findJointTeamList(Map<String, Object> param, RowBounds bounds);
	public int findJointTeamCount(Map<String, Object> param);
	
	public List<Map<String, Object>> findJointPersonList(Map<String, Object> param, RowBounds bounds);
	public int findJointPersonCount(Map<String, Object> param);
	
	public List<Map<String, Object>> findJointPersonList(Map<String, Object> param);

	public Map<String, Object> findGridPathByOrgCode(Map<String, Object> param);
	public Map<String, Object> findStatByOrgCode(Map<String, Object> param);
}
