package cn.ffcs.zhsq.courtsynergism.service;

import java.util.*;

import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.system.publicUtil.StringUtils;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.gdpersionsendflow.service.IGdPersionSendWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.courtsynergism.CourtSynergism;
import cn.ffcs.zhsq.mybatis.persistence.courtsynergism.CourtSynergismMapper;
import cn.ffcs.zhsq.mybatis.persistence.gdpersionsendflow.GdPersionSendFlowMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.ffcs.system.publicUtil.EUDGPagination;

/**
 * @Description: 法院协同办公模块服务实现
 * @Author: zhangch
 * @Date: 05-20 11:01:56
 * @Copyright: 2020 福富软件
 */
@Service("courtSynergismServiceImpl")
@Transactional
public class CourtSynergismServiceImpl implements ICourtSynergismService {

	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	//系统管理用户管理
	@Autowired
	private UserManageOutService userManageOutService;
	//组织管理
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;
	// 新字典服务
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	//注入法院协同办公模块dao
	@Autowired
	private CourtSynergismMapper courtSynergismMapper;

	private static final Map<String, String> STARUS_MAP = new HashMap<String, String>(){
		{
			put("01", "草稿");
			put("02", "办理中");
			put("03", "待评价");
			put("04", "结束");
		}
	};

	public static final String ACTOR_TYPE_ORG = "1";		//1表示ACTOR_ID为组织ID
	public static final String ACTOR_TYPE_ROLE = "2";		//2表示ACTOR_ID为角色ID
	public static final String ACTOR_TYPE_USER = "3";		//3表示ACTOR_ID为用户ID
	public static final String ACTOR_TYPE_POSITION = "4";	//4表示ACTOR_ID为职位ID
	//流程图表单Id：8150  任务流程图名称 任务流程类型
	private final String CASE_WORKFLOW_NAME = "法院协同办公",QU_TASK_ID="task3",COURT_TASK_ID="task4",END_TASK_ID="task5",
			CASE_WFTYPE_ID = "court_synergism";
    //我发起
    private final String MENUTYPE_MYCREATE = "mycreate",
            MENUTYPE_MYWAIT = "mywait",MENUTYPE_MYHANDLE = "myhandle";
	/**
	 * 新增数据
	 * @param bo 法院协同办公业务对象
	 * @return 法院协同办公id
	 */
	@Override
	public Long insert(CourtSynergism bo) {
		courtSynergismMapper.insert(bo);
		return bo.getSynergismId();
	}

	/**
	 * 修改数据
	 * @param bo 法院协同办公业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(CourtSynergism bo) {
		long result = courtSynergismMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 法院协同办公业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(CourtSynergism bo) {
		long result = courtSynergismMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param bo 查询参数
	 * @return 法院协同办公分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, CourtSynergism bo) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		String menuType =  bo.getMenuType();
        EUDGPagination pagination;
		if(StringUtils.isBlank(menuType) || MENUTYPE_MYCREATE.equals(menuType)){
            pagination = searchList4MyCreate(bo, rowBounds);
        }else if(MENUTYPE_MYWAIT.equals(menuType)){
            pagination = searchList4MyWait(bo, rowBounds);
        }else{
            pagination = searchList4MyHandle(bo, rowBounds);
        }
		return pagination;
	}

    /**
     * 经办
     * @param bo
     * @param rowBounds
     * @return
     */
    private EUDGPagination searchList4MyHandle(CourtSynergism bo, RowBounds rowBounds) {
        List<CourtSynergism> list = courtSynergismMapper.searchList4MyHandle(bo, rowBounds);
        long count = courtSynergismMapper.count4MyHandle(bo);
        return  new EUDGPagination(count, list);
    }

    /**
     * 我的待办
     * @param bo
     * @param rowBounds
     * @return
     */
    private EUDGPagination searchList4MyWait(CourtSynergism bo, RowBounds rowBounds) {
        List<CourtSynergism> list = courtSynergismMapper.searchList4MyWait(bo, rowBounds);
        long count = courtSynergismMapper.countList4MyWait(bo);
        return  new EUDGPagination(count, list);
    }

    /**
     * 我的发起
     * @param bo
     * @param rowBounds
     * @return
     */
    private EUDGPagination searchList4MyCreate(CourtSynergism bo, RowBounds rowBounds) {
        List<CourtSynergism> list = courtSynergismMapper.searchList4MyCreate(bo, rowBounds);
        long count = courtSynergismMapper.countList4MyCreate(bo);
        return  new EUDGPagination(count, list);
    }

