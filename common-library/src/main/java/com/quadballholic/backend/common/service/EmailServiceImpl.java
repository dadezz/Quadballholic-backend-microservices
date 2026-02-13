package com.quadballholic.backend.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.mail.host")
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${common.frontend.url}")
    private String frontendUrl;

    public void sendAccountActivationEmail(String userMail, String token) {
        String subject = "Activate your Quadballholic Account";
        String link = frontendUrl + "/activate-account?token=" + token;

        String body = "Hello,\n\n" +
                "Thank you for signing up to our project. Click the link below to activate your account:\n\n" +
                link + "\n\n" +
                "If you did not request this, you can safely ignore this email.\n" +
                "This link will expire in 15 minutes.";

        sendTextEmail(userMail, subject, body);
    }

    public void sendPasswordResetEmail(String userMail,String token) {
        String subject = "Reset your Quadballholic Password";
        String link = frontendUrl + "/reset-password?token=" + token;

        String body = "Hello,\n\n" +
                "We received a request to reset your password. Click the link below to choose a new one:\n\n" +
                link + "\n\n" +
                "If you did not request this, you can safely ignore this email.\n" +
                "This link will expire in 15 minutes.";

        sendTextEmail(userMail, subject, body);
    }

    public void sendReservationConfirmEmail(String userMail, LocalDate matchDateTime) {
        String subject = "Confirm your reservation";

        String body = "Hello,\n\n" +
                "Your reservation for the upcoming match has been successfully confirmed!\n\n" +
                "Match Details:\n" +
                "Date & Time: " + matchDateTime + "\n\n" +
                "Important Information:\n" +
                "Please note that payment must be settled on-site at the stadium entrance " +
                "before the match starts. We recommend arriving at least 15 minutes early " +
                "to complete the transaction and secure your seat.\n\n" +
                "Thank you for being part of the Quadballholic community. Enjoy the game!";

        sendTextEmail(userMail, subject, body);
    }

    @Override
    @Async
    public void sendTextEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
