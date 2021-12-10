package cn.ffcs.zhsq.mybatis.domain.map.arcgis;

import java.io.Serializable;

public class ArcgisInfoOfPeople implements Serializable {
    private Long id;//地图信息id
    private Long ciRsId;//
    private Long partyId;//
    private String wid;//关联信息id
    private String name;//姓名
    private String address;//地址
    private Double x;//中心点x
    private Double y;//中心点y
    private Integer mapt;//地图类型

    private boolean editAble=false;
    private String elementsCollectionStr;//地图专题图层相关配置集合
    private String subBizType;//业务子类型

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCiRsId() {
        return ciRsId;
    }

    public void setCiRsId(Long ciRsId) {
        this.ciRsId = ciRsId;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Integer getMapt() {
        return mapt;
    }

    public void setMapt(Integer mapt) {
        this.mapt = mapt;
    }

    public boolean isEditAble() {
        return editAble;
    }

    public void setEditAble(boolean editAble) {
        this.editAble = editAble;
    }

    public String getElementsCollectionStr() {
        return elementsCollectionStr;
    }

    public void setElementsCollectionStr(String elementsCollectionStr) {
        this.elementsCollectionStr = elementsCollectionStr;
    }

    public String getSubBizType() {
        return subBizType;
    }

    public void setSubBizType(String subBizType) {
        this.subBizType = subBizType;
    }
}
