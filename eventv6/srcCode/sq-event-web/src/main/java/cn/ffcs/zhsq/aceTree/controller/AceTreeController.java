package cn.ffcs.zhsq.aceTree.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.mybatis.domain.aceTreeBo.AceTreeBo;
import cn.ffcs.zhsq.mybatis.domain.userOrganTree.UserOrganTree;
import cn.ffcs.zhsq.oaUserOrgan.service.OaUserOrganService;
import cn.ffcs.zhsq.utils.ConstantValue;

@Controller
@RequestMapping("/web/aceTree")
public class AceTreeController {

	private static final String ORG_TYPE = "0";		//组织类型
	private static final String ENTITY_TYPE = "1";	//地域类型
	private static final String PERSON_TYPE = "2";	//人员类型
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;
	
	@Autowired
	private OaUserOrganService oaUserOrganService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;
	
	@RequestMapping("page")
	public String page(HttpServletRequest request,ModelMap model,
			String selectedIds,String chooseType,String showToLevel){
		
		if(StringUtils.isEmpty(chooseType) || !isNumeric(chooseType)){
			chooseType = ORG_TYPE;
		}
		model.put("chooseType", chooseType);
		
		if(StringUtils.isEmpty(showToLevel) || !isNumeric(showToLevel))
			showToLevel = "6";
		model.put("showToLevel", showToLevel);
		
		String folderSelect = request.getParameter("folderSelect");
		if(StringUtils.isNotEmpty(folderSelect)){
			model.put("folderSelect", Boolean.parseBoolean(folderSelect));
		} else {
			model.put("folderSelect", true);
		}
		
		String multi = request.getParameter("multi");
		if(StringUtils.isNotEmpty(multi)){
			model.put("multi", Boolean.parseBoolean(multi));
		} else {
			model.put("multi", true);
		}
		
		if(StringUtils.isNotBlank(selectedIds)){
			
			List<Long> idsList = new ArrayList<Long>();
			for(String id : selectedIds.split(",")){
				if(StringUtils.isEmpty(id))
					continue;
				idsList.add(Long.parseLong(id));
			}
			
			List<AceTreeBo> childrenData = new ArrayList<AceTreeBo>();
			AceTreeBo aceTreeBo = null;
			if(ORG_TYPE.equals(chooseType)){//组织数据回填
				List<UserOrganTree> list = oaUserOrganService.getInfoByIds(idsList,"o");
				for(UserOrganTree org : list){
					aceTreeBo = new AceTreeBo();
					aceTreeBo.setId(org.getId());
					aceTreeBo.setName(org.getName());
					aceTreeBo.setCode(org.getOrgCode());
					aceTreeBo.setType(ORG_TYPE);
					childrenData.add(aceTreeBo);
				}
			}
			model.put("dataList", childrenData);
			
			model.put("selectedIds", selectedIds);
		}
		
		String suffix = request.getParameter("suffix");
		if(StringUtils.isNotBlank(suffix)){
			return "common/aceTreePage_"+suffix+".html";
		}
		model.put("ldType",request.getParameter("ldType"));
		return "/ypms/ldOrg/aceTreePage.ftl";
	}
	
	@RequestMapping("/initTree")
	@ResponseBody
	public Object initTree(HttpServletRequest request,String chooseType,
			Integer showToLevel){
		
		UserInfo user = this.getUserInfo(request);
		
		Long currentOrgId = 0L;
		String orgParam = request.getParameter("id");
		if(StringUtils.isEmpty(orgParam)){
			currentOrgId = user.getOrgId();
		} else {
			currentOrgId = Long.parseLong(orgParam);
		}
		
		Map<String,Object> dataMap = new HashMap<String,Object>();
		
		if(StringUtils.isEmpty(chooseType)){
			chooseType = ORG_TYPE;
		}
		
		
		if(ORG_TYPE.equals(chooseType)){//组织、人员类型数据
			AceTreeBo pOrg = new AceTreeBo();
			pOrg.setId(user.getOrgId());
			pOrg.setpId(user.getOrgId());
			pOrg.setName(user.getOrgName());
			pOrg.setCode(user.getOrgCode());
			pOrg.setType(ORG_TYPE);
			pOrg.setLevel("1");
			pOrg.setIsLeaf(false);
			dataMap.put("pData", pOrg);
		}
		
		if(ORG_TYPE.equals(chooseType)){//组织类型数据
			dataMap.put("cData", initOrgData(currentOrgId,showToLevel));
		}
		
		return dataMap;
	}
	
