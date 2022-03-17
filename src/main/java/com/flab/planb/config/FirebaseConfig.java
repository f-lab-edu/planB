package com.flab.planb.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import javax.annotation.PostConstruct;

@Configuration
@PropertySource({"classpath:properties/application.properties"})
@Slf4j
public class FirebaseConfig {

    @Value("${firebase.sdk.path}")
    private String firebasesdkPath;

    @PostConstruct
    public void init() throws IOException {
        FirebaseOptions options = FirebaseOptions
            .builder()
            .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebasesdkPath).getInputStream()))
            .build();
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            log.info("Firebase application has been initialized");
        }
    }
}
