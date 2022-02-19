package com.flab.planb.dto.shop;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ShopInfo {

    private long id;
    @NotBlank(message = "배달지역은 Null과 공백이 아닌 값이 들어가야 합니다.")
    private String deliveryArea;
    @Pattern(regexp = "[0-9]{2}", message = "최소조리시간은 2자리 숫자여야 합니다.")
    private String minCookingTime;
    @Pattern(regexp = "[0-9]{2}", message = "최대조리시간은 2자리 숫자여야 합니다.")
    private String maxCookingTime;
    @Positive(message = "배달팁은 양수여야 합니다.")
    private int tip;
    private String introduction;

    public ShopInfo(long id, String deliveryArea, String minCookingTime,
                    String maxCookingTime, int tip, String introduction) {
        this.id = id;
        this.deliveryArea = deliveryArea;
        this.minCookingTime = minCookingTime;
        this.maxCookingTime = maxCookingTime;
        this.tip = tip;
        this.introduction = introduction;
    }

}
