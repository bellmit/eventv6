package cn.ffcs.zhsq.map.arcgis.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.lzly.mybatis.domain.propagandaPosition.PropagandaPosition;
import cn.ffcs.lzly.mybatis.domain.twoStation.TwoStation;
import cn.ffcs.lzly.service.propagandaPosition.IPropagandaPositionService;
import cn.ffcs.lzly.service.twoStation.ITwoStationService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 宣传阵地 地图数据
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/Lzly/pp")
public class GisDataLzlyPPController extends ZZBaseController {

	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ITwoStationService twoStationService;
	
	@Autowired
	private IPropagandaPositionService propagandaPositionService; //注入宣传阵地模块服务

	@RequestMapping(value="/toPropagandaPositionList")
	public String toPropagandaPositionList(HttpSession session,
										  @RequestParam(value="orgCode") String orgCode,
										  @RequestParam(value="infoOrgCode") String infoOrgCode,
										  @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr, ModelMap map) {
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/propagandaPosition/list_propagandaPosition.ftl";
	}

	@ResponseBody
	@RequestMapping(value="/propagandaPositionListData", method=RequestMethod.POST)
	public EUDGPagination propagandaPositionListData(HttpSession session,
													 @RequestParam(value="page") int page,
													 @RequestParam(value="rows") int rows,
													 @RequestParam(value="regionCode") String regionCode,
													 @RequestParam(value="personInCharge", required=false) String personInCharge,
													 @RequestParam(value="type", required=false) String type,
													 @RequestParam(value="name", required=false) String name,
													 @RequestParam(value="phone", required=false) String phone
													) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(page<=0) page=1;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgCode", userInfo.getOrgCode());
		
		params.put("regionCode", regionCode);
		
		if(StringUtils.isNotBlank(personInCharge)){
			params.put("personInCharge", personInCharge);
		}
        if(StringUtils.isNotBlank(type)){
        	params.put("type", type);
		}
        
        if(StringUtils.isNotBlank(name)){
        	params.put("name", name);
		}
        if(StringUtils.isNotBlank(phone)){
        	params.put("phone", phone);
		}
		
		EUDGPagination pagination = propagandaPositionService.searchList(page, rows, params);
		
	
		return pagination;
	}

	/**
	 * 获取标注点的信息
	 * @param session
	 * @param map
	 * @param res
	 * @param ids
	 * @param markType
	 * @param mapt
	 * @param elementsCollectionStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ppListGisData")
	public List<ArcgisInfoOfPublic> ppListGisData(HttpSession session,
														  ModelMap map, HttpServletResponse res,
														  @RequestParam(value="ids") String ids,
														  @RequestParam(value="markType", required=false) String markType,
														  @RequestParam(value="mapt", required=false) Integer mapt,
														  @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();
		if (StringUtils.isNotBlank(ids)) {
			try {
				list = this.arcgisDataOfLocalService.getArcgisPropagandaPositionLocateDataListByIds(ids,mapt,markType);
				for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
					arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@RequestMapping(value="/propagandaPositionDetail")
	public String propagandaPositionDetail(HttpSession session,
								   @RequestParam(value="ppId") Long ppId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			
		PropagandaPosition bo = propagandaPositionService.searchById(ppId);
		map.addAttribute("bo", bo);

		map.addAttribute("RESOURSE_SERVER_PATH",  getNewUrl(session, App.IMG.getDomain(session)));//支持多个域名访问综治网格

		return "/map/arcgis/standardmappage/propagandaPosition/detail_propagandaPosition.ftl";
	}


}
