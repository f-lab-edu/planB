package com.flab.planb.dto.subscription;


import com.flab.planb.type.Day;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class PushInfo {

    private long memberId;
    private Day subscriptionDay;
    private String shopName;
    private String subscriptionTime;

    public PushInfo(long memberId, Day subscriptionDay, String shopName, String subscriptionTime) {
        this.memberId = memberId;
        this.subscriptionDay = subscriptionDay;
        this.shopName = shopName;
        this.subscriptionTime = subscriptionTime;
    }

}
