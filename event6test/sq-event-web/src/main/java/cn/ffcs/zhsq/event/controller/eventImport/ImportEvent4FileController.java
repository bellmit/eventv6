package cn.ffcs.zhsq.event.controller.eventImport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventImport4FileService;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;

/**
 * 事件批量导入，数据展示 T_EVENT_IMPORT
 * @author zhangls
 *
 */
@Controller
@RequestMapping(value = "/zhsq/event/importEvent4File")
public class ImportEvent4FileController extends ZZBaseController {
	@Autowired
	private IEventImport4FileService eventImportService;
	
	/**
	 * 跳转事件中间表记录展示列表
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public String toList(HttpSession session, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		map.putAll(params);
		
		return "/zzgl/event/eventImport/eventImport4File/list_event_i4f.ftl";
	}
	
	/**
	 * 调整编辑页面
	 * @param session
	 * @param rowid
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toEdit")
	public String toEdit(HttpSession session, 
			@RequestParam(value = "rowid") String rowid,
			ModelMap map) {
		Map<String, Map<String, Object>> recordMap = eventImportService.findEventImportByRowid(rowid);
		
		map.addAttribute("recordMap", recordMap);
		
		return "/zzgl/event/eventImport/eventImport4File/edit_event_i4f.ftl";
	}
	
	/**
	 * 跳转到事件中间表详情页面
	 * @param session
	 * @param rowid
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toDetail")
	public String toDetail(HttpSession session, 
			@RequestParam(value = "rowid") String rowid,
			ModelMap map) {
		Map<String, Map<String, Object>> recordMap = eventImportService.findEventImportByRowid(rowid);
		
		map.addAttribute("recordMap", recordMap);
		
		return "/zzgl/event/eventImport/eventImport4File/detail_event_i4f.ftl";
	}
	
	/**
	 * 文件导入
	 * @param session
	 * @param importFile	需要导入数据的文件
	 * @param fileSuffix	文件后缀，支持csv、xls
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/importData") 
	public String importData(HttpSession session,
			@RequestParam(value = "csvImportFile") MultipartFile importFile,
			@RequestParam(value = "fileSuffix") String fileSuffix,
			ModelMap map) {
		//重定向，用于避免刷新页面后，事件重复导入
		String forwardUrl = "redirect:/zhsq/event/importEvent4File/toList.jhtml?system_privilege_code=zhsq_event_import_4_file";
		Map<String, Object> resultMap = null;
		
		try {
			resultMap = eventImportService.recordImport(importFile.getInputStream(), fileSuffix);
		} catch (Exception e) {
			String msgWrong = e.getMessage();
			
			if(msgWrong.length() > 100) {
				msgWrong = msgWrong.substring(0, 100);
			}
			
			try {
				msgWrong = URLEncoder.encode(msgWrong,"UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			
			forwardUrl += "&msgWrong=" + msgWrong;
			
			e.printStackTrace();
		}
		
		if(resultMap != null && !resultMap.isEmpty()) {
			for(String key : resultMap.keySet()) {
				forwardUrl += "&" + key + "=" + resultMap.get(key);
			}
		}
		
		return forwardUrl;
	}
	
	/**
	 * 启动/关闭归档事件导入线程
	 * @param session
	 * @param isEnd	true，关闭线程；false，启动线程
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/startOrEndThread4Archive")
	public Map<String, Object> startOrEndThread4Archive(HttpSession session, 
			@RequestParam(value = "isEnd", required = false, defaultValue="false") boolean isEnd,
			ModelMap map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean result = true;
		String tipMsg = "导入线程，";
		
		if(isEnd) {
			tipMsg += "关闭";
			eventImportService.endThread4Archive();
		} else {
			tipMsg += "启动";
			eventImportService.startThread4Archive();
		}
		
		if(result) {
			tipMsg += "成功！";
		} else {
			tipMsg += "失败！";
		}
		
		resultMap.put("success", result);
		resultMap.put("tipMsg", tipMsg);
		
		return resultMap;
	}
	
	/**
	 * 依据rowid更新记录
	 * @param session
	 * @param recordMap
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateData")
	public ResultObj updateData(HttpSession session, 
			@RequestParam Map<String, Object> recordMap,
			ModelMap map) {
		boolean result = eventImportService.updateEventImportByRowid(recordMap);
		
		return Msg.EDIT.getResult(result);
	}
	
	/**
	 * 依据rowid删除事件中间表记录
	 * @param session
	 * @param rowid
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delData")
	public ResultObj delData(HttpSession session, 
			@RequestParam(value = "rowid") String rowid,
			ModelMap map) {
		boolean result = eventImportService.delEventImportByRowid(rowid);
		
		return Msg.DELETE.getResult(result);
	}
	
	/**
	 * 获取中间表T_EVENT_IMPORT列信息
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchColumnInfo")
	public Map<String, Map<String, Object>> fetchColumnInfo(HttpSession session, 
			ModelMap map) {
		Map<String, Map<String, Object>> columnInfo = eventImportService.findTableColInfo();
		
		return columnInfo;
	}
	
	/**
	 * 加载事件中间表记录
	 * @param page
	 * @param rows
	 * @param params
	 * 			keyWord	关键字
	 * 			status	导入状态
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listData", method = RequestMethod.POST)
	public EUDGPagination listData(
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam Map<String, Object> params) {
		
		EUDGPagination eventPagination = eventImportService.findEventImportPagination(page, rows, params);
		
		return eventPagination;
	}
}
