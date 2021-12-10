package cn.ffcs.zhsq.decisionMaking;

import cn.ffcs.shequ.zzgl.service.holidayinfo.IHolidayInfoService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @Description:南昌指挥调度事件状态决策类
 * @Author: zhangtc
 * @Date: 2019/7/12 15:41
 */
@Service(value = "eventStatusDecisionMaking4NCZHDDService")
public class EventStatusDecisionMaking4NCZHDDServiceImpl extends EventStatusDecisionMakingServiceImpl {

    @Autowired
    private IEventDisposalWorkflowService eventDisposalWorkflowService;

    @Autowired
    private IEventDisposalService eventDisposalService;
    
    @Autowired
	private IHolidayInfoService holidayInfoService;

    protected static final String CLOSE_NODE_CODE = "task8";		//结案环节节点编码
    private static final String EVALUATE_NODE_CODE = "task9";		//评价环节
    private static final String CONFIRM_NODE_CODE = "task13";		//复核环节
    
    //需要设置事件办结时限的环节
    private static final String DISTRICT_NODE_CODE = "task5";	//县区处理环节
    private static final String TOWNDISPOSAl_NODE_CODE = "task4";	//乡镇处理环节
    private static final String DISTRICT_DEPARTMENT_NODE_CODE = "task7";		//区职能部门处理环节

    @Override
    protected Map<String, Object> initStatusMap(String chiefLevel) {
        Map<String, Object> statusMap = super.initStatusMap(chiefLevel);
        //事件结案-待核实
        statusMap.put(CLOSE_NODE_CODE + "-" + CONFIRM_NODE_CODE, ConstantValue.EVENT_STATUS_ARCHIVE);

        return statusMap;
    }
    @Override
    protected String updateEventStatus(Long eventId, Long userId, Long orgId, String chiefLevel, String curNodeCode, String nextNodeCode, Date handleDate, Map<String, Object> extraParam) throws Exception {
        Long instanceId = null;
        String eventStatus = super.updateEventStatus(eventId, userId, orgId, chiefLevel, curNodeCode, nextNodeCode, handleDate, extraParam);

        if(CommonFunctions.isNotBlank(extraParam, "instanceId")) {
            instanceId = Long.valueOf(extraParam.get("instanceId").toString());
        } else {
            instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
        }

        if(ConstantValue.EVENT_STATUS_ARCHIVE.equals(eventStatus) && null != instanceId && instanceId > 0){
            //获取事件当前环节，如果当前环节是复核环节，将事件子状态设置为待核实；如果是评价环节,将子状态设置为待评价
            Map<String,Object> curDataMap = eventDisposalWorkflowService.curNodeData(instanceId);
            String curTaskNode = null;
            if(null != curDataMap && CommonFunctions.isNotBlank(curDataMap,"NODE_NAME")){
                curTaskNode = String.valueOf(curDataMap.get("NODE_NAME"));

                if(null != curTaskNode){
                    EventDisposal eventTmp = new EventDisposal();

                    eventStatus = ConstantValue.EVENT_STATUS_ARCHIVE;
                    eventTmp.setEventId(eventId);
                    eventTmp.setStatus(eventStatus);

                    //获取事件当前环节，如果当前环节是复核环节，将事件子状态设置为待核实；
                    if(CONFIRM_NODE_CODE.equals(curTaskNode)){
                        eventTmp.setSubStatus(ConstantValue.CONFIRMING_STATUS);
                    }else if(EVALUATE_NODE_CODE.equals(curTaskNode)){
                        //如果是评价环节,将子状态设置为待评价
                        eventTmp.setSubStatus("08");
                    }
                    eventDisposalService.updateEventDisposalById(eventTmp);
                }
            }
        }
        
        //更新事件的处理时限
        if(CommonFunctions.isNotBlank(extraParam, "eventHandleInterval")) {
        	
			Integer intervalDays = 0;
			Date eventHandleDate = null;
			String interval = extraParam.get("eventHandleInterval").toString();

			if (StringUtils.isNotBlank(interval)) {
				try {
					intervalDays = Integer.valueOf(interval);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}

			if (intervalDays > 0 && intervalDays <= 7) {

				// 县区分派事件给乡镇街道或县区职能部门时，可设置处理时间
				if (DISTRICT_NODE_CODE.equals(curNodeCode) && (TOWNDISPOSAl_NODE_CODE.equals(nextNodeCode)
						|| DISTRICT_DEPARTMENT_NODE_CODE.equals(nextNodeCode))) {

					EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);

					eventHandleDate = holidayInfoService.getWorkDateByAfterWorkDay(event.getCreateTime(), intervalDays);
					if (null != eventHandleDate) {
						
						EventDisposal eventTemp=new EventDisposal();
						eventTemp.setEventId(eventId);
						eventTemp.setHandleDate(eventHandleDate);

						eventDisposalService.updateEventDisposalById(eventTemp);
					}

				}

			}
        	
        	
        }
		
        return eventStatus;
    }
}
