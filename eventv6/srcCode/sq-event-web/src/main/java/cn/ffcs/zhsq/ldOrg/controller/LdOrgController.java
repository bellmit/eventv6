package cn.ffcs.zhsq.ldOrg.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.addressBook.service.AddressBookService;
import cn.ffcs.zhsq.ldOrg.service.LdOrgService;
import cn.ffcs.zhsq.mybatis.domain.addressBook.AddressBook;
import cn.ffcs.zhsq.mybatis.domain.ldOrg.LdOrg;
import cn.ffcs.zhsq.utils.ConstantValue;

import com.alibaba.fastjson.JSON;

/**   
 * @Description: 联动单位管理表模块控制器
 * @Author: linwd
 * @Date: 04-13 15:21:53
 * @Copyright: 2018 福富软件
 */ 
@Controller("ldOrgController")
@RequestMapping("/zhsq/ldOrg")
public class LdOrgController {

	@Autowired
	private LdOrgService ldOrgService; //注入联动单位管理表模块服务
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private AddressBookService addressBookService; //注入联动单位管理表模块服务
	
	private static final String LD_TYPE = "0"; //联动单位类型
	
	private static final String DW_TYPE = "1"; //专业化队伍类型
	
	private static final String LEADER = "0"; //分管领导
	private static final String CONTACTER = "1"; //联络员
	private static final String LXR= "2"; //联系人
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		List<BaseDataDict> data= baseDictionaryService.getDataDictListOfSinglestage("B710", null);
		JSONArray dataDictArray = new JSONArray();
		for (BaseDataDict bo : data) {
			JSONObject json = new JSONObject();
			json.put("id",bo.getDictGeneralCode());
			json.put("pId",bo.getDictPid());
			json.put("name",bo.getDictName());
			json.put("isParent","true");
			if ("B710001".equals(bo.getDictCode())) {
				json.put("ldType","0");
			} else {
				json.put("ldType","1");
			}
			
			dataDictArray.add(json);
		}
		//String dataDict = dataDictArray.toJSONString();
		String dataDict = dataDictArray.toString();
		map.put("dataDict", dataDict);
		return "/ypms/ldOrg/list_ldOrg.ftl";
	}

	/**
	 * 列表页面
	 */
	@RequestMapping("/indexList")
	public Object indexList(HttpServletRequest request, HttpSession session, ModelMap map) {
		return "/ypms/ldOrg/list_ldOrg_for_case.ftl";
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
		params.put("ldId", request.getParameter("ldId"));
		params.put("ldType", request.getParameter("ldType"));
		params.put("ldName", request.getParameter("ldName"));
		params.put("orgCode", request.getParameter("orgCode"));
		params.put("orgId", request.getParameter("orgId"));
		params.put("status", request.getParameter("status"));
		EUDGPagination pagination = ldOrgService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		LdOrg bo = ldOrgService.searchById(id);
		map.addAttribute("bo", bo);
		return "/gmis/ldOrg/detail_ldOrg.ftl";
	}

	/**
	 * 表单页面
	 */
	@ResponseBody
	@RequestMapping("/searchById")
	public Object searchById(HttpServletRequest request, HttpSession session, ModelMap map) {
		LdOrg bo = new LdOrg();
		Long id = Long.parseLong(request.getParameter("id"));
		if (id != null) {
			bo = ldOrgService.searchById(id);
			List<AddressBook> list = addressBookService.searchByLdId(id);
			for (int i=0; i<list.size(); i++) {
				if (LEADER.equals(list.get(i).getAbRole())) {
					bo.setLeaderId(list.get(i).getAbId());
					bo.setLeader(list.get(i).getAbName());
					bo.setLeaderDuty(list.get(i).getAbDuty());
					bo.setLeaderMobile(list.get(i).getAbMobile());
				}
				
				if (CONTACTER.equals(list.get(i).getAbRole())) {
					bo.setContacterId(list.get(i).getAbId());
					bo.setContacter(list.get(i).getAbName());
					bo.setContacterDuty(list.get(i).getAbDuty());
					bo.setContacterMobile(list.get(i).getAbMobile());
				}
				
				if (LXR.equals(list.get(i).getAbRole())) {
					bo.setLxrId(list.get(i).getAbId());
					bo.setLxr(list.get(i).getAbName());
					bo.setLxrMobile(list.get(i).getAbMobile());
				}
			}
		}
		return bo;
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		LdOrg bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		AddressBook addressBook = new AddressBook();
		AddressBook addressBook1 = new AddressBook();
		boolean updateResult = ldOrgService.update(bo);
		if (updateResult) {
			if (LD_TYPE.equals(bo.getLdType())) {
				//分管领导
				addressBook.setAbName(bo.getLeader());
				addressBook.setAbDuty(bo.getLeaderDuty());
				addressBook.setAbMobile(bo.getLeaderMobile());
				addressBook.setLdId(bo.getLdId());
				
				if (bo.getLeaderId() == null) {
					//添加分管领导
					addressBook.setAbRole(LEADER);
					addressBook.setOrgCode(bo.getOrgCode());
					addressBook.setOrgId(bo.getOrgId());
					addressBookService.insert(addressBook);
				} else {
					//修改分管领导
					addressBook.setAbId(bo.getLeaderId());
					addressBookService.update(addressBook);
				}
				
				//联络人
				addressBook1.setAbName(bo.getContacter());
				addressBook1.setAbDuty(bo.getContacterDuty());
				addressBook1.setAbMobile(bo.getContacterMobile());
				addressBook.setLdId(bo.getLdId());
				
				if (bo.getContacterId() == null) {
					//添加联络人
					addressBook1.setAbRole(CONTACTER);
					addressBook1.setOrgId(bo.getOrgId());
					addressBook1.setOrgCode(bo.getOrgCode());
					addressBookService.insert(addressBook1);
				} else {
					//修改联络人
					addressBook1.setAbId(bo.getContacterId());
					addressBookService.update(addressBook1);
				}
			}
			if (DW_TYPE.equals(bo.getLdType())) {
				addressBook.setAbName(bo.getLxr());
				addressBook.setAbDuty("业务热线");
				addressBook.setAbMobile(bo.getLxrMobile());
				
				addressBook.setLdId(bo.getLdId());
				if (bo.getLxrId() == null) {
					//添加联系人
					addressBook.setAbRole(LXR);
					addressBook.setOrgId(bo.getOrgId());
					addressBook.setOrgCode(bo.getOrgCode());
					addressBookService.insert(addressBook);
				} else {
					//修改联系人
					addressBook.setAbId(bo.getLxrId());
					addressBookService.update(addressBook);
				}
			}
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 批量添加联动单位数据
	 */
	@ResponseBody
	@RequestMapping("/addLdOrg")
	public Object addLdOrg(HttpServletRequest request, HttpSession session, ModelMap map,
		String info) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String result = "fail";
		
		JSONArray jsonArray = JSONArray.fromObject(info);
		List<LdOrg> list = JSON.parseArray(jsonArray.toString(), LdOrg.class);
		
		for (LdOrg bo : list) {
			bo.setCreater(userInfo.getUserId());
		}
		
		if (list.size() > 0) {
			ldOrgService.batchInsert(list);
			result="success";
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
		LdOrg bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		Long id = Long.parseLong(request.getParameter("id"));
		bo.setLdId(id);
		
		boolean delResult = ldOrgService.deleteById(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 获取知识库数数据
	 */
	@ResponseBody
	@RequestMapping("/getTreeData")
	public Object getTreeData(HttpServletRequest request, HttpSession session, ModelMap map,
		LdOrg bo) {
		String id = request.getParameter("id");
		bo.setLdType(id);
		List<LdOrg> list = ldOrgService.searchByLdType(bo);
		JSONArray jsonArray = new JSONArray();
		for (LdOrg tmp : list) {
			JSONObject json = new JSONObject();
			json.put("id",tmp.getLdId());
			json.put("pId",tmp.getLdType());
			json.put("name",tmp.getLdName());
			json.put("isParent","false");
			json.put("ldType",tmp.getLdType());
			json.put("orgCode",tmp.getOrgCode());
			json.put("orgId",tmp.getOrgId());
			jsonArray.add(json);
		}
		//String ldOrg = jsonArray.toJSONString();
		return jsonArray;
	}
}