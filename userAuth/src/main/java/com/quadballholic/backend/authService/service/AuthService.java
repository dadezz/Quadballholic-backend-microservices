package com.quadballholic.backend.authService.service;

import com.quadballholic.backend.authService.dto.ResetPasswordRequest;
import com.quadballholic.backend.authService.dto.SignInRequest;
import com.quadballholic.backend.authService.dto.SignInResponse;
import com.quadballholic.backend.authService.dto.SignUpRequest;

public interface AuthService {

    SignInResponse signIn(SignInRequest signInRequest);
    Long signUp(SignUpRequest signUpRequest);
    void activateUser(String token);
    void requestPasswordReset(String email);
    void completePasswordReset(ResetPasswordRequest resetPasswordRequest);
}
