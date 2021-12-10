package cn.ffcs.zhsq.jointDefence.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * 联防长 联防组功能(原V3功能)
 * @author zhenglj 迁移
 *
 */
public interface IJointDefenceService {

	EUDGPagination findPagination(int pageNo, int pageSize, Map<String, Object> params, UserInfo userInfo);

	EUDGPagination findPagination(int pageNo, int pageSize, Map<String, Object> params);

	List<Map<String, Object>> findList(Map<String, Object> params);

	EUDGPagination findEventPagination(int pageNo, int pageSize, Map<String, Object> params);

	Map<String, Object> findGridPathByOrgCode(Map<String, Object> param);

	Map<String, Object> findStatByOrgCode(Map<String, Object> param);

	EUDGPagination findJointTeam(int pageNo, int pageSize,
                                 Map<String, Object> params);

	EUDGPagination findJointPerson(int pageNo, int pageSize,
                                   Map<String, Object> params);

	List<Map<String, Object>> findJointPersonList(Map<String, Object> param);
}
