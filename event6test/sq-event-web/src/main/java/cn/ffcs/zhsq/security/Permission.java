package cn.ffcs.zhsq.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permission {

	/**
	 * @param 权限编号
	 * 
	 * @author zengy 2014-5-5 
	 * */	
	String rightCode();

	/**
	 * @param 菜单编号
	 * 
	 * @return
	 */
	String parentCode();
	
}

