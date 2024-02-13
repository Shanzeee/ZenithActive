package com.brvsk.ZenithActive.qrcode;

import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import com.brvsk.ZenithActive.membership.Membership;
import com.brvsk.ZenithActive.membership.MembershipRepository;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService{

    private final MemberRepository memberRepository;
    private final MembershipRepository membershipRepository;
    @Override
    public BufferedImage generateQrCodeImage(UUID userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Membership latestMembership = getLatestActiveMembership(userId);

        QrCode data = buildQrCodeData(member, latestMembership);
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

    private Membership getLatestActiveMembership(UUID userId) {
        return membershipRepository.findByMember_UserIdAndEndDateAfterOrderByEndDateDesc(userId, LocalDate.now())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No active membership found"));
    }

    private QrCode buildQrCodeData(Member member, Membership latestMembership) {
        return QrCode.builder()
                .userId(member.getUserId())
                .membershipType(latestMembership.getMembershipType())
                .expirationDateTime(LocalDateTime.now().plusMinutes(15))
                .build();
    }


}
