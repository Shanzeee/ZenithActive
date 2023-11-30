package com.brvsk.ZenithActive.qrcode;

import com.brvsk.ZenithActive.member.Member;
import com.brvsk.ZenithActive.member.MemberRepository;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QrCodeServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private QrCodeServiceImpl qrCodeService;

    @Test
    void saveQrCode_ShouldSaveQrCodeToMember() {
        // Given
        UUID userId = UUID.randomUUID();
        Member member = new Member();
        member.setUserId(userId);

        when(memberRepository.findById(userId)).thenReturn(Optional.of(member));

        // When
        qrCodeService.saveQrCode(userId);

        // Then
        verify(memberRepository, times(1)).save(member);
        assertNotNull(member.getQrCode());
    }

    @Test
    void saveQrCode_ShouldThrowUserNotFoundException_WhenUserNotExists() {
        // Given
        UUID userId = UUID.randomUUID();

        when(memberRepository.findById(userId)).thenReturn(Optional.empty());

        // When/Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> qrCodeService.saveQrCode(userId));
        assertEquals("User with id " + userId + " not found", exception.getMessage());
        verify(memberRepository, never()).save(any());
    }

}