package cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.service.impl;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.reportMsgCCCfg.ReportMsgCCCfgMapper;
import cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.IReportMsgCCCfgService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 消息配送人员配置信息相关操作
 * 				  相关表T_EVENT_MSG_CC_CFG
 * @ClassName:   ReportMsgCCCfgServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年9月26日 下午3:05:37
 */
@Service(value = "reportMsgCCCfgService")
public class ReportMsgCCCfgServiceImpl implements IReportMsgCCCfgService {
	@Autowired
	private ReportMsgCCCfgMapper reportMsgCCCfgMapper;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	private static final String DEFAULT_START_NODE_NAME = "task";
	
	@Override
	public String saveOrUpdateCfgInfo(Map<String, Object> cfgMap, UserInfo userInfo) throws Exception {
		String cfgUUID = null;
		
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		
		Long operateUserId = userInfo.getUserId();
		
		if(CommonFunctions.isNotBlank(cfgMap, "cfgValue")) {
			Long cfgValue = null;
			
			try {
				cfgValue = Long.valueOf(cfgMap.get("cfgValue").toString());
			} catch(NumberFormatException e) {
				throw new ZhsqEventException("配置值【cfgValue】：[" + cfgMap.get("cfgValue") + "]不能转换为有效的数值！");
			}
			
			if(cfgValue != null && cfgValue > 0) {
				cfgMap.put("cfgValue", cfgValue);
			} else {
				throw new ZhsqEventException("配置值【cfgValue】：[" + cfgValue + "]不是有效的数值！");
			}
		}
		
		if(isDuplicatedExternal(cfgMap)) {
			throw new ZhsqEventException("该配置已添加，无需重复添加！");
		}
		
		if(CommonFunctions.isBlank(cfgMap, "creator")) {
			cfgMap.put("creator", operateUserId);
		}
		
		if(CommonFunctions.isBlank(cfgMap, "updator")) {
			cfgMap.put("updator", operateUserId);
		}
		
		if(CommonFunctions.isNotBlank(cfgMap, "cfgUUID")) {
			cfgUUID = cfgMap.get("cfgUUID").toString();
			updateCfgInfo(cfgMap, userInfo);
		} else {
			cfgUUID = saveCfgInfo(cfgMap, userInfo);
		}
		
		return cfgUUID;
	}
	
	/**
	 * 新增人员配送信息
	 * @param cfgMap	人员配送信息
	 * @param userInfo	操作用户信息
	 * @return
	 */
	private String saveCfgInfo(Map<String, Object> cfgMap, UserInfo userInfo) {
		String cfgUUID = null;
		
		if(CommonFunctions.isBlank(cfgMap, "orgChiefLevel") && CommonFunctions.isBlank(cfgMap, "orgCode")) {
			throw new IllegalArgumentException("缺少组织层级【orgChiefLevel】、组织编码【orgCode】！");
		}
		
		if(CommonFunctions.isBlank(cfgMap, "workflowName")) {
			throw new IllegalArgumentException("缺少工作流名称【workflowName】！");
		}
		
		if(CommonFunctions.isBlank(cfgMap, "wfEndNodeName")) {
			throw new IllegalArgumentException("缺少流程节点名称【wfEndNodeName】！");
		}
		
		if(CommonFunctions.isBlank(cfgMap, "ccType")) {
			throw new IllegalArgumentException("缺少人员配送类型【ccType】！");
		}
		
		if(CommonFunctions.isBlank(cfgMap, "cfgValue")) {
			throw new IllegalArgumentException("缺少配置值【cfgValue】！");
		}
		
		if(reportMsgCCCfgMapper.insert(cfgMap) > 0) {
			cfgUUID = cfgMap.get("cfgUUID").toString();
		}
		
		return cfgUUID;
	}
	
	/**
	 * 编辑人员配送信息
	 * @param cfgMap	人员配送信息
	 * @param userInfo	操作用户
	 * @return
	 */
	private String updateCfgInfo(Map<String, Object> cfgMap, UserInfo userInfo) {
		String cfgUUID = null;
		
		if(reportMsgCCCfgMapper.update(cfgMap) > 0) {
			cfgUUID = cfgMap.get("cfgUUID").toString();
		}
		
		return cfgUUID;
	}
	
