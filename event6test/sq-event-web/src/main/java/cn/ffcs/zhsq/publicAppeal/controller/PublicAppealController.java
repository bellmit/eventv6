package cn.ffcs.zhsq.publicAppeal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.shequ.wap.util.DateUtils;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.intermediateData.publicDemand.service.PublicDemandService;
import cn.ffcs.zhsq.mybatis.domain.drugEnforcementEvent.DrugEnforcementEvent;
import cn.ffcs.zhsq.mybatis.domain.publicAppeal.PublicAppeal;
import cn.ffcs.zhsq.publicAppeal.service.PublicAppealService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.ffcs.system.publicUtil.EUDGPagination;

/**
 * @Description: 公众诉求模块控制器
 * @Author: zhongshm
 * @Date: 09-01 15:32:03
 * @Copyright: 2017 福富软件
 */
@Controller("publicAppealController")
@RequestMapping("/zhsq/publicAppeal")
public class PublicAppealController extends ZZBaseController {

	@Autowired
	private PublicAppealService publicAppealService; //注入公众诉求模块服务

    @Autowired
    private PublicDemandService publicDemandService; //注入公众诉求模块服务


    private final String FORM_TYPE = "publicAppeal";//表单类型
    
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request,String listType, HttpSession session, ModelMap map) {
		map.addAttribute("listType",listType);
		return "/zzgl/publicAppeal/list_publicAppeal.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,
		int page, int rows,String listType,@RequestParam Map<String,Object> params) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;

		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		params.put("startInfoOrgCode", this.getDefaultInfoOrgCode(session));
		if (listType != null){
			params.put("listType", listType);
		}
		
		EUDGPagination pagination = publicAppealService.searchList(page, rows,userInfo,params);
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		PublicAppeal bo = publicAppealService.searchById(id);
		map.addAttribute("bo", bo);
		return "/zzgl/publicAppeal/detail_publicAppeal.ftl";
	}

	/**
	 * 审核页面
	 */
	@RequestMapping("/examine")
	public Object examine(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		PublicAppeal bo = publicAppealService.searchById(id);
		map.addAttribute("bo", bo);
		
		//获取诉求类型与事件类型的映射B915007
		List<BaseDataDict> appealMappingList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.APPEAL_MAPPING_PCODE, userInfo.getOrgCode());
		if(appealMappingList!=null&&appealMappingList.size()>0) {
			
			Map<String,Object> cataMap=new HashMap<String,Object>();
			cataMap.put("appealCatalog", bo.getAppealCatalog());
			cataMap.put("appealCatalogMappingType", "");
			DataDictHelper.setDictValueForField(cataMap, "appealCatalog", "appealCatalogMappingType",
					appealMappingList);
			cataMap.remove("appealCatalog");
			if(CommonFunctions.isNotBlank(cataMap, "appealCatalogMappingType")) {
				map.addAllAttributes(cataMap);
			}
		}
		
		map.put("imgDownPath", App.IMG.getDomain(session));//用于附件上传组件中的缩略图展示
		return "/zzgl/publicAppeal/examine_publicAppeal.ftl";
	}

	/**
	 * 驳回页面
	 */
	@RequestMapping("/to/reject")
	public Object to_reject(HttpServletRequest request, HttpSession session, ModelMap map, Long id) {
		PublicAppeal bo = publicAppealService.searchById(id);
		map.addAttribute("bo", bo);
		return "/zzgl/publicAppeal/reject_publicAppeal.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/reject", method = RequestMethod.POST)
	public ResultObj reject(HttpSession session, @RequestParam(value = "appealId") Long publicAppealId
			, @RequestParam(value = "handleRs") String handleRs) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj resultObj = null;
		try {
			PublicAppeal publicAppeal = new PublicAppeal();
			publicAppeal.setAppealId(publicAppealId);
			publicAppeal.setHandleSit(ConstantValue.PUBLIC_APPEAL_HANDLESIT_04);
			publicAppeal.setHandleRs(handleRs);
			publicAppeal.setRep_org_name(userInfo.getOrgName());
			publicAppeal.setRep_time(DateUtils.getNow());
			boolean result = publicAppealService.update(publicAppeal);
//			boolean result = publicDemandService.reportPublicAppealEvent(publicAppealId, userInfo);
			resultObj = Msg.OPERATE.getResult(result);
		} catch (Exception e) {
			resultObj = Msg.OPERATE.getResult(e);
			e.printStackTrace();
		}

		return resultObj;
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		if (id != null) {
			PublicAppeal bo = publicAppealService.searchById(id);
			map.put("bo", bo);
		}
		return "/zzgl/publicAppeal/form_publicAppeal.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		PublicAppeal bo) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		if (bo.getAppealId() == null) { //新增
			Long id = publicAppealService.insert(bo);
			if (id != null && id > 0) {
				result = "success";
			}
		} else { //修改
			boolean updateResult = publicAppealService.update(bo);
			if (updateResult) {
				result = "success";
			}
		}
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		PublicAppeal bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = publicAppealService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 审核详情1：草果，2：待办，3：辖区待办,4：经办，5：我发起的
	 */
	@RequestMapping("/EventDetail")
	public String eventDetail(HttpServletRequest request, HttpSession session, ModelMap map,
							  Long id,@RequestParam(value = "listType", required=false, defaultValue="1") Integer listType)
	{
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		PublicAppeal bo = publicAppealService.searchById(id);

		if(bo == null) {
			bo = new PublicAppeal();
		}

		try {
			this.capWorkflowRelativeData(id, listType, userInfo, map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.addAttribute("bo", bo);
		map.addAttribute("listType", listType);

		map.put("imgDownPath", App.IMG.getDomain(session));//用于附件上传组件中的缩略图展示
		return "/zzgl/publicAppeal/event_publicAppeal.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/startWorkflow", method = RequestMethod.POST)
	public ResultObj startWorkflow(HttpSession session,@RequestParam(value = "publicAppealId") Long publicAppealId
			,@RequestParam Map<String,Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj resultObj = null;

		PublicAppeal publicAppeal = publicAppealService.searchById(publicAppealId);
		Long saveAndReport = publicAppealService.saveAndReport(publicAppeal, userInfo,params);
		resultObj = Msg.OPERATE.getResult(saveAndReport);
//		try {
//			boolean result = publicDemandService.reportPublicAppealEvent(publicAppealId, userInfo);
//			PublicAppeal publicAppeal = new PublicAppeal();
//			publicAppeal.setAppealId(publicAppealId);
//			publicAppeal.setHandleSit(ConstantValue.PUBLIC_APPEAL_HANDLESIT_01);
//			publicAppeal.setRep_org_name(userInfo.getOrgName());
//			publicAppeal.setRep_time(DateUtils.getNow());
//			boolean updateResult = publicAppealService.update(publicAppeal);
//
//			resultObj = Msg.OPERATE.getResult(result);
//		} catch (Exception e) {
//			resultObj = Msg.OPERATE.getResult(e);
//			e.printStackTrace();
//		}

		return resultObj;
	}

    /**
     * 启动民众诉求事件流程
     * @param session
     * @param publicAppealId	民众诉求事件id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/startWorkflow4PublicAppeal", method = RequestMethod.POST)
    public ResultObj startWorkflow4PublicAppeal(HttpSession session,
                                                @RequestParam(value = "publicAppealId") Long publicAppealId) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        ResultObj resultObj = null;

        try {
            boolean result = publicDemandService.reportPublicAppealEvent(publicAppealId, userInfo);
			PublicAppeal publicAppeal = new PublicAppeal();
			publicAppeal.setAppealId(publicAppealId);
			publicAppeal.setHandleSit(ConstantValue.PUBLIC_APPEAL_HANDLESIT_01);
			publicAppeal.setRep_org_name(userInfo.getOrgName());
			publicAppeal.setRep_time(DateUtils.getNow());
			boolean updateResult = publicAppealService.update(publicAppeal);

            resultObj = Msg.OPERATE.getResult(result);
        } catch (Exception e) {
            resultObj = Msg.OPERATE.getResult(e);
            e.printStackTrace();
        }

        return resultObj;
    }

    /**
     * 获取民众诉求事件相关工作流新
     * @param publicAppealId
     * @param listType
     * @param userInfo
     * @param map
     * @throws Exception
     */
    private void capWorkflowRelativeData(Long publicAppealId, Integer listType, UserInfo userInfo, ModelMap map) throws Exception {
        Map<String, Object> curTaskData = null;
        Long taskId = -1L;

        Long instanceId = publicDemandService.capInstanceId(publicAppealId, userInfo);

        if(instanceId > 0) {
            curTaskData = publicDemandService.findCurTaskData(publicAppealId, userInfo);

            if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
                taskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
            }

            List<Map<String, Object>> participantMapList = publicDemandService.findParticipantsByTaskId(taskId);
            if(participantMapList != null) {
                StringBuffer taskPersonStr = new StringBuffer(";");

                for(Map<String, Object> participant : participantMapList){
                    Object orgNameObj = participant.get("ORG_NAME");

                    if(CommonFunctions.isNotBlank(participant, "USER_NAME")){
                        taskPersonStr.append(participant.get("USER_NAME"));
                        if(orgNameObj != null){
                            taskPersonStr.append("(").append(orgNameObj).append(");");
                        }
                    }else if(orgNameObj != null){
                        taskPersonStr.append(orgNameObj).append(";");
                    }
                }

                map.addAttribute("taskPersonStr", taskPersonStr.substring(1));

                if(listType == 2) {//待办
                    map.addAttribute("isCurHandler", publicDemandService.isCurTaskPaticipant(participantMapList, userInfo, null));
                }
            }

            if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
                map.addAttribute("curTaskName", curTaskData.get("WF_ACTIVITY_NAME_"));
            }
            if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
                map.addAttribute("curNodeName", curTaskData.get("NODE_NAME"));
            }
            if(CommonFunctions.isNotBlank(curTaskData, "NODE_ID")) {
                Integer nodeId = Integer.valueOf(curTaskData.get("NODE_ID").toString());
                map.addAttribute("operateList", publicDemandService.findOperateByNodeId(nodeId));
            }

            map.addAttribute("nextTaskNodes", publicDemandService.findNextTaskNodes(publicAppealId, userInfo));
            map.addAttribute("formId", publicAppealId);
            map.addAttribute("formType", FORM_TYPE);
            map.addAttribute("instanceId", instanceId);
        }
    }

    /**
     * 提交民众诉求事件
     * @param session
     * @param publicAppealId	民众诉求id
     * @param nextNodeName		下一环节名称
     * @param params
     * 			nextUserIds 	下一环节办理人员，以英文逗号分隔
     * 			nextOrgIds 		下一环节办理组织，以英文逗号分隔，与nextUserIds一一对应
     * 			advice 			办理意见
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/subWorkflowData", method = RequestMethod.POST)
    public ResultObj subWorkflowData(HttpSession session,
                                     @RequestParam(value = "formId") Long publicAppealId,
                                     @RequestParam(value = "nextNodeName") String nextNodeName,
                                     @RequestParam Map<String, Object> params) throws Exception {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        ResultObj result = null;
		if(nextNodeName.equals("end1")){
			String advice = params.get("advice").toString();
			Long formId = Long.valueOf(params.get("formId").toString());
			PublicAppeal publicAppeal = new PublicAppeal();
			publicAppeal.setAppealId(formId);
			publicAppeal.setHandleSit(ConstantValue.PUBLIC_APPEAL_HANDLESIT_03);
			publicAppeal.setHandleRs(advice);
			publicAppeal.setRep_org_name(userInfo.getOrgName());
			publicAppeal.setRep_time(DateUtils.getNow());
			boolean update = publicAppealService.update(publicAppeal);
//			result = Msg.OPERATE.getResult(update);
		}

		try {
            boolean flag = publicDemandService.subWorkflow(publicAppealId, nextNodeName, null, userInfo, params);
            result = Msg.OPERATE.getResult(flag);
        } catch (Exception e) {
            result = Msg.OPERATE.getResult(e);
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 跳转民众诉求事件详情页面
     * @param session
     * @param publicAppealId	民众诉求事件id
     * @param listType			列表类型
     * 			1	草稿
     * 			2	待办
     * 			3	辖区所有
     * @param map
     * @return
     */
    @RequestMapping(value="/toDetail")
    public String toDetail(HttpSession session,
                           @RequestParam(value = "publicAppealId") Long publicAppealId,
                           @RequestParam(value = "listType", required=false, defaultValue="1") Integer listType,
                           ModelMap map) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        String orgCode = userInfo.getOrgCode();
