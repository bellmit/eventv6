package cn.ffcs.zhsq.event.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.task.DoubleRandomTask;
import cn.ffcs.zhsq.mybatis.domain.event.task.DoubleRandomTaskStatistics;

/**
 * @Description: 双随机任务模块服务
 * @Author: zkongbai
 * @Date: 11-07 10:15:16
 * @Copyright: 2017 福富软件
 */
public interface DoubleRandomTaskService {

	/**
	 * 新增数据
	 * @param bo 双随机任务业务对象
	 * @return 双随机任务id
	 */
	public Long insert(DoubleRandomTask bo);

	/**
	 * 修改数据
	 * @param bo 双随机任务业务对象
	 * @return 是否修改成功
	 */
	public boolean update(DoubleRandomTask bo);

	/**
	 * 删除数据
	 * @param bo 双随机任务业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(DoubleRandomTask bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 双随机任务分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 分页获取当月(结案时间是当月)的已办结的事件
	 * @param params
	 * @return
	 */
	public EUDGPagination findEventPageList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 双随机任务id
	 * @return 双随机任务业务对象
	 */
	public DoubleRandomTask searchById(Long id);
	
	/**
	 * 获取统计结果
	 * @param query
	 * @return
	 */
	public List<DoubleRandomTaskStatistics> statisticsByCountry(Map<String, Object> params);
	
	/**
	 * 随机获取 <=N 条已办结的事件 (排除双随机的事件)
	 * @param startTime 事件完结开始时间
	 * @param endTime 事件完结结束时间
	 * @param size 数量N,(最多返回的记录数)
	 * @return
	 */
	public List<EventDisposal> getRandomEvent(String orgCode,String startTime,String endTime,int size);
	
	/**
	 * 督查事件列表
	 * @param params
	 * @param rowBounds
	 * @return
	 */
	public EUDGPagination findDcEventPageList(int page, int rows, Map<String, Object> params);
	/**
	 * 获取指定的事件分页列表(评价结果为满意)
	 * @param params
	 * @param rowBounds
	 * @return
	 */
	public EUDGPagination findAssignEvent(int page, int rows, Map<String, Object> params);
	
	public long getCount(Map<String, Object> params);

}