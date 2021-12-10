package cn.ffcs.zhsq.mwInternet.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.mwInternet.DailyTask;

import java.util.Map;

/**
 * @Description: 日常任务表模块服务
 * @Author: guoyd
 * @Date: 04-08 10:20:12
 * @Copyright: 2018 福富软件
 */
public interface DailyTaskService {

	/**
	 * 新增数据
	 * @param bo 告警任务表业务对象
	 * @return 告警任务表id
	 */
	public Map<String, Object> insert(DailyTask bo,Map<String, Object> params);
	/**
	 * 新增数据
	 * @param bo 日常任务表业务对象
	 * @return 日常任务表id
	 */
	public Long insert(DailyTask bo);

	/**
	 * 修改数据
	 * @param bo 日常任务表业务对象
	 * @return 是否修改成功
	 */
	public Map<String, Object> update(DailyTask bo,Map<String, Object> params);
	/**
	 * 修改数据
	 * @param bo 日常任务表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(DailyTask bo);

	/**
	 * 删除数据
	 * @param bo 日常任务表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(DailyTask bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 日常任务表分页数据对象
	 */
	public EUDGPagination searchDBList(int page, int rows, Map<String, Object> params,String orgCode);
	
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 日常任务表分页数据对象
	 */
	public EUDGPagination searchJBList(int page, int rows, Map<String, Object> params,String orgCode);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 日常任务表分页数据对象
	 */
	public EUDGPagination searchAllList(int page, int rows, Map<String, Object> params,String orgCode);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 日常任务表分页数据对象
	 */
	public EUDGPagination searchFQList(int page, int rows, Map<String, Object> params,String orgCode);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 日常任务表分页数据对象
	 */
	public EUDGPagination searchEndList(int page, int rows, Map<String, Object> params,String orgCode);
	
	/**
	 * 根据业务id查询数据
	 * @param id 日常任务表id
	 * @return 日常任务表业务对象
	 */
	public DailyTask searchById(Long id,String orgCode);

}