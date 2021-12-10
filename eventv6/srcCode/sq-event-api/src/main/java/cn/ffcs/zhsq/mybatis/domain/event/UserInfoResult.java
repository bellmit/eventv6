package cn.ffcs.zhsq.mybatis.domain.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="datas")
public class UserInfoResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7546370341977084832L;
	private List<UserInfoData> userInfoData;

	@XmlElement(name="data")
	public List<UserInfoData> getUserInfoData() {
		return userInfoData;
	}

	public void setUserInfoData(List<UserInfoData> userInfoData) {
		this.userInfoData = userInfoData;
	}
	

}
