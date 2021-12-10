package cn.ffcs.zhsq.sweepBlackRemoveEvil.eventSBREClue.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.sweepBlackRemoveEvil.eventSBREClue.EventSBREClue;
import cn.ffcs.zhsq.sweepBlackRemoveEvil.eventSBREClue.service.IEventSBREClueService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.SpringContextUtil;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Created by 张天慈 on 2018/05/18.
 */
@Controller("eventSBREClueController")
@RequestMapping("/zhsq/eventSBREClue")
public class EventSBREClueController extends ZZBaseController{

    //线索服务
    @Autowired
    private IEventSBREClueService clueService;

    @Autowired
	private IFunConfigurationService funConfigurationService;
    
    private String FORM_TYPE = "eventSBREClue",//表单类型
            	   ATTACHMENT_TYPE = "eventSBREClueAttachment";//附件类型
    /**
     * 跳转列表页面
     * @param session
     * @param listType
     *          1   草稿
     *          2   待办
     *          3   经办
     *          4   辖区所有
     * @return
     */
    @RequestMapping(value = "/toList")
    public String toList(HttpSession session,ModelMap map,
                         @RequestParam(value = "listType",required = false,defaultValue = "0")Integer listType,
                         @RequestParam Map<String, Object> params){
        map.addAttribute("listType",listType);
        map.addAllAttributes(params);
        map.addAttribute("extraParam", params);
        
        return "zzgl/sweepBlackRemoveEvil/eventSBREClue/list_eventSBREClue.ftl";
    }
    /**
     * 获取列表记录
     * @param session
     * @param page		页码
     * @param rows		每页记录数
     * @param params
     * 			infoOrgCode	发生地域编码
     * 			regionCode	所属地域编码
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listData",method = RequestMethod.POST)
    public EUDGPagination listData(HttpSession session,
                                   @RequestParam(value="page")int page,
                                   @RequestParam(value = "rows")int rows,
                                   @RequestParam Map<String,Object> params){
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        String startInfoOrgCode = this.getDefaultInfoOrgCode(session);
        
        params.put("startRegionCode", startInfoOrgCode);

        EUDGPagination cluePage = clueService.findCluePagination(page, rows, params, userInfo);

        return cluePage;
    }

    /**
     * 新增/编辑线索信息
     * @param session
     * @param copyClueId	用于信息复制的线索id
     * @param clueId		线索id
     * @param map
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd(HttpSession session, 
    		@RequestParam(value = "copyClueId", required = false) Long copyClueId,
    		@RequestParam(value = "clueId", required = false) Long clueId,
    		ModelMap map){
        //获取当前登录用户信息
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String,Object> resultMap = new HashMap<>();
        StringBuffer msgWrong = new StringBuffer("");
        Long queryClueId = -1L;
        String removeDictCodeStr = null, CLUESOURCE_PCODE = "B591001",
        	   userOrgCode = userInfo.getOrgCode();
        
        if(copyClueId != null && copyClueId > 0) {
        	queryClueId = copyClueId;
        } else {
        	queryClueId = clueId;
        }
        
        if (queryClueId != null && queryClueId > 0) {
        	Map<String, Object> params = new HashMap<String, Object>();
        	params.put("userOrgCode", userOrgCode);
        	params.put("isCheckEncrypt", true);
        	params.put("encryptUserId", userInfo.getUserId());
        	params.put("encryptOrgId", userInfo.getOrgId());
        	params.put("isCapGangInfo", true);
        	params.put("isCapInformantInfo", true);
        	params.put("isCapReportedInfo", true);
        	
        	try {
				resultMap = clueService.findByClueId(queryClueId, params);
			} catch (Exception e) {
				msgWrong.append(e.getMessage());
				e.printStackTrace();
			}
        }
        
        if(CommonFunctions.isBlank(resultMap, "clue")) {
        	Map<String,Object> defaultGridInfo = this.getDefaultGridInfo(session);
        	EventSBREClue eventSBREClue = new EventSBREClue();
        	
        	eventSBREClue.setInfoOrgCode(defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString());
        	eventSBREClue.setGridName(defaultGridInfo.get(KEY_START_GRID_NAME).toString());
        	
    		resultMap.put("clue", eventSBREClue);
    	} else {
    		EventSBREClue clue = (EventSBREClue)resultMap.get("clue");
    		
    		clue.setClueId(clueId);
    		
    		if(clueId != null && clueId > 0) {
        		try {//用于控制提交按钮的展示
					Long instanceId = clueService.capInstanceId(clueId, userInfo);
					if(instanceId > 0) {
						map.addAttribute("instanceId", instanceId);
					}
				} catch (Exception e) {
					msgWrong.append(e.getMessage());
					e.printStackTrace();
				}
        	}
    		
    		resultMap.put("clue", clue);
    	}
        
        if(msgWrong.length() > 0) {
        	map.addAttribute("msgWrong", msgWrong);
        	map.addAttribute("clue", new EventSBREClue());
        } else {
        	map.addAllAttributes(resultMap);
        }
        
        removeDictCodeStr = funConfigurationService.changeCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.REMOVE_DICT_CODE + CLUESOURCE_PCODE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode);
        
    	if(StringUtils.isNotBlank(removeDictCodeStr)) {
    		String[] removeDictCodeArray = removeDictCodeStr.split(";"),
    				 dictCodeArray = null;
    		StringBuffer removeDictCodeBuffer = new StringBuffer("");
    		boolean isRemove = false;
    		
    		for(String dictCodeStr : removeDictCodeArray) {
    			if(StringUtils.isNotBlank(dictCodeStr)) {
    				dictCodeArray = dictCodeStr.split(":");
    				if(dictCodeArray != null && dictCodeArray.length > 1) {
    					isRemove = Boolean.valueOf(dictCodeArray[1]);
    					if(isRemove) {
    						removeDictCodeBuffer.append(",").append(dictCodeArray[0]);
    					}
    				}
    			}
    		}
    		
    		if(removeDictCodeBuffer.length() > 0) {
    			map.addAttribute("removeCode", removeDictCodeBuffer.substring(1));
    		}
    	}
    	
        map.addAttribute("attachmentType",ATTACHMENT_TYPE);
        map.addAttribute("maxReportDate", DateUtils.getToday());

        return "zzgl/sweepBlackRemoveEvil/eventSBREClue/add_eventSBREClue.ftl";
    }

    /**
     * 保存新增/编辑线索信息
     * @param session
     * @param clueId	线索id
     * @param eventSBREClue
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveClue")
    public Map<String,Object> saveClue(HttpSession session,
                                       Long clueId,
                                       EventSBREClue eventSBREClue,
                                       @RequestParam Map<String,Object> param,
                                       ModelMap map){
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        boolean result = false;
        ResultObj resultObj = null;
        Map<String,Object> resultMap = new HashMap<>();

        if (clueId != null && clueId > 0) {
            try {
                result = clueService.updateClue(eventSBREClue,param,userInfo);
                resultObj = Msg.EDIT.getResult(result);
            } catch (Exception e) {
                e.printStackTrace();
                resultObj = Msg.EDIT.getResult(e);
            }
        } else {
            try {
                clueId = clueService.saveClue(eventSBREClue,param,userInfo);
                result = clueId > 0;
                resultObj = Msg.ADD.getResult(result);
            } catch (Exception e) {
                e.printStackTrace();
                resultObj = Msg.ADD.getResult(e);
            }

        }

        resultMap.put("result",result);
        resultMap.put("tipMsg",resultObj.getTipMsg());
        resultMap.put("clueId",clueId);

        return resultMap;
    }
    /**
     * 删除线索信息
     * @param session
     * @param clueId	线索id
     */
    @ResponseBody
    @RequestMapping("/delClue")
    public Object delClue(HttpSession session,Long clueId){
        //获取当前登录用户信息
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        boolean result = false;
        ResultObj resultObj = null;

        if(clueId != null){
            result = clueService.deleteByClueId(clueId,userInfo.getUserId());
        }

        resultObj = Msg.DELETE.getResult(result);

        return resultObj;
    }

