package com.flab.planb.response.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@RequiredArgsConstructor
@Component
@Slf4j
public class MessageLookup {

    public static final String ENCODIG = StandardCharsets.UTF_8.toString();
    private static final Locale LOCALE = Locale.getDefault();
    private final MessageSource messageSource;

    public String getMessage(String messageKey) {
        return messageSource.getMessage(messageKey, null, "", MessageLookup.LOCALE);
    }

    public String getMessage(String messageKey, Object... args) {
        return messageSource.getMessage(messageKey, args, "", MessageLookup.LOCALE);
    }

}
