package cn.ffcs.zhsq.map.jiangyinPlatform.controller;


import cn.ffcs.common.Pagination;
import cn.ffcs.resident.bo.CiRsCriteria;
import cn.ffcs.resident.service.CiRsService;
import cn.ffcs.resident.service.HiLogoutService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.shequ.zzgl.service.globalEyes.MonitorService;
import cn.ffcs.shequ.zzgl.service.grid.IGridAdminService;
import cn.ffcs.sms.bo.SendResult;
import cn.ffcs.sms.service.SendSmsService;
import cn.ffcs.sms.service.SmsLogService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OutUserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.controller.EventDisposalController;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.jiangyinPlatform.addressBook.service.IJYAddressBookService;
import cn.ffcs.zhsq.jiangyinPlatform.communicationLog.service.ICommunicationLogService;
import cn.ffcs.zhsq.jiangyinPlatform.recentContact.service.IRecentContactService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.map.jiangYinPlatform.service.IJiangYinPlatformService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.addressBook.JYAddressBook;
import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.communicationLog.CommunicationLog;
import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.recentContact.RecentContact;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfo;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  	????????????
 * @author sunjian
 * */
@Controller
@RequestMapping("/zhsq/map/jiangYinPlatform")
public class JiangYinPlatformController extends ZZBaseController {
	
	@Autowired
	private IJYAddressBookService jyAddressBookService; //??????????????????????????????
	@Autowired
	private ICommunicationLogService communicationLogService; //?????????????????????????????????
	@Autowired
	private IRecentContactService recentContactService; //????????????????????????????????????
	@Autowired
	private IEvent4WorkflowService event4WorkflowService;
	@Autowired
	private IGridAdminService gridAdminService;
	@Autowired
	private MonitorService monitorService;
	@Autowired
	private SendSmsService sendSmsService;  //????????????
	@Autowired
	private SmsLogService smsLogService;
	@Autowired
	private UserManageOutService userManageOutService;
	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	@Autowired
	private IJiangYinPlatformService jiangYinPlatformService;
	@Autowired
	private CiRsService ciRsService;
	@Autowired
	private HiLogoutService hiLogoutService;
 
	public static final String CALL_TYPE_IN = "1";
	public static final String CALL_TYPE_OUT = "2";
	public static final String CONTACT_BY_TEL = "1";
	public static final String CONTACT_BY_VIDEO = "2";
	public static final String CONTACT_BY_SMS = "3";
	
	/**
	 * ??????????????????
	 * @param session
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/index")
	public String index(HttpSession session, HttpServletRequest request,
		  	HttpServletResponse response, ModelMap map) throws Exception{
		String forward = "/map/jiangYinPlatform/index.ftl";

		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//??????????????????????????????????????????
		if(gridInfo==null) throw new Exception("?????????????????????????????????");
		//Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.put("gridId", gridInfo.getGridId());
		map.put("gridCode", gridInfo.getGridCode());
		map.put("gridName", gridInfo.getGridName());
		map.put("gridLevel", gridInfo.getGridLevel());
		map.put("infoOrgCode", gridInfo.getInfoOrgCode());
		map.put("userName", userInfo.getUserName());
		return forward;
	}
	
	/******************************????????? start*************************************/
	
	/******************************????????? end**************************************/
	
	
	/******************************start2*************************************/
	
