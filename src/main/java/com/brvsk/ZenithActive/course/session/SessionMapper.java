package com.brvsk.ZenithActive.course.session;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class SessionMapper {

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

    public SessionResponse mapToResponse (Session session){
        String formattedStartTime = session.getStartTime().format(timeFormatter);
        String formattedEndTime = session.getEndTime().format(timeFormatter);
        String formattedDuration = formattedStartTime + " - " + formattedEndTime;

        return SessionResponse.builder()
                .courseId(session.getCourse().getId())
                .courseType(session.getCourse().getCourseType())
                .courseName(session.getCourse().getName())
                .courseDescription(session.getCourse().getDescription())
                .sessionId(session.getId())
                .facilityName(session.getFacility().getName())
                .instructorId(session.getInstructor().getUserId())
                .instructorName(session.getInstructor().getFirstName()+" "+session.getInstructor().getLastName())
                .groupSize(session.getGroupSize())
                .localDate(session.getLocalDate())
                .courseDuration(formattedDuration)
                .build();
    }
}
