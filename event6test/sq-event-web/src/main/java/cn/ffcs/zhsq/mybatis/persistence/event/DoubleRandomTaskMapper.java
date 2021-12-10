package cn.ffcs.zhsq.mybatis.persistence.event;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.task.DoubleRandomTask;
import cn.ffcs.zhsq.mybatis.domain.event.task.DoubleRandomTaskStatistics;

/**
 * @Description: 双随机任务模块dao接口
 * @Author: dingyw
 * @Date: 11-07 10:15:16
 * @Copyright: 2017 福富软件
 */
public interface DoubleRandomTaskMapper {
	
	/**
	 * 新增数据
	 * @param bo 双随机任务业务对象
	 * @return 双随机任务id
	 */
	public long insert(DoubleRandomTask bo);
	
	/**
	 * 修改数据
	 * @param bo 双随机任务业务对象
	 * @return 修改的记录数
	 */
	public long update(DoubleRandomTask bo);
	
	/**
	 * 删除数据
	 * @param bo 双随机任务业务对象
	 * @return 删除的记录数
	 */
	public long delete(DoubleRandomTask bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 双随机任务数据列表
	 */
	public List<DoubleRandomTask> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 分页获取当月(结案时间是当月)的已办结的事件
	 * @param params
	 * @param rowBounds
	 * @return
	 */
	public List<EventDisposal> findEventPageList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 随机获取 <=N 条已办结的事件 (排除双随机的事件)
	 * @param startTime 事件完结开始时间
	 * @param endTime 事件完结结束时间
	 * @param size 数量N,(最多返回的记录数)
	 * @return
	 */
	public List<EventDisposal> getRandomEvent(@Param("orgCode")String orgCode,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("size")int size);
	
	/**
	 * 获取当月(结案时间是当月)的已办结的事件数量
	 * @param params
	 * @return
	 */
	public long countEventList(Map<String, Object> params);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 双随机任务数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 双随机任务id
	 * @return 双随机任务业务对象
	 */
	public DoubleRandomTask searchById(Long id);
	
	/**
	 * 按乡镇维度 获取统计的结果
	 * @param query
	 * @return
	 */
	public List<DoubleRandomTaskStatistics> statisticsByCountry(Map<String, Object> params);
	
	/**
	 * 督查事件列表
	 * @param params
	 * @param rowBounds
	 * @return
	 */
	public List<EventDisposal> findDcEventPageList(Map<String, Object> params, RowBounds rowBounds);
	public long countDcEventList(Map<String, Object> params);
	
	
	/**
	 * 获取指定的事件分页列表(评价结果为满意)
	 * @param params
	 * @param rowBounds
	 * @return
	 */
	public List<EventDisposal> getAssignEvent(Map<String, Object> params, RowBounds rowBounds);
	public long countAssignEvent(Map<String, Object> params);
	
	

}