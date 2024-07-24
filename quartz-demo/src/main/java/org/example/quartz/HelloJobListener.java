package org.example.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * @author Huang Z.Y.
 */
@Slf4j
public class HelloJobListener implements JobListener {
    private String name;

    public HelloJobListener(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        log.info("{} 还未执行", name);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        log.info("{} 即将被执行，但又被 TriggerListener 否决时会调用该方法", name);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException e) {
        log.info("{} 执行完成", name);
    }
}
