package com.brvsk.ZenithActive.payment.discount;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DiscountCodeNotFoundException extends RuntimeException {

    public DiscountCodeNotFoundException(String code) {
        super("Discount code not found: " + code);
    }
}