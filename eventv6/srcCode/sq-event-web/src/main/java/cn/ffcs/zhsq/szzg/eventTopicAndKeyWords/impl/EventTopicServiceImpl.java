package cn.ffcs.zhsq.szzg.eventTopicAndKeyWords.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.shequ.commons.util.StringUtils;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.eventTopicAndKeyWords.service.IEventTopicService;
import cn.ffcs.zhsq.mybatis.domain.eventTopicAndKeyWords.EventTopic;
import cn.ffcs.zhsq.mybatis.persistence.eventTopicAndKeyWords.EventTopicMapper;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 热点事件主题表模块服务实现
 * @Author: os.wuzhj
 * @Date: 10-15 16:01:04
 * @Copyright: 2019 福富软件
 */
@Service("eventTopicServiceImpl")
@Transactional
public class EventTopicServiceImpl implements IEventTopicService {

	@Autowired
	private EventTopicMapper eventTopicMapper; //注入热点事件主题表模块dao
	
	@Autowired
    private IBaseDictionaryService baseDictionaryService;//字典服务

	/**
	 * 新增数据
	 * @param bo 热点事件主题表业务对象
	 * @return 热点事件主题表id
	 */
	@Override
	public Long insert(EventTopic bo) {
		eventTopicMapper.insert(bo);
		return bo.getId_();
	}

	/**
	 * 修改数据
	 * @param bo 热点事件主题表业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(EventTopic bo) {
		long result = eventTopicMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 热点事件主题表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(EventTopic bo) {
		long result = eventTopicMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 热点事件主题表分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<EventTopic> list = null;
		long count = eventTopicMapper.countList(params);
		if(count > 0) {
			list = eventTopicMapper.searchList(params, rowBounds);
			if(list != null && list.size() > 0) {
				//发布状态
				Map<String,String> statusMap = listToMap(baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.STATUS_DICTCODE, null));
				for(int i = 0,l=list.size();i<l;i++) {
					list.get(i).setIsReleaseStr(statusMap.get(list.get(i).getIsRelease()));
				}
			}
		}else {
			list = new ArrayList<>();
		}
		return new EUDGPagination(count, list);
	}
	
	//字典list转Map
	public static Map<String,String> listToMap(List<BaseDataDict> list){
		
		Map<String,String> map = new HashMap<>();
		
		for(int i = 0,l=list.size();i<l;i++) {
			map.put(list.get(i).getDictGeneralCode(), list.get(i).getDictName());
		}
		
		return map;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 热点事件主题表id
	 * @return 热点事件主题表业务对象
	 */
	@Override
	public EventTopic searchById(Long id) {
		EventTopic bo = eventTopicMapper.searchById(id);
		return bo;
	}

	@Override
	public Integer findReleaseCount(String bizType) {
		if(StringUtils.isBlank(bizType)) {
			return -1;
		}
		return eventTopicMapper.findReleaseCount(bizType);
	}

}