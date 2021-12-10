package cn.ffcs.zhsq.event.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.MyTask;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.event.service.IEventImport4FileService;
//import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.persistence.event.EventImport4FileMapper;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * 事件导入数据 T_EVENT_IMPORT
 * @author zhangls
 *
 */
@Service(value = "eventImportService")
public class EventImport4FileServiceImpl implements IEventImport4FileService {
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventDisposalDockingService eventDisposalDockingService;
	
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
//	@Autowired
//	private IFiveKeyElementService fiveKeyElementService;
	
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private EventImport4FileMapper eventImportMapper;
	
	private Long archiveThreadId = null;//线程号
	private static final String INITIAL_STATUS = "2",		//初始化状态，未开始归档的记录
								SUCCESS_STATUS = "1",		//记录归档成功
								FAIL_STATUS = "0",			//记录归档失败
								DEFAULT_EVENT_TYPE = "99",	//其他
								COLLECT_TASK_CODE = "CJ";	//采集事件环节编码
	
	@Override
	public Map<String, Object> recordImport(InputStream fileInputStream) throws Exception {
		BufferedReader csvReader = null;
		String contentLineStr = null, titileLineStr = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int successTotal = 0, total = 0, validTotal = 0;
		
		csvReader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
		
		//表头，默认首行为表头，使用表T_EVENT_IMPORT中各字段作为列名
		titileLineStr = csvReader.readLine();
		
		if(titileLineStr != null) {
			String[] titleArray = titileLineStr.split(","),
					 contentArray = null;
			List<Map<String, Object>> recordMapList = new ArrayList<Map<String, Object>>();
			Map<String, Object> recordMap = null, columnMap = new HashMap<String, Object>();
			int recordLen = 0;
			
			contentLineStr = csvReader.readLine();
			Map<String, Map<String, Object>> columInfoMap = findTableColInfo();
			
			for(String key : columInfoMap.keySet()) {
				columnMap.put(key, "");
			}
			
			while(contentLineStr != null) {
				contentArray = contentLineStr.split(",");
				recordMap = new HashMap<String, Object>();
				recordMap.putAll(columnMap);
				recordLen = contentArray.length;
                
				try {//防止某一条异常数据，导致后续导入被阻断
					for(int index = 0; index < recordLen; index++) {
						recordMap.put(titleArray[index], contentArray[index]);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				
				if(!recordMap.isEmpty()) {
					recordFormatIn(recordMap, columInfoMap);
					
					recordMapList.add(recordMap);
				}
				
				total++;
				
				contentLineStr = csvReader.readLine();
			}
			
			validTotal = recordMapList.size();
			
			if(total > 0) {
				List<Map<String, Object>> recordInsertMapList = new ArrayList<Map<String, Object>>();
				int start = 0, end = 100, insertPage = 100;
				
				do {
					recordInsertMapList = recordMapList.subList(start, end);
					
					successTotal += eventImportMapper.insert4Batch(recordInsertMapList);
					
					start += insertPage;
					end = start + insertPage;
					end = end > total ? total : end;
				} while(start <= total);
				
			}
		}
		
		resultMap.put("successTotal", successTotal);
		resultMap.put("validTotal", validTotal);
		resultMap.put("total", total);
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> recordImport(InputStream fileInputStream, String fileSuffix) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> recordInsertMapList = null;
		int total = 0, successTotal = 0;
		
		if(StringUtils.isNotBlank(fileSuffix)) {
			fileSuffix = fileSuffix.toLowerCase();
		}
		
		if("csv".equals(fileSuffix)) {
			recordInsertMapList = this.recordImport4CSV(fileInputStream);
		} else if("xls".equals(fileSuffix)) {
			recordInsertMapList = this.recordImport4XLS(fileInputStream);
		} else if("xlsx".equals(fileSuffix)) {
			recordInsertMapList = recordImport4XLSX(fileInputStream);
		}
		
		if(recordInsertMapList != null) {
			Map<String, Map<String, Object>> columInfoMap = findTableColInfo();
			
			for(Map<String, Object> recordMap : recordInsertMapList) {
				this.recordFormatIn(recordMap, columInfoMap);
			}
			
			total = recordInsertMapList.size();
		}
		
		if(total > 0) {
			int start = 0, end = 100, insertPage = 100;
			List<Map<String, Object>> recordMapList = null;
			
			do {
				end = end > total ? total : end;
				
				recordMapList = recordInsertMapList.subList(start, end);
				
				successTotal += eventImportMapper.insert4Batch(recordMapList);
				
				start += insertPage;
				end = start + insertPage;
			} while(start <= total);
		}
		
		resultMap.put("successTotal", successTotal);
		resultMap.put("total", total);
		
		return resultMap;
	}
	
	/**
	 * 导入csv文件
	 * @param fileInputStream
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, Object>> recordImport4CSV(InputStream fileInputStream) throws Exception {
		List<Map<String, Object>> recordInsertMapList = new ArrayList<Map<String, Object>>();
		
		BufferedReader csvReader = null;
		String contentLineStr = null, titileLineStr = null;
		
		csvReader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
		
		//表头，默认首行为表头，使用表T_EVENT_IMPORT中各字段作为列名
		titileLineStr = csvReader.readLine();
		
		if(titileLineStr != null) {
			String[] titleArray = titileLineStr.split(","),
					 contentArray = null;
			List<Map<String, Object>> recordMapList = new ArrayList<Map<String, Object>>();
			Map<String, Object> recordMap = null, columnMap = new HashMap<String, Object>();
			int recordLen = 0;
			String msgWrong = verifyTitle(titleArray);
			
			if(StringUtils.isBlank(msgWrong)) {
				contentLineStr = csvReader.readLine();
				
				while(contentLineStr != null) {
					contentArray = contentLineStr.split(",");
					recordMap = new HashMap<String, Object>();
					recordMap.putAll(columnMap);
					recordLen = contentArray.length;
	                
					try {//防止某一条异常数据，导致后续导入被阻断
						for(int index = 0; index < recordLen; index++) {
							recordMap.put(titleArray[index], contentArray[index]);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					if(!recordMap.isEmpty()) {
						recordMapList.add(recordMap);
					}
					
					contentLineStr = csvReader.readLine();
				}
			} else {
				throw new Exception(msgWrong);
			}
		}
		
		return recordInsertMapList;
	}
	
	/**
	 * 导入xls文件
	 * @param fileInputStream
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, Object>> recordImport4XLS(InputStream fileInputStream) throws Exception {
		List<Map<String, Object>> recordInsertMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> recordMap = new HashMap<String, Object>();
		Workbook excelEvent = null;
		Sheet sheet = null;
		Cell cell = null;
		DateCell dateCell = null;//日期单元格
		String cellContent = "", msgWrong = null;
		int colIndex = 0, colLen = 0, rowLen = 0;
		String[] titleArray = null;
		
		excelEvent = Workbook.getWorkbook(fileInputStream);
		sheet = excelEvent.getSheet(0);
		colLen = sheet.getColumns();
		rowLen = sheet.getRows();
		
		titleArray = new String[colLen];
		
		//获取表头
		for(colIndex = 0; colIndex < colLen; colIndex++) {
			cell = sheet.getCell(colIndex, 0);
			
			cellContent = cell.getContents();
			
			titleArray[colIndex] = cellContent.trim();
		}
		
		msgWrong = this.verifyTitle(titleArray);
		
		if(StringUtils.isBlank(msgWrong)) {
			//去除表头
			for(int rowIndex = 1; rowIndex < rowLen; rowIndex++) {
				recordMap = new HashMap<String, Object>();
				
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
							recordMap.put(titleArray[colIndex], cellContent);
						}
					}
				}
				
				if(!recordMap.isEmpty()) {
					recordInsertMapList.add(recordMap);
				}
			}
		} else {
			throw new Exception(msgWrong);
		}
		
		return recordInsertMapList;
	}
	
	/**
	 * xlsx文件导入
	 * @param fileInputStream
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, Object>> recordImport4XLSX(InputStream fileInputStream) throws Exception {
		List<Map<String, Object>> recordInsertMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> recordMap = new HashMap<String, Object>();
		String cellContent = "", msgWrong = null;
		int colIndex = 0, colLen = 0, rowLen = 0, cellType = 0;
		String[] titleArray = null;
		XSSFWorkbook xlsWorkbook = null;
		XSSFSheet xlsSheet = null;
		XSSFRow titleRow = null;
		XSSFCell cell = null;
		
		xlsWorkbook = new XSSFWorkbook(fileInputStream);
		xlsSheet = xlsWorkbook.getSheetAt(0);
		titleRow = xlsSheet.getRow(0);
		colLen = titleRow.getLastCellNum() - titleRow.getFirstCellNum();
		rowLen = xlsSheet.getLastRowNum() - xlsSheet.getFirstRowNum();
		
		titleArray = new String[colLen];
		
		//获取表头
		for(colIndex = 0; colIndex < colLen; colIndex++) {
			cell = titleRow.getCell(colIndex);
			
			cellContent = cell.getStringCellValue();
			
			titleArray[colIndex] = cellContent.trim();
		}
		
		msgWrong = this.verifyTitle(titleArray);
		
		if(StringUtils.isBlank(msgWrong)) {
			//去除表头
			for(int rowIndex = 1; rowIndex <= rowLen; rowIndex++) {
				recordMap = new HashMap<String, Object>();
				
				for(colIndex = 0; colIndex < colLen; colIndex++) {
					cell = xlsSheet.getRow(rowIndex).getCell(colIndex);
					cellType = cell.getCellType();
					
					switch(cellType) {
						case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC : {
							if(HSSFDateUtil.isCellDateFormatted(cell)) {//日期
								cellContent = DateUtils.formatDate(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()), DateUtils.PATTERN_24TIME);
							} else {//数字
								cellContent = String.valueOf(cell.getNumericCellValue());
							}
							break;
						}
						case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN : {//布尔类型
							cellContent = String.valueOf(cell.getBooleanCellValue());
							break;
						}
						default : {//字符串类型
							cellContent = cell.getStringCellValue();
						}
					}
					
					if(StringUtils.isNotBlank(cellContent)) {
						cellContent = cellContent.trim();
						if(StringUtils.isNotBlank(cellContent)) {
							recordMap.put(titleArray[colIndex], cellContent);
						}
					}
				}
				
				if(!recordMap.isEmpty()) {
					recordInsertMapList.add(recordMap);
				}
			}
		} else {
			xlsWorkbook.close();
			
			throw new Exception(msgWrong);
		}
		
		xlsWorkbook.close();
		
		return recordInsertMapList;
	}
	
	@Override
	public void startThread4Archive() {
		if(archiveThreadId == null) {
			Thread thread = new Thread() {
				public void run() {
					archiveThreadId = this.getId();
					
					//防止可调整异常导致线程中断
					try {
						archiveEvent();
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			};
			
			thread.start();
		}
	}
	
	@Override
	public void endThread4Archive() {
		archiveThreadId = null;
	}
	
	/**
	 * 事件列表有转换为小写的分页查询
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	private EUDGPagination findEventImport4EventPagination(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		
		int count = eventImportMapper.findCountByCriteria(params);
		List<Map<String, Object>> eventList = eventImportMapper.findPageList4EventByCriteria(params, rowBounds);
		
		dataFormatOut(eventList);
		
		EUDGPagination eventPagination = new EUDGPagination(count, eventList);
		
		return eventPagination;
	}
	
	
	@Override
	public EUDGPagination findEventImportPagination(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		
		int count = eventImportMapper.findCountByCriteria(params);
		List<Map<String, Object>> eventList = eventImportMapper.findPageListByCriteria(params, rowBounds);
		
		recordFormatOut(eventList);
		
		EUDGPagination eventPagination = new EUDGPagination(count, eventList);
		
		return eventPagination;
	}
	
	@Override
	public Map<String, Map<String, Object>>  findEventImportByRowid(String rowid) {
		Map<String, Map<String, Object>> resultMap = this.findTableColInfo();
		
		if(StringUtils.isNotBlank(rowid)) {
			Map<String, Object> recordMap = eventImportMapper.findByRowid(rowid);
			if(recordMap != null && !recordMap.isEmpty()) {
				Map<String, Object> columnMap = null;
				
				for(String columnName : recordMap.keySet()) {
					if(resultMap.containsKey(columnName)) {
						resultMap.get(columnName).put("COLUMN_VALUE", recordMap.get(columnName));
					} else {//存放ROWID字段
						columnMap = new HashMap<String, Object>();
						columnMap.put("COLUMN_NAME", columnName);
						columnMap.put("COLUMN_VALUE", recordMap.get(columnName));
						
						resultMap.put(columnName, columnMap);
					}
				}
				
				if(resultMap.containsKey("MSG_WRONG")) {
					resultMap.get("MSG_WRONG").put("COLUMN_VALUE", recordMap.get("MSG_WRONG_STR"));
					resultMap.remove("MSG_WRONG_STR");
				}
			}
		}
		
		return resultMap;
	}
	
	@Override
	public boolean updateEventImportByRowid(Map<String, Object> recordMap) {
		boolean flag = false;
		
		if(recordMap != null && !recordMap.isEmpty()) {
			if(CommonFunctions.isNotBlank(recordMap, "ROW_ID") && CommonFunctions.isBlank(recordMap, "rowid")) {
				flag = updateEventImportByRowid(recordMap, findTableColInfo());
			} else if(CommonFunctions.isNotBlank(recordMap, "rowid")) {
				flag = eventImportMapper.update(recordMap) > 0;
			}
		}
		
		return flag;
	}
	
	/**
	 * 动态更新事件导入中间表字段内容
	 * @param recordMap	导入数据
	 * @param columnMap	导入中间表列信息
	 * @return
	 */
	private boolean updateEventImportByRowid(Map<String, Object> recordMap, Map<String, Map<String, Object>> columnMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>(), colMap = null;
		String rowid = "", columnValue = null, columnDataType = null;
		StringBuffer updateBuffer = new StringBuffer("");//更新语句
		boolean flag = false;
		
		if(CommonFunctions.isNotBlank(recordMap, "ROW_ID") && CommonFunctions.isBlank(recordMap, "rowid")) {
			rowid = recordMap.get("ROW_ID").toString();
			recordMap.remove("ROW_ID");
		}
		
		recordFormatIn(recordMap, columnMap);
		
		for(String columnName : columnMap.keySet()) {
			if(recordMap.containsKey(columnName)) {
				columnValue = recordMap.get(columnName).toString();
				colMap = columnMap.get(columnName);
				
				if(StringUtils.isNotBlank(columnValue)) {
					columnDataType = colMap.get("DATA_TYPE").toString();
					
					if("NUMBER".equals(columnDataType)) {
						updateBuffer.append(",").append(" T.").append(columnName).append(" = ").append(columnValue);
					} else if("DATE".equals(columnDataType)) {
						columnValue = DateUtils.formatDate((Date)recordMap.get(columnName), DateUtils.PATTERN_24TIME);
						updateBuffer.append(",").append(" T.").append(columnName).append(" = TO_DATE('").append(columnValue).append("', 'YYYY-MM-DD HH24:MI:SS')");
					} else {
						updateBuffer.append(",").append(" T.").append(columnName).append(" = '").append(columnValue).append("'");
					}
				} else {
					updateBuffer.append(",").append(" T.").append(columnName).append(" = NULL");
				}
			}
		}
		
		if(updateBuffer.length() > 0) {
			resultMap.put("updateSql", updateBuffer.substring(1));
			resultMap.put("rowid", rowid);
			
			flag = eventImportMapper.updateByRowid(resultMap) > 0;
		}
		
		return flag;
	}
	
	@Override
	public boolean delEventImportByRowid(String rowid) {
		boolean flag = false;
		
		if(StringUtils.isNotBlank(rowid)) {
			flag = eventImportMapper.deleteByRowid(rowid) > 0;
		}
		
		return flag;
	}
	
	/**
	 * 格式化输出数据
	 * @param recordMapList
	 */
	private void dataFormatOut(List<Map<String, Object>> recordMapList) {
		if(recordMapList != null && recordMapList.size() > 0) {
			for(Map<String, Object> recordMap : recordMapList) {
				if(CommonFunctions.isNotBlank(recordMap, "happenTime")) {
					recordMap.put("happenTimeStr", DateUtils.formatDate((Date)recordMap.get("happenTime"), DateUtils.PATTERN_24TIME));
				}
				
				if(CommonFunctions.isNotBlank(recordMap, "finTime")) {
					recordMap.put("finTimeStr", DateUtils.formatDate((Date)recordMap.get("finTime"), DateUtils.PATTERN_24TIME));
				}
				
				if(CommonFunctions.isNotBlank(recordMap, "createTime")) {
					recordMap.put("createTimeStr", DateUtils.formatDate((Date)recordMap.get("createTime"), DateUtils.PATTERN_24TIME));
				}
				
				if(CommonFunctions.isNotBlank(recordMap, "handleDate")) {
					recordMap.put("handleDateStr", DateUtils.formatDate((Date)recordMap.get("handleDate"), DateUtils.PATTERN_24TIME));
				}
			}
		}
	}
	
	/**
	 * 格式化输出数据
	 * @param recordMapList
	 */
	private void recordFormatOut(List<Map<String, Object>> recordMapList) {
		if(recordMapList != null && recordMapList.size() > 0) {
			Map<String, Map<String, Object>> columnMap = findTableColInfo();
			String columnDataType = null;
			
			for(Map<String, Object> recordMap : recordMapList) {
				for(String columnName : recordMap.keySet()) {
					if(CommonFunctions.isNotBlank(recordMap, columnName) && columnMap.containsKey(columnName)) {
						columnDataType = columnMap.get(columnName).get("DATA_TYPE").toString();
						if("DATE".equals(columnDataType)) {
							recordMap.put(columnName, DateUtils.formatDate((Date)recordMap.get(columnName), DateUtils.PATTERN_24TIME));
						}
					}
				}
				
				if(recordMap.containsKey("MSG_WRONG")) {
					recordMap.remove("MSG_WRONG");
				}
			}
		}
	}
	
	/**
	 * 设置字段默认值
	 * @param eventMapList
	 */
	private void enactDefaultValue(List<Map<String, Object>> eventMapList) {
		if(eventMapList != null && eventMapList.size() > 0) {
			MixedGridInfo defaultGridInfo = null;
			List<OrgSocialInfoBO> orgInfoList = null;
			List<UserBO> userBOList = null;
			OrgSocialInfoBO defaultOrgInfo = null;
			UserBO defaultUserBO = null;
			String infoOrgCode = "";
			Long defaultOrgId = null;
			
			for(Map<String, Object> eventMap : eventMapList) {
				defaultGridInfo = null;
				orgInfoList = null;
				userBOList = null;
				defaultOrgInfo = null;
				defaultUserBO = null;
				infoOrgCode = "";
				defaultOrgId = null;
				
				if(CommonFunctions.isNotBlank(eventMap, "userId")) {
					defaultUserBO = userManageService.getUserInfoByUserId(Long.valueOf(eventMap.get("userId").toString()));
				}
				if(CommonFunctions.isBlank(eventMap, "happenTimeStr")) {
					eventMap.put("happenTimeStr", DateUtils.getNow());
				}
				if(CommonFunctions.isBlank(eventMap, "finTimeStr")) {
					eventMap.put("finTimeStr", DateUtils.getNow());
				}
				if(CommonFunctions.isNotBlank(eventMap, "infoOrgCode")) {
					infoOrgCode = eventMap.get("infoOrgCode").toString();
					
					defaultGridInfo = mixedGridInfoService.getDefaultGridByOrgCode(infoOrgCode);
					
					if(defaultGridInfo != null) {
						eventMap.put("gridId", defaultGridInfo.getGridId());
						eventMap.put("gridName", defaultGridInfo.getGridName());
						eventMap.put("gridCode", infoOrgCode);
						
						if(CommonFunctions.isNotBlank(eventMap, "orgId")) {
							try {
								defaultOrgId = Long.valueOf(eventMap.get("orgId").toString());
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
							
							if(defaultOrgId > 0) {
								defaultOrgInfo = orgSocialInfoService.findByOrgId(defaultOrgId);
							}
						}
						
						if(defaultOrgInfo == null) {
							orgInfoList = orgSocialInfoService.findByLocationId(defaultGridInfo.getInfoOrgId());
							
							if(orgInfoList != null && orgInfoList.size() > 0) {
								defaultOrgInfo = orgInfoList.get(0);
								defaultOrgId = defaultOrgInfo.getOrgId();
							}
						}
						
						if(defaultOrgInfo != null) {
							eventMap.put("orgId", defaultOrgId);
							eventMap.put("orgCode", defaultOrgInfo.getOrgCode());
							
							if(CommonFunctions.isBlank(eventMap, "closerOrgName")) {
								eventMap.put("closerOrgName", defaultOrgInfo.getOrgName());
							}
							
							if(defaultUserBO == null) {
								userBOList = userManageService.getUserListByUserExampleParamsOut(null, null, defaultOrgId);
							if(userBOList != null && userBOList.size() > 0) {								
//								if(ConstantValue.GRID_ORG_LEVEL.equals(defaultOrgInfo.getChiefLevel())) {//事件导入不会由网格员操作，判断层级不适用
//									for(UserBO userBO : userBOList) {
//										if(fiveKeyElementService.isUserIdGridAdmin(defaultOrgId, userBO.getUserId())) {
//											defaultUserBO = userBO; break;
//										}
//									}
//									
//									eventMap.put("isGridOrgInfo", true);
//								} else {
//									defaultUserBO = userBOList.get(0);
//								}
								defaultUserBO = userBOList.get(0);
							}
							}
							if(defaultUserBO != null) {
								eventMap.put("userId", defaultUserBO.getUserId());
								if(CommonFunctions.isBlank(eventMap, "closerName")) {
									eventMap.put("closerName", defaultUserBO.getPartyName());
								}
								if(CommonFunctions.isBlank(eventMap, "contactUser")) {
									eventMap.put("contactUser", defaultUserBO.getPartyName());
									if(CommonFunctions.isBlank(eventMap, "tel")) {
										eventMap.put("tel", defaultUserBO.getVerifyMobile());
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 验证事件字段是否有效
	 * @param event
	 * @return
	 * @throws ZhsqEventException
	 */
	private boolean isEventValid(EventDisposal event, Map<String, Object> eventMap) throws ZhsqEventException{
		boolean isValid = true;
		
		if(event != null) {
			Long gridId = event.getGridId();
			Date happenTime = event.getHappenTime(),
				 finTime = event.getFinTime();
			StringBuffer msgWrong = new StringBuffer("");
			
			if(CommonFunctions.isBlank(eventMap, "infoOrgCode")) {
				msgWrong.append("所属网格不得为空！");
			} else if(gridId == null || gridId < 0) {
				msgWrong.append("所属网格[").append(eventMap.get("infoOrgCode")).append("] 不是有效的地域编码！");
			} else {//网格有效
				if(CommonFunctions.isBlank(eventMap, "orgId")) {
					msgWrong.append("所属网格[").append(event.getGridCode()).append("][").append(gridId).append("] 未能找到有效的组织信息！");
				} else if(CommonFunctions.isBlank(eventMap, "userId")) {//组织有效 用户无效
					msgWrong.append("所属网格[").append(event.getGridCode())
							.append("][").append(gridId).append("] 对应的组织信息[")
							.append(eventMap.get("orgId")).append("] ");
					
					if(CommonFunctions.isNotBlank(eventMap, "isGridOrgInfo") && Boolean.valueOf(eventMap.get("isGridOrgInfo").toString())) {
						msgWrong.append("未能找到有效的网格员信息！");
					} else {
						msgWrong.append("未能找到有效的用户信息！");
					}
				}
			}
			
			if(happenTime == null) {
				if(CommonFunctions.isNotBlank(eventMap, "happenTimeStr")) {
					msgWrong.append("事发时间[").append(eventMap.get("happenTimeStr")).append("] 不是有效的时间， 时间格式需为：YYYY-MM-DD HH:MM:SS！");
				} else {
					msgWrong.append("事发时间不得为空！");
				}
			} else {
				String createTimeStr = event.getCreateTimeStr();
				Date createTime = event.getCreateTime();
				
				if(StringUtils.isBlank(createTimeStr) && createTime == null) {//采集时间为空时，使用事发时间
					event.setCreateTime(happenTime);//将事件采集事件设置为事发时间
				}
			}
			
			if(finTime == null) {
				if(CommonFunctions.isNotBlank(eventMap, "finTimeStr")) {
					msgWrong.append("结案时间[").append(eventMap.get("finTimeStr")).append("] 不是有效的时间， 时间格式需为：YYYY-MM-DD HH:MM:SS！");
				} else {
					msgWrong.append("结案时间不得为空！");
				}
			}
			
			int eventNameLength = 100,
				contentLength = 4000,
				occurredLength = 255,
				resultLength = 1000;
			
			if(!CommonFunctions.isLengthValidate(eventMap, "eventName", eventNameLength)) {
				msgWrong.append("事件标题的输入长度超过了[").append(eventNameLength).append("]个字符！");
			}
			if(!CommonFunctions.isLengthValidate(eventMap, "content", contentLength)) {
				msgWrong.append("事件描述的输入长度超过了[").append(contentLength).append("]个字符！");
			}
			if(!CommonFunctions.isLengthValidate(eventMap, "occurred", occurredLength)) {
				msgWrong.append("事发地址的输入长度超过了[").append(occurredLength).append("]个字符！");
			}
			if(!CommonFunctions.isLengthValidate(eventMap, "result", resultLength)) {
				msgWrong.append("事件办理结果的输入长度超过了[").append(resultLength).append("]个字符！");
			}
			
			if(msgWrong.length() > 0) {
				isValid = false;
				throw new ZhsqEventException(msgWrong.toString());
			}
		}
		
		return isValid;
	}
	
	/**
	 * 通过节点编码查找对应的节点
	 * @param instanceId	流程事件
	 * @param nodeCode		节点编码
	 * @return
	 */
	private Node capCollectNode(Long instanceId, String nodeCode) {
		ProInstance proInstance = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
		Node collectNode = null;
		
		if(proInstance != null) {
			List<Node> nodeList = eventDisposalWorkflowService.queryNodes(proInstance.getWorkFlowId());
			if(nodeList != null) {
				for(Node node : nodeList) {
					if(nodeCode.equals(node.getNodeCode())) {
						collectNode = node; break;
					}
				}
			}
		}
		
		return collectNode;
	}
	
	/**
	 * 处理未归档的事件中间记录，每次100条
	 */
	private void archiveEvent() {
		String bigTypeDicCode = null, type = null;
		String[] typeNameArray = null;
		Map<String, String> influenceMap = new HashMap<String, String>(),//影响范围
							urgencyMap = new HashMap<String, String>();//紧急程度
		
		Map<String, Object> dictMap = new HashMap<String, Object>();
		dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
		
		List<BaseDataDict> influenceDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.INFLUENCE_DEGREE_PCODE, null),
						   urgencyDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.URGENCY_DEGREE_PCODE, null),
						   eventTypeDict = baseDictionaryService.findDataDictListByCodes(dictMap);
		
		if(influenceDictList != null) {
			for(BaseDataDict dataDict : influenceDictList) {
				influenceMap.put(dataDict.getDictName(), dataDict.getDictGeneralCode());
			}
		}
		if(urgencyDictList != null) {
			for(BaseDataDict dataDict : urgencyDictList) {
				urgencyMap.put(dataDict.getDictName(), dataDict.getDictGeneralCode());
			}
		}
		
		while(archiveThreadId != null) {
			int pageNo = 1, pageSize = 100, recordTotal = 0;
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("status", INITIAL_STATUS);
			
			EUDGPagination eventList = this.findEventImport4EventPagination(pageNo, pageSize, params);
			recordTotal = eventList.getTotal();
			
			if(recordTotal > 0) {
				List<Map<String, Object>> eventMapList = (List<Map<String, Object>>)eventList.getRows();
				
				EventDisposal event = null;
				boolean isEventValid = true;
				UserInfo userInfo = new UserInfo();
				Map<String, Object> resultMap = new HashMap<String, Object>();
				
				List<MyTask> myTaskList = null;
				Map<String, Object> taskDoneMap = new HashMap<String, Object>(),
									taskMap = new HashMap<String, Object>(),
									eventImportMap = new HashMap<String, Object>();
				MyTask myTask = null;
				
				boolean result = true;
				Long eventId = -1L, instanceIdL = -1L;
				String instanceId = "";
				StringBuffer msgWrong = new StringBuffer("");
				
				enactDefaultValue(eventMapList);
				
				for(Map<String, Object> eventMap : eventMapList) {
					eventImportMap.clear();
					resultMap.clear();
					eventId = -1L;
					instanceId = "";
					instanceIdL = -1L;
					msgWrong.setLength(0);
					
					if(CommonFunctions.isNotBlank(eventMap, "eventId")) {
						eventId = Long.valueOf(eventMap.get("eventId").toString());
						
						if(eventId > 0) {
							instanceIdL = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
						}
					}
					
					if(instanceIdL > 0) {
						eventMap.put("instanceId", instanceIdL);
						
						result = eventDisposalWorkflowService.archiveAndEndWorkflowForEvent(eventMap);
						
						resultMap.put("result", result);
						resultMap.put("eventId", eventId);
						resultMap.put("instanceId", instanceIdL);
					} else {
						if(CommonFunctions.isNotBlank(eventMap, "typeName")) {//事件类型，名称转换为字典业务编码，格式为：城市管理-市容管理
							typeNameArray = eventMap.get("typeName").toString().split("-");
							bigTypeDicCode = ConstantValue.BIG_TYPE_PCODE;
							type = null;
							
							for(String typeName : typeNameArray) {
								if(StringUtils.isNotBlank(typeName)) {
									for(BaseDataDict dataDict : eventTypeDict) {
										if(typeName.equals(dataDict.getDictName()) && bigTypeDicCode.equals(dataDict.getDictPcode())) {
											bigTypeDicCode = dataDict.getDictCode();
											type = dataDict.getDictGeneralCode();
											break;
										}
									}
								}
							}
							
							eventMap.put("type", type);
						}
						if(CommonFunctions.isBlank(eventMap, "type")) {
							eventMap.put("type", DEFAULT_EVENT_TYPE);//设置默认事件类别为"其他"
						}
						if(!influenceMap.isEmpty() && CommonFunctions.isBlank(eventMap, "influenceDegree") && CommonFunctions.isNotBlank(eventMap, "influenceDegreeName")) {
							eventMap.put("influenceDegree", influenceMap.get(eventMap.get("influenceDegreeName")));
						}
						if(!urgencyMap.isEmpty() && CommonFunctions.isBlank(eventMap, "urgencyDegree") && CommonFunctions.isNotBlank(eventMap, "urgencyDegreeName")) {
							eventMap.put("urgencyDegree", urgencyMap.get(eventMap.get("urgencyDegreeName")));
						}
						
						event = eventDisposalDockingService.jsonToEvent(eventMap);
						
						try {
							isEventValid = isEventValid(event, eventMap);
						} catch (ZhsqEventException e) {
							isEventValid = false;
							msgWrong.append(e.getMessage());
							e.printStackTrace();
						}
						
						if(isEventValid) {
							userInfo = new UserInfo();
							
							if(CommonFunctions.isNotBlank(eventMap, "userId")) {
								userInfo.setUserId((Long)eventMap.get("userId"));
							}
							
							userInfo.setPartyName(eventMap.get("closerName").toString());
							
							if(CommonFunctions.isNotBlank(eventMap, "orgId")) {
								userInfo.setOrgId((Long)eventMap.get("orgId"));
							}
							if(CommonFunctions.isNotBlank(eventMap, "orgCode")) {
								userInfo.setOrgCode(eventMap.get("orgCode").toString());
							}
							
							resultMap = eventDisposalDockingService.saveEventDisposalAndEvaluate(event, userInfo, event.getResult());
						}
					}
					
					if(CommonFunctions.isNotBlank(resultMap, "result")) {
						result = Boolean.valueOf(resultMap.get("result").toString());
						
						if(CommonFunctions.isNotBlank(resultMap, "eventId")) {
							eventId = Long.valueOf(resultMap.get("eventId").toString());
							eventImportMap.put("eventId", eventId);
						}
						
						if(CommonFunctions.isNotBlank(resultMap, "instanceId")) {
							instanceId = resultMap.get("instanceId").toString();
							
							try {
								instanceIdL = Long.valueOf(instanceId);
							} catch(NumberFormatException e) {
								instanceIdL = -1L;
								e.printStackTrace();
							}
						}
						
						if(result && instanceIdL > 0) {//更新事件采集任务的相关属性
							if(eventId > 0) {
								eventId = Long.valueOf(resultMap.get("eventId").toString());
								
								//更新处理时限
								EventDisposal eventTmp = eventDisposalService.findEventByIdSimple(eventId),
											  eventTemp = new EventDisposal();
								Date createTime = eventTmp.getCreateTime(), handleDate = eventTmp.getHandleDate();
								
								if(createTime != null && handleDate != null) {
									eventTemp.setEventId(eventId);
									eventTemp.setHandleDate(new Date(createTime.getTime() + handleDate.getTime() - new Date().getTime()));
									eventDisposalService.updateEventDisposalById(eventTemp);
								}
							}
							
							//获取事件采集节点任务
							Node collectNode = capCollectNode(instanceIdL, COLLECT_TASK_CODE);
							
							if(collectNode != null) {
								taskDoneMap = eventDisposalWorkflowService.capDoneTaskInfo(instanceIdL, collectNode.getNodeName());
							}
							
							if(CommonFunctions.isNotBlank(taskDoneMap, "tasks")) {
								taskMap = new HashMap<String, Object>();
								
								myTaskList = (List<MyTask>)taskDoneMap.get("tasks");
								
								if(myTaskList != null && myTaskList.size() > 0) {//变更事件采集环节任务信息
									myTask = myTaskList.get(0);
									taskMap.put("taskId", myTask.getTaskId());
									taskMap.put("taskName", myTask.getTaskName());
									taskMap.put("transactorName", eventMap.get("closerName"));
									taskMap.putAll(resultMap);
									taskMap.put("transactOrgName", eventMap.get("closerOrgName"));
									taskMap.put("taskAdvice", eventMap.get("result"));
									taskMap.put("taskCreateTime", eventMap.get("happenTimeStr"));
									taskMap.put("taskReadTime", eventMap.get("happenTimeStr"));
									taskMap.put("taskEndTime", eventMap.get("finTimeStr"));
									
									eventDisposalWorkflowService.saveOrUpdateTask(taskMap);
								}
							}
						} else {
							if(eventId < 0) {
								msgWrong.append("事件新增失败！");
							} else if(StringUtils.isBlank(instanceId)) {
								msgWrong.append("事件流程启动失败，事件编号为[").append(eventId).append("]！");
							}
						}
					}
					
					if(msgWrong.length() > 0) {
						eventImportMap.put("status", FAIL_STATUS);
						eventImportMap.put("msgWrong", msgWrong);
					} else {
						eventImportMap.put("status", SUCCESS_STATUS);
					}
					
					if(CommonFunctions.isNotBlank(eventMap, "rowid")) {
						eventImportMap.put("rowid", eventMap.get("rowid"));
					}
					
					this.updateEventImportByRowid(eventImportMap);
				}
			} else {
				archiveThreadId = null;
			}
		}
	}
	
	@Override
	public Map<String, Map<String, Object>> findTableColInfo() {
		List<Map<String, Object>> colInfoList = eventImportMapper.findTableColInfo();
		Map<String, Map<String, Object>> colInfoMap = new HashMap<String, Map<String, Object>>();
		
		for(Map<String, Object> colMap : colInfoList) {
			colInfoMap.put(colMap.get("COLUMN_NAME").toString(), colMap);
		}
		
		return colInfoMap;
	}
	
	/**
	 * 检查表头有效性
	 * @param titleArray
	 * @return
	 */
	private String verifyTitle(String[] titleArray) {
		StringBuffer msgWrong = new StringBuffer("");
		
		if(titleArray != null && titleArray.length > 0) {
			Map<String, Map<String, Object>> columnMap = findTableColInfo();
			for(String title : titleArray) {
				if(!columnMap.containsKey(title)) {
					msgWrong.append("导入项[").append(title).append("] 不在中间表内，请核查！");
				}
			}
		}
		
		return msgWrong.toString();
	}
	
	/**
	 * 导入字段格式化
	 * @param recordMap	导入字段
	 * @param columnMap	表字段信息
	 */
	private void recordFormatIn(Map<String, Object> recordMap, Map<String, Map<String, Object>> columnMap) {
		if(recordMap != null && !recordMap.isEmpty() && columnMap != null) {
			Map<String, Object> colMap = null;
			String columnValue = null, columnDataType = null, isColumnNullable = null,
				   STATUS_INIT = "2", STATUS_FAIL = "0";
			Integer columnDataLength = null;
			StringBuffer msgWrong = new StringBuffer("");
			
			for(String columnName : columnMap.keySet()) {
				if(recordMap.containsKey(columnName)) {
					columnValue = recordMap.get(columnName).toString();
					colMap = columnMap.get(columnName);
					isColumnNullable = colMap.get("NULLABLE").toString();
					
					if(StringUtils.isNotBlank(columnValue)) {
						columnDataType = colMap.get("DATA_TYPE").toString();
						columnDataLength = Integer.valueOf(colMap.get("DATA_LENGTH").toString());
						
						if(!"DATE".equals(columnDataType) && !CommonFunctions.isLengthValidate(columnValue, columnDataLength)) {
							recordMap.put(columnName, "");
							
							msgWrong.append("字段[").append(columnName).append("]，值为[").append(columnValue).append("] 超过了长度限制").append(columnDataLength).append("，请检查！");
						}
						
						if("NUMBER".equals(columnDataType)) {
							try {
								recordMap.put(columnName, Long.valueOf(columnValue));
							} catch(NumberFormatException e) {
								recordMap.put(columnName, "");
								
								msgWrong.append("字段[").append(columnName).append("]，值为[").append(columnValue).append("] 不是有效的数值，请检查！");
							}
						} else if("DATE".equals(columnDataType)) {
							try {
								recordMap.put(columnName, DateUtils.convertStringToDate(columnValue, DateUtils.PATTERN_24TIME));
							} catch (ParseException e) {
								recordMap.put(columnName, "");
								
								msgWrong.append("字段[").append(columnName).append("]，值为[").append(columnValue).append("] 不是有效的时间，时间格式为[").append(DateUtils.PATTERN_24TIME).append("]，请检查！");
							}
						}
					} else if("N".equals(isColumnNullable)) {
						msgWrong.append("字段[").append(columnName).append("] 不得为空，请检查！");
					}
				} else {
					recordMap.put(columnName, "");
				}
			}
			
			if(msgWrong.length() > 0) {
				recordMap.put("STATUS_", STATUS_FAIL);
			} else if(CommonFunctions.isBlank(recordMap, "STATUS_")) {
				recordMap.put("STATUS_", STATUS_INIT);
			}
			
			recordMap.put("MSG_WRONG", msgWrong.toString());
		}
	}
}
