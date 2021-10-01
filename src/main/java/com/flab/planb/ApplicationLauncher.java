package com.flab.planb;

import com.flab.planb.global.GlobalMethods;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationLauncher {

    private static final Logger log = LoggerFactory.getLogger(ApplicationLauncher.class);
    private static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
    private static final String APP_BASE = ".";
    private static final String PREFIX = "tomcat.";
    private static final String ROOT_PATH = "user.dir";

    public static void main(String[] args) {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(createTempDir());
        tomcat.setPort(PORT);
        tomcat.getHost().setAppBase(APP_BASE);
        Context context = tomcat.addWebapp("", APP_BASE);
        context.setReloadable(true);
        context.setRequestCharacterEncoding(GlobalMethods.encoding);
        context.setResponseCharacterEncoding(GlobalMethods.encoding);
        tomcat.getConnector();
        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException le) {
            log.error("톰캣 start 실패", le);
        }
    }

    private static String createTempDir() {
        try {
            deleteDirectory(Paths.get(System.getProperty(ROOT_PATH)).toFile(),
                new PrefixFileFilter(PREFIX, IOCase.SENSITIVE));
            Path tempPath = Files.createTempDirectory(Paths.get(System.getProperty(ROOT_PATH)),
                PREFIX);
            File tempFile = tempPath.toFile();
            tempFile.deleteOnExit();

            return tempFile.getAbsolutePath();
        } catch (IOException ex) {
            log.error("temp 디렉터리 생성 실패", ex);
            throw new NullPointerException("absolutePath가 null이므로 톰캣 start 중지");
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
