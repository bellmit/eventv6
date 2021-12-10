package cn.ffcs.zhsq.ypms.mscase.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.json.JsonUtils;
import cn.ffcs.shequ.utils.StringUtils;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.sms.bo.SendResult;
import cn.ffcs.sms.service.SendSmsService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.addressBook.service.AddressBookService;
import cn.ffcs.zhsq.callInPerson.service.CallInPersonService;
import cn.ffcs.zhsq.mybatis.domain.addressBook.AddressBook;
import cn.ffcs.zhsq.mybatis.domain.callInPerson.CallInPerson;
import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.CaseHandler;
import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.DeptHandle;
import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.MsCase;
import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.ReceiveDept;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.ypms.mscase.service.CaseHandlerService;
import cn.ffcs.zhsq.ypms.mscase.service.DeptHandleService;
import cn.ffcs.zhsq.ypms.mscase.service.MsCaseService;
import cn.ffcs.zhsq.ypms.mscase.service.ReceiveDeptService;

import com.alibaba.fastjson.JSON;

/**   
 * @Description: 延平民生案件表模块控制器
 * @Author: zhangzhenhai
 * @Date: 04-13 14:43:28
 * @Copyright: 2018 福富软件
 */ 
@Controller("msCaseController")
@RequestMapping("/zhsq/ypms/mscase")
public class MsCaseController extends ZZBaseController{

	public static final String ATTACHMENT_TYPE_YPMSCASE = "ypmsCase";
	@Autowired
	private IAttachmentService attachmentService;//上传附件接口
	@Autowired
	private SendSmsService sendSmsService; //发送短信接口
	
	@Autowired
	private MsCaseService msCaseService; //注入延平民生案件表模块服务
	@Autowired
	private CaseHandlerService caseHandlerService; //注入延平民生案件操作员模块服务
	@Autowired
	private ReceiveDeptService receiveDeptService; //注入延平民生案件派发单位表模块服务
	@Autowired
	private CallInPersonService callInPersonService; //主叫人员信息表模块服务
	@Autowired
	private AddressBookService addressBookService; //主叫人员信息表模块服务
	@Autowired
	private DeptHandleService deptHandleService; //主叫人员信息表模块服务
	
	//所属公司ID
	@Value(value="${COMPANY_ID}")
	private String COMPANY_ID;
	//员工ID
	@Value(value="${STAFF_ID}")
	private String STAFF_ID;
	//员工工号
	@Value(value="${STAFF_NO}")
	private String STAFF_NO;
	//员工密码
	@Value(value="${STAFF_PWD}")
	private String STAFF_PWD;
	//部门名称
	@Value(value="${DEPT_NAME}")
	private String DEPT_NAME;
	//呼坐席的主叫,一般送企
	@Value(value="${AGENTANI}")
	private String AGENTANI;

	
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		setOrgInfoToSession(session,map);
		