	/*******************************end2**************************************/
	
	
	/******************************start3*************************************/
	/**
	 * ????????????????????????
	 * @param infoOrgCode
	 * @param maxGridLevel
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/getGridAdminNum")
	public Object getGridAdminNum(HttpSession session, HttpServletRequest request,
		  	HttpServletResponse response,@RequestParam Map<String, String> params,@RequestParam(value="jsonpcallback",required=false)String jsonpcallback) throws Exception{
		String infoOrgCode = params.get("infoOrgCode");
		String useType = params.get("useType");
		String platformName = params.get("platformName");
		String channelName = params.get("channelName");
		String eyesType = params.get("eyesType");
		String globalType = params.get("globalType");
		String gridId = params.get("gridId");
		Integer workerCount = gridAdminService.countGridAdminListByGridId((long) Integer.parseInt(gridId));//??????id?????????????????????
		//return gridAdminService.countGridAdmin(infoOrgCode,Integer.parseInt(maxGridLevel));
		Pagination globalEyesCount =monitorService.listGlobalEyeConfig(infoOrgCode, 1, 10, null, useType, platformName, channelName, eyesType,globalType);
		Map<String,Object> params1 = new HashMap<String,Object>();
		params1.put("workerCount",workerCount );
		params1.put("globalEyesCount",globalEyesCount.getTotalCount());
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(params1) +")";
		}else {
			jsonpcallback =  JsonHelper.getJsonString(params1) ;
			
		}
		return jsonpcallback;
	}
	
	/**
	 * ??????????????????
	 * @param session
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getEventTodayData")
	public ModelMap getEventTodayData(HttpSession session, HttpServletRequest request,
		  	HttpServletResponse response, ModelMap map,@RequestParam Map<String, String> params) throws Exception{
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String,Object> searchParam=new HashMap<String,Object>();
		searchParam.put("infoOrgCode", params.get("infoOrgCode"));
		searchParam.put("status", "00,01,02,03,04");//??????????????????????????????????????????--?????????????????????
		searchParam.put("listType", params.get("listType"));
		//??????????????????
		searchParam.put("createTimeStart", params.get("begintime"));
		searchParam.put("createTimeEnd", params.get("endtime"));
		searchParam.put("userId", userInfo.getUserId());
		searchParam.put("orgId", userInfo.getOrgId());
		searchParam.put("orgCode", userInfo.getOrgCode());
		//????????????
		Integer todayAdd =event4WorkflowService.findEventCount(searchParam);
		map.put("todayAdd", todayAdd);
		
		//????????????????????????0304
		Map<String,Object> searchParam1=new HashMap<String,Object>();
		searchParam1.put("infoOrgCode", params.get("infoOrgCode"));
		searchParam1.put("status", "03,04");//??????????????????????????????????????????--?????????????????????
		searchParam1.put("listType", params.get("listType"));
		searchParam1.put("createTimeStart", params.get("begintime"));
		searchParam1.put("createTimeEnd", params.get("endtime"));
		searchParam1.put("userId", userInfo.getUserId());
		searchParam1.put("orgId", userInfo.getOrgId());
		searchParam1.put("orgCode", userInfo.getOrgCode());
		
		//????????????
		Integer todaySettle =event4WorkflowService.findEventCount(searchParam1);
		map.put("todaySettle", todaySettle);
		
		String dealTodayRate = (double)(Math.round(todaySettle*100/todayAdd))+"";
		map.put("dealTodayRate", dealTodayRate);
		
		//???????????????????????????????????????0304
		Map<String,Object> searchParam2=new HashMap<String,Object>();
		searchParam2.put("infoOrgCode", params.get("infoOrgCode"));
		searchParam2.put("status", "03,04");//??????????????????????????????????????????--?????????????????????
		searchParam2.put("listType", params.get("listType"));
		searchParam2.put("createTimeStart", params.get("begintime"));
		searchParam2.put("createTimeEnd", params.get("endtime"));
		searchParam2.put("userId", userInfo.getUserId());
		searchParam2.put("orgId", userInfo.getOrgId());
		searchParam2.put("orgCode", userInfo.getOrgCode());
		
		//??????????????????
		Integer allSettle =event4WorkflowService.findEventCount(searchParam2);
		map.put("allSettle", allSettle);
		
		//???????????????
		Map<String,Object> searchParam3=new HashMap<String,Object>();
		searchParam3.put("infoOrgCode", params.get("infoOrgCode"));
		searchParam3.put("status", "04");//??????????????????????????????????????????--?????????????????????
		searchParam3.put("listType", "4");
		searchParam3.put("createTimeStart", params.get("begintime"));
		searchParam3.put("createTimeEnd", params.get("endtime"));
		searchParam3.put("userId", userInfo.getUserId());
		searchParam3.put("orgId", userInfo.getOrgId());
		searchParam3.put("orgCode", userInfo.getOrgCode());
		searchParam3.put("isUseTSjEvaResult", true);
		
		//??????????????????
		Integer satisfaction =event4WorkflowService.findEventCount(searchParam3);
		String satisfactionRate = (double)(Math.round(satisfaction*100/todaySettle))+"%";
		map.put("satisfactionRate", satisfactionRate);
		
		return map;
	}
	
	
	/*******************************end3**************************************/
	
	
	/****************************** start4 *************************************/

