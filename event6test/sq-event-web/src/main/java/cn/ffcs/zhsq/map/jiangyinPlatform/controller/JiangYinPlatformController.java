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
 *  	江阴大屏
 * @author sunjian
 * */
@Controller
@RequestMapping("/zhsq/map/jiangYinPlatform")
public class JiangYinPlatformController extends ZZBaseController {
	
	@Autowired
	private IJYAddressBookService jyAddressBookService; //注入通讯录表模块服务
	@Autowired
	private ICommunicationLogService communicationLogService; //注入通讯记录表模块服务
	@Autowired
	private IRecentContactService recentContactService; //注入最近联系人表模块服务
	@Autowired
	private IEvent4WorkflowService event4WorkflowService;
	@Autowired
	private IGridAdminService gridAdminService;
	@Autowired
	private MonitorService monitorService;
	@Autowired
	private SendSmsService sendSmsService;  //短信服务
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
	 * 江阴大屏首页
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
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		//Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.put("gridId", gridInfo.getGridId());
		map.put("gridCode", gridInfo.getGridCode());
		map.put("gridName", gridInfo.getGridName());
		map.put("gridLevel", gridInfo.getGridLevel());
		map.put("infoOrgCode", gridInfo.getInfoOrgCode());
		map.put("userName", userInfo.getUserName());
		return forward;
	}
	
	/******************************吴展杰 start*************************************/
	
	/******************************吴展杰 end**************************************/
	
	
	/******************************start2*************************************/
	
	/*******************************end2**************************************/
	
	
	/******************************start3*************************************/
	/**
	 * 获取工作人员数量
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
		Integer workerCount = gridAdminService.countGridAdminListByGridId((long) Integer.parseInt(gridId));//根据id获取网格员数量
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
	 * 江阴大屏首页
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
		searchParam.put("status", "00,01,02,03,04");//统计个数的时候使用全状态条件--先改成未办结的
		searchParam.put("listType", params.get("listType"));
		//时间单位是日
		searchParam.put("createTimeStart", params.get("begintime"));
		searchParam.put("createTimeEnd", params.get("endtime"));
		searchParam.put("userId", userInfo.getUserId());
		searchParam.put("orgId", userInfo.getOrgId());
		searchParam.put("orgCode", userInfo.getOrgCode());
		//今日新增
		Integer todayAdd =event4WorkflowService.findEventCount(searchParam);
		map.put("todayAdd", todayAdd);
		
		//创建时间是当日，0304
		Map<String,Object> searchParam1=new HashMap<String,Object>();
		searchParam1.put("infoOrgCode", params.get("infoOrgCode"));
		searchParam1.put("status", "03,04");//统计个数的时候使用全状态条件--先改成未办结的
		searchParam1.put("listType", params.get("listType"));
		searchParam1.put("createTimeStart", params.get("begintime"));
		searchParam1.put("createTimeEnd", params.get("endtime"));
		searchParam1.put("userId", userInfo.getUserId());
		searchParam1.put("orgId", userInfo.getOrgId());
		searchParam1.put("orgCode", userInfo.getOrgCode());
		
		//今日办结
		Integer todaySettle =event4WorkflowService.findEventCount(searchParam1);
		map.put("todaySettle", todaySettle);
		
		String dealTodayRate = (double)(Math.round(todaySettle*100/todayAdd))+"";
		map.put("dealTodayRate", dealTodayRate);
		
		//当日办结，更新时间是当日，0304
		Map<String,Object> searchParam2=new HashMap<String,Object>();
		searchParam2.put("infoOrgCode", params.get("infoOrgCode"));
		searchParam2.put("status", "03,04");//统计个数的时候使用全状态条件--先改成未办结的
		searchParam2.put("listType", params.get("listType"));
		searchParam2.put("createTimeStart", params.get("begintime"));
		searchParam2.put("createTimeEnd", params.get("endtime"));
		searchParam2.put("userId", userInfo.getUserId());
		searchParam2.put("orgId", userInfo.getOrgId());
		searchParam2.put("orgCode", userInfo.getOrgCode());
		
		//今日办结所有
		Integer allSettle =event4WorkflowService.findEventCount(searchParam2);
		map.put("allSettle", allSettle);
		
		//今日满意数
		Map<String,Object> searchParam3=new HashMap<String,Object>();
		searchParam3.put("infoOrgCode", params.get("infoOrgCode"));
		searchParam3.put("status", "04");//统计个数的时候使用全状态条件--先改成未办结的
		searchParam3.put("listType", "4");
		searchParam3.put("createTimeStart", params.get("begintime"));
		searchParam3.put("createTimeEnd", params.get("endtime"));
		searchParam3.put("userId", userInfo.getUserId());
		searchParam3.put("orgId", userInfo.getOrgId());
		searchParam3.put("orgCode", userInfo.getOrgCode());
		searchParam3.put("isUseTSjEvaResult", true);
		
		//今日办结所有
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
		String orgCode = request.getParameter("orgCode");// 信息域组织编码
		String platformName = request.getParameter("platformName"); // 名称
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
	 * 2014-06-24 chenlf add 获取全球眼的定位信息
	 * 
	 * @param session
	 * @param ids
	 *            全球眼ids
	 * @param mapt
	 *            地图类型
	 * @param showType
	 *            地图显示类型1、全显示，2、只显示当前页
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
		
		map.put("account", userInfo.getUserName());// 主叫用户regisValue
		OutUserBO userOutbo = userManageOutService.getOutUserBOByOutParams(map);
		map.put("account", regisValue);// 被叫用户regisValue
		OutUserBO userBO = userManageOutService.getOutUserBOByOutParams(map);
		if (userOutbo != null) {
			resultMap.put("senderId", userOutbo.getOpposideusercode());// 主叫用户账号
		}
		resultMap.put("senderName", userInfo.getPartyName());// 主叫用户姓名
		if (userBO != null) {
			resultMap.put("receiverId", userBO.getOpposideusercode());// 被叫用户账号
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
	 * 获取通讯连接列表数据
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping("/getContactData")
	public Object getContactData(HttpServletRequest request, HttpSession session, ModelMap map, @RequestParam(value="jsonpcallback",required=false) String jsonpcallback) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //替换成本地常量
		//根据联系类型查询不同数据
		String contactType = request.getParameter("contactType");
		//查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", request.getParameter("name"));
		
		//数据集合
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
	 * 获取通讯连接详情
	 */
	/*
	@ResponseBody
	@RequestMapping("/getContactDetail")
	public Object getContactDetail(HttpServletRequest request, HttpSession session, ModelMap map, @RequestParam(value="jsonpcallback",required=false) String jsonpcallback) {
		//根据联系类型查询不同数据
		String contactType = request.getParameter("contactType");
		String id = request.getParameter("id");
		
		//数据详情对象
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
	 * 根据手机号码获取通讯录详情
	 */
	@ResponseBody
	@RequestMapping("/getContactDetailByTel")
	public Object getContactDetailByTel(HttpServletRequest request, HttpSession session, ModelMap map, @RequestParam(value="jsonpcallback",required=false) String jsonpcallback) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //替换成本地常量
		
		String tel = request.getParameter("tel");
		Long userId= userInfo.getUserId();
		
		//查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tel", tel);
		params.put("userId", userId);
		
		//返回页面的map集合
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//详情数据
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
	 * 保存通讯录数据
	 */
	@ResponseBody
	@RequestMapping("/saveAddressBook")
	public Object saveAddressBook(HttpServletRequest request, HttpSession session, ModelMap map, @RequestParam(value="jsonpcallback",required=false) String jsonpcallback,
		JYAddressBook bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //替换成本地常量
		String tel = bo.getTel();
		Long userId = userInfo.getUserId();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		String resultMsg = "保存失败!";
		String boId = "";
		if (StringUtils.isBlank(bo.getUuid())) { //新增
			//判断电话是否重复
			boolean isExist = jyAddressBookService.checkExist(tel, userId);
			if (isExist) {
				resultMsg = "已存在相同号码!";
			} else {
				bo.setCreator(userId);  //设置创建人
				bo.setUpdator(userId);  //设置更新人
				String id = jyAddressBookService.insert(bo);
				if (StringUtils.isNotBlank(id)) {
					result = "success";
					resultMsg = "保存成功!";
					boId = id;
				};
			}
		} else { //修改
			//将该条记录的原数据和修改后的数据比较，若电话号码没改，则不用判断重复性
			JYAddressBook temp = jyAddressBookService.searchByUUID(bo.getUuid());
			boolean isExist = false;
			if (!temp.getTel().equals(bo.getTel())) {
				isExist = jyAddressBookService.checkExist(tel, userId);
			}
			
			if (isExist) {
				resultMsg = "已存在相同号码!";
			} else {
				bo.setUpdator(userId);  //设置更新人
				boolean updateResult = jyAddressBookService.update(bo);
				if (updateResult) {
					result = "success";
					resultMsg = "保存成功!";
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
	 * 删除通讯录数据
	 */
	@ResponseBody
	@RequestMapping("/delAddressBook")
	public Object delAddressBook(HttpServletRequest request, HttpSession session, ModelMap map, @RequestParam(value="jsonpcallback",required=false) String jsonpcallback,
		JYAddressBook bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION"); //替换成本地常量
		bo.setUpdator(userInfo.getUserId());  //设置更新人
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		String resultMsg = "删除失败!";
		boolean delResult = jyAddressBookService.delete(bo);
		if (delResult) {
			result = "success";
			resultMsg = "删除成功!";
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
	 * 获取通讯记录列表数据
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping("/getCommunicationLogData")
	public Object getCommunicationLogData(HttpServletRequest request, HttpSession session, ModelMap map, @RequestParam(value="jsonpcallback",required=false) String jsonpcallback) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //替换成本地常量
		String contactType = request.getParameter("type");
		String contactedTel = request.getParameter("contactedTel");
		//查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contactType", contactType);
		params.put("userTel", userInfo.getVerifyMobile());
		//被联系人账号
		params.put("contactedTel", contactedTel);
		//数据集合
		List list = null;
		if ("3".equals(contactType)) {
			//短信查询参数
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
	 * 发送短信
	 */
	@ResponseBody
	@RequestMapping("/sendMsg")
	public Object sendMsg(HttpServletRequest request, HttpSession session, ModelMap map, @RequestParam(value="jsonpcallback",required=false) String jsonpcallback) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //替换成本地常量
		String tel = request.getParameter("tel");
		String message = request.getParameter("message");
		String sysUserName = request.getParameter("sysUserName");
		String[] telArr = {tel};
		
		//返回响应内容
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		String resultMsg = "";
		try {
			//发送短信
			SendResult rs = sendSmsService.sendSms("oauth", userInfo.getUserId(), userInfo.getOrgCode(), telArr, message, SendSmsService.TYPE_SMS, null, null);
//			SendResult rs = new SendResult();
//			rs.setCode(0);
			if (rs.getCode() == 0) {
				resultMsg = "短信提交成功";
				result = "success";
				//最近联系人插入数据
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
				resultMsg = "短信提交失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = "短信提交失败";
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
	 * 保存通讯记录和最近联系人
	 */
	@ResponseBody
	@RequestMapping("/saveLogAndRecentContact")
	public Object saveLogAndRecentContact(HttpServletRequest request, HttpSession session, ModelMap map, @RequestParam(value="jsonpcallback",required=false) String jsonpcallback) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //替换成本地常量
		Long userId = userInfo.getUserId();	
		String userTel = userInfo.getVerifyMobile();	//当前用户手机号码
		String callType = request.getParameter("callType");	//呼入呼出类型
		String tel = request.getParameter("tel");	//联系人手机
		String sysUserName = request.getParameter("sysUserName");	//组织列表用户名称
		String contactDuration = request.getParameter("contactDuration");	//联系时长
		String contactType = request.getParameter("contactType");	//通讯类别(1:电话, 2:视频, 3:短信) 
		String contactTimeStr = request.getParameter("contactTimeStr");	//联系时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date contactTime = null;
		try {
			contactTime = sdf.parse(contactTimeStr);
		} catch (ParseException e1) {
			e1.printStackTrace();
			contactTime = new Date();
		}
		
		//构建最近联系人对象
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
		
		
		//构建通讯记录对象
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
		
		//返回响应内容
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
	
	/*******************************游伟进 start**************************************/
	/**
	 * 获取事件列表数据
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
	
	/*******************************游伟进 end**************************************/

}
