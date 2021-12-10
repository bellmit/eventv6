package cn.ffcs.zhsq.mybatis.domain.map.labelLocation;

import java.io.Serializable;

/**
 * 部件地图标注地理位置信息
 *
 * @Author sulch
 * @Date 2016-11-10 14:27
 */
public class ComponentsLLInfo implements Serializable {
    private static final long serialVersionUID = -7734223296801258647L;

    private Long componentsId;              //部件id
    private String componentsName;          //部件名称
    private String componentsAddress;       //部件地址
    private String x;                       //定位坐标x
    private String y;                       //定位坐标y
    private String mapt;                    //定位地图类型

    public Long getComponentsId() {
        return componentsId;
    }

    public void setComponentsId(Long componentsId) {
        this.componentsId = componentsId;
    }

    public String getComponentsName() {
        return componentsName;
    }

    public void setComponentsName(String componentsName) {
        this.componentsName = componentsName;
    }

    public String getComponentsAddress() {
        return componentsAddress;
    }

    public void setComponentsAddress(String componentsAddress) {
        this.componentsAddress = componentsAddress;
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

    public String getMapt() {
        return mapt;
    }

    public void setMapt(String mapt) {
        this.mapt = mapt;
    }

}
