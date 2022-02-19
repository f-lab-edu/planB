package com.flab.planb.dto.subscription.request;

import com.flab.planb.dto.subscription.Subscription;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@ToString
public class SubscriptionRequest extends Subscription {

    @NotNull(message = "구독 메뉴는 null일 수 없습니다.")
    @Size(min = 1, message = "구독 메뉴는 1개 이상 있어야 합니다.")
    private Map<Long, List<Long>> subscriptionMenus;

    public SubscriptionRequest(long memberId, long shopId, long addressId, int subscriptionDay,
                               LocalTime subscriptionTime, Map<Long, List<Long>> subscriptionMenus) {
        super(memberId, shopId, addressId, subscriptionDay, subscriptionTime);
        this.subscriptionMenus = subscriptionMenus;
    }

}