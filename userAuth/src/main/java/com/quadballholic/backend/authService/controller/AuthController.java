package com.quadballholic.backend.authService.controller;

import com.quadballholic.backend.authService.dto.ResetPasswordRequest;
import com.quadballholic.backend.authService.dto.SignInRequest;
import com.quadballholic.backend.authService.dto.SignInResponse;
import com.quadballholic.backend.authService.dto.SignUpRequest;
import com.quadballholic.backend.authService.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        Long id = authService.signUp(signUpRequest);
        return new ResponseEntity<>(id, HttpStatus.CREATED);

    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        SignInResponse response = authService.signIn(signInRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/activate-account")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token){
        authService.activateUser(token);
        return new ResponseEntity<>("Account validation completed successfully, you can sign in to your account", HttpStatus.OK);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@RequestParam("email") String email) {
        authService.requestPasswordReset(email);
        return new ResponseEntity<>("If an account exists with that email, a password reset link has been sent", HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authService.completePasswordReset(request);
        return new ResponseEntity<>("Password has been successfully reset", HttpStatus.OK);
    }

}
