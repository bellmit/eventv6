package cn.ffcs.zhsq.internetEnterprise;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.INumberConfigureService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.spring.RequestionService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.event.Patrol;
import cn.ffcs.zhsq.mybatis.domain.requestion.CorpLink;
import cn.ffcs.zhsq.mybatis.domain.requestion.ReqLink;
import cn.ffcs.zhsq.mybatis.domain.requestion.Requestion;
import cn.ffcs.zhsq.requestion.service.ICorpLinkService;
import cn.ffcs.zhsq.requestion.service.IReqLinkService;
import cn.ffcs.zhsq.requestion.service.IRequestionService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

@Controller("internetEnterpriseController")
@RequestMapping("/zhsq/internetEnterprise")
public class InternetEnterpriseController extends ZZBaseController{

	@Autowired
	private IRequestionService requestionService; //注入诉求表模块服务
	@Autowired
	private RequestionService workflowService;
	@Autowired
	private IReqLinkService reqLinkService;//注入诉求联动单位模块服务
	@Autowired
	private INumberConfigureService numberConfigureService;
	@Autowired
	private IAttachmentService attachmentService;
	@Autowired
    private FileUploadService fileUploadService;
	@Autowired
    private UserManageOutService userManageService;
	@Autowired
    private OrgSocialInfoOutService orgSocialInfoService;
	
	static String formTypeId = "";
	static String workflowId = "";
	static String formTypeId_ = "";
	static String workflowId_ = "";
	private String REQ_ATTACHMENT_TYPE = "requestionAtta";//诉求表附件类型
	private String REQ_LINK_ATTACHMENT_TYPE = "reqLinkAtta";//诉求表附件类型
	@Value("${zqhl_code}")
	private String ORG_CODE;

