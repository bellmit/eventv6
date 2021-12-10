package cn.ffcs.zhsq.mybatis.persistence.mwInternet;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.mwInternet.WarnTask;

import java.util.List;
import java.util.Map;

/**
 * @Description: 告警任务表模块dao接口
 * @Author: guoyd
 * @Date: 04-08 10:18:25
 * @Copyright: 2018 福富软件
 */
public interface WarnTaskMapper {
	
	/**
	 * 新增数据
	 * @param bo 告警任务表业务对象
	 * @return 告警任务表id
	 */
	public long insert(WarnTask bo);
	
	/**
	 * 修改数据
	 * @param bo 告警任务表业务对象
	 * @return 修改的记录数
	 */
	public long update(WarnTask bo);
	
	/**
	 * 删除数据
	 * @param bo 告警任务表业务对象
	 * @return 删除的记录数
	 */
	public long delete(WarnTask bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 告警任务表数据列表
	 */
	public List<WarnTask> searchDBList(Map<String, Object> params, RowBounds rowBounds);
	
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
	public List<WarnTask> searchJBList(Map<String, Object> params, RowBounds rowBounds);
	
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
	public List<WarnTask> searchAllList(Map<String, Object> params, RowBounds rowBounds);
	
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
	public List<WarnTask> searchFQList(Map<String, Object> params, RowBounds rowBounds);
	
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
	public List<WarnTask> searchEndList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 告警任务表数据总数
	 */
	public long countEndList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 告警任务表id
	 * @return 告警任务表业务对象
	 */
	public WarnTask searchById(Long id);

}