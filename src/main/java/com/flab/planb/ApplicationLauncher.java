package com.flab.planb;

import com.flab.planb.server.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class ApplicationLauncher {

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
