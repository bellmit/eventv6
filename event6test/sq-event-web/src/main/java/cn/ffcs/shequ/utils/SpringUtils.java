package cn.ffcs.shequ.utils;/**
 * Created by Administrator on 2017/6/29.
 */

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author zhongshm
 * @create 2017-06-29 19:57
 **/
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }

    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isNullString(String str){
        if("".equals(str) ||str==null){
            return true;
        }else{
            return false;
        }
    }

}
