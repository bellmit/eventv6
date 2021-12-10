package cn.ffcs.zhsq.mybatis.domain.devicecollectdata;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 设备采集GPS数据模块bo对象
 * @Author: huangjianming
 * @Date: 2021/3/9 15:39
 * @Copyright: 2021 福富软件
 */
public class DeviceGpsData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String gpsId;//主键ID
    private String bizType;//业务类型，01执法记录仪
    private Long bizId;//业务主键
    private String bizUUID;//业务主键UUID
    private String bizCode;//业务编码
    private String bizName;//业务名称
    private String lon;//经度
    private String lat;//纬度
    private String mapt;//地图类型
    private Date createTime;//定位时间
    private Date createDate;//创建日期
    private String isValid;//有效状态，1 有效；0 无效

    public String getGpsId() {
        return gpsId;
    }

    public void setGpsId(String gpsId) {
        this.gpsId = gpsId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public Long getBizId() {
        return bizId;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }

    public String getBizUUID() {
        return bizUUID;
    }

    public void setBizUUID(String bizUUID) {
        this.bizUUID = bizUUID;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getMapt() {
        return mapt;
    }

    public void setMapt(String mapt) {
        this.mapt = mapt;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
}
