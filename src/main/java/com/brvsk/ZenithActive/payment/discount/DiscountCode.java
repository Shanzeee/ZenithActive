package com.brvsk.ZenithActive.payment.discount;

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
    private String code;
    private Integer discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
}
