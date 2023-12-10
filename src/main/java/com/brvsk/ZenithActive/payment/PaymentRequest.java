package com.brvsk.ZenithActive.payment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class PaymentRequest {

    @NotNull(message = "Value cannot be null")
    @Positive(message = "Value must be positive")
    private Double value;

    @NotNull(message = "Discount type cannot be null")
    private DiscountType discountType;

    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @NotNull(message = "Product type cannot be null")
    private ProductType productType;

    @NotNull(message = "Product ID cannot be null")
    private UUID productId;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;
}
