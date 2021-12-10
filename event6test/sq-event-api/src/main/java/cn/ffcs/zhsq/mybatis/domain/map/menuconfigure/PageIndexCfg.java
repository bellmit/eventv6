package cn.ffcs.zhsq.mybatis.domain.map.menuconfigure;

import java.util.Date;

public class PageIndexCfg {
    private Integer pgIdxCfgId;

    private String pgIdxType;

    private String regionCode;

    private Integer creator;

    private Date created;

    private String creatorName;

    private Integer updater;

    private Date updated;

    private String updaterName;

    private String status;
    
    private String displayStyle;
    
    private String gridName;
    
    private String pgIdxTypeName;
    
    private Long gridId;

    public Integer getPgIdxCfgId() {
        return pgIdxCfgId;
    }

    public void setPgIdxCfgId(Integer pgIdxCfgId) {
        this.pgIdxCfgId = pgIdxCfgId;
    }

    public String getPgIdxType() {
        return pgIdxType;
    }

    public void setPgIdxType(String pgIdxType) {
        this.pgIdxType = pgIdxType == null ? null : pgIdxType.trim();
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode == null ? null : regionCode.trim();
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName == null ? null : creatorName.trim();
    }

    public Integer getUpdater() {
        return updater;
    }

    public void setUpdater(Integer updater) {
        this.updater = updater;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName == null ? null : updaterName.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

	public String getDisplayStyle() {
		return displayStyle;
	}

	public void setDisplayStyle(String displayStyle) {
		this.displayStyle = displayStyle;
	}

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public String getPgIdxTypeName() {
		return pgIdxTypeName;
	}

	public void setPgIdxTypeName(String pgIdxTypeName) {
		this.pgIdxTypeName = pgIdxTypeName;
	}

	public Long getGridId() {
		return gridId;
	}

	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}
    
}