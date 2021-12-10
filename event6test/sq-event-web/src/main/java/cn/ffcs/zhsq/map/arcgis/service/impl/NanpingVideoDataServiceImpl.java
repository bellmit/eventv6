package cn.ffcs.zhsq.map.arcgis.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.map.arcgis.service.INanpingVideoDataService;
import cn.ffcs.zhsq.mybatis.persistence.map.arcgis.NanpingVideoDataMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.DateUtils;

@Service(value="nanpingVideoDataService")
public class NanpingVideoDataServiceImpl implements INanpingVideoDataService{
	
	@Autowired
	private NanpingVideoDataMapper  nanpingVideoDataMapper;
	

	@Override
	public List<Map<String, Object>> findData(Map<String, Object> params) {
		
		return nanpingVideoDataMapper.findGridByParams(params);
	}


	@Override
	public Map<String, Object> findOtherData(Map<String, Object> params) {
		//全球眼的数量
		int globalNum = nanpingVideoDataMapper.findGlobalByParams(params);
		
		//派出所
		int policeNum = nanpingVideoDataMapper.findPoliceByParams(params);
		
		//消防
		int fireNum = nanpingVideoDataMapper.findFireByParams(params);
		
		//网格员
		int gridNum = nanpingVideoDataMapper.finGridMemberByParams(params);
		
		//医院
		int hospitalNum = nanpingVideoDataMapper.findHospitalByParams(params);
		
		//事件总量
		int sumEvent = nanpingVideoDataMapper.findSumEventByParams(params);
		
		
		//事件办结
		int endEvent = nanpingVideoDataMapper.findEndByParams(params);
		
		//事件超时
		int timeoutEvent = nanpingVideoDataMapper.findTimeoutByParams(params);
		Map<String, Object> map = new HashMap<String, Object>();
		//事件待办
		int backlogEvent = 0;
				
		if(CommonFunctions.isNotBlank(params, "orgCode")) {//没有传时默认登录用户的id
			List<Map<String, Object>> list=	nanpingVideoDataMapper.findSocialListByOrgCode(params);
			if(list.size()>0) {
				params.put("orgId", list.get(0).get("orgId"));
				backlogEvent = nanpingVideoDataMapper.findBacklogByParams(params);
			}
		} 
		
		map.put("globalNum", globalNum);
		map.put("policeNum", policeNum);
		map.put("fireNum", fireNum);
		map.put("gridNum", gridNum);
		map.put("hospitalNum", hospitalNum);
		
		map.put("sumEvent", sumEvent);
		map.put("backlogEvent", backlogEvent);
		map.put("endEvent", endEvent);
		map.put("timeoutEvent", timeoutEvent);
		
		return map;
	}


	@Override
	public List<Map<String, Object>> gridListData(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return nanpingVideoDataMapper.getGridListData(params);
	}


	@Override
	public List<Map<String, Object>> globalsEyeList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return nanpingVideoDataMapper.getGlobalsEyeList(params);
	}


	@Override
	public List<Map<String, Object>> findListByUrgencyDegree(Map<String, Object> params) {
		if(CommonFunctions.isNotBlank(params, "orgCode")) {//没有传时默认登录用户的id
			List<Map<String, Object>> list=	nanpingVideoDataMapper.findSocialListByOrgCode(params);
			if(list.size()>0) {
				params.put("orgId", list.get(0).get("orgId"));
			}else {
				return  list;
			}
		} 
		return nanpingVideoDataMapper.findListByUrgencyDegree(params);
	}
	
	public EUDGPagination findPagListByUrgencyDegree(int page, int rows,
			Map<String, Object> params) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		if(CommonFunctions.isNotBlank(params, "orgCode")) {//没有传时默认登录用户的id
			List<Map<String, Object>> list=	nanpingVideoDataMapper.findSocialListByOrgCode(params);
			if(list.size()>0) {
				params.put("orgId", list.get(0).get("orgId"));
			}else {
				return  new EUDGPagination(0, list);
			}
		}
		long count = nanpingVideoDataMapper.findBacklogByParams(params);
		List<Map<String, Object>> list = nanpingVideoDataMapper.findPagListByUrgencyDegree(params, rowBounds);
		for (Map<String, Object> eventMap : list) {
			if(CommonFunctions.isNotBlank(eventMap, "happenTime")) {
				eventMap.put("happenTimeStr", DateUtils.formatDate((Date)eventMap.get("happenTime"), DateUtils.PATTERN_24TIME));
			}
			if(CommonFunctions.isNotBlank(eventMap, "createTime")) {
				eventMap.put("createTimeStr", DateUtils.formatDate((Date)eventMap.get("createTime"), DateUtils.PATTERN_24TIME));
			}
		}
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

}
