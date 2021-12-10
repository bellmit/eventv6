package cn.ffcs.zhsq.requestion.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.dubbo.rpc.RpcException;

import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.mobile.api.bo.Push;
import cn.ffcs.mobile.api.service.PushService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.holidayinfo.IHolidayInfoService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.INumberConfigureService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.spring.RequestionService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.requestion.CorpLink;
import cn.ffcs.zhsq.mybatis.domain.requestion.ReqLink;
import cn.ffcs.zhsq.mybatis.domain.requestion.Requestion;
import cn.ffcs.zhsq.requestion.service.ICorpLinkService;
import cn.ffcs.zhsq.requestion.service.IReqLinkService;
import cn.ffcs.zhsq.requestion.service.IRequestionService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;

/**   
 * @Description: 诉求表模块控制器
 * @Author: caiby
 * @Date: 03-12 08:45:59
 * @Copyright: 2018 福富软件
 */ 
@Controller("requestionController")
@RequestMapping("/zhsq/requestion")
public class RequestionController extends ZZBaseController{

	@Autowired
	private IRequestionService requestionService; //注入诉求表模块服务
	@Autowired
	private RequestionService workflowService;
	@Autowired
	private IReqLinkService reqLinkService;//注入诉求联动单位模块服务
	@Autowired
	private INumberConfigureService numberConfigureService;
	@Autowired
	private ICorpLinkService corpLinkService;
	@Autowired
	private IAttachmentService attachmentService;
	@Autowired
    private FileUploadService fileUploadService;
	@Autowired
    private UserManageOutService userManageService;
	@Autowired
    private OrgSocialInfoOutService orgSocialInfoService;
	@Autowired
    private IFunConfigurationService funConfigurationService;
	@Autowired
    private PushService pushService;
	@Autowired
    private IHolidayInfoService holidayInfoService;
	
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	static String formTypeId = "";
	static String workflowId = "";
	static String formTypeId_ = "";
	static String workflowId_ = "";
	private String REQ_ATTACHMENT_TYPE = "requestionAtta";//诉求表附件类型
	private String REQ_LINK_ATTACHMENT_TYPE = "reqLinkAtta";//诉求表附件类型
//	private String ORG_CODE = "3501";
	@Value("${zqhl_code}")
	private String ORG_CODE;
	//投诉类caseType_01
	private String caseType_01 = "10";
	//求助类caseType_02
	private String caseType_02 = "3";
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value = "type", required = false) String type) {
		//数据字典
		map.addAttribute("caseTypeDict", ConstantValue.CASE_TYPE_CODE);
		if("undo".equals(type)){
			return "/zzgl/requestion/list_requestion_db.ftl";
		}else if("do".equals(type)){
			return "/zzgl/requestion/list_requestion_jb.ftl";
		}else{
			return "/zzgl/requestion/list_requestion_all.ftl";
		}
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,
		@RequestParam(value = "keyWord", required = false) String keyWord,
		@RequestParam(value = "type", required = false) String type,
		@RequestParam(value = "happenTimeStr", required = false) String happenTimeStr,
		@RequestParam(value = "endTimeStr", required = false) String endTimeStr,
		int page, int rows) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("keyWord", keyWord);
		params.put("type", type);
		params.put("happenTimeStr", happenTimeStr);
		params.put("endTimeStr", endTimeStr);
		EUDGPagination pagination = requestionService.searchList(page, rows, params);
		return pagination;
	}
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/indexDB")
	public Object indexDB(HttpServletRequest request, HttpSession session, ModelMap map) {
		//数据字典
		map.addAttribute("caseTypeDict", ConstantValue.CASE_TYPE_CODE);
		return "/zzgl/requestion/list_requestion_db.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listDataDB")
	public Object listDataDB(HttpServletRequest request, HttpSession session, ModelMap map,
		@RequestParam(value = "keyWord", required = false) String keyWord,
		@RequestParam(value = "type", required = false) String type,
		@RequestParam(value = "happenTimeStr", required = false) String happenTimeStr,
		@RequestParam(value = "endTimeStr", required = false) String endTimeStr,
		@RequestParam(value = "wfCurOrg", required = false) String wfCurOrg,
		int page, int rows) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		initParams();
		params.put("formTypeId", Long.parseLong(formTypeId));
		params.put("formTypeId_", Long.parseLong(formTypeId_));
		params.put("dbOrgId", userInfo.getOrgId().toString()); 
		
		params.put("orgCode", userInfo.getOrgCode());
		params.put("keyWord", keyWord);
		params.put("wfCurOrg", wfCurOrg);
		params.put("type", type);
		params.put("happenTimeStr", happenTimeStr);
		params.put("endTimeStr", endTimeStr);
		EUDGPagination pagination = requestionService.searchDBList(page, rows, params);
		return pagination;
	}
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/indexDB_Main")
	public Object indexDB_Main(HttpServletRequest request, HttpSession session, ModelMap map) {
		//数据字典
		map.addAttribute("caseTypeDict", ConstantValue.CASE_TYPE_CODE);
		return "/zzgl/requestion/list_requestion_db_main.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listDataDB_Main")
	public Object listDataDB_Main(HttpServletRequest request, HttpSession session, ModelMap map,
		@RequestParam(value = "keyWord", required = false) String keyWord,
		@RequestParam(value = "type", required = false) String type,
		@RequestParam(value = "happenTimeStr", required = false) String happenTimeStr,
		@RequestParam(value = "endTimeStr", required = false) String endTimeStr,
		@RequestParam(value = "wfCurOrg", required = false) String wfCurOrg,
		@RequestParam(value = "childState", required = false) String childState,
		@RequestParam(value = "state", required = false) String state,
		@RequestParam(value = "overTime", required = false) String overTime,
		
		int page, int rows) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		initParams();
		params.put("formTypeId", Long.parseLong(formTypeId));
		params.put("formTypeId_", Long.parseLong(formTypeId_));
		params.put("dbOrgId", userInfo.getOrgId().toString()); 
		
		params.put("orgCode", userInfo.getOrgCode());
		params.put("keyWord", keyWord);
		params.put("wfCurOrg", wfCurOrg);
		params.put("type", type);
		
		if(!StringUtils.isBlank(happenTimeStr)){
			happenTimeStr += " 00:00:00";
			params.put("happenTimeStr", happenTimeStr);
		}
		if(!StringUtils.isBlank(endTimeStr)){
			endTimeStr += " 23:59:59";
			params.put("endTimeStr", endTimeStr);
		}
		params.put("childState", childState);
		params.put("state", state);
		params.put("overTimeStatus", overTime);
		
		EUDGPagination pagination = requestionService.searchDBList_Main(page, rows, params);
		return pagination;
	}
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/indexDB_Task")
	public Object indexDB_Task(HttpServletRequest request, HttpSession session, ModelMap map) {
		//数据字典
		map.addAttribute("caseTypeDict", ConstantValue.CASE_TYPE_CODE);
		return "/zzgl/requestion/list_requestion_db_task.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listDataDB_Task")
	public Object listDataDB_Task(HttpServletRequest request, HttpSession session, ModelMap map,
		@RequestParam(value = "keyWord", required = false) String keyWord,
		@RequestParam(value = "type", required = false) String type,
		@RequestParam(value = "happenTimeStr", required = false) String happenTimeStr,
		@RequestParam(value = "endTimeStr", required = false) String endTimeStr,
		@RequestParam(value = "wfCurOrg", required = false) String wfCurOrg,
		@RequestParam(value = "overTime", required = false) String overTime,
		
		int page, int rows) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		initParams();
