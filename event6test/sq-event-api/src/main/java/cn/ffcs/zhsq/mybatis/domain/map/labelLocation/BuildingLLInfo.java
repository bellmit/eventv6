package cn.ffcs.zhsq.mybatis.domain.map.labelLocation;

import java.io.Serializable;

/**
 * 楼宇地图标注地理位置信息
 *
 * @Author sulch
 * @Date 2016-11-10 14:20
 */
public class BuildingLLInfo implements Serializable {
    private static final long serialVersionUID = -7734223296801258647L;

    private Long buildingId;        //楼宇id
    private String buildingName;    //楼宇名称
    private String buildingAddress; //楼宇地址
    private String x;               //定位坐标x
    private String y;               //定位坐标y
    private Integer mapt;            //定位地图类型

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public Integer getMapt() {
        return mapt;
    }

    public void setMapt(Integer mapt) {
        this.mapt = mapt;
    }

}
