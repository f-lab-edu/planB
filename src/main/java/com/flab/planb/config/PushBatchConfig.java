package com.flab.planb.config;

import com.flab.planb.batch.push.PushBatchInjectObject;
import com.flab.planb.batch.push.PushItemWriter;
import com.flab.planb.batch.push.Pusher;
import com.flab.planb.dto.push.PushInfo;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@PropertySource({"classpath:properties/application.properties"})
@Slf4j
public class PushBatchConfig {

    private final PushBatchInjectObject injectObject;
    private final Pusher<PushInfo> pusher;
    private final int chunkSize;

    public PushBatchConfig(PushBatchInjectObject injectObject, @Value("${batch.chunk-size}") int chunkSize) {
        this.injectObject = injectObject;
        this.chunkSize = chunkSize;
        this.pusher = new Pusher<>();
    }

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(
            JdbcTemplateLockProvider.Configuration
                .builder()
                .withJdbcTemplate(new JdbcTemplate(dataSource))
                .usingDbTime()
                .build()
        );
    }

    @Bean
    public Job pushJob() {
        return injectObject.getJobBuilderFactory()
                           .get(StaticValue.JOB_NAME)
                           .preventRestart()
                           .start(pushStep())
                           .next(saveFailInfo())
                           .build();
    }

    @Bean
    public Step pushStep() {
        return injectObject.getStepBuilderFactory()
                           .get(StaticValue.STEP_NAME)
                           .<PushInfo, PushInfo>chunk(chunkSize)
                           .reader(mybatisItemReader())
                           .writer(itemWriter())
                           .taskExecutor(taskExecutor())
                           .build();
    }

    @Bean
    public Step saveFailInfo() {
        return injectObject.getStepBuilderFactory()
                           .get(StaticValue.FAIL_STEP_NAME)
                           .tasklet((contribution, chunkContext) -> {
                               pusher.getFailedList().forEach(item -> log.info(item.toString()));
                               pusher.resetFailList();
                               return RepeatStatus.FINISHED;
                           }).build();
    }

    @Bean
    public MyBatisPagingItemReader<PushInfo> mybatisItemReader() {
        return new MyBatisPagingItemReaderBuilder<PushInfo>()
            .pageSize(chunkSize)
            .sqlSessionFactory(injectObject.getSqlSessionFactory())
            .queryId(StaticValue.QUERY_ID)
            .parameterValues(createParameterValues())
            .build();
    }

    @Bean
    public PushItemWriter<PushInfo> itemWriter() {
        return new PushItemWriter<>(pusher, injectObject.getMessageLookup().getMessage(StaticValue.PUSH_TITLE));
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return injectObject.getExecutorCreator().create(StaticValue.THREAD_NAME_PREFIX);
    }

    private Map<String, Object> createParameterValues() {
        LocalTime currentTime = LocalTime.now();
        int subscriptionDay = LocalDate.now().getDayOfWeek().getValue() + (currentTime.getHour() < 23 ? 0 : 1);
        LocalTime subscriptionTime = LocalTime
            .of(currentTime.getHour(), (currentTime.getMinute() < 30 ? 0 : 30)).plusHours(1);

        return Map.of("subscriptionDay", subscriptionDay, "subscriptionTime", subscriptionTime);
    }

    static class StaticValue {

        private static final String JOB_NAME = "pushSchedulerJob";
        private static final String STEP_NAME = "pushSchedulerStep";
        public static final String FAIL_STEP_NAME = "failedPushSaveStep";
        private static final String QUERY_ID = "com.flab.planb.service.mapper.SubscriptionMapper.findPushList";
        private static final String THREAD_NAME_PREFIX = "push-async-";
        private static final String PUSH_TITLE = "alert.subscription.schedule.title";
    }

}
