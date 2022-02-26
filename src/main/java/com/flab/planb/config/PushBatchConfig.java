package com.flab.planb.config;

import com.flab.planb.batch.push.Pusher;
import com.flab.planb.dto.subscription.PushInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
@Slf4j
public class PushBatchConfig {

    private static final String JOB_NAME = "pushSchedulerJob";
    private static final String STEP_NAME = "pushSchedulerStep";
    private static final String QUERY_ID = "com.flab.planb.service.mapper.SubscriptionMapper.findPushList";
    private static final int CHUNK_SIZE = 10;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SqlSessionFactory sqlSessionFactory;
    private final Pusher pusher;

    @Bean
    public Job pushJob() throws Exception {
        return jobBuilderFactory.get(JOB_NAME)
                                .preventRestart()
                                .start(pushStep())
                                .build();
    }

    @Bean
    public Step pushStep() throws Exception {
        return stepBuilderFactory.get(STEP_NAME)
                                 .<PushInfo, PushInfo>chunk(CHUNK_SIZE)
                                 .reader(mybatisItemReader())
                                 .writer(jdbcPagingItemWriter())
                                 .build();
    }

    @Bean
    public MyBatisPagingItemReader<PushInfo> mybatisItemReader() throws Exception {
        return new MyBatisPagingItemReaderBuilder<PushInfo>()
            .pageSize(CHUNK_SIZE)
            .sqlSessionFactory(sqlSessionFactory)
            .queryId(QUERY_ID)
            .parameterValues(Map.of("subscriptionDay", LocalDate.now().getDayOfWeek().getValue()))
            .build();
    }

    private ItemWriter<PushInfo> jdbcPagingItemWriter() {
        return list -> {
            for (PushInfo info : list) {
                pusher.push(info);
            }
        };
    }

}
