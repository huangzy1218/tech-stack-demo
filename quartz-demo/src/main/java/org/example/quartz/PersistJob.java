package org.example.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@PersistJobDataAfterExecution
public class PersistJob implements Job {
    private Integer executeCount;

    public void setExecuteCount(Integer executeCount) {
        this.executeCount = executeCount;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String data = LocalDateTime.now().
                format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("Execute count: {}, current time: {}",
                ++executeCount, data);
        //将累加的 count 存入JobDataMap中
        jobExecutionContext.getJobDetail().
                getJobDataMap().put("executeCount", executeCount);
    }
}
