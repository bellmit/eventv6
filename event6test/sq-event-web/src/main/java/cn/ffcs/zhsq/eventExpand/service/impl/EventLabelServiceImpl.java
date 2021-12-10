package cn.ffcs.zhsq.eventExpand.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.eventExpand.service.IEventLabelService;
import cn.ffcs.zhsq.mybatis.persistence.eventExpand.EventLabelMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;

/**
 * 事件分类标签关联表接口
 * @author youwj
 * 2019/11/12
 */
@Service(value = "eventLabelService")
public class EventLabelServiceImpl implements IEventLabelService {
	
	@Autowired
	private EventLabelMapper eventLabelMapper;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;

	@Override
	public Integer saveOrUpdate(Map<String, Object> eventLabel, UserInfo userInfo) {
		Integer result=0;
		if(userInfo!=null) {
			eventLabel.put("creator", userInfo.getUserId());
			eventLabel.put("updater", userInfo.getUserId());
		}
		try {
			if(CommonFunctions.isBlank(eventLabel, "labelId")) {//id为空执行新增
				result=eventLabelMapper.insert(eventLabel);
			}else {
				result=eventLabelMapper.update(eventLabel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Integer deleteEventLabel(Map<String, Object> eventLabel, UserInfo userInfo) {
		if(CommonFunctions.isBlank(eventLabel, "updater")&&userInfo!=null) {
			eventLabel.put("updater", userInfo.getUserId());
		}
		if(CommonFunctions.isNotBlank(eventLabel, "labelId")) {
			eventLabel.put("labelId", Long.valueOf(eventLabel.get("labelId").toString()));
		}
		return eventLabelMapper.deleteByLabelId(eventLabel);
	}

	@Override
	public List<Map<String, Object>> searchListByParam(Map<String, Object> eventLabel) {
		this.formatMapDataIn(eventLabel);
		return eventLabelMapper.searchByParams(eventLabel);
	}

	@Override
	public EUDGPagination searchList(Map<String, Object> eventLabel) {
		this.formatMapDataIn(eventLabel);
		Integer pageNo=1;
		Integer pageSize=10;
		try {
			if(CommonFunctions.isNotBlank(eventLabel, "page")) {
				pageNo=Integer.valueOf(eventLabel.get("page").toString());
			}
			if(CommonFunctions.isNotBlank(eventLabel, "rows")) {
				pageSize=Integer.valueOf(eventLabel.get("rows").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		Long count = 0L;
		List<Map<String, Object>> resultList = null;
		
		try {
			count = eventLabelMapper.countList(eventLabel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(count > 0) {
			resultList = eventLabelMapper.searchList(eventLabel, rowBounds);
		} else {
			resultList = new ArrayList<Map<String, Object>>();
		}
		
		formatMapDataOut(resultList, eventLabel);
		
		EUDGPagination eventPagination = new EUDGPagination(count, resultList);
		
		return eventPagination;
	}

	@Override
	public Long countList(Map<String, Object> eventLabel) {
		return eventLabelMapper.countList(eventLabel);
	}
	
	
	
	/**
	 * 格式化输入数据
	 * @param resultList
	 * @param params
	 */
	private void formatMapDataIn(Map<String, Object> params) {
		
		if(CommonFunctions.isNotBlank(params, "eventLabelIds")) {
			Object eventLabelIdObj = params.get("eventLabelIds");
			String[] eventLabelIdArray = null;
			List<Long> eventLabelIdList = new ArrayList<Long>();
			
			if(eventLabelIdObj instanceof String) {
				eventLabelIdArray = ((String) eventLabelIdObj).split(",");
			} if(eventLabelIdObj instanceof String[]) {
				eventLabelIdArray = (String[])eventLabelIdObj;
			} else if(eventLabelIdObj instanceof List) {
				List<Object> eventIdObjList = (List<Object>)eventLabelIdObj;
				List<String> eventIdStrList = new ArrayList<String>();
				
				for(Object eventIdObj : eventIdObjList) {
					eventIdStrList.add(eventIdObj.toString());
				}
				
				eventLabelIdArray = eventIdStrList.toArray(new String[eventIdStrList.size()]);
			}
			
			if(eventLabelIdArray != null) {
				Long eventLabelIdL = -1L;
				
				for(String eventLabelId : eventLabelIdArray) {
					if(StringUtils.isNotBlank(eventLabelId)) {
						try {
							eventLabelIdL = Long.valueOf(eventLabelId);
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
						
						if(eventLabelIdL != null && eventLabelIdL > 0) {
							eventLabelIdList.add(eventLabelIdL);
						}
					}
				}
			}
			
			params.put("eventLabelIdList", eventLabelIdList);
		}
	}
	/**
	 * 格式化输出数据
	 * @param resultList
	 * @param params
	 */
	private void formatMapDataOut(List<Map<String, Object>> resultList, Map<String, Object> params) {
		if(resultList != null && resultList.size() > 0) {
			//翻译标签所属模块
			String userOrgCode=null;
			if(CommonFunctions.isNotBlank(params, "orgCode")) {
				userOrgCode=params.get("orgCode").toString();
			}
			
			List<BaseDataDict> labelBizTypeDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.LABEL_BIZ_TYPE, userOrgCode);
			for (Map<String,Object> result : resultList) {
				DataDictHelper.setDictValueForField(result, "labelModel", "labelModelName", labelBizTypeDict);
			}
		}
	}

	@Override
	public Map<String, Object> searchById(Long labelId) {
		Map<String, Object> searchById = eventLabelMapper.searchById(labelId);
		//构造格式化输出
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		list.add(searchById);
		this.formatMapDataOut(list, new HashMap<String,Object>());
		return searchById;
	}

	

}
