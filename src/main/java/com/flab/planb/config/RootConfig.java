package com.flab.planb.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"com.flab.planb"})
@PropertySource({"classpath:properties/application.properties"})
public class RootConfig {

}
