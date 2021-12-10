package cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.elastic.util.CommonFunctions;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.ProfessionBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.ProfessionOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.IReportMsgCCCfgService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;

/**
 * @Description: 消息配送人员配置
 * @ClassName:   ReportMsgCCCfgController   
 * @author:      张联松(zhangls)
 * @date:        2020年10月16日 上午9:14:06
 */
@Controller
@RequestMapping("/zhsq/reportMsgCCCfg")
public class ReportMsgCCCfgController extends ZZBaseController {
	@Autowired
	private IReportMsgCCCfgService reportMsgCCCfgService;
	
	@Autowired
	private ProfessionOutService professionService;
	
	/**
	 * 跳转新增页面
	 * @param session
	 * @param params
	 * 			cfgUUID	人员配置UUID，编辑时传入
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd(HttpSession session, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		Map<String, Object> cfgMap = null;
		
		if(CommonFunctions.isNotBlank(params, "cfgUUID")) {
			String cfgUUID = params.get("cfgUUID").toString();
			params = params == null ? new HashMap<String, Object>() : params;
			
			params.put("isDictTransfer", false);
			
			cfgMap = reportMsgCCCfgService.findCfgInfoByUUID(cfgUUID, params);
		} else {
			cfgMap = new HashMap<String, Object>();
		}
		
		map.addAttribute("cfgInfo", cfgMap);
		map.addAttribute("professionDictJsonStr", capProfessionDictJson());
		
		return "/zzgl/reportFocus/reportMsgCCCfg/add_msg_cc_cfg.ftl";
	}
	
	/**
	 * 跳转详情页面
	 * @param session
	 * @param cfgUUID	人员配置UUID
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toDetail")
	public String toDetail(HttpSession session, 
			@RequestParam(value = "cfgUUID") String cfgUUID, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		Map<String, Object> cfgMap = reportMsgCCCfgService.findCfgInfoByUUID(cfgUUID, params);
		
		if(cfgMap == null) {
			cfgMap = new HashMap<String, Object>();
		}
		
		map.addAttribute("cfgInfo", cfgMap);
		
		return "/zzgl/reportFocus/reportMsgCCCfg/detail_msg_cc_cfg.ftl";
	}
	
	/**
	 * 跳转列表页面
	 * @param session
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public String toList(HttpSession session, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		
		return "/zzgl/reportFocus/reportMsgCCCfg/list_msg_cc_cfg.ftl";
	}
	
	/**
	 * 保存/更新人员配置信息
	 * @param session
	 * @param cfgMap	人员配置信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveCfgInfo")
	public ResultObj saveCfgInfo(HttpSession session, 
			@RequestParam Map<String, Object> cfgMap) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String cfgUUID = null;
		ResultObj resultObj = null;
		
		try {
			cfgUUID = reportMsgCCCfgService.saveOrUpdateCfgInfo(cfgMap, userInfo);
			resultObj = Msg.OPERATE.getResult(StringUtils.isNotBlank(cfgUUID));
		} catch (Exception e) {
			resultObj = Msg.OPERATE.getResult(false);
			resultObj.setTipMsg(e.getMessage());
			e.printStackTrace();
		}
		
		return resultObj;
	}
	
	/**
	 * 删除人员配置信息
	 * @param session
	 * @param cfgUUID	人员配置UUID
	 * @param cfgMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delCfgInfo")
	public ResultObj delCfgInfo(HttpSession session, 
			@RequestParam(value = "cfgUUID") String cfgUUID, 
			@RequestParam Map<String, Object> cfgMap) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		ResultObj resultObj = Msg.DELETE.getResult(reportMsgCCCfgService.delCfgInfoByUUID(cfgUUID, userInfo));
		
		return resultObj;
	}
	
	/**
	 * 分页加载人员配置信息
	 * @param session
	 * @param page
	 * @param rows
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listData")
	public EUDGPagination listData(HttpSession session, 
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, 
			@RequestParam Map<String, Object> params) {
		EUDGPagination reportFocusPagination = null;

		params = params == null ? new HashMap<String, Object>() : params;
		
		reportFocusPagination = reportMsgCCCfgService.findCfg4Pagination(page, rows, params);
		
		return reportFocusPagination;
	}
	
	/**
	 * 获取专业json串信息
	 * @return	[{'name':'政府管理', 'value':'zfgl'}]
	 */
	private String capProfessionDictJson() {
		List<ProfessionBO> professionAllList = professionService.findAllProfessionBO();
		StringBuffer professionCodeBuffer = new StringBuffer("");
		
		if(professionAllList != null) {
			String professionCode = null;
			
			for(ProfessionBO profession : professionAllList) {
				professionCode = profession.getProfessionCode();
				
				if(StringUtils.isNotBlank(professionCode)) {
					professionCodeBuffer.append(",{").append("'name':'").append(profession.getProfessionName()).append("(").append(professionCode).append(")").append("', ")
										.append("'value':'").append(professionCode).append("'}");
				}
			}
			
			if(professionCodeBuffer.length() > 0) {
				professionCodeBuffer = new StringBuffer("[").append(professionCodeBuffer.substring(1)).append("]");
			}
		}
		
		return professionCodeBuffer.toString();
	}
}
