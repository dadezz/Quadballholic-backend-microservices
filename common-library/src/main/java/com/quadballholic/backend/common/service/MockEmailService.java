package com.quadballholic.backend.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(name = "spring.mail.username", havingValue = "false", matchIfMissing = true)
public class MockEmailService implements EmailService {
    @Override
    public void sendAccountActivationEmail(String userMail, String token) {
        log.info("sendAccountActivationEmail Mock Email due to malformed environment file");
    }

    @Override
    public void sendAccountCreatedEmail(String userMail, String token) {
        log.info("sendAccountCreatedEmail Mock Email due to malformed environment file");
    }
    @Override
    public void sendPasswordResetEmail(String userMail,String token) {
        log.info("sendPasswordResetEmail Mock Email due to malformed environment file");
    }
    @Override
    public void sendReservationConfirmEmail(String userMail, String matchDateTime) {
        log.info("sendReservationConfirmEmail Mock Email due to malformed environment file");
    }

    @Override
    @Async
    public void sendTextEmail(String to, String subject, String body) {
        log.info("sendTextEmail Mock Email due to malformed environment file");
    }
}