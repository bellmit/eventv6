package cn.ffcs.zhsq.mybatis.domain.event.docking;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ReportedIncident",namespace="http://www.dscomm.com.cn/ns/soc/dailyIncidentService")
@XmlAccessorType(XmlAccessType.NONE)
public class ReportedIncident implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5816396458685294669L;

	@XmlAttribute(name = "incidentName")
	private String incidentName;

	@XmlAttribute(name = "incidentCode")
	private String incidentCode;

	@XmlAttribute(name = "incidentType")
	private String incidentType;

	@XmlAttribute(name = "regionCode")
	private String regionCode;

	@XmlAttribute(name = "reportOrgCode")
	private String reportOrgCode;

	@XmlAttribute(name = "incidentSource")
	private int incidentSource;

	@XmlAttribute(name = "incidentSourceCode")
	private String incidentSourceCode;

	@XmlAttribute(name = "description")
	private String description;

	@XmlAttribute(name = "occurPlace")
	private String occurPlace;

	@XmlAttribute(name = "reportPerson")
	private String reportPerson;

	@XmlAttribute(name = "reportMobile")
	private String reportMobile;

	@XmlAttribute(name = "occurDate")
	private String occurDate;

	public String getIncidentCode() {
		return incidentCode;
	}

	public void setIncidentCode(String incidentCode) {
		this.incidentCode = incidentCode;
	}

	public String getIncidentType() {
		return incidentType;
	}

	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getReportOrgCode() {
		return reportOrgCode;
	}

	public void setReportOrgCode(String reportOrgCode) {
		this.reportOrgCode = reportOrgCode;
	}

	public String getIncidentSourceCode() {
		return incidentSourceCode;
	}

	public void setIncidentSourceCode(String incidentSourceCode) {
		this.incidentSourceCode = incidentSourceCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOccurPlace() {
		return occurPlace;
	}

	public void setOccurPlace(String occurPlace) {
		this.occurPlace = occurPlace;
	}

	public String getReportPerson() {
		return reportPerson;
	}

	public void setReportPerson(String reportPerson) {
		this.reportPerson = reportPerson;
	}

	public String getReportMobile() {
		return reportMobile;
	}

	public void setReportMobile(String reportMobile) {
		this.reportMobile = reportMobile;
	}

	public String getOccurDate() {
		return occurDate;
	}

	public void setOccurDate(String occurDate) {
		this.occurDate = occurDate;
	}

	public String getIncidentName() {
		return incidentName;
	}

	public void setIncidentName(String incidentName) {
		this.incidentName = incidentName;
	}

	public int getIncidentSource() {
		return incidentSource;
	}

	public void setIncidentSource(int incidentSource) {
		this.incidentSource = incidentSource;
	}

}