	@Override
	public boolean delCfgInfoByUUID(String cfgUUID, UserInfo userInfo) {
		if(StringUtils.isBlank(cfgUUID)) {
			throw new IllegalArgumentException("缺少配置信息UUID！");
		}
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("cfgUUID", cfgUUID);
		params.put("updator", userInfo.getUserId());
		
		return reportMsgCCCfgMapper.delete(params) > 0;
	}
	
	@Override
	public Map<String, Object> findCfgInfoByUUID(String cfgUUID, Map<String, Object> params) {
		Map<String, Object> cfgInfoMap = null;
		
		if(StringUtils.isBlank(cfgUUID)) {
			throw new IllegalArgumentException("缺少配置信息UUID！");
		}
		
		params = params == null ? new HashMap<String, Object>() : params;
		params.put("cfgUUID", cfgUUID);
		
		cfgInfoMap = reportMsgCCCfgMapper.findCfgByUUID(params);
		
		if(cfgInfoMap != null && !cfgInfoMap.isEmpty()) {
			List<Map<String, Object>> msgCCCfgList = new ArrayList<Map<String, Object>>();
			
			msgCCCfgList.add(cfgInfoMap);
			
			formatDataOut(msgCCCfgList, params);
		}
		
		return cfgInfoMap;
	}
	
	@Override
	public EUDGPagination findCfg4Pagination(int pageNo, int pageSize, Map<String, Object> params) {
		int total = 0;
		List<Map<String, Object>> reportMsgCfgList = null;
		
		formatParamIn(params);
		
		total = reportMsgCCCfgMapper.findCfg4Count(params);
		
		if(total > 0) {
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 10 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			
			reportMsgCfgList = reportMsgCCCfgMapper.findCfg4Pagination(params, rowBounds);
			
			formatDataOut(reportMsgCfgList, params);
		} else {
			reportMsgCfgList = new ArrayList<Map<String, Object>>();
		}
		
		EUDGPagination reportPagination = new EUDGPagination(total, reportMsgCfgList);
		
		return reportPagination;
		
	}
	
	@Override
	public List<Map<String, Object>> findCfg4List(Map<String, Object> params) {
		List<Map<String, Object>> cfgMapList = null;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(params != null && !params.isEmpty()) {
			String CFG_STATUS_VALID = "1";
			
			formatParamIn(params);
			
			if(CommonFunctions.isBlank(params, "wfStartNodeName")) {
				params.put("wfStartNodeName", DEFAULT_START_NODE_NAME);
			}
			
			if(CommonFunctions.isBlank(params, "cfgStatus")) {
				params.put("cfgStatus", CFG_STATUS_VALID);
			}
			
			if(CommonFunctions.isNotBlank(params, "wfStartNodeName") && CommonFunctions.isNotBlank(params, "wfEndNodeName")) {
				cfgMapList = reportMsgCCCfgMapper.findCfg4List(params);
			}
		}
		
		return cfgMapList;
	}
	
	@Override
	public Map<String, List<UserBO>> findCfg4User(Map<String, Object> params, String benchmarkOrgCode) {
		OrgSocialInfoBO benchmarkOrgInfo = null;
		
		if(StringUtils.isNotBlank(benchmarkOrgCode)) {
			benchmarkOrgInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode(benchmarkOrgCode);
			
			if(benchmarkOrgInfo == null || benchmarkOrgInfo.getOrgId() == null) {
				throw new IllegalArgumentException("组织编码【" + benchmarkOrgCode + "】没有找到有效的组织信息！");
			}
		}
		
		return findCfg4User(params, benchmarkOrgInfo);
	}
	
	@Override
	public Map<String, List<UserBO>> findCfg4User(Map<String, Object> params, OrgSocialInfoBO benchmarkOrgInfo) {
		List<Map<String, Object>> cfgMapList = findCfg4List(params);
		
		return findCfg4User(params, benchmarkOrgInfo, cfgMapList);
	}
	
