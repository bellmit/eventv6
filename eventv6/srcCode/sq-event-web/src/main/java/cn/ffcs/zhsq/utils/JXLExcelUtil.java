package cn.ffcs.zhsq.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.beanutils.PropertyUtils;

public class JXLExcelUtil<T> {
	private String[] columnNames;
	private String[] dbColumnNames;
	private Integer[] columnWidths;
	private String excelName;
	List<T> list =null;

	public void buildExcelDocument(Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		String[] titles = (String[]) map.get("titles");
		if (null != titles && titles.length > 0) {
			columnNames = titles;
		}
		String[] columns = (String[]) map.get("columns");
		if (null != columns && columns.length > 0) {
			dbColumnNames = columns;
		}
		Integer[] widths = (Integer[]) map.get("widths");
		if (null != widths && widths.length > 0) {
			columnWidths = widths;
		}
		String name = (String) map.get("name");
		if (null != name && !name.equals("")) {
			excelName = name;
		}

		OutputStream os = null;
		WritableWorkbook work=null;
		int startRow=1;
		int columNameIndex=0;
		try {

			// String excelName = "用户信息.xls";
			// 设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ URLEncoder.encode(excelName + ".xls", "UTF-8"));
			os = response.getOutputStream();
			// sheet名称
			String sheetName = excelName; // "用户信息";

			// 全局设置
			WorkbookSettings setting = new WorkbookSettings();
			java.util.Locale locale = new java.util.Locale("zh", "CN");
			setting.setLocale(locale);
			setting.setEncoding("ISO-8859-1");
			// 创建工作薄
			work = Workbook.createWorkbook(os); // 建立excel文件
			// 创建第一个工作表
			jxl.write.WritableSheet ws = work.createSheet(sheetName, 0); // sheet名称
			
			
			int mergeCells=request.getParameter("mergeCells")==null?0:Integer.valueOf(request.getParameter("mergeCells")+"");
			if(mergeCells!=0){
					ws.getSettings().setVerticalFreeze(1);//固定表头第一行
					// 添加标题
					ws.mergeCells(0, 0, mergeCells, 0);  
					// 设置字体
		            WritableFont font =new WritableFont(WritableFont.ARIAL,16, WritableFont.NO_BOLD);
		             // 生成一个单元格样式控制对象  
		            WritableCellFormat titleFormate = new WritableCellFormat(font);  
		            // 单元格中的内容水平方向居中  
		            titleFormate.setAlignment(jxl.format.Alignment.CENTRE);  
		            // 单元格的内容垂直方向居中  
		            titleFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);  
		            Label title = new Label(0, 0, sheetName, titleFormate);  
		            ws.addCell(title); 
		            
		            columNameIndex=1;
		            startRow=2;
			}
			list =(List<T>) map.get("list");
			addColumNameToWsheet(ws,columNameIndex);
			writeContext(work,ws,startRow);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			// 写入文件
			try {
				work.write();
				work.close();
				os.flush();
				os.close();
			} catch (WriteException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	

	private <T> void writeContext(WritableWorkbook work,WritableSheet wsheet,int startRow) {
		int rows = list.size();
		jxl.write.Label wlabel = null;
		jxl.write.WritableCellFormat wcf = getFormat();
		int cols = dbColumnNames.length;
		String columnName = null;
		String value = null;
		try {
			
			for (int i = 0; i < rows; i++) {
				T t = (T) list.get(i);
				int mergeCells=0;
				for (int j = 0; j < cols; j++) {
					columnName = dbColumnNames[j];//.toUpperCase();
					Object o=PropertyUtils.getProperty(t, columnName);
					if(columnName!=null&&(columnName.equals("imgsAffter")||columnName.equals("imgsBefor"))){
						if(o!=null){
							List<String> imgs=(List<String>)o;
							WritableImage wimage =null;
						    OutputStream output =null;
							for (int k = 0; k < imgs.size(); k++) {
								String imgPath=imgs.get(k);
								System.out.println("imgPath="+imgPath);
			                    byte[] data = getImageFromNetByUrl(imgPath);  
			                    if(data!=null){
				                    File file = File.createTempFile("img", ".png");
				                    if (!file.exists()) {// 判断文件是否存在
				                    	file.createNewFile();// 创建目标文件
				                    }
				                    output = new FileOutputStream(file);
				                    BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
				                    bufferedOutput.write(data);
				                    
				                    wsheet.setColumnView(j+mergeCells+k,10);
				                    wsheet.setRowView(i + startRow,400);
				                    wimage = new WritableImage(j+mergeCells+k, (i + startRow), 1, 1, file);
				                    wsheet.addImage(wimage);
				                    //在程序退出时删除临时文件
				                    file.deleteOnExit();
				                    if(output!=null)output.close();
			                    }
							}
							  mergeCells+=(imgs.size()-1)>=0?(imgs.size()-1):0;
						}
						continue;	
					}
					value = (o==null?"":o.toString());
					if(!value.equals("")){
						if(columnNames[j].indexOf("比例")>0) {
							value+="%";
						}  
					}
					 wsheet.setRowView(i + startRow,400);
					wlabel = new jxl.write.Label(j, (i + startRow), value + "", wcf);
					wsheet.addCell(wlabel);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
    /**  
     * 根据地址获得数据的字节流  
     * @param strUrl 网络连接地址  
     * @return  
     */    
    public static byte[] getImageFromNetByUrl(String strUrl){    
    	 byte[] btImg=null;
    	 InputStream inStream =null;
        try {    
            URL url = new URL(strUrl);    
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
            conn.setRequestMethod("GET");    
            conn.setConnectTimeout(6 * 1000);    
            inStream = conn.getInputStream();//通过输入流获取图片数据    
            btImg = readInputStream(inStream);//得到图片的二进制数据    
        } catch (Exception e) {    
            e.printStackTrace();    
        } 
        return btImg;    
    } 
	  /**  
     * 从输入流中获取数据  
     * @param inStream 输入流  
     * @return  
     * @throws Exception  
     */    
    public static byte[] readInputStream(InputStream inStream) throws Exception{    
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();    
        byte[] buffer = new byte[1024];    
        int len = 0;    
        while( (len=inStream.read(buffer)) != -1 ){    
            outStream.write(buffer, 0, len);    
        }   
        if(inStream!=null)
        	inStream.close();    
        return outStream.toByteArray();    
    } 
	// 添加标题样式
	private void addColumNameToWsheet(jxl.write.WritableSheet wsheet,int columNameIndex )
			throws RowsExceededException, WriteException {

		// 设置excel标题
		jxl.write.WritableFont wfont = getFont();
		if (null == wfont) {
			wfont = new WritableFont(WritableFont.ARIAL,WritableFont.DEFAULT_POINT_SIZE, WritableFont.NO_BOLD);

		}
		jxl.write.WritableCellFormat wcfFC = getTitleFormat();
		
		if (null == wcfFC) {
			wcfFC = new jxl.write.WritableCellFormat(wfont);
			try {
				wcfFC.setWrap(true);// 自动换行
				wcfFC.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);//给单元格加边
				wcfFC.setAlignment(Alignment.CENTRE);
				wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);// 设置对齐方式
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}

		jxl.write.Label wlabel1 = null;
		String[] columNames = columnNames;
		if (null == columNames)
			return;
		int colSize = columNames.length;

		Integer[] colsWidth = columnWidths;
		if (null == colsWidth) {
			colsWidth = new Integer[colSize];
			for (int i = 0; i < colSize; i++) {
				colsWidth[i] = 20;
			}
		}

		int temp = 0;
		String colName = null;
		int mergeCells=0;
		for (int i = 0; i < colSize; i++) {
			colName = columNames[i];
			if (null == colName || "".equals(colName))
				colName = "";
			wlabel1 = new jxl.write.Label(i+mergeCells, columNameIndex, colName, wcfFC);
			try {
				List<String> imgs=null;
				int maxImgsBeforSize=0;//最多图片
				int maxImgsAffterSize=0;
				for (int j = 0; j < list.size(); j++) {
					T t = (T) list.get(j);
					Object o=PropertyUtils.getProperty(t, "imgsBefor");
					if(o!=null){
						imgs=(List<String>)o;
						if(imgs!=null&&maxImgsBeforSize<imgs.size()){
							maxImgsBeforSize=imgs.size();
						}
					}
					o=PropertyUtils.getProperty(t, "imgsAffter");
					if(o!=null){
						imgs=(List<String>)o;
						if(imgs!=null&&maxImgsAffterSize<imgs.size()){
							maxImgsAffterSize=imgs.size();
						}
					}
					
				}
				if(list.size()>0&&(colName.equals("处理前图片")||colName.equals("现场图片")||colName.equals("处理后图片"))){
					if(colName.equals("处理前图片")||colName.equals("现场图片")){
						wsheet.mergeCells(i+mergeCells, columNameIndex, i+mergeCells+((maxImgsBeforSize-1)>=0?(maxImgsBeforSize-1):0), columNameIndex);  
					}else if(colName.equals("处理后图片")){
						wsheet.mergeCells(i+mergeCells, columNameIndex, i+mergeCells+((maxImgsAffterSize-1)>=0?(maxImgsAffterSize-1):0), columNameIndex);  
					}
					
				}
				wsheet.addCell(wlabel1);
				temp = colsWidth[i].intValue();
				// 默认设置列宽
				temp = temp == 0 ? 20 : temp;
				wsheet.setColumnView(i+mergeCells, temp);
				if(imgs!=null){
					if(list.size()>0&&(colName.equals("处理前图片")||colName.equals("现场图片")||colName.equals("处理后图片"))){
						if(colName.equals("处理前图片")||colName.equals("现场图片")){
							mergeCells+=(maxImgsBeforSize-1)>=0?(maxImgsBeforSize-1):0;
						}else if(colName.equals("处理后图片")){
							mergeCells+=(maxImgsAffterSize-1)>=0?(maxImgsAffterSize-1):0;
						}
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		
		}

	}

	// 设置格式
	private WritableCellFormat getFormat() {

		jxl.write.WritableFont wfont = getFont();
		jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(
				wfont);
		try {
			wcfFC.setWrap(false);
			wcfFC.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);//给单元格加边
			wcfFC.setAlignment(Alignment.CENTRE);
			wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return wcfFC;
	}
	// 设置格式
	private WritableCellFormat getTitleFormat() {

		// 设置字体
        WritableFont font =new WritableFont(WritableFont.ARIAL,14, WritableFont.NO_BOLD);
		jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(
				font);
		try {
			wcfFC.setWrap(true);
			wcfFC.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);//给单元格加边
			wcfFC.setAlignment(Alignment.CENTRE);
			wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);
			//wcfFC.setBackground(Colour.GREEN); 
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return wcfFC;
	}

	// 设置字体
	private WritableFont getFont() {
		return new WritableFont(WritableFont.ARIAL,WritableFont.DEFAULT_POINT_SIZE, WritableFont.NO_BOLD);
	}
}
