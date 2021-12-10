package cn.ffcs.zhsq.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.http.HttpSession;
import org.springframework.core.io.ClassPathResource;

public class ReadProperties {
	
	public static String javaLoadProperties(String key,HttpSession session,String prpertiesName){
		String str=File.separator;
		String fath=str+"WEB-INF"+str+"classes"+str+prpertiesName;
	    InputStream path=session.getServletContext().getResourceAsStream(fath);
	
	    //InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("password.properties");
	
	    /*File filepath=new File(this.getServletContext().getRealPath(str+"WEB-INF"+str+"classes")+str+"password.properties");
	
	    InputStream path=new FileInputStream(filepath);*/
	    Properties pros = new Properties();
	    try {
	        pros.load(path);
	    } catch (IOException ex) {
	        return "资源文件不存在";
	    }
	    return pros.getProperty(key);
	}
	
	public static String javaLoadProperties(String key,String prpertiesName){
	    ClassPathResource cr = new ClassPathResource(prpertiesName);//会重新加载spring框架
	    Properties pros = new Properties();
	    try {
	        pros.load(cr.getInputStream());
	    } catch (IOException ex) {
	    	return "资源文件不存在";
	    }
	    return pros.getProperty(key);
	}
}
