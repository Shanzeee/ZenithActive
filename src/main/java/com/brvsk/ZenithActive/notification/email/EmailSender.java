package com.brvsk.ZenithActive.notification.email;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.instructor.Instructor;
import com.brvsk.ZenithActive.member.Member;

public interface EmailSender {

    void sendEnrollmentConfirmation(Member member, Course course);

    void sendTrainingPlanRequestConfirmation(Member member);

    void sendTrainingPlanConfirmation(Member member, Instructor instructor);
}
