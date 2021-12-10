package cn.ffcs.zhsq.reportFocus.epidemicPreControl.controller;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl.EPCCollectSourceEnum;
import cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl.EPCDataSourceEnum;
import cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl.EPCDictEnum;
import cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl.EPCKpcTypeEnum;
import cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl.FocusReportNode352Enum;
import cn.ffcs.zhsq.reportFocus.reportFeedback.IReportFeedbackService;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.controller.ReportTwoVioPreController;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 疫情防控(Epidemic Prevention and Control)
 * @ClassName:   ReportEPCController   
 * @author:      张联松(zhangls)
 * @date:        2020年11月11日 上午11:36:25
 */
@Controller
@RequestMapping("/zhsq/reportEPC")
public class ReportEPCController extends ReportTwoVioPreController {
	@Autowired
	private IBaseDictionaryService baseDictionaryService;

	@SuppressWarnings("serial")
	private static Map<String, String[]> KPC_TYPE = new HashMap<String, String[]>() {
		{
			//网格员巡查发现
			put(EPCCollectSourceEnum.GRID_INSPECT.getCollectSource(), new String[] {EPCKpcTypeEnum.IMMIGRATION_PERSONNEL.getKpcType(), EPCKpcTypeEnum.KEY_AREA_RETURN.getKpcType(), EPCKpcTypeEnum.NOT_ISOLATED_HOME.getKpcType(), EPCKpcTypeEnum.OUTSIDE_PROVINCE.getKpcType(), EPCKpcTypeEnum.OTHER_KEY_TYPE.getKpcType()});
			//市外事组推送
			put(EPCCollectSourceEnum.FOREIGN_AFFAIRS_SECTION.getCollectSource(), new String[] {EPCKpcTypeEnum.IMMIGRATION_PERSONNEL.getKpcType()});
			//市疫情防控组推送
			put(EPCCollectSourceEnum.EPIDEMINC_PRE_CONTROL.getCollectSource(), new String[] {EPCKpcTypeEnum.KEY_AREA_RETURN.getKpcType(), EPCKpcTypeEnum.CLOSE_CONTACTS.getKpcType(), EPCKpcTypeEnum.SECONDARY_ASYMPTOMATIC.getKpcType()});
			//市大数据组推送
			put(EPCCollectSourceEnum.BIG_DATA_GROUP.getCollectSource(), new String[] {EPCKpcTypeEnum.KEY_AREA_RETURN.getKpcType()});
		}
	};
	
