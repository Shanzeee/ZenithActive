package com.brvsk.ZenithActive.payment.discount;

import java.util.List;
import java.util.Optional;

public interface DiscountCodeService {
    DiscountCode createDiscountCode(DiscountCodeCreateRequest request);

    void deleteDiscountCode(String code);

    Optional<DiscountCode> getDiscountCode(String code);

    Integer calculateDiscountPercentage(String code);

    List<DiscountCode> getAllDiscountCodes();
}
