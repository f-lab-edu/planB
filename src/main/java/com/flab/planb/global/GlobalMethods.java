package com.flab.planb.global;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class GlobalMethods {

    public static String getDefaultEncToString() {
        return "UTF-8";
    }

    public static Charset getDefaultEncToCharacter() {
        return StandardCharsets.UTF_8;
    }
}
