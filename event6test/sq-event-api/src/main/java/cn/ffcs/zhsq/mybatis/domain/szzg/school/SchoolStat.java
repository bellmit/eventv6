package cn.ffcs.zhsq.mybatis.domain.szzg.school;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 学校人数统计表模块bo对象
 * @Author: os.wuzhj
 * @Date: 04-04 08:43:01
 * @table: 表信息描述 ZG_SCHOOL_STAT 学校人数统计表  学校人数统计表  序列SEQ_ZG_SCHOOL_STAT
 * @Copyright: 2019 福富软件
 */
public class SchoolStat implements Serializable {

  private static final long serialVersionUID = 1L;

  	private Long statId; //主键  序列 SEQ_ZG_SCHOOL_STAT
	private Long schId; //关联zg_school_seq_id 	NVARCHAR2(40)
	private String schoolName; //学校名称
	private Long teachers; //老师人数 	NUMBER(22)
	private Long students; //学生人数 	NUMBER(22)
	private Long males; //男生人数 	NUMBER(22)
	private Long females; //女生人数 	NUMBER(22)
	private Integer statYear; //统计年份 	NUMBER(4)
	private String status; //状态：1-有效，0-删除 	CHAR(1)
	private Date createTime; //创建时间 	DATE
	private Long createUser; //创建人 	NUMBER(9)
	private Date updateTime; //更新时间 	DATE
	private Long updateUser; //更新人 	NUMBER(9)

	
	public Long getStatId() {
		return statId;
	}
	public void setStatId(Long statId) {
		this.statId = statId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public Long getSchId() {  //关联zg_school_seq_id
		return schId;
	}
	public void setSchId(Long schId) { //关联zg_school_seq_id
		this.schId = schId;
	}
	public Long getTeachers() {  //老师人数
		return teachers;
	}
	public void setTeachers(Long teachers) { //老师人数
		this.teachers = teachers;
	}
	public Long getStudents() {  //学生人数
		return students;
	}
	public void setStudents(Long students) { //学生人数
		this.students = students;
	}
	public Long getMales() {  //男生人数
		return males;
	}
	public void setMales(Long males) { //男生人数
		this.males = males;
	}
	public Long getFemales() {  //女生人数
		return females;
	}
	public void setFemales(Long females) { //女生人数
		this.females = females;
	}
	public Integer getStatYear() {  //统计年份
		return statYear;
	}
	public void setStatYear(Integer statYear) { //统计年份
		this.statYear = statYear;
	}
	public String getStatus() {  //状态：1-有效，0-删除
		return status;
	}
	public void setStatus(String status) { //状态：1-有效，0-删除
		this.status = status;
	}
	public Date getCreateTime() {  //创建时间
		return createTime;
	}
	public void setCreateTime(Date createTime) { //创建时间
		this.createTime = createTime;
	}
	public Long getCreateUser() {  //创建人
		return createUser;
	}
	public void setCreateUser(Long createUser) { //创建人
		this.createUser = createUser;
	}
	public Date getUpdateTime() {  //更新时间
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) { //更新时间
		this.updateTime = updateTime;
	}
	public Long getUpdateUser() {  //更新人
		return updateUser;
	}
	public void setUpdateUser(Long updateUser) { //更新人
		this.updateUser = updateUser;
	}


}