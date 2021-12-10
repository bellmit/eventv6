package cn.ffcs.zhsq.map.jiangyinPlatform.service.impl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.jiangyinPlatform.communicationLog.service.ICommunicationLogService;
import cn.ffcs.zhsq.jiangyinPlatform.recentContact.service.IRecentContactService;
import cn.ffcs.zhsq.map.jiangYinPlatform.service.IJiangYinPlatformService;
import java.util.List;
import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.communicationLog.CommunicationLog;
import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.recentContact.RecentContact;
import cn.ffcs.zhsq.mybatis.persistence.map.jiangYinPlatform.JiangYinPlatformMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 	江阴大屏接口实现类
 *
 * @Author sunjian
 * @Date 2020-03-04 21:31
 */
@Service(value = "jiangYinPlatformServiceImpl")
public class JiangYinPlatformServiceImpl implements IJiangYinPlatformService {
	
	@Autowired
    private JiangYinPlatformMapper jiangYinPlatformMapper;
	@Autowired
	private ICommunicationLogService communicationLogService; //注入通讯记录表模块服务
	@Autowired
	private IRecentContactService recentContactService; //注入最近联系人表模块服务
	
	@Override
	public EUDGPagination findGridInfoPagination(int pageNo, int pageSize, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/******************************吴展杰 start*************************************/
	
	@Override
	public List<Map<String, Object>> findAcquisitionTrendData(Map<String, Object> params) {
		if(CommonFunctions.isNotBlank(params, "withClose")) {
			return jiangYinPlatformMapper.findAcquisitionTrendTotalAndCloseData(params);
		}else {
			return jiangYinPlatformMapper.findAcquisitionTrendData(params);
		}
	}
	
	@Override
	public List<Map<String, Object>> findAreaRankTop5Data(Map<String, Object> params) {
		return jiangYinPlatformMapper.findAreaRankTop5Data(params);
	}
	
	@Override
	public Map<String, Object> findCumulativeData(Map<String, Object> params) {
		return jiangYinPlatformMapper.findCumulativeData(params);
	}
	
	@Override
    public List<Map<String,Object>> findHotEventData(Map<String, Object> params){
    	return jiangYinPlatformMapper.findHotEventData(params);
    }


	@Override
	public List<Map<String, Object>> findUrgencyNum(Map<String, Object> params) {
		return jiangYinPlatformMapper.findUrgencyNum(params);
	}


	@Override
	public List<Map<String, Object>> findOverseeNum(Map<String, Object> params) {
		return jiangYinPlatformMapper.findOverseeNum(params);
	}
	
	@Override
	public List<Map<String, Object>> findEventXY(Map<String, Object> map) {
		return jiangYinPlatformMapper.findEventXY(map);
	}


	
	/******************************吴展杰 end**************************************/
	
	
	/******************************start2*************************************/
	@Override
	public List<Map<String, Object>> getEventNumGroupByType(Map<String, Object> map) {
		return jiangYinPlatformMapper.getEventNumGroupByType(map);
	}
	
	/*******************************end2**************************************/
	
	
	/******************************start3*************************************/
	
	/*******************************end3**************************************/
	
	
	/******************************start4*************************************/
	
	/*******************************end4**************************************/
	
	
	/******************************start5*************************************/
	@Override
	public boolean saveLogAndRecentContact(RecentContact reCon, CommunicationLog comLog) {
		boolean flag = true;
		try {
			if (reCon != null) {
				recentContactService.insert(reCon);
			}
			if (comLog != null) {
				communicationLogService.insert(comLog);
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	/*******************************end5**************************************/

	@Override
	public List<Map<String, Object>> findInfoOrgCodesByOrgIds(Map<String, Object> params) {
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			String orgId = params.get("orgId").toString();
			if(orgId.contains(",")) {
				params.put("orgIds", orgId.split(","));
				params.remove("orgId");
			}
		}
		return jiangYinPlatformMapper.findInfoOrgCodesByOrgIds(params);
	}
}
