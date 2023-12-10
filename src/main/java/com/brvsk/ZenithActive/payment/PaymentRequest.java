package com.brvsk.ZenithActive.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class PaymentRequest {
    private Double value;
    private DiscountType discountType;
    private String fullName;
    private ProductType productType;
    private UUID productId;
}
