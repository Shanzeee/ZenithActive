package com.brvsk.ZenithActive.payment.discount;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DiscountCodeCreateRequest {

    @NotBlank(message = "Code cannot be blank")
    private String code;

    @NotNull(message = "Discount percentage cannot be null")
    @Min(value = 0, message = "Discount percentage cannot be negative")
    @Max(value = 100, message = "Discount percentage cannot be greater than 100")
    private Integer discountPercentage;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be in the present or future")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;
}