	@Override
	public Map<String, List<UserBO>> findCfg4User(Map<String, Object> params, OrgSocialInfoBO benchmarkOrgInfo, List<Map<String, Object>> cfgMapList) {
		Map<String, List<UserBO>> cfgUserMap = null;
		
		if(cfgMapList != null) {
			String ccType = null;
			Map<String, List<Map<String, Object>>> ccTypeMap = new HashMap<String, List<Map<String, Object>>>();
			List<Map<String, Object>> cfgMapTmpList = null;
			
			for(Map<String, Object> cfgMap : cfgMapList) {
				if(CommonFunctions.isNotBlank(cfgMap, "ccType")) {
					ccType = cfgMap.get("ccType").toString();
					
					if(ccTypeMap.containsKey(ccType)) {
						cfgMapTmpList = ccTypeMap.get(ccType);
					} else {
						cfgMapTmpList = new ArrayList<Map<String, Object>>();
					}
					
					cfgMapTmpList.add(cfgMap);
					ccTypeMap.put(ccType, cfgMapTmpList);
				}
			}
			
			if(!ccTypeMap.isEmpty()) {
				cfgUserMap = new HashMap<String, List<UserBO>>();
				List<UserBO> ccTypeUserInfoList = null;
				
				for(String ccTypeKey : ccTypeMap.keySet()) {
					ccTypeUserInfoList = findUserInfoByCfg(ccTypeMap.get(ccTypeKey), benchmarkOrgInfo);
					
					if(ccTypeUserInfoList != null && ccTypeUserInfoList.size() > 0) {
						cfgUserMap.put(ccTypeKey, ccTypeUserInfoList);
					}
				}
			}
		}
		
		return cfgUserMap;
	}
	
	/**
	 * 判断人员配置信息是否重复添加
	 * @param cfgMap
	 * 判断条件：
	 *  1、orgChiefLevel/orgCode + workflowName + wfStartNodeName + wfEndNodeName + ccType + cfgType + cfgValue
	 *  2、orgChiefLevel/orgCode + workflowName + wfEndNodeName + ccType + cfgType + cfgValue
	 * @return
	 */
	private boolean isDuplicatedExternal(Map<String, Object> cfgMap) {
		boolean isDuplicated = true;
		
		if(cfgMap != null && !cfgMap.isEmpty()) {
			Map<String, Object> duplicateParams = new HashMap<String, Object>();
			
			duplicateParams.putAll(cfgMap);
			
			if(CommonFunctions.isBlank(duplicateParams, "wfStartNodeName")) {
				duplicateParams.put("wfStartNodeName", DEFAULT_START_NODE_NAME);
			}
			
			if(CommonFunctions.isNotBlank(duplicateParams, "wfStartNodeName") && CommonFunctions.isNotBlank(duplicateParams, "wfEndNodeName")) {
				isDuplicated = isDuplicated(duplicateParams);
			}
		}
		
		return isDuplicated;
	}
	
