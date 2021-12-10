package cn.ffcs.zhsq.decisionMaking;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.oa.entity.interalrule.IdRecord;
import cn.ffcs.oa.service.interalrule.IntegralRuleService;
import cn.ffcs.shequ.eventReportRecord.domain.EventReportRecordInfo;
import cn.ffcs.shequ.eventReportRecord.service.IEventReportRecordService;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.dispute.service.IDisputeMediationService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeMediation;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.JsonUtils;

/**
 * @Description: 甘南州卓尼县(ZHuoNi)新版事件通用 事件状态变更决策类
 * 必填参数
 * 		decisionService 指定实现类 eventStatusDecisionMaking4CGQService
 * 		eventId			事件id
 * 		curNodeCode		当前节点编码
 *		nextNodeCode	下一节点编码
 * 		userId			办理人员id
 * 		orgId			办理人员所属组织
 * 非必填参数
 * 		handleDate		环节处理时限，办结时为结案时间
 * 		isReject		true时，表示当前操作为驳回操作
 * 		preNodeCode		上一节点编码，当isReject为true时，传入
 * 
 * @ClassName:   EventStatusDecisionMaking4ZHNServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年4月21日 上午9:24:32
 */
@Service(value = "eventStatusDecisionMaking4ZHNService")
public class EventStatusDecisionMaking4ZHNServiceImpl extends EventStatusDecisionMaking4CGQServiceImpl {
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IDisputeMediationService disputeMediationService;
	
	@Autowired
	private IEventReportRecordService eventReportRecordService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoService;
	
	@Autowired
	private IntegralRuleService integralRuleService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		Long eventId = -1L,			//事件id
			 userId = -1L,			//事件操作人员Id
			 orgId = -1L;			//事件操作人员所属组织
		
		String eventStatus = "",	//事件状态
			   curNodeCode = "",	//当前环节节点编码
			   nextNodeCode = "";	//下一环节节点编码
		
		Date handleDate = null;		//环节办理时限
		
		if(CommonFunctions.isNotBlank(params, "eventId")) {
			try{
				eventId = Long.valueOf(params.get("eventId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[eventId]："+params.get("eventId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[eventId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "nextNodeCode")) {
			nextNodeCode = params.get("nextNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[nextNodeCode]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "userId")) {
			try{
				userId = Long.valueOf(params.get("userId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[userId]："+params.get("userId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[userId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			try{
				orgId = Long.valueOf(params.get("orgId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[orgId]："+params.get("orgId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[orgId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "handleDate")) {
			handleDate = (Date)params.get("handleDate");
		}
		
		eventStatus = this.updateEventStatus(eventId, userId, orgId, "", curNodeCode, nextNodeCode, handleDate, params);
		
		if((START_NODE_CODE.equals(curNodeCode) && COLLECT_NODE_CODE.equals(nextNodeCode)) || END_NODE_CODE.equals(nextNodeCode)) {//事件归档时，新增个人积分信息
			addUserDetailScore(eventId, curNodeCode, nextNodeCode, userId);
		}
		
		return eventStatus;
	}
	
	/**
	 * 添加积分
	 * 非矛盾纠纷上报事件，每条事件加2分，每月上限为40分；
	 * 矛盾纠纷上报事件归档时，每条事件加1分，每月上限为20分；
	 * @param eventId
	 * @param curNodeCode
	 * @param nextNodeCode
	 * @param operatorId
	 */
	private void addUserDetailScore(Long eventId, String curNodeCode, String nextNodeCode, Long operatorId) {
		ProInstance proInstance = null;
		String subModularCode = null, interalReason = null;
		DisputeMediation disputeMediation = findMediationByEventId(eventId);
		Long mediationId = null;
		
		if(disputeMediation != null) {
			mediationId = disputeMediation.getMediationId();
		}
		
		if(START_NODE_CODE.equals(curNodeCode) && COLLECT_NODE_CODE.equals(nextNodeCode)) {
			if(mediationId == null || mediationId < 0) {
				subModularCode = "F-OA004ZHN001";
				interalReason = "采集事件，事件id【" + eventId + "】！";
			}
		} else if(END_NODE_CODE.equals(nextNodeCode)) {
			if(mediationId != null && mediationId > 0) {
				subModularCode = "F-OA004ZHN003";
				interalReason = "事件归档，事件id【" + eventId + "】，矛盾纠纷id【" + mediationId + "】！";
			}
		}
		
		if(StringUtils.isNotBlank(subModularCode)) {
			proInstance = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
		}
		
		if(proInstance != null) {
			Long creatorId = proInstance.getUserId(), createOrgId = proInstance.getOrgId(), createRegionId = null;
			OrgSocialInfoBO orgInfo = null;
			String regionCode = null, creatorOrgCode = null, 
				   TABLE_TYPE = "GRID_USER", 
				   RULE_TYPE_ADD = "01";//加分
			IdRecord idRecord = new IdRecord();
			
			if(createOrgId != null && createOrgId > 0) {
				orgInfo = orgSocialInfoService.findByOrgId(createOrgId);
				
				if(orgInfo != null) {
					creatorOrgCode = orgInfo.getOrgCode();
					createRegionId = orgInfo.getLocationOrgId();
				}
			}
			
			if(createRegionId != null && createRegionId > 0) {
				OrgEntityInfoBO regionInfo = orgEntityInfoService.findByOrgId(createRegionId);
				
				if(regionInfo != null) {
					regionCode = regionInfo.getOrgCode();
				}
			}
			
			idRecord.setPartyMemberId(creatorId);
			idRecord.setOrgCode(creatorOrgCode);
			idRecord.setRegionCode(regionCode);
			idRecord.setCreator(operatorId);
			idRecord.setSubmodularCode(subModularCode);
			idRecord.setTableType(TABLE_TYPE);
			idRecord.setBizId(eventId);
			idRecord.setInteralReason(interalReason);
			idRecord.setRuleType(RULE_TYPE_ADD);
			
			try {
				integralRuleService.rewardPointsV2(idRecord, null);
			} catch (Exception e) {
				if(logger.isErrorEnabled()) {
					logger.error(e.getMessage());
					logger.error("积分添加失败！参数如下：" + JsonUtils.beanToJson(idRecord));
				}
			}
		}
	}
	
	/**
	 * 依据事件id获取矛盾纠纷信息
	 * @param eventId	事件id
	 * @return
	 */
	private DisputeMediation findMediationByEventId(Long eventId) {
		String DISPUPTE_MEDIATION_BIZ_TYPE = "DISPUTE_MEDIATION_DF";
		DisputeMediation disputeMediation = null;
		
		if(eventId != null && eventId > 0) {
			EventReportRecordInfo eventReportRecordInfo = new EventReportRecordInfo();
			Long mediationId = -1L;
			
			eventReportRecordInfo.setEventId(eventId);
			eventReportRecordInfo.setBizType(DISPUPTE_MEDIATION_BIZ_TYPE);
			
			EventReportRecordInfo resultRecord = eventReportRecordService.findEventReportRecordInfoByEventId(eventReportRecordInfo);
			
			if(resultRecord != null) {
				mediationId = resultRecord.getBizId();
				
				if(mediationId != null && mediationId > 0) {//判断矛盾纠纷的有效性，因为此处是以事件的角度添加矛盾纠纷的个人加分，因此必须保证矛盾纠纷有效，才可添加
					disputeMediation = disputeMediationService.selectByPrimaryKey(mediationId);
				}
			}
		}
		
		return disputeMediation;
	}
}
