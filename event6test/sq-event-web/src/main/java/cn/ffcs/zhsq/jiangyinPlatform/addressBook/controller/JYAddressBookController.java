package cn.ffcs.zhsq.jiangyinPlatform.addressBook.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.jiangyinPlatform.addressBook.service.IJYAddressBookService;
import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.addressBook.JYAddressBook;

/**   
 * @Description: 通讯录表模块控制器
 * @Author: 林树轩
 * @Date: 03-06 17:20:57
 * @Copyright: 2020 福富软件
 */ 
@Controller("jyAddressBookController")
@RequestMapping("/zhsq/jiangyinPlatform/jyAddressBook")
public class JYAddressBookController {

	@Autowired
	private IJYAddressBookService jyAddressBookService; //注入通讯录表模块服务
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		return "/map/jiangYinPlatform/addressBook/address_book_list.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,
		int page, int rows) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //替换成本地常量
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", request.getParameter("name"));
		params.put("creator", userInfo.getUserId());
		EUDGPagination pagination = jyAddressBookService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
		String id) {
		if (id != null) {
			JYAddressBook bo = jyAddressBookService.searchByUUID(id);
			map.put("bo", bo);
		}
		return "/map/jiangYinPlatform/addressBook/address_book_form.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		JYAddressBook bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //替换成本地常量
		String tel = bo.getTel();
		Long userId = userInfo.getUserId();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		String resultMsg = "保存失败!";
		if (StringUtils.isBlank(bo.getUuid())) { //新增
			//判断电话是否重复
			boolean isExist = jyAddressBookService.checkExist(tel, userId);
			if (isExist) {
				resultMsg = "已存在相同号码!";
			} else {
				bo.setCreator(userId);  //设置创建人
				bo.setUpdator(userId);  //设置更新人
				String id = jyAddressBookService.insert(bo);
				if (StringUtils.isNotBlank(id)) {
					result = "success";
					resultMsg = "保存成功!";
				};
			}
		} else { //修改
			//将该条记录的原数据和修改后的数据比较，若电话号码没改，则不用判断重复性
			JYAddressBook temp = jyAddressBookService.searchByUUID(bo.getUuid());
			boolean isExist = false;
			if (!temp.getTel().equals(bo.getTel())) {
				isExist = jyAddressBookService.checkExist(tel, userId);
			}
			
			if (isExist) {
				resultMsg = "已存在相同号码!";
			} else {
				bo.setUpdator(userId);  //设置更新人
				boolean updateResult = jyAddressBookService.update(bo);
				if (updateResult) {
					result = "success";
					resultMsg = "保存成功!";
				}
			}
		}
		resultMap.put("result", result);
		resultMap.put("resultMsg", resultMsg);
		return resultMap;
	}

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		JYAddressBook bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION"); //替换成本地常量
		bo.setUpdator(userInfo.getUserId());  //设置更新人
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = jyAddressBookService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
}