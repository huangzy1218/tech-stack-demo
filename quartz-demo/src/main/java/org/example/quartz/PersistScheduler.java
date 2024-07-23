package org.example.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author Huang Z.Y.
 */
public class PersistScheduler {
    public static void main(String[] args) throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        JobDetail job = JobBuilder.newJob(PersistJob.class)
                .withIdentity("job2", "group2")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger2", "group2")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(3)
                        .withRepeatCount(10)).build();
        // 将 executeCount 设置到 JobDataMap 中
        job.getJobDataMap().put("executeCount", 0);

        scheduler.scheduleJob(job, trigger);
        scheduler.start();
    }
}
