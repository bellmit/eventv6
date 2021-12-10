package cn.ffcs.zhsq.mybatis.domain.event.docking;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
public class SJLCData implements Serializable{
	//

	/**
	 * 
	 */
	private static final long serialVersionUID = 2517860542375573421L;

	@XmlElement(name = "SJLC_ID")
	private String SJLC_ID;

	@XmlElement(name = "JSSJ")
	private String JSSJ;

	@XmlElement(name = "BLSJ")
	private String BLSJ;

	@XmlElement(name = "BLRY")
	private String BLRY;

	@XmlElement(name = "BLYJ")
	private String BLYJ;

	@XmlElement(name = "BLBM")
	private String BLBM;

	@XmlElement(name = "SJID")
	private String SJID;

	@XmlElement(name = "WGMC")
	private String WGMC;

	@XmlElement(name = "LZBJ")
	private String LZBJ;

	@XmlElement(name = "BLLB")
	private String BLLB;

	public String getSJLC_ID() {
		return SJLC_ID;
	}

	public void setSJLC_ID(String sJLC_ID) {
		SJLC_ID = sJLC_ID;
	}

	public String getJSSJ() {
		return JSSJ;
	}

	public void setJSSJ(String jSSJ) {
		JSSJ = jSSJ;
	}

	public String getBLSJ() {
		return BLSJ;
	}

	public void setBLSJ(String bLSJ) {
		BLSJ = bLSJ;
	}

	public String getBLRY() {
		return BLRY;
	}

	public void setBLRY(String bLRY) {
		BLRY = bLRY;
	}

	public String getBLYJ() {
		return BLYJ;
	}

	public void setBLYJ(String bLYJ) {
		BLYJ = bLYJ;
	}

	public String getBLBM() {
		return BLBM;
	}

	public void setBLBM(String bLBM) {
		BLBM = bLBM;
	}

	public String getSJID() {
		return SJID;
	}

	public void setSJID(String sJID) {
		SJID = sJID;
	}

	public String getWGMC() {
		return WGMC;
	}

	public void setWGMC(String wGMC) {
		WGMC = wGMC;
	}

	public String getLZBJ() {
		return LZBJ;
	}

	public void setLZBJ(String lZBJ) {
		LZBJ = lZBJ;
	}

	public String getBLLB() {
		return BLLB;
	}

	public void setBLLB(String bLLB) {
		BLLB = bLLB;
	}
	
}
