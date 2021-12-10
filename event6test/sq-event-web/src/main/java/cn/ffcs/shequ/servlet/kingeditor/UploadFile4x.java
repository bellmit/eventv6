package cn.ffcs.shequ.servlet.kingeditor;

import cn.ffcs.file.service.FileUploadService;



import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.SpringContextUtil;
import cn.ffcs.zhsq.utils.domain.App;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 实现KindEditor图片上传的Servlet
 * 
 * @author YangCQ
 * 
 * @since 2015/01/09
 */
public class UploadFile4x extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// 上传文件的大小
	protected long MAX_SIZE = 1024*1024*100;
	private static StringBuffer extPatternStr = new StringBuffer("doc|docx|xls|xlsx|txt|jpg|gif|png|rar|zip|tif|pdf|mp4|avi|rmvb|rm|asf|divx|mpg|mpeg|mpe|wmv|mkv|vob");
	private static Pattern extPattern = Pattern.compile(extPatternStr.toString());
	//-- 文件上传目录
	private static final String FILE_UPLOAD_EXTERIOR = "attachment";
	// 定义上传文件保存目录路径
	protected String UPLOAD_PATH = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String downPath = App.IMG.getDomain(request.getSession());// 支持多域名
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String savePath = this.getInitParameter("UPLOAD_PATH");
		if (savePath == null || savePath.equals("")) {
			out.println(alertMsg("你还没设置上传文件保存的目录路径!"));
			return;
		}

		// 文件保存目录URL
		String saveUrl = "";

		if (!ServletFileUpload.isMultipartContent(request)) {
			out.println(alertMsg("请选择要上传的文件。"));
			return;
		}
		
		if (ServletFileUpload.isMultipartContent(request)) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				List items = upload.parseRequest(request);
				Iterator itr = items.iterator();
				while (itr.hasNext()) {
					FileItem item = (FileItem) itr.next();
					if ((!item.isFormField()) && (item.getName().length() > 0)) {
						String extName = FilenameUtils.getExtension(item.getName()).toLowerCase(Locale.ENGLISH);
						if(item.getSize()>MAX_SIZE){
							out.println(alertMsg("上传文件大小不能大于10M！"));
							return;
						}
						// 扩展名判断
						if (extPattern.matcher(extName).find()) {
							byte[] multipartFileBytes = getBytesFromFile(item);
							FileUploadService fileUploadService = SpringContextUtil
									.getApplicationContext().getBean(
											FileUploadService.class);
							saveUrl = fileUploadService.uploadSingleFile(
									item.getName(), multipartFileBytes,
									ConstantValue.RESOURSE_DOMAIN_KEY,
									FILE_UPLOAD_EXTERIOR);
							JSONObject obj = new JSONObject();
							obj.put("error", 0);
							obj.put("url", downPath + saveUrl);
							out.println(obj.toString());
						} else {
							out.println(alertMsg("上传文件格式不符合要求！"));
							return;
						}
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			} finally {
				out.flush();
				out.close();
			}
		}
	}

	/**
	 * 输出打印上传失败语句的脚本
	 * 
	 * @param message
	 *            失败信息
	 * 
	 * @return 页面打印的脚本语句
	 */
	public String alertMsg(String message) {
		JSONObject obj = new JSONObject();
		obj.put("error", 1);
		obj.put("message", message);
		return obj.toString();
	}

	/**
	 * 输出允许上传图片类型的数组
	 * 
	 * @param filesType
	 *            允许上传的图片类型
	 * 
	 * @return 允许上传图片类型
	 */
	public String[] toArray(String filesType) {

		if (filesType == null) {
			return null;
		}

		String[] types = filesType.split(",");
		String[] allowTypes = new String[types.length];
		int i = 0;
		for (String type : types) {
			allowTypes[i] = type;
			i++;
		}

		return allowTypes;
	}

	private  byte[] getBytesFromFile(FileItem item) throws IOException {
        //file size
        long length =item.getSize();
        InputStream is = null;
        is = item.getInputStream();
        if (length > Integer.MAX_VALUE) {
             throw new IOException("File is to large " +item.getName());
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
             offset += numRead;
        }
         if (offset < bytes.length) {
                 throw new IOException("Could not completely read file " + item.getName());
        }
        is.close();
        return bytes;

  }
}
