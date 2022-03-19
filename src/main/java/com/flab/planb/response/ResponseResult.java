package com.flab.planb.response;

import lombok.Getter;

@Getter
public enum ResponseResult {
    ERROR("error"), SUCCEED("succeed");

    private final String str;

    ResponseResult(String str) {
        this.str = str;
    }

    public String getName() {
        return this.name();
    }

}
