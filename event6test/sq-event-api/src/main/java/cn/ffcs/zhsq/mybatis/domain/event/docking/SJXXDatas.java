package cn.ffcs.zhsq.mybatis.domain.event.docking;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="datas")
@XmlAccessorType(XmlAccessType.NONE)
public class SJXXDatas implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "data")
	private List<SJXXData> sjxxDates;
	
	@XmlAttribute(name = "tableName")
	private String tableName;
	
	@XmlAttribute(name = "count")
	private String count;

	public List<SJXXData> getSjxxDates() {
		return sjxxDates;
	}

	public void setSjxxDates(List<SJXXData> sjxxDates) {
		this.sjxxDates = sjxxDates;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
	
	
	
	
	
//	@XmlAttribute(name = "incidentName")

}
