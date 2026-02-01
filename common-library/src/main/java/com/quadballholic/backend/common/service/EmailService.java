package com.quadballholic.backend.common.service;

import java.time.LocalDate;

public interface EmailService {

    void sendAccountActivationEmail(String userMail, String token);
    void sendPasswordResetEmail(String email,String token);
    void sendReservationConfirmEmail(String userMail, LocalDate matchDateTime);
    void sendTextEmail(String to, String subject, String body);
}
