package cn.ffcs.zhsq.gdpersionsendflow.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.ffcs.system.publicUtil.StringUtils;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.gdpersionsendflow.service.IGdPersionSendFlowService;
import cn.ffcs.zhsq.gdpersionsendflow.service.IGdPersionSendWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.common.Msg;
import cn.ffcs.zhsq.mybatis.domain.gdpersionsendflow.GdPersionSendFlow;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.ResultObj;

/**   
 * @Description: 网格员协助送达流程模块控制器
 * @Author: zhangch
 * @Date: 10-16 09:18:15
 * @Copyright: 2019 福富软件
 */ 
@Controller("gdPersionSendFlowController")
@RequestMapping("/zhsq/gdPersionSendFlow")
public class GdPersionSendFlowController {

	@Autowired
	private IGdPersionSendFlowService gdPersionSendFlowService; //注入网格员协助送达流程模块服务
	@Autowired
	private IGdPersionSendWorkflowService gdPersionSendWorkflowService;

	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	//===========================================张祯海  start==============================================//
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		sendDefValToModelMap(request, session, map);
		String menuType = request.getParameter("menuType");
		map.put("menuType", menuType);
		return "/zzgl/gdpersionsendflow/gd_persion_send_flow_list.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,
		GdPersionSendFlow gdPersionSendFlow) {
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute(ConstantValue.USER_IN_SESSION);
		String menuType = request.getParameter("menuType");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("gdPersionSendFlow", gdPersionSendFlow);
		params.put("menuType", menuType);
		params.put("userInfo", userInfo);
		EUDGPagination pagination = gdPersionSendFlowService.searchList(params);
		return pagination;
	}

	
	
	/**
	 * 办理的详情页面
	 */
	@RequestMapping("/toHandle")
	public Object toHandle(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		setFileComponentsDomain( session, map);
		GdPersionSendFlow bo = gdPersionSendFlowService.searchById(id);
		map.addAttribute("bo", bo);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);  //替换成本地常量
		try {
			long instanceId = gdPersionSendWorkflowService.capInstanceId(id,userInfo);
			map.addAttribute("instanceId", instanceId);
		} catch (Exception e) {
			logger.error("获取工作流实例ID异常！",e);
		}

		return "/zzgl/gdpersionsendflow/gd_persion_send_flow_handle.ftl";
	}
	
	
	/**
	 * 保存办理意见
	 * @param request
	 * @param session
	 * @param map
	 * @param bo 实体
	 * @param saveType 保存类型 1 草稿 2 发布 启动工作流
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveHandleInfo")
	public Object saveHandleInfo(HttpServletRequest request, HttpSession session, ModelMap map,
		GdPersionSendFlow bo) {
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute(ConstantValue.USER_IN_SESSION);
		Msg msg = Msg.error("提交失败!");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userInfo", userInfo);
		//下一环节的nodeName
		String nextLink = request.getParameter("nextLink");
		params.put("nextLink", nextLink);
		//发送类型（组织，网格员）
		String sendType = request.getParameter("sendType");
		params.put("sendType", sendType);
		//办理组织的信息
		String sendOrgCode = request.getParameter("sendOrgCode");
		params.put("sendOrgCode", sendOrgCode);
		//办理人员（网格员）的信息
		String handlerInfoJson = request.getParameter("handlerInfoJson");
		params.put("handlerInfoJson", handlerInfoJson);
		//网格员反馈信息
		String advice = request.getParameter("advice");
		params.put("advice", advice);
		try {
			boolean flag = gdPersionSendFlowService.saveHandleInfo(bo,params);
			if (flag) {
				msg = Msg.success("提交成功!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getMessage()!=null)
				msg.setMsg(e.getMessage());
		}
		return msg;
	}

	/**
	 * 驳回页面
	 */
	@RequestMapping("/reject")
	public Object reject(HttpServletRequest request, HttpSession session, ModelMap map,Long id) {
		GdPersionSendFlow bo = new GdPersionSendFlow();
		if (id != null) {
			bo = gdPersionSendFlowService.searchById(id);
		}else{
			bo.setPublishDate(new Date());
			bo.setPublishDateStr(DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
		}
		map.put("bo", bo);
		return "/zzgl/gdpersionsendflow/gd_persion_send_flow_reject.ftl";
	}

	/**
	 * 驳回办理意见
	 * @param request
	 * @param session
	 * @param map
	 * @param bo 实体
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/rejectHandleInfo")
	public Object rejectHandleInfo(HttpServletRequest request, HttpSession session, ModelMap map,
								 GdPersionSendFlow bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");

		ResultObj resultObj = new ResultObj("驳回成功!",null);
		//驳回
		int workType = gdPersionSendFlowService.reject(bo,userInfo);
		if(workType != 1){
			resultObj.setSuccess(false);
			resultObj.setType(String.valueOf(workType));
			resultObj.setTipMsg("驳回失败！");
		}
		return resultObj;
	}

	//===========================================张祯海  end==============================================//
	/**
	 * 详情页面
	 */
	@RequestMapping("/view")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		sendDefValToModelMap(request, session, map);
		setFileComponentsDomain( session, map);
		GdPersionSendFlow bo = gdPersionSendFlowService.searchById(id);
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);  //替换成本地常量
		try {
			long instanceId = gdPersionSendWorkflowService.capInstanceId(id,userInfo);
			map.addAttribute("instanceId", instanceId);
		} catch (Exception e) {
			logger.error("获取工作流实例ID异常！",e);
		}
		map.addAttribute("isFormLast", request.getParameter("isFormLast"));

		//流程当前环节信息
		try {
			Map<String, Object> currLinkData = gdPersionSendWorkflowService.findCurTaskData(bo.getSendId(), userInfo);
			if(currLinkData!=null) {
				String currLink = "05".equals(bo.getStatus())?"-":(String)currLinkData.get("WF_ACTIVITY_NAME_");
				String currOrgName = (String) currLinkData.get("WF_ORGNAME");
				String currUserName = (String) currLinkData.get("WF_USERNAME");
				
				//设置更新的参数
				bo.setCurrentCodeCn(currOrgName);//当前组织名称
				bo.setCurrentHandler(currUserName);//当前办理人
				bo.setCurrentNode(currLink);//当前组织环节
				bo.setCurrLink(currLink);//当前环节
				
				//当前环节的节点code(task1..)
				map.addAttribute("nodeName", currLinkData.get("NODE_NAME"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String showHandle = request.getParameter("showHandle");
		map.put("showHandle", showHandle);
		
		map.addAttribute("bo", bo);
		return "/zzgl/gdpersionsendflow/gd_persion_send_flow_view.ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		setFileComponentsDomain( session, map);
		GdPersionSendFlow bo = new GdPersionSendFlow();
		if (id != null) {
			bo = gdPersionSendFlowService.searchById(id);
		}else{
			bo.setPublishDate(new Date());
			bo.setPublishDateStr(DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
		}
		map.put("bo", bo);

		return "/zzgl/gdpersionsendflow/gd_persion_send_flow_form.ftl";
	}

	private void setFileComponentsDomain(HttpSession session, ModelMap map) {
		map.addAttribute("componentsDomain", App.COMPONENTS.getDomain(session));
		map.addAttribute("skyDomain",App.SKY.getDomain(session));
		map.addAttribute("uiDomain",App.UI.getDomain(session));
		map.addAttribute("imgDomain", App.IMG.getDomain(session));
		map.addAttribute("fileDomain", App.SQFILE.getDomain(session));
	}

	/**
	 *
	 * @param request
	 * @param session
	 * @param map
	 * @param bo 实体
	 * @param saveType 保存类型 1 草稿 2 发布 启动工作流
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		GdPersionSendFlow bo,String saveType) {
		String message = "保存失败!";
		int startSuccess = -99;//非 -99 表示工作流异常。
		if(!bo.getStatus().equals("01")){
			message = "状态不正确!";
			ResultObj resultObj = new ResultObj(String.valueOf(startSuccess),false,message,null);
			return resultObj;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);  //替换成本地常量
		//限制天数转限制日期
		bo.setLimitDate(DateUtils.addInterval(bo.getPublishDate(),bo.getLimitDateNum(), "00"));
		Long sendId = null;
		try {
			if (bo.getSendId() == null) { //新增
				bo.setCreator(userInfo.getUserId());  //设置创建人
				bo.setOrgCode(userInfo.getOrgCode());
				Long id= gdPersionSendFlowService.insert(bo);
				if (id != null && id > 0) {
					message = "保存成功！";
					sendId = id;
				}
			} else { //修改
				bo.setUpdator(userInfo.getUserId());  //设置更新人
				boolean updateResult = gdPersionSendFlowService.update(bo);
				if (updateResult) {
					message = "保存成功！";
					sendId = bo.getSendId();
				}
			}
			//启动工作流
			if(saveType.equals("2")&& sendId != null){
				startSuccess = gdPersionSendFlowService.startWorkflow(sendId,userInfo,null);
				if(startSuccess != 1){
					sendId =null;
					message = "提交失败！";
				}else{
					//获取当前节点并写入
					Map<String, Object>  re = gdPersionSendWorkflowService.findCurTaskData(sendId,userInfo);
					bo.setCurrentNode((String) re.get("WF_ACTIVITY_NAME_"));
					bo.setCurrentCodeCn((String) re.get("WF_ORGNAME"));
					String currOrgId = (String) re.get("WF_ORGID");
					String currOrgCode = orgSocialInfoService.findByOrgId(Long.valueOf(currOrgId)).getOrgCode();
					if(!StringUtils.isBlank(currOrgCode)){
						bo.setCurrentCode(currOrgCode);
					}
					bo.setCurrentHandler((String) re.get("WF_USERNAME"));
					bo.setUpdator(userInfo.getUserId());  //设置更新人
					bo.setStatus("02");
					gdPersionSendFlowService.update(bo);
				}
			}
		} catch (Exception e) {
			sendId = null;
			logger.error("网格员协作异常！",e);
			message = "内部异常！";
		}
		ResultObj resultObj = new ResultObj(String.valueOf(startSuccess),sendId != null,message,null);
		return resultObj;
	}

	/**
	 * 重启工作流表单页面
	 */
	@RequestMapping("/resend")
	public Object restart(HttpServletRequest request, HttpSession session, ModelMap map,
					   Long id) {
		setFileComponentsDomain( session, map);
		if (id != null) {
			GdPersionSendFlow bo = gdPersionSendFlowService.searchById(id);
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);  //替换成本地常量
			GdPersionSendFlow resBo = new GdPersionSendFlow(bo);
			resBo.setLastSendId(bo.getSendId());
			resBo.setStatus("01");
			resBo.setCreator(userInfo.getUserId());
			try {
				int success = gdPersionSendFlowService.resInsert(resBo, userInfo);
				//入库 失败
				if (success != 1) {
					resBo = null;
				}
			} catch (Exception e) {
				resBo = null;
				logger.error("保存数据异常！", e);
			}
			map.put("bo", resBo);
		}
		return "/zzgl/gdpersionsendflow/gd_persion_send_flow_form.ftl";
	}

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		GdPersionSendFlow bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION"); //替换成本地常量
		bo.setUpdator(userInfo.getUserId());  //设置更新人
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = gdPersionSendFlowService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}


	/**
     * 设置modleMap的默认常用默认值
     * @author zhangzhenhai
     * @date 2019-1-31 下午3:31:24
     * @param @param request
     * @param @param session
     * @param @param map
     * @return void
     */
    private void sendDefValToModelMap(HttpServletRequest request, HttpSession session, ModelMap map){
    	UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
    	map.put("userInfo", userInfo);
		// 地域Code
    	map.put("regionId", userInfo.getOrgId());
    	//map.put("regionId", 406607);
		map.put("regionCode", userInfo.getOrgCode());
		map.put("regionName", userInfo.getOrgName());
    }

}