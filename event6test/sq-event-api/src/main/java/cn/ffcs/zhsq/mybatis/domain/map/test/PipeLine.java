package cn.ffcs.zhsq.mybatis.domain.map.test;

import java.io.Serializable;

public class PipeLine implements Serializable {
    private String areaId;//areaId
    private String startPoint;//开始节点
    private String endPoint;//结束节点
    private String pipeLineKind;//管种
    private String startLongitude;//起点经度
    private String startLatitude;//起点纬度
    private String endLongitude;//终点经度
    private String endLatitude;//终点维度

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getPipeLineKind() {
        return pipeLineKind;
    }

    public void setPipeLineKind(String pipeLineKind) {
        this.pipeLineKind = pipeLineKind;
    }

    public String getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(String startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(String startLatitude) {
        this.startLatitude = startLatitude;
    }

    public String getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(String endLongitude) {
        this.endLongitude = endLongitude;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }
}


