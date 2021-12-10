package cn.ffcs.zhsq.mybatis.persistence.triadRelatedCases;

import cn.ffcs.zhsq.mybatis.domain.triadRelatedCases.CaseFilling;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import java.util.List;
import java.util.Map;


/**
 * @Description: 涉黑案件填报表模块dao接口
 * @Author: chenshikai
 * @Date: 09-08 17:46:05
 * @Copyright: 2021 福富软件
 */
public interface CaseFillingMapper {
	
	/**
	 * 新增数据
	 * @param bo 涉黑案件填报表业务对象
	 * @return 涉黑案件填报表id
	 */
	public Long insert(CaseFilling bo);
	
	/**
	 * 修改数据
	 * @param bo 涉黑案件填报表业务对象
	 * @return 修改的记录数
	 */
	public long update(CaseFilling bo);
	
	/**
	 * 删除数据
	 * @param bo 涉黑案件填报表业务对象
	 * @return 删除的记录数
	 */

	public long delete(@Param(value = "userId")Long userId,@Param(value = "undCaseUuidList") List<String> undCaseUuidList);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 涉黑案件填报表数据列表
	 */
	public List<CaseFilling> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 涉黑案件填报表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 涉黑案件填报表id
	 * @return 涉黑案件填报表业务对象
	 */
	public CaseFilling searchById(String undCaseUuid);


	/**
	 * 获取下一级地市名称
	 * @param params 查询参数
	 * @return 涉黑案件填报表数据总数
	 */
	public List<Map<String,Object>> countByRegionList(String orgCode);
	/**
	 * 汇总
	 * @param params 查询参数
	 * @return 涉黑案件填报表数据总数
	 */
	public Long countTotal(Map<String, Object> params);

	/**
	 * 按类型获取数据
	 * @param params 查询参数
	 * @return 涉黑案件填报表数据总数
	 */
	public List<Map<String,Object>> countBytypeList(Map<String, Object> params);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 涉黑案件填报表数据列表
	 */
	public List<CaseFilling> searchzzList(Map<String, Object> params, RowBounds rowBounds);

	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 涉黑案件填报表数据总数
	 */
	public long countzzList(Map<String, Object> params);


	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 涉黑案件填报表数据列表
	 */
	public List<CaseFilling> searchzsList(Map<String, Object> params, RowBounds rowBounds);

	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 涉黑案件填报表数据总数
	 */
	public long countzsList(Map<String, Object> params);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 涉黑案件填报表数据列表
	 */
	public List<CaseFilling> searchysList(Map<String, Object> params, RowBounds rowBounds);

	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 涉黑案件填报表数据总数
	 */
	public long countysList(Map<String, Object> params);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 涉黑案件填报表数据列表
	 */
	public List<CaseFilling> searchesList(Map<String, Object> params, RowBounds rowBounds);

	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 涉黑案件填报表数据总数
	 */
	public long countesList(Map<String, Object> params);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 涉黑案件填报表数据列表
	 */
	public List<CaseFilling> searchsjList(Map<String, Object> params, RowBounds rowBounds);

	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 涉黑案件填报表数据总数
	 */
	public long countsjList(Map<String, Object> params);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 涉黑案件填报表数据列表
	 */
	public List<CaseFilling> searchallList(Map<String, Object> params, RowBounds rowBounds);

	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 涉黑案件填报表数据总数
	 */
	public long countallList(Map<String, Object> params);

}