package cn.ffcs.zhsq.base.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.file.service.FileUrlProvideService;
import cn.ffcs.lzly.mybatis.domain.file.FileSuffix;
import cn.ffcs.shequ.json.JsonUtils;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.zhsq.utils.domain.App;


@RequestMapping("file")
@Controller
public class FileController {
	@Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private FileUrlProvideService fileUrlProvideService;
    @Autowired
    private IAttachmentService attachmentService;
    
    @RequestMapping("deleteByAttId/{attaId}")
    @ResponseBody
    public Object deleteByAttId(@PathVariable("attaId")Long attaId){
    	return attachmentService.deleteAttachmentById(attaId);
    }
    
    /**
     * 根据附件id或者路径下载文件
     * @param attaId
     * @param path
     * @param response
     */
    @RequestMapping("downloadFile")
    public void downloadFile(Long attaId,String path,HttpServletResponse response,HttpServletRequest request){
    	String fileName = null;
    	if(path != null){
    		if(path.startsWith("http://"))
    			path = path.replaceFirst(fileUrlProvideService.getFileDomain(), "");
    		fileName = "download."+FileSuffix.getSuffix(path);
    	}
    	if(attaId != null){
    		Attachment att = attachmentService.findById(attaId);
    		if(att != null){
    			path = att.getFilePath();
    			fileName = att.getFileName();
    			try {
					downLoad(request,response,att);
				} catch (Exception e) {
					e.printStackTrace();
				}
    		}
    	} else {
        	byte[] buffer = fileUploadService.downloadFileWithPath(path);
            response.reset();// 清空response
            try {
    			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
    			response.setContentType("application/octet-stream");

    			// 解决文件名乱码处理，判断浏览器类型
    			String userAgent = request.getHeader("user-agent").toLowerCase();
    			if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
    				// win10 ie edge 浏览器 和其他系统的ie
    				fileName = URLEncoder.encode(fileName, "UTF-8");
    			} else {
    				// 非ie
    				fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
    			}
                
    			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    			toClient.write(buffer);
    			toClient.flush();
    			toClient.close();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    
    private void downLoad(HttpServletRequest request, HttpServletResponse response, Attachment attr) throws Exception {
		OutputStream out = null;
		byte[] buffer = fileUploadService.downloadFileWithPath(attr.getFilePath());
		if (buffer == null || buffer.length == 0) throw new java.io.FileNotFoundException();
		try {
			String agent = request.getHeader("USER-AGENT");
			String fileName = attr.getFileName();
			// 解决文件名乱码处理，判断浏览器类型
			String userAgent = request.getHeader("user-agent").toLowerCase();
			if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
				// win10 ie edge 浏览器 和其他系统的ie
				fileName = URLEncoder.encode(fileName, "UTF-8");
			} else {
				// 非ie
				fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
			}
			response.setContentType("application/x-msdownload");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			out = response.getOutputStream();
			out.write(buffer);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	/**
	 * 上传文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("uploadFile")
	public void uploadFile(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "limitSize", required = false) String limitSize) throws IOException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<MultipartFile> uploadFiles = getUploadFiles(request);
		if (limitSize != null && !"".equals(limitSize)) {
			Integer lmt = Integer.valueOf(limitSize);
			Long length = uploadFiles.get(0).getSize();
			if (length > lmt * 1024 * 1024) {
				resultMap.put("result", "limit");
				resultMap.put("limit", lmt);
				String s = JsonUtils.mapToJson(resultMap);
				response.getWriter().write(s);
				return;
			}
		}

		Map<String, Object> oneFileResultMap = new HashMap<String, Object>();
		List<Map<String, Object>> uploadFileResultList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < uploadFiles.size(); i++) {
			MultipartFile file = uploadFiles.get(i);
			try {
				String tmpPath = System.getProperty("java.io.tmpdir");// 先放入临时文件夹
				String tmpFileName = String.valueOf(System.currentTimeMillis());
				File tempFile = new File(tmpPath, tmpFileName);
				FileOutputStream out = new FileOutputStream(tempFile);
				out.flush();
				out.write(file.getBytes());
				out.close();
				oneFileResultMap = uploadFile(file.getOriginalFilename(),tempFile,request.getSession());
				uploadFileResultList.add(oneFileResultMap);
			} catch (Exception e) {
				resultMap.put("result", e.getMessage());// 出错就直接返回错误信息
				String s = JsonUtils.mapToJson(resultMap);
				response.getWriter().write(s);
				return;
			}
		}
		resultMap.put("result", "success");
		resultMap.put("resultList", uploadFileResultList);
		response.setContentType("text/html");
		String s = JsonUtils.mapToJson(resultMap);
		response.getWriter().write(s);
	}

	/**
	 * 获得批量的上传文件集合
	 * 
	 * @param request
	 * @return
	 */
	private List<MultipartFile> getUploadFiles(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		List<MultipartFile> files = new ArrayList<MultipartFile>();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile mf = entity.getValue();
			files.add(mf);
		}
		return files;
	}

	private Map<String, Object> uploadFile(String fileName, File file,HttpSession session)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String path, showPath;
		byte[] bytes = FileUtils.readFileToByteArray(file);
		path = fileUploadService.uploadSingleFile(fileName, bytes, "xinj", "workorder");
		showPath = App.IMG.getDomain(session) + path;
		resultMap.put("result", "success");
		resultMap.put("fileName", fileName);
		resultMap.put("path", path.trim());// 数据库保存的路径
		resultMap.put("showPath", showPath.trim());// 前台保存的路径
		return resultMap;
	}
	
	
	
	
	/**
	 * 提供给app端的图片上传 luth add
	 * @param request
	 * @return
	 */
	@RequestMapping("addAttachment")
	@ResponseBody
	public Map<String,Object> addAttachment(HttpServletRequest request) {
		
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> data = new HashMap<String, Object>();
		
		String domain = fileUrlProvideService.getFileDomain();
		data.put("domain", domain);
		try{
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> files = multipartRequest.getFiles("files");
			List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
			
			for (int i = 0; i < files.size(); i++) {
				MultipartFile file = files.get(i);
			    String fileNameP = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('/')+1,file.getOriginalFilename().length());
				String fileName = fileNameP.substring(0,fileNameP.lastIndexOf('.'));
				//将文件存放到相应路径
				String path = fileUploadService.uploadSingleFile(fileName, file.getBytes(), "xinj", "workorder");
				Attachment att = new Attachment();
				att.setFileName(fileName);
				att.setFilePath(path);
				String id = attachmentService.saveAttachment(att).toString();
				//返回附件ID和附件存放url
				Map<String, Object> item = new HashMap<String, Object>();
				//
//				String id=DateUtils.formatDate(new Date(),"yyyyMMddHHmmss");
				item.put("id", id);
				item.put("url", domain+path);
				item.put("path", path);
				item.put("fileName", fileName);
				System.out.println("path~~~~~~~~"+domain+path);
				itemList.add(item);
			}
			data.put("list", itemList);
			map.put("data", data);
			map.put("desc", "");
			map.put("status", 0);
			return map;
		}catch(Exception e){
			e.printStackTrace();
			map.put("desc", "上传错误");
			map.put("status", 1);
			return map;
		}
		
	}
	
}
