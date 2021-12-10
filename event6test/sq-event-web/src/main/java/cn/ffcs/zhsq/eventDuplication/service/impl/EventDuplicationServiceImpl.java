package cn.ffcs.zhsq.eventDuplication.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.eventDuplication.service.IEventDuplicationService;
import cn.ffcs.zhsq.mybatis.persistence.eventDuplication.EventDuplicationMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * 重复事件处理接口，对应表T_EVENT_DUPLICATION
 * @author zhangls
 *
 */
@Service(value = "eventDuplicationService")
public class EventDuplicationServiceImpl implements IEventDuplicationService {
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private EventDuplicationMapper eventDuplicationMapper;
	
	@Override
	public int saveEventDuplication(Map<String, Object> eventDuplication, UserInfo userInfo) {
		List<Map<String, Object>> eventDuplicationMapList = new ArrayList<Map<String, Object>>();
		int result = 0;
		
		formatParamIn(eventDuplication, userInfo);
		
		if(CommonFunctions.isNotBlank(eventDuplication, "duplicateEventId")) {
			Object duplicateEventIdObj = eventDuplication.get("duplicateEventId");
			Long duplicateEventId = null;
			
			if(duplicateEventIdObj instanceof String) {
				String[] duplicateEventIdArray = duplicateEventIdObj.toString().split(",");
				Map<String, Object> dupEventMap = null;
				
				for(String duplicateEventIdStr : duplicateEventIdArray) {
					duplicateEventId = null;
					
					if(StringUtils.isNotBlank(duplicateEventIdStr)) {
						try {
							duplicateEventId = Long.valueOf(duplicateEventIdStr);
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
					}
					
					if(duplicateEventId != null && duplicateEventId > 0) {
						dupEventMap = new HashMap<String, Object>();
						dupEventMap.putAll(eventDuplication);
						dupEventMap.put("duplicateEventId", duplicateEventId);
						
						eventDuplicationMapList.add(dupEventMap);
					}
				}
			} else if(duplicateEventIdObj instanceof Long) {
				duplicateEventId = (Long) duplicateEventIdObj;
				
				if(duplicateEventId > 0) {
					eventDuplication.put("duplicateEventId", duplicateEventId);
				} else {
					throw new IllegalArgumentException("属性【duplicateEventId】：" + duplicateEventId + "，不是有效的数值！");
				}
				
				eventDuplicationMapList.add(eventDuplication);
			}
		}
		
		if(eventDuplicationMapList.size() > 0) {
			result = eventDuplicationMapper.insertOrUpdate(eventDuplicationMapList);
		}
		
		return result;
	}

	@Override
	public int findDuplicatedEventCount(Map<String, Object> params, UserInfo userInfo) {
		int count = 0;
		
		if(CommonFunctions.isBlank(params, "leadEventId")) {
			throw new IllegalArgumentException("缺少属性[leadEventId]！");
		}
		
		count = eventDuplicationMapper.findDuplicatedEventCount(params);
		
		return count;
	}
	
	@Override
	public List<Map<String, Object>> findDuplicatedEventList(Map<String, Object> params, UserInfo userInfo) {
		List<Map<String, Object>> duplicatedEventList = null;
		
		if(CommonFunctions.isBlank(params, "leadEventId")) {
			throw new IllegalArgumentException("缺少属性[leadEventId]！");
		}
		
		duplicatedEventList = eventDuplicationMapper.findDuplicatedEventList(params);
		
		if(duplicatedEventList != null && duplicatedEventList.size() > 0) {
			String eventType = null;
			List<BaseDataDict> statusDictList = null,
					                       eventTypeDict = null;
			boolean isDictTransfer4Event = true;
			
			if(CommonFunctions.isNotBlank(params, "isDictTransfer4Event")) {
				isDictTransfer4Event = Boolean.valueOf(params.get("isDictTransfer4Event").toString());
			}
			
			if(isDictTransfer4Event) {
				Map<String, Object> dictMap = new HashMap<String, Object>();
				String userOrgCode = null;
				
				if(userInfo != null) {
					userOrgCode = userInfo.getOrgCode();
				} else if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
					userOrgCode = params.get("userOrgCode").toString();
				}
				
				dictMap.put("orgCode", userOrgCode);
				dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
				
				statusDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.STATUS_PCODE, userOrgCode);
				eventTypeDict = baseDictionaryService.findDataDictListByCodes(dictMap);
			}
			
			for(Map<String, Object> duplicatedEvent : duplicatedEventList) {
				eventType = null;
				
				if(CommonFunctions.isNotBlank(duplicatedEvent, "type")) {
					eventType = duplicatedEvent.get("type").toString();
				}
				
				DateUtils.convert2String(duplicatedEvent, "happenTimeStr", "happenTime", DateUtils.PATTERN_24TIME);
				DataDictHelper.setDictValueForField(duplicatedEvent, "status", "eventStatusName", statusDictList);
				
				if(StringUtils.isNotBlank(eventType) && eventTypeDict != null) {
					StringBuffer eventClass = new StringBuffer("");
					String bigType = eventType, bigTypeName = "", bigDictCode = null;
					
					do {//从最小类递归到最大类，中间使用"-"分隔
						bigTypeName = "";
						
						for(BaseDataDict dataDict : eventTypeDict) {
							if((StringUtils.isNotBlank(bigDictCode) 
									&& !ConstantValue.BIG_TYPE_PCODE.equals(bigDictCode) 
									&& bigDictCode.equals(dataDict.getDictCode()))
								||
								(StringUtils.isNotBlank(bigType) && bigType.equals(dataDict.getDictGeneralCode()))) {
								bigTypeName = dataDict.getDictName();
								bigDictCode = dataDict.getDictPcode();
								bigType = null;
								break;
							}
						}
						
						if(StringUtils.isNotBlank(bigTypeName)) {
							eventClass.insert(0, bigTypeName).insert(0, "-");
						}
					} while(StringUtils.isNotBlank(bigTypeName));
					
					if(eventClass.length() > 0) {
						duplicatedEvent.put("typeName", eventClass.substring(eventClass.lastIndexOf("-") + 1));
						duplicatedEvent.put("eventClass", eventClass.substring(1));
					}
				}
			}
		}
		