//		params.put("formTypeId", Long.parseLong(formTypeId));
		params.put("formTypeId_", Long.parseLong(formTypeId_));
		params.put("dbOrgId", userInfo.getOrgId().toString()); 
		
		params.put("orgCode", userInfo.getOrgCode());
		params.put("keyWord", keyWord);
		params.put("wfCurOrg", wfCurOrg);
		params.put("type", type);
		if(!StringUtils.isBlank(happenTimeStr)){
			happenTimeStr += " 00:00:00";
			params.put("happenTimeStr", happenTimeStr);
		}
		if(!StringUtils.isBlank(endTimeStr)){
			endTimeStr += " 23:59:59";
			params.put("endTimeStr", endTimeStr);
		}
		params.put("overTimeStatus", overTime);
		
		EUDGPagination pagination = requestionService.searchDBList_Task(page, rows, params);
		return pagination;
	}
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/indexJB")
	public Object indexJB(HttpServletRequest request, HttpSession session, ModelMap map) {
		//数据字典
		map.addAttribute("caseTypeDict", ConstantValue.CASE_TYPE_CODE);
		return "/zzgl/requestion/list_requestion_jb.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listDataJB")
	public Object listDataJB(HttpServletRequest request, HttpSession session, ModelMap map,
		@RequestParam(value = "keyWord", required = false) String keyWord,
		@RequestParam(value = "type", required = false) String type,
		@RequestParam(value = "happenTimeStr", required = false) String happenTimeStr,
		@RequestParam(value = "endTimeStr", required = false) String endTimeStr,
		@RequestParam(value = "wfCurOrg", required = false) String wfCurOrg,
		int page, int rows) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		initParams();
		params.put("formTypeId", Long.parseLong(formTypeId));
		params.put("formTypeId_", Long.parseLong(formTypeId_));
//		params.put("transactorId", userInfo.getUserId());
		params.put("transactorOrgId", userInfo.getOrgId());
		
		params.put("orgCode", userInfo.getOrgCode());
		params.put("keyWord", keyWord);
		params.put("type", type);
		params.put("wfCurOrg", wfCurOrg);
		params.put("happenTimeStr", happenTimeStr);
		params.put("endTimeStr", endTimeStr);
		EUDGPagination pagination = requestionService.searchJBList(page, rows, params);
		return pagination;
	}
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/indexJB_Task")
	public Object indexJB_Task(HttpServletRequest request, HttpSession session, ModelMap map) {
		//数据字典
		map.addAttribute("caseTypeDict", ConstantValue.CASE_TYPE_CODE);
		return "/zzgl/requestion/list_requestion_jb_task.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listDataJB_Task")
	public Object listDataJB_Task(HttpServletRequest request, HttpSession session, ModelMap map,
		@RequestParam(value = "keyWord", required = false) String keyWord,
		@RequestParam(value = "type", required = false) String type,
		@RequestParam(value = "happenTimeStr", required = false) String happenTimeStr,
		@RequestParam(value = "endTimeStr", required = false) String endTimeStr,
		@RequestParam(value = "wfCurOrg", required = false) String wfCurOrg,
		@RequestParam(value = "overTime", required = false) String overTime,
		
		int page, int rows) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		initParams();
		params.put("formTypeId", Long.parseLong(formTypeId));
		params.put("formTypeId_", Long.parseLong(formTypeId_));
//		params.put("transactorId", userInfo.getUserId());
		params.put("transactorOrgId", userInfo.getOrgId());
		
		params.put("orgCode", userInfo.getOrgCode());
		params.put("keyWord", keyWord);
		params.put("type", type);
		params.put("wfCurOrg", wfCurOrg);
		if(!StringUtils.isBlank(happenTimeStr)){
			happenTimeStr += " 00:00:00";
			params.put("happenTimeStr", happenTimeStr);
		}
		if(!StringUtils.isBlank(endTimeStr)){
			endTimeStr += " 23:59:59";
			params.put("endTimeStr", endTimeStr);
		}
		params.put("overTimeStatus", overTime);
		
		EUDGPagination pagination = requestionService.searchJBList_Task(page, rows, params);
		return pagination;
	}
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/indexAll")
	public Object indexAll(HttpServletRequest request, HttpSession session, ModelMap map) {
		//数据字典
		map.addAttribute("caseTypeDict", ConstantValue.CASE_TYPE_CODE);
		return "/zzgl/requestion/list_requestion_all.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listDataAll")
	public Object listDataAll(HttpServletRequest request, HttpSession session, ModelMap map,
		@RequestParam(value = "keyWord", required = false) String keyWord,
		@RequestParam(value = "type", required = false) String type,
		@RequestParam(value = "happenTimeStr", required = false) String happenTimeStr,
		@RequestParam(value = "endTimeStr", required = false) String endTimeStr,
		@RequestParam(value = "wfCurOrg", required = false) String wfCurOrg,
		int page, int rows) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		initParams();
		params.put("formTypeId", Long.parseLong(formTypeId));
		params.put("formTypeId_", Long.parseLong(formTypeId_));
		
		params.put("orgCode", userInfo.getOrgCode());
		params.put("keyWord", keyWord);
		params.put("type", type);
		params.put("wfCurOrg", wfCurOrg);
		params.put("happenTimeStr", happenTimeStr);
		params.put("endTimeStr", endTimeStr);
		EUDGPagination pagination = requestionService.searchAllList(page, rows, params);
		return pagination;
	}

	/**
	 * 列表页面
	 */
	@RequestMapping("/indexAll_Main")
	public Object indexAll_Main(HttpServletRequest request, HttpSession session, ModelMap map) {
		//数据字典
		map.addAttribute("caseTypeDict", ConstantValue.CASE_TYPE_CODE);
		return "/zzgl/requestion/list_requestion_all_main.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listDataAll_Main")
	public Object listDataAll_Main(HttpServletRequest request, HttpSession session, ModelMap map,
		@RequestParam(value = "keyWord", required = false) String keyWord,
		@RequestParam(value = "type", required = false) String type,
		@RequestParam(value = "happenTimeStr", required = false) String happenTimeStr,
		@RequestParam(value = "endTimeStr", required = false) String endTimeStr,
		@RequestParam(value = "wfCurOrg", required = false) String wfCurOrg,
		@RequestParam(value = "childState", required = false) String childState,
		@RequestParam(value = "state", required = false) String state,
		@RequestParam(value = "overTime", required = false) String overTime,
		
		int page, int rows) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		initParams();
		params.put("formTypeId", Long.parseLong(formTypeId));