	@RequestMapping("/getChildrenData")
	@ResponseBody
	public Object getChildrenData(HttpServletRequest request,
			@RequestParam(value="pId")Long pId,String chooseType,Integer showToLevel){
		
		if(StringUtils.isEmpty(chooseType) || !isNumeric(chooseType)){
			chooseType = ORG_TYPE;
		}
		
		if(ORG_TYPE.equals(chooseType)){//组织类型数据
			return initOrgData(pId,showToLevel);
		}
		return new ArrayList<AceTreeBo>();
	}
	
	/**
	 * 初始化组织数据
	 * @param pId
	 * @param showToLevel
	 * @return
	 */
	private List<AceTreeBo> initOrgData(Long pId,Integer showToLevel){
		List<OrgSocialInfoBO> list = orgSocialInfoOutService.findOrgSocialListByParentId(pId);
		List<AceTreeBo> childrenData = new ArrayList<AceTreeBo>();
		AceTreeBo aceTreeBo = null;
		for(OrgSocialInfoBO org : list){
			String level = org.getChiefLevel();
			if(Integer.parseInt(level)>showToLevel.intValue())
				continue;
			
			aceTreeBo = new AceTreeBo();
			if(Integer.parseInt(level)<showToLevel.intValue()){
				aceTreeBo.setIsLeaf(false);
			} else {
				aceTreeBo.setIsLeaf(true);
			}
			
			aceTreeBo.setId(org.getOrgId());
			aceTreeBo.setpId(org.getParentOrgId());
			aceTreeBo.setName(org.getOrgName());
			aceTreeBo.setCode(org.getOrgCode());
			aceTreeBo.setLevel(org.getChiefLevel());
			aceTreeBo.setType(ORG_TYPE);
			childrenData.add(aceTreeBo);
		}
		return childrenData;
	}
	
	/**
	 * 初始化地域数据
	 * @param pId
	 * @param showToLevel
	 * @return
	 */
	private List<AceTreeBo> initEntityData(Long pId,Integer showToLevel){
		List<OrgEntityInfoBO> list = orgEntityInfoOutService.findOrgListByParentId(pId);
		
		List<AceTreeBo> childrenData = new ArrayList<AceTreeBo>();
		if(list == null || list.isEmpty())
			return childrenData;
		
		AceTreeBo aceTreeBo = null;
		for(OrgEntityInfoBO org : list){
			String level = org.getChiefLevel();
			if(Integer.parseInt(level)>showToLevel.intValue())
				continue;
			
			aceTreeBo = new AceTreeBo();
			if(Integer.parseInt(level)<showToLevel.intValue()){
				aceTreeBo.setIsLeaf(false);
			} else {
				aceTreeBo.setIsLeaf(true);
			}
			
			aceTreeBo.setId(org.getOrgId());
			aceTreeBo.setpId(org.getParentOrgId());
			aceTreeBo.setName(org.getOrgName());
			aceTreeBo.setCode(org.getOrgCode());
			aceTreeBo.setLevel(org.getChiefLevel());
			aceTreeBo.setType(ENTITY_TYPE);
			childrenData.add(aceTreeBo);
		}
		return childrenData;
	}
	
	/**
	 * 初始化人员数据(按组织层级)
	 * @param pId
	 * @return
	 */
	private List<AceTreeBo> initOrgUserData(Long pId,Integer showToLevel){
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pId", pId);
		List<UserOrganTree> list = oaUserOrganService.getUserAndBMByParam(paramMap);
		List<AceTreeBo> childrenData = new ArrayList<AceTreeBo>();
		AceTreeBo aceTreeBo = null;
		for(UserOrganTree org : list){
			
			aceTreeBo = new AceTreeBo();
			if("u".equals(org.getUserOrOrgan())){
				aceTreeBo.setIsLeaf(true);
				aceTreeBo.setType(PERSON_TYPE);
			} else {
				String level = org.getChiefLevel();
				if(Integer.parseInt(level)>showToLevel.intValue())
					continue;
				aceTreeBo.setIsLeaf(false);
				aceTreeBo.setType(ORG_TYPE);
			}
			
			aceTreeBo.setId(org.getId());
			aceTreeBo.setpId(org.getPid());
			aceTreeBo.setName(org.getName());
			aceTreeBo.setCode(org.getOrgCode());
			aceTreeBo.setLevel(org.getChiefLevel());
			childrenData.add(aceTreeBo);
		}
		return childrenData;
	}
	
	/**
	 * 获取web用户信息
	 * @param request
	 * @return
	 */
	public UserInfo getUserInfo(HttpServletRequest request){
		HttpSession session = request.getSession();
		return (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
	}
	
	private boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
