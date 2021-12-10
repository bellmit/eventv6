package cn.ffcs.zhsq.event.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * 事件增添经办组织查询
 * @author zhangls
 *
 */
@Controller
@RequestMapping("/zhsq/event/eventOrg")
public class EventOrgController extends ZZBaseController {
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IEvent4WorkflowService event4WorkflowService;
	
	/**
	 * 跳转经办组织事件列表
	 * @param session
	 * @param params
	 * 			isForce2Unit	是否将当前组织强制转换为对应单位组织，true为是，默认为false
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toListEventOrg")
	public String toListEventOrg(HttpSession session, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String userOrgCode = userInfo.getOrgCode();
		Long orgRootId = userInfo.getOrgId();
		List<BaseDataDict> statusDC = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.STATUS_PCODE, userOrgCode);
		params = params == null ? new HashMap<String, Object>() : params;
		
		map.addAllAttributes(params);
		
		if(statusDC != null) {//合并字典名称相同的字典项
			LinkedHashMap<String, BaseDataDict> dictMap = new LinkedHashMap<String, BaseDataDict>();
			String dictName = null;
			BaseDataDict dictTmp = null;
			List<BaseDataDict> dictList = new ArrayList<BaseDataDict>();
			
			for(BaseDataDict dict : statusDC) {
				dictName = dict.getDictName();
				
				if(dictMap.containsKey(dictName)) {
					dictTmp = dictMap.get(dictName);
					dictTmp.setDictGeneralCode(dictTmp.getDictGeneralCode() + "," + dict.getDictGeneralCode());
					
					dictMap.put(dictName, dictTmp);
				} else {
					dictMap.put(dictName, dict);
				}
			}
			
			for(String key : dictMap.keySet()) {
				dictList.add(dictMap.get(key));
			}
			
			map.addAttribute("statusDC", dictList);
		}
		
		//构造nodeCode
		map.addAttribute("nodeCode", "__"+INodeCodeHandler.OPERATE_GLOBAL+"0__");
		map.addAttribute("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_ENCRYPT_INFO_ORG_CODE));
		map.addAttribute("startGridLevel", defaultGridInfo.get(KEY_START_GRID_LEVEL));
		
		if(CommonFunctions.isNotBlank(params, "isForce2Unit")) {
			boolean isForce2Unit = Boolean.valueOf(params.get("isForce2Unit").toString());
			
			if(isForce2Unit) {
				Map<String, Object> orgParam = new HashMap<String, Object>();
				List<OrgEntityInfoBO> regionInfoList = userInfo.getInfoOrgList();
				
				if(regionInfoList != null && regionInfoList.size() > 0) {
					List<OrgSocialInfoBO> orgInfoList = null;
					
					orgParam.put("regionId", regionInfoList.get(0).getOrgId());
					orgParam.put("orgType", ConstantValue.ORG_TYPE_UNIT);
					orgParam.put("professionCode", ConstantValue.GOV_PROFESSION_CODE);
					
					orgInfoList = orgSocialInfoService.findOrgSocialListByPara(orgParam);
					
					if(orgInfoList != null && orgInfoList.size() > 0) {
						orgRootId = orgInfoList.get(0).getOrgId();
					}
				}
			}
		}
		
		map.addAttribute("orgRootId", orgRootId);//从当前用户所在组织开始展示人员选择
		
		//是否设置默认查询采集时间近三个月的事件
		if(CommonFunctions.isNotBlank(params, "isEnableDefaultCreatTime")) {
			boolean isEnableDefaultCreatTime = Boolean.valueOf(params.get("isEnableDefaultCreatTime").toString());
			
			if(isEnableDefaultCreatTime) {
				if(CommonFunctions.isBlank(params, "createTimeStart")) {
					map.put("createTimeStart", DateUtils.getMonthFirstDay());
				}
				if(CommonFunctions.isBlank(params, "createTimeEnd")) {
					map.addAttribute("createTimeEnd", DateUtils.getToday());
				}
			}
		}
		
		//获取可进行远程排序的网格层级
		map.addAttribute("remotSortGridLevel", funConfigurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.REMOT_SORT_GRID_LEVEL, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0));
		
		String privateFolderName = funConfigurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE,ConstantValue.PRIVATE_FOLDER_NAME4_EVENT+"List", IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);
		
		return "/zzgl/event"+ privateFolderName +"/eventOrg/list_event_org.ftl";
	}
	
	/**
	 * 加载转派经办组织事件数据
	 * @param session
	 * @param page
	 * @param rows
	 * @param params
	 * 			infoOrgCode	网格所属地域
	 * 			code		事件编码
	 * 			type		事件类别
	 * 			statusList	事件状态
	 * 			happenTimeStart 发生开始时间
	 * 			happenTimeEnd	发生结束时间
	 * 			createTimeStart	采集开始时间
	 * 			createTimeEnd	采集结束时间
	 * 			contactUser	采集人员姓名
	 * 			collectWay	采集渠道
	 * 			source		事件来源
	 * 			influenceDegree	影响范围
	 * 			urgencyDegree	紧急程度
	 * 			attrFlag	附件类型
	 * 			keyWord		关键字
	 * 			taskStatus	办理状态
	 * 			curUserIds	任务办理人员，以英文逗号分隔
	 * 			curUserOrgIds 任务办理人员组织，以英文逗号分隔
	 * 			curOrgIds 任务办理所属组织，优先于curUserOrgIds生效
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listEventOrgData", method = RequestMethod.POST)
	public EUDGPagination listEventOrgData(
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		EUDGPagination pagination = new EUDGPagination(0, new ArrayList<List<Map<String, Object>>>());
		
		if(CommonFunctions.isNotBlank(params, "curOrgIds")) {
			params.put("eventOrgOrgId", params.get("curOrgIds"));
		} else if(CommonFunctions.isNotBlank(params, "curUserOrgIds")) {
			params.put("eventOrgOrgId", params.get("curUserOrgIds"));
		}
		
		if(CommonFunctions.isNotBlank(params, "curUserIds")) {
			params.put("eventOrgUserId", params.get("curUserIds"));
		}
		
		if(CommonFunctions.isBlank(params, "listType")) {
			params.put("listType", 3);//辖区所有(支持办理人员查询)
		}
		params.put("userOrgCode", userInfo.getOrgCode());
		
		try {
			pagination = event4WorkflowService.findEventListPagination(page, rows, params);
		} catch (ZhsqEventException e) {
			e.printStackTrace();
		}
		
		return pagination;
	}
}
