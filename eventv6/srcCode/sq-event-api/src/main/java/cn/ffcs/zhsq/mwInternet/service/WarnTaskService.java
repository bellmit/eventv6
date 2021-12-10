package cn.ffcs.zhsq.mwInternet.service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.mwInternet.WarnTask;

import java.util.Map;

/**
 * @Description: 告警任务表模块服务
 * @Author: guoyd
 * @Date: 04-08 10:18:25
 * @Copyright: 2018 福富软件
 */
public interface WarnTaskService {
	
	/**
	 * 新增数据
	 * @param bo 告警任务表业务对象
	 * @return 告警任务表id
	 */
	public Map<String, Object> insert(WarnTask bo,Map<String, Object> params);
	
	/**
	 * 新增数据
	 * @param bo 告警任务表业务对象
	 * @return 告警任务表id
	 */
	public Long insert(WarnTask bo);

	/**
	 * 修改数据
	 * @param bo 告警任务表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(WarnTask bo);

	/**
	 * 删除数据
	 * @param bo 告警任务表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(WarnTask bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 告警任务表分页数据对象
	 */
	public EUDGPagination searchDBList(int page, int rows, Map<String, Object> params,String orgCode);
	
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 告警任务表分页数据对象
	 */
	public EUDGPagination searchJBList(int page, int rows, Map<String, Object> params,String orgCode);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 告警任务表分页数据对象
	 */
	public EUDGPagination searchAllList(int page, int rows, Map<String, Object> params,String orgCode);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 告警任务表分页数据对象
	 */
	public EUDGPagination searchFQList(int page, int rows, Map<String, Object> params,String orgCode);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 告警任务表分页数据对象
	 */
	public EUDGPagination searchEndList(int page, int rows, Map<String, Object> params,String orgCode);
	/**
	 * 根据业务id查询数据
	 * @param id 告警任务表id
	 * @return 告警任务表业务对象
	 */
	public WarnTask searchById(Long id,String orgCode);
	
	
	//app接口=====================
	/**
	 * 
	 * @param bo
	 * @param userInfo(userInfo.getUserId(), userInfo.getPartyName() userInfo.getOrgId()), userInfo.getOrgName())
	 * @return  status(0失败,1成功),message(错误信息)formId(表单ID)
	 */
	public Object appSave(WarnTask bo, UserInfo userInfo);
	
	/**
	 * 办理初始化
	 * @param id
	 * @param instanceId
	 * @param taskId
	 * @param userInfo
	 * @return
	 */
	public Object appInitToDo(Long id, Long instanceId, String taskId, UserInfo userInfo);
	/**
	 * 
	 * @param buttonType(1提交，2驳回)
	 * @param userInfo
	 * @return
	 */
	public Object appSubmit(String buttonType, UserInfo userInfo,Long dwtId, 
			String instanceId, String taskId,String nextNodeName,String overview,
			String userIds,String curOrgIds,String overTime);
	
	/**
	 * 查看信息
	 * @param id
	 * @param userInfo
	 * @return
	 */
	public Object appDetail(Long id, UserInfo userInfo);
	
	/**
	 * 角色选人
	 * @param userInfo
	 * @return
	 */
	public Object appSelectUser(String roleId, UserInfo userInfo);
	/**
	 * 角色列表
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	public Object selectRole(UserInfo userInfo);
	
}