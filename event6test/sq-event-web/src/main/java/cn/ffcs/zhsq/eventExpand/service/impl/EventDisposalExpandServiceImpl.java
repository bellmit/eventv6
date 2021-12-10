package cn.ffcs.zhsq.eventExpand.service.impl;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.eventExpand.service.IEventDisposal4ExpandService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author youwj
 *
 */
@Service(value = "eventDisposalExpandService")
public class EventDisposalExpandServiceImpl extends
ApplicationObjectSupport implements IEventDisposal4ExpandService {
	//功能配置接口
	@Autowired
	private IFunConfigurationService funConfigurationService;

	@Override
	public boolean updateEventDisposal(EventDisposal event, Map<String, Object> params,UserInfo userInfo) {
		
		IEventDisposal4ExpandService eventDisposalExpandService = this.capServiceImpl(params,userInfo.getOrgCode()); 
		
		return eventDisposalExpandService.updateEventDisposal(event, params,userInfo);
	}

	@Override
	public boolean saveOrUpdateEventEvaluate(UserInfo userInfo,Long eventId,String evaLevel,String evaContent,Map<String, Object> params) throws Exception {
		IEventDisposal4ExpandService eventDisposalExpandService = this.capServiceImpl(params,userInfo.getOrgCode());

		return eventDisposalExpandService.saveOrUpdateEventEvaluate(userInfo, eventId,evaLevel,evaContent,params);
	}

	@Override
	public List<Map<String, Object>> findEvaResultList(Long eventId,String evaObj,UserInfo userInfo,Map<String, Object> params) throws Exception {
		IEventDisposal4ExpandService eventDisposalExpandService = this.capServiceImpl(params,userInfo.getOrgCode());

		return eventDisposalExpandService.findEvaResultList(eventId,evaObj,userInfo,params);
	}
	
	@Override
    public Map<String, Object> delEventById(List<Long> eventIdList, UserInfo userInfo, Map<String, Object> params) {
    	IEventDisposal4ExpandService eventDisposalExpandService = this.capServiceImpl(params, userInfo.getOrgCode());
    	
    	return eventDisposalExpandService.delEventById(eventIdList, userInfo, params);
    }

	@Override
	public void expandFormatMapDataOut(Map<String, Object> eventMap,UserInfo userInfo, Map<String, Object> params) {
		IEventDisposal4ExpandService eventDisposalExpandService = null;
		String userOrgCode = null;

		if(null != userInfo && StringUtils.isNotBlank(userInfo.getOrgCode())){
			userOrgCode = userInfo.getOrgCode();
		}else if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
			userOrgCode = params.get("userOrgCode").toString();
		} else if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userOrgCode = params.get("orgCode").toString();
		}

		eventDisposalExpandService = this.capServiceImpl(params,userOrgCode);

		eventDisposalExpandService.expandFormatMapDataOut(eventMap,userInfo, params);
	}
	
	@Override
	public List<Map<String, Object>> capPlanConfigStaff(String planType, String planLevel, UserInfo userInfo, Map<String, Object> params) throws Exception {
		IEventDisposal4ExpandService eventDisposalExpandService = this.capServiceImpl(params, userInfo.getOrgCode());
    	
    	return eventDisposalExpandService.capPlanConfigStaff(planType, planLevel, userInfo, params);
	}
	
	@Override
	public EventDisposal init4Event(EventDisposal event, UserInfo userInfo, Map<String, Object> params) throws Exception {
		IEventDisposal4ExpandService eventDisposalExpandService = this.capServiceImpl(params, userInfo.getOrgCode());
		
		return eventDisposalExpandService.init4Event(event, userInfo, params);
	}
	
	/**
	 * 获取对应的实现类
	 * @param params
	 * 			IEventDisposalExpandService	自定义的实现类，用于当orgCode为空的情况
	 * @param orgCode 组织编码
	 * 
	 * @return
	 */
    private IEventDisposal4ExpandService capServiceImpl(Map<String, Object> params, String orgCode) {
        String serviceImpl = "",
        	   trigCondition = ConstantValue.EVENT_EXPAND_SERVICE;

        if(CommonFunctions.isNotBlank(params, "eventDisposalExpandService")) {
            serviceImpl = params.get("eventDisposalExpandService").toString();
        } else {
            serviceImpl = funConfigurationService.changeCodeToValue(ConstantValue.EVENT_EXPAND_ATTRIBUTE, trigCondition, IFunConfigurationService.CFG_TYPE_JAVA_PROCESSOR, orgCode);
        }

        if(StringUtils.isBlank(serviceImpl)) {
            serviceImpl = "eventDisposalExpandBaseServiceImpl";
        }

        return (IEventDisposal4ExpandService) this.getApplicationContext().getBean(serviceImpl);
    }
}
