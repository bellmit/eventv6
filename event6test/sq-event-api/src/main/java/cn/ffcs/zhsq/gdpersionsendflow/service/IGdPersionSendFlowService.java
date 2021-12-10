package cn.ffcs.zhsq.gdpersionsendflow.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.mybatis.domain.courtsynergism.CourtSynergism;
import cn.ffcs.zhsq.mybatis.domain.gdpersionsendflow.GdPersionSendFlow;

import java.util.Map;

/**
 * @Description: 网格员协助送达流程模块服务
 * @Author: zhangch
 * @Date: 10-16 09:18:15
 * @Copyright: 2019 福富软件
 */
public interface IGdPersionSendFlowService {

	/**
	 * 新增数据
	 * @param bo 网格员协助送达流程业务对象
	 * @return 网格员协助送达流程id
	 */
	public Long insert(GdPersionSendFlow bo) throws Exception;

	/**
	 * 修改数据
	 * @param bo 网格员协助送达流程业务对象
	 * @return 是否修改成功
	 */
	public boolean update(GdPersionSendFlow bo);

	/**
	 * 删除数据
	 * @param bo 网格员协助送达流程业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(GdPersionSendFlow bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 网格员协助送达流程分页数据对象
	 */
	public EUDGPagination searchList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 网格员协助送达流程id
	 * @return 网格员协助送达流程业务对象
	 */
	public GdPersionSendFlow searchById(Long id);

	/**
	 * 启动并提交工作流
	 * @param taskId
	 * @param userInfo
	 * @param extraParam
	 * @return  0 启动工作流成功，派发区专班失败 1 成功 -1 查询区专班人员失败 -2 查询上级组织失败 -3 启动工作流失败
	 */
	int startWorkflow(Long taskId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;

	/**
	 * 重新发起工作流
	 * @param bo
	 */
    int resInsert(GdPersionSendFlow bo,UserInfo userInfo);

    /**
	 * 保存办理信息
	 * @param bo
     * @throws Exception 
	 */
	public boolean saveHandleInfo(GdPersionSendFlow bo, Map<String, Object> params) throws Exception;

	/**
	 * 驳回
	 * @param bo
	 * @param userInfo
	 * @return
	 */
	public int reject(GdPersionSendFlow bo, UserInfo userInfo);
}