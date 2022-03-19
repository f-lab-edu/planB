package com.flab.planb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flab.planb.response.message.MessageLookup;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
@PropertySource({"classpath:properties/application.properties"})
@ComponentScan(
    basePackages = "com.flab.planb",
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.REGEX,
            pattern = {"com.flab.planb.controller.*", "com.flab.planb.server.*"}
        ),
        @ComponentScan.Filter(
            type = FilterType.ANNOTATION,
            value = Configuration.class
        )
    }
)
public class RootConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/message");
        messageSource.setDefaultEncoding(MessageLookup.ENCODIG);
        messageSource.setCacheSeconds(60);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

}
