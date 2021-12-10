package cn.ffcs.zhsq.mybatis.domain.szzg.EmeryencyPlan;


public class EmeryencyPlanTree implements java.io.Serializable {
	private static final long serialVersionUID = 7934940814787899996L;

	private Long id;

    private String name;

    private Long parentId;

    private String parentName;

    private Long priority;

    private String status;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "EmeryencyPlanTree{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", parentName='" + parentName + '\'' +
                ", priority=" + priority +
                ", status='" + status + '\'' +
                '}';
    }
}