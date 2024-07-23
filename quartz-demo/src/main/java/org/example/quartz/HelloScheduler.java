package org.example.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

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

        // 调度器关联触发器, 并启动
        scheduler.scheduleJob(job, trigger);
        scheduler.start();
    }
}
