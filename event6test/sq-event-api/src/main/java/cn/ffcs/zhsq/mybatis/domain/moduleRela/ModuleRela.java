package cn.ffcs.zhsq.mybatis.domain.moduleRela;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:两张业务表的关联中间表模块bo对象
 * @Author: ztc
 * @Date: 2018/5/1816:08
 */
public class ModuleRela implements Serializable{
    private Long relaId; //关联编号，序列为：SEQ_RELA_ID
    private String moduleCodeLeft; //左方模块编码，001 扫黑除恶_线索管理；
    private Long moduleIdLeft; //左方模块编号
    private String moduleCodeRight; //右方模块编码，001 扫黑除恶_黑恶团伙管理
    private Long moduleIdRight; //右方模块编号
    private String status; //状态，1 有效；0 无效
    private Date createDate; //登记时间
    private Long creatorId; //登记人员
    private Date updateDate; //更新时间
    private Long updaterId; //更新人员

    public ModuleRela() {
    }

    public ModuleRela(Long relaId, String moduleCodeLeft, Long moduleIdLeft, String moduleCodeRight, Long moduleIdRight, String status, Date createDate, Long creatorId, Date updateDate, Long updaterId) {
        this.relaId = relaId;
        this.moduleCodeLeft = moduleCodeLeft;
        this.moduleIdLeft = moduleIdLeft;
        this.moduleCodeRight = moduleCodeRight;
        this.moduleIdRight = moduleIdRight;
        this.status = status;
        this.createDate = createDate;
        this.creatorId = creatorId;
        this.updateDate = updateDate;
        this.updaterId = updaterId;
    }

    public Long getRelaId() {
        return relaId;
    }

    public void setRelaId(Long relaId) {
        this.relaId = relaId;
    }

    public String getModuleCodeLeft() {
        return moduleCodeLeft;
    }

    public void setModuleCodeLeft(String moduleCodeLeft) {
        this.moduleCodeLeft = moduleCodeLeft;
    }

    public Long getModuleIdLeft() {
        return moduleIdLeft;
    }

    public void setModuleIdLeft(Long moduleIdLeft) {
        this.moduleIdLeft = moduleIdLeft;
    }

    public String getModuleCodeRight() {
        return moduleCodeRight;
    }

    public void setModuleCodeRight(String moduleCodeRight) {
        this.moduleCodeRight = moduleCodeRight;
    }

    public Long getModuleIdRight() {
        return moduleIdRight;
    }

    public void setModuleIdRight(Long moduleIdRight) {
        this.moduleIdRight = moduleIdRight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }
}