	/**
	 * 判断人员配置信息是否重复添加
	 * 判断条件：
	 *  orgChiefLevel/orgCode + workflowName + wfEndNodeName + ccType + cfgType + cfgValue
	 * @param cfgMap
	 * @return
	 */
	private boolean isDuplicated(Map<String, Object> cfgMap) {
		String cfgUUID = null;
		boolean isDuplicated = true;
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> cfgMapInfo = new HashMap<String, Object>();
		
		if(CommonFunctions.isNotBlank(cfgMap, "cfgUUID")) {
			cfgUUID = cfgMap.get("cfgUUID").toString();
			cfgMapInfo = this.findCfgInfoByUUID(cfgUUID, null);
		}
		
		if(cfgMapInfo != null) {
			cfgMapInfo.putAll(cfgMap);
		}
		
		if(CommonFunctions.isNotBlank(cfgMap, "orgChiefLevel")) {
			params.put("orgChiefLevel", cfgMap.get("orgChiefLevel"));
		} else if(CommonFunctions.isNotBlank(cfgMap, "orgCode")) {
			params.put("orgCode", cfgMap.get("orgCode"));
		} else if(CommonFunctions.isNotBlank(cfgMapInfo, "orgChiefLevel")) {
			params.put("orgChiefLevel", cfgMapInfo.get("orgChiefLevel"));
		} else if(CommonFunctions.isNotBlank(cfgMapInfo, "orgCode")) {
			params.put("orgCode", cfgMapInfo.get("orgCode"));
		}
		
		if(CommonFunctions.isNotBlank(cfgMapInfo, "professionCode")) {
			params.put("professionCode", cfgMapInfo.get("professionCode"));
		}
		
		if(CommonFunctions.isNotBlank(cfgMapInfo, "workflowName")) {
			params.put("workflowName", cfgMapInfo.get("workflowName"));
		}
		
		if(CommonFunctions.isNotBlank(cfgMapInfo, "wfStartNodeName")) {
			params.put("wfStartNodeName", cfgMapInfo.get("wfStartNodeName"));
		}
		
		if(CommonFunctions.isNotBlank(cfgMapInfo, "wfEndNodeName")) {
			params.put("wfEndNodeName", cfgMapInfo.get("wfEndNodeName"));
		}
		
		if(CommonFunctions.isNotBlank(cfgMapInfo, "ccType")) {
			params.put("ccType", cfgMapInfo.get("ccType"));
		}
		
		if(CommonFunctions.isNotBlank(cfgMapInfo, "cfgType")) {
			params.put("cfgType", cfgMapInfo.get("cfgType"));
		}
		
		if(CommonFunctions.isNotBlank(cfgMapInfo, "cfgValue")) {
			params.put("cfgValue", cfgMapInfo.get("cfgValue"));
		}
		
		if(!params.isEmpty()) {
			List<Map<String, Object>> cfgMapList = this.findCfg4List(params);
			
			if(cfgMapList == null || cfgMapList.size() == 0) {
				isDuplicated = false;
			} else if(cfgMapList.size() > 1) {
				isDuplicated = true;
			} else if(cfgMapList.size() == 1) {
				Map<String, Object> _cfgMapInfo = cfgMapList.get(0);
				String _cfgUUID = null;
				
				if(CommonFunctions.isNotBlank(_cfgMapInfo, "cfgUUID")) {
					_cfgUUID = _cfgMapInfo.get("cfgUUID").toString();
					
					isDuplicated = !_cfgUUID.equals(cfgUUID);
				}
			}
		}
		
		return isDuplicated;
	}
	
