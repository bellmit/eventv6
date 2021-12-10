package cn.ffcs.zhsq.base.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * MyBatis方法接口
 * @author huangzhb
 */
public interface MyBatisBaseMapper<T> {
	
	public int insert(T entity);
	
	public int update(T entity);
	
	public int delete(@Param(value="userId") Long userId, @Param(value="id") Long id);
	public int delete(@Param(value="id") Long id);
	public T findById(Long id);
	
	/**
	 * 根据条件统计记录数
	 * @param param 参数
	 * @return
	 */
	public int findCountByCriteria(Map<String, Object> param);
	
	/**
	 * 根据条件统计记录数
	 * @param param 参数
	 * @return
	 */
	public int findCountByCriteriaByGridLevel(Map<String, Object> param);

	/**
	 * 根据条件搜索实现分页
	 * @param param 参数
	 * @param bounds 分页信息
	 * @return
	 */
	public List<T> findPageListByCriteria(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 根据条件搜索实现分页
	 * @param param 参数
	 * @param bounds 分页信息
	 * @return
	 */
	public List<T> findPageListByCriteria2(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 根据条件搜索实现分页
	 * @param param 参数
	 * @param bounds 分页信息
	 * @return
	 */
	public List<T> findPageListByCriteriaByGridLevel(Map<String, Object> param, RowBounds bounds);
}
