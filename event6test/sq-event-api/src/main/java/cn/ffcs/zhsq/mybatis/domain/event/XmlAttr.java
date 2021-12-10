package cn.ffcs.zhsq.mybatis.domain.event;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import java.text.ParseException;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import cn.ffcs.zhsq.mybatis.domain.crowd.VisitRecord;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;


@XmlRootElement(name="attr")
public class XmlAttr implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8489550009586111719L;
	private String attrType;
	private String attrName;
	private String attrURL;
	private String attrSize;
	private String attrBiz;
	private String attrBASE64;
	private String attrBase64;
	private String base64;
	public String getAttrType() {
		return attrType;
	}
	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public String getAttrURL() {
		return attrURL;
	}
	public void setAttrURL(String attrURL) {
		this.attrURL = attrURL;
	}
	public String getAttrSize() {
		return attrSize;
	}
	public void setAttrSize(String attrSize) {
		this.attrSize = attrSize;
	}
	public String getAttrBiz() {
		return attrBiz;
	}
	public void setAttrBiz(String attrBiz) {
		this.attrBiz = attrBiz;
	}


	public String getAttrBASE64() {
		return attrBASE64;
	}

	public void setAttrBASE64(String attrBASE64) {
		this.attrBASE64 = attrBASE64;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public String getAttrBase64() {
		return attrBase64;
	}

	public void setAttrBase64(String attrBase64) {
		this.attrBase64 = attrBase64;
	}
}
