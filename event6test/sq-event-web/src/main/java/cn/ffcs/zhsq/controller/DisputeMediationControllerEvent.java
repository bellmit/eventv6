package cn.ffcs.zhsq.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.ffcs.oa.entity.interalrule.IdRecord;
import cn.ffcs.oa.service.interalrule.IntegralRuleService;
import cn.ffcs.shequ.crypto.HashIdManager;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.EvaResult;
import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.shequ.zzgl.service.event.IEvaResultService;
import cn.ffcs.system.publicFilter.CommonController;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.base.local.service.LogModule;
import cn.ffcs.zhsq.base.local.service.IGridLogService.ActionType;
import cn.ffcs.zhsq.dispute.service.IDisputeMediationJXService;
import cn.ffcs.zhsq.dispute.service.IDisputeMediationService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeFlowInfo;
import cn.ffcs.zhsq.mybatis.domain.dispute.MediationCase;
import cn.ffcs.zhsq.utils.JsonUtils;
import cn.ffcs.zhsq.utils.ParamUtils;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.gmis.mybatis.domain.userscore.UserDetailScore;
import cn.ffcs.gmis.userscore.service.UserScoreService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.eventReportRecord.domain.EventReportRecordInfo;
import cn.ffcs.shequ.eventReportRecord.service.IEventReportRecordService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.GisMapBuidingInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.GisMapGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.GisMapSegmentGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeMediation;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;


/**
 * ??????????????????
 * @author zhongshm
 */
