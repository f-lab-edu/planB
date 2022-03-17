package com.flab.planb.type;

import lombok.Getter;
import org.apache.ibatis.type.TypeException;
import java.util.Arrays;

public enum Day {
    SUN(1), MON(2), TUE(3), WED(4), THU(5), FRI(6), SAT(7);

    @Getter
    private final int code;

    Day(int code) {
        this.code = code;
    }

    public static Day getDay(int input) {
        return Arrays.stream(values()).filter(v -> v.getCode() == input).findFirst().orElseThrow(TypeException::new);
    }

}
