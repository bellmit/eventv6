package cn.ffcs.zhsq.faceRecognition.service;

import cn.ffcs.zhsq.mybatis.domain.faceRecognition.FaceRecognition;
import cn.ffcs.zhsq.mybatis.domain.faceRecognition.PageBean;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface IFaceRecognitionService {
    String upload(MultipartFile file, File files, String token);

    Map<String, Object> getFeature(String imageUrl, String token);

    JSONObject getFeatureById(String id, String token);

    PageBean<FaceRecognition> searchList(HashMap<String, Object> hashMap, String token);

    FaceRecognition findById(String id, String token);

    ArrayList<FaceRecognition> findByIdS(String[] ids, String token);
}
