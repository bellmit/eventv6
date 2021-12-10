package cn.ffcs.zhsq.mybatis.domain.map.sanitationtrunck;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 环卫车基础信息表模块bo对象
 * @Author: dingyw
 * @Date: 01-02 14:08:17
 * @Copyright: 2018 福富软件
 */
public class SanitationTrunck implements Serializable {

	private static final long serialVersionUID = -910717164878100080L;
	
	private String carcode; //车牌号码
	private String carnumber; //车辆编号
	private String createtime; //增加时间
	private String gpsx; //X坐标*10的6次方
	private String gpsy; //Y坐标*10的6次方
	private String id; //编码
	private String identifier; //车辆标识
	private String lastreporttime; //最后上报时间
	private String placecode; //所属区域编码
	private String telephone; //车载上报卡号
	private String unit; //车辆所属单位
	private String validity; //是否有效：0有效1无效
	private Date synDate; //接口同步时间

	public SanitationTrunck() {
		super();
	}

	public String getCarcode() {
		return carcode;
	}

	public void setCarcode(String carcode) {
		this.carcode = carcode;
	}

	public String getCarnumber() {
		return carnumber;
	}

	public void setCarnumber(String carnumber) {
		this.carnumber = carnumber;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getGpsx() {
		return gpsx;
	}

	public void setGpsx(String gpsx) {
		this.gpsx = gpsx;
	}

	public String getGpsy() {
		return gpsy;
	}

	public void setGpsy(String gpsy) {
		this.gpsy = gpsy;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getLastreporttime() {
		return lastreporttime;
	}

	public void setLastreporttime(String lastreporttime) {
		this.lastreporttime = lastreporttime;
	}

	public String getPlacecode() {
		return placecode;
	}

	public void setPlacecode(String placecode) {
		this.placecode = placecode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public Date getSynDate() {
		return synDate;
	}

	public void setSynDate(Date synDate) {
		this.synDate = synDate;
	}

}