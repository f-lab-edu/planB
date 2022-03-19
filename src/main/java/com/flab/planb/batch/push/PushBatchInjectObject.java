package com.flab.planb.batch.push;

import com.flab.planb.batch.ThreadPoolTaskExecutorCreator;
import com.flab.planb.response.message.MessageLookup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Getter
public class PushBatchInjectObject {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SqlSessionFactory sqlSessionFactory;
    private final MessageLookup messageLookup;
    private final ThreadPoolTaskExecutorCreator executorCreator;

}
