package com.brvsk.ZenithActive.notification.email;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.notification.newsletter.NewsletterSubscriber;

import java.util.List;

public interface EmailSender {

    void sendEnrollmentConfirmation(Member member, Course course);
    void sendTrainingPlanRequestConfirmation(Member member);
    void sendTrainingPlanConfirmation(Member member, Instructor instructor);
    void sendWeeklyNewsletter(NewsletterSubscriber newsletterSubscriber, List<String> newsletterContents);
    void sendNewsletterConfirmationEmail(String firstName, String email);
}
