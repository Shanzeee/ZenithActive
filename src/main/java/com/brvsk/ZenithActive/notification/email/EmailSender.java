package com.brvsk.ZenithActive.notification.email;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.notification.newsletter.NewsletterSubscriber;

import java.time.YearMonth;
import java.util.List;

public interface EmailSender {

    void sendEnrollmentConfirmation(Member member, Course course);
    void sendTrainingPlanRequestConfirmation(Member member);
    void sendTrainingPlanConfirmation(Member member, Instructor instructor);
    void sendWeeklyNewsletter(NewsletterSubscriber newsletterSubscriber, List<String> newsletterContents);
    void sendNewsletterConfirmationEmail(String firstName, String email);
    void sendPurchaseConfirmedEmail(String firstName, String email, String purchaseNumber);
    void sendLoyaltyPointsThresholdEmail(String firstName, String email);
    void sendNewWorkScheduleNotification(String employeeEmail, String employeeName, YearMonth yearMonth);
    void sendUpdatedWorkScheduleNotification(String employeeEmail, String employeeName, YearMonth yearMonth);
}
