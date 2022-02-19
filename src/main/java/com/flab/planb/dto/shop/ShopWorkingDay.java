package com.flab.planb.dto.shop;

import com.flab.planb.type.Day;
import com.flab.planb.annotation.TimePattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ShopWorkingDay {

    @Positive(message = "가게 ID는 양수여야 합니다.")
    private long shopId;
    @Min(value = 0, message = "휴일 최솟값은 0(일요일) 입니다.")
    @Max(value = 6, message = "휴일 최댓값은 6(토요일) 입니다.")
    private Day day;
    @TimePattern
    private LocalTime operationStartTime;
    @TimePattern
    private LocalTime operationEndTime;

    public ShopWorkingDay(long shopId, Day day, LocalTime operationStartTime, LocalTime operationEndTime) {
        this.shopId = shopId;
        this.day = day;
        this.operationStartTime = operationStartTime;
        this.operationEndTime = operationEndTime;
    }

}
