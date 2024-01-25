package com.brvsk.ZenithActive.notification.email;

import com.brvsk.ZenithActive.session.Session;
import com.brvsk.ZenithActive.notification.newsletter.NewsletterSubscriber;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.member.Member;
import org.springframework.scheduling.annotation.Async;

import java.time.YearMonth;
import java.util.List;

public interface EmailSender {

    @Async
    void sendEnrollmentConfirmation(Member member, Session session);
    @Async
    void sendTrainingPlanRequestConfirmation(Member member);
    @Async
    void sendTrainingPlanConfirmation(Member member, Instructor instructor);
    @Async
    void sendWeeklyNewsletter(NewsletterSubscriber newsletterSubscriber, List<String> newsletterContents);
    @Async
    void sendNewsletterConfirmationEmail(String firstName, String email);
    @Async
    void sendPurchaseConfirmedEmail(String firstName, String email, String purchaseNumber);
    @Async
    void sendLoyaltyPointsThresholdEmail(String firstName, String email);
    @Async
    void sendNewWorkScheduleNotification(String employeeEmail, String employeeName, YearMonth yearMonth);
    @Async
    void sendUpdatedWorkScheduleNotification(String employeeEmail, String employeeName, YearMonth yearMonth);
}
