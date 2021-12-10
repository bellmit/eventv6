package cn.ffcs.zhsq.publicAppeal.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.publicAppeal.PublicAppeal;
import cn.ffcs.zhsq.mybatis.persistence.PublicAppeal.PublicAppealMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Description: 公众诉求模块服务实现
 * @Author: zhongshm
 * @Date: 09-01 15:32:03
 * @Copyright: 2017 福富软件
 */
@Service("publicAppealServiceImpl")
@Transactional
public class PublicAppealServiceImpl implements PublicAppealService {

	@Autowired
	private PublicAppealMapper publicAppealMapper; // 注入公众诉求模块dao
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private IAttachmentService attachmentService;
	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;

	@Autowired
	private IBaseDictionaryService baseDictionaryService;

	private final String PUBLIC_APPEAL_WORKFLOW_NAME = "民众诉求流程", PUBLIC_APPEAL_WFTYPE_ID = "public_appeal",
			URGENCY_DEGREE = "01", INFLUENCE_DEGREE = "01",ATTMENT_PUBLIC_APPEAL = "PUBLIC_APPEAL",
		            ATTMENT_ZHSQ_EVENT = "ZHSQ_EVENT";;

	private String url = "http://mq.sq.aishequ.org";
	private String img_url = "http://img2.sq.aishequ.org";

