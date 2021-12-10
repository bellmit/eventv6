package cn.ffcs.zhsq.filter;

import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.uam.service.IFunConfigurationService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

/**
 * 诉求拦截器
 */
public class PublicDemandInterceptor implements HandlerInterceptor {
    /** Logger available to subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private IFunConfigurationService funConfigurationService;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private IAttachmentService attachmentService;


    private String img_url = "http://img2.sq.aishequ.org";

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("----------->Pre-handle");
        System.out.println("preHandle");
        return true;
    }

    public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {
        if(modelAndView == null) return;
        System.out.println("postHandle");
        logger.debug("----------->postHandle");
    }

    public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.out.println("afterCompletion");
        logger.debug("----------->After completion handle");
        System.out.println(handler);
        ServletOutputStream outputStream = response.getOutputStream();


        String updateUrl = funConfigurationService.changeCodeToValue("CMD_PATH_CFG","atts_url", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        /*if(StringUtils.isNotBlank(updateUrl)){
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String params = "bizId="+bo.getOutId();

            String s = HttpUtil.doPost(updateUrl, params);

            List<Attachment> attachments = new ArrayList<Attachment>();
            List<Map<String, Object>> attrs = new ArrayList<Map<String, Object>>();
            if(StringUtils.isNotBlank(s)){
                JSONObject jsonObject = JSONObject.fromObject(s);
                if(jsonObject != null){
                    if(null != jsonObject.get("attrs")){
                        JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("attrs"));
                        Iterator iterator = jsonArray.iterator();
                        while (iterator.hasNext()){
                            Map<String, Object> map = new HashMap<String, Object>();
                            JSONObject next = (JSONObject)iterator.next();
                            String fileName = (String)next.get("fileName");
                            String filePath = (String)next.get("filePath");
                            map.put("fileName",fileName);
                            map.put("filePath",img_url + filePath);
                            attrs.add(map);

                            InputStream inputStream = HttpUtil.returnBitMap(img_url + filePath);
                            byte[] bytes = HttpUtil.readInputStream(inputStream);
                            String imgUrl = fileUploadService.uploadSingleFile(fileName, bytes, "PUBLIC_APPEAL", "perfile");

                            Attachment attachment = new Attachment();
                            attachment.setBizId(bo.getAppealId());
                            attachment.setAttachmentType("PUBLIC_APPEAL");
                            attachment.setEventSeq("1");
                            attachment.setFilePath(imgUrl);
                            attachment.setFileName(fileName);
                            attachments.add(attachment);
                        }
                        attachmentService.saveAttachment(attachments);
                        bo.setAttrs(attrs);
                    }
                }
            }
        }*/
    }
}
