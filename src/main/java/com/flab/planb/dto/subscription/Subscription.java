package com.flab.planb.dto.subscription;

import com.flab.planb.annotation.TimePattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;


@NoArgsConstructor
@Getter
@ToString
public class Subscription {

    @Setter
    private long id;
    private long memberId;
    @Positive(message = "가게 ID는 양수여야 합니다.")
    private long shopId;
    @Positive(message = "회원배달주소 ID는 양수여야 합니다.")
    private long addressId;
    @Min(value = 1, message = "구독요일 최솟값은 1(월요일) 입니다.")
    @Max(value = 7, message = "구독요일 최댓값은 7(일요일) 입니다.")
    private int subscriptionDay;
    @TimePattern
    private LocalTime subscriptionTime;

    public Subscription(long memberId, long shopId, long addressId,
                        int subscriptionDay, LocalTime subscriptionTime) {
        this.memberId = memberId;
        this.shopId = shopId;
        this.addressId = addressId;
        this.subscriptionDay = subscriptionDay;
        this.subscriptionTime = subscriptionTime;
    }

}