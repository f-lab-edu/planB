package com.flab.planb.config;

import com.flab.planb.common.ApplicationContextProvider;
import com.flab.planb.common.Common;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@PropertySource({"classpath:properties/application.properties"})
@Configuration
@ComponentScan(
    basePackages = {"com.flab.planb"},
    useDefaultFilters = false,
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Service.class),
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Repository.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
            ApplicationContextProvider.class})
    },
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = RestControllerAdvice.class),
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)
    }
)

public class RootConfig {

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:messages/message");
    messageSource.setDefaultEncoding(Common.ENCODIG);
    messageSource.setCacheSeconds(60);
    messageSource.setUseCodeAsDefaultMessage(true);
    return messageSource;
  }

}
