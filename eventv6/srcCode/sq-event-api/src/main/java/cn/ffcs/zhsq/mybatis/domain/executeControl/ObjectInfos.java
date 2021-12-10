package cn.ffcs.zhsq.mybatis.domain.executeControl;

import java.io.Serializable;

public class ObjectInfos implements Serializable {

    private String libType;//布控库类型
    private String imageUrl;//图片URL

    public String getLibType() {
        return libType;
    }

    public void setLibType(String libType) {
        this.libType = libType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
