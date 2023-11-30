package com.brvsk.ZenithActive.qrcode;

import com.brvsk.ZenithActive.member.Member;
import com.brvsk.ZenithActive.member.MemberRepository;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService{

    private final MemberRepository memberRepository;

    @Override
    public void saveQrCode(UUID userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        QrCode qrCode = new QrCode();
        qrCode.setUserId(userId);
        qrCode.setExpirationDateTime(LocalDateTime.now().plusMinutes(180L));
        String qrCodeString = generateQrCode(qrCode);
        member.setQrCode(qrCodeString);

        memberRepository.save(member);
    }
    private String generateQrCode(QrCode data) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(data.toString(), BarcodeFormat.QR_CODE, 200, 200, hints);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            byte[] qrCodeBytes = outputStream.toByteArray();

            return Base64.getEncoder().encodeToString(qrCodeBytes);
        } catch (WriterException e) {
            System.err.println("Error generating QR code: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return null;
        }
    }


}
