package cn.ffcs.zhsq.event.controller.eventImport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * excel归档事件导入，支持文件格式为xls
 * @author zhangls
 *
 */
@Controller
@RequestMapping(value = "/zhsq/event/import")
public class ImportEventController extends ImportEventAbstractController {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//表头字段转换
	private static final Map<Integer, String> EVENT_KEY_MAP = new HashMap<Integer, String>() {
		{
			put(0, "eventName");
			put(1, "happenTimeStr");
			put(2, "occurred");
			put(3, "infoOrgCode");
			put(4, "content");
			put(5, "typeName");
			put(6, "influenceDegreeName");
			put(7, "urgencyDegreeName");
			put(8, "contactUser");
			put(9, "tel");
			put(10, "closeUserName");
			put(11, "closeOrgName");
			put(12, "finTimeStr");
			put(13, "result");
			put(14, "createTimeStr");
			put(15, "infoOrgName");
		}
	};
	
	/**
	 * 事件数据导入
	 * @param session
	 * @param importFile
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/importData") 
	public String importData(HttpSession session,
			@RequestParam(value = "importFile") MultipartFile importFile,
			ModelMap map) {
		final MultipartFile file = importFile;
		 
		Thread thread = new Thread() {
			public void run() {
				List<Map<String, Object>> eventMapList = new ArrayList<Map<String, Object>>();
				int tipMsgType = 0;
				Long threadId = this.getId();
				
				try {
					eventMapList = changeXlsToMap(file, threadId);
				} catch (BiffException e) {
					tipMsgType = 1;
					e.printStackTrace();
				} catch (IOException e) {
					tipMsgType = 4;
					e.printStackTrace();
				}
				
				importResultMap.put(threadId+"_tipMsgType", tipMsgType);
				importResultMap.put(threadId+"_isActive", true);//用于区分线程变量是被清空了，还是未解析完成
				
				handleEvent(eventMapList, threadId);
			}
		};
		
		thread.start();
		
		//重定向，由于避免刷新页面后，事件重复导入
		return "redirect:/zhsq/event/import/toIndex.jhtml?threadId="+thread.getId();
	}
	
	/**
	 * 将xls文件转换为事件map
	 * @param importFile	导入文件
	 * @param threadId		线程id
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	private List<Map<String, Object>> changeXlsToMap(MultipartFile importFile, Long threadId) throws BiffException, IOException {
		Workbook excelEvent = null;
		Sheet sheet = null;
		Cell cell = null;
		DateCell dateCell = null;//日期单元格
		String cellContent = "";
		List<Map<String, Object>> eventMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> eventMap = new HashMap<String, Object>();
		int colIndex = 0, colLen = 0, rowLen = 0;
		
		excelEvent = Workbook.getWorkbook(importFile.getInputStream());
		sheet = excelEvent.getSheet(0);
		colLen = sheet.getColumns();
		rowLen = sheet.getRows();
		
		//为了防止解析过慢，导致页面展示提前结束
		if(CommonFunctions.isBlank(importResultMap, threadId+"_total") && rowLen > 1) {
			importResultMap.put(threadId+"_total", rowLen - 1);//需要扣除表头
		}
		
		//去除表头
		for(int rowIndex = 1; rowIndex < rowLen; rowIndex++) {
			eventMap = new HashMap<String, Object>();
			
			for(colIndex = 0; colIndex < colLen; colIndex++) {
				cell = sheet.getCell(colIndex, rowIndex);
				if(cell.getType() == CellType.DATE) {
					dateCell = (DateCell)cell;
					
					TimeZone timeZone = TimeZone.getTimeZone("GMT");//Greenwich Mean Time 使用格林尼治标准时间
	                SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.PATTERN_24TIME);  
	                dateFormat.setTimeZone(timeZone);  
	                cellContent = dateFormat.format(dateCell.getDate());
				} else {
					cellContent = cell.getContents();
				}
				if(StringUtils.isNotBlank(cellContent)) {
					cellContent = cellContent.trim();
					if(StringUtils.isNotBlank(cellContent)) {
						eventMap.put(EVENT_KEY_MAP.get(colIndex), cellContent);
					}
				}
			}
			
			if(!eventMap.isEmpty()) {
				//数据调整
				alterEventMap(eventMap);
				
				eventMapList.add(eventMap);
			}
		}
		
		return eventMapList;
	}

	@Override
	public String exportFailEventContent(List<Map<String, Object>> eventMapList) {
		Map<Integer, String> workTitleMap = new HashMap<Integer, String>() {
        	{
        		put(0, "事件标题");
        		put(1, "事发时间");
        		put(2, "事发地址");
        		put(3, "地域编码");
        		put(4, "事件描述");
        		put(5, "事件类型");
        		put(6, "影响范围");
        		put(7, "紧急程度");
        		put(8, "反馈人员姓名");
        		put(9, "反馈人员电话");
        		put(10, "结案人姓名");
        		put(11, "结案人所属组织名称");
        		put(12, "结案时间");
        		put(13, "事件办理结果");
        		put(14, "采集时间");
        		put(15, "地域名称");
        		put(16, "失败原因");
        		
        	}
        };
        Map<Integer, String> eventKeyMap = new HashMap<Integer, String>() {
			{
				putAll(EVENT_KEY_MAP);
				put(16, "msg");
			}
		};
		
        StringBuffer exportContent = new StringBuffer("");
        
        if(eventMapList != null && eventMapList.size() > 0) {
        	exportContent.append("<table border='1'>");//设置边框
        	int colIndex = 0, colLen = workTitleMap.size();
        			
			//添加表头
			exportContent.append("<tr>");
			for(colIndex = 0; colIndex < colLen; colIndex++) {
				
				exportContent.append("<td>");
				exportContent.append(workTitleMap.get(colIndex));
				exportContent.append("</td>");
				
			}
			exportContent.append("</tr>");
			
			for(Map<String, Object> eventMap : eventMapList) {
				exportContent.append("<tr>");
				
				for(colIndex = 0; colIndex < colLen; colIndex++) {
					exportContent.append("<td>");
					
					if(CommonFunctions.isNotBlank(eventMap, eventKeyMap.get(colIndex))) {
						exportContent.append(eventMap.get(eventKeyMap.get(colIndex)));
					}
					
					exportContent.append("</td>");
				}
				exportContent.append("</tr>");
			}
    			
        	exportContent.append("</table>");
        }
        
        return exportContent.toString();
	}

	@Override
	public String fetchIndexUrl() {
		return "/zzgl/event/eventImport/import_event.ftl";
	}

	@Override
	public String fetchExportMsg() {
		return "请使用Microsoft Excel 97/2000/XP/2003 文件(*.xls)";
	}

	@Override
	public String fetchExportName() {
		String exportName = "";
		
		try {
			exportName = URLEncoder.encode(EXPORT_EXCEL_NAME, "UTF-8") + ".xls";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return exportName;
	}
	
	/**
	 * 个性处理导入数据
	 * @param eventMap
	 */
	private void alterEventMap(Map<String, Object> eventMap) {
		if(eventMap != null && !eventMap.isEmpty()) {
			if(CommonFunctions.isNotBlank(eventMap, "infoOrgName") && CommonFunctions.isBlank(eventMap, "infoOrgCode")) {
				OrgEntityInfoBO orgEntityInfo = findOrgEntityByName(eventMap.get("infoOrgName").toString());
				if(orgEntityInfo  != null) {
					eventMap.put("infoOrgCode", orgEntityInfo.getOrgCode());
				}
			}
		}
	}
	
