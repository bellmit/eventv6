package cn.ffcs.zhsq.accountabilityEnforcement.service.impl;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.INumberConfigureService;
import cn.ffcs.zhsq.accountabilityEnforcement.service.IAccountabilityEnforcementService;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.mybatis.persistence.accountabilityEnforcement.AccountabilityEnforcementMapper;
import cn.ffcs.zhsq.typeRela.service.ITypeRelaService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 张天慈 on 2018/3/12.
 */
@Service("accountabilityEnforcementService")
public class AccountabilityEnforcementServiceImpl extends AccountEnforceWorkflowServiceImpl implements IAccountabilityEnforcementService {
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private IAttachmentService attachmentService;
	
	@Autowired
	private IInvolvedPeopleService involvedPeopleService;

	@Autowired
	private AccountabilityEnforcementMapper accountabilityEnforcementMapper;
	
	@Autowired
	private ITypeRelaService typeRelaService;
	
	@Autowired
	private INumberConfigureService numberConfigureService;

	private String ATTACHMENT_TYPE = "accountabilityEnforcement",	//附件类型
				   INVOLVEDPEOPLE_BIZ_TYPE = "08",					//涉及人员业务类型
				   END_NODE_CODE = "end1",							//归档节点编码
				   NUMBER_CFG_BIZ_CODE = "10",						//编号配置业务编码
				   VIOLATION_OBJ_TYPE_UNIT = "1",					//对象类别——单位
				   VIOLATION_OBJ_TYPE_PERSON = "2",					//对象类别——个人
			       TYPERELA_BIZ_TYPE = "00",					    //关联表（T_TYPE_RELA）业务类型：00执纪问责问题
					SOURCE_PCODE = "B590000",//问题线索来源字典编码
					VIOLATIONMONEYTYPE_PCODE = "B590002",//违规违纪资金类别字典编码
					VIOLATIONTYPE_PCODE = "B590003";//违规违纪违法类别

