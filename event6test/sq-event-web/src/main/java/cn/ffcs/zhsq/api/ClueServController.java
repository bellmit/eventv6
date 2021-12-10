package cn.ffcs.zhsq.api;/**
 * Created by Administrator on 2017/8/19.
 */

import cn.ffcs.common.FileUtils;
import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.intermediateData.eventVerify.service.IEventVerifyBaseService;
import cn.ffcs.zhsq.mybatis.domain.publicAppeal.PublicAppeal;
import cn.ffcs.zhsq.publicAppeal.service.PublicAppealService;
import cn.ffcs.zhsq.utils.Base64;
import cn.ffcs.zhsq.utils.ConstantValue;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 *
 */
@Controller
@RequestMapping("/service/clue")
public class ClueServController {

    @Autowired
    private PublicAppealService publicAppealService; //注入公众诉求模块服务

    @Autowired
    private IEventVerifyBaseService eventVerifyBaseService;
    @Autowired
    private IAttachmentService attachmentService;
    @Autowired
    private FileUploadService fileUploadService;

    private static String BIZ_PLATFORM = "202";
    private static String APP_KEY = "07a5069a05fe4f9329ec595cdfc25009";//接口凭证

    @ResponseBody
    @RequestMapping(value="/test")
    public String test(@RequestParam(value="name",defaultValue="success") String name){
//        Map<String, Object> eventVerify = new HashMap<String, Object>();
//        eventVerify.put("infoOrgCode","35");
//        eventVerify.put("userOrgCode","35");
//        eventVerify.put("bizPlatform","202");
//
//        EUDGPagination eventVerifyPagination = eventVerifyBaseService.findEventVerifyPagination(0, 100, eventVerify);
//        System.out.println(eventVerifyPagination);

        return name;
    }

    @ResponseBody
    @RequestMapping(value="/report", produces="application/json;charset=UTF-8")
    public String save(HttpServletRequest request, HttpSession session, ModelMap map) {
        JSONObject resultJson = new JSONObject();
        String jsonStr = null;
//        try {
//            InputStream is = request.getInputStream();
//            if(request.getContentLength() > 0){
//                System.out.println(request.getContentLength());
//                byte bytes[] = new byte[request.getContentLength()];
//                is.read(bytes);
//                System.out.println(request.getCharacterEncoding());
//                jsonStr = new String(bytes, request.getCharacterEncoding());
//                System.out.println("json data：" + jsonStr);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            jsonStr = FileUtils.readData(request.getInputStream(), request.getCharacterEncoding());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            InputStream is = request.getInputStream();
//            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
//            byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据
//            int rc = 0;
//            while ((rc = is.read(buff, 0, 100)) > 0) {
//                swapStream.write(buff, 0, rc);
//            }
//            byte[] in_b = swapStream.toByteArray(); //in_b为转换之后的结果
//            jsonStr = new String(in_b, request.getCharacterEncoding());
//            System.out.println("json data：" + jsonStr);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



        if(StringUtils.isBlank(jsonStr)){
            resultJson.put("status", 0);
            resultJson.put("desc", "数据格式有误");
            resultJson.put("data", "");
            return resultJson.toString();
        }
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        String check = check(jsonObject);
        if(!check.equals("{}"))
            return check;

        Map<String, Object> eventVerify = new HashMap<String, Object>();
        eventVerify.put("content", jsonObject.get("content"));
        eventVerify.put("contactUser", jsonObject.get("contactUser"));
        eventVerify.put("tel", jsonObject.get("tel"));
        eventVerify.put("happenTimeStr", jsonObject.get("happenTimeStr"));
        eventVerify.put("occurred", jsonObject.get("occurred"));
        eventVerify.put("code", jsonObject.get("code"));
        eventVerify.put("bizId", jsonObject.get("oppoSideBusiCode"));
        eventVerify.put("latitude", jsonObject.get("x"));
        eventVerify.put("longitude", jsonObject.get("y"));
        eventVerify.put("dataJson", jsonStr);
//        eventVerify.put("attachmentType", ConstantValue.EVENT_CLUE_ATTACHMENT_TYPE);
//        eventVerify.put("markerType", ConstantValue.EVENT_WECHAT_MODULE_CODE);
        eventVerify.put("bizPlatform", BIZ_PLATFORM);


        Long saveEventVerify = null;
        try {
            saveEventVerify = eventVerifyBaseService.saveEventVerify(eventVerify);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Long saveEventVerify = -1L;

        if(jsonObject.get("attr") != null){
            JSONArray attrJsonArray = JSONArray.fromObject(jsonObject.get("attr"));
            System.out.println(attrJsonArray);
            List<Attachment> attachments = new ArrayList<Attachment>();
            Iterator<Object> it = attrJsonArray.iterator();
            while (it.hasNext()){
                JSONObject attrJsonObj = (JSONObject) it.next();
                String attrName = attrJsonObj.get("attrName").toString();
                String attrType = attrJsonObj.get("attrType").toString();
                String base64 = attrJsonObj.get("base64").toString();

                String imgUrl = fileUploadService.uploadSingleFile(attrName+attrType, Base64.decode(base64), ConstantValue.EVENT_CLUE_ATTACHMENT_TYPE, "perfile");

                Attachment attachment = new Attachment();
                attachment.setBizId(saveEventVerify);
                attachment.setAttachmentType(ConstantValue.EVENT_CLUE_ATTACHMENT_TYPE);
                attachment.setFilePath(imgUrl);
                attachment.setFileName(attrName);
                attachment.setEventSeq("1");
                attachments.add(attachment);
            }
            Long saveAttachment = attachmentService.saveAttachment(attachments);
            System.out.println("saveAttachment-"+saveAttachment);
        }

        resultJson.put("status", 1);
        resultJson.put("desc", "操作成功");
        resultJson.put("data", saveEventVerify);
        return resultJson.toString();
    }

    private String check(JSONObject jsonObject){
        JSONObject resultObject = new JSONObject();
        if(jsonObject.get("appkey") == null){
            resultObject.put("status", 0);
            resultObject.put("desc", "[appkey]网格平台分配的接口调用凭证不可为空");
            resultObject.put("data", "");
            return resultObject.toString();
        }
        if(!jsonObject.get("appkey").equals(APP_KEY)){
            resultObject.put("status", 0);
            resultObject.put("desc", "[appkey]网格平台分配的接口调用凭证无效");
            resultObject.put("data", "");
            return resultObject.toString();
        }
        if(jsonObject.get("content") == null){
            resultObject.put("status", 0);
            resultObject.put("desc", "[content]线索内容不可为空");
            resultObject.put("data", "");
            return resultObject.toString();
        }
        if(jsonObject.get("contactUser") == null){
            resultObject.put("status", 0);
            resultObject.put("desc", "[contactUser]反馈人姓名不可为空");
            resultObject.put("data", "");
            return resultObject.toString();
        }
        if(jsonObject.get("tel") == null){
            resultObject.put("status", 0);
            resultObject.put("desc", "[tel]反馈人手机号码不可为空");
            resultObject.put("data", "");
            return resultObject.toString();
        }
        if(jsonObject.get("happenTimeStr") == null){
            resultObject.put("status", 0);
            resultObject.put("desc", "[happenTimeStr]线索举报时间不可为空");
            resultObject.put("data", "");
            return resultObject.toString();
        }
        if(jsonObject.get("occurred") == null){
            resultObject.put("status", 0);
            resultObject.put("desc", "[occurred]地点不可为空");
            resultObject.put("data", "");
            return resultObject.toString();
        }
        if(jsonObject.get("y") == null){
            resultObject.put("status", 0);
            resultObject.put("desc", "[y]经度不可为空");
            resultObject.put("data", "");
            return resultObject.toString();
        }
        if(jsonObject.get("x") == null){
            resultObject.put("status", 0);
            resultObject.put("desc", "[x]纬度不可为空");
            resultObject.put("data", "");
            return resultObject.toString();
        }
        if(jsonObject.get("code") == null){
            resultObject.put("status", 0);
            resultObject.put("desc", "[code]线索编号不可为空");
            resultObject.put("data", "");
            return resultObject.toString();
        }
        if(jsonObject.get("oppoSideBusiCode") == null){
            resultObject.put("status", 0);
            resultObject.put("desc", "[oppoSideBusiCode]第三方平台线索唯一标志不可为空");
            resultObject.put("data", "");
            return resultObject.toString();
        }
        if(jsonObject.get("attr") != null){
            JSONArray attrJsonArray = JSONArray.fromObject(jsonObject.get("attr"));
            Iterator<Object> it = attrJsonArray.iterator();
            while (it.hasNext()){
                JSONObject attrJsonObj = (JSONObject) it.next();
                if(attrJsonObj.get("attrName") == null){
                    resultObject.put("status", 0);
                    resultObject.put("desc", "[attrName]附件名不可为空");
                    resultObject.put("data", "");
                    return resultObject.toString();
                }
                if(attrJsonObj.get("attrType") == null){
                    resultObject.put("status", 0);
                    resultObject.put("desc", "[attrType]图片类型不可为空");
                    resultObject.put("data", "");
                    return resultObject.toString();
                }
                if(attrJsonObj.get("base64") == null){
                    resultObject.put("status", 0);
                    resultObject.put("desc", "[base64]base64编码不可为空");
                    resultObject.put("data", "");
                    return resultObject.toString();
                }
                if(!(attrJsonObj.get("attrType").equals(".png") || attrJsonObj.get("attrType").equals(".jpg"))){
                    resultObject.put("status", 0);
                    resultObject.put("desc", "[attrType]图片类型不正确");
                    resultObject.put("data", "");
                    return resultObject.toString();
                }
            }
        }
        System.out.println(jsonObject);
        return resultObject.toString();
    }
}
