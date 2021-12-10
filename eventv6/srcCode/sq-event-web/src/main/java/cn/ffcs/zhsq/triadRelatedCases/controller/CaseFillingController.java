package cn.ffcs.zhsq.triadRelatedCases.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import cn.ffcs.shequ.commons.util.StringUtils;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.shequ.web.ParamUtils;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.mybatis.domain.common.LayuiPage;

import cn.ffcs.zhsq.mybatis.domain.triadRelatedCases.CaseFilling;
import cn.ffcs.zhsq.mybatis.domain.triadRelatedCases.TrialRecord;
import cn.ffcs.zhsq.triadRelatedCases.ICaseFillingService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.ResultObj;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.ffcs.system.publicUtil.EUDGPagination;


/**   
 * @Description: 涉黑案件填报表模块控制器
 * @Author: chenshikai
 * @Date: 09-08 17:46:05
 * @Copyright: 2021 福富软件
 */ 
@Controller("caseFillingController")
@RequestMapping("/zhsq/caseFilling")
public class CaseFillingController {

	@Autowired
	private ICaseFillingService caseFillingService; //注入涉黑案件填报表模块服务
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;

	private static final String LIST_TYPE = "01";
	private static final String DETAIL_TYPE = "01";//涉黑案件大屏模式
	
	/**
	 * 列表页面
	 */

	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		putListPageParam(map,session);
		map.put("moduleName","涉黑案件上报");
		String listType = ParamUtils.getString(request,"listType","");
		if(LIST_TYPE.equals(listType)){
			return "/zzgl/triadRelatedCases/province_case_list.ftl";
		}
		return "/zzgl/triadRelatedCases/case_list.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request,CaseFilling bo,
						   LayuiPage page) {
		EUDGPagination pagination = null;
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute(ConstantValue.USER_IN_SESSION);
		String listType = ParamUtils.getString(request,"listType","");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("filingTime",bo.getFilingTime());
		params.put("caseName",bo.getCaseName());
		params.put("fillStatus",bo.getFillStatus());
		params.put("involvedNature",bo.getInvolvedNature());
		params.put("gridCode",bo.getGridCode());
		if(LIST_TYPE.equals(listType)){
			//省级查看各地提交的内容
			params.put("fillStatus","01");
		}else {
			//非省级根据组织查看
			params.put("orgCode",userInfo.getOrgCode());
			params.put("creator",userInfo.getUserId());
		}
		try {
			pagination = caseFillingService.searchList(page,params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		String  undCaseUuid) {
		String type = ParamUtils.getString(request,"detailType","");
		map.put("detailType", type);
		putListPageParam(map,session);
		CaseFilling bo = caseFillingService.searchById(undCaseUuid);
		List<TrialRecord> trialRecords = caseFillingService.searchListById(undCaseUuid);
		bo.setTrialRecords(trialRecords);
		map.put("bo", bo);
		return "/zzgl/triadRelatedCases/case_view.ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpSession session, ModelMap map,
		String undCaseUuid) {
		putListPageParam(map,session);
		map.put("moduleName","新增涉黑案件");
		if (undCaseUuid != null) {
			CaseFilling bo = caseFillingService.searchById(undCaseUuid);
			List<TrialRecord> trialRecords = caseFillingService.searchListById(undCaseUuid);
			bo.setTrialRecords(trialRecords);
			map.put("bo", bo);
			map.put("moduleName","编辑涉黑案件");
		}
		return "/zzgl/triadRelatedCases/case_add_list.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session,
		CaseFilling bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj obj = new ResultObj(null,null,false);
		//设置创建人
		bo.setCreator(userInfo.getUserId());
		//设置更新人
		bo.setUpdator(userInfo.getUserId());
		//组织编码
		bo.setOrgCode(userInfo.getOrgCode());
		obj.setSuccess(caseFillingService.insertOrUpdate(bo));
		return obj;
	}

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
					  String ids) {
		ResultObj obj = new ResultObj(null,null,false);
		List<String>undCaseUuidList = Arrays.asList(ids.split(","));
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		obj.setSuccess(caseFillingService.delete(userInfo.getUserId(),undCaseUuidList));
		return obj;
	}


	/**
	 * 提交数据回收
	 */
	@ResponseBody
	@RequestMapping("/recovery")
	public Object recovery(HttpServletRequest request, HttpSession session, ModelMap map,
					  Long id) {
		ResultObj obj = new ResultObj(null,null,false);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		CaseFilling bo = new CaseFilling();
		bo.setUpdator(userInfo.getUserId());
		bo.setUndCaseId(id);
		bo.setFillStatus("00");
		obj.setSuccess(caseFillingService.update(bo));
		return obj;
	}


	/**
	 * 列表页面通用参数赋值
	 * @param map
	 * @param userInfo
	 */
	private void putListPageParam(ModelMap map, HttpSession session){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.put("regionCode",userInfo.getInfoOrgList().get(0).getOrgCode());
		map.put("regionName",userInfo.getInfoOrgList().get(0).getOrgName());
		map.put("involvedNatureDict",ConstantValue.INVOLVED_NATURE);
		map.put("trialStatusDict",ConstantValue.TRIAL_STATUS);
		List<BaseDataDict> indusCodeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.ELIMINATE_LETTER_THO_INDUS,null);
		map.put("indusCodeDictList", JSONObject.toJSONString(indusCodeDictList));
		map.put("componentsDomain", App.COMPONENTS.getDomain(session));
		map.put("skyDomain",App.SKY.getDomain(session));
		map.put("uiDomain",App.UI.getDomain(session));
		map.put("imgDomain", App.IMG.getDomain(session));
		map.put("fileDomain", App.SQFILE.getDomain(session));
		map.put("creator",userInfo.getUserId());
	}


	/**
	 * 涉黑案件大屏jsonp接口
	 */
	@ResponseBody
	@RequestMapping(value = "/triadRelatedJsonp")
	public String triadRelatedJsonp(HttpSession session,
								  @RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String jsonpcallback = "";
		if(StringUtils.isBlank((String) params.get("gridID"))) {
			//没有传则默认取当前登入者的账号信息.
			params.put("gridID",userInfo.getInfoOrgList().get(0).getOrgId());
		}
		List<Map<String, Object>> resultMap = caseFillingService.getJsnpData(params);
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}

}