package com.brvsk.ZenithActive.qrcode;

import java.awt.image.BufferedImage;
import java.util.UUID;

public interface QrCodeService {
    BufferedImage generateQrCodeImage(UUID userId);
}
