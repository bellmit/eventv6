package cn.ffcs.zhsq.mybatis.domain.eliminatelettertho;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 三书一函行业领域表模块bo对象
 * @Author: liangbzh
 * @Date: 08-09 16:39:13
 * @table: 表信息描述 T_ELIMINATE_LETTER_INDUS 三书一函行业领域表  序列SEQ_ELIMINATE_LETTER_INDUS
 * @Copyright: 2021 福富软件
 */
public class EliminateLetterIndus implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long indusId; //主键 	NUMBER(9)
    private String indusUuid; //逻辑主键 	VARCHAR2(32)
    private Long thoId; //主表主键 	NUMBER(9)
    private String industrialCode; //行业领域CODE 	CHAR(2)
    private String orgCode; //组织编码 	VARCHAR2(24)
    private Long creator; //创建人 	NUMBER(9)
    private Date createTime; //创建时间 	TIMESTAMP
    private Long updator; //修改人 	NUMBER(9)
    private Date updateTime; //修改时间 	TIMESTAMP
    private String isValid; //有效状态 	CHAR(1)
    private String remark; //备注 	VARCHAR2(200)

    public Long getIndusId() {  //主键
        return indusId;
    }

    public void setIndusId(Long indusId) { //主键
        this.indusId = indusId;
    }

    public String getIndusUuid() {  //逻辑主键
        return indusUuid;
    }

    public void setIndusUuid(String indusUuid) { //逻辑主键
        this.indusUuid = indusUuid;
    }

    public Long getThoId() {  //主表主键
        return thoId;
    }

    public void setThoId(Long thoId) { //主表主键
        this.thoId = thoId;
    }

    public String getIndustrialCode() {  //行业领域CODE
        return industrialCode;
    }

    public void setIndustrialCode(String industrialCode) { //行业领域CODE
        this.industrialCode = industrialCode;
    }

    public String getOrgCode() {  //组织编码
        return orgCode;
    }

    public void setOrgCode(String orgCode) { //组织编码
        this.orgCode = orgCode;
    }

    public Long getCreator() {  //创建人
        return creator;
    }

    public void setCreator(Long creator) { //创建人
        this.creator = creator;
    }

    public Date getCreateTime() {  //创建时间
        return createTime;
    }

    public void setCreateTime(Date createTime) { //创建时间
        this.createTime = createTime;
    }

    public Long getUpdator() {  //修改人
        return updator;
    }

    public void setUpdator(Long updator) { //修改人
        this.updator = updator;
    }

    public Date getUpdateTime() {  //修改时间
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) { //修改时间
        this.updateTime = updateTime;
    }

    public String getIsValid() {  //有效状态
        return isValid;
    }

    public void setIsValid(String isValid) { //有效状态
        this.isValid = isValid;
    }

    public String getRemark() {  //备注
        return remark;
    }

    public void setRemark(String remark) { //备注
        this.remark = remark;
    }

}