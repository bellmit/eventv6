package cn.ffcs.zhsq.gdpersionsendflow.service;

import cn.ffcs.gmis.fgv.court.service.ICourtService;
import cn.ffcs.gmis.mybatis.domain.fgv.court.Court;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.system.publicUtil.StringUtils;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.courtsynergism.CourtSynergism;
import cn.ffcs.zhsq.mybatis.domain.gdpersionsendflow.GdPersionSendFlow;
import cn.ffcs.zhsq.mybatis.persistence.gdpersionsendflow.GdPersionSendFlowMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

/**
 * @Description: 网格员协助送达流程模块服务实现
 * @Author: zhangch
 * @Date: 10-16 09:18:15
 * @Copyright: 2019 福富软件
 */
@Service("gdPersionSendFlowServiceImpl")
@Transactional
public class GdPersionSendFlowServiceImpl implements IGdPersionSendFlowService {

	public static final String ATTACTH_TYPE_GD = "GD_PERSION_FLOW_TYPE";

	@Autowired
	private GdPersionSendFlowMapper gdPersionSendFlowMapper; //注入网格员协助送达流程模块dao

	@Autowired
	private IGdPersionSendWorkflowService gdPersionSendWorkflowService;//网格员协助送达流程-工作流服务封装
	
	@Autowired
	private IAttachmentService attachmentService;

	@Autowired
	private IBaseWorkflowService baseWorkflowService;

	@Autowired
	private UserManageOutService userManageOutService;//系统管理用户管理
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;//组织管理

	@Autowired
	private IBaseDictionaryService baseDictionaryService; // 新字典服务

	@Autowired
	private ICourtService courtService; // 参与法院诉前调解化解案件

	private static final Map<String, String> STARUS_MAP = new HashMap<String, String>(){
		{
			put("01", "草稿");
			put("02", "流转中");
			put("03", "已送达");
			put("04", "未送达");
			put("05", "归档");
		}
	};

	public static final String ACTOR_TYPE_ORG = "1";		//1表示ACTOR_ID为组织ID
	public static final String ACTOR_TYPE_ROLE = "2";		//2表示ACTOR_ID为角色ID
	public static final String ACTOR_TYPE_USER = "3";		//3表示ACTOR_ID为用户ID
	public static final String ACTOR_TYPE_POSITION = "4";	//4表示ACTOR_ID为职位ID
	//流程图表单Id：815
	private final String CASE_WORKFLOW_NAME = "网格员协助送达流程",//任务流程图名称
			CASE_WFTYPE_ID = "gd_person_send";//任务流程类型
	public static final String APPLY_TYPE_SQTJ = "003";	//诉前调解协作类型
	/**
	 * 菜单对应的选择项
	 * wait_survey 		待办列表：	        1
	 * no_file	 		未归档的在办任务 ：	2
	 */
	protected static final Map<String,Integer> MENUTYPE_MAP = new HashMap<String,Integer>();
	static{
		MENUTYPE_MAP.put("mycreate", 1);//我的发起
		MENUTYPE_MAP.put("mywait", 2);//我的待办
		MENUTYPE_MAP.put("myhandle", 3);//我的经办
    }

	/**
	 * 新增数据
	 * @param bo 网格员协助送达流程业务对象
	 * @return 网格员协助送达流程id
	 */
	@Override
	public Long insert(GdPersionSendFlow bo) throws Exception{
		gdPersionSendFlowMapper.insert(bo);
		if(bo.getAttachmentId() != null && bo.getAttachmentId().length > 0){
			attachmentService.updateBizId(bo.getSendId(),ATTACTH_TYPE_GD,bo.getAttachmentId());
		}


		return bo.getSendId();
	}

	/**
	 * 修改数据
	 * @param bo 网格员协助送达流程业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(GdPersionSendFlow bo) {
		long result = gdPersionSendFlowMapper.update(bo);
        if(bo.getAttachmentId() != null && bo.getAttachmentId().length > 0){
            attachmentService.updateBizId(bo.getSendId(),ATTACTH_TYPE_GD,bo.getAttachmentId());
        }
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 网格员协助送达流程业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(GdPersionSendFlow bo) {
		long result = gdPersionSendFlowMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 网格员协助送达流程id
	 * @return 网格员协助送达流程业务对象
	 */
	@Override
	public GdPersionSendFlow searchById(Long id) {
		GdPersionSendFlow bo = gdPersionSendFlowMapper.searchById(id);
		translationByBo(bo, null);
		return bo;
	}

