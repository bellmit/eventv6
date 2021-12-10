package cn.ffcs.zhsq.mybatis.persistence.PublicAppeal;

import cn.ffcs.zhsq.mybatis.domain.drugEnforcementEvent.DrugEnforcementEvent;
import cn.ffcs.zhsq.mybatis.domain.publicAppeal.PublicAppeal;
import org.apache.ibatis.session.RowBounds;
import java.util.List;
import java.util.Map;

/**
 * @Description: 公众诉求模块dao接口
 * @Author: zhongshm
 * @Date: 09-01 11:37:06
 * @Copyright: 2017 福富软件
 */
public interface PublicAppealMapper {
	
	/**
	 * 新增数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public long insert(PublicAppeal bo);
	
	/**
	 * 修改数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public long update(PublicAppeal bo);
	
	/**
	 * 删除数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public long delete(PublicAppeal bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 数据列表
	 */
	public List<PublicAppeal> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 业务id
	 * @return 业务对象
	 */
	public PublicAppeal searchById(Long id);
	public PublicAppeal searchByEventId(Long id);


	/**
	 * 获取待办记录统计数量
	 * @param params
	 * @return
	 */
	public Long findTodoCountByCriteria(Map<String, Object> params);

	/**
	 * 分页获取待办记录列表
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<PublicAppeal> findTodoListByCriteria(Map<String, Object> params, RowBounds bounds);

	/**
	 * 获取经办记录统计数量
	 * @param params
	 * @return
	 */
	public Long findDoneCountByCriteria(Map<String, Object> params);

	/**
	 * 分页获取经办记录列表
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<PublicAppeal> findDoneListByCriteria(Map<String, Object> params, RowBounds bounds);

	/**
	 * 获取我发起记录统计数量
	 * @param params
	 * @return
	 */
	public Long findMyCountByCriteria(Map<String, Object> params);

	/**
	 * 分页获取我发起记录列表
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<PublicAppeal> findMyListByCriteria(Map<String, Object> params, RowBounds bounds);

}