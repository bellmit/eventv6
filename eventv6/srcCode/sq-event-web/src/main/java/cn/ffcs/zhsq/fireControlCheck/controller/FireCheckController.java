package cn.ffcs.zhsq.fireControlCheck.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.service.IFunConfigurationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
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
 * 消防检查
 * @author qiud
 *
 */
@Controller
@RequestMapping(value="/zhsq/fireControlCheck")
public class FireCheckController extends ZZBaseController{

	@Autowired
	private IBusInspectionRecordService busInspectionRecordService;
	@Autowired
	private IAttachmentService attachmentService;
	@Autowired
	private IFunConfigurationService configurationService;
	
	private String FC_INSPECT_TYPE_PCODE = "A001093199863";
	private String FC_ATTACHMENT_TYPE = "fireControlCheck";//消防检查附件类型
	private String PATH = "/zzgl/fireCheck";//消防检查页面路径
	private String IS_DEL_DRAFT = "2";//记录草稿状态
	private String FC_NUM_BIZ_CODE = "07";//消防检查编码编号类型

	@RequestMapping(value = "/index")
	public String index(HttpSession session, ModelMap map) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		String gridName = defaultGridInfo.get(KEY_START_GRID_NAME).toString();

		UserInfo userInfo = (UserInfo) session
				.getAttribute(ConstantValue.USER_IN_SESSION);
		//获取该组织的消防检查事件类型
		String inspectInfoEventType = "";
		//获取事件类型对应的模块类型bizType
		if(!StringUtils.isBlank(userInfo.getOrgCode())){
			inspectInfoEventType =  configurationService.
					changeCodeToValue(ConstantValue.REPORT_EVENT_TYPE,ConstantValue.INSPECT_INFO_EVENT_TYPE, IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode());
			if(StringUtils.isBlank(userInfo.getOrgCode())){
				inspectInfoEventType =  configurationService.
						changeCodeToValue(ConstantValue.REPORT_EVENT_TYPE,ConstantValue.INSPECT_INFO_EVENT_TYPE,IFunConfigurationService.CFG_TYPE_FACT_VAL);
			}
		}else{
			inspectInfoEventType =  configurationService.
					changeCodeToValue(ConstantValue.REPORT_EVENT_TYPE,ConstantValue.INSPECT_INFO_EVENT_TYPE,IFunConfigurationService.CFG_TYPE_FACT_VAL);
		}
		map.addAttribute("inspectInfoEventType",inspectInfoEventType);
		map.addAttribute("iframeCloseCallBack", ConstantValue.closeCallBack);
		map.addAttribute("INSPECT_INFO_BIZ_TYPE", ConstantValue.INSPECT_INFO_BIZ_TYPE);
		map.addAttribute("ZZGRID", App.ZZGRID.getDomain(session));

		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("gridName", gridName);
		map.addAttribute("FC_INSPECT_TYPE_PCODE", FC_INSPECT_TYPE_PCODE);
		
