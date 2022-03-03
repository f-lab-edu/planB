package com.flab.planb.config;

import com.flab.planb.batch.push.PushItemWriter;
import com.flab.planb.batch.push.Pusher;
import com.flab.planb.dto.subscription.PushInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
@Slf4j
public class PushBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SqlSessionFactory sqlSessionFactory;
    private final Pusher<PushInfo> pusher;

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
    public Job pushJob() throws Exception {
        return jobBuilderFactory.get(StaticValue.JOB_NAME)
                                .preventRestart()
                                .start(pushStep())
                                .build();
    }

    @Bean
    public Step pushStep() throws Exception {
        return stepBuilderFactory.get(StaticValue.STEP_NAME)
                                 .<PushInfo, PushInfo>chunk(StaticValue.CHUNK_SIZE)
                                 .reader(mybatisItemReader())
                                 .writer(itemWriter())
                                 .taskExecutor(taskExecutor())
                                 .build();
    }

    @Bean
    public MyBatisPagingItemReader<PushInfo> mybatisItemReader() throws Exception {
        return new MyBatisPagingItemReaderBuilder<PushInfo>()
            .pageSize(StaticValue.CHUNK_SIZE)
            .sqlSessionFactory(sqlSessionFactory)
            .queryId(StaticValue.QUERY_ID)
            .parameterValues(createParameterValues())
            .build();
    }

    @Bean
    public PushItemWriter<PushInfo> itemWriter() {
        return new PushItemWriter<>(pusher);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(StaticValue.CORE_POOL_SIZE);
        executor.setMaxPoolSize(StaticValue.MAX_POOL_SIZE);
        executor.setQueueCapacity(StaticValue.QUEUE_CAPACITY);
        executor.setThreadNamePrefix("push-async-");
        executor.initialize();
        return executor;
    }

    private Map<String, Object> createParameterValues() {
        LocalTime currentTime = LocalTime.now();
        int subscriptionDay = LocalDate.now().getDayOfWeek().getValue() + (currentTime.getHour() < 23 ? 0 : 1);
        LocalTime subscriptionTime = LocalTime.of(currentTime.getHour(), (currentTime.getMinute() < 30 ? 0 : 30))
                                              .plusHours(1);

        return Map.of("subscriptionDay", subscriptionDay, "subscriptionTime", subscriptionTime);
    }

    static class StaticValue {

        private static final String JOB_NAME = "pushSchedulerJob";
        private static final String STEP_NAME = "pushSchedulerStep";
        private static final String QUERY_ID = "com.flab.planb.service.mapper.SubscriptionMapper.findPushList";
        private static final int CHUNK_SIZE = 10;
        private static final int CORE_POOL_SIZE = 5;
        private static final int MAX_POOL_SIZE = 10;
        private static final int QUEUE_CAPACITY = 100;

    }

}
