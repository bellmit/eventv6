package cn.ffcs.zhsq.peopleLivelihoodInfo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.file.service.IAttachmentByUUIDService;
import cn.ffcs.file.service.IAttachmentService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.peopleLivelihoodInfo.service.IPeopleLivelihoodInfoService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * @Description: 民生信息模块控制层
 * @Author: youwj
 * @Date: 09-01 15:32:03
 * @Copyright: 2020 福富软件
 */
@Controller
@RequestMapping(value = "/zhsq/peopleLivelihood")
public class PeopleLivelihoodInfoController extends ZZBaseController{
	
	
	//功能配置
	@Autowired
	private IFunConfigurationService configurationService;
	
	@Autowired
	private IPeopleLivelihoodInfoService peopleLivelihoodInfoService;
	
	@Autowired
	private IAttachmentService attachmentService;
	
	
	private final String FORM_TYPE = "people_livelihood";//表单类型

	/**
	 * 跳转列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map
			,@RequestParam Map<String,Object> params) {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.putAll(params);
		MixedGridInfo grid = mixedGridInfoService.getDefaultGridByOrgCode(userInfo.getInfoOrgCodeStr());
		
		//设置网格树展示位置
		String treStyle = null;
		
		if(CommonFunctions.isNotBlank(params, "treStyle")) {
			treStyle = params.get("treStyle").toString();
		} else {
			treStyle = configurationService.changeCodeToValue(
					ConstantValue.GRID_CONFIGURATION, ConstantValue.GRID, IFunConfigurationService.CFG_TYPE_FACT_VAL,
					userInfo.getOrgCode());
		}
		map.addAttribute("treStyle",treStyle);
		
		map.addAttribute("startGridId", grid.getGridId());
		map.addAttribute("gridCode",grid.getGridCode());
		map.addAttribute("gridName",grid.getGridName());

		
		map.addAttribute("infoOrgCode", userInfo.getInfoOrgCodeStr());
		map.addAttribute("orgCode", userInfo.getOrgCode());
		map.addAttribute("orgId", userInfo.getOrgId());
		
		return "/zzgl/peopleLivelihood/list_people_livelihood.ftl";
	}

	
	
	
	/**
	 * 获取列表数据
	 */
	@RequestMapping("/listData")
	@ResponseBody
	public EUDGPagination listData(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam Map<String,Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(CommonFunctions.isBlank(params, "orgCode")) {
			params.put("orgCode", userInfo.getOrgCode());
		}
		if(CommonFunctions.isBlank(params, "userId")) {
			params.put("userId", userInfo.getUserId());
		}
		if(CommonFunctions.isBlank(params, "orgId")) {
			params.put("orgId", userInfo.getOrgId());
		}
		
		EUDGPagination searchList=new EUDGPagination();
		try {
			searchList = peopleLivelihoodInfoService.searchList(page, rows, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return searchList;
	}
	
	
	
	
	/**
	 * 跳转新增/编辑页面
	 */
	@RequestMapping("/toAddPage")
	public Object toAddPage(HttpServletRequest request, HttpSession session, ModelMap map
			,@RequestParam Map<String,Object> params) {
		
		map.addAttribute("params", params);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		if(CommonFunctions.isNotBlank(params, "infoId")) {
			Map<String, Object> peopleLivelihood = peopleLivelihoodInfoService.searchById(params.get("infoId").toString(), userInfo.getOrgCode());
			if(peopleLivelihood!=null) {
				map.addAllAttributes(peopleLivelihood);
			}
			return "/zzgl/peopleLivelihood/edit_people_livelihood.ftl";
		}else {
			
			//设置初始网格区域
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);

			map.addAttribute("gridId",defaultGridInfo.get(KEY_START_GRID_ID).toString());
			map.addAttribute("gridName",defaultGridInfo.get(KEY_START_GRID_NAME).toString());
			map.addAttribute("infoOrgCode",defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString());
			return "/zzgl/peopleLivelihood/add_people_livelihood.ftl";
		}
		
	}
	
	/**
	 * 跳转详情页面
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toDetailPage")
	public String toDetailPage(HttpSession session, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		
		map.addAttribute("params", params);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> peopleLivelihood = peopleLivelihoodInfoService.searchById(params.get("infoId").toString(), userInfo.getOrgCode());
		if(peopleLivelihood!=null) {
			map.addAllAttributes(peopleLivelihood);
		}
		
		return "/zzgl/peopleLivelihood/detail_people_livelihood.ftl";
	}
	
	/**
	 * 跳转带有工作流信息的详情页面
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toWorkflowDetailPage")
	public String toWorkflowDetailPage(HttpSession session, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		
		map.addAttribute("params", params);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> peopleLivelihood = peopleLivelihoodInfoService.searchById(params.get("infoId").toString(), userInfo.getOrgCode());
		if(peopleLivelihood!=null) {
			map.addAllAttributes(peopleLivelihood);
		}
		
		try {
			map.addAllAttributes(this.capWorkflowRelativeData(params.get("infoId").toString(), Integer.valueOf(params.get("listType").toString()), userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(CommonFunctions.isNotBlank(params,"listType")
				&&params.get("listType").toString().equals("2")) {//待办详情页面
			
		}
		
		
		return "/zzgl/peopleLivelihood/detail_people_livelihood_workflow.ftl";
	}
	
	
	/**
	 * 新增(修改)民生信息
	 * @param session
	 * @param dictPcode
	 * @param orgCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdateInfo")
	public Map<String,Object> saveOrUpdateInfo(
			HttpSession session,
			@RequestParam Map<String, Object> params,
			@RequestParam(value = "attachmentId", required = false) Long[] ids) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String,Object> result =new HashMap<String,Object>();
		
		Map<String, Object> saveOrUpdate = peopleLivelihoodInfoService.saveOrUpdate(params, userInfo);
		try {
			if(CommonFunctions.isNotBlank(saveOrUpdate, "operationResult")) {
				if(!saveOrUpdate.get("operationResult").toString().equals("false")) {
					if(ids!=null&&ids.length>0) {
						
						String uuid = saveOrUpdate.get("operationResult").toString();
						
						Map<String, Object> searchById = peopleLivelihoodInfoService.searchById(uuid, userInfo.getOrgCode());
						if(CommonFunctions.isNotBlank(searchById, "infoSeqId")) {
							attachmentService.updateBizId(Long.valueOf(searchById.get("infoSeqId").toString()), "PEOPLE_LIVE", ids);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		result.put("result", saveOrUpdate);
		return result;
	}
	
	
	
	/**
	 * 删除民生信息
	 * @param session
	 * @param dictPcode
	 * @param orgCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteInfo")
	public Map<String,Object> deleteInfo(
			HttpSession session,
			@RequestParam Map<String, Object> params) {
		Map<String,Object> result=new HashMap<String,Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		params.put("updator", userInfo.getUserId());
		Boolean delete = peopleLivelihoodInfoService.delete(params);
		result.put("result", delete);
		return result;
	}
	
	
	/**工作流相关部分控制层start*/
	
	
	/**
	 * 删除民生信息
	 * @param session
	 * @param dictPcode
	 * @param orgCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/startWorkflow")
	public Map<String,Object> startWorkflow(
			HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        Integer resultCode=0;
        String resultMsg="";
        Long infoSeqId=-1L;
        
        if(CommonFunctions.isNotBlank(params, "infoId")) {
        	Map<String, Object> searchById = peopleLivelihoodInfoService.searchById(params.get("infoId").toString(), userInfo.getOrgCode());
        	if(CommonFunctions.isNotBlank(searchById, "infoSeqId")) {
        		infoSeqId=Long.valueOf(searchById.get("infoSeqId").toString());
        	}else {
        		resultMsg="民生信息不完整。";
        	}
        }else {
        	resultMsg="民生信息不完整。";
        }
        //如果下一环节是决策节点，那么进行自动流转
        if(StringUtils.isBlank(resultMsg)) {
        	
        	try {
        		Long result = peopleLivelihoodInfoService.startWorkflow(infoSeqId,userInfo,params);
        		resultMap.put("instanceId", result);
        		resultCode=result>0?1:0;
        	} catch (Exception e) {
        		resultMsg=e.getMessage();
        		e.printStackTrace();
        	}
        	
        }
        resultMap.put("code", resultCode);
        resultMap.put("msg", resultMsg);

        return resultMap;
	}
	
	
	
	
	 /**
     * 提交线索
     * @param session
     * @param infoId	信息id
     * @param nextNodeName		下一环节名称
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/subWorkflow")
    public Map<String,Object> subWorkflow(HttpSession session,
    		@RequestParam(value = "attachmentId", required = false) Long[] ids,
            @RequestParam(value = "nextNodeName") String nextNodeName,
            @RequestParam Map<String, Object> params) {
    	UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        Integer resultCode=0;
        String resultMsg="";
        Long infoSeqId=-1L;
        
        if(CommonFunctions.isNotBlank(params, "infoId")) {
        	Map<String, Object> searchById = peopleLivelihoodInfoService.searchById(params.get("infoId").toString(), userInfo.getOrgCode());
        	if(CommonFunctions.isNotBlank(searchById, "infoSeqId")) {
        		infoSeqId=Long.valueOf(searchById.get("infoSeqId").toString());
        	}else {
        		resultMsg="民生信息不完整。";
        	}
        }else {
        	resultMsg="民生信息不完整。";
        }
        
		
		try {
			attachmentService.updateBizId(infoSeqId, "PEOPLE_LIVE", ids);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        
        if(StringUtils.isBlank(resultMsg)) {

	        try {
	            boolean flag = peopleLivelihoodInfoService.subWorkflow(infoSeqId, nextNodeName, null, userInfo, params);
	            resultCode=flag?1:0;
	            
	        } catch (Exception e) {
	        	resultMsg=e.getMessage();
	            e.printStackTrace();
	        }
        }
        resultMap.put("code", resultCode);
        resultMap.put("msg", resultMsg);

        return resultMap;
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
    @RequestMapping(value = "/rejectWorkflow4Clue")
    public Map<String,Object> rejectWorkflow4Clue(HttpSession session,
                                         @RequestParam(value = "rejectToNodeName", required=false) String rejectToNodeName,
                                         @RequestParam Map<String, Object> params) {
    	UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        Integer resultCode=0;
        String resultMsg="";
        Long infoSeqId=-1L;
        
        if(CommonFunctions.isNotBlank(params, "infoId")) {
        	Map<String, Object> searchById = peopleLivelihoodInfoService.searchById(params.get("infoId").toString(), userInfo.getOrgCode());
        	if(CommonFunctions.isNotBlank(searchById, "infoSeqId")) {
        		infoSeqId=Long.valueOf(searchById.get("infoSeqId").toString());
        	}else {
        		resultMsg="民生信息不完整。";
        	}
        }else {
        	resultMsg="民生信息不完整。";
        }
        
        if(StringUtils.isBlank(resultMsg)) {
        	
        	try {
        		boolean flag = peopleLivelihoodInfoService.rejectWorkflow4Clue(infoSeqId, rejectToNodeName, userInfo, params);
        		resultCode=flag?1:0;
        	} catch (Exception e) {
        		resultMsg=e.getMessage();
        		e.printStackTrace();
        	}
        }


        resultMap.put("code", resultCode);
        resultMap.put("msg", resultMsg);

        return resultMap;
    }
    
    
    
    /**
	 * 选择办理环节后，获取办理人员信息
	 * @param session
	 * @param request
	 * @param curnodeName	当前节点名称
	 * @param nodeName		选中的环节名称
	 * @param nodeCode		环节编码，如U6R1U5
	 * @param nodeId		选中的节点id
	 * @param eventId		事件id
	 * @param params
	 * 			formId		表单id
	 * 			formType	表单类型
	 * 			instanceId	流程实例id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getNodeInfo")
	public Map<String, Object> getNodeInfo(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "curnodeName") String curnodeName,
			@RequestParam(value = "nodeName") String nodeName,
			@RequestParam(value = "nodeCode") String nodeCode,
			@RequestParam(value = "nodeId") String nodeId,
			@RequestParam(value = "eventId", required = false) Long eventId,
			@RequestParam Map<String, Object> params) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		resultMap = peopleLivelihoodInfoService.getNodeInfo(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		
		return resultMap;
	}
	
	
    /**
	 * 获取民生信息相关工作流信息
	 * @param infoId	信息id
	 * @param listType	列表类型
	 * 			1 草稿采集
	 * 			2 待办
	 * 			3 经办
	 * 			4 辖区所有
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> capWorkflowRelativeData(String infoId, Integer listType, UserInfo userInfo) throws Exception {
		
		return peopleLivelihoodInfoService.capWorkflowRelativeData(infoId, listType, userInfo);
	}
	
	
	/**工作流相关部分控制层end*/


}