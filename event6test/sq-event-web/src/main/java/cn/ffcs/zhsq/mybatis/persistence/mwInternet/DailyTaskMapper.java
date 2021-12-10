package cn.ffcs.zhsq.mybatis.persistence.mwInternet;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.mwInternet.DailyTask;

import java.util.List;
import java.util.Map;

/**
 * @Description: 日常任务表模块dao接口
 * @Author: guoyd
 * @Date: 04-08 10:20:12
 * @Copyright: 2018 福富软件
 */
public interface DailyTaskMapper {
	
	/**
	 * 新增数据
	 * @param bo 日常任务表业务对象
	 * @return 日常任务表id
	 */
	public long insert(DailyTask bo);
	
	/**
	 * 修改数据
	 * @param bo 日常任务表业务对象
	 * @return 修改的记录数
	 */
	public long update(DailyTask bo);
	
	/**
	 * 删除数据
	 * @param bo 日常任务表业务对象
	 * @return 删除的记录数
	 */
	public long delete(DailyTask bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 日常任务表数据列表
	 */
	public List<DailyTask> searchDBList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 日常任务表数据总数
	 */
	public long countDBList(Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 日常任务表数据列表
	 */
	public List<DailyTask> searchJBList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 日常任务表数据总数
	 */
	public long countJBList(Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 日常任务表数据列表
	 */
	public List<DailyTask> searchAllList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 日常任务表数据总数
	 */
	public long countAllList(Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 日常任务表数据列表
	 */
	public List<DailyTask> searchFQList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 日常任务表数据总数
	 */
	public long countFQList(Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 日常任务表数据列表
	 */
	public List<DailyTask> searchEndList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 日常任务表数据总数
	 */
	public long countEndList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 日常任务表id
	 * @return 日常任务表业务对象
	 */
	public DailyTask searchById(Long id);

}