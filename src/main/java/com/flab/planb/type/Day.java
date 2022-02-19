package com.flab.planb.type;

import lombok.Getter;
import org.apache.ibatis.type.TypeException;
import java.util.Arrays;

public enum Day {
    SUN(0), MON(1), TUE(2), WED(3), THU(4), FRI(5), SAT(6);

    @Getter
    private final int code;

    Day(int code) {
        this.code = code;
    }

    public static Day getDay(int input) {
        return Arrays.stream(values()).filter(v -> v.getCode() == input).findFirst().orElseThrow(TypeException::new);
    }

}
