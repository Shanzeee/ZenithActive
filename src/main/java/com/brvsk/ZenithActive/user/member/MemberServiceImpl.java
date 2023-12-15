package com.brvsk.ZenithActive.user.member;

import com.brvsk.ZenithActive.course.CourseMapper;
import com.brvsk.ZenithActive.course.CourseResponse;
import com.brvsk.ZenithActive.loyalty.LoyaltyPoints;
import com.brvsk.ZenithActive.notification.newsletter.NewsletterService;
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
    private final NewsletterService newsletterService;

    @Override
    public void createMember(MemberCreateRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        handleNewsletterSubscription(request);

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

//    @Override
//    public int calculateTotalLoyaltyPoints(Member member) {
//        Set<LoyaltyPoints> loyaltyPoints = member.getLoyaltyPoints();
//            return loyaltyPoints.stream()
//                    .filter(Objects::nonNull)
//                    .mapToInt(LoyaltyPoints::getPointsAmount)
//                    .sum();
//    }

    @Override
    public int calculateTotalLoyaltyPoints(Member member) {
        Set<LoyaltyPoints> loyaltyPoints = member.getLoyaltyPoints();
        int totalPoints = 0;

        if (loyaltyPoints != null) {
            for (LoyaltyPoints loyaltyPoint : loyaltyPoints) {
                if (loyaltyPoint != null) {
                    Integer pointsAmount = loyaltyPoint.getPointsAmount();
                    if (pointsAmount != null) {
                        totalPoints += pointsAmount;
                    }
                }
            }
        }

        return totalPoints;
    }


    private Member toEntity(MemberCreateRequest request){
        Member member = new Member();
        member.setUserId(UUID.randomUUID());
        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        member.setGender(request.getGender());
        member.setHeight(request.getHeight());
        member.setWeight(request.getWeight());
        member.setEmail(request.getEmail());
        return member;
    }

    private void handleNewsletterSubscription(MemberCreateRequest request){
        if (request.isEmailNewsletter()) {
            newsletterService.subscribeNewsletter(request.getFirstName(), request.getEmail());
        }
    }
}