	/**
	 * 依据地域名称转换地域编码
	 * @param orgName	地域名称
	 * @return
	 */
	private OrgEntityInfoBO findOrgEntityByName(String orgName) {
		StringBuffer sql = new StringBuffer("");
		OrgEntityInfoBO orgEntityInfo = null;
		
		sql.append(" SELECT T1.ORG_ID, T1.ORG_NAME, T1.ORG_CODE ")
		   .append(" FROM T_DC_ORG_ENTITY_INFO T1 ")
		   .append(" WHERE T1.STATUS = '001' AND T1.ORG_NAME = ? ");
		
		List<OrgEntityInfoBO> orgEntityInfoList = jdbcTemplate.query(sql.toString(), new Object[]{orgName}, new RowMapper<OrgEntityInfoBO>() {
			@Override
			public OrgEntityInfoBO mapRow(ResultSet rs, int rowNum) throws SQLException {
				OrgEntityInfoBO orgEntityInfo = new OrgEntityInfoBO();
				
				orgEntityInfo.setOrgId(rs.getLong("ORG_ID"));
				orgEntityInfo.setOrgName(rs.getString("ORG_NAME"));
				orgEntityInfo.setOrgCode(rs.getString("ORG_CODE"));
				
				return orgEntityInfo;
			}
		});
		
		if(orgEntityInfoList != null && orgEntityInfoList.size() > 0) {
			orgEntityInfo = orgEntityInfoList.get(0);
		}
		
		return orgEntityInfo;
	}
}
