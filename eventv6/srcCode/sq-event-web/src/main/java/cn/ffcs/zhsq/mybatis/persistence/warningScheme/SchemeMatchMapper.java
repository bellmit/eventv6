package cn.ffcs.zhsq.mybatis.persistence.warningScheme;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.warningScheme.SchemeMatch;

import java.util.List;
import java.util.Map;


/**
 * @Description: 方案判定匹配表模块dao接口
 * @Author: youwj
 * @Date: 05-28 16:24:21
 * @Copyright: 2019 福富软件
 */
public interface SchemeMatchMapper {
	
	/**
	 * 新增数据
	 * @param bo 方案判定匹配表业务对象
	 * @return 方案判定匹配表id
	 */
	public long insert(SchemeMatch bo);
	
	/**
	 * 修改数据
	 * @param bo 方案判定匹配表业务对象
	 * @return 修改的记录数
	 */
	public long update(SchemeMatch bo);
	
	/**
	 * 删除数据
	 * @param bo 方案判定匹配表业务对象
	 * @return 删除的记录数
	 */
	public long delete(SchemeMatch bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 方案判定匹配表数据列表
	 */
	public List<SchemeMatch> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 方案判定匹配表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 方案判定匹配表id
	 * @return 方案判定匹配表业务对象
	 */
	public SchemeMatch searchById(Long id);


	/**
	 * 根据业务id伪删
	 * @param id 方案判定匹配表id
	 * @return 方案判定匹配表业务对象
	 */
	public Long deleteByUpdateStatus(Long schemeId);

	/**
	 * 获取生效的记录
	 */
	public List<SchemeMatch> findSchemeEffect();

}