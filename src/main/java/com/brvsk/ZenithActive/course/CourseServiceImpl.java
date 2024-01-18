package com.brvsk.ZenithActive.course;

import com.brvsk.ZenithActive.facility.Facility;
import com.brvsk.ZenithActive.facility.FacilityNotFoundException;
import com.brvsk.ZenithActive.facility.FacilityRepository;
import com.brvsk.ZenithActive.notification.email.EmailSender;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.instructor.InstructorRepository;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.user.member.MemberMapper;
import com.brvsk.ZenithActive.user.member.MemberRepository;
import com.brvsk.ZenithActive.user.member.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final FacilityRepository facilityRepository;
    private final CourseMapper courseMapper;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final EmailSender emailSender;

    @Override
    public void createNewCourse(CourseCreateRequest request){
        Instructor instructor = getInstructor(request.getInstructorId());
        Facility facility = getFacility(request.getFacilityId());


        validateCourseHours(request.getLocalDate(), request.getStartTime(), request.getEndTime(), facility);
        validateInstructorAvailability(request.getLocalDate(), request.getStartTime(), request.getEndTime(), instructor);

        Course courseToAdd = toEntity(request);
        courseToAdd.setFacility(facility);
        courseToAdd.setInstructor(instructor);
        courseRepository.save(courseToAdd);
    }

    @Override
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> getCoursesForCourseType(CourseType courseType) {
        return courseRepository.getCoursesByCourseType(courseType)
                .stream()
                .map(courseMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> getCoursesForInstructor(UUID instructorId) {
        return courseRepository.getCoursesByInstructor_UserId(instructorId)
                .stream()
                .map(courseMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Set<CourseResponse> getCoursesForMember(UUID userId){
        Member member = getMember(userId);

        return member.getEnrolledCourses()
                .stream()
                .map(courseMapper::mapToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void enrolMemberToCourse(UUID courseId, UUID userId){
        Course course = getCourse(courseId);
        Member member = getMember(userId);

        course.getEnrolledMembers().add(member);
        member.getEnrolledCourses().add(course);

        courseRepository.save(course);
        memberRepository.save(member);

        emailSender.sendEnrollmentConfirmation(member, course);
    }

    @Override
    public Set<MemberResponse> getMembersForCourse(UUID courseId){
        Course course = getCourse(courseId);

        return course.getEnrolledMembers()
                .stream()
                .map(memberMapper::toMemberResponse)
                .collect(Collectors.toSet());
    }

    private Instructor getInstructor(UUID instructorId) {
        return instructorRepository.findById(instructorId)
                .orElseThrow(() -> new UserNotFoundException(instructorId));
    }

    private Facility getFacility(UUID facilityId) {
        return facilityRepository.findById(facilityId)
                .orElseThrow(() -> new FacilityNotFoundException(facilityId));
    }

    private Member getMember(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException(memberId));
    }

    private Course getCourse(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }

    private void validateCourseHours(LocalDate localDate, LocalTime startTime, LocalTime endTime, Facility facility) {
        List<Course> overlappingCourses = courseRepository.findOverlappingCourses(localDate, startTime, endTime, facility);

        if (!overlappingCourses.isEmpty()) {
            throw new IllegalArgumentException("The course hours overlap with existing courses.");
        }
    }

    private void validateInstructorAvailability(LocalDate localDate, LocalTime startTime, LocalTime endTime, Instructor instructor) {
        List<Course> overlappingCourses = courseRepository.findOverlappingInstructorCourses(localDate, startTime, endTime, instructor);

        if (!overlappingCourses.isEmpty()) {
            throw new IllegalArgumentException("The instructor is not available during the specified hours.");
        }
    }

    private Course toEntity(CourseCreateRequest request){
        return Course.builder()
                .courseType(request.getCourseType())
                .name(request.getName())
                .description(request.getDescription())
                .groupSize(request.getGroupSize())
                .localDate(request.getLocalDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
    }
}
