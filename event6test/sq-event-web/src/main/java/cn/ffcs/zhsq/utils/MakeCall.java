package cn.ffcs.zhsq.utils;

import java.io.InputStream;
import java.util.Properties;

public class MakeCall {
	private static Properties initProps = null;

	public static String getMakeCallProperty(String name) {
		loadProperties();
		if(initProps.getProperty(name) != null && !"".equals(initProps.getProperty(name)))
			return initProps.getProperty(name);
		else
			return "";
	}

	private synchronized static void loadProperties() {
		if (initProps == null || initProps.isEmpty()) {
			initProps = new Properties();
			InputStream in = null;
			try {
				in = MakeCall.class.getResourceAsStream("make_call.properties");
				initProps.load(in);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Load make_call.properties configure file error!");
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (Exception e) {
				}
			}
		}
	}
}