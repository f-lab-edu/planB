package com.flab.planb.batch.push;

import java.util.Map;
import javax.sql.DataSource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
@Slf4j
public class PushBatchTestConfig {

    private static final String JOB_NAME = "pushSchedulerTestJob";
    private static final String STEP_NAME = "pushSchedulerTestStep";
    private static final int CHUNK_SIZE = 10;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

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
                                 .<Count, Count>chunk(CHUNK_SIZE)
                                 .reader(mybatisItemReader())
                                 .writer(new CustomItemWriter<>())
                                 .taskExecutor(taskExecutor())
                                 .build();
    }

    @Bean
    public JdbcPagingItemReader<Count> mybatisItemReader() throws Exception {
        return new JdbcPagingItemReaderBuilder<Count>()
            .pageSize(CHUNK_SIZE)
            .fetchSize(CHUNK_SIZE)
            .dataSource(dataSource)
            .rowMapper(new BeanPropertyRowMapper<>(Count.class))
            .queryProvider(createQueryProvider())
            .name("jdbcPagingItemReader")
            .build();
    }

    @Bean
    public PagingQueryProvider createQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("select a.num, concat(a.num, '번째') as num_str ");
        queryProvider.setFromClause("from (WITH RECURSIVE cte AS( "
                                        + "    SELECT 1 AS num "
                                        + "    UNION ALL "
                                        + "    SELECT num + 1 FROM cte WHERE num < 200 "
                                        + ") SELECT num FROM cte) a");
        queryProvider.setSortKeys(Map.of("num", Order.ASCENDING));

        return queryProvider.getObject();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }

}

@NoArgsConstructor
@Getter
@Setter
@ToString
class Count {

    int num;
    String numStr;

    public Count(int num, String numStr) {
        this.num = num;
        this.numStr = numStr;
    }
}
