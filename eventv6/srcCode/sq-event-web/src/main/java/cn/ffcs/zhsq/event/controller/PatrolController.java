package cn.ffcs.zhsq.event.controller;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.eventReportRecord.domain.EventReportRecordInfo;
import cn.ffcs.shequ.eventReportRecord.service.IEventReportRecordService;
import cn.ffcs.shequ.utils.WindowHelper;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventRelaService;
import cn.ffcs.zhsq.event.service.IPatrolService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.EventRela;
import cn.ffcs.zhsq.mybatis.domain.event.Patrol;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

@Controller
@RequestMapping("/zhsq/patrolController")
public class PatrolController extends ZZBaseController {
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventDisposalDockingService eventDisposalDockingService;
	
	@Autowired
	private IEventRelaService eventRelaService;
	
	@Autowired
	private IPatrolService patrolService;

	@Autowired
	private IAttachmentService attachmentService;
	
	@Autowired
	private IFunConfigurationService configurationService;
	
	@Autowired
	private IEventReportRecordService eventReportRecordService;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@RequestMapping(value = "/show")
	public String show(HttpSession session, 
			@RequestParam(value = "patrolId") Long patrolId,
			@RequestParam(value = "isDomain", required = false) String isDomain,
    		@RequestParam(value = "source", required = false) String source,
			ModelMap map){
		Patrol patrol = patrolService.findById(patrolId);
		map.addAttribute("patrol", patrol);
		if(!StringUtils.isBlank(source)){
    		map.addAttribute("source", source);
    		map.addAttribute("updomain", App.TOP.getDomain(session));
    	}
		map.addAttribute("bizType", ConstantValue.BIZ_TYPE_EVENT_RELA_PATROL);
		return "/zzgl/patrol/detail.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public Map<String, Object> del(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "idStr") String idStr) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String[] ids = idStr.split(",");
		int result = 0;//删除的事件标识
		for(int index = 0, length = ids.length; index < length; index++){
			Patrol patrol = new Patrol();
			String patrolId = WindowHelper.decode(ids[index], null, "0", "", "0");
			patrol.setPatrolId(Long.valueOf(patrolId));
			patrol.setUpdated(new Date());
			patrol.setUpdator(userInfo.getUserId());
			patrol.setUpdatorName(userInfo.getUserName());
			int record = patrolService.deleteByIdForLogic(patrol);
			if(record > 0){
				result ++;
				//删除业务事件关联信息
				//获取关联模块
				EventReportRecordInfo eventReportRecordInfo = new EventReportRecordInfo();
				if(!StringUtils.isBlank(patrolId)){
					eventReportRecordInfo.setBizId(Long.valueOf(patrolId));
					eventReportRecordInfo.setProcStatus("3");//删除
					eventReportRecordService.editEventReportRecordInfo(eventReportRecordInfo);
				}
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result);
		return resultMap;
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "attachmentId", required = false) Long[] ids,
			@RequestParam(value = "patrolStatus", required = false) String patrolStatus,
            @ModelAttribute(value = "patrol") Patrol patrol, 
            ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		if(patrol != null && patrol.getPatrolId() != null){
			patrol.setUpdated(new Date());
			patrol.setUpdator(userInfo.getUserId());
			patrol.setUpdatorName(userInfo.getUserName());
			patrol.setStatus(patrolStatus);
			if(!"0".equals(patrolStatus)){
				patrol.setCode(patrolService.getPatrolCode());
			}
			int record = patrolService.updatePatrol(patrol);
			map.addAttribute("result", record > 0 ? "操作成功！" : "操作失败！");
		}else{
			patrol.setCreated(new Date());
			patrol.setCreator(userInfo.getUserId());
			patrol.setCreatorName(userInfo.getUserName());
			patrol.setStatus(patrolStatus);
			if(!"0".equals(patrolStatus)){
				patrol.setCode(patrolService.getPatrolCode());
			}
			int record = patrolService.insertPatrol(patrol);
			map.addAttribute("result", record > 0 ? "操作成功！" : "操作失败！");
		}
		//report event
		if(!StringUtils.isBlank(patrolStatus)){
			if(patrolStatus.equals(ConstantValue.PATROL_OPERATE_TYPE_REPORT)){
				EventDisposal event = new EventDisposal();
				//获取该组织的矛盾纠纷事件类型
				String patrolEventType = "";
				if(!StringUtils.isBlank(userInfo.getOrgCode())){
					patrolEventType =  configurationService.
							changeCodeToValue(ConstantValue.REPORT_EVENT_TYPE,ConstantValue.PATROL_EVENT_TYPE,IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode());
					if(StringUtils.isBlank(userInfo.getOrgCode())){
						patrolEventType =  configurationService.
								changeCodeToValue(ConstantValue.REPORT_EVENT_TYPE,ConstantValue.PATROL_EVENT_TYPE,IFunConfigurationService.CFG_TYPE_FACT_VAL);
					}
				}else{
					patrolEventType =  configurationService.
							changeCodeToValue(ConstantValue.REPORT_EVENT_TYPE,ConstantValue.PATROL_EVENT_TYPE,IFunConfigurationService.CFG_TYPE_FACT_VAL);
				}
				if(!StringUtils.isBlank(patrolEventType)){
					event.setType(patrolEventType);
					//event.setType(ConstantValue.PATROL_TYPE);
				}
				event.setContent(patrol.getContent());
				event.setGridId(patrol.getGridId());
				event.setEventName(patrol.getName());
				event.setHappenTimeStr(patrol.getStartPatrolTimeStr());
				event.setOccurred(patrol.getOccurred());
				Map<String, Object> result = eventDisposalDockingService.saveEventDisposalAndReport(event, userInfo, "巡防上报事件！");
				EventRela eventRela = new EventRela();
//				System.out.println("patrol.getPatrolId()"+patrol.getPatrolId());
				eventRela.setBizType(ConstantValue.BIZ_TYPE_EVENT_RELA_PATROL);
				eventRela.setBizId(patrol.getPatrolId());
				eventRela.setEventId((Long)result.get("eventId"));
				eventRela.setCreator(userInfo.getUserId());
				eventRela.setCreatorName(userInfo.getUserName());
				int recordId = eventRelaService.insertEventRela(eventRela);
				//在模块事件上报记录关联表中添加记录
				EventReportRecordInfo eventReportRecordInfo = new EventReportRecordInfo();
				eventReportRecordInfo.setBizId(patrol.getPatrolId());
				eventReportRecordInfo.setProcStatus("0");//对应的事件未处理
				EventDisposal eventDisposal = new EventDisposal();
				String bizType = "";
				if((Long)result.get("eventId") != null){
					eventReportRecordInfo.setEventId((Long)result.get("eventId"));
					//获取事件信息
					eventDisposal = eventDisposalService.findEventById((Long)result.get("eventId"), userInfo.getOrgCode());
					if(eventDisposal != null){
						if(eventDisposal.getType()!=null){
							//获取事件类型对应的模块类型bizType
							bizType =  configurationService.
									turnCodeToValue(ConstantValue.EVENT_BIZ_TYPE,eventDisposal.getType(),IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
							if(!StringUtils.isBlank(bizType)){
								eventReportRecordInfo.setBizType(bizType);
							}
						}
					}
				}
//				上报的事件处理后，回调的服务名称
				String serviceName =  configurationService
						.turnCodeToValue(ConstantValue.EVENT_ARCH_CALLBACK_SERVICE, bizType, IFunConfigurationService.CFG_TYPE_JAVA_PROCESSOR, IFunConfigurationService.CFG_ORG_TYPE_0);
				eventReportRecordInfo.setServiceName(serviceName);
				Long recordIds = eventReportRecordService.insertEventReportRecordInfo(eventReportRecordInfo, userInfo.getOrgCode());
				map.addAttribute("result", recordId > 0 ? "操作成功！" : "操作失败！");
			}
		}
		//附件
		if(ids!=null && ids.length>0){
			boolean b = attachmentService.updateBizId(patrol.getPatrolId(), ConstantValue.BIZ_TYPE_EVENT_RELA_PATROL, ids);
		}
		return "/zzgl/patrol/result_head.ftl";
	}
	
	@RequestMapping(value = "/toEdit")
	public String toEdit(HttpSession session, 
			@RequestParam(value = "patrolId") Long patrolId,
			ModelMap map){
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		Patrol patrol = patrolService.findById(patrolId);
		map.addAttribute("patrol", patrol);
		map.addAttribute("bizType", ConstantValue.BIZ_TYPE_EVENT_RELA_PATROL);
		return "/zzgl/patrol/create.ftl";
	}
	
	@RequestMapping(value = "/toAdd")
	public String toAdd(HttpSession session, 
			ModelMap map){
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("startGridName", defaultGridInfo.get(KEY_START_GRID_NAME));
		Patrol patrol = new Patrol();
		//获取红袖标编号
//		patrol.setCode(patrolService.getPatrolCode());
		patrol.setGridName(defaultGridInfo.get(KEY_START_GRID_NAME).toString());
		patrol.setGridId((Long)defaultGridInfo.get(KEY_START_GRID_ID));
		
		String dateStr = "";
		Date date = new Date();
		dateStr = sdf.format(date);
		map.put("dateStr", dateStr);
		
		map.addAttribute("patrol", patrol);
		map.addAttribute("bizType", ConstantValue.BIZ_TYPE_EVENT_RELA_PATROL);
		return "/zzgl/patrol/create.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/listData", method = RequestMethod.POST)
	public EUDGPagination listData(
			HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@ModelAttribute(value = "patrol") Patrol patrol) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if (page <= 0)
			page = 1;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridId", patrol.getGridId());
		if(!StringUtils.isBlank(patrol.getKeyWord())){
			int a = patrol.getKeyWord().indexOf("%");
			if(a>=0){
				String astr = patrol.getKeyWord().replaceAll("%", "\\\\%");
				System.out.println(astr);
				patrol.setKeyWord(astr);
			}
			int b = patrol.getKeyWord().indexOf("_");
			if(b>=0){
				String bstr = patrol.getKeyWord().replaceAll("_", "\\\\_");
				System.out.println(bstr);
				patrol.setKeyWord(bstr);
			}
		}
		params.put("keyWord", patrol.getKeyWord());
		params.put("type", patrol.getBizType());
		
		EUDGPagination EUDGPagination = patrolService.findPatrolPagination(page, rows, params);
		return EUDGPagination;
	}
	
