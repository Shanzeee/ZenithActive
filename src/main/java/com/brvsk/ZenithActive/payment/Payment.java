package com.brvsk.ZenithActive.payment;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Payment")
@Table(name = "payment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String paymentNumber;
    private Double value;
    private DiscountType discountType;

    private ProductType productType;
    private UUID productId;

    @CreationTimestamp
    private LocalDateTime cratedAt;
}
