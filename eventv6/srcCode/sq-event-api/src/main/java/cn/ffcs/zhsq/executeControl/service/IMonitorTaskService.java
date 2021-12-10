package cn.ffcs.zhsq.executeControl.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.executeControl.MonitorTask;

import java.util.Map;

/**
 * @Description: 布控任务管理模块服务
 * @Author: dtj
 * @Date: 07-22 17:50:39
 * @Copyright: 2020 福富软件
 */
public interface IMonitorTaskService {

	/**
	 * 新增数据
	 * @param bo 布控任务管理业务对象
	 * @return 布控任务管理id
	 */
	public Long insert(MonitorTask bo, String token) throws Exception;

	/**
	 * 修改数据
	 * @param bo 布控任务管理业务对象
	 * @return 是否修改成功
	 */
	public boolean update(MonitorTask bo, String token) throws Exception;

	/**
	 * 删除数据
	 * @param bo 布控任务管理业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(MonitorTask bo, String token) throws Exception;

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 布控任务管理分页数据对象
	 */
	public EUDGPagination searchList(MonitorTask bo, Map<String, Object> params) throws Exception;

	/**
	 * 根据业务id查询数据
	 * @param id 布控任务管理id
	 * @return 布控任务管理业务对象
	 */
	public MonitorTask searchById(Long id, String token) throws Exception;

	long getCount(MonitorTask bo) throws Exception;

	MonitorTask searchByControlTaskId(String id, String token) throws Exception;

	boolean editIgnoreStatus(MonitorTask bo, String token) throws Exception;

	boolean editTaskStatus(MonitorTask bo, String token) throws Exception;

    String getToken() throws Exception;
}