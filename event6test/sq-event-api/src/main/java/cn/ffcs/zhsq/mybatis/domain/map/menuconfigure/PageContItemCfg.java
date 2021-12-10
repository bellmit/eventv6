package cn.ffcs.zhsq.mybatis.domain.map.menuconfigure;

import java.io.Serializable;

public class PageContItemCfg implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8631482830484265461L;

	private Integer contItemId;

    private Integer pContItemId;

    private String name;

    private Integer displayOrder;

    private Integer layCfgId;

    private Integer pgIdxId;

    private String status;
    
    private String orgCode;
    
    private String pgIdxType;

    public Integer getContItemId() {
        return contItemId;
    }

    public void setContItemId(Integer contItemId) {
        this.contItemId = contItemId;
    }

    public Integer getpContItemId() {
        return pContItemId;
    }

    public void setpContItemId(Integer pContItemId) {
        this.pContItemId = pContItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getLayCfgId() {
        return layCfgId;
    }

    public void setLayCfgId(Integer layCfgId) {
        this.layCfgId = layCfgId;
    }

    public Integer getPgIdxId() {
        return pgIdxId;
    }

    public void setPgIdxId(Integer pgIdxId) {
        this.pgIdxId = pgIdxId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getPgIdxType() {
		return pgIdxType;
	}

	public void setPgIdxType(String pgIdxType) {
		this.pgIdxType = pgIdxType;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
}