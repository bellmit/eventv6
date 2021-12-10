package cn.ffcs.zhsq.security;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 权限控制切面
 * 
 * @author zengy 2014-05-07
 * */
public class RightInterceptorAOP {

	/**
	 * 拦截
	 * */
	public void doInterceptor(JoinPoint joinPoint) throws Exception {
		// 获得具体调用的方法
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		if (method != null) {
			// 获取该方法的注解
			Permission permission = method.getAnnotation(Permission.class);
			// 如果为空在表示该方法不需要进行权限验证
			if (permission == null) {// pass
				return;
			}
			// 验证是否具有权限 (这里根据需求来修改)
			IRightHandler handler = new WebRightHandler();
			handler.hasRight(permission.parentCode(), permission.rightCode());
		}
	}
}
