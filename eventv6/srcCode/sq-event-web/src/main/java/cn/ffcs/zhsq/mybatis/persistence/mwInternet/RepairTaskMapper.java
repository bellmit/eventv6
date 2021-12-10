package cn.ffcs.zhsq.mybatis.persistence.mwInternet;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.mwInternet.RepairTask;

import java.util.List;
import java.util.Map;

/**
 * @Description: 报修任务表模块dao接口
 * @Author: guoyd
 * @Date: 04-08 10:19:19
 * @Copyright: 2018 福富软件
 */
public interface RepairTaskMapper {
	
	/**
	 * 新增数据
	 * @param bo 报修任务表业务对象
	 * @return 报修任务表id
	 */
	public long insert(RepairTask bo);
	
	/**
	 * 修改数据
	 * @param bo 报修任务表业务对象
	 * @return 修改的记录数
	 */
	public long update(RepairTask bo);
	
	/**
	 * 删除数据
	 * @param bo 报修任务表业务对象
	 * @return 删除的记录数
	 */
	public long delete(RepairTask bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 告警任务表数据列表
	 */
	public List<RepairTask> searchDBList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 告警任务表数据总数
	 */
	public long countDBList(Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 告警任务表数据列表
	 */
	public List<RepairTask> searchJBList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 告警任务表数据总数
	 */
	public long countJBList(Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 告警任务表数据列表
	 */
	public List<RepairTask> searchAllList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 告警任务表数据总数
	 */
	public long countAllList(Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 告警任务表数据列表
	 */
	public List<RepairTask> searchFQList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 告警任务表数据总数
	 */
	public long countFQList(Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 告警任务表数据列表
	 */
	public List<RepairTask> searchEndList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 告警任务表数据总数
	 */
	public long countEndList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 报修任务表id
	 * @return 报修任务表业务对象
	 */
	public RepairTask searchById(Long id);

}