package cn.ffcs.zhsq.times.service;/**
 * Created by Administrator on 2017/6/29.
 */

import cn.ffcs.shequ.utils.QuartzManager;
import cn.ffcs.zhsq.mybatis.domain.timerManage.TimerManage;
import cn.ffcs.zhsq.time.ITimerManageService;
import org.quartz.Scheduler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhongshm
 * @create 2017-06-29 20:07
 **/
//@Service
public class TestService{

    @Autowired
    private ITimerManageService timerManageService;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    public void initTask() throws Exception {
        System.out.println("===加载定时器===");
        Map<String, Object> params = new HashMap<String, Object>();
        List<TimerManage> timerManageList = timerManageService.getTimerManageList(params);
        if(timerManageList.size()>0){
            System.out.println(timerManageList.get(0).getTimerName());
            for(TimerManage timerManage : timerManageList){
                Scheduler sche = schedulerFactoryBean.getScheduler();
                try {
//                    QuartzManager.addJob(sche, "动态任务调度", Class.forName("cn.ffcs.zhsq.event.service.stub.gansu.QuartzJob"), "0 0/1 * * * ?");
                    QuartzManager.addJob(sche, timerManage.getTimerName(), Class.forName(timerManage.getTaskClass()), timerManage.getExpression());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
