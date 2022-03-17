package com.flab.planb.type;

import lombok.Getter;

@Getter
public enum Result {
    ERROR("error"), SUCCEED("succeed");

    private final String str;

    Result(String str) {
        this.str = str;
    }

    public String getName() {
        return this.name();
    }

}