	@RequestMapping(value = "/index")
	public String index(
			HttpSession session, 
			@RequestParam(value = "bizType", required = false) String bizType,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("bizType", bizType == null ? "":bizType);
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("startGridName", defaultGridInfo.get(KEY_START_GRID_NAME));
		map.addAttribute("iframeCloseCallBack", ConstantValue.closeCallBack);
		map.addAttribute("iframeUrl", App.EVENT.getDomain(session)+ConstantValue.iframeUrl);
		//巡防管理事件类型
		//获取该组织的巡防管理事件类型
		String patrolEventType = "";
		//获取事件类型对应的模块类型bizType
		if(!StringUtils.isBlank(userInfo.getOrgCode())){
			patrolEventType =  configurationService.
					changeCodeToValue(ConstantValue.REPORT_EVENT_TYPE,ConstantValue.PATROL_EVENT_TYPE,IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode());
			if(StringUtils.isBlank(userInfo.getOrgCode())){
				patrolEventType =  configurationService.
						changeCodeToValue(ConstantValue.REPORT_EVENT_TYPE,ConstantValue.PATROL_EVENT_TYPE,IFunConfigurationService.CFG_TYPE_FACT_VAL);
			}
		}else{
			patrolEventType =  configurationService.
					changeCodeToValue(ConstantValue.REPORT_EVENT_TYPE,ConstantValue.PATROL_EVENT_TYPE,IFunConfigurationService.CFG_TYPE_FACT_VAL);
		}
		map.addAttribute("patrolEventType", patrolEventType);
		return "/zzgl/patrol/index.ftl";
	}
	
	/**
	 * 事件上报
	 * @param session
	 * @param prjId 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/report", method = RequestMethod.POST)
	public Map<String, Object> report(HttpSession session, 
			@RequestParam(value = "patrolId", required = false) Long patrolId, 
			@RequestParam(value = "eventId", required = false) Long eventId,
			ModelMap map, @RequestParam(value = "status", required = false) String status){
		//获取修改人
		if(patrolId != null && !StringUtils.isBlank(status)){
			Patrol patrol = new Patrol();
			patrol.setPatrolId(patrolId);
			patrol.setStatus(status);
			patrol.setCode(patrolService.getPatrolCode());
			patrolService.updatePatrol(patrol);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", patrolId>0L?9 :10);
		return resultMap;
	}
}
