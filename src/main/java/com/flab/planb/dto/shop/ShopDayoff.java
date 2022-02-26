package com.flab.planb.dto.shop;

import com.flab.planb.type.Day;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ShopDayoff {

    @Positive(message = "가게 ID는 양수여야 합니다.")
    private long shopId;
    @Min(value = 1, message = "휴일 최솟값은 1(월요일) 입니다.")
    @Max(value = 7, message = "휴일 최댓값은 7(일요일) 입니다.")
    private Day day;

    public ShopDayoff(long shopId, Day day) {
        this.shopId = shopId;
        this.day = day;
    }

}
