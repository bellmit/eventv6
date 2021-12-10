package cn.ffcs.zhsq.mybatis.persistence.requestion;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.requestion.CorpLink;

import java.util.List;
import java.util.Map;

/**
 * @Description: 企业联动单位模块dao接口
 * @Author: caiby
 * @Date: 03-12 10:28:45
 * @Copyright: 2018 福富软件
 */
public interface CorpLinkMapper {
	
	/**
	 * 新增数据
	 * @param bo 企业联动单位业务对象
	 * @return 企业联动单位id
	 */
	public long insert(CorpLink bo);
	
	/**
	 * 修改数据
	 * @param bo 企业联动单位业务对象
	 * @return 修改的记录数
	 */
	public long update(CorpLink bo);
	
	/**
	 * 删除数据
	 * @param bo 企业联动单位业务对象
	 * @return 删除的记录数
	 */
	public long delete(CorpLink bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 企业联动单位数据列表
	 */
	public List<CorpLink> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 企业联动单位数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 企业联动单位id
	 * @return 企业联动单位业务对象
	 */
	public CorpLink searchById(Long id);
	/**
	 * 根据业务gridId查询数据
	 * @param gridId 组织树选择的单位gridId
	 * @return 企业联动单位业务对象
	 */
	public CorpLink searchByGridId(Long id);

}