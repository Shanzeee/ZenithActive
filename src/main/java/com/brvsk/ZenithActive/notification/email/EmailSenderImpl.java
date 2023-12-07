package com.brvsk.ZenithActive.notification.email;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.course.CourseRepository;
import com.brvsk.ZenithActive.instructor.Instructor;
import com.brvsk.ZenithActive.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSender{

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendEnrollmentConfirmation(Member member, Course course){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(member.getEmail());
        message.setSubject("Enrollment Confirmation");
        message.setText("Dear " + member.getFirstName() + ",\n\n"
                + "You have successfully enrolled in the course '" + course.getName() + "'.\n\n"
                + "Thank you for choosing our platform.\n\n"
                + "Best regards,\nZenith Active ;)");

        javaMailSender.send(message);
    }

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
    }

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
    }

}
