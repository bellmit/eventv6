package cn.ffcs.zhsq.szzg.eventPush.service.impl;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.eventPush.service.IEventPushService;
import cn.ffcs.zhsq.mybatis.domain.eventPush.EventPush;
import cn.ffcs.zhsq.mybatis.persistence.eventPush.EventPushMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;


/**
 * @Description: 事件推送模块服务实现
 * @Author: os.wuzhj
 * @Date: 06-10 09:13:18
 * @Copyright: 2019 福富软件
 */
@Service("eventPushServiceImpl")
@Transactional
public class EventPushServiceImpl implements IEventPushService {

	@Autowired
	private EventPushMapper eventPushMapper; //注入事件推送模块dao
	
	@Autowired
    private IBaseDictionaryService baseDictionaryService;//字典服务
	
	/**
	 * 新增数据
	 * @param bo 事件推送业务对象
	 * @return 事件推送id
	 */
	@Override
	public Integer insert(Map<String,Object> map) {
		return eventPushMapper.insert(map);
	}

	/**
	 * 修改数据
	 * @param bo 事件推送业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(EventPush eventPush) {
		long result = eventPushMapper.update(eventPush);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 事件推送业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(EventPush eventPush) {
		long result = eventPushMapper.delete(eventPush);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param infoOrgCode 组织的infoOrgCode
	 * @param eventName 事件标题
	 * @param code 事件编号
	 * @param pushName 推送人
	 * @param createTimeStart 采集事件开始
	 * @param createTimeEnd 采集事件结束
	 * @return 事件推送分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<EventPush> list = null;
		int count = eventPushMapper.countList(params);
		
		if(count > 0 ) {
			list = changeDictForEventList(eventPushMapper.searchList(params, rowBounds),baseDictionaryService,params);
		}else {
			list = new ArrayList<>();
		}
		
		return new EUDGPagination(count,list);
	}
	
	//事件类型和事件状态 字典转换
	public static List<EventPush> changeDictForEventList(List<EventPush> eventPushList,
			IBaseDictionaryService baseDictionaryService,Map<String, Object> params){
		Map<String, Object> dictMap = new HashMap<String, Object>();
		String userOrgCode = null;
		String eventType = null;
						  
		//ConstantValue.STATUS_PCODE    BIG_TYPE_PCODE  SMALL_TYPE_PCODE
		
		if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
			userOrgCode = params.get("userOrgCode").toString();
		} else if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userOrgCode = params.get("orgCode").toString();
		}
		
		dictMap.put("orgCode", userOrgCode);
		dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
		
		Map<String, String> statusMap = listToMap(baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.STATUS_PCODE, userOrgCode));
		
		for(int i=0,len=eventPushList.size();i<len;i++) {
			eventPushList.get(i).setStatusCN(statusMap.get(eventPushList.get(i).getStatus()));
		}
		
		List<BaseDataDict> eventTypeDict = baseDictionaryService.findDataDictListByCodes(dictMap);
		
		for(EventPush eventMap : eventPushList) {
			userOrgCode = null; eventType = null; 
			
			// 设置事件分类的值 大类+小类
			if(StringUtils.isNotBlank(eventMap.getType())) {
				eventType = eventMap.getType();
			}
			
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
					eventMap.setTypeCN(eventClass.substring(1));
				}
			}
		}
		
		return eventPushList;
	}
	
	//字典list转Map
	public static Map<String,String> listToMap(List<BaseDataDict> list){
		
		Map<String,String> map = new HashMap<>();
		
		for(int i = 0,l=list.size();i<l;i++) {
			map.put(list.get(i).getDictGeneralCode(), list.get(i).getDictName());
		}
		return map;
	}

	@Override
	public List<EventPush> searchByEventIds(Map<String, Object> params) {
		return eventPushMapper.searchByEventIds(params);
	}
}