    /**
     * 跳转详情页面
     * @param session
     * @param clueId	线索id
     * @param listType	列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 辖区所有
     * @param map
     * @return
     */
    @RequestMapping("/toDetail")
    public String toDetail(HttpSession session,
                           Long clueId,
                           @RequestParam(value = "listType", required = false, defaultValue = "0") Integer listType,
                           ModelMap map){
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> clueDetailMap = new HashMap<String, Object>();
        StringBuffer msgWrong = new StringBuffer("");
        
        if (clueId > 0) {
        	Map<String, Object> params = new HashMap<String, Object>();
        	params.put("userOrgCode", userInfo.getOrgCode());
        	params.put("isCheckEncrypt", true);
        	params.put("encryptUserId", userInfo.getUserId());
        	params.put("encryptOrgId", userInfo.getOrgId());
        	params.put("isCapGangInfo", true);
        	params.put("isCapInformantInfo", true);
        	params.put("isCapReportedInfo", true);
        	params.put("isCapDisposeUnit", true);
        	
        	try {
				clueDetailMap = clueService.findByClueId(clueId, params);
			} catch (Exception e) {
				msgWrong.append(e.getMessage());
				e.printStackTrace();
			}
        }
        
        if(CommonFunctions.isBlank(clueDetailMap, "clue")) {
        	clueDetailMap.put("clue", new EventSBREClue());
        } else {
        	try {
    			map.addAllAttributes(this.capWorkflowRelativeData(clueId, listType, userInfo));
    		} catch (Exception e) {
    			msgWrong.append(e.getMessage());
    			e.printStackTrace();
    		}
        	
        	map.addAttribute("currentDate", DateUtils.getToday());
        }
        
        if(msgWrong.length() > 0) {
        	map.addAttribute("msgWrong", msgWrong.toString());
        	clueDetailMap.put("clue", new EventSBREClue());
        } else {
        	map.addAllAttributes(clueDetailMap);
        }
        map.addAttribute("attachmentType",ATTACHMENT_TYPE);
        map.addAttribute("listType", listType);

        return "zzgl/sweepBlackRemoveEvil/eventSBREClue/detail_eventSBREClue.ftl";
    }

