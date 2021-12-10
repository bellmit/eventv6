package cn.ffcs.zhsq.mybatis.domain.userOrganTree;

public class UserOrganTree implements java.io.Serializable{

	private static final long serialVersionUID = 5160489570196703601L;
	
	private Long id;
	private String name;
	private Long pid;
	private String orgCode;
	private String orgName;
	private String chiefLevel;
	/**
	 * u表示user,o表示organ
	 */
	private String userOrOrgan;
	
	//数据库无该字段  用来显示用user的职位名称
	private String positionName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public String getUserOrOrgan() {
		return userOrOrgan;
	}
	public void setUserOrOrgan(String userOrOrgan) {
		this.userOrOrgan = userOrOrgan;
	}
	/**
	 * @return the orgCode
	 */
	public String getOrgCode() {
		return orgCode;
	}
	/**
	 * @param orgCode the orgCode to set
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	public String getChiefLevel() {
		return chiefLevel;
	}
	public void setChiefLevel(String chiefLevel) {
		this.chiefLevel = chiefLevel;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
}
