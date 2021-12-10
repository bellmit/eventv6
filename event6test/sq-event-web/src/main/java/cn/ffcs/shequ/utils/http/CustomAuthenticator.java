package cn.ffcs.shequ.utils.http;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.nio.charset.Charset;

import sun.misc.BASE64Encoder;

public class CustomAuthenticator extends Authenticator {

	/**
	 * @param userName 账号
	 * @param pass 密码
	 * @param orginal 是否使用原始字符串信息,即调用 getBasicAuthorization 方法设置Authorization请求头
	 */
	public CustomAuthenticator(String userName, String pass,boolean orginal) {
		this.userName = userName;
		this.pass = pass;
		this.orginal = orginal;
	}
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName,pass.toCharArray());
	}

	private String userName;
	private String pass;
	private boolean orginal;
	
	/**
	 * 根据用户名和密码获取Authorization加密请求头,加密方式为Basic+空格+base64(账号:密码)
	 * @return
	 */
	public String getBasicAuthorization(){
		byte[] ab = (userName+":"+pass).getBytes(Charset.forName("utf-8"));
		return "Basic " + new BASE64Encoder().encode(ab);
	}
	public boolean isOrginal() {
		return orginal;
	}
}
