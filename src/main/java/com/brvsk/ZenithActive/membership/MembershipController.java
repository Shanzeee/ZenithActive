package com.brvsk.ZenithActive.membership;

import com.brvsk.ZenithActive.user.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/membership")
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/create")
    public ResponseEntity<String> addNewMembershipToMember (@RequestBody @Valid MembershipRequest request){
        try {
            membershipService.addNewMembershipToMember(request);
            return ResponseEntity.ok("membership created");
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