	@Override
	public Long insertProb(Map<String, Object> param, UserInfo userInfo) throws Exception {
		Long probId = -1L;
		
		if(param != null && !param.isEmpty()) {
			int num = 0;
			
			formatMapDataIn(param);
			
			if(CommonFunctions.isBlank(param, "creator")) {
				param.put("creator", userInfo.getUserId());
			}
			
			if(CommonFunctions.isBlank(param, "probNo")) {//获取问题编号
				try {//防止问题编码获取失败，导致新增失败
					param.put("probNo", numberConfigureService.getNumber("", NUMBER_CFG_BIZ_CODE));
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			num = accountabilityEnforcementMapper.insert(param);
			
			if(num > 0) {
				probId = Long.valueOf(param.get("probId").toString());
				
				saveExtraInfo(probId, param, userInfo);
		
				if(CommonFunctions.isNotBlank(param, "isStart")) {
					boolean isStart = Boolean.valueOf(param.get("isStart").toString());
					
					if(isStart) {
						this.startWorkflow4AccountEn(probId, userInfo, param);
					}
				}
			}
		}
		
		return probId;
	}

	@Override
	public Boolean updateByProbId(Long probId, Map<String, Object> param, UserInfo userInfo) throws Exception {
		boolean flag = false;
		
		if(probId != null && probId > 0) {
			formatMapDataIn(param);
			
			if(CommonFunctions.isBlank(param, "updater")) {
				param.put("updater", userInfo.getUserId());
			}
			if(CommonFunctions.isBlank(param, "probId")) {
				param.put("probId", probId);
			}
			
			flag = accountabilityEnforcementMapper.update(param) > 0;
			
			if(flag) {
				saveExtraInfo(probId, param, userInfo);
				
				if(CommonFunctions.isNotBlank(param, "isStart")) {
					boolean isStart = Boolean.valueOf(param.get("isStart").toString());
					
					if(isStart) {
						this.startWorkflow4AccountEn(probId, userInfo, param);
					}
				}
			}
		}
		
		return flag;
	}

	@Override
	public Boolean deleteByProbId(Long probId, Long delUserId) {
		
		int num = accountabilityEnforcementMapper.deleteByProbId(probId,delUserId);
		if (num > 0) {
			try {
				Map<String,Object> param = new HashMap<>();
				param.put("bizId",probId);
				param.put("bizType",TYPERELA_BIZ_TYPE);
				typeRelaService.delTypeRela(param);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (num>0?true:false);
	}

	@Override
	public Map<String, Object> findByProbId(Long probId, Map<String, Object> params) {
		Map<String, Object> resultMap = null;
		
		if(probId != null && probId > 0) {
			resultMap = accountabilityEnforcementMapper.findById(probId);

			if(resultMap != null && !resultMap.isEmpty()) {
				boolean isViolationObjNeeded = true;
				List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
				
				if(CommonFunctions.isNotBlank(params, "isViolationObjNeeded")) {
					isViolationObjNeeded = Boolean.valueOf(params.get("isViolationObjNeeded").toString());
				}
				
				if(isViolationObjNeeded) {
					List<InvolvedPeople> list = involvedPeopleService.findInvolvedPeopleListByBiz(probId,INVOLVEDPEOPLE_BIZ_TYPE);
					if(list != null && list.size() > 0){
						InvolvedPeople involvedPeople = list.get(0);
						String orgCode = null,
							   OBJ_SEX_PCODE = "B590005",
							   OBJ_POLITICS_PCODE = "B590006",
							   OBJ_PROFESSION_TYPE_PERSON_PCODE = "B590007",
							   OBJ_PROFESSION_TYPE_UNIT_PCODE = "B590008",
							   violationObjType = null;
						
						if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
							orgCode = params.get("userOrgCode").toString();
						}
						if(CommonFunctions.isNotBlank(resultMap, "violationObjType")) {
							violationObjType = resultMap.get("violationObjType").toString();
						}
						
						if(VIOLATION_OBJ_TYPE_UNIT.equals(violationObjType)) {
							try {
								DataDictHelper.setDictValueForField(involvedPeople, "professionType", "professionTypeName", OBJ_PROFESSION_TYPE_UNIT_PCODE, orgCode);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							resultMap.put("objProfession",involvedPeople.getProfession());
							resultMap.put("objProfessionTypeUnit",involvedPeople.getProfessionType());
							resultMap.put("objProfessionTypeNameUnit",involvedPeople.getProfessionTypeName());
						} else if(VIOLATION_OBJ_TYPE_PERSON.equals(violationObjType)) {
							try {
								DataDictHelper.setDictValueForField(involvedPeople, "sex", "sexName", OBJ_SEX_PCODE, orgCode);
								DataDictHelper.setDictValueForField(involvedPeople, "politics", "politicsName", OBJ_POLITICS_PCODE, orgCode);
								DataDictHelper.setDictValueForField(involvedPeople, "professionType", "professionTypeName", OBJ_PROFESSION_TYPE_PERSON_PCODE, orgCode);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							resultMap.put("objName",involvedPeople.getName());
							resultMap.put("objSex",involvedPeople.getSex());
							resultMap.put("objSexName",involvedPeople.getSexName());
							resultMap.put("objPolitics",involvedPeople.getPolitics());
							resultMap.put("objPoliticsName",involvedPeople.getPoliticsName());
							resultMap.put("objProfessionTypePerson",involvedPeople.getProfessionType());
							resultMap.put("objProfessionTypeNamePerson",involvedPeople.getProfessionTypeName());
							resultMap.put("objProfession",involvedPeople.getProfession());
							resultMap.put("objWorkUnit",involvedPeople.getWorkUnit());
						}
					}
				}
				
				resultMapList.add(resultMap);
				
				formatMapDataOut(resultMapList, params);
				
				formatMapDataOut(resultMap, params);
			}
		}
		
		return resultMap;
	}
	
	@Override
	public EUDGPagination findAccountEnPagination(int pageNo, int pageSize,
			Map<String, Object> params, UserInfo userInfo) {
		int listType = 1;
		EUDGPagination accountEnPagination = null;
		Long userId = -1L, userOrgId = -1L;
		String userOrgCode = null;
		
		if(userInfo != null) {
			userOrgCode = userInfo.getOrgCode();
			userId = userInfo.getUserId();
			userOrgId = userInfo.getOrgId();
		}
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isBlank(params, "userOrgCode") && StringUtils.isNotBlank(userOrgCode)) {
			params.put("userOrgCode", userOrgCode);
		}
		
		formatMapDataIn(params);
		
		switch(listType) {
			case 1: {//草稿列表
				if(CommonFunctions.isBlank(params, "creator") && userId > 0) {
					params.put("creator", userId);
				}
				
				accountEnPagination = findPagination4Draft(pageNo, pageSize, params);
				break;
			}
			case 2: {//待办列表
				if(CommonFunctions.isBlank(params, "curUserId") && userId > 0) {
					params.put("curUserId", String.valueOf(userId));
				}
				if(CommonFunctions.isBlank(params, "curOrgId") && userOrgId > 0) {
					params.put("curOrgId", String.valueOf(userOrgId));
				}
				
				accountEnPagination = findPagination4Todo(pageNo, pageSize, params);
				break;
			}
			case 3: {//经办列表
				if(CommonFunctions.isBlank(params, "handledUserId") && userId > 0) {
					params.put("handledUserId", String.valueOf(userId));
				}
				if(CommonFunctions.isBlank(params, "handledOrgId") && userOrgId > 0) {
					params.put("handledOrgId", String.valueOf(userOrgId));
				}
				
				accountEnPagination = findPagination4Handled(pageNo, pageSize, params);
				break;
			}
			case 4: {//我发起的列表
				if(CommonFunctions.isBlank(params, "initiatorId") && userId > 0) {
					params.put("initiatorId", String.valueOf(userId));
				}
				if(CommonFunctions.isBlank(params, "initiatorOrgId") && userOrgId > 0) {
					params.put("initiatorOrgId", String.valueOf(userOrgId));
				}
				
				accountEnPagination = findPagination4Initiator(pageNo, pageSize, params);
				break;
			}
			case 5: {//辖区所有列表
				if(CommonFunctions.isBlank(params, "regionCode") && CommonFunctions.isNotBlank(params, "startInfoOrgCode")) {
					params.put("regionCode", params.get("startInfoOrgCode"));
				}
				
				accountEnPagination = findPagination4Jurisdiction(pageNo, pageSize, params);
				break;
			}
			case 6: {//辖区归档
				if(CommonFunctions.isBlank(params, "regionCode") && CommonFunctions.isNotBlank(params, "startInfoOrgCode")) {
					params.put("regionCode", params.get("startInfoOrgCode"));
				}
				
				accountEnPagination = findPagination4JurisdictionArchived(pageNo, pageSize, params);
				break;
			}
		}
		
		if(accountEnPagination == null) {
			accountEnPagination = new EUDGPagination();
			accountEnPagination.setTotal(0);
			accountEnPagination.setRows(new ArrayList<Map<String, Object>>());//为了使页面能正常展示
		}
		
		return accountEnPagination;
	}
	
	/**
	 * 分页获取草稿列表信息
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	private EUDGPagination findPagination4Draft(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		
		int count = accountabilityEnforcementMapper.findCountByCriteria(params);
		List<Map<String, Object>> accountEnList = accountabilityEnforcementMapper.findPageListByCriteria(params, rowBounds);
		
		formatMapDataOut(accountEnList, params);
		
		EUDGPagination accountEnPagination = new EUDGPagination(count, accountEnList);
		
		return accountEnPagination;
	}
	
	/**
	 * 分页获取待办列表信息
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * 			curUserId	当前办理人员id
	 * 			curOrgId	当前办理人员所属组织id
	 * @return
	 */
	private EUDGPagination findPagination4Todo(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		
		int count = accountabilityEnforcementMapper.findCount4Todo(params);
		List<Map<String, Object>> accountEnList = accountabilityEnforcementMapper.findPageList4Todo(params, rowBounds);
		
		formatMapDataOut(accountEnList, params);
		
		EUDGPagination accountEnPagination = new EUDGPagination(count, accountEnList);
		
		return accountEnPagination;
	}
	
	/**
	 * 分页获取经办列表数据
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * 			handledUserId	经办人员id
	 * 			handledOrgId	经办人员所属组织id
	 * @return
	 */
	private EUDGPagination findPagination4Handled(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		
		int count = accountabilityEnforcementMapper.findCount4Handled(params);
		List<Map<String, Object>> accountEnList = accountabilityEnforcementMapper.findPageList4Handled(params, rowBounds);
		
		formatMapDataOut(accountEnList, params);
		
		EUDGPagination accountEnPagination = new EUDGPagination(count, accountEnList);
		
		return accountEnPagination;
	}
	
	/**
	 * 分页获取我发起的列表记录
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * 			initiatorId		发起人员id
	 * 			initiatorOrgId	发起人员所属组织id
	 * @return
	 */
	private EUDGPagination findPagination4Initiator(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		
		int count = accountabilityEnforcementMapper.findCount4Initiator(params);
		List<Map<String, Object>> accountEnList = accountabilityEnforcementMapper.findPageList4Initiator(params, rowBounds);
		
		formatMapDataOut(accountEnList, params);
		
		EUDGPagination accountEnPagination = new EUDGPagination(count, accountEnList);
		
		return accountEnPagination;
	}
	
	/**
	 * 分页获取辖区所有列表记录
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * 			regionCode	地域编码
	 * @return
	 */
	private EUDGPagination findPagination4Jurisdiction(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		
		int count = accountabilityEnforcementMapper.findCount4Jurisdiction(params);
		List<Map<String, Object>> accountEnList = accountabilityEnforcementMapper.findPageList4Jurisdiction(params, rowBounds);
		
		formatMapDataOut(accountEnList, params);
		
		EUDGPagination accountEnPagination = new EUDGPagination(count, accountEnList);
		
		return accountEnPagination;
	}
	
	/**
	 * 辖区归档列表
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * 			regionCode	地域编码
	 * @return
	 */
	private EUDGPagination findPagination4JurisdictionArchived(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		
		int count = accountabilityEnforcementMapper.findCount4JurisdictionArchived(params);
		List<Map<String, Object>> accountEnList = accountabilityEnforcementMapper.findPageList4JurisdictionArchived(params, rowBounds);
		
		formatMapDataOut(accountEnList, params);
		
		EUDGPagination accountEnPagination = new EUDGPagination(count, accountEnList);
		
		return accountEnPagination;
	}
	
	@Override
	public boolean startWorkflow4AccountEn(Long probId, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		boolean flag = super.startWorkflow4AccountEn(probId, userInfo, extraParam);
		
		if(flag) {
			boolean isClose = false;
			
			if(CommonFunctions.isNotBlank(extraParam, "isClose")) {
				isClose = Boolean.valueOf(extraParam.get("isClose").toString());
			}
			
			if(isClose) {//采集并归档时，填写结案时间
				Map<String, Object> param = new HashMap<String, Object>();
				
				param.put("isViolationObjAlter", false);
				param.put("finTime", new Date());
				
				this.updateByProbId(probId, param, userInfo);
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean subWorkflow4AccountEn(Long probId, String nextNodeName,
			List<UserInfo> nextUserInfoList, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		boolean flag = super.subWorkflow4AccountEn(probId, nextNodeName, nextUserInfoList, userInfo, extraParam);
		
		if(flag && END_NODE_CODE.equals(nextNodeName)) {//归档时，填写结案时间
			Map<String, Object> param = new HashMap<String, Object>();
			
			param.put("isViolationObjAlter", false);
			param.put("finTime", new Date());
			
			this.updateByProbId(probId, param, userInfo);
		}
		
		return flag;
	}
	
	/**
	 * 保存额外信息
	 * @param probId	问题id
	 * @param param
	 * 			attachmentIds	附件id，以英文逗号相连
	 * 			isViolationObjAlter		true表示要调整对象信息，false不调整；默认为true
	 * @param isAdd		true表示新增
	 */
	private void saveExtraInfo(Long probId, Map<String, Object> param, UserInfo userInfo) {
		if(probId != null && probId > 0) {
			List<InvolvedPeople> list = null;
			Boolean isViolationObjAlter = true;//是否执行涉及人员保存或更新的操作，默认执行

			if (CommonFunctions.isNotBlank(param, "attachmentIds")) {
				attachmentService.updateBizId(probId, ATTACHMENT_TYPE, param.get("attachmentIds").toString());
			}

			//类型关联表保存支持多选的字段，供统计用
			if (CommonFunctions.isNotBlank(param,"source")||CommonFunctions.isNotBlank(param,"violationType")||CommonFunctions.isNotBlank(param,"violationMoneyType")) {
				try {

					param.put("bizType",TYPERELA_BIZ_TYPE);
					param.put("bizId",probId);

					try {
						typeRelaService.delTypeRela(param);
					} catch (Exception e) {
						e.printStackTrace();
					}

					//类别名称:问题来源
					if (CommonFunctions.isNotBlank(param,"source")) {
						param.put("typeValue",param.get("source"));
						param.put("typeCode",SOURCE_PCODE);
						param.put("bizColumn","SOURCE");
						typeRelaService.insertTypeRela(param,userInfo);
					}
					//类别名称:违规违纪违法类别
					if (CommonFunctions.isNotBlank(param,"violationType")) {
						param.put("typeValue",param.get("violationType"));
						param.put("typeCode",VIOLATIONTYPE_PCODE);
						param.put("bizColumn","VIOLATION_TYPE");
						typeRelaService.insertTypeRela(param,userInfo);
					}
					//类别名称:违规违纪资金类别
					if (CommonFunctions.isNotBlank(param,"violationMoneyType")) {
						param.put("typeValue",param.get("violationMoneyType"));
						param.put("typeCode",VIOLATIONMONEYTYPE_PCODE);
						param.put("bizColumn","VIOLATION_MONEY_TYPE");
						typeRelaService.insertTypeRela(param,userInfo);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			//如果isViolationObjAlter不为空，且值为false，则不执行涉及人员的相关操作
			if (CommonFunctions.isNotBlank(param, "isViolationObjAlter")) {
				isViolationObjAlter = Boolean.valueOf(param.get("isViolationObjAlter").toString());
			}
			
			if (isViolationObjAlter) {
				Long ipId = -1L;
				InvolvedPeople involvedPeople = null;
				String violationObjType = null;
				
				if(CommonFunctions.isNotBlank(param, "violationObjType")) {
					violationObjType = param.get("violationObjType").toString();
				}
				
				if(StringUtils.isNotBlank(violationObjType)) {
					list = involvedPeopleService.findInvolvedPeopleListByBiz(probId, INVOLVEDPEOPLE_BIZ_TYPE);
					
					if(list != null && list.size() > 0) {
						involvedPeople = list.get(0);
					} else {
						involvedPeople = new InvolvedPeople();
					}
					
					involvedPeople.setBizId(probId);
					involvedPeople.setBizType(INVOLVEDPEOPLE_BIZ_TYPE);
					
					//对象类型为 个人
					if(VIOLATION_OBJ_TYPE_PERSON.equals(violationObjType)) {
						if (CommonFunctions.isNotBlank(param, "objName")) {
							involvedPeople.setName(param.get("objName").toString());
						}
						if (CommonFunctions.isNotBlank(param, "objSex")) {
							involvedPeople.setSex(param.get("objSex").toString());
						}
						if (CommonFunctions.isNotBlank(param, "objPolitics")) {
							involvedPeople.setPolitics(param.get("objPolitics").toString());
						}
						if (CommonFunctions.isNotBlank(param, "objProfessionTypePerson")) {
							involvedPeople.setProfessionType(param.get("objProfessionTypePerson").toString());
						}
						if (CommonFunctions.isNotBlank(param, "objProfessionPerson")) {
							involvedPeople.setProfession(param.get("objProfessionPerson").toString());
						}
						if (CommonFunctions.isNotBlank(param, "objWorkUnit")) {
							involvedPeople.setWorkUnit(param.get("objWorkUnit").toString());
						}
					
					//对象类型为 单位
					} else if(VIOLATION_OBJ_TYPE_UNIT.equals(violationObjType)) {
						if (CommonFunctions.isNotBlank(param, "objProfessionUnit")) {
							involvedPeople.setProfession(param.get("objProfessionUnit").toString());
						}
						if (CommonFunctions.isNotBlank(param, "objProfessionTypeUnit")) {
							involvedPeople.setProfessionType(param.get("objProfessionTypeUnit").toString());
						}
					}
					
					ipId = involvedPeople.getIpId();
					
					try {
						if(ipId != null && ipId > 0) {
							involvedPeopleService.updateInvolvedPeople(involvedPeople);
						} else {
							involvedPeopleService.insertInvolvedPeople(involvedPeople);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}

		}
	}
	
	/**
	 * 格式化输入数据
	 * @param params
	 */
	private void formatMapDataIn(Map<String, Object> params) {
		boolean isSaveHandleResult = false;
		
		if(CommonFunctions.isNotBlank(params, "amountInvolved")) {
			try {
				Double amountInvolved = Double.valueOf(params.get("amountInvolved").toString());
				params.put("amountInvolved", amountInvolved);
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isNotBlank(params, "amountInvolvedStart")) {
			try {
				Double amountInvolvedStart = Double.valueOf(params.get("amountInvolvedStart").toString());
				params.put("amountInvolvedStart", amountInvolvedStart);
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isNotBlank(params, "amountInvolvedEnd")) {
			try {
				Double amountInvolvedEnd = Double.valueOf(params.get("amountInvolvedEnd").toString());
				params.put("amountInvolvedEnd", amountInvolvedEnd);
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isBlank(params, "violationDate") && CommonFunctions.isNotBlank(params, "violationDateStr")) {
			try {
				params.put("violationDate", DateUtils.convertStringToDate(params.get("violationDateStr").toString(), DateUtils.PATTERN_MONTH));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isBlank(params, "procDeadline") && CommonFunctions.isNotBlank(params, "procDeadlineStr")) {
			try {
				params.put("procDeadline", DateUtils.convertStringToDate(params.get("procDeadlineStr").toString(), DateUtils.PATTERN_DATE));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isBlank(params, "orgProcDate") && CommonFunctions.isNotBlank(params, "orgProcDateStr")) {
			try {
				params.put("orgProcDate", DateUtils.convertStringToDate(params.get("orgProcDateStr").toString(), DateUtils.PATTERN_MONTH));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isBlank(params, "partyPunishmentDate") && CommonFunctions.isNotBlank(params, "partyPunishmentDateStr")) {
			try {
				params.put("partyPunishmentDate", DateUtils.convertStringToDate(params.get("partyPunishmentDateStr").toString(), DateUtils.PATTERN_MONTH));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isBlank(params, "disciplinePunishment") && CommonFunctions.isNotBlank(params, "disciplinePunishmentStr")) {
			try {
				params.put("disciplinePunishment", DateUtils.convertStringToDate(params.get("disciplinePunishmentStr").toString(), DateUtils.PATTERN_MONTH));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isNotBlank(params, "isSaveHandleResult")) {
			isSaveHandleResult = Boolean.valueOf(params.get("isSaveHandleResult").toString());
		}
		
		if(isSaveHandleResult) {//存储处置结果
			if(CommonFunctions.isBlank(params, "partyPunishmentDate") && CommonFunctions.isNotBlank(params, "partyPunishmentDateStr")) {
				try {
					params.put("partyPunishmentDate", DateUtils.convertStringToDate(params.get("partyPunishmentDateStr").toString(), DateUtils.PATTERN_MONTH));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if(CommonFunctions.isBlank(params, "disciplinePunishmentDate") && CommonFunctions.isNotBlank(params, "disciplinePunishmentDateStr")) {
				try {
					params.put("disciplinePunishmentDate", DateUtils.convertStringToDate(params.get("disciplinePunishmentDateStr").toString(), DateUtils.PATTERN_MONTH));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if(CommonFunctions.isBlank(params, "orgProcDate") && CommonFunctions.isNotBlank(params, "orgProcDateStr")) {
				try {
					params.put("orgProcDate", DateUtils.convertStringToDate(params.get("orgProcDateStr").toString(), DateUtils.PATTERN_MONTH));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} else {
			String[] handleResultKeyArray = new String[] {
				"blameFlag", "procStatus", "shape", "amountOfRecovery",
				"partyFlag", "partyPunishmentDate", "partyPunishmentDateStr", "partyPunishmentOrgan",
				"disciplineFlag", "disciplinePunishmentDate", "disciplinePunishmentDateStr", "disciplinePunishmentOrgan",
				"orgProcType", "orgProcDate", "orgProcDateStr", "orgProcDateOrgan",
				"disciplinaryPunishment", "accountabilityDisposition",
				"transferJusticeFlag", "procResult"
			};
			
			for(String handleResultKey : handleResultKeyArray) {
				if(params.containsKey(handleResultKey)) {
					params.remove(handleResultKey);
				}
			}
		}
	}
	
	/**
	 * 格式化输出数据
	 * @param resultMapList
	 * @param params
	 * 			userOrgCode	组织编码
	 */
	private void formatMapDataOut(List<Map<String, Object>> resultMapList, Map<String, Object> params) {
		if(resultMapList != null && resultMapList.size() > 0) {
			String orgCode = null, SOURCE_PCODE = "B590000", 
				   violationObjType = null, VIOLATION_OBJ_TYPE_PCODE = "B590004";
			StringBuffer sourceName = new StringBuffer("");
			String[] sourceArray = null;
			
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				orgCode = params.get("userOrgCode").toString();
			}
			
			List<BaseDataDict> sourceDictList = baseDictionaryService.getDataDictListOfSinglestage(SOURCE_PCODE, orgCode),
							   violationObjTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(VIOLATION_OBJ_TYPE_PCODE, orgCode);
			
			for(Map<String, Object> resultMap : resultMapList) {
				if(CommonFunctions.isNotBlank(resultMap, "created")) {
					resultMap.put("createdStr", DateUtils.formatDate((Date)resultMap.get("created"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(resultMap, "violationDate")) {
					resultMap.put("violationDateStr", DateUtils.formatDate((Date)resultMap.get("violationDate"), DateUtils.PATTERN_MONTH));
				}
				if(CommonFunctions.isNotBlank(resultMap, "source")) {
					sourceName.setLength(0);
					sourceArray = resultMap.get("source").toString().split(",");
					
					for(String source : sourceArray) {
						for(BaseDataDict sourceDict : sourceDictList) {
							if(source.equals(sourceDict.getDictGeneralCode())) {
								sourceName.append(",").append(sourceDict.getDictName());
								break;
							}
						}
					}
					
					if(sourceName.length() > 0) {
						resultMap.put("sourceName", sourceName.substring(1));
					}
				}
				if(CommonFunctions.isNotBlank(resultMap, "violationObjType")) {
					violationObjType = resultMap.get("violationObjType").toString();
					
					for(BaseDataDict violationObjTypeDict : violationObjTypeDictList) {
						if(violationObjType.equals(violationObjTypeDict.getDictGeneralCode())) {
							resultMap.put("violationObjTypeName", violationObjTypeDict.getDictName());
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * 详情数据格式化输出
	 * @param resultMap
	 * @param params
	 * 			userOrgCode	组织编码
	 */
	private void formatMapDataOut(Map<String, Object> resultMap, Map<String, Object> params) {
		if(resultMap != null && !resultMap.isEmpty()) {
			String orgCode = null, 
				   violationMoneyType = null, VIOLATION_MONEY_TYPE_PCODE = "B590002",
				   violationType = null, VIOLATION_TYPE_PCODE = "B590003",
				   partyFlag = null, PARTY_FLAG_PCODE = "B590012001",
				   disciplineFlag = null, DISCIPLINE_FLAG_PCODE = "B590012002",
				   orgProcType = null, ORG_PROC_TYPE_PCODE = "B590013",
				   blameFlag = "0", transferJusticeFlag = "0",
				   shape = null, SHAPE_PCODE = "B590014",
				   procStatus = null, PROC_STATUS_PCODE = "B590009";
			
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				orgCode = params.get("userOrgCode").toString();
			}
			
			if(CommonFunctions.isNotBlank(resultMap, "procDeadline")) {
				resultMap.put("procDeadlineStr", DateUtils.formatDate((Date)resultMap.get("procDeadline"), DateUtils.PATTERN_DATE));
			}
			
			if(CommonFunctions.isNotBlank(resultMap, "violationMoneyType")) {
				List<BaseDataDict> violationMoneyTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(VIOLATION_MONEY_TYPE_PCODE, orgCode);
				violationMoneyType = resultMap.get("violationMoneyType").toString();
				StringBuffer dictFullPath = new StringBuffer("");
				String[] violationMoneyTypeArr = violationMoneyType.split(",");

				for(String moneyType : violationMoneyTypeArr) {
					for(BaseDataDict dataDict : violationMoneyTypeDictList) {
						if(moneyType.equals(dataDict.getDictGeneralCode())) {
							dictFullPath.append(",").append(dataDict.getDictName());
							break;
						}
					}
				}
				if(dictFullPath.length() > 0) {
					resultMap.put("violationMoneyTypeName", dictFullPath.substring(1));
				}
			}
			if(CommonFunctions.isNotBlank(resultMap, "violationType")) {
				violationType = resultMap.get("violationType").toString();
				Map<String, Object> dictMap = new HashMap<String, Object>();
				dictMap.put("orgCode", orgCode);
				dictMap.put("dictPcode", VIOLATION_TYPE_PCODE);
				
				List<BaseDataDict> violationTypeDictList = baseDictionaryService.findDataDictListByCodes(dictMap);
				
				StringBuffer dictFullPath = new StringBuffer("");
				String[] bigTypeArr = violationType.split(",");
				String bigTypeName = "", bigDictCode = null;
				int n = 0;

				for (String bigType:bigTypeArr) {
					do {
						bigTypeName = "";
	
						for(BaseDataDict dataDict : violationTypeDictList) {
							if((StringUtils.isNotBlank(bigDictCode) && !VIOLATION_TYPE_PCODE.equals(bigDictCode) && bigDictCode.equals(dataDict.getDictCode()))
									||
									(StringUtils.isNotBlank(bigType) && bigType.equals(dataDict.getDictGeneralCode()))) {
								bigTypeName = dataDict.getDictName();
								bigDictCode = dataDict.getDictPcode();
								bigType = null;
								break;
							}
						}
						//判断是偶数次还是奇数次，偶数次用"-"分割，奇数次用","分割
						if(StringUtils.isNotBlank(bigTypeName) && n%2==0) {
							dictFullPath.insert(0, bigTypeName).insert(0, "-");
							n++;
						} else if (StringUtils.isNotBlank(bigTypeName)) {
							dictFullPath.insert(0, bigTypeName).insert(0, ",");
							n++;
						}
					} while(StringUtils.isNotBlank(bigTypeName));
	
				}
				if(dictFullPath.length() > 0) {
					resultMap.put("violationTypeName", dictFullPath.substring(1));
				}
			}
			if(CommonFunctions.isNotBlank(resultMap, "partyFlag")) {
				partyFlag = resultMap.get("partyFlag").toString();
				List<BaseDataDict> partyFlagDictList = baseDictionaryService.getDataDictListOfSinglestage(PARTY_FLAG_PCODE, orgCode);
				
				for(BaseDataDict partyFlagDict : partyFlagDictList) {
					if(partyFlag.equals(partyFlagDict.getDictGeneralCode())) {
						resultMap.put("partyFlagName", partyFlagDict.getDictName());
						break;
					}
				}
			}
			if(CommonFunctions.isNotBlank(resultMap, "partyPunishmentDate")) {
				resultMap.put("partyPunishmentDateStr", DateUtils.formatDate((Date)resultMap.get("partyPunishmentDate"), DateUtils.PATTERN_MONTH));
			}
			if(CommonFunctions.isNotBlank(resultMap, "disciplineFlag")) {
				disciplineFlag = resultMap.get("disciplineFlag").toString();
				List<BaseDataDict> disciplineFlagDictList = baseDictionaryService.getDataDictListOfSinglestage(DISCIPLINE_FLAG_PCODE, orgCode);
				
				for(BaseDataDict disciplineFlagDict : disciplineFlagDictList) {
					if(disciplineFlag.equals(disciplineFlagDict.getDictGeneralCode())) {
						resultMap.put("disciplineFlagName", disciplineFlagDict.getDictName());
						break;
					}
				}
			}
			if(CommonFunctions.isNotBlank(resultMap, "disciplinePunishmentDate")) {
				resultMap.put("disciplinePunishmentDateStr", DateUtils.formatDate((Date)resultMap.get("disciplinePunishmentDate"), DateUtils.PATTERN_MONTH));
			}
			if(CommonFunctions.isNotBlank(resultMap, "orgProcType")) {
				orgProcType = resultMap.get("orgProcType").toString();
				Map<String, Object> dictMap = new HashMap<String, Object>();
				dictMap.put("orgCode", orgCode);
				dictMap.put("dictPcode", ORG_PROC_TYPE_PCODE);
				
				List<BaseDataDict> orgProcTypeDictList = baseDictionaryService.findDataDictListByCodes(dictMap);
				
				StringBuffer dictFullPath = new StringBuffer("");
				String bigType = orgProcType, bigTypeName = "", bigDictCode = null;
				
				do {
					bigTypeName = "";
					
					for(BaseDataDict dataDict : orgProcTypeDictList) {
						if((StringUtils.isNotBlank(bigDictCode) && !ORG_PROC_TYPE_PCODE.equals(bigDictCode) && bigDictCode.equals(dataDict.getDictCode()))
							||
							(StringUtils.isNotBlank(bigType) && bigType.equals(dataDict.getDictGeneralCode()))) {
							bigTypeName = dataDict.getDictName();
							bigDictCode = dataDict.getDictPcode();
							bigType = null;
							break;
						}
					}
					
					if(StringUtils.isNotBlank(bigTypeName)) {
						dictFullPath.insert(0, bigTypeName).insert(0, "-");
					}
				} while(StringUtils.isNotBlank(bigTypeName));
				
				if(dictFullPath.length() > 0) {
					resultMap.put("orgProcTypeName", dictFullPath.substring(1));
				}
			}
			if(CommonFunctions.isNotBlank(resultMap, "orgProcDate")) {
				resultMap.put("orgProcDateStr", DateUtils.formatDate((Date)resultMap.get("orgProcDate"), DateUtils.PATTERN_MONTH));
			}
			if(CommonFunctions.isNotBlank(resultMap, "blameFlag")) {
				blameFlag = resultMap.get("blameFlag").toString();
			}
			if(CommonFunctions.isNotBlank(resultMap, "transferJusticeFlag")) {
				transferJusticeFlag = resultMap.get("transferJusticeFlag").toString();
			}
			if(CommonFunctions.isNotBlank(resultMap, "shape")) {
				shape = resultMap.get("shape").toString();
				List<BaseDataDict> shapeDictList = baseDictionaryService.getDataDictListOfSinglestage(SHAPE_PCODE, orgCode);
				
				for(BaseDataDict shapeDict : shapeDictList) {
					if(shape.equals(shapeDict.getDictGeneralCode())) {
						resultMap.put("shapeName", shapeDict.getDictName());
						break;
					}
				}
			}
			if(CommonFunctions.isNotBlank(resultMap, "procStatus")) {
				procStatus = resultMap.get("procStatus").toString();
				List<BaseDataDict> procStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(PROC_STATUS_PCODE, orgCode);
				
				for(BaseDataDict procStatusDict : procStatusDictList) {
					if(procStatus.equals(procStatusDict.getDictGeneralCode())) {
						resultMap.put("procStatusName", procStatusDict.getDictName());
						break;
					}
				}
			}
			
			if("1".equals(blameFlag)) {
				resultMap.put("blameFlagName", "是");
			} else {
				resultMap.put("blameFlagName", "否");
			}
			
			if("1".equals(transferJusticeFlag)) {
				resultMap.put("transferJusticeFlagName", "是");
			} else {
				resultMap.put("transferJusticeFlagName", "否");
			}
		}
	}
}