//		params.put("formTypeId_", Long.parseLong(formTypeId_));
		
		params.put("orgCode", userInfo.getOrgCode());
		params.put("keyWord", keyWord);
		params.put("type", type);
		params.put("wfCurOrg", wfCurOrg);
		if(!StringUtils.isBlank(happenTimeStr)){
			happenTimeStr += " 00:00:00";
			params.put("happenTimeStr", happenTimeStr);
		}
		if(!StringUtils.isBlank(endTimeStr)){
			endTimeStr += " 23:59:59";
			params.put("endTimeStr", endTimeStr);
		}
		params.put("state", state);
		params.put("childState", childState);
		params.put("overTimeStatus", overTime);
		
		EUDGPagination pagination = requestionService.searchAllList_Main(page, rows, params);
		return pagination;
	}
	
	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id,String tasktype) {
		
		initParams();
		Requestion bo = null;
		Map<String, Object> params = new HashMap<String, Object>();
		
		if("1".equals(tasktype)){
			bo = requestionService.searchById(id,formTypeId);
			List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(bo.getReqId(), "", "requestion");
			map.addAttribute("taskList", initDetail(bo.getReqId(), taskList));
			List<Map<String, Object>> temp = toShowBackList_(id);
			if(temp!=null&&!temp.isEmpty()&&temp.size()>0){
				map.addAttribute("isBack", "1");
			}
		}else {
			ReqLink reqLink = reqLinkService.searchById(id);
			bo = requestionService.searchById(reqLink.getReqId(),formTypeId);
			List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(id, "", "linkageunit");
			map.addAttribute("taskList", initDetailLink(taskList));
			params.put("rluId", id);
		}
		
		params.put("reqId", bo.getReqId());
		params.put("formTypeId_", formTypeId_);
		List<ReqLink> childList = reqLinkService.searchList(params);
		map.addAttribute("childList", childList);
		map.addAttribute("REQ_ATTACHMENT_TYPE",REQ_ATTACHMENT_TYPE);
		map.addAttribute("REQ_LINK_ATTACHMENT_TYPE", REQ_LINK_ATTACHMENT_TYPE);
		map.addAttribute("bo", bo);
		return "/zzgl/requestion/detail_requestion.ftl";
	}

	private List<Map<String, Object>> initDetail(Long reqId,List<Map<String, Object>> taskList){
		
		for(Map<String, Object> task : taskList){
			String bizId = ((BigDecimal) task.get("TASK_ID")).toString();
			task.put("fileList", attachmentService.findByBizId(Long.parseLong(bizId), "REQ_WFTASK"));
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("formTypeId_", formTypeId_);
		params.put("reqId", reqId);
		params.put("status", "2");
		List<ReqLink> reqLinkList = reqLinkService.searchList(params);
		for(ReqLink re : reqLinkList){
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("TASK_NAME", "联动单位办理");
			temp.put("TRANSACTOR_NAME", re.getLinkageUnitName());
			temp.put("END_TIME", re.getUpdateTimeStr());
			temp.put("REMARKS", re.getOverview());
			params.clear();
			params.put("rluId", re.getRluId());
			params.put("formTypeId_", formTypeId_);
			List<Map<String,Object>> tempList = reqLinkService.queryWFtaskId(params);
			List<Attachment> fileList = new ArrayList<Attachment>();
			for(Map<String,Object> tempFile : tempList){
				String bizId = ((BigDecimal) tempFile.get("TASK_ID")).toString();
				fileList.addAll(attachmentService.findByBizId(Long.parseLong(bizId), "REQ_LINK_WFTASK"));
			}
			temp.put("fileList",fileList);
			
			taskList.add(temp);
		}
		return taskList;
	}
	
	private List<Map<String, Object>> initDetailLink(List<Map<String, Object>> taskList){
		
		for(Map<String, Object> task : taskList){
			String bizId = ((BigDecimal) task.get("TASK_ID")).toString();
			List<Attachment> attr = attachmentService.findByBizId(Long.parseLong(bizId), "REQ_LINK_WFTASK");
			task.put("fileList", attr);
		}
		return taskList;
	}
	
	
	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,Long id) {
		
		initParams();
		//设置默认网格信息
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		String gridName = defaultGridInfo.get(KEY_START_GRID_NAME).toString();
		if (id != null) {
			Requestion bo = requestionService.searchById(id,formTypeId);
			map.put("bo", bo);
		}
		map.put("infoOrgCode", infoOrgCode);
		map.put("gridName", gridName);
		return "/zzgl/requestion/form_requestion.ftl";
	}
	
	/**
	 * 到评价页面
	 */
	@RequestMapping("/todeal")
	public Object todeal(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		if (id != null) {
			ReqLink reqLink = reqLinkService.searchById(id);
			map.put("bo", reqLink);
		}
		return "/zzgl/requestion/deal_requestion.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/saveDeal")
	public Object saveDeal(HttpServletRequest request, HttpSession session, ModelMap map,
		Long rluId,String satisfaction,String detectedOverview) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		if (rluId != null) { //新增
			ReqLink reqLink = reqLinkService.searchById(rluId);
			reqLink.setSatisfaction(satisfaction);
			reqLink.setVisit(detectedOverview);
			reqLink.setState("6");
			reqLinkService.update(reqLink);
			updateMainChildState(reqLink.getReqId());
			result = "success";
		} 
		//status 1,message
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 到发起人评价页面
	 */
	@RequestMapping("/toOwnerDeal")
	public Object toOwnerDeal(HttpServletRequest request, HttpSession session, ModelMap map,Long id) {
		
		initParams();
		if (id != null) {
			Requestion bo = requestionService.searchById(id,formTypeId);
			map.put("bo", bo);
		}
		return "/zzgl/requestion/owner_deal_requestion.ftl";
	}
	/**
	 * 发起人评价保存
	 * @param request
	 * @param session
	 * @param map
	 * @param reqId
	 * @param satisfaction
	 * @param detectedOverview
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveOwnerDeal")
	public Object saveOwnerDeal(HttpServletRequest request, HttpSession session, ModelMap map,
		Long reqId,String satisfaction,String detectedOverview) {
		
		initParams();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		if (reqId != null) { //新增
			Requestion bo = requestionService.searchById(reqId,formTypeId);
			bo.setVisit(detectedOverview);
			bo.setSatisfaction(satisfaction);
			bo.setState("5");
			requestionService.update(bo);
			//更新网站群
			if("1".equals(bo.getSource())){//手机
				try {
					//TODO
					String zoneDomain = (String) App.ZONE.getDomain(session);
					System.out.println("-----SQ_ZONE_URL-----"+zoneDomain);
					JSONObject sendToResult = sendToOwnerDeal(bo,zoneDomain);
					String flag = sendToResult.get("state").toString();
					String message = sendToResult.get("msg").toString();
					System.out.println("----------message----------------"+message);
				} catch (Exception e) {
					System.out.println("-------------发起人评价更新网站群失败--------------");
					e.printStackTrace();
				}
			}
			
			result = "success";
		} 
		//status 1,message
		resultMap.put("result", result);
		return resultMap;
	}
	
	
	/**
	 * 查找处理人员信息
	 */
	@ResponseBody
	@RequestMapping("/searchDeal")
	public Object searchDeal(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		CorpLink corpLink = corpLinkService.searchByGridId(id);
		String result = "fail";
		if (corpLink != null) {
			result = "success";
			resultMap.put("corpLink", corpLink);
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 初始化办理表单页面
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/todo")
	public Object todo(HttpServletRequest request, HttpSession session, ModelMap map,Long id,
		@RequestParam(value = "instanceId", required = true) Long instanceId,
		@RequestParam(value = "taskId", required=true) String taskId,
		@RequestParam(value = "todoType", required=false) String todoType,
		@RequestParam(value = "tasktype", required=false) String tasktype) {
		
		initParams();
		//设置默认网格信息
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		String gridName = defaultGridInfo.get(KEY_START_GRID_NAME).toString();
		if (id != null) {
			Requestion bo = null;
			Map<String, Object> params = new HashMap<String, Object>();
			
			if("2".equals(tasktype)){//子流程
				ReqLink reqLink = reqLinkService.searchById(id);
				bo = requestionService.searchById(reqLink.getReqId(),formTypeId);
				List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(id, "", "linkageunit");
				map.addAttribute("taskList", initDetailLink(taskList));
				params.put("type", "linkageunit");
				if("0".equals(taskId)){
					taskId = workflowService.getCurTask(instanceId.toString());
					map.put("isParent2Do", "1");
				}
				Map<String, Object> rs = workflowService.initFlowInfo(taskId, instanceId, params);
				List<Node> taskNodes = (List<Node>) rs.get("taskNodes");
				if(!taskNodes.isEmpty()){
					map.put("nextNodeName", taskNodes.get(0).getNodeName());//下一环节
				}
				List<OperateBean> operateLists = (List<OperateBean>) rs.get("operateLists");
				if(!operateLists.isEmpty()){
					map.put("operateLists", operateLists.size());//驳回，提交数量
				}
				map.put("rluId", reqLink.getRluId());
				map.put("overTime", reqLink.getOverTime());
			}else {
				bo = requestionService.searchById(id,formTypeId);
				List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(bo.getReqId(), "", "requestion");
				map.addAttribute("taskList", initDetail(bo.getReqId(), taskList));
				if("2".equals(bo.getState())){
					todoType = "send";
				}
				if(!StringUtils.isBlank(todoType)){
					params.clear();
					params.put("reqId", bo.getReqId());
					params.put("formTypeId_", formTypeId_);
					List<ReqLink> childList = reqLinkService.searchList(params);
					map.addAttribute("childList", childList);
				}
				if("caseType_01".equals(bo.getType())){
					map.addAttribute("caseTypeTime", caseType_01);
				}else if("caseType_02".equals(bo.getType())){
					map.addAttribute("caseTypeTime", caseType_02);
				}
			}
			map.put("bo", bo);
			map.put("instanceId", instanceId);
			map.put("taskId", taskId);
		}
		map.addAttribute("REQ_ATTACHMENT_TYPE",REQ_ATTACHMENT_TYPE);
		map.addAttribute("REQ_LINK_ATTACHMENT_TYPE", REQ_LINK_ATTACHMENT_TYPE);
		map.put("imgDownPath", App.IMG.getDomain(session));//用于附件上传组件中的缩略图展示
		map.put("infoOrgCode", infoOrgCode);
		map.put("gridName", gridName);
		
		if(!StringUtils.isBlank(tasktype)){//子流程办理
			return "/zzgl/requestion/edit_reqLink.ftl";
		}else{
			if("1".equals(request.getParameter("isOwnerNode"))){
				map.addAttribute("caseTypeDict", ConstantValue.CASE_TYPE_CODE);
				return "/zzgl/requestion/owner_requestion.ftl";
			}else{
				if(!StringUtils.isBlank(todoType)){
					return "/zzgl/requestion/send_requestion.ftl";
				}
				return "/zzgl/requestion/edit_requestion.ftl";
			}
		}
	}
	
	/**
	 * 到评价页面
	 */
	@RequestMapping("/toShowReqLink")
	public Object toShowReqLink(HttpServletRequest request, HttpSession session, ModelMap map,
		Long rluId) {
		if (rluId != null) {
			List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(rluId, "", "linkageunit");
			map.addAttribute("taskList", initDetailLink(taskList));
		}
		return "/zzgl/requestion/deal_showReqLink.ftl";
	}
	
	private List<Map<String, Object>> toShowBackList_(Long reqId){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("reqId", reqId);
		params.put("formTypeId_", formTypeId_);
		params.put("state", "8");
		List<ReqLink> list = reqLinkService.showBackList(params);
		List<Map<String, Object>> temp = new ArrayList<Map<String,Object>>();
		for(ReqLink t : list){
			List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(t.getRluId(), "", "linkageunit");
			for(Map<String, Object> task : taskList){
				String name = (String) task.get("TASK_NAME");
				String type = task.get("OPERATE_TYPE").toString();
				if("单位办理".equals(name)&&"2".equals(type)){
					String endTime = task.get("END_TIME").toString();
					if(!StringUtils.isBlank(endTime)){
						task.put("UPDATETIMESTR", endTime.substring(0, 10));
					}
					task.put("LINKAGEUNITNAME", t.getLinkageUnitName());
					task.put("LINKMAN", t.getLinkMan());
					task.put("LINKMANTEL", t.getLinkManTel());
					temp.add(task);
				}
			}
		}
		
		return temp;
	}
	
	/**
	 * 到驳回信息页面
	 */
	@RequestMapping("/toShowBackList")
	public Object toShowBackList(HttpServletRequest request, HttpSession session, ModelMap map, Long reqId) {
		
		if (reqId != null) {
			map.addAttribute("taskList", toShowBackList_(reqId));
		}
		
		return "/zzgl/requestion/deal_showBackList.ftl";
	}
	
	/**
	 * 手机请求提交保存数据
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	@ResponseBody
	@RequestMapping("/appSave")
	public Object appSave(HttpServletRequest request,HttpServletResponse response,HttpSession session, ModelMap map,
		@RequestParam(value = "formJson", required = false) String formJson,
		Requestion bo1,String fileList) throws JsonParseException, JsonMappingException, IOException {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Requestion bo = objectMapper.readValue(formJson, Requestion.class);
		List<Attachment> fileList_ = new ArrayList<Attachment>();
		if(!StringUtils.isBlank(fileList)){
			JSONArray jsonArray = JSONArray.fromObject(fileList);//把String转换为json   
			fileList_ = JSONArray.toList(jsonArray,Attachment.class);//这里的t是Class<T>   
		}
		
		String result = "fail";
		if (bo.getReqId() == null) { //新增
			resultMap.put("userId", bo.getUserId());
			resultMap.put("userName", bo.getUserName());
			if(!StringUtils.isBlank(bo.getReqObjCode())){
				OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoService.selectOrgSocialInfoByOrgCode(bo.getReqObjCode());
				resultMap.put("orgId", orgSocialInfoBO.getOrgId());
				resultMap.put("orgName", orgSocialInfoBO.getOrgName());
				bo.setOrgId(orgSocialInfoBO.getOrgId());
				bo.setOrgName(orgSocialInfoBO.getOrgName());
			}
			
			resultMap.put("type", "requestion");
			String userIds = "";
			String orgIds = "";
			try {
				OrgSocialInfoBO orgSocialInfo= orgSocialInfoService.selectOrgSocialInfoByOrgCode(ORG_CODE);
				orgIds = orgSocialInfo.getOrgId().toString();
			} catch (Exception e) {
				e.printStackTrace();
				resultMap.put("status", "0");
				resultMap.put("message", "中心组织获取失败");
				System.out.println("-------------中心组织获取失败---------------ORG_CODE="+ORG_CODE);
			}
			try {
				List<cn.ffcs.uam.bo.UserInfo> userInfoList = userManageService.findUserByOrgCode(ORG_CODE);
				if(!userInfoList.isEmpty()){
					userIds = userInfoList.get(0).getUserId().toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultMap.put("status", "0");
				resultMap.put("message", "中心人员获取失败");
				System.out.println("-------------中心组织获取失败----------------");
			}
			
			resultMap.put("userIds", userIds);
			resultMap.put("curOrgIds", orgIds);
			
			String code = null;
			try {
				code = numberConfigureService.getNumber(ORG_CODE, "09");
			} catch (Exception e) {
				resultMap.put("status", "0");
				resultMap.put("message", "编号生成失败");
				e.printStackTrace();
			}
			if(!StringUtils.isBlank(code)){
				bo.setCode(code);
				bo.setSource("1");
				bo.setState("1");
				resultMap = requestionService.insert(bo,resultMap);
			}
			String status = (String) resultMap.get("status");
			if("1".equals(status)&&!fileList_.isEmpty()&&fileList_.size()>0){
				//http附件下载再上传
				if(resultMap.get("formId")!=null){
					Long keyId = Long.parseLong(resultMap.get("formId").toString());
					for(Attachment temp : fileList_){
						try {
							downFile(keyId, temp.getFileName(), temp.getFilePath());
						} catch (Exception e) {
							System.out.println("附件下载失败");
							e.printStackTrace();
						}
					}
				}
			}
			result = "success";
		}else{//退回提交
			bo.setState("1");
			requestionService.update(bo);
			
			resultMap.put("formId", bo.getReqId());
			resultMap.put("type", "requestion");
			resultMap.put("userId", bo.getUserId());
			resultMap.put("userName", bo.getUserName());
			resultMap.put("orgId", bo.getOrgId());
			resultMap.put("orgName", bo.getOrgName());
			
			String userIds = "";
			String orgIds = "";
			try {
				OrgSocialInfoBO orgSocialInfo= orgSocialInfoService.selectOrgSocialInfoByOrgCode(ORG_CODE);
				orgIds = orgSocialInfo.getOrgId().toString();
			} catch (Exception e) {
				e.printStackTrace();
				resultMap.put("status", "0");
				resultMap.put("message", "中心组织获取失败");
				System.out.println("-------------中心组织获取失败---------------ORG_CODE="+ORG_CODE);
			}
			try {
				List<cn.ffcs.uam.bo.UserInfo> userInfoList = userManageService.findUserByOrgCode(ORG_CODE);
				if(!userInfoList.isEmpty()){
					userIds = userInfoList.get(0).getUserId().toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultMap.put("status", "0"); 
				resultMap.put("message", "中心人员获取失败");
				System.out.println("-------------中心组织获取失败----------------");
			}
			
			resultMap.put("userIds", userIds);
			resultMap.put("curOrgIds", orgIds);
			
			if(!fileList_.isEmpty()&&fileList_.size()>0){
				//http附件下载再上传
				Long keyId = bo.getReqId();
				for(Attachment temp : fileList_){
					try {
						downFile(keyId, temp.getFileName(), temp.getFilePath());
					} catch (Exception e) {
						System.out.println("附件下载失败");
						e.printStackTrace();
					}
				}
			}
			try {
				resultMap = workflowService.submitRequestionFlowForMob(resultMap);
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//status 1,message
		resultMap.put("result", result);
//		PrintWriter writer = response.getWriter();
//		writer.write(objectMapper.writeValueAsString(resultMap));
//		writer.close();
		JSONObject xx = JSONObject.fromObject(resultMap);
		return xx.toString();
	}
	
	@ResponseBody
	@RequestMapping("/appUpdateOwnerDeal")
	public Object appUpdateOwnerDeal(HttpServletRequest request,HttpServletResponse response,HttpSession session, ModelMap map,
		@RequestParam(value = "formJson", required = false) String formJson) throws JsonParseException, JsonMappingException, IOException {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		Requestion temp = objectMapper.readValue(formJson, Requestion.class);
		
		String result = "fail";
		if(null!=temp.getReqId()){
			try {
				formTypeId = workflowService.getFormTypeId("requestion");
				Requestion bo = requestionService.searchById(temp.getReqId(),formTypeId);
				bo.setVisit(temp.getVisit());
				bo.setSatisfaction(temp.getSatisfaction());
				bo.setState("5");
				requestionService.update(bo);
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//status 1,message
		resultMap.put("result", result);
//		PrintWriter writer = response.getWriter();
//		writer.write(objectMapper.writeValueAsString(resultMap));
//		writer.close();
		JSONObject xx = JSONObject.fromObject(resultMap);
		return xx.toString();
	}
	
	/**
	 * 手机请求提交保存数据
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@ResponseBody
	@RequestMapping("/appDetail")
	public Object appDetail(HttpServletRequest request,HttpServletResponse response,HttpSession session, ModelMap map,
			Long reqId) throws JsonParseException, JsonMappingException, IOException {
		
		initParams();
		String result = "fail";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(reqId!=null){
			Requestion requestion = requestionService.searchByKeyId(reqId);
			if(requestion!=null){
				resultMap.put("state", requestion.getState());
				String fileDomain = funConfigurationService.getAppDomain("$SQ_FILE_DOMAIN");
				String imageDomain = funConfigurationService.getAppDomain("$IMG_DOMAIN");
				System.out.println("----------------imageDomain----------------"+imageDomain);
				resultMap.put("fileDomain", fileDomain);
				resultMap.put("imageDomain", imageDomain);
				List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(requestion.getReqId(), "", "requestion");
				resultMap.put("taskList", initDetail(requestion.getReqId(), taskList)); 
			}
			result = "success";
//			JSONObject json = JSONObject.fromObject(requestion);
//	        String strJson=json.toString();
		}
		resultMap.put("result", result);
//		PrintWriter writer = response.getWriter();
//		writer.write(objectMapper.writeValueAsString(resultMap));
//		writer.close();
		JSONObject xx = JSONObject.fromObject(resultMap);
		return xx.toString();
	}
	
	/**
	 * 提交归档
	 */
	@SuppressWarnings("unused")
	@ResponseBody
	@RequestMapping("/submit")
	public Object submit(HttpServletRequest request, HttpSession session, ModelMap map,
		@RequestParam(value = "buttonType", required=true) String buttonType,
		@RequestParam(value = "taskId", required=true) String taskId,
		@RequestParam(value = "nextNodeName", required=true) String nextNodeName,
		Long reqId,String detectedOverview,String gridId,String gridName,
		String limitDate,String afterDate,String instanceId,
		@RequestParam(value = "attachmentId", required = false) Long[] attachmentIds,
		Requestion tempBo) {
		
		initParams();
		//buttonType(1不予受理,2派发,3归档,4退回)
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String result = "fail";
		if("2".equals(buttonType)){//新增子流程
			if(!StringUtils.isBlank(gridId)&&!StringUtils.isBlank(gridName)){//组织
				String linkageUnitIds[] = gridId.split(",");
				String linkageUnitNames[] = gridName.split(",");
				String limitDates[] = limitDate.split(",");//期限
				for(int i=0; i<linkageUnitIds.length; i++){
					if(!StringUtils.isBlank(linkageUnitIds[i])){
						Date overTime = holidayInfoService.getWorkDateByAfterWorkDay(new Date(), Integer.parseInt(limitDates[i]));
						
						ReqLink reqLink = new ReqLink(Long.parseLong(linkageUnitIds[i]),linkageUnitNames[i],
								reqId, userInfo.getUserId(), userInfo.getPartyName(),limitDates[i],DateUtils.formatDate(overTime, "yyyy-MM-dd"));
						resultMap.put("userId", userInfo.getUserId());
						resultMap.put("userName", userInfo.getPartyName());
						resultMap.put("type", "linkageunit");
//						resultMap.put("userIds", userInfo.getUserId());//联动单位人员
						Map<String, Object> rs = queryUserAndOrg(Long.parseLong(linkageUnitIds[i]));
						resultMap.put("userIds", rs.get("userIds"));
						resultMap.put("curOrgIds", rs.get("curOrgIds"));
						resultMap.put("orgId", userInfo.getOrgId());
						resultMap.put("orgName", userInfo.getOrgName());
						
						resultMap.put("parentId", instanceId);
						resultMap.put("remarks", detectedOverview);
						try {
							reqLinkService.insert(reqLink, resultMap);
							result = "success";
						} catch (Exception e) {
							result = "fail";
							e.printStackTrace();
							System.out.println("-----子流程提交失败-----");
						}
						resultMap.clear();
					}
				}
				Requestion requestion = requestionService.searchById(reqId,formTypeId);
//				requestion.setDesc(detectedOverview);
				requestion.setState("2");
				requestionService.update(requestion);
				
				if(null!=attachmentIds){
					saveAtta(reqId, REQ_ATTACHMENT_TYPE, attachmentIds);
				}
				//案卷在派发后：推送消息给诉求人（政企互联APP端），告知案卷被受理
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("msgContent", "标题："+requestion.getTitle()+"的案件正在被受理中...");
				params.put("userIds", requestion.getUserId().toString());
				pushMsg2Mobile(session, params);
			}
		}else if("4".equals(buttonType)){//退回发起人
			try {
				Requestion requestion = null;
				UserBO user = new UserBO();
				user.setUserId(userInfo.getUserId());
				user.setPartyName(userInfo.getPartyName());
				OrgSocialInfoBO org = new OrgSocialInfoBO();
				org.setOrgId(userInfo.getOrgId());
				org.setOrgName(userInfo.getOrgName());
				workflowService.rejectWorkFlow(taskId, detectedOverview, instanceId, user, org);
				
				requestion = requestionService.searchById(reqId,formTypeId);
				requestion.setState("3");
				requestionService.update(requestion);
				
				if(null!=attachmentIds){
					saveAtta(Long.parseLong(taskId), "REQ_WFTASK", attachmentIds);
				}
				result = "success";
				//案卷在派发后：推送消息给诉求人（政企互联APP端），告知案卷被受理
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("msgContent", "标题："+requestion.getTitle()+"的案件已被退回...");
				params.put("userIds", requestion.getUserId().toString());
				pushMsg2Mobile(session, params);
				//网站群
				try {
					String zoneDomain = (String) App.ZONE.getDomain(session);
					System.out.println("-----SQ_ZONE_URL-----"+zoneDomain);
					JSONObject sendToResult = sendTo(requestion,zoneDomain,"4");
					String flag = sendToResult.get("state").toString();
					String message = sendToResult.get("msg").toString();
					System.out.println("----------message----------------"+message);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("----------网站群更新归档失败----------------");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("-----工作流驳回失败-----");
			}
		}else if("5".equals(buttonType)){//发起人提交
			try {
				if(tempBo.getReqId()!=null){
					tempBo.setState("1");
					requestionService.update(tempBo);
					
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("formId", tempBo.getReqId());
					params.put("type", "requestion");
					params.put("userId", userInfo.getUserId());
					params.put("userName", userInfo.getPartyName());
					params.put("orgId", userInfo.getOrgId());
					params.put("orgName", userInfo.getOrgName());
					
					String userIds = "";
					String orgIds = "";
					try {
						OrgSocialInfoBO orgSocialInfo= orgSocialInfoService.selectOrgSocialInfoByOrgCode(ORG_CODE);
						orgIds = orgSocialInfo.getOrgId().toString();
					} catch (Exception e) {
						e.printStackTrace();
						resultMap.put("status", "0");
						resultMap.put("message", "中心组织获取失败");
						System.out.println("-------------中心组织获取失败---------------ORG_CODE="+ORG_CODE);
					}
					try {
						List<cn.ffcs.uam.bo.UserInfo> userInfoList = userManageService.findUserByOrgCode(ORG_CODE);
						if(!userInfoList.isEmpty()){
							userIds = userInfoList.get(0).getUserId().toString();
						}
					} catch (Exception e) {
						e.printStackTrace();
						resultMap.put("status", "0"); 
						resultMap.put("message", "中心人员获取失败");
						System.out.println("-------------中心组织获取失败----------------");
					}
					
					params.put("userIds", userIds);
					params.put("curOrgIds", orgIds);
					
					if(null!=attachmentIds){
						saveAtta(tempBo.getReqId(), REQ_ATTACHMENT_TYPE, attachmentIds);
					}
					
					workflowService.submitRequestionFlowForMob(params);
				}
				
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("-----工作流发起人提交失败-----");
			}
		}else{
			Requestion requestion = null;
			try {
				resultMap.put("taskId", taskId);
				resultMap.put("nextNodeName", nextNodeName);
				resultMap.put("userId", userInfo.getUserId());
				resultMap.put("userName", userInfo.getPartyName());
				resultMap.put("remarks", detectedOverview);
				resultMap.put("orgId", userInfo.getOrgId());
				resultMap.put("orgName", userInfo.getOrgName());
				
				resultMap =  workflowService.submitRequestionFlow(resultMap);
				requestion = requestionService.searchById(reqId,formTypeId);
				requestion.setState("4");
				requestion.setChildState("99");
				requestionService.update(requestion);
				if(null!=attachmentIds){
					saveAtta(Long.parseLong(taskId), "REQ_WFTASK", attachmentIds);
				}
				if("1".equals(buttonType)){//不予受理
					//告知有事件不予受理，及不予受理原因。
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("msgContent", "标题："+requestion.getTitle()+"的案件不予受理,理由如下："+detectedOverview);
					params.put("userIds", requestion.getUserId().toString());
					pushMsg2Mobile(session, params);
				}else if("3".equals(buttonType)){//归档
					//专业部门处理反馈，提交成功后：推送消息给诉求人（政企互联APP端），告知具体事件已处理反馈；
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("msgContent", "标题："+requestion.getTitle()+"的案件已处理反馈完成");
					params.put("userIds", requestion.getUserId().toString());
					pushMsg2Mobile(session, params);
				}
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("-----工作流提交失败-----");
			}
			//网站群
			try {
				String zoneDomain = (String) App.ZONE.getDomain(session);
				System.out.println("-----SQ_ZONE_URL-----"+zoneDomain);
				JSONObject sendToResult = sendTo(requestion,zoneDomain,"3");
				String flag = sendToResult.get("state").toString();
				String message = sendToResult.get("msg").toString();
				System.out.println("----------message----------------"+message);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("----------网站群更新归档失败----------------");
			}
		}
		
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		Requestion bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		if (bo.getReqId() == null) { //新增
			Long id = requestionService.insert(bo);
			if (id != null && id > 0) {
				result = "success";
			};
		} else { //修改
			boolean updateResult = requestionService.update(bo);
			if (updateResult) {
				result = "success";
			}
		}
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		Requestion bo) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = requestionService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping("/getWorkingDay")
	public Object getWorkingDay(HttpServletRequest request, HttpSession session, ModelMap map,
			int workingDay) {
		
		String result = "fail";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Date now =  new Date();
			Date date = holidayInfoService.getWorkDateByAfterWorkDay(now, workingDay);
			resultMap.put("afterDate", DateUtils.formatDate(date, "yyyy-MM-dd"));
			result = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resultMap.put("result", result);
		return resultMap;
	}
	
	
	/**
	 * 提交归档
	 */
	@ResponseBody
	@RequestMapping("/submitChild")
	public Object submitChild(HttpServletRequest request, HttpSession session, ModelMap map,
		@RequestParam(value = "buttonType", required=true) String buttonType,
		@RequestParam(value = "taskId", required=true) String taskId,
		@RequestParam(value = "nextNodeName", required=true) String nextNodeName,
		Long rluId,String detectedOverview,String instanceId,
		@RequestParam(value = "attachmentId", required = false) Long[] attachmentIds) {
		
		//buttonType(1不予受理,2派发,3归档)
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String result = "fail";
		ReqLink reqLink = null;
		
		if("2".equals(buttonType)){//提交
			resultMap.put("taskId", taskId);
			resultMap.put("nextNodeName", nextNodeName);
			resultMap.put("userId", userInfo.getUserId());
			resultMap.put("userName", userInfo.getPartyName());
			resultMap.put("orgId", userInfo.getOrgId());
			resultMap.put("orgName", userInfo.getOrgName());
			resultMap.put("remarks", detectedOverview);
			try {
				reqLink = reqLinkService.searchById(rluId);
				if(nextNodeName.indexOf("end")>-1){
					reqLink.setOverview(detectedOverview);
					reqLink.setState("9");
					reqLink.setUpdateTime(new Date());
				}else{
					reqLink.setState("7");
					Map<String, Object> rs = queryUserAndOrg(reqLink.getLinkageUnitId());
					resultMap.put("userIds", rs.get("userIds"));
					resultMap.put("curOrgIds", rs.get("curOrgIds"));
				}
				resultMap =  workflowService.submitRequestionFlow(resultMap);
				reqLinkService.update(reqLink);
				
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{//驳回
			try {
				UserBO user = new UserBO();
				user.setUserId(userInfo.getUserId());
				user.setPartyName(userInfo.getPartyName());
				OrgSocialInfoBO org = new OrgSocialInfoBO();
				org.setOrgId(userInfo.getOrgId());
				org.setOrgName(userInfo.getOrgName());
				workflowService.rejectWorkFlow(taskId, detectedOverview, instanceId, user, org);
				reqLink = reqLinkService.searchById(rluId);
				reqLink.setState("8");
				reqLinkService.update(reqLink);
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("-----工作流驳回失败-----");
			}
		}
		
		if(null!=attachmentIds){
			saveAtta(Long.parseLong(taskId), "REQ_LINK_WFTASK", attachmentIds);
		}
		
		if(reqLink!=null){
			updateMainChildState(reqLink.getReqId());
		}
		
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/delChild")
	public Object delChild(HttpServletRequest request, HttpSession session, ModelMap map,
		ReqLink bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		bo = reqLinkService.searchById(bo.getRluId());
		Long reqId = bo.getReqId();
		boolean delResult = reqLinkService.delete(bo);
		if (delResult) {
			updateMainChildState(reqId);
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}
	/**
	 * 更新网站群状态
	 * @param requestion
	 * @param urlDomain
	 * @param status(3归档，4退回)
	 * @return
	 * @throws Exception
	 */
	private JSONObject sendTo(Requestion requestion,String urlDomain,String status) throws Exception{
		
		String url = urlDomain+"/mobile/appeal/v2/out/updateReqState.jhtml?reqId="+requestion.getKeyId().toString()+"&status="+status;
		JSONObject json = HttpUtil.invokeOutInterface(url, "POST", "", null, null);
		return json;
	}
	/**
	 * 更新网站群发起人评价信息
	 * @param requestion
	 * @param urlDomain
	 * @param status
	 * @return
	 * @throws Exception
	 */
	private JSONObject sendToOwnerDeal(Requestion requestion,String urlDomain) throws Exception{
		
		String url = urlDomain
				+ "/mobile/appeal/v2/out/visitNotify.jhtml?reqId="
				+ requestion.getKeyId().toString() + "&satisfaction="
				+ requestion.getSatisfaction() + "&visit="
				+ requestion.getVisit();
		JSONObject json = HttpUtil.invokeOutInterface(url, "POST", "", null, null);
		return json;
	}
	
	
	/**
	 * 向手机端推送消息
	 * @param session
	 * @param jsoncallback
	 * @param params
	 * 			msgContent	消息内容
	 * 			userIds		消息接收人员id，以英文逗号分隔
	 * @return
	 */
	private Object pushMsg2Mobile(HttpSession session, Map<String, Object> params) {
		
//		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String msgContent = "",
			   msgUrl = "",
			   userIds = "",
			   msgWrong = "",
			   MOBILE_PUSH_TYPE = "1",//1：web，2：安卓
			   isSendMsg2MobileStr = "true";
//			   isSendMsg2MobileStr = funConfigurationService.turnCodeToValue(ConstantValue.SEND_MSG_AND_SMS_CFG, ConstantValue.SEND_MSG_AND_SMS_CFG_TRIGGER_4_MOBILE_MSG, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		Set<Long> userIdSet = new HashSet<Long>();
		List<Long> userIdList = new ArrayList<Long>();
		List<Push> push2MobileList = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean result = true,
				isSendMsg2Mobile = Boolean.valueOf(isSendMsg2MobileStr);
		
		if(isSendMsg2Mobile) {//开启消息推送功能后，继续后续的操作
			if(CommonFunctions.isNotBlank(params, "msgContent")) {
				try {
					msgContent = java.net.URLDecoder.decode(params.get("msgContent").toString(), "utf-8").trim();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if(CommonFunctions.isNotBlank(params, "msgUrl")) {
				msgUrl = params.get("msgUrl").toString();
			}
			if(CommonFunctions.isNotBlank(params, "userIds")) {
				userIds = params.get("userIds").toString();
				
				if(StringUtils.isNotBlank(userIds)) {
					String[] userIdArray = userIds.split(",");
					Long userId = -1L;
					
					for(String userIdStr : userIdArray) {
						try {
							userId = Long.valueOf(userIdStr);
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
						
						if(userId > 0) {
							userIdSet.add(userId);
						}
					}
					
					if(userIdSet.size() > 0) {
						push2MobileList = new ArrayList<Push>();
						String msgTitle = msgContent;
						if(StringUtils.isNotBlank(msgTitle) && msgTitle.length() > 7) {
							msgTitle = msgTitle.substring(0, 7);
						}
						
						for(Long _userId : userIdSet) {
							Push mobilePush = new Push();
							
							mobilePush.setUserId(_userId.toString());
							mobilePush.setTitle(msgTitle);
							mobilePush.setContent(msgContent);
							mobilePush.setType(MOBILE_PUSH_TYPE);//url不为空时，需要传递该属性
							
							if(StringUtils.isNotBlank(msgUrl)) {
								mobilePush.setUrl(msgUrl);
//								mobilePush.setType(MOBILE_PUSH_TYPE);//url不为空时，需要传递该属性
							}
							
							push2MobileList.add(mobilePush);
							
							userIdList.add(_userId);
						}
					}
				}
			}
			
			if(userIdList.size() > 0) {
				List<Push> resultPushList = null;
				
				if(push2MobileList != null && push2MobileList.size() > 0) {
					try {
						resultPushList = pushService.pushByUserId(push2MobileList);
					} catch(RpcException e) {
						result = false;
						e.printStackTrace();
					}
				}
				
				if(resultPushList != null && resultPushList.size() > 0) {
					result = false;
					msgWrong = "手机消息推送失败！";
					
					logger.error("手机消息推送错误，begin：==============================================================================");
					
					for(Push resultPush : resultPushList) {
						
						logger.error(resultPush.getUserId() + "  " + resultPush.getResult() + "  " + resultPush.getFailureReason());
					}
					
					logger.error("手机消息推送错误，end：==============================================================================");
				}
			} else {
				result = false;
				msgWrong = "没有可推送手机消息的人员！";
			}
		} else {
			logger.warn("手机消息推送功能未开启！功能编码为：" + ConstantValue.SEND_MSG_AND_SMS_CFG + "  触发条件为：" + ConstantValue.SEND_MSG_AND_SMS_CFG_TRIGGER_4_MOBILE_MSG);
		}
		
		resultMap.put("result", result);
		resultMap.put("msg", msgWrong);
		
//		if(StringUtils.isNotBlank(jsoncallback)) {
//			jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
//		} else {
//			jsoncallback = JsonHelper.getJsonString(resultMap);
//		}
		
		return resultMap;
	}
	
	
	@SuppressWarnings("unused")
	@ResponseBody
	@RequestMapping("/test")
	public Object test(HttpServletRequest request,HttpServletResponse response,HttpSession session,
			ModelMap map) throws Exception {
		
		Long keyId = 86L;
//		String fileName = "测试图片2.jpg";
		String urlstr = "http://img.sq.aishequ.org/wechat/common/2017/12/26/wechat-common-79941a541f4a4e6585eee85d7fde778e.jpg";
//		String urlstr = "http://filedev.fjsq.org/js/swfupload/images/icon/icon_png.gif";
		try{
//			URL url = new URL(urlstr);
//			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//			conn.setConnectTimeout(3*1000);
//			//防止屏蔽程序抓取而返回403错误
//			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//			//得到输入流
//			InputStream inputStream = conn.getInputStream();
//			//获取自己数组
//			byte[] buffer = new byte[1024];
//			int len = 0;
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			while((len = inputStream.read(buffer)) != -1) {
//				bos.write(buffer, 0, len);
//			}
//			bos.close();
//			byte[] getData  = bos.toByteArray();
			
			InputStream fis = null;
			File file = new File(urlstr);
	        URL url = new URL(urlstr);  
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	        conn.setConnectTimeout(10 * 1000);  
	        conn.setRequestMethod("GET");  
	        conn.connect();
			fis = conn.getInputStream();
	        byte[] getData = new byte[fis.available()];
			
			String path = fileUploadService.uploadSingleFile("icon_png.gif", getData, ConstantValue.RESOURSE_DOMAIN_KEY, "eventPhoto");
    	}catch(Exception e){
    		e.printStackTrace();
    	}
//		InputStream fis = null;
//		if(StringUtils.isEmpty(urlstr)) return null;
//		try {
//			URL url = new URL(urlstr);
//			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
//			httpConn.setRequestMethod("POST");
//			httpConn.setRequestProperty("Charset", "UTF-8");
//			httpConn.setConnectTimeout(10 * 1000);  
//			httpConn.connect();
//			
//			fis = httpConn.getInputStream();
//			byte[] buffer = new byte[fis.available()];
//			String filePath = this.fileUploadService.uploadSingleFile(fileName, buffer, ConstantValue.RESOURSE_DOMAIN_KEY, "requestionAtta");
//			
//			Attachment attachment = new Attachment();
//			attachment.setAttachmentType("REQ_ATTACHMENT_TYPE");
//			attachment.setBizId(keyId);
//			attachment.setCreateTime(new Date());
//			attachment.setEventSeq("1");
//			attachment.setFileName(fileName);
//			attachment.setFilePath(filePath);
//			attachment.setStatus("001");
//			
//			attachmentService.saveAttachment(attachment);
//			fis.close();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//			fis.close();
//		}
		return null;
	}
	
	@SuppressWarnings("null")
	private Boolean downFile(Long keyId, String fileName, String urlstr) throws IOException{
		
		InputStream fis = null;
		if(StringUtils.isEmpty(urlstr)) return null;
		try {
//			urlstr = "http://img.sq.aishequ.org/wechat/common/2017/12/26/wechat-common-79941a541f4a4e6585eee85d7fde778e.jpg";
//			File file = new File(urlstr); 
//	        URL url = new URL(urlstr);  
//	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
//	        conn.setConnectTimeout(10 * 1000);  
//	        conn.setRequestMethod("GET");  
//	        conn.connect();
//			fis = conn.getInputStream();
//	        byte[] buffer = new byte[fis.available()];
			InputStream inputStream = HttpUtil.returnBitMap(urlstr);
			System.out.println("---------inputStream----------"+inputStream);
			byte[] bytes = HttpUtil.readInputStream(inputStream);
//			String filePath = this.fileUploadService.uploadSingleFile(new Date().getTime()+".gif", bytes, "zhsq_event", "attachment");
			String filePath = this.fileUploadService.uploadSingleFile(fileName, bytes, "zhsq_event", "attachment");
			
			Attachment attachment = new Attachment();
			attachment.setAttachmentType(REQ_ATTACHMENT_TYPE);
			attachment.setBizId(keyId);
			attachment.setCreateTime(new Date());
			attachment.setEventSeq("1");
			attachment.setFileName(fileName);
			attachment.setFilePath(filePath);
			attachment.setStatus("001");
			
			attachmentService.saveAttachment(attachment);
			fis.close();
		} catch (Exception e) {
			System.out.println("----------app下载附件失败----------------urlstr="+urlstr);
			e.printStackTrace();
			fis.close();
		}
		
		return null;
	}
	
	private void initParams() {
		
		if(StringUtils.isBlank(formTypeId)){
			formTypeId = workflowService.getFormTypeId("requestion");
		}
		if(StringUtils.isBlank(workflowId)){
			workflowId = workflowService.getWorkflowId("requestion");
		}
		if(StringUtils.isBlank(formTypeId_)){
			formTypeId_ = workflowService.getFormTypeId("linkageunit");
		}
		if(StringUtils.isBlank(workflowId_)){
			workflowId_ = workflowService.getWorkflowId("linkageunit");
		}
	}
	
	/**
	 * 保存附件信息
	 * @param id 主表ID
	 * @param bizType 附件类型
	 * @param attachmentIds 附件ID
	 */
	private void saveAtta(Long id,String bizType,Long[] attachmentIds) {
		if(attachmentIds!=null && attachmentIds.length>0){
			attachmentService.updateBizId(id, bizType, attachmentIds);
		}
	}
	
	private Map<String, Object> queryUserAndOrg(Long orgId){
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String userIds = "";
		OrgSocialInfoBO orgSocialInfo = orgSocialInfoService.findByOrgId(orgId);
		String orgIds = orgSocialInfo.getOrgId().toString();
		List<cn.ffcs.uam.bo.UserInfo> userInfoList = userManageService.findUserByOrgCode(orgSocialInfo.getOrgCode());
		if(!userInfoList.isEmpty()){
			userIds = userInfoList.get(0).getUserId().toString();
		}
//		for(UserInfo userInfo : userInfoList){
//			userIds += userInfo.getUserId() + ",";
//			orgIds += orgSocialInfo.getOrgId() + ",";
//		}
//		if(!StringUtils.isBlank(userIds)){
//			userIds = userIds.substring(0, userIds.length()-1);
//			orgIds = orgIds.substring(0, orgIds.length()-1);
//		}
		
		resultMap.put("userIds", userIds);
		resultMap.put("curOrgIds", orgIds);
		
		return resultMap;
	}

	private void updateMainChildState(Long reqId){
		
		initParams();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("reqId", reqId);
		params.put("formTypeId_", formTypeId_);
		List<ReqLink> childList = reqLinkService.searchList(params);
		int sum_8 = 0;
		int sum_9 = 0;
		for(ReqLink temp : childList){
			if("8".equals(temp.getState())){
				sum_8++;
			}else if("9".equals(temp.getState())){
				sum_9++;
			}
		}
		Requestion bo = requestionService.searchById(reqId,formTypeId);
		if((sum_8+sum_9)>0){
			if(sum_8>0&&sum_9>0){
				bo.setChildState("3");
			}else if(sum_8>0){
				bo.setChildState("1");
			}else if(sum_9>0){
				bo.setChildState("2");
			}
		}else{
			bo.setChildState("99");
		}
		requestionService.update(bo);
	}
}