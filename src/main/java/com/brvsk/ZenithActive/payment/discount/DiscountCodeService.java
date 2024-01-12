package com.brvsk.ZenithActive.payment.discount;

import java.util.List;

public interface DiscountCodeService {
    DiscountCode createDiscountCode(DiscountCodeCreateRequest request);

    void deleteDiscountCode(String code);

    Integer calculateDiscountPercentage(String code);

    List<DiscountCode> getAllDiscountCodes();
}
