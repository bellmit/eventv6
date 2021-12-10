package cn.ffcs.zhsq.mybatis.domain.faceRecognition;

import java.io.Serializable;

public class FaceRecognition implements Serializable {

    private String id;//抓拍人表主键
    private String imgId;//原始图片ID
    private String cid;//摄影机cid
    private String deviceId;//摄影机id
    private String captureTime;//抓拍时间时间戳
    private String longitude;//经度
    private String latitude;//维度
    private String cameraTags;//摄像机标签
    private String sceneUrl;//原始地图URL
    private String aid;//虚拟人员id
    private String alikeAids;//疑似虚拟人员ID集合
    private String personTags;//人员标签
    private String deviceName;//设备名称
    private String address;//抓拍的地址
    private String personInfoUrl;//人员档案的URL
    private String faceRect;//人脸坐标位置
    private String faceUrl;//人脸小图URL
    private String faceConfidence;//人脸置信度
    private boolean hasBody;//是否有人体
    private Double score;//比分
    private String facePose;//人脸角度
    private String imgType;
    private String imgSubType;
    private String aidTags;
    private String faceQuality;
    private Boolean faceLowQuality;
    private String timeTags;
    private Boolean hasArchives;
    private String realName;
    private String faceFeatureVersion;
    private String cardId;


    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getImgSubType() {
        return imgSubType;
    }

    public void setImgSubType(String imgSubType) {
        this.imgSubType = imgSubType;
    }

    public String getAidTags() {
        return aidTags;
    }

    public void setAidTags(String aidTags) {
        this.aidTags = aidTags;
    }

    public String getFaceQuality() {
        return faceQuality;
    }

    public void setFaceQuality(String faceQuality) {
        this.faceQuality = faceQuality;
    }

    public Boolean getFaceLowQuality() {
        return faceLowQuality;
    }

    public void setFaceLowQuality(Boolean faceLowQuality) {
        this.faceLowQuality = faceLowQuality;
    }

    public String getTimeTags() {
        return timeTags;
    }

    public void setTimeTags(String timeTags) {
        this.timeTags = timeTags;
    }

    public Boolean getHasArchives() {
        return hasArchives;
    }

    public void setHasArchives(Boolean hasArchives) {
        this.hasArchives = hasArchives;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getFaceFeatureVersion() {
        return faceFeatureVersion;
    }

    public void setFaceFeatureVersion(String faceFeatureVersion) {
        this.faceFeatureVersion = faceFeatureVersion;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getFacePose() {
        return facePose;
    }

    public void setFacePose(String facePose) {
        this.facePose = facePose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(String captureTime) {
        this.captureTime = captureTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCameraTags() {
        return cameraTags;
    }

    public void setCameraTags(String cameraTags) {
        this.cameraTags = cameraTags;
    }

    public String getSceneUrl() {
        return sceneUrl;
    }

    public void setSceneUrl(String sceneUrl) {
        this.sceneUrl = sceneUrl;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getAlikeAids() {
        return alikeAids;
    }

    public void setAlikeAids(String alikeAids) {
        this.alikeAids = alikeAids;
    }

    public String getPersonTags() {
        return personTags;
    }

    public void setPersonTags(String personTags) {
        this.personTags = personTags;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPersonInfoUrl() {
        return personInfoUrl;
    }

    public void setPersonInfoUrl(String personInfoUrl) {
        this.personInfoUrl = personInfoUrl;
    }

    public String getFaceRect() {
        return faceRect;
    }

    public void setFaceRect(String faceRect) {
        this.faceRect = faceRect;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getFaceConfidence() {
        return faceConfidence;
    }

    public void setFaceConfidence(String faceConfidence) {
        this.faceConfidence = faceConfidence;
    }

    public boolean isHasBody() {
        return hasBody;
    }

    public void setHasBody(boolean hasBody) {
        this.hasBody = hasBody;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

}
