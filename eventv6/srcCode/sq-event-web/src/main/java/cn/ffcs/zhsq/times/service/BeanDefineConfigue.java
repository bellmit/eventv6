package cn.ffcs.zhsq.times.service;/**
 * Created by Administrator on 2017/6/29.
 */

import cn.ffcs.shequ.utils.QuartzManager;
import cn.ffcs.zhsq.event.service.stub.gansu.QuartzJob;
import cn.ffcs.zhsq.mybatis.domain.timerManage.TimerManage;
import cn.ffcs.zhsq.time.ITimerManageService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加载定时器
 * @author zhongshm
 * @create 2017-06-29 10:25
 **/
//@Component("BeanDefineConfigue")
public class BeanDefineConfigue implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ITimerManageService timerManageService;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        /*if(event.getApplicationContext().getParent() == null){
            System.out.println("===加载定时器===");

            Map<String, Object> params = new HashMap<String, Object>();
            List<TimerManage> timerManageList = timerManageService.getTimerManageList(params);
            System.out.println(timerManageList.get(0).getTimerName());

            for(TimerManage timerManage : timerManageList){
                Scheduler sche = schedulerFactoryBean.getScheduler();
                String job_name = "动态任务调度";
                System.out.println("【系统启动】开始(每1秒输出一次)...");
                try {
                    QuartzManager.addJob(sche, job_name, Class.forName("cn.ffcs.zhsq.event.service.stub.gansu.QuartzJob"), "0 0/1 * * * ?");
//                QuartzManager.addJob(sche, timerManage.getTimerName(), Class.forName(timerManage.getTaskClass()), timerManage.getExpression());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }
}