		String menuType = request.getParameter("menuType");
		map.put("menuType", menuType);
		map.put("COMPANY_ID", COMPANY_ID);
		map.put("STAFF_ID", STAFF_ID);
		map.put("STAFF_NO", STAFF_NO);
		map.put("STAFF_PWD", STAFF_PWD);
		map.put("DEPT_NAME", DEPT_NAME);
		map.put("AGENTANI", AGENTANI);
		return "/ypms/mscase/list_msCase.ftl";
		
	}
	
	
	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,
		int page, int rows) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		Map<String, Object> params = new HashMap<String, Object>();
		//案件类别
		String caseType = request.getParameter("caseType");
		if ("-1".equals(caseType)){
			caseType = "";
		}
		params.put("caseType", caseType);
		//处理方式
		String handleWay = request.getParameter("handleWay");
		if ("-1".equals(handleWay)){
			handleWay = "";
		}
		params.put("handleWay", handleWay);
		//关键字
		params.put("caseNo", request.getParameter("caseNo"));
		//采集人员(接听人员)
		params.put("receiverName", request.getParameter("receiverName"));
		//所属辖区
		String caseFromCode = request.getParameter("caseFromCode");
		params.put("caseFromCode", caseFromCode);
		//办理部门
		params.put("handleDeptName", request.getParameter("handleDeptName"));
		//呼叫时间
		params.put("startDateCallin", request.getParameter("startDateCallin"));
		params.put("endDateCallin", request.getParameter("endDateCallin"));
		
		
		String menuType = request.getParameter("menuType");
		EUDGPagination pagination = null;
		if ("index".equals(menuType)) {//案件联动
			
			params.put("createOrgCode", userInfo.getOrgCode());//当前用户的组织
			pagination = msCaseService.searchList(page, rows, params);
			
		}else if ("untreated".equals(menuType)) {//未处理案件
			
			params.put("createOrgCode", userInfo.getOrgCode());//当前用户的组织
//			params.put("ldType", "0");//0是联动队伍
//			params.put("deptHandleStatus", "01");//单位办理状态：待办
			params.put("handleStatus", "01");//单位办理状态：待办
			pagination = msCaseService.searchListForUntreated(page, rows, params);
			
		}else if ("search".equals(menuType)) {//案件查询
			
			params.put("createOrgCode", userInfo.getOrgCode());//当前用户的组织
			pagination = msCaseService.searchList(page, rows, params);
			
		}else if ("wait".equals(menuType)) {//待办案件
			
			//委办单位待办,中心待办
			params.put("currOrgCode", userInfo.getOrgCode());//当前用户的组织
			params.put("ldType", "0");//0是联动队伍
			//主从表状态参数
			String status_a = "02,03";//派发单位的处理状态
			params.put("status_a",Arrays.asList(status_a.split(",")));
			String status_b = "01,02,04";//公文状态
			params.put("status_b",Arrays.asList(status_b.split(",")));
			
			String status_c = "02,04";//公文状态
			params.put("status_c",Arrays.asList(status_c.split(",")));
			
			pagination = msCaseService.searchListForWait(page, rows, params);
			
		}else if ("hanlde".equals(menuType)) {//经办案件
			
			params.put("handlerId", userInfo.getUserId());//0是联动队伍
			pagination = msCaseService.searchListForHanlde(page, rows, params);
			
		}else if ("archive".equals(menuType)) {//归档案件
			
			params.put("createOrgCode", userInfo.getOrgCode());//当前用户的组织
			params.put("handleStatus", "03");//03是已归档状态
			pagination = msCaseService.searchList(page, rows, params);
			
		}
		
		
		return pagination;
	}
	
	/**
	 * 来电记录列表数据
	 */
	@ResponseBody
	@RequestMapping("/listDataCallInList")
	public Object listDataCallInList(HttpServletRequest request, HttpSession session, ModelMap map,
			int page, int rows) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		String callinNum = request.getParameter("callinNum");
		if(StringUtils.isEmpty(callinNum)){
			params.put("callinNum", "-1");
		}else{
			params.put("callinNum", callinNum);
		}
		
		//呼叫时间
		String endDateCallin = formatDateToString(new Date(), "yyyy-MM-dd");
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
        c.add(Calendar.YEAR, -1);
        Date y = c.getTime();
		String startDateCallin = formatDateToString(y, "yyyy-MM-dd");
		
		params.put("startDateCallin", startDateCallin);
		params.put("endDateCallin", endDateCallin);
		
		EUDGPagination pagination = msCaseService.searchList(page, rows, params);
		return pagination;
	}
	
	/**
	 * 案件列表数据（重复案件号使用）
	 */
	@ResponseBody
	@RequestMapping("/listDataByParams")
	public Object listDataByParams(HttpServletRequest request, HttpSession session, ModelMap map,
			int page, int rows) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		String caseNo = request.getParameter("q");
		params.put("caseNo", caseNo);
		
		EUDGPagination pagination = msCaseService.searchList(page, rows, params);
		return pagination;
	}
	
	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listDataHandleHis")
	public Object listDataHandleHis(HttpServletRequest request, HttpSession session, ModelMap map,
			int page, int rows) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		String caseId = request.getParameter("caseId");
		if(!StringUtils.isEmpty(caseId)){
			params.put("relacaseId", Long.parseLong(caseId));
		}
		EUDGPagination pagination = deptHandleService.searchList(page, rows, params);
		return pagination;
	}
	
	/**
	 * 根据电话号码获取来电客户信息
	 * @author zhangzhenhai
	 * @date 2018-4-20 上午9:24:54
	 * @param @param request
	 * @param @param session
	 * @param @param map
	 * @param @return    
	 * @return Object
	 */
	@ResponseBody
	@RequestMapping("/getCusetomerDataByTel")
	public Object getCusetomerDataByTel(HttpServletRequest request, HttpSession session, ModelMap map) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> params_a = new HashMap<String, Object>();
		String callinNum = request.getParameter("callinNum");
		if(StringUtils.isEmpty(callinNum)){
			params_a.put("cpMobile", "-1");
		}else{
			params_a.put("cpMobile", callinNum);
		}
		List<CallInPerson> callInPersonList = callInPersonService.searchByParams(params_a);
		CallInPerson callInPerson = null;
		if (callInPersonList != null && callInPersonList.size() > 0) {
			callInPerson = callInPersonList.get(0);
		}
		
		resultMap.put("callInPerson", callInPerson);
		return resultMap;
	}
	
	
	/**
	 * 根据参数获取案件信息
	 * @author zhangzhenhai
	 * @date 2018-4-20 上午9:24:54
	 * @param @param request
	 * @param @param session
	 * @param @param map
	 * @param @return    
	 * @return Object
	 */
	@ResponseBody
	@RequestMapping("/getCaseInfoByParams")
	public Object getCaseInfoByParams(HttpServletRequest request, HttpSession session, ModelMap map) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String caseNo = request.getParameter("caseNo");
		
		Map<String, Object> params_a = new HashMap<String, Object>();
		params_a.put("caseNo", caseNo);
		MsCase msCase = msCaseService.searchByParams(params_a);
		
		resultMap.put("msCase", msCase);
		return resultMap;
	}
	
	
	
	/**
	 * 操作员记录列表页面
	 * @author zhangzhenhai
	 * @date 2018-4-19 上午10:42:39
	 * @param @param request
	 * @param @param session
	 * @param @param map
	 * @param @return    
	 * @return Object
	 */
	@RequestMapping("/showCaseHandlerInfo")
	public Object showCaseHandlerInfo(HttpServletRequest request, HttpSession session, ModelMap map) {
		//关联的案件ID
		String caseId = request.getParameter("caseId");
		if (caseId != null && !"".equals(caseId)){
			map.put("caseId", caseId);
		}else{
			map.put("caseId", "-1");
		}
		//列表类型：驳回reject，日志log
		String listType = request.getParameter("listType");
		map.put("listType", listType);
		String url = "";
		if("log".equals(listType)){
			url = "/ypms/mscase/list_caseHandler_log.ftl";
		}else if ("reject".equals(listType)){
			url = "/ypms/mscase/list_caseHandler_reject.ftl";
		}
		return url;
	}
	
	/**
	 * 操作员操作记录数据
	 * @author zhangzhenhai
	 * @date 2018-4-19 上午10:49:22
	 * @param @param request
	 * @param @param session
	 * @param @param map
	 * @param @param page
	 * @param @param rows
	 * @param @return    
	 * @return Object
	 */
	@ResponseBody
	@RequestMapping("/listDataCaseHandler")
	public Object listDataCaseHandler(HttpServletRequest request, HttpSession session, ModelMap map,
			int page, int rows) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("relaCaseId", request.getParameter("relaCaseId"));
		String listType = request.getParameter("listType");
		if ("reject".equals(listType)){
			params.put("handleType", "03" );
		}else if ("log".equals(listType)){
			params.put("handleType", "" );
		}
		
		EUDGPagination pagination = caseHandlerService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
