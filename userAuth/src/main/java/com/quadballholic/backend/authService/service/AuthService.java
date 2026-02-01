package com.quadballholic.backend.authService.service;

import com.quadballholic.backend.authService.api.ResetPasswordRequest;
import com.quadballholic.backend.authService.api.SignInRequest;
import com.quadballholic.backend.authService.api.SignInResponse;
import com.quadballholic.backend.authService.api.SignUpRequest;

public interface AuthService {

    SignInResponse signIn(SignInRequest signInRequest);
    Long signUp(SignUpRequest signUpRequest);
    void activateUser(String token);
    void requestPasswordReset(String email);
    void completePasswordReset(ResetPasswordRequest resetPasswordRequest);
}
