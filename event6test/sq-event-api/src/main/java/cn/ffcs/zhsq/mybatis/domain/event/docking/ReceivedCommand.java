package cn.ffcs.zhsq.mybatis.domain.event.docking;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ReceivedCommand",namespace="http://www.dscomm.com.cn/ns/soc/dailyIncidentService")
@XmlAccessorType(XmlAccessType.NONE)
public class ReceivedCommand  implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5464025914367310593L;

	@XmlAttribute(name = "commandCode")
	private String commandCode;
	
	@XmlAttribute(name = "receiveUser")
	private String receiveUser;

	public String getCommandCode() {
		return commandCode;
	}

	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}

	public String getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(String receiveUser) {
		this.receiveUser = receiveUser;
	}
}
