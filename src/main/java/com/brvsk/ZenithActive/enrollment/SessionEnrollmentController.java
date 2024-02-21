package com.brvsk.ZenithActive.enrollment;


import com.brvsk.ZenithActive.session.SessionResponse;
import com.brvsk.ZenithActive.user.member.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/session_enrollments")
@RequiredArgsConstructor
public class SessionEnrollmentController {

    private final SessionEnrollmentService sessionEnrollmentService;

    @PostMapping("/enroll")
    @PreAuthorize("hasRole('MEMBER') or hasRole('EMPLOYEE')")
    public ResponseEntity<String> enrollMemberToSession(@RequestParam UUID sessionId, @RequestParam UUID memberId) {
        sessionEnrollmentService.enrollMemberToSession(sessionId, memberId);
        return ResponseEntity.ok("Member enrolled to session successfully");
    }

    @GetMapping("/byMemberId/{userId}")
    public ResponseEntity<Set<SessionResponse>> getSessionsForMember(@PathVariable UUID userId) {
        try {
            Set<SessionResponse> sessions = sessionEnrollmentService.getSessionsForMember(userId);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{sessionId}/members")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<Set<MemberResponse>> getMembersForSession(@PathVariable UUID sessionId) {
        try {
            Set<MemberResponse> members = sessionEnrollmentService.getMembersForSession(sessionId);
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