	/**
	 * 列表页面
	 */
	@RequestMapping("/getDateList")
	public Object getDateList(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value = "type", required = false) String type) {
		//数据字典
		map.addAttribute("caseTypeDict", ConstantValue.CASE_TYPE_CODE);
		return "/zzgl/internetEnterprise/list_internetEnterprise_all.ftl";
	}

	
	/**
	 * 新增列表页面
	 */
	@RequestMapping("/addView")
	public Object addView(HttpServletRequest request, HttpSession session, ModelMap map) {
		//数据字典
		map.addAttribute("caseTypeDict", ConstantValue.CASE_TYPE_CODE);
		return "/zzgl/internetEnterprise/add_internetEnterprise.ftl";
	}

	

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listDataAll")
	public Object listDataAll(HttpServletRequest request, HttpSession session, ModelMap map,
		@RequestParam(value = "keyWord", required = false) String keyWord,
		@RequestParam(value = "type", required = false) String type,
		@RequestParam(value = "happenTimeStr", required = false) String happenTimeStr,
		@RequestParam(value = "endTimeStr", required = false) String endTimeStr,
		@RequestParam(value = "wfCurOrg", required = false) String wfCurOrg,
		int page, int rows) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		initParams();
		//params.put("userId", userInfo.getUserId());
		params.put("formTypeId", Long.parseLong(formTypeId));
		
		params.put("orgCode", userInfo.getOrgCode());
		params.put("keyWord", keyWord);
		params.put("type", type);
		params.put("wfCurOrg", wfCurOrg);
		params.put("happenTimeStr", happenTimeStr);
		params.put("endTimeStr", endTimeStr);
		EUDGPagination pagination = requestionService.searchList_Main(page, rows, params);
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id, String tasktype) {
		
		initParams();
		Requestion bo = null;
		Map<String, Object> params = new HashMap<String, Object>();
		
		if("1".equals(tasktype)){
			bo = requestionService.searchById(id,formTypeId);
			List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(bo.getReqId(), "", "requestion");
			map.addAttribute("taskList", initDetail(bo.getReqId(), taskList));
			List<Map<String, Object>> temp = toShowBackList_(id);
			if(temp!=null&&!temp.isEmpty()&&temp.size()>0){
				map.addAttribute("isBack", "1");
			}
		}else {
			ReqLink reqLink = reqLinkService.searchById(id);
			bo = requestionService.searchById(reqLink.getReqId(),formTypeId);
			List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(id, "", "linkageunit");
			map.addAttribute("taskList", initDetailLink(taskList));
			params.put("rluId", id);
		}
		
		params.put("reqId", bo.getReqId());
		params.put("formTypeId_", formTypeId_);
		List<ReqLink> childList = reqLinkService.searchList(params);
		map.addAttribute("childList", childList);
		map.addAttribute("REQ_ATTACHMENT_TYPE",REQ_ATTACHMENT_TYPE);
		map.addAttribute("REQ_LINK_ATTACHMENT_TYPE", REQ_LINK_ATTACHMENT_TYPE);
		map.addAttribute("bo", bo);
		return "/zzgl/internetEnterprise/detail_internetEnterprise.ftl";
	}

	
	/**
	 * 详情页面
	 */
	@RequestMapping("/details")
	public Object details(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id, String tasktype) {
		
		initParams();
		Requestion bo = null;
		Map<String, Object> params = new HashMap<String, Object>();
		bo = requestionService.searchBy_id(id);
		
		params.put("reqId", bo.getReqId());
		params.put("formTypeId_", formTypeId_);
		map.addAttribute("REQ_ATTACHMENT_TYPE", REQ_ATTACHMENT_TYPE);
		map.addAttribute("REQ_LINK_ATTACHMENT_TYPE", REQ_LINK_ATTACHMENT_TYPE);
		map.addAttribute("bo", bo);
		return "/zzgl/internetEnterprise/details_internetEnterprise.ftl";
	}
	
	
	private List<Map<String, Object>> initDetail(Long reqId,List<Map<String, Object>> taskList){
		
		for(Map<String, Object> task : taskList){
			String bizId = ((BigDecimal) task.get("TASK_ID")).toString();
			task.put("fileList", attachmentService.findByBizId(Long.valueOf(bizId.toString()), REQ_ATTACHMENT_TYPE));
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("formTypeId_", formTypeId_);
		params.put("reqId", reqId);
		params.put("status", "2");
		List<ReqLink> reqLinkList = reqLinkService.searchList(params);
		for(ReqLink re : reqLinkList){
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("TASK_NAME", "联动单位办理");
			temp.put("TRANSACTOR_NAME", re.getLinkageUnitName());
			temp.put("END_TIME", re.getUpdateTimeStr());
			temp.put("REMARKS", re.getOverview());
			params.clear();
			params.put("rluId", re.getRluId());
			params.put("formTypeId_", formTypeId_);
			List<Map<String,Object>> tempList = reqLinkService.queryWFtaskId(params);
			List<Attachment> fileList = new ArrayList<Attachment>();
			for(Map<String,Object> tempFile : tempList){
				String bizId = ((BigDecimal) tempFile.get("TASK_ID")).toString();
				fileList.addAll(attachmentService.findByBizId(Long.parseLong(bizId), "REQ_LINK_WFTASK"));
			}
			temp.put("fileList",fileList);
			
			taskList.add(temp);
		}
		return taskList;
	}
	
	private List<Map<String, Object>> initDetailLink(List<Map<String, Object>> taskList){
		
		for(Map<String, Object> task : taskList){
			String bizId = ((BigDecimal) task.get("TASK_ID")).toString();
			List<Attachment> attr = attachmentService.findByBizId(Long.parseLong(bizId), "REQ_LINK_WFTASK");
			task.put("fileList", attr);
		}
		return taskList;
	}
	
	
	/**
	 * PC请求提交保存数据
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws ParseException 
	 */
	@RequestMapping("/pcSave")
	public String appSave(HttpServletRequest request,HttpServletResponse response,HttpSession session, ModelMap map,
		Requestion bo,@RequestParam(value = "attachmentId", required = false) Long[] attachmentIds ) throws JsonParseException, JsonMappingException, IOException, ParseException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String result = "保存失败";
				if (bo.getReqId() == null) {
					if ("0".equals(bo.getState())){
							//新增
							long save = 0;
							String code = null;
							try {
								code = numberConfigureService.getNumber(ORG_CODE, "09");
							} catch (Exception e) {
								e.printStackTrace();
							}
							if(!StringUtils.isBlank(code)){
								try {
									bo.setCode(code);
									bo.setSource("2");
									bo.setState("0");
									bo.setUserId(userInfo.getUserId());
									bo.setUserName(userInfo.getPartyName());
									bo.setCreatTime(null);
									/*String expectTime = bo.getExpectTimeStr();
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									bo.setExpectTime(sdf.parse(expectTime));*/
									bo.setReqObjCode(userInfo.getOrgCode());
									bo.setOrgId(userInfo.getOrgId());
									bo.setOrgName(userInfo.getOrgName());
									
									save = requestionService.insert(bo);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if(save != 0){
								result = "保存草稿成功";
								resultMap.put("result", result);
							}
							if(null != attachmentIds){
								Long reqId = Long.valueOf(bo.getReqId());
								saveAtta(reqId, REQ_ATTACHMENT_TYPE, attachmentIds);
							}
					}else {
						resultMap.put("userId", userInfo.getUserId());
						resultMap.put("userName", userInfo.getPartyName());
						resultMap.put("orgId", userInfo.getOrgId());
						resultMap.put("orgName", userInfo.getOrgName());
						
						resultMap.put("type", "requestion");
						String userIds = "";
						String orgIds = "";
						try {
							OrgSocialInfoBO orgSocialInfo= orgSocialInfoService.selectOrgSocialInfoByOrgCode(ORG_CODE);
							orgIds = orgSocialInfo.getOrgId().toString();
						} catch (Exception e) {
							e.printStackTrace();
							resultMap.put("status", "0");
							resultMap.put("message", "中心组织获取失败");
							System.out.println("-------------中心组织获取失败---------------ORG_CODE="+ORG_CODE);
						}
						try {
							List<cn.ffcs.uam.bo.UserInfo> userInfoList = userManageService.findUserByOrgCode(ORG_CODE);
							if(!userInfoList.isEmpty()){
								userIds = userInfoList.get(0).getUserId().toString();
							}
						} catch (Exception e) {
							e.printStackTrace();
							resultMap.put("status", "0");
							resultMap.put("message", "中心人员获取失败");
							System.out.println("-------------中心组织获取失败----------------");
						}
						
						resultMap.put("userIds", userIds);
						resultMap.put("curOrgIds", orgIds);
						
						String code = null;
						try {
							code = numberConfigureService.getNumber(ORG_CODE, "09");
						} catch (Exception e) {
							resultMap.put("status", "0");
							resultMap.put("message", "编号生成失败");
							e.printStackTrace();
						}
						if(!StringUtils.isBlank(code)){
							bo.setCode(code);
							bo.setSource("2");
							bo.setState("1");
							bo.setUserId(userInfo.getUserId());
							bo.setUserName(userInfo.getPartyName());
							bo.setCreatTime(new Date());
							/*String expectTime = bo.getExpectTimeStr();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							bo.setExpectTime(sdf.parse(expectTime));*/
							resultMap = requestionService.insert(bo,resultMap);
						}
						if("1".equals(resultMap.get("status"))){
							result = "保存成功";
						}
						if(null != attachmentIds){
							Long reqId = Long.valueOf(resultMap.get("reqId").toString());
							saveAtta(reqId, REQ_ATTACHMENT_TYPE, attachmentIds);
						}
				}
			}
			
		map.addAttribute("result", result);
		return "/zzgl/internetEnterprise/result_head.ftl";
	}
	
	/**
	 * 更新页面
	 * @param session
	 * @param reqId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toEdit")
	public String toEdit(HttpSession session, @RequestParam(value = "reqId") Long reqId, ModelMap map){
		map.addAttribute("caseTypeDict", ConstantValue.CASE_TYPE_CODE);
		Requestion requestion = null;
		Map<String, Object> params = new HashMap<String, Object>();
		requestion = requestionService.searchBy_id(reqId);
		
		params.put("reqId", requestion.getReqId());
		params.put("formTypeId_", formTypeId_);
		List<ReqLink> childList = reqLinkService.searchList(params);
		map.addAttribute("childList", childList);
		map.addAttribute("REQ_ATTACHMENT_TYPE",REQ_ATTACHMENT_TYPE);
		map.addAttribute("REQ_LINK_ATTACHMENT_TYPE", REQ_LINK_ATTACHMENT_TYPE);
		map.addAttribute("requestion", requestion);
		return "/zzgl/internetEnterprise/edit_internetEnterprisel.ftl";
	}
	
	
	/**
	 * 更新
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping("/update")
	public String update(HttpServletRequest request, HttpSession session, ModelMap map,
			Requestion bo,@RequestParam(value = "attachmentId", required = false) Long[] attachmentIds) throws ParseException {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		Requestion requestion = null;
		Date date = null; 
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		if ("0".equals(bo.getState())){
			requestion = requestionService.searchBy_id(bo.getReqId());
			/*date = format.parse(bo.getExpectTimeStr()); 
			requestion.setExpectTime(date);*/
			requestion.setType(bo.getType());
			requestion.setCreatTime(null);
			requestion.setTypeStr(bo.getTypeStr());
			requestion.setTitle(bo.getTitle());
			requestion.setContent(bo.getContent());
			requestion.setReqObjName(bo.getReqObjName());
			requestion.setLinkMan(bo.getLinkMan());
			requestion.setLinkTel(bo.getLinkTel());
			requestion.setDesc(bo.getDesc());
			requestionService.update(requestion);
		}else {
			try {
				String userIds = "";
				String orgIds = "";
				try {
					OrgSocialInfoBO orgSocialInfo= orgSocialInfoService.selectOrgSocialInfoByOrgCode(ORG_CODE);
					orgIds = orgSocialInfo.getOrgId().toString();
				} catch (Exception e) {
					e.printStackTrace();
					resultMap.put("status", "0");
					resultMap.put("message", "中心组织获取失败");
					System.out.println("-------------中心组织获取失败---------------ORG_CODE="+ORG_CODE);
				}
				try {
					List<cn.ffcs.uam.bo.UserInfo> userInfoList = userManageService.findUserByOrgCode(ORG_CODE);
					if(!userInfoList.isEmpty()){
						userIds = userInfoList.get(0).getUserId().toString();
					}
				} catch (Exception e) {
					e.printStackTrace();
					resultMap.put("status", "0");
					resultMap.put("message", "中心人员获取失败");
					System.out.println("-------------中心组织获取失败----------------");
				}
				try {
					resultMap.put("userId", userInfo.getUserId());
					resultMap.put("userName", userInfo.getPartyName());
					resultMap.put("type", "requestion");
					resultMap.put("orgId", userInfo.getOrgId());
					resultMap.put("orgName", userInfo.getOrgName());
					resultMap.put("userIds", userIds);
					resultMap.put("curOrgIds", orgIds);
					resultMap.put("formId", bo.getReqId());
					resultMap = workflowService.startRequestionFlow(resultMap);

				} catch (Exception e) {
					resultMap.put("status", "0");
					resultMap.put("message", "工作流启动失败");
					System.out.println("-----工作流启动失败------");
					e.printStackTrace();
				}
				if ("工作流启动失败".equals(resultMap.get("message"))){
					return result;
				}
				requestion = requestionService.searchBy_id(bo.getReqId());
			/*	date = format.parse(bo.getExpectTimeStr()); 
				requestion.setExpectTime(date);*/
				requestion.setType(bo.getType());
				requestion.setTypeStr(bo.getTypeStr());
				requestion.setState("1");
				requestion.setCreatTime(new Date());
				requestion.setTitle(bo.getTitle());
				requestion.setContent(bo.getContent());
				requestion.setReqObjName(bo.getReqObjName());
				requestion.setLinkMan(bo.getLinkMan());
				requestion.setLinkTel(bo.getLinkTel());
				requestion.setDesc(bo.getDesc());
				requestionService.update(requestion);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("-----工作流提交失败-----");
			}
		}
		if(null!=attachmentIds){
			saveAtta(bo.getReqId(), REQ_ATTACHMENT_TYPE, attachmentIds);
		}
		result = "success";
		return result;
	}
	

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		Requestion bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = requestionService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	
	private JSONObject sendTo(Requestion requestion,String urlDomain) throws Exception{
		
		String url = urlDomain+"/mobile/appeal/v2/out/updateReqState.jhtml?reqId="+requestion.getKeyId().toString()+"&status=3";
		JSONObject json = HttpUtil.invokeOutInterface(url, "POST", "", null, null);
		return json;
	}
	
	
	
	private Boolean downFile(Long keyId, String fileName, String urlstr) throws IOException{
		
		InputStream fis = null;
		if(StringUtils.isEmpty(urlstr)) return null;
		try {
			InputStream inputStream = HttpUtil.returnBitMap(urlstr);
			byte[] bytes = HttpUtil.readInputStream(inputStream);
			String filePath = this.fileUploadService.uploadSingleFile(new Date().getTime()+".gif", bytes, "zhsq_event", "attachment");
			
			Attachment attachment = new Attachment();
			attachment.setAttachmentType(REQ_ATTACHMENT_TYPE);
			attachment.setBizId(keyId);
			attachment.setCreateTime(new Date());
			attachment.setEventSeq("1");
			attachment.setFileName(fileName);
			attachment.setFilePath(filePath);
			attachment.setStatus("001");
			
			attachmentService.saveAttachment(attachment);
			fis.close();
		} catch (Exception e) {
			System.out.println("----------app下载附件失败----------------urlstr="+urlstr);
			e.printStackTrace();
			fis.close();
		}
		
		return null;
	}
	
	private void initParams() {
		
		if(StringUtils.isBlank(formTypeId)){
			formTypeId = workflowService.getFormTypeId("requestion");
		}
		if(StringUtils.isBlank(workflowId)){
			workflowId = workflowService.getWorkflowId("requestion");
		}
		if(StringUtils.isBlank(formTypeId_)){
			formTypeId_ = workflowService.getFormTypeId("linkageunit");
		}
		if(StringUtils.isBlank(workflowId_)){
			workflowId_ = workflowService.getWorkflowId("linkageunit");
		}
	}
	
	/**
	 * 保存附件信息
	 * @param id 主表ID
	 * @param bizType 附件类型
	 * @param attachmentIds 附件ID
	 */
	private void saveAtta(Long id,String bizType,Long[] attachmentIds) {
		if(attachmentIds!=null && attachmentIds.length>0){
			attachmentService.updateBizId(id, bizType, attachmentIds);
		}
	}
	
	private Map<String, Object> queryUserAndOrg(Long orgId){
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String userIds = "";
		OrgSocialInfoBO orgSocialInfo = orgSocialInfoService.findByOrgId(orgId);
		String orgIds = orgSocialInfo.getOrgId().toString();
		List<cn.ffcs.uam.bo.UserInfo> userInfoList = userManageService.findUserByOrgCode(orgSocialInfo.getOrgCode());
		if(!userInfoList.isEmpty()){
			userIds = userInfoList.get(0).getUserId().toString();
		}
		
		resultMap.put("userIds", userIds);
		resultMap.put("curOrgIds", orgIds);
		
		return resultMap;
	}

	private List<Map<String, Object>> toShowBackList_(Long reqId){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("reqId", reqId);
		params.put("formTypeId_", formTypeId_);
		params.put("state", "8");
		List<ReqLink> list = reqLinkService.showBackList(params);
		List<Map<String, Object>> temp = new ArrayList<Map<String,Object>>();
		for(ReqLink t : list){
			List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(t.getRluId(), "", "linkageunit");
			for(Map<String, Object> task : taskList){
				String name = (String) task.get("TASK_NAME");
				String type = task.get("OPERATE_TYPE").toString();
				if("单位办理".equals(name)&&"2".equals(type)){
					String endTime = task.get("END_TIME").toString();
					if(!StringUtils.isBlank(endTime)){
						task.put("UPDATETIMESTR", endTime.substring(0, 10));
					}
					task.put("LINKAGEUNITNAME", t.getLinkageUnitName());
					task.put("LINKMAN", t.getLinkMan());
					task.put("LINKMANTEL", t.getLinkManTel());
					temp.add(task);
				}
			}
		}
		
		return temp;
	}
	
}
