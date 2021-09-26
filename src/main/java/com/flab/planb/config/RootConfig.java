package com.flab.planb.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:properties/application.properties"})
@ComponentScan(basePackages = {"com.flab.planb"})
public class RootConfig {

}
