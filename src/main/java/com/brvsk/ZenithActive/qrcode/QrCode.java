package com.brvsk.ZenithActive.qrcode;


import com.brvsk.ZenithActive.membership.MembershipType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class QrCode {

    private UUID userId;
    private MembershipType membershipType;
    private LocalDateTime expirationDateTime;
    private boolean isUsed;

    @Override
    public String toString() {
        return "QrCode{" +
                "userId=" + userId +
                ", membershipType=" + membershipType +
                ", expirationDateTime=" + expirationDateTime +
                ", isUsed=" + isUsed +
                '}';
    }
}
