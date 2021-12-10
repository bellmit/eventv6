package cn.ffcs.zhsq.mybatis.domain.map.buildingBind;

import java.io.Serializable;
import java.util.Date;

/**
 * 汽车轨迹信息
 *
 * @Author sulch
 * @Date 2017-10-19 17:22
 */
public class CarTrajectoryInfo implements Serializable {
    private Long carTrajectoryId;//主键
    private Long carId;//车辆id
    private String x;//定位坐标X
    private String y;//定位坐标Y
    private String z;//定位坐标Z
    private String mapType;//地图类型
    private Date localTime;//定位时间
    private Long creator;//创建人
    private Date creatDate;//创建时间

    public Long getCarTrajectoryId() {
        return carTrajectoryId;
    }

    public void setCarTrajectoryId(Long carTrajectoryId) {
        this.carTrajectoryId = carTrajectoryId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
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

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public Date getLocalTime() {
        return localTime;
    }

    public void setLocalTime(Date localTime) {
        this.localTime = localTime;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Date getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(Date creatDate) {
        this.creatDate = creatDate;
    }
}
