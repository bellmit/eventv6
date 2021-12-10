package cn.ffcs.zhsq.dispute.controller;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.eventReportRecord.domain.EventReportRecordInfo;
import cn.ffcs.shequ.eventReportRecord.service.IEventReportRecordService;
import cn.ffcs.shequ.mybatis.domain.zzgl.dispute.DisputeMediation;
import cn.ffcs.shequ.zzgl.service.dispute.IDisputeMediationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/zhsq/event/dispute")
public class DisputeMediationController extends ZZBaseController {

	@Autowired
	private IEventReportRecordService eventReportRecordService;
	@Autowired 
	private IDisputeMediationService disputeMediationService;

	@RequestMapping(value="/mediation/detail")
	public String mediation_detail(HttpSession session, HttpServletRequest request,
								   HttpServletResponse response,
									   @RequestParam(value="eventId",required = true) Long eventId,
								   ModelMap map){
		EventReportRecordInfo eventReportRecordInfo = new EventReportRecordInfo();
		DisputeMediation disputeMediation = null;
		
		eventReportRecordInfo.setEventId(eventId);
		eventReportRecordInfo = eventReportRecordService.findEventReportRecordInfoByEventId(eventReportRecordInfo);
		
		if(eventReportRecordInfo != null) {
			disputeMediation = disputeMediationService.selectByPrimaryKey(eventReportRecordInfo.getBizId());
		}
		if(disputeMediation == null) {
			disputeMediation = new DisputeMediation();
		}
		
		map.addAttribute("disputeMediation",disputeMediation);
		
		return "/zzgl/dispute/mediation/add_mediation.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResultObj save(
			HttpSession session,
			HttpServletRequest request,
			@ModelAttribute(value = "disputeMediation") DisputeMediation disputeMediation,
			ModelMap map) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj resultObj = null;
		int recordId = 0;
		disputeMediation.setDisputeStatus("3");//矛盾纠纷结案状态
		if(disputeMediation.getMediationId() != null){
			recordId = disputeMediationService.updateByPrimaryKeySelective(disputeMediation);
		}
		if(disputeMediation.getDisputeId() != null){
			disputeMediationService.updateWeiXinDispute(disputeMediation.getDisputeId(), "3");//1：未受理，2：上报，3：结案，4：已受理，5：驳回，6：已评价
		}
		resultObj = Msg.OPERATE.getResult(recordId > 0, disputeMediation.getMediationId());
		return resultObj;
	}
}
