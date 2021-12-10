package cn.ffcs.zhsq.accountabilityEnforcement.service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;

import java.util.Map;

/**
 * Created by 张天慈 on 2018/3/12.
 */
public interface IAccountabilityEnforcementService extends IAccountEnforceWorkflowService {

	/**
	 * 新增问题
	 * @param param
	 * 			isViolationObjAlter		true表示要调整对象信息，false不调整；默认为true
	 * @return 问题主键
	 * @throws Exception 
	 */
	public Long insertProb(Map<String,Object> param, UserInfo userInfo) throws Exception;

	/**
	 * 根据问题id更新问题
	 * @param param
	 * 			isViolationObjAlter		true表示要调整对象信息，false不调整；默认为true
	 * @param probId
	 * @throws Exception 
	 * */
	public Boolean updateByProbId(Long probId, Map<String,Object> param, UserInfo userInfo) throws Exception;

	/**
	 * 根据问题id删除问题
	 * @param probId	问题id
	 * @param delUserId	删除用户id
	 * @return
	 */
	public Boolean deleteByProbId(Long probId,Long delUserId);
	
	/**
	 * 依据问题id查找问题记录
	 * @param probId
	 * @param params
	 * 			userOrgCode	组织编码
	 * 			isViolationObjNeeded	true则获取对象信息，false则不获取；默认为true
	 * @return
	 */
	public Map<String,Object> findByProbId(Long probId, Map<String, Object> params);

	/**
	 * 分页获取列表记录
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * 			listType	列表类型
	 * 				1 草稿
	 * 				2 待办
	 * 				3 经办
	 * 				4 我发起的
	 * 				5 辖区所有
	 * 			startInfoOrgCode	默认地域编码
	 * 			regionCode			地域编码，为空时使用属性startInfoOrgCode
	 * 			userOrgCode			组织编码，为空时，使用userInfo中的组织编码
	 * 			
	 * @param userInfo	用户信息
	 * @return
	 */
	public EUDGPagination findAccountEnPagination(int pageNo, int pageSize,
			Map<String, Object> params, UserInfo userInfo);
}
