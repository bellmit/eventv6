package cn.ffcs.zhsq.addressBook.controller;

import java.util.HashMap;
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
import cn.ffcs.zhsq.addressBook.service.AddressBookService;
import cn.ffcs.zhsq.ldOrg.service.LdOrgService;
import cn.ffcs.zhsq.mybatis.domain.addressBook.AddressBook;
import cn.ffcs.zhsq.mybatis.domain.ldOrg.LdOrg;
import cn.ffcs.zhsq.utils.ConstantValue;

/**   
 * @Description: 通讯录表模块控制器
 * @Author: linwd
 * @Date: 04-13 15:24:43
 * @Copyright: 2018 福富软件
 */ 
@Controller("addressBookController")
@RequestMapping("/zhsq/addressBook")
public class AddressBookController {

	@Autowired
	private AddressBookService addressBookService; //注入通讯录表模块服务
	
	@Autowired
	private LdOrgService ldOrgService;//注入联动单位模块服务
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value = "type", required = false) String type) {
		//数据字典
		map.addAttribute("caseTypeDict", ConstantValue.CASE_TYPE_CODE);
		return "/zzgl/addressBook/list_addressBook.ftl";
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
		params.put("keyWord", request.getParameter("keyWord"));
		params.put("telPhone", request.getParameter("telPhone"));
		EUDGPagination pagination = addressBookService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		AddressBook bo = addressBookService.searchById(id);
		map.addAttribute("bo", bo);
		return "/zzgl/addressBook/detail_addressBook.ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/toEdit")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
		Long abId) {
		if (abId != null) {
			AddressBook bo = addressBookService.searchById(abId);
			map.put("bo", bo);
		}
		return "/zzgl/addressBook/edit_addressBook.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		AddressBook bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		LdOrg ldOrg = new LdOrg();
		if (bo.getAbId() == null) { //新增
			Long id = addressBookService.insert(bo);
			if (id != null && id > 0) {
				result = "success";
			};
		} else { //修改
			
		/*	bo.setAbRole(bo.getAbRole());
			bo.setAbName(bo.getAbName());
			bo.setAbMobile(bo.getAbMobile());
			bo.setAbDuty(bo.getAbDuty());*/
			String ldName = bo.getLdName(); 
			boolean updateResult = addressBookService.update(bo);
			bo = addressBookService.searchById(bo.getAbId());
			ldOrg = ldOrgService.searchById(bo.getLdId());
			ldOrg.setLdName(ldName);
			boolean updateLdResult = ldOrgService.update(ldOrg);
			if (updateResult && updateLdResult) {
				result = "success";
			}
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
		AddressBook bo, @RequestParam(value = "idStr") String idStr) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int result = 0;
		String[] ids = idStr.split(",");
		for(int index = 0, length = ids.length; index < length; index++){
			long abId = Long.valueOf(ids[index].toString());
			bo = addressBookService.searchById(abId);
			boolean delResult = addressBookService.delete(bo);
			LdOrg ldOrg = ldOrgService.searchById(bo.getLdId());
		//	boolean delLdResult = ldOrgService.deleteById(ldOrg);
			if (delResult) {
				result = ids.length;
			}
			resultMap.put("result", result);
		}
	
		return resultMap;
	}

}