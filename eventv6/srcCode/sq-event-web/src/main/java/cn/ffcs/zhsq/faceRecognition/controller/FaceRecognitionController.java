package cn.ffcs.zhsq.faceRecognition.controller;

import cn.ffcs.zhsq.executeControl.service.IControlLibraryService;
import cn.ffcs.zhsq.faceRecognition.service.IFaceRecognitionService;
import cn.ffcs.zhsq.mybatis.domain.faceRecognition.FaceRecognition;
import cn.ffcs.zhsq.mybatis.domain.faceRecognition.PageBean;
import cn.ffcs.zhsq.utils.domain.App;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 布控任务管理模块控制器
 * @Author: dtj
 * @Date: 07-22 17:50:39
 * @Copyright: 2020 福富软件
 */
@Controller("faceRecognitionController")
@RequestMapping("/zhsq/event/faceRecognition")
public class FaceRecognitionController {

    @Autowired
    private IFaceRecognitionService recognitionService; //注入人脸图库模块服务

    @Autowired
    private IControlLibraryService controlLibraryService; //注入布控库管理模块服务

    /**
     * 列表页面
     */
    @RequestMapping("/index")
    public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
        map.put("sysDomain", App.SYSTEM.getDomain(session));
        map.put("uiDomain", App.UI.getDomain(session));
        return "/zzgl/faceRecognition/face-lib.html";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequestMapping("/listData")
    public Object listData(HttpServletRequest request, HttpSession session, ModelMap map, String placeTags,
                           String headTags,String upperBodyTags,String cids,String score,String startTime,
                           String endTime,String limit,String feature,String sexTags,String ageSectionTags,
                           String minId,String maxId) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("placeTags",placeTags);
        hashMap.put("headTags",headTags);
        hashMap.put("upperBodyTags",upperBodyTags);
        hashMap.put("cids",cids);
        hashMap.put("score",score);
        hashMap.put("startTime",startTime);
        hashMap.put("endTime",endTime);
        hashMap.put("limit",limit);
        hashMap.put("feature",feature);
        hashMap.put("sexTags",sexTags);
        hashMap.put("ageSectionTags",ageSectionTags);
        hashMap.put("minId",minId);
        hashMap.put("maxId",maxId);
        PageBean<FaceRecognition> list = null;
        try {
            String token = controlLibraryService.getToken();
            list = recognitionService.searchList(hashMap,token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 详情页面
     */
    @RequestMapping("/view")
    public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
                       String id) {

        FaceRecognition bo = null;
        try {
            String token = controlLibraryService.getToken();
            bo = recognitionService.findById(id,token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("bo",bo);
        return "/zzgl/faceRecognition/face-detail.html";
    }

    /**
     * 轨迹页面
     */
    @RequestMapping("/trajectory")
    public Object getTrajectory(HttpServletRequest request, HttpSession session, ModelMap map,String[] ids) {

        ArrayList<FaceRecognition> list = new ArrayList<>();
        if (ids != null && ids.length > 0){
            try {
                String token = controlLibraryService.getToken();
                list = recognitionService.findByIdS(ids,token);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        map.put("list",list);
        return "/zzgl/faceRecognition/face-lib-trajectory.html";
    }

    @ResponseBody
    @RequestMapping("upload")
    public Map upload(MultipartFile file, HttpServletRequest request) {
        //保存上传
        OutputStream out = null;
        InputStream fileInput = null;
        File files = null;
        try {
            if (file != null) {
                files = new File(System.getProperty("java.io.tmpdir") + File.separator + file.getName());
                //打印查看上传路径
                String token = controlLibraryService.getToken();
                if (!files.getParentFile().exists()) {
                    files.getParentFile().mkdirs();
                }
                file.transferTo(files);
                String imageUrl = recognitionService.upload(file, files, token);
                Map<String, Object> featureMap = recognitionService.getFeature(imageUrl,token);
                Map<String, Object> map = new HashMap<>();
                map.put("code", 0);
                map.put("msg", "");
                map.put("data", featureMap);
                featureMap.put("src", imageUrl);
                return map;
            }
        } catch (Exception e) {
        } finally {
            if (file != null) {
                files.delete();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (fileInput != null) {
                    fileInput.close();
                }
            } catch (IOException e) {
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        map.put("msg", "");
        return map;
    }

    /**
     * 根据图片URL获取 feature
     */
    @RequestMapping("/getFeature")
    @ResponseBody
    public Object getFeature(HttpServletRequest request, HttpSession session, ModelMap map,
                       String faceUrl) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String result = "fail";
        Map<String, Object> feature = null;
        try {
            String token = controlLibraryService.getToken();
            feature = recognitionService.getFeature(faceUrl, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (feature != null) {
            result = "success";
            resultMap.put("feature",feature.get("feature"));
        }
        resultMap.put("result", result);
        return resultMap;
    }

    /**
     * 根据图片ID获取 feature
     */
    @RequestMapping("/getFeatureById")
    @ResponseBody
    public Object getFeatureById(HttpServletRequest request, HttpSession session, ModelMap map,
                             String id) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String result = "fail";
        JSONObject jsonObject = null;
        try {
            String token = controlLibraryService.getToken();
            jsonObject = recognitionService.getFeatureById(id, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            result = "success";
            resultMap.put("jsonObject",jsonObject);
        }
        resultMap.put("result", result);
        return resultMap;
    }
}