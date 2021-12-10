package cn.ffcs.zhsq.eventExpand.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.eventExpand.service.IEventLabelRelaService;
import cn.ffcs.zhsq.mybatis.persistence.eventExpand.EventLabelRelaMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 事件分类标签关联表接口
 * @author youwj
 * 2019/11/12
 */
@Service(value = "eventLabelRelaService")
public class EventLabelRelaServiceImpl implements IEventLabelRelaService {
	
	@Autowired
	private EventLabelRelaMapper eventLabelRelaMapper;

	@Override
	public Integer saveOrUpdate(Map<String, Object> eventLabelRela, UserInfo userInfo) {
		
		Integer result=-1;
		
		//判断这条记录是否存在
		Map<String,Object> searchParams=new HashMap<String,Object>();
		searchParams.put("eventId", eventLabelRela.get("eventId"));
		searchParams.put("labelName", eventLabelRela.get("labelName"));
		
		List<Map<String, Object>> eventLabelRelaList = eventLabelRelaMapper.searchByEventId(searchParams);
		if(eventLabelRelaList!=null&&eventLabelRelaList.size()>0) {//记录存在，执行更新操作
			eventLabelRela.put("updater", userInfo.getUserId());
			result=eventLabelRelaMapper.update(eventLabelRela);
		}else {//如果记录不存在，则需要进行新增操作
			
			//判断必填参数
			if(CommonFunctions.isNotBlank(eventLabelRela, "eventId")
					&&CommonFunctions.isNotBlank(eventLabelRela, "labelName")
					&&CommonFunctions.isNotBlank(eventLabelRela, "bizId")) {
				
				eventLabelRela.put("creator", userInfo.getUserId());
				eventLabelRela.put("updater", userInfo.getUserId());
				
				result=eventLabelRelaMapper.insert(eventLabelRela);
				
			}
			
		}
		
		return result;
	}

	@Override
	public Integer deleteEventLabelRela(Map<String, Object> eventLabelRela, UserInfo userInfo) {
		
		Integer result=-1;
		
		//验证eventId和labelName是否存在
		if(CommonFunctions.isNotBlank(eventLabelRela, "eventId")&&CommonFunctions.isNotBlank(eventLabelRela, "labelName")) {
			//构造修改人Id
			if(CommonFunctions.isBlank(eventLabelRela, "updater")) {
				eventLabelRela.put("updater", userInfo.getUserId());
			}
			
			if(CommonFunctions.isNotBlank(eventLabelRela, "updater")) {
				result=eventLabelRelaMapper.deleteByEventIdAndLabelInfo(eventLabelRela);
			}
		}
		
		return result;
	}

	@Override
	public List<Map<String, Object>> searchByEventId(Long eventId,Map<String, Object> eventLabelRela) {
		
		List<Map<String, Object>> result =new ArrayList<Map<String, Object>>();
		
		if(eventId!=null&&eventId>0) {
			eventLabelRela.put("eventId", eventId);
		}
		
		if(CommonFunctions.isNotBlank(eventLabelRela, "eventId")) {
			result=eventLabelRelaMapper.searchByEventId(eventLabelRela);
		}
		
		return result;
	}

	@Override
	public Map<String, Object> searchByBizInfo(Map<String, Object> eventLabelRela) {
		Map<String, Object> result=new HashMap<String,Object>();
		if(CommonFunctions.isNotBlank(eventLabelRela, "labelName")) {
			result=eventLabelRelaMapper.searchByBizInfo(eventLabelRela);
		}
		return result;
	}


	

}