	@Override
	public Long saveAndReport(PublicAppeal bo, UserInfo userInfo,Map<String,Object> params) {
		bo.setHandleSit(ConstantValue.PUBLIC_APPEAL_HANDLESIT_02);
		EventDisposal eventDisposal = new EventDisposal();
		eventDisposal.setEventName(bo.getAppealTitle());// 标题
		eventDisposal.setContent(bo.getContent());// 内容
		eventDisposal.setHappenTimeStr(bo.getAppealTimeStr());//
		eventDisposal.setContactUser(bo.getUserName());// 诉求人
		eventDisposal.setTel(bo.getPhone());// 手机号
		if(CommonFunctions.isNotBlank(params, "eventType")) {
			eventDisposal.setType(params.get("eventType").toString());
		}else {
			eventDisposal.setType("0702");
		}
		eventDisposal.setBizPlatform("202");
		eventDisposal.setGridCode(bo.getOrgCode());
		eventDisposal.setStatus(ConstantValue.EVENT_STATUS_DRAFT);
		eventDisposal.setUrgencyDegree(URGENCY_DEGREE);
		eventDisposal.setInfluenceDegree(INFLUENCE_DEGREE);
		eventDisposal.setInvolvedNum("00");
		eventDisposal.setSource("04");
		eventDisposal.setCollectWay("02");
		eventDisposal.setOccurred(bo.getOrgEntityPath());
		Long eventId = eventDisposalService.saveEventDisposalReturnId(eventDisposal, userInfo);
		System.out.println(eventId);
		List<Attachment> attachmentList = attachmentService.findByBizId(bo.getAppealId(), ATTMENT_PUBLIC_APPEAL);
        for(Attachment attachment : attachmentList){
            attachment.setBizId(eventId);
            attachment.setEventSeq("1");//处理前
            attachment.setAttachmentType(ATTMENT_ZHSQ_EVENT);
            attachmentService.saveAttachment(attachment);
        }
		
		// if(null == eventId || eventId.equals(-1L)) return convertToXmlResult("0",
		// "事件保存失败！");
		// 获取workflowId
		Long workflowId = eventDisposalWorkflowService.capWorkflowId(null, eventId, userInfo, null);
		String instanceId = "";
		// 启动事件
		if (workflowId != null && workflowId > 0) {
			Map<String, Object> extraParam = new HashMap<String, Object>();
			// extraParam.put("advice", eventResult.getEvent().getAdvice());
			try {
				instanceId = eventDisposalWorkflowService.startFlowByWorkFlow(eventId, workflowId,
						ConstantValue.WORKFLOW_IS_NO_CLOSE, userInfo, extraParam);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(instanceId);
		}
		Long taskId = eventDisposalWorkflowService.curNodeTaskId(Long.valueOf(instanceId));
		try {
			eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(Long.valueOf(instanceId),
					String.valueOf(taskId), ConstantValue.ACCEPT_TASK_CODE, userInfo.getUserId().toString(),
					userInfo.getOrgId().toString(), "", userInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// if(StringUtils.isBlank(instanceId)) return convertToXmlResult("0",
		// "事件启动失败！");
		this.saveEventDataExchangeStatus("202", bo.getOutId().toString(), String.valueOf(eventId), "");
		bo.setEventId(eventId);
		if(CommonFunctions.isNotBlank(params, "isPub")) {
			bo.setIsPub(params.get("isPub").toString());
		}else {
			bo.setIsPub("1");
		}
		this.update(bo);
		return eventId;
	}

	@Autowired
	private IDataExchangeStatusService dataExchangeStatusService;

	/**
	 * 保存事件中间记录
	 * 
	 * @param bizPlatform
	 * @param oppo
	 * @param own
	 * @param xmlData
	 * @return
	 */
	private Boolean saveEventDataExchangeStatus(String bizPlatform, String oppo, String own, String xmlData) {
		DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
		dataExchangeStatus.setDestPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		dataExchangeStatus.setSrcPlatform(bizPlatform);
		dataExchangeStatus.setOppoSideBizCode(oppo);
		dataExchangeStatus.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		dataExchangeStatus.setOwnSideBizCode(own);
		dataExchangeStatus.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		dataExchangeStatus.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
		dataExchangeStatus.setStatus(IDataExchangeStatusService.STATUS_VALIDATE);
		dataExchangeStatus.setXmlData(xmlData);
		return dataExchangeStatusService.saveDataExchangeStatus(dataExchangeStatus) > 0;
	}

	/**
	 * 新增数据
	 * 
	 * @param bo
	 *            业务对象
	 * @return 结果
	 */
	@Override
	public Long insert(PublicAppeal bo) throws IOException {
		publicAppealMapper.insert(bo);
		
		String updateUrl = funConfigurationService.changeCodeToValue("CMD_PATH_CFG", "atts_url",
				IFunConfigurationService.CFG_TYPE_FACT_VAL);
		if (StringUtils.isNotBlank(updateUrl)) {

			String params = "bizId=" + bo.getOutId();

			String s = HttpUtil.doPost(updateUrl, params);

			List<Attachment> attachments = new ArrayList<Attachment>();
			List<Map<String, Object>> attrs = new ArrayList<Map<String, Object>>();
			String imgurlConfig = funConfigurationService.changeCodeToValue("CMD_PATH_CFG", "IMG_URL",
					IFunConfigurationService.CFG_TYPE_FACT_VAL,bo.getOrgCode());
			if(StringUtils.isNotBlank(imgurlConfig)) {
				img_url=imgurlConfig;
			}
			if (StringUtils.isNotBlank(s)) {
				JSONObject jsonObject = JSONObject.fromObject(s);
				if (jsonObject != null) {
					if (null != jsonObject.get("attrs")) {
						JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("attrs"));
						Iterator iterator = jsonArray.iterator();
						while (iterator.hasNext()) {
							Map<String, Object> map = new HashMap<String, Object>();
							JSONObject next = (JSONObject) iterator.next();
							String fileName = (String) next.get("fileName");
							String filePath = (String) next.get("filePath");
							map.put("fileName", fileName);
							map.put("filePath", img_url + filePath);
							attrs.add(map);
							System.out.println("开始获取流："+img_url + filePath);
							InputStream inputStream = HttpUtil.returnBitMap(img_url + filePath);
							byte[] bytes = HttpUtil.readInputStream(inputStream);
							String imgUrl = fileUploadService.uploadSingleFile(fileName, bytes, "PUBLIC_APPEAL",
									"perfile");
							System.out.println("上传图片："+imgUrl);
							Attachment attachment = new Attachment();
							attachment.setBizId(bo.getAppealId());
							attachment.setAttachmentType("PUBLIC_APPEAL");
							attachment.setEventSeq("1");
							attachment.setFilePath(imgUrl);
							attachment.setFileName(fileName);
							attachments.add(attachment);
						}
						attachmentService.saveAttachment(attachments);
						bo.setAttrs(attrs);
					}
				}
			}
		}
		return bo.getAppealId();
	}

	/**
	 * 修改数据
	 * 
	 * @param bo
	 *            业务对象
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean update(PublicAppeal bo) {
		long result = publicAppealMapper.update(bo);
		url = funConfigurationService.changeCodeToValue("CMD_PATH_CFG", "URL",
				IFunConfigurationService.CFG_TYPE_FACT_VAL);

		PublicAppeal publicAppeal = this.searchById(bo.getAppealId());
		Long appealId = publicAppeal.getOutId();
		String handleSit = bo.getHandleSit();
		String handleRs = bo.getHandleRs();
		String rep_org_name = bo.getRep_org_name();
		String rep_time = bo.getRep_time();

		String updateUrl = url + "";
		String params = "appealId=" + appealId + "&handleSit=" + handleSit;
		if (StringUtils.isNotBlank(handleRs)) {
			params = params + "&handleRs=" + handleRs;
		}
		if (StringUtils.isNotBlank(rep_org_name)) {
			params = params + "&repOrgName=" + rep_org_name;
		}
		if (StringUtils.isNotBlank(rep_time)) {
			params = params + "&repTime=" + rep_time;
		}

		String s = HttpUtil.doPost(updateUrl, params);
		System.out.println(s);

		return result > 0;
	}

	/**
	 * 删除数据
	 * 
	 * @param bo
	 *            业务对象
	 * @return 结果
	 */
	@Override
	public boolean delete(PublicAppeal bo) {
		long result = publicAppealMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * 
	 * @param params
	 *            查询参数
	 * @return 数据列表
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, UserInfo userInfo, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		Integer listType = 1;
		Long count = 0L;
		List<PublicAppeal> publicAppealList = null;
		String userOrgCode = "", orgCode = "";
		Long userId = -1L, orgId = -1L, curUserId = -1L, curOrgId = -1L;

		if (userInfo != null) {
			userId = userInfo.getUserId();
			orgId = userInfo.getOrgId();
			orgCode = userInfo.getOrgCode();
		}
		if (CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if (CommonFunctions.isNotBlank(params, "userOrgCode")) {
			userOrgCode = params.get("userOrgCode").toString();
		}
		if (StringUtils.isBlank(userOrgCode) && StringUtils.isNotBlank(orgCode)) {
			userOrgCode = orgCode;
		}

		switch (listType) {
		case 1: {// 草稿
			params.put("createUser", userId);
			//params.put("handleSit", "1");// 待审核--万宁诉求页面看到所有的诉求（不局限于待审核的状态）
			String isOpenRestriction = funConfigurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE,ConstantValue.OPEN_RESTRICTION, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);
			if(StringUtils.isNotBlank(isOpenRestriction)) {
				if(Boolean.valueOf(isOpenRestriction)) {
					params.put("orgCode", userInfo.getOrgCode());
				}
			}
			count = publicAppealMapper.countList(params);
			publicAppealList = publicAppealMapper.searchList(params, rowBounds);

			break;
		}
		case 3: {// 辖区所有
			// params.put("statusArray", new String[] {"2"});

			if (CommonFunctions.isBlank(params, "infoOrgCode")
					&& CommonFunctions.isNotBlank(params, "startInfoOrgCode")) {
				params.put("infoOrgCode", params.get("startInfoOrgCode"));
			}

			count = publicAppealMapper.countList(params);
			publicAppealList = publicAppealMapper.searchList(params, rowBounds);

			break;
		}
		case 2: {// 待办
			if (CommonFunctions.isNotBlank(params, "curUserId")) {
				try {
					curUserId = Long.valueOf(params.get("curUserId").toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			if (CommonFunctions.isNotBlank(params, "curOrgId")) {
				try {
					curOrgId = Long.valueOf(params.get("curOrgId").toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}

			if (curUserId < 0 && userId > 0) {
				curUserId = userId;
			}
			if (curOrgId < 0 && orgId > 0) {
				curOrgId = orgId;
			}

			// params.put("statusArray", new String[] {"2"});
			params.put("curUserId", curUserId.toString());
			params.put("curOrgId", curOrgId.toString());

			if (CommonFunctions.isBlank(params, "workflowName")) {
				params.put("workflowName", PUBLIC_APPEAL_WORKFLOW_NAME);
			}
			if (CommonFunctions.isBlank(params, "wfTypeId")) {
				params.put("wfTypeId", PUBLIC_APPEAL_WFTYPE_ID);
			}

			count = publicAppealMapper.findTodoCountByCriteria(params);
			publicAppealList = publicAppealMapper.findTodoListByCriteria(params, rowBounds);

			break;
		}

		case 4: {// 经办
			if (CommonFunctions.isNotBlank(params, "curUserId")) {
				try {
					curUserId = Long.valueOf(params.get("curUserId").toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			if (CommonFunctions.isNotBlank(params, "curOrgId")) {
				try {
					curOrgId = Long.valueOf(params.get("curOrgId").toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}

			if (curUserId < 0 && userId > 0) {
				curUserId = userId;
			}
			if (curOrgId < 0 && orgId > 0) {
				curOrgId = orgId;
			}

			// params.put("statusArray", new String[] {"2"});
			params.put("curUserId", curUserId.toString());
			params.put("curOrgId", curOrgId.toString());

			if (CommonFunctions.isBlank(params, "workflowName")) {
				params.put("workflowName", PUBLIC_APPEAL_WORKFLOW_NAME);
			}
			if (CommonFunctions.isBlank(params, "wfTypeId")) {
				params.put("wfTypeId", PUBLIC_APPEAL_WFTYPE_ID);
			}

			count = publicAppealMapper.findDoneCountByCriteria(params);
			publicAppealList = publicAppealMapper.findDoneListByCriteria(params, rowBounds);

			break;
		}

		case 5: {// 我发起
			if (CommonFunctions.isNotBlank(params, "curUserId")) {
				try {
					curUserId = Long.valueOf(params.get("curUserId").toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			if (CommonFunctions.isNotBlank(params, "curOrgId")) {
				try {
					curOrgId = Long.valueOf(params.get("curOrgId").toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}

			if (curUserId < 0 && userId > 0) {
				curUserId = userId;
			}
			if (curOrgId < 0 && orgId > 0) {
				curOrgId = orgId;
			}

			// params.put("statusArray", new String[] {"2"});
			params.put("curUserId", curUserId.toString());
			params.put("curOrgId", curOrgId.toString());

			if (CommonFunctions.isBlank(params, "workflowName")) {
				params.put("workflowName", PUBLIC_APPEAL_WORKFLOW_NAME);
			}
			if (CommonFunctions.isBlank(params, "wfTypeId")) {
				params.put("wfTypeId", PUBLIC_APPEAL_WFTYPE_ID);
			}

			count = publicAppealMapper.findMyCountByCriteria(params);
			publicAppealList = publicAppealMapper.findMyListByCriteria(params, rowBounds);

			break;
		}
		}

		this.formatDataOut(publicAppealList, userOrgCode);

		EUDGPagination drugEnforcementEventPagination = new EUDGPagination(count, publicAppealList);

		return drugEnforcementEventPagination;
	}

	@Override
	public PublicAppeal searchByEventId(Long id) {
		PublicAppeal bo = publicAppealMapper.searchByEventId(id);
		List<PublicAppeal> publicAppealList=new ArrayList<PublicAppeal>();
		publicAppealList.add(bo);
		this.formatDataOut(publicAppealList, bo.getOrgCode());
		return bo;
	}

	/**
	 * 根据业务id查询数据
	 * 
	 * @param id
	 *            业务id
	 * @return 业务对象
	 */
	@Override
	public PublicAppeal searchById(Long id) {
		url = funConfigurationService.changeCodeToValue("CMD_PATH_CFG", "URL",
				IFunConfigurationService.CFG_TYPE_FACT_VAL);
		img_url = funConfigurationService.changeCodeToValue("CMD_PATH_CFG", "IMG_URL",
				IFunConfigurationService.CFG_TYPE_FACT_VAL);

		PublicAppeal bo = publicAppealMapper.searchById(id);
		String updateUrl = url + "/myAppeal/getAttsByBizId.jhtml";
		String params = "bizId=" + id;
		// String params = "bizId=64";
		// String s = HttpUtil.doPost(updateUrl, params);
		//
		// List<Map<String, Object>> attrs = new ArrayList<Map<String, Object>>();
		// JSONObject jsonObject = JSONObject.fromObject(s);
		// if(jsonObject!=null){
		// JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("attrs"));
		// Iterator iterator = jsonArray.iterator();
		// while (iterator.hasNext()){
		// Map<String, Object> map = new HashMap<String, Object>();
		// JSONObject next = (JSONObject)iterator.next();
		// String fileName = (String)next.get("fileName");
		// String filePath = (String)next.get("filePath");
		// map.put("fileName",fileName);
		// map.put("filePath",img_url + filePath);
		// attrs.add(map);
		// attrs.add(map);
		// }
		// bo.setAttrs(attrs);
		// }
		List<PublicAppeal> publicAppealList=new ArrayList<PublicAppeal>();
		publicAppealList.add(bo);
		this.formatDataOut(publicAppealList, bo.getOrgCode());
		return bo;
	}

	// 格式化输出数据
	private void formatDataOut(List<PublicAppeal> publicAppealList, String orgCode) {

		if (publicAppealList != null && publicAppealList.size() > 0) {
			List<BaseDataDict> appealTypeDictList = null, appealHandleSitDictList = null,appealSourceDictList = null;

			appealTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.APPEAL_TYPE_PCODE,orgCode);
			appealHandleSitDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.APPEAL_HANDLE_SIT_PCODE,orgCode);
			appealSourceDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.APPEAL_SOURCE_PCODE,orgCode);
			
			for (PublicAppeal PublicAppeal : publicAppealList) {
				
				// 诉求类别
				DataDictHelper.setDictValueForField(PublicAppeal, "appealCatalog", "appealCatalogStr", appealTypeDictList);
				
				// 诉求处置状态
				DataDictHelper.setDictValueForField(PublicAppeal, "handleSit", "handleSitStr", appealHandleSitDictList);
				
				// 诉求来源
				DataDictHelper.setDictValueForField(PublicAppeal, "source", "sourceStr", appealSourceDictList);
				
			}
		}

	}

}