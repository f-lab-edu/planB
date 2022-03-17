package com.flab.planb.dto.push;


import com.flab.planb.type.Day;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Map;

@NoArgsConstructor
@Getter
@ToString
public class PushInfo {

    private String fcmToken;
    private long memberId;
    private Day subscriptionDay;
    private String shopName;
    private String subscriptionTime;

    public PushInfo(String fcmToken, long memberId, Day subscriptionDay, String shopName, String subscriptionTime) {
        this.fcmToken = fcmToken;
        this.memberId = memberId;
        this.subscriptionDay = subscriptionDay;
        this.shopName = shopName;
        this.subscriptionTime = subscriptionTime;
    }

    public Map<String, String> createData(String title) {
        return Map.of("title", title,
                      "shopName", this.shopName,
                      "subscriptionDay", this.subscriptionDay.name(),
                      "subscriptionTime", this.subscriptionTime);
    }

}
