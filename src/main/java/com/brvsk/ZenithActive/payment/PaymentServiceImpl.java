package com.brvsk.ZenithActive.payment;

import com.brvsk.ZenithActive.notification.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final EmailSender emailSender;

    @Override
    public void crateNewPayment(PaymentRequest request){
        Payment payment = toEntity(request);
        paymentRepository.save(payment);

        emailSender.sendPurchaseConfirmedEmail(request.getFullName(), request.getEmail(), payment.getPaymentNumber());
    }

    private Payment toEntity(PaymentRequest request) {
        return Payment.builder()
                .paymentNumber(returnFakePaymentNumber())
                .value(request.getValue())
                .discountType(request.getDiscountType())
                .productType(request.getProductType())
                .productId(request.getProductId())
                .build();
    }



    private String returnFakePaymentNumber () {
        return UUID.randomUUID().toString();
    }
}
