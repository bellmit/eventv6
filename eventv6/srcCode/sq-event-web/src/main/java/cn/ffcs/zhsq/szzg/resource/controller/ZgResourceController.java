package cn.ffcs.zhsq.szzg.resource.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.resource.ZgResourceType;
import cn.ffcs.zhsq.szzg.resource.service.ZgResourceInfoService;
import cn.ffcs.zhsq.szzg.resource.service.ZgResourceTypeService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;

/**   
 * @Description: 资源表模块控制器
 * @Author: huangwenbin
 * @Date: 08-04 17:56:37
 * @Copyright: 2017 福富软件
 */ 
@Controller
@RequestMapping("/zhsq/szzg/zgResourceController")
public class ZgResourceController {

	@Autowired
	private ZgResourceTypeService zgResourceTypeService;
	@Autowired
	private ZgResourceInfoService zgResourceInfoService;
	
	@RequestMapping(value ="/page")
	public String page(HttpServletRequest request, HttpSession session, ModelMap map) {
		map.addAttribute("id", request.getParameter("id"));
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		// -- 获取管理的信息域组织
		String defaultInfoOrgName = "";
		String defaultInfoOrgCode = "";
		if (userInfo.getInfoOrgList() != null && userInfo.getInfoOrgList().size() > 0) {
			defaultInfoOrgCode = userInfo.getInfoOrgList().get(0).getOrgCode();
			defaultInfoOrgName = userInfo.getInfoOrgList().get(0).getOrgName();
		}
		map.addAttribute("orgName", defaultInfoOrgName);
		map.addAttribute("orgCode", defaultInfoOrgCode);
		return "/szzg/resource/"+request.getParameter("page")+".ftl";
	}
	
	/**
	 * 后台树
	 */
	@ResponseBody
	@RequestMapping("/findTypeTree")
	public List<ZgResourceType> findTypeTree(HttpServletRequest request, HttpSession session, ModelMap map) {
		 return zgResourceTypeService.findTree(null);
	}
	