    /**
	 * 根据业务id查询数据
	 * @param id 法院协同办公id
	 * @return 法院协同办公业务对象
	 */
	@Override
	public CourtSynergism searchById(Long id,UserInfo userInfo ) {
		CourtSynergism bo = new CourtSynergism();
		if(id != null){
            bo = courtSynergismMapper.searchById(id);
			bo.setApplyDateStr(DateUtils.formatDate(bo.getApplyDate(),DateUtils.PATTERN_DATE));
			bo.setApplyTypeCN(baseDictionaryService.changeCodeToName(ConstantValue.APPLY_TYPE_DICTCODE,bo.getApplyType(),null));
			bo.setSatisfactionCN(baseDictionaryService.changeCodeToName(ConstantValue.SATISFACTION_DICTCODE,bo.getSatisfaction(),null));
			bo.setStatusCN(STARUS_MAP.get(bo.getStatus()));
		}else{
			bo.setGridCode(userInfo.getOrgCode());
			bo.setGridName(userInfo.getOrgName());
			bo.setApplyDateStr(DateUtils.formatDate(new Date(),DateUtils.PATTERN_DATE));
		}
		return bo;
	}

	@Override
	public int startWorkFlow(CourtSynergism bo ,UserInfo userInfo) {
		List<UserInfo> nextUserInfoList = getNextUserInfoList(userInfo,4);
		if(nextUserInfoList == null){
			return -1;
		}
		try {
			Long instanceId = baseWorkflowService.startWorkflow4Base(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, bo.getSynergismId(),userInfo, null);
			if(instanceId > 0){
				String nextNodeName="task2";
				boolean flag = baseWorkflowService.subWorkflow4Base(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, bo.getSynergismId(), nextNodeName, nextUserInfoList, userInfo, null);
				if(flag){
					return 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		}
		return 0;
	}

	@Override
	public Map<String, Object> getCurNode(Long synergismId, UserInfo userInfo) throws Exception {
		Long instanceId = -1L;
		if(synergismId != null && synergismId > 0) {
			instanceId = baseWorkflowService.capInstanceId(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, synergismId, userInfo);
		}
		return baseWorkflowService.findCurTaskData(instanceId);
	}

	@Override
	public List<Node> findNextTaskNodes(Long synergismId, UserInfo userInfo) throws Exception {
		return baseWorkflowService.findNextTaskNodes(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, synergismId,userInfo,null);
	}

	@Override
	public int submit(CourtSynergism bo, UserInfo userInfo) {
		try {
			List<UserInfo> nextUserInfoList = null;
			if(QU_TASK_ID.equals(bo.getNextNodeId())){
				nextUserInfoList = getNextUserInfoList(userInfo,3);
			}else if(COURT_TASK_ID.equals(bo.getNextNodeId())){
				List<OrgSocialInfoBO> orgSocialInfoBOList = orgSocialInfoOutService.findCilndByOrgId(userInfo.getOrgId(),null);
				OrgSocialInfoBO fyBo = null;
				for(OrgSocialInfoBO socialInfoBO : orgSocialInfoBOList){
					if(socialInfoBO.getOrgName().indexOf("法院") > -1){
						fyBo = socialInfoBO;
					}
				}
				if(fyBo == null){
					return -2;
				}
				nextUserInfoList = userManageOutService.findUserByOrgCode(fyBo.getOrgCode());
			} else if(END_TASK_ID.equals(bo.getNextNodeId())){
				nextUserInfoList = new ArrayList<>();
				UserBO userBO = userManageOutService.getUserInfoByUserId(bo.getCreator());
				OrgSocialInfoBO bo1 = orgSocialInfoOutService.getOrgIdByLocationCode(bo.getGridCode());
				UserInfo user = new UserInfo();
				user.setUserId(userBO.getUserId());
				user.setOrgId(Long.valueOf(bo1.getOrgId()));
				user.setOrgCode(bo.getGridCode());
				user.setPartyName(userBO.getPartyName());
				user.setOrgName(userBO.getOrgName());
				nextUserInfoList.add(user);
			}
			if(nextUserInfoList == null && !END_TASK_ID.equals(bo.getNowNodeId())){
				return -1;
			}
			String nextNodeName = bo.getNextNodeId();
			Map<String,Object> params = new HashMap<>(1);
			params.put("advice",bo.getAdvice());
			boolean flag = baseWorkflowService.subWorkflow4Base(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, bo.getSynergismId(), nextNodeName, nextUserInfoList, userInfo, params);
			if(flag){
				if(COURT_TASK_ID.equals(bo.getNowNodeId()) || END_TASK_ID.equals(bo.getNowNodeId())){
					String status = COURT_TASK_ID.equals(bo.getNowNodeId())? "03":"04";
					CourtSynergism x = new CourtSynergism();
					x.setStatus(status);
					x.setAdvice(bo.getAdvice());
					x.setSynergismId(bo.getSynergismId());
					x.setSatisfaction(bo.getSatisfaction());
					courtSynergismMapper.update(x);
				}
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		}
		return 0;
	}

	@Override
	public int reject(CourtSynergism bo, UserInfo userInfo) {
		try {
			Map<String,Object> params = new HashMap<>(1);
			params.put("advice",bo.getAdvice());
			//流程驳回
			boolean flag = rejectWorkflow4Case(bo.getSynergismId(),"", userInfo, params);
			if(flag){
				//更改法院提交的办理意见
				if(COURT_TASK_ID.equals(bo.getNowNodeId()) || END_TASK_ID.equals(bo.getNowNodeId())){
					String status = COURT_TASK_ID.equals(bo.getNowNodeId())? "03":"04";
					CourtSynergism x = new CourtSynergism();
					x.setStatus(status);
					x.setAdvice(bo.getAdvice());
					x.setSynergismId(bo.getSynergismId());
					x.setSatisfaction(bo.getSatisfaction());
					courtSynergismMapper.update(x);
				}
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	@Override
	public boolean rejectWorkflow4Case(Long sendId, String rejectToNodeName, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;

		if(sendId == null || sendId < 0) {
			throw new IllegalArgumentException("缺少任务id！");
		}

		//判断是否是当前环节办理人
		if(isCurTaskPaticipant(sendId, userInfo, null)) {
			//流程驳回
			flag = baseWorkflowService.rejectWorkflow4Base(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, sendId, rejectToNodeName, userInfo, extraParam);
		} else {
			String msgWrong = "用户["+ userInfo.getOrgName() +"-"+ userInfo.getPartyName() +"] 不是当前环节[" + this.capCurTaskName(sendId, userInfo) + "] 的办理人员！";
			throw new Exception(msgWrong);
		}

		return flag;
	}

	@Override
	public Long capInstanceId(CourtSynergism bo,UserInfo userInfo) {
		try {
			return baseWorkflowService.capInstanceId(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, bo.getSynergismId(), userInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<UserInfo> getNextUserInfoList(UserInfo userInfo,int gridLevel){
		List<OrgSocialInfoBO> orgSocialInfoBOList = orgSocialInfoOutService.findBySelfIdAndLevel(userInfo.getOrgId(),gridLevel);
		if(orgSocialInfoBOList.size() <= 0){
			return null;
		}
		List<UserBO> userBOS = userManageOutService.queryUserListByPositionAndOrg("专班",null,"000",null,orgSocialInfoBOList.get(0).getOrgCode());
		if(userBOS.size() <= 0){
			return null;
		}
		List<UserInfo> nextUserInfoList = new ArrayList<>();
		for (UserBO userBO : userBOS) {
			UserInfo user = new UserInfo();
			user.setUserId(userBO.getUserId());
			user.setOrgId(Long.valueOf(userBO.getSocialOrgId()));
			user.setOrgCode(userBO.getOrgCode());
			user.setPartyName(userBO.getPartyName());
			user.setOrgName(userBO.getOrgName());
			nextUserInfoList.add(user);
		}
		return nextUserInfoList;
	}

	/**
	 * 判断是否是当前环节办理人
	 */
	@Override
	public boolean isCurTaskPaticipant(Long sendId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		Long wfTaskId = -1L;

		Map<String, Object>	curTaskData = baseWorkflowService.findCurTaskData(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, sendId, userInfo);

		if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
			wfTaskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
		}

		if(wfTaskId > 0) {
			List<Map<String, Object>> participantMapList = this.findParticipantsByTaskId(wfTaskId);

			flag = this.isCurTaskPaticipant(participantMapList, userInfo, extraParam);
		}


		return flag;
	}

	/**
	 * 判断用户是否为当前任务的办理人员
	 */
	@Override
	public boolean isCurTaskPaticipant(List<Map<String, Object>> participantMapList, UserInfo userInfo,
									   Map<String, Object> extraParam) throws Exception {
		boolean flag = false;

		if(participantMapList != null && participantMapList.size() > 0) {
			Long userId = -1L, orgId = -1L,
					curTaskUserId = -1L, curTaskOrgId = -1L;
			String userType = "";//1表示USER_ID为组织ID；3表示USER_ID为用户ID

			if(userInfo != null) {
				userId = userInfo.getUserId();
				orgId = userInfo.getOrgId();
			}

			for(Map<String, Object> participantMap : participantMapList) {
				curTaskUserId = -1L;
				curTaskOrgId = -1L;
				userType = "";

				if(CommonFunctions.isNotBlank(participantMap, "USER_ID")) {
					curTaskUserId = Long.valueOf(participantMap.get("USER_ID").toString());
				}
				if(CommonFunctions.isNotBlank(participantMap, "USER_TYPE")) {
					userType = participantMap.get("USER_TYPE").toString();
				}
				if(CommonFunctions.isNotBlank(participantMap, "ORG_ID")) {
					curTaskOrgId =  Long.valueOf(participantMap.get("ORG_ID").toString());
				}

				//当前办理人要相同的用户和相同的组织
				if(this.ACTOR_TYPE_USER.equals(userType)) {
					flag = userId.equals(curTaskUserId) && orgId.equals(curTaskOrgId);
				} else if(this.ACTOR_TYPE_ORG.equals(userType)) {
					flag = orgId.equals(curTaskUserId);
				}

				if(flag) {
					break;
				}

			}
		}

		return flag;
	}

	/**
	 * 获取当前环节中文名称
	 * @param sendId	业务主键id
	 * @param userInfo	用户信息
	 * @return
	 * @throws Exception
	 */
	private String capCurTaskName(Long sendId, UserInfo userInfo) throws Exception {
		Map<String, Object> curTaskData = this.findCurTaskData(sendId, userInfo);
		String curTaskName = null;

		if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
			curTaskName = curTaskData.get("WF_ACTIVITY_NAME_").toString();
		}

		if(curTaskName == null) {
			throw new Exception("任务已完结！");
		}

		return curTaskName;
	}

	/**
	 * 依据任务ID获取当前办理任务信息
	 */
	@Override
	public Map<String, Object> findCurTaskData(Long sendId, UserInfo userInfo) throws Exception {
		Map<String, Object> curTaskData = null;

		if(sendId != null && sendId > 0) {
			curTaskData = baseWorkflowService.findCurTaskData(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, sendId, userInfo);
		}

		return curTaskData;
	}

	/**
	 * 依据工作流任务id获取任务办理人员信息
	 */
	@Override
	public List<Map<String, Object>> findParticipantsByTaskId(Long wfTaskId) {
		List<Map<String, Object>> participantMapList = null;

		if(wfTaskId != null && wfTaskId > 0){
			participantMapList = baseWorkflowService.findParticipantsByTaskId(wfTaskId);
		}

		if(participantMapList != null && participantMapList.size() > 0) {
			Long userId = -1L, orgId = -1L;
			OrgSocialInfoBO orgInfo = null;
			UserBO user = null;

			for(Map<String, Object> participantMap : participantMapList) {
				if(CommonFunctions.isNotBlank(participantMap, "USER_TYPE")) {
					String userType = participantMap.get("USER_TYPE").toString();

					if(this.ACTOR_TYPE_USER.equals(userType)) {
						if(CommonFunctions.isNotBlank(participantMap, "USER_ID")) {
							userId = Long.valueOf(participantMap.get("USER_ID").toString());
							user = userManageOutService.getUserInfoByUserId(userId);

							if(user != null) {
								participantMap.put("USER_NAME", user.getPartyName());
							}
						}
					}
				}

				if(CommonFunctions.isNotBlank(participantMap, "ORG_ID")) {
					orgId = Long.valueOf(participantMap.get("ORG_ID").toString());
					orgInfo = orgSocialInfoOutService.findByOrgId(orgId);

					if(orgInfo != null) {
						participantMap.put("ORG_NAME", orgInfo.getOrgName());
					}
				}
			}
		}
		return participantMapList;
	}
}