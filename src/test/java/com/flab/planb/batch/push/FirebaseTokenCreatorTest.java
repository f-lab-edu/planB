package com.flab.planb.batch.push;

import com.flab.planb.config.FirebaseConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@ExtendWith({SpringExtension.class})
@ContextConfiguration(
    classes = {
        FirebaseConfig.class
    }
)
@PropertySource(
    {
        "file:src/main/resources/properties/*.properties",
        "file:src/main/resources/logback-dev.xml"
    }
)
@Slf4j
public class FirebaseTokenCreatorTest {

    @Test
    void create() throws FirebaseAuthException {
        log.debug(FirebaseAuth.getInstance().createCustomToken(UUID.randomUUID().toString()));
    }
}
