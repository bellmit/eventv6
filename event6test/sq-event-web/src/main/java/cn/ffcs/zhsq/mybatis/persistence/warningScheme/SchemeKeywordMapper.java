package cn.ffcs.zhsq.mybatis.persistence.warningScheme;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.warningScheme.SchemeKeyword;

import java.util.List;
import java.util.Map;

/**
 * @Description: 方案匹配关键字模块dao接口
 * @Author: youwj
 * @Date: 05-28 16:24:45
 * @Copyright: 2019 福富软件
 */
public interface SchemeKeywordMapper {
	
	/**
	 * 新增数据
	 * @param bo 方案匹配关键字业务对象
	 * @return 方案匹配关键字id
	 */
	public long insert(SchemeKeyword bo);
	
	/**
	 * 修改数据
	 * @param bo 方案匹配关键字业务对象
	 * @return 修改的记录数
	 */
	public long update(SchemeKeyword bo);
	
	/**
	 * 删除数据
	 * @param bo 方案匹配关键字业务对象
	 * @return 删除的记录数
	 */
	public long delete(SchemeKeyword bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 方案匹配关键字数据列表
	 */
	public List<SchemeKeyword> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 方案匹配关键字数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 方案匹配关键字id
	 * @return 方案匹配关键字业务对象
	 */
	public SchemeKeyword searchById(Long id);

	/**
	 * 根据外键scheme_id查询数据
	 * @param id 方案匹配关键字id
	 * @return 方案匹配关键字业务对象
	 */
	public List<SchemeKeyword> searchBySchemeId(Long schemeId);

	/**
	 * 根据集合批量新增
	 */
	public void insertByList(List<SchemeKeyword> list);
	
	/**
	 * 根据集合批量修改
	 */
	public void updateByList(List<SchemeKeyword> list);

	/**
	 * 获取所有的code
	 */
	public List<String> findAllCode();

	
	/**
	 * 判断关键字匹配
	 */
	public Map<String, Object> fetchKeyword(Map<String, Object> params);

}