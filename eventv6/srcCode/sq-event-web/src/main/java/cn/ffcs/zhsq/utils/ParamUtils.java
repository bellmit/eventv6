package cn.ffcs.zhsq.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class ParamUtils {
	private static Log log = LogFactory.getLog(ParamUtils.class);

    public static String getString(HttpServletRequest request, String paramName) {
        return request.getParameter(paramName);
    }

    public static String getString(HttpServletRequest request, String paramName, String defaultString) {
        String temp = request.getParameter(paramName);
        return (temp == null)||(temp.length()<=0) ? defaultString : temp;
    }
    
    public static String getDecodeString(HttpServletRequest request, String paramName, String defaultString) {
        return getDecodeString(request, paramName, defaultString, "GBK");
    }
    public static int getInt(HttpServletRequest request, String paramName) {
        return Integer.parseInt(request.getParameter(paramName));
    }
    public static int getInt(HttpServletRequest request, String paramName,int defaultVal) {
        try {
            return Integer.parseInt(request.getParameter(paramName));
        } catch(Exception e) {
            return defaultVal;
        }
    }
    public static long getLong(HttpServletRequest request, String paramName) {
        return Long.parseLong(request.getParameter(paramName));
    }
    
    public static long getLong(HttpServletRequest request, String paramName,long defaultVal) {
        try {
            return Long.parseLong(request.getParameter(paramName));
        } catch(Exception e) {
            return defaultVal;
        }
    }
    public static double getDouble(HttpServletRequest request, String paramName) {
        return Double.parseDouble(request.getParameter(paramName));
    }

    public static double getDouble(HttpServletRequest request, String paramName,double defaultVal) {
        try {
            return Double.parseDouble(request.getParameter(paramName));
        } catch(Exception e) {
            return defaultVal;
        }
    }
    public static String getDecodeString(HttpServletRequest request, String paramName, String defaultString, String charset) {
        String temp = request.getParameter(paramName);
        temp = temp == null ? defaultString : temp;

        try {
            return java.net.URLDecoder.decode(temp, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return temp;
        }
    }

    /**
     *  保存MultipartFile 到指定目录
     * @param multipartFile  文件对象
     * @param serverPath 图片服务器根路径
     * @param folder   图片存在在服务器根路径下的文件夹路径
     * @return
     * @throws Exception
     */
    public static java.util.Map saveMultipartFile(MultipartFile multipartFile,String serverPath,String folder){
        try {
             if(!serverPath.endsWith("/") && !folder.startsWith("/")) folder = "/"+folder;
            if(!folder.endsWith("/")) folder += "/";

            java.text.SimpleDateFormat formatFileName = new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS");
            //java.text.SimpleDateFormat format_1 = new java.text.SimpleDateFormat("yyyyMMdd");
            String oriFileName = multipartFile.getOriginalFilename();
            String newFileName = formatFileName.format(new Date())+oriFileName.substring(oriFileName.indexOf("."),oriFileName.length());
            String serverPathDir =  serverPath+folder;
            //目录
            File folderObj = new File(serverPathDir);
            if (!folderObj.exists()) {
                folderObj.mkdirs();
            }
            //文件
            String filePath = serverPathDir+newFileName;
           // File file = new File(filePath);
            //file.deleteOnExit();
            log.debug("------------>"+oriFileName);
            log.debug("------------>"+filePath);
            java.io.FileOutputStream out = new java.io.FileOutputStream(filePath);
            java.io.InputStream in = multipartFile.getInputStream();
            byte[] buffer = new byte[2048];
            while(in.read(buffer) != -1) {
                out.write(buffer);
            }
            out.flush();
            if(out != null) out.close();
            if(in != null) in.close();
            java.util.Map retMap = new java.util.HashMap();
            retMap.put("fileName",newFileName);
            retMap.put("filePath",folder+newFileName);
            return retMap;
        } catch(Exception e) {
            e.printStackTrace();
            log.error("图片上传异常:"+e.toString());
        }
        return null;
    }
    
    /**
     *  保存MultipartFile 到指定目录
     * @param multipartFile  文件对象
     * @param serverPath 图片服务器根路径
     * @param folder   图片存在在服务器根路径下的文件夹路径
     * @return
     * @throws Exception
     */
    public static java.util.Map saveMultipartFileNew(MultipartFile multipartFile,String serverPath,String folder){
        try {
             if(!serverPath.endsWith("/") && !folder.startsWith("/")) folder = "/"+folder;
            if(!folder.endsWith("/")) folder += "/";

            java.text.SimpleDateFormat formatFileName = new java.text.SimpleDateFormat("yyyyMMdd");
            String dateStr = formatFileName.format(new Date());
            String oriFileName = multipartFile.getOriginalFilename();
            long time=System.currentTimeMillis();
    		String newFileName=oriFileName+"_"+time;
    		newFileName = MD5.compile(newFileName);
            newFileName = newFileName+oriFileName.substring(oriFileName.indexOf("."),oriFileName.length());
            String serverPathDir =  (serverPath+folder+dateStr);
            //目录
            File folderObj = new File(serverPathDir);
            if (!folderObj.exists()) {
                folderObj.mkdirs();
            }
            //文件
            String filePath = serverPathDir+"/"+newFileName;
           // File file = new File(filePath);
            //file.deleteOnExit();
            log.debug("------------>"+oriFileName);
            log.debug("------------>"+filePath);
            java.io.FileOutputStream out = new java.io.FileOutputStream(filePath);
            java.io.InputStream in = multipartFile.getInputStream();
            byte[] buffer = new byte[2048];
            while(in.read(buffer) != -1) {
                out.write(buffer);
            }
            out.flush();
            if(out != null) out.close();
            if(in != null) in.close();
            java.util.Map retMap = new java.util.HashMap();
            retMap.put("fileName",newFileName);
            retMap.put("filePath",(folder+dateStr+"/"+newFileName));
            return retMap;
        } catch(Exception e) {
            e.printStackTrace();
            log.error("图片上传异常:"+e.toString());
        }
        return null;
    }

    public static java.util.List saveMultipartFiles(MultipartFile[] multipartFile,String serverPath,String folder){
       return null;
    }
}
