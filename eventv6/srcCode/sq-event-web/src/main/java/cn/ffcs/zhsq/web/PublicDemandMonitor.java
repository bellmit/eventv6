package cn.ffcs.zhsq.web;/**
 * Created by Administrator on 2017/11/9.
 */

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 诉求
 *
 * @author zhongshm
 * @create 2017-11-09 14:43
 **/

@Aspect
@Component
public class PublicDemandMonitor {

    public PublicDemandMonitor(){}

    @Pointcut("execution(* cn.ffcs.zhsq.api.PublicDemandServController.test())")
    public void test(){}

    @AfterReturning("test()")
    public void afterSleep(){
        System.out.println("吼吼吼！");
    }

    @Around("execution(* cn.ffcs.zhsq.api.PublicDemandServController.test())")
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable{
        System.out.println("进入环绕通知");
        Object object = pjp.proceed();//执行该方法
        System.out.println("退出方法");
        return object;
    }
}
