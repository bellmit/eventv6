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
public class SJLCDatas implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6778649627409529627L;

	@XmlElement(name = "data")
	private List<SJLCData> sjlcDates;
	
	@XmlAttribute(name = "tableName")
	private String tableName;
	
	@XmlAttribute(name = "count")
	private String count;

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

	public List<SJLCData> getSjlcDates() {
		return sjlcDates;
	}

	public void setSjlcDates(List<SJLCData> sjlcDates) {
		this.sjlcDates = sjlcDates;
	}
	
	
	
	
	
//	@XmlAttribute(name = "incidentName")

}
