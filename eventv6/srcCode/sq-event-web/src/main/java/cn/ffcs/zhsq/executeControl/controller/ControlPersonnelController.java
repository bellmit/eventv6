package cn.ffcs.zhsq.executeControl.controller;

import cn.ffcs.common.GetFileSuffix;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.executeControl.service.IControlPersonnelService;
import cn.ffcs.zhsq.executeControl.service.IMonitorTaskService;
import cn.ffcs.zhsq.mybatis.domain.executeControl.ControlPersonnel;
import cn.ffcs.zhsq.mybatis.domain.executeControl.MonitorTask;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**   
 * @Description: 布控库对象模块控制器
 * @Author: dtj
 * @Date: 07-17 09:21:58
 * @Copyright: 2020 福富软件
 */ 
@Controller("controlPersonnelController")
@RequestMapping("/zhsq/event/controlPersonnel")
public class ControlPersonnelController {

	@Autowired
	private IControlPersonnelService controlPersonnelService; //注入布控库对象模块服务

	@Autowired
	private IBaseDictionaryService baseDictionaryService;//字典
	
	@Autowired
	private IAttachmentService attachmentService;

	@Autowired
	private IMonitorTaskService monitorTaskService; //注入布控任务管理模块服务

	/**
	 * 附件所属模块类型
	 */
	private final static String BKK_FORM  = "bkk_form";//布控库，附件(损坏部位照片)


	/**
	 * 列表页面
	 * @param request
	 * @param session
	 * @param map
	 * @param libIds
	 * @param id
	 * @param libType
	 * @param controlTaskId
	 * @return
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map,String libIds,
						Long id,String libType,String controlTaskId) {
		try {
			String token = controlPersonnelService.getToken();
			MonitorTask monitorTask = monitorTaskService.searchById(id, token);
			ControlPersonnel bo = new ControlPersonnel();
			bo.setControlLibraryId(libIds);
			bo.setLibType(libType);
			bo.setControlTaskId(controlTaskId);
			map.put("deviceDis",monitorTask.getDeviceIds());
			map.put("bo",bo);
			map.put("id",id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/zzgl/executeControl/list_controlPersonnel.ftl";
	}

	/**
	 * 列表数据
	 * @param request
	 * @param bo
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, ControlPersonnel bo,HttpSession session, ModelMap map) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("controlLibraryId",bo.getControlLibraryId());
		params.put("controlTaskId",bo.getControlTaskId());
		params.put("name",bo.getName());
		params.put("identityCardNumber",bo.getIdentityCardNumber());
		params.put("mobile",bo.getMobile());
		EUDGPagination pagination = null;
		try {
			pagination = controlPersonnelService.searchList(bo,params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

	/**
	 * 表单页面
	 * @param request
	 * @param session
	 * @param map
	 * @param id
	 * @param controlLibraryId
	 * @param libType
	 * @param controlTaskId
	 * @return
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id,String controlLibraryId,String libType,String controlTaskId) {
		ControlPersonnel bo = new ControlPersonnel();
		bo.setControlLibraryId(controlLibraryId);
		bo.setLibType(libType);
		bo.setControlTaskId(controlTaskId);
		if (id != null) {
			try {
				bo = controlPersonnelService.searchById(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//附件
			/*List<Attachment> attList = attachmentService.findByBizId(id, BKK_FORM);
			map.put("attList", formateAttList(attList));*/
		}
		map.put("bo", bo);
		map.put("libTypeName",baseDictionaryService.changeCodeToName(ConstantValue.LIB_TYPE,libType,null));
		map.put("NATIONALITY", ConstantValue.NATIONALITY);
		map.addAttribute("IMG_URL", App.IMG.getDomain(session));
		map.addAttribute("COMPONENTS_DOMAIN", App.COMPONENTS.getDomain(session));
		map.addAttribute("RS_DOMAIN", App.RS.getDomain(session));
		map.addAttribute("skyDomain", App.SKY.getDomain(session));
		map.addAttribute("uiDomain", App.UI.getDomain(session));
		return "/zzgl/executeControl/form_controlPersonnel.ftl";
	}

	/**
	 * 保存数据
	 * @param request
	 * @param session
	 * @param map
	 * @param bo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
					   ControlPersonnel bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //替换成本地常量
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		try {
			String token = controlPersonnelService.getToken();
			if (bo.getId() == null ) { //新增
				bo.setCreator(userInfo.getUserId());  //设置创建人
				Long id = controlPersonnelService.insert(bo,token);
				opertionAtta(bo.getAttList(),id,userInfo,BKK_FORM);//保存附件
				if (id > 0){
					result = "success";
				};
			} else { //修改
				bo.setUpdator(userInfo.getUserId());  //设置更新人
				boolean updateResult = controlPersonnelService.update(bo,token);
				opertionAtta(bo.getAttList(),bo.getId(),userInfo,BKK_FORM);//保存附件
				if (updateResult) {
					result = "success";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 删除数据
	 * @param request
	 * @param session
	 * @param map
	 * @param bo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		ControlPersonnel bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION"); //替换成本地常量
		bo.setUpdator(userInfo.getUserId());  //设置更新人
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		try {
			String token = controlPersonnelService.getToken();
			boolean delResult = controlPersonnelService.delete(bo,token);
			if (delResult) {
				result = "success";
			}
			resultMap.put("result", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 用Attachment类中的title字段表示其后缀，方便页面上展示
	 * @param attList
	 * @return
	 */
	private List<Attachment> formateAttList(List<Attachment> attList) {
		for (Attachment att : attList) {
			att.setTitle(GetFileSuffix.getFileSuffixByPath(att.getFilePath()));
		}
		return attList;
	}

	/**
	 * 保存附件数据
	 * @param cm
	 * @param id 附件业务id
	 * @param userInfo 操作用户
	 * @param type 附件业务类型
	 */
	private void opertionAtta(List<Attachment> atts, long id, UserInfo userInfo, String type) {
		//附件
		if(atts != null && atts.size() > 0){
			attachmentService.deleteByBizId(id, null);
			for(Attachment att : atts){
				if(StringUtils.isEmpty(att.getFilePath()))
					continue;
				att.setAttachmentType(att.getAttachmentType());
				att.setBizId(id);
				att.setCreateTime(new Date());
				att.setCreatorId(userInfo.getPartyId());
				att.setCreatorName(userInfo.getPartyName());
			}
			attachmentService.saveAttachment(atts);
		}
	}

}