	/**
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findList")
	public EUDGPagination findList(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", request.getParameter("id"));
		params.put("name", request.getParameter("name"));
		return zgResourceTypeService.findList(page, rows, params);
	}
	
	@RequestMapping(value = "/toAdd")
	public String toAdd(HttpSession session, @RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "pid", required = false) Long pid,ModelMap map) {
		ZgResourceType entity = new ZgResourceType();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(id !=null){
			entity = zgResourceTypeService.findById(id);
		}else{
			ZgResourceType parent =zgResourceTypeService.findById(pid);
			entity.setParentTypeId(pid);
			if(parent != null){
				entity.setParentTypeName(parent.getResTypeName());
			}
		}
		map.addAttribute("userId",userInfo.getUserId());
		map.addAttribute("entity",entity);
		return "/szzg/resource/add_resource.ftl";
	}
	/**
	 * 查询typeCode 是否存在
	 */
	@ResponseBody
	@RequestMapping(value = "/findByTypeCode")
	public int findByTypeCode(HttpSession session, @RequestParam(value = "typeCode") String typeCode,
			ModelMap map) {
		return zgResourceTypeService.findByTypeCode(typeCode);
	}
	/**
	 * 移动节点
	 */
	@ResponseBody
	@RequestMapping(value = "/moveNode")
	public ResultObj moveNode(HttpSession session,@RequestParam(value = "pid") Long pid,
			@RequestParam(value = "id") Long id,
			ModelMap map) {
		ZgResourceType entity = new ZgResourceType();
		entity.setResTypeId(id);
		entity.setParentTypeId(pid);
		return Msg.OPERATE.getResult(zgResourceTypeService.moveNode(entity)>0);
	}
	/**
	 * 是否有子项
	 */
	@ResponseBody
	@RequestMapping(value = "/isHaveChildren")
	public int isHaveChildren(HttpSession session, @RequestParam(value = "id") Long id,
			ModelMap map) {
		return zgResourceTypeService.isHaveChildren(id);
	}
	@RequestMapping(value = "/detail")
	public String detail(HttpSession session, @RequestParam(value = "id") Long id,
			ModelMap map) {
		map.addAttribute("entity",zgResourceTypeService.findById(id));
		return "/szzg/resource/detail_resource.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete")
	public ResultObj del(HttpSession session, ModelMap map,
			@RequestParam(value="id")Long id) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("id", id);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		param.put("updateId", userInfo.getUserId());
		return Msg.DELETE.getResult(zgResourceTypeService.delete(param));
	}
	
	/**
	 * 更新或保存
	 * @param session
	 * @param map
	 * @param pt
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate")
	public ResultObj saveOrUpdate(HttpSession session, ModelMap map,ZgResourceType entity)  {
		Msg msg = Msg.OPERATE;
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(entity.getResTypeId() != null){
			msg =Msg.EDIT;
			entity.setUpdateUser(userInfo.getUserId());
		}else{
			msg =Msg.ADD;
			int count = zgResourceTypeService.findByTypeCode(entity.getTypeCode());
			if(count >0){
				return msg.getResult(false, "资源编码已经存在!");
			}
			entity.setCreateUser(userInfo.getUserId());
		}
		
		return msg.getResult(zgResourceTypeService.addOrUpdate(entity));
	}
	
	/**
	 * 公用详情
	 */
	@RequestMapping(value ="/indexDetails")
	public String indexDetails(HttpServletRequest request, HttpSession session, ModelMap map) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", request.getParameter("id"));
		map.addAttribute("res", zgResourceInfoService.findResource(params));
		map.addAttribute("img", App.IMG.getDomain(session));
		map.put("isGis", request.getParameter("isGis"));
		return "/szzg/resource/public_resource.ftl";
	}
	
	/**
	 * 	部件详情
	 */
	@RequestMapping(value ="/indexDetailsComponent")
	public String indexDetailsComponent(HttpServletRequest request, HttpSession session, ModelMap map) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", request.getParameter("id"));
		map.addAttribute("res", zgResourceInfoService.findComponentResource(params));
		map.addAttribute("img", App.IMG.getDomain(session));
		/*String page = request.getParameter("page");
		if(page!=null) {
			return "/szzg/resource/"+page+"_resource.ftl";
		}*/
		map.put("isGis", request.getParameter("isGis"));
		return "/szzg/resource/public_resource_detail.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Map<String, Object> listData(HttpServletRequest request, HttpSession session, ModelMap modelmap) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("xmin", request.getParameter("xmin"));
		params.put("xmax",request.getParameter("xmax"));
		params.put("ymin", request.getParameter("ymin"));
		params.put("ymax", request.getParameter("ymax"));
		params.put("typeCodes", request.getParameter("typeCodes"));
		params.put("orgCode",request.getParameter("orgCode"));
		Map<String, String> appMap = new HashMap<String, String>();
		App[] apps = App.values();
		
		for (App app : apps) {
			appMap.put(app.getCode(), app.getDomain(session));
		}
		List<Map<String,Object>> res = zgResourceTypeService.findMenuDetailUrl(params);
		//http://eventdev.fjsq.org/zhsq_event
		for (Map<String, Object> map : res) {
			if( map.get("URL") != null){
				String url =  map.get("URL").toString();
				int l = url.indexOf("/");
				map.put("URL", appMap.get(url.substring(0, l))+url.substring(l));
			}
			
		}		
		params.put("res", res);
		params.put("list", zgResourceInfoService.findByParam(params));
		return params;
	}
	/**
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findInfoList")
	public EUDGPagination findInfoList(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("typeCode", request.getParameter("typeCode"));
		params.put("name", request.getParameter("name"));
		params.put("orgCode", request.getParameter("orgCode"));
		return zgResourceInfoService.findList(page, rows, params);
	}
	
	

}