package cn.ffcs.zhsq.courtsynergism.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.ffcs.system.publicUtil.StringUtils;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.courtsynergism.service.ICourtSynergismService;
import cn.ffcs.zhsq.mybatis.domain.courtsynergism.CourtSynergism;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.message.ResultObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.ffcs.system.publicUtil.EUDGPagination;

/**   
 * @Description: 法院协同办公模块控制器
 * @Author: zhangch
 * @Date: 05-20 11:01:56
 * @Copyright: 2020 福富软件
 */ 
@Controller("courtSynergismController")
@RequestMapping("/zhsq/courtSynergism")
public class CourtSynergismController {

	@Autowired
	//注入法院协同办公模块服务
	private ICourtSynergismService courtSynergismService;
	// 新字典服务
	@Autowired
	private IBaseDictionaryService baseDictionaryService;

	@Autowired
	private IBaseWorkflowService baseWorkflowService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {

		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");
		List<BaseDataDict> applyTypeDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.APPLY_TYPE_DICTCODE,userInfo.getOrgCode());
		List<BaseDataDict> satisfDict= baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SATISFACTION_DICTCODE,userInfo.getOrgCode());
		Map<String,String> statusMap=  new HashMap<String, String>(){
			{
				put("01", "草稿");
				put("02", "办理中");
				put("03", "待评价");
				put("04", "结束");
			}
		};
		map.put("applyTypeDict",applyTypeDict.stream().collect(Collectors.toMap(BaseDataDict::getDictGeneralCode, BaseDataDict::getDictName)));
		map.put("satisfDict",satisfDict.stream().collect(Collectors.toMap(BaseDataDict::getDictGeneralCode, BaseDataDict::getDictName)));
		map.put("statusMap",statusMap);
		map.put("menuType",	request.getParameter("menuType"));
		return "/zzgl/courtSynergism/court_synergism_list.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,
		int page, int rows,CourtSynergism bo) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
        UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");
        bo.setCreator(userInfo.getUserId());
        bo.setCurOrgId(userInfo.getOrgId());
        bo.setCurUserId(userInfo.getUserId());
        EUDGPagination pagination = courtSynergismService.searchList(page, rows, bo);
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/view")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		CourtSynergism bo = courtSynergismService.searchById(id,null);
		map.addAttribute("bo", bo);
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");
		Long iId = courtSynergismService.capInstanceId(bo,userInfo);
		map.addAttribute("instanceId", iId);
		return "/zzgl/courtSynergism/court_synergism_view.ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");
		CourtSynergism bo = courtSynergismService.searchById(id,userInfo);
		map.put("bo", bo);
		return "/zzgl/courtSynergism/court_synergism_form.ftl";
	}

	/**
	 * 办理页面
	 */
	@RequestMapping("/handle")
	public Object handle(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");
		CourtSynergism bo = courtSynergismService.searchById(id,userInfo);
		bo.setCurUserName(userInfo.getPartyName());
		map.put("bo", bo);
		Map<String, Object> now = null;
		List<Node> nodes = null;
		try {
			now = courtSynergismService.getCurNode(id,userInfo);
			nodes = courtSynergismService.findNextTaskNodes(id,userInfo);
		} catch (Exception e) {
			logger.error("获取工作流节点信息异常！",e);
		}
		map.put("nowNode", now);
		map.put("nextNode", nodes.get(0));
		return "/zzgl/courtSynergism/court_synergism_handle.ftl";
	}

	/**
	 * 发起保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		CourtSynergism bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");
		ResultObj resultObj = new ResultObj("保存成功!",null);
		String submit = request.getParameter("saveType");
		//新增
		boolean success = false;
		bo.setStatus("01");
		if (bo.getSynergismId() == null) {
			bo.setCreator(userInfo.getUserId());
			bo.setCreatorName(userInfo.getPartyName());
			bo.setOrgCode(userInfo.getOrgCode());
			Long id = courtSynergismService.insert(bo);
			if (id != null && id > 0) {
				success = true;
			}
		} else { //修改
			bo.setUpdator(userInfo.getUserId());
			boolean updateResult = courtSynergismService.update(bo);
			if (updateResult) {
				success = true;
			}
		}
		if(success && !StringUtils.isBlank(submit)){
			int workType = courtSynergismService.startWorkFlow(bo,userInfo);
			if(workType != 1){
				success = false;
			}else{
				CourtSynergism x = new CourtSynergism();
				x.setStatus("02");
                x.setSynergismId(bo.getSynergismId());
				courtSynergismService.update(x);
			}
			resultObj.setType(String.valueOf(workType));
		}
		resultObj.setSuccess(success);
		return resultObj;
	}

	/**
	 * 提交数据
	 */
	@ResponseBody
	@RequestMapping("/submit")
	public Object submit(HttpServletRequest request, HttpSession session, ModelMap map,
						 CourtSynergism bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");
		ResultObj resultObj = new ResultObj("保存成功!",null);
		//新增
		int workType = courtSynergismService.submit(bo,userInfo);
		if(workType != 1){
			resultObj.setSuccess(false);
			resultObj.setType(String.valueOf(workType));
			resultObj.setTipMsg("保存失败！");
		}
		return resultObj;
	}

	/**
	 * 驳回数据
	 */
	@ResponseBody
	@RequestMapping("/reject")
	public Object reject(HttpServletRequest request, HttpSession session, ModelMap map,
		CourtSynergism bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");
		ResultObj resultObj = new ResultObj("驳回成功!",null);
		//驳回
		int workType = courtSynergismService.reject(bo,userInfo);
		if(workType != 1){
			resultObj.setSuccess(false);
			resultObj.setType(String.valueOf(workType));
			resultObj.setTipMsg("驳回失败！");
		}
		return resultObj;
	}

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		CourtSynergism bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");
		//设置更新人
		bo.setUpdator(userInfo.getUserId());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = courtSynergismService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}

}