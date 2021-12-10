package cn.ffcs.zhsq.servlet;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.UserManageOutService;

public class UploadFileServlet extends HttpServlet implements Servlet {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// 文件上传
	public static final String FILE_UPLOAD = "up";
	// 文件下载
	public static final String FILE_DOWN = "down";

	// -- 文件上传目录
	private static final String FILE_UPLOAD_EXTERIOR = "attachment";

	// 文件下载
	public static final String FILE_DELETE = "delete";

	private static final String POINT = ".";

	private static final Pattern picPattern = Pattern.compile("jpg|png");
	private static final Pattern extPattern = Pattern.compile("doc|docx|xls|xlsx|txt|jpg|gif|png|rar|zip|tif|pdf|amr|mp3");

	private WebApplicationContext ctx;

	@Autowired
	private IAttachmentService attachmentService;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private UserManageOutService userManageService;
	
	public void init(ServletConfig config) throws ServletException {
		ctx = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		attachmentService = ctx.getBean(IAttachmentService.class);
		fileUploadService = ctx.getBean(FileUploadService.class);
		userManageService = ctx.getBean(UserManageOutService.class);
		super.init(config);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		String moudle = null;// 对应模块
		String method = request.getParameter("method");
		//String rootPath = request.getSession().getServletContext().getRealPath("/");
		//request.setCharacterEncoding("UTF-8");
		String eventSeq = request.getParameter("eventSeq");
		String userId = request.getParameter("___userId");
		String appCode = request.getParameter("___appCode");
		String partyName = null;//request.getParameter("___partyName");
		// 文件上传
		if (FILE_UPLOAD.equals(method)) {
			if(StringUtils.isNotBlank(userId)) {
				Long userIdL = -1L;
				
				try {
					userIdL = Long.valueOf(userId);
				} catch(NumberFormatException e) {
					userIdL = -1L;
					userId = null;
				}
				
				if(userIdL > 0) {
					UserBO user = userManageService.getUserInfoByUserId(userIdL);
					if(user != null) {
						partyName = user.getPartyName();
					} else {
						userId = null;
					}
				}
			}
			try {
				moudle = request.getParameter("moudle");
				if (moudle == null || moudle.equals("")) {
					moudle = "interview";
				}
				// 返回数据
				Attachment att = new Attachment();

				if (ServletFileUpload.isMultipartContent(request)) {
					DiskFileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);

					Iterator itr = items.iterator();

					while (itr.hasNext()) {

						FileItem item = (FileItem) itr.next();
						if ((!item.isFormField()) && (item.getName().length() > 0)) {

							String extName = FilenameUtils.getExtension(item.getName()).toLowerCase(Locale.ENGLISH);

							// 扩展名判断
							if (extPattern.matcher(extName).find()) {
								// 当后缀是图片时获得宽度和高度
								if (picPattern.matcher(extName).find()) {
									Image imgOri = javax.imageio.ImageIO.read(item.getInputStream());
									Integer imgWidthOri = imgOri.getWidth(null);
									Integer imgHeightOri = imgOri.getHeight(null);

									att.setImgWidth(imgWidthOri + "");
									att.setImgHeight(imgHeightOri + "");
								}
								byte[] multipartFileBytes = getBytesFromFile(item);
								
								if(StringUtils.isBlank(appCode)) {
									appCode = ConstantValue.RESOURSE_DOMAIN_KEY;
								}
								
								String picUrl = fileUploadService.uploadSingleFile(item.getName(), multipartFileBytes,
										appCode, FILE_UPLOAD_EXTERIOR);
								
								att.setFilePath(picUrl);
								att.setFileName(item.getName());
								att.setFileSize(String.valueOf(item.getSize()));
								att.setTitle(request.getParameter("title"));
								att.setEventSeq(eventSeq);
								if (StringUtils.isNotBlank(userId)) {
									att.setCreatorId(Long.valueOf(userId));
								}
								if (StringUtils.isNotBlank(partyName)) {
									att.setCreatorName(partyName);
								}
								Long id = attachmentService.saveAttachment(att);
								if (id > 0) {
									att.setAttachmentId(id);
								}
							} else {
								response.getWriter().print("{error:'上传文件格式不符合要求！'}");
								return;
							}
						}
					}
					ObjectMapper mapper = new ObjectMapper();
					String message = mapper.writeValueAsString(att);
					response.getWriter().print(message);
				}

			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().print("{error:'未知异常！请检查文件大小、格式是否符合要求！'}");
			}
		} else if (FILE_DOWN.equals(method)) {
			try {

				String filepath = request.getParameter("filepath");
				String path = null;
				Long attachmentId = null;
				String downPath = App.IMG.getDomain(request.getSession());
				if (filepath == null) {
					// 文件下载attachmentId
					attachmentId = request.getParameter("attachmentId") != null ? Long.parseLong(request
							.getParameter("attachmentId")) : 0L;
					Attachment attr = attachmentService.findById(attachmentId);
					if (attr == null) throw new java.io.FileNotFoundException();
					// path = downPath + File.separator + attr.getFilePath();
					this.netDownLoad(ConstantValue.RESOURSE_SAVE_ROOT_PATH, downPath, attr.getFilePath(), request, response, attr.getFileName());
				} else {
					path = downPath + File.separator + filepath;
					response.sendRedirect(path);
				}
			} catch (Exception e) {
				if (e instanceof java.io.FileNotFoundException) {
					this.message(request, response, "下载出错：文件不存在！");
				} else {
					this.message(request, response, "下载出错：" + e.getMessage());
				}
				e.printStackTrace();
			}

		} else if (FILE_DELETE.equals(method)) {
			// 删除文件
			PrintWriter out = response.getWriter();
			try {
				// 删除文件
				Long attachmentId = request.getParameter("attachmentId") != null ? Long.parseLong(request
						.getParameter("attachmentId")) : 0L;
				boolean isDel = attachmentService.deleteAttachmentById(attachmentId);
				String result = "-1";
				String callBack = request.getParameter("callback");
				
				if(isDel) {
					result = "1";
				}
				
				if(StringUtils.isNotBlank(callBack)) {
					result = callBack+"(["+ result +"])";
				}
				out.print(result);
			} catch (Exception e) {
				out.print(-1);
				e.printStackTrace();
			}
		}

	}

	private void download(String path, HttpServletResponse response, String realName) {
		try {
			// path是指欲下载的文件的路径。
			File file = new File(path);
			String filename = file.getName();
			String ext = filename.substring(filename.lastIndexOf(POINT) + 1);// .toUpperCase();

			String newfilename = filename;
			if (realName != null && !"".equals(realName))
				newfilename = realName + POINT + ext;

			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(newfilename, "UTF-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (FileNotFoundException e) {
			System.out.println("---------文件下载失败，该文件不存在。-----------");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private byte[] getBytesFromFile(FileItem item) throws IOException {
		// file size
		long length = item.getSize();
		InputStream is = null;
		is = item.getInputStream();
		if (length > Integer.MAX_VALUE) {
			throw new IOException("File is to large " + item.getName());
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
	
	class ResourceFile {
		InputStream inputStream = null;
		long fileSize = 0;
		String realName = "";
		boolean isOpen = false;
	}
	
	private ResourceFile getResourceFile(String rootPath, String realPath, HttpServletRequest request, String realName)
			throws Exception {
		ResourceFile resourceFile = new ResourceFile();
		String agent = request.getHeader("USER-AGENT");
		if (null != agent && -1 != agent.indexOf("MSIE")) {// IE
			resourceFile.realName = java.net.URLEncoder.encode(realName, "UTF-8");
		} else if (null != agent && -1 != agent.indexOf("Mozilla")) {// Firefox
			resourceFile.realName = new String(realName.getBytes("UTF-8"), "iso-8859-1");
		} else {
			resourceFile.realName = java.net.URLEncoder.encode(realName, "UTF-8");
		}
		String extName = FilenameUtils.getExtension(realPath).toLowerCase(Locale.ENGLISH);
		resourceFile.isOpen = picPattern.matcher(extName).find();
		if (StringUtils.isNotBlank(rootPath)) {
			if (rootPath.startsWith("http")) {
				URL url = new URL(rootPath + realPath);
				URLConnection conn = url.openConnection();
				resourceFile.inputStream = conn.getInputStream();
				resourceFile.fileSize = conn.getContentLength();
			} else {
				File file = new File(rootPath + realPath);
				resourceFile.inputStream = new FileInputStream(file);
				resourceFile.fileSize = file.length();
			}
		} else {
			throw new Exception("未指定文件服务器跟目录！");
		}
		return resourceFile;
	}
	
	private void netDownLoad(String localRootPath, String httpRootPath, String realPath, HttpServletRequest request,
			HttpServletResponse response, String realName) throws Exception {
		OutputStream out = null;
		ResourceFile resourceFile = null;
		try {
			// 第一次请求本地资源
			logger.info("第一次请求本地资源地址：" + localRootPath + realPath);
			resourceFile = this.getResourceFile(localRootPath, realPath, request, realName);
		} catch(Exception e) {
			logger.error("请求本地资源出错：" + e.getMessage());
			e.printStackTrace();
			logger.info("第二次请求网络资源地址：" + httpRootPath + realPath);
			// 第二次请求网络资源（一般要配host指向文件服务器）
			resourceFile = this.getResourceFile(httpRootPath, realPath, request, realName);
		}
		try {
			if (resourceFile.isOpen) {
				response.addHeader("Content-Disposition", "filename=" + resourceFile.realName);
			} else {
				response.setContentType("application/x-msdownload");
				response.addHeader("Content-Disposition", "attachment;filename=" + resourceFile.realName);
			}
			response.addHeader("Content-Length", String.valueOf(resourceFile.fileSize));
			out = response.getOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = resourceFile.inputStream.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
			if (resourceFile.inputStream != null) resourceFile.inputStream.close();
		}
	}

	private void message(HttpServletRequest request, HttpServletResponse response, String msg) throws IOException {
		response.setHeader("content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		msg = new String(msg.getBytes("UTF-8"));
		Writer writer = response.getWriter();
		writer.write("<script>alert('" + msg + "');window.opener=null;window.open('','_self');window.close();</script>");
		writer.flush();
		writer.close();
	}
}