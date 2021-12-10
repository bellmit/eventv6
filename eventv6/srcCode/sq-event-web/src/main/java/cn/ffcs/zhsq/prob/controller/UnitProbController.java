package cn.ffcs.zhsq.prob.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.prob.service.IUnitProbService;

/**
 * 
 * @author luth
 *
 */
@Controller
@RequestMapping("/zhsq/unitProb")
public class UnitProbController extends ZZBaseController{
	
	@Autowired
	private IUnitProbService unitProbService;
	
	@RequestMapping(value="index")
	public String indexUnit(HttpSession session, ModelMap map){
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("gridId",defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("gridCode",defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("gridName",defaultGridInfo.get(KEY_START_GRID_NAME));
		return "zzgl/prob/list_unitProb.ftl";
	}
	
	@RequestMapping(value="windowPrint")
	public String windowPrint(){
		return "zzgl/prob/windowPrint_unit.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "unitListData",method = RequestMethod.POST)
	public EUDGPagination unitListData(HttpSession session, ModelMap map, int page, int rows,@RequestParam Map<String, Object> params){
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Object regionCode=params.get("regionCode");
		if(regionCode==null || regionCode.toString().equals("")){
			params.put("regionCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		}
		EUDGPagination accountEn = unitProbService.searchList(page,rows,params);
		return accountEn;
	}
	
	@RequestMapping("/caseBriefDetail")
	public Object showCaseBriefDetail( HttpSession session, ModelMap map,int probId) {
		Map<String, Object> bo = unitProbService.searchById(probId);
		map.addAttribute("caseBrief", bo.get("caseBrief"));
		return "zzgl/prob/caseBriefDetail.ftl";
	}
	
	
	/**
	 * 打印
	 * @param session
	 * @param map
	 * @param regionCode
	 * @param page
	 * @param rows
	 * @param params
	 * @return
	 */
	@RequestMapping(value="printPage")
	public String printPage(HttpSession session, ModelMap map,String regionCode,Integer page, Integer rows,@RequestParam Map<String, Object> params){
		
		if(page==null){
			page=1;
		}
		if(rows==null){
			rows=20;
		}
		EUDGPagination pagination = unitProbService.searchList(page,rows,params);
		map.addAttribute("pagination",pagination);
		return "zzgl/prob/print_page_unit.ftl";
	}
}
