package cn.ffcs.zhsq.mybatis.persistence.ypms.mscase;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.DeptHandle;

/**
 * @Description: 延平民生派发单位处理过程表模块dao接口
 * @Author: zhangzhenhai
 * @Date: 05-03 14:34:50
 * @Copyright: 2018 福富软件
 */
public interface DeptHandleMapper {
	
	/**
	 * 新增数据
	 * @param bo 延平民生派发单位处理过程表业务对象
	 * @return 延平民生派发单位处理过程表id
	 */
	public long insert(DeptHandle bo);
	
	/**
	 * 修改数据
	 * @param bo 延平民生派发单位处理过程表业务对象
	 * @return 修改的记录数
	 */
	public long update(DeptHandle bo);
	
	/**
	 * 删除数据
	 * @param bo 延平民生派发单位处理过程表业务对象
	 * @return 删除的记录数
	 */
	public long delete(DeptHandle bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 延平民生派发单位处理过程表数据列表
	 */
	public List<DeptHandle> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 延平民生派发单位处理过程表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 延平民生派发单位处理过程表id
	 * @return 延平民生派发单位处理过程表业务对象
	 */
	public DeptHandle searchById(Long id);

	public List<DeptHandle> searchListByParams(Map<String, Object> params_h);

	public long updateByMore(DeptHandle deptHandle);


}