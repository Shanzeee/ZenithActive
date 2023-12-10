package com.brvsk.ZenithActive.payment.discount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountCodeRepository extends JpaRepository<DiscountCode, String> {

}
