package cn.ffcs.zhsq.mybatis.persistence.executeControl;

import cn.ffcs.zhsq.mybatis.domain.executeControl.MonitorTask;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * @Description: 布控任务管理模块dao接口
 * @Author: dtj
 * @Date: 07-22 17:50:39
 * @Copyright: 2020 福富软件
 */
public interface MonitorTaskMapper {
	
	/**
	 * 新增数据
	 * @param bo 布控任务管理业务对象
	 * @return 布控任务管理id
	 */
	public long insert(MonitorTask bo);
	
	/**
	 * 修改数据
	 * @param bo 布控任务管理业务对象
	 * @return 修改的记录数
	 */
	public long update(MonitorTask bo);
	
	/**
	 * 删除数据
	 * @param bo 布控任务管理业务对象
	 * @return 删除的记录数
	 */
	public long delete(MonitorTask bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 布控任务管理数据列表
	 */
	public List<MonitorTask> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 布控任务管理数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 布控任务管理id
	 * @return 布控任务管理业务对象
	 */
	public MonitorTask searchById(Long id);

	/**
	 * 设置布控任务状态/是否忽略报警
	 */
	public long updateStatusOrIgnore(MonitorTask bo);

}