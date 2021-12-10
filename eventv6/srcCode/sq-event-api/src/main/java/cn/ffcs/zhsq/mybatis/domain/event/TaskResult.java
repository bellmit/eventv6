package cn.ffcs.zhsq.mybatis.domain.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="datas")
public class TaskResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8094784823763414939L;
	private List<TaskInfoData> taskInfoData;

	@XmlElement(name="data")
	public List<TaskInfoData> getTaskInfoData() {
		return taskInfoData;
	}

	public void setTaskInfoData(List<TaskInfoData> taskInfoData) {
		this.taskInfoData = taskInfoData;
	}

}
