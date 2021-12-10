/**
 * 党建工作-支部建设-特殊党员情况
 */
package cn.ffcs.zhsq.map.arcgis.controller;



import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;






import cn.ffcs.resident.bo.CiRsCriteria;
import cn.ffcs.resident.bo.Pagination;
import cn.ffcs.resident.service.CiRsPartyService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;



/**
 * 特殊党员情况基础信息数据
 * @author wangty
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisDataOfSpecialPartyController")
public class ArcgisDataOfSpecialPartyController {

	@Autowired
	private CiRsPartyService ciRsPartyService;
	@Autowired
	private IMixedGridInfoService iMixedGridInfoService;
	
	///---党建工作-支部建设-特殊党员情况 start----------------------------------------------
	/**
	 * 特殊党员情况基础信息数据页
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/specialPartyInfo")
	public String specialPartyInfo(HttpSession session, @RequestParam(value="orgCode") String orgCode, @RequestParam(value="partyMemberType") String partyMemberType
			,@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr, ModelMap map) {			
		MixedGridInfo mixedGridInfo = iMixedGridInfoService.getDefaultGridByOrgCode(orgCode);
		Long gridId = mixedGridInfo.getGridId();
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("partyMemberType", partyMemberType);
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardSpecialPartyInfo.ftl";
	}
	
	/**
	 * 特殊党员情况基础信息数据
	 * @param session
	 * @param page 
	 * @param rows		
	 * @param partyMemberType	
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/specialPartyInfoListData", method=RequestMethod.POST)
	public Pagination partyOrgInfoListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="orgCode") String orgCode, @RequestParam(value="partyMemberType", required=false) String partyMemberType, @RequestParam(value="name", required=false) String name) {
		if(page<=0) page=1;						
		CiRsCriteria criteria = new CiRsCriteria();
		criteria.setOrgCode(orgCode);
		criteria.setName(name);
		criteria.setPartyMemberType(partyMemberType);
		//EUDGPagination pagination = null;
		Pagination pagination = ciRsPartyService.findPage(criteria, page, rows);
		return pagination;
	}		
	///---党建工作-支部建设-特殊党员情况 end----------------------------------------------
}
