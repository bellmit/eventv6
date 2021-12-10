package cn.ffcs.zhsq.mybatis.domain.executeControl;

import java.util.List;

public class ControlPersonnelTwo {

    private String libId;
    private SelfAttr selfAttr;
    private List<ObjectInfos> objectInfos;

    public String getLibId() {
        return libId;
    }

    public void setLibId(String libId) {
        this.libId = libId;
    }

    public SelfAttr getSelfAttr() {
        return selfAttr;
    }

    public void setSelfAttr(SelfAttr selfAttr) {
        this.selfAttr = selfAttr;
    }

    public List<ObjectInfos> getObjectInfos() {
        return objectInfos;
    }

    public void setObjectInfos(List<ObjectInfos> objectInfos) {
        this.objectInfos = objectInfos;
    }
}
