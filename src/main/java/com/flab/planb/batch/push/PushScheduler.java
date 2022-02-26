package com.flab.planb.batch.push;

import com.flab.planb.config.PushBatchConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RequiredArgsConstructor
@Component
@EnableScheduling
@Slf4j
public class PushScheduler {

    private static final String KEY = "pushSchedulerStep";
    private final JobLauncher jobLauncher;
    private final PushBatchConfig pushBatchConfig;

    @Scheduled(cron = "0 0/30 * * * *")
    public void pushScheduler() throws Exception {
        startLog();
        JobExecution execution = jobLauncher.run(
            pushBatchConfig.pushJob(),
            new JobParametersBuilder()
                .addDate(KEY, Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .toJobParameters());
        endLog(execution.getStatus());
    }


    private void startLog() {
        log.debug("{} started at : {}", KEY, LocalDateTime.now());
    }

    private void endLog(BatchStatus status) {
        log.debug("{} finished at {} with status : {}", KEY, LocalDateTime.now(), status);
    }

}
