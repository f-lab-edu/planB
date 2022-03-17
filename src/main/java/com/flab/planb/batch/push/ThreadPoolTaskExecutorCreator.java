package com.flab.planb.batch.push;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@PropertySource({"classpath:properties/application.properties"})
public class ThreadPoolTaskExecutorCreator {

    @Value("${batch.pool.core-size}")
    private int corePoolSize;
    @Value("${batch.pool.max-size}")
    private int maxPoolSize;
    @Value("${batch.queue-capacity}")
    private int queueCapacity;

    public ThreadPoolTaskExecutor create(String prefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(prefix);
        executor.initialize();
        return executor;
    }
    
}
