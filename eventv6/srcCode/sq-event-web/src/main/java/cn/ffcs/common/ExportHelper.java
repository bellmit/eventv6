package cn.ffcs.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.ffcs.common.ExcelCellData;
import cn.ffcs.common.ExcelTable;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.SpringContextUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class ExportHelper {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private Configuration configuration;

	public ExportHelper(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.request = request;
		this.response = response;
		FreeMarkerConfigurer freemarkerConfig = SpringContextUtil.getApplicationContext().getBean(FreeMarkerConfigurer.class);
		configuration = freemarkerConfig.getConfiguration();
	}

	/**
	 * 
	 * @param fileName
	 * @param templatePath
	 * @param dataMap
	 * @throws Exception
	 */
	public void exportExcel(String fileName, String templatePath, Map<String, Object> dataMap) throws Exception {
		if (StringUtils.isBlank(templatePath)) throw new Exception("未指明模板路径！");
		PrintWriter out = response.getWriter();
		try {
			StringBuffer sb = this.getTemplateHtml(templatePath, dataMap);
			this.setOutputParams(fileName);
			out.write(sb.toString());
			out.flush();
		} finally {
			out.close();
		}
	}

	/**
	 * 
	 * @param fileName
	 * @param keys
	 * 			String[]：长度为2；第一位：表头名称，第二位：数据源key；
	 * @param vals
	 * @throws Exception
	 */
	public void exportExcel(String fileName, List<String[]> keys, List<Map<String, Object>> vals) throws Exception {
		if (StringUtils.isNotBlank(fileName)) {
			if (keys != null && keys.size() > 0) {
				ExcelTable excelTable = new ExcelTable(fileName);
				for (int i = 0; i < keys.size(); i++) {
					excelTable.addData(new ExcelCellData(0, i, keys.get(i)[0]));
				}
				if (vals != null && vals.size() > 0) {
					for (int i = 1; i <= vals.size(); i++) {
						Map<String, Object> map = vals.get(i - 1);
						for (int j = 0; j < keys.size(); j++) {
							Object obj = map.get(keys.get(j)[1]);
							excelTable.addData(new ExcelCellData(i, j, obj == null ? "" : String.valueOf(obj)));
						}
					}
					this.exportExcel(excelTable);
				} else {
					this.printMsg("无数据可导出！");
				}
			} else {
				throw new Exception("导出出错，请指定导出表头字段与数据源对应关系！");
			}
		} else {
			throw new Exception("导出出错，请指定导出文件名称！");
		}
	}

	/**
	 * 
	 * @param excelTable
	 * @throws Exception
	 */
	public void exportExcel(ExcelTable excelTable) throws Exception {
		ServletOutputStream outputStream = response.getOutputStream();
		response.reset();// 清空输出流
		if (excelTable != null) {
			this.setOutputParams(excelTable.getTableName());
			WritableWorkbook workbook = null;
			WorkbookSettings settings = new WorkbookSettings();
			settings.setWriteAccess(null);
			workbook = Workbook.createWorkbook(outputStream, settings);
			WritableSheet wsheet = workbook.createSheet(excelTable.getTableName(), 0);
			for (ExcelCellData data : excelTable.getDataList())
				wsheet.addCell(new Label(data.getCol(), data.getRow(), data.getValue())); // 列号 行号 值
			// -- 单元格合并
			try {
				if (excelTable.getMergeCells() != null && excelTable.getMergeCells().size() > 0) {
					for (String mc : excelTable.getMergeCells()) {
						String[] rowColStr = mc.split(",");
						if (rowColStr.length == 4)
							wsheet.mergeCells(Integer.parseInt(rowColStr[0]), Integer.parseInt(rowColStr[1]),
									Integer.parseInt(rowColStr[2]), Integer.parseInt(rowColStr[3]));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			workbook.write();
			workbook.close();
		} else {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			String content = "<script type='text/javascript'>alert('无数据可导出');window.close();</script>";
			outputStream.write(content.getBytes("UTF-8"));
		}
		outputStream.flush();
		outputStream.close();
	}

	private void printMsg(String msg) throws Exception {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		String content = "<script type='text/javascript'>alert('" + msg + "');window.close();</script>";
		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(content.getBytes("UTF-8"));
		outputStream.flush();
		outputStream.close();
	}
	
	private StringBuffer getTemplateHtml(String templateName, Map<String, Object> dataMap) throws Exception {
		Template t = null;
		StringWriter writer = null;
		try {
			t = configuration.getTemplate(templateName);
			writer = new StringWriter(5 * 1024);
			t.process(dataMap, writer);
			return writer.getBuffer();
		} finally {
			writer.close();
		}
	}
	
	private void setOutputParams(String fileName) {
		String userAgent = request.getHeader("User-Agent");
		String filename = CommonFunctions.buildFileNameByUa(fileName + ".xls", userAgent);
		response.setHeader("Content-disposition", ("attachment; " + filename));// 设定输出文件头
		response.setContentType("application/msexcel");// 定义输出类型
	}
}