		return duplicatedEventList;
	}
	
	@Override
	public int delEventDuplication(Map<String, Object> eventDuplication, UserInfo userInfo) {
		int result = 0;
		
		formatParamIn(eventDuplication, userInfo);
		
		result = eventDuplicationMapper.delete(eventDuplication);
		
		return result;
	}
	
	/**
	 * 格式化输入参数
	 * @param eventDuplication
	 * @param userInfo
	 */
	private void formatParamIn(Map<String, Object> eventDuplication, UserInfo userInfo) {
		if(eventDuplication == null || eventDuplication.isEmpty()) {
			throw new IllegalArgumentException("缺少有效的重复事件处理记录！");
		}
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少有效的操作用户信息！");
		} else {
			Long operateUserId = userInfo.getUserId();
			
			if(operateUserId == null || operateUserId < 0) {
				throw new IllegalArgumentException("缺少有效的操作用户id！");
			}
			
			eventDuplication.put("creator", operateUserId);
			eventDuplication.put("updater", operateUserId);
		}
		
		if(CommonFunctions.isNotBlank(eventDuplication, "leadEventId")) {
			Long leadEventId = null;
			
			try {
				leadEventId = Long.valueOf(eventDuplication.get("leadEventId").toString());
			} catch(NumberFormatException e) {
				throw new IllegalArgumentException("属性【leadEventId】：" + eventDuplication.get("leadEventId") + "，不是有效的数值！");
			}
			
			if(leadEventId == null || leadEventId < 0) {
				throw new IllegalArgumentException("缺少有效的主事件id【leadEventId】！");
			}
			
			eventDuplication.put("leadEventId", leadEventId);
		}
		
		if(CommonFunctions.isNotBlank(eventDuplication, "duplicationId")) {
			Long duplicationId = null;
			
			try {
				duplicationId = Long.valueOf(eventDuplication.get("duplicationId").toString());
			} catch(NumberFormatException e) {
				throw new IllegalArgumentException("属性【duplicationId】：" + eventDuplication.get("duplicationId") + "，不是有效的数值！");
			}
			
			if(duplicationId != null && duplicationId > 0) {
				eventDuplication.put("duplicationId", duplicationId);
			}
		}
	}

}
