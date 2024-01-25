package com.brvsk.ZenithActive.qrcode;

import com.brvsk.ZenithActive.user.UserNotFoundException;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService{

    private final MemberRepository memberRepository;
    @Override
    public BufferedImage generateQrCodeImage(UUID userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        QrCode data = buildQrCodeData(member);
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(data.toString(), BarcodeFormat.QR_CODE, 200, 200, hints);

            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            System.err.println("Error generating QR code: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return null;
        }
    }

    private QrCode buildQrCodeData(Member member){
        return QrCode.builder()
                .userId(member.getUserId())
                .membershipType(member.getMembership().getMembershipType())
                .expirationDateTime(LocalDateTime.now().plusMinutes(15))
                .build();
    }


}
