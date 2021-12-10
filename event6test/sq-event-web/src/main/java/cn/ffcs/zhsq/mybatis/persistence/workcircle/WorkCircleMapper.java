package cn.ffcs.zhsq.mybatis.persistence.workcircle;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.workcircle.WorkCircle;

/**
 * @Description: 工作圈表模块dao接口
 * @Author: zhengyi
 * @Date: 2019-09-09
 * @Copyright: 2019 福富软件
 */
public interface WorkCircleMapper {

	/**
	 * 新增数据
	 * 
	 * @param bo 工作圈表业务对象
	 * @return 工作圈表id
	 */
	long insert(WorkCircle bo);

	/**
	 * 修改数据
	 * 
	 * @param bo 工作圈表业务对象
	 * @return 修改的记录数
	 */
	long update(WorkCircle bo);

	/**
	 * 删除数据
	 * 
	 * @param bo 工作圈表业务对象
	 * @return 删除的记录数
	 */
	long delete(WorkCircle bo);

	/**
	 * 查询数据（分页）
	 * 
	 * @param params    查询参数
	 * @param rowBounds 分页对象
	 * @return 工作圈表数据列表
	 */
	List<WorkCircle> searchList(Map<String, Object> params, RowBounds rowBounds);

	/**
	 * 查询数据（不分页）
	 * 
	 * @param params    查询参数
	 * @param rowBounds 分页对象
	 * @return 工作圈表数据列表
	 */
	List<WorkCircle> searchListNotPage(Map<String, Object> params);

	/**
	 * 查询数据总数
	 * 
	 * @param params 查询参数
	 * @return 工作圈表数据总数
	 */
	long countList(Map<String, Object> params);

	/**
	 * 根据业务id查询数据
	 * 
	 * @param id 工作圈表id
	 * @return 工作圈表业务对象
	 */
	WorkCircle searchById(Long id);

	WorkCircle searchByDept(WorkCircle bo);

	WorkCircle searchByUserMonth(WorkCircle bo);

	WorkCircle searchByUserYear(WorkCircle bo);

	Integer searchTimeout(WorkCircle bo);

	/**
	 * 	查询工作圈
	 * @param params 查询参数
	 * @return 工作圈
	 */
	WorkCircle searchByEventId(long eventId);	
}