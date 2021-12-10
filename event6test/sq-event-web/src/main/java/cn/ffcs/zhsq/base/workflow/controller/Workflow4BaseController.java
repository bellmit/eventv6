package cn.ffcs.zhsq.base.workflow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 工作流相关基础操作
 * @ClassName:   Workflow4BaseController   
 * @author:      张联松(zhangls)
 * @date:        2020年12月29日 下午3:37:41
 */
@Controller
@RequestMapping("/zhsq/workflow4Base")
public class Workflow4BaseController extends ZZBaseController {
	@Autowired
	private IWorkflow4BaseService workflow4BaseService;
	
	/**
	 * 跳转督办/催办操作页面
	 * @param session
	 * @param instanceId	流程实例id
	 * @param operateType	操作类型
	 * 				0		催办
	 * 				1		督办
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddUrgeOrRemind")
	public String toAddUrgeOrRemind(HttpSession session, 
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam(value = "operateType", required = false, defaultValue = "0") Integer operateType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		params = params == null ? new HashMap<String, Object>() : params;
		
		map.addAllAttributes(params);
		map.addAttribute("instanceId",instanceId);
		map.addAttribute("operateType", operateType);
		
		return "/zzgl/event/workflow/basic/add_urge_base.ftl";
	}
	
	/**
	 * 添加督办/催办记录
	 * @param session
	 * @param instanceId		流程实例id
	 * @param remarks			督办/催办意见
	 * @param otherMobileNums	接收短信信息手机号码
	 * @param smsContent		短信内容
	 * @param operateType		操作类型
	 * 				0			催办
	 * 				1			督办
	 * @param params			额外参数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addUrgeOrRemind")
	public Map<String, Object> addUrgeOrRemind(
			HttpSession session,
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam(value = "remarks", required = false) String remindRemark,
			@RequestParam(value = "otherMobileNums", required = false) String otherMobileNums,
			@RequestParam(value = "smsContent", required = false) String smsContent,
			@RequestParam(value = "operateType", required = false, defaultValue = "0") Integer operateType,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		StringBuffer msgWrong = new StringBuffer("");
		boolean result = false;
		String category = null;
		
		params = params == null ? new HashMap<String, Object>() : params;
		
		switch(operateType) {
			case 0: {//催办
				category = "1";
				break;
			}
			case 1: {//督办
				category = "2";
				break;
			}
		}
		
		params.put("category", category);
		params.put("remindRemark", remindRemark);
		
		try {
			result = workflow4BaseService.addUrgeOrRemind(instanceId, params, userInfo);
		} catch (Exception e) {
			msgWrong.append(e.getMessage());
			e.printStackTrace();
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("operateType", operateType);
		resultMap.put("result", result);
		if(msgWrong.length() > 0) {
			resultMap.put("msgWrong", msgWrong.toString());
		}
		
		return resultMap;
	}
	
	/**
	 * 获取催办/督办记录列表
	 * @param session
	 * @param instanceId	流程实例id
	 * @param params		额外参数
	 * 			category	记录类型，1 催办；2 督办；
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findUrgeOrRemindList")
	public Map<String, Object> findUrgeOrRemindList(
			HttpSession session,
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> remindMapList = null;
		boolean result = false;
		
		try {
			remindMapList = workflow4BaseService.findUrgeOrRemindList(params);
		} catch(ZhsqEventException e) {
			resultMap.put("msgWrong", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			resultMap.put("msgWrong", e.getMessage());
			e.printStackTrace();
		}
		
		if(remindMapList != null && remindMapList.size() > 0) {
			resultMap.put("remindMapList", remindMapList);
			result = true;
		}
		
		resultMap.put("result", result);
		
		return resultMap;
	}
}