@Controller
@RequestMapping("/zhsq/disputeMediation")
public class DisputeMediationControllerEvent extends ZZBaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IBaseDictionaryService dictionaryService;
	/*@Autowired
	ITaskInfoService taskInfoService;*/
	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	private IInvolvedPeopleService involvedPeopleService;

	@Autowired
	protected IGisInfoService gisInfoService; // gis??????????????????
	//???????????????
	@Autowired
	private IAttachmentService attachmentService;
	@Resource(name="disputeMediationServiceImpl")
	private IDisputeMediationService disputeMediationService;
	@Autowired
	private IFunConfigurationService configurationService;
	@Autowired
	private IEventReportRecordService eventReportRecordService;
	//@Autowired
	//private IEventDisposalService eventService;

	@Autowired
	private IEventDisposalDockingService eventDisposalDockingService;

	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;

	@Autowired
	private IFunConfigurationService funConfigurationService;//????????????
	@Autowired
	private IMixedGridInfoService gridInfoService;
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;

	@Autowired
	private IEvaResultService evaResultService;

	@Autowired
	private IResMarkerService resMarkerService;

	@Autowired
	private UserScoreService userScoreService;//??????????????????

	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	@Autowired
	private UserManageOutService userManageService;
	@Autowired
	private IDisputeMediationJXService disputeMediationJXService;
	@Autowired
	private IntegralRuleService integralRuleService;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private final static String attachmentType = "dispute_attachment_type";//????????????????????????


	@RequestMapping(value = "/{type}/index")
	public String newIndex(HttpSession session,HttpServletRequest request,
			@PathVariable String type, ModelMap map,
			@RequestParam(value = "page",required = false) String page,		
			@RequestParam(value="orgId",required = false) Long orgId,
			@RequestParam(value = "location",required = false) String location) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		MixedGridInfo gridInfo = null;
		Object startGridId = defaultGridInfo.get(KEY_START_GRID_ID),
			   startGridName = defaultGridInfo.get(KEY_START_GRID_NAME),
			   InfoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		String gridId = request.getParameter("gridId");
		String infoOrgCode = request.getParameter("infoOrgCode");	
		String typeDispute = request.getParameter("typeDispute");
		
		map.addAttribute("typeDispute", typeDispute);
		
		if (StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = (String) defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		}
		if (StringUtils.isNotBlank(gridId)) {
			gridInfo = gridInfoService.findMixedGridInfoById(Long.valueOf(gridId), false);
		} else if(StringUtils.isNotBlank(infoOrgCode)) {
			gridInfo = gridInfoService.getDefaultGridByOrgCode(infoOrgCode);
		}
		map.addAttribute("disputeTypeDict", this.getDisputeTypeDict(infoOrgCode));
		map.addAttribute("disputeScaleDict",ConstantValue.DISPUTE_SCALE_CODE);
		if(gridInfo != null) {
			startGridId = gridInfo.getGridId();
			startGridName = gridInfo.getGridName();
		}
		map.addAttribute("startGridId", startGridId);
		map.addAttribute("startGridName", startGridName);
		map.addAttribute("InfoOrgCode", InfoOrgCode);

		String status = request.getParameter("status");
		if (StringUtils.isNotBlank(status)) {
			map.addAttribute("status", status);
		}
		String mediationType = request.getParameter("mediationType");
		if (StringUtils.isNotBlank(mediationType)) {
			map.addAttribute("mediationType", mediationType);
		}
		String disputeType = request.getParameter("disputeType");
		if (StringUtils.isNotBlank(disputeType)) {
			map.addAttribute("disputeType", disputeType);
		}
		String source = request.getParameter("source");
		if (StringUtils.isNotBlank(source)) {
			map.addAttribute("source", source);
		}

		String startHappenTime = request.getParameter("startHappenTime"),
			   endHappenTime = request.getParameter("endHappenTime");
		if(StringUtils.isNotBlank(startHappenTime)) {
			map.addAttribute("startHappenTime", startHappenTime);
		}
		if(StringUtils.isNotBlank(endHappenTime)) {
			map.addAttribute("endHappenTime", endHappenTime);
		}

		String startCreateTime = request.getParameter("startCreateTime"),
			   endCreateTime = request.getParameter("endCreateTime");
		if(StringUtils.isNotBlank(startCreateTime)) {
			map.addAttribute("startCreateTime", startCreateTime);
		}
		if(StringUtils.isNotBlank(endCreateTime)) {
			map.addAttribute("endCreateTime", endCreateTime);
		}

		String isSuccess = request.getParameter("isSuccess");
		if(StringUtils.isNotBlank(isSuccess)) {
			map.addAttribute("isSuccess", isSuccess);
		}
		String handleDateFlag = request.getParameter("handleDateFlag");
		if(StringUtils.isNotBlank(handleDateFlag) && StringUtils.isNotEmpty(handleDateFlag) ) {
			map.addAttribute("handleDateFlag", handleDateFlag);
		}
		String orgCode = request.getParameter("orgCode");
		map.addAttribute("orgCode",orgCode);
		map.addAttribute("orgType",request.getParameter("orgType"));
		map.addAttribute("event_url", App.EVENT.getDomain(session));
		map.addAttribute("iframeCloseCallBack", ConstantValue.closeCallBack);
		map.addAttribute("iframeUrl", App.ZZGRID.getDomain(session)+ConstantValue.iframeUrl2);
		//?????????????????????????????????????????????????????????????????????????????????????????????
		String isNewEvent = "";
		isNewEvent =  funConfigurationService.
				turnCodeToValue(ConstantValue.IS_USE_NEW_EVENT,"",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			map.addAttribute("isNewEvent", isNewEvent);

		map.addAttribute("SCOPE_INFLUNECE",ConstantValue.SCOPE_INFLUNECE);
		map.addAttribute("EVENT_NATURE",ConstantValue.EVENT_NATURE);
		map.addAttribute("createId",userInfo.getUserId());	
		String isPerfect = request.getParameter("isPerfect");
		map.addAttribute("isPerfect",isPerfect);
		if(StringUtils.isNotEmpty(infoOrgCode) && infoOrgCode.startsWith("36")){
			String eventAndDisputeMediation  = request.getParameter("eventAndDisputeMediation");
			if(StringUtils.isNotEmpty(eventAndDisputeMediation)) {
				map.addAttribute("eventAndDisputeMediation",eventAndDisputeMediation);
				return "/zzgl/dispute/mediation_9x/jiangxi/index_eventAnddispute.ftl";
			}
			return "/zzgl/dispute/mediation_9x/jiangxi/index_dispute.ftl";
		}
		return "/zzgl/dispute/mediation_9x/dafeng/index_dispute.ftl";
	}

	@ResponseBody
	@RequestMapping(value="/listData")
	public EUDGPagination listData(HttpSession session, HttpServletRequest request,
			@RequestParam(value="page") int page,
			@RequestParam(value="rows") int rows, ModelMap map,
            @RequestParam(value="isSuccess",required = false) String isSuccess,
            @RequestParam(value="mediationDeadlineStart",required = false) String mediationDeadlineStart,
            @RequestParam(value="mediationDeadlineEnd",required = false) String mediationDeadlineEnd,
            @RequestParam(value="startHappenTime",required = false) String startHappenTime,
            @RequestParam(value="endHappenTime",required = false) String endHappenTime,
            @RequestParam(value="startCreateTime",required = false) String startCreateTime,
            @RequestParam(value="endCreateTime",required = false) String endCreateTime,
            @RequestParam(value="gridId",required = false) String gridId,
            @RequestParam(value="keyWord",required = false) String keyWord,
            @RequestParam(value="infoOrgName",required = false) String infoOrgName,
            @RequestParam(value="handleDateFlag",required = false) String handleDateFlag,
            @RequestParam(value="disputeEventName",required = false) String disputeEventName,
            @RequestParam(value="addressColumn",required = false) String addressColumn,
            @RequestParam(value="itype",required = false) String itype,
            @RequestParam(value="nanchang",required = false) String nanchang,
            @RequestParam(value="status",required = false) String status,
            @RequestParam(value="createTimeStart",required = false) String createTimeStart,
            @RequestParam(value="createTimeEnd",required = false) String createTimeEnd,
            @RequestParam(value="createId",required = false) String createId,           
            @RequestParam(value="reportCase",required = false) String reportCase,
			@RequestParam(value="search",required = false) String search,
			@RequestParam(value="orgCode",required = false) String orgCode,
			@RequestParam(value="eventAndDisputeMediation",required = false) String eventAndDisputeMediation,
			@RequestParam(value="departmentAndGrid",required = false) String departmentAndGrid,
			@RequestParam(value="orgType",required = false) String orgType,
			@RequestParam(value="typeFlag",required = false) String typeFlag,
			@RequestParam(value="listStatus",required = false) String listStatus,
			@RequestParam(value="chiefLevel",required = false) String chiefLevel,
			@RequestParam(value="listOrgId",required = false) String listOrgId,
			@RequestParam(value="department",required = false) String department,
			@RequestParam(value="bizPlatform",required = false) String bizPlatform,
			@RequestParam(value="orgLevel",required = false) String orgLevel,
            @RequestParam(value="disputeCondition",required = false) String disputeCondition)
	{
        if(page <= 0) {
        	page = ConstantValue.DEFAULT_PAGE_SIZE;
        }
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		MixedGridInfo gridInfo = null;
		boolean flag = false;
		if (StringUtils.isNotBlank(gridId)) {
			gridInfo = gridInfoService.findMixedGridInfoById(Long.valueOf(gridId), false);
			flag = super.checkDataPermission(session, CommonController.TYPE_GRID, gridInfo.getInfoOrgCode());
		}
		if(StringUtils.isNotBlank(orgCode)){
			flag = super.checkDataPermission(session, CommonController.TYPE_ORG, orgCode);
		}
        Map<String, Object> params = new HashMap<String, Object>();
		String disputeTypeList = ParamUtils.getString(request, "disputeTypeList");
		if(StringUtils.isNotBlank(disputeTypeList)){
			List<String> list = Arrays.asList(disputeTypeList.split(","));
			params.put("disputeTypeList",list);
		}
		if(StringUtils.isNotBlank(handleDateFlag)){
			params.put("handleDateFlag", handleDateFlag);
		}
		if(StringUtils.isNotBlank(eventAndDisputeMediation)){params.put("eventAndDisputeMediation", eventAndDisputeMediation);}
		if(StringUtils.isNotBlank(departmentAndGrid)){			params.put("departmentAndGrid", departmentAndGrid);		}
		if(StringUtils.isNotBlank(orgType)){			params.put("orgType", orgType);		}
		if(StringUtils.isNotBlank(typeFlag)){			params.put("typeFlag", typeFlag);		}
		if(StringUtils.isNotBlank(listStatus)){			params.put("listStatus", listStatus);		}
		if(StringUtils.isNotBlank(chiefLevel)){			params.put("chiefLevel", chiefLevel);		}
		if(StringUtils.isNotBlank(listOrgId)){			params.put("listOrgId", listOrgId);		}
		if(StringUtils.isNotBlank(department)){			params.put("department", department);		}
		if(StringUtils.isNotBlank(bizPlatform)){			params.put("bizPlatform", bizPlatform);		}
		if(StringUtils.isNotBlank(orgLevel)){			params.put("orgLevel", orgLevel);		}
		
		if(StringUtils.isNotBlank(isSuccess)){
			params.put("isSuccess", isSuccess);
		}
		if(StringUtils.isNotBlank(mediationDeadlineStart)){
			params.put("mediationDeadlineStart", mediationDeadlineStart);
		}
		if(StringUtils.isNotBlank(mediationDeadlineEnd)){
			params.put("mediationDeadlineEnd", mediationDeadlineEnd);
		}
        if(!StringUtils.isBlank(keyWord)){
			int a = keyWord.indexOf("%");
			if(a>=0){
				String astr = keyWord.replaceAll("%", "/%");
				keyWord = astr;
			}
			int b = keyWord.indexOf("_");
			if(b>=0){
				String bstr = keyWord.replaceAll("_", "/_");
				keyWord = bstr;
			}
		}
		if(StringUtils.isNotBlank(keyWord)){
			params.put("keyWord", keyWord);
		}
		if(StringUtils.isNotBlank(startHappenTime)){
			params.put("startHappenTime", startHappenTime);
		}
		if(endHappenTime!=null && !"".equals(endHappenTime)){
			params.put("endHappenTime", endHappenTime);
		}

		if(StringUtils.isNotBlank(startCreateTime)){
			params.put("startCreateTime", startCreateTime);
		}
		if(StringUtils.isNotBlank(endCreateTime)){
			params.put("endCreateTime", endCreateTime);
		}
		
		if(StringUtils.isNotBlank(itype)){
			params.put("involveType", itype);
		}
		if(StringUtils.isNotBlank(addressColumn)){
			params.put("addressColumn", addressColumn);
		}
		if(StringUtils.isNotBlank(disputeCondition)){
			params.put("disputeCondition", disputeCondition);
		}
		if(StringUtils.isNotBlank(disputeEventName)){
			params.put("disputeEventName", disputeEventName);
		}
		String infoOrgCode = request.getParameter("infoOrgCode");
		if(StringUtils.isBlank(infoOrgCode)){
			params.put("infoOrgCode", gridInfo.getGridCode());
		}else{
			params.put("infoOrgCode", infoOrgCode);
			params.put("type","jx");
			flag = super.checkDataPermission(session, CommonController.TYPE_GRID, infoOrgCode);
		}
		params.put("gridId", gridId);
		String source = request.getParameter("source");
		if (StringUtils.isNotBlank(source)) {
			params.put("source", source);
		}
		if (StringUtils.isNotBlank(nanchang)) {
			params.put("nanchang", nanchang);
		}
		if(StringUtils.isNotBlank(orgCode)){
			//???????????????????????????Id
			OrgEntityInfoBO ebo = orgSocialInfoOutService.selectOrgEntityInfoByOrgCode(orgCode);//???????????????
			MixedGridInfo	orgInfo=mixedGridInfoService.getDefaultGridByOrgCode(ebo.getOrgCode());
            params.put("gridId", orgInfo.getGridId());
      //      params.put("ownLevel",true);
		}
		String mediationType = request.getParameter("mediationType");
		if (StringUtils.isNotBlank(mediationType)) {
			params.put("mediationType", mediationType);
		}
		String disputeType = request.getParameter("disputeType");
		if (StringUtils.isNotBlank(disputeType)) {
			params.put("disputeType", disputeType);
		}
		String disputeStatus = request.getParameter("disputeStatus");
		if (StringUtils.isNotBlank(disputeStatus)) {
			params.put("disputeStatus", disputeStatus);
		}
		String noDisputeStatus = request.getParameter("noDisputeStatus");
		if (StringUtils.isNotBlank(noDisputeStatus)) {
			params.put("noDisputeStatus", noDisputeStatus);
		}
		String disputeStatuss = request.getParameter("disputeStatuss");
		if (StringUtils.isNotBlank(disputeStatuss)) {
			params.put("disputeStatuss", disputeStatuss.split(","));
		}
		String disputeScale = request.getParameter("disputeScale");
		if (StringUtils.isNotBlank(disputeScale)) {
			params.put("disputeScale", disputeScale);
		}
		if (StringUtils.isNotBlank(search)) {
			params.put("search", search);
		}
		/*if (StringUtils.isNotBlank(reportCase)) {
			params.put("reportCase",reportCase);
		}*/
		if (StringUtils.isNotBlank(status)) {
			params.put("status", status);
		}
		if (StringUtils.isNotBlank(createTimeStart)) {
			params.put("createTimeStart",createTimeStart);
		}
		if (StringUtils.isNotBlank(createTimeEnd)) {
			params.put("createTimeEnd",createTimeEnd);
		}
	/*	if (StringUtils.isNotBlank(createId)) {
			params.put("createId",createId);
		}*/
		String isPerfect = request.getParameter("isPerfect");
		if (StringUtils.isNotBlank(isPerfect)) {
			params.put("isPerfect", isPerfect);
		}
		if (StringUtils.isNotBlank(infoOrgName)) {
			params.put("infoOrgName", this.getDefaultInfoOrgName(session));
			return disputeMediationService.findDisputeAndZeroPagination(page, rows, params);
		}
		String o = (String) defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		if(StringUtils.isNotEmpty(o) && o.startsWith("36")){
			params.put("orgCode", orgCode);
			params.put("orgType",request.getParameter("orgType"));
			params.put("type","jx");
			EUDGPagination disputePagination = new EUDGPagination();
			if(flag){
				disputePagination = disputeMediationJXService.findDisputePagination(page, rows, params);
				if(disputePagination.getRows() != null){
					List<DisputeMediation> rows1 = (List<DisputeMediation>) disputePagination.getRows();
					rows1.forEach(in->{
						in.setHashId(HashIdManager.encrypt(in.getMediationId()));
					});
				}
			}
			return disputePagination;
		}
		return  disputeMediationService.findDisputePagination(page, rows, params);
	}

	/**
	 * ??????
	 * Sep 19, 2014
	 * 9:54:40 AM
	 * @param map
	 */
	public void getDictional(ModelMap map, String orgCode){
		List<BaseDataDict> disputeType= dictionaryService.getDataDictListOfSinglestage(ConstantValue.DISPUTE_TYPE_CODE, orgCode);
        List<BaseDataDict> mediationTypeDC= dictionaryService.getDataDictListOfSinglestage(ConstantValue.MEDIATION_TYPE_CODE, orgCode);
        List<BaseDataDict> disputeScaleDC= dictionaryService.getDataDictListOfSinglestage(ConstantValue.DISPUTE_SCALE_CODE, orgCode);
        List<BaseDataDict> cardTypeDC= dictionaryService.getDataDictListOfSinglestage("D030001", orgCode);
        List<BaseDataDict> peopleTypeDC= dictionaryService.getDataDictListOfSinglestage("B589", orgCode);
        List<BaseDataDict> nationDC= dictionaryService.getDataDictListOfSinglestage("B162", orgCode);
        List<BaseDataDict> eduDC= dictionaryService.getDataDictListOfSinglestage("B064", orgCode);
        
        List<BaseDataDict> FXLXDM= dictionaryService.getDataDictListOfSinglestage("LM90112", null);
        List<BaseDataDict> FXDJDM= dictionaryService.getDataDictListOfSinglestage("LM10017", null);

        List<BaseDataDict> disputeType_9x = new ArrayList<BaseDataDict>();
        List<BaseDataDict> disputeType9x = dictionaryService.getDataDictListOfSinglestage(getDisputeTypeDict(orgCode), orgCode);
        disputeType_9x.addAll(disputeType9x);
        for(BaseDataDict baseDataDict : disputeType9x){
            List<BaseDataDict> disputeTypeSubs = dictionaryService.getDataDictListOfSinglestage(baseDataDict.getDictCode(), orgCode);
            disputeType_9x.addAll(disputeTypeSubs);
        }

        List<BaseDataDict> peopleTypeDC_9x = new ArrayList<BaseDataDict>();
        List<BaseDataDict> peopleTypeDC9x = dictionaryService.getDataDictListOfSinglestage("B416", orgCode);
        peopleTypeDC_9x.addAll(peopleTypeDC9x);
        for(BaseDataDict baseDataDict : peopleTypeDC9x){
            List<BaseDataDict> peopleTypeSubs = dictionaryService.getDataDictListOfSinglestage(baseDataDict.getDictCode(), orgCode);
            peopleTypeDC_9x.addAll(peopleTypeSubs);
        }

        List<BaseDataDict> mediationTypeDC_9x= new ArrayList<BaseDataDict>();
        List<BaseDataDict> mediationTypeDC9x= dictionaryService.getDataDictListOfSinglestage("B417", orgCode);
        mediationTypeDC_9x.addAll(mediationTypeDC9x);
        for(BaseDataDict baseDataDict : mediationTypeDC9x){
        	List<BaseDataDict> mediationTypeSubs = dictionaryService.getDataDictListOfSinglestage(baseDataDict.getDictCode(), orgCode);
        	mediationTypeDC_9x.addAll(mediationTypeSubs);
        }
        
        
        List<BaseDataDict> caseAssessment= dictionaryService.getDataDictListOfSinglestage("DM94696", orgCode);//????????????
        List<BaseDataDict> intenseDegree= dictionaryService.getDataDictListOfSinglestage("DM94697", orgCode);//????????????
        List<BaseDataDict> disputeLevel= dictionaryService.getDataDictListOfSinglestage("DM94698", orgCode);//????????????
        List<BaseDataDict> warningLevel= dictionaryService.getDataDictListOfSinglestage("DM94699", orgCode);//????????????
        List<BaseDataDict> disputeType3= dictionaryService.getDataDictListOfSinglestage("DM01588", orgCode);//??????????????????
        
        
     
        map.addAttribute("FXLXDM",FXLXDM);
        map.addAttribute("FXDJDM",FXDJDM);
        map.addAttribute("caseAssessment",caseAssessment);
        map.addAttribute("intenseDegree",intenseDegree);
        map.addAttribute("disputeLevel",disputeLevel);
        map.addAttribute("warningLevel",warningLevel);
        map.addAttribute("disputeType3",disputeType3);

        
        map.addAttribute("disputeType_9x",disputeType_9x);
        map.addAttribute("peopleTypeDC_9x",peopleTypeDC_9x);
        map.addAttribute("mediationTypeDC_9x",mediationTypeDC_9x);

        map.addAttribute("eduDC",eduDC);
        map.addAttribute("cardTypeDC",cardTypeDC);
        map.addAttribute("peopleTypeDC",peopleTypeDC);
        map.addAttribute("nationDC",nationDC);
		map.addAttribute("disputeScaleDC",disputeScaleDC);
        map.addAttribute("mediationTypeDC",mediationTypeDC);
		map.addAttribute("disputeType", disputeType);
	}

	@RequestMapping(value="/{type}/create")
	public String createNewFlow(HttpSession session,HttpServletRequest request,
			@PathVariable String type, ModelMap map,
								@RequestParam(value = "location",required = false) String location) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("userInfo", userInfo);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String orgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		map.addAttribute("disputeTypeDict", this.getDisputeTypeDict(orgCode));
		DisputeMediation disputeMediation = new DisputeMediation();
		//????????????????????????
		//disputeMediation.setMediationCode(disputeMediationService.getMediationCode());
		disputeMediation.setGridName(defaultGridInfo.get(KEY_START_GRID_NAME).toString());
		disputeMediation.setGridId(Long.valueOf(defaultGridInfo.get(KEY_START_GRID_ID).toString()));
		Date date = new Date();
		disputeMediation.setAcceptedDateStr(DateUtils.formatDate(date, "yyyy-MM-dd"));
		disputeMediation.setHappenTimeStr(DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
		disputeMediation.setAcceptedDate(date);
		disputeMediation.setHappenTime(date);
		map.addAttribute("disputeMediation",disputeMediation);

		InvolvedPeople involvedPeople = new InvolvedPeople();
		involvedPeople.setCardType("1");//??????????????????????????????
		involvedPeople.setNation("??????");//????????????
		map.addAttribute("involvedPeople",involvedPeople);
		map.addAttribute("GEO_URL", App.GEO.getDomain(session));//?????????????????????

        getDictional(map,orgCode);
        map.addAttribute("DISPUTE_SCALE_CODE",ConstantValue.DISPUTE_SCALE_CODE);
        map.addAttribute("DISPUTE_TYPE_CODE",ConstantValue.DISPUTE_TYPE_CODE);

		map.addAttribute("SCOPE_INFLUNECE",ConstantValue.SCOPE_INFLUNECE);
		map.addAttribute("EVENT_NATURE",ConstantValue.EVENT_NATURE);
        map.addAttribute("MEDIATION_TYPE_CODE",ConstantValue.MEDIATION_TYPE_CODE);
        //????????????
        map.addAttribute("fileDomain", App.SQFILE.getDomain(request.getSession()));
        map.addAttribute("imgDomain", App.IMG.getDomain(request.getSession()));
		//-- ?????????????????????
//		dispute.setLatitude(orgExtraInfo.getLatitude());
//		dispute.setLongitude(orgExtraInfo.getLongitude());

		map.addAttribute("editAble", "");
		map.addAttribute("bizType", ConstantValue.DISPUTE_ATTACHMENT_TYPE);

		// ??????????????????
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));

		String orgCodeStr = ConstantValue.GANSU_FUNC_ORG_CODE;
		String[] orgCodes = orgCodeStr.split(",");
		String result = "";
		for(int i=0; i<orgCodes.length; i++){
			if(orgCode.indexOf(orgCodes[i])==0){
				result = "gansu";
			}
		}
		map.addAttribute("result", result);
		map.addAttribute("GIS_MAP_GANSU_URL", App.EVENT.getDomain(session));
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));

		map.addAttribute("module", ConstantValue.DISPUTE_MEDIATION); // ????????????
		map.addAttribute("SQ_FILE_URL", App.SQFILE.getDomain(session)); //
		map.addAttribute("IMG_URL", App.IMG.getDomain(session)); //
		map.addAttribute("COMPONENTS_DOMAIN",App.COMPONENTS.getDomain(session));
		map.addAttribute("skyDomain",App.SKY.getDomain(session));
		map.addAttribute("uiDomain",App.UI.getDomain(session));
		map.addAttribute("itype", request.getParameter("itype"));
		String displayTaiwangFlag = configurationService.changeCodeToValue(ConstantValue.DISPLAY_TAIWANG_FLAG,"",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode());
		map.addAttribute("displayTaiwangFlag", displayTaiwangFlag);

		map.addAttribute("SCOPE_INFLUNECE",ConstantValue.SCOPE_INFLUNECE);
		map.addAttribute("EVENT_NATURE",ConstantValue.EVENT_NATURE);
		return "/zzgl/dispute/mediation_9x/dafeng/create_dispute.ftl";
	
	}

	
	/**
	 * ??????????????????
	 * ????????????????????????
	 * */
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResultObj save(
			HttpSession session,
			HttpServletRequest request,
            @ModelAttribute(value = "disputeMediation") DisputeMediation disputeMediation,
            ModelMap map
            ,@RequestParam(value="areaType", required=false) String areaType
			,@RequestParam(value="wid", required=false) Long wid
			,@RequestParam(value="mapt", required=false) Integer mapt
			,@RequestParam(value="x", required=false) Float x
			,@RequestParam(value="y", required=false) Float y
			,@RequestParam(value="levnum", required=false) String levnum
			,@RequestParam(value="hs", required=false) String hs
			,@RequestParam(value="color", required=false) String color
			,@RequestParam(value="mapCenterLevel", required=false) Integer mapCenterLevel
			,@RequestParam(value="gjColor", required=false) String gjColor
			) throws Exception {
		if(!StringUtils.isBlank(request.getParameter("resMarker.x"))){
			x =Float.valueOf(request.getParameter("resMarker.x"));
		}
		if(!StringUtils.isBlank(request.getParameter("resMarker.y"))){
			y =Float.valueOf(request.getParameter("resMarker.y"));
		}
		if(!StringUtils.isBlank(request.getParameter("resMarker.mapType"))){
			mapt =Integer.valueOf(request.getParameter("resMarker.mapType"));
		}
		String isPerfect = request.getParameter("isPerfect");
		String eventSeq = request.getParameter("eventSeq");
		ResultObj resultObj= new ResultObj("", "");
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		int recordId = 0;
		Long mediationId = 0L;
		try {
			disputeMediation.setInvolveType(disputeMediation.getInvolveType() == null ? "":disputeMediation.getInvolveType());
			if(disputeMediation.getMediationId() != null){
				disputeMediation.setUpdateId(userInfo.getUserId().intValue());
				if(!"1".equals(disputeMediation.getDisputeStatus())){
					disputeMediation.setMediationCode(disputeMediationService.getMediationCode());
				}
				if (disputeMediation.getInvolvedPeople() != null && disputeMediation.getInvolvedPeople().size() > 0) {
					involvedPeopleService.deleteInvolvedPeopleByBiz(disputeMediation.getMediationId(), InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
					for (InvolvedPeople people : disputeMediation.getInvolvedPeople()) {
						if(StringUtils.isBlank(people.getIdCard())){
							continue;
						}
						wid = disputeMediation.getMediationId();
						people.setBizType(InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
						people.setBizId(disputeMediation.getMediationId());
						Long record = involvedPeopleService.insertInvolvedPeople(people, false);
					}
				}
				
				recordId = disputeMediationService.updateByPrimaryKeySelective(disputeMediation);
				try {
					if (disputeMediation.getAttList() != null && disputeMediation.getAttList().size() > 0) {
						List<Attachment> newList = new ArrayList<>();
						int a = attachmentService.deleteByBizId(disputeMediation.getMediationId(),attachmentType);
						for (Attachment att : disputeMediation.getAttList()) {
							if(att.getFilePath() == null){
								continue;
							}
							att.setAttachmentType(attachmentType);
							att.setBizId(disputeMediation.getMediationId());
							att.setCreateTime(new Date());
							att.setCreatorId(userInfo.getUserId());
							att.setCreatorName(userInfo.getUserName());
							att.setEventSeq(eventSeq);
							newList.add(att);
						}
						attachmentService.saveAttachment(newList);
					}
				} catch (Exception e) {
					map.put("msg", "????????????");
				}
				/*if(recordId > 0){
					involvedPeopleService.deleteInvolvedPeopleByBiz(disputeMediation.getMediationId(), InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
				}*/
				resultObj = Msg.OPERATE.getResult(recordId > 0, disputeMediation.getMediationId());
				map.addAttribute("result", recordId > 0 ? "???????????????" : "???????????????");
			}else{
				disputeMediation.setCreatorId(userInfo.getUserId());
				disputeMediation.setOrgId(userInfo.getOrgId());
				mediationId = disputeMediationService.insertDisputeMediation(disputeMediation);
				wid = mediationId;
				if (disputeMediation.getInvolvedPeople() != null && disputeMediation.getInvolvedPeople().size() > 0) {
					for (InvolvedPeople people : disputeMediation.getInvolvedPeople()) {
						if(StringUtils.isBlank(people.getIdCard())){
							continue;
						}
						people.setBizType(InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
						people.setBizId(mediationId);
						Long record = involvedPeopleService.insertInvolvedPeople(people, false);
					}
				}
				disputeMediation.setMediationId(mediationId);
				try {
					if (disputeMediation.getAttList() != null && disputeMediation.getAttList().size() > 0) {
						List<Attachment> newList = new ArrayList<>();
						for (Attachment att : disputeMediation.getAttList()) {
							if(att.getFilePath() == null){
								continue;
							}
							att.setAttachmentType(attachmentType);
							att.setBizId(mediationId);
							att.setCreateTime(new Date());
							att.setCreatorId(userInfo.getUserId());
							att.setCreatorName(userInfo.getUserName());
							att.setEventSeq(eventSeq);
							newList.add(att);
						}
						attachmentService.saveAttachment(newList);
					}
				} catch (Exception e) {
					map.put("msg", "????????????");
				}
				resultObj = Msg.OPERATE.getResult(mediationId > 0, mediationId);
				/*if(ids!=null && ids.length()>0){
					attachmentService.updateBizId(disputeMediation.getMediationId(), ConstantValue.DISPUTE_ATTACHMENT_TYPE, ids);
				}*/
				map.addAttribute("result", mediationId > 0 ? "???????????????" : "???????????????");
			}
			if(!StringUtils.isBlank(disputeMediation.getDisputeStatus())){
				if(disputeMediation.getDisputeStatus().equals(ConstantValue.DISPUTESTATUS_REPORT)&&!"0".equals(isPerfect)){
					
					//report event
					EventDisposal event = new EventDisposal();
					if(!"1".equals(disputeMediation.getDisputeStatus())){
						disputeMediation.setMediationCode(disputeMediationService.getMediationCode());
					}
					//??????????????????????????????????????????
//					String disputeEventType = disputeMediationService.getDisputeEventType(userInfo, null);
//					if(!StringUtils.isBlank(disputeEventType)){
//						event.setType(disputeEventType);
//						//event.setType(ConstantValue.DISPUTE_TYPE);
//					}

					String disputeEventType= disputeMediationService.getDisputeEventType(userInfo, disputeMediation.getDisputeType2());//??????????????????????????????????????????
					if(!StringUtils.isBlank(disputeEventType)){
						event.setType(disputeEventType);
					}

					event.setContent(disputeMediation.getDisputeCondition());
					event.setTel(userInfo.getVerifyMobile());
					event.setGridId(disputeMediation.getGridId());
					event.setEventName(disputeMediation.getDisputeEventName());
					event.setHappenTimeStr(disputeMediation.getHappenTimeStr());
					event.setOccurred(disputeMediation.getHappenAddr());
					//????????????????????????????????????
					Map<String, Object> result = null;
					try {
						result = eventDisposalDockingService.saveEventDisposalAndReport(event, userInfo, "???????????????????????????");
					} catch (Exception e) {
						e.printStackTrace();
					}
					//?????????????????????????????????????????????
					if (result != null){
						disputeMediation.setEventId((Long)result.get("eventId"));
						recordId = disputeMediationService.updateByPrimaryKeySelective(disputeMediation);
						//??????????????????
						if(disputeMediation.getEventId() != null && disputeMediation.getMediationId() != null){
							disputeMediationService.saveEventReport(disputeMediation, userInfo);
						}
						disputeMediation.setCreatorId(userInfo.getUserId());
						disputeMediation.setOrgId(userInfo.getOrgId());
						// ???????????????????????????1.????????????????????????
					}
					// ???????????????????????????1.????????????????????????
					disputeMediation.setCreatorId(userInfo.getUserId());
					disputeMediation.setOrgId(userInfo.getOrgId());
					getScoreReport(disputeMediation);
					//oa???????????????
					setUserScoreService(userInfo, disputeMediation);
				}else if(disputeMediation.getDisputeStatus().equals(ConstantValue.DISPUTESTATUS_CLOSE)&&!"0".equals(isPerfect)){//????????????????????????????????????
					EventDisposal event = new EventDisposal();
					if(!"1".equals(disputeMediation.getDisputeStatus())){
						disputeMediation.setMediationCode(disputeMediationService.getMediationCode());
					}
					//??????????????????????????????????????????
//					String disputeEventType = disputeMediationService.getDisputeEventType(userInfo, null);
//					if(!StringUtils.isBlank(disputeEventType)){
//						event.setType(disputeEventType);
//						//event.setType(ConstantValue.DISPUTE_TYPE);
//					}

					String disputeEventType= disputeMediationService.getDisputeEventType(userInfo, disputeMediation.getDisputeType2());//??????????????????????????????????????????
					if(!StringUtils.isBlank(disputeEventType)){
						event.setType(disputeEventType);
					}

					Map<String, Object> params = new HashMap<String, Object>();
					params.put("contactUser",userInfo.getPartyName());
					params.put("content",disputeMediation.getDisputeCondition());
					params.put("gridId",disputeMediation.getGridId());
					params.put("eventName",disputeMediation.getDisputeEventName());
					params.put("happenTimeStr",disputeMediation.getHappenTimeStr());
					params.put("occurred",disputeMediation.getHappenAddr());
//					params.put("collectWay","01");//?????????05???APP???01
					params.put("userInfo",userInfo);
					params.put("advice","???????????????????????????");

					//????????????????????????????????????ids
					/*if (ids != null && ids.length() > 0) {
						params.put("outerAttachmentIds", ids);
					}*/
					if(!StringUtils.isBlank(disputeEventType)){
						params.put("type",disputeEventType);
					}
					//Map<String, Object> result = eventDisposalDockingService.saveEventDisposalAndClose(params);
					
					
					event.setContactUser(userInfo.getPartyName());
					event.setTel(userInfo.getVerifyMobile());
					event.setContent(disputeMediation.getDisputeCondition());
					event.setGridId(disputeMediation.getGridId());
					event.setEventName(disputeMediation.getDisputeEventName());
					event.setHappenTimeStr(disputeMediation.getHappenTimeStr());
					event.setOccurred(disputeMediation.getHappenAddr());
					
					Map<String, Object> result = eventDisposalDockingService.saveEventDisposalAndEvaluate(event,userInfo,"??????");
					
					
					//??????????????????????????????????????????????????????????????????
					if(!(Boolean) result.get("result")){
						disputeMediation.setDisputeStatus("1");
						disputeMediationService.updateByPrimaryKeySelective(disputeMediation);
						resultObj.setSuccess((Boolean) result.get("result"));
						resultObj.setTipMsg(result.get("msg").toString());
						return resultObj;
					}

						
					try {
						if (disputeMediation.getAttList() != null && disputeMediation.getAttList().size() > 0) {
							List<Attachment> newList = new ArrayList<>();
							for (Attachment att : disputeMediation.getAttList()) {
								if(att.getFilePath() == null){
									continue;
								}
								att.setAttachmentType(ConstantValue.EVENT_ATTACHMENT_TYPE);
								att.setBizId((Long) result.get("eventId"));
								att.setCreateTime(new Date());
								att.setCreatorId(userInfo.getUserId());
								att.setCreatorName(userInfo.getUserName());
								att.setEventSeq(eventSeq);
								newList.add(att);
							}
							attachmentService.saveAttachment(newList);
						}
					} catch (Exception e) {
						map.put("msg", "????????????");
					}

					if((Long) result.get("eventId") != null) {
						disputeMediation.setEventId((Long) result.get("eventId"));
						disputeMediationService.updateByPrimaryKeySelective(disputeMediation);
					}

					//??????????????????
					if(disputeMediation.getEventId() != null && disputeMediation.getMediationId() != null){
						disputeMediationService.saveEventReport(disputeMediation, userInfo);
						disputeMediation.setCreatorId(userInfo.getUserId());
						disputeMediation.setOrgId(userInfo.getOrgId());
						//???????????????????????????
						//getScoreClose(disputeMediation);
					}
					//oa???????????????
					setUserScoreService(userInfo, disputeMediation);
				}
			}
		} catch (Exception e) {
			resultObj.setData(e.getMessage());
			e.printStackTrace();
		}
		if(x != null ){
			saveDrawAreaDispute(session, map, request,areaType,wid,mapt,x, y,levnum,hs,color,mapCenterLevel,gjColor);
		}
		return resultObj;
//		return "/zzgl/dispute/mediation/result.ftl";
	}

	@RequestMapping(value = "/{type}/edit")
	public String edit(HttpSession session,HttpServletRequest request,
			@RequestParam(value = "mediationId") Long mediationId, @PathVariable String type,
			ModelMap map,
					   @RequestParam(value = "location",required = false) String location,
					   @RequestParam(value = "isPerfect",required = false) String isPerfect) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String orgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		map.addAttribute("disputeTypeDict", this.getDisputeTypeDict(orgCode));
		getDictional(map,orgCode);
		DisputeMediation disputeMediation = disputeMediationService.selectByPrimaryKey(mediationId);
		List<InvolvedPeople> involvedPeoples = involvedPeopleService.findInvolvedPeopleListByBiz(mediationId, InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
		map.addAttribute("involvedPeoples", involvedPeoples);
		map.addAttribute("disputeMediation", disputeMediation);
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("GEO_URL", App.GEO.getDomain(session));//?????????????????????
		map.addAttribute("bizType", ConstantValue.DISPUTE_ATTACHMENT_TYPE);
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
        map.addAttribute("DISPUTE_SCALE_CODE",ConstantValue.DISPUTE_SCALE_CODE);
        map.addAttribute("DISPUTE_TYPE_CODE",ConstantValue.DISPUTE_TYPE_CODE);
        map.addAttribute("MEDIATION_TYPE_CODE",ConstantValue.MEDIATION_TYPE_CODE);
		map.addAttribute("itype", request.getParameter("itype"));
		String displayTaiwangFlag = configurationService.changeCodeToValue(ConstantValue.DISPLAY_TAIWANG_FLAG,"",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode());
		map.addAttribute("displayTaiwangFlag", displayTaiwangFlag);
		map.addAttribute("module", ConstantValue.DISPUTE_MEDIATION); // ????????????
		map.addAttribute("SQ_FILE_URL", App.SQFILE.getDomain(session)); //
		map.addAttribute("IMG_URL", App.IMG.getDomain(session)); //
	    //????????????
        map.addAttribute("fileDomain", App.SQFILE.getDomain(request.getSession()));
        map.addAttribute("imgDomain", App.IMG.getDomain(request.getSession()));
        //?????????????????????
  		List<Attachment> att = attachmentService.findByBizId(mediationId, attachmentType);
  		map.put("attList", formateAttList(att));

		map.addAttribute("COMPONENTS_DOMAIN",App.COMPONENTS.getDomain(session));
		map.addAttribute("skyDomain",App.SKY.getDomain(session));
		map.addAttribute("uiDomain",App.UI.getDomain(session));

		map.addAttribute("SCOPE_INFLUNECE",ConstantValue.SCOPE_INFLUNECE);
		map.addAttribute("EVENT_NATURE",ConstantValue.EVENT_NATURE);
		
		map.addAttribute("isPerfect",isPerfect);
		if("0".equals(isPerfect)) {
			return "/zzgl/dispute/mediation_9x/dafeng/perfect_info_dispute.ftl";
		}
		return "/zzgl/dispute/mediation_9x/dafeng/create_dispute.ftl";			
	}

	@ResponseBody
	@RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
	public Map<String, Object> batchDelete(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "mediationIds") String idStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String eventIdStr = request.getParameter("eventIds");
		String[] ids = idStr.split(",");
		List<Long> mediationIds = new ArrayList<Long>();
		List<Long> eventIdList = new ArrayList<Long>();
		for(String id : ids){
			mediationIds.add(Long.valueOf(id));
		}
		if(StringUtils.isNotEmpty(eventIdStr)){
			String[] eventSplit = eventIdStr.split(",");
			for(String eventId : eventSplit){
				eventIdList.add(Long.valueOf(eventId));
			}
		}

		System.out.println("userInfo.getUserId():::::::;"+userInfo.getUserId());
		int result = disputeMediationService.deleteByIds(mediationIds, userInfo.getUserId());
		//??????????????????????????????
		if(result>0){
			//??????????????????
			EventReportRecordInfo eventReportRecordInfo = new EventReportRecordInfo();
			for (int i = 0; i < eventIdList.size(); i++) {
				if(eventIdList.get(i)!=null && !"".equals(eventIdList.get(i))){
					eventReportRecordInfo.setEventId(eventIdList.get(i));
					eventReportRecordInfo.setBizType("DISPUTE_MEDIATION_DF");
					EventReportRecordInfo eventReportRecordInfoByEventId = null;
					if(eventReportRecordInfo.getEventId() != null){
						eventReportRecordInfoByEventId = eventReportRecordService.findEventReportRecordInfoByEventId(eventReportRecordInfo);
					}

					if(eventReportRecordInfoByEventId!=null){
						//???????????????
						eventReportRecordInfoByEventId.setProcStatus("2");//??????
						eventReportRecordService.editEventReportRecordInfo(eventReportRecordInfoByEventId);
						//????????????
						//List<Long> eventIdList = new ArrayList<Long>();
						eventIdList.add(eventReportRecordInfoByEventId.getEventId());
						int i1 = eventDisposalService.deleteEventDisposalByIds(eventIdList,userInfo.getUserId());
					}
				}
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result > 0 ? "???????????????????????????" + result + "????????????" : "???????????????");
		return resultMap;
	}

	@RequestMapping(value = "/show")
	public String show(HttpSession session, HttpServletRequest request,
					   @RequestParam(value = "mediationId") Long mediationId,
					   @RequestParam(value = "isDomain", required = false) String isDomain,
					   @RequestParam(value = "showClose", required = false) String showClose,
					   @RequestParam(value = "source", required = false) String source,
					   @RequestParam(value = "location", required = false) String location,
					   ModelMap map) {
		return this.show_type(session,null,request,mediationId,isDomain,showClose,source,location,map);
	}

	@RequestMapping(value = "/show/{type}")
	public String show_type(HttpSession session, @PathVariable String type,HttpServletRequest request,
			@RequestParam(value = "mediationId", required = false) Long mediationId,
			@RequestParam(value = "isDomain", required = false) String isDomain,
			@RequestParam(value = "showClose", required = false) String showClose,
    		@RequestParam(value = "source", required = false) String source,
			@RequestParam(value = "location", required = false) String location,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String dataSource = ParamUtils.getString(request, "dataSource");
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String orgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		map.addAttribute("disputeTypeDict", this.getDisputeTypeDict(orgCode));
		getDictional(map,orgCode);
		String hashId = request.getParameter("hashId");
		if(StringUtils.isNotBlank(hashId) && mediationId == null){
			mediationId = HashIdManager.decryptLong(hashId);
		}
		DisputeMediation disputeMediation = disputeMediationService.selectByPrimaryKey(mediationId);
		if(StringUtils.isNotBlank(disputeMediation.getDisputeType3())) {
			disputeMediation.setDisputeType3(disputeMediation.getDisputeType3().trim());
		}

		String gridNames = defaultGridInfo.get("startGridName").toString();
		Long gridId = Long.valueOf(disputeMediation.getGridId());
		if(gridId!=null){
			String gridNamePath = mixedGridInfoService.getGridPath(gridId);
			if(gridNamePath != "" && gridNamePath != null){
				gridNames  = gridNamePath;
			}
		}else{
			String gridNamePath = mixedGridInfoService.getGridPath(disputeMediation.getGridCode());
			if(gridNamePath != "" && gridNamePath != null){
				gridNames  = gridNamePath;
			}
		}

		disputeMediation.setGridName(gridNames);
		List<InvolvedPeople> involvedPeoples = involvedPeopleService.findInvolvedPeopleListByBiz(mediationId, InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
		if(involvedPeoples!=null&&involvedPeoples.size()>0){
			involvedPeoples.forEach(in->{
				in.setHashId(HashIdManager.encrypt(in.getIpId()));
			});
			map.addAttribute("involvedPeoples", involvedPeoples);
			String invoStr = "";
			for(InvolvedPeople involvedPeople : involvedPeoples){
				invoStr = invoStr + "," + involvedPeople.getName();
			}
			map.addAttribute("invoStr", invoStr.substring(1,invoStr.length()));
		}

		map.addAttribute("disputeMediation", disputeMediation);
		if("isDomain".equals(isDomain)){
    		map.addAttribute("iframeCloseCallBack", ConstantValue.closeCallBack);
    		map.addAttribute("iframeUrl", App.EVENT.getDomain(session)+ConstantValue.iframeUrl);
    	}
		if(!StringUtils.isBlank(source)){
    		map.addAttribute("source", source);
    		map.addAttribute("updomain", App.TOP.getDomain(session));
    	}
		map.addAttribute("bizType", ConstantValue.DISPUTE_ATTACHMENT_TYPE);
		map.addAttribute("showClose", showClose);
		map.addAttribute("itype", StringUtils.isBlank(request.getParameter("itype"))?"":request.getParameter("itype"));
		String displayTaiwangFlag = configurationService.changeCodeToValue(ConstantValue.DISPLAY_TAIWANG_FLAG,"",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode());
		map.addAttribute("displayTaiwangFlag", displayTaiwangFlag);
//        if(type.equals("9x")){
//    		return "/zzgl/dispute/mediation_9x/show.ftl";
//        }
		map.addAttribute("IMG_URL", App.IMG.getDomain(session)); //
		map.addAttribute("SQ_FILE_URL", App.SQFILE.getDomain(session)); //
		map.addAttribute("INFOORGNAME", this.getDefaultInfoOrgName(session)); //
		map.addAttribute("COMPONENTS_DOMAIN",App.COMPONENTS.getDomain(session));
		map.addAttribute("skyDomain",App.SKY.getDomain(session));
		map.addAttribute("uiDomain",App.UI.getDomain(session));
		//?????????????????????
		List<Attachment> att = attachmentService.findByBizId(mediationId, attachmentType);
		map.put("fileDomain", App.SQFILE.getDomain(request.getSession()));
		map.put("imgDomain", App.IMG.getDomain(request.getSession()));
		map.put("attList", formateAttList(att));
		

		map.addAttribute("SCOPE_INFLUNECE",ConstantValue.SCOPE_INFLUNECE);
		map.addAttribute("EVENT_NATURE",ConstantValue.EVENT_NATURE);
		
		/*String index = request.getParameter("index");
		String ipId = request.getParameter("ipId");
		InvolvedPeople involvedPeople = involvedPeopleService.findInvolvedPeopleById(Long.valueOf(ipId));
		map.addAttribute("involvedPeople",involvedPeople);
		map.addAttribute("index",index);*/
		
		if(disputeMediation.getScopeInfluenece()!=null)
		{
			List<BaseDataDict> scopeInfluences = dictionaryService.getDataDictListOfSinglestage("B466",orgCode);
			for(BaseDataDict baseDataDict:scopeInfluences)
			{

				if(baseDataDict.getDictGeneralCode().equals(disputeMediation.getScopeInfluenece()))
				{
					map.addAttribute("scopeInfluence",baseDataDict.getDictName());
				}
			}
		}

		if(disputeMediation.getEventNature()!=null)
		{
			List<BaseDataDict> eventNature = dictionaryService.getDataDictListOfSinglestage("B467",orgCode);
			for(BaseDataDict baseDataDict:eventNature)
			{

				if(baseDataDict.getDictGeneralCode().equals(disputeMediation.getEventNature()))
				{
					map.addAttribute("eventNature",baseDataDict.getDictName());
				}
			}
		}
		if(StringUtils.isNotEmpty(dataSource) && dataSource.equals("01")){
			map.addAttribute("needClose",ParamUtils.getString(request,"needClose"));
			List<DisputeFlowInfo> disputeFlowInfos = disputeMediationJXService.searchFlowList(mediationId);
			List<MediationCase> leaders = disputeMediationJXService.searchCaseList(mediationId,"01");
			List<MediationCase> persons = disputeMediationJXService.searchCaseList(mediationId, "00");
			map.addAttribute("disputeFlowInfos",disputeFlowInfos);
			map.addAttribute("leaders",leaders);
			map.addAttribute("persons",persons);
			MixedGridInfo gridInfo = null;
			gridInfo = gridInfoService.findMixedGridInfoById(Long.valueOf(gridId), false);
			boolean flag = super.checkDataPermission(session, CommonController.TYPE_GRID, gridInfo.getInfoOrgCode());
			if(!flag){
				map.addAttribute("involvedPeoples", null);
				map.addAttribute("disputeMediation", null);
				map.addAttribute("disputeFlowInfos",null);
				map.addAttribute("leaders",null);
				map.addAttribute("persons",null);
			}
			return "/zzgl/dispute/mediation_9x/jiangxi/show_dispute.ftl";
		}
		return "/zzgl/dispute/mediation_"+type+"/dafeng/show_dispute.ftl";
	
	}



	/**
	 * ????????????
	 * @param session
	 * @param mediationId
	 * @param map
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/event/report", method = RequestMethod.POST)
	public Map<String, Object> event_report(HttpSession session,
											@RequestParam(value = "mediationId", required = false) Long mediationId,
											ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		DisputeMediation disputeMediation = disputeMediationService.selectByPrimaryKey(mediationId);
		Map<String, Object> params = new HashMap<String, Object>();
		boolean eventAndReport = disputeMediationService.updateAndReport(disputeMediation, userInfo, params);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", eventAndReport);
		return resultMap;
	}

	/**
	 * ????????????????????????
	 * @param session
	 * @param mediationId
	 * @param disputeId
	 * @param map
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/event/report/wx", method = RequestMethod.POST)
	public Map<String, Object> event_report_wx(HttpSession session,
									  @RequestParam(value = "mediationId", required = false) Long mediationId,
									  @RequestParam(value = "disputeId", required = false) Long disputeId,
									  	@RequestParam(value = "mediationDeadlineStr", required = false) String mediationDeadlineStr,
									  ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Boolean r = disputeMediationService.eventReport(mediationId, disputeId, mediationDeadlineStr, userInfo);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", r);
		return resultMap;
	}

	/**
	 * ??????????????????????????????
	 * ?????????????????????????????????????????????
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/report", method = RequestMethod.POST)
	public Map<String, Object> report(HttpSession session,
			@RequestParam(value = "mediationId", required = false) Long mediationId,
			@RequestParam(value = "eventId", required = false) Long eventId,
			ModelMap map, @RequestParam(value = "status", required = false) String status){
		//???????????????
		if(mediationId != null && !StringUtils.isBlank(status)){

			DisputeMediation disputeMediation = new DisputeMediation();
			disputeMediation.setMediationId(mediationId);
//			disputeMediation.setStatus(status);
			disputeMediation.setDisputeStatus(status);
			if(!"1".equals(status)){
				disputeMediation.setMediationCode(disputeMediationService.getMediationCode());
			}
			disputeMediationService.updateByPrimaryKeySelective(disputeMediation);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", mediationId>0L?9 :10);
		return resultMap;
	}

	private String getDisputeTypeDict(String infoOrgCode) {
//		if (StringUtils.isNotBlank(infoOrgCode)) {
//			if (infoOrgCode.startsWith("62")) {
//				return "B037";
//			}
//		}
		if(infoOrgCode.startsWith("36")){
			return "B799";
		}
		return "A001093199056";
	}

	/**
	 * ???????????????
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveZero", method = RequestMethod.POST)
	public Map<String, Object> saveZero(HttpSession session, ModelMap map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String,Object> gridinfo = this.getDefaultGridInfo(session);
		String gridId = gridinfo.get(KEY_START_GRID_ID).toString();
		try {
			/*Integer zeroId=disputeMediationService.findZeroIdByGridId(gridId);
			if(zeroId != null){
				resultMap.put("result", false);
				resultMap.put("msg", "?????????????????????????????????!");
				return resultMap;
			}*/
			Integer count=disputeMediationService.findCountByGridId(gridId);
			if(count>0 ){
				resultMap.put("result", false);
				resultMap.put("msg", "???????????????????????????????????????????????????????????????!");
				return resultMap;
			}
			DisputeMediation dm = new DisputeMediation();
			dm.setDisputeType2("1701");//?????????
			//dm.setDisputeCondition(gridinfo.get(KEY_START_GRID_NAME).toString()+disputeCondition);
			dm.setOrgId(userInfo.getOrgId());
			dm.setUpdateId(userInfo.getUserId().intValue());
			dm.setCreatorId(userInfo.getUserId());
			dm.setStatus("3");
			dm.setGridId(Long.valueOf(gridId));
			dm.setGridName(gridinfo.get(KEY_START_GRID_NAME).toString());
			dm.setSource("03");
			disputeMediationService.insertDisputeMediationZero(dm);
		} catch (Exception e) {
			resultMap.put("result", false);
			resultMap.put("msg", e.getMessage());
			e.printStackTrace();
		}
		resultMap.put("result", true);
		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "/deleteZero", method = RequestMethod.POST)
	public Map<String,Object> deleteZero(HttpSession session, ModelMap map){
		Map<String,Object> data = new HashMap<String, Object>();
		Map<String,Object> gridinfo = this.getDefaultGridInfo(session);
		String gridId = gridinfo.get(KEY_START_GRID_ID).toString();
		Integer zeroId = disputeMediationService.findZeroIdByGridId(gridId);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(zeroId!=null){
			data.put("zeroId", zeroId);
			data.put("delete", disputeMediationService.deleteZero(gridId,userInfo.getUserId()));
		}
		return data;
	}
	

	/**
	* ????????????????????????
	 * @param session
	 * @param map
	 * @param mediationId ????????????ID
	 * @param disputeStatus ????????????????????????
	* */
	@RequestMapping(value = "/toInvalid")
	public String toInvalid(HttpSession session,
						  ModelMap map,
						  @RequestParam(value = "mediationId",required = true)Long mediationId,
						  @RequestParam(value = "disputeStatus",required = true)String disputeStatus){

		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		//??????????????????id?????????????????????????????????????????????????????????????????????
		EventReportRecordInfo eventReportRecordInfo = new EventReportRecordInfo();

		//??????????????????id?????????????????????EventReportRecordInfo??????????????????
		eventReportRecordInfo.setBizId(mediationId);
		eventReportRecordInfo.setBizType("DISPUTE_MEDIATION_SECURITY");
		EventReportRecordInfo eventReportRecordInfoByEventId = this.eventReportRecordService.findEventReportRecordInfoByEventId(eventReportRecordInfo);

		if (null == eventReportRecordInfoByEventId) {
			eventReportRecordInfo.setBizType("DISPUTE_MEDIATION");
			eventReportRecordInfoByEventId = this.eventReportRecordService.findEventReportRecordInfoByEventId(eventReportRecordInfo);
		}

		//??????EventReportRecordInfo??????????????????????????????id
		Map<String,Object> result = new HashMap();
		Long eventId = -1L;
		if (null != eventReportRecordInfoByEventId) {
			eventId = eventReportRecordInfoByEventId.getEventId();
			EventDisposal eventDisposal = this.eventDisposalService.findEventById(eventId, userInfo.getOrgCode());

			//??????eventId?????????????????????id
			if (null != eventDisposal) {
				String instanceId = this.eventDisposalWorkflowService.capInstanceIdByEventId(eventDisposal.getEventId());

				//?????????????????????id???????????????????????????????????????????????????
				result = this.eventDisposalWorkflowService.curNodeData(Long.parseLong(instanceId));
			}
		}

		//?????????????????????1??????(??????)2??????3??????4??????????????????????????????????????????????????????
		if ("2".equals(disputeStatus)) {
			result.put("statusName","??????");
		} else if ("3".equals(disputeStatus)) {
			result.put("statusName","??????");
		}

		result.put("eventId",eventId);
		result.put("mediationId",mediationId);
		map.addAttribute("result",result);

		return "/zzgl/dispute/mediation_9x/nanchang/invalid.ftl";
	}

	/**
	 * ??????????????????
	 * @param map
	 * @param request
	 * 		  mediationId ????????????ID
	 * 		  invalidReason ????????????
	 * */
	@ResponseBody
	@RequestMapping(value = "/invalid")
	public Object invalid(ModelMap map,HttpServletRequest request,HttpSession session){

		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		Boolean result = true;
		String msg = "";

		map.addAttribute("msg",msg);
		map.addAttribute("result",result);

		//??????????????????id
		Long mediationId = Long.parseLong(request.getParameter("mediationId"));
		DisputeMediation disputeMediation = new DisputeMediation();
		if (null != mediationId && mediationId > -1) {
			disputeMediation = this.disputeMediationService.selectByPrimaryKey(mediationId);
			if (null == disputeMediation) {
				result = false;
				msg = "?????????????????????????????????";
				return map;
			}
		}

		//??????????????????
		String invalidReason = request.getParameter("invalidReason");
		if (null != invalidReason && !"".equals(invalidReason)) {
			if (null != disputeMediation) {
				disputeMediation.setInvalidReason(invalidReason);
				//???????????????????????????4???????????????
				disputeMediation.setDisputeStatus("4");
				int count = this.disputeMediationService.updateByPrimaryKeySelective(disputeMediation);
				if (count <= 0) {
					result = false;
					msg = "???????????????????????????";
					return map;
				}
			}

		}
		//????????????
		Long eventId = Long.parseLong(request.getParameter("eventId"));
		if (null != eventId && eventId > -1L) {
			List<Long> list = new ArrayList<Long>();
			list.add(eventId);
			int count = this.eventDisposalService.deleteEventDisposalByIds(list,userInfo.getUserId());
			if (count <= 0) {
				result = false;
				msg = "?????????????????????";
				return map;
			}
		}

		return map;
	}
	
	/**
	 * ???Attachment?????????title?????????????????????????????????????????????
	 * @param attList
	 * @return
	 */
	private List<Attachment> formateAttList(List<Attachment> attList) {
		for (Attachment att : attList) {
			att.setTitle(getFileSuffixByPath(att.getFilePath()));
		}
		return attList;
	}
	
	private String getFileSuffixByPath(String path) {
		// ?????????????????????????????????????????????????????????
		String fileType = path.substring(path.lastIndexOf(".") + 1,path.length()).toLowerCase();
		
		if ("bmp|jpg|jpeg|png|gif".indexOf(fileType) > -1) {
			return "image";
		} else if ("xls|xlsx|xlsm|xltx".indexOf(fileType) > -1) {
			return "excel";
		} else if("docx|docm|dotx|doc".indexOf(fileType) > -1) {
			return "word";
		} else if("pptx|pptm|ppsx|ppt".indexOf(fileType) > -1) {
			return "ppt";
		} 
		return "other";
	}
	
	/**
	 * ????????????
	 * @param request
	 * @param attIdAPP_URL_GEO
	 * @return
	 */
	@RequestMapping("/delAtt")
	public @ResponseBody Object delAtt(HttpServletRequest request,Long attId) {
		
		return this.attachmentService.deleteAttachmentById(attId);
	}
	
	/**
	 * ??????????????????
	 * @param request
	 * @param attId
	 * @return
	 */
	@RequestMapping("/detailAtt")
	public @ResponseBody Object detailAtt(HttpServletRequest request,Long mediationId,ModelMap map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//?????????????????????
		List<Attachment> att = attachmentService.findByBizId(mediationId, attachmentType);
		resultMap.put("fileDomain", App.SQFILE.getDomain(request.getSession()));
		resultMap.put("imgDomain", App.IMG.getDomain(request.getSession()));
		resultMap.put("attList", formateAttList(att));
		return resultMap;
	}
	
	
	@RequestMapping(value="/saveDrawAreaDispute")
	public @ResponseBody int saveDrawAreaDispute(HttpSession session, ModelMap map, HttpServletRequest request
			,@RequestParam(value="areaType", required=false) String areaType
			,@RequestParam(value="wid", required=false) Long wid
			,@RequestParam(value="mapt", required=false) Integer mapt
			,@RequestParam(value="x", required=false) Float x
			,@RequestParam(value="y", required=false) Float y
			,@RequestParam(value="levnum", required=false) String levnum
			,@RequestParam(value="hs", required=false) String hs
			,@RequestParam(value="color", required=false) String color
			,@RequestParam(value="mapCenterLevel", required=false) Integer mapCenterLevel
			,@RequestParam(value="gjColor", required=false) String gjColor
			) throws Exception{
		int result = 0;
		if("building".equals(areaType)){//????????????
			GisMapBuidingInfo bean = gisInfoService.findGisMapBuidingInfoByBuildingIdAndMapt(wid, mapt);
			if(bean==null){
				bean = new GisMapBuidingInfo();
				bean.setBuildingId(wid);
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				
				result = gisInfoService.insertGisMapBuidingInfo(bean);
				
				///????????????
				if(result>0){
					bean = gisInfoService.findGisMapBuidingInfoByBuildingIdAndMapt(wid, mapt);
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getBuildingName();
					logService.savelog2(request, LogModule.mapDataForBuilding, ActionType.insert, 
							bean.getId(), recordName, null, valueAfter);
				}
			}else{
				String valueBefore = JSONObject.fromObject(bean).toString();
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				result = gisInfoService.updateGisMapBuidingInfo(bean);
				///????????????
				if(result>0){
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getBuildingName();
					logService.savelog2(request, LogModule.mapDataForBuilding, ActionType.update, 
							bean.getId(), recordName, valueBefore, valueAfter);
				}
			}
		}else if("grid".equals(areaType)){//?????????
			GisMapGridInfo bean = gisInfoService.findGisMapGridInfoByGridIdAndMapt(wid, mapt);
			
			if(bean==null){
				bean = new GisMapGridInfo();
				bean.setGridId(wid);
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				bean.setMapCenterLevel(mapCenterLevel);
				bean.setGjColor(gjColor);
				result = gisInfoService.insertGisMapGridInfo(bean);
				///????????????
				if(result>0){
					bean = gisInfoService.findGisMapGridInfoByGridIdAndMapt(wid, mapt);
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getGridName();
					logService.savelog2(request, LogModule.mapDataForGrid, ActionType.insert, 
							bean.getId(), recordName, null, valueAfter);
				}
			}else{
				String valueBefore = JSONObject.fromObject(bean).toString();
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				bean.setMapCenterLevel(mapCenterLevel);
				bean.setGjColor(gjColor);
				result = gisInfoService.updateGisMapGridInfo(bean);
				///????????????
				if(result>0){
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getGridName();
					logService.savelog2(request, LogModule.mapDataForGrid, ActionType.update, 
							bean.getId(), recordName, valueBefore, valueAfter);
				}
			}
		}else if("segmentgrid".equals(areaType)) {
			GisMapSegmentGridInfo bean = gisInfoService.findGisMapSegmentGridInfoBySegmentGridIdAndMapt(wid, mapt);
			if(bean == null) {
				bean = new GisMapSegmentGridInfo();
				bean.setSegmentGridId(wid);
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				result = gisInfoService.insertGisMapSegmentGridInfo(bean);
				///????????????
				if(result>0){
					bean  = gisInfoService.findGisMapSegmentGridInfoBySegmentGridIdAndMapt(wid, mapt);
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getSegmentGridName();
					logService.savelog2(request, LogModule.mapDataForSegmentGrid, ActionType.insert, 
							bean.getId(), recordName, null, valueAfter);
				}
			}else {
				String valueBefore = JSONObject.fromObject(bean).toString();
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				result = gisInfoService.updateGisMapSegmentGridInfo(bean);
				///????????????
				if(result>0){
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getSegmentGridName();
					logService.savelog2(request, LogModule.mapDataForSegmentGrid, ActionType.update, 
							bean.getId(), recordName, valueBefore, valueAfter);
				}
			}
		}else{ //????????????????????????
			ResMarker marker = null;
			ActionType action;
			Long keyId=null;
			String valueBefore =null, valueAfter=null;
			List<ResMarker> list = resMarkerService.findResMarkerByParam(areaType, wid, mapt+"");
			if(list!=null && list.size()>0){
				marker = list.get(0);
				keyId = marker.getMarkerId();
				action = ActionType.update;
				valueBefore = JSONObject.fromObject(marker).toString();
			}else{
				marker = new ResMarker();
				action = ActionType.insert;
				valueBefore = null;
			}
			
			marker.setResourcesId(wid);
			String markerType = ConstantValue.MARKER_TYPE_DISPUTE_MEDIATION;
			marker.setMarkerType(markerType);
			marker.setMapType(mapt+"");
			marker.setX(x+"");
			marker.setY(y+"");
			marker.setHs(hs);
			marker.setColordesc(color);
			marker.setLevnum(levnum);
			boolean re = resMarkerService.saveOrUpdateResMarker(marker);
			if(re){
				result++;
				if(keyId==null){
					List<ResMarker> list2 = resMarkerService.findResMarkerByParam(areaType, wid, mapt+"");
					keyId = list2.get(0).getMarkerId();
				}
				///????????????
				logService.savelog2(request, LogModule.mapDataForResMarker, action, 
						keyId, areaType+ "--"+ keyId, valueBefore, valueAfter);
			}
		}
		return result;
	}
	
	
	private void getScoreReport(DisputeMediation disputeMediation) {
		
		Long disputeMediationOrgId = disputeMediation.getOrgId(),
			 createUserId = disputeMediation.getCreatorId();
		String orgCode = "";
		if(createUserId != null && createUserId > 0) {
			if(disputeMediationOrgId != null && disputeMediationOrgId > 0) {
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(disputeMediationOrgId);
				if(orgInfo != null) {
					orgCode = orgInfo.getOrgCode();
				}
			}
			
			if(StringUtils.isNotBlank(orgCode)) {
				String userPartyName = "";
				UserBO userBO = userManageService.getUserInfoByUserId(createUserId);
				UserDetailScore userDetailScore = new UserDetailScore();
				
				if(userBO != null) {
					userPartyName = userBO.getPartyName();
				}
				
				userDetailScore.setUserId(createUserId);
				userDetailScore.setUserName(userPartyName);
				userDetailScore.setOrgCode(orgCode);
				userDetailScore.setCreateBy(createUserId);
				userDetailScore.setScoreType("D1");
				userDetailScore.setRemark(String.valueOf(disputeMediation.getMediationId()));
				userScoreService.insertUserDetailScore(userDetailScore);
			}
		}
	}
	
	//??????
	private void getScoreClose(DisputeMediation disputeMediation) {
		Long disputeMediationOrgId = disputeMediation.getOrgId(),
				createUserId = disputeMediation.getCreatorId();
		String orgCode = "";
		
		if(createUserId != null && createUserId > 0) {
			if(disputeMediationOrgId != null && disputeMediationOrgId > 0) {
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(disputeMediationOrgId);
				if(orgInfo != null) {
					orgCode = orgInfo.getOrgCode();
				}
			}
			
			if(StringUtils.isNotBlank(orgCode)) {
				String userPartyName = "";
				UserBO userBO = userManageService.getUserInfoByUserId(createUserId);
				UserDetailScore userDetailScore = new UserDetailScore();
				
				if(userBO != null) {
					userPartyName = userBO.getPartyName();
				}
				
				userDetailScore.setUserId(createUserId);
				userDetailScore.setUserName(userPartyName);
				userDetailScore.setOrgCode(orgCode);
				userDetailScore.setCreateBy(createUserId);
				userDetailScore.setScoreType("D2");
				userDetailScore.setRemark(String.valueOf(disputeMediation.getMediationId()));
				userScoreService.insertUserDetailScore(userDetailScore);
			}
		}
	}
	
	
	/**
	 * ????????????
	 * @param session
	 * @param type
	 * @param request
	 * @param mediationId
	 * @param isDomain
	 * @param showClose
	 * @param source
	 * @param location
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/print/{type}")
	public String print(HttpSession session, @PathVariable String type,HttpServletRequest request,
			@RequestParam(value = "mediationId") Long mediationId,
			@RequestParam(value = "isDomain", required = false) String isDomain,
			@RequestParam(value = "showClose", required = false) String showClose,
    		@RequestParam(value = "source", required = false) String source,
			@RequestParam(value = "location", required = false) String location,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String orgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		map.addAttribute("disputeTypeDict", this.getDisputeTypeDict(orgCode));
		getDictional(map,orgCode);
		DisputeMediation disputeMediation = disputeMediationService.selectByPrimaryKey(mediationId);

		String gridNames = defaultGridInfo.get("startGridName").toString();
		Long gridId = Long.valueOf(disputeMediation.getGridId());
		if(gridId!=null){
			String gridNamePath = mixedGridInfoService.getGridPath(gridId);
			if(gridNamePath != "" && gridNamePath != null){
				gridNames  = gridNamePath;
			}
		}else{
			String gridNamePath = mixedGridInfoService.getGridPath(disputeMediation.getGridCode());
			if(gridNamePath != "" && gridNamePath != null){
				gridNames  = gridNamePath;
			}
		}

		disputeMediation.setGridName(gridNames);
		List<InvolvedPeople> involvedPeoples = involvedPeopleService.findInvolvedPeopleListByBiz(mediationId, InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
		if(involvedPeoples!=null&&involvedPeoples.size()>0){
			map.addAttribute("involvedPeoples", involvedPeoples);
			String invoStr = "";
			for(InvolvedPeople involvedPeople : involvedPeoples){
				invoStr = invoStr + "," + involvedPeople.getName();
			}
			map.addAttribute("invoStr", invoStr.substring(1,invoStr.length()));
		}

		map.addAttribute("disputeMediation", disputeMediation);
		if("isDomain".equals(isDomain)){
    		map.addAttribute("iframeCloseCallBack", ConstantValue.closeCallBack);
    		map.addAttribute("iframeUrl", App.EVENT.getDomain(session)+ConstantValue.iframeUrl);
    	}
		if(!StringUtils.isBlank(source)){
    		map.addAttribute("source", source);
    		map.addAttribute("updomain", App.TOP.getDomain(session));
    	}
		map.addAttribute("bizType", ConstantValue.DISPUTE_ATTACHMENT_TYPE);
		map.addAttribute("showClose", showClose);
		map.addAttribute("itype", StringUtils.isBlank(request.getParameter("itype"))?"":request.getParameter("itype"));
		String displayTaiwangFlag = configurationService.changeCodeToValue(ConstantValue.DISPLAY_TAIWANG_FLAG,"",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode());
		map.addAttribute("displayTaiwangFlag", displayTaiwangFlag);
		
		map.addAttribute("IMG_URL", App.IMG.getDomain(session)); //
		map.addAttribute("SQ_FILE_URL", App.SQFILE.getDomain(session)); //
		map.addAttribute("INFOORGNAME", this.getDefaultInfoOrgName(session)); //
		
		//?????????????????????
		List<Attachment> att = attachmentService.findByBizId(mediationId, attachmentType);
		map.put("fileDomain", App.SQFILE.getDomain(request.getSession()));
		map.put("imgDomain", App.IMG.getDomain(request.getSession()));
		map.put("attList", formateAttList(att));
		

		map.addAttribute("SCOPE_INFLUNECE",ConstantValue.SCOPE_INFLUNECE);
		map.addAttribute("EVENT_NATURE",ConstantValue.EVENT_NATURE);
		
		if(disputeMediation.getScopeInfluenece()!=null)
		{
			List<BaseDataDict> scopeInfluences = dictionaryService.getDataDictListOfSinglestage("B466",orgCode);
			for(BaseDataDict baseDataDict:scopeInfluences)
			{

				if(baseDataDict.getDictGeneralCode().equals(disputeMediation.getScopeInfluenece()))
				{
					map.addAttribute("scopeInfluence",baseDataDict.getDictName());
				}
			}
		}

		if(disputeMediation.getEventNature()!=null)
		{
			List<BaseDataDict> eventNature = dictionaryService.getDataDictListOfSinglestage("B467",orgCode);
			for(BaseDataDict baseDataDict:eventNature)
			{

				if(baseDataDict.getDictGeneralCode().equals(disputeMediation.getEventNature()))
				{
					map.addAttribute("eventNature",baseDataDict.getDictName());
				}
			}
		}
		return "/zzgl/dispute/mediation_9x/dafeng/print.ftl";
	
	}
	/**
	 * ????????????????????????????????????
	 * @param session
	 * @param request
	 * @param mediationId ????????????ID
	 * @return
	 */
	@RequestMapping(value = "/getPeopleSize")
	@ResponseBody
	public int getPeopleSize(HttpSession session,HttpServletRequest request,
			@RequestParam(value = "mediationId",required = false) Long mediationId) {
		List<InvolvedPeople> involvedPeoples = null;
		if(mediationId != null) {
			involvedPeoples = involvedPeopleService.findInvolvedPeopleListByBiz(mediationId, InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
		}
		return involvedPeoples.size();
	}

	/**
	 * ??????????????? ???????????????????????? ????????? ???????????????????????????
	 * @param request
	 * @param map
	 * @param mediationId
	 * @return
	 */
	@RequestMapping(value = "/editResolve")
	public Object editResolve(HttpServletRequest request,ModelMap map,
							  @RequestParam(value = "mediationId",required = false) String mediationId){
		DisputeMediation disputeMediation = disputeMediationService.searchResInfoById(Long.valueOf(mediationId));
		map.addAttribute("disputeMediation",disputeMediation);
		map.addAttribute("id",mediationId);
		return "/zzgl/dispute/mediation_9x/dafeng/edit_dispute_resolving.ftl";
	}

	/**
	 * PC?????????????????????
	 * @param request
	 * @param map
	 * @param disputeMediation
	 * @return
	 */
	@RequestMapping(value = "/resolveSave")
	@ResponseBody
	public Object resolveSave(HttpServletRequest request,ModelMap map,DisputeMediation disputeMediation){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = disputeMediationService.insertOrUpdateResovle(disputeMediation);
		resultMap.put("status",flag==true?"0":"-1");
		resultMap.put("success",flag);
		resultMap.put("tipMsg","????????????");
		return resultMap;
	}

	/**
	 * ??????????????????ID?????? ????????????
	 * @param request
	 * @param map
	 * @param mediationId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getResInfo")
	public Object getResInfo(HttpServletRequest request,ModelMap map,@RequestParam(value = "mediationId",required = false) String mediationId){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			DisputeMediation disputeMediation = disputeMediationService.searchResInfoById(Long.valueOf(mediationId));
			List<BaseDataDict> mediationTypeDC_9x= new ArrayList<BaseDataDict>();
			List<BaseDataDict> mediationTypeDC9x= dictionaryService.getDataDictListOfSinglestage("B417", "");
			mediationTypeDC_9x.addAll(mediationTypeDC9x);
			for(BaseDataDict baseDataDict : mediationTypeDC9x){
				List<BaseDataDict> mediationTypeSubs = dictionaryService.getDataDictListOfSinglestage(baseDataDict.getDictCode(), "");
				mediationTypeDC_9x.addAll(mediationTypeSubs);
			}
			Map<String,Object> data = new HashMap<>();
			data.put("disputeMediation",disputeMediation);
			data.put("dictData",mediationTypeDC_9x);
			resultMap.put("status",disputeMediation!=null?"0":"-1");
			resultMap.put("data",data);
			resultMap.put("info","??????????????????");
			resultMap.put("exception",null);
		}catch (Exception e){
			resultMap.put("status","-1");
			resultMap.put("data",null);
			resultMap.put("info","??????????????????");
			resultMap.put("exception",e);
		}
		return resultMap;
	}

	/**
	 * ????????????????????????????????????????????? ???????????????????????? ??????????????????
	 * @param request
	 * @param map
	 * @param type
	 * @param listType
	 * @return
	 */
	@RequestMapping("/bigScreen/dataPage/{bigScreenType}")
	public Object dataPage(HttpServletRequest request, ModelMap map, @PathVariable("bigScreenType") String type, String listType){
		String orgCode = ParamUtils.getString(request, "orgCode");
		String nowYear = ParamUtils.getString(request, "nowYear");
		String month = ParamUtils.getString(request, "month");
		map.addAttribute("orgCode",orgCode);
		map.addAttribute("listType",listType);
		map.addAttribute("nowYear",nowYear);
		map.addAttribute("month",month);
		MixedGridInfo gridInfo = null;
		gridInfo = gridInfoService.getDefaultGridByOrgCode(orgCode);
		if(gridInfo != null) {
			Long startGridId = gridInfo.getGridId();
			String startGridName = gridInfo.getGridName();
			map.addAttribute("startGridId", startGridId);
			map.addAttribute("startGridName", startGridName);
		}
		return "/zzgl/dispute/mediation_9x/jiangxi/bigScreen/"+type+".ftl";
	}

	/**
	 * ????????????????????????
	 * @param request
	 * @param listType
	 * @param page
	 * @param rows
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/bigScreen/screenListData/listData")
	public Object screenListData(HttpServletRequest request,String listType, int page,int rows){
		Map<String, Object> params = new HashMap<>();
		params.put("listType",listType);
		params.put("orgCode",ParamUtils.getString(request,"orgCode"));
		params.put("createStart",ParamUtils.getString(request,"createStart"));
		params.put("createEnd",ParamUtils.getString(request,"createEnd"));
		params.put("keyWord",ParamUtils.getString(request,"keyWord"));
		EUDGPagination pagination = null;
		if("n3".equals(listType)){
			pagination = disputeMediationJXService.findDisputeUserJX(page,rows,params);
		}else{
			params.put("happenStart",ParamUtils.getString(request,"happenStart"));
			params.put("happenEnd",ParamUtils.getString(request,"happenEnd"));
			params.put("disputeType",ParamUtils.getString(request,"disputeType"));
			String disputeTypeList = ParamUtils.getString(request, "disputeTypeList");
			if(StringUtils.isNotBlank(disputeTypeList)){
				List<String> list = Arrays.asList(disputeTypeList.split(","));
				params.put("disputeTypeList",list);
			}
			pagination = disputeMediationJXService.findDisputeJX(page, rows, params);
		}
		return pagination;
	}
	/**
	 * ?????????????????????????????????
	 * @param request
	 * @param map
	 * @param type
	 * @param mediationId
	 * @return
	 */
	@RequestMapping("/bigScreen/dataDetail/{bigScreenDetailType}")
	public Object dataDetail(HttpServletRequest request,ModelMap map, @PathVariable("bigScreenDetailType") String type, Long mediationId){
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(request.getSession());
		String orgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		getDictional(map,orgCode);
		DisputeMediation disputeMediation = disputeMediationService.selectByPrimaryKey(mediationId);
		if(StringUtils.isNotBlank(disputeMediation.getDisputeType3())) {
			disputeMediation.setDisputeType3(disputeMediation.getDisputeType3().trim());
		}
		List<InvolvedPeople> involvedPeoples = involvedPeopleService.findInvolvedPeopleListByBiz(mediationId, InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
		map.addAttribute("disputeMediation", disputeMediation);
		map.addAttribute("involvedPeoples",involvedPeoples);
		List<DisputeFlowInfo> disputeFlowInfos = disputeMediationJXService.searchFlowList(mediationId);
		List<MediationCase> leaders = disputeMediationJXService.searchCaseList(mediationId,"01");
		List<MediationCase> persons = disputeMediationJXService.searchCaseList(mediationId, "00");
		map.addAttribute("disputeFlowInfos",disputeFlowInfos);
		map.addAttribute("leaders",leaders);
		map.addAttribute("persons",persons);

		return "/zzgl/dispute/mediation_9x/jiangxi/bigScreen/"+type+".ftl";
	}

	/**
	 * ?????????????????????????????????
	 * @param request
	 * @param map
	 * @param mediationId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/bigScreen/detailInfo/detail")
	public Object detailInfo(HttpServletRequest request,ModelMap map, Long mediationId){
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(request.getSession());
		String orgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		DisputeMediation disputeMediation = disputeMediationService.selectByPrimaryKey(mediationId);
		if(StringUtils.isNotBlank(disputeMediation.getDisputeType3())) {
			disputeMediation.setDisputeType3(disputeMediation.getDisputeType3().trim());
		}
		List<InvolvedPeople> involvedPeoples = involvedPeopleService.findInvolvedPeopleListByBiz(mediationId, InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
		List<DisputeFlowInfo> disputeFlowInfos = disputeMediationJXService.searchFlowList(mediationId);
		List<MediationCase> leaders = disputeMediationJXService.searchCaseList(mediationId,"01");
		List<MediationCase> persons = disputeMediationJXService.searchCaseList(mediationId, "00");

		List<BaseDataDict> disputeType_9x = new ArrayList<BaseDataDict>();
		List<BaseDataDict> disputeType9x = dictionaryService.getDataDictListOfSinglestage(getDisputeTypeDict(orgCode), orgCode);
		disputeType_9x.addAll(disputeType9x);
		for(BaseDataDict baseDataDict : disputeType9x){
			List<BaseDataDict> disputeTypeSubs = dictionaryService.getDataDictListOfSinglestage(baseDataDict.getDictCode(), orgCode);
			disputeType_9x.addAll(disputeTypeSubs);
		}
		resultMap.put("disputeTypeDC",disputeType_9x);
		resultMap.put("disputeMediation", disputeMediation);
		resultMap.put("involvedPeoples",involvedPeoples);
		resultMap.put("disputeFlowInfos",disputeFlowInfos);
		resultMap.put("leaders",leaders);
		resultMap.put("persons",persons);
		return resultMap;
	}

	public boolean setUserScoreService(UserInfo userInfo,DisputeMediation bo){
		IdRecord idRecord = new IdRecord();
		idRecord.setPartyMemberId(userInfo.getUserId());
		idRecord.setOrgCode(userInfo.getOrgCode());
		idRecord.setRegionCode(userInfo.getInfoOrgList().get(0).getOrgCode());
		idRecord.setCreator(userInfo.getUserId());
		idRecord.setSubmodularCode("F-OA004ZHN002");
		idRecord.setTableType("GRID_USER");
		idRecord.setBizId(bo.getMediationId());
		idRecord.setRuleType("01");
		idRecord.setInteralReason("??????????????????");
		idRecord.setInteralDate(new Date());
		boolean flag = integralRuleService.rewardPoints(idRecord, null);
		if(!flag){
			if(!flag && logger.isErrorEnabled()) {
				logger.error("????????????????????????????????????" + JsonUtils.beanToJson(idRecord));
			}
		}
		return flag;
	}
}
