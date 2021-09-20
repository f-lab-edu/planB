package com.flab.planb;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationLauncher {

    private static final Logger log = LoggerFactory.getLogger(ApplicationLauncher.class);
    private static final int PORT = Integer.parseInt(
        Optional.ofNullable(System.getProperty("port")).orElse("8080"));

    public static void main(String[] args) {
        String encoding = Optional.ofNullable(System.getProperty("encoding")).orElse("UTF-8");
        String appBase = ".";
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(createTempDir());
        tomcat.setPort(PORT);
        tomcat.getHost().setAppBase(appBase);
        Context context = tomcat.addWebapp("", appBase);
        context.setReloadable(true);
        context.setRequestCharacterEncoding(encoding);
        context.setResponseCharacterEncoding(encoding);
        tomcat.getConnector();
        try {
            tomcat.start();
        } catch (LifecycleException le) {
            throw new RuntimeException("톰캣 start 실패", le);
        }
        tomcat.getServer().await();
    }

    private static String createTempDir() {
        try {
            String prefix = "tomcat.";
            String rootPath = "user.dir";
            deleteDirectory(Paths.get(System.getProperty(rootPath)).toFile(),
                new PrefixFileFilter(prefix, IOCase.SENSITIVE));
            Path tempPath = Files.createTempDirectory(Paths.get(System.getProperty(rootPath)),
                prefix);
            File tempFile = tempPath.toFile();
            tempFile.deleteOnExit();

            return tempFile.getAbsolutePath();
        } catch (IOException ex) {
            throw new RuntimeException(
                "temp 디렉터리 생성 실패. java.io.tmpdir 위치 : " + System.getProperty("java.io.tmpdir"), ex);
        }
    }

    private static void deleteDirectory(File directoryToBeDeleted, FileFilter fileFilter) {
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
