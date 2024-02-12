package com.brvsk.ZenithActive.qrcode;

import com.brvsk.ZenithActive.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/qrcode")
@RequiredArgsConstructor
public class QrCodeController {

    private final QrCodeService qrCodeService;

    @GetMapping(value = "/generate/{userId}", produces = MediaType.IMAGE_PNG_VALUE)
    @PreAuthorize("hasRole('MEMBER') or hasRole('EMPLOYEE')")
    public ResponseEntity<byte[]> generateQrCode(@PathVariable UUID userId) {
        try {
            BufferedImage qrCodeImage = qrCodeService.generateQrCodeImage(userId);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrCodeImage, "png", baos);

            byte[] imageBytes = baos.toByteArray();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
