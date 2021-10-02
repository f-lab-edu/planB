package com.flab.planb.server;

import com.flab.planb.global.GlobalMethods;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmbeddedTomcatServer {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Tomcat tomcat = new Tomcat();

    @Value("${tomcat.port}")
    private int port;

    @Value("${tomcat.app-base}")
    private String appBase;

    @Value("${tomcat.prefix}")
    private String prefix;

    @Value("${tomcat.root-path}")
    private String rootPath;

    @PostConstruct
    public void start() throws LifecycleException {
        // Tomcat 구성 요소 중 하나가 시작되지 않는 경우 Tomcat을 중지하기 위한 것
        System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");
        tomcat.setBaseDir(createTempDir());
        tomcat.setPort(port);
        tomcat.getHost().setAppBase(appBase);
        Context context = tomcat.addWebapp("", appBase);
        context.setReloadable(true);
        context.setRequestCharacterEncoding(GlobalMethods.encoding);
        context.setResponseCharacterEncoding(GlobalMethods.encoding);
        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();
    }

    @PreDestroy
    public void stop() throws LifecycleException {
        tomcat.stop();
    }

    private String createTempDir() {
        try {
            deleteDirectory(Paths.get(System.getProperty(rootPath)).toFile(),
                new PrefixFileFilter(prefix, IOCase.SENSITIVE));
            Path tempPath = Files.createTempDirectory(Paths.get(System.getProperty(rootPath)),
                prefix);
            File tempFile = tempPath.toFile();
            tempFile.deleteOnExit();

            return tempFile.getAbsolutePath();
        } catch (IOException ex) {
            log.error("temp 디렉터리 생성 실패", ex);
            throw new NullPointerException("absolutePath가 null이므로 톰캣 start 중지");
        }
    }

    private void deleteDirectory(File directoryToBeDeleted, FileFilter fileFilter) {
        File[] allContents = directoryToBeDeleted.listFiles(fileFilter);
        if (allContents != null) {
            for (File file : allContents) {
                if (file.isDirectory()) {
                    deleteDirectory(file, null);
                }
                if (!file.delete()) {
                    log.info(file.getAbsoluteFile() + " 삭제 실패");
                }
            }
        }
    }

}