package com.flab.planb.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource({"classpath:properties/application.properties"})
@Configuration
@ComponentScan(basePackages = {"com.flab.planb.server"})
public class ServerConfig {

}