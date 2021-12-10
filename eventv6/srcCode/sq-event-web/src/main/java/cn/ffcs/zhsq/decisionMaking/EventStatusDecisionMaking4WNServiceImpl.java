package cn.ffcs.zhsq.decisionMaking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.common.message.bo.ReceiverBO;
import cn.ffcs.system.publicUtil.StringUtils;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.warningScheme.SchemeKeyword;
import cn.ffcs.zhsq.mybatis.domain.warningScheme.SchemeMatch;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.warningScheme.service.IWarningSchemeService;

/**
 * 新版事件通用 事件状态变更决策类(万宁修改事件等级子类) 必填参数 decisionService 指定实现类
 * eventStatusDecisionMakingService eventId 事件id curNodeCode 当前节点编码 nextNodeCode
 * 下一节点编码 userId 办理人员id orgId 办理人员所属组织 非必填参数 handleDate 环节处理时限，办结时为结案时间 isReject
 * true时，表示当前操作为驳回操作 preNodeCode 上一节点编码，当isReject为true时，传入
 * 
 * @author youwj
 *
 */
@Service(value = "eventStatusDecision4WNMakingService")
public class EventStatusDecisionMaking4WNServiceImpl extends EventStatusDecisionMakingServiceImpl
		implements IWorkflowDecisionMakingService<String> {

	// 事件处理模块
	@Autowired
	private IEventDisposalService eventDisposalService;

	// 消息发送模块
	@Autowired
	private MessageOutService messageService;

	// 预警方案服务
	@Autowired
	private IWarningSchemeService warningSchemeService;

	// 字典服务
	@Autowired
	private IBaseDictionaryService dictionaryService;

	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;

	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;

	/**
	 * 更新事件状态，并新增中间记录
	 * 
	 * @param eventId
	 * @param userId
	 * @param orgId
	 * @param chiefLevel
	 * @param curNodeCode
	 * @param nextNodeCode
	 * @param handleDate
	 * @return
	 * @throws Exception
	 */
	protected String updateEventStatus(Long eventId, Long userId, Long orgId, String chiefLevel, String curNodeCode,
			String nextNodeCode, Date handleDate, Map<String, Object> extraParam) throws Exception {
		if(StringUtils.isBlank(curNodeCode)){
			if(null!=eventId&&eventId>0){
				ProInstance pro = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
				if(pro!=null){
					curNodeCode=pro.getCurNode();
				}else{
					
					throw new ZhsqEventException("传入的当前环节不能为空");
				}
			}else{
				throw new ZhsqEventException("传入的事件Id不正确");
			}
		}
		
		if(orgId==null||orgId<0){
			throw new ZhsqEventException("传入的组织Id不正确");
		}
		
		try {
			if (START_NODE_CODE.equals(curNodeCode)) {// 如果是首次提交则进行等级匹配
				List<BaseDataDict> dataDictListByDictCode = dictionaryService.getDataDictTree(ConstantValue.URGENCY_DEGREE_PCODE,
						orgSocialInfoService.findByOrgId(orgId).getOrgCode());

				Map<String, Long> codeMap = new HashMap<String, Long>();
				for (BaseDataDict var : dataDictListByDictCode) {
					codeMap.put(var.getDictGeneralCode(), var.getDictOrderby());
				}

				Map<String, String> nameMap = new HashMap<String, String>();
				for (BaseDataDict var : dataDictListByDictCode) {
					nameMap.put(var.getDictGeneralCode(), var.getDictName());
				}
				// 更新事件状态
				EventDisposal eventTmp = new EventDisposal();
				eventTmp.setEventId(eventId);
				// 设置事件紧急程度
				// 获取原先库中的事件
				EventDisposal orgEvent = eventDisposalService.findEventByIdSimple(eventId);
				// 获取当前库中生效的预警方案并判断是否唯一,唯一则进行等级变动
				List<SchemeMatch> schemeEffect = warningSchemeService.findSchemeEffect();
				if (schemeEffect != null && schemeEffect.size() == 1) {
					// 获取关键字详情
					List<SchemeKeyword> schemeKeywordBySchemeId = warningSchemeService
							.findSchemeKeywordBySchemeId(schemeEffect.get(0).getSchemeId());

					List<SchemeKeyword> schemeKeywordForSearch = new ArrayList<SchemeKeyword>();

					for (int i = 0, j = schemeKeywordBySchemeId.size(); i < j; i++) {
						if (null != schemeKeywordBySchemeId.get(i).getKeyword()
								&& schemeKeywordBySchemeId.get(i).getKeyword() != "") {
							schemeKeywordForSearch.add(schemeKeywordBySchemeId.get(i));
						}
					}

					for (SchemeKeyword schemeKeyword : schemeKeywordForSearch) {
						if (schemeKeyword.getKeyword() != null && schemeKeyword.getKeyword().length() > 0) {

							List<String> keyword = Arrays.asList(schemeKeyword.getKeyword().split("，"));
							if (keyword != null && keyword.size() > 0) {
								Map<String, Object> curEvent = warningSchemeService.fetchKeyWord(keyword, eventId);

								// 用户设定等级
								Long userDegree = codeMap.get(orgEvent.getUrgencyDegree());
								// 自动匹配等级
								Long autoDegree = codeMap.get(schemeKeyword.getCode());

								if (curEvent != null && curEvent.size() == 1 && userDegree > autoDegree) {// 符合匹配关键字并且等级大于用户所设定的等级
									eventTmp.setUrgencyDegree(schemeKeyword.getCode());
									if (orgEvent.getBizPlatform().equals("0")) {// 如果不是对接和审核的事件，则发送消息

										this.sendMessage(eventId, userId, orgId, nameMap.get(schemeKeyword.getCode()));

									}
									break;
								}
							}
						}
					}

				}

				eventDisposalService.updateEventDisposalById(eventTmp);
				

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.updateEventStatus(eventId, userId, orgId, chiefLevel, curNodeCode, nextNodeCode, handleDate,
				extraParam);
	}

	public void sendMessage(Long eventId, Long userId, Long orgId, String levelName) {
		ReceiverBO receiver = new ReceiverBO();
		List<Long> nextUserIdList = new ArrayList<Long>();
		EventDisposal event = warningSchemeService.findEventById(eventId);
		ProInstance pro = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
		String EVENT_MEG_MODULE_CODE = "02", // 事件消息所属模块编码
				EVENT_VIEW_LINK = "$EVENT_DOMAIN/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&eventId=", // 事件消息查看详情链接
				EVENT_MSG_ACTION = "系统发送了", // 消息发送操作
				viewLink = null, msgContent = "您上报的【@eventName】达到了" + levelName + "等级条件,已被系统自动变更为" + levelName + "等级";// 消息内容

		if (event != null) {
			viewLink = EVENT_VIEW_LINK + eventId + "&instanceId=" + pro.getInstanceId();
			msgContent = msgContent.replaceAll("@eventName", event.getEventName());

			nextUserIdList.add(Long.valueOf(event.getCreatorId()));

			receiver.setUserIdList(nextUserIdList);

			messageService.sendCommonMessageA(null, null, EVENT_MEG_MODULE_CODE, eventId, viewLink, msgContent, EVENT_MSG_ACTION, receiver);
		}
	}

}
