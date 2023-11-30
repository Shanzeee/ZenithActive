package com.brvsk.ZenithActive.qrcode;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QrCode {

    private UUID userId;
    private LocalDateTime expirationDateTime;

}
