package com.brvsk.ZenithActive.notification.email;

import com.brvsk.ZenithActive.session.Session;
import com.brvsk.ZenithActive.notification.newsletter.NewsletterSubscriber;
import com.brvsk.ZenithActive.user.instructor.Instructor;
import com.brvsk.ZenithActive.user.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderImpl implements EmailSender{

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    @Async
    @Override
    public void sendEnrollmentConfirmation(Member member, Session session){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(member.getEmail());
        message.setSubject("Enrollment Confirmation");
        message.setText("Dear " + member.getFirstName() + ",\n\n"
                + "You have successfully enrolled in the course '" + session.getCourse().getName() + "'.\n\n"
                + "Thank you for choosing our platform.\n\n"
                + "Best regards,\nZenith Active ;)");

        javaMailSender.send(message);

        log.info("Enrollment confirmation email sent to: {}", member.getEmail());
    }

    @Async
    @Override
    public void sendTrainingPlanRequestConfirmation(Member member) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(member.getEmail());
        message.setSubject("Training Plan Request Confirmation");
        message.setText("Dear " + member.getFirstName() + ",\n\n"
                + "Your request for a training plan has been received.\n\n"
                + "Our instructor will review your request and create a personalized training plan for you.\n\n"
                + "Thank you for choosing our platform.\n\n"
                + "Best regards,\nZenith Active ;)");

        javaMailSender.send(message);

        log.info("Training plan request confirmation email sent to: {}", member.getEmail());

    }

    @Async
    @Override
    public void sendTrainingPlanConfirmation(Member member, Instructor instructor) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(member.getEmail());
        message.setSubject("Training Plan Confirmation");
        message.setText("Dear " + member.getFirstName() + ",\n\n"
                + "Your personalized training plan has been created by: " +instructor.getFirstName() + " " + instructor.getLastName() + "\n\n"
                + "Follow this plan to achieve your fitness goals.\n\n"
                + "Thank you for choosing our platform.\n\n"
                + "Best regards,\nZenith Active ;)");

        javaMailSender.send(message);

        log.info("Training plan confirmation email sent to: {}", member.getEmail());
    }

    @Async
    @Override
    public void sendWeeklyNewsletter(NewsletterSubscriber subscriber, List<String> newsletterContents) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(subscriber.getEmail());
        message.setSubject("Weekly Newsletter");
        message.setText("Dear " + subscriber.getFirstName() + ",\n\n"
                + "Here is your weekly newsletter:\n\n"
                + String.join("\n", newsletterContents) + "\n\n"
                + "Best regards,\nZenith Active");

        javaMailSender.send(message);

        log.info("Weekly newsletter sent to: {}", subscriber.getEmail());
    }

    @Async
    @Override
    public void sendNewsletterConfirmationEmail(String firstName, String email) {
        String confirmationLink = buildConfirmationLink(email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("Newsletter Subscription Confirmation");
        message.setText("Dear " + firstName + ",\n\n"
                + "Thank you for subscribing to our newsletter. You will receive weekly updates and exciting news!\n\n"
                + "Please confirm your subscription by clicking the following link:\n" + confirmationLink + "\n\n"
                + "Best regards,\nZenith Active");

        javaMailSender.send(message);

        log.info("Newsletter confirmation email sent to: {}", email);
    }

    @Async
    @Override
    public void sendPurchaseConfirmedEmail(String fullName, String email, String purchaseNumber) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("Newsletter Subscription Confirmation");
        message.setText("Dear " + fullName + ",\n\n"
                + "Thank you for your purchase\n"
                + "It is your purchase number: " + purchaseNumber + "\n\n"
                + "Best regards,\nZenith Active");

        javaMailSender.send(message);

        log.info("Purchase confirmation email sent to: {}", email);
    }

    @Async
    @Override
    public void sendLoyaltyPointsThresholdEmail(String firstName, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("Congratulations on Reaching 1000 Loyalty Points!");
        message.setText("Dear " + firstName + ",\n\n"
                + "Congratulations! You have reached 1000 loyalty points. Thank you for your loyalty and dedication.\n\n"
                + "Keep up the good work!\n\n"
                + "Now you can exchange your LP for amazing prizes :D \n\n"
                + "Best regards,\nZenith Active");

        javaMailSender.send(message);

        log.info("Loyalty points threshold email sent to: {}", email);
    }

    @Async
    @Override
    public void sendNewWorkScheduleNotification(String employeeEmail, String employeeName, YearMonth yearMonth) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(employeeEmail);
        message.setSubject("New Work Schedule Notification");
        message.setText("Dear " + employeeName + ",\n\n"
                + "You have received a new work schedule for " + yearMonth.toString() + ".\n\n"
                + "Please log in to our platform to view the details.\n\n"
                + "Best regards,\nZenith Active");

        javaMailSender.send(message);

        log.info("New work schedule notification email sent to: {}", employeeEmail);
    }

    @Async
    @Override
    public void sendUpdatedWorkScheduleNotification(String employeeEmail, String employeeName, YearMonth yearMonth) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(employeeEmail);
        message.setSubject("Updated Work Schedule Notification");
        message.setText("Dear " + employeeName + ",\n\n"
                + "Your work schedule for " + yearMonth.toString() + " has been updated.\n\n"
                + "Please log in to our platform to view the changes.\n\n"
                + "Best regards,\nZenith Active");

        javaMailSender.send(message);

        log.info("Updated work schedule notification email sent to: {}", employeeEmail);
    }

    private String buildConfirmationLink(String email) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String scheme = request.getScheme();
        String host = request.getHeader("Host");

        return scheme + "://" + host + "/api/v1/newsletter/confirm/" + email;
    }

}
