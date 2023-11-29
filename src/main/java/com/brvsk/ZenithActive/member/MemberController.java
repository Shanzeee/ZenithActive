package com.brvsk.ZenithActive.member;

import com.brvsk.ZenithActive.course.CourseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<String> createMember(@RequestBody @Valid MemberCreateRequest request) {
        memberService.createMember(request);
        return ResponseEntity.ok("Member created successfully");
    }

    @GetMapping("/{userId}/courses")
    public ResponseEntity<Set<CourseResponse>> getCoursesForMember(@PathVariable UUID userId) {
        Set<CourseResponse> courses = memberService.getCoursesForMember(userId);
        return ResponseEntity.ok(courses);
    }
}