//        DrugEnforcementEvent drugEnforcementEvent = publicAppealService.findDrugEnforcementEventById(drugEnforcementId, orgCode);
//
//        if(drugEnforcementEvent == null) {
//            drugEnforcementEvent = new DrugEnforcementEvent();
//        }

        try {
            this.capWorkflowRelativeData(publicAppealId, listType, userInfo, map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //   map.addAttribute("drugEnforcementEvent", drugEnforcementEvent);
        map.addAttribute("listType", listType);

        return "/zzgl/inter,ediateData/publicAppeal/detail_publicAppeal.ftl";
    }



	/**
	 * 驳回上一步
	 * @param session
	 * @param publicAppealId	民众诉求事件id
	 * @param rejectToNodeName	驳回环节的名称
	 * @param params
	 * 			advice 			驳回意见
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/rejectWorkflowData", method = RequestMethod.POST)
	public ResultObj rejectWorkflowData(HttpSession session,
										@RequestParam(value = "formId") Long publicAppealId,
										@RequestParam(value = "rejectToNodeName", required=false) String rejectToNodeName,
										@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj result = null;

		try {
			boolean flag = publicDemandService.rejectWorkflow(publicAppealId, rejectToNodeName, userInfo, params);
			result = Msg.OPERATE.getResult(flag);
		} catch (Exception e) {
			result = Msg.OPERATE.getResult(e);
			e.printStackTrace();
		}

		return result;
	}
	
	
}