package cn.ffcs.zhsq.executeControl.service.impl;

import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.utils.SSLClient;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.executeControl.service.IControlPersonnelService;
import cn.ffcs.zhsq.mybatis.domain.executeControl.ControlPersonnel;
import cn.ffcs.zhsq.mybatis.domain.executeControl.ControlPersonnelTwo;
import cn.ffcs.zhsq.mybatis.domain.executeControl.ObjectInfos;
import cn.ffcs.zhsq.mybatis.domain.executeControl.SelfAttr;
import cn.ffcs.zhsq.mybatis.persistence.executeControl.ControlPersonnelMapper;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @Description: 布控库对象模块服务实现
 * @Author: dtj
 * @Date: 07-16 20:37:45
 * @Copyright: 2020 福富软件
 */
@Service("controlPersonnelServiceImpl")
@Transactional
public class ControlPersonnelServiceImpl implements IControlPersonnelService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ControlPersonnelMapper controlPersonnelMapper; //注入布控库对象模块dao

    @Autowired
    private IBaseDictionaryService baseDictionaryService;//字典

    @Autowired
    private IFunConfigurationService funConfigurationService;

    @Autowired
    private IAttachmentService attachmentService;
    @Autowired
    private FileUploadService fileUploadService;

    @Value(value = "${DOMAIN_GANZHOU:jxsr-eye.antelopecloud.cn}")
    private String domain;

    @Value(value="${LOGINNAME_GANZHOU:18970005533_ysq}")
    private String loginName;

    @Value(value="${PASSWORD_GANZHOU:Kd_123456}")
    private String password;


    /**
     * 新增数据
     *
     * @param bo 布控库对象业务对象
     * @return 布控库对象id
     */
    @Override
    public Long insert(ControlPersonnel bo, String token) throws Exception{
        //获取图片地址
        List<String> imageUrlList = getImageUrl(token, bo);
        List<ObjectInfos> listInfo = new ArrayList<>();
        for (String imageUrl : imageUrlList) {
            ObjectInfos objectInfos = new ObjectInfos();
            objectInfos.setLibType(bo.getLibType());
            objectInfos.setImageUrl(imageUrl);
            listInfo.add(objectInfos);
        }
        String url = "https://" + domain + "/api/alarm/v1/monitorLib/addMonitorLibPerson";
        String gender = "男";
        if (bo.getGender() != null && !"".equals(bo.getGender())) {
            if (bo.getGender().equals("F")) {
                gender = "女";
            }
        }
        String birthdayStr = DateUtils.formatDate(bo.getBirthday(),"yyyy-MM-dd");
        ControlPersonnelTwo controlPersonnelTwo = new ControlPersonnelTwo();
        controlPersonnelTwo.setLibId(bo.getControlLibraryId());
        SelfAttr selfAttr = new SelfAttr();
        BeanUtils.copyProperties(selfAttr,bo);
        selfAttr.setBirthday(birthdayStr);
        selfAttr.setGender(gender);
        controlPersonnelTwo.setSelfAttr(selfAttr);
        controlPersonnelTwo.setObjectInfos(listInfo);
        String json = JSON.toJSONString(controlPersonnelTwo);
        JSONObject jsonObject = doPost(url, token, json);
        if (jsonObject != null){
            String objectMainId = jsonObject.getString("objectMainId");
            bo.setControlObjectId(objectMainId);
            controlPersonnelMapper.insert(bo);
            return bo.getId();
        }
       return 0L;
    }

    /**
     * 修改数据
     *
     * @param bo 布控库对象业务对象
     * @return 是否修改成功
     */
    @Override
    public boolean update(ControlPersonnel bo, String token) throws Exception{
        //获取图片地址
        List<String> imageUrlList = getImageUrl(token, bo);
        List<ObjectInfos> listInfo = new ArrayList<>();
        for (String imageUrl : imageUrlList) {
            ObjectInfos objectInfos = new ObjectInfos();
            objectInfos.setLibType(bo.getLibType());
            objectInfos.setImageUrl(imageUrl);
            listInfo.add(objectInfos);
        }
        String url = "https://" + domain + "/api/alarm/v1/monitorLib/addMonitorLibPerson";
        String gender = "男";
        if (bo.getGender() != null && !"".equals(bo.getGender())) {
            if (bo.getGender().equals("F")) {
                gender = "女";
            }
        }
        String birthdayStr = DateUtils.formatDate(bo.getBirthday(),"yyyy-MM-dd");
        ControlPersonnelTwo controlPersonnelTwo = new ControlPersonnelTwo();
        controlPersonnelTwo.setLibId(bo.getControlLibraryId());
        SelfAttr selfAttr = new SelfAttr();
        BeanUtils.copyProperties(selfAttr,bo);
        selfAttr.setBirthday(birthdayStr);
        selfAttr.setGender(gender);
        controlPersonnelTwo.setSelfAttr(selfAttr);
        controlPersonnelTwo.setObjectInfos(listInfo);
        String json = JSON.toJSONString(controlPersonnelTwo);
        JSONObject jsonObject = doPost(url, token, json);
        if (jsonObject != null){
            long result = controlPersonnelMapper.update(bo);
            return result > 0;
        }
        return false;
    }

    /**
     * 删除数据
     *
     * @param bo 布控库对象业务对象
     * @return 是否删除成功
     */
    @Override
    public boolean delete(ControlPersonnel bo, String token) {
        String url = "https://" + domain + "/api/alarm/v1/monitorLib/deleteMonitorLibPerson/" + bo.getControlObjectId();
        String params = "{\"id\":\""+bo.getControlObjectId()+"\"}";
        try {
            JSONObject jsonObject = doPost(url,token,params);
            //删除图片
            long result = controlPersonnelMapper.delete(bo);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查询数据（分页）
     *
     * @param params 查询参数
     * @return 布控库对象分页数据对象
     */
    @Override
    public EUDGPagination searchList(ControlPersonnel controlPersonnel, Map<String, Object> params) {
        RowBounds rowBounds = new RowBounds((controlPersonnel.getPage() - 1) * controlPersonnel.getRows(), controlPersonnel.getRows());
        List<ControlPersonnel> list = controlPersonnelMapper.searchList(params, rowBounds);
        for (ControlPersonnel bo : list) {
            bo.setNationalityCN(baseDictionaryService.changeCodeToName(ConstantValue.NATIONALITY, bo.getNationality(), null));
        }
        long count = controlPersonnelMapper.countList(params);
        EUDGPagination pagination = new EUDGPagination(count, list);
        return pagination;
    }

    /**
     * 根据业务id查询数据
     *
     * @param id 布控库对象id
     * @return 布控库对象业务对象
     */
    @Override
    public ControlPersonnel searchById(Long id) {
        ControlPersonnel bo = controlPersonnelMapper.searchById(id);
        if (bo != null && bo.getNationality()!= null){
            bo.setNationalityCN(baseDictionaryService.changeCodeToName(ConstantValue.NATIONALITY,bo.getNationality(),null));
        }
        return bo;
    }

    @Override
    public String getToken() {
        String login = this.login();
        return login;
    }

    //赣州登录获取token
    public String login(){
        String url = "https://"+domain+"/api/user/v1/loginWithoutIdentifyCode";
        String passwordBase = new String(Base64.encode(password.getBytes()));
        String params = "{\"loginName\":\""+loginName+"\",\"userPassword\":\""+passwordBase+"\"}";
        Object result = doPost(url,null,params);
        if(result!=null){
            JSONObject jsonObject = (JSONObject)result;
            String token = jsonObject.get("token").toString();
            logger.info("token = "+token);
            return token;
        }else{
            logger.info("赣州登录失败！");
        }
        return null;
    }

    public List<String> getImageUrl(String token, ControlPersonnel bo) {
        String IMG_URL = funConfigurationService.changeCodeToValue("APP_DOMAIN", "$IMG_DOMAIN", IFunConfigurationService.CFG_TYPE_URL);
        List<String> imageUrls = new ArrayList<>();
        if (bo.getAttList()!= null){
            for (Attachment attachment : bo.getAttList()) {
                byte[] bytes = fileUploadService.downloadFileWithPath(attachment.getFilePath());
                File file = null;
                try {
                    // 根据绝对路径初始化文件
                    file = new File(System.getProperty("java.io.tmpdir")+File.separator+bo.getAttList().get(0).getFileName());
                    try {
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        // 输出流
                        OutputStream os = new FileOutputStream(file);
                        os.write(bytes);
                        os.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("fileName",bo.getAttList().get(0).getFileName());
                    String url = "https://"+domain+"/api/alarm/v1/upload/uploadMonitorPersonPic";
                    Map<String, Object> map = uploadFileByHTTP(file, url, hashMap, token);
                    String jsonString = (String) map.get("data");
                    JSONObject jsonObj = JSONObject.parseObject(jsonString);
                    if ("0".equals(jsonObj.get("code").toString())) {
                        imageUrls.add(jsonObj.getJSONObject("data").getString("url"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (file != null) {
                        file.delete();
                    }
                }
            }
        }
        //logger.error("获取羚眸视频图像智能应用平台接口失败");
        return imageUrls;
    }

    /**
     * 模拟表单上传文件
     */
    public static Map<String, Object> uploadFileByHTTP(File postFile, String postUrl, Map<String, String> postParam,String token) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {

            //把一个普通参数和文件上传给下面这个地址    是一个servlet
            HttpPost httpPost = new HttpPost(postUrl);
            //把文件转换成流对象FileBody
            //FileBody fundFileBin = new FileBody(postFile);
            //设置传输参数
            MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
            multipartEntity.addBinaryBody("file", postFile);//相当于<input type="file" name="picFile"/>
            //设计文件以外的参数
            if (postParam != null) {
                Set<String> keySet = postParam.keySet();
                for (String key : keySet) {
                    //相当于<input type="text" name="name" value=name>
                    //System.out.println(key+" : " + postParam.get(key));
                    multipartEntity.addPart(key, new StringBody(postParam.get(key), ContentType.create("text/plain", Consts.UTF_8)));
                }
            }
            HttpEntity reqEntity = multipartEntity.build();
            if (token != null) {
                httpPost.setHeader("Authorization", token);
            }
            httpPost.setEntity(reqEntity);

            //System.out.println("发起请求的页面地址 " + httpPost.getRequestLine());
            //发起请求   并返回请求的响应
            CloseableHttpResponse response = null;
            try {
                response = SSLClient.buildSSLCloseableHttpClient().execute(httpPost);
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
                return resultMap;
            }*/
            try {
                //System.out.println("----------------------------------------");
                //打印响应状态
                //System.out.println(response.getStatusLine());
                resultMap.put("statusCode", response.getStatusLine().getStatusCode());
                //获取响应对象
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    //打印响应长度
                    //System.out.println("Response content length: " + resEntity.getContentLength());
                    //打印响应内容
                    resultMap.put("data", EntityUtils.toString(resEntity, Charset.forName("UTF-8")));
                }
                //销毁
                EntityUtils.consume(resEntity);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("uploadFileByHTTP result:"+resultMap);
        return resultMap;
    }

    public static JSONObject doPost(String url,String token,String params){
        HttpClient httpclient = null;
        try {
            httpclient = new SSLClient();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        // 执行请求操作，并拿到结果（同步阻塞）
        HttpResponse response = null;
        try {
            httpPost.setHeader("Content-Type","application/json;charset=UTF-8");
            if(token!=null){
                httpPost.setHeader("Authorization", token);
            }
            // 封装请求参数
            if (params != null){
                HttpEntity httpEntity = new StringEntity(params, ContentType.APPLICATION_JSON);
                httpPost.setEntity(httpEntity);
            }
            response = httpclient.execute(httpPost, new BasicHttpContext());
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                JSONObject jsonObj = JSONObject.parseObject(jsonString);
                if ("0".equals(jsonObj.get("code").toString())){
                    return jsonObj.getJSONObject("data");
                }else {
                    //logger.error("获取羚眸视频图像智能应用平台接口失败");
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}