package cn.ffcs.zhsq.map.thresholdcolorcfg.controller;

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
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.thresholdcolorcfg.service.IThresholdColorCfgService;
import cn.ffcs.zhsq.mybatis.domain.map.thresholdcolorcfg.ThresholdColorCfg;

@Controller
@RequestMapping(value="/zhsq/map/thresholdcolorcfg/thresholdColorCfg")
public class ThresholdColorCfgController  extends ZZBaseController{
	
	@Autowired
	private IThresholdColorCfgService thresholdColorCfgService;
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	
	/**
	 * 2014-10-13 liushi add
	 * 链接到图层菜单管理页面
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/index")
	public String index(HttpSession session, HttpServletRequest request, ModelMap map){
		String forward = "/map/thresholdcolorcfg/index_thresholdColorCfg.ftl";
		Map<String, Object> defaultOrgInfo = this.getDefaultOrgInfo(session);
		String orgCode = (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		String gridName = (String)defaultOrgInfo.get(KEY_START_GRID_NAME);
		Long gridId = (Long)defaultOrgInfo.get(KEY_START_GRID_ID);
		map.put("orgCode", orgCode);
		map.put("gridName", gridName);
		map.put("gridId", gridId);
		return forward;
	}
	
	/**
	 * 2015-09-25 liushi add 根据信息域查询列表
	 * @param session
	 * @param request
	 * @param map
	 * @param pageNo
	 * @param pageSize
	 * @param orgCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listData")
	public EUDGPagination listData(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer rows,
			@RequestParam(required = false) String orgCode
			){
		Map<String, Object> params = new HashMap<String, Object>();
		if(orgCode == null || "".equals(orgCode)) {
			Map<String, Object> defaultOrgInfo = this.getDefaultOrgInfo(session);
			orgCode = (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		}
		BaseDataDict dict = this.baseDictionaryService.getDataDictListOfMultistage("B898", null);
		List<BaseDataDict> dictList = dict.getChildrenList();
		params.put("orgCode", orgCode);
		EUDGPagination pagination = this.thresholdColorCfgService.findThresholdColorCfgPagination(page, rows, params);
		if(dictList != null && dictList.size()>0) {
			List<ThresholdColorCfg> list = (List<ThresholdColorCfg>) pagination.getRows();
			for(ThresholdColorCfg obj:list) {
				for(BaseDataDict objDict:dictList) {
					if(objDict.getDictGeneralCode().equals(obj.getClass_())) {
						obj.setClassName(objDict.getDictName());
					}
				}
			}
		}
		return pagination;
	}
	
	/**
	 * 2015-09-25 liushi add 新增
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/add")
	public String add(HttpSession session, HttpServletRequest request, ModelMap map){
		ThresholdColorCfg thresholdColorCfg = new ThresholdColorCfg();
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		thresholdColorCfg.setGridId((Long)defaultGridInfo.get(KEY_START_GRID_ID));
		thresholdColorCfg.setOrgCode((String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		thresholdColorCfg.setGridName((String)defaultGridInfo.get(KEY_START_GRID_NAME));
		thresholdColorCfg.setStatus_("1");
		map.put("thresholdColorCfg", thresholdColorCfg);
		String forward = "/map/thresholdcolorcfg/add_thresholdColorCfg.ftl";
		return forward;
	}
	
	/**
	 * 2015-09-25 liushi add 编辑
	 * @param session
	 * @param request
	 * @param map
	 * @param tcId
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String edit(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = true) Long tcId){
		ThresholdColorCfg thresholdColorCfg = this.thresholdColorCfgService.findThresholdColorCfgById(tcId);
		map.put("thresholdColorCfg", thresholdColorCfg);
		String forward = "/map/thresholdcolorcfg/add_thresholdColorCfg.ftl";
		return forward;
	}
	
	/**
	 * 2015-09-25 liushi add 保存（包含新增跟修改功能）
	 * @param session
	 * @param request
	 * @param map
	 * @param thresholdColorCfg
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save")
	public Map<String,Object> save(HttpSession session, HttpServletRequest request, ModelMap map,
			ThresholdColorCfg thresholdColorCfg
			){
		
		boolean flag = this.thresholdColorCfgService.saveThresholdColorCfg(thresholdColorCfg);
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("flag", flag);
		return result;
	}
	
	/**
	 * 2015-09-25 liushi add 保存（包含新增跟修改功能）
	 * @param session
	 * @param request
	 * @param map
	 * @param tcId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/del")
	public Map<String,Object> del(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = true) Long tcId
			){
		boolean flag = this.thresholdColorCfgService.deleteThresholdColorCfg(tcId);
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("flag", flag);
		return result;
	}
}
