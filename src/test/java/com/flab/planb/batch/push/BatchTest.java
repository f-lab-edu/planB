package com.flab.planb.batch.push;

import com.flab.planb.config.DBConfig;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@ExtendWith({SpringExtension.class})
@ContextConfiguration(
    classes = {
        DBConfig.class,
        PushBatchTestConfig.class
    }
)
@PropertySource(
    {
        "file:src/main/resources/properties/*.properties",
        "file:src/main/resources/logback-dev.xml"
    }
)
public class BatchTest {

    private static final String KEY = "pushSchedulerTestStep";
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private PushBatchTestConfig pushBatchTestConfig;

    @Test
    void test() throws Exception {
        JobExecution execution = jobLauncher.run(
            pushBatchTestConfig.pushJob(),
            new JobParametersBuilder()
                .addDate(KEY, Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .toJobParameters());
        Assertions.assertEquals(pushBatchTestConfig.testPusher().getFailedList().size(), 200);
    }

}
