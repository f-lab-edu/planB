package com.flab.planb.server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:properties/application.properties"})
@ComponentScan(basePackages = {"com.flab.planb.server"})
public class ServerConfig {}