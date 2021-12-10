package cn.ffcs.zhsq.mybatis.domain.szzg.school;

import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ljq on 2017/7/29.
 */
public class SchoolBo  implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6863516635861854674L;
    private Long seqid;
    private String orgCode;
    private String orgName;
    private String type;
    private String typeName;
    private String schoolName;
    private Long teachers;
    private Long students;
    private Long males;
    private Long females;
    private String address;
    private String url;
    private Date createTime;
    private Date updateTime;
    private String x;
    private String y;
    private ResMarker resMarker;
    private String area;
    private String status;
    private String orgPath;

    private Integer year;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getOrgPath() {
        return orgPath;
    }

    public void setOrgPath(String orgPath) {
        this.orgPath = orgPath;
    }

    private Long orgLevel;
    private Long schoolNumber;

    public Long getSchoolNumber() {
        return schoolNumber;
    }

    public void setSchoolNumber(Long schoolNumber) {
        this.schoolNumber = schoolNumber;
    }

    public Long getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(Long orgLevel) {
        this.orgLevel = orgLevel;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getSeqid() {
        return seqid;
    }

    public void setSeqid(Long seqid) {
        this.seqid = seqid;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getTeachers() {
        return teachers;
    }

    public void setTeachers(Long teachers) {
        this.teachers = teachers;
    }

    public Long getStudents() {
        return students;
    }

    public void setStudents(Long students) {
        this.students = students;
    }

    public Long getMales() {
        return males;
    }

    public void setMales(Long males) {
        this.males = males;
    }

    public Long getFemales() {
        return females;
    }

    public void setFemales(Long females) {
        this.females = females;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public ResMarker getResMarker() {
        return resMarker;
    }

    public void setResMarker(ResMarker resMarker) {
        this.resMarker = resMarker;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