    /**
     * 跳转已上报线索页面
     * @param session
     * @param params
     * 			involvedPeopleName4Accurate	用于精确查找的举报人员/被举报人员姓名
     * 			involvedPeopleBizType		人员业务类型，09 举报人员；10 被举报人员
     * @param map
     * @return
     */
    @RequestMapping("/toListReportedClue")
    public String toReportedClue(HttpSession session,
                           @RequestParam Map<String, Object> params,
                           ModelMap map) {
    	
    	map.addAllAttributes(params);
    	
    	return "zzgl/sweepBlackRemoveEvil/eventSBREClue/list_eventSBREClue_reported.ftl";
    }
    
    /**
     * 获取线索数量
     * @param session
     * @param params
     * 			involvedPeopleName4Accurate	用于精确查找的举报人员/被举报人员姓名
     * 			involvedPeopleBizType		人员业务类型，09 举报人员；10 被举报人员
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findReportedClueCount",method = RequestMethod.POST)
    public Map<String, Object> findReportedClueCount(HttpSession session,
    		@RequestParam Map<String, Object> params) {
    	UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	int total = clueService.findClueCount(params, userInfo);
    	
    	resultMap.put("total", total);
    	
    	return resultMap;
    }
    
    /**
     *启动线索流程
     * @param session
     * @param params
     * @param clueId  线索id
     * @return
     * */
    @ResponseBody
    @RequestMapping(value = "/startWorkflow4Clue",method = RequestMethod.POST)
    public Map<String,Object> startWorkflow4Clue(HttpSession session,
                                            @RequestParam(value = "clueId")Long clueId,
                                            @RequestParam Map<String,Object> params,
                                            EventSBREClue eventSBREClue) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String,Object> resultMap = new HashMap<>();
        ResultObj resultObj = null;

        try {
            boolean result = this.clueService.startWorkflow4Clue(clueId,userInfo,params);
            resultObj = Msg.OPERATE.getResult(result);
        } catch (Exception e) {
            resultObj = Msg.OPERATE.getResult(e);
            e.printStackTrace();
        }

        resultMap.put("clueId",clueId);
        resultMap.put("success",resultObj.isSuccess());
        resultMap.put("tipMsg",resultObj.getTipMsg());

        return resultMap;
    }

    /**
     * 提交线索
     * @param session
     * @param clueId	线索id
     * @param nextNodeName		下一环节名称
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/subWorkflow4Clue", method = RequestMethod.POST)
    public ResultObj subWorkflow4Clue(HttpSession session,
                                      @RequestParam(value = "formId") Long clueId,
                                      @RequestParam(value = "nextNodeName") String nextNodeName,
                                      @RequestParam Map<String, Object> params) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        ResultObj result = null;

        try {
            boolean flag = this.clueService.subWorkflow4Clue(clueId, nextNodeName, null, userInfo, params);
            result = Msg.OPERATE.getResult(flag);
        } catch (Exception e) {
            result = Msg.OPERATE.getResult(e);
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 驳回上一步
     * @param session
     * @param clueId	线索id
     * @param rejectToNodeName	驳回环节的名称
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/rejectWorkflow4Clue", method = RequestMethod.POST)
    public ResultObj rejectWorkflow4Clue(HttpSession session,
                                         @RequestParam(value = "formId") Long clueId,
                                         @RequestParam(value = "rejectToNodeName", required=false) String rejectToNodeName,
                                         @RequestParam Map<String, Object> params) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        ResultObj result = null;

        try {
            boolean flag = this.clueService.rejectWorkflow4Clue(clueId, rejectToNodeName, userInfo, params);
            result = Msg.OPERATE.getResult(flag);
        } catch (Exception e) {
            result = Msg.OPERATE.getResult(e);
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 判断当前用户是否能够查看指定线索信息
     * @param session
     * @param clueId	线索id
     * @param isEncrypt	是否加密，1 是；0 否
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkAuthority")
    public Map<String, Object> checkAuthority(HttpSession session,
    							@RequestParam(value = "clueId") Long clueId,
    							@RequestParam(value = "isEncrypt", required = false) String isEncrypt){
        //获取当前登录用户信息
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> params = new HashMap<String, Object>();
        String NO_NEED_ENCRYPT = "0";//没有加密
        //如果是没有加密的，则直接跳过，无需进行权限验证
        boolean result = StringUtils.isNotBlank(isEncrypt) && NO_NEED_ENCRYPT.equals(isEncrypt);
        
        if(!result && clueId != null && clueId > 0){
            try {
				result = clueService.checkAuthority(clueId, userInfo);
			} catch (Exception e) {
				params.put("msgWrong", e.getMessage());
				e.printStackTrace();
			}
        }
        
        params.put("result", result);

        return params;
    }
    
    /**
	 * 获取线索相关工作流信息
	 * @param clueId	线索id
	 * @param listType	列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 辖区所有
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> capWorkflowRelativeData(Long clueId, Integer listType, UserInfo userInfo) throws Exception {
		Map<String, Object> curTaskData = null,
							resultData = new HashMap<String, Object>();
		Long taskId = -1L;
		
		Long instanceId = clueService.capInstanceId(clueId, userInfo);
		
		if(instanceId > 0) {
			curTaskData = clueService.findCurTaskData(clueId, userInfo);
			
			if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
				taskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
			}
			
			List<Map<String, Object>> participantMapList = clueService.findParticipantsByTaskId(taskId);
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
				
				resultData.put("taskPersonStr", taskPersonStr.substring(1));
				
				if(listType == 2) {//待办
					resultData.put("isCurHandler", clueService.isCurTaskPaticipant(participantMapList, userInfo, null));
					
					if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
						resultData.put("curNodeName", curTaskData.get("NODE_NAME"));
					}
					if(CommonFunctions.isNotBlank(curTaskData, "NODE_ID")) {
						Integer nodeId = Integer.valueOf(curTaskData.get("NODE_ID").toString());
						resultData.put("operateList", clueService.findOperateByNodeId(nodeId));
					}
					
					resultData.put("nextTaskNodes", clueService.findNextTaskNodes(clueId, userInfo, null));
				}
			}
			
			if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
				resultData.put("curTaskName", curTaskData.get("WF_ACTIVITY_NAME_"));
			}
			
			resultData.put("formId", clueId);
			resultData.put("formType", FORM_TYPE);
			resultData.put("instanceId", instanceId);
		}
		
		return resultData;
	}
	
	/**
	 * 导出线索台账
	 * @param session
	 * @param request
	 * @param response
	 * @param params
	 * @param exportName
	 * @throws Exception
	 */
	@RequestMapping(value = "/toExport")
	public void toExport(HttpSession session,
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam Map<String, Object> params,
			@RequestParam(value = "exportName") String exportName
			
			) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(exportName, "UTF-8") + ".xls");
		PrintWriter out = response.getWriter();
		Template t = null;
		StringWriter writer = null;
		try {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			params.put("startInfoOrgCode", this.getDefaultInfoOrgCode(session));
			params.put("userOrgCode", userInfo.getOrgCode());
			params.put("listType", 4);//辖区所有
			List<Map<String, Object>> list= clueService.findEventSBREClueList(params,userInfo);
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("list", list);
			FreeMarkerConfigurer freemarkerConfig = SpringContextUtil.getApplicationContext().getBean(FreeMarkerConfigurer.class);
			Configuration configuration = freemarkerConfig.getConfiguration();
			t = configuration.getTemplate("zzgl/sweepBlackRemoveEvil/eventSBREClue/export_eventSBREClue.ftl");
			writer = new StringWriter(5 * 1024);
			t.process(dataMap, writer);
			StringBuffer sb = writer.getBuffer();
			out.write(sb.toString());
			out.flush();
		} finally {
			if(writer!=null)
			writer.close();
			out.close();
		}
	}

}
