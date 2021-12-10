package cn.ffcs.zhsq.szzg.hydropowerCoal.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.utils.StringUtils;
import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.szzg.hydropowerCoal.HydropowerCoal;
import cn.ffcs.zhsq.szzg.hydropowerCoal.IHydropowerCoalService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;
import freemarker.core.ParseException;

/**   
 * @Description: 居民水电煤模块控制器
 * @Author: 林祝
 * @Date: 07-21 15:39:18
 * @Copyright: 2017 福富软件
 */ 
@Controller
@RequestMapping(value = "/zhsq/szzg/hydropowerCoal")
public class HydropowerCoalController extends ZZBaseController{

	@Autowired
	private IHydropowerCoalService hydropowerCoalService; //注入hydropowerCoal模块服务
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;
	
	// 模块路径
	private final static String REAL_PATH = "/szzg/";
	// 模块名称
	private final static String SUB_MAIN = "hydropowerCoal";
		  
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpSession session, ModelMap map) {
		Map<String, Object> defaultOrgInfo = this.getDefaultOrgInfo(session);
		//设置默认值
		String defaultOrgCode = defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		String defaultOrgName = defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_NAME).toString();
		map.put("orgCode", defaultOrgCode);
		map.put("orgName", defaultOrgName);
		String type=request.getParameter("type");
		if(StringUtils.isEmpty(type)){
			type="1";
		}
		map.put("type", type);
		String syear=request.getParameter("syear");
		if(StringUtils.isEmpty(syear)){
			syear=DateUtils.getToday("yyyy");
		}
		map.put("syear", syear);
		return REAL_PATH + SUB_MAIN + "/list_" + SUB_MAIN + ".ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public EUDGPagination listData(HttpServletRequest request, HttpSession session, ModelMap map,
		int page, int rows) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("syear", request.getParameter("syear"));
		params.put("orgCode", request.getParameter("orgCode"));
		params.put("type", request.getParameter("type"));
		params.put("status", request.getParameter("status"));
		EUDGPagination pagination = hydropowerCoalService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public String view(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		HydropowerCoal bo = hydropowerCoalService.searchById(id);
		map.addAttribute("bo", bo);
		return REAL_PATH + SUB_MAIN + "/detail_" + SUB_MAIN + ".ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/add")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		HydropowerCoal bo;
		if (id != null) {
			 bo = hydropowerCoalService.searchById(id);
		}else{
			 bo =new HydropowerCoal();
			 bo.setSyear(DateUtils.formatDate(new Date(), "yyyyMM"));
		}
		map.put("bo", bo);
		return REAL_PATH + SUB_MAIN + "/add_" + SUB_MAIN + ".ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public ResultObj saveOrUpdate(HttpSession session, ModelMap map,
			@ModelAttribute(value = "record") HydropowerCoal hydropowerCoal) throws ParseException {
		// 获取当前用户信息
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj rs=null;
		if (hydropowerCoal.getHydropowerCoalId() != null
				&& hydropowerCoal.getHydropowerCoalId() > 0L) {// 更新
			hydropowerCoal.setUpdateUserId(userInfo.getUserId());
			try {
				HydropowerCoal oldHydropowerCoal=hydropowerCoalService.searchById(hydropowerCoal.getHydropowerCoalId());
				if(!oldHydropowerCoal.getOrgCode().equals(hydropowerCoal.getOrgCode())){
					Map<String, Object> params=new HashMap<String, Object>();
					params.put("syear", hydropowerCoal.getSyear());
					params.put("orgCode", hydropowerCoal.getOrgCode());
					params.put("type", hydropowerCoal.getType());
					if(hydropowerCoalService.findByParams(params)){
						return Msg.EDIT.getResult(false, "对应记录已存在");
					}
				}
				rs= Msg.EDIT.getResult(hydropowerCoalService.update(hydropowerCoal));
			} catch (Exception e) {
				rs= Msg.EDIT.getResult(false, e.getMessage());
			}
		} else {// 保存
			
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("syear", hydropowerCoal.getSyear());
			params.put("orgCode", hydropowerCoal.getOrgCode());
			params.put("type", hydropowerCoal.getType());
			if(!hydropowerCoalService.findByParams(params)){
				hydropowerCoal.setCreateUserId(userInfo.getUserId());
				rs= Msg.ADD.getResult(hydropowerCoalService.insert(hydropowerCoal));
			}else{
				rs= Msg.ADD.getResult(false, "对应记录已存在");
			}
		}
		return rs;
	}

	/**
	 * 点击删除(批量删除)
	 * 
	 * @param session
	 * @param requese
	 * @param idStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
	public Map<String, Object> batchDelete(HttpSession session,
			@RequestParam(value = "idStr") String idStr) {
		// 获取用户信息
		UserInfo userInfo = (UserInfo) session
				.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(idStr)) {
			String[] ids = idStr.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (int i = 0; i < ids.length; i++) {
				Long dispId = Long.parseLong(ids[i]);
				idList.add(dispId);
			}
			boolean result = hydropowerCoalService.deleteByIds(userInfo.getUserId(), idList);
			resultMap.put("result", result ? idList.size() : 0L);
		} else {
			resultMap.put("result", "缺少参数[idStr]，请检查！");
		}
		return resultMap;
	}
	
	
	   /**
	  * 首页
	  *
	  * @param map
	  * @return
	  */
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 @RequestMapping(value = "/report")
	 public String report(HttpServletRequest request,HttpSession session, ModelMap model)throws Exception {
		String syear=request.getParameter("syear");
		if(StringUtils.isEmpty(syear)){
			syear=DateUtils.getToday("yyyy");
		}
		String type=request.getParameter("type");
		if(StringUtils.isEmpty(type)){
			type="1";
		}
		String orgCode=  request.getParameter("orgCode");
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		if(StringUtils.isEmpty(orgCode)){
			orgCode=infoOrgCode;
		}
		model.put("orgCode", orgCode);
		model.addAttribute("curYear", syear);
        //下级组织列表
		List<OrgEntityInfoBO> orgEntityInfos= orgEntityInfoOutService.findOrgListByParentId( orgEntityInfoOutService.selectOrgEntityInfoByOrgCode(orgCode).getOrgId());
		model.addAttribute("orgEntityInfos",orgEntityInfos);
		Enumeration enu = request.getParameterNames();
			while (enu.hasMoreElements()) {
				String paraName = (String) enu.nextElement();
				model.addAttribute(paraName,request.getParameter(paraName));
		}
	 	return REAL_PATH + SUB_MAIN + "/report_" + SUB_MAIN + ".ftl";
	 } 
	 
	    /**
	     * 
	     * @param request
	     * @param session
	     * @param model
	     * @return
	     * @throws Exception
	     */
	    @ResponseBody
	    @RequestMapping(value = "/jsonData")
	    public Map<String, Object> jsonData(HttpServletRequest request,HttpSession session, ModelMap model)throws Exception {
	    	Map<String, Object> map =new HashMap<String, Object>();
	    	String syear=request.getParameter("syear");
			if(StringUtils.isEmpty(syear)){
				syear=DateUtils.getToday("yyyy");
			}
			String type=request.getParameter("type");
			if(StringUtils.isEmpty(type)){
				type="1";
			}
			String orgCode=  request.getParameter("orgCode");
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
			if(StringUtils.isEmpty(orgCode)){
				orgCode=infoOrgCode;
			}
			map.put("curYear", syear);
			String  lastYear=(Integer.valueOf(syear)-1)+"";
			map.put("lastYear",lastYear);
			map.put("curList",hydropowerCoalService.getUsageDataByYear(orgCode,syear,type));
			map.put("lastList",hydropowerCoalService.getUsageDataByYear(orgCode,lastYear,type));
			Enumeration enu = request.getParameterNames();
			while (enu.hasMoreElements()) {
				String paraName = (String) enu.nextElement();
				model.addAttribute(paraName,request.getParameter(paraName));
			}
	    	return map;
	    }
	 
	
/*	
	@ResponseBody
	@RequestMapping("/listDataByYear")
	public Map<String, Object> listDataByYear(HttpServletRequest request, HttpSession session, ModelMap map) throws Exception {
		Map<String, Object> pmap=new HashMap<String, Object>();
		String syear=request.getParameter("syear");
		
		if(StringUtils.isEmpty(syear)){
			syear=DateUtils.getToday("yyyy");
		}
		pmap.put("curYear", syear);
		String type=request.getParameter("type");
		if(StringUtils.isEmpty(syear)){
			type="1";
		}
		pmap.put("type", type);
		String orgCode=  request.getParameter("orgCode");
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		if(StringUtils.isEmpty(orgCode)){
			orgCode=infoOrgCode;
		}
		pmap.put("orgCode", orgCode);
		
		String  lastYear=(Integer.valueOf(syear)-1)+"";
		pmap.put("lastYear",lastYear);
		pmap.put("curList",hydropowerCoalService.getUsageDataByYear(orgCode,syear,type));
		pmap.put("lastList",hydropowerCoalService.getUsageDataByYear(orgCode,lastYear,type));
		return pmap;
	}*/

}