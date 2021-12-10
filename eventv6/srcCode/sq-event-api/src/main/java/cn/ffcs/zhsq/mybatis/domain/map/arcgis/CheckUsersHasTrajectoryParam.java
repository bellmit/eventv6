package cn.ffcs.zhsq.mybatis.domain.map.arcgis;

import java.io.Serializable;

public class CheckUsersHasTrajectoryParam implements Serializable {
    public Long id;
    public String locateTimeBegin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocateTimeBegin() {
        return locateTimeBegin;
    }

    public void setLocateTimeBegin(String locateTimeBegin) {
        this.locateTimeBegin = locateTimeBegin;
    }

    public String getLocateTimeEnd() {
        return locateTimeEnd;
    }

    public void setLocateTimeEnd(String locateTimeEnd) {
        this.locateTimeEnd = locateTimeEnd;
    }

    public String locateTimeEnd;


}
