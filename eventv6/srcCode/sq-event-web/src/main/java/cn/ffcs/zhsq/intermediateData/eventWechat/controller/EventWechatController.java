package cn.ffcs.zhsq.intermediateData.eventWechat.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import cn.ffcs.shequ.crypto.HashIdManager;
import cn.ffcs.system.publicFilter.CommonController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.intermediateData.eventVerify.service.IEventVerifyBaseService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;

/**   
 * @Description: 微信事件信息对接模块控制器
 * @Author: zhangls
 * @Date: 08-21 09:05:05
 * @Copyright: 2017 福富软件
 */ 
@Controller
@RequestMapping("/zhsq/eventWechat")
public class EventWechatController extends ZZBaseController {

	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	
	@Autowired
	private IEventVerifyBaseService eventVerifyBaseService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	// 中间表记录
	//@Autowired
	//private IDataExchangeStatusService dataExchangeStatusService;
	
	/**
	 * 跳转微信事件列表
	 * 不设置infoOrgCode的默认值，因为存在默认不查询本辖区内的情况
	 * @param session
	 * @param params
	 * 			isJurisdictionQuery		是否进行辖区内查询；true 则获取辖区内数据
	 * 			isDefaultJurisdiction	是否使用默认地域查询；默认为true
	 * @param map
	 * @return
	 */
	@RequestMapping("/toList")
	public String toList(HttpSession session, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		map.addAttribute("EVENT_VERIFY_STATUS_PCODE", ConstantValue.EVENT_VERIFY_STATUS_PCODE);
		
		// 获取平台来源字典查询父级编码
		String mapkeyValue = null;
		String EVENT_VERIFY_FUNC_CODE = "EVENT_VERIFY_ATTRIBUTE";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String userOrgCode = userInfo.getOrgCode();
		mapkeyValue = funConfigurationService.turnCodeToValue(EVENT_VERIFY_FUNC_CODE, "bizPlatformSearchPcode",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);

		map.addAttribute("EVENT_VERIFY_BIZPLATFORM_PCODE", mapkeyValue);

		if (!params.isEmpty()) {
			map.addAllAttributes(params);
			map.addAttribute("extraParam", params);
		}
		
		String privateFolder = funConfigurationService.turnCodeToValue(ConstantValue.EVENT_VERIFY_ATTRIBUTE, ConstantValue.PRIVATE_FOLDER_NAME4_EVENT_VERIFY + "List", IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
		return "/zzgl/intermediateData/eventWechat" + privateFolder + "/list_eventWechat.ftl";
	}

	/**
	 * 跳转微信事件编辑页面
	 * @param session
	 * @param eventVerifyHashId		加密审核记录id
	 * @param verifyType		审核类型
	 * 				Enterprise	政企直通
	 * @param eventVerifyMap	额外参数
	 * @param map
	 * @return
	 */
	@RequestMapping("/toEdit")
	public String toEdit(HttpSession session, 
//			@RequestParam(value = "eventVerifyId") Long eventVerifyId,
			@RequestParam(value = "eventVerifyHashId") String eventVerifyHashId,
			@RequestParam(value = "verifyType", required = false) String verifyType,
			@RequestParam Map<String, Object> eventVerifyMap,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> eventVerify = null,
							params = new HashMap<String, Object>();
		String infoOrgCode = this.getDefaultInfoOrgCode(session),
			   userOrgCode = userInfo.getOrgCode();
		
		eventVerifyMap.put("userOrgCode", userOrgCode);
		params.put("userOrgCode", userOrgCode);
		params.put("verifyType", verifyType);
		
		eventVerify = eventVerifyBaseService.findEventVerifyById(HashIdManager.decryptLong(eventVerifyHashId), eventVerifyMap);
		String privateFolder = funConfigurationService.turnCodeToValue(ConstantValue.EVENT_VERIFY_ATTRIBUTE, ConstantValue.PRIVATE_FOLDER_NAME4_EVENT_VERIFY + "Add", IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
		
		if(eventVerify == null) {
			eventVerify = new HashMap<String, Object>();
		} else if(CommonFunctions.isNotBlank(eventVerify, "infoOrgCode")) {
			infoOrgCode = eventVerify.get("infoOrgCode").toString();
			if(userInfo.getInfoOrgList()!=null && userInfo.getInfoOrgList().size()>0) {
				if(!eventVerify.get("infoOrgCode").toString().startsWith(userInfo.getInfoOrgList().get(0).getOrgCode())) {
					map.addAttribute("eventWechat", new HashMap<String, Object>());
					return "/zzgl/intermediateData/eventWechat" + privateFolder + "/detail_eventWechat.ftl";
				}
			}
		}
		eventVerify.put("eventVerifyHashId",eventVerifyHashId);
		
		if(StringUtils.isNotBlank(infoOrgCode)) {
			MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(infoOrgCode);
			if(gridInfo != null) {
				map.addAttribute("startGridId", gridInfo.getParentGridId());
				map.addAttribute("gridName", gridInfo.getGridName());
				map.addAttribute("gridId", gridInfo.getGridId());
				map.addAttribute("gridLevel", gridInfo.getGridLevel());
			}
		}
		
		map.addAttribute("eventWechat", eventVerify);
		map.addAttribute("verifyType", verifyType);
		map.addAttribute("imgDownPath", App.IMG.getDomain(session));//用于附件上传组件中的缩略图展示
		map.addAttribute("markerOperation", ConstantValue.TITLE_MARKER);//标注操作
		map.addAllAttributes(eventVerifyBaseService.fetchParam4Event(eventVerifyMap));
		
		
		return "/zzgl/intermediateData/eventWechat" + privateFolder + "/add_eventWechat.ftl";
	}
	
	/**
	 * 跳转微信事件详情页面
	 * @param session
	 * @param eventVerifyHashId		加密的审核记录id
	 * @param isClosable		是否展示关闭按钮
	 * @param verifyType		审核类型
	 * 				Enterprise	政企直通
	 * @param eventVerifyMap	额外参数
	 * @param map
	 * @return
	 */
	@RequestMapping("/toDetail")
	public String toDetail(HttpSession session, 
//			@RequestParam(value = "eventVerifyId") Long eventVerifyId,
			@RequestParam(value = "eventVerifyHashId") String eventVerifyHashId,
			@RequestParam(value = "isClosable", required=false, defaultValue="true") boolean isClosable,
			@RequestParam(value = "verifyType", required = false) String verifyType,
			@RequestParam Map<String, Object> eventVerifyMap,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> eventWechat = null,
							params = new HashMap<String, Object>();
		
		String infoOrgCode = null, userOrgCode = userInfo.getOrgCode();
		
		eventVerifyMap.put("userOrgCode", userOrgCode);

		eventWechat = eventVerifyBaseService.findEventVerifyById(HashIdManager.decryptLong(eventVerifyHashId), eventVerifyMap);
		
		if(CommonFunctions.isNotBlank(eventWechat, "infoOrgCode")) {
			infoOrgCode = eventWechat.get("infoOrgCode").toString();
			if(userInfo.getInfoOrgList()!=null && userInfo.getInfoOrgList().size()>0) {
				if(!eventWechat.get("infoOrgCode").toString().startsWith(userInfo.getInfoOrgList().get(0).getOrgCode())) {
					eventWechat = new HashMap<String, Object>();
				}
			}
		}
		
		if(StringUtils.isNotBlank(infoOrgCode)) {
			MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(infoOrgCode);
			if(gridInfo != null) {
				map.addAttribute("gridId", gridInfo.getGridId());
			}
		}
		
		params.put("userOrgCode", userInfo.getOrgCode());
		params.put("verifyType", verifyType);
		
		map.addAttribute("isClosable", isClosable);
		map.addAttribute("eventWechat", eventWechat);
		map.addAttribute("imgDownPath", App.IMG.getDomain(session));//用于附件上传组件中的缩略图展示
		map.addAttribute("markerOperation", ConstantValue.TITLE_MARKER);//标注操作
		map.addAllAttributes(eventVerifyBaseService.fetchParam4Event(params));
		
		String privateFolder = funConfigurationService.turnCodeToValue(ConstantValue.EVENT_VERIFY_ATTRIBUTE, ConstantValue.PRIVATE_FOLDER_NAME4_EVENT_VERIFY + "Detail", IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
		
		return "/zzgl/intermediateData/eventWechat" + privateFolder + "/detail_eventWechat.ftl";
	}
	
	/**
	 * 保存微信事件记录
	 * @param session
	 * @param eventWechat
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveEventWechat")
	public ResultObj saveEventWechat(HttpSession session, 
			@RequestParam Map<String, Object> eventVerify,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Long eventVerifyId = null;
		boolean result = false;
		Msg msg = Msg.EDIT;
		ResultObj resultObj = null;
		
		if(CommonFunctions.isNotBlank(eventVerify, "eventVerifyHashId")) {
			try {
				eventVerifyId = HashIdManager.decryptLong(eventVerify.get("eventVerifyHashId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		eventVerify.put("userOrgCode", userInfo.getOrgCode());
		
		try {
			if(eventVerifyId != null && eventVerifyId > 0) { //修改
				eventVerify.put("eventVerifyId", eventVerifyId);
				result = eventVerifyBaseService.updateEventVerifyById(eventVerify, userInfo);
			} else { //新增
				result = eventVerifyBaseService.saveEventVerify(eventVerify) > 0;
				msg = Msg.ADD;
			}
			
			resultObj = msg.getResult(result);
		} catch(ZhsqEventException e) {
			resultObj = msg.getResult(e);
			resultObj.setTipMsg(e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			resultObj = msg.getResult(e);
		}
		
		return resultObj;
	}

	/**
	 * 删除微信事件记录
	 * @param session
	 * @param eventVerifyHashId
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delEventWechat")
	public ResultObj delEventWechat(HttpSession session, 
//			@RequestParam(value = "eventVerifyId") Long eventVerifyId,
			@RequestParam(value = "eventVerifyHashId") String eventVerifyHashId,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> eventVerify = new HashMap<String, Object>();
		eventVerify.put("userOrgCode", userInfo.getOrgCode());
		boolean delResult = false;
		eventVerify = eventVerifyBaseService.findEventVerifyById(HashIdManager.decryptLong(eventVerifyHashId), eventVerify);
		
		if(CommonFunctions.isNotBlank(eventVerify, "infoOrgCode")) {
			if(userInfo.getInfoOrgList()!=null && userInfo.getInfoOrgList().size()>0) {
				if(eventVerify.get("infoOrgCode").toString().startsWith(userInfo.getInfoOrgList().get(0).getOrgCode())) {
					delResult = eventVerifyBaseService.deleteEventVerifyById(HashIdManager.decryptLong(eventVerifyHashId), userInfo.getUserId(), eventVerify);
				}else {
					return Msg.DELETE.getResult(delResult, "删除权限不足！");
				}
			}
		}
		
		
		return Msg.DELETE.getResult(delResult);
	}
	
	/**
	 * 作废审核表事件记录
	 * @param session
	 * @param eventVerifyHashId
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/invalidEventVerify")
	public ResultObj invalidEventVerify(HttpSession session, 
//			@RequestParam(value = "eventVerifyId") Long eventVerifyId,
			@RequestParam(value = "eventVerifyHashId") String eventVerifyHashId,
			ModelMap map) {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		Map<String, Object> eventVerify = new HashMap<String, Object>();
		
		eventVerify.put("eventVerifyId", HashIdManager.decryptLong(eventVerifyHashId));
		eventVerify.put("status", ConstantValue.IS_INVALID);
		
		boolean delResult=false;
		try {
			delResult = eventVerifyBaseService.updateEventVerifyById(eventVerify, userInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Msg.INVALID.getResult(delResult);
				
	}
	
	/**
	 * 加载微信事件记录
	 * @param session
	 * @param page		页码
	 * @param rows		每页记录
	 * @param params
	 * 			infoOrgCode	所属地域
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public EUDGPagination listData(HttpSession session, 
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		EUDGPagination pagination = null;
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		if(CommonFunctions.isBlank(params, "defaultInfoOrgCode")) {
			params.put("defaultInfoOrgCode", this.getDefaultInfoOrgCode(session));
		}
		params.put("userOrgCode", userInfo.getOrgCode());
		boolean flag = true;
		if(CommonFunctions.isNotBlank(params, "infoOrgCode")) {
			flag = super.checkDataPermission(session, CommonController.TYPE_GRID, params.get("infoOrgCode").toString());
		}
		if(flag){
			pagination = eventVerifyBaseService.findEventVerifyPagination(page, rows, params);
		}else{
			pagination = new EUDGPagination(0, new ArrayList<Map<String, Object>>());
		}
		return pagination;
	}
	
	/**
	 * 获取事件上报所需参数
	 * @param session
	 * @param params
	 * 			eventWechatId	微信事件id
	 * 			verifyType		审核类型
	 * 				Enterprise	政企直通
	 * @param map
	 * @return
	 * 		outerAttachmentIds	附件id，以英文逗号连接
	 */
	@ResponseBody
	@RequestMapping("/fetchParam4Report")
	public Map<String, Object> fetchParam4Report(HttpSession session, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		params.put("userOrgCode", userInfo.getOrgCode());
		params.put("isCapAttachmentId", true);
		
		resultMap = eventVerifyBaseService.fetchParam4Event(params);
		
		return resultMap;
	}
}