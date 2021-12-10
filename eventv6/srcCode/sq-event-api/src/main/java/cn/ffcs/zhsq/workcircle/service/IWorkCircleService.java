package cn.ffcs.zhsq.workcircle.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.workcircle.WorkCircle;

import java.util.Map;

/**
 * @Description: 工作圈表模块服务
 * @Author: zhengyi
 * @Date: 2019-09-09
 * @Copyright: 2019 福富软件
 */
public interface IWorkCircleService {

	/**
	 * 新增数据
	 * @param bo 工作圈表业务对象
	 * @return 工作圈表id
	 */
	Long insert(WorkCircle bo);

	/**
	 * 修改数据
	 * @param bo 工作圈表业务对象
	 * @return 是否修改成功
	 */
	boolean update(WorkCircle bo);

	/**
	 * 删除数据
	 * @param bo 工作圈表业务对象
	 * @return 是否删除成功
	 */
	boolean delete(WorkCircle bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 工作圈表分页数据对象
	 */
	EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 工作圈表id
	 * @return 工作圈表业务对象
	 */
	WorkCircle searchById(Long id);
	
	
	/**
	 * 根据部门CODE和操作日期 查询速报
	 */
	WorkCircle searchByDept(WorkCircle bo);
	
	/**
	 * 根据个人和操作日期 查询速报
	 */
	WorkCircle searchByUserId(WorkCircle bo);
	
	/**
	 * 	新增或保存
	 * @param {
	 *		event {eventId, eventName, happenTime, happenTimeStr, occurred, remark} 
	 *		params {
	 *			opType; // 操作类型 1 上报 2 受理 3 结案 4 上报并结案
	 *			nextUserId; // 下一办理人
	 *			nextUserName; // 下一办理人名称
	 *			nextDeptCode; // 下一办理部门
	 *			nextDeptName; // 下一办理部门名称
	 *			workflowName; // 流程名
	 *		}	
	 * }
	 */
	boolean saveOrUpdate(EventDisposal event, WorkCircle workCircle, UserInfo u, Map<String, Object> params);
}