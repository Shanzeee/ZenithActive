package com.brvsk.ZenithActive.qrcode;

import java.awt.image.BufferedImage;
import java.util.UUID;

public interface QrCodeService {
    void saveQrCode(UUID userId);
    BufferedImage generateQrCodeImage(QrCode data);
}