	/**
	 * 将配置信息转换为用户信息
	 * @param cfgMapList
	 * @return
	 */
	private List<UserBO> findUserInfoByCfg(List<Map<String, Object>> cfgMapList, OrgSocialInfoBO benchmarkOrgInfo) {
		List<UserBO> userBOList = null;
		String benchmarkOrgChiefLevel = null;
		Long benchmarkOrgId = null;
		List<OrgSocialInfoBO> socialInfoBOs = null;

		if(benchmarkOrgInfo != null) {
			benchmarkOrgChiefLevel = benchmarkOrgInfo.getChiefLevel();
			benchmarkOrgId = benchmarkOrgInfo.getOrgId();
		}
		
		if(cfgMapList != null) {
			OrgSocialInfoBO orgInfo = null;
			String cfgType = null,
					//目标组织专业
					professionCode = null;
			Long cfgValue = null, benchmarkOrgIdCfg = null;
			UserBO userBO = null;
			userBOList = new ArrayList<UserBO>();
			List<OrgSocialInfoBO> orgInfoList = null;
			OrgSocialInfoBO benchmarkOrgInfoCfg = null;
			
			for(Map<String, Object> cfgMap : cfgMapList) {
				orgInfoList = new ArrayList<OrgSocialInfoBO>();
				benchmarkOrgInfoCfg = benchmarkOrgInfo;
				benchmarkOrgIdCfg = benchmarkOrgId;
				cfgType = null;
				professionCode = null;
				cfgValue = null;
				
				if(CommonFunctions.isNotBlank(cfgMap, "orgCode")) {
					String orgCode = cfgMap.get("orgCode").toString();
					orgInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode(orgCode);
					
					orgInfoList.add(orgInfo);
				} else if(CommonFunctions.isNotBlank(cfgMap, "orgChiefLevel") && StringUtils.isNotBlank(benchmarkOrgChiefLevel)) {
					//目标层级
					String orgChiefLevel = cfgMap.get("orgChiefLevel").toString();

					if(CommonFunctions.isNotBlank(cfgMap, "benchmarkOrgCode")) {
						benchmarkOrgInfoCfg = orgSocialInfoService.selectOrgSocialInfoByOrgCode(cfgMap.get("benchmarkOrgCode").toString());
						
						if(benchmarkOrgInfoCfg != null && benchmarkOrgInfoCfg.getOrgId() != null) {
							benchmarkOrgIdCfg = benchmarkOrgInfoCfg.getOrgId();
						}
					}
					
					if(CommonFunctions.isNotBlank(cfgMap, "professionCode")) {
						professionCode = cfgMap.get("professionCode").toString();
					}

					if(orgChiefLevel.equals(benchmarkOrgChiefLevel)) {
						//这里没有开放同层级不同专业之间的查找，因为完全可以通过组织编码配置进行替换

						//放开同层级专业部门的查找
						//1.professionCode目标组织的专业为部门 来源组织专业为单位，获取来源组织下所有该专业的职能部门
						if(StringUtils.isNotBlank(professionCode) && !professionCode.equals(ConstantValue.GOV_PROFESSION_CODE)){
							socialInfoBOs = orgSocialInfoService.findUnitBySame(benchmarkOrgIdCfg, false, ConstantValue.ORG_TYPE_DEPT);

							if(null != socialInfoBOs && socialInfoBOs.size() > 0){
								for(OrgSocialInfoBO socialOrg : socialInfoBOs){
									if(professionCode.equals(socialOrg.getProfessionCode())){
										orgInfoList.add(socialOrg);
									}
								}
							}
						}else {
							orgInfoList.add(benchmarkOrgInfoCfg);
						}
					} else if(benchmarkOrgChiefLevel.compareTo(orgChiefLevel) > 0) {//向上查找
						List<OrgSocialInfoBO> orgInfoUnitList = null;
						
						professionCode = StringUtils.isBlank(professionCode) ? ConstantValue.GOV_PROFESSION_CODE : professionCode;
						
						orgInfoUnitList = orgSocialInfoService.findSuperiorByCode(benchmarkOrgIdCfg, Integer.valueOf(benchmarkOrgChiefLevel) - Integer.valueOf(orgChiefLevel), ConstantValue.GOV_PROFESSION_CODE);
						
						if(ConstantValue.GOV_PROFESSION_CODE.equals(professionCode)) {
							orgInfoList.addAll(orgInfoUnitList);
						} else {
							orgInfoList.addAll(findDeptOrgInfoList(orgInfoUnitList, professionCode));
						}
					} else if(benchmarkOrgChiefLevel.compareTo(orgChiefLevel) < 0) {//向下查找
						//目前只开向下查找职能部门的情况，向下查找单位的，尽量通过向上查找适配
						if(!ConstantValue.GOV_PROFESSION_CODE.equals(professionCode)) {
							//以下写法只支持旧版组织树结构
							int levelInterval = Integer.valueOf(orgChiefLevel) - Integer.valueOf(benchmarkOrgChiefLevel);
							Long parentOrgId = benchmarkOrgIdCfg;
							List<OrgSocialInfoBO> parentOrgInfoList = null;
							
							for(int index = 0; index < levelInterval; index++) {
								parentOrgInfoList = orgSocialInfoService.findLevelByProfession(parentOrgId, professionCode, false);
								
								//需要通过专业设置，将可查找的下一级组织设置为只会查找到至多一个
								if(parentOrgInfoList != null && parentOrgInfoList.size() > 0) {
									parentOrgId = parentOrgInfoList.get(0).getOrgId();
								}
							}
							
							if(parentOrgInfoList != null) {
								orgInfoList.addAll(parentOrgInfoList);
							}
						}
					}
				}
				
				if(CommonFunctions.isNotBlank(cfgMap, "cfgType")) {
					cfgType = cfgMap.get("cfgType").toString();
				}
				
				if(CommonFunctions.isNotBlank(cfgMap, "cfgValue")) {
					cfgValue = Long.valueOf(cfgMap.get("cfgValue").toString());
				}
				
				if(cfgValue != null && cfgValue > 0 && orgInfoList != null) {
					Long orgId = null;
					for(OrgSocialInfoBO orgInfoTmp : orgInfoList) {
						if(orgInfoTmp != null) {
							orgId = orgInfoTmp.getOrgId();
						}
						
						if(orgId != null) {
							if(ReportMsgCfgTypeEnum.CFG_TYPE_USER.getCfgType().equals(cfgType)) {
								userBO = userManageService.getUserInfoByUserId(cfgValue);
								
								if(userBO != null && userBO.getUserId() != null) {
									userBO.setSocialOrgId(String.valueOf(orgId));
									userBO.setOrgCode(orgInfoTmp.getOrgCode());
									userBO.setOrgName(orgInfoTmp.getOrgName());
									
									userBOList.add(userBO);
								}
							} else if(ReportMsgCfgTypeEnum.CFG_TYPE_ORG.getCfgType().equals(cfgType)) {
								userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(null, null, cfgValue));
							} else if(ReportMsgCfgTypeEnum.CFG_TYPE_ROLE.getCfgType().equals(cfgType)) {
								userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(cfgValue, null, orgId));
							} else if(ReportMsgCfgTypeEnum.CFG_TYPE_POSITION.getCfgType().equals(cfgType)) {
								userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(null, cfgValue, orgId));
							}
						}
					}
				}
			}
			
			//用户去重
			if(userBOList.size() > 0) {
				Long userId = null;
				String orgIdStr = null;
				List<UserBO> userBORemainedList = new ArrayList<UserBO>();
				StringBuffer userOrgKey = null;
				Set<String> userOrgSet = new HashSet<String>();
				
				for(UserBO userBOTmp : userBOList) {
					userId = userBOTmp.getUserId();
					orgIdStr = userBOTmp.getSocialOrgId();
					
					if(userId != null && userId > 0 && StringUtils.isNotBlank(orgIdStr)) {
						userOrgKey = new StringBuffer("");
						userOrgKey.append(userId).append("-").append(orgIdStr);
						
						if(!userOrgSet.contains(userOrgKey.toString())) {
							userOrgSet.add(userOrgKey.toString());
						
							userBORemainedList.add(userBOTmp);
						}
					}
				}
				
				userBOList = userBORemainedList;
			}
		}
		
		return userBOList;
	}
	
	/**
	 * 依据单位组织获取同层级的职能部门信息
	 * @param unitOrgInfoList	单位组织列表
	 * @param professionCode	专业编码
	 * @return
	 */
	private List<OrgSocialInfoBO> findDeptOrgInfoList(List<OrgSocialInfoBO> unitOrgInfoList, String professionCode) {
		List<OrgSocialInfoBO> orgDeptList = new ArrayList<OrgSocialInfoBO>();
		Integer ORG_TYPE_DEPARTMENT = 0;//职能部门组织类型
		
		if(unitOrgInfoList != null) {
			Long unitOrgId = null;
			
			for(OrgSocialInfoBO unitOrgInfo : unitOrgInfoList) {
				unitOrgId = unitOrgInfo.getOrgId();
				
				orgDeptList.addAll(orgSocialInfoService.findUnitBySame(unitOrgId, false, ORG_TYPE_DEPARTMENT));
			}
		}
		
		if(orgDeptList.size() > 0 && StringUtils.isNotBlank(professionCode)) {
			List<OrgSocialInfoBO> deptRemainOrgList = new ArrayList<OrgSocialInfoBO>();
			
			for(OrgSocialInfoBO deptOrgInfo : orgDeptList) {
				if(professionCode.equals(deptOrgInfo.getProfessionCode())) {
					deptRemainOrgList.add(deptOrgInfo);
				}
			}
			
			orgDeptList = deptRemainOrgList;
		}
		
		return orgDeptList;
	}
	
	/**
	 * 格式化输入参数
	 * @param params
	 */
	@SuppressWarnings("unchecked")
	private void formatParamIn(Map<String, Object> params) {
		if(params != null && !params.isEmpty()) {
			if(CommonFunctions.isNotBlank(params, "ccType")) {
				String ccType = params.get("ccType").toString();
				
				if(ccType.contains(",")) {
					params.put("ccTypeArray", ccType.split(","));
					params.remove("ccType");
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "wfNodeNameList")) {
				Object wfNodeNameListObj = params.get("wfNodeNameList");
				
				if(wfNodeNameListObj instanceof List) {
					List<Map<String, Object>>  wfNodeNameList = (List<Map<String, Object>>) wfNodeNameListObj,
											   removeNodeNameList = new ArrayList<Map<String, Object>>();
					for(Map<String, Object> wfNodeName : wfNodeNameList) {
						if(CommonFunctions.isNotBlank(wfNodeName, "wfEndNodeName")) {
							String wfEndNodeName = wfNodeName.get("wfEndNodeName").toString();
							
							if(wfEndNodeName.contains("->")) {
								String[] wfNodeNameArray = wfEndNodeName.split("->");
								if(wfNodeNameArray.length == 2) {
									if(CommonFunctions.isBlank(wfNodeName, "wfStartNodeName")) {
										wfNodeName.put("wfStartNodeName", wfNodeNameArray[0]);
									}
									
									wfNodeName.put("wfEndNodeName", wfNodeNameArray[1]);
								}
							}
						}
						
						if(CommonFunctions.isBlank(wfNodeName, "wfStartNodeName") || CommonFunctions.isBlank(wfNodeName, "wfEndNodeName")) {
							removeNodeNameList.add(wfNodeName);
						}
					}
					
					wfNodeNameList.removeAll(removeNodeNameList);
					
					if(wfNodeNameList.size() == 0) {
						throw new IllegalArgumentException("属性【wfNodeNameList】需要同时传入【wfStartNodeName】和【wfEndNodeName】！");
					}
					
				} else {
					throw new IllegalArgumentException("属性【wfNodeNameList】需要转换为如下类型：List<Map<String, Object>>！");
				}
			} else if(CommonFunctions.isNotBlank(params, "wfEndNodeName")) {
				String wfEndNodeName = params.get("wfEndNodeName").toString();
				
				if(wfEndNodeName.contains("->")) {
					String[] wfNodeNameArray = wfEndNodeName.split("->");
					if(wfNodeNameArray.length == 2) {
						if(CommonFunctions.isBlank(params, "wfStartNodeName")) {
							params.put("wfStartNodeName", wfNodeNameArray[0]);
						}
						
						params.put("wfEndNodeName", wfNodeNameArray[1]);
					}
				}
			}
		}
	}
	
	/**
	 * 格式化输出参数
	 * @param msgCCCfgList
	 * @param params
	 */
	private void formatDataOut(List<Map<String, Object>> msgCCCfgList, Map<String, Object> params) {
		boolean isDictTransfer = true;
		List<BaseDataDict> orgLevelDictList = null, msgCfgTypeDictList = null, msgCCTypeDictList = null;
		
		if(CommonFunctions.isNotBlank(params, "isDictTransfer")) {
			isDictTransfer = Boolean.valueOf(params.get("isDictTransfer").toString());
		}
		
		if(isDictTransfer) {
			String userOrgCode = null, ORG_LEVEL_DICT_PCODE = "E008";
			BaseDataDict baseDataDict = null;
			
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			}
			
			orgLevelDictList = baseDictionaryService.getDataDictListOfSinglestage(ORG_LEVEL_DICT_PCODE, userOrgCode);
			msgCfgTypeDictList = new ArrayList<BaseDataDict>();
			msgCCTypeDictList = new ArrayList<BaseDataDict>();
			
			for(ReportMsgCfgTypeEnum cfgTypeEnum : ReportMsgCfgTypeEnum.values()) {
				baseDataDict = new BaseDataDict();
				baseDataDict.setDictGeneralCode(cfgTypeEnum.getCfgType());
				baseDataDict.setDictName(cfgTypeEnum.getCfgTypeName());
				
				msgCfgTypeDictList.add(baseDataDict);
			}
			
			for(ReportMsgCCTypeEnum ccTypeEnum : ReportMsgCCTypeEnum.values()) {
				baseDataDict = new BaseDataDict();
				baseDataDict.setDictGeneralCode(ccTypeEnum.getCcType());
				baseDataDict.setDictName(ccTypeEnum.getCcTypeName());
				
				msgCCTypeDictList.add(baseDataDict);
			}
		}
		
		for(Map<String, Object> msgCCCfgMap : msgCCCfgList) {
			DataDictHelper.setDictValueForField(msgCCCfgMap, "orgChiefLevel", "orgChiefLevelName", orgLevelDictList);
			DataDictHelper.setDictValueForField(msgCCCfgMap, "cfgType", "cfgTypeName", msgCfgTypeDictList);
			DataDictHelper.setDictValueForField(msgCCCfgMap, "ccType", "ccTypeName", msgCCTypeDictList);
		}
	}
}