	@Override
	public int startWorkflow(Long taskId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		List<OrgSocialInfoBO> orgSocialInfoBOList = orgSocialInfoOutService.findSuperior(userInfo.getOrgId());
		if(orgSocialInfoBOList.size() <= 0){
			return -2;
		}
		List<UserBO> userBOS = userManageOutService.queryUserListByPositionAndOrg("专班",null,"000",null,orgSocialInfoBOList.get(0).getOrgCode());
		if(userBOS.size() <= 0){
			return -1;
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
		Long instanceId = baseWorkflowService.startWorkflow4Base(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, taskId,userInfo, extraParam);
		if(instanceId > 0){
			String nextNodeName="task2";
			boolean flag = baseWorkflowService.subWorkflow4Base(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, taskId, nextNodeName, nextUserInfoList, userInfo, extraParam);
			if(flag){
				return 1;
			}
		}
		return 0;
	}

	@Override
	public int resInsert(GdPersionSendFlow bo,UserInfo userInfo) {
		int success = 0;
//		//先归档？
//		Map<String,Object> params = new HashMap<>();
//		params.put("nextLink", "end1");
//		params.put("sendType", "end1");
//		params.put("userInfo", userInfo);
//		boolean flag = this.saveHandleInfo(bo,params);
//		if(flag){
			gdPersionSendFlowMapper.insert(bo);
			List<Attachment> attachments = attachmentService.findByBizId(bo.getLastSendId(),ATTACTH_TYPE_GD);
			if(attachments != null && attachments.size() > 0){
				for(Attachment attachment : attachments){
					attachment.setBizId(bo.getSendId());
					attachment.setAttachmentId(null);
					attachment.setCreatorId(bo.getCreator());
					attachment.setCreateTime(null);
				}
				attachmentService.saveAttachment(attachments);
			}
			translationByBo(bo,userInfo);
			success = 1;
//		}else{
//			success = -1;
//		}
		return success;
	}

	/**
	 * 实体格式化
	 * @param bo
	 * @param userInfo 用于查询流程信息
	 */
	private void translationByBo(GdPersionSendFlow bo,UserInfo userInfo) {
		if (bo != null) {
			//1.格式化创建时间
			if(bo.getLimitDate() != null && bo.getPublishDate() != null){
				Long bTime = bo.getLimitDate().getTime();
				Long sTime = bo.getPublishDate().getTime();
				Integer limitDateNum =(int) ((bTime - sTime)/(3600F * 1000 * 24));
				bo.setLimitDateNum(limitDateNum);//办理时间

				//剩余的办理天数
				Date currDate=new Date();
				try {
					currDate = DateUtils.convertStringToDate(DateUtils.getToday(), DateUtils.PATTERN_DATE);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Long currTime = currDate.getTime();
				Integer surplusDateNum = (int) ((bTime - currTime)/(3600F * 1000 * 24));
				bo.setSurplusDateNum(surplusDateNum);//剩余天数
				//字典翻译
				bo.setSendTypeCN(baseDictionaryService.changeCodeToName(ConstantValue.SEND_TYPE_DICTCODE,bo.getSendType(),null));
			}



			String publishDateStr = DateUtils.formatDate(bo.getPublishDate(), DateUtils.PATTERN_DATE);
			String limitDateStr = DateUtils.formatDate(bo.getLimitDate(), DateUtils.PATTERN_DATE);
			String statusStr = STARUS_MAP.get(bo.getStatus());

			bo.setLimitDateStr(limitDateStr);
			bo.setStatusStr(statusStr);
			bo.setPublishDateStr(publishDateStr);
			String str = null;
			if(StringUtils.isBlank(bo.getReceiveState()) || "0".equals(bo.getReceiveState())){
				str = "未知";
			}else if("1".equals(bo.getReceiveState())){
				str = "已送达";
			}else if("2".equals(bo.getReceiveState())){
				str = "未送达";
			}
			bo.setReceiveStateStr(str);

			//2.格式化流程信息(当前办理信息)
			if(userInfo!=null) {
				Map<String, Object> currLinkData = null;
				try {
					currLinkData = gdPersionSendWorkflowService.findCurTaskData(bo.getSendId(), userInfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(currLinkData != null ) {
					String currLink = StringUtils.isBlank((String)currLinkData.get("WF_ACTIVITY_NAME_"))?"-":(String)currLinkData.get("WF_ACTIVITY_NAME_");
					String currOrgName = (String) currLinkData.get("WF_ORGNAME");
					String currUserName = (String) currLinkData.get("WF_USERNAME");
					
					//设置更新的参数
					bo.setCurrentCodeCn(currOrgName);//当前组织名称
					bo.setCurrentHandler(currUserName);//当前办理人
					bo.setCurrentNode(currLink);//当前组织环节
					bo.setCurrLink(currLink);//当前环节
					
				}
			}
			

		}
	}


	//=============================================张祯海  start===================================================//

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 网格员协助送达流程分页数据对象
	 */
	@Override
	public EUDGPagination searchList(Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) params.get("userInfo");
		GdPersionSendFlow gdPersionSendFlow = (GdPersionSendFlow) params.get("gdPersionSendFlow");
		gdPersionSendFlow = gdPersionSendFlow == null? new GdPersionSendFlow():gdPersionSendFlow;
		int page = gdPersionSendFlow.getPage();
        int rows = gdPersionSendFlow.getRows();
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);

        EUDGPagination pagination = null;

        Integer menuType = 0;
        if (CommonFunctions.isNotBlank(params, "menuType")) {
			try {
				menuType = Integer.valueOf(MENUTYPE_MAP.get((String)params.get("menuType")));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

        //根据菜单类型，修改设置查询参数
        capQueryParam(params);
        switch (menuType) {
			case 1: {// 我的发起
				pagination = getPaginationForMyCreate(params,rowBounds);
				break;
			}
			case 2: {// 我的待办
				pagination = getPaginationForMyWait(params,rowBounds);
				break;
			}
			case 3: {// 我的经办
				pagination = getPaginationForMyHandle(params,rowBounds);
				break;
			}
		}

        //为了使页面能正常展示，菜单类型为空时，返回空数据
		if (pagination == null) {
			pagination = new EUDGPagination();
			pagination.setTotal(0);
			pagination.setRows(new ArrayList<Map<String, Object>>());
		}
		return pagination;
	}

	/**
	 * 我的发起列表分页数据
	 * @param params
	 * @param rowBounds
	 * @return
	 */
	private EUDGPagination getPaginationForMyCreate(Map<String, Object> params, RowBounds rowBounds) {
		UserInfo userInfo = (UserInfo) params.get("userInfo");
		List<GdPersionSendFlow> list = gdPersionSendFlowMapper.getPaginationForMyCreate(params, rowBounds);
		for (GdPersionSendFlow gdPersionSendFlow : list) {
			translationByBo(gdPersionSendFlow,userInfo);
		}
		long count = gdPersionSendFlowMapper.getCountForMyCreate(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 我的待办列表分页数据
	 * @param params
	 * @param rowBounds
	 * @return
	 */
	private EUDGPagination getPaginationForMyWait(Map<String, Object> params, RowBounds rowBounds) {
		UserInfo userInfo = (UserInfo) params.get("userInfo");
		List<GdPersionSendFlow> list = gdPersionSendFlowMapper.getPaginationForMyWait(params, rowBounds);
		for (GdPersionSendFlow gdPersionSendFlow : list) {
			translationByBo(gdPersionSendFlow,userInfo);
		}
		long count = gdPersionSendFlowMapper.getCountForMyWait(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 我的经办列表分页数据
	 * @param params
	 * @param rowBounds
	 * @return
	 */
	private EUDGPagination getPaginationForMyHandle(Map<String, Object> params, RowBounds rowBounds) {
		UserInfo userInfo = (UserInfo) params.get("userInfo");
		List<GdPersionSendFlow> list = gdPersionSendFlowMapper.getPaginationForMyHandle(params, rowBounds);
		for (GdPersionSendFlow gdPersionSendFlow : list) {
			translationByBo(gdPersionSendFlow,userInfo);
		}
		long count = gdPersionSendFlowMapper.getCountForMyHandle(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 设置查询参数
	 * @param params
	 * gdPersionSendFlow：实体对象
	 * menuType 列表菜单类型  [根据菜单类型获取对应的列表数据]
	 * 		材料送达任务状态：01草稿、02流转中、03已送达、04未送达、05归档
	 * userInfo 用户信息
	 *
	 * @return
	 */
	private Map<String, Object> capQueryParam(Map<String, Object> params) {
		//当前用户 信息，用于判断是否可以操作当前工作流任务
		UserInfo userInfo = (UserInfo) params.get("userInfo");
		GdPersionSendFlow gdPersionSendFlow = (GdPersionSendFlow) params.get("gdPersionSendFlow");
		
		//查询数据去前后空格
		gdPersionSendFlow.setFlowName(StringUtils.isBlank(gdPersionSendFlow.getFlowName())?"":gdPersionSendFlow.getFlowName().trim());
		gdPersionSendFlow.setReceiveName(StringUtils.isBlank(gdPersionSendFlow.getReceiveName())?"":gdPersionSendFlow.getReceiveName().trim());
		gdPersionSendFlow.setReceiveAddr(StringUtils.isBlank(gdPersionSendFlow.getReceiveAddr())?"":gdPersionSendFlow.getReceiveAddr().trim());
				
		
		if (userInfo != null) {
			params.put("curUserId", userInfo.getUserId());
			params.put("curOrgId", userInfo.getOrgId());
			params.put("curOrgCode", userInfo.getOrgCode());
		}

		//任务状态值list，用于查询复合状态的列表
		List<String> statusList = new ArrayList<String>();
		//根据菜单类型获取菜单对应的列表
		Integer menuType = 0;
        if (CommonFunctions.isNotBlank(params, "menuType")) {
			try {
				menuType = Integer.valueOf(MENUTYPE_MAP.get((String)params.get("menuType")));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

        String status = gdPersionSendFlow.getStatus();
        if("-1".equals(status))
        	gdPersionSendFlow.setStatus("");
        
        switch (menuType) {
			case 1: {// 我的发起
				gdPersionSendFlow.setCreator(userInfo.getUserId());
				params.put("gdPersionSendFlow", gdPersionSendFlow);
				break;
			}
			case 2: {// 我的待办

				break;
			}
			case 3: {// 我的经办

				break;
			}
		}

		return params;

	}

	@Override
	public boolean saveHandleInfo(GdPersionSendFlow bo, Map<String, Object> params) throws Exception {
		UserInfo userInfo = (UserInfo) params.get("userInfo");
		String sendType = (String) params.get("sendType");
		String nextLink = (String) params.get("nextLink");
		
		//是否送达参数
		String receiveState = bo.getReceiveState();
		
		Long sendId = bo.getSendId();
		bo = gdPersionSendFlowMapper.searchById(sendId);
		
		try {
			//*****调用工作流提交新的下一流程任务
			//0：获取当前流程节点信息
			Map<String, Object> curTaskData = gdPersionSendWorkflowService.findCurTaskData(sendId,userInfo);
			String currNodeName = (String) curTaskData.get("NODE_NAME");
			
			//1：办理人员信息，及办理人员意见数据整理
			List<UserInfo> userInfoList = new ArrayList<UserInfo>();
			if("org".equals(sendType)) {//需要发送给组织的专班人员
				String sendOrgCode = (String) params.get("sendOrgCode");//根据组织和职位获取办理人员
				List<UserBO> userBOS = userManageOutService.queryUserListByPositionAndOrg("专班",null,"000",null,sendOrgCode);
				if(userBOS.size() <= 0){
					throw new Exception("001");//:该组织下没有配置【专班】职位人员
				}
				for (UserBO userBO : userBOS) {
					UserInfo user = new UserInfo();
					user.setUserId(userBO.getUserId());
					user.setOrgId(Long.valueOf(userBO.getSocialOrgId()));
					user.setOrgCode(userBO.getOrgCode());
					user.setPartyName(userBO.getPartyName());
					user.setOrgName(userBO.getOrgName());
					userInfoList.add(user);
				}
			}else if("person".equals(sendType)) {//发送给网格员的
				String handlerInfo = (String) params.get("handlerInfoJson");//办理人员
				if(!StringUtils.isBlank(handlerInfo)) {
					ObjectMapper mapper = new ObjectMapper();
					userInfoList = mapper.readValue(handlerInfo,new TypeReference<List<UserInfo>>(){});
				}
			}
			
			Map<String, Object> extraParam = new HashMap<>();//办理意见
			extraParam.put("advice", params.get("advice"));
			
			//2：工作流决策环节（根据用户选择项，设置工作流的下一环节以及任务状态）
			String nextNodeName = "";
			String sendStatus = "";
			
			/****01草稿、02流转中、03已送达、04未送达、05归档****/
			//1:流程状态是流转中，处于区专班 ，镇专班，村专班,网格员办理环节（task2,task3,task4,task5）===》送达状态修改为流转中，流转到下一环节，
			boolean flag1 = "02".equals(bo.getStatus()) && ("task2,task3,task4".indexOf(currNodeName)>-1);
			//2:流程状态是流转中，处于网格员办理环节（task5）===》流程状态修改为03已送达、04未送达，流转到下一环节法院；
			boolean flag2 = "02".equals(bo.getStatus()) && "task5".equals(currNodeName);
			//3:流程状态是03已送达或04未送达，处于法院办理环节（task6）===》流程状态修改为归档，流转到结束
			boolean flag3 = ("03,04".indexOf(bo.getStatus())>-1) && "task6".equals(currNodeName);
			
			nextNodeName = nextLink;
			if(flag1) {//处于流转中的时候下一环节
				sendStatus = "02";
			}else if (flag2){//下一环节流转到法院
				bo.setReceiveState(receiveState);
				sendStatus = "1".equals(receiveState)?"03":"04";
				userInfoList = getCourtUserInfoList(bo,userInfo);
			}else if (flag3){
				sendStatus = "05";
				nextNodeName = "end1";//流程结束环节
				userInfoList = null;
			}
			boolean subflag = gdPersionSendWorkflowService.subWorkflow4Case(sendId, nextNodeName, userInfoList, userInfo, extraParam);

			//工作流提交成功修改任务状态
			if(subflag) {
				//提交成功后的当前环节信息
				Map<String, Object> curTaskDataForSub = gdPersionSendWorkflowService.findCurTaskData(sendId,userInfo);
				String currLinkName = (String) curTaskDataForSub.get("WF_ACTIVITY_NAME_");
				String currOrgId = (String) curTaskDataForSub.get("WF_ORGID");
				String currOrgCode = "";
				if(!StringUtils.isBlank(currOrgId))
					currOrgCode = orgSocialInfoOutService.findByOrgId(Long.valueOf(currOrgId)).getOrgCode();
				String currOrgName = (String) curTaskDataForSub.get("WF_ORGNAME");
				String currUserName = (String) curTaskDataForSub.get("WF_USERNAME");
				
				//设置更新的参数
				bo.setCurrentCode(currOrgCode);
				bo.setCurrentCodeCn(currOrgName);
				bo.setCurrentHandler(currUserName);
				bo.setCurrentNode(currLinkName);
				bo.setStatus(sendStatus);
				bo.setUpdator(userInfo.getUserId());
				bo.setAdvice((String)params.get("advice"));
				this.update(bo);

				if (flag2){
					if(APPLY_TYPE_SQTJ.equals(bo.getSendType())){
						Court court = new Court();
						String orgCode = userInfo.getOrgCode();
						String orgName = userInfo.getOrgName();
						if(userInfo.getOrgCode().length() > 12){
							orgCode = orgCode.substring(0,12);
							orgName = orgSocialInfoOutService.findOrgNameByOrgCode(orgCode);
						}
						court.setInfoOrgCode(orgCode);
						court.setInfoOrgName(orgName);
						court.setCreator(userInfo.getUserId());
						court.setUpdator(userInfo.getUserId());
						court.setRecordDate(new Date());
						court.setRecordDateStr(DateUtils.formatDate(new Date(),DateUtils.PATTERN_DATE));
						//参与法院诉前调解化解案件 为 4 必填
						court.setFgvcType("4");
						court.setPlaintiff(bo.getPlaintiff());
						court.setDefendant(bo.getDefendant());
						court.setCaseNo(bo.getCaseNo());
						court.setCaseReason(bo.getCaseReason());
						try{
							Long id = courtService.insert(court);
							System.out.println(id);
						}catch (Exception e){
							e.printStackTrace();
						}
					}
				}

			}else {
				return false;
			}
			
		}catch (Exception e) {
			String msg = e.getMessage();
			if("001".equals(msg))
				throw new Exception("该组织下没有配置【专班】职位人员");//:
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public int reject(GdPersionSendFlow bo, UserInfo userInfo) {
		try {
			Map<String,Object> params = new HashMap<>(1);
			params.put("advice",bo.getAdvice());
			//流程驳回
			boolean flag = gdPersionSendWorkflowService.rejectWorkflow4Case(bo.getSendId(),"", userInfo, params);
			if(flag){
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	private List<UserInfo> getCourtUserInfoList(GdPersionSendFlow bo,UserInfo userInfo) {
		
		//法院的办理人员就是原来的发起人
		UserBO userBO = userManageOutService.getUserInfoByUserId(bo.getCreator());
		ProInstance proInstance=null;
		try {
			proInstance = gdPersionSendWorkflowService.getProInstanceById(bo.getSendId(), userInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<UserInfo> userInfoList = new ArrayList<>();
		UserInfo user = new UserInfo();
		user.setUserId(userBO.getUserId());
		user.setOrgId(proInstance.getOrgId());
		user.setOrgCode(userBO.getOrgCode());
		user.setPartyName(userBO.getPartyName());
		user.setOrgName(userBO.getOrgName());
		userInfoList.add(user);
		
		return userInfoList;
	}

	//===========================================张祯海  end==============================================//

}