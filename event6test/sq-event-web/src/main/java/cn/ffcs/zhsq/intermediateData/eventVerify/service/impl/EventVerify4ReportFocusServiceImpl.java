package cn.ffcs.zhsq.intermediateData.eventVerify.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.DataDictHelper;

/**
 * @Description: 南安市入格事件审核实现跳转中心
 * @ClassName: EventVerify4ReportFocusServiceImpl
 * @author: wuxq
 * @date: 2021年5月19日 
 */
@Service("eventVerify4ReportFocusService")
public class EventVerify4ReportFocusServiceImpl extends EventVerifyServiceImpl {

	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private IReportIntegrationService reportIntegrationService;

	private static final String eventTypeCode = "B508";//事件分类字典
	private static final String eventCode = "99";//事件分类-其他事件（字典编码=99）

//	审核列表-事件分类字典翻译
//	@Override
//	public EUDGPagination findEventVerifyPagination(int pageNo, int pageSize, Map<String, Object> params) {
//
//		EUDGPagination findEventVerifyPagination = super.findEventVerifyPagination(pageNo, pageSize, params);
//
//		if (null != findEventVerifyPagination && findEventVerifyPagination.getTotal() > 0) {
//
//			if (CommonFunctions.isNotBlank(params, "userOrgCode")) {
//
//				List<BaseDataDict> eventTypeDict = baseDictionaryService.getDataDictListOfSinglestage(code,
//						params.get("userOrgCode").toString());
//
//				if (null != eventTypeDict && eventTypeDict.size() > 0) {
//
//					List<Map<String, Object>> findEventVerifyList = (List<Map<String, Object>>) findEventVerifyPagination
//							.getRows();
//
//					for (Map<String, Object> eventVerify : findEventVerifyList) {
//						DataDictHelper.setDictValueForField(eventVerify, "eventType", "eventTypeName", eventTypeDict);
//
//					}
//				}
//
//			}
//
//		}
//
//		return findEventVerifyPagination;
//	}

	@Override
	public Map<String, Object> findEventVerifyById(Long eventVerifyId, Map<String, Object> eventVerify) {

		Map<String, Object> eventVerifyMap = super.findEventVerifyById(eventVerifyId, eventVerify);

		//事件分类名称
		if (CommonFunctions.isNotBlank(eventVerify, "userOrgCode")) {
			String userOrgCode = eventVerify.get("userOrgCode").toString();
			List<BaseDataDict> eventTypeDict = baseDictionaryService.getDataDictListOfSinglestage(eventTypeCode, userOrgCode);
			if (null != eventTypeDict && eventTypeDict.size() > 0) {
				DataDictHelper.setDictValueForField(eventVerifyMap, "eventType", "eventTypeName", eventTypeDict);
			}
		}
		
		//入格事件根据reportId查找reportUUId
		if (CommonFunctions.isNotBlank(eventVerifyMap, "eventId") &&
				CommonFunctions.isNotBlank(eventVerifyMap, "eventType")) {
			
			String eventType = eventVerifyMap.get("eventType").toString();
			//非其他事件则为入格事件
			if (!eventCode.equals(eventType)) {
				
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("reportId", eventVerifyMap.get("eventId").toString());
				Map<String, Object> reportFocus = reportIntegrationService.findReportFocusByUUIDSimple("", null, param);
				if (CommonFunctions.isNotBlank(reportFocus,"reportUUID")) {
					eventVerifyMap.put("reportUUID", reportFocus.get("reportUUID").toString());
					eventVerifyMap.put("reportType", reportFocus.get("reportType").toString());
				}
			}
			
		}

		return eventVerifyMap;
	}
}
