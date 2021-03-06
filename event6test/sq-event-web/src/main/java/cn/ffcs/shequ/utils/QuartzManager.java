package cn.ffcs.shequ.utils;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

/**
 * Created by Administrator on 2017/6/27.
 */
 

/**
 * @author zhongshm
 * @create 2017-06-27 16:00
 **/
public class QuartzManager {
    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";

    /**
     * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
     *
     * @param sched
     *            调度器
     *
     * @param jobName
     *            任务名
     * @param cls
     *            任务
     * @param time
     *            时间设置，参考quartz说明文档
     *
     * @Title: QuartzManager.java
     */
    public static void addJob(Scheduler sched, String jobName, @SuppressWarnings("rawtypes") Class cls, String time) {
        /*try {
            JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, cls);// 任务名，任务组，任务执行类
            
            // 触发器
            CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// 触发器名,触发器组
            trigger.setCronExpression(time);// 触发器时间设定
            sched.scheduleJob(jobDetail, trigger);
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    	JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, JOB_GROUP_NAME).build();
    	Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, TRIGGER_GROUP_NAME)
				.withSchedule(CronScheduleBuilder.cronSchedule(time)).build(); 
		try {
			sched.scheduleJob(jobDetail, trigger);
			if (!sched.isShutdown()) {
                sched.start();
            }
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
    }

    /**
     * @Description: 添加一个定时任务
     *
     * @param sched
     *            调度器
     *
     * @param jobName
     *            任务名
     * @param jobGroupName
     *            任务组名
     * @param triggerName
     *            触发器名
     * @param triggerGroupName
     *            触发器组名
     * @param jobClass
     *            任务
     * @param time
     *            时间设置，参考quartz说明文档
     *
     * @Title: QuartzManager.java
     */
    public static void addJob(Scheduler sched, String jobName, String jobGroupName, String triggerName, String triggerGroupName, @SuppressWarnings("rawtypes") Class jobClass, String time) {
        /*try {
            JobDetail jobDetail = new JobDetail(jobName, jobGroupName, jobClass);// 任务名，任务组，任务执行类
            // 触发器
            CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);// 触发器名,触发器组
            trigger.setCronExpression(time);// 触发器时间设定
            sched.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }

    /**
     * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
     *
     * @param sched
     *            调度器
     * @param jobName
     * @param time
     *
     * @Title: QuartzManager.java
     */
    public static void modifyJobTime(Scheduler sched, String jobName, String time) {
        /*try {
            CronTrigger trigger = (CronTrigger) sched.getTrigger(jobName, TRIGGER_GROUP_NAME);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                JobDetail jobDetail = sched.getJobDetail(jobName, JOB_GROUP_NAME);
                Class objJobClass = jobDetail.getJobClass();
                removeJob(sched, jobName);
                addJob(sched, jobName, objJobClass, time);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    	modifyJobTime(sched,jobName,TRIGGER_GROUP_NAME,time);
    }

    /**
     * @Description: 修改一个任务的触发时间
     *
     * @param sched
     *            调度器 *
     * @param sched
     *            调度器
     * @param triggerName
     * @param triggerGroupName
     * @param time
     *
     * @Title: QuartzManager.java
     */
    public static void modifyJobTime(Scheduler sched, String triggerName, String triggerGroupName, String time) {
    	TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        CronTrigger trigger;
		try {
			trigger = (CronTrigger) sched.getTrigger(triggerKey);
			if (trigger == null) {  
				return;  
			}
			 String oldTime = trigger.getCronExpression();  
	            if (!oldTime.equalsIgnoreCase(time)) { 
	                // 触发器  
	                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
	                // 触发器名,触发器组  
	                triggerBuilder.withIdentity(triggerName, triggerGroupName);
	                triggerBuilder.startNow();
	                // 触发器时间设定  
	                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(time));
	                // 创建Trigger对象
	                trigger = (CronTrigger) triggerBuilder.build();
	                // 方式一 ：修改一个任务的触发时间
	                sched.rescheduleJob(triggerKey, trigger);
	            }  
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}  
    }

    /**
     * @Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     *
     * @param sched
     *            调度器
     * @param jobName
     *
     * @Title: QuartzManager.java
     */
    public static void removeJob(Scheduler sched, String jobName) {
       /* try {
            sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// 停止触发器
            sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// 移除触发器
            sched.deleteJob(jobName, JOB_GROUP_NAME);// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    	try {  
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME);
            sched.pauseTrigger(triggerKey);// 停止触发器  
            sched.unscheduleJob(triggerKey);// 移除触发器  
            sched.deleteJob(JobKey.jobKey(jobName, JOB_GROUP_NAME));// 删除任务  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }

    /**
     * @Description: 移除一个任务
     *
     * @param sched
     *            调度器
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     *
     * @Title: QuartzManager.java
     */
    public static void removeJob(Scheduler sched, String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        /*try {
            sched.pauseTrigger(triggerName, triggerGroupName);// 停止触发器
            sched.unscheduleJob(triggerName, triggerGroupName);// 移除触发器
            sched.deleteJob(jobName, jobGroupName);// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }

    /**
     * @Description:启动所有定时任务
     *
     * @param sched
     *            调度器
     *
     * @Title: QuartzManager.java
     */
    public static void startJobs(Scheduler sched) {
        try {
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:关闭所有定时任务
     *
     *
     * @param sched
     *            调度器
     *
     *
     * @Title: QuartzManager.java
     */
    public static void shutdownJobs(Scheduler sched) {
        try {
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
