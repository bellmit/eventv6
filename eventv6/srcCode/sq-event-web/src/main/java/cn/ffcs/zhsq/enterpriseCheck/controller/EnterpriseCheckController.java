package cn.ffcs.zhsq.enterpriseCheck.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import cn.ffcs.system.publicUtil.EUDGPagination;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.ffcs.gmis.busInspectionRecord.service.IBusInspectionRecordService;
import cn.ffcs.gmis.mybatis.domain.busInspectionRecord.BusInspectionRecord;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.wap.util.DateUtils;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;

/**
 * 环保企业巡查
 * @author zhangls
 *
 */
@Controller
@RequestMapping(value="/zhsq/enterpriseCheck")
public class EnterpriseCheckController extends ZZBaseController {
	@Autowired
	private IBusInspectionRecordService busInspectionRecordService;
	
	@Autowired
	private IAttachmentService attachmentService;
		
	private String EP_PATH = "/zzgl/enterpriseCheck/environmentalProtection";//环保企业巡查页面路径
	private String EP_ATTACHMENT_TYPE = "environmentalProtection";//环保企业附件类型
	private String MODULE = "Environmental_Protection";//地图标注模块编码
	private String EP_MARKER_TYPE = "ENVIRONMENTAL_PROTECTION";//环保企业巡查地图定位类型
	private String EP_NUM_BIZ_CODE = "06";//环保企业巡查检查编码编号类型
	private String IS_DEL_DRAFT = "2";//巡查记录草稿状态
	private String EP_INSPECT_TYPE_PCODE = "A001093199855";
	
