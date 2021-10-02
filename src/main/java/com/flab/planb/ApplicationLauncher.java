package com.flab.planb;

import com.flab.planb.config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationLauncher {

    private static final Logger log = LoggerFactory.getLogger(ApplicationLauncher.class);

    public static void main(String[] ars) {
        try {
            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.register(ServerConfig.class);
            ctx.refresh();
            ctx.start();
        } catch (BeansException | IllegalStateException e) {
            log.error("Application start 실패", e);
        }

    }
}
