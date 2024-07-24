package org.example.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.EverythingMatcher;
import org.quartz.impl.matchers.KeyMatcher;

/**
 * @author Huang Z.Y.
 */
public class HelloScheduler {
    public static void main(String[] args) throws SchedulerException {
        // 1. 从工厂获取调度器实例
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        // 2. 创建执行的任务对象
        JobDetail job = JobBuilder.newJob(HelloJob.class)
                // 任务名称,组名称
                .withIdentity("job1", "group1")
                // 添加属性
                .usingJobData("jobSays", "Hello World!")
                .usingJobData("myFloatValue", 3.141f)
                .build();


        // 3. 构造触发器，控制执行次数和执行时间
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                // 立刻启动
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().
                        withIntervalInSeconds(3).
                        // 循环10次,每次间隔3s
                                withRepeatCount(10))
                .build();
        
        // 注册全局监听器
        scheduler.getListenerManager().addJobListener(new HelloJobListener("job-listener-global"), EverythingMatcher.allJobs());
        // 注册局部监听器
        scheduler.getListenerManager().addJobListener(new HelloJobListener("job-listener"), KeyMatcher.keyEquals(JobKey.jobKey("job1", "group1")));
        // 注册全局 Trigger 监听器
        scheduler.getListenerManager().addTriggerListener(new HelloTriggerListener("trigger-listener"), KeyMatcher.keyEquals(TriggerKey.triggerKey("job1", "group1")));
        // 注册全局 Scheduler 监听器
        scheduler.getListenerManager().addSchedulerListener(new HelloSchedulerListener());

        // 调度器关联触发器, 并启动
        scheduler.scheduleJob(job, trigger);
        scheduler.start();
    }
}
