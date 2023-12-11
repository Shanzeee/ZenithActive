package com.brvsk.ZenithActive.payment.discount;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "DiscountCode")
@Table(name = "discount_code")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountCode {

    @Id
    @Column(length = 31)
    private String code;
    @Column(nullable = false)
    private Integer discountPercentage;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;
}
