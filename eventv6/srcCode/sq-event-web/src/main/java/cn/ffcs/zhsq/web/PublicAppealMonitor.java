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

//@Aspect
//@Component
public class PublicAppealMonitor {

    public PublicAppealMonitor(){}

    @Pointcut("execution(* ccn.ffcs.zhsq.publicAppeal.service.PublicAppealServiceImpl.searchList())")
    public void searchList(){}

    @AfterReturning("searchList()")
    public void afterSleep(){
        System.out.println("吼吼吼！");
    }

    @Around("execution(* ccn.ffcs.zhsq.publicAppeal.service.PublicAppealServiceImpl.searchList())")
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable{
        System.out.println("进入环绕通知");
        Object object = pjp.proceed();//执行该方法
        System.out.println("退出方法");
        return object;
    }
}
