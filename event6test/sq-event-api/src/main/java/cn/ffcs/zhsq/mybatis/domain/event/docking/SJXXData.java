package cn.ffcs.zhsq.mybatis.domain.event.docking;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
public class SJXXData implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2517860542375573421L;

	@XmlElement(name = "SJXX_COPY_ID")
	private String SJXX_COPY_ID;

	@XmlElement(name = "WGBH")
	private String WGBH;

	@XmlElement(name = "WGMC")
	private String WGMC;

	@XmlElement(name = "SJLX")
	private String SJLX;

	@XmlElement(name = "SJWZJD")
	private String SJWZJD;

	@XmlElement(name = "SJWZWD")
	private String SJWZWD;

	@XmlElement(name = "LYXX")
	private String LYXX;

	@XmlElement(name = "SJMS")
	private String SJMS;

	@XmlElement(name = "FSSJ")
	private String FSSJ;

	@XmlElement(name = "FKRY")
	private String FKRY;

	@XmlElement(name = "SJRS")
	private String SJRS;

	@XmlElement(name = "SJXZ")
	private String SJXZ;

	@XmlElement(name = "YXFW")
	private String YXFW;

	@XmlElement(name = "XXLY")
	private String XXLY;

	@XmlElement(name = "JJCD")
	private String JJCD;

	@XmlElement(name = "SXZT")
	private String SXZT;

	@XmlElement(name = "YJZT")
	private String YJZT;

	@XmlElement(name = "SJZT")
	private String SJZT;

	@XmlElement(name = "SJMC")
	private String SJMC;

	@XmlElement(name = "DSFSJBS")
	private String DSFSJBS;

	public String getSJXX_COPY_ID() {
		return SJXX_COPY_ID;
	}

	public void setSJXX_COPY_ID(String sJXX_COPY_ID) {
		SJXX_COPY_ID = sJXX_COPY_ID;
	}

	public String getWGBH() {
		return WGBH;
	}

	public void setWGBH(String wGBH) {
		WGBH = wGBH;
	}

	public String getWGMC() {
		return WGMC;
	}

	public void setWGMC(String wGMC) {
		WGMC = wGMC;
	}

	public String getSJLX() {
		return SJLX;
	}

	public void setSJLX(String sJLX) {
		SJLX = sJLX;
	}

	public String getSJWZJD() {
		return SJWZJD;
	}

	public void setSJWZJD(String sJWZJD) {
		SJWZJD = sJWZJD;
	}

	public String getSJWZWD() {
		return SJWZWD;
	}

	public void setSJWZWD(String sJWZWD) {
		SJWZWD = sJWZWD;
	}

	public String getLYXX() {
		return LYXX;
	}

	public void setLYXX(String lYXX) {
		LYXX = lYXX;
	}

	public String getSJMS() {
		return SJMS;
	}

	public void setSJMS(String sJMS) {
		SJMS = sJMS;
	}

	public String getFSSJ() {
		return FSSJ;
	}

	public void setFSSJ(String fSSJ) {
		FSSJ = fSSJ;
	}

	public String getFKRY() {
		return FKRY;
	}

	public void setFKRY(String fKRY) {
		FKRY = fKRY;
	}

	public String getSJRS() {
		return SJRS;
	}

	public void setSJRS(String sJRS) {
		SJRS = sJRS;
	}

	public String getSJXZ() {
		return SJXZ;
	}

	public void setSJXZ(String sJXZ) {
		SJXZ = sJXZ;
	}

	public String getYXFW() {
		return YXFW;
	}

	public void setYXFW(String yXFW) {
		YXFW = yXFW;
	}

	public String getXXLY() {
		return XXLY;
	}

	public void setXXLY(String xXLY) {
		XXLY = xXLY;
	}

	public String getJJCD() {
		return JJCD;
	}

	public void setJJCD(String jJCD) {
		JJCD = jJCD;
	}

	public String getSXZT() {
		return SXZT;
	}

	public void setSXZT(String sXZT) {
		SXZT = sXZT;
	}

	public String getYJZT() {
		return YJZT;
	}

	public void setYJZT(String yJZT) {
		YJZT = yJZT;
	}

	public String getSJZT() {
		return SJZT;
	}

	public void setSJZT(String sJZT) {
		SJZT = sJZT;
	}

	public String getSJMC() {
		return SJMC;
	}

	public void setSJMC(String sJMC) {
		SJMC = sJMC;
	}

	public String getDSFSJBS() {
		return DSFSJBS;
	}

	public void setDSFSJBS(String dSFSJBS) {
		DSFSJBS = dSFSJBS;
	}
	
}