	/**
	 * 跳转列表页面
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public String toList(HttpSession session, ModelMap map) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		
		String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		String gridName = defaultGridInfo.get(KEY_START_GRID_NAME).toString();
		
		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("gridName", gridName);
		map.addAttribute("EP_INSPECT_TYPE_PCODE", EP_INSPECT_TYPE_PCODE);
		
		return EP_PATH + "/list_epEnterpriseCheck.ftl";
	}
	
	/**
	 * 跳转新增/编辑页面
	 * @param session
	 * @param birId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd(HttpSession session, 
			@RequestParam(value = "birId", required = false) Long birId,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		BusInspectionRecord inspection = null;
		int markerOperation = ConstantValue.ADD_MARKER;
		
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long defaultGridId = -1L;
		
		try {
			defaultGridId = Long.valueOf(defaultGridInfo.get(KEY_START_GRID_ID).toString());
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
		
		if(birId != null && birId > 0) {
			inspection = busInspectionRecordService.findBusInspectionRecordById(birId);
			markerOperation = ConstantValue.EDIT_MARKER;
		}
		
		if(inspection == null) {
			inspection = new BusInspectionRecord();
			
			//设置默认网格信息
			String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString(),
				   gridName = defaultGridInfo.get(KEY_START_GRID_NAME).toString();
			
			inspection.setGridId(defaultGridId);
			inspection.setGridCode(infoOrgCode);
			inspection.setGridName(gridName);
			inspection.setType(BusInspectionRecord.TYPE.ENVIRONMENTAL_PROTECTION.toString());
			inspection.setStartTimeStr(DateUtils.getToday());
		} else {
			formatDataOut(inspection);
		}
		
		if(defaultGridId > 0) {
			map.addAttribute("defaultGridId", defaultGridId);
		}
		map.addAttribute("inspection", inspection);
		map.addAttribute("EP_ATTACHMENT_TYPE", EP_ATTACHMENT_TYPE);//附件上传类型
		map.addAttribute("createUserId", userInfo.getUserId());//附件上传人员
		map.put("imgDownPath", App.IMG.getDomain(session));//用于附件上传组件中的缩略图展示
		map.addAttribute("markerOperation", markerOperation);//标注操作
		map.addAttribute("module", MODULE);//地图标注模块编码
		map.addAttribute("EP_INSPECT_TYPE_PCODE", EP_INSPECT_TYPE_PCODE);
		
		return EP_PATH + "/add_epEnterpriseCheck.ftl";
	}
	
	/**
	 * 跳转详情页面
	 * @param session
	 * @param birId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toDetail")
	public String toDetail(HttpSession session, 
			@RequestParam(value = "birId") Long birId,
			ModelMap map) {
		Map<String, Object> extraParam = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		extraParam.put("userOrgCode", userInfo.getOrgCode());
		
		BusInspectionRecord inspection = busInspectionRecordService.findInspectionRecordById(birId, extraParam);
		
		if(inspection == null) {
			inspection = new BusInspectionRecord();
		} else {
			formatDataOut(inspection);
		}
		
		map.addAttribute("inspection", inspection);
		map.addAttribute("EP_ATTACHMENT_TYPE", EP_ATTACHMENT_TYPE);
		map.addAttribute("markerOperation", ConstantValue.WATCH_MARKER);//标注操作
		map.addAttribute("module", MODULE);
		
		return EP_PATH + "/detail_epEnterpriseCheck.ftl";
	}
	
	/**
	 * 新增/编辑企业巡查信息
	 * @param session
	 * @param inspection
	 * @param attachmentIds
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveEnterpriseCheck")
	public ResultObj saveEnterpriseCheck(HttpSession session, 
			@ModelAttribute(value = "inspection") BusInspectionRecord inspection, 
			@RequestParam(value = "attachmentId", required = false) Long[] attachmentIds,
			ModelMap map) {
		Long birId = inspection.getBirId(), resultBirId = -1L;
		ResultObj resultObj = null;
		Map<String, Object> extraParam = new HashMap<String, Object>();
		boolean isEdit = birId != null && birId > 0;
		
		//extraParam.put("markerType", EP_MARKER_TYPE);
		extraParam.put("numberBizCode", EP_NUM_BIZ_CODE);
		
		String startTimeStr = inspection.getStartTimeStr(),
			   endTimeStr = inspection.getEndTimeStr();
		if(StringUtils.isNotBlank(startTimeStr)) {
			try {
				inspection.setStartTime(DateUtils.convertStringToDate(startTimeStr, DateUtils.PATTERN_DATE));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(StringUtils.isNotBlank(endTimeStr)) {
			try {
				inspection.setEndTime(DateUtils.convertStringToDate(endTimeStr, DateUtils.PATTERN_DATE));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if(!isEdit || isDraft(inspection.getBirId())) {//已有记录只有操作状态才可进行编辑
			resultBirId = busInspectionRecordService.saveOrUpdateInspectionRecord(inspection, extraParam);
		}
		
		if(isEdit) {
			resultObj = Msg.EDIT.getResult(resultBirId > 0);
		} else {
			resultObj = Msg.ADD.getResult(resultBirId > 0);
		}
		
		if(attachmentIds!=null && attachmentIds.length>0){
			attachmentService.updateBizId(resultBirId, EP_ATTACHMENT_TYPE, attachmentIds);
		}
		
		return resultObj;
	}
	
	/**
	 * 删除企业巡查信息
	 * @param session
	 * @param birId
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delEnterpriseCheck")
	public ResultObj delEnterpriseCheck(HttpSession session, 
			@RequestParam(value = "birId") Long birId,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<Long> birIdList = new ArrayList<Long>();
		birIdList.add(birId);
		
		birIdList = isDraft(birIdList);//只有草稿状态的巡查记录才可删除
		
		boolean result = busInspectionRecordService.deleteBIRByIds(userInfo.getUserId(), birIdList);
		
		return Msg.DELETE.getResult(result);
	}
	
	/**
	 * 加载企业巡查记录
	 * @param session
	 * @param page
	 * @param rows
	 * @param params
	 * 			inspectType			检查类型
	 * 			gridCode			所属网格
	 * 			startTime			检查时间 开始
	 * 			endStartTime		检查时间 结束
	 * 			inspectResultFlag	是否发现污染单位
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listEPCheckData")
	public EUDGPagination listEPCheckData(HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, 
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		params.put("userOrgCode", userInfo.getOrgCode());
		params.put("type", BusInspectionRecord.TYPE.ENVIRONMENTAL_PROTECTION.toString());
		
		EUDGPagination inspectionPagination = busInspectionRecordService.findBIRPagination(page, rows, params);
		return inspectionPagination;
	}
	
	/**
	 * 过滤出草稿巡查记录
	 * @param birIdList
	 * @return
	 */
	private List<Long> isDraft(List<Long> birIdList) {
		List<Long> birIdArray = new ArrayList<Long>();
		
		if(birIdList != null && birIdList.size() > 0) {
			for(Long birId : birIdList) {
				if(birId != null && birId > 0) {
					if(isDraft(birId)) {
						birIdArray.add(birId);
					}
				}
			}
		}
		
		return birIdArray;
	}
	
	/**
	 * 判断巡查记录是否为操作状态
	 * @param inspection
	 * @return
	 */
	private boolean isDraft(Long birId) {
		boolean flag = false;
		
		if(birId != null && birId > 0) {
			BusInspectionRecord inspection = busInspectionRecordService.findBusInspectionRecordById(birId);
			 
			if(inspection != null) {
				flag = IS_DEL_DRAFT.equals(inspection.getIsDel());
			}
		}
		
		return flag;
	}
	
	/**
	 * 格式化输出数据 个性化输出
	 * @param inspection
	 */
	private void formatDataOut(BusInspectionRecord inspection) {
		Date startTime = inspection.getStartTime();
		
		if(startTime != null) {
			inspection.setStartTimeStr(DateUtils.formatDate(startTime, DateUtils.PATTERN_DATE));
		}
	}
}
