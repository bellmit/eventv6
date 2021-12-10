package cn.ffcs.zhsq.jointDefence.controller;

import cn.ffcs.resident.bo.CiRsCriteria;
import cn.ffcs.resident.bo.CiRsTop;
import cn.ffcs.resident.bo.HouseHoldCount;
import cn.ffcs.resident.service.CiRsService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.jointDefence.service.IJointDefenceService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 联防长
 * @author zhongshm
 *
 */
@Controller
@RequestMapping("/zhsq/jointDefence")
public class JointDefenceController extends ZZBaseController {
	@Autowired
	private CiRsService ciRsService; // 人口
	@Autowired
	private IJointDefenceService jointDefenceService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	
	//private String FORM_TYPE = "eventCase";//表单类型
	
	/**
	 * 跳转案件列表
	 * @param session
	 * @param listType	列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 我发起的
	 * 			5 辖区所有
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public String toList(HttpSession session, 
			@RequestParam(value = "positionId", required=false) String positionId,
			@RequestParam(value = "positionCode", required=false) String positionCode,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		String gridName = defaultGridInfo.get(KEY_START_GRID_NAME).toString();
		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("gridName", gridName);
		map.addAttribute("positionId", funConfigurationService.changeCodeToValue("JOIN_DEFENCE_CFG","POSITION_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL));
		map.addAttribute("positionCode", positionCode);
		return "/zzgl/jointDefence/list_jointDefence.ftl";
	}

	@RequestMapping(value = "/toDetail")
	public String toDetail(HttpSession session, 
			@RequestParam(value = "id") String id,
			@RequestParam(value = "userId") Long userId,
			@RequestParam(value = "userRegionCode") String userRegionCode,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
//        CiRsTop ciRsTop = new CiRsTop();
//        Map<String, Object> ciRsTopMap = ciRsService.getCiRsTopByOrgCodeAndIdCard(userInfo.getOrgCode(), id);
//
//        if(ciRsTopMap.get("ciRs") != null){
//            ciRsTop = (CiRsTop) ciRsTopMap.get("ciRs");
//        }
//		map.addAttribute("ciRsTop", ciRsTop);
		map.addAttribute("userId", userId);
		map.addAttribute("idCard", id);
		map.addAttribute("userOrgCode", userInfo.getOrgCode());
		map.addAttribute("RS_URL", App.RS.getDomain(session));
		map.addAttribute("userRegionCode", userRegionCode);
		return "/zzgl/jointDefence/detail_jointDefence.ftl";
	}

    /**
     * 联防长数据
     * @param session
     * @param page
     * @param rows
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listData")
    public EUDGPagination listData(HttpSession session,
                                   @RequestParam(value = "page") int page,
                                   @RequestParam(value = "rows") int rows,
                                   @RequestParam Map<String, Object> params) {
        //UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        params.put("startInfoOrgCode", this.getDefaultInfoOrgCode(session));
        EUDGPagination eventCasePagination = jointDefenceService.findPagination(page, rows, params);
        return eventCasePagination;
    }

	@ResponseBody
	@RequestMapping(value = "/cirs/listData")
	public Object cirs_listData(HttpSession session,
			@RequestParam Map<String, Object> params,int page, int rows) {
		//UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		params.put("startInfoOrgCode", this.getDefaultInfoOrgCode(session));
        CiRsCriteria ciRsCriteria = new CiRsCriteria();
        ciRsCriteria.setOrgCode(params.get("userRegionCode").toString());
        List<CiRsTop> holdAndRsList = ciRsService.findHoldAndRsList(ciRsCriteria, page, rows);
        HouseHoldCount hold=ciRsService.findHoldAndRsCount(ciRsCriteria);
        EUDGPagination pagination = new EUDGPagination(hold.getHoldCount(), holdAndRsList);
//		HomeInfoCriteria homeInfoCriteria = new HomeInfoCriteria();
//        homeInfoCriteria.setOrgCode(params.get("userRegionCode").toString());
//        List<HomeInfo> listHasRemark = homeInfoService.findListHasRemark(homeInfoCriteria, 1, 100);
//        System.out.println(listHasRemark);
		return pagination;
	}
    @RequestMapping(value="/viewfamily")
    public String viewfamily(HttpSession session, HttpServletRequest request, ModelMap map){ 	
    	String familyId=request.getParameter("familyId");
    	if (familyId==null||"null".equals(familyId)) {
    		familyId="-1";
		}
    	map.addAttribute("RS_URL", App.RS.getDomain(session));
    	map.addAttribute("familyId", familyId);
		return "/zzgl/jointDefence/viewfamily.ftl";
    	
    }
    /**
     * 获取家庭成员
     * @param session
     * @param request
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/viewfamilyData")
    public Object viewfamilyData(HttpSession session, HttpServletRequest request, ModelMap map){ 	
    	String familyId=request.getParameter("familyId");
    	List<CiRsTop> cirsMap=ciRsService.findRsTopByFamilyId(Long.parseLong(familyId));//获取家庭成员信息
    	return cirsMap;   	
    }
   
	@ResponseBody
	@RequestMapping(value = "/event/listData")
	public EUDGPagination event_listData(HttpSession session,
                                         @RequestParam(value = "page") int page,
                                         @RequestParam(value = "rows") int rows,
                                         @RequestParam Map<String, Object> params) {
		//UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
//		params.put("startInfoOrgCode", this.getDefaultInfoOrgCode(session));
        EUDGPagination eventCasePagination = jointDefenceService.findEventPagination(page, rows, params);
		return eventCasePagination;
	}

	/**
	 * 展示十户联防
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/showTen")
	public String showTen(HttpServletRequest request,HttpSession session, ModelMap map) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("orgCode", request.getParameter("orgCode"));
		map.put("title", jointDefenceService.findGridPathByOrgCode(param));
		map.put("stat", jointDefenceService.findStatByOrgCode(param));
		return "/zzgl/jointDefence/index_ten.ftl";
	}
	
	/**
	 * 展示
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/showTenPage")
	public String showTenTeam(HttpServletRequest request,HttpSession session, ModelMap map,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		map.put("orgCode", request.getParameter("orgCode"));
		map.put("elementsCollectionStr", elementsCollectionStr);
		map.put("mapt", request.getParameter("mapt"));
		return "/zzgl/jointDefence/"+request.getParameter("page")+".ftl";
	}
	
	/**
	 * 展示联防组
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findTenTeam")
	public EUDGPagination findTenTeam(HttpServletRequest request, HttpSession session, ModelMap map,
                                      @RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("orgCode", request.getParameter("orgCode"));
		param.put("mapt", request.getParameter("mapt"));
		return jointDefenceService.findJointTeam(page, rows, param);
	}
	/**
	 * 展示联防长
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findTenPerson")
	public EUDGPagination findTenPerson(HttpServletRequest request, HttpSession session, ModelMap map,
                                        @RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("orgCode", request.getParameter("orgCode"));
		return  jointDefenceService.findJointPerson(page, rows, param);
	}
	
	/**
	 * 展示联防组详情
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/findTenTeamInfo")
	public String findTenTeamInfo(HttpServletRequest request,HttpSession session, ModelMap map,
			@RequestParam(value = "userId") String userId,@RequestParam(value = "orgCode") String orgCode) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("orgCode", orgCode);
		if(isNum(orgCode)) {
			map.put("stat", jointDefenceService.findStatByOrgCode(param));
			if(isNum(userId)) {
				List<Map<String, Object>> list = jointDefenceService.findList(param); 
				map.put("data", list.get(0));
			}else {
				
			}
		}else {
			map.put("stat", new HashMap<String, Object>());
		}
		
		return "/zzgl/jointDefence/team_info.ftl";
	}
	public boolean isNum(String num) {
		return num!=null && Pattern.compile("[0-9]{1,}").matcher(num).matches();
	}
	/**
	 * 展示联防长详情
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/findTenPersonInfo")
	public String findTenPersonInfo(HttpServletRequest request,HttpSession session, ModelMap map,
			@RequestParam(value = "idCard") String idCard,@RequestParam(value = "orgCode") String orgCode) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("idCard", idCard);
		param.put("orgCode", orgCode);
		List<Map<String, Object>> list = jointDefenceService.findJointPersonList(param); 
		map.put("data", list.get(0));

		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		return "/zzgl/jointDefence/person_info.ftl";
	}
}