		return PATH + "/list_fireControlCheck.ftl";
	}
	
	/**
	 * 加载消防检查记录
	 */
	@ResponseBody
	@RequestMapping(value = "/listFireCheckData")
	public EUDGPagination listFireCheckData(HttpSession session,
											@RequestParam(value = "page") int page,
											@RequestParam(value = "rows") int rows,
											@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		params.put("userOrgCode", userInfo.getOrgCode());
		params.put("type", BusInspectionRecord.TYPE.FIRE_FIGHTER.toString());

		EUDGPagination inspectionPagination = busInspectionRecordService.findBIRPagination(page, rows, params);
		return inspectionPagination;
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
		
		Map<String, Object> extraParam = new HashMap<String, Object>();
		extraParam.put("type", BusInspectionRecord.TYPE.FIRE_FIGHTER.toString());
		
		if(birId != null && birId > 0) {
			inspection = busInspectionRecordService.findInspectionRecordById(birId, extraParam);
		}
		
		if(inspection == null) {
			inspection = new BusInspectionRecord();
			
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			//设置默认网格信息
			String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
			String gridName = defaultGridInfo.get(KEY_START_GRID_NAME).toString();
			
			try {
				Long gridId = Long.valueOf(defaultGridInfo.get(KEY_START_GRID_ID).toString());
				inspection.setGridId(gridId);
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
			inspection.setGridCode(infoOrgCode);
			inspection.setGridName(gridName);
			inspection.setType(BusInspectionRecord.TYPE.FIRE_FIGHTER.toString());
			inspection.setStartTimeStr(DateUtils.getToday());
		} else {
			formatDataOut(inspection);
		}
		
		map.addAttribute("inspection", inspection);
		map.addAttribute("FC_ATTACHMENT_TYPE", FC_ATTACHMENT_TYPE);//附件上传类型
		map.addAttribute("createUserId", userInfo.getUserId());//附件上传人员
		map.put("imgDownPath", App.IMG.getDomain(session));//用于附件上传组件中的缩略图展示
		map.addAttribute("FC_INSPECT_TYPE_PCODE", FC_INSPECT_TYPE_PCODE);
		
		return PATH + "/add_fireControlCheck.ftl";
	}
	
	/**
	 * 保存企业巡查信息
	 */
	@ResponseBody
	@RequestMapping(value = "/saveFireCheck")
	public ResultObj saveFireCheck(HttpSession session, 
			@ModelAttribute(value = "inspection") BusInspectionRecord inspection, 
			@RequestParam(value = "attachmentId", required = false) Long[] attachmentIds,
			ModelMap map) {
		Long birId = inspection.getBirId(), resultBirId = -1L;
		ResultObj resultObj = null;
		Map<String, Object> extraParam = new HashMap<String, Object>();
		boolean isEdit = birId != null && birId > 0;
		
		extraParam.put("numberBizCode", FC_NUM_BIZ_CODE);
		
		String startTimeStr = inspection.getStartTimeStr();
		String endTimeStr = inspection.getEndTimeStr();
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
			attachmentService.updateBizId(resultBirId, FC_ATTACHMENT_TYPE, attachmentIds);
		}
		
		return resultObj;
	}
	
	
	/**
	 * 删除消防检查信息
	 */
	@ResponseBody
	@RequestMapping(value = "/delFireCheck")
	public ResultObj delFireCheck(HttpSession session, ModelMap map,
			@RequestParam(value = "birId") Long birId ) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<Long> birIdList = new ArrayList<Long>();
		birIdList.add(birId);
		
		birIdList = isDraft(birIdList);//只有草稿状态的巡查记录才可删除
		
		boolean result = busInspectionRecordService.deleteBIRByIds(userInfo.getUserId(), birIdList);
		
		return Msg.DELETE.getResult(result);
	}
	
	/**
	 * 跳转详情页面
	 */
	@RequestMapping(value = "/toDetail")
	public String toDetail(HttpSession session, 
			@RequestParam(value = "birId") Long birId,
			ModelMap map) {
		Map<String, Object> extraParam = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		extraParam.put("userOrgCode", userInfo.getOrgCode());
		extraParam.put("type", BusInspectionRecord.TYPE.FIRE_FIGHTER.toString());
		BusInspectionRecord inspection = busInspectionRecordService.findInspectionRecordById(birId, extraParam);
		
		if(inspection == null) {
			inspection = new BusInspectionRecord();
		} else {
			formatDataOut(inspection);
		}
		
		map.addAttribute("inspection", inspection);
		map.addAttribute("FC_ATTACHMENT_TYPE", FC_ATTACHMENT_TYPE);
		map.addAttribute("markerOperation", ConstantValue.WATCH_MARKER);//标注操作
		
		return PATH + "/detail_fireControlCheck.ftl";
	}

	/**
	 * 事件上报
	 * @param session
	 * @param birId
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="report", method = RequestMethod.POST)
	public Map<String, Object> report(HttpSession session,
			  @RequestParam(value = "birId", required = false) Long birId,
			  ModelMap map, @RequestParam(value = "status", required = false) String status){
		//获取修改人
		if(birId != null && !StringUtils.isBlank(status)){
			BusInspectionRecord inspection = new BusInspectionRecord();
			inspection.setBirId(birId);
			inspection.setIsDel(status);
			birId = busInspectionRecordService.updateBusInspectionRecord(inspection);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", birId>0L?9 :10);
		return resultMap;
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
