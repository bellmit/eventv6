package cn.ffcs.zhsq.event.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserInfoOutService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description:南昌市背街小巷工作流处理类
 * @Author: youwj
 * @Date: 2020/7/15 17:19
 */
@Service(value = "eventDisposalWorkflowForNCHBJXXService")
public class EventDisposalWorkflowForNCHBJXXServiceImpl extends
        EventDisposalWorkflowForEventNewServiceImpl{

    @Autowired
    private OrgSocialInfoOutService orgSocialInfoService;
    
    @Autowired
    private IEventDisposalService eventDisposalService;

    @Autowired
    private UserInfoOutService userInfoService;

    @Autowired
    private OrgSocialInfoOutService orgSocialInfoOutService;

    //南昌市问题上报事件工作流决策类
    private final String NCHBJXX_DECISION_SERVICE = "workflowEventDecisionMaking4NCBJXXService";

    private static final String POSITION_NAME = "街巷长";			    		//进行流程流转得职位名称

    /**
     * @param extraParam
     *          infoOrgCode 事件所属街道地域编码 必填
     * */
    @Override
    public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
        StringBuffer msgWrong = new StringBuffer();
        String instanceId = null;
        //禁止采集并结案操作
        if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose)){
            msgWrong.append("问题上报事件不允许采集并结案操作，请检查！");
        }else {
            UserBO user = new UserBO();
            Map<String, Object> userMap = new HashMap<String, Object>();
            String wftypeId = ConstantValue.EVENT_WORKFLOW_WFTYPEID;
            Integer formId = 0;
            OrgSocialInfoBO orgInfo = null;
            Long orgId = -1L;
            Long userId = -1L;

            if(eventId != null){
                formId = Integer.valueOf(eventId.toString());
            }
            if(userInfo != null){
                orgId = userInfo.getOrgId();
                userId = userInfo.getUserId();
                user.setUserId(userId);
                user.setPartyName(userInfo.getPartyName());
                orgInfo = orgSocialInfoService.findByOrgId(orgId);
            }
            if(extraParam != null) {
                userMap.putAll(extraParam);
            }
            userMap.put("decisionService",NCHBJXX_DECISION_SERVICE);
            
            // 事件所属街道地域编码 360121001
            String infoOrgCode = "";
            /*extraParam.put("infoOrgCode",infoOrgCode);*/
            //获取所属地域编码（小程序传参：街道层级地域编码）
            if (CommonFunctions.isNotBlank(extraParam, "infoOrgCode")) {
                infoOrgCode = String.valueOf(extraParam.get("infoOrgCode"));
            } else {
            	//从事件的信息中获取事件所属地域
            	EventDisposal findEventByIdSimple = eventDisposalService.findEventByIdSimple(eventId);
            	if(findEventByIdSimple!=null&&StringUtils.isNotBlank(findEventByIdSimple.getGridCode())) {
            		infoOrgCode=findEventByIdSimple.getGridCode();
            	}else {
            		msgWrong.append("事件所属地域编码为空，请检查小程序端是否正常传参！");
            	}
            }
            
            //根据街道地域编码查找对应的街道组织
            OrgSocialInfoBO orgSocialInfoBO_street = null;
            if(StringUtils.isNotBlank(infoOrgCode)){
                orgSocialInfoBO_street = orgSocialInfoOutService.getOrgIdByLocationCode(infoOrgCode);
            }
            //根据街道组织获取对应的县区组织
            if(null == orgSocialInfoBO_street){
                msgWrong.append("街道地域编码对应组织为空，请检查！");
            }
            
            if(StringUtils.isBlank(msgWrong.toString())) {
            	//启动流程
            	instanceId = startFlowByWorkFlow(workFlowId, formId, wftypeId, user, orgInfo, userMap);
            }
           
        }
        if(StringUtils.isNotBlank(msgWrong.toString())){
            throw new ZhsqEventException(msgWrong.toString());
        }
        return instanceId;
    }

    
    /**
     * 校验用户是否可以启动流程
     * 背街小巷启动流程人员必须是市级，且职位是街巷长职位
     * */
    @Override
    public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {

        int result = -1;
        StringBuffer msgWrong = new StringBuffer("");

        if (null != orgSocialInfo) {

            String orgLevel = orgSocialInfo.getChiefLevel();
            boolean isAdminPosition = true;

            //判断用户在该组织下是否具有管理员职位
            isAdminPosition = userInfoService.cheakUserForParas(userId, orgSocialInfo.getOrgId(), POSITION_NAME) > 0;

            if (StringUtils.isNotBlank(orgLevel)) {
                if(!ConstantValue.STREET_ORG_LEVEL.equals(orgLevel)){
                    msgWrong.append("组织层级必须是街道层级，当前用户组织层级不是街道，请先检验！");
                }
            }else {
                msgWrong.append("当前组织层级不存在，请先检验！");
            }

            if (!isAdminPosition) {
                msgWrong.append("当前用户不具有[" + POSITION_NAME + "]职位！");
            }
        }else {
            msgWrong.append("当前组织不存在，请先检验！");
        }

        if(msgWrong.length() > 0) {
            throw new Exception(msgWrong.toString());
        } else {
            result = 1;
        }

        return result;
    }
}
