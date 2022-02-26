package com.flab.planb.dto.subscription;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class PushInfo {

    private long memberId;
    private String shopName;
    private String subscriptionTime;

    public PushInfo(long memberId, String shopName, String subscriptionTime) {
        this.memberId = memberId;
        this.shopName = shopName;
        this.subscriptionTime = subscriptionTime;
    }

}
