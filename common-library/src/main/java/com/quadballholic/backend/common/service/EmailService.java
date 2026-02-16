package com.quadballholic.backend.common.service;

public interface EmailService {

    void sendAccountActivationEmail(String userMail, String token);
    void sendPasswordResetEmail(String email,String token);
    void sendReservationConfirmEmail(String userMail, String matchDateTime);
    void sendTextEmail(String to, String subject, String body);
}
