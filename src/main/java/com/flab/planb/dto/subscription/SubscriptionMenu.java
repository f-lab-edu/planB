package com.flab.planb.dto.subscription;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class SubscriptionMenu {

    @Positive(message = "구독정보 ID는 양수여야 합니다.")
    private long subscriptionId;
    @Positive(message = "메뉴 ID는 양수여야 합니다.")
    private long menuId;
    private List<Long> menuOptions;

    public SubscriptionMenu(long subscriptionId, long menuId, List<Long> menuOptions) {
        this.subscriptionId = subscriptionId;
        this.menuId = menuId;
        this.menuOptions = menuOptions;
    }

}
