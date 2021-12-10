package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.workcircle.service.IWorkCircleService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description:松溪县城管局事件流程-工作流接口
 * @Author: zhangtc
 * @Date: 2018/12/26 10:47
 */
@Service(value = "eventDisposalWorkflow4SXXService")
public class EventDisposalWorkflow4SXXServiceImpl extends EventDisposalWorkflowForEventNewServiceImpl{

	@Autowired
	private IWorkCircleService workCircleService;
	
    @Autowired
    private IBaseWorkflowService baseWorkflowService;

    @Autowired
    private OrgSocialInfoOutService orgSocialInfoService;

    private static final String END_NODE_CODE = "end1";			//归档环节

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
        String instanceId = super.startFlowByWorkFlow(eventId,workFlowId,toClose,userInfo,extraParam);

        if (StringUtils.isNotBlank(instanceId)) {
	        //采集并结案，不走决策类，直接强制结束
	        if (ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose)) {
	
	            //强制归档
	            try {
	                Boolean result = baseWorkflowService.endWorkflow4Base(Long.valueOf(instanceId),userInfo,extraParam);
	
	                //归档成功后仿禁毒的更新事件状态
	                if (result) {
	                    Map<String,Object> resultMap = new HashMap<>();
	                    OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
	
	                    LinkedHashMap<String, Date> duedate = new LinkedHashMap<String, Date>();
	                    duedate.put(END_NODE_CODE + "_" + END_NODE_CODE, new Date());
	
	                    resultMap.put("duedate", duedate);
	
	                    this.updateEventStatus(eventId, userInfo.getUserId(), orgInfo, resultMap);
	                }
	
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        
	        this.addWorkCircle(eventId, null, userInfo, extraParam);
        }
        
        return instanceId;
    }

    @Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{
		boolean result = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		
		if(result) {
			this.addWorkCircle(null, instanceId, userInfo, extraParam);
		}
		
		return result;
    }
    
    @Override
	public boolean rejectWorkFlow(String taskId, String instanceId,
			String advice, UserInfo userInfo) throws Exception {
    	boolean result = super.rejectWorkFlow(taskId, instanceId, advice, userInfo);
    	
    	if(result) {
    		this.addWorkCircle(null, Long.valueOf(instanceId), userInfo, null);
		}
    	
    	return result;
    }
    
    @Override
	public boolean recallWorkflow(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		boolean flag = super.recallWorkflow(instanceId, userInfo, params);
		
		if(flag) {
			this.addWorkCircle(null, instanceId, userInfo, params);
		}
		
		return flag;
    }
    
    @Override
    public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {

        int result = -1;
        StringBuffer msgWrong = new StringBuffer("");

        if (null != orgSocialInfo) {
            String orgLevel = orgSocialInfo.getChiefLevel(),
                   orgType = orgSocialInfo.getOrgType();

            if (StringUtils.isNotBlank(orgLevel)) {
                if (ConstantValue.DISTRICT_ORG_LEVEL.compareTo(orgLevel) > 0 || ConstantValue.STREET_ORG_LEVEL.compareTo(orgLevel) < 0) {
                    msgWrong.append("组织层级不可超过区县级，不能低于街道级，请先检验！");
                }
            } else {
                msgWrong.append("当前组织层级不存在，请先检验！");
            }

            if (StringUtils.isNotBlank(orgType)) {
                if (String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgType)) {

                } else if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(orgType)){
                    msgWrong.append("组织类型不可为单位，请先检验！");
                } else {
                    msgWrong.append("当前用户的组织类型有误：既不是单位，也不是部门。请先修正！");
                }
            } else {
                msgWrong.append("当前组织类型不存在，请先检验！");
            }
        } else {
            msgWrong.append("当前组织不存在，请先检验！");
        }


        if(msgWrong.length() > 0) {
            throw new Exception(msgWrong.toString());
        } else {
            result = 1;
        }

        return result;
    }
    
    /**
     * 增添工作圈信息
     * @param eventId		事件id
     * @param instanceId	流程实例id
     * @param userInfo		用户信息
     * @param params		额外参数
     * 					eventId		事件id
     * 					formId		表单id
     */
    private void addWorkCircle(Long eventId, Long instanceId, UserInfo userInfo, Map<String, Object> params) {
    	if(eventId == null) {
    		if(CommonFunctions.isNotBlank(params, "eventId")) {
    			eventId = Long.valueOf(params.get("eventId").toString());
    		} else if(CommonFunctions.isNotBlank(params, "formId")) {
    			eventId = Long.valueOf(params.get("formId").toString());
    		} else if(instanceId != null && instanceId > 0) {
    			ProInstance proInstance = this.capProInstanceByInstanceId(instanceId);
    			if(proInstance != null) {
    				eventId = proInstance.getFormId();
    			}
    		}
    	}
    	
    	if(eventId != null && eventId > 0) {
    		String WORK_CIRCLE_CREATE_WAY = "WHEN_INSERT";
        	Map<String, Object> workCircleParams = new HashMap<String, Object>();
        	EventDisposal event = new EventDisposal();
        	boolean flag = false;
        	
	    	event.setEventId(eventId);
	    	workCircleParams.put("createWay", WORK_CIRCLE_CREATE_WAY);
	    	
	    	flag = workCircleService.saveOrUpdate(event, null, userInfo, workCircleParams);
	    	
	    	if(!flag) {
	    		logger.error("事件【" + eventId + "】增添工作圈失败，增添方式为【" + WORK_CIRCLE_CREATE_WAY + "】！");
	    	}
    	} else {
    		logger.error("增添工作圈失败，缺少有效的事件id！");
    	}
    }


}
