package cn.ffcs.zhsq.decisionMaking;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.gmis.mybatis.domain.userscore.UserDetailScore;
import cn.ffcs.gmis.userscore.service.UserScoreService;
import cn.ffcs.shequ.eventReportRecord.domain.EventReportRecordInfo;
import cn.ffcs.shequ.eventReportRecord.service.IEventReportRecordService;
import cn.ffcs.shequ.mybatis.domain.zzgl.dispute.DisputeMediation;
import cn.ffcs.shequ.zzgl.service.dispute.IDisputeMediationService;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * 矛盾纠纷 个人积分 事件状态决策类
 * 必填参数
 * decisionService 指定实现类 eventStatusDescisionMaking4DisputeScoreService
 * eventId		事件id
 * curNodeCode	当前节点编码
 * nextNodeCode	下一节点编码
 * userId		办理人员id
 * orgId		办理人员所属组织
 * 非必填参数
 * handleDate	环节处理时限，办结时为结案时间
 * 
 * @author zhangls
 *
 */
@Service(value = "eventStatusDescisionMaking4DisputeScoreService")
public class EventStatusDescisionMaking4DisputeScoreServiceImpl extends
		EventStatusDecisionMakingServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private UserScoreService userScoreService;
	
	@Autowired
	private IEventReportRecordService eventReportRecordService;
	
	@Autowired
	private IDisputeMediationService disputeMediationService;
	
	/**
	 * 添加个人积分
	 * 业务信息必须存在：
	 * 		userId，userName，orgCode，createBy，
	 * 		scoreType 
	 * 			D2 矛盾纠纷 1分；矛盾纠纷模块上报的事件归档
	 * 可选信息：dsTime（不存在值时默认当前时间），createTime（不存在值时默认当前时间）
	 * 不填写信息：dsId，updateBy，updateTime
	 */
	protected void addUserDetailScore(Long eventId) {
		DisputeMediation mediation = this.findMediationByEventId(eventId);
		
		if(isReportByMediation(mediation)) {
			addMediationDate(mediation);//添加化解信息
			
			UserDetailScore userDetailScore = new UserDetailScore();
			Long createUserId = -1L, createOrgId = -1L;
			String createUserName = null;
			String SCORE_TYPE_2 = "D2";
			
			if(eventId != null && eventId > 0) {
				ProInstance pro = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
				if(pro != null) {
					createUserId = pro.getUserId();
					createUserName = pro.getUserName();
					createOrgId = pro.getOrgId();
				}
			}
			
			if(StringUtils.isBlank(createUserName)) {
				UserBO userBO = userManageService.getUserInfoByUserId(createUserId);
				if(userBO != null && userBO.getUserId() != null && userBO.getUserId() > 0) {
					createUserName = userBO.getPartyName();
				} else {
					createUserId = -1L;
				}
			}
			
			if(createOrgId != null && createOrgId > 0) {
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(createOrgId);
				if(orgInfo != null && orgInfo.getOrgId() != null && orgInfo.getOrgId() > 0) {
					userDetailScore.setOrgCode(orgInfo.getOrgCode());
				} else {
					createOrgId = -1L;
				}
			}
			
			if(createUserId != null && createUserId > 0 && createOrgId != null && createOrgId > 0) {
				userDetailScore.setUserId(createUserId);
				userDetailScore.setUserName(createUserName);
				userDetailScore.setCreateBy(createUserId);
				userDetailScore.setScoreType(SCORE_TYPE_2);
				userDetailScore.setRemark(String.valueOf(eventId));
				
				userScoreService.insertUserDetailScore(userDetailScore);
			}
		} else {
			super.addUserDetailScore(eventId);
		}
	}
	
	/**
	 * 判断该事件是否由矛盾纠纷模块上传
	 * @param eventId
	 * @return
	 */
	private boolean isReportByMediation(DisputeMediation disputeMediation) {
		boolean flag = false;
			
		if(disputeMediation != null) {
			Long mediationId = disputeMediation.getMediationId();
			
			flag = mediationId != null && mediationId > 0;
		}
		
		return flag;
	}
	
	/**
	 * 依据事件id获取矛盾纠纷信息
	 * @param eventId	事件id
	 * @return
	 */
	private DisputeMediation findMediationByEventId(Long eventId) {
		String DISPUPTE_MEDIATION_BIZ_TYPE = "DISPUTE_MEDIATION_SECURITY";
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
	
	/**
	 * 更新矛盾纠纷的化解信息
	 * @param eventId
	 */
	private void addMediationDate(DisputeMediation disputeMediation) {
		if(disputeMediation != null) {
			Date mediationDate = disputeMediation.getMediationDate();
			String mediationDateStr = disputeMediation.getMediationDateStr();
			
			if(mediationDate == null || StringUtils.isBlank(mediationDateStr)) {
				
				disputeMediation.setMediationDateStr(DateUtils.getToday());
				
				disputeMediationService.updateByPrimaryKeySelective(disputeMediation);
			}
		}
	}
}