	@ResponseBody
	@RequestMapping(value = "/globalEyesListData")
	public String globalEyesListData(HttpServletRequest request, HttpSession session,
			@RequestParam Map<String, Object> params) {
		int page = Integer.valueOf(request.getParameter("page"));
		if (page <= 0)
			page = 1;
		int rows = Integer.valueOf(request.getParameter("rows"));
		String orgCode = request.getParameter("orgCode");// ?????????????????????
		String platformName = request.getParameter("platformName"); // ??????
		String eyesType = request.getParameter("eyesType");
		String jsonpcallback = "";
		Pagination pagination = monitorService.listGlobalEyeConfig(orgCode, page, rows, null, null, platformName, null,
				eyesType, "003");
		if (CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if (StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(pagination) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(pagination);
		}

		return jsonpcallback;
	}

	/**
	 * 2014-06-24 chenlf add ??????????????????????????????
	 * 
	 * @param session
	 * @param ids
	 *            ?????????ids
	 * @param mapt
	 *            ????????????
	 * @param showType
	 *            ??????????????????1???????????????2?????????????????????
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfGlobalEyes")
	public String getArcgisGlobalEyesLocateDataList(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "ids20", required = false) String ids20,
			String orgCode,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "showType") String showType,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		if ("1".equals(showType)) {
			ids = "";
		}
		List<ArcgisInfo> list = this.arcgisDataOfLocalService.getArcgisGlobalEyesLocateDataListByIds(ids, mapt,orgCode);
		if (StringUtils.isNotBlank(ids20)) {
			List<ArcgisInfo> listFFcs = this.arcgisDataOfLocalService
					.getArcgisGlobalEyesLocateDataListByDeviceNums(ids20, mapt);
			if (listFFcs != null && listFFcs.size() > 0) {
				list.addAll(listFFcs);
			}
		}
		for (ArcgisInfo arcgisInfo : list) {
			arcgisInfo.setElementsCollectionStr(elementsCollectionStr);
		}
		if (CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if (StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(list) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(list);
		}

		return jsonpcallback;
	}

	@ResponseBody
	@RequestMapping(value = "/sphj")
	public String testJsonp(HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		String regisValue=request.getParameter("regisValue");
		
		map.put("account", userInfo.getUserName());// ????????????regisValue
		OutUserBO userOutbo = userManageOutService.getOutUserBOByOutParams(map);
		map.put("account", regisValue);// ????????????regisValue
		OutUserBO userBO = userManageOutService.getOutUserBOByOutParams(map);
		if (userOutbo != null) {
			resultMap.put("senderId", userOutbo.getOpposideusercode());// ??????????????????
		}
		resultMap.put("senderName", userInfo.getPartyName());// ??????????????????
		if (userBO != null) {
			resultMap.put("receiverId", userBO.getOpposideusercode());// ??????????????????
		}
		if (CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}

		if (StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}

		return jsonpcallback;
	}

	/******************************* end4 **************************************/
	
	
	/******************************start5*************************************/
	
	/**
	 * ??????????????????????????????
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping("/getContactData")
	public Object getContactData(HttpServletRequest request, HttpSession session, ModelMap map, @RequestParam(value="jsonpcallback",required=false) String jsonpcallback) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //?????????????????????
		//????????????????????????????????????
		String contactType = request.getParameter("contactType");
		//????????????
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", request.getParameter("name"));
		
		//????????????
		List list = null;
		if ("1".equals(contactType)) {
			params.put("userTel", userInfo.getVerifyMobile());
			params.put("creator", userInfo.getUserId());
			list = recentContactService.searchList(params);
		} else if ("3".equals(contactType)) {
			params.put("creator", userInfo.getUserId());
			list = jyAddressBookService.searchListNoPagination(params);
		} else {
			list = new ArrayList();
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("data", list);
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	/**
	 * ????????????????????????
	 */
	/*
	@ResponseBody
	@RequestMapping("/getContactDetail")
	public Object getContactDetail(HttpServletRequest request, HttpSession session, ModelMap map, @RequestParam(value="jsonpcallback",required=false) String jsonpcallback) {
		//????????????????????????????????????
		String contactType = request.getParameter("contactType");
		String id = request.getParameter("id");
		
		//??????????????????
		Object bo = null;
		if ("3".equals(contactType)) {
			bo = jyAddressBookService.searchByUUID(id);
		} else {
			bo = new Object();
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("data", bo);
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	*/
	
	/**
	 * ???????????????????????????????????????
	 */
	@ResponseBody
	@RequestMapping("/getContactDetailByTel")
	public Object getContactDetailByTel(HttpServletRequest request, HttpSession session, ModelMap map, @RequestParam(value="jsonpcallback",required=false) String jsonpcallback) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //?????????????????????
		
		String tel = request.getParameter("tel");
		Long userId= userInfo.getUserId();
		
		//????????????
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tel", tel);
		params.put("userId", userId);
		
		//???????????????map??????
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//????????????
		JYAddressBook bo = jyAddressBookService.searchByTelAndCreator(params);
		
		resultMap.put("data", bo);
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	/**
	 * ?????????????????????
	 */
	@ResponseBody
	@RequestMapping("/saveAddressBook")
	public Object saveAddressBook(HttpServletRequest request, HttpSession session, ModelMap map, @RequestParam(value="jsonpcallback",required=false) String jsonpcallback,
		JYAddressBook bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //?????????????????????
		String tel = bo.getTel();
		Long userId = userInfo.getUserId();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		String resultMsg = "????????????!";
		String boId = "";
		if (StringUtils.isBlank(bo.getUuid())) { //??????
			//????????????????????????
			boolean isExist = jyAddressBookService.checkExist(tel, userId);
			if (isExist) {
				resultMsg = "?????????????????????!";
			} else {
				bo.setCreator(userId);  //???????????????
				bo.setUpdator(userId);  //???????????????
				String id = jyAddressBookService.insert(bo);
				if (StringUtils.isNotBlank(id)) {
					result = "success";
					resultMsg = "????????????!";
					boId = id;
				};
			}
		} else { //??????
			//?????????????????????????????????????????????????????????????????????????????????????????????????????????
			JYAddressBook temp = jyAddressBookService.searchByUUID(bo.getUuid());
			boolean isExist = false;
			if (!temp.getTel().equals(bo.getTel())) {
				isExist = jyAddressBookService.checkExist(tel, userId);
			}
			
			if (isExist) {
				resultMsg = "?????????????????????!";
			} else {
				bo.setUpdator(userId);  //???????????????
				boolean updateResult = jyAddressBookService.update(bo);
				if (updateResult) {
					result = "success";
					resultMsg = "????????????!";
					boId = bo.getUuid();
				}
			}
		}
		resultMap.put("result", result);
		resultMap.put("resultMsg", resultMsg);
		resultMap.put("boId", boId);

		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	/**
	 * ?????????????????????
	 */
	@ResponseBody
	@RequestMapping("/delAddressBook")
	public Object delAddressBook(HttpServletRequest request, HttpSession session, ModelMap map, @RequestParam(value="jsonpcallback",required=false) String jsonpcallback,
		JYAddressBook bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION"); //?????????????????????
		bo.setUpdator(userInfo.getUserId());  //???????????????
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		String resultMsg = "????????????!";
		boolean delResult = jyAddressBookService.delete(bo);
		if (delResult) {
			result = "success";
			resultMsg = "????????????!";
		}
		resultMap.put("result", result);
		resultMap.put("resultMsg", resultMsg);
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	/**
	 * ??????????????????????????????
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping("/getCommunicationLogData")
	public Object getCommunicationLogData(HttpServletRequest request, HttpSession session, ModelMap map, @RequestParam(value="jsonpcallback",required=false) String jsonpcallback) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //?????????????????????
		String contactType = request.getParameter("type");
		String contactedTel = request.getParameter("contactedTel");
		//????????????
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contactType", contactType);
		params.put("userTel", userInfo.getVerifyMobile());
		//??????????????????
		params.put("contactedTel", contactedTel);
		//????????????
		List list = null;
		if ("3".equals(contactType)) {
			//??????????????????
			Map<String, Object> smsParams = new HashMap<String, Object>();
			smsParams.put("receiverEq", contactedTel);
			smsParams.put("smsType", "sms");
			smsParams.put("showStatus", false);
			cn.ffcs.shequ.bo.Pagination pagination = smsLogService.findSmsLogPage(smsParams,20,1);
			list = pagination.getList();
		} else {
			list = communicationLogService.searchList(params);
		}
		if (list == null) {
			list = new ArrayList();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("data", list);
		resultMap.put("type", request.getParameter("type"));
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	/**
	 * ????????????
	 */
	@ResponseBody
	@RequestMapping("/sendMsg")
	public Object sendMsg(HttpServletRequest request, HttpSession session, ModelMap map, @RequestParam(value="jsonpcallback",required=false) String jsonpcallback) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //?????????????????????
		String tel = request.getParameter("tel");
		String message = request.getParameter("message");
		String sysUserName = request.getParameter("sysUserName");
		String[] telArr = {tel};
		
		//??????????????????
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		String resultMsg = "";
		try {
			//????????????
			SendResult rs = sendSmsService.sendSms("oauth", userInfo.getUserId(), userInfo.getOrgCode(), telArr, message, SendSmsService.TYPE_SMS, null, null);
//			SendResult rs = new SendResult();
//			rs.setCode(0);
			if (rs.getCode() == 0) {
				resultMsg = "??????????????????";
				result = "success";
				//???????????????????????????
				RecentContact bo = new RecentContact();
				bo.setCallType(CALL_TYPE_OUT);
				bo.setContactTime(new Date());
				bo.setContactType(CONTACT_BY_SMS);
				bo.setContactedAccount(tel);
				bo.setContactAccount(userInfo.getVerifyMobile());
				bo.setCreator(userInfo.getUserId());
				bo.setUpdator(userInfo.getUserId());
				bo.setSysUserName(sysUserName);
				recentContactService.insert(bo);
			} else {
				resultMsg = "??????????????????";
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = "??????????????????";
		}
		resultMap.put("result", result);
		resultMap.put("resultMsg", resultMsg);
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	/**
	 * ????????????????????????????????????
	 */
	@ResponseBody
	@RequestMapping("/saveLogAndRecentContact")
	public Object saveLogAndRecentContact(HttpServletRequest request, HttpSession session, ModelMap map, @RequestParam(value="jsonpcallback",required=false) String jsonpcallback) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //?????????????????????
		Long userId = userInfo.getUserId();	
		String userTel = userInfo.getVerifyMobile();	//????????????????????????
		String callType = request.getParameter("callType");	//??????????????????
		String tel = request.getParameter("tel");	//???????????????
		String sysUserName = request.getParameter("sysUserName");	//????????????????????????
		String contactDuration = request.getParameter("contactDuration");	//????????????
		String contactType = request.getParameter("contactType");	//????????????(1:??????, 2:??????, 3:??????) 
		String contactTimeStr = request.getParameter("contactTimeStr");	//????????????
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date contactTime = null;
		try {
			contactTime = sdf.parse(contactTimeStr);
		} catch (ParseException e1) {
			e1.printStackTrace();
			contactTime = new Date();
		}
		
		//???????????????????????????
		RecentContact reCon = new RecentContact();
		reCon.setContactType(contactType);
		reCon.setSysUserName(sysUserName);
		reCon.setContactTime(contactTime);
		reCon.setCallType(callType);
		if (callType.equals(CALL_TYPE_IN)) {
			reCon.setContactedAccount(userTel);
			reCon.setContactAccount(tel);
		} else {
			reCon.setContactedAccount(tel);
			reCon.setContactAccount(userTel);
		}
		reCon.setCreator(userId);
		reCon.setUpdator(userId);
		
		
		//????????????????????????
		CommunicationLog comLog = null;
		if (StringUtils.isNotBlank(contactDuration)) {
			comLog = new CommunicationLog();
			comLog.setContactType(contactType);
			comLog.setContactTime(contactTime);
			comLog.setContactDuration(contactDuration);
			comLog.setCallType(callType);
			if (callType.equals(CALL_TYPE_IN)) {
				comLog.setContactedAccount(userTel);
				comLog.setContactAccount(tel);
			} else {
				comLog.setContactedAccount(tel);
				comLog.setContactAccount(userTel);
			}
			comLog.setCreator(userId);
			comLog.setUpdator(userId);
		}
		
		//??????????????????
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		try {
			boolean flag = jiangYinPlatformService.saveLogAndRecentContact(reCon, comLog);
			if (flag) {
				result = "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("result", result);
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	/*******************************end5**************************************/
	
	/*******************************????????? start**************************************/
	/**
	 * ????????????????????????
	 */
	@ResponseBody
	@RequestMapping("/getEventList")
	public Object getEventList(HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "eventType") String eventType,
			@ModelAttribute(value = "event") EventDisposal event,
			@RequestParam(value = "generalSearch", required = false) String generalSearch,
			@RequestParam(value = "startHappenTime", required=false) String startHappenTime,
			@RequestParam(value = "endHappenTime", required=false) String endHappenTime,
			@RequestParam(value = "statuss[]", required=false) String statuss[],
			@RequestParam(value = "handleStatus", required=false) String handleStatus,
			@RequestParam(value = "handleStatuss", required=false) String handleStatuss,
			@RequestParam(value = "types", required=false) String types,
			@RequestParam(value = "eventStatus", required = false) String eventStatus,
			@RequestParam(value = "typesForList", required = false) String typesForList,
			@RequestParam(value = "eventAttrTrigger", required = false) String eventAttrTrigger,
			@RequestParam Map<String, Object> paramMap, 
			@RequestParam(value="jsonpcallback",required=false) String jsonpcallback) {
		
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		EventDisposalController eventDisposalController = (EventDisposalController) wac.getBean("eventDisposalController");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		EUDGPagination result = new EUDGPagination();
		try {
			result=eventDisposalController.listData(request, session, page, rows, eventType, event, generalSearch, startHappenTime, endHappenTime, statuss, handleStatus, handleStatuss, types, eventStatus, typesForList, eventAttrTrigger, paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		};
		resultMap.put("data", result);
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	/*******************************????????? end**************************************/

}
