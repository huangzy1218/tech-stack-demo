package org.example.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * @author Huang Z.Y.
 */
@Slf4j
public class HelloJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        // 从 DataMap 获取属性
        String jobSays = dataMap.getString("jobSays");
        float myFloatValue = dataMap.getFloat("myFloatValue");
        JobKey key = context.getJobDetail().getKey();
        log.info("Instance {} of HelloJob says: {}, and val is: {}", key, jobSays, myFloatValue);
    }
}
