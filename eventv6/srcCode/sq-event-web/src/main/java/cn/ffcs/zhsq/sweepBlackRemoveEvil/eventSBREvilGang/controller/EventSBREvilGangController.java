package cn.ffcs.zhsq.sweepBlackRemoveEvil.eventSBREvilGang.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.mybatis.domain.sweepBlackRemoveEvil.eventSBREvilGang.EventSBREvilGang;
import cn.ffcs.zhsq.sweepBlackRemoveEvil.eventSBREvilGang.service.IEventSBREvilGangService;

/**   
 * @Description: 扫黑除恶_黑恶团伙管理模块控制器
 * @Author: LINZHU
 * @Date: 05-23 10:37:41
 * @Copyright: 2018 福富软件
 */ 
@Controller("eventSBREvilGangController")
@RequestMapping("/zhsq/eventSBREvilGang")
public class EventSBREvilGangController  extends ZZBaseController{

	@Autowired
	private IEventSBREvilGangService eventSBREvilGangService; //注入扫黑除恶_黑恶团伙管理模块服务
	
    @Autowired
	private IInvolvedPeopleService involvedPeopleService;
    
   private static String bizType="11";//涉及人员表 黑恶团伙类型
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		//获取默认网格信息
        Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
	    map.addAttribute("infoOrgCode",defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		return "zzgl/sweepBlackRemoveEvil/eventSBREvilGang/list_eventSBREvilGang.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,
		int page, int rows) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gangName", request.getParameter("gangName"));
		params.put("infoOrgCode", request.getParameter("regionCode"));
		params.put("activityZone", request.getParameter("activityZone"));
		params.put("situation", request.getParameter("situation"));
		params.put("hitStatus", request.getParameter("hitStatus"));
		params.put("gangStatus", request.getParameter("gangStatus"));
		EUDGPagination pagination = eventSBREvilGangService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,Long gangId) {
		EventSBREvilGang bo = eventSBREvilGangService.searchById(gangId);
		map.addAttribute("bo", bo);
		List<InvolvedPeople>  members=	involvedPeopleService.findInvolvedPeopleListByBiz(gangId, bizType);
		map.put("members", members);
		return "zzgl/sweepBlackRemoveEvil/eventSBREvilGang/detail_eventSBREvilGang.ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/add")
	public Object add(HttpServletRequest request, HttpSession session, ModelMap map,Long gangId) {
		EventSBREvilGang bo =new EventSBREvilGang();
		if (gangId != null) { 
			bo = eventSBREvilGangService.searchById(gangId);
			map.put("bo", bo);
			List<InvolvedPeople>  members=	involvedPeopleService.findInvolvedPeopleListByBiz(gangId, bizType);
			map.put("members", members); 
		}
		Map<String,Object> defaultGridInfo = this.getDefaultGridInfo(session);
		bo.setInfoOrgCode(defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString());
		bo.setGridPath(getMixedGridInfo(session, request).getGridPath());
		bo.setInfoOrgName(defaultGridInfo.get(KEY_START_GRID_NAME).toString());
		map.put("bo", bo);
		return "zzgl/sweepBlackRemoveEvil/eventSBREvilGang/add_eventSBREvilGang.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		EventSBREvilGang bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		
		//判断名称是否存在
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("allName", bo.getGangName());
		params.put("gangId", bo.getGangId());
		List<EventSBREvilGang>  list=eventSBREvilGangService.getListByParams(params);
		if(list!=null && list.size()>0){
			result = "exsit";
			resultMap.put("result", result);
			resultMap.put("gangName", list.get(0).getGangName());
			resultMap.put("infoOrgName", list.get(0).getInfoOrgName());
			resultMap.put("gridPath", list.get(0).getGridPath());
			return resultMap;
		}
		
		//获取成员列表
		List<InvolvedPeople>  members=JSONArray.toList(JSONArray.fromObject(request.getParameter("memberlist")), new InvolvedPeople(), new JsonConfig());
    	
		//先批量删除 
    	params.put("list", members);
		involvedPeopleService.batchDelete(params);
	
		if (bo.getGangId() == null) { //新增
			Long id = eventSBREvilGangService.insert(bo);
			if (id != null && id > 0) {
				result = "success";
			};	
		} else { //修改
			boolean updateResult = eventSBREvilGangService.update(bo);
			if (updateResult) {
				result = "success";
			}
		}
		List<InvolvedPeople> updates=new ArrayList<InvolvedPeople>();
		List<InvolvedPeople> inserts=new ArrayList<InvolvedPeople>();	
		for (InvolvedPeople involvedPeople : members) {
			if(involvedPeople.getIpId()!=null && involvedPeople.getIpId()>0 ){//存在主键id
				updates.add(involvedPeople);
			}else{
				inserts.add(involvedPeople);
				involvedPeople.setBizId(bo.getGangId());
			}
		}
		if(inserts.size()>0){
			involvedPeopleService.batchInsert(inserts);//批量新增
		}
		if(updates.size()>0){
			involvedPeopleService.batchUpdate(updates);
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
		EventSBREvilGang bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = eventSBREvilGangService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 选择页面
	 */
	@RequestMapping("/check")
	public Object check(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		EventSBREvilGang bo = eventSBREvilGangService.searchById(id);
		map.addAttribute("bo", bo);
		map.addAttribute("ids", request.getParameter("ids"));
		map.addAttribute("infoOrgCode", request.getParameter("infoOrgCode"));
		return "zzgl/sweepBlackRemoveEvil/eventSBREvilGang/check_eventSBREvilGang.ftl";
	}

}