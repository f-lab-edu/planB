package com.flab.planb.common;

import org.springframework.context.MessageSource;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class Common {

  public static final String ENCODIG = StandardCharsets.UTF_8.toString();
  public static final Locale LOCALE = Locale.getDefault();

  private Common() {
  }

  public static String getMessage(String messageKey) {
    return getMessageSource().getMessage(messageKey, null, "", Common.LOCALE);
  }

  public static String getMessage(String messageKey, Object[] args) {
    return getMessageSource().getMessage(messageKey, args, "", Common.LOCALE);
  }

  private static MessageSource getMessageSource() {
    return (MessageSource) ApplicationContextProvider.getApplicationContext()
        .getBean("messageSource");
  }

}