//		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
//		map.put("userInfo", userInfo);
		
		MsCase bo = msCaseService.searchById(id);
		map.addAttribute("bo", bo);
		
		//获取案件关联的派发单位list,用于初始化页面拼装联动单位
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("relaCaseId", bo.getCaseId());
		List<ReceiveDept> receiveDeptList = receiveDeptService.getReceiveDeptListByRelaCaseId(params);
		map.put("receiveDeptList", receiveDeptList);
		
		
		//附件上传控件参数
		map.addAttribute("bizType", ATTACHMENT_TYPE_YPMSCASE);
		map.addAttribute("SQ_FILE_URL", App.SQFILE.getDomain(session));
		
		return "/ypms/mscase/detail_msCase.ftl";
	}
	
	/**
	 * 详情页面
	 */
	@RequestMapping("/detailDataHandle")
	public Object detailDataHandle(HttpServletRequest request, HttpSession session, ModelMap map,
			Long id) {
//		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
//		map.put("userInfo", userInfo);
		
		DeptHandle bo = deptHandleService.searchById(id);
		map.addAttribute("bo", bo);
		
		return "/ypms/mscase/detail_deptHandle.ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.put("userInfo", userInfo);
	
		String zzgridDomain = App.ZZGRID.getDomain(session);
		String urlval = zzgridDomain + "/zzgl/grid/knowledgeLibrary/frame.jhtml?dictCode=B051&mode=view" ;
		map.put("urlval", urlval);
		
		//将当前信息域id，code，name吐到页面上
		setOrgInfoToSession(session,map);
		
		String menuType =request.getParameter("menuType");
		map.put("menuType", menuType);
		
		String url = "";
		if (id != null) {//编辑
			Map<String, Object> params_h = new HashMap<String, Object>();
			params_h.put("relacaseId", id);
			params_h.put("rdOrgCode", userInfo.getOrgCode());
			List<DeptHandle> deptHandleList = deptHandleService.searchListByParams(params_h);
			if(deptHandleList.size() > 0){
				map.put("dph", deptHandleList.get(0));
			}
			
			MsCase bo = msCaseService.searchById(id);
			map.put("bo", bo);
			
			//获取案件关联的派发单位list,用于初始化页面拼装联动单位
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("relaCaseId", bo.getCaseId());
			List<ReceiveDept> receiveDeptList = receiveDeptService.getReceiveDeptListByRelaCaseId(params);
			map.put("receiveDeptList", receiveDeptList);
			
			//获取案件关联的派发单位ldIdStr,用于页面新增的去重判断
			String recevieDeptInfoStr = JsonUtils.listToJson(receiveDeptList);
			recevieDeptInfoStr = recevieDeptInfoStr.replaceAll("\"", "'");
			map.put("recevieDeptInfoStr", recevieDeptInfoStr);
			
			//拼装联动单位jsonStr
			String ldIdArrStr = "";
			for (ReceiveDept receiveDept : receiveDeptList) {
				ldIdArrStr += receiveDept.getLdorgId() +",";
				map.put("ldIdArrStr", ldIdArrStr.substring(0, ldIdArrStr.length()-1));
			}
			
			url = "/ypms/mscase/edit_msCase.ftl";
		}else{//新增登记
			
			//进入登记页发方式，auto:电话打进是自动打开、manual:操作员手动登记
			String registerType = request.getParameter("registerType");
			if ("auto".equals(registerType)) {//接听来电自动打开登记页面
				//需要自动填入主叫号码，被叫号码，呼叫时间，客户信息
				//主叫号码
				String callinNum = request.getParameter("callinNum");
				map.put("callinNum", callinNum);
				//被叫号码
				String calledNum = request.getParameter("calledNum");
				map.put("calledNum", calledNum);
				//呼叫时间
				String callinTimeStr = request.getParameter("callinTime");
				map.put("callinTimeStr", callinTimeStr);
				
			}else if ("manual".equals(registerType)){//手动登记
				
				String dateSdf = "yyyy-MM-dd HH:mm:ss";
				Date currDate = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat(dateSdf);
				String callinTimeStr = "";
				if(currDate != null){
					callinTimeStr = sdf.format(currDate);
				}
				map.put("callinTimeStr", callinTimeStr);
				
			}
			
			url = "/ypms/mscase/form_msCase.ftl";
		}
		
		//附件上传控件参数
		map.addAttribute("bizType", ATTACHMENT_TYPE_YPMSCASE);
		map.addAttribute("SQ_FILE_URL", App.SQFILE.getDomain(session));
		
		return url;
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		MsCase bo,DeptHandle dh) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> params_a = new HashMap<String, Object>();
		params_a.put("bo", bo);
		params_a.put("dh", dh);
		//当前用户信息
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		params_a.put("userInfo", userInfo);
		
		//菜单类型
		String menuType = request.getParameter("menuType");
		params_a.put("menuType", menuType);
		
		//派发单位List数据(包含专业化队伍),需要入库
		String recevieDeptInfoStr = request.getParameter("recevieDeptInfoStr");
		List<ReceiveDept> recevieDeptList = jsonStrToListBO(recevieDeptInfoStr);
		params_a.put("recevieDeptList", recevieDeptList);
		
		//筛选出需要发短信的联动单位
		List<ReceiveDept> sendMsgTeamList = new ArrayList<ReceiveDept>();
		for (ReceiveDept receiveDept : recevieDeptList) {
			if (!"".equals(receiveDept.getMsgSendPepo())) {
				sendMsgTeamList.add(receiveDept);
			}
		}
		
		//获取需要发送的号码list
		List<String> mobilePhonesList = new ArrayList<String>();
		for (ReceiveDept receiveDept : sendMsgTeamList) {
			String msgSendPepo = receiveDept.getMsgSendPepo();
			List<AddressBook> addressBookList = addressBookService.searchByLdId(receiveDept.getLdorgId());
			
			if("01".equals(msgSendPepo)){//分管领导
				for (AddressBook addressBook : addressBookList) {
					if("0".equals(addressBook.getAbRole())){//分管领导
						mobilePhonesList.add(addressBook.getAbMobile());
					}
				}
			}else if ("02".equals(msgSendPepo)){//联络员
				for (AddressBook addressBook : addressBookList) {
					if("1".equals(addressBook.getAbRole())){//联络员
						mobilePhonesList.add(addressBook.getAbMobile());
					}
				}
			}else if ("12".equals(msgSendPepo)){//分管领导+联络员
				for (int i = 0; i < addressBookList.size(); i++) {
					mobilePhonesList.add(addressBookList.get(i).getAbMobile());
				}
			}
		}
		
		String result = "fail";
		if (bo.getCaseId() == null) { //新增
			String insertType = request.getParameter("insertType");
			params_a.put("insertType", insertType);
			Long id = msCaseService.insert(params_a);
			
			if (id != null && id > 0) {
				//发送短信,编辑短信内容，发送对象号码数组
				//短信模板：【案件描述】，请安排处理，联系人姓名【联系人姓名】，联系电话【联系电话】。
				String smsContent = "【"+bo.getCaseDesc()+"】，请安排处理，联系人姓名【"+bo.getCustomerName()+"】，联系电话【"+bo.getCallinNum()+"】";
				String[] mobilePhones = new String[mobilePhonesList.size()];
				mobilePhonesList.toArray(mobilePhones);
				SendResult rs = sendMsg(session, smsContent, mobilePhones);
				String msgTip = rs.getTaskId();
				String msgNote = rs.getNote();
				resultMap.put("msgTip", msgTip);
				resultMap.put("msgNote", msgNote);
				//附件上传
				if (bo.getAttachmentId() != null && bo.getAttachmentId().length > 0) {
					boolean uploadResult = attachmentService.updateBizId(id, ATTACHMENT_TYPE_YPMSCASE, bo.getAttachmentId());
				}
				result = "success";
			};
		} else { //修改
			
			String updateType = request.getParameter("updateType");
			params_a.put("updateType", updateType);
			boolean updateResult = msCaseService.update(params_a);
			if (updateResult) {
				if("01".equals(updateType)){//派发就要再发短信
					//发送短信,编辑短信内容，发送对象号码数组
					//短信模板：【案件描述】，请安排处理，联系人姓名【联系人姓名】，联系电话【联系电话】。
					String smsContent = "【"+bo.getCaseDesc()+"】，请安排处理，联系人姓名【"+bo.getCustomerName()+"】，联系电话【"+bo.getCallinNum()+"】";
					String[] mobilePhones = new String[mobilePhonesList.size()];
					mobilePhonesList.toArray(mobilePhones);
					SendResult rs = sendMsg(session, smsContent, mobilePhones);
					String msgTip = rs.getTaskId();
					String msgNote = rs.getNote();
					resultMap.put("msgTip", msgTip);
					resultMap.put("msgNote", msgNote);
				}
				
				//附件上传
				if (bo.getAttachmentId() != null && bo.getAttachmentId().length > 0) {
					boolean uploadResult = attachmentService.updateBizId(bo.getCaseId(), ATTACHMENT_TYPE_YPMSCASE, bo.getAttachmentId());
				}
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
		MsCase bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = msCaseService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 跳转操作员回退填写回退信息的页面
	 * @author zhangzhenhai
	 * @date 2018-4-26 下午4:27:05
	 * @param @param request
	 * @param @param session
	 * @param @param map
	 * @param @return    
	 * @return Object
	 */
	@RequestMapping("/toRejectContent")
	public Object toRejectContent(HttpServletRequest request, HttpSession session, ModelMap map) {
		String caseId = request.getParameter("caseId");
		map.put("caseId", caseId);
		String chain = request.getParameter("chain");//驳回的环节
		map.put("chain", chain);
		String rdIdStr = request.getParameter("rdIdStr");//驳回的环节
		map.put("rdIdStr", rdIdStr);
		
		return "/ypms/mscase/rejectContent.ftl";
	}
	
	/**
	 * 保存操作员操作意见(驳回、回退),更新联动单位状态
	 * @author zhangzhenhai
	 * @date 2018-4-26 下午4:40:05
	 * @param @param request
	 * @param @param session
	 * @param @param map
	 * @param @param caseHandler
	 * @param @return    
	 * @return Object
	 */
	@ResponseBody
	@RequestMapping("/saveRejectContent")
	public Object saveRejectContent(HttpServletRequest request, HttpSession session, ModelMap map,
			CaseHandler caseHandler) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		caseHandler.setHandlerId(userInfo.getUserId());
		caseHandler.setHandlerName(userInfo.getUserName());
		caseHandler.setHandlerOrgId(userInfo.getOrgId());
		caseHandler.setHandlerOrgCode(userInfo.getOrgCode());
		caseHandler.setHandlerTel(userInfo.getVerifyMobile());
		caseHandler.setHandleTime(new Date());
		
		String chain = request.getParameter("chain");
		//chain:环节编号，决定了操作员操作环节的编号
		if("01".equals(chain)){//委办局驳回
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("caseHandler", caseHandler);
			params.put("userInfo", userInfo);
			boolean rejectResult = msCaseService.deptReject(params);
			
		}else if ("02".equals(chain)){//中心回退
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("request", request);
			params.put("caseHandler", caseHandler);
			boolean rejectResult = msCaseService.centerRegresses(params);
			
		}
		
		Long id = caseHandlerService.insert(caseHandler);
		if (id != null && id > 0) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	
	/**
	 * 获取信息域id，code，name,设置到session中
	 * @author zhangzhenhai
	 * @date 2018-4-20 下午5:14:40
	 * @param @param session
	 * @param @param map
	 * @param @param orgType    
	 * @return void
	 */
	public void setOrgInfoToSession(HttpSession session, ModelMap map){
		Map<String, Object>  orgInfo = this.getDefaultOrgInfo(session);
		Long orgInfoId = (Long) orgInfo.get(KEY_DEFAULT_INFO_ORG_ID);
		String orgInfoCode = (String) orgInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		String orgInfoName = (String) orgInfo.get(KEY_DEFAULT_INFO_ORG_NAME);
		
		map.put("orgInfoId", orgInfoId);
		map.put("orgInfoCode", orgInfoCode);
		map.put("orgInfoName", orgInfoName);
		
	}
	
	/**
	 * json字符串数据转list数据
	 * @author zhangzhenhai
	 * @date 2018-4-26 上午10:58:16
	 * @param @param jsonStr
	 * @param @return    
	 * @return List<ReceiveDept>
	 */
	public static List<ReceiveDept> jsonStrToListBO(String jsonStr){
		List<ReceiveDept> list = new ArrayList<ReceiveDept>();
		if(jsonStr != null && !"".equals(jsonStr)){
			jsonStr = jsonStr.replaceAll("'", "\"");
			System.out.println(jsonStr);
			list = JSON.parseArray(jsonStr, ReceiveDept.class);  
		}
		return list;
		
	}
	
	/**
	 * 发送短信
	 * @author zhangzhenhai
	 * @date 2018-4-26 上午11:09:13
	 * @param @param session
	 * @param @param smsContent
	 * @param @param mobilePhones
	 * @param @return    
	 * @return SendResult
	 */
	public SendResult sendMsg(HttpSession session,String smsContent,String[] mobilePhones ){
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		SendResult result = sendSmsService.sendSms(ConstantValue.SMS_PLATFORM_FLAG,
				userInfo.getUserId(), userInfo.getOrgCode(),
				mobilePhones, smsContent, SendSmsService.TYPE_SMS, null,
				null);
		
		return result;
	}
	
	
	public String formatDateToString(Date paramDate,String dateSdf){
		
		SimpleDateFormat sdf = new SimpleDateFormat(dateSdf);
		String dateStr = "";
		if(paramDate != null){
			dateStr = sdf.format(paramDate);
		}
		return dateStr;
	}
	
	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/isArchiver")
	public Object isArchiver(HttpServletRequest request, HttpSession session, ModelMap map,
		MsCase bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = msCaseService.isArchiver(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
}