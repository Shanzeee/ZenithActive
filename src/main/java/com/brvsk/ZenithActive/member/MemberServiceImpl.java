package com.brvsk.ZenithActive.member;

import com.brvsk.ZenithActive.course.CourseMapper;
import com.brvsk.ZenithActive.course.CourseResponse;
import com.brvsk.ZenithActive.user.EmailAlreadyExistsException;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import com.brvsk.ZenithActive.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final CourseMapper courseMapper;
    private final UserRepository userRepository;

    @Override
    public void createMember(MemberCreateRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        Member member = toEntity(request);
        memberRepository.save(member);
    }

    @Override
    public Set<CourseResponse> getCoursesForMember(UUID userId){
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return member.getEnrolledCourses()
                .stream()
                .map(courseMapper::mapToResponse)
                .collect(Collectors.toSet());
    }

    private Member toEntity(MemberCreateRequest request){
        Member member = new Member();
        member.setUserId(UUID.randomUUID());
        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        member.setGender(request.getGender());
        member.setHeight(request.getHeight());
        member.setWeight(request.getWeight());
        return member;
    }
}