	/**
	 * 跳转疫情防控列表
	 * @param session
	 * @param listType		列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 我发起的
	 * 			5 辖区所有
	 * @param reportType	上报类型
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public String toList(HttpSession session, 
			@RequestParam(value = "listType") Integer listType,
			@RequestParam(value = "reportType", required=false, defaultValue="4") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {

		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		super.toList(session, listType, reportType, params, map);

		map.put("userOrgCode",userInfo.getOrgCode());

		return "/zzgl/reportFocus/epidemicPreControl/list_epc.ftl";
	}
	
	/**
	 * 跳转疫情防控阅办列表
	 * @param session
	 * @param listType		列表类型
	 * @param reportType	上报类型
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toMsgReadingList")
	public String toMsgReadingList(HttpSession session, 
			@RequestParam(value = "listType") Integer listType,
			@RequestParam(value = "reportType", required=false, defaultValue="4") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		this.toList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/epidemicPreControl/list_epc_msgReading.ftl";
	}
	
	/**
	 * 跳转疫情防控新增/编辑页面
	 * @param session
	 * @param reportUUID	上报UUID
	 * @param reportType	上报类型
	 * @param params		额外参数
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/toAdd")
	public String toAdd(HttpSession session, 
			@RequestParam(value = "reportUUID", required=false) String reportUUID,
			@RequestParam(value = "listType") Integer listType,
			@RequestParam(value = "reportType", required=false, defaultValue="4") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		String collectSource = EPCCollectSourceEnum.GRID_INSPECT.getCollectSource(), kpcTypeDictCodes = null;
		String[] initKpcType = null;
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> collectSourceMap = null;
		
		super.toAdd(session, reportUUID, listType, reportType, params, map);
		
		if(CommonFunctions.isNotBlank(map, "reportFocus")) {
			collectSourceMap = (Map<String, Object>) map.get("reportFocus");
		} else {
			collectSourceMap = params;
		}
		
		if(CommonFunctions.isNotBlank(collectSourceMap, "collectSource")) {
			collectSource = collectSourceMap.get("collectSource").toString();
		}
		
		if(CommonFunctions.isNotBlank(KPC_TYPE, collectSource)) {
			initKpcType = KPC_TYPE.get(collectSource);
		}
		
		if(initKpcType != null) {
			Map<String, Object> dictParams = new HashMap<String, Object>();
			List<BaseDataDict> kpcTypeDictList = null;
			
			dictParams.put("orgCode", userInfo.getOrgCode());
			dictParams.put("dictPcode", EPCDictEnum.KPC_TYPE.getDictCode());
			dictParams.put("dictGeneralCodeList", initKpcType);
			
			kpcTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(dictParams);
			
			if(kpcTypeDictList != null) {
				StringBuffer kpcTypeDictBuffer = new StringBuffer("");
				
				for(BaseDataDict kpcTypeDict : kpcTypeDictList) {
					kpcTypeDictBuffer.append(",").append(kpcTypeDict.getDictCode());
				}
				
				if(kpcTypeDictBuffer.length() > 0) {
					kpcTypeDictCodes = "'" + kpcTypeDictBuffer.substring(1).replaceAll(",", "','") + "'";
				}
			}
		}
		
		map.addAttribute("kpcTypeDictCodes", kpcTypeDictCodes);
		
		return "/zzgl/reportFocus/epidemicPreControl/add_epc.ftl";
	}
	
	/**
	 * 跳转疫情防控详情页面
	 * @param session
	 * @param reportUUID	上报UUID
	 * @param reportType	上报类型
	 * @param params		额外参数
	 * 			instanceId	流程实例id
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/toDetail")
	public String toDetail(HttpSession session, 
			@RequestParam(value = "reportUUID") String reportUUID,
			@RequestParam(value = "listType") Integer listType,
			@RequestParam(value = "reportType", required=false, defaultValue="4") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toDetail(session, reportUUID, listType, reportType, params, map);
		
		map.addAttribute("formType", FocusReportNode352Enum.FORM_TYPE.toString());
		if(CommonFunctions.isNotBlank(map,"isEditable") && Boolean.valueOf(String.valueOf(map.get("isEditable")))) {
			String collectSource = EPCCollectSourceEnum.GRID_INSPECT.getCollectSource(), kpcTypeDictCodes = null;
			String[] initKpcType = null;
			Map<String, Object> reportFocusMap = null;
			
			if(CommonFunctions.isNotBlank(map, "reportFocus")) {
				reportFocusMap = (Map<String, Object>) map.get("reportFocus");
			}
			
			if(CommonFunctions.isNotBlank(reportFocusMap, "collectSource")) {
				collectSource = reportFocusMap.get("collectSource").toString();
			}
			if(CommonFunctions.isNotBlank(KPC_TYPE, collectSource)) {
				initKpcType = KPC_TYPE.get(collectSource);
			}
			if(initKpcType != null) {
				UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
				Map<String, Object> dictParams = new HashMap<String, Object>();
				List<BaseDataDict> kpcTypeDictList = null;

				dictParams.put("orgCode", userInfo.getOrgCode());
				dictParams.put("dictPcode", EPCDictEnum.KPC_TYPE.getDictCode());
				dictParams.put("dictGeneralCodeList", initKpcType);

				kpcTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(dictParams);

				if(kpcTypeDictList != null) {
					StringBuffer kpcTypeDictBuffer = new StringBuffer("");

					for(BaseDataDict kpcTypeDict : kpcTypeDictList) {
						kpcTypeDictBuffer.append(",").append(kpcTypeDict.getDictCode());
					}

					if(kpcTypeDictBuffer.length() > 0) {
						kpcTypeDictCodes = "'" + kpcTypeDictBuffer.substring(1).replaceAll(",", "','") + "'";
					}
				}
			}
			map.addAttribute("kpcTypeDictCodes", kpcTypeDictCodes);
			return "/zzgl/reportFocus/epidemicPreControl/detail_epc_editable.ftl";
		}
		
		return "/zzgl/reportFocus/epidemicPreControl/detail_epc.ftl";
	}

	@Override
	protected boolean isFrom12345(Object dataSource) {
		return EPCDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource);
	}

	@Override
	protected String getBizType() {
		return IReportFeedbackService.BizType.EPIDEMIC_PREVENTION_CONTROL.getCode();
	}
}
