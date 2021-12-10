package cn.ffcs.zhsq.ypms.mscase.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.DeptHandle;

/**
 * @Description: 延平民生派发单位处理过程表模块服务
 * @Author: zhangzhenhai
 * @Date: 05-03 14:34:50
 * @Copyright: 2018 福富软件
 */
public interface DeptHandleService {

	/**
	 * 新增数据
	 * @param bo 延平民生派发单位处理过程表业务对象
	 * @return 延平民生派发单位处理过程表id
	 */
	public Long insert(DeptHandle bo);

	/**
	 * 修改数据
	 * @param bo 延平民生派发单位处理过程表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(DeptHandle bo);

	/**
	 * 删除数据
	 * @param bo 延平民生派发单位处理过程表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(DeptHandle bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 延平民生派发单位处理过程表分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 延平民生派发单位处理过程表id
	 * @return 延平民生派发单位处理过程表业务对象
	 */
	public DeptHandle searchById(Long id);

	public List<DeptHandle> searchListByParams(Map<String, Object> params_h);

	public boolean updateByMore(DeptHandle deptHandle);